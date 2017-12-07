# Managing Tags Using SDK \(AWS SDK for \.NET\)<a name="tagging-manage-dotnet"></a>

The following C\# code example does the following:

+ Create an object with tags\.

+ Retrieve tag set\.

+ Update the tag set \(replace the existing tag set\)\.

For instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.

```
using System;
using Amazon.S3;
using Amazon.S3.Model;
using System.Collections.Generic;

namespace s3.amazon.com.docsamples
{
    class ObjectTaggingTest
    {
        static string bucketName = "*** bucket ****";
        static string keyName = "*** object key name ****";
        static string filePath = "***file to upload ***";

        static IAmazonS3 client;

        public static void Main(string[] args)
        {
            using (client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1))
            {
                Console.WriteLine("Uploading an object");
                PutObjectWithTagsTest();
            }

            Console.WriteLine("Press any key to continue...");
            Console.ReadKey();
        }

        static void PutObjectWithTagsTest()
        {
            try
            {
                // 1. Put object with tags.
                PutObjectRequest putRequest = new PutObjectRequest
                {
                    BucketName = bucketName,
                    Key = keyName,
                    FilePath = filePath,
                    TagSet = new List<Tag>{
                    new Tag { Key = "Key1", Value = "Value1"},
                    new Tag { Key = "Key2", Value = "Value2" }
                    }
                };

                PutObjectResponse response = client.PutObject(putRequest);
                // 2. Retrieve object tags.
                GetObjectTaggingRequest getTagsRequest = new GetObjectTaggingRequest();
                getTagsRequest.BucketName = bucketName;
                getTagsRequest.Key = keyName;

                GetObjectTaggingResponse objectTags = client.GetObjectTagging(getTagsRequest);

                // 3. Replace the tagset.

                Tagging newTagSet = new Tagging();
                newTagSet.TagSet = new List<Tag>{
                    new Tag { Key = "Key3", Value = "Value3"},
                    new Tag { Key = "Key4", Value = "Value4" }
                };


                PutObjectTaggingRequest putObjTagsRequest = new PutObjectTaggingRequest();
                putObjTagsRequest.BucketName = bucketName;
                putObjTagsRequest.Key = keyName;
                putObjTagsRequest.Tagging = newTagSet;

                PutObjectTaggingResponse response2 = client.PutObjectTagging(putObjTagsRequest);

                // 4. Retrieve object tags.
                GetObjectTaggingRequest getTagsRequest2 = new GetObjectTaggingRequest();
                getTagsRequest2.BucketName = bucketName;
                getTagsRequest2.Key = keyName;
                GetObjectTaggingResponse objectTags2 = client.GetObjectTagging(getTagsRequest2);

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