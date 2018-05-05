# Abort Multipart Uploads to an S3 Bucket Using the AWS SDK for \.NET \(Low\-Level\)<a name="LLAbortMPUnet"></a>

You can abort an in\-progress multipart upload by calling the `AmazonS3Client.AbortMultipartUploadAsync` method\. In addition to aborting the upload, this method deletes all parts that were uploaded to Amazon S3\. 

To abort a multipart upload, you provide the upload ID, and the bucket and key names that are used in the upload\. After you have aborted a multipart upload, you can't use the upload ID to upload additional parts\. For more information about Amazon S3 multipart uploads, see [Multipart Upload Overview](mpuoverview.md)\.

The following C\# example shows how to abort an multipart upload\. For a complete C\# sample that includes the following code, see [Upload a File to an S3 Bucket Using the AWS SDK for \.NET \(Low\-Level API\)](LLuploadFileDotNet.md)\.

```
AbortMultipartUploadRequest abortMPURequest = new AbortMultipartUploadRequest
{
    BucketName = existingBucketName,
    Key = keyName,
    UploadId = initResponse.UploadId
};
await AmazonS3Client.AbortMultipartUploadAsync(abortMPURequest);
```

You can also abort all in\-progress multipart uploads that were initiated prior to a specific time\. This clean\-up operation is useful for aborting multipart uploads that didn't complete or were aborted\. For more information, see [Abort Multipart Uploads to an S3 Bucket Using the AWS SDK for \.NET \(High\-L:evel API\)](HLAbortDotNet.md)\.

## More Info<a name="LLAbortMPUnet-more-info"></a>

[AWS SDK for \.NET](https://aws.amazon.com/sdk-for-net/)