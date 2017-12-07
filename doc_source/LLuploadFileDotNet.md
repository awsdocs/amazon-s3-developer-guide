# Upload a File<a name="LLuploadFileDotNet"></a>

The following tasks guide you through using the low\-level \.NET classes to upload a file\.


**Low\-Level API File UploadingProcess**  

|  |  | 
| --- |--- |
| 1 | Create an instance of the `AmazonS3Client` class, by providing your AWS credentials\. | 
| 2 | Initiate multipart upload by executing the `AmazonS3Client.InitiateMultipartUpload` method\. You will need to provide information required to initiate the multipart upload by creating an instance of the `InitiateMultipartUploadRequest` class\.  | 
| 3 | Save the Upload ID that the `AmazonS3Client.InitiateMultipartUpload` method returns\. You will need to provide this upload ID for each subsequent multipart upload operation\. | 
| 4 | Upload the parts\. For each part upload, execute the `AmazonS3Client.UploadPart` method\. You will need to provide part upload information such as upload ID, bucket name, and the part number\. You provide this information by creating an instance of the `UploadPartRequest` class\.  | 
| 5 | Save the response of the `AmazonS3Client.UploadPart` method in a list\. This response includes the ETag value and the part number you will later need to complete the multipart upload\.  | 
| 6 | Repeat tasks 4 and 5 for each part\. | 
| 7 | Execute the AmazonS3Client\.CompleteMultipartUpload method to complete the multipart upload\.  | 

The following C\# code sample demonstrates the preceding tasks\.

**Example**  

```
 1. IAmazonS3 s3Client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1);
 2. 
 3. // List to store upload part responses.
 4. List<UploadPartResponse> uploadResponses = new List<UploadPartResponse>();
 5. 
 6. // 1. Initialize.
 7. InitiateMultipartUploadRequest initiateRequest = new InitiateMultipartUploadRequest
 8.     {
 9.         BucketName = existingBucketName,
10.         Key = keyName
11.     };
12. 
13. InitiateMultipartUploadResponse initResponse = 
14.     s3Client.InitiateMultipartUpload(initRequest);
15. 
16. // 2. Upload Parts.
17. long contentLength = new FileInfo(filePath).Length;
18. long partSize = 5242880; // 5 MB
19. 
20. try
21. {
22.     long filePosition = 0;
23.     for (int i = 1; filePosition < contentLength; i++)
24.     {
25. 
26.         // Create request to upload a part.
27.         UploadPartRequest uploadRequest = new UploadPartRequest
28.                         {
29.                             BucketName = existingBucketName,
30.                             Key = keyName,
31.                             UploadId = initResponse.UploadId,
32.                             PartNumber = i,
33.                             PartSize = partSize,
34.                             FilePosition = filePosition,
35.                             FilePath = filePath
36.                         };
37. 
38.         // Upload part and add response to our list.
39.          uploadResponses.Add(s3Client.UploadPart(uploadRequest));
40. 
41.         filePosition += partSize;
42.     }
43. 
44.     // Step 3: complete.
45.     CompleteMultipartUploadRequest completeRequest = new CompleteMultipartUploadRequest
46.        {
47.            BucketName = existingBucketName,
48.            Key = keyName,
49.            UploadId = initResponse.UploadId,
50.         };
51. 
52.     CompleteMultipartUploadResponse completeUploadResponse =
53.       s3Client.CompleteMultipartUpload(completeRequest);
54.  
55. }
56. catch (Exception exception)
57. {
58.     Console.WriteLine("Exception occurred: {0}", exception.Message);
59.     AbortMultipartUploadRequest abortMPURequest = new AbortMultipartUploadRequest
60.       {
61.             BucketName = existingBucketName,
62.             Key = keyName,
63.             UploadId = initResponse.UploadId
64.        };
65.     s3Client.AbortMultipartUpload(abortMPURequest);
66. }
```

**Note**  
 When uploading large objects using the \.NET API, timeout might occur even while data is being written to the request stream\. You can set explicit timeout using the `UploadPartRequest`\. 

**Example**  
The following C\# code example uploads a file to an Amazon S3 bucket\. For instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.  

```
using System;
using System.Collections.Generic;
using System.IO;
using Amazon.S3;
using Amazon.S3.Model;

namespace s3.amazon.com.docsamples
{
    class UploadFileMPULowLevelAPI
    {
        static string existingBucketName = "*** bucket name ***";
        static string keyName            = "*** key name ***";
        static string filePath           = "*** file path ***";

        static void Main(string[] args)
        {
            IAmazonS3 s3Client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1);

            // List to store upload part responses.
            List<UploadPartResponse> uploadResponses = new List<UploadPartResponse>();

            // 1. Initialize.
            InitiateMultipartUploadRequest initiateRequest = new InitiateMultipartUploadRequest
                {
                    BucketName = existingBucketName,
                    Key = keyName
                };

            InitiateMultipartUploadResponse initResponse =
                s3Client.InitiateMultipartUpload(initiateRequest);

            // 2. Upload Parts.
            long contentLength = new FileInfo(filePath).Length;
            long partSize = 5 * (long)Math.Pow(2, 20); // 5 MB

            try
            {
                long filePosition = 0;
                for (int i = 1; filePosition < contentLength; i++)
                {
                    UploadPartRequest uploadRequest = new UploadPartRequest
                        {
                            BucketName = existingBucketName,
                            Key = keyName,
                            UploadId = initResponse.UploadId,
                            PartNumber = i,
                            PartSize = partSize,
                            FilePosition = filePosition,
                            FilePath = filePath
                        };

                    // Upload part and add response to our list.
                    uploadResponses.Add(s3Client.UploadPart(uploadRequest));

                    filePosition += partSize;
                }

                // Step 3: complete.
                CompleteMultipartUploadRequest completeRequest = new CompleteMultipartUploadRequest
                    {
                        BucketName = existingBucketName,
                        Key = keyName,
                        UploadId = initResponse.UploadId,
                        //PartETags = new List<PartETag>(uploadResponses)

                    };
                completeRequest.AddPartETags(uploadResponses);

                CompleteMultipartUploadResponse completeUploadResponse =
                    s3Client.CompleteMultipartUpload(completeRequest);

            }
            catch (Exception exception)
            {
                Console.WriteLine("Exception occurred: {0}", exception.Message);
                AbortMultipartUploadRequest abortMPURequest = new AbortMultipartUploadRequest
                {
                    BucketName = existingBucketName,
                    Key = keyName,
                    UploadId = initResponse.UploadId
                };
                s3Client.AbortMultipartUpload(abortMPURequest);
            }
        }
    }
}
```