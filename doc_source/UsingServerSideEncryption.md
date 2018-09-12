# Protecting Data Using Server\-Side Encryption with Amazon S3\-Managed Encryption Keys \(SSE\-S3\)<a name="UsingServerSideEncryption"></a>

Server\-side encryption protects data at rest\. Server\-side encryption with Amazon S3\-managed encryption keys \(SSE\-S3\) uses strong multi\-factor encryption\. Amazon S3 encrypts each object with a unique key\. As an additional safeguard, it encrypts the key itself with a master key that it rotates regularly\. Amazon S3 server\-side encryption uses one of the strongest block ciphers available, 256\-bit Advanced Encryption Standard \(AES\-256\), to encrypt your data\.

If you need server\-side encryption for all of the objects that are stored in a bucket, use a bucket policy\. For example, the following bucket policy denies permissions to upload an object unless the request includes the `x-amz-server-side-encryption` header to request server\-side encryption:

```
 1. {
 2.   "Version": "2012-10-17",
 3.   "Id": "PutObjPolicy",
 4.   "Statement": [
 5.     {
 6.       "Sid": "DenyIncorrectEncryptionHeader",
 7.       "Effect": "Deny",
 8.       "Principal": "*",
 9.       "Action": "s3:PutObject",
10.       "Resource": "arn:aws:s3:::YourBucket/*",
11.       "Condition": {
12.         "StringNotEquals": {
13.           "s3:x-amz-server-side-encryption": "AES256"
14.         }
15.       }
16.     },
17.     {
18.       "Sid": "DenyUnEncryptedObjectUploads",
19.       "Effect": "Deny",
20.       "Principal": "*",
21.       "Action": "s3:PutObject",
22.       "Resource": "arn:aws:s3:::YourBucket/*",
23.       "Condition": {
24.         "Null": {
25.           "s3:x-amz-server-side-encryption": "true"
26.         }
27.       }
28.     }
29.   ]
30. }
```

Server\-side encryption encrypts only the object data, not object metadata\. 

## API Support for Server\-Side Encryption<a name="APISupportforServer-SideEncryption"></a>

To request server\-side encryption using the object creation REST APIs, provide the , `x-amz-server-side-encryption` request header\. For information about the REST APIs, see [Specifying Server\-Side Encryption Using the REST API](SSEUsingRESTAPI.md)\.

The following Amazon S3 APIs support this header:
+ PUT operations—Specify the request header when uploading data using the PUT API\. For more information, see [PUT Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html)\.
+ Initiate Multipart Upload—Specify the header in the initiate request when uploading large objects using the multipart upload API \. For more information, see [Initiate Multipart Upload](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html)\.
+ COPY operations—When you copy an object, you have both a source object and a target object\. For more information, see [PUT Object \- Copy](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html)\.

**Note**  
When using a POST operation to upload an object, instead of providing the request header, you provide the same information in the form fields\. For more information, see [POST Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html)\. 

The AWS SDKs also provide wrapper APIs that you can use to request server\-side encryption\. You can also use the AWS Management Console to upload objects and request server\-side encryption\.

**Note**  
You can't enforce SSE\-S3 encryption on  objects that are uploaded using presigned URLs\. You can specify server\-side encryption only with the AWS Management Console or an HTTP request header\. For more information, see [Specifying Conditions in a Policy](amazon-s3-policy-keys.md)\.