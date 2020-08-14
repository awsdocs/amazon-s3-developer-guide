# Amazon S3 default encryption for S3 buckets<a name="bucket-encryption"></a>

Amazon S3 default encryption provides a way to set the default encryption behavior for an S3 bucket\. You can set default encryption on a bucket so that all new objects are encrypted when they are stored in the bucket\. The objects are encrypted using server\-side encryption with either Amazon S3\-managed keys \(SSE\-S3\) or customer master keys \(CMKs\) stored in AWS Key Management Service \(AWS KMS\)\. 

When you use server\-side encryption, Amazon S3 encrypts an object before saving it to disk and decrypts it when you download the objects\. For more information about protecting data using server\-side encryption and encryption key management, see [Protecting data using server\-side encryption](serv-side-encryption.md)\.

**Topics**
+ [How do I set up Amazon S3 default encryption for an S3 bucket?](#bucket-encryption-how-to-set-up)
+ [Using encryption for cross\-account operations](#bucket-encryption-update-bucket-policy)
+ [Using default encryption with replication](#bucket-encryption-update-bucket-policy)
+ [Monitoring default encryption with CloudTrail and CloudWatch](#bucket-encryption-tracking)
+ [More Info](#bucket-encryption-related-resources)

## How do I set up Amazon S3 default encryption for an S3 bucket?<a name="bucket-encryption-how-to-set-up"></a>

This section describes how to set up Amazon S3 default encryption\. You can use the AWS SDKs, the Amazon S3 REST API, the AWS Command Line Interface \(AWS CLI\), or the Amazon S3 console to enable the default encryption\. The easiest way to set up default encryption for an S3 bucket is by using the AWS Management Console\.

To set up default encryption on a bucket, you can use any of these methods:
+ Use the Amazon S3 console\. For more information, see [How Do I Enable Default Encryption for an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/default-bucket-encryption.html) in the *Amazon Simple Storage Service Console User Guide*\.
+ Use the REST API [PUT Bucket encryption](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTencryption.html) operation to enable default encryption and set the type of server\-side encryption to use—SSE\-S3 or SSE\-KMS\.
+ Use the AWS CLI and AWS SDKs\. For more information, see [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)\. 

After you enable default encryption for a bucket, the following encryption behavior applies:
+ There is no change to the encryption of the objects that existed in the bucket before default encryption was enabled\. 
+ When you upload objects after enabling default encryption:
  + If your `PUT` request headers don't include encryption information, Amazon S3 uses the bucket’s default encryption settings to encrypt the objects\. 
  + If your `PUT` request headers include encryption information, Amazon S3 uses the encryption information from the `PUT` request to encrypt objects before storing them in Amazon S3\.
+ If you use the SSE\-KMS option for your default encryption configuration, you are subject to the RPS \(requests per second\) limits of AWS KMS\. For more information about AWS KMS limits and how to request a limit increase, see [AWS KMS limits](https://docs.aws.amazon.com/kms/latest/developerguide/limits.html)\. 

To encrypt your existing Amazon S3 objects with a single request, you can use Amazon S3 batch operations\. You provide S3 Batch Operations with a list of objects to operate on, and Batch Operations calls the respective API to perform the specified operation\. You can use the copy operation to copy the existing unencrypted objects and write the new encrypted objects to the same bucket\. A single Batch Operations job can perform the specified operation on billions of objects containing exabytes of data\.

**Note**  
Amazon S3 buckets with default bucket encryption using SSE\-KMS cannot be used as destination buckets for [Amazon S3 server access logging](ServerLogs.md)\. Only SSE\-S3 default encryption is supported for server access log destination buckets\.

## Using encryption for cross\-account operations<a name="bucket-encryption-update-bucket-policy"></a>

Be aware of the following when using encryption for cross\-account operations:
+ The AWS managed CMK \(aws/s3\) is used when a CMK ARN or alias is not provided at request\-time, nor via the bucket's default encryption configuration\.
+ If you're uploading or accessing S3 objects using AWS Identity and Access Management \(IAM\) principals that are in the same AWS account as your CMK, you can use the AWS managed CMK \(aws/s3\)\. 
+ Use a customer managed CMK if you want to grant cross\-account access to your S3 objects\. You can configure the policy of a customer managed CMK to allow access from another account\.
+ If specifying your own CMK, you should use a fully qualified CMK key ARN\. When using a CMK alias, be aware that KMS will resolve the key within the requester’s account\. This may result in data encrypted with a CMK that belongs to the requester, and not the bucket administrator\.
+ You must specify a key that you \(the requester\) has been granted `Encrypt` permission to\. For more information, see [Allows Key Users to Use a CMK for Cryptographic Operations](https://docs.aws.amazon.com/kms/latest/developerguide/key-policies.html#key-policy-users-crypto)\.

For more information about when to use customer managed CMKs and the AWS managed CMK, see [Should I use an AWS AWS KMS\-managed key or a custom AWS AWS KMS key to encrypt my objects on Amazon S3](http://aws.amazon.com/premiumsupport/knowledge-center/s3-object-encrpytion-keys/)\.

## Using default encryption with replication<a name="bucket-encryption-update-bucket-policy"></a>

After you enable default encryption for a replication destination bucket, the following encryption behavior applies: 
+ If objects in the source bucket are not encrypted, the replica objects in the destination bucket are encrypted using the default encryption settings of the destination bucket\. This results in the `ETag` of the source object being different from the `ETag` of the replica object\. You must update applications that use the `ETag` to accommodate for this difference\.
+ If objects in the source bucket are encrypted using SSE\-S3 or SSE\-KMS, the replica objects in the destination bucket use the same encryption as the source object encryption\. The default encryption settings of the destination bucket are not used\.

For more information about using default encryption with SSE\-KMS, see [Replicating encrypted objects](replication-config-for-kms-objects.md)\.

## Monitoring default encryption with CloudTrail and CloudWatch<a name="bucket-encryption-tracking"></a>

You can track default encryption configuration requests through AWS CloudTrail events\. The API event names used in CloudTrail logs are `PutBucketEncryption`, `GetBucketEncryption`, and `DeleteBucketEncryption`\. You can also create Amazon CloudWatch Events with S3 bucket\-level operations as the event type\. For more information about CloudTrail events, see [How Do I Enable Object\-Level Logging for an S3 Bucket with CloudTrail Data Events?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-cloudtrail-events.html)

You can use CloudTrail logs for object\-level Amazon S3 actions to track `PUT` and `POST` requests to Amazon S3 to verify whether default encryption is being used to encrypt objects when incoming `PUT` requests don't have encryption headers\. 

When Amazon S3 encrypts an object using the default encryption settings, the log includes the following field as the name/value pair: `"SSEApplied":"Default_SSE_S3" or "SSEApplied":"Default_SSE_KMS"`\. 

When Amazon S3 encrypts an object using the `PUT` encryption headers, the log includes the following field as the name/value pair: `"SSEApplied":"SSE_S3", "SSEApplied":"SSE_KMS`, or `"SSEApplied":"SSE_C"`\. For multipart uploads, this information is included in the `InitiateMultipartUpload` API requests\. For more information about using CloudTrail and CloudWatch, see [Monitoring Amazon S3](monitoring-overview.md)\.

## More Info<a name="bucket-encryption-related-resources"></a>
+  [PUT Bucket encryption](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTencryption.html) 
+  [DELETE Bucket encryption](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEencryption.html) 
+  [GET Bucket encryption](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETencryption.html) 