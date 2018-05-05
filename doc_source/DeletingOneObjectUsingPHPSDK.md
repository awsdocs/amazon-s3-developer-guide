# Deleting an Object Using the AWS SDK for PHP<a name="DeletingOneObjectUsingPHPSDK"></a>

This topic shows how to use classes from version 3 of the AWS SDK for PHP to delete an object from a non\-versioned bucket\. For information on deleting an object from a versioned bucket, see [Deleting an Object Using the REST API](DeletingAnObjectsUsingREST.md)\. 

This topic assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\.

The following PHP example deletes an object from a bucket\. Because this example shows how to delete objects from non\-versioned buckets, it provides only the bucket name and object key \(not a version ID\) in the delete request\. \. For information about running the PHP examples in this guide, see [Running PHP Examples](UsingTheMPphpAPI.md#running-php-samples)\.

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

// Delete an object from the bucket.
$s3->deleteObject([
    'Bucket' => $bucket,
    'Key'    => $keyname
]);
```

## Related Resources<a name="RelatedResources-DeletingOneObjectUsingPHPSDK"></a>
+ [ AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.S3.S3Client.html) 
+ [AWS SDK for PHP Documentation](http://aws.amazon.com/documentation/sdk-for-php/)