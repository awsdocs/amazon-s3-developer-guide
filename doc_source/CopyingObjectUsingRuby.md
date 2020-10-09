# Copy an Object Using the AWS SDK for Ruby<a name="CopyingObjectUsingRuby"></a>

The following tasks guide you through using the Ruby classes to copy an object in Amazon S3, from one bucket to another or to copy an object within the same bucket\. 


**Copying Objects**  

|  |  | 
| --- |--- |
|  1  |  Use the Amazon S3 modularized gem for version 3 of the AWS SDK for Ruby, require 'aws\-sdk\-s3', and provide your AWS credentials\. For more information about how to provide your credentials, see [Making requests using AWS account or IAM user credentials](AuthUsingAcctOrUserCredentials.md)\.  | 
|  2  |  Provide the request information, such as source bucket name, source key name, destination bucket name, and destination key\.   | 

 The following Ruby code example demonstrates the preceding tasks using the `#copy_object` method to copy an object from one bucket to another\.

**Example**  

```
require 'aws-sdk-s3'

# Copies an object from one Amazon S3 bucket to another.
#
# Prerequisites:
#
# - Two S3 buckets (a source bucket and a target bucket).
# - An object in the source bucket to be copied.
#
# @param s3_client [Aws::S3::Client] An initialized Amazon S3 client.
# @param source_bucket_name [String] The source bucket's name.
# @param source_key [String] The name of the object
#   in the source bucket to be copied.
# @param target_bucket_name [String] The target bucket's name.
# @param target_key [String] The name of the copied object.
# @return [Boolean] true if the object was copied; otherwise, false.
# @example
#   s3_client = Aws::S3::Client.new(region: 'us-east-1')
#   exit 1 unless object_copied?(
#     s3_client,
#     'doc-example-bucket1',
#     'my-source-file.txt',
#     'doc-example-bucket2',
#     'my-target-file.txt'
#   )
def object_copied?(
  s3_client,
  source_bucket_name,
  source_key,
  target_bucket_name,
  target_key)

  return true if s3_client.copy_object(
    bucket: target_bucket_name,
    copy_source: source_bucket_name + '/' + source_key,
    key: target_key
  )
rescue StandardError => e
  puts "Error while copying object: #{e.message}"
end
```