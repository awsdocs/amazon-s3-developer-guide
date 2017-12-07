# Generate a Pre\-signed Object URL using AWS SDK for \.NET<a name="ShareObjectPreSignedURLDotNetSDK"></a>

The following tasks guide you through using the \.NET classes to generate a pre\-signed URL\.


**Downloading Objects**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `AmazonS3` class\. For information about providing your credentials see [Using the AWS SDK for \.NET](UsingTheMPDotNetAPI.md)\. These credentials are used in creating a signature for authentication when you generate a pre\-signed URL\.  | 
|  2  |  Execute the `AmazonS3.GetPreSignedURL` method to generate a pre\-signed URL\. You provide information including a bucket name, an object key, and an expiration date by creating an instance of the `GetPreSignedUrlRequest` class\.  | 

The following C\# code sample demonstrates the preceding tasks\.

**Example**  

```
 1. static IAmazonS3 s3Client;
 2. s3Client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1)
 3. 
 4. GetPreSignedUrlRequest request1 = new GetPreSignedUrlRequest()
 5. {
 6.      BucketName = bucketName,
 7.      Key = objectKey,
 8.      Expires = DateTime.Now.AddMinutes(5)
 9. };
10. 
11. string url = s3Client.GetPreSignedURL(request1);
```

**Example**  
The following C\# code example generates a pre\-signed URL for a specific object\. For instructions about how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.  

```
using System;
using Amazon.S3;
using Amazon.S3.Model;

namespace s3.amazon.com.docsamples
{
    class GeneratePresignedURL
    {
        static string bucketName ="*** Provide a bucket name ***";
        static string objectKey  = "*** Provide an object name ***";
        static IAmazonS3 s3Client;

        public static void Main(string[] args)
        {
  
            using (s3Client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1))
            {
                string urlString = GeneratePreSignedURL();
            }


            Console.WriteLine("Press any key to continue...");
            Console.ReadKey();
        }

        static string GeneratePreSignedURL()
        {
            string urlString = "";
            GetPreSignedUrlRequest request1 = new GetPreSignedUrlRequest
            {
                 BucketName = bucketName,
                 Key = objectKey,
                 Expires = DateTime.Now.AddMinutes(5)
                 
            };

            try
            {
                urlString = s3Client.GetPreSignedURL(request1);
                //string url = s3Client.GetPreSignedURL(request1);
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

            return urlString;

        }
    }
}
```