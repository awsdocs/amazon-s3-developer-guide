# Stop multipart uploads to an S3 Bucket using the AWS SDK for \.NET \(high\-level API\)<a name="HLAbortDotNet"></a>

To stop in\-progress  multipart uploads, use the `TransferUtility` class from the AWS SDK for \.NET\. You provide a `DateTime`value\. The API then stops all of the multipart uploads that were initiated before the specified date and time and remove the uploaded parts\. An upload is considered to be in\-progress after you initiate it and it completes or you stop it\. 

Because you are billed for all storage associated with uploaded parts, it's important that you either complete the multipart upload to finish creating the object or stop it to remove uploaded parts\. For more information about Amazon S3 multipart uploads, see [Multipart upload overview](mpuoverview.md)\. For information about pricing, see [Multipart upload and pricing](mpuoverview.md#mpuploadpricing)\.

The following C\# example stop all in\-progress multipart uploads that were initiated on a specific bucket over a week ago\. For information about the example's compatibility with a specific version of the AWS SDK for \.NET and instructions on creating and testing a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.

```
using Amazon;
using Amazon.S3;
using Amazon.S3.Transfer;
using System;
using System.Threading.Tasks;

namespace Amazon.DocSamples.S3
{
    class AbortMPUUsingHighLevelAPITest
    {
        private const string bucketName = "*** provide bucket name ***";
        // Specify your bucket region (an example region is shown).
        private static readonly RegionEndpoint bucketRegion = RegionEndpoint.USWest2;
        private static IAmazonS3 s3Client;

        public static void Main()
        {
            s3Client = new AmazonS3Client(bucketRegion);
            AbortMPUAsync().Wait();
        }

        private static async Task AbortMPUAsync()
        {
            try
            {
                var transferUtility = new TransferUtility(s3Client);

                // Abort all in-progress uploads initiated before the specified date.
                await transferUtility.AbortMultipartUploadsAsync(
                    bucketName, DateTime.Now.AddDays(-7));
            }
            catch (AmazonS3Exception e)
            {
                Console.WriteLine("Error encountered on server. Message:'{0}' when writing an object", e.Message);
            }
            catch (Exception e)
            {
                Console.WriteLine("Unknown encountered on server. Message:'{0}' when writing an object", e.Message);
            }
        } 
    }
}
```

**Note**  
You can also stop a specific multipart upload\. For more information, see [List multipart uploads to an S3 Bucket using the AWS SDK for \.NET \(low\-level\)List multipart uploads ](LLlistMPuploadsDotNet.md)\. 

## More info<a name="HLAbortDotNet-more-info"></a>

[AWS SDK for \.NET](https://aws.amazon.com/sdk-for-net/)