# Using the AWS Java SDK for multipart upload \(high\-level API\)<a name="usingHLmpuJava"></a>

**Topics**
+ [Upload a file](HLuploadFileJava.md)
+ [Stop multipart uploads](HLAbortMPUploadsJava.md)
+ [Track multipart upload progress](HLTrackProgressMPUJava.md)

The AWS SDK for Java exposes a high\-level API, called `TransferManager`, that simplifies multipart uploads \(see [Uploading objects using multipart upload API](uploadobjusingmpu.md)\)\. You can upload data from a file or a stream\. You can also set advanced options, such as the part size you want to use for the multipart upload, or the number of concurrent threads you want to use when uploading the parts\. You can also set optional object properties, the storage class, or the ACL\. You use the `PutObjectRequest` and the `TransferManagerConfiguration` classes to set these advanced options\. 

When possible, `TransferManager` attempts to use multiple threads to upload multiple parts of a single upload at once\. When dealing with large content sizes and high bandwidth, this can increase throughput significantly\.

In addition to file\-upload functionality, the `TransferManager` class enables you to stop an in\-progress multipart upload\. An upload is considered to be in progress after you initiate it and until you complete or stop it\. The `TransferManager` stops all in\-progress multipart uploads on a specified bucket that were initiated before a specified date and time\. 

For more information about multipart uploads, including additional functionality offered by the low\-level API methods, see [Uploading objects using multipart upload API](uploadobjusingmpu.md)\. 