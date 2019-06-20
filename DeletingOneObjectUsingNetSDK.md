# Deleting an Object Using the AWS SDK for \.NET<a name="DeletingOneObjectUsingNetSDK"></a>

When you delete an object from a non\-versioned bucket, the object is removed\. If you have versioning enabled on the bucket, you have the following options:
+ Delete a specific version of an object by specifying a version ID\.
+ Delete an object without specifying a version ID\. Amazon S3 adds a delete marker\. For more information about delete markers, see [Object Versioning](ObjectVersioning.md)\.

The following examples show how to delete an object from both versioned and non\-versioned buckets\. For more information about versioning, see [Object Versioning](ObjectVersioning.md)\. 

**Example Deleting an Object from a Non\-versioned Bucket**  
The following C\# example deletes an object from a non\-versioned bucket\. The example assumes that the objects don't have version IDs, so you don't specify version IDs\. You specify only the object key\. For information about how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.   

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

﻿using Amazon.S3;
using Amazon.S3.Model;
using System;
using System.Threading.Tasks;

namespace Amazon.DocSamples.S3
{
    class DeleteObjectNonVersionedBucketTest
    {
        private const string bucketName = "*** bucket name ***"; 
        private const string keyName = "*** object key ***";
        // Specify your bucket region (an example region is shown).
        private static readonly RegionEndpoint bucketRegion = RegionEndpoint.USWest2;
        private static IAmazonS3 client;

        public static void Main()
        {
            client = new AmazonS3Client(bucketRegion);
            DeleteObjectNonVersionedBucketAsync().Wait();
        }

        private static async Task DeleteObjectNonVersionedBucketAsync()
        {
            try
            {
                var deleteObjectRequest = new DeleteObjectRequest
                {
                    BucketName = bucketName,
                    Key = keyName
                };

                Console.WriteLine("Deleting an object");
                await client.DeleteObjectAsync(deleteObjectRequest);
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

**Example Deleting an Object from a Versioned Bucket**  
The following C\# example deletes an object from a versioned bucket\. It deletes a specific version of the object by specifying the object key name and version ID\.   
The code performs the following tasks:  

1. Enables versioning on a bucket that you specify \(if versioning is already enabled, this has no effect\)\.

1. Adds a sample object to the bucket\. In response, Amazon S3 returns the version ID of the newly added object\. The example uses this version ID in the delete request\.

1. Deletes the sample object by specifying both the object key name and a version ID\.
**Note**  
You can also get the version ID of an object by sending a `ListVersions` request:  

   ```
   var listResponse = client.ListVersions(new ListVersionsRequest { BucketName = bucketName, Prefix = keyName }); 
   ```
For information about how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.   

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

﻿using Amazon.S3;
using Amazon.S3.Model;
using System;
using System.Threading.Tasks;

namespace Amazon.DocSamples.S3
{
    class DeleteObjectVersion
    {
        private const string bucketName = "*** versioning-enabled bucket name ***";
        private const string keyName = "*** Object Key Name ***";
        // Specify your bucket region (an example region is shown).
        private static readonly RegionEndpoint bucketRegion = RegionEndpoint.USWest2;
        private static IAmazonS3 client;

        public static void Main()
        {
            client = new AmazonS3Client(bucketRegion);
            CreateAndDeleteObjectVersionAsync().Wait();
        }

        private static async Task CreateAndDeleteObjectVersionAsync()
        {
            try
            {
                // Add a sample object. 
                string versionID = await PutAnObject(keyName);

                // Delete the object by specifying an object key and a version ID.
                DeleteObjectRequest request = new DeleteObjectRequest
                {
                    BucketName = bucketName,
                    Key = keyName,
                    VersionId = versionID
                };
                Console.WriteLine("Deleting an object");
                await client.DeleteObjectAsync(request);
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

        static async Task<string> PutAnObject(string objectKey)
        {
            PutObjectRequest request = new PutObjectRequest
            {
                BucketName = bucketName,
                Key = objectKey,
                ContentBody = "This is the content body!"
            };
            PutObjectResponse response = await client.PutObjectAsync(request);
            return response.VersionId;
        }
    }
}
```