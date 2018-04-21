# Using the AWS \.NET SDK for Multipart Upload \(Low\-Level API\)<a name="usingLLmpuDotNet"></a>

**Topics**
+ [Upload a File](LLuploadFileDotNet.md)
+ [List Multipart Uploads](LLlistMPuploadsDotNet.md)
+ [Track Multipart Upload Progress](LLTrackProgressMPUNet.md)
+ [Abort a Multipart Upload](LLAbortMPUnet.md)

The AWS SDK for \.NET exposes a low\-level API that closely resembles the Amazon S3 REST API for multipart upload \(see [Using the REST API for Multipart Upload](UsingRESTAPImpUpload.md) \)\. Use the low\-level API when you need to pause and resume multipart uploads, vary part sizes during the upload, or do not know the size of the data in advance\. Use the high\-level API \(see [Using the AWS \.NET SDK for Multipart Upload \(High\-Level API\)](usingHLmpuDotNet.md)\), whenever you don't have these requirements\.