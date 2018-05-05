# Upload a File in Multiple Parts Using the PHP SDK Low\-Level API<a name="LLuploadFilePHP"></a>

This topic guides shows how to use the low\-level `uploadPart` method from version 3 of the AWS SDK for PHP to upload a file in multiple parts\. It assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\.

The following PHP example uploads a file to an Amazon S3 bucket using the low\-level PHP API multipart upload\. For information about running the PHP examples in this guide, see [Running PHP Examples](UsingTheMPphpAPI.md#running-php-samples)\. 

```
<?php

require 'vendor/autoload.php';

use Aws\S3\S3Client;

$bucket = '*** Your Bucket Name ***';
$keyname = '*** Your Object Key ***';
$filename = '*** Path to and Name of the File to Upload ***';

$s3 = new S3Client([
    'version' => 'latest',
    'region'  => 'us-east-1'
]);

$result = $s3->createMultipartUpload([
    'Bucket'       => $bucket,
    'Key'          => $keyname,
    'StorageClass' => 'REDUCED_REDUNDANCY',
    'ACL'          => 'public-read',
    'Metadata'     => [
        'param1' => 'value 1',
        'param2' => 'value 2',
        'param3' => 'value 3'
    ]
]);
$uploadId = $result['UploadId'];

// Upload the file in parts.
try {
    $file = fopen($filename, 'r');
    $partNumber = 1;
    while (!feof($file)) {
        $result = $s3->uploadPart([
            'Bucket'     => $bucket,
            'Key'        => $keyname,
            'UploadId'   => $uploadId,
            'PartNumber' => $partNumber,
            'Body'       => fread($file, 5 * 1024 * 1024),
        ]);
        $parts['Parts'][$partNumber] = [
            'PartNumber' => $partNumber,
            'ETag' => $result['ETag'],
        ];
        $partNumber++;

        echo "Uploading part {$partNumber} of {$filename}." . PHP_EOL;
    }
    fclose($file);
} catch (S3Exception $e) {
    $result = $s3->abortMultipartUpload([
        'Bucket'   => $bucket,
        'Key'      => $keyname,
        'UploadId' => $uploadId
    ]);

    echo "Upload of {$filename} failed." . PHP_EOL;
}

// Complete the multipart upload.
$result = $s3->completeMultipartUpload([
    'Bucket'   => $bucket,
    'Key'      => $keyname,
    'UploadId' => $uploadId,
    'MultipartUpload'    => $parts,
]);
$url = $result['Location'];

echo "Uploaded {$filename} to {$url}." . PHP_EOL;
```

## Related Resources<a name="RelatedResources-LLuploadFilePHP"></a>
+ [ AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.S3.S3Client.html) 
+ [ Amazon S3 Multipart Uploads](http://docs.aws.amazon.com/aws-sdk-php/v3/guide/service/s3-multipart-upload.html) 
+ [AWS SDK for PHP Documentation](http://aws.amazon.com/documentation/sdk-for-php/)