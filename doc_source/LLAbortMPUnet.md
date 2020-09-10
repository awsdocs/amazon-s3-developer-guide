# Stop multipart uploads to an S3 Bucket using the AWS SDK for \.NET \(low\-level\)<a name="LLAbortMPUnet"></a>

You can stop an in\-progress multipart upload by calling the `AmazonS3Client.AbortMultipartUploadAsync` method\. In addition to stopping the upload, this method deletes all parts that were uploaded to Amazon S3\. 

To stop a multipart upload, you provide the upload ID, and the bucket and key names that are used in the upload\. After you have stopped a multipart upload, you can't use the upload ID to upload additional parts\. For more information about Amazon S3 multipart uploads, see [Multipart upload overview](mpuoverview.md)\.

The following C\# example shows how to stop a multipart upload\. For a complete C\# sample that includes the following code, see [Upload a file to an S3 Bucket using the AWS SDK for \.NET \(low\-level API\)](LLuploadFileDotNet.md)\.

```
AbortMultipartUploadRequest abortMPURequest = new AbortMultipartUploadRequest
{
    BucketName = existingBucketName,
    Key = keyName,
    UploadId = initResponse.UploadId
};
await AmazonS3Client.AbortMultipartUploadAsync(abortMPURequest);
```

You can also stop all in\-progress multipart uploads that were initiated prior to a specific time\. This clean\-up operation is useful for stopping multipart uploads that didn't complete or were stopped\. For more information, see [Stop multipart uploads to an S3 Bucket using the AWS SDK for \.NET \(high\-level API\)](HLAbortDotNet.md)\.

## More info<a name="LLAbortMPUnet-more-info"></a>

[AWS SDK for \.NET](https://aws.amazon.com/sdk-for-net/)