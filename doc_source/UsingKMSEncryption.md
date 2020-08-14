# Protecting Data Using Server\-Side Encryption with CMKs Stored in AWS Key Management Service \(SSE\-KMS\)<a name="UsingKMSEncryption"></a>

Server\-side encryption is the encryption of data at its destination by the application or service that receives it\. AWS Key Management Service \(AWS KMS\) is a service that combines secure, highly available hardware and software to provide a key management system scaled for the cloud\. Amazon S3 uses AWS KMS customer master keys \(CMKs\) to encrypt your Amazon S3 objects\. AWS KMS encrypts only the object data\. Any object metadata is not encrypted\. 

If you use CMKs, you use AWS KMS via the [AWS Management Console](https://console.aws.amazon.com/kms) or [AWS KMS APIs](https://docs.aws.amazon.com/kms/latest/APIReference/) to centrally create CMKs, define the policies that control how CMKs can be used, and audit CMKs usage to prove that they are being used correctly\. You can use these CMKs to protect your data in Amazon S3 buckets\. When you use SSE\-KMS encryption with an S3 bucket, the AWS KMS CMK must be in the same Region as the bucket\.

There are additional charges for using AWS KMS CMKs\. For more information, see [AWS Key Management Service Concepts \- Customer Master Keys \(CMKs\) ](https://docs.aws.amazon.com/kms/latest/developerguide/concepts.html#master_keys) and [AWS Key Management Service Pricing](https://aws.amazon.com/kms/pricing) in the *AWS Key Management Service Developer Guide*\.

**Note**  
You need the `kms:Decrypt` permission when you upload or download an Amazon S3 object encrypted with an AWS KMS CMK\., This is in addition to the `kms:ReEncrypt`, `kms:GenerateDataKey`, and `kms:DescribeKey` permissions\. For more information, see [Failure to upload a large file to Amazon S3 with encryption using an AWS KMS CMK](https://aws.amazon.com/premiumsupport/knowledge-center/s3-large-file-encryption-kms-key/)\.

## AWS Managed CMKs and Customer Managed CMKs<a name="aws-managed-customer-managed-cmks"></a>

When you use server\-side encryption with AWS KMS \(SSE\-KMS\), you can use the default [AWS managed CMK](https://docs.aws.amazon.com/kms/latest/developerguide/concepts.html#aws-managed-cmk), or you can specify a [customer managed CMK](https://docs.aws.amazon.com/kms/latest/developerguide/concepts.html#customer-cmk) that you have already created\. 

If you do not specify a customer managed CMK, Amazon S3 automatically creates an AWS managed CMK in your AWS account the first time that you add an object encrypted with SSE\-KMS to a bucket\. By default, Amazon S3 uses this CMK for SSE\-KMS\. 

If you want to use a customer managed CMK for SSE\-KMS, you can create the CMK before you configure SSE\-KMS\. Then, when you configure SSE\-KMS for your bucket, you can specify the existing customer managed CMK\. 

Creating your own customer managed CMK gives you more flexibility and control over the CMK\. For example, you can create, rotate, and disable customer managed CMKs\. You can also define access controls and audit the customer managed CMKs that you use to protect your data\. For more information about customer managed and AWS managed CMKs, see [AWS KMS Concepts](https://docs.aws.amazon.com/kms/latest/developerguide/concepts.html) in the *AWS Key Management Service Developer Guide*\.

**Important**  
When you use an AWS KMS CMK for server\-side encryption in Amazon S3, you must choose a symmetric CMK\. Amazon S3 only supports symmetric CMKs and not asymmetric CMKs\. For more information, see [Using Symmetric and Asymmetric Keys](https://docs.aws.amazon.com/kms/latest/developerguide/symmetric-asymmetric.html) in the *AWS Key Management Service Developer Guide*\.

## AWS Signature Version 4<a name="aws-signature-version-4-sse-kms"></a>

If you are uploading or accessing objects encrypted by SSE\-KMS, you need to use AWS Signature Version 4 for added security\. For more information on how to do this using an AWS SDK, see [Specifying Signature Version in Request Authentication](https://docs.aws.amazon.com/AmazonS3/latest/dev/UsingAWSSDK.html#specify-signature-version)\.

**Important**  
All GET and PUT requests for an object protected by AWS KMS will fail if they are not made via SSL or if they are not made using SigV4\.

## SSE\-KMS Highlights<a name="sse-kms-highlights"></a>

The highlights of SSE\-KMS are as follows:
+ You can choose a customer managed CMK that you create and manage, or you can choose an AWS managed CMK that Amazon S3 creates in your AWS account and manages for you\. Like a customer managed CMK, your AWS managed CMK is unique to your AWS account and Region\. Only Amazon S3 has permission to use this CMK on your behalf\. Amazon S3 only supports symmetric CMKs\.
+ You can create, rotate, and disable auditable customer managed CMKs from the AWS KMS console\. 
+ The ETag in the response is not the MD5 of the object data\.
+ The data keys used to encrypt your data are also encrypted and stored alongside the data they protect\. 
+ The security controls in AWS KMS can help you meet encryption\-related compliance requirements\.

## Requiring Server\-Side Encryption<a name="require-sse-kms"></a>

To require server\-side encryption of all objects in a particular Amazon S3 bucket, you can use a policy\. For example, the following bucket policy denies upload object \(`s3:PutObject`\) permission to everyone if the request does not include the `x-amz-server-side-encryption` header requesting server\-side encryption with SSE\-KMS\.

```
 1. {
 2.    "Version":"2012-10-17",
 3.    "Id":"PutObjectPolicy",
 4.    "Statement":[{
 5.          "Sid":"DenyUnEncryptedObjectUploads",
 6.          "Effect":"Deny",
 7.          "Principal":"*",
 8.          "Action":"s3:PutObject",
 9.          "Resource":"arn:aws:s3:::awsexamplebucket1/*",
10.          "Condition":{
11.             "StringNotEquals":{
12.                "s3:x-amz-server-side-encryption":"aws:kms"
13.             }
14.          }
15.       }
16.    ]
17. }
```

To require that a particular AWS KMS CMK be used to encrypt the objects in a bucket, you can use the `s3:x-amz-server-side-encryption-aws-kms-key-id` condition key\. To specify the AWS KMS CMK, you must use a key Amazon Resource Name \(ARN\) that is in the "`arn:aws:kms:region:acct-id:key/key-id"` format\.

**Note**  
When you upload an object, you can specify the AWS KMS CMK using the `x-amz-server-side-encryption-aws-kms-key-id` header\. If the header is not present in the request, Amazon S3 assumes the AWS managed CMK\. Regardless, the AWS KMS key ID that Amazon S3 uses for object encryption must match the AWS KMS key ID in the policy, otherwise Amazon S3 denies the request\.

For a complete list of Amazon S3‐specific condition keys and more information about specifying condition keys, see [Amazon S3 Condition Keys](amazon-s3-policy-keys.md)\.

## Using AWS Key Management Service in the Amazon S3 Console<a name="kms-encryption-s3-console"></a>

For more information about using the Amazon S3 console with CMKs stored in AWS KMS, see [How Do I Add Encryption to an S3 Object?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/add-object-encryption.html) in the *Amazon Simple Storage Service Console User Guide*\.

## API Support for AWS Key Management Service in Amazon S3<a name="APISupportforKMSEncryption"></a>

To request SSE\-KMS in the object creation REST APIs, use the `x-amz-server-side-encryption` request header\. To specify the ID of the AWS KMS CMK that was used for the object, use `x-amz-server-side-encryption-aws-kms-key-id`\. The Amazon S3 API also supports encryption context, with the `x-amz-server-side-encryption-context` header\. For more information, see [Specifying the AWS Key Management Service in Amazon S3 Using the REST API](KMSUsingRESTAPI.md)\. 

The AWS SDKs also provide wrapper APIs for you to request SSE\-KMS with Amazon S3\. For more information, see [Specifying the AWS Key Management Service in Amazon S3 Using the AWS SDKs](kms-using-sdks.md)\.