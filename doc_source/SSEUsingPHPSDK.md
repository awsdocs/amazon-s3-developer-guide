# Specifying Server\-Side Encryption Using the AWS SDK for PHP<a name="SSEUsingPHPSDK"></a>

This topic shows how to use classes from version 3 of the AWS SDK for PHP to add server\-side encryption to objects that you upload to Amazon Simple Storage Service \(Amazon S3\)\. It assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\.

To upload an object to Amazon S3, use the [ Aws\\S3\\S3Client::putObject\(\)](http://docs.aws.amazon.com/aws-sdk-php/v3/api/api-s3-2006-03-01.html#putobject) method\. To add the `x-amz-server-side-encryption` request header to your upload request, specify the `ServerSideEncryption` parameter with the value `AES256`, as shown in the following code sample\. For information about server\-side encryption requests, see [Specifying Server\-Side Encryption Using the REST API](SSEUsingRESTAPI.md)\.

```
<?php

require 'vendor/autoload.php';

use Aws\S3\S3Client;

$bucket = '*** Your Bucket Name ***';
$keyname = '*** Your Object Key ***';

// $filepath should be an absolute path to a file on disk.
$filepath = '*** Your File Path ***';

$s3 = new S3Client([
    'version' => 'latest',
    'region'  => 'us-east-1'
]);

// Upload a file with server-side encryption.
$result = $s3->putObject([
    'Bucket'               => $bucket,
    'Key'                  => $keyname,
    'SourceFile'           => $filepath,
    'ServerSideEncryption' => 'AES256',
]);
```

In response, Amazon S3 returns the `x-amz-server-side-encryption` header with the value of the encryption algorithm that was used to encrypt your object's data\. 

When you upload large objects using the multipart upload API, you can specify server\-side encryption for the objects that you are uploading, as follows: 
+ When using the low\-level multipart upload API, specify server\-side encryption when you call the [ Aws\\S3\\S3Client::createMultipartUpload\(\)](http://docs.aws.amazon.com/aws-sdk-php/v3/api/api-s3-2006-03-01.html#createmultipartupload) method\. To add the `x-amz-server-side-encryption` request header to your request, specify the `array` parameter's `ServerSideEncryption` key with the value `AES256`\. For more information about the low\-level Multipart upload API, see [Using the AWS PHP SDK for Multipart Upload \(Low\-Level API\)](usingLLmpuPHP.md)\.
+ When using the high\-level multipart upload API, specify server\-side encryption using the `ServerSideEncryption` parameter of the [CreateMultipartUpload](http://docs.aws.amazon.com/aws-sdk-php/v3/api/api-s3-2006-03-01.html#createmultipartupload) method\. For an example of using the `setOption()` method with the high\-level Multipart upload API, see [Using the AWS PHP SDK for Multipart Upload](usingHLmpuPHP.md)\.

## Determining Encryption Algorithm Used<a name="DeterminingEncryptionAlgorithmUsed04"></a>

To determine the encryption state of an existing object, retrieve the object metadata by calling the [Aws\\S3\\S3Client::headObject\(\)](http://docs.aws.amazon.com/aws-sdk-php/v3/api/api-s3-2006-03-01.html#headobject) method as shown in the following PHP code sample\.

```
<?php

require 'vendor/autoload.php';

use Aws\S3\S3Client;

$bucket = '*** Your Bucket Name ***';
$keyname = '*** Your Object Key ***';
            
$s3 = new S3Client([
    'version' => 'latest',
    'region'  => 'us-east-1'
]);

// Check which server-side encryption algorithm is used.
$result = $s3->headObject([
    'Bucket' => $bucket,
    'Key'    => $keyname,
]);
echo $result['ServerSideEncryption'];
```

## Changing Server\-Side Encryption of an Existing Object \(Copy Operation\)<a name="ChangingServer-SideEncryptionofanExistingObjectCopyOperation04"></a>

To change the encryption state of an existing object, make a copy of the object using the [Aws\\S3\\S3Client::copyObject\(\)](http://docs.aws.amazon.com/aws-sdk-php/v3/api/api-s3-2006-03-01.html#copyobject) method and delete the source object\. Note that by default `copyObject()` will not encrypt the target, unless you explicitly request server\-side encryption of the destination object using the `ServerSideEncryption` parameter with the value `AES256`\. The following PHP code sample makes a copy of an object and adds server\-side encryption to the copied object\.

```
<?php

require 'vendor/autoload.php';

use Aws\S3\S3Client;

$sourceBucket = '*** Your Source Bucket Name ***';
$sourceKeyname = '*** Your Source Object Key ***';

$targetBucket = '*** Your Target Bucket Name ***';
$targetKeyname = '*** Your Target Object Key ***';

$s3 = new S3Client([
    'version' => 'latest',
    'region'  => 'us-east-1'
]);

// Copy an object and add server-side encryption.
$s3->copyObject([
    'Bucket'               => $targetBucket,
    'Key'                  => $targetKeyname,
    'CopySource'           => "{$sourceBucket}/{$sourceKeyname}",
    'ServerSideEncryption' => 'AES256',
]);
```

### Related Resources<a name="RelatedResources-ChangingServer-SideEncryptionofanExistingObjectCopyOperation04"></a>
+ [ AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.S3.S3Client.html) 
+ [AWS SDK for PHP Documentation](http://aws.amazon.com/documentation/sdk-for-php/)