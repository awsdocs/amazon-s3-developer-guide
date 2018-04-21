# List Multipart Uploads Using the Low\-Level AWS SDK for PHP API<a name="LLlistMPuploadsPHP"></a>

This topic guides you through using the low\-level API classes from the AWS SDK for PHP to list all in\-progress multipart uploads on a bucket\.

**Note**  
 This topic assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\. 


**PHP SDK Low\-Level API Multipart Uploads Listing Process**  

|  |  | 
| --- |--- |
| 1 |  Create an instance of an Amazon S3 client by using the [Aws\\S3\\S3Client](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html) class [factory\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory) method\.  | 
| 2 | Execute the `` [Aws\\S3\\S3Client::listMultipartUploads\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_listMultipartUploads) method by providing a bucket name\. The method returns all of the in\-progress multipart uploads on the specified bucket\.  | 

The following PHP code sample demonstrates listing all in\-progress multipart uploads on a bucket\.

**Example**  

```
1. use Aws\S3\S3Client;
2. 
3. $s3 = S3Client::factory();
4. 
5. $bucket = '*** Your Bucket Name ***';
6. 
7. $result = $s3->listMultipartUploads(array('Bucket' => $bucket));
8. 
9. print_r($result->toArray());
```

## Related Resources<a name="RelatedResources-LLlistMPuploadsPHP"></a>
+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html)
+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::factory\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory)
+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::listMultipartUploads\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_listMultipartUploads)
+ [AWS SDK for PHP for Amazon S3](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/service-s3.html)
+ [AWS SDK for PHP Documentation](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/index.html)