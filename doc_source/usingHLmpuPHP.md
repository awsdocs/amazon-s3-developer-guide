# Using the AWS PHP SDK for Multipart Upload<a name="usingHLmpuPHP"></a>

You can upload large files to Amazon S3 in multiple parts\. You must use a multipart upload for files larger than 5 GB\. The AWS SDK for PHP exposes the [MultipartUploader](http://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.S3.MultipartUploader.html) class that simplifies multipart uploads\. 

The `upload` method of the `MultipartUploader` class is best used for a simple multipart upload\. If you need to pause and resume multipart uploads, vary part sizes during the upload, or do not know the size of the data in advance, use the low\-level PHP API\. For more information, see [Using the AWS PHP SDK for Multipart Upload \(Low\-Level API\)](usingLLmpuPHP.md)\. 

For more information about multipart uploads, see [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)\. For information on uploading files that are less than 5GB in size, see [Upload an Object Using the AWS SDK for PHP](UploadObjSingleOpPHP.md)\. 

## Upload a File Using the High\-Level Multipart Upload<a name="HLuploadFilePHP"></a>

This topic explains how to use the high\-level `Aws\S3\Model\MultipartUpload\UploadBuilder` class from the AWS SDK for PHP for multipart file uploads\. It assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\.

The following PHP example uploads a file to an Amazon S3 bucket\. The example demonstrates how to set parameters for the `MultipartUploader` object\. 

For information about running the PHP examples in this guide, see [Running PHP Examples](UsingTheMPphpAPI.md#running-php-samples)\.

```
<?php

require 'vendor/autoload.php';

use Aws\Common\Exception\MultipartUploadException;
use Aws\S3\MultipartUploader;
use Aws\S3\S3Client;

$bucket = '*** Your Bucket Name ***';
$keyname = '*** Your Object Key ***';
                        
$s3 = new S3Client([
    'version' => 'latest',
    'region'  => 'us-east-1'
]);
 
// Prepare the upload parameters.
$uploader = new MultipartUploader($s3, '/path/to/large/file.zip', [
    'bucket' => $bucket,
    'key'    => $keyname
]);

// Perform the upload.
try {
    $result = $uploader->upload();
    echo "Upload complete: {$result['ObjectURL']}" . PHP_EOL;
} catch (MultipartUploadException $e) {
    echo $e->getMessage() . PHP_EOL;
}
```

### Related Resources<a name="RelatedResources-HLuploadFilePHP"></a>
+ [ AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.S3.S3Client.html) 
+ [ Amazon S3 Multipart Uploads](http://docs.aws.amazon.com/aws-sdk-php/v3/guide/service/s3-multipart-upload.html) 
+ [AWS SDK for PHP Documentation](http://aws.amazon.com/documentation/sdk-for-php/)