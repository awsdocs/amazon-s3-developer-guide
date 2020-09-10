# Making requests using AWS account or IAM user credentials \- AWS SDK for Ruby<a name="AuthUsingAcctOrUserCredRuby"></a>

Before you can use version 3 of the AWS SDK for Ruby to make calls to Amazon S3, you must set the AWS access credentials that the SDK uses to verify your access to your buckets and objects\. If you have shared credentials set up in the AWS credentials profile on your local system, version 3 of the SDK for Ruby can use those credentials without your having to declare them in your code\. For more information about setting up shared credentials, see [Making requests using AWS account or IAM user credentials](AuthUsingAcctOrUserCredentials.md)\.

The following Ruby code snippet uses the credentials in a shared AWS credentials file on a local computer to authenticate a request to get all of the object key names in a specific bucket\. It does the following:

1. Creates an instance of the `Aws::S3::Resource` class\. 

1. Makes a request to Amazon S3 by enumerating objects in a bucket using the `bucket` method of `Aws::S3::Resource`\. The client generates the necessary signature value from the credentials in the AWS credentials file on your computer, and includes it in the request it sends to Amazon S3\.

1. Prints the array of object key names to the terminal\.

**Example**  

```
require 'aws-sdk-s3'

# Prints the list of objects in an Amazon S3 bucket.
#
# @param s3_client [Aws::S3::Client] An initialized Amazon S3 client.
# @param bucket_name [String] The bucket's name.
# @return [Boolean] true if all operations succeed; otherwise, false.
# @example
#   s3_client = Aws::S3::Client.new(region: 'us-east-1')
#   exit 1 unless list_bucket_objects?(s3_client, 'my-bucket')
def list_bucket_objects?(s3_client, bucket_name)
  puts "Accessing the bucket named '#{bucket_name}'..."
  objects = s3_client.list_objects_v2(
    bucket: bucket_name,
    max_keys: 50
  )

  if objects.count.positive?
    puts 'The object keys in this bucket are (first 50 objects):'
    objects.contents.each do |object|
      puts object.key
    end
  else
    puts 'No objects found in this bucket.'
  end

  return true
rescue StandardError => e
  puts "Error while accessing the bucket named '#{bucket_name}': #{e.message}"
  return false
end
```

If you don't have a local AWS credentials file, you can still create the `Aws::S3::Resource` resource and run code against Amazon S3 buckets and objects\. Requests that are sent using version 3 of the SDK for Ruby are anonymous, with no signature by default\. Amazon S3 returns an error if you send anonymous requests for a resource that's not publicly available\.

You can use and expand the previous code snippet for SDK for Ruby applications, as in the following more robust example\. The credentials that are used for this example come from a local AWS credentials file on the computer that is running this application\. The credentials are for an IAM user who can list objects in the bucket that the user specifies when they run the application\.

```
require 'aws-sdk-s3'

# Prints command line usage information.
def print_usage()
  puts 'Incorrect command line arguments provided or -h or --help specified.'
  puts 'Usage: auth_request_test.rb <bucket>'
  puts '  <bucket> The name of the bucket containing the objects to list.'
  puts 'Example: auth_request_test.rb my-bucket'
end

# Checks whether the correct command line arguments were provided.
# On failure, prints command line usage information and then exits.
def check_inputs()
  if ARGV.length != 1 || ARGV[0] == '-h' || ARGV[0] == '--help'
    print_usage
    exit 1
  end
end

# Prints the list of objects in the specified Amazon S3 bucket.
#
# @param s3_client [Aws::S3::Client] An initialized Amazon S3 client.
# @param bucket_name [String] The bucket's name.
# @return [Boolean] true if all operations succeed; otherwise, false.
# @example
#   s3_client = Aws::S3::Client.new(region: 'us-east-1')
#   exit 1 unless can_list_bucket_objects?(s3_client, 'my-bucket')
def list_bucket_objects?(s3_client, bucket_name)
  puts "Accessing the bucket named '#{bucket_name}'..."
  objects = s3_client.list_objects_v2(
    bucket: bucket_name,
    max_keys: 50
  )

  if objects.count.positive?
    puts 'The object keys in this bucket are (first 50 objects):'
    objects.contents.each do |object|
      puts object.key
    end
  else
    puts 'No objects found in this bucket.'
  end

  return true
rescue StandardError => e
  puts "Error while accessing the bucket named '#{bucket_name}': #{e.message}"
end
```