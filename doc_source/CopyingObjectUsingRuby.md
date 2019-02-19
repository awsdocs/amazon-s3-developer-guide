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
#**
 #* Copyright 2010-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 #*
 #* This file is licensed under the Apache License, Version 2.0 (the "License").
 #* You may not use this file except in compliance with the License. A copy of
 #* the License is located at
 #*
 #* http://aws.amazon.com/apache2.0/
 #*
 #* This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 #* CONDITIONS OF ANY KIND, either express or implied. See the License for the
 #* specific language governing permissions and limitations under the License.
#**


# snippet-sourcedescription:[copy_object_between_buckets demonstrates the preceding tasks using the "copy_object" method to copy an object from one bucket to another.] 
# snippet-service:[s3]
# snippet-keyword:[Ruby]
# snippet-keyword:[Amazon S3]
# snippet-keyword:[Code Sample]
# snippet-keyword:[COPY object]
# snippet-sourcetype:[full-example]
# snippet-sourcedate:[2019-01-28]
# snippet-sourceauthor:[AWS]

# snippet-start:[s3.ruby.copy_object_between_buckets.rb]
require 'aws-sdk-s3'

source_bucket_name = '*** Provide bucket name ***'
target_bucket_name = '*** Provide bucket name ***'
source_key = '*** Provide source key ***'
target_key = '*** Provide target key ***'

s3 = Aws::S3::Client.new(region: 'us-west-2')
s3.copy_object({bucket: target_bucket_name, copy_source: source_bucket_name + '/' + source_key, key: target_key})

puts "Copying file #{source_key} to #{target_key}."
# snippet-end:[s3.ruby.copy_object_between_buckets.rb]
```