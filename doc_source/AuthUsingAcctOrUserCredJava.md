# Making Requests Using AWS Account or IAM User Credentials \- AWS SDK for Java<a name="AuthUsingAcctOrUserCredJava"></a>

The following tasks guide you through using the Java classes to send authenticated requests using your AWS account credentials or IAM user credentials\. 


**Making Requests Using Your AWS account or IAM user credentials**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `AmazonS3Client` class\.   | 
|  2  |  Execute one of the `AmazonS3Client` methods to send requests to Amazon S3\. The client generates the necessary signature value from your credentials and includes it in the request it sends to Amazon S3\.   | 

The following Java code sample demonstrates the preceding tasks\.

**Example**  

```
1. AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());        
2. 
3. // Send sample request (list objects in a given bucket).
4. ObjectListing objectListing = s3client.listObjects(new 
5.      ListObjectsRequest().withBucketName(bucketName));
```

**Note**  
You can create the AmazonS3Client class without providing your security credentials\. Requests sent using this client are anonymous requests, without a signature\. Amazon S3 returns an error if you send anonymous requests for a resource that is not publicly available\.

To see how to make requests using your AWS credentials within the context of an example of listing all the object keys in your bucket, see [Listing Keys Using the AWS SDK for Java](ListingObjectKeysUsingJava.md)\. For more examples, see [Working with Amazon S3 Objects](UsingObjects.md) and [Working with Amazon S3 Buckets](UsingBucket.md)\. You can test these examples using your AWS Account or IAM user credentials\. 

## Related Resources<a name="RelatedResources002"></a>
+ [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)