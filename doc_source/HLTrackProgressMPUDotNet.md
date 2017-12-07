# Track Multipart Upload Progress<a name="HLTrackProgressMPUDotNet"></a>

The high\-level multipart upload API provides an event, `TransferUtilityUploadRequest.UploadProgressEvent`, to track the upload progress when uploading data using the `TransferUtility` class\. 

The event occurs periodically and returns multipart upload progress information such as the total number of bytes to transfer, and the number of bytes transferred at the time event occurred\. 

The following C\# code sample demonstrates how you can subscribe to the `UploadProgressEvent` event and write a handler\.

**Example**  

```
 1. TransferUtility fileTransferUtility =
 2.      new TransferUtility(new AmazonS3Client(Amazon.RegionEndpoint.USEast1));
 3. 
 4. // Use TransferUtilityUploadRequest to configure options.
 5. // In this example we subscribe to an event.
 6. TransferUtilityUploadRequest uploadRequest =
 7.     new TransferUtilityUploadRequest
 8.     {
 9.         BucketName = existingBucketName,
10.         FilePath = filePath, 
11.         Key = keyName
12.     };
13.               
14. uploadRequest.UploadProgressEvent +=
15.     new EventHandler<UploadProgressArgs>
16.         (uploadRequest_UploadPartProgressEvent);
17. 
18. fileTransferUtility.Upload(uploadRequest);
19. 
20. static void uploadRequest_UploadPartProgressEvent(object sender, UploadProgressArgs e)
21. {
22.     // Process event.
23.     Console.WriteLine("{0}/{1}", e.TransferredBytes, e.TotalBytes);
24. }
```

**Example**  
The following C\# code example uploads a file to an Amazon S3 bucket and tracks the progress by subscribing to the `TransferUtilityUploadRequest.UploadProgressEvent` event\. For instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.  

```
using System;
using System.Collections.Specialized;
using System.Configuration;
using Amazon.S3;
using Amazon.S3.Transfer;


namespace s3.amazon.com.docsamples
{
    class TrackMPUUsingHighLevelAPI
    {
        static string existingBucketName = "*** Provide bucket name ***";
        static string keyName            = "*** Provide key name ***";
        static string filePath           = "*** Provide file to upload ***";

        static void Main(string[] args)
        {
            try
            {
                TransferUtility fileTransferUtility =
                    new TransferUtility(new AmazonS3Client(Amazon.RegionEndpoint.USEast1));

                // Use TransferUtilityUploadRequest to configure options.
                // In this example we subscribe to an event.
                TransferUtilityUploadRequest uploadRequest =
                    new TransferUtilityUploadRequest
                    {
                        BucketName = existingBucketName,
                        FilePath = filePath, 
                        Key = keyName
                    };
              
                uploadRequest.UploadProgressEvent +=
                    new EventHandler<UploadProgressArgs>
                        (uploadRequest_UploadPartProgressEvent);

                fileTransferUtility.Upload(uploadRequest);
                Console.WriteLine("Upload completed");
            }

            catch (AmazonS3Exception e)
            {
                Console.WriteLine(e.Message, e.InnerException);
            }
        }

        static void uploadRequest_UploadPartProgressEvent(
            object sender, UploadProgressArgs e)
        {
            // Process event.
            Console.WriteLine("{0}/{1}", e.TransferredBytes, e.TotalBytes);
        }
    }
}
```