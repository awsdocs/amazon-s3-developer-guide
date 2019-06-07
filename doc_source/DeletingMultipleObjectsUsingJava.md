# Deleting Multiple Objects Using the AWS SDK for Java<a name="DeletingMultipleObjectsUsingJava"></a>

The AWS SDK for Java provides the `AmazonS3Client.deleteObjects()` method for deleting multiple objects\. For each object that you want to delete, you specify the key name\. If the bucket is versioning\-enabled, you have the following options:
+ Specify only the object's key name\. Amazon S3 will add a delete marker to the object\.
+ Specify both the object's key name and a version ID to be deleted\. Amazon S3 will delete the specified version of the object\.

**Example**  
The following example uses the Multi\-Object Delete API to delete objects from a bucket that is not version\-enabled\. The example uploads sample objects to the bucket and then uses the `AmazonS3Client.deleteObjects()` method to delete the objects in a single request\. In the `DeleteObjectsRequest`, the example specifies only the object key names because the objects do not have version IDs\.   
For more information about deleting objects, see [Deleting Objects](DeletingObjects.md)\. For instructions on creating and testing a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\.   

```
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
]) ->getPath('Contents/*/Key');

// 3. Delete the objects.
$s3->deleteObjects([
    'Bucket'  => $bucket,
    'Delete' => [
        'Objects' => array_map(function ($key) {
            return ['Key' => $key];
        }, $keys)
    ],
]);
```

**Example**  
The following example uses the Multi\-Object Delete API to delete objects from a version\-enabled bucket\. It does the following:   

1. Creates sample objects and then deletes them, specifying the key name and version ID for each object to delete\. The operation deletes only the specified object versions\.

1. Creates sample objects and then deletes them by specifying only the key names\. Because the example doesn't specify version IDs, the operation adds a delete marker to each object, without deleting any specific object versions\. After the delete markers are added, these objects will not appear in the AWS Management Console\.

1. Remove the delete markers by specifying the object keys and version IDs of the delete markers\. The operation deletes the delete markers, which results in the objects reappearing in the AWS Management Console\.

```
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
    'Status' => 'Enabled',
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
$versions = $s3->listObjectVersions(['Bucket' => $bucket])
    ->getPath('Versions');

// 4. Delete the object versions.
$s3->deleteObjects([
    'Bucket'  => $bucket,
    'Delete' => [
        'Objects' => array_map(function ($version) {
          return [
              'Key'       => $version['Key'],
              'VersionId' => $version['VersionId']
        }, $versions),
    ],       
]);

echo "The following objects were deleted successfully:". PHP_EOL;
foreach ($result['Deleted'] as $object) {
    echo "Key: {$object['Key']}, VersionId: {$object['VersionId']}" . PHP_EOL;
}

echo PHP_EOL . "The following objects could not be deleted:" . PHP_EOL;
foreach ($result['Errors'] as $object) {
    echo "Key: {$object['Key']}, VersionId: {$object['VersionId']}" . PHP_EOL;
}

// 5. Suspend object versioning for the bucket.
$s3->putBucketVersioning([
    'Bucket' => $bucket,
    'Status' => 'Suspended',
]);
```