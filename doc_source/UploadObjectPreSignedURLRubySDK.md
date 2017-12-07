# Upload an Object Using a Pre\-Signed URL \(AWS SDK for Ruby\)<a name="UploadObjectPreSignedURLRubySDK"></a>

The following tasks guide you through using a Ruby script to upload an object using a pre\-signed URL for SDK for Ruby \- Version 3\.


**Uploading Objects \- SDK for Ruby \- Version 3**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `Aws::S3::Resource` class\.  | 
|  2  |  Provide a bucket name and an object key by calling the `#bucket[]` and the `#object[]` methods of your `Aws::S3::Resource` class instance\. Generate a pre\-signed URL by creating an instance of the `URI` class, and use it to parse the `.presigned_url` method of your `Aws::S3::Resource` class instance\. You must specify `:put` as an argument to `.presigned_url`, and you must specify `PUT` to `Net::HTTP::Session#send_request` if you want to upload an object\.  | 
|  3  |  Anyone with the pre\-signed URL can upload an object\.  The upload creates an object or replaces any existing object with the same key that is specified in the pre\-signed URL\.  | 

The following Ruby code example demonstrates the preceding tasks for SDK for Ruby \- Version 3\.

**Example**  

```
 1. #Uploading an object using a pre-signed URL for SDK for Ruby - Version 3.
 2. 
 3. require 'aws-sdk-s3'
 4. require 'net/http'
 5. 
 6. s3 = Aws::S3::Resource.new(region:'us-west-2')
 7. 
 8. obj = s3.bucket('BucketName').object('KeyName')
 9. # Replace BucketName with the name of your bucket.
10. # Replace KeyName with the name of the object you are creating or replacing.
11. 
12. url = URI.parse(obj.presigned_url(:put))
13. 
14. body = "Hello World!"
15. # This is the contents of your object. In this case, it's a simple string.
16. 
17. Net::HTTP.start(url.host) do |http|
18.   http.send_request("PUT", url.request_uri, body, {
19. # This is required, or Net::HTTP will add a default unsigned content-type.
20.     "content-type" => "",
21.   })
22. end
23. 
24. puts obj.get.body.read
25. # This will print out the contents of your object to the terminal window.
```