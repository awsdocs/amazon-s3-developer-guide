# Abort Multipart Uploads<a name="HLAbortDotNet"></a>

The `TransferUtility` class provides a method, `AbortMultipartUploads`, to abort multipart uploads in progress\. An upload is considered to be in\-progress once you initiate it and until you complete it or abort it\. You provide a `DateTime` value and this API aborts all the multipart uploads, on that bucket, that were initiated before the specified `DateTime` and in progress\. 

Because you are billed for all storage associated with uploaded parts \(see [Multipart Upload and Pricing](mpuoverview.md#mpuploadpricing)\), it is important that you either complete the multipart upload to have the object created or abort the multipart upload to remove any uploaded parts\.

The following tasks guide you through using the high\-level \.NET classes to abort multipart uploads\.


**High\-Level API Multipart Uploads Aborting Process**  

|  |  | 
| --- |--- |
| 1 | Create an instance of the `TransferUtility` class by providing your AWS credentials\.  | 
| 2 | Execute the `TransferUtility.AbortMultipartUploads` method by passing the bucket name and a `DateTime` value\. | 

The following C\# code sample demonstrates the preceding tasks\.

**Example**  

```
1. TransferUtility utility = new TransferUtility();
2. utility.AbortMultipartUploads(existingBucketName, DateTime.Now.AddDays(-7));
```

**Example**  
The following C\# code aborts all multipart uploads in progress that were initiated on a specific bucket over a week ago\. For instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.  

```
using System;
using Amazon.S3;
using Amazon.S3.Transfer;

namespace s3.amazon.com.docsamples
{
    class AbortMPUUsingHighLevelAPI
    {
        static string existingBucketName = "***Provide bucket name***";

        static void Main(string[] args)
        {
            try
            {
                TransferUtility transferUtility =
                    new TransferUtility(new AmazonS3Client(Amazon.RegionEndpoint.USEast1));
                // Aborting uploads that were initiated over a week ago.
                transferUtility.AbortMultipartUploads(
                    existingBucketName, DateTime.Now.AddDays(-7));
            }

            catch (AmazonS3Exception e)
            {
                Console.WriteLine(e.Message, e.InnerException);
            }
        }
    }
}
```

**Note**  
You can also abort a specific multipart upload\. For more information, see [List Multipart Uploads](LLlistMPuploadsDotNet.md)\. 