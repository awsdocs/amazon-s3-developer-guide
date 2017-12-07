# Deleting an Object Using the AWS SDK for \.NET<a name="DeletingOneObjectUsingNetSDK"></a>

You can delete an object from a bucket\. If you have versioning enabled on the bucket, you can also delete a specific version of an object\. 

The following tasks guide you through using the \.NET classes to delete an object\. 


**Deleting an Object \(Non\-Versioned Bucket\)**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `AmazonS3Client` class by providing your AWS credentials\.   | 
|  2  |  Execute the `AmazonS3.DeleteObject` method by providing a bucket name and an object key in an instance of `DeleteObjectRequest`\. If you have not enabled versioning on the bucket, the operation deletes the object\. If you have enabled versioning, the operation adds a delete marker\. For more information, see [Deleting One Object Per Request](DeletingOneObject.md)\.  | 

The following C\# code sample demonstrates the preceding steps\. 

```
 1. static IAmazonS3 client;
 2. client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1);
 3. 
 4. DeleteObjectRequest deleteObjectRequest =
 5.     new DeleteObjectRequest
 6.         {
 7.             BucketName = bucketName,
 8.             Key = keyName
 9.         };
10. 
11. using (client = Amazon.AWSClientFactory.CreateAmazonS3Client(
12.      accessKeyID, secretAccessKeyID))
13. {
14.      client.DeleteObject(deleteObjectRequest);
15. }
```


**Deleting a Specific Version of an Object \(Version\-Enabled Bucket\)**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `AmazonS3Client` class by providing your AWS credentials\.   | 
|  2  |  Execute the `AmazonS3.DeleteObject` method by providing a bucket name, an object key name, and object version Id in an instance of `DeleteObjectRequest`\. The `DeleteObject` method deletes the specific version of the object\.  | 

The following C\# code sample demonstrates the preceding steps\.

**Example**  

```
 1. IAmazonS3 client
 2. client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1)
 3. 
 4. DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest
 5.    {
 6.         BucketName = bucketName,
 7.         Key = keyName,
 8.         VersionId = versionID
 9.    };
10. 
11. using (client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1))
12. {
13.    client.DeleteObject(deleteObjectRequest);
14.    Console.WriteLine("Deleting an object");
15. }
```

**Example 1: Deleting an Object \(Non\-Versioned Bucket\)**  
The following C\# code example deletes an object from a bucket\. It does not provide a version Id in the delete request\. If you have not enabled versioning on the bucket, Amazon S3 deletes the object\. If you have enabled versioning, Amazon S3 adds a delete marker and the object is not deleted\. For information about how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.   

```
using System;
using Amazon.S3;
using Amazon.S3.Model;

namespace s3.amazon.com.docsamples
{
    class DeleteObjectNonVersionedBucket
    {
        static string bucketName = "*** Provide a bucket name ***";
        static string keyName    = "*** Provide a key name ****";
        static IAmazonS3 client;

        public static void Main(string[] args)
        {
            using (client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1))
            {
                DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest
                {
                    BucketName = bucketName,
                    Key = keyName
                };
                try
                {
                    client.DeleteObject(deleteObjectRequest);
                    Console.WriteLine("Deleting an object");
                }
                catch (AmazonS3Exception s3Exception)
                {
                    Console.WriteLine(s3Exception.Message,
                                      s3Exception.InnerException);
                }
            }
            Console.WriteLine("Press any key to continue...");
            Console.ReadKey();
        }
    }
}
```

**Example 2: Deleting an Object \(Versioned Bucket\)**  
The following C\# code example deletes an object from a versioned bucket\. The `DeleteObjectRequest` instance specifies an object key name and a version ID\. The `DeleteObject` method removes the specific object version from the bucket\.   
To test the sample, you must provide a bucket name\. The code sample performs the following tasks:  

1. Enable versioning on the bucket\.

1. Add a sample object to the bucket\. In response, Amazon S3 returns the version ID of the newly added object\. You can also obtain version IDs of an object by sending a ListVersions request\.

   ```
   var listResponse = client.ListVersions(new ListVersionsRequest { BucketName = bucketName, Prefix = keyName }); 
   ```

1. Delete the sample object using the `DeleteObject` method\. The `DeleteObjectRequest` class specifies both an object key name and a version ID\.
For information about how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.   

```
using System;
using Amazon.S3;
using Amazon.S3.Model;

namespace s3.amazon.com.docsamples
{
    class DeleteObjectVersion
    {
        static string bucketName = "*** Provide a Bucket Name ***";
        static string keyName    = "*** Provide a Key Name ***";
        static IAmazonS3 client;

        public static void Main(string[] args)
        {
            using (client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1))
            {
                try
                {
                    // Make the bucket version-enabled.
                    EnableVersioningOnBucket(bucketName);

                    // Add a sample object. 
                    string versionID = PutAnObject(keyName);

                    // Delete the object by specifying an object key and a version ID.
                    DeleteObjectRequest request = new DeleteObjectRequest
                    {
                        BucketName = bucketName,
                        Key = keyName,
                        VersionId = versionID
                    };
                    Console.WriteLine("Deleting an object");
                    client.DeleteObject(request);

                }
                catch (AmazonS3Exception s3Exception)
                {
                    Console.WriteLine(s3Exception.Message,
                                      s3Exception.InnerException);
                }
            }
            Console.WriteLine("Press any key to continue...");
            Console.ReadKey();
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

        static string PutAnObject(string objectKey)
        {

            PutObjectRequest request = new PutObjectRequest
            {
                BucketName = bucketName,
                Key = objectKey,
                ContentBody = "This is the content body!"
            };

            PutObjectResponse response = client.PutObject(request);
            return response.VersionId;

        }
    }
}
```