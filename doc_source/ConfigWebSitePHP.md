# Managing Websites with the AWS SDK for PHP<a name="ConfigWebSitePHP"></a>

 This topic guides you through using classes from the AWS SDK for PHP to configure and manage an Amazon S3 bucket for website hosting\. For more information about the Amazon S3 website feature, see [Hosting a Static Website on Amazon S3](WebsiteHosting.md)\.

**Note**  
 This topic assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\.

 The following tasks guide you through using the PHP SDK classes to configure and manage an Amazon S3 bucket for website hosting\.


**Configuring a Bucket for Website Hosting**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of an Amazon S3 client by using the [Aws\\S3\\S3Client](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html) class [factory\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory) method\.  | 
|  2  |  To configure a bucket as a website, execute the [Aws\\S3\\S3Client::putBucketWebsite\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_putBucketWebsite) method\. You need to provide the bucket name and the website configuration information, including the index document and the error document names\. If you don't provide these document names, this method adds the `index.html` and `error.html` default names to the website configuration\. You must verify that these documents are present in the bucket\.  | 
|  3  |  To retrieve existing bucket website configuration, execute the [Aws\\S3\\S3Client::getBucketWebsite\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_getBucketWebsite) method\.  | 
|  4  |  To delete website configuration from a bucket, execute the [Aws\\S3\\S3Client::deleteBucketWebsite\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_deleteBucketWebsite) method, passing the bucket name as a parameter\. If you remove the website configuration, the bucket is no longer accessible from the website endpoints\.  | 

The following PHP code sample demonstrates the preceding tasks\.

**Example**  

```
 1. use Aws\S3\S3Client;
 2. 
 3. $bucket = '*** Your Bucket Name ***';
 4. 				
 5. // 1. Instantiate the client.
 6. $s3 = S3Client::factory();
 7. 
 8. // 2. Add website configuration.
 9. $result = $s3->putBucketWebsite(array(
10.     'Bucket'        => $bucket,    
11.     'IndexDocument' => array('Suffix' => 'index.html'),
12.     'ErrorDocument' => array('Key' => 'error.html'),
13. ));
14. 
15. // 3. Retrieve website configuration.
16. $result = $s3->getBucketWebsite(array(
17.     'Bucket' => $bucket,
18. ));
19. echo $result->getPath('IndexDocument/Suffix');
20. 
21. // 4.) Delete website configuration.
22. $result = $s3->deleteBucketWebsite(array(
23.     'Bucket' => $bucket,
24. ));
```

**Example of Configuring an Bucket Amazon S3 for Website Hosting**  
The following PHP code example first adds a website configuration to the specified bucket\. The `create_website_config` method explicitly provides the index document and error document names\. The sample also retrieves the website configuration and prints the response\. For more information about the Amazon S3 website feature, see [Hosting a Static Website on Amazon S3](WebsiteHosting.md)\.  
 For instructions on how to create and test a working sample, see [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md)\.   

```
<?php

// Include the AWS SDK using the Composer autoloader.
require 'vendor/autoload.php';

use Aws\S3\S3Client;

$bucket = '*** Your Bucket Name ***';

// Instantiate the client.
$s3 = S3Client::factory();

// 1.) Add website configuration.
$result = $s3->putBucketWebsite(array(
    'Bucket'        => $bucket,    
    'IndexDocument' => array('Suffix' => 'index.html'),
    'ErrorDocument' => array('Key' => 'error.html'),
));

// 2.) Retrieve website configuration.
$result = $s3->getBucketWebsite(array(
    'Bucket' => $bucket,
));
echo $result->getPath('IndexDocument/Suffix');

// 3.) Delete website configuration.
$result = $s3->deleteBucketWebsite(array(
    'Bucket' => $bucket,
));
```

## Related Resources<a name="RelatedResources-"></a>
+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html)
+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::deleteBucketWebsite\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_deleteBucketWebsite)
+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::factory\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory)
+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::getBucketWebsite\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_getBucketWebsite)
+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::putBucketWebsite\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_putBucketWebsite)
+ [AWS SDK for PHP for Amazon S3](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/service-s3.html)
+ [AWS SDK for PHP Documentation](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/index.html)