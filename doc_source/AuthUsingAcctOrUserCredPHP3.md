# Making Requests Using AWS Account or IAM User Credentials \- Version 3 of the AWS SDK for PHP<a name="AuthUsingAcctOrUserCredPHP3"></a>

This section guides you through using a class from the AWS SDK for PHP to send authenticated requests using your AWS account or IAM user credentials\. 

**Note**  
 This topic assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\. 


**Making Requests Using Your AWS Account or IAM user Credentials**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of an Amazon S3 client by using the [Aws\\S3\\S3Client](http://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.S3.S3Client.html) class [constructor](http://docs.aws.amazon.com//aws-sdk-php/v3/api/class-Aws.S3.S3Client.html#___construct)\.  | 
|  2  |  Execute one of the `Aws\S3\S3Client` methods to send requests to Amazon S3\. For example, you can use the [Aws\\S3\\S3Client::listBuckets\(\)](http://docs.aws.amazon.com/aws-sdk-php/v3/api/api-s3-2006-03-01.html#listbuckets) method to send a request to list all of the buckets for your account\. The client API generates the necessary signature using your credentials and includes it in the request it sends to Amazon S3\.   | 

The following PHP code sample demonstrates the preceding tasks and illustrates how the client makes a request using your security credentials to list all of the buckets for your account\. 

**Example**  

```
1. use Aws\S3\S3Client;
2. 
3. // Instantiate the S3 client with your AWS credentials
4. $s3 = new S3Client();
5. 
6. $result = $s3->listBuckets();
```

For working examples, see [Operations on Objects](ObjectOperations.md)\. You can test these examples using your AWS account or IAM user credentials\. 

For an example of listing object keys in a bucket, see [Listing Keys Using the AWS SDK for PHP](ListingObjectKeysUsingPHP.md)\. 

## Related Resources<a name="RelatedResources-AuthUsingAcctOrUserCredPHP3-related-resources"></a>
+ [ AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.S3.S3Client.html) 
+ [AWS SDK for PHP Documentation](http://aws.amazon.com/documentation/sdk-for-php/)