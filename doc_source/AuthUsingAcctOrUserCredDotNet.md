# Making Requests Using AWS Account or IAM User Credentials \- AWS SDK for \.NET<a name="AuthUsingAcctOrUserCredDotNet"></a>

The following tasks guide you through using the \.NET classes to send authenticated requests using your AWS account or IAM user credentials\. 


**Making Requests Using Your AWS Account or IAM User Credentials**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `AmazonS3Client` class\.   | 
|  2  |  Execute one of the `AmazonS3Client` methods to send requests to Amazon S3\. The client generates the necessary signature from your credentials and includes it in the request it sends to Amazon S3\.   | 

The following C\# code sample demonstrates the preceding tasks\.

 For information on running the \.NET examples in this guide and for instructions on how to store your credentials in a configuration file, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.

**Example**  

```
using System;
using Amazon.S3;
using Amazon.S3.Model;

namespace s3.amazon.com.docsamples
{
    class MakeS3Request
    {
        static string bucketName        = "*** Provide bucket name ***";
        static IAmazonS3 client;

        public static void Main(string[] args)
        {
            using (client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1))
                {
                    Console.WriteLine("Listing objects stored in a bucket");
                    ListingObjects();
                }

            Console.WriteLine("Press any key to continue...");
            Console.ReadKey();
        }

        static void ListingObjects()
        {
            try
            {
                ListObjectsRequest request = new ListObjectsRequest
                {
                    BucketName = bucketName,
                    MaxKeys = 2
                };

                do
                {
                    ListObjectsResponse response = client.ListObjects(request);

                    // Process response.
                    foreach (S3Object entry in response.S3Objects)
                    {
                        Console.WriteLine("key = {0} size = {1}",
                            entry.Key, entry.Size);
                    }

                    // If response is truncated, set the marker to get the next 
                    // set of keys.
                    if (response.IsTruncated)
                    {
                        request.Marker = response.NextMarker;
                    }
                    else
                    {
                        request = null;
                    }
                } while (request != null);
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
        }
    }
}
```

**Note**  
You can create the `AmazonS3Client` client without providing your security credentials\. Requests sent using this client are anonymous requests, without a signature\. Amazon S3 returns an error if you send anonymous requests for a resource that is not publicly available\.

For working examples, see [Working with Amazon S3 Objects](UsingObjects.md) and [Working with Amazon S3 Buckets](UsingBucket.md)\. You can test these examples using your AWS Account or an IAM user credentials\. 

For example, to list all the object keys in your bucket, see [Listing Keys Using the AWS SDK for \.NET](ListingObjectKeysUsingNetSDK.md)\. 

## Related Resources<a name="RelatedResources003"></a>

+ [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)