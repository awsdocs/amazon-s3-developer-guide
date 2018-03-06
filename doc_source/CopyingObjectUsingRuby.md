# Copy an Object Using the AWS SDK for Ruby<a name="CopyingObjectUsingRuby"></a>

The following tasks guide you through using the Ruby classes to copy an object in Amazon S3, from one bucket to another or to copy an object within the same bucket\. 


**Copying Objects**  

|  |  | 
| --- |--- |
| 1 | Use the Amazon S3 modularized gem for version 3 of the AWS SDK for Ruby, require 'aws\-sdk\-s3', and provide your AWS credentials\. For more information about how to provide your credentials, see [Making Requests Using AWS Account or IAM User Credentials](AuthUsingAcctOrUserCredentials.md)\. | 
| 2 |  Provide the request information, such as source bucket name, source key name, destination bucket name, and destination key\.   | 

 The following Ruby code example demonstrates the preceding tasks using the `#copy_object` method to copy an object from one bucket to another\.

**Example**  

```
 1. require 'aws-sdk-s3'
 2. 
 3. source_bucket_name = '*** Provide bucket name ***'
 4. target_bucket_name = '*** Provide bucket name ***'
 5. source_key = '*** Provide source key ***'
 6. target_key = '*** Provide target key ***'
 7. 
 8. s3 = Aws::S3::Client.new(region: 'us-west-2')
 9. s3.copy_object({bucket: target_bucket_name, copy_source: source_bucket_name + '/' + source_key, key: target_key})
10. 
11. puts "Copying file #{source_key} to #{target_key}."
```