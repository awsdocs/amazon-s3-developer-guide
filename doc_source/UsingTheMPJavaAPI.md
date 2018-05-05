# Using the AWS SDK for Java<a name="UsingTheMPJavaAPI"></a>

The AWS SDK for Java provides an API for the Amazon S3 bucket and object operations\. For object operations, in addition to providing the API to upload objects in a single operation, the SDK provides an API to upload large objects in parts\. For more information, see [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)\. 

**Topics**
+ [The Java API Organization](#JavaAPIOrganization)
+ [Testing the Amazon S3 Java Code Examples](#TestingJavaSamples)

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
+ **com\.amazonaws\.services\.s3—**Provides the APIs for creating Amazon S3 clients and working with buckets and objects\. For example, it enables you to create buckets, upload objects, get objects, delete objects, and list keys\. 
+ **com\.amazonaws\.services\.s3\.transfer—**Provides the high\-level API data operations\.

  This high\-level API is designed to simplify transferring objects to and from Amazon S3\. It includes the `TransferManager` class, which provides asynchronous methods for working with, querying, and manipulating transfers\. It also includes the `TransferManagerConfiguration` class, which you can use to configure the minimum part size for uploading parts and the threshold in bytes of when to use multipart uploads\.
+ **com\.amazonaws\.services\.s3\.model—**Provides the low\-level API classes to create requests and process responses\. For example, it includes the `GetObjectRequest` class to describe your get object request, the `ListObjectsRequest` class to describe your list keys requests, and the `InitiateMultipartUploadRequest` class to create multipart uploads\. 

For more information about the AWS SDK for Java API, see [AWS SDK for Java API Reference](http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/)\.

## Testing the Amazon S3 Java Code Examples<a name="TestingJavaSamples"></a>

The Java examples in this guide are compatible with the AWS SDK for Java version 1\.11\.321\. For instructions on setting up and running code samples, see [Getting Started with the AWS SDK for Java](http://docs.aws.amazon.com/AWSSdkDocsJava/latest/DeveloperGuide/java-dg-setup.html) in the AWS SDK for Java Developer Guide\. 