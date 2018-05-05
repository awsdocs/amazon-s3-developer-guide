# Protecting Data Using Server\-Side Encryption with Customer\-Provided Encryption Keys \(SSE\-C\)<a name="ServerSideEncryptionCustomerKeys"></a>

Server\-side encryption is about protecting data at rest\. Using server\-side encryption with customer\-provided encryption keys \(SSE\-C\) allows you to set your own encryption keys\. With the encryption key you provide as part of your request, Amazon S3 manages both the encryption, as it writes to disks, and decryption, when you access your objects\. Therefore, you don't need to maintain any code to perform data encryption and decryption\. The only thing you do is manage the encryption keys you provide\.

When you upload an object, Amazon S3 uses the encryption key you provide to apply AES\-256 encryption to your data and removes the encryption key from memory\. 

**Important**  
Amazon S3 does not store the encryption key you provide\. Instead, we store a randomly salted HMAC value of the encryption key in order to validate future requests\. The salted HMAC value cannot be used to derive the value of the encryption key or to decrypt the contents of the encrypted object\. That means, if you lose the encryption key, you lose the object\. 

When you retrieve an object, you must provide the same encryption key as part of your request\. Amazon S3 first verifies that the encryption key you provided matches, and then decrypts the object before returning the object data to you\. 

The highlights of SSE\-C are:
+  You must use https\. 
**Important**  
Amazon S3 will reject any requests made over http when using SSE\-C\. For security considerations, we recommend you consider any key you send erroneously using http to be compromised\. You should discard the key, and rotate as appropriate\.
+ The ETag in the response is not the MD5 of the object data\. 
+ You manage a mapping of which encryption key was used to encrypt which object\. Amazon S3 does not store encryption keys\. You are responsible for tracking which encryption key you provided for which object\.
  + If your bucket is versioning\-enabled, each object version you upload using this feature can have its own encryption key\. You are responsible for tracking which encryption key was used for which object version\. 
  + Because you manage encryption keys on the client side, you manage any additional safeguards, such as key rotation, on the client side\.
**Warning**  
If you lose the encryption key any GET request for an object without its encryption key will fail, and you lose the object\.

## Using SSE\-C<a name="sse-c-how-to-programmatically-intro"></a>

When using server\-side encryption with customer\-provided encryption keys \(SSE\-C\), you must provide encryption key information using the following request headers\. 


|  Name  |  Description  | 
| --- | --- | 
| x\-amz\-server\-side​\-encryption​\-customer\-algorithm  |  Use this header to specify the encryption algorithm\. The header value must be "AES256"\.   | 
| x\-amz\-server\-side​\-encryption​\-customer\-key  |  Use this header to provide the 256\-bit, base64\-encoded encryption key for Amazon S3 to use to encrypt or decrypt your data\.   | 
| x\-amz\-server\-side​\-encryption​\-customer\-key\-MD5  |  Use this header to provide the base64\-encoded 128\-bit MD5 digest of the encryption key according to [RFC 1321](http://tools.ietf.org/html/rfc1321)\. Amazon S3 uses this header for a message integrity check to ensure the encryption key was transmitted without error\.  | 

You can use AWS SDK wrapper libraries to add these headers to your request\. If you need to, you can make the Amazon S3 REST API calls directly in your application\. 

**Note**  
You cannot use the Amazon S3 console to upload an object and request SSE\-C\. You also cannot use the console to update \(for example, change the storage class or add metadata\) an existing object stored using SSE\-C\.

The following Amazon S3 APIs support these headers\.
+ GET operation — When retrieving objects using the GET API \(see [GET Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html)\), you can specify the request headers\. Torrents are not supported for objects encrypted using SSE\-C\.
+ HEAD operation — To retrieve object metadata using the HEAD API \(see [HEAD Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectHEAD.html)\), you can specify these request headers\.
+ PUT operation — When uploading data using the PUT API \(see [PUT Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html)\), you can specify these request headers\. 
+ Multipart Upload — When uploading large objects using the multipart upload API, you can specify these headers\. You specify these headers in the initiate request \(see [Initiate Multipart Upload](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html)\) and each subsequent part upload request \([Upload Part](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPart.html)\)\. For each part upload request, the encryption information must be the same as what you provided in the initiate multipart upload request\.
+ POST operation — When using a POST operation to upload an object \(see [POST Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html)\), instead of the request headers, you provide the same information in the form fields\.
+ Copy operation — When you copy an object \(see [PUT Object \- Copy](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html)\), you have both a source object and a target object\. Accordingly, you have the following to consider:
  + If you want the target object encrypted using server\-side encryption with AWS\-managed keys, you must provide the `x-amz-server-side​-encryption` request header\.
  +  If you want the target object encrypted using SSE\-C, you must provide encryption information using the three headers described in the preceding table\.
  +  If the source object is encrypted using SSE\-C, you must provide encryption key information using the following headers so that Amazon S3 can decrypt the object for copying\.    
[\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/ServerSideEncryptionCustomerKeys.html)

## Presigned URL and SSE\-C<a name="ssec-and-presignedurl"></a>

You can generate a presigned URL that can be used for operations such as upload a new object, retrieve an existing object, or object metadata\. Presigned URLs support the SSE\-C as follows:
+ When creating a presigned URL, you must specify the algorithm using the `x-amz-server-side​-encryption​-customer-algorithm` in the signature calculation\.
+ When using the presigned URL to upload a new object, retrieve an existing object, or retrieve only object metadata, you must provide all the encryption headers in your client application\. 

For more information, see the following topics:
+ [Specifying Server\-Side Encryption with Customer\-Provided Encryption Keys Using the AWS SDK for Java](sse-c-using-java-sdk.md)
+ [Specifying Server\-Side Encryption with Customer\-Provided Encryption Keys Using the AWS SDK for \.NET](sse-c-using-dot-net-sdk.md)
+ [Specifying Server\-Side Encryption with Customer\-Provided Encryption Keys Using the REST API](ServerSideEncryptionCustomerKeysSSEUsingRESTAPI.md)