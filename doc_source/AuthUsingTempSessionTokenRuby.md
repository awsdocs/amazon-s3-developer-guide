# Making Requests Using IAM User Temporary Credentials \- AWS SDK for Ruby<a name="AuthUsingTempSessionTokenRuby"></a>

An IAM user or an AWS account can request temporary security credentials \(see [Making Requests](MakingRequests.md)\) using AWS SDK for Ruby and use them to access Amazon S3\. These credentials expire after the session duration\. By default, the session duration is one hour\. If you use IAM user credentials, you can specify the duration, between 1 and 36 hours, when requesting the temporary security credentials\. 


**Making Requests Using IAM User Temporary Security Credentials**  

|  |  | 
| --- |--- |
|  1  |  Create a new AWS Security Token Service \(AWS STS\) client and get temporary credentials with `Aws::STS::Client.new`\.  | 
|  2  |  Create a new IAM user policy for the new user, granting temporary permissions to list the contents in a bucket\.  | 
|  3  |  Create an Amazon S3 client with the temporary credentials, and use the temporary credentials to list the contents of a specified bucket\. If you send requests using expired credentials, Amazon S3 returns an error\.  | 

The following Ruby code example demonstrates the preceding tasks\.

**Example**  

```
require 'aws-sdk-s3'

# Create new STS client and get temporary credentials
sts = Aws::STS::Client.new(region: region)

temp_creds = sts.get_federation_token({
   duration_seconds: 3600,
   name: user_name,
   policy: "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Sid\":\"Stmt1\",\"Effect\":\"Allow\",\"Action\":\"s3:ListBucket\",\"Resource\":\"arn:aws:s3:::#{bucket_name}\"}]}",
})

credentials = temp_creds.credentials

=begin
access_key_id     = credentials.access_key_id     # String
expiration        = credentials.expiration        # Time
secret_access_key = credentials.secret_access_key # String
session_token     = credentials.session_token     # String
=end

# Create S3 client with temporary credentials
s3 = Aws::S3::Client.new(region: region, credentials: credentials)

# Get an Amazon S3 resource
s3 = Aws::S3::Resource.new(region: region)

# Create an array of the object keynames in the bucket, up to the first 100
bucket = s3.bucket('example_bucket').objects.collect(&:key)

# Print the array to the terminal
puts bucket
```

**Note**  
If you obtain temporary security credentials using your AWS account security credentials, the temporary security credentials are valid for only one hour\. You can specify session duration only if you use IAM user credentials to request a session\.

The following Ruby code example creates a federated policy for a temporary user to list the items in a specified bucket for one hour\. To use this code example, your AWS credentials must have the necessary permission to create new AWS STS clients, and list Amazon S3 buckets\.

```
require 'aws-sdk-s3'

USAGE = <<DOC

Usage: sts_create_bucket_policy.rb -b BUCKET -u USER [-r REGION] [-d] [-h]

  Creates a federated policy for USER to list items in BUCKET for one hour

  BUCKET is required and must not already exist

  USER is required and if not found, is created

  If REGION is not supplied, defaults to us-west-2.

  -d gives you extra (debugging) information.

  -h displays this message and quits

DOC

$debug = false

def print_debug(s)
  if $debug
    puts s
  end
end

def get_user(region, user_name, create)
  user = nil

  iam = Aws::IAM::Resource.new(region: region)

  if create
    print_debug("Trying to create new user #{user_name} in region #{region}")
  else
    print_debug("Getting user #{user_name} in region #{region}")
  end

  # First see if user exists
  user = iam.user(user_name)

  if user == nil && create
    user = iam.create_user(user_name: user_name)
    iam.wait_until(:user_exists, user_name: user_name)

    print_debug("Created new user #{user_name}")
  else
    print_debug("Found user #{user_name}")
  end

  user
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

# IAM user we allow to list S3 bucket items for an hour
user = get_user(region, user_name, true)

# Create new STS client and get temporary credentials
sts = Aws::STS::Client.new(region: region)

temp_creds = sts.get_federation_token({
   duration_seconds: 3600,
   name: user_name,
   policy: "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Sid\":\"Stmt1\",\"Effect\":\"Allow\",\"Action\":\"s3:ListBucket\",\"Resource\":\"arn:aws:s3:::#{bucket_name}\"}]}",
})

credentials = temp_creds.credentials

=begin
access_key_id     = credentials.access_key_id     # String
expiration        = credentials.expiration        # Time
secret_access_key = credentials.secret_access_key # String
session_token     = credentials.session_token     # String
=end

# Create S3 client with temporary credentials
s3 = Aws::S3::Client.new(region: region, credentials: credentials)

# List the items for the specified S3 bucket
s3 = Aws::S3::Resource.new(region: region)
begin
  bucket = s3.bucket(bucket_name)

  count = bucket.objects.count

  puts "Items (#{count}):"
  puts

  # List the object key
  bucket.objects.each do |obj|
    puts "  Name: #{obj.key}"
  end
rescue Aws::S3::Errors::PermanentRedirect
  puts
  puts 'The bucket is not in the ' + region + ' region'
  exit 1
end
```