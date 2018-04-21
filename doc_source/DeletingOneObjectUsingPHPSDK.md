# Deleting an Object Using the AWS SDK for PHP<a name="DeletingOneObjectUsingPHPSDK"></a>

 This topic guides you through using classes from the AWS SDK for PHP to delete an object from a non\-versioned bucket\. For information on deleting an object from a versioned bucket, see [Deleting an Object Using the REST API](DeletingAnObjectsUsingREST.md)\. 

**Note**  
 This topic assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\.


**Deleting One Object \(Non\-Versioned Bucket\)**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of an Amazon S3 client by using the [Aws\\S3\\S3Client](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html) class [factory\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory) method\.  | 
|  2  |  Execute the [Aws\\S3\\S3Client::deleteObject\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_deleteObject) method\. You must provide a bucket name and a key name in the `array` parameter's required keys, `Bucket` and `Key`\. If you have not enabled versioning on the bucket, the operation deletes the object\. If you have enabled versioning, the operation adds a delete marker\. For more information, see [Deleting Objects](DeletingObjects.md)\.  | 

The following PHP code sample demonstrates how to delete an object from an Amazon S3 bucket using the `deleteObject()` method\.

```
 1. use Aws\S3\S3Client;
 2. 
 3. $s3 = S3Client::factory();
 4. 
 5. $bucket = '*** Your Bucket Name ***';
 6. $keyname = '*** Your Object Key ***';
 7. 
 8. $result = $s3->deleteObject(array(
 9.     'Bucket' => $bucket,
10.     'Key'    => $keyname
11. ));
```

**Example Deleting an Object from a Non\-Versioned Bucket**  
The following PHP code example deletes an object from a bucket\. It does not provide a version Id in the delete request\. If you have not enabled versioning on the bucket, Amazon S3 deletes the object\. If you have enabled versioning, Amazon S3 adds a delete marker and the object is not deleted\. For information about running the PHP examples in this guide, go to [Running PHP Examples](UsingTheMPphpAPI.md#running-php-samples)\. For information on deleting an object from a versioned bucket, see [Deleting an Object Using the REST API](DeletingAnObjectsUsingREST.md)\.   

```
<?php

// Include the AWS SDK using the Composer autoloader.
require 'vendor/autoload.php';

use Aws\S3\S3Client;

$s3 = S3Client::factory();

$bucket = '*** Your Bucket Name ***';
$keyname = '*** Your Object Key ***';

$result = $s3->deleteObject(array(
    'Bucket' => $bucket,
    'Key'    => $keyname
));
```

## Related Resources<a name="RelatedResources-DeletingOneObjectUsingPHPSDK"></a>
+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html)
+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::factory\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory)
+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::deleteObject\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_deleteObject)
+ [AWS SDK for PHP for Amazon S3](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/service-s3.html)
+ [AWS SDK for PHP Documentation](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/index.html)