# Deleting multiple objects using the AWS SDK for PHP<a name="DeletingMultipleObjectsUsingPHPSDK"></a>

This topic shows how to use classes from version 3 of the AWS SDK for PHP to delete multiple objects from versioned and non\-versioned Amazon S3 buckets\. For more information about versioning, see [Using versioning](Versioning.md)\.

 This topic assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\.

**Example Deleting multiple objects from a non\-versioned bucket**  
The following PHP example uses the `deleteObjects()` method to delete multiple objects from a bucket that is not version\-enabled\.  
 For information about running the PHP examples in this guide, see [Running PHP Examples](UsingTheMPphpAPI.md#running-php-samples)\.   

```
<?php

require 'vendor/autoload.php';

use Aws\S3\S3Client;

$bucket = '*** Your Bucket Name ***';

$s3 = new S3Client([
    'version' => 'latest',
    'region'  => 'us-east-1'
]);

// 1. Create a few objects.
for ($i = 1; $i <= 3; $i++) {
    $s3->putObject([
        'Bucket' => $bucket,
        'Key'    => "key{$i}",
        'Body'   => "content {$i}",
    ]);
}

// 2. List the objects and get the keys.
$keys = $s3->listObjects([
    'Bucket' => $bucket
]); 

// 3. Delete the objects.
foreach ($keys['Contents'] as $key)
{
    $s3->deleteObjects([
        'Bucket'  => $bucket,
        'Delete' => [
            'Objects' => [
                [
                    'Key' => $key['Key']
                ]
            ]
        ]
    ]);
}
```

**Example Deleting multiple objects from a version\-enabled bucket**  
The following PHP example uses the `deleteObjects()` method to delete multiple objects from a version\-enabled bucket\.  
 For information about running the PHP examples in this guide, see [Running PHP Examples](UsingTheMPphpAPI.md#running-php-samples)\.   

```
<?php

require 'vendor/autoload.php';

use Aws\S3\S3Client;

$bucket = '*** Your Bucket Name ***';
$keyname = '*** Your Object Key ***';

$s3 = new S3Client([
    'version' => 'latest',
    'region'  => 'us-east-1'
]);

// 1. Enable object versioning for the bucket.
$s3->putBucketVersioning([
    'Bucket' => $bucket,
    'VersioningConfiguration' => [ 
        'Status' => 'Enabled'
    ]
]);

// 2. Create a few versions of an object.
for ($i = 1; $i <= 3; $i++) {
    $s3->putObject([
        'Bucket' => $bucket,
        'Key'    => $keyname,
        'Body'   => "content {$i}",
    ]);
}

// 3. List the objects versions and get the keys and version IDs.
$versions = $s3->listObjectVersions(['Bucket' => $bucket]);

// 4. Delete the object versions.
$deletedResults = 'The following objects were deleted successfully:' . PHP_EOL;
$deleted = false;
$errorResults = 'The following objects could not be deleted:' . PHP_EOL;
$errors = false;

foreach ($versions['Versions'] as $version)
{
    $result = $s3->deleteObjects([
        'Bucket'  => $bucket,
        'Delete' => [
            'Objects' => [
                [
                    'Key' => $version['Key'],
                    'VersionId' => $version['VersionId']
                ]
            ]
        ]
    ]);

    if (isset($result['Deleted']))
    {
        $deleted = true;

        $deletedResults .= "Key: {$result['Deleted'][0]['Key']}, " . 
            "VersionId: {$result['Deleted'][0]['VersionId']}" . PHP_EOL;
    }

    if (isset($result['Errors']))
    {
        $errors = true;

        $errorResults .= "Key: {$result['Errors'][0]['Key']}, " . 
            "VersionId: {$result['Errors'][0]['VersionId']}, " .
            "Message: {$result['Errors'][0]['Message']}" . PHP_EOL;
    }
}

if ($deleted)
{
    echo $deletedResults;
}

if ($errors)
{
    echo $errorResults;
}

// 5. Suspend object versioning for the bucket.
$s3->putBucketVersioning([
    'Bucket' => $bucket,
    'VersioningConfiguration' => [ 
        'Status' => 'Suspended'
    ]
]);
```

## Related resources<a name="RelatedResources-DeletingMultipleObjectsUsingPHPSDK"></a>
+ [ AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](https://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.S3.S3Client.html) 
+ [AWS SDK for PHP Documentation](http://aws.amazon.com/documentation/sdk-for-php/)