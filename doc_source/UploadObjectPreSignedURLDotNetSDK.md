# Upload an object using a presigned URL \(AWS SDK for \.NET\)<a name="UploadObjectPreSignedURLDotNetSDK"></a>

The following C\# example shows how to use the AWS SDK for \.NET to upload an object to an S3 bucket using a presigned URL\. For more information about presigned URLs, see [Uploading objects using presigned URLs](PresignedUrlUploadObject.md)\.

This example generates a presigned URL for a specific object and uses it to upload a file\. For information about the example's compatibility with a specific version of the AWS SDK for \.NET and instructions about how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.

```
using Amazon;
using Amazon.S3;
using Amazon.S3.Model;
using System;
using System.IO;
using System.Net;

namespace Amazon.DocSamples.S3
{
    class UploadObjectUsingPresignedURLTest
    {
        private const string bucketName = "*** provide bucket name ***";
        private const string objectKey  = "*** provide the name for the uploaded object ***";
        private const string filePath   = "*** provide the full path name of the file to upload ***";
        // Specify how long the presigned URL lasts, in hours
        private const double timeoutDuration = 12;
        // Specify your bucket region (an example region is shown).
        private static readonly RegionEndpoint bucketRegion = RegionEndpoint.USWest2; 
        private static IAmazonS3 s3Client;

        public static void Main()
        {
            s3Client = new AmazonS3Client(bucketRegion);
            var url = GeneratePreSignedURL(timeoutDuration);
            UploadObject(url);
        }

        private static void UploadObject(string url)
        {
            HttpWebRequest httpRequest = WebRequest.Create(url) as HttpWebRequest;
            httpRequest.Method = "PUT";
            using (Stream dataStream = httpRequest.GetRequestStream())
            {
                var buffer = new byte[8000];
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

        private static string GeneratePreSignedURL(double duration)
        {
            var request = new GetPreSignedUrlRequest
            {
                BucketName = bucketName,
                Key        = objectKey,
                Verb       = HttpVerb.PUT,
                Expires    = DateTime.UtcNow.AddHours(duration)
            };

           string url = s3Client.GetPreSignedURL(request);
           return url;
        }
    }
}
```

## More info<a name="UploadObjectPreSignedURLDotNetSDK-more-info"></a>

[AWS SDK for \.NET](https://aws.amazon.com/sdk-for-net/)