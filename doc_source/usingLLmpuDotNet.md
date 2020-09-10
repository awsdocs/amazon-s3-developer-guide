# Using the AWS SDK for \.NET for multipart upload \(low\-level API\)<a name="usingLLmpuDotNet"></a>

The AWS SDK for \.NET exposes a low\-level API that closely resembles the Amazon S3 REST API for multipart upload \(see [Using the REST API for multipart upload](UsingRESTAPImpUpload.md) \)\. Use the low\-level API when you need to pause and resume multipart uploads, vary part sizes during the upload, or when you do not know the size of the data in advance\. Use the high\-level API \(see [Using the AWS SDK for \.NET for multipart upload \(high\-level API\)](usingHLmpuDotNet.md)\), whenever you don't have these requirements\.

**Topics**
+ [Upload a file to an S3 Bucket using the AWS SDK for \.NET \(low\-level API\)](LLuploadFileDotNet.md)
+ [List multipart uploads to an S3 Bucket using the AWS SDK for \.NET \(low\-level\)](LLlistMPuploadsDotNet.md)
+ [Track the progress of a multipart upload to an S3 Bucket using the AWS SDK for \.NET \(low\-level\)](LLTrackProgressMPUNet.md)
+ [Stop multipart uploads to an S3 Bucket using the AWS SDK for \.NET \(low\-level\)](LLAbortMPUnet.md)