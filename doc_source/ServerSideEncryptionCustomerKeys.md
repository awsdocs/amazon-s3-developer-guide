# Protecting data using server\-side encryption with customer\-provided encryption keys \(SSE\-C\)<a name="ServerSideEncryptionCustomerKeys"></a>

Server\-side encryption is about protecting data at rest\. Server\-side encryption encrypts only the object data, not object metadata\. Using server\-side encryption with customer\-provided encryption keys \(SSE\-C\) allows you to set your own encryption keys\. With the encryption key you provide as part of your request, Amazon S3 manages the encryption as it writes to disks and decryption when you access your objects\. Therefore, you don't need to maintain any code to perform data encryption and decryption\. The only thing you do is manage the encryption keys you provide\. 

When you upload an object, Amazon S3 uses the encryption key you provide to apply AES\-256 encryption to your data and removes the encryption key from memory\.  When you retrieve an object, you must provide the same encryption key as part of your request\. Amazon S3 first verifies that the encryption key you provided matches and then decrypts the object before returning the object data to you\. 

**Important**  
Amazon S3 does not store the encryption key you provide\. Instead, it stores a randomly salted HMAC value of the encryption key to validate future requests\. The salted HMAC value cannot be used to derive the value of the encryption key or to decrypt the contents of the encrypted object\. That means if you lose the encryption key, you lose the object\. 

## SSE\-C overview<a name="sse-c-highlights"></a>

This section provides an overview of SSE\-C:
+  You must use HTTPS\. 
**Important**  
Amazon S3 rejects any requests made over HTTP when using SSE\-C\. For security considerations, we recommend that you consider any key you erroneously send using HTTP to be compromised\. You should discard the key and rotate as appropriate\.
+ The ETag in the response is not the MD5 of the object data\. 
+ You manage a mapping of which encryption key was used to encrypt which object\. Amazon S3 does not store encryption keys\. You are responsible for tracking which encryption key you provided for which object\.
  + If your bucket is versioning\-enabled, each object version you upload using this feature can have its own encryption key\. You are responsible for tracking which encryption key was used for which object version\. 
  + Because you manage encryption keys on the client side, you manage any additional safeguards, such as key rotation, on the client side\.
**Warning**  
If you lose the encryption key, any GET request for an object without its encryption key fails, and you lose the object\.

## Topics<a name="sse-c-topic-list"></a>

For more information, see the following topics:
+ [Specifying server\-side encryption with customer\-provided encryption keys using the AWS SDK for Java](sse-c-using-java-sdk.md)
+ [Specifying server\-side encryption with customer\-provided encryption keys using the AWS SDK for \.NET](sse-c-using-dot-net-sdk.md)
+ [Specifying server\-side encryption with customer\-provided encryption keys using the REST API](ServerSideEncryptionCustomerKeysSSEUsingRESTAPI.md)