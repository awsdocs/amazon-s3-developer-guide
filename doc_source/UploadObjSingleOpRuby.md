# Upload an Object Using the AWS SDK for Ruby<a name="UploadObjSingleOpRuby"></a>

The AWS SDK for Ruby \- Version 3 has two ways of uploading an object to Amazon S3\. The first is a managed file uploader, which makes it easy to upload files of any size from disk\.


**Uploading a File**  

|  |  | 
| --- |--- |
| 1 | Create an instance of the `Aws::S3::Resource` class\. | 
| 2 |  Reference the target object by bucket name and key\. Objects live in a bucket and have unique keys that identify the object\.  | 
| 3 |  Call`#upload_file` on the object\.  | 

**Example**  

```
1. require 'aws-sdk-s3'
2. 
3. s3 = Aws::S3::Resource.new(region:'us-west-2')
4. obj = s3.bucket('bucket-name').object('key')
5. obj.upload_file('/path/to/source/file')
```

The second way that AWS SDK for Ruby \- Version 3 can upload an object is to use the `#put` method of `Aws::S3::Object`\. This is useful if the object is a string or an IO object that is not a file on disk\.


**Put Object**  

|  |  | 
| --- |--- |
| 1 | Create an instance of the `Aws::S3::Resource` class\. | 
| 2 |  Reference the target object by bucket name and key\.  | 
| 3 |  Call`#put` passing in the string or IO object\.  | 

**Example**  

```
 1. require 'aws-sdk-s3'
 2. 
 3. s3 = Aws::S3::Resource.new(region:'us-west-2')
 4. obj = s3.bucket('bucket-name').object('key')
 5. 
 6. # string data
 7. obj.put(body: 'Hello World!')
 8. 
 9. # IO object
10. File.open('source', 'rb') do |file|
11.   obj.put(body: file)
12. end
```