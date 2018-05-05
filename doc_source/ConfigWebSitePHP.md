# Managing Websites with the AWS SDK for PHP<a name="ConfigWebSitePHP"></a>

This topic explains how to use classes from the AWS SDK for PHP to configure and manage an Amazon S3 bucket for website hosting\. It assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\. For more information about the Amazon S3 website feature, see [Hosting a Static Website on Amazon S3](WebsiteHosting.md)\.

The following PHP example adds a website configuration to the specified bucket\. The `create_website_config` method explicitly provides the index document and error document names\. The example also retrieves the website configuration and prints the response\. For more information about the Amazon S3 website feature, see [Hosting a Static Website on Amazon S3](WebsiteHosting.md)\.

 For instructions on creating and testing a working sample, see [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md)\. 

```
<?php

require 'vendor/autoload.php';

use Aws\S3\S3Client;

$bucket = '*** Your Bucket Name ***';
                
$s3 = new S3Client([
    'version' => 'latest',
    'region'  => 'us-east-1'
]);

         
// Add the website configuration.
$s3->putBucketWebsite([
    'Bucket'                => $bucket,
    'WebsiteConfiguration'  => [
        'IndexDocument' => ['Suffix' => 'index.html'],
        'ErrorDocument' => ['Key' => 'error.html']
    ]
]);
        
// Retrieve the website configuration.
$result = $s3->getBucketWebsite([
    'Bucket' => $bucket
]);
echo $result->getPath('IndexDocument/Suffix');
        
// Delete the website configuration.
$s3->deleteBucketWebsite([
    'Bucket' => $bucket
]);
```

## Related Resources<a name="RelatedResources-"></a>
+ [ AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.S3.S3Client.html) 
+ [AWS SDK for PHP Documentation](http://aws.amazon.com/documentation/sdk-for-php/)