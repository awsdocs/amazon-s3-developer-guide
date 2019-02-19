# Upload an Object Using a Presigned URL \(AWS SDK for Ruby\)<a name="UploadObjectPreSignedURLRubySDK"></a>

The following tasks guide you through using a Ruby script to upload an object using a presigned URL for SDK for Ruby \- Version 3\.


**Uploading Objects \- SDK for Ruby \- Version 3**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `Aws::S3::Resource` class\.  | 
|  2  |  Provide a bucket name and an object key by calling the `#bucket[]` and the `#object[]` methods of your `Aws::S3::Resource` class instance\. Generate a presigned URL by creating an instance of the `URI` class, and use it to parse the `.presigned_url` method of your `Aws::S3::Resource` class instance\. You must specify `:put` as an argument to `.presigned_url`, and you must specify `PUT` to `Net::HTTP::Session#send_request` if you want to upload an object\.  | 
|  3  |  Anyone with the presigned URL can upload an object\.  The upload creates an object or replaces any existing object with the same key that is specified in the presigned URL\.  | 

The following Ruby code example demonstrates the preceding tasks for SDK for Ruby \- Version 3\.

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


# snippet-sourcedescription:[create_bucket.rb allows anyone with the presigned URL can upload an object by using the AWS SDK for Ruby.] 
# snippet-service:[s3]
# snippet-keyword:[Ruby]
# snippet-keyword:[Amazon S3]
# snippet-keyword:[PUT Bucket, Presigned URL]
# snippet-keyword:[Upload-presigned-url]
# snippet-sourcetype:[snippet]
# snippet-sourcedate:[2019-01-28]
# snippet-sourceauthor:[AWS]

# snippet-start:[s3.ruby.upload_object_presigned_url.rb]
#Uploading an object using a presigned URL for SDK for Ruby - Version 3.

require 'aws-sdk-s3'
require 'net/http'

s3 = Aws::S3::Resource.new(region:'us-west-2')

obj = s3.bucket('BucketName').object('KeyName')
# Replace BucketName with the name of your bucket.
# Replace KeyName with the name of the object you are creating or replacing.

url = URI.parse(obj.presigned_url(:put))

body = "Hello World!"
# This is the contents of your object. In this case, it's a simple string.

Net::HTTP.start(url.host) do |http|
  http.send_request("PUT", url.request_uri, body, {
# This is required, or Net::HTTP will add a default unsigned content-type.
    "content-type" => "",
  })
end

puts obj.get.body.read
# This will print out the contents of your object to the terminal window.
# snippet-end:[s3.ruby.upload_object_presigned_url.rb]
```