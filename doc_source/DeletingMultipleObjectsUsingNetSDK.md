# Deleting Multiple Objects Using the AWS SDK for \.NET<a name="DeletingMultipleObjectsUsingNetSDK"></a>

The following tasks guide you through using the AWS SDK for \.NET classes to delete multiple objects in a single HTTP request\. 


**Deleting Multiple Objects \(Non\-Versioned Bucket\)**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `AmazonS3Client` class\.   | 
|  2  |  Create an instance of the `DeleteObjectsRequest` class and provide list of the object keys you want to delete\.  | 
|  3  |  Execute the `AmazonS3Client.DeleteObjects` method\.  If one or more objects fail to delete, Amazon S3 throws a `DeleteObjectsException`\.  | 

The following C\# code sample demonstrates the preceding steps\. 

```
 1. DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest();
 2. multiObjectDeleteRequest.BucketName = bucketName;
 3.    
 4. multiObjectDeleteRequest.AddKey("<object Key>", null); // version ID is null.
 5. multiObjectDeleteRequest.AddKey("<object Key>", null);
 6. multiObjectDeleteRequest.AddKey("<object Key>", null);
 7. 
 8. try
 9. {
10.   DeleteObjectsResponse response = client.DeleteObjects(multiObjectDeleteRequest);
11.   Console.WriteLine("Successfully deleted all the {0} items", response.DeletedObjects.Count);
12. }
13. catch (DeleteObjectsException e)
14. {
15.   // Process exception.
16. }
```

The DeleteObjectsRequest can also take the list of KeyVersion objects as parameter\. For bucket without versioning, version ID is null\.

```
List<KeyVersion> keys = new List<KeyVersion>();
KeyVersion keyVersion = new KeyVersion
  {
       Key = key,
       VersionId = null // For buckets without versioning.
  };

keys.Add(keyVersion);
List<KeyVersion> keys = new List<KeyVersion>(); 
...
DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest
{
    BucketName = bucketName, 
    Objects = keys // This includes the object keys and null version IDs.
};
```

In the event of an exception, you can review the `DeleteObjectsException` to determine which objects failed to delete and why as shown in the following C\# code example\.

```
1. DeleteObjectsResponse errorResponse = e.Response;
2. Console.WriteLine("No. of objects successfully deleted = {0}", errorResponse.DeletedObjects.Count);
3. Console.WriteLine("No. of objects failed to delete = {0}", errorResponse.DeleteErrors.Count);
4. Console.WriteLine("Printing error data...");
5. foreach (DeleteError deleteError in errorResponse.DeleteErrors)
6. {
7.    Console.WriteLine("Object Key: {0}\t{1}\t{2}", deleteError.Key, deleteError.Code, deleteError.Message);
8. }
```

The following tasks guide you through deleting objects from a version\-enabled bucket\. 


**Deleting Multiple Objects \(Version\-Enabled Bucket\)**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `AmazonS3Client` class\.   | 
|  2  |  Create an instance of the `DeleteObjectsRequest` class and provide a list of object keys and optionally the version IDs of the objects that you want to delete\. If you specify the version ID of the object you want to delete, Amazon S3 deletes the specific object version\. If you don't specify the version ID of the object that you want to delete, Amazon S3 adds a delete marker\. For more information, see [Deleting One Object Per Request](DeletingOneObject.md)\.  | 
|  3  |  Execute the `AmazonS3Client.DeleteObjects` method\.  | 

The following C\# code sample demonstrates the preceding steps\. 

```
 1. List<KeyVersion> keysAndVersions = new List<KeyVersion>();
 2. // provide a list of object keys and versions.
 3. 
 4. DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest
 5.   {
 6.     BucketName = bucketName,
 7.     Objects = keysAndVersions 
 8.   };
 9. 
10. try
11. {
12.   DeleteObjectsResponse response = client.DeleteObjects(multiObjectDeleteRequest);
13.   Console.WriteLine("Successfully deleted all the {0} items", response.DeletedObjects.Count);
14. }
15. catch (DeleteObjectsException e)
16. {
17.   // Process exception.
18. }
```

**Example 1: Multi\-Object Delete \(Non\-Versioned Bucket\)**  
The following C\# code example uses the Multi\-Object API to delete objects from a bucket that is not version\-enabled\. The example first uploads the sample objects to the bucket and then uses the `DeleteObjects` method to delete the objects in a single request\. In the `DeleteObjectsRequest`, the example specifies only the object key names because the version IDs are null\.  
For information about how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.  

```
using System;
using System.Collections.Generic;
using Amazon.S3;
using Amazon.S3.Model;

namespace s3.amazon.com.docsamples
{
    class DeleteMultipleObjects
    {
        static string bucketName = "*** Provide a bucket name ***";
        static IAmazonS3 client;

        public static void Main(string[] args)
        {
            using (client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1))
            {
                var keysAndVersions = PutObjects(3);
                // Delete the objects.
                MultiObjectDelete(keysAndVersions);
            }

            Console.WriteLine("Click ENTER to continue.....");
            Console.ReadLine();
        }

        static void MultiObjectDelete(List<KeyVersion> keys)
        {
            // a. multi-object delete by specifying the key names and version IDs.
            DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest
            {
                BucketName = bucketName, 
                Objects = keys // This includes the object keys and null version IDs.
            };
            multiObjectDeleteRequest.AddKey("AWSSDKcopy2.dll", null);
            try
            {
                DeleteObjectsResponse response = client.DeleteObjects(multiObjectDeleteRequest);
                Console.WriteLine("Successfully deleted all the {0} items", response.DeletedObjects.Count);
            }
            catch (DeleteObjectsException e)
            {
                PrintDeletionReport(e);
            }
        }

        private static void PrintDeletionReport(DeleteObjectsException e)
        {
            // var errorResponse = e.ErrorResponse;
            DeleteObjectsResponse errorResponse = e.Response;
            Console.WriteLine("x {0}", errorResponse.DeletedObjects.Count);

            Console.WriteLine("No. of objects successfully deleted = {0}", errorResponse.DeletedObjects.Count);
            Console.WriteLine("No. of objects failed to delete = {0}", errorResponse.DeleteErrors.Count);

            Console.WriteLine("Printing error data...");
            foreach (DeleteError deleteError in errorResponse.DeleteErrors)
            {
                Console.WriteLine("Object Key: {0}\t{1}\t{2}", deleteError.Key, deleteError.Code, deleteError.Message);
            }
        }

        static List<KeyVersion> PutObjects(int number)
        {
            List<KeyVersion> keys = new List<KeyVersion>();
            for (int i = 0; i < number; i++)
            {
                string key = "ExampleObject-" + new System.Random().Next();
                PutObjectRequest request = new PutObjectRequest
                {
                    BucketName = bucketName,
                    Key = key,
                    ContentBody = "This is the content body!",

                };

                PutObjectResponse response = client.PutObject(request);
                KeyVersion keyVersion = new KeyVersion
                {
                     Key = key,
                     // For non-versioned bucket operations, we only need object key.
                     // VersionId = response.VersionId
                };
                keys.Add(keyVersion);
            }
            return keys;
        }
    }
}
```

**Example 2: Multi\-Object Delete \(Version\-Enabled Bucket\)**  
The following C\# code example uses the Multi\-Object API to delete objects from a version\-enabled bucket\. In addition to showing the DeleteObjects Multi\-Object Delete API usage, it also illustrates how versioning works in a version\-enabled bucket\.  
Before you can test the sample, you must create a sample bucket and provide the bucket name in the example\. You can use the AWS Management Console to create a bucket\.   
The example performs the following actions:  

1.  Enable versioning on the bucket\. 

1.  Perform a versioned\-delete\.

   The example first uploads the sample objects\. In response, Amazon S3 returns the version IDs for each sample object that you uploaded\. The example then deletes these objects using the Multi\-Object Delete API\. In the request, it specifies both the object keys and the version IDs \(that is, versioned delete\)\. 

1. Perform a non\-versioned delete\. 

   The example uploads the new sample objects\. Then, it deletes the objects using the Multi\-Object API\. However, in the request, it specifies only the object keys\. In this case, Amazon S3 adds the delete markers and the objects disappear from your bucket\.

1. Delete the delete markers\. 

   To illustrate how the delete markers work, the sample deletes the delete markers\. In the Multi\-Object Delete request, it specifies the object keys and the version IDs of the delete markers it received in the response in the preceding step\. This action makes the objects reappear in your bucket\.
For information about how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.  

```
using System;
using System.Collections.Generic;
using Amazon.S3;
using Amazon.S3.Model;

namespace s3.amazon.com.docsamples
{
    class DeleteMultipleObjectsVersionedBucket
    {
        static string bucketName = "*** Provide a bucket name ***";
        static IAmazonS3 client;

        public static void Main(string[] args)
        {
            using (client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1))
            {

                // 1. Enable versioning on the bucket.
                EnableVersioningOnBucket(bucketName);

                // 2a. Upload the sample objects.
                var keysAndVersions1 = PutObjects(3);
                // 2b. Delete the specific object versions.
                VersionedDelete(keysAndVersions1);

                // 3a. Upload the sample objects.
                var keysAndVersions2 = PutObjects(3);

                // 3b. Delete objects using only keys. Amazon S3 creates a delete marker and 
                // returns its version Id in the response.
                List<DeletedObject> deletedObjects = NonVersionedDelete(keysAndVersions2);

                // 3c. Additional exercise - using a multi-object versioned delete, remove the 
                // delete markers received in the preceding response. This results in your objects 
                // reappearing in your bucket.
                RemoveMarkers(deletedObjects);
            }

            Console.WriteLine("Click ENTER to continue.....");
            Console.ReadLine();
        }

        private static void PrintDeletionReport(DeleteObjectsException e)
        {
            var errorResponse = e.Response;
            Console.WriteLine("No. of objects successfully deleted = {0}", errorResponse.DeletedObjects.Count);
            Console.WriteLine("No. of objects failed to delete = {0}", errorResponse.DeleteErrors.Count);
            Console.WriteLine("Printing error data...");
            foreach (DeleteError deleteError in errorResponse.DeleteErrors)
            {
                Console.WriteLine("Object Key: {0}\t{1}\t{2}", deleteError.Key, deleteError.Code, deleteError.Message);
            }
        }

        static void EnableVersioningOnBucket(string bucketName)
        {
            PutBucketVersioningRequest setBucketVersioningRequest = new PutBucketVersioningRequest
            {
                BucketName = bucketName,
                VersioningConfig = new S3BucketVersioningConfig { Status = VersionStatus.Enabled }
            };
            client.PutBucketVersioning(setBucketVersioningRequest);
        }

        static void VersionedDelete(List<KeyVersion> keys)
        {
            // a. Perform a multi-object delete by specifying the key names and version IDs.
            DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest
            {
                BucketName = bucketName,
                Objects = keys // This includes the object keys and specific version IDs.
            };
            try
            {
                Console.WriteLine("Executing VersionedDelete...");
                DeleteObjectsResponse response = client.DeleteObjects(multiObjectDeleteRequest);
                Console.WriteLine("Successfully deleted all the {0} items", response.DeletedObjects.Count);
            }
            catch (DeleteObjectsException e)
            {
                PrintDeletionReport(e);
            }
        }

        static List<DeletedObject> NonVersionedDelete(List<KeyVersion> keys)
        {
            // Create a request that includes only the object key names.
            DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest();
            multiObjectDeleteRequest.BucketName = bucketName;

            foreach (var key in keys)
            {
                multiObjectDeleteRequest.AddKey(key.Key);
            }
            // Execute DeleteObjects - Amazon S3 add delete marker for each object
            // deletion. The objects disappear from your bucket. 
            // You can verify that using the Amazon S3 console.
            DeleteObjectsResponse response;
            try
            {
                Console.WriteLine("Executing NonVersionedDelete...");
                response = client.DeleteObjects(multiObjectDeleteRequest);
                Console.WriteLine("Successfully deleted all the {0} items", response.DeletedObjects.Count);
            }
            catch (DeleteObjectsException e)
            {
                PrintDeletionReport(e);
                throw; // Some deletes failed. Investigate before continuing.
            }
            // This response contains the DeletedObjects list which we use to delete the delete markers.
            return response.DeletedObjects; 
        }

        private static void RemoveMarkers(List<DeletedObject> deletedObjects)
        {
            List<KeyVersion> keyVersionList = new List<KeyVersion>();

            foreach (var deletedObject in deletedObjects)
            {
                KeyVersion keyVersion = new KeyVersion
                {
                    Key = deletedObject.Key,
                    VersionId = deletedObject.DeleteMarkerVersionId
                };
                keyVersionList.Add(keyVersion);
            }
            // Create another request to delete the delete markers.
            var multiObjectDeleteRequest = new DeleteObjectsRequest
            {
                BucketName = bucketName,
                Objects = keyVersionList
            };

            // Now, delete the delete marker to bring your objects back to the bucket.
            try
            {
                Console.WriteLine("Removing the delete markers .....");
                var deleteObjectResponse = client.DeleteObjects(multiObjectDeleteRequest);
                Console.WriteLine("Successfully deleted all the {0} delete markers", 
                                            deleteObjectResponse.DeletedObjects.Count);
            }
            catch (DeleteObjectsException e)
            {
                PrintDeletionReport(e);
            }
        }

        static List<KeyVersion> PutObjects(int number)
        {
            List<KeyVersion> keys = new List<KeyVersion>();

            for (int i = 0; i < number; i++)
            {
                string key = "ObjectToDelete-" + new System.Random().Next();
                PutObjectRequest request = new PutObjectRequest
                {
                    BucketName = bucketName,
                    Key = key,
                    ContentBody = "This is the content body!",

                };

                PutObjectResponse response = client.PutObject(request);
                KeyVersion keyVersion = new KeyVersion
                {
                     Key = key,
                     VersionId = response.VersionId
                };

                keys.Add(keyVersion);
            }
            return keys;
        }
    }
}
```