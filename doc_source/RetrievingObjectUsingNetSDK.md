# Get an Object Using the AWS SDK for \.NET<a name="RetrievingObjectUsingNetSDK"></a>

When you download an object, you get all of the object's metadata and a stream from which to read the contents\. You should read the content of the stream as quickly as possible because the data is streamed directly from Amazon S3 and your network connection will remain open until you read all the data or close the input stream\. You do the following to get an object:
+ Execute the `getObject` method by providing bucket name and object key in the request\.
+ Execute one of the `GetObjectResponse` methods to process the stream\.

The following are some variations you might use:
+ Instead of reading the entire object, you can read only the portion of the object data by specifying the byte range in the request, as shown in the following C\# example:  
**Example**  

  ```
  1. GetObjectRequest request = new GetObjectRequest 
  2. {
  3.     BucketName = bucketName,
  4.     Key = keyName,
  5.     ByteRange = new ByteRange(0, 10)
  6. };
  ```
+ When retrieving an object, you can optionally override the response header values \(see [Getting Objects](GettingObjectsUsingAPIs.md)\) by using the `ResponseHeaderOverrides` object and setting the corresponding request property\. The following C\# code example shows how to do this\. For example, you can use this feature to indicate that the object should be downloaded into a file with a different filename that the object key name\.   
**Example**  

  ```
   1. GetObjectRequest request = new GetObjectRequest 
   2. {
   3.     BucketName = bucketName,
   4.     Key = keyName
   5. };
   6. 
   7. ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
   8. responseHeaders.CacheControl = "No-cache";
   9. responseHeaders.ContentDisposition = "attachment; filename=testing.txt";
  10. 
  11. request.ResponseHeaderOverrides = responseHeaders;
  ```

**Example**  
The following C\# code example retrieves an object from an Amazon S3 bucket\. From the response, the example reads the object data using the `GetObjectResponse.ResponseStream` property\. The example also shows how you can use the `GetObjectResponse.Metadata` collection to read object metadata\. If the object you retrieve has the `x-amz-meta-title` metadata, the code prints the metadata value\.  
For instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.  

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

﻿using Amazon.S3;
using Amazon.S3.Model;
using System;
using System.IO;
using System.Threading.Tasks;

namespace Amazon.DocSamples.S3
{
    class GetObjectTest
    {
        private const string bucketName = "*** bucket name ***";
        private const string keyName = "*** object key ***";
        // Specify your bucket region (an example region is shown).
        private static readonly RegionEndpoint bucketRegion = RegionEndpoint.USWest2;
        private static IAmazonS3 client;

        public static void Main()
        {
            client = new AmazonS3Client(bucketRegion);
            ReadObjectDataAsync().Wait();
        }

        static async Task ReadObjectDataAsync()
        {
            string responseBody = "";
            try
            {
                GetObjectRequest request = new GetObjectRequest
                {
                    BucketName = bucketName,
                    Key = keyName
                };
                using (GetObjectResponse response = await client.GetObjectAsync(request))
                using (Stream responseStream = response.ResponseStream)
                using (StreamReader reader = new StreamReader(responseStream))
                {
                    string title = response.Metadata["x-amz-meta-title"]; // Assume you have "title" as medata added to the object.
                    string contentType = response.Headers["Content-Type"];
                    Console.WriteLine("Object metadata, Title: {0}", title);
                    Console.WriteLine("Content type: {0}", contentType);

                    responseBody = reader.ReadToEnd(); // Now you process the response body.
                }
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