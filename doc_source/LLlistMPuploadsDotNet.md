# List Multipart Uploads to an S3 Bucket Using the AWS SDK for \.NET \(Low\-level\)<a name="LLlistMPuploadsDotNet"></a>

To list all of the in\-progress multipart uploads on a specific bucket, use the AWS SDK for \.NET low\-level multipart upload API's `ListMultipartUploadsRequest` class\. The `AmazonS3Client.ListMultipartUploads` method returns an instance of the `ListMultipartUploadsResponse` class that provides information about the in\-progress multipart uploads\. 

An in\-progress multipart upload is a multipart upload that has been initiated using the initiate multipart upload request, but has not yet been completed or aborted\. For more information about Amazon S3 multipart uploads, see [Multipart Upload Overview](mpuoverview.md)\.

The following C\# example shows how to use the AWS SDK for \.NET to list all in\-progress multipart uploads on a bucket\. For information about the example's compatibility with a specific version of the AWS SDK for \.NET and instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.

```
ListMultipartUploadsRequest request = new ListMultipartUploadsRequest
{
	 BucketName = bucketName // Bucket receiving the uploads.
};

ListMultipartUploadsResponse response = await AmazonS3Client.ListMultipartUploadsAsync(request);
```

## More Info<a name="LLlistMPuploadsDotNet-more-info"></a>

[AWS SDK for \.NET](https://aws.amazon.com/sdk-for-net/)