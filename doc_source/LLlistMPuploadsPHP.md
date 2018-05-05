# List Multipart Uploads Using the Low\-Level AWS SDK for PHP API<a name="LLlistMPuploadsPHP"></a>

This topic shows how to use the low\-level API classes from version 3 of the AWS SDK for PHP to list all in\-progress multipart uploads on a bucket\. It assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\. 

The following PHP example demonstrates listing all in\-progress multipart uploads on a bucket\.

```
<?php 

require 'vendor/autoload.php';

use Aws\S3\S3Client;

$bucket = '*** Your Bucket Name ***';

$s3 = new S3Client([
    'version' => 'latest',
    'region'  => 'us-east-1'
]);

// Retrieve a list of the current multipart uploads.
$result = $s3->listMultipartUploads([
    'Bucket' => $bucket
]);

// Write the list of uploads to the page.
print_r($result->toArray());
```

## Related Resources<a name="RelatedResources-LLlistMPuploadsPHP"></a>
+ [ AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.S3.S3Client.html) 
+ [ Amazon S3 Multipart Uploads](http://docs.aws.amazon.com/aws-sdk-php/v3/guide/service/s3-multipart-upload.html) 
+ [AWS SDK for PHP Documentation](http://aws.amazon.com/documentation/sdk-for-php/)