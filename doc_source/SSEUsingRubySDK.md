# Specifying Server\-Side Encryption Using the AWS SDK for Ruby<a name="SSEUsingRubySDK"></a>

When using the AWS SDK for Ruby to upload an object, you can specify that the object be stored encrypted at rest with server\-side encryption \(SSE\)\. When you read the object back, it is automatically decrypted\.

The following AWS SDK for Ruby – Version 3 example demonstrates how to specify that a file uploaded to Amazon S3 be encrypted at rest\.

```
require 'aws-sdk-s3'

# Uploads a file to an Amazon S3 bucket and then encrypts the file server-side
#   by using the 256-bit Advanced Encryption Standard (AES-256) block cipher.
#
# Prerequisites:
#
# - An Amazon S3 bucket.
#
# @param s3_client [Aws::S3::Client] An initialized Amazon S3 client.
# @param bucket_name [String] The name of the bucket.
# @param object_key [String] The name for the uploaded object.
# @param object_content [String] The content to upload into the object.
# @return [Boolean] true if the file was successfully uploaded and then
#   encrypted; otherwise, false.
# @example
#   exit 1 unless upload_file_encrypted_aes256_at_rest?(
#     Aws::S3::Client.new(region: 'us-east-1'),
#     'doc-example-bucket',
#     'my-file.txt',
#     'This is the content of my-file.txt.'
#   )
def upload_file_encrypted_aes256_at_rest?(
  s3_client,
  bucket_name,
  object_key,
  object_content
)
  s3_client.put_object(
    bucket: bucket_name,
    key: object_key,
    body: object_content,
    server_side_encryption: 'AES256'
  )
  return true
rescue StandardError => e
  puts "Error uploading object: #{e.message}"
  return false
end
```

For an example that shows how to upload an object without SSE, see [Upload an object using the AWS SDK for Ruby](UploadObjSingleOpRuby.md)\.

## Determining the Encryption Algorithm Used<a name="DeterminingEncryptionAlgorithmUsed03"></a>

The following code example demonstrates how to determine the encryption state of an existing object\.

```
require 'aws-sdk-s3'

# Gets the server-side encryption state of an object in an Amazon S3 bucket.
#
# Prerequisites:
#
# - An Amazon S3 bucket.
# - An object within that bucket.
#
# @param s3_client [Aws::S3::Client] An initialized Amazon S3 client.
# @param bucket_name [String] The bucket's name.
# @param object_key [String] The object's key.
# @return [String] The server-side encryption state.
# @example
#   s3_client = Aws::S3::Client.new(region: 'us-east-1')
#   puts get_server_side_encryption_state(
#     s3_client,
#     'doc-example-bucket',
#     'my-file.txt'
#   )
def get_server_side_encryption_state(s3_client, bucket_name, object_key)
  response = s3_client.get_object(
    bucket: bucket_name,
    key: object_key
  )
  encryption_state = response.server_side_encryption
  encryption_state.nil? ? 'not set' : encryption_state
rescue StandardError => e
  "unknown or error: #{e.message}"
end
```

If server\-side encryption is not used for the object that is stored in Amazon S3, the method returns null\.

## Changing Server\-Side Encryption of an Existing Object \(Copy Operation\)<a name="ChangingServer-SideEncryptionofanExistingObjectCopyOperation03"></a>

To change the encryption state of an existing object, make a copy of the object and delete the source object\. By default, the copy methods do not encrypt the target unless you explicitly request server\-side encryption\. You can request the encryption of the target object by specifying the `server_side_encryption` value in the options hash argument as shown in the following Ruby code example\. The code example demonstrates how to copy an object and encrypt the copy\. 

```
require 'aws-sdk-s3'

# Copies an object from one Amazon S3 bucket to another,
#   changing the object's server-side encryption state during 
#   the copy operation.
#
# Prerequisites:
#
# - A bucket containing an object to be copied.
# - A separate bucket to copy the object into.
#
# @param s3_client [Aws::S3::Client] An initialized Amazon S3 client.
# @param source_bucket_name [String] The source bucket's name.
# @param source_object_key [String] The name of the object to be copied.
# @param target_bucket_name [String] The target bucket's name.
# @param target_object_key [String] The name of the copied object.
# @param encryption_type [String] The server-side encryption type for
#   the copied object.
# @return [Boolean] true if the object was copied with the specified
#   server-side encryption; otherwise, false.
# @example
#   s3_client = Aws::S3::Client.new(region: 'us-east-1')
#   if object_copied_with_encryption?(
#     s3_client,
#     'doc-example-bucket1',
#     'my-source-file.txt',
#     'doc-example-bucket2',
#     'my-target-file.txt',
#     'AES256'
#   )
#     puts 'Copied.'
#   else
#     puts 'Not copied.'
#   end
def object_copied_with_encryption?(
  s3_client,
  source_bucket_name,
  source_object_key,
  target_bucket_name,
  target_object_key,
  encryption_type
)
  response = s3_client.copy_object(
    bucket: target_bucket_name,
    copy_source: source_bucket_name + '/' + source_object_key,
    key: target_object_key,
    server_side_encryption: encryption_type
  )
  return true if response.copy_object_result
rescue StandardError => e
  puts "Error while copying object: #{e.message}"
end
```

For a sample of how to copy an object without encryption, see [Copy an Object Using the AWS SDK for Ruby](CopyingObjectUsingRuby.md)\. 