# Upload an object using the AWS SDK for Ruby<a name="UploadObjSingleOpRuby"></a>

The AWS SDK for Ruby \- Version 3 has two ways of uploading an object to Amazon S3\. The first uses a managed file uploader, which makes it easy to upload files of any size from disk\. To use the managed file uploader method:

1. Create an instance of the `Aws::S3::Resource` class\.

1. Reference the target object by bucket name and key\. Objects live in a bucket and have unique keys that identify each object\.

1. Call`#upload_file` on the object\.

**Example**  

```
require 'aws-sdk-s3'

s3 = Aws::S3::Resource.new(region:'us-west-2')
obj = s3.bucket('bucket-name').object('key')
obj.upload_file('/path/to/source/file')
```

The second way that AWS SDK for Ruby \- Version 3 can upload an object uses the `#put` method of `Aws::S3::Object`\. This is useful if the object is a string or an I/O object that is not a file on disk\. To use this method:

1. Create an instance of the `Aws::S3::Resource` class\.

1. Reference the target object by bucket name and key\.

1. Call`#put`, passing in the string or I/O object\.

**Example**  

```
require 'aws-sdk-s3'

s3 = Aws::S3::Resource.new(region:'us-west-2')
obj = s3.bucket('bucket-name').object('key')

# I/O object
File.open('/path/to/source.file', 'rb') do |file|
  obj.put(body: file)
end
```