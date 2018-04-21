# Protecting Data Using Server\-Side Encryption with AWS KMS–Managed Keys \(SSE\-KMS\)<a name="UsingKMSEncryption"></a>

Server\-side encryption is about protecting data at rest\. AWS Key Management Service \(AWS KMS\) is a service that combines secure, highly available hardware and software to provide a key management system scaled for the cloud\. AWS KMS uses customer master keys \(CMKs\) to encrypt your Amazon S3 objects\. You use AWS KMS via the Encryption Keys section in the IAM console or via AWS KMS APIs to centrally create encryption keys, define the policies that control how keys can be used, and audit key usage to prove they are being used correctly\. You can use these keys to protect your data in Amazon S3 buckets\.

The first time you add an SSE\-KMS–encrypted object to a bucket in a region, a default CMK is created for you automatically\. This key is used for SSE\-KMS encryption unless you select a CMK that you created separately using AWS Key Management Service\. Creating your own CMK gives you more flexibility, including the ability to create, rotate, disable, and define access controls, and to audit the encryption keys used to protect your data\.

For more information, see [What is AWS Key Management Service?](http://docs.aws.amazon.com/kms/latest/developerguide/overview.html) in the *AWS Key Management Service Developer Guide*\. If you use AWS KMS, there are additional charges for using AWS\-KMS keys\. For more information, see [AWS Key Management Service Pricing](https://aws.amazon.com/kms/pricing)\.

**Note**  
If you are uploading or accessing objects encrypted by SSE\-KMS, you need to use AWS Signature Version 4 for added security\. For more information on how to do this using an AWS SDK, see [Specifying Signature Version in Request Authentication](http://docs.aws.amazon.com/AmazonS3/latest/dev/UsingAWSSDK.html#specify-signature-version)\.

The highlights of SSE\-KMS are:
+ You can choose to create and manage encryption keys yourself, or you can choose to use your default service key uniquely generated on a customer by service by region level\. 
+ The ETag in the response is not the MD5 of the object data\.
+ The data keys used to encrypt your data are also encrypted and stored alongside the data they protect\. 
+ Auditable master keys can be created, rotated, and disabled from the IAM console\. 
+ The security controls in AWS KMS can help you meet encryption\-related compliance requirements\.

Amazon S3 supports bucket policies that you can use if you require server\-side encryption for all objects that are stored in your bucket\. For example, the following bucket policy denies upload object \(`s3:PutObject`\) permission to everyone if the request does not include the `x-amz-server-side-encryption` header requesting server\-side encryption with SSE\-KMS\.

```
 1. {
 2.    "Version":"2012-10-17",
 3.    "Id":"PutObjPolicy",
 4.    "Statement":[{
 5.          "Sid":"DenyUnEncryptedObjectUploads",
 6.          "Effect":"Deny",
 7.          "Principal":"*",
 8.          "Action":"s3:PutObject",
 9.          "Resource":"arn:aws:s3:::YourBucket/*",
10.          "Condition":{
11.             "StringNotEquals":{
12.                "s3:x-amz-server-side-encryption":"aws:kms"
13.             }
14.          }
15.       }
16.    ]
17. }
```

Amazon S3 also supports the `s3:x-amz-server-side-encryption-aws-kms-key-id` condition key, which you can use to require a specific KMS key for object encryption\. The KMS key you specify in the policy must use the "`arn:aws:kms:region:acct-id:key/key-id" ` format\.

**Note**  
When you upload an object, you can specify the KMS key using the `x-amz-server-side-encryption-aws-kms-key-id` header\. If the header is not present in the request, Amazon S3 assumes the default KMS key\. Regardless, the KMS key ID that Amazon S3 uses for object encryption must match the KMS key ID in the policy, otherwise Amazon S3 denies the request\.

**Important**  
All GET and PUT requests for an object protected by AWS KMS will fail if they are not made via SSL or by using SigV4\. 

SSE\-KMS encrypts only the object data\. Any object metadata is not encrypted\.

## Using AWS Key Management Service in the Amazon S3 Management Console<a name="kms-encryption-s3-console"></a>

For more information about using KMS\-Managed Encryption Keys in the Amazon S3 Management Console, see [Uploading S3 Objects](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/upload-objects.html) in the *Amazon Simple Storage Service User Guide*\.

## API Support for AWS Key Management Service in Amazon S3<a name="APISupportforKMSEncryption"></a>

The object creation REST APIs \(see [Specifying the AWS Key Management Service in Amazon S3 Using the REST API](KMSUsingRESTAPI.md)\) provide a request header, `x-amz-server-side-encryption` that you can use to request SSE\-KMS with the value of `aws:kms`\. There's also `x-amz-server-side-encryption-aws-kms-key-id`, which specifies the ID of the AWS KMS master encryption key that was used for the object\. The Amazon S3 API also supports encryption context, with the `x-amz-server-side-encryption-context` header\.

The encryption context can be any value that you want, provided that the header adheres to the Base64\-encoded JSON format\. However, because the encryption context is not encrypted and because it is logged if AWS CloudTrail logging is turned on, the encryption context should not include sensitive information\. We further recommend that your context describe the data being encrypted or decrypted so that you can better understand the CloudTrail events produced by AWS KMS\. For more information, see [Encryption Context](http://docs.aws.amazon.com/kms/latest/developerguide/encryption-context.html) in the *AWS Key Management Service Developer Guide*\.

Also, Amazon S3 may append a predefined key of aws:s3:arn with the value equal to the object's ARN for the encryption context that you provide\. This only happens if the key aws:s3:arn is not already in the encryption context that you provided, in which case this predefined key is appended when Amazon S3 processes your Put requests\. If this aws:s3:arn key is already present in your encryption context, the key is not appended a second time to your encryption context\.

Having this predefined key as a part of your encryption context means that you can track relevant requests in CloudTrail, so you’ll always be able to see which S3 object's ARN was used with which encryption key\. In addition, this predefined key as a part of your encryption context guarantees that the encryption context is not identical between different S3 objects, which provides additional security for your objects\. Your full encryption context will be validated to have the value equal to the object's ARN\.

The following Amazon S3 APIs support these request headers\.
+ PUT operation — When uploading data using the PUT API \(see [PUT Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html)\), you can specify these request headers\. 
+ Initiate Multipart Upload — When uploading large objects using the multipart upload API, you can specify these headers\. You specify these headers in the initiate request \(see [Initiate Multipart Upload](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html)\)\.
+ POST operation — When using a POST operation to upload an object \(see [POST Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html)\), instead of the request headers, you provide the same information in the form fields\.
+ COPY operation — When you copy an object \(see [PUT Object \- Copy](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html)\), you have both a source object and a target object\. When you pass SSE\-KMS headers with the COPY operation, they will be applied only to the target object\.

The AWS SDKs also provide wrapper APIs for you to request SSE\-KMS with Amazon S3\. 