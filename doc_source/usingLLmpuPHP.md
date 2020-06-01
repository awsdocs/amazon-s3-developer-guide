# Using the AWS PHP SDK for multipart upload \(low\-level API\)<a name="usingLLmpuPHP"></a>

**Topics**
+ [Upload a file in multiple parts using the PHP SDK low\-level API](LLuploadFilePHP.md)
+ [List multipart uploads using the low\-level AWS SDK for PHP API](LLlistMPuploadsPHP.md)
+ [Abort a multipart upload](LLAbortMPUphp.md)

The AWS SDK for PHP exposes a low\-level API that closely resembles the Amazon S3 REST API for multipart upload \(see [Using the REST API for multipart upload](UsingRESTAPImpUpload.md) \)\. Use the low\-level API when you need to pause and resume multipart uploads, vary part sizes during the upload, or if you do not know the size of the data in advance\. Use the AWS SDK for PHP high\-level abstractions \(see [Using the AWS PHP SDK for multipart upload](usingHLmpuPHP.md)\) whenever you don't have these requirements\.