# Amazon S3 Default Encryption for S3 Buckets<a name="bucket-encryption"></a>

Amazon S3 default encryption provides a way to set the default encryption behavior for an S3 bucket\. You can set default encryption on a bucket so that all objects are encrypted when they are stored in the bucket\. The objects are encrypted using server\-side encryption with either Amazon S3\-managed keys \(SSE\-S3\) or AWS KMS\-managed keys \(SSE\-KMS\)\. 

When you use server\-side encryption, Amazon S3 encrypts an object before saving it to disk in its data centers and decrypts it when you download the objects\. For more information about protecting data using server\-side encryption and encryption key management, see [Protecting Data Using Encryption](UsingEncryption.md)\.

Default encryption works with all existing and new S3 buckets\. Without default encryption, to encrypt all objects stored in a bucket, you must include encryption information with every object storage request\. You must also set up an S3 bucket policy to reject storage requests that don't include encryption information\. 

There are no new charges for using default encryption for S3 buckets\. Requests to configure the default encryption feature incur standard Amazon S3 request charges\. For information about pricing, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\. For SSE\-KMS encryption key storage, AWS Key Management Service charges apply and are listed at [AWS KMS Pricing](https://aws.amazon.com/kms/pricing/)\. 

**Topics**
+ [How Do I Set Up Amazon S3 Default Encryption for an S3 Bucket?](#bucket-encryption-how-to-set-up)
+ [Moving to Default Encryption from Using Bucket Policies for Encryption Enforcement](#bucket-encryption-update-bucket-policy)
+ [Using Default Encryption with Cross\-Region Replication](#bucket-encryption-update-bucket-policy)
+ [Monitoring Default Encryption with CloudTrail and CloudWatch](#bucket-encryption-tracking)
+ [More Info](#bucket-encryption-related-resources)

## How Do I Set Up Amazon S3 Default Encryption for an S3 Bucket?<a name="bucket-encryption-how-to-set-up"></a>

This section describes how to set up Amazon S3 default encryption\. You can use the AWS SDKs, the Amazon S3 REST API, the AWS Command Line Interface \(AWS CLI\), or the Amazon S3 console to enable the default encryption\. The easiest way to set up default encryption for an S3 bucket is by using the AWS Management Console\.

You can set up default encryption on a bucket using any of the following ways:
+ Use the Amazon S3 console\. For more information, see [How Do I Enable Default Encryption for an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/default-bucket-encryption.html) in the *Amazon Simple Storage Service Console User Guide*\.
+ Use the following REST APIs:
  + Use the REST API [PUT Bucket encryption](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTencryption.html) operation to enable default encryption and to set the type of server\-side encryption to use—SSE\-S3 or SSE\-KMS\.
  + Use the REST API [DELETE Bucket encryption](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEencryption.html) to disable the default encryption of objects\. After you disable default encryption, Amazon S3 encrypts objects only if `PUT` requests include the encryption information\. For more information, see [PUT Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html) and [PUT Object \- Copy](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html)\.
  + Use the REST API [GET Bucket encryption](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETencryption.html) to check the current default encryption configuration\.
+ Use the AWS CLI and AWS SDKs\. For more information, see [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)\. 

After you enable default encryption for a bucket, the following encryption behavior applies:
+ There is no change to the encryption of the objects that existed in the bucket before default encryption was enabled\. 
+ When you upload objects after enabling default encryption:
  + If your `PUT` request headers don't include encryption information, Amazon S3 uses the bucket’s default encryption settings to encrypt the objects\. 
  + If your `PUT` request headers include encryption information, Amazon S3 uses the encryption information from the `PUT` request to encrypt objects before storing them in Amazon S3\. If the `PUT` succeeds, the response is an `HTTP/1.1 200 OK` with the encryption information in the response headers\. For more information, see [PUT Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html)\.
+ If you use the SSE\-KMS option for your default encryption configuration, you are subject to the RPS \(requests per second\) limits of AWS KMS\. For more information about AWS KMS limits and how to request a limit increase, see [AWS KMS limits](http://docs.aws.amazon.com/kms/latest/developerguide/limits.html)\. 

## Moving to Default Encryption from Using Bucket Policies for Encryption Enforcement<a name="bucket-encryption-update-bucket-policy"></a>

If you currently enforce object encryption for an S3 bucket by using a bucket policy to reject `PUT` requests without encryption headers, we recommend that you use the following procedure to start using default encryption\.

**To change from using a bucket policy to reject `PUT` requests without encryption headers to using default encryption**

1. If you plan to specify that default encryption use SSE\-KMS, make sure that all `PUT` and `GET` object requests are signed using Signature Version 4 and sent over an SSL connection to Amazon S3\. For information about using AWS KMS, see [Protecting Data Using Server\-Side Encryption with AWS KMS–Managed Keys \(SSE\-KMS\)](UsingKMSEncryption.md)\. 
**Note**  
By default, the Amazon S3 console, the AWS CLI version 1\.11\.108 and later, and all AWS SDKs released after May 2016 use Signature Version 4 signed requests sent to Amazon S3 over an SSL connection\. 

1. Delete the bucket policy statements that reject `PUT` requests without encryption headers\. \(We recommend that you save a backup copy of the bucket policy that is being replaced\.\)

1. To ensure that the encryption behavior is set as you want, test multiple `PUT` requests to closely simulate your actual workload\. 

1. If you are using default encryption with SSE\-KMS, monitor your clients for failing `PUT` and `GET` requests that weren’t failing before your changes\. Most likely these are the requests that you didn't update according to Step 1\. Change the failing `PUT` or `GET` requests to be signed with AWS Signature Version 4 and sent over SSL\.

After you enable default encryption for your S3 bucket, objects stored in Amazon S3 through any `PUT` requests without encryption headers are encrypted using the bucket\-level default encryption settings\.

## Using Default Encryption with Cross\-Region Replication<a name="bucket-encryption-update-bucket-policy"></a>

After you enable default encryption for a cross\-region replication destination bucket, the following encryption behavior applies: 
+ If objects in the source bucket are not encrypted, the replica objects in the destination bucket are encrypted using the default encryption settings of the destination bucket\. This results in the `ETag` of the source object being different from the `ETag` of the replica object\. You must update applications that use the `ETag` to accommodate for this difference\.
+ If objects in the source bucket are encrypted using SSE\-S3 or SSE\-KMS, the replica objects in the destination bucket use the same encryption as the source object encryption\. The default encryption settings of the destination bucket are not used\.

## Monitoring Default Encryption with CloudTrail and CloudWatch<a name="bucket-encryption-tracking"></a>

You can track default encryption configuration requests through AWS CloudTrail events\. The API event names used in CloudTrail logs are `PutBucketEncryption`, `GetBucketEncryption`, and `DeleteBucketEncryption`\. You can also create Amazon CloudWatch Events with S3 bucket\-level operations as the event type\. For more information about CloudTrail events, see [How Do I Enable Object\-Level Logging for an S3 Bucket with CloudWatch Data Events?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-cloudtrail-events.html)

You can use CloudTrail logs for object\-level Amazon S3 actions to track `PUT` and `POST` requests to Amazon S3 to verify whether default encryption is being used to encrypt objects when incoming `PUT` requests don't have encryption headers\. 

When Amazon S3 encrypts an object using the default encryption settings, the log includes the following field as the name/value pair: `"SSEApplied":"Default_SSE_S3" or "SSEApplied":"Default_SSE_KMS"`\. 

When Amazon S3 encrypts an object using the `PUT` encryption headers, the log includes the following field as the name/value pair: `"SSEApplied":"SSE_S3", "SSEApplied":"SSE_KMS`, or `"SSEApplied":"SSE_C"`\. For multipart uploads, this information is included in the `InitiateMultipartUpload` API requests\. For more information about using CloudTrail and CloudWatch, see [Monitoring Amazon S3](monitoring-overview.md)\.

## More Info<a name="bucket-encryption-related-resources"></a>
+  [PUT Bucket encryption](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTencryption.html) 
+  [DELETE Bucket encryption](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEencryption.html) 
+  [GET Bucket encryption](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETencryption.html) 