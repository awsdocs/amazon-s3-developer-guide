# Specifying the AWS Key Management Service in Amazon S3 Using the REST API<a name="KMSUsingRESTAPI"></a>

When you create an object—that is, when you upload a new object or copy an existing object—you can specify the use of server\-side encryption with AWS Key Management Service \(AWS KMS\) customer master keys \(CMKs\) to encrypt your data\. To do this, add the `x-amz-server-side-encryption` header to the request\. Set the value of the header to the encryption algorithm `aws:kms`\. Amazon S3 confirms that your object is stored using SSE\-KMS by returning the response header `x-amz-server-side-encryption`\. 

If you specify the `x-amz-server-side-encryption` header with a value of `aws:kms`, you can also use the following request headers:
+ `x-amz-server-side-encryption-aws-kms-key-id`
+ `x-amz-server-side-encryption-context`

## Amazon S3 REST APIs that Support SSE\-KMS<a name="sse-request-headers-kms"></a>

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

## Encryption Context \(`x-amz-server-side-encryption-context`\)<a name="s3-kms-encryption-context"></a>

If you specify `x-amz-server-side-encryption:aws:kms`, the Amazon S3 API supports an encryption context with the `x-amz-server-side-encryption-context` header\. An encryption context is an optional set of key\-value pairs that can contain additional contextual information about the data\. For more information about encryption context, see [AWS Key Management Service Concepts \- Encryption Context](https://docs.aws.amazon.com/kms/latest/developerguide/concepts.html#encrypt_context) in the *AWS Key Management Service Developer Guide*\. 

The encryption context can be any value that you want, provided that the header adheres to the Base64\-encoded JSON format\. However, because the encryption context is not encrypted and because it is logged if AWS CloudTrail logging is turned on, the encryption context should not include sensitive information\. We further recommend that your context describe the data being encrypted or decrypted so that you can better understand the CloudTrail events produced by AWS KMS\. 

Also, Amazon S3 might append a predefined key of `aws:s3:arn` with the value equal to the object's Amazon Resource Name \(ARN\) for the encryption context that you provide\. This only happens if the key `aws:s3:arn` is not already in the encryption context that you provided\. In this case, this predefined key is appended when Amazon S3 processes your Put requests\. If this `aws:s3:arn` key is already present in your encryption context, the key is not appended a second time to your encryption context\.

Having this predefined key as a part of your encryption context means that you can track relevant requests in CloudTrail\. So you can always see which Amazon S3 object's ARN was used with which encryption key\. In addition, this predefined key as a part of your encryption context helps ensure that the encryption context is not identical between different Amazon S3 objects, which provides additional security for your objects\. Your full encryption context will be validated to have the value equal to the object's ARN\.

## AWS KMS Key ID \(`x-amz-server-side-encryption-aws-kms-key-id`\)<a name="s3-kms-key-id-api"></a>

You can use the `x-amz-server-side-encryption-aws-kms-key-id` header to specify the ID of the customer managed CMK used to protect the data\. If you specify `x-amz-server-side-encryption:aws:kms`, but don't provide `x-amz-server-side-encryption-aws-kms-key-id`, Amazon S3 uses the AWS managed CMK in AWS KMS to protect the data\. If you want to use a customer managed AWS KMS CMK, you must provide the `x-amz-server-side-encryption-aws-kms-key-id` of the customer managed CMK\.

**Important**  
When you use an AWS KMS CMK for server\-side encryption in Amazon S3, you must choose a symmetric CMK\. Amazon S3 only supports symmetric CMKs and not asymmetric CMKs\. For more information, see [Using Symmetric and Asymmetric Keys](https://docs.aws.amazon.com/kms/latest/developerguide/symmetric-asymmetric.html) in the *AWS Key Management Service Developer Guide*\.