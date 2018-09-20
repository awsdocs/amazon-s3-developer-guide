# Listing Keys Using the AWS SDK for PHP<a name="ListingObjectKeysUsingPHP"></a>

This topic guides you through using classes from version 3 of the AWS SDK for PHP to list the object keys contained in an Amazon S3 bucket\. 

 This topic assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\. 

To list the object keys contained in a bucket using the AWS SDK for PHP you first must list the objects contained in the bucket and then extract the key from each of the listed objects\. When listing objects in a bucket you have the option of using the low\-level [Aws\\S3\\S3Client::listObjects\(\)](http://docs.aws.amazon.com/aws-sdk-php/v3/api/api-s3-2006-03-01.html#listobjects) method or the high\-level [Aws\\ResultPaginator](http://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.ResultPaginator.html) class\. 

The low\-level `listObjects()` method maps to the underlying Amazon S3 REST API\. Each `listObjects()` request returns a page of up to 1,000 objects\. If you have more than 1,000 objects in the bucket, your response will be truncated and you will need to send another `listObjects()` request to retrieve the next set of 1,000 objects\. 

You can use the high\-level `ListObjects` paginator to make your task of listing the objects contained in a bucket a bit easier\. To use the `ListObjects` paginator to create a list of objects you execute the Amazon S3 client [getPaginator\(\)](http://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.AwsClientInterface.html#_getPaginator) method that is inherited from [Aws/AwsClientInterface](http://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.AwsClientInterface.html) class with the `ListObjects` command as the first argument and an array to contain the returned objects from the specified bucket as the second argument\. When used as a `ListObjects` paginator the `getPaginator()` method returns all the objects contained in the specified bucket\. There is no 1,000 object limit, so you don't need to worry if the response is truncated or not\.

The following tasks guide you through using the PHP Amazon S3 client methods to list the objects contained in a bucket from which you can list the object keys\.

**Example of Listing Object Keys**  
The following PHP example demonstrates how to list the keys from a specified bucket\. It shows how to use the high\-level `getIterator()` method to list the objects in a bucket and then how to extract the key from each of the objects in the list\. It also show how to use the low\-level `listObjects()` method to list the objects in a bucket and then how to extract the key from each of the objects in the list returned\. For information about running the PHP examples in this guide, go to [Running PHP Examples](UsingTheMPphpAPI.md#running-php-samples)\.   

```
<?php

require 'vendor/autoload.php';

use Aws\S3\S3Client;
use Aws\S3\Exception\S3Exception;

$bucket = '*** Your Bucket Name ***';

// Instantiate the client.
$s3 = new S3Client([
    'version' => 'latest',
    'region'  => 'us-east-1'
]);

// Use the high-level iterators (returns ALL of your objects).
try {
    $results = $s3->getPaginator('ListObjects', [
        'Bucket' => $bucket
    ]);

    foreach ($results as $result) {
        foreach ($result['Contents'] as $object) {
            echo $object['Key'] . PHP_EOL;
        }
    }
} catch (S3Exception $e) {
    echo $e->getMessage() . PHP_EOL;
}

// Use the plain API (returns ONLY up to 1000 of your objects).
try {
    $objects = $s3->listObjects([
        'Bucket' => $bucket
    ]);
    foreach ($objects['Contents']  as $object) {
        echo $object['Key'] . PHP_EOL;
    }
} catch (S3Exception $e) {
    echo $e->getMessage() . PHP_EOL;
}
```

## Related Resources<a name="RelatedResources-ListingObjectKeysUsingPHP"></a>
+ [ AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.S3.S3Client.html) 
+ [ Paginators](http://docs.aws.amazon.com/aws-sdk-php/v3/guide/guide/paginators.html) 
+ [AWS SDK for PHP Documentation](http://aws.amazon.com/documentation/sdk-for-php/)