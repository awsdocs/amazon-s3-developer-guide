# Using the AWS Java SDK for a multipart upload \(low\-level API\)<a name="mpListPartsJavaAPI"></a>

**Topics**
+ [Upload a file](llJavaUploadFile.md)
+ [List multipart uploads](LLlistMPuploadsJava.md)
+ [Abort a multipart upload](LLAbortMPUJava.md)

The AWS SDK for Java exposes a low\-level API that closely resembles the Amazon S3 REST API for multipart uploads \(see [Uploading objects using multipart upload API](uploadobjusingmpu.md)\. Use the low\-level API when you need to pause and resume multipart uploads, vary part sizes during the upload, or do not know the size of the upload data in advance\. When you don't have these requirements, use the high\-level API \(see [Using the AWS Java SDK for multipart upload \(high\-level API\)](usingHLmpuJava.md)\)\.