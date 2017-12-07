# Upload an Object Using the AWS SDK for PHP<a name="UploadObjSingleOpPHP"></a>

 This topic guides you through using classes from the AWS SDK for PHP to upload an object of up to 5 GB in size\. For larger files you must use multipart upload API\. For more information, see [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)\.

**Note**  
 This topic assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\.


**Uploading Objects**  

|  |  | 
| --- |--- |
| 1 |  Create an instance of an Amazon S3 client by using the [Aws\\S3\\S3Client](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html) class [factory\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory) method\.  | 
| 2 |   Execute the [ Aws\\S3\\S3Client::putObject\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_putObject) method\. You must provide a bucket name and a key name in the `array` parameter's required keys, `Bucket` and `Key`\. If you are uploading a file, you specify the file name by adding the array parameter with the `SourceFile` key\. You can also provide the optional object metadata using the array parameter\.  | 

The following PHP code example demonstrates how to create an object by uploading a file specified in the `SourceFile` key in the `putObject` method's array parameter\. 

**Example**  

```
 1. use Aws\S3\S3Client;
 2. 
 3. $bucket = '*** Your Bucket Name ***';
 4. $keyname = '*** Your Object Key ***';
 5. // $filepath should be absolute path to a file on disk						
 6. $filepath = '*** Your File Path ***';
 7. 						
 8. // Instantiate the client.
 9. $s3 = S3Client::factory();
10. 
11. // Upload a file.
12. $result = $s3->putObject(array(
13.     'Bucket'       => $bucket,
14.     'Key'          => $keyname,
15.     'SourceFile'   => $filepath,
16.     'ContentType'  => 'text/plain',
17.     'ACL'          => 'public-read',
18.     'StorageClass' => 'REDUCED_REDUNDANCY',
19.     'Metadata'     => array(    
20.         'param1' => 'value 1',
21.         'param2' => 'value 2'
22.     )
23. ));
24. 
25. echo $result['ObjectURL'];
```

Instead of specifying a file name, you can provide object data inline by specifying the array parameter with the `Body` key, as shown in the following PHP code example\. 

**Example**  

```
 1. use Aws\S3\S3Client;
 2. 
 3. $bucket = '*** Your Bucket Name ***';
 4. $keyname = '*** Your Object Key ***';
 5. 						
 6. // Instantiate the client.
 7. $s3 = S3Client::factory();
 8. 
 9. // Upload data.
10. $result = $s3->putObject(array(
11.     'Bucket' => $bucket,
12.     'Key'    => $keyname,
13.     'Body'   => 'Hello, world!'
14. ));
15. 
16. echo $result['ObjectURL'];
```

**Example of Creating an Object in an Amazon S3 bucket by Uploading Data**  
The following PHP example creates an object in a specified bucket by uploading data using the `putObject()` method\. For information about running the PHP examples in this guide, go to [Running PHP Examples](UsingTheMPphpAPI.md#running-php-samples)\.   

```
<?php

// Include the AWS SDK using the Composer autoloader.
require 'vendor/autoload.php';

use Aws\S3\S3Client;
use Aws\S3\Exception\S3Exception;

$bucket = '*** Your Bucket Name ***';
$keyname = '*** Your Object Key ***';
						
// Instantiate the client.
$s3 = S3Client::factory();

try {
    // Upload data.
    $result = $s3->putObject(array(
        'Bucket' => $bucket,
        'Key'    => $keyname,
        'Body'   => 'Hello, world!',
        'ACL'    => 'public-read'
    ));

    // Print the URL to the object.
    echo $result['ObjectURL'] . "\n";
} catch (S3Exception $e) {
    echo $e->getMessage() . "\n";
}
```

## Related Resources<a name="RelatedResources-UploadObjSingleOpPHP"></a>

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::factory\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::putObject\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_putObject)

+ [AWS SDK for PHP for Amazon S3](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/service-s3.html)

+ [AWS SDK for PHP Documentation](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/index.html)