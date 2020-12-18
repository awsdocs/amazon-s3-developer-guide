# Specifying AWS KMS in Amazon S3 using the REST API<a name="KMSUsingRESTAPI"></a>

When you create an object—that is, when you upload a new object or copy an existing object—you can specify the use of server\-side encryption with AWS Key Management Service \(AWS KMS\) customer master keys \(CMKs\) to encrypt your data\. To do this, add the `x-amz-server-side-encryption` header to the request\. Set the value of the header to the encryption algorithm `aws:kms`\. Amazon S3 confirms that your object is stored using SSE\-KMS by returning the response header `x-amz-server-side-encryption`\. 

If you specify the `x-amz-server-side-encryption` header with a value of `aws:kms`, you can also use the following request headers:
+ `x-amz-server-side-encryption-aws-kms-key-id`
+ `x-amz-server-side-encryption-context`

**Topics**
+ [Amazon S3 REST APIs that support SSE\-KMS](#sse-request-headers-kms)
+ [Encryption context \(x\-amz\-server\-side\-encryption\-context\)](#s3-kms-encryption-context)
+ [AWS KMS key ID \(x\-amz\-server\-side\-encryption\-aws\-kms\-key\-id\)](#s3-kms-key-id-api)
+ [S3 Bucket Keys \(x\-amz\-server\-side\-encryption\-aws\-bucket\-key\-enabled\)](#bucket-key-api)

## Amazon S3 REST APIs that support SSE\-KMS<a name="sse-request-headers-kms"></a>

The following REST APIs accept the `x-amz-server-side-encryption`, `x-amz-server-side-encryption-aws-kms-key-id`, and `x-amz-server-side-encryption-context` request headers\.
+ [PUT Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html) — When you upload data using the PUT API, you can specify these request headers\. 
+ [PUT Object \- Copy](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html)— When you copy an object, you have both a source object and a target object\. When you pass SSE\-KMS headers with the COPY operation, they are applied only to the target object\. When copying an existing object, regardless of whether the source object is encrypted or not, the destination object is not encrypted unless you explicitly request server\-side encryption\.
+ [POST Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html)— When you use a POST operation to upload an object, instead of the request headers, you provide the same information in the form fields\.
+ [Initiate Multipart Upload](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html)— When you upload large objects using the multipart upload API, you can specify these headers\. You specify these headers in the initiate request\.

The response headers of the following REST APIs return the `x-amz-server-side-encryption` header when an object is stored using server\-side encryption\.
+ [PUT Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html)
+ [PUT Object \- Copy](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html)
+ [POST Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html)
+ [Initiate Multipart Upload](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html)
+ [Upload Part](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPart.html)
+ [Upload Part \- Copy](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPartCopy.html)
+ [Complete Multipart Upload](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadComplete.html)
+ [Get Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html)
+ [Head Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectHEAD.html)

**Important**  
All GET and PUT requests for an object protected by AWS KMS will fail if you don't make them using Secure Sockets Language \(SSL\) or Signature Version 4\.
If your object uses SSE\-KMS, don't send encryption request headers for `GET` requests and `HEAD` requests, or you’ll get an HTTP 400 BadRequest error\.

## Encryption context \(x\-amz\-server\-side\-encryption\-context\)<a name="s3-kms-encryption-context"></a>

If you specify `x-amz-server-side-encryption:aws:kms`, the Amazon S3 API supports an encryption context with the `x-amz-server-side-encryption-context` header\. An encryption context is an optional set of key\-value pairs that can contain additional contextual information about the data\. For more information about encryption context, see [AWS Key Management Service Concepts \- Encryption Context](https://docs.aws.amazon.com/kms/latest/developerguide/concepts.html#encrypt_context) in the *AWS Key Management Service Developer Guide*\. 

The encryption context can be any value that you want, provided that the header adheres to the Base64\-encoded JSON format\. However, because the encryption context is not encrypted and because it is logged if AWS CloudTrail logging is turned on, the encryption context should not include sensitive information\. We further recommend that your context describe the data being encrypted or decrypted so that you can better understand the CloudTrail events produced by AWS KMS\.

In Amazon S3, the object or bucket Amazon Resource Name \(ARN\) is commonly used as an encryption context\. If you use SSE\-KMS without enabling an S3 Bucket Key, you use the object ARN as your encryption context, for example, `arn:aws:s3:::object_ARN`\. However, if you use SSE\-KMS and enable an S3 Bucket Key, you use the bucket ARN for your encryption context, for example, `arn:aws:s3:::bucket_ARN`\. For more information about S3 Bucket Keys, see [Reducing the cost of SSE\-KMS with Amazon S3 Bucket Keys](bucket-key.md)\.

If the key `aws:s3:arn` is not already in the encryption context, Amazon S3 can append a predefined key of `aws:s3:arn` to the encryption context that you provide\. Amazon S3 appends this predefined key when it processes your requests\. If you use SSE\-KMS without an S3 Bucket Key, the value is equal to the object ARN\. If you use SSE\-KMS with an S3 Bucket Key enabled, the value is equal to the bucket ARN\. 

You can use this predefined key to track relevant requests in CloudTrail\. So you can always see which Amazon S3 ARN was used with which encryption key\. You can use CloudTrail logs to ensure that the encryption context is not identical between different Amazon S3 objects and buckets, which provides additional security\. Your full encryption context will be validated to have the value equal to the object or bucket ARN\. 

## AWS KMS key ID \(x\-amz\-server\-side\-encryption\-aws\-kms\-key\-id\)<a name="s3-kms-key-id-api"></a>

You can use the `x-amz-server-side-encryption-aws-kms-key-id` header to specify the ID of the customer managed CMK used to protect the data\. If you specify `x-amz-server-side-encryption:aws:kms`, but don't provide `x-amz-server-side-encryption-aws-kms-key-id`, Amazon S3 uses the AWS managed CMK in AWS KMS to protect the data\. If you want to use a customer managed AWS KMS CMK, you must provide the `x-amz-server-side-encryption-aws-kms-key-id` of the customer managed CMK\.

**Important**  
When you use an AWS KMS CMK for server\-side encryption in Amazon S3, you must choose a symmetric CMK\. Amazon S3 only supports symmetric CMKs and not asymmetric CMKs\. For more information, see [Using Symmetric and Asymmetric Keys](https://docs.aws.amazon.com/kms/latest/developerguide/symmetric-asymmetric.html) in the *AWS Key Management Service Developer Guide*\.

## S3 Bucket Keys \(x\-amz\-server\-side\-encryption\-aws\-bucket\-key\-enabled\)<a name="bucket-key-api"></a>

You can use the `x-amz-server-side-encryption-aws-bucket-key-enabled` request header to enable or disable an S3 Bucket Key at the object\-level\. S3 Bucket Keys can reduce your AWS KMS request costs by decreasing the request traffic from Amazon S3 to AWS KMS\. For more information, see [Reducing the cost of SSE\-KMS with Amazon S3 Bucket Keys](bucket-key.md)\.

If you specify `x-amz-server-side-encryption:aws:kms`, but don't provide `x-amz-server-side-encryption-aws-bucket-key-enabled`, your object uses the S3 Bucket Key settings for the destination bucket to encrypt your object\. For more information, see [Configuring an S3 Bucket Key at the object level using the REST API, AWS SDKs, or AWS CLI](configuring-bucket-key-object.md)\.