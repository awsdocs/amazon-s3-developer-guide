# Copy an Amazon S3 Object in a Single Operation Using the AWS SDK for \.NET<a name="CopyingObjectUsingNetSDK"></a>

The following C\# example shows how to use the high\-level AWS SDK for \.NET to copy objects that are as big as 5 GB in a single operation\. For objects that are bigger than 5 GB, use the multipart upload copy example described in [Copy an Amazon S3 Object Using the AWS SDK for \.NET Multipart Upload API](CopyingObjctsUsingLLNetMPUapi.md)\.

This example makes a copy of an object that is a maximum of 5 GB\. For information about the example's compatibility with a specific version of the AWS SDK for \.NET and instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

﻿using Amazon.S3;
using Amazon.S3.Model;
using System;
using System.Threading.Tasks;

namespace Amazon.DocSamples.S3
{
    class CopyObjectTest
    {
        private const string sourceBucket = "*** provide the name of the bucket with source object ***";
        private const string destinationBucket = "*** provide the name of the bucket to copy the object to ***";
        private const string objectKey = "*** provide the name of object to copy ***";
        private const string destObjectKey = "*** provide the destination object key name ***";
        // Specify your bucket region (an example region is shown).
        private static readonly RegionEndpoint bucketRegion = RegionEndpoint.USWest2; 
        private static IAmazonS3 s3Client;

        public static void Main()
        {
            s3Client = new AmazonS3Client(bucketRegion);
            Console.WriteLine("Copying an object");
            CopyingObjectAsync().Wait();
        }

        private static async Task CopyingObjectAsync()
        {
            try
            {
                CopyObjectRequest request = new CopyObjectRequest
                {
                    SourceBucket = sourceBucket,
                    SourceKey = objectKey,
                    DestinationBucket = destinationBucket,
                    DestinationKey = destObjectKey
                };
                CopyObjectResponse response = await s3Client.CopyObjectAsync(request);
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

## More Info<a name="CopyingObjectUsingNetSDK-more-info"></a>

[AWS SDK for \.NET](https://aws.amazon.com/sdk-for-net/)