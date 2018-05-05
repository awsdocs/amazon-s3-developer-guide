# Using the AWS SDK for \.NET<a name="UsingTheMPDotNetAPI"></a>

The AWS SDK for \.NET provides the API for the Amazon S3 bucket and object operations\. For object operations, in addition to providing the API to upload objects in a single operation, the SDK provides the API to upload large objects in parts \(see [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)\)\. 

**Topics**
+ [The \.NET API Organization](#DotNetAPIOrg)
+ [Running the Amazon S3 \.NET Code Examples](#TestingDotNetApiSamples)

The AWS SDK for \.NET gives you the option of using a high\-level or low\-level API\. 

**Low\-Level API**  
The low\-level APIs correspond to the underlying Amazon S3 REST operations, including the create, update, and delete operations that apply to buckets and objects\. When you upload large objects using the low\-level multipart upload API \(see [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)\), it provides greater control\. For example, it lets you pause and resume multipart uploads, vary part sizes during the upload, or begin uploads when you don't know the size of the data in advance\. If you do not have these requirements, use the high\-level API for uploading objects\.

**High\-Level API**  
For uploading objects, the SDK provides a higher level of abstraction by providing the `TransferUtility` class\. The high\-level API is a simpler API, where in just a few lines of code, you can upload files and streams to Amazon S3\. You should use this API to upload data unless you need to control the upload as described in the preceding Low\-Level API section\.

For smaller data size, the `TransferUtility` API uploads data in a single operation\. However, the `TransferUtility` switches to using the multipart upload API when the data size reaches a certain threshold\. By default, it uses multiple threads to concurrently upload the parts\. If a part upload fails, the API retries the failed part upload up to three times\. However, these are configurable options\. 

**Note**  
When you're using a stream for the source of data, the `TransferUtility` class does not do concurrent uploads\.

## The \.NET API Organization<a name="DotNetAPIOrg"></a>

When writing Amazon S3 applications using the AWS SDK for \.NET, you use the `AWSSDK.dll`\. The following namespaces in this assembly provide the multipart upload API:
+ **Amazon\.S3\.Transfer—**Provides the high\-level API to upload your data in parts\. 

  It includes the `TransferUtility` class that enables you to specify a file, directory, or stream for uploading your data\. It also includes the `TransferUtilityUploadRequest` and `TransferUtilityUploadDirectoryRequest` classes to configure advanced settings, such as the number of concurrent threads, part size, object metadata, the storage class \(STANDARD, REDUCED\_REDUNDANCY\), and object access control list \(ACL\)\.
+ **Amazon\.S3—**Provides the implementation for the low\-level APIs\. 

  It provides methods that correspond to the Amazon S3 REST multipart upload API \(see [Using the REST API for Multipart Upload](UsingRESTAPImpUpload.md)\)\.
+ **Amazon\.S3\.Model—**Provides the low\-level API classes to create requests and process responses\. For example, it provides the `InitiateMultipartUploadRequest` and `InitiateMultipartUploadResponse` classes you can use when initiating a multipart upload, and the `UploadPartRequest` and `UploadPartResponse` classes when uploading parts\. 
+ **Amazon\.S3\.Encryption—** Provides `AmazonS3EncryptionClient`\.
+ **Amazon\.S3\.Util—** Provides various utility classes such as `AmazonS3Util` and `BucketRegionDetector`\.

For more information about the AWS SDK for \.NET API, see [AWS SDK for \.NET Version 3 API Reference](http://docs.aws.amazon.com/sdkfornet/v3/apidocs/Index.html)\. 

## Running the Amazon S3 \.NET Code Examples<a name="TestingDotNetApiSamples"></a>

The \.NET code examples in this guide are compatible with the AWS SDK for \.NET version 3\.0\. For information about setting up and running the code examples, see [Getting Started with the AWS SDK for \.NET](http://docs.aws.amazon.com/sdk-for-net/v3/developer-guide/net-dg-setup.html) in the *AWS SDK for \.NET Developer Guide*\. 