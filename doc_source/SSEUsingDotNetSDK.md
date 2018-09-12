# Specifying Server\-Side Encryption Using the AWS SDK for \.NET<a name="SSEUsingDotNetSDK"></a>

When you upload an object, you can direct Amazon S3 to encrypt it\. To change the encryption state of an existing object, you make a copy of the object and delete the source object\. By default, the copy operation encrypts the target only if you explicitly request server\-side encryption of the target object\. To specify server\-side encryption in the `CopyObjectRequest`, add the following:

```
 ServerSideEncryptionMethod = ServerSideEncryptionMethod.AES256
```

## <a name="SSEUsingDotNetExample"></a>

For a working sample of how to copy an object, see [Copy an Amazon S3 Object in a Single Operation Using the AWS SDK for \.NET](CopyingObjectUsingNetSDK.md)\. 

The following example uploads an object\. In the request, the example directs Amazon S3 to encrypt the object\. The example then retrieves object metadata and verifies the encryption method that was used\. For information about creating and testing a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

﻿using Amazon.S3;
using Amazon.S3.Model;
using System;
using System.Threading.Tasks;

namespace Amazon.DocSamples.S3
{
    class SpecifyServerSideEncryptionTest
    {
        private const string bucketName = "*** bucket name ***";
        private const string keyName = "*** key name for object created ***";
        // Specify your bucket region (an example region is shown).
        private static readonly RegionEndpoint bucketRegion = RegionEndpoint.USWest2;
        private static IAmazonS3 client;

        public static void Main()
        {
            client = new AmazonS3Client(bucketRegion);
            WritingAnObjectAsync().Wait();
        }

        static async Task WritingAnObjectAsync()
        {
            try
            {
                var putRequest = new PutObjectRequest
                {
                    BucketName = bucketName,
                    Key = keyName,
                    ContentBody = "sample text",
                    ServerSideEncryptionMethod = ServerSideEncryptionMethod.AES256
                };

                var putResponse = await client.PutObjectAsync(putRequest);

                // Determine the encryption state of an object.
                GetObjectMetadataRequest metadataRequest = new GetObjectMetadataRequest
                {
                    BucketName = bucketName,
                    Key = keyName
                };
                GetObjectMetadataResponse response = await client.GetObjectMetadataAsync(metadataRequest);
                ServerSideEncryptionMethod objectEncryption = response.ServerSideEncryptionMethod;

                Console.WriteLine("Encryption method used: {0}", objectEncryption.ToString());
            }
            catch (AmazonS3Exception e)
            {
                Console.WriteLine("Error encountered ***. Message:'{0}' when writing an object", e.Message);
            }
            catch (Exception e)
            {
                Console.WriteLine("Unknown encountered on server. Message:'{0}' when writing an object", e.Message);
            }
        }
    }
}
```

## Changing Server\-Side Encryption of an Existing Object \(Copy Operation\)<a name="ChangingServer-SideEncryptionofanExistingObjectCopyOperation02"></a>