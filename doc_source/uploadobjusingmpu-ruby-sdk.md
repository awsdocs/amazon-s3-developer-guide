# Using the AWS SDK for Ruby for Multipart Upload<a name="uploadobjusingmpu-ruby-sdk"></a>

The AWS SDK for Ruby version 3 supports Amazon S3 multipart uploads in two ways\. For the first option, you can use a managed file upload helper\. This is the recommended method for uploading files to a bucket and it provides the following benefits:
+ Manages multipart uploads for objects larger than 15MB\.
+ Correctly opens files in binary mode to avoid encoding issues\.
+ Uses multiple threads for uploading parts of large objects in parallel\.

For more information, see [Uploading Files to Amazon S3](https://aws.amazon.com/blogs/developer/uploading-files-to-amazon-s3/) in the AWS Developer Blog\.

Alternatively, you can use the following multipart upload client operations directly:
+ [create\_multipart\_upload](http://docs.aws.amazon.com/sdk-for-ruby/v3/api/Aws/S3/Client.html#create_multipart_upload-instance_method) – Initiates a multipart upload and returns an upload ID\.
+ [upload\_part](http://docs.aws.amazon.com/sdk-for-ruby/v3/api/Aws/S3/Client.html#upload_part-instance_method) – Uploads a part in a multipart upload\.
+ [upload\_part\_copy](http://docs.aws.amazon.com/sdk-for-ruby/v3/api/Aws/S3/Client.html#upload_part_copy-instance_method) – Uploads a part by copying data from an existing object as data source\.
+ [complete\_multipart\_upload](http://docs.aws.amazon.com/sdk-for-ruby/v3/api/Aws/S3/Client.html#complete_multipart_upload-instance_method) – Completes a multipart upload by assembling previously uploaded parts\.
+ [abort\_multipart\_upload](http://docs.aws.amazon.com/sdk-for-ruby/v3/api/Aws/S3/Client.html#abort_multipart_upload-instance_method) – Aborts a multipart upload\.

For more information, see [Using the AWS SDK for Ruby \- Version 3](UsingTheMPRubyAPI.md)\.