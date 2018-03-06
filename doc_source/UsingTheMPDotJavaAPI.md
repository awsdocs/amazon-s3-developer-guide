# Using the AWS SDK for Java<a name="UsingTheMPDotJavaAPI"></a>

The AWS SDK for Java provides an API for the Amazon S3 bucket and object operations\. For object operations, in addition to providing the API to upload objects in a single operation, the SDK provides an API to upload large objects in parts\. For more information, see [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)\. 


+ [The Java API Organization](#JavaAPIOrganization)
+ [Testing the Java Code Examples](#TestingJavaSamples)

The AWS SDK for Java gives you the option of using a high\-level or low\-level API\. 

**Low\-Level API**  
The low\-level APIs correspond to the underlying Amazon S3 REST operations, such as create, update, and delete operations that apply to buckets and objects\. When you upload large objects using the low\-level multipart upload API, it provides greater control\. For example, it lets you pause and resume multipart uploads, vary part sizes during the upload, or begin uploads when you don't know the size of the data in advance\. If you don't have these requirements, use the high\-level API to upload objects\. 

**High\-Level API**  
For uploading objects, the SDK provides a higher level of abstraction by providing the `TransferManager` class\. The high\-level API is a simpler API, where in just a few lines of code you can upload files and streams to Amazon S3\. You should use this API to upload data unless you need to control the upload as described in the preceding Low\-Level API section\.

For smaller data size, the `TransferManager` API uploads data in a single operation\. However, the `TransferManager` switches to using the multipart upload API when the data size reaches a certain threshold\. When possible, the `TransferManager` uses multiple threads to concurrently upload the parts\. If a part upload fails, the API retries the failed part upload up to three times\. However, these are configurable options using the `TransferManagerConfiguration` class\. 

**Note**  
When you're using a stream for the source of data, the `TransferManager` class does not do concurrent uploads\.

## The Java API Organization<a name="JavaAPIOrganization"></a>

The following packages in the AWS SDK for Java provide the API:

+ **com\.amazonaws\.services\.s3—**Provides the implementation APIs for Amazon S3 bucket and object operations\. 

  For example, it provides methods to create buckets, upload objects, get objects, delete objects, and list keys\. 

+ **com\.amazonaws\.services\.s3\.transfer—**Provides the high\-level API data upload\.

  This high\-level API is designed to further simplify uploading objects to Amazon S3\. It includes the `TransferManager` class\. It is particularly useful when uploading large objects in parts\. It also includes the `TransferManagerConfiguration` class, which you can use to configure the minimum part size for uploading parts and the threshold in bytes of when to use multipart uploads\.

+ **com\.amazonaws\.services\.s3\.model—**Provides the low\-level API classes to create requests and process responses\.

  For example, it includes the `GetObjectRequest` class to describe your get object request, the `ListObjectRequest` class to describe your list keys requests, and the `InitiateMultipartUploadRequest` and `InitiateMultipartUploadResult` classes when initiating a multipart upload\. 

For more information about the AWS SDK for Java API, see [AWS SDK for Java API Reference](http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/)\.

## Testing the Java Code Examples<a name="TestingJavaSamples"></a>

The easiest way to get started with the Java code examples is to install the latest AWS Toolkit for Eclipse\. For information about setting up your Java development environment and the AWS Toolkit for Eclipse, see [Installing the AWS SDK for Java](http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/java-dg-install-sdk.html) in the *AWS SDK for Java Developer Guide*\.

The following tasks guide you through the creation and testing of the Java code examples provided in this guide\.


**General Process of Creating Java Code Examples**  

|  |  | 
| --- |--- |
|  1  |  Create an AWS credentials profile file as described in [Set Up your AWS Credentials for Use with the AWS SDK for Java](http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/set-up-creds.html) in the *AWS SDK for Java Developer Guide*\.   | 
|  2  |  Create a new AWS Java project in Eclipse\. The project is pre\-configured with the AWS SDK for Java\.  | 
|  3  |  Copy the code from the section you are reading to your project\.   | 
|  4  | Update the code by providing any required data\. For example, if uploading a file, provide the file path and the bucket name\. | 
| 5 | Run the code\. Verify that the object is created by using the AWS Management Console\. For more information about the AWS Management Console, see [https://aws\.amazon\.com/console/](https://aws.amazon.com/console/)\. | 