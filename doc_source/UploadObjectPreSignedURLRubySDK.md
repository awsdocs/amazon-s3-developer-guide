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
require 'aws-sdk-s3'
require 'net/http'

# Uploads an object to a bucket in Amazon Simple Storage Service (Amazon S3)
#   by using a presigned URL.
#
# Prerequisites:
#
# - An S3 bucket.
# - An object in the bucket to upload content to.
#
# @param s3_client [Aws::S3::Resource] An initialized S3 resource.
# @param bucket_name [String] The name of the bucket.
# @param object_key [String] The name of the object.
# @param object_content [String] The content to upload to the object.
# @param http_client [Net::HTTP] An initialized HTTP client.
#   This is especially useful for testing with mock HTTP clients.
#   If not specified, a default HTTP client is created.
# @return [Boolean] true if the object was uploaded; otherwise, false.
# @example
#   exit 1 unless object_uploaded_to_presigned_url?(
#     Aws::S3::Resource.new(region: 'us-east-1'),
#     'doc-example-bucket',
#     'my-file.txt',
#     'This is the content of my-file.txt'
#   )
def object_uploaded_to_presigned_url?(
  s3_resource,
  bucket_name,
  object_key,
  object_content,
  http_client = nil
)
  object = s3_resource.bucket(bucket_name).object(object_key)
  url = URI.parse(object.presigned_url(:put))

  if http_client.nil?
    Net::HTTP.start(url.host) do |http|
      http.send_request(
        'PUT',
        url.request_uri,
        object_content,
        'content-type' => ''
      )
    end
  else
    http_client.start(url.host) do |http|
      http.send_request(
        'PUT',
        url.request_uri,
        object_content,
        'content-type' => ''
      )
    end
  end
  content = object.get.body
  puts "The presigned URL for the object '#{object_key}' in the bucket " \
    "'#{bucket_name}' is:\n\n"
  puts url
  puts "\nUsing this presigned URL to get the content that " \
    "was just uploaded to this object, the object\'s content is:\n\n"
  puts content.read
  return true
rescue StandardError => e
  puts "Error uploading to presigned URL: #{e.message}"
  return false
end
```