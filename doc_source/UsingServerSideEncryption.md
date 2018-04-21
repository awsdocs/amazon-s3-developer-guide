# Protecting Data Using Server\-Side Encryption with Amazon S3\-Managed Encryption Keys \(SSE\-S3\)<a name="UsingServerSideEncryption"></a>

Server\-side encryption is about protecting data at rest\. Server\-side encryption with Amazon S3\-managed encryption keys \(SSE\-S3\) employs strong multi\-factor encryption\. Amazon S3 encrypts each object with a unique key\. As an additional safeguard, it encrypts the key itself with a master key that it regularly rotates\. Amazon S3 server\-side encryption uses one of the strongest block ciphers available, 256\-bit Advanced Encryption Standard \(AES\-256\), to encrypt your data\.

Amazon S3 supports bucket policies that you can use if you require server\-side encryption for all objects that are stored in your bucket\. For example, the following bucket policy denies upload object \(`s3:PutObject`\) permission to everyone if the request does not include the `x-amz-server-side-encryption` header requesting server\-side encryption\.

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

Server\-side encryption encrypts only the object data\. Any object metadata is not encrypted\. 

## API Support for Server\-Side Encryption<a name="APISupportforServer-SideEncryption"></a>

The object creation REST APIs \(see [Specifying Server\-Side Encryption Using the REST API](SSEUsingRESTAPI.md)\) provide a request header, `x-amz-server-side-encryption`, which you can use to request server\-side encryption\.

The following Amazon S3 APIs support these headers\.
+ PUT operation — When uploading data using the PUT API \(see [PUT Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html)\), you can specify these request headers\. 
+ Initiate Multipart Upload — When uploading large objects using the multipart upload API, you can specify these headers\. You specify these headers in the initiate request \(see [Initiate Multipart Upload](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html)\)\.
+ POST operation — When using a POST operation to upload an object \(see [POST Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html)\), instead of the request headers, you provide the same information in the form fields\.
+ COPY operation — When you copy an object \(see [PUT Object \- Copy](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html)\), you have both a source object and a target object\.

The AWS SDKs also provide wrapper APIs for you to request server\-side encryption\. You can also use the AWS Management Console to upload objects and request server\-side encryption\.

**Note**  
You can't enforce whether objects are encrypted with SSE\-S3 when they are uploaded using presigned URLs\. This is because the only way you can specify server\-side encryption is through the AWS Management Console or through an HTTP request header\. For more information, see [Specifying Conditions in a Policy](amazon-s3-policy-keys.md)\.