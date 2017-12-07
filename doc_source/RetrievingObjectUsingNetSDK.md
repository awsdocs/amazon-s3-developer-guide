# Get an Object Using the AWS SDK for \.NET<a name="RetrievingObjectUsingNetSDK"></a>

The following tasks guide you through using the \.NET classes to retrieve an object or a portion of the object, and save it locally to a file\.


**Downloading Objects**  

|  |  | 
| --- |--- |
| 1 | Create an instance of the `AmazonS3` class\.  | 
| 2 | Execute one of the `AmazonS3.GetObject` methods\. You need to provide information such as bucket name, file path, or a stream\. You provide this information by creating an instance of the `GetObjectRequest` class\. | 
| 3 | Execute one of the `GetObjectResponse.WriteResponseStreamToFile` methods to save the stream to a file\. | 

The following C\# code example demonstrates the preceding tasks\. The examples saves the object to a file on your desktop\.

**Example**  

```
 1. static IAmazonS3 client;
 2. using (client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1)) 
 3. {
 4.     GetObjectRequest request = new GetObjectRequest 
 5.     {
 6.         BucketName = bucketName,
 7.         Key = keyName
 8.     };
 9. 
10.     using (GetObjectResponse response = client.GetObject(request))  
11.     {
12.         string dest = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.Desktop), keyName);
13.         if (!File.Exists(dest))
14.         {
15.             response.WriteResponseStreamToFile(dest);
16.         }
17.     }
18. }
```

Instead of reading the entire object you can read only the portion of the object data by specifying the byte range in the request, as shown in the following C\# code example\.

**Example**  

```
1. GetObjectRequest request = new GetObjectRequest 
2. {
3.     BucketName = bucketName,
4.     Key = keyName,
5.     ByteRange = new ByteRange(0, 10)
6. };
```

When retrieving an object, you can optionally override the response header values \(see [Getting Objects](GettingObjectsUsingAPIs.md)\) by using the `ResponseHeaderOverrides` object and setting the corresponding request property, as shown in the following C\# code example\. You can use this feature to indicate the object should be downloaded into a different filename that the object key name\. 

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
The following C\# code example retrieves an object from an Amazon S3 bucket\. From the response, the example reads the object data using the `GetObjectResponse.ResponseStream` property\. The example also shows how you can use the `GetObjectResponse.Metadata` collection to read object metadata\. If the object you retrieve has the `x-amz-meta-title` metadata, the code will print the metadata value\.  
For instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.  

```
using System;
using System.IO;
using Amazon.S3;
using Amazon.S3.Model;

namespace s3.amazon.com.docsamples
{
    class GetObject
    {
        static string bucketName = "*** bucket name ***";
        static string keyName    = "*** object key ***";
        static IAmazonS3 client;

        public static void Main(string[] args)
        {
            try
            {
                Console.WriteLine("Retrieving (GET) an object");
                string data = ReadObjectData();
            }
            catch (AmazonS3Exception s3Exception)
            {
                Console.WriteLine(s3Exception.Message,
                                  s3Exception.InnerException);
            }
            Console.WriteLine("Press any key to continue...");
            Console.ReadKey();
        }

        static string ReadObjectData()
        {
            string responseBody = "";

            using (client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1)) 
            {
                GetObjectRequest request = new GetObjectRequest 
                {
                    BucketName = bucketName,
                    Key = keyName
                };

                using (GetObjectResponse response = client.GetObject(request))  
                using (Stream responseStream = response.ResponseStream)
                using (StreamReader reader = new StreamReader(responseStream))
                {
                    string title = response.Metadata["x-amz-meta-title"];
                    Console.WriteLine("The object's title is {0}", title);

                    responseBody = reader.ReadToEnd();
                }
            }
            return responseBody;
        }
    }
}
```