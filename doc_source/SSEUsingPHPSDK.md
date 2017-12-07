# Specifying Server\-Side Encryption Using the AWS SDK for PHP<a name="SSEUsingPHPSDK"></a>

 This topic guides you through using classes from the AWS SDK for PHP to add server\-side encryption to objects you are uploading to Amazon S3\.

**Note**  
 This topic assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\.

You can use the [ Aws\\S3\\S3Client::putObject\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_putObject) method to upload an object to Amazon S3\. For a working sample of how to upload an object, see [Upload an Object Using the AWS SDK for PHP](UploadObjSingleOpPHP.md)\. 

To add the `x-amz-server-side-encryption` request header \(see [Specifying Server\-Side Encryption Using the REST API](SSEUsingRESTAPI.md)\) to your upload request, specify the `array` parameter's `ServerSideEncryption` key with the value `AES256` as shown in the following PHP code sample\. 

```
 1. use Aws\S3\S3Client;
 2. 
 3. $bucket = '*** Your Bucket Name ***';
 4. $keyname = '*** Your Object Key ***';
 5. // $filepath should be absolute path to a file on disk						
 6. $filepath = '*** Your File Path ***';
 7. 
 8. // Instantiate the client.
 9. $s3 = S3Client::factory();
10. 
11. // Upload a file with server-side encryption.
12. $result = $s3->putObject(array(
13.     'Bucket'               => $bucket,
14.     'Key'                  => $keyname,
15.     'SourceFile'           => $filepath,
16.     'ServerSideEncryption' => 'AES256',
17. ));
```

In response, Amazon S3 returns the `x-amz-server-side-encryption` header with the value of the encryption algorithm used to encrypt your object data\. 

To upload large objects using the multipart upload API, you can specify server\-side encryption for the objects that you are uploading\. 

+  When using the low\-level multipart upload API \(see [Using the AWS PHP SDK for Multipart Upload \(Low\-Level API\)](usingLLmpuPHP.md)\), you can specify server\-side encryption when you call the [ Aws\\S3\\S3Client::createMultipartUpload\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_createMultipartUpload()) method\. To add the `x-amz-server-side-encryption` request header to your request, specify the `array` parameter's `ServerSideEncryption` key with the value `AES256`\. 

+ When using the high\-level multipart upload, you can specify server\-side encryption using the [Aws\\S3\\Model\\MultipartUpload\\UploadBuilder:setOption\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.Model.MultipartUpload.UploadBuilder.html#_setOption) method like `setOption('ServerSideEncryption','AES256')`\. For an example of using the `setOption()` method with the high\-level UploadBuilder, see [Using the AWS PHP SDK for Multipart Upload \(High\-Level API\)](usingHLmpuPHP.md)\. 

## Determining Encryption Algorithm Used<a name="DeterminingEncryptionAlgorithmUsed04"></a>

To determine the encryption state of an existing object, retrieve the object metadata by calling the [Aws\\S3\\S3Client::headObject\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_headObject) method as shown in the following PHP code sample\.

```
 1. use Aws\S3\S3Client;
 2. 
 3. $bucket = '*** Your Bucket Name ***';
 4. $keyname = '*** Your Object Key ***';
 5. 			
 6. // Instantiate the client.
 7. $s3 = S3Client::factory();
 8. 
 9. // Check which server-side encryption algorithm is used.
10. $result = $s3->headObject(array(
11.     'Bucket' => $bucket,
12.     'Key'    => $keyname,
13. ));
14. echo $result['ServerSideEncryption'];
```

## Changing Server\-Side Encryption of an Existing Object \(Copy Operation\)<a name="ChangingServer-SideEncryptionofanExistingObjectCopyOperation04"></a>

To change the encryption state of an existing object, make a copy of the object using the [Aws\\S3\\S3Client::copyObject\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_copyObject) method and delete the source object\. Note that by default `copyObject()` will not encrypt the target, unless you explicitly request server\-side encryption of the destination object using the `array` parameter's `ServerSideEncryption` key with the value `AES256`\. The following PHP code sample makes a copy of an object and adds server\-side encryption to the copied object\.

```
 1. use Aws\S3\S3Client;
 2. 
 3. $sourceBucket = '*** Your Source Bucket Name ***';
 4. $sourceKeyname = '*** Your Source Object Key ***';
 5. 
 6. $targetBucket = '*** Your Target Bucket Name ***';
 7. $targetKeyname = '*** Your Target Object Key ***';
 8. 
 9. // Instantiate the client.
10. $s3 = S3Client::factory();
11. 
12. // Copy an object and add server-side encryption.
13. $result = $s3->copyObject(array(
14.     'Bucket'               => $targetBucket,
15.     'Key'                  => $targetKeyname,
16.     'CopySource'           => "{$sourceBucket}/{$sourceKeyname}",
17.     'ServerSideEncryption' => 'AES256',
18. ));
```

For a working sample of how to copy an object, see [Copy an Object Using the AWS SDK for PHP](CopyingObjectUsingPHP.md)\. 

### Related Resources<a name="RelatedResources-ChangingServer-SideEncryptionofanExistingObjectCopyOperation04"></a>

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::factory\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::copyObject\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_copyObject)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::createMultipartUpload\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_createMultipartUpload)

+ [ AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::headObject\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_headObject)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::putObject\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_putObject)

+ [Aws\\S3\\Model\\MultipartUpload\\UploadBuilder:setOption\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.Model.MultipartUpload.UploadBuilder.html#_setOption)

+ [AWS SDK for PHP for Amazon S3](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/service-s3.html)

+ [AWS SDK for PHP Documentation](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/index.html)