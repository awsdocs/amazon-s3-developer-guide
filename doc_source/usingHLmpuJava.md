# Using the AWS Java SDK for Multipart Upload \(High\-Level API\)<a name="usingHLmpuJava"></a>


+ [Upload a File](HLuploadFileJava.md)
+ [Abort Multipart Uploads](HLAbortMPUploadsJava.md)
+ [Track Multipart Upload Progress](HLTrackProgressMPUJava.md)

The AWS SDK for Java exposes a high\-level API that simplifies multipart upload \(see [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)\)\. You can upload data from a file or a stream\. You can also set advanced options, such as the part size you want to use for the multipart upload, or the number of threads you want to use when uploading the parts concurrently\. You can also set optional object properties, the storage class, or ACL\. You use the `PutObjectRequest` and the `TransferManagerConfiguration` classes to set these advanced options\. The `TransferManager` class of the Java API provides the high\-level API for you to upload data\.

When possible, `TransferManager` attempts to use multiple threads to upload multiple parts of a single upload at once\. When dealing with large content sizes and high bandwidth, this can have a significant increase on throughput\. 

In addition to file upload functionality, the `TransferManager` class provides a method for you to abort multipart upload in progress\. You must provide a `Date` value, and then the API aborts all the multipart uploads that were initiated before the specified date\. 