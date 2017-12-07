# Upload an Object Using the AWS SDK for \.NET<a name="UploadObjSingleOpNET"></a>

The tasks in the following process guide you through using the \.NET classes to upload an object\. The API provides several variations, overloads, of the `PutObject` method to easily upload your data\.


**Uploading Objects**  

|  |  | 
| --- |--- |
| 1 | Create an instance of the `AmazonS3` class\.  | 
| 2 | Execute one of the `AmazonS3.PutObject`\. You need to provide information such as a bucket name, file path, or a stream\. You provide this information by creating an instance of the `PutObjectRequest` class\. | 

The following C\# code example demonstrates the preceding tasks\.

**Example**  

```
1. static IAmazonS3 client;
2. client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1);
3. PutObjectRequest request = new PutObjectRequest()
4. {
5.     BucketName = bucketName,
6.     Key = keyName,
7.     FilePath = filePath
8. };
9. PutObjectResponse response2 = client.PutObject(request);
```

**Example**  
The following C\# code example uploads an object\. The object data is provided as a text string in the code\. The example uploads the object twice\.   

+ The first `PutObjectRequest` specifies only the bucket name, key name, and a text string embedded in the code as sample object data\. 

+ The second `PutObjectRequest` provides additional information including the optional object metadata and a ContentType header\. The request specifies a file name to upload\.
Each successive call to `AmazonS3.PutObject` replaces the previous upload\. For instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.  

```
using System;
using Amazon.S3;
using Amazon.S3.Model;

namespace s3.amazon.com.docsamples
{
    class UploadObject
    {
        static string bucketName = "*** bucket name ***";
        static string keyName    = "*** key name when object is created ***";
        static string filePath   = "*** absolute path to a sample file to upload ***";

        static IAmazonS3 client;

        public static void Main(string[] args)
        {
            using (client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1))
            {
                Console.WriteLine("Uploading an object");
                WritingAnObject();
            }

            Console.WriteLine("Press any key to continue...");
            Console.ReadKey();
        }

        static void WritingAnObject()
        {
            try
            {
                PutObjectRequest putRequest1 = new PutObjectRequest
                {
                    BucketName = bucketName,
                    Key = keyName,
                    ContentBody = "sample text" 
                };

                PutObjectResponse response1 = client.PutObject(putRequest1);

                // 2. Put object-set ContentType and add metadata.
                PutObjectRequest putRequest2 = new PutObjectRequest
                {
                    BucketName = bucketName,
                    Key = keyName,
                    FilePath = filePath,
                    ContentType = "text/plain"
                };
                putRequest2.Metadata.Add("x-amz-meta-title", "someTitle");
                
                PutObjectResponse response2 = client.PutObject(putRequest2);

            }
            catch (AmazonS3Exception amazonS3Exception)
            {
                if (amazonS3Exception.ErrorCode != null &&
                    (amazonS3Exception.ErrorCode.Equals("InvalidAccessKeyId")
                    ||
                    amazonS3Exception.ErrorCode.Equals("InvalidSecurity")))
                {
                    Console.WriteLine("Check the provided AWS Credentials.");
                    Console.WriteLine(
                        "For service sign up go to http://aws.amazon.com/s3");
                }
                else
                {
                    Console.WriteLine(
                        "Error occurred. Message:'{0}' when writing an object"
                        , amazonS3Exception.Message);
                }
            }
        }
    }
}
```