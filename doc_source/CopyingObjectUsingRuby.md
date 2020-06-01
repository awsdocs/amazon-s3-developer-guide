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

source_bucket_name = '*** Provide bucket name ***'
target_bucket_name = '*** Provide bucket name ***'
source_key = '*** Provide source key ***'
target_key = '*** Provide target key ***'

begin
  s3 = Aws::S3::Client.new(region: 'us-west-2')
  s3.copy_object(bucket: target_bucket_name, copy_source: source_bucket_name + '/' + source_key, key: target_key)
rescue StandardError => ex
  puts 'Caught exception copying object ' + source_key + ' from bucket ' + source_bucket_name + ' to bucket ' + target_bucket_name + ' as ' + target_key + ':'
  puts ex.message
end

puts 'Copied ' +  source_key + ' from bucket ' + source_bucket_name + ' to bucket ' + target_bucket_name + ' as ' + target_key
```