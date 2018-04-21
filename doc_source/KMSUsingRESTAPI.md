# Specifying the AWS Key Management Service in Amazon S3 Using the REST API<a name="KMSUsingRESTAPI"></a>

At the time of object creation—that is, when you are uploading a new object or making a copy of an existing object—you can specify the use of server\-side encryption with AWS KMS–managed encryption keys \(SSE\-KMS\) to encrypt your data by adding the `x-amz-server-side-encryption` header to the request\. Set the value of the header to the encryption algorithm `aws:kms`\. Amazon S3 confirms that your object is stored using SSE\-KMS by returning the response header `x-amz-server-side-encryption`\. 

The following REST upload APIs accept the `x-amz-server-side-encryption` request header\.
+ [PUT Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html)
+ [PUT Object \- Copy](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html)
+ [POST Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html)
+ [Initiate Multipart Upload](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html)

When uploading large objects using the multipart upload API, you can specify SSE\-KMS by adding the `x-amz-server-side-encryption` header to the Initiate Multipart Upload request with the value of `aws:kms`\. When copying an existing object, regardless of whether the source object is encrypted or not, the destination object is not encrypted unless you explicitly request server\-side encryption\.

The response headers of the following REST APIs return the `x-amz-server-side-encryption` header when an object is stored using server\-side encryption\.
+ [PUT Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html)
+ [PUT Object \- Copy](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html)
+ [POST Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html)
+ [Initiate Multipart Upload](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html)
+ [Upload Part](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPart.html)
+ [Upload Part \- Copy](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPartCopy.html)
+ [Complete Multipart Upload](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadComplete.html)
+ [Get Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html)
+ [Head Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectHEAD.html)

**Note**  
Encryption request headers should not be sent for `GET` requests and `HEAD` requests if your object uses SSE\-KMS or you’ll get an HTTP 400 BadRequest error\.