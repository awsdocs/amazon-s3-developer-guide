# Abort a Multipart Upload<a name="LLAbortMPUJava"></a>

You can abort an in\-progress multipart upload by calling the `AmazonS3.abortMultipartUpload` method\. This method deletes any parts that were uploaded to Amazon S3 and frees up the resources\. You must provide the upload ID, bucket name, and key name\. The following Java code sample demonstrates how to abort an in\-progress multipart upload\.

**Example**  

```
1. InitiateMultipartUploadRequest initRequest =
2.     new InitiateMultipartUploadRequest(existingBucketName, keyName);
3. InitiateMultipartUploadResult initResponse = 
4.                s3Client.initiateMultipartUpload(initRequest);
5. 
6. AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider()); 
7. s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(
8.             existingBucketName, keyName, initResponse.getUploadId()));
```

**Note**  
Instead of a specific multipart upload, you can abort all your multipart uploads initiated before a specific time that are still in progress\. This clean\-up operation is useful to abort old multipart uploads that you initiated but neither completed nor aborted\. For more information, see [Abort Multipart Uploads](HLAbortMPUploadsJava.md)\.