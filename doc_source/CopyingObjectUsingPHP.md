# Copy an Object Using the AWS SDK for PHP<a name="CopyingObjectUsingPHP"></a>

 This topic guides you through using classes from the AWS SDK for PHP to copy a single object and multiple objects within Amazon S3, from one bucket to another or within the same bucket\.

**Note**  
 This topic assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\.

The following tasks guide you through using PHP SDK classes to copy an object that is already stored in Amazon S3\.


**Copying an Object**  

|  |  | 
| --- |--- |
| 1 |  Create an instance of an Amazon S3 client by using the [Aws\\S3\\S3Client](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html) class [factory\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory) method\.  | 
| 2 |  To copy an object, execute the [Aws\\S3\\S3Client::copyObject\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_copyObject) method\. You need to provide information such as source bucket, source key name, target bucket, and target key name\.  | 

The following PHP code example demonstrates using the `copyObject()` method to copy an object that is already stored in Amazon S3\.

**Example**  

```
 1. use Aws\S3\S3Client;
 2. 
 3. $sourceBucket = '*** Your Source Bucket Name ***';
 4. $sourceKeyname = '*** Your Source Object Key ***';
 5. $targetBucket = '*** Your Target Bucket Name ***';
 6. $targetKeyname = '*** Your Target Key Name ***';		
 7. 					
 8. // Instantiate the client.
 9. $s3 = S3Client::factory();
10. 
11. // Copy an object.
12. $s3->copyObject(array(
13.     'Bucket'     => $targetBucket,
14.     'Key'        => $targetKeyname,
15.     'CopySource' => "{$sourceBucket}/{$sourceKeyname}",
16. ));
```

The following tasks guide you through using PHP classes to make multiple copies of an object within Amazon S3\. 


**Copying Objects**  

|  |  | 
| --- |--- |
| 1 |  Create an instance of an Amazon S3 client by using the `Aws\S3\S3Client` class `factory()` method\.  | 
| 2 |  To make multiple copies of an object, you execute a batch of calls to the Amazon S3 client [getCommand\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Guzzle.Service.Client.html#_getCommand) method, which is inherited from the [Guzzle\\Service\\Client](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Guzzle.Service.Client.html) class\. You provide the `CopyObject` command as the first argument and an array containing the source bucket, source key name, target bucket, and target key name as the second argument\.   | 

The following PHP code example demonstrates making multiple copies of an object that is stored in Amazon S3\.

**Example**  

```
 1. use Aws\S3\S3Client;
 2. 
 3. $sourceBucket = '*** Your Source Bucket Name ***';
 4. $sourceKeyname = '*** Your Source Object Key ***';
 5. $targetBucket = '*** Your Target Bucket Name ***';
 6. $targetKeyname = '*** Your Target Key Name ***';				
 7. 
 8. // Instantiate the client.
 9. $s3 = S3Client::factory();
10. 
11. // Perform a batch of CopyObject operations.
12. $batch = array();
13. for ($i = 1; $i <= 3; $i++) {
14.     $batch[] = $s3->getCommand('CopyObject', array(
15.         'Bucket'     => $targetBucket,
16.         'Key'        => "{targetKeyname}-{$i}",
17.         'CopySource' => "{$sourceBucket}/{$sourceKeyname}",
18.     ));
19. }
20. try {
21.     $successful = $s3->execute($batch);
22.     $failed = array();
23. } catch (\Guzzle\Service\Exception\CommandTransferException $e) {
24.     $successful = $e->getSuccessfulCommands();
25.     $failed = $e->getFailedCommands();
26. }
```

**Example of Copying Objects within Amazon S3**  
The following PHP example illustrates the use of the `copyObject()` method to copy a single object within Amazon S3 and using a batch of calls to `CopyObject` using the `getcommand()` method to make multiple copies of an object\.  

```
<?php

// Include the AWS SDK using the Composer autoloader.
require 'vendor/autoload.php';

use Aws\S3\S3Client;

$sourceBucket = '*** Your Source Bucket Name ***';
$sourceKeyname = '*** Your Source Object Key ***';
$targetBucket = '*** Your Target Bucket Name ***';

// Instantiate the client.
$s3 = S3Client::factory();

// Copy an object.
$s3->copyObject(array(
    'Bucket'     => $targetBucket,
    'Key'        => "{$sourceKeyname}-copy",
    'CopySource' => "{$sourceBucket}/{$sourceKeyname}",
));

// Perform a batch of CopyObject operations.
$batch = array();
for ($i = 1; $i <= 3; $i++) {
    $batch[] = $s3->getCommand('CopyObject', array(
        'Bucket'     => $targetBucket,
        'Key'        => "{$sourceKeyname}-copy-{$i}",
        'CopySource' => "{$sourceBucket}/{$sourceKeyname}",
    ));
}
try {
    $successful = $s3->execute($batch);
    $failed = array();
} catch (\Guzzle\Service\Exception\CommandTransferException $e) {
    $successful = $e->getSuccessfulCommands();
    $failed = $e->getFailedCommands();
}
```

## Related Resources<a name="RelatedResources-CopyingObjectUsingPHP"></a>
+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html)
+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::copyObject\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_copyObject)
+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::factory\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory)
+ [AWS SDK for PHP for Amazon S3 Guzzle\\Service\\Client Class](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Guzzle.Service.Client.html)
+ [AWS SDK for PHP for Amazon S3 Guzzle\\Service\\Client::getCommand\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Guzzle.Service.Client.html#_getCommand)
+ [AWS SDK for PHP for Amazon S3](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/service-s3.html)
+ [AWS SDK for PHP Documentation](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/index.html)