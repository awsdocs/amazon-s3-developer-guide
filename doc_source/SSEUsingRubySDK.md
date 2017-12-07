# Specifying Server\-Side Encryption Using the AWS SDK for Ruby<a name="SSEUsingRubySDK"></a>

When using the AWS SDK for Ruby to upload an object, you can specify that the object be stored encrypted at rest with server\-side encryption \(SSE\)\. When you read the object back, it is automatically decrypted\.

The following AWS SDK for Ruby – Version 3 example demonstrates how to specify that a file uploaded to Amazon S3 be encrypted at rest\.

```
1. require 'aws-sdk-s3' 
2. 
3. s3 = Aws::S3::Resource.new(region:'us-west-2') 
4. obj = s3.bucket('my-bucket').object('key') 
5. obj.upload_file('local/path/to/file', :server_side_encryption => 'AES256')
```

For an example that shows how to upload an object without SSE, see [Upload an Object Using the AWS SDK for Ruby](UploadObjSingleOpRuby.md)\.

## Determining the Encryption Algorithm Used<a name="DeterminingEncryptionAlgorithmUsed03"></a>

The following code example demonstrates how to determine the encryption state of an existing object\.

```
1. # Determine server-side encryption of an object.
2. require 'aws-sdk-s3'
3. 
4. s3 = Aws::S3::Resource.new(region:'us-west-2')
5. enc = s3.bucket('bucket-name').object('key').server_side_encryption
6. enc_state = (enc != nil) ? enc : "not set"
7. puts "Encryption state is #{enc_state}."
```

If server\-side encryption is not used for the object that is stored in Amazon S3, the method returns null\.

## Changing Server\-Side Encryption of an Existing Object \(Copy Operation\)<a name="ChangingServer-SideEncryptionofanExistingObjectCopyOperation03"></a>

To change the encryption state of an existing object, make a copy of the object and delete the source object\. By default, the copy methods do not encrypt the target unless you explicitly request server\-side encryption\. You can request the encryption of the target object by specifying the `server_side_encryption` value in the options hash argument as shown in the following Ruby code example\. The code example demonstrates how to copy an object and encrypt the copy\. 

```
1. require 'aws-sdk-s3'
2. 
3. s3 = Aws::S3::Resource.new(region:'us-west-2')
4. bucket1 = s3.bucket('source-bucket-name')
5. bucket2 = s3.bucket('target-bucket-name')
6. obj1 = bucket1.object('key')
7. obj2 = bucket2.object('key')
8.  
9. obj1.copy_to(obj2, :server_side_encryption => 'AES256')
```

For a sample of how to copy an object without encryption, see [Copy an Object Using the AWS SDK for Ruby](CopyingObjectUsingRuby.md)\. 