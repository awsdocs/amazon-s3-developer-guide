# Get an Object Using the AWS SDK for PHP<a name="RetrieveObjSingleOpPHP"></a>

This topic guides you through using a class from the AWS SDK for PHP to retrieve an object\. You can retrieve an entire object or specify a byte range to retrieve from the object\. 

**Note**  
 This topic assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\.


**Downloading an Object**  

|  |  | 
| --- |--- |
| 1 |  Create an instance of an Amazon S3 client by using the [Aws\\S3\\S3Client](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html) class [factory\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory) method\.  | 
| 2 |  Execute the [Aws\\S3\\S3Client::getObject\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_getObject) method\. You must provide a bucket name and a key name in the `array` parameter's required keys, `Bucket` and `Key`\. Instead of retrieving the entire object you can retrieve a specific byte range from the object data\. You provide the range value by specifying the array parameter's `Range` key in addition to the required keys\. You can save the object you retrieved from Amazon S3 to a file in your local file system by specifying a file path to where to save the file in the array parameter's `SaveAs` key, in addition to the required keys, `Bucket` and `Key`\.  | 

The following PHP code example demonstrates the preceding tasks for downloading an object\.

**Example**  

```
 1. use Aws\S3\S3Client;
 2. 
 3. $bucket = '*** Your Bucket Name ***';
 4. $keyname = '*** Your Object Key ***';
 5. $filepath = '*** Your File Path ***';
 6. 					
 7. // Instantiate the client.
 8. $s3 = S3Client::factory();
 9. 
10. // Get an object.
11. $result = $s3->getObject(array(
12.     'Bucket' => $bucket,
13.     'Key'    => $keyname
14. ));
15. 
16. // Get a range of bytes from an object.
17. $result = $s3->getObject(array(
18.     'Bucket' => $bucket,
19.     'Key'    => $keyname,
20.     'Range'  => 'bytes=0-99'
21. ));
22. 
23. // Save object to a file.
24. $result = $s3->getObject(array(
25.     'Bucket' => $bucket,
26.     'Key'    => $keyname,
27.     'SaveAs' => $filepath
28. ));
```

When retrieving an object, you can optionally override the response header values \(see [Getting Objects](GettingObjectsUsingAPIs.md)\) by adding the array parameter's response keys, `ResponseContentType`, `ResponseContentLanguage`, `ResponseContentDisposition`, `ResponseCacheControl`, and `ResponseExpires`, to the `getObject()` method, as shown in the following PHP code example\.

**Example**  

```
1. $result = $s3->getObject(array(
2.     'Bucket'                     => $bucket,
3.     'Key'                        => $keyname,
4.     'ResponseContentType'        => 'text/plain',
5.     'ResponseContentLanguage'    => 'en-US',
6.     'ResponseContentDisposition' => 'attachment; filename=testing.txt',
7.     'ResponseCacheControl'       => 'No-cache',
8.     'ResponseExpires'            => gmdate(DATE_RFC2822, time() + 3600),
9. ));
```

**Example of Downloading an Object Using PHP**  
The following PHP example retrieves an object and displays object content in the browser\. The example illustrates the use of the `getObject()` method\. For information about running the PHP examples in this guide, go to [Running PHP Examples](UsingTheMPphpAPI.md#running-php-samples)\.   

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
    // Get the object
    $result = $s3->getObject(array(
        'Bucket' => $bucket,
        'Key'    => $keyname
    ));

    // Display the object in the browser
    header("Content-Type: {$result['ContentType']}");
    echo $result['Body'];
} catch (S3Exception $e) {
    echo $e->getMessage() . "\n";
}
```

## Related Resources<a name="RelatedResources-RetrieveObjSingleOpPHP"></a>

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::factory\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::getObject\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_getObject)

+ [AWS SDK for PHP for Amazon S3 \- Downloading Objects](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/service-s3.html#downloading-objects)

+ [AWS SDK for PHP for Amazon S3](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/service-s3.html)

+ [AWS SDK for PHP Documentation](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/index.html)