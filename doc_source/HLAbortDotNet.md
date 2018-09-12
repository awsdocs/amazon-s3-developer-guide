# Abort Multipart Uploads to an S3 Bucket Using the AWS SDK for \.NET \(High\-L:evel API\)<a name="HLAbortDotNet"></a>

To abort in\-progress  multipart uploads, use the `TransferUtility` class from the AWS SDK for \.NET\. You provide a `DateTime`value\. The API then aborts all of the multipart uploads that were initiated before the specified date and time and remove the uploaded parts\. An upload is considered to be in\-progress after you initiate it and it completes or you abort it\. 

Because you are billed for all storage associated with uploaded parts, it's important that you either complete the multipart upload to finish creating the object or abort it to remove uploaded parts\. For more information about Amazon S3 multipart uploads, see [Multipart Upload Overview](mpuoverview.md)\. For information about pricing, see [Multipart Upload and Pricing](mpuoverview.md#mpuploadpricing)\.

The following C\# example aborts all in\-progress multipart uploads that were initiated on a specific bucket over a week ago\. For information about the example's compatibility with a specific version of the AWS SDK for \.NET and instructions on creating and testing a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

﻿using Amazon.S3;
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
You can also abort a specific multipart upload\. For more information, see [List Multipart Uploads to an S3 Bucket Using the AWS SDK for \.NET \(Low\-level\)List Multipart Uploads ](LLlistMPuploadsDotNet.md)\. 

## More Info<a name="HLAbortDotNet-more-info"></a>

[AWS SDK for \.NET](https://aws.amazon.com/sdk-for-net/)