# Restore an Archived Object Using the AWS SDK for \.NET<a name="restore-object-dotnet"></a>

The following tasks guide you through using the AWS SDK for \.NET to initiate a restoration of an archived object\.


**Downloading Objects**  

|  |  | 
| --- |--- |
| 1 | Create an instance of the `AmazonS3` class\.  | 
| 2 | Create an instance of `RestoreObjectRequest` class by providing bucket name, object key to restore and the number of days for which you the object copy restored\. | 
| 3 | Execute one of the `AmazonS3.RestoreObject` methods to initiate the archive restoration\. | 

The following C\# code sample demonstrates the preceding tasks\.

**Example**  

```
 1. IAmazonS3 client;
 2. string bucketName = "examplebucket";
 3. string objectKey = "examplekey";
 4. 
 5. client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1);
 6. 
 7. RestoreObjectRequest restoreRequest = new RestoreObjectRequest()
 8.  {
 9.      BucketName = bucketName,
10.      Key = objectKey,
11.      Days = 2
12.  };
13. 
14. client.RestoreObject(restoreRequest);
```

Amazon S3 maintains the restoration status in the object metadata\. You can retrieve object metadata and check the value of the `RestoreInProgress` property as shown in the following C\# code snippet\.

```
 1. IAmazonS3 client;
 2. string bucketName = "examplebucket";
 3. string objectKey = "examplekey";
 4. 
 5. client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1);
 6. 
 7. GetObjectMetadataRequest metadataRequest = new GetObjectMetadataRequest()
 8. {
 9.       BucketName = bucketName,
10.       Key = objectKey
11. };
12. GetObjectMetadataResponse response = client.GetObjectMetadata(metadataRequest);
13. Console.WriteLine("Restoration status: {0}", response.RestoreInProgress);
14. if (response.RestoreInProgress == false)
15.     Console.WriteLine("Restored object copy expires on: {0}", response.RestoreExpiration);
```

**Example**  
The following C\# code example initiates a restoration request for the specified archived object\. You must update the code and provide a bucket name and an archived object key name\. For instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.  

```
using System;
using Amazon.S3;
using Amazon.S3.Model;

namespace s3.amazon.com.docsamples
{
    class RestoreArchivedObject
    {
        static string bucketName = "*** provide bucket name ***";
        static string objectKey  = "*** archived object keyname ***";

        static IAmazonS3 client;

        public static void Main(string[] args)
        {
            try
            {
                using (client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1))
                {
                    RestoreObject(client, bucketName, objectKey);
                    CheckRestorationStatus(client, bucketName, objectKey);
                }

                Console.WriteLine("Example complete. To continue, click Enter...");
                Console.ReadKey();
            }
            catch (AmazonS3Exception amazonS3Exception)
            {
                Console.WriteLine("S3 error occurred. Exception: " + amazonS3Exception.ToString());
            }
            catch (Exception e)
            {
                Console.WriteLine("Exception: " + e.ToString());
            }
        }

        static void RestoreObject(IAmazonS3 client, string bucketName, string objectKey)
        {
            RestoreObjectRequest restoreRequest = new RestoreObjectRequest
            {
                BucketName = bucketName,
                Key = objectKey,
                Days = 2
            };
            RestoreObjectResponse response = client.RestoreObject(restoreRequest);
        }

        static void CheckRestorationStatus(IAmazonS3 client, string bucketName, string objectKey)
        {
            GetObjectMetadataRequest metadataRequest = new GetObjectMetadataRequest
            {
                BucketName = bucketName,
                Key = objectKey
            };
            GetObjectMetadataResponse response = client.GetObjectMetadata(metadataRequest);
            Console.WriteLine("Restoration status: {0}", response.RestoreInProgress);
            if (response.RestoreInProgress == false)
                Console.WriteLine("Restored object copy expires on: {0}", response.RestoreExpiration);
        }
    }
}
```