# Viewing settings for an S3 Bucket Key<a name="viewing-bucket-key-settings"></a>

You can view settings for an S3 Bucket Key at the bucket or object level using the Amazon S3 console, REST API, AWS CLI, or AWS SDKs\.

S3 Bucket Keys decrease request traffic from Amazon S3 to AWS KMS and reduce the cost of server\-side encryption using AWS Key Management Service \(SSE\-KMS\)\. For more information, see [Reducing the cost of SSE\-KMS with Amazon S3 Bucket Keys](bucket-key.md)\. 

## Using the Amazon S3 console<a name="bucket-key-settings-console"></a>

You can use the S3 console to view settings for an S3 Bucket Key at the bucket or object level\. For more information, see [Configuring an S3 Bucket Key in the S3 console](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/s3-bucket-key.html)\.

## Using the REST API<a name="bucket-key-settings-rest"></a>

**To return bucket\-level S3 Bucket Key settings**  
To return encryption information for a bucket, including settings for an S3 Bucket Key, use the `GetBucketEncryption` operation\. S3 Bucket Key settings are returned in the response body in the `ServerSideEncryptionConfiguration` with the `BucketKeyEnabled` setting\. For more information, see [GetBucketEncryption](https://docs.aws.amazon.com/AmazonS3/latest/API/API_GetBucketEncryption.html) in the *Amazon S3 API Reference*\. 

**To return object\-level settings for an S3 Bucket Key**  
To return the S3 Bucket Key status for an object, use the `HeadObject` operation\. `HeadObject` returns the `x-amz-server-side-encryption-bucket-key-enabled` response header to show whether an S3 Bucket Key is enabled or disabled for the object\. For more information, see [HeadObject](https://docs.aws.amazon.com/AmazonS3/latest/API/API_HeadObject.html) in the *Amazon S3 API Reference*\. 

The following API operations also return the `x-amz-server-side-encryption-bucket-key-enabled` response header if an S3 Bucket Key is configured for an object: 
+ [PutObject](https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutObject.html) 
+ [PostObject](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html) 
+ [CopyObject](https://docs.aws.amazon.com/AmazonS3/latest/API/API_CopyObject.html) 
+ [CreateMultipartUpload](https://docs.aws.amazon.com/AmazonS3/latest/API/API_CreateMultipartUpload.html) 
+ [UploadPartCopy](https://docs.aws.amazon.com/AmazonS3/latest/API/API_UploadPartCopy.html) 
+ [UploadPart](https://docs.aws.amazon.com/AmazonS3/latest/API/API_UploadPart.html) 
+ [CompleteMultipartUpload](https://docs.aws.amazon.com/AmazonS3/latest/API/API_CompleteMultipartUpload.html) 
+ [GetObject](https://docs.aws.amazon.com/AmazonS3/latest/API/API_GetObject.html) 