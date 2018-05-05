# Upload an Object Using the AWS SDK for PHP<a name="UploadObjSingleOpPHP"></a>

 This topic guides you through using classes from the AWS SDK for PHP to upload an object of up to 5 GB in size\. For larger files you must use multipart upload API\. For more information, see [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)\.

 This topic assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\.

**Example of Creating an Object in an Amazon S3 bucket by Uploading Data**  
The following PHP example creates an object in a specified bucket by uploading data using the `putObject()` method\. For information about running the PHP examples in this guide, go to [Running PHP Examples](UsingTheMPphpAPI.md#running-php-samples)\.   

```
<?php

require 'vendor/autoload.php';

use Aws\S3\S3Client;
use Aws\S3\Exception\S3Exception;

$bucket = '*** Your Bucket Name ***';
$keyname = '*** Your Object Key ***';
                        
$s3 = new S3Client([
    'version' => 'latest',
    'region'  => 'us-east-1'
]);

try {
    // Upload data.
    $result = $s3->putObject([
        'Bucket' => $bucket,
        'Key'    => $keyname,
        'Body'   => 'Hello, world!',
        'ACL'    => 'public-read'
    ]);

    // Print the URL to the object.
    echo $result['ObjectURL'] . PHP_EOL;
} catch (S3Exception $e) {
    echo $e->getMessage() . PHP_EOL;
}
```

## Related Resources<a name="RelatedResources-UploadObjSingleOpPHP"></a>
+ [ AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.S3.S3Client.html) 
+ [AWS SDK for PHP Documentation](http://aws.amazon.com/documentation/sdk-for-php/)