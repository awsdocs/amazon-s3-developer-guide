# API Support for Multipart Upload<a name="sdksupportformpu"></a>

You can use an AWS SDK to upload an object in parts\. The following AWS SDK libraries support multipart upload:
+ [AWS SDK for Java ](https://aws.amazon.com/sdk-for-java/)
+ [AWS SDK for \.NET](https://aws.amazon.com/sdk-for-net/)
+ [AWS SDK for PHP](https://aws.amazon.com/sdk-for-php/)

These libraries provide a high\-level abstraction that makes uploading multipart objects easy\. However, if your application requires, you can use the REST API directly\. The following sections in the Amazon Simple Storage Service API Reference describe the REST API for multipart upload\. 
+ [Initiate Multipart Upload](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html)
+ [Upload Part](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPart.html)
+ [Upload Part \(Copy\)](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPartCopy.html)
+ [Complete Multipart Upload](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadComplete.html)
+ [Abort Multipart Upload](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadAbort.html)
+ [List Parts](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadListParts.html)
+ [List Multipart Uploads](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadListMPUpload.html)