# Using the AWS PHP SDK for Multipart Upload \(Low\-Level API\)<a name="usingLLmpuPHP"></a>

**Topics**
+ [Upload a File in Multiple Parts Using the PHP SDK Low\-Level API](LLuploadFilePHP.md)
+ [List Multipart Uploads Using the Low\-Level AWS SDK for PHP API](LLlistMPuploadsPHP.md)
+ [Abort a Multipart Upload](LLAbortMPUphp.md)

The AWS SDK for PHP exposes a low\-level API that closely resembles the Amazon S3 REST API for multipart upload \(see [Using the REST API for Multipart Upload](UsingRESTAPImpUpload.md) \)\. Use the low\-level API when you need to pause and resume multipart uploads, vary part sizes during the upload, or if you do not know the size of the data in advance\. Use the AWS SDK for PHP high\-level abstractions \(see [Using the AWS PHP SDK for Multipart Upload](usingHLmpuPHP.md)\) whenever you don't have these requirements\.