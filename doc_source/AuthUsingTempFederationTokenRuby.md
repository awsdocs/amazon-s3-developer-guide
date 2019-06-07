# Making Requests Using Federated User Temporary Credentials \- AWS SDK for Ruby<a name="AuthUsingTempFederationTokenRuby"></a>

You can provide temporary security credentials for your federated users and applications so that they can send authenticated requests to access your AWS resources\. When requesting temporary credentials from the IAM service, you must provide a user name and an IAM policy that describes the resource permissions that you want to grant\. By default, the session duration is one hour\. However, if you are requesting temporary credentials using IAM user credentials, you can explicitly set a different duration value when requesting the temporary security credentials for federated users and applications\. For information about temporary security credentials for your federated users and applications, see [Making Requests](MakingRequests.md)\.

**Note**  
For added security when you request temporary security credentials for federated users and applications, you might want to use a dedicated IAM user with only the necessary access permissions\. The temporary user you create can never get more permissions than the IAM user who requested the temporary security credentials\. For more information, see [ AWS Identity and Access Management FAQs ](https://aws.amazon.com/iam/faqs/#What_are_the_best_practices_for_using_temporary_security_credentials)\.

**Example**  
The following Ruby code example allows a federated user with a limited set of permissions to lists keys in the specified bucket\.   

```
require 'aws-sdk-s3'
require 'aws-sdk-iam'

USAGE = <<DOC

Usage: ruby auth_federation_token_request_test.rb -b BUCKET -u USER [-r REGION] [-d] [-h]

  Creates a federated policy for USER to list items in BUCKET for one hour.

  BUCKET is required and must already exist.

  USER is required and if not found, is created.

  If REGION is not supplied, defaults to us-west-2.

  -d gives you extra (debugging) information.

  -h displays this message and quits.

DOC

def print_debug(debug, s)
  if debug
    puts s
  end
end

# Get the user if they exist, otherwise create them
def get_user(region, user_name, debug)
  iam = Aws::IAM::Client.new(region: 'us-west-2')
  
  # See if user exists
  user = iam.user(user_name)
 
  # If user does not exist, create them
  if user == nil
    user = iam.create_user(user_name: user_name)
    iam.wait_until(:user_exists, user_name: user_name)
    print_debug(debug, "Created new user #{user_name}")
  else
    print_debug(debug, "Found user #{user_name} in region #{region}")
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

# Create a new STS client and get temporary credentials.
sts = Aws::STS::Client.new(region: region)

creds = sts.get_federation_token({
  duration_seconds: 3600,
  name: user_name,
  policy: "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Sid\":\"Stmt1\",\"Effect\":\"Allow\",\"Action\":\"s3:ListBucket\",\"Resource\":\"arn:aws:s3:::#{bucket_name}\"}]}",
})

# Create an Amazon S3 resource with temporary credentials.
s3 = Aws::S3::Resource.new(region: region, credentials: creds)

puts "Contents of '%s':" % bucket_name
puts '  Name => GUID'

begin
  s3.bucket(bucket_name).objects.limit(50).each do |obj|
    puts "  #{obj.key} => #{obj.etag}"
  end
rescue StandardError => ex
  puts 'Caught exception accessing bucket ' + bucket_name + ':'
  puts ex.message
end
```