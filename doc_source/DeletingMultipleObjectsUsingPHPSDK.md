# Deleting Multiple Objects Using the AWS SDK for PHP<a name="DeletingMultipleObjectsUsingPHPSDK"></a>

 This topic guides you through using classes from the AWS SDK for PHP to delete multiple objects from versioned and non\-versioned Amazon S3 buckets\. For more information about versioning, see [Using Versioning](Versioning.md)\.

**Note**  
 This topic assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\.

The following tasks guide you through using the PHP SDK classes to delete multiple objects from a non\-versioned bucket\. 


**Deleting Multiple Objects \(Non\-Versioned Bucket\)**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of an Amazon S3 client by using the [Aws\\S3\\S3Client](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html) class [factory\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory) method\.  | 
|  2  |  Execute the [Aws\\S3\\S3Client::deleteObjects\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_deleteObjects) method\. You need to provide a bucket name and an array of object keys as parameters\. You can specify up to 1000 keys\.  | 

The following PHP code sample demonstrates deleting multiple objects from an Amazon S3 non\-versioned bucket\.

```
 1. use Aws\S3\S3Client;
 2. 
 3. $bucket = '*** Your Bucket Name ***';
 4. $keyname1 = '*** Your Object Key1 ***';
 5. $keyname2 = '*** Your Object Key2 ***';
 6. $keyname3 = '*** Your Object Key3 ***';
 7. 
 8. $s3 = S3Client::factory();
 9. 
10. // Delete objects from a bucket
11. $result = $s3->deleteObjects(array(
12.     'Bucket'  => $bucket,
13.     'Objects' => array(
14.         array('Key' => $keyname1),
15.         array('Key' => $keyname2),
16.         array('Key' => $keyname3),
17.     )
18. ));
```

The following tasks guide you through deleting multiple objects from an Amazon S3 version\-enabled bucket\.


**Deleting Multiple Objects \(Version\-Enabled Bucket\)**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of an Amazon S3 client by using the `Aws\S3\S3Client` class `factory()` method\.  | 
|  2  |  Execute the `Aws\S3\S3Client::deleteObjects()` method and provide a list of objects keys and optionally the version IDs of the objects that you want to delete\.  If you specify version ID of the object that you want to delete, Amazon S3 deletes the specific object version\. If you don't specify the version ID of the object that you want to delete, Amazon S3 adds a delete marker\. For more information, see [Deleting One Object Per Request](DeletingOneObject.md)\.  | 

The following PHP code sample demonstrates deleting multiple objects from an Amazon S3 version\-enabled bucket\. 

```
 1. use Aws\S3\S3Client;
 2. 
 3. $bucket = '*** Your Bucket Name ***';
 4. $keyname = '*** Your Object Key ***';
 5. $versionId1 = '*** Your Object Key Version ID1 ***';             
 6. $versionId2 = '*** Your Object Key Version ID2 ***';    
 7. $versionId3 = '*** Your Object Key Version ID3 ***';    
 8. 
 9. $s3 = S3Client::factory();
10. 
11. // Delete object versions from a versioning-enabled bucket.
12. $result = $s3->deleteObjects(array(
13.     'Bucket'  => $bucket,
14.     'Objects' => array(
15.         array('Key' => $keyname, 'VersionId' => $versionId1),
16.         array('Key' => $keyname, 'VersionId' => $versionId2),
17.         array('Key' => $keyname, 'VersionId' => $versionId3),
18.     )
19. ));
```

Amazon S3 returns a response that shows the objects that were deleted and objects it could not delete because of errors \(for example, permission errors\)\.

 The following PHP code sample prints the object keys for objects that were deleted\. It also prints the object keys that were not deleted and the related error messages\. 

```
1. echo "The following objects were deleted successfully:\n";
2. foreach ($result['Deleted'] as $object) {
3.     echo "Key: {$object['Key']}, VersionId: {$object['VersionId']}\n";
4. }
5. 
6. echo "\nThe following objects could not be deleted:\n";
7. foreach ($result['Errors'] as $object) {
8.     echo "Key: {$object['Key']}, VersionId: {$object['VersionId']}\n";
9. }
```

**Example 1: Multi\-Object Delete \(Non\-Versioned Bucket\)**  
The following PHP code example uses the `deleteObjects()` method to delete multiple objects from a bucket that is not version\-enabled\.  
The example performs the following actions:  

1.  Creates a few objects by using the [ Aws\\S3\\S3Client::putObject\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_putObject) method\.

1.  Lists the objects and gets the keys of the created objects using the [ Aws\\S3\\S3Client::listObjects\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_listObjects) method\.

1.  Performs a non\-versioned delete by using the `Aws\S3\S3Client::deleteObjects()` method\.
 For information about running the PHP examples in this guide, go to [Running PHP Examples](UsingTheMPphpAPI.md#running-php-samples)\.   

```
<?php

// Include the AWS SDK using the Composer autoloader.
require 'vendor/autoload.php';

use Aws\S3\S3Client;

$bucket = '*** Your Bucket Name ***';

// Instantiate the client.
$s3 = S3Client::factory();

// 1. Create a few objects.
for ($i = 1; $i <= 3; $i++) {
    $s3->putObject(array(
        'Bucket' => $bucket,
        'Key'    => "key{$i}",
        'Body'   => "content {$i}",
    ));
}

// 2. List the objects and get the keys.
$keys = $s3->listObjects(array('Bucket' => $bucket))
    ->getPath('Contents/*/Key');

// 3. Delete the objects.
$result = $s3->deleteObjects(array(
    'Bucket'  => $bucket,
    'Objects' => array_map(function ($key) {
        return array('Key' => $key);
    }, $keys),
));
```

**Example 2: Multi\-Object Delete \(Version\-Enabled Bucket\)**  
The following PHP code example uses the `deleteObjects()` method to delete multiple objects from a version\-enabled bucket\.  
The example performs the following actions:  

1.  Enables versioning on the bucket by using the [ Aws\\S3\\S3Client::putBucketVersioning\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_putBucketVersioning) method\.

1.  Creates a few versions of an object by using the `Aws\S3\S3Client::putObject()` method\.

1.  Lists the objects versions and gets the keys and version IDs for the created object versions using the [ Aws\\S3\\S3Client::listObjectVersions\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_listObjectVersions) method\.

1.  Performs a versioned\-delete by using the `Aws\S3\S3Client::deleteObjects()` method with the retrieved keys and versions IDs\.

1.  Disables versioning on the bucket by using the `Aws\S3\S3Client::putBucketVersioning()` method\.
 For information about running the PHP examples in this guide, go to [Running PHP Examples](UsingTheMPphpAPI.md#running-php-samples)\.   

```
<?php

// Include the AWS SDK using the Composer autoloader.
require 'vendor/autoload.php';

use Aws\S3\S3Client;

$bucket = '*** Your Bucket Name ***';
$keyname = '*** Your Object Key ***';

// Instantiate the client.
$s3 = S3Client::factory();

// 1. Enable object versioning for the bucket.
$s3->putBucketVersioning(array(
    'Bucket' => $bucket,
    'Status' => 'Enabled',
));

// 2. Create a few versions of an object.
for ($i = 1; $i <= 3; $i++) {
    $s3->putObject(array(
        'Bucket' => $bucket,
        'Key'    => $keyname,
        'Body'   => "content {$i}",
    ));
}

// 3. List the objects versions and get the keys and version IDs.
$versions = $s3->listObjectVersions(array('Bucket' => $bucket))
    ->getPath('Versions');

// 4. Delete the object versions.
$result = $s3->deleteObjects(array(
    'Bucket'  => $bucket,
    'Objects' => array_map(function ($version) {
        return array(
            'Key'       => $version['Key'],
            'VersionId' => $version['VersionId']
        );
    }, $versions),
));

echo "The following objects were deleted successfully:\n";
foreach ($result['Deleted'] as $object) {
    echo "Key: {$object['Key']}, VersionId: {$object['VersionId']}\n";
}

echo "\nThe following objects could not be deleted:\n";
foreach ($result['Errors'] as $object) {
    echo "Key: {$object['Key']}, VersionId: {$object['VersionId']}\n";
}

// 5. Suspend object versioning for the bucket.
$s3->putBucketVersioning(array(
    'Bucket' => $bucket,
    'Status' => 'Suspended',
));
```

## Related Resources<a name="RelatedResources-DeletingMultipleObjectsUsingPHPSDK"></a>

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::factory\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::deleteObject\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_deleteObject)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::listObjects\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_listObjects)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::listObjectVersions\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_listObjectVersions)

+ [ AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::putObject\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_putObject)

+ [ AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::putBucketVersioning\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_putBucketVersioning)

+ [AWS SDK for PHP for Amazon S3](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/service-s3.html)

+ [AWS SDK for PHP Documentation](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/index.html)