# Using the AWS SDK for PHP and Running PHP Examples<a name="UsingTheMPphpAPI"></a>

The AWS SDK for PHP provides access to the API for Amazon S3 bucket and object operations\. The SDK gives you the option of using the service's low\-level API or using higher\-level abstractions\.

The SDK is available at [AWS SDK for PHP](https://aws.amazon.com/sdk-for-php/), which also has instructions for installing and getting started with the SDK\. 

**Note**  
The setup for using the AWS SDK for PHP depends on your environment and how you want to run your application\. To set up your environment to run the examples in this documentation, see the [AWS SDK for PHP Getting Started Guide](http://docs.aws.amazon.com/aws-sdk-php/v2/guide/index.html#getting-started)\.

## AWS SDK for PHP Levels<a name="TheMPphpAPI"></a>

### Low\-Level API<a name="Lowlevel-php-api"></a>

The low\-level APIs correspond to the underlying Amazon S3 REST operations, including the create, update, and delete operations on buckets and objects\. The low\-level APIs provide greater control over these operations\. For example, you can batch your requests and execute them in parallel, or when using the multipart upload API \(see [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)\), you can manage the object parts individually\. Note that these low\-level API calls return a result that includes all the Amazon S3 response details\.

### High\-Level Abstractions<a name="Highlevel-php-api"></a>

 The high\-level abstractions are intended to simplify common use cases\. For example, for uploading large objects using the low\-level API, you must first call `Aws\S3\S3Client::createMultipartUpload()`, then call the `Aws\S3\S3Client::uploadPart()` method to uploads object parts and then call the `Aws\S3\S3Client::completeMultipartUpload()` method to complete the upload\. Instead, you could use the higher\-level `Aws\S3\Model\MultipartUpload\UploadBuilder` object that simplifies creating a multipart upload\.

 Another example of using a higher\-level abstraction is when enumerating objects in a bucket you can use the iterators feature of the AWS SDK for PHP to return all the object keys, regardless of how many objects you have stored in the bucket\. If you use the low\-level API the response returns only up to 1,000 keys and if you have more than a 1,000 objects in the bucket, the result will be truncated and you will have to manage the response and check for any truncation\.

## Running PHP Examples<a name="running-php-samples"></a>

The following procedure describes how to run the PHP code examples in this guide\.


**To Run the PHP Code Examples**  

|  |  | 
| --- |--- |
|  1  | Download and install the [AWS SDK for PHP](https://aws.amazon.com/sdk-for-php/), and then verify that your environment meets the minimum requirements as described in the [AWS SDK for PHP Getting Started Guide](http://docs.aws.amazon.com/aws-sdk-php/v2/guide/index.html#getting-started)\.  | 
|  2  |  Install the AWS SDK for PHP according to the instructions in the [AWS SDK for PHP Getting Started Guide](http://docs.aws.amazon.com/aws-sdk-php/v2/guide/index.html#getting-started)\. Depending on the installation method that you use, you might have to modify your code to resolve dependencies among the PHP extensions\. All of the PHP code examples in this document use the Composer dependency manager that is described in the [AWS SDK for PHP Getting Started Guide](http://docs.aws.amazon.com/aws-sdk-php/v2/guide/index.html#getting-started)\. Each code sample includes the following line to include its dependencies: 

```
require 'vendor/autoload.php';
``` | 
|  3  |  Create a credentials profile for your AWS credentials as described in the AWS SDK for PHP topic [ Using the AWS credentials file and credential profiles](http://docs.aws.amazon.com/aws-sdk-php/guide/latest/credentials.html#using-the-aws-credentials-file-and-credential-profiles)\. At run time, when you create a new Amazon S3 client object, the client will obtain your AWS credentials from the credentials profile\.  | 
|  4  |  Copy the example code from the document to your project\. Depending upon your environment, you might need to add lines to the code example that reference your configuration and SDK files\. For example, to load a PHP example in a browser, add the following to the top of the PHP code, and then save it as a PHP file \(extension `.php`\) in the Web application directory \(such as `www` or `htdocs`\): 

```
<?php
header('Content-Type: text/plain; charset=utf-8');

// Include the AWS SDK using the Composer autoloader
require 'vendor/autoload.php';
```  | 
|  5  |  Test the example according to your setup\.  | 

## Related Resources<a name="RelatedResources-UsingTheMPphpAPI"></a>

+ [AWS SDK for PHP for Amazon S3](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/service-s3.html)

+ [AWS SDK for PHP Documentation](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/index.html)