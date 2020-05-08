# Upload an object using a presigned URL \(AWS SDK for Ruby\)<a name="UploadObjectPreSignedURLRubySDK"></a>

The following tasks guide you through using a Ruby script to upload an object using a presigned URL for SDK for Ruby \- Version 3\.


**Uploading objects \- SDK for Ruby \- version 3**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `Aws::S3::Resource` class\.  | 
|  2  |  Provide a bucket name and an object key by calling the `#bucket[]` and the `#object[]` methods of your `Aws::S3::Resource` class instance\. Generate a presigned URL by creating an instance of the `URI` class, and use it to parse the `.presigned_url` method of your `Aws::S3::Resource` class instance\. You must specify `:put` as an argument to `.presigned_url`, and you must specify `PUT` to `Net::HTTP::Session#send_request` if you want to upload an object\.  | 
|  3  |  Anyone with the presigned URL can upload an object\.  The upload creates an object or replaces any existing object with the same key that is specified in the presigned URL\.  | 

The following Ruby code example demonstrates the preceding tasks for SDK for Ruby \- Version 3\.

**Example**  

```
# Upload an object using a presigned URL for SDK for Ruby - Version 3.

require 'aws-sdk-s3'
require 'net/http'

s3 = Aws::S3::Resource.new(region: 'us-west-2')

# Replace BucketName with the name of your bucket.
# Replace KeyName with the name of the object you are creating or replacing.
obj = s3.bucket('BucketName').object('KeyName')

url = URI.parse(obj.presigned_url(:put))

# The contents of your object, as a string
body = 'Hello World!'

Net::HTTP.start(url.host) do |http|
  http.send_request('PUT', url.request_uri, body,
                     # Or else Net::HTTP adds a default, unsigned content-type
                    'content-type' => '')
end

# Print the contents of your object to the terminal window
puts obj.get.body.read
```