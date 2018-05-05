# Making Requests Using IAM User Temporary Credentials \- AWS SDK for Ruby<a name="AuthUsingTempSessionTokenRuby"></a>

An IAM user or an AWS account can request temporary security credentials using AWS SDK for Ruby and use them to access Amazon S3\. These credentials expire after the session duration\. By default, the session duration is one hour\. If you use IAM user credentials, you can specify the duration \(from 1 to 36 hours\) when requesting the temporary security credentials\. For information about requesting temporary security credentials, see [Making Requests](MakingRequests.md)\.

**Note**  
If you obtain temporary security credentials using your AWS account security credentials, the temporary security credentials are valid for only one hour\. You can specify session duration only if you use IAM user credentials to request a session\.

The following Ruby example creates a temporary user to list the items in a specified bucket for one hour\. To use this example, you must have AWS credentials that have the necessary permissions to create new AWS Security Token Service \(AWS STS\) clients, and list Amazon S3 buckets\.

```
require 'aws-sdk-core'
require 'aws-sdk-s3'
require 'aws-sdk-iam'


USAGE = <<DOC

Usage: assumerole_create_bucket_policy.rb -b BUCKET -u USER [-r REGION] [-d] [-h]

  Assumes a role for USER to list items in BUCKET for one hour.

  BUCKET is required and must already exist.

  USER is required and if not found, is created.

  If REGION is not supplied, defaults to us-west-2.

  -d gives you extra (debugging) information.

  -h displays this message and quits.

DOC

$debug = false

def print_debug(s)
  if $debug
    puts s
  end
end

def get_user(region, user_name, create)
  user = nil
  iam = Aws::IAM::Client.new(region: 'us-west-2')
  
begin
  user = iam.create_user(user_name: user_name)
  iam.wait_until(:user_exists, user_name: user_name)
  print_debug("Created new user #{user_name}")
rescue Aws::IAM::Errors::EntityAlreadyExists
  print_debug("Found user #{user_name} in region #{region}")
end
end

# main
region = 'us-west-2'
user_name = ''
bucket_name = ''

i = 0

while i < ARGV.length
  case ARGV[i]

    when '-b'
      i += 1
      bucket_name = ARGV[i]

    when '-u'
      i += 1
      user_name = ARGV[i]

    when '-r'
      i += 1

      region = ARGV[i]

    when '-d'
      puts 'Debugging enabled'
      $debug = true

    when '-h'
      puts USAGE
      exit 0

    else
      puts 'Unrecognized option: ' + ARGV[i]
      puts USAGE
      exit 1

  end

  i += 1
end

if bucket_name == ''
  puts 'You must supply a bucket name'
  puts USAGE
  exit 1
end

if user_name == ''
  puts 'You must supply a user name'
  puts USAGE
  exit 1
end

#Identify the IAM user that is allowed to list Amazon S3 bucket items for an hour.
user = get_user(region, user_name, true)

# Create a new Amazon STS client and get temporary credentials. This uses a role that was already created.
creds = Aws::AssumeRoleCredentials.new(
  client: Aws::STS::Client.new(region: region),
  role_arn: "arn:aws:iam::111122223333:role/assumedrolelist",
  role_session_name: "assumerole-s3-list"
)

# Create an Amazon S3 resource with temporary credentials.
s3 = Aws::S3::Resource.new(region: region, credentials: creds)

puts "Contents of '%s':" % bucket_name
puts '  Name => GUID'

 s3.bucket(bucket_name).objects.limit(50).each do |obj|
      puts "  #{obj.key} => #{obj.etag}"
end
```