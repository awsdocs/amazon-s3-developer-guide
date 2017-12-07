# Managing Websites with the AWS SDK for \.NET<a name="ConfigWebSiteDotNet"></a>

The following tasks guide you through using the \.NET classes to manage website configuration on your bucket\. For more information about the Amazon S3 website feature, see [Hosting a Static Website on Amazon S3](WebsiteHosting.md)\.


**Managing Bucket Website Configuration**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `AmazonS3Client` class\.   | 
|  2  |  To add website configuration to a bucket, execute the `PutBucketWebsite` method\. You need to provide the bucket name and the website configuration information, including the index document and the error document names\. You must provide the index document, but the error document is optional\. You provide this information by creating a `PutBucketWebsiteRequest` object\. To retrieve website configuration, execute the `GetBucketWebsite` method by providing the bucket name\. To delete your bucket website configuration, execute the `DeleteBucketWebsite` method by providing the bucket name\. After you remove the website configuration, the bucket is no longer available from the website endpoint\. For more information, see [Website Endpoints](WebsiteEndpoints.md)\.   | 

The following C\# code sample demonstrates the preceding tasks\.

**Example**  

```
 1. static IAmazonS3 client;
 2. client = new AmazonS3Client(Amazon.RegionEndpoint.USWest2);
 3. 
 4. // Add website configuration.
 5. PutBucketWebsiteRequest putRequest = new PutBucketWebsiteRequest()
 6. {
 7.     BucketName = bucketName,
 8.     WebsiteConfiguration = new WebsiteConfiguration()
 9.     {
10.         IndexDocumentSuffix = indexDocumentSuffix,
11.         ErrorDocument = errorDocument
12.     }
13. };
14. client.PutBucketWebsite(putRequest);
15. 
16. // Get bucket website configuration.
17. GetBucketWebsiteRequest getRequest = new GetBucketWebsiteRequest()
18. {
19.     BucketName = bucketName
20. };
21. 
22. GetBucketWebsiteResponse getResponse = client.GetBucketWebsite(getRequest);
23. 
24. // Print configuration data.
25. Console.WriteLine("Index document: {0}", getResponse.WebsiteConfiguration.IndexDocumentSuffix);
26. Console.WriteLine("Error document: {0}", getResponse.WebsiteConfiguration.ErrorDocument);
27. 
28. // Delete website configuration.
29. DeleteBucketWebsiteRequest deleteRequest = new DeleteBucketWebsiteRequest()
30. {
31.     BucketName = bucketName
32. };        
33. client.DeleteBucketWebsite(deleteRequest);
```

**Example**  
The following C\# code example adds a website configuration to the specified bucket\. The configuration specifies both the index document and the error document names\. For instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.  

```
using System;
using System.Configuration;
using System.Collections.Specialized;
using Amazon.S3;
using Amazon.S3.Model;

namespace s3.amazon.com.docsamples
{
    class AddWebsiteConfig
    {
        static string bucketName          = "*** Provide existing bucket name ***";
        static string indexDocumentSuffix = "*** Provide index document name ***";
        static string errorDocument       = "*** Provide error document name ***";
        static IAmazonS3 client;

        public static void Main(string[] args)
        {
            using (client = new AmazonS3Client(Amazon.RegionEndpoint.USWest2))
            {
                Console.WriteLine("Adding website configuration");
                AddWebsiteConfiguration(bucketName, indexDocumentSuffix, errorDocument); 
            }
            
            // Get bucket website configuration.
            GetBucketWebsiteRequest getRequest = new GetBucketWebsiteRequest()
            {
                BucketName = bucketName
            };

            GetBucketWebsiteResponse getResponse = client.GetBucketWebsite(getRequest);
           // Print configuration data.
            Console.WriteLine("Index document: {0}", getResponse.WebsiteConfiguration.IndexDocumentSuffix);
            Console.WriteLine("Error document: {0}", getResponse.WebsiteConfiguration.ErrorDocument);
           
            Console.WriteLine("Press any key to continue...");
            Console.ReadKey();
        }

        static void AddWebsiteConfiguration(string bucketName,
                                            string indexDocumentSuffix,
                                            string errorDocument)
        {
            try
            {
                PutBucketWebsiteRequest putRequest = new PutBucketWebsiteRequest()
                {
                    BucketName = bucketName,
                    WebsiteConfiguration = new WebsiteConfiguration()
                    {
                        IndexDocumentSuffix = indexDocumentSuffix,
                        ErrorDocument = errorDocument
                    }
                };
                client.PutBucketWebsite(putRequest);
            }
            catch (AmazonS3Exception amazonS3Exception)
            {
                if (amazonS3Exception.ErrorCode != null &&
                    (amazonS3Exception.ErrorCode.Equals("InvalidAccessKeyId")
                    || amazonS3Exception.ErrorCode.Equals("InvalidSecurity")))
                {
                    Console.WriteLine("Check the provided AWS Credentials.");
                    Console.WriteLine("Sign up for service at http://aws.amazon.com/s3");
                }
                else
                {
                    Console.WriteLine(
                        "Error:{0}, occurred when adding website configuration. Message:'{1}",
                        amazonS3Exception.ErrorCode, amazonS3Exception.Message);
                }
            }
        }
    }
}
```