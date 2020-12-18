# Configuring an S3 Bucket Key at the object level using the REST API, AWS SDKs, or AWS CLI<a name="configuring-bucket-key-object"></a>

When you perform a PUT or COPY operation using the REST API, AWS SDKs, or AWS CLI, you can enable or disable an S3 Bucket Key at the object level\. S3 Bucket Keys reduce the cost of server\-side encryption using AWS Key Management Service \(AWS KMS\) \(SSE\-KMS\) by decreasing request traffic from Amazon S3 to AWS KMS\. For more information, see [Reducing the cost of SSE\-KMS with Amazon S3 Bucket Keys](bucket-key.md)\. 

When you configure an S3 Bucket Key for an object using a PUT or COPY operation, Amazon S3 only updates the settings for that object\. The S3 Bucket Key settings for the destination bucket do not change\. If you don't specify an S3 Bucket Key for your object, Amazon S3 applies the S3 Bucket Key settings for the destination bucket to the object\.

**Prerequisite:**  
Before you configure your object to use an S3 Bucket Key, review [Changes to note before enabling an S3 Bucket Key](bucket-key.md#bucket-key-changes)\. 

**Topics**
+ [Using the REST API](#bucket-key-object-rest)
+ [Using AWS SDK \(PutObject\)](#bucket-key-object-sdk)
+ [Using the AWS CLI](#bucket-key-object-cli)

## Using the REST API<a name="bucket-key-object-rest"></a>

When you use SSE\-KMS, you can enable an S3 Bucket Key for an object using the following APIs: 
+ [PutObject](https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutObject.html) – When you upload an object, you can specify the `x-amz-server-side-encryption-bucket-key-enabled` request header to enable or disable an S3 Bucket Key at the object level\. 
+ [CopyObject](https://docs.aws.amazon.com/AmazonS3/latest/API/API_CopyObject.html) – When you copy an object and configure SSE\-KMS, you can specify the `x-amz-server-side-encryption-bucket-key-enabled` request header to enable or disable an S3 Bucket Key for your object\. 
+ [PostObject](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html) – When you use a POST operation to upload an object and configure SSE\-KMS, you can use the `x-amz-server-side-encryption-bucket-key-enabled` form field to enable or disable an S3 Bucket Key for your object\.
+ [CreateMutlipartUpload](https://docs.aws.amazon.com/AmazonS3/latest/API/API_CreateMultipartUpload.html) – When you upload large objects using the multipart upload API and configure SSE\-KMS, you can use the `x-amz-server-side-encryption-bucket-key-enabled` request header to enable or disable an S3 Bucket Key for your object\.

To enable an S3 Bucket Key at the object level, include the `x-amz-server-side-encryption-bucket-key-enabled` request header\. For more information about SSE\-KMS and the REST API, see [Specifying the AWS Key Management Service in Amazon S3 Using the REST API](https://docs.aws.amazon.com/AmazonS3/latest/dev/KMSUsingRESTAPI.html)\.

## Using AWS SDK \(PutObject\)<a name="bucket-key-object-sdk"></a>

You can use the following example to configure an S3 Bucket Key at the object level using the AWS SDK for Java\.

------
#### [ Java ]

```
AmazonS3 s3client = AmazonS3ClientBuilder.standard()
    .withRegion(Regions.DEFAULT_REGION)
    .build();

String bucketName = "bucket name";
String keyName = "key name for object";
String contents = "file contents";

PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, keyName, contents)
    .withBucketKeyEnabled(true);
    
s3client.putObject(putObjectRequest);
```

------

## Using the AWS CLI<a name="bucket-key-object-cli"></a>

You can use the following AWS CLI example to configure an S3 Bucket Key at the object level as part of a `PutObject` request\.

```
aws s3api put-object --bucket <bucket name> --key <object key name> --server-side-encryption aws:kms --bucket-key-enabled —body <filepath>
```