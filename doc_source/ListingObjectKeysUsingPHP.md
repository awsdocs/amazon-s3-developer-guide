# Listing Keys Using the AWS SDK for PHP<a name="ListingObjectKeysUsingPHP"></a>

This topic guides you through using classes from the AWS SDK for PHP to list the object keys contained in an Amazon S3 bucket\. 

**Note**  
 This topic assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\. 

To list the object keys contained in a bucket using the AWS SDK for PHP you first must list the objects contained in the bucket and then extract the key from each of the listed objects\. When listing objects in a bucket you have the option of using the low\-level [Aws\\S3\\S3Client::listObjects\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_listObjects) method or the high\-level [Aws\\S3\\Iterator\\ListObjects](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.Iterator.ListObjectsIterator.html) iterator\. 

The low\-level `listObjects()` method maps to the underlying Amazon S3 REST API\. Each `listObjects()` request returns a page of up to 1,000 objects\. If you have more than 1,000 objects in the bucket, your response will be truncated and you will need to send another `listObjects()` request to retrieve the next set of 1,000 objects\. 

You can use the high\-level `ListObjects` iterator to make your task of listing the objects contained in a bucket a bit easier\. To use the `ListObjects` iterator to create a list of objects you execute the Amazon S3 client [getIterator\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Guzzle.Service.Client.html#_getIteratorgetIterator) method that is inherited from [Guzzle\\Service\\Client](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Guzzle.Service.Client.html) class with the `ListObjects` command as the first argument and an array to contain the returned objects from the specified bucket as the second argument\. When used as a `ListObjects` iterator the `getIterator()` method returns all the objects contained in the specified bucket\. There is no 1,000 object limit, so you don't need to worry if the response is truncated or not\.

The following tasks guide you through using the PHP Amazon S3 client methods to list the objects contained in a bucket from which you can list the object keys\.


**Listing Object Keys**  

|  |  | 
| --- |--- |
| 1 |  Create an instance of an Amazon S3 client by using the [Aws\\S3\\S3Client](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html) class [factory](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory) method\.  | 
| 2 | Execute the high\-level Amazon S3 client `getIterator()` method with the `ListObjects` command as the first argument and an array to contain the returned objects from the specified bucket as the second argument\. Or you can execute the low\-level Amazon S3 client `listObjects()` method with an array to contain the returned objects from the specified bucket as the argument\.  | 
| 3 | Extract the object key from each object in the list of returned objects\.  | 

The following PHP code sample demonstrates how to list the objects contained in a bucket from which you can list the object keys\.

**Example**  

```
 1. use Aws\S3\S3Client;
 2. 
 3. // Instantiate the client.
 4. $s3 = S3Client::factory();
 5. 
 6. $bucket = '*** Bucket Name ***';
 7. 					
 8. // Use the high-level iterators (returns ALL of your objects).
 9. $objects = $s3->getIterator('ListObjects', array('Bucket' => $bucket));
10. 
11. echo "Keys retrieved!\n";
12. foreach ($objects as $object) {
13.     echo $object['Key'] . "\n";
14. }
15. 
16. // Use the plain API (returns ONLY up to 1000 of your objects).
17. $result = $s3->listObjects(array('Bucket' => $bucket));
18. 
19. echo "Keys retrieved!\n";
20. foreach ($result['Contents'] as $object) {
21.     echo $object['Key'] . "\n";
22. }
```

**Example of Listing Object Keys**  
The following PHP example demonstrates how to list the keys from a specified bucket\. It shows how to use the high\-level `getIterator()` method to list the objects in a bucket and then how to extract the key from each of the objects in the list\. It also show how to use the low\-level `listObjects()` method to list the objects in a bucket and then how to extract the key from each of the objects in the list returned\. For information about running the PHP examples in this guide, go to [Running PHP Examples](UsingTheMPphpAPI.md#running-php-samples)\.   

```
<?php

// Include the AWS SDK using the Composer autoloader.
require 'vendor/autoload.php';

use Aws\S3\S3Client;
use Aws\S3\Exception\S3Exception;

$bucket = '*** Your Bucket Name ***';

// Instantiate the client.
$s3 = S3Client::factory();

// Use the high-level iterators (returns ALL of your objects).
try {
    $objects = $s3->getIterator('ListObjects', array(
        'Bucket' => $bucket
    ));

    echo "Keys retrieved!\n";
    foreach ($objects as $object) {
        echo $object['Key'] . "\n";
    }
} catch (S3Exception $e) {
    echo $e->getMessage() . "\n";
}

// Use the plain API (returns ONLY up to 1000 of your objects).
try {
    $result = $s3->listObjects(array('Bucket' => $bucket));

    echo "Keys retrieved!\n";
    foreach ($result['Contents'] as $object) {
        echo $object['Key'] . "\n";
    }
} catch (S3Exception $e) {
    echo $e->getMessage() . "\n";
}
```

## Related Resources<a name="RelatedResources-ListingObjectKeysUsingPHP"></a>

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\Iterator\\ListObjects](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.Iterator.ListObjectsIterator.html)

+ [AWS SDK for PHP for Amazon S3 Guzzle\\Service\\Client Class](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Guzzle.Service.Client.html)

+ [AWS SDK for PHP for Amazon S3 Guzzle\\Service\\Client::getIterator\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Guzzle.Service.Client.html#_getIteratorgetIterator)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::factory\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::listObjects\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_listObjects)

+ [AWS SDK for PHP for Amazon S3](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/service-s3.html)

+ [AWS SDK for PHP Documentation](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/index.html)