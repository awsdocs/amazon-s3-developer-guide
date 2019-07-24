# Using the AWS SDK for \.NET for Multipart Upload \(Low\-Level API\)<a name="usingLLmpuDotNet"></a>

The AWS SDK for \.NET exposes a low\-level API that closely resembles the Amazon S3 REST API for multipart upload \(see [Using the REST API for Multipart Upload](UsingRESTAPImpUpload.md) \)\. Use the low\-level API when you need to pause and resume multipart uploads, vary part sizes during the upload, or when you do not know the size of the data in advance\. Use the high\-level API \(see [Using the AWS SDK for \.NET for Multipart Upload \(High\-Level API\)](usingHLmpuDotNet.md)\), whenever you don't have these requirements\.

**Topics**
+ [Upload a File to an S3 Bucket Using the AWS SDK for \.NET \(Low\-Level API\)](LLuploadFileDotNet.md)
+ [List Multipart Uploads to an S3 Bucket Using the AWS SDK for \.NET \(Low\-level\)](LLlistMPuploadsDotNet.md)
+ [Track the Progress of a Multipart Upload to an S3 Bucket Using the AWS SDK for \.NET \(Low\-Level\)](LLTrackProgressMPUNet.md)
+ [Abort Multipart Uploads to an S3 Bucket Using the AWS SDK for \.NET \(Low\-Level\)](LLAbortMPUnet.md)