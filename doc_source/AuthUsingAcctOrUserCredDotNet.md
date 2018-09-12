# Making Requests Using AWS Account or IAM User Credentials \- AWS SDK for \.NET<a name="AuthUsingAcctOrUserCredDotNet"></a>

To send authenticated requests using your AWS account or IAM user credentials:
+ Create an instance of the `AmazonS3Client` class\. 
+ Execute one of the `AmazonS3Client` methods to send requests to Amazon S3\. The client generates the necessary signature from the credentialsthat you provide and includes it in the request it sends to Amazon S3\. 

The following C\# example shows how to perform the preceding tasks\. For information about running the \.NET examples in this guide and for instructions on how to store your credentials in a configuration file, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.

**Example**  

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

﻿using Amazon.S3;
using Amazon.S3.Model;
using System;
using System.Threading.Tasks;

namespace Amazon.DocSamples.S3
{
    class MakeS3RequestTest
    {
        private const string bucketName = "*** bucket name ***"; 
        // Specify your bucket region (an example region is shown).
        private static readonly RegionEndpoint bucketRegion = RegionEndpoint.USWest2;
        private static IAmazonS3 client;

        public static void Main()
        {
            using (client = new AmazonS3Client(bucketRegion))
            {
                Console.WriteLine("Listing objects stored in a bucket");
                ListingObjectsAsync().Wait();
            }
        }

        static async Task ListingObjectsAsync()
        {
            try
            {
                ListObjectsRequest request = new ListObjectsRequest
                {
                    BucketName = bucketName,
                    MaxKeys = 2
                };
                do
                {
                    ListObjectsResponse response = await client.ListObjectsAsync(request);
                    // Process the response.
                    foreach (S3Object entry in response.S3Objects)
                    {
                        Console.WriteLine("key = {0} size = {1}",
                            entry.Key, entry.Size);
                    }

                    // If the response is truncated, set the marker to get the next 
                    // set of keys.
                    if (response.IsTruncated)
                    {
                        request.Marker = response.NextMarker;
                    }
                    else
                    {
                        request = null;
                    }
                } while (request != null);
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
You can create the `AmazonS3Client` client without providing your security credentials\. Requests sent using this client are anonymous requests, without a signature\. Amazon S3 returns an error if you send anonymous requests for a resource that is not publicly available\.

For working examples, see [Working with Amazon S3 Objects](UsingObjects.md) and [Working with Amazon S3 Buckets](UsingBucket.md)\. You can test these examples using your AWS Account or an IAM user credentials\. 

For example, to list all the object keys in your bucket, see [Listing Keys Using the AWS SDK for \.NET](ListingObjectKeysUsingNetSDK.md)\. 

## Related Resources<a name="RelatedResources003"></a>
+ [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)