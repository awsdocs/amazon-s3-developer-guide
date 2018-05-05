# Making Requests Using AWS Account or IAM User Credentials \- AWS SDK for PHP<a name="AuthUsingAcctOrUserCredPHP3"></a>

This section explains how to use a class from version 3 of the AWS SDK for PHP to send authenticated requests using your AWS account or IAM user credentials\. It assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\. 

The following PHP example shows how the client makes a request using your security credentials to list all of the buckets for your account\. 

**Example**  

```
<?php


require 'vendor/autoload.php';

use Aws\Sts\StsClient;
use Aws\S3\S3Client;
use Aws\S3\Exception\S3Exception;

$bucket = '*** Your Bucket Name ***';

$s3 = new S3Client([
    'region' => 'us-east-1',
    'version' => 'latest',
]);

// Retrieve the list of buckets.
$result = $s3->listBuckets();

try {
    // Retrieve a paginator for listing objects.
    $objects = $s3->getPaginator('ListObjects', [
        'Bucket' => $bucket
    ]);

    echo "Keys retrieved!" . PHP_EOL;
    
    // Print the list of objects to the page.
    foreach ($objects as $object) {
        echo $object['Key'] . PHP_EOL;
    }
} catch (S3Exception $e) {
    echo $e->getMessage() . PHP_EOL;
}
```

**Note**  
You can create the `S3Client` client without providing your security credentials\. Requests sent using this client are anonymous requests, without a signature\. Amazon S3 returns an error if you send anonymous requests for a resource that is not publicly available\. 

For working examples, see [Operations on Objects](ObjectOperations.md)\. You can test these examples using your AWS account or IAM user credentials\. 

For an example of listing object keys in a bucket, see [Listing Keys Using the AWS SDK for PHP](ListingObjectKeysUsingPHP.md)\. 

## Related Resources<a name="RelatedResources-AuthUsingAcctOrUserCredPHP3-related-resources"></a>
+ [ AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.S3.S3Client.html) 
+ [AWS SDK for PHP Documentation](http://aws.amazon.com/documentation/sdk-for-php/)