# Using the AWS SDK for \.NET for Multipart Upload \(High\-Level API\)<a name="usingHLmpuDotNet"></a>

**Topics**
+ [Upload a File to an S3 Bucket Using the AWS SDK for \.NET \(High\-Level API\)](HLuploadFileDotNet.md)
+ [Upload a Directory](HLuploadDirDotNet.md)
+ [Abort Multipart Uploads to an S3 Bucket Using the AWS SDK for \.NET \(High\-L:evel API\)](HLAbortDotNet.md)
+ [Track the Progress of a Multipart Upload to an S3 Bucket Using the AWS SDK for \.NET \(High\-level API\)](HLTrackProgressMPUDotNet.md)

The AWS SDK for \.NET exposes a high\-level API that simplifies multipart uploads \(see [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)\)\. You can upload data from a file, a directory, or a stream\. For more information about Amazon S3 multipart uploads, see [Multipart Upload Overview](mpuoverview.md)\.

The `TransferUtility` class provides a methods for uploading files and directories, tracking upload progress, and aborting multipart uploads\.