# Making Requests Using AWS Account or IAM User Credentials \- AWS SDK for Ruby<a name="AuthUsingAcctOrUserCredRuby"></a>

Before you can use version 3 of the AWS SDK for Ruby to make calls to Amazon S3, you must set the AWS access credentials that the SDK uses to verify your access to your buckets and objects\. If you have shared credentials set up in the AWS credentials profile on your local system, version 3 of the SDK for Ruby can use those credentials without your having to declare them in your code\. For more information about setting up shared credentials, see [Making Requests Using AWS Account or IAM User Credentials](AuthUsingAcctOrUserCredentials.md)\.

The following Ruby code snippet uses the credentials in a shared AWS credentials file on a local computer to authenticate a request to get all of the object key names in a specific bucket\. It does the following:

1. Creates an instance of the `Aws::S3::Resource` class\. 

1. Makes a request to Amazon S3 by enumerating objects in a bucket using the `bucket` method of `Aws::S3::Resource`\. The client generates the necessary signature value from the credentials in the AWS credentials file on your computer, and includes it in the request it sends to Amazon S3\.

1. Prints the array of object key names to the terminal\.

**Example**  

```
 1. # Use the Amazon S3 modularized gem for version 3 of the AWS Ruby SDK.
 2. require 'aws-sdk-s3'
 3. 
 4. # Get an Amazon S3 resource.
 5. s3 = Aws::S3::Resource.new(region: 'us-west-2')
 6. 
 7. # Create an array of up to the first 100 object keynames in the bucket.
 8. bucket = s3.bucket('example_bucket').objects.collect(&:key)
 9. 
10. # Print the array to the terminal.
11. puts bucket
```

If you don't have a local AWS credentials file, you can still create the `Aws::S3::Resource` resource and execute code against Amazon S3 buckets and objects\. Requests that are sent using version 3 of the SDK for Ruby are anonymous, with no signature by default\. Amazon S3 returns an error if you send anonymous requests for a resource that's not publicly available\.

You can use and expand the previous code snippet for SDK for Ruby applications, as in the following more robust example\. The credentials that are used for this example come from a local AWS credentials file on the computer that is running this application\. The credentials are for an IAM user who can list objects in the bucket that the user specifies when they run the application\.

```
# auth_request_test.rb
# Use the Amazon S3 modularized gem for version 3 of the AWS Ruby SDK.
require 'aws-sdk-s3'

# Usage: ruby auth_request_test.rb list BUCKET

# Set the name of the bucket on which the operations are performed.
# This argument is required
bucket_name = nil

# The operation to perform on the bucket.
operation = 'list' # default
operation = ARGV[0] if (ARGV.length > 0)

if ARGV.length > 1
  bucket_name = ARGV[1]
else
  exit 1
end

# Get an Amazon S3 resource.
s3 = Aws::S3::Resource.new(region: 'us-west-2')

# Get the bucket by name.
bucket = s3.bucket(bucket_name)

case operation

when 'list'
  if bucket.exists?
    # Enumerate the bucket contents and object etags.
    puts "Contents of '%s':" % bucket_name
    puts '  Name => GUID'

    bucket.objects.limit(50).each do |obj|
      puts "  #{obj.key} => #{obj.etag}"
    end
  else
    puts "The bucket '%s' does not exist!" % bucket_name
  end

else
  puts "Unknown operation: '%s'! Only list is supported." % operation
end
```