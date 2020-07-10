# Copy object using the REST multipart upload API<a name="CopyingObjctsUsingRESTMPUapi"></a>

The following sections in the *Amazon Simple Storage Service API Reference* describe the REST API for multipart upload\. For copying an existing object you use the Upload Part \(Copy\) API and specify the source object by adding the `x-amz-copy-source` request header in your request\. 
+ [Initiate Multipart Upload](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html)
+ [Upload Part](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPart.html)
+ [Upload Part \(Copy\)](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPartCopy.html)
+ [Complete Multipart Upload](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadComplete.html)
+ [Abort Multipart Upload](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadAbort.html)
+ [List Parts](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadListParts.html)
+ [List Multipart Uploads](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadListMPUpload.html)

You can use these APIs to make your own REST requests, or you can use one the SDKs we provide\. For more information about the SDKs, see [API support for multipart upload](sdksupportformpu.md)\.