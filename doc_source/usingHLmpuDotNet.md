# Using the AWS \.NET SDK for Multipart Upload \(High\-Level API\)<a name="usingHLmpuDotNet"></a>


+ [Upload a File](HLuploadFileDotNet.md)
+ [Upload a Directory](HLuploadDirDotNet.md)
+ [Abort Multipart Uploads](HLAbortDotNet.md)
+ [Track Multipart Upload Progress](HLTrackProgressMPUDotNet.md)

The AWS SDK for \.NET exposes a high\-level API that simplifies multipart upload \(see [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)\)\. You can upload data from a file, directory, or a stream\. When uploading data from a file, if you don't provide the object's key name, the API uses the file name for the object's key name\. You must provide the object's key name if you are uploading data from a stream\. You can optionally set advanced options such as the part size you want to use for the multipart upload, number of threads you want to use when uploading the parts concurrently, optional file metadata, the storage class \(STANDARD or REDUCED\_REDUNDANCY\), or ACL\. The high\-level API provides the `TransferUtilityUploadRequest` class to set these advanced options\.

The `TransferUtility` class provides a method for you to abort multipart uploads in progress\. You must provide a `DateTime` value, and then the API aborts all the multipart uploads that were initiated before the specified date and time\. 