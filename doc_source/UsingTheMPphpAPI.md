# Using the AWS SDK for PHP and Running PHP Examples<a name="UsingTheMPphpAPI"></a>

The AWS SDK for PHP provides access to the API for Amazon S3 bucket and object operations\. The SDK gives you the option of using the service's low\-level API or using higher\-level abstractions\.

The SDK is available at [AWS SDK for PHP](https://aws.amazon.com/sdk-for-php/), which also has instructions for installing and getting started with the SDK\. 

The setup for using the AWS SDK for PHP depends on your environment and how you want to run your application\. To set up your environment to run the examples in this documentation, see the [AWS SDK for PHP Getting Started Guide](http://docs.aws.amazon.com/aws-sdk-php/v2/guide/index.html#getting-started)\.

**Topics**
+ [AWS SDK for PHP Levels](#TheMPphpAPI)
+ [Running PHP Examples](#running-php-samples)
+ [Related Resources](#RelatedResources-UsingTheMPphpAPI)

## AWS SDK for PHP Levels<a name="TheMPphpAPI"></a>

The AWS SDK for PHP gives you the option of using a high\-level or low\-level API\. 

### Low\-Level API<a name="Lowlevel-php-api"></a>

The low\-level APIs correspond to the underlying Amazon S3 REST operations, including the create, update, and delete operations on buckets and objects\. The low\-level APIs provide greater control over these operations\. For example, you can batch your requests and execute them in parallel\. Or, when using the multipart upload API, you can manage the object parts individually\. Note that these low\-level API calls return a result that includes all of the Amazon S3 response details\. For more information about the multipart upload API, see [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)\.

### High\-Level Abstractions<a name="Highlevel-php-api"></a>

The high\-level abstractions are intended to simplify common use cases\. For example, for uploading large objects using the low\-level API, you call `Aws\S3\S3Client::createMultipartUpload()`, call the `Aws\S3\S3Client::uploadPart()` method to upload the object parts, then call the `Aws\S3\S3Client::completeMultipartUpload()` method to complete the upload\. You can use the higher\-level `Aws\S3\\MultipartUploader` object that simplifies creating a multipart upload instead\.

As another example, when enumerating objects in a bucket, you can use the iterators feature of the AWS SDK for PHP to return all of the object keys, regardless of how many objects you have stored in the bucket\. If you use the low\-level API, the response returns a maximum of 1,000 keys\. If a bucket contains more than 1,000 objects, the result is truncated and you have to manage the response and check for truncation\.

## Running PHP Examples<a name="running-php-samples"></a>

To set up and use the Amazon S3 samples for version 3 of the AWS SDK for PHP, see [Installation](http://docs.aws.amazon.com/aws-sdk-php/guide/latest/getting-started/installation.html) in the AWS SDK for PHP Developer Guide\.

## Related Resources<a name="RelatedResources-UsingTheMPphpAPI"></a>
+ [AWS SDK for PHP for Amazon S3](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/service-s3.html)
+ [AWS SDK for PHP Documentation](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/index.html)