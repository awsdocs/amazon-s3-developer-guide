# Using the AWS Java SDK for Multipart Upload \(Low\-Level API\)<a name="mpListPartsJavaAPI"></a>

**Topics**
+ [Upload a File](llJavaUploadFile.md)
+ [List Multipart Uploads](LLlistMPuploadsJava.md)
+ [Abort a Multipart Upload](LLAbortMPUJava.md)

The AWS SDK for Java exposes a low\-level API that closely resembles the Amazon S3 REST API for multipart upload \(see [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)\. Use the low\-level API when you need to pause and resume multipart uploads, vary part sizes during the upload, or do not know the size of the data in advance\. Use the high\-level API \(see [Using the AWS Java SDK for Multipart Upload \(High\-Level API\)](usingHLmpuJava.md)\) whenever you don't have these requirements\.