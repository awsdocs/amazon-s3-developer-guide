# Upload a File in Multiple Parts Using the PHP SDK Low\-Level API<a name="LLuploadFilePHP"></a>

 This topic guides you through using low\-level multipart upload classes from the AWS SDK for PHP to upload a file in multiple parts\.

**Note**  
 This topic assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\.


**PHP SDK Low\-Level API Multipart File Upload Process**  

|  |  | 
| --- |--- |
| 1 |  Create an instance of an Amazon S3 client by using the [Aws\\S3\\S3Client](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html) class [factory\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory) method\.  | 
| 2 |  Initiate multipart upload by executing the [ Aws\\S3\\S3Client::createMultipartUpload\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_createMultipartUpload) method\. You must provide a bucket name and a key name in the `array` parameter's required keys, `Bucket` and `Key`\. Retrieve and save the `UploadID` from the response body\. The `UploadID` is used in each subsequent multipart upload operation\.  | 
| 3 |   Upload the file in parts by executing the [ Aws\\S3\\S3Client::uploadPart\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_uploadPart) method for each file part until the end of the file is reached\. The required `array` parameter keys for `upload_part()` are `Bucket`, `Key`, `UploadId`, and `PartNumber`\. You must increment the value passed as the argument for the `PartNumber` key for each subsequent call to `upload_part()` to upload each successive file part\.  Save the response of each of the `upload_part()` methods calls in an array\. Each response includes the ETag value you will later need to complete the multipart upload\.   | 
| 4 |   Execute the [ Aws\\S3\\S3Client::completeMultipartUpload\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_completeMultipartUpload) method to complete the multipart upload\. The required `array` parameters for `completeMultipartUpload()` are `Bucket`, `Key`, and `UploadId`\.  | 

The following PHP code example demonstrates uploading a file in multiple parts using the PHP SDK low\-level API\.

**Example**  

```
 1. use Aws\S3\S3Client;
 2. 
 3. $bucket = '*** Your Bucket Name ***';
 4. $keyname = '*** Your Object Key ***';
 5. $filename = '*** Path to and Name of the File to Upload ***';					
 6. 					
 7. // 1. Instantiate the client.
 8. $s3 = S3Client::factory();
 9. 
10. // 2. Create a new multipart upload and get the upload ID.
11. $response = $s3->createMultipartUpload(array(
12.     'Bucket' => $bucket,
13.     'Key'    => $keyname
14. ));
15. $uploadId = $response['UploadId'];
16. 
17. // 3. Upload the file in parts.
18. $file = fopen($filename, 'r');
19. $parts = array();
20. $partNumber = 1;
21. while (!feof($file)) {
22.     $result = $s3->uploadPart(array(
23.         'Bucket'     => $bucket,
24.         'Key'        => $key,
25.         'UploadId'   => $uploadId,
26.         'PartNumber' => $partNumber,
27.         'Body'       => fread($file, 5 * 1024 * 1024),
28.     ));
29.     $parts[] = array(
30.         'PartNumber' => $partNumber++,
31.         'ETag'       => $result['ETag'],
32.     );
33. }
34. 
35. // 4. Complete multipart upload.
36. $result = $s3->completeMultipartUpload(array(
37.     'Bucket'   => $bucket,
38.     'Key'      => $key,
39.     'UploadId' => $uploadId,
40.     'Parts'    => $parts,
41. ));
42. $url = $result['Location'];
43. 
44. fclose($file);
```

**Example of Uploading a File to an Amazon S3 Bucket Using the Low\-level Multipart Upload PHP SDK API**  
The following PHP code example uploads a file to an Amazon S3 bucket using the low\-level PHP API multipart upload\. For information about running the PHP examples in this guide, go to [Running PHP Examples](UsingTheMPphpAPI.md#running-php-samples)\.   

```
<?php

// Include the AWS SDK using the Composer autoloader
require 'vendor/autoload.php';

use Aws\S3\S3Client;

$bucket = '*** Your Bucket Name ***';
$keyname = '*** Your Object Key ***';
$filename = '*** Path to and Name of the File to Upload ***';

// 1. Instantiate the client.
$s3 = S3Client::factory();

// 2. Create a new multipart upload and get the upload ID.
$result = $s3->createMultipartUpload(array(
    'Bucket'       => $bucket,
    'Key'          => $keyname,
    'StorageClass' => 'REDUCED_REDUNDANCY',
    'ACL'          => 'public-read',
    'Metadata'     => array(
        'param1' => 'value 1',
        'param2' => 'value 2',
        'param3' => 'value 3'
    )
));
$uploadId = $result['UploadId'];

// 3. Upload the file in parts.
try {    
    $file = fopen($filename, 'r');
    $parts = array();
    $partNumber = 1;
    while (!feof($file)) {
        $result = $s3->uploadPart(array(
            'Bucket'     => $bucket,
            'Key'        => $keyname,
            'UploadId'   => $uploadId,
            'PartNumber' => $partNumber,
            'Body'       => fread($file, 5 * 1024 * 1024),
        ));
        $parts[] = array(
            'PartNumber' => $partNumber++,
            'ETag'       => $result['ETag'],
        );

        echo "Uploading part {$partNumber} of {$filename}.\n";
    }
    fclose($file);
} catch (S3Exception $e) {
    $result = $s3->abortMultipartUpload(array(
        'Bucket'   => $bucket,
        'Key'      => $keyname,
        'UploadId' => $uploadId
    ));

    echo "Upload of {$filename} failed.\n";
}

// 4. Complete multipart upload.
$result = $s3->completeMultipartUpload(array(
    'Bucket'   => $bucket,
    'Key'      => $keyname,
    'UploadId' => $uploadId,
    'Parts'    => $parts,
));
$url = $result['Location'];

echo "Uploaded {$filename} to {$url}.\n";
```

## Related Resources<a name="RelatedResources-LLuploadFilePHP"></a>
+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html)
+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::factory\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory)
+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::createMultipartUpload\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_createMultipartUpload)
+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::uploadPart\(\)Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_uploadPart)
+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::completeMultipartUpload\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_completeMultipartUpload)
+ [AWS SDK for PHP for Amazon S3](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/service-s3.html)
+ [AWS SDK for PHP Documentation](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/index.html)