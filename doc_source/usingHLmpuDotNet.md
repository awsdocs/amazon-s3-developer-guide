# Using the AWS SDK for \.NET for multipart upload \(high\-level API\)<a name="usingHLmpuDotNet"></a>

**Topics**
+ [Upload a file to an S3 Bucket using the AWS SDK for \.NET \(high\-level API\)](HLuploadFileDotNet.md)
+ [Upload a directory](HLuploadDirDotNet.md)
+ [Abort multipart uploads to an S3 Bucket using the AWS SDK for \.NET \(high\-level API\)](HLAbortDotNet.md)
+ [Track the progress of a multipart upload to an S3 Bucket using the AWS SDK for \.NET \(high\-level API\)](HLTrackProgressMPUDotNet.md)

The AWS SDK for \.NET exposes a high\-level API that simplifies multipart uploads \(see [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)\)\. You can upload data from a file, a directory, or a stream\. For more information about Amazon S3 multipart uploads, see [Multipart Upload Overview](mpuoverview.md)\.

The `TransferUtility` class provides a methods for uploading files and directories, tracking upload progress, and aborting multipart uploads\.