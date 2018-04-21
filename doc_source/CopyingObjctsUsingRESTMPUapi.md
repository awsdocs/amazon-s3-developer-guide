# Copy Object Using the REST Multipart Upload API<a name="CopyingObjctsUsingRESTMPUapi"></a>

The following sections in the *Amazon Simple Storage Service API Reference* describe the REST API for multipart upload\. For copying an existing object you use the Upload Part \(Copy\) API and specify the source object by adding the `x-amz-copy-source` request header in your request\. 
+ [Initiate Multipart Upload](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html)
+ [Upload Part](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPart.html)
+ [Upload Part \(Copy\)](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPartCopy.html)
+ [Complete Multipart Upload](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadComplete.html)
+ [Abort Multipart Upload](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadAbort.html)
+ [List Parts](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadListParts.html)
+ [List Multipart Uploads](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadListMPUpload.html)

You can use these APIs to make your own REST requests, or you can use one the SDKs we provide\. For more information about the SDKs, see [API Support for Multipart Upload](sdksupportformpu.md)\.