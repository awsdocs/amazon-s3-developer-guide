# Specifying server\-side encryption with customer\-provided encryption keys using the REST API<a name="ServerSideEncryptionCustomerKeysSSEUsingRESTAPI"></a>

At the time of object creation with the REST API, you can specify server\-side encryption with customer\-provided encryption keys \(SSE\-C\)\. When you use SSE\-C, you must provide encryption key information using the following request headers\. 


|  Name  |  Description  | 
| --- | --- | 
| x\-amz\-server\-side​\-encryption​\-customer\-algorithm  |  Use this header to specify the encryption algorithm\. The header value must be "AES256"\.   | 
| x\-amz\-server\-side​\-encryption​\-customer\-key  |  Use this header to provide the 256\-bit, base64\-encoded encryption key for Amazon S3 to use to encrypt or decrypt your data\.   | 
| x\-amz\-server\-side​\-encryption​\-customer\-key\-MD5  |  Use this header to provide the base64\-encoded 128\-bit MD5 digest of the encryption key according to [RFC 1321](http://tools.ietf.org/html/rfc1321)\. Amazon S3 uses this header for a message integrity check to ensure that the encryption key was transmitted without error\.  | 

You can use AWS SDK wrapper libraries to add these headers to your request\. If you need to, you can make the Amazon S3 REST API calls directly in your application\. 

**Note**  
You cannot use the Amazon S3 console to upload an object and request SSE\-C\. You also cannot use the console to update \(for example, change the storage class or add metadata\) an existing object stored using SSE\-C\.

## Amazon S3 rest APIs that support SSE\-C<a name="sse-c-supported-apis"></a>

The following Amazon S3 APIs support server\-side encryption with customer\-provided encryption keys \(SSE\-C\)\.
+ **GET operation** — When retrieving objects using the GET API \(see [GET Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html)\), you can specify the request headers\. Torrents are not supported for objects encrypted using SSE\-C\.
+ **HEAD operation** — To retrieve object metadata using the HEAD API \(see [HEAD Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectHEAD.html)\), you can specify these request headers\.
+ **PUT operation** — When uploading data using the PUT Object API \(see [PUT Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html)\), you can specify these request headers\. 
+ **Multipart Upload **— When uploading large objects using the multipart upload API, you can specify these headers\. You specify these headers in the initiate request \(see [Initiate Multipart Upload](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html)\) and each subsequent part upload request \(see [Upload Part](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPart.html) or 

  [Upload Part \- Copy](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPartCopy.html)\)

  \)\. For each part upload request, the encryption information must be the same as what you provided in the initiate multipart upload request\.
+ **POST operation **— When using a POST operation to upload an object \(see [POST Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html)\), instead of the request headers, you provide the same information in the form fields\.
+ **Copy operation **— When you copy an object \(see [PUT Object \- Copy](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html)\), you have both a source object and a target object:
  + If you want the target object encrypted using server\-side encryption with AWS managed keys, you must provide the `x-amz-server-side​-encryption` request header\.
  +  If you want the target object encrypted using SSE\-C, you must provide encryption information using the three headers described in the preceding table\.
  +  If the source object is encrypted using SSE\-C, you must provide encryption key information using the following headers so that Amazon S3 can decrypt the object for copying\.    
[\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/ServerSideEncryptionCustomerKeysSSEUsingRESTAPI.html)

## Presigned URLs and SSE\-C<a name="ssec-and-presignedurl"></a>

You can generate a presigned URL that can be used for operations such as upload a new object, retrieve an existing object, or object metadata\. Presigned URLs support the SSE\-C as follows:
+ When creating a presigned URL, you must specify the algorithm using the `x-amz-server-side​-encryption​-customer-algorithm` in the signature calculation\.
+ When using the presigned URL to upload a new object, retrieve an existing object, or retrieve only object metadata, you must provide all the encryption headers in your client application\. 
**Note**  
For non\-SSE\-C objects, you can generate a presigned URL and directly paste that into a browser, for example to access the data\.   
However, this is not true for SSE\-C objects because in addition to the presigned URL, you also need to include HTTP headers that are specific to SSE\-C objects\. Therefore, you can use the presigned URL for SSE\-C objects only programmatically\.

## More info<a name="sse-c-more-info"></a>
+ [Specifying server\-side encryption with customer\-provided encryption keys using the AWS SDK for \.NET](sse-c-using-dot-net-sdk.md)
+ [Specifying server\-side encryption with customer\-provided encryption keys using the AWS SDK for Java](sse-c-using-java-sdk.md)