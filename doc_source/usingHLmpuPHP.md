# Using the AWS PHP SDK for Multipart Upload \(High\-Level API\)<a name="usingHLmpuPHP"></a>

Amazon S3 allows you to upload large files in multiple parts\. You must use a multipart upload for files larger than 5 GB\. The AWS SDK for PHP exposes the high\-level [Aws\\S3\\Model\\MultipartUpload\\UploadBuilder](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.Model.MultipartUpload.UploadBuilder.html) class that simplifies multipart uploads\. 

The `Aws\S3\Model\MultipartUpload\UploadBuilder` class is best used for a simple multipart upload\. If you need to pause and resume multipart uploads, vary part sizes during the upload, or do not know the size of the data in advance, you should use the low\-level PHP API\. For more information, see [Using the AWS PHP SDK for Multipart Upload \(Low\-Level API\)](usingLLmpuPHP.md)\. 

 For more information about multipart uploads, see [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)\. For information on uploading files that are less than 5GB in size, see [Upload an Object Using the AWS SDK for PHP](UploadObjSingleOpPHP.md)\. 

## Upload a File Using the High\-Level Multipart Upload<a name="HLuploadFilePHP"></a>

 This topic guides you through using the high\-level `Aws\S3\Model\MultipartUpload\UploadBuilder` class from the AWS SDK for PHP for multipart file uploads\. 

**Note**  
 This topic assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\.


**High\-Level Multipart File Upload Process**  

|  |  | 
| --- |--- |
| 1 |  Create an instance of an Amazon S3 client by using the [Aws\\S3\\S3Client](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html) class [factory\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory) method\.  | 
| 2 |  Create an instance of the UploadBuilder using the Amazon S3 `Aws\S3\Model\MultipartUpload\UploadBuilder` class [newInstance\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.Common.Model.MultipartUpload.AbstractUploadBuilder.html#_newInstance) method, which is inherited from the [Aws\\Common\\Model\\MultipartUpload\\AbstractUploadBuilder](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.Common.Model.MultipartUpload.AbstractUploadBuilder.html) class\. For the UploadBuilder object set the client, the bucket name, and the key name using the [setClient\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.Common.Model.MultipartUpload.AbstractUploadBuilder.html#_setClient), [setBucket\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.Model.MultipartUpload.UploadBuilder.html#_setBucket), and [setKey\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.Model.MultipartUpload.UploadBuilder.html#_setKey) methods\. Set the path and name of the file you want to upload with the [setSource\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.Common.Model.MultipartUpload.AbstractUploadBuilder.html#_setSource) method\.   | 
| 3 | Execute the `UploadBuilder` object's [build\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.Model.MultipartUpload.UploadBuilder.html#_build) method to build the appropriate uploader transfer object based on the builder options you set\. \(The transfer object is of a subclass of the [Aws\\S3\\Model\\MultipartUpload\\AbstractTransfer](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.Model.MultipartUpload.AbstractTransfer.html) class\.\)  | 
| 4 | Execute the `upload()` method of the built transfer object to perform the upload\. | 

The following PHP code sample demonstrates how to upload a file using the high\-level `UploadBuilder` object\.

**Example**  

```
 1. use Aws\Common\Exception\MultipartUploadException;
 2. use Aws\S3\Model\MultipartUpload\UploadBuilder;
 3. use Aws\S3\S3Client;
 4. 
 5. $bucket = '*** Your Bucket Name ***';
 6. $keyname = '*** Your Object Key ***';
 7. 						
 8. // Instantiate the client.
 9. $s3 = S3Client::factory();
10. 
11. // Prepare the upload parameters.
12. $uploader = UploadBuilder::newInstance()
13.     ->setClient($s3)
14.     ->setSource('/path/to/large/file.mov')
15.     ->setBucket($bucket)
16.     ->setKey($keyname)
17.     ->build();
18. 
19. // Perform the upload. Abort the upload if something goes wrong.
20. try {
21.     $uploader->upload();
22.     echo "Upload complete.\n";
23. } catch (MultipartUploadException $e) {
24.     $uploader->abort();
25.     echo "Upload failed.\n";
26.     echo $e->getMessage() . "\n";
27. }
```

**Example of a Multipart Upload of a File to an Amazon S3 Bucket Using the High\-level UploadBuilder**  
 The following PHP example uploads a file to an Amazon S3 bucket\. The example demonstrates how to set advanced options for the UploadBuilder object\. For example, you can use the [setMinPartSize\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.Model.MultipartUpload.UploadBuilder.html#_setMinPartSize) method to set the part size you want to use for the multipart upload and the [setOption\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.Model.MultipartUpload.UploadBuilder.html#_setOption) method to set optional file metadata or an access control list \(ACL\)\.   
 The example also demonstrates how to upload file parts in parallel by setting the concurrency option using the [setConcurrency\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.Model.MultipartUpload.UploadBuilder.html#_setConcurrency) method for the UploadBuilder object\. The example creates a transfer object that will attempt to upload three parts in parallel until the entire file has been uploaded\. For information about running the PHP examples in this guide, go to [Running PHP Examples](UsingTheMPphpAPI.md#running-php-samples)\.   

```
<?php

// Include the AWS SDK using the Composer autoloader.
require 'vendor/autoload.php';

use Aws\Common\Exception\MultipartUploadException;
use Aws\S3\Model\MultipartUpload\UploadBuilder;
use Aws\S3\S3Client;

$bucket = '*** Your Bucket Name ***';
$keyname = '*** Your Object Key ***';
						
// Instantiate the client.
$s3 = S3Client::factory();
 
// Prepare the upload parameters.
$uploader = UploadBuilder::newInstance()
    ->setClient($s3)
    ->setSource('/path/to/large/file.mov')
    ->setBucket($bucket)
    ->setKey($keyname)
    ->setMinPartSize(25 * 1024 * 1024)
    ->setOption('Metadata', array(
        'param1' => 'value1',
        'param2' => 'value2'
    ))
    ->setOption('ACL', 'public-read')
    ->setConcurrency(3)
    ->build();

// Perform the upload. Abort the upload if something goes wrong.
try {
    $uploader->upload();
    echo "Upload complete.\n";
} catch (MultipartUploadException $e) {
    $uploader->abort();
    echo "Upload failed.\n";
    echo $e->getMessage() . "\n";
}
```

### Related Resources<a name="RelatedResources-HLuploadFilePHP"></a>

+ [AWS SDK for PHP Aws\\Common\\Model\\MultipartUpload\\AbstractUploadBuilder Class](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.Common.Model.MultipartUpload.AbstractUploadBuilder.html)

+ [AWS SDK for PHP Aws\\Common\\Model\\MultipartUpload\\AbstractUploadBuilder::newInstance\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.Common.Model.MultipartUpload.AbstractUploadBuilder.html#_newInstance)

+ [AWS SDK for PHP Aws\\Common\\Model\\MultipartUpload\\AbstractUploadBuilder::SetSource\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.Common.Model.MultipartUpload.AbstractUploadBuilder.html#_setSource)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\Model\\MultipartUpload\\UploadBuilder Class](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.Model.MultipartUpload.UploadBuilder.html)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\Model\\MultipartUpload\\UploadBuilder::build\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.Model.MultipartUpload.UploadBuilder.html)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\Model\\MultipartUpload\\UploadBuilder:setMinPartSize\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.Model.MultipartUpload.UploadBuilder.html#_setMinPartSize)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\Model\\MultipartUpload\\UploadBuilder:setOption\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.Model.MultipartUpload.UploadBuilder.html#_setOption)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\Model\\MultipartUpload\\UploadBuilder:setConcurrency\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.Model.MultipartUpload.UploadBuilder.html#_setConcurrency)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html) 

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::factory\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory) 

+ [AWS SDK for PHP for Amazon S3 \- Uploading Large Files Using Multipart Uploads](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/service-s3.html#uploading-large-files-using-multipart-uploads)

+ [AWS SDK for PHP for Amazon S3](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/service-s3.html)

+ [AWS SDK for PHP Documentation](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/index.html)