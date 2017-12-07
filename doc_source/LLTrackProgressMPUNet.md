# Track Multipart Upload Progress<a name="LLTrackProgressMPUNet"></a>

The low\-level multipart upload API provides an event, `UploadPartRequest.StreamTransferProgress`, to track the upload progress\. 

The event occurs periodically and returns multipart upload progress information such as the total number of bytes to transfer, and the number of bytes transferred at the time event occurred\. 

The following C\# code sample demonstrates how you can subscribe to the `StreamTransferProgress` event and write a handler\.

**Example**  

```
 1. UploadPartRequest uploadRequest = new UploadPartRequest
 2.  {
 3.    // provide request data.
 4.  };
 5. 
 6.  uploadRequest.StreamTransferProgress += 
 7.      new EventHandler<StreamTransferProgressArgs>(UploadPartProgressEventCallback);
 8. 
 9. ...
10. public static void UploadPartProgressEventCallback(object sender, StreamTransferProgressArgs e)
11. {
12.     // Process event. 
13.     Console.WriteLine("{0}/{1}", e.TransferredBytes, e.TotalBytes);
14. }
```