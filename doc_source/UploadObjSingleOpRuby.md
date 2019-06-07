# Upload an Object Using the AWS SDK for Ruby<a name="UploadObjSingleOpRuby"></a>

The AWS SDK for Ruby \- Version 3 has two ways of uploading an object to Amazon S3\. The first uses a managed file uploader, which makes it easy to upload files of any size from disk\. To use the managed file uploader method:

1. Create an instance of the `Aws::S3::Resource` class\.

1. Reference the target object by bucket name and key\. Objects live in a bucket and have unique keys that identify each object\.

1. Call`#upload_file` on the object\.

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


# snippet-sourcedescription:[upload_files_using_managed_file_uploader uses a managed file uploader, which makes it easy to upload files of any size from disk.] 
# snippet-service:[s3]
# snippet-keyword:[Ruby]
# snippet-keyword:[Amazon S3]
# snippet-keyword:[Code Sample]
# snippet-keyword:[UPLOAD file]
# snippet-sourcetype:[full-example]
# snippet-sourcedate:[2019-01-28]
# snippet-sourceauthor:[AWS]

# snippet-start:[s3.ruby.upload_files_using_managed_file_uploader.rb]
require 'aws-sdk-s3'

s3 = Aws::S3::Resource.new(region:'us-west-2')
obj = s3.bucket('bucket-name').object('key')
obj.upload_file('/path/to/source/file')
# snippet-end:[s3.ruby.upload_files_using_managed_file_uploader.rb]
```

The second way that AWS SDK for Ruby \- Version 3 can upload an object uses the `#put` method of `Aws::S3::Object`\. This is useful if the object is a string or an I/O object that is not a file on disk\. To use this method:

1. Create an instance of the `Aws::S3::Resource` class\.

1. Reference the target object by bucket name and key\.

1. Call`#put`, passing in the string or I/O object\.

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


# snippet-sourcedescription:[upload_files_using_put_object_method.rb shows how to upload an object using the #put method of Amazon S3 object. This is useful if the object is a string or an I/O object that is not a file on disk.] 
# snippet-service:[s3]
# snippet-keyword:[Ruby]
# snippet-keyword:[Amazon S3]
# snippet-keyword:[Code Sample]
# snippet-keyword:[PUT Object]
# snippet-sourcetype:[full-example]
# snippet-sourcedate:[2019-01-28]
# snippet-sourceauthor:[AWS]

# snippet-start:[s3.ruby.upload_files_using_put_object_method.rb]
  require 'aws-sdk-s3'

  s3 = Aws::S3::Resource.new(region:'us-west-2')
  obj = s3.bucket('bucket-name').object('key')

# string data
  obj.put(body: 'Hello World!')

# I/O object
  File.open('/path/to/source.file', 'rb') do |file|
  obj.put(body: file)
  end
# snippet-end:[s3.ruby.upload_files_using_put_object_method.rb]
```