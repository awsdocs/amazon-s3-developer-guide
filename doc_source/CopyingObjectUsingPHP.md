# Copy an Object Using the AWS SDK for PHP<a name="CopyingObjectUsingPHP"></a>

 This topic guides you through using classes from version 3 of the AWS SDK for PHP to copy a single object and multiple objects within Amazon S3, from one bucket to another or within the same bucket\.

 This topic assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\.

The following tasks guide you through using PHP SDK classes to copy an object that is already stored in Amazon S3\.

The following tasks guide you through using PHP classes to make multiple copies of an object within Amazon S3\. 


**Copying Objects**  

|  |  | 
| --- |--- |
| 1 |  Create an instance of an Amazon S3 client by using the `Aws\S3\S3Client` class constructor\.  | 
| 2 |  To make multiple copies of an object, you execute a batch of calls to the Amazon S3 client [getCommand\(\)](http://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.AwsClientInterface.html#_getCommand) method, which is inherited from the [Aws\\CommandInterface](http://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.CommandInterface.html) class\. You provide the `CopyObject` command as the first argument and an array containing the source bucket, source key name, target bucket, and target key name as the second argument\.   | 

**Example of Copying Objects within Amazon S3**  
The following PHP example illustrates the use of the `copyObject()` method to copy a single object within Amazon S3 and using a batch of calls to `CopyObject` using the `getcommand()` method to make multiple copies of an object\.  

```
<?php

require 'vendor/autoload.php';

use Aws\S3\S3Client;

$sourceBucket = '*** Your Source Bucket Name ***';
$sourceKeyname = '*** Your Source Object Key ***';
$targetBucket = '*** Your Target Bucket Name ***';

$s3 = new S3Client([
    'version' => 'latest',
    'region'  => 'us-east-1'
]);

// Copy an object.
$s3->copyObject([
    'Bucket'     => $targetBucket,
    'Key'        => "{$sourceKeyname}-copy",
    'CopySource' => "{$sourceBucket}/{$sourceKeyname}",
]);

// Perform a batch of CopyObject operations.
$batch = array();
for ($i = 1; $i <= 3; $i++) {
    $batch[] = $s3->getCommand('CopyObject', [
        'Bucket'     => $targetBucket,
        'Key'        => "{targetKeyname}-{$i}",
        'CopySource' => "{$sourceBucket}/{$sourceKeyname}",
    ]);
}
try {
    $results = CommandPool::batch($s3, $batch);
    foreach($results as $result) {
        if ($result instanceof ResultInterface) {
            // Result handling here
        }
        if ($result instanceof AwsException) {
            // AwsException handling here
        }
    }
} catch (\Exception $e) {
    // General error handling here
}
```

## Related Resources<a name="RelatedResources-CopyingObjectUsingPHP"></a>
+ [ AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.S3.S3Client.html) 
+ [AWS SDK for PHP Documentation](http://aws.amazon.com/documentation/sdk-for-php/)