# Copy an Object Using the AWS SDK for \.NET<a name="CopyingObjectUsingNetSDK"></a>

The following tasks guide you through using the high\-level \.NET classes to upload a file\. The API provides several variations, *overloads*, of the `Upload` method to easily upload your data\.


**Copying Objects**  

|  |  | 
| --- |--- |
| 1 | Create an instance of the `AmazonS3` class\.  | 
| 2 | Execute one of the `AmazonS3.CopyObject`\. You need to provide information such as source bucket, source key name, target bucket, and target key name\. You provide this information by creating an instance of the `CopyObjectRequest` class\. | 

The following C\# code example demonstrates the preceding tasks\.

**Example**  

```
 1. static IAmazonS3 client;
 2. client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1);
 3. 
 4. CopyObjectRequest request = new CopyObjectRequest()
 5. {
 6.     SourceBucket      = bucketName,
 7.     SourceKey         = objectKey,
 8.     DestinationBucket = bucketName,
 9.     DestinationKey    = destObjectKey
10. };
11. CopyObjectResponse response = client.CopyObject(request);
```

**Example**  
The following C\# code example makes a copy of an object\. You will need to update code and provide your bucket names, and object keys\. For instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.  

```
using System;
using Amazon.S3;
using Amazon.S3.Model;

namespace s3.amazon.com.docsamples
{
    class CopyObject
    {
        static string sourceBucket      = "*** Bucket on which to enable logging ***";
        static string destinationBucket = "*** Bucket where you want logs stored ***";
        static string objectKey         = "*** Provide key name ***";
        static string destObjectKey     = "*** Provide destination key name ***";
        static IAmazonS3 client;

        public static void Main(string[] args)
        {
            using (client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1))
                {
                    Console.WriteLine("Copying an object");
                    CopyingObject();
                }
            Console.WriteLine("Press any key to continue...");
            Console.ReadKey();
        }

        static void CopyingObject()
        {
            try
            {
                CopyObjectRequest request = new CopyObjectRequest
                {
                    SourceBucket      = sourceBucket,
                    SourceKey         = objectKey,
                    DestinationBucket = destinationBucket,
                    DestinationKey    = destObjectKey
                };
                CopyObjectResponse response = client.CopyObject(request);
            }
            catch (AmazonS3Exception s3Exception)
            {
                Console.WriteLine(s3Exception.Message,
                                  s3Exception.InnerException);
            }
        }
    }
}
```