# Copy an Object Using the AWS SDK for \.NET Multipart Upload API<a name="CopyingObjctsUsingLLNetMPUapi"></a>

The following task guides you through using the \.NET SDK to copy an Amazon S3 object from one source location to another, such as from one bucket to another\. You can use the code demonstrated here to copy objects that are greater than 5 GB\. For objects less than 5 GB, use the single operation copy described in [Copy an Object Using the AWS SDK for \.NET](CopyingObjectUsingNetSDK.md)\.


**Copying Objects**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `AmazonS3Client` class by providing your AWS credentials\.  | 
|  2  |  Initiate a multipart copy by executing the `AmazonS3Client.InitiateMultipartUpload` method\. Create an instance of the `InitiateMultipartUploadRequest`\. You will need to provide a bucket name and key name\.  | 
|  3  |  Save the upload ID from the response object that the `AmazonS3Client.InitiateMultipartUpload` method returns\. You will need to provide this upload ID for each subsequent multipart upload operation\.  | 
|  4  |  Copy all the parts\. For each part copy, create a new instance of the `CopyPartRequest` class and provide part information including source bucket, destination bucket, object key, uploadID, first byte of the part, last byte of the part, and the part number\.   | 
|  5  |  Save the response of the `CopyPartRequest` method in a list\. The response includes the ETag value and the part number you will need to complete the multipart upload\.   | 
|  6  |  Repeat tasks 4 and 5 for each part\.  | 
|  7  | Execute the AmazonS3Client\.CompleteMultipartUpload method to complete the copy\.  | 

The following C\# code sample demonstrates the preceding tasks\.

**Example**  

```
 1. // Step 1. Create instance and provide credentials.
 2. IAmazonS3 s3Client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1);
 3. 
 4. // List to store upload part responses.
 5. List<UploadPartResponse> uploadResponses = new List<UploadPartResponse>();
 6. List<CopyPartResponse> copyResponses = new List<CopyPartResponse>();
 7. InitiateMultipartUploadRequest initiateRequest =
 8.         new InitiateMultipartUploadRequest
 9.             {
10.                 BucketName = targetBucket,
11.                 Key = targetObjectKey
12.             };
13. 
14. // Step 2. Initialize.
15. InitiateMultipartUploadResponse initResponse = s3Client.InitiateMultipartUpload(initiateRequest);
16. 
17. // Step 3. Save Upload Id.
18. String uploadId = initResponse.UploadId;
19. 
20. try
21. {
22.     // Get object size.
23.     GetObjectMetadataRequest metadataRequest = new GetObjectMetadataRequest
24.         {
25.              BucketName = sourceBucket,
26.              Key        = sourceObjectKey
27.         };
28. 
29.     GetObjectMetadataResponse metadataResponse = 
30.                  s3Client.GetObjectMetadata(metadataRequest);
31.     long objectSize = metadataResponse.ContentLength; // in bytes
32. 
33.     // Copy parts.
34.     long partSize = 5 * (long)Math.Pow(2, 20); // 5 MB
35. 
36.     long bytePosition = 0;
37.     for (int i = 1; bytePosition < objectSize; i++)
38.     {
39. 
40.         CopyPartRequest copyRequest = new CopyPartRequest
41.             {
42.                 DestinationBucket = targetBucket,
43.                 DestinationKey = targetObjectKey,
44.                 SourceBucket = sourceBucket,
45.                 SourceKey = sourceObjectKey,
46.                 UploadId = uploadId,
47.                 FirstByte = bytePosition,
48.                 LastByte = bytePosition + partSize - 1 >= objectSize ? objectSize - 1 : bytePosition + partSize - 1,
49.                 PartNumber = i
50.             };
51. 
52.         copyResponses.Add(s3Client.CopyPart(copyRequest));
53. 
54.                     bytePosition += partSize;
55.     }
56.                 CompleteMultipartUploadRequest completeRequest =
57.           new CompleteMultipartUploadRequest
58.               {
59.                   BucketName = targetBucket,
60.                   Key = targetObjectKey,
61.                   UploadId = initResponse.UploadId
62.               };
63. 
64.     completeRequest.AddPartETags(copyResponses);
65.     CompleteMultipartUploadResponse completeUploadResponse = s3Client.CompleteMultipartUpload(completeRequest);
66. 
67. }
68. catch (Exception e) {
69.     Console.WriteLine(e.Message);
70. }
```

**Example**  
The following C\# code example copies an object from one Amazon S3 bucket to another\. For instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.  

```
using System;
using System.Collections.Generic;
using Amazon.S3;
using Amazon.S3.Model;

namespace s3.amazon.com.docsamples
{
    class CopyObjectUsingMPUapi
    {

        static string sourceBucket    = "*** Source bucket name ***";
        static string targetBucket    = "*** Target bucket name ***";
        static string sourceObjectKey = "*** Source object key ***";
        static string targetObjectKey = "*** Target object key ***";

        static void Main(string[] args)
        {
            IAmazonS3 s3Client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1);

            // List to store upload part responses.
            List<UploadPartResponse> uploadResponses = new List<UploadPartResponse>();

            List<CopyPartResponse> copyResponses = new List<CopyPartResponse>();
            InitiateMultipartUploadRequest initiateRequest =
                   new InitiateMultipartUploadRequest
                       {
                           BucketName = targetBucket,
                           Key = targetObjectKey
                       };

            InitiateMultipartUploadResponse initResponse =
                s3Client.InitiateMultipartUpload(initiateRequest);
            String uploadId = initResponse.UploadId;

            try
            {
                // Get object size.
                GetObjectMetadataRequest metadataRequest = new GetObjectMetadataRequest
                    {
                         BucketName = sourceBucket,
                         Key        = sourceObjectKey
                    };

                GetObjectMetadataResponse metadataResponse = 
                             s3Client.GetObjectMetadata(metadataRequest);
                long objectSize = metadataResponse.ContentLength; // in bytes

                // Copy parts.
                long partSize = 5 * (long)Math.Pow(2, 20); // 5 MB

                long bytePosition = 0;
                for (int i = 1; bytePosition < objectSize; i++)
                {

                    CopyPartRequest copyRequest = new CopyPartRequest
                        {
                            DestinationBucket = targetBucket,
                            DestinationKey = targetObjectKey,
                            SourceBucket = sourceBucket,
                            SourceKey = sourceObjectKey,
                            UploadId = uploadId,
                            FirstByte = bytePosition,
                            LastByte = bytePosition + partSize - 1 >= objectSize ? objectSize - 1 : bytePosition + partSize - 1,
                            PartNumber = i
                        };

                    copyResponses.Add(s3Client.CopyPart(copyRequest));

                    bytePosition += partSize;
                }
                CompleteMultipartUploadRequest completeRequest =
                      new CompleteMultipartUploadRequest
                          {
                              BucketName = targetBucket,
                              Key = targetObjectKey,
                              UploadId = initResponse.UploadId
                          };

                completeRequest.AddPartETags(copyResponses);
                CompleteMultipartUploadResponse completeUploadResponse = s3Client.CompleteMultipartUpload(completeRequest);

            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }

        // Helper function that constructs ETags.
        static List<PartETag> GetETags(List<CopyPartResponse> responses)
        {
            List<PartETag> etags = new List<PartETag>();
            foreach (CopyPartResponse response in responses)
            {
                etags.Add(new PartETag(response.PartNumber, response.ETag));
            }
            return etags;
        }
    }
}
```