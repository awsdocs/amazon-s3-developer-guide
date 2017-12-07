# Abort a Multipart Upload<a name="LLAbortMPUnet"></a>

You can abort an in\-progress multipart upload by calling the AmazonS3Client\.AbortMultipartUpload method\. This method deletes any parts that were uploaded to S3 and free up the resources\. You must provide the upload ID, bucket name and the key name\. The following C\# code sample demonstrates how you can abort a multipart upload in progress\.

**Example**  

```
1. s3Client.AbortMultipartUpload(new AbortMultipartUploadRequest
2. {
3.     BucketName = existingBucketName,
4.     Key = keyName,
5.     UploadId = uploadID
6. };
```

**Note**  
Instead of a specific multipart upload, you can abort all your in\-progress multipart uploads initiated prior to a specific time\. This clean up operation is useful to abort old multipart uploads that you initiated but neither completed or aborted\. For more information, see [Abort Multipart Uploads](HLAbortDotNet.md)\.