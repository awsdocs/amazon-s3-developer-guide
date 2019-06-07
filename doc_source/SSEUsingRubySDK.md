# Specifying Server\-Side Encryption Using the AWS SDK for Ruby<a name="SSEUsingRubySDK"></a>

When using the AWS SDK for Ruby to upload an object, you can specify that the object be stored encrypted at rest with server\-side encryption \(SSE\)\. When you read the object back, it is automatically decrypted\.

The following AWS SDK for Ruby – Version 3 example demonstrates how to specify that a file uploaded to Amazon S3 be encrypted at rest\.

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

# snippet-sourcedescription:[s3_encrypt_file_upload.rb demonstrates how to specify that a file uploaded to Amazon S3 be encrypted at rest.] 
# snippet-service:[s3]
# snippet-keyword:[Ruby]
# snippet-keyword:[Amazon S3]
# snippet-keyword:[Code Sample]
# snippet-keyword:[ENCRYPT UPLOAD File]
# snippet-sourcetype:[full-example]
# snippet-sourcedate:[2019-01-28]
# snippet-sourceauthor:[AWS]

# snippet-start:[s3.ruby.s3_encrypt_file_upload.rb]
# The following example demonstrates how to specify that a file uploaded to Amazon S3 be encrypted at rest.
require 'aws-sdk-s3' 

regionName = 'us-west-2' 
bucketName = 'my-bucket' 
key = 'key' 
filePath = 'local/path/to/file'
encryptionType = 'AES256'

s3 = Aws::S3::Resource.new(region:regionName) 
obj = s3.bucket(bucketName).object(key) 
obj.upload_file(filePath, :server_side_encryption => encryptionType)
# snippet-end:[s3.ruby.s3_encrypt_file_upload.rb]
```

For an example that shows how to upload an object without SSE, see [Upload an Object Using the AWS SDK for Ruby](UploadObjSingleOpRuby.md)\.

## Determining the Encryption Algorithm Used<a name="DeterminingEncryptionAlgorithmUsed03"></a>

The following code example demonstrates how to determine the encryption state of an existing object\.

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


# snippet-sourcedescription:[determine_object_encryption_state.rb shows how to determine the encryption state of an existing object.] 
# snippet-service:[s3]
# snippet-keyword:[Ruby]
# snippet-keyword:[Amazon S3]
# snippet-keyword:[Code Sample]
# snippet-keyword:[GET server_side_encryption Object]
# snippet-sourcetype:[full-example]
# snippet-sourcedate:[2019-01-28]
# snippet-sourceauthor:[AWS]

# snippet-start:[s3.ruby.determine_object_encryption_state.rb]
# Determine server-side encryption of an object.
require 'aws-sdk-s3'

regionName = 'us-west-2' 
bucketName='bucket-name'
key = 'key' '

s3 = Aws::S3::Resource.new(region:regionName)
enc = s3.bucket(bucketName).object(key).server_side_encryption
enc_state = (enc != nil) ? enc : "not set"
puts "Encryption state is #{enc_state}."
# snippet-end:[s3.ruby.determine_object_encryption_state.rb]
```

If server\-side encryption is not used for the object that is stored in Amazon S3, the method returns null\.

## Changing Server\-Side Encryption of an Existing Object \(Copy Operation\)<a name="ChangingServer-SideEncryptionofanExistingObjectCopyOperation03"></a>

To change the encryption state of an existing object, make a copy of the object and delete the source object\. By default, the copy methods do not encrypt the target unless you explicitly request server\-side encryption\. You can request the encryption of the target object by specifying the `server_side_encryption` value in the options hash argument as shown in the following Ruby code example\. The code example demonstrates how to copy an object and encrypt the copy\. 

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

# snippet-sourcedescription:[copy_object_encrypt_copy.rb demonstrates how to copy an object and encrypt the copy.] 
# snippet-service:[s3]
# snippet-keyword:[Ruby]
# snippet-keyword:[Amazon S3]
# snippet-keyword:[Code Sample]
# snippet-keyword:[COPY Object]
# snippet-sourcetype:[full-example]
# snippet-sourcedate:[2019-01-28]
# snippet-sourceauthor:[AWS]

# snippet-start:[s3.ruby.copy_object_encrypt_copy.rb]
require 'aws-sdk-s3'

regionName = 'us-west-2' 
encryptionType = 'AES256'

s3 = Aws::S3::Resource.new(region:regionName)
bucket1 = s3.bucket('source-bucket-name')
bucket2 = s3.bucket('target-bucket-name')
obj1 = bucket1.object('Bucket1Key')
obj2 = bucket2.object('Bucket2Key')
 
obj1.copy_to(obj2, :server_side_encryption => encryptionType)
# snippet-end:[s3.ruby.copy_object_encrypt_copy.rb]
```

For a sample of how to copy an object without encryption, see [Copy an Object Using the AWS SDK for Ruby](CopyingObjectUsingRuby.md)\. 