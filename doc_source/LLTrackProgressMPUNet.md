# Track the progress of a multipart upload to an S3 Bucket using the AWS SDK for \.NET \(low\-level\)<a name="LLTrackProgressMPUNet"></a>

To track the progress of a multipart upload, use the `UploadPartRequest.StreamTransferProgress` event provided by the AWS SDK for \.NET low\-level multipart upload API\. The event occurs periodically\. It returns information such as the total number of bytes to transfer and the number of bytes transferred\. 

The following C\# example shows how to track the progress of multipart uploads\. For a complete C\# sample that includes the following code, see [Upload a file to an S3 Bucket using the AWS SDK for \.NET \(low\-level API\)](LLuploadFileDotNet.md)\.

```
UploadPartRequest uploadRequest = new UploadPartRequest
{
// Provide the request data.
};

uploadRequest.StreamTransferProgress += 
     new EventHandler<StreamTransferProgressArgs>(UploadPartProgressEventCallback);

...
public static void UploadPartProgressEventCallback(object sender, StreamTransferProgressArgs e)
{
    // Process the event. 
    Console.WriteLine("{0}/{1}", e.TransferredBytes, e.TotalBytes);
}
```

## More info<a name="LLTrackProgressMPUNet-more-info"></a>

[AWS SDK for \.NET](https://aws.amazon.com/sdk-for-net/)