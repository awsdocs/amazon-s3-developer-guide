# Upload an Object Using a Pre\-Signed URL \(AWS SDK for \.NET\)<a name="UploadObjectPreSignedURLDotNetSDK"></a>

The following tasks guide you through using the \.NET classes to upload an object using a pre\-signed URL\.


**Uploading Objects**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `AmazonS3` class\.  These credentials are used in creating a signature for authentication when you generate a pre\-signed URL\.  | 
|  2  |  Generate a pre\-signed URL by executing the `AmazonS3.GetPreSignedURL` method\. You provide a bucket name, an object key, and an expiration date by creating an instance of the `GetPreSignedUrlRequest` class\. You must specify the HTTP verb PUT when creating this URL if you plan to use it to upload an object\.  | 
|  3  |  Anyone with the pre\-signed URL can upload an object\. You can create an instance of the `HttpWebRequest` class by providing the pre\-signed URL and uploading the object\.   | 

The following C\# code example demonstrates the preceding tasks\.

**Example**  

```
 1. IAmazonS3 client;
 2. client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1);
 3. // Generate a pre-signed URL.
 4. GetPreSignedUrlRequest request = new GetPreSignedUrlRequest
 5.    {
 6.        BucketName = bucketName,
 7.         Key        = objectKey,
 8.         Verb       = HttpVerb.PUT,
 9.         Expires    = DateTime.Now.AddMinutes(5)
10.     };
11. string url = null;
12.  url = client.GetPreSignedURL(request);
13. 
14. // Upload a file using the pre-signed URL.
15. HttpWebRequest httpRequest = WebRequest.Create(url) as HttpWebRequest;
16. httpRequest.Method = "PUT";
17. using (Stream dataStream = httpRequest.GetRequestStream())
18. {
19.    // Upload object.
20. }
21. 
22. HttpWebResponse response = httpRequest.GetResponse() as HttpWebResponse;
```

**Example**  
The following C\# code example generates a pre\-signed URL for a specific object and uses it to upload a file\. For instructions about how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.  

```
using System;
using System.IO;
using System.Net;
using Amazon.S3;
using Amazon.S3.Model;

namespace s3.amazon.com.docsamples
{
    class UploadObjectUsingPresignedURL
    {
        static IAmazonS3 s3Client;
        // File to upload.
        static string filePath   = "*** Specify file to upload ***";
        // Information to generate pre-signed object URL.
        static string bucketName = "*** Provide bucket name ***";
        static string objectKey  = "*** Provide object key for the new object ***";

        public static void Main(string[] args)
        {
            try
            {
                using (s3Client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1))
                {
                    string url = GeneratePreSignedURL();
                    UploadObject(url);

                }
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
                    "To sign up for service, go to http://aws.amazon.com/s3");
                }
                else
                {
                    Console.WriteLine(
                     "Error occurred. Message:'{0}' when listing objects",
                     amazonS3Exception.Message);
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
            Console.WriteLine("Press any key to continue...");
            Console.ReadKey();
        }


        static void UploadObject(string url)
        {
            HttpWebRequest httpRequest = WebRequest.Create(url) as HttpWebRequest;
            httpRequest.Method = "PUT";
            using (Stream dataStream = httpRequest.GetRequestStream())
            {
                byte[] buffer = new byte[8000];
                using (FileStream fileStream = new FileStream(filePath, FileMode.Open, FileAccess.Read))
                {
                    int bytesRead = 0;
                    while ((bytesRead = fileStream.Read(buffer, 0, buffer.Length)) > 0)
                    {
                        dataStream.Write(buffer, 0, bytesRead);
                    }
                }
            }

            HttpWebResponse response = httpRequest.GetResponse() as HttpWebResponse;
        }

        static string GeneratePreSignedURL()
        {
            GetPreSignedUrlRequest request = new GetPreSignedUrlRequest
                {
                    BucketName = bucketName,
                    Key        = objectKey,
                    Verb       = HttpVerb.PUT,
                    Expires    = DateTime.Now.AddMinutes(5)
                };
 
            string url = null;
            url = s3Client.GetPreSignedURL(request);
            return url;
        }
    }
}
```
