# Reducing the cost of SSE\-KMS with Amazon S3 Bucket Keys<a name="bucket-key"></a>

Amazon S3 Bucket Keys reduce the cost of Amazon S3 server\-side encryption using AWS Key Management Service \(SSE\-KMS\)\. This new bucket\-level key for SSE can reduce AWS KMS request costs by up to 99 percent by decreasing the request traffic from Amazon S3 to AWS KMS\. With a few clicks in the AWS Management Console, and without any changes to your client applications, you can configure your bucket to use an S3 Bucket Key for AWS KMS\-based encryption on new objects\.

## S3 Bucket Keys for SSE\-KMS<a name="bucket-key-overview"></a>

Workloads that access millions or billions of objects encrypted with SSE\-KMS can generate large volumes of requests to AWS KMS\. When you use SSE\-KMS to protect your data without an S3 Bucket Key, Amazon S3 uses an individual AWS KMS [data key](https://docs.aws.amazon.com/kms/latest/developerguide/concepts.html#data-keys) for every object\. It makes a call to AWS KMS every time a request is made against a KMS\-encrypted object\. For information about how SSE\-KMS works, see [Protecting data with server\-side encryption using AWS KMS CMKs \(SSE\-KMS\)](UsingKMSEncryption.md)\. 

When you configure your bucket to use an S3 Bucket Key for SSE\-KMS on new objects, AWS KMS generates a bucket\-level key that is used to create unique data keys for objects in the bucket\. This S3 Bucket Key is used for a time\-limited period within Amazon S3, reducing the need for Amazon S3 to make requests to AWS KMS to complete encryption operations\. This reduces traffic from S3 to AWS KMS, allowing you to access AWS KMS\-encrypted objects in S3 at a fraction of the previous cost\. 

Amazon S3 will only share an S3 Bucket Key for objects accessed by the same AWS KMS customer master key \(CMK\)\.

![\[Diagram showing AWS KMS generating a bucket key that creates data keys for objects in a bucket in S3.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/S3-Bucket-Keys.png)

## Configuring S3 Bucket Keys<a name="configure-bucket-key"></a>

You can configure your bucket to use an S3 Bucket Key for SSE\-KMS on new objects through the Amazon S3 console, AWS SDKs, AWS CLI, or REST API\. You can also override the S3 Bucket Key configuration for specific objects in a bucket with an individual per\-object KMS key using the REST API, AWS SDK, or AWS CLI\. You can also view S3 Bucket Key settings\. 

Before you configure your bucket to use an S3 Bucket Key, review [Changes to note before enabling an S3 Bucket Key](#bucket-key-changes)\. 

### Configuring an S3 Bucket Key using the Amazon S3 console<a name="configure-bucket-key-console"></a>

When you create a new bucket, you can configure your bucket to use an S3 Bucket Key for SSE\-KMS on new objects\. You can also configure an existing bucket to use an S3 Bucket Key for SSE\-KMS on new objects by updating your bucket properties\. 

For more information, see [Configuring your bucket to use S3 Bucket Keys using the S3 console](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-bucket-key.html) in the *Amazon Simple Storage Service Console User Guide*\.

### REST API, AWS CLI, and AWS SDK support for S3 Bucket Keys<a name="configure-bucket-key-programmatic"></a>

You can use the REST API, AWS CLI, or AWS SDK to configure your bucket to use an S3 Bucket Key for SSE\-KMS on new objects\. You can also enable an S3 Bucket Key at the object level\.

For more information, see the following: 
+ [Configuring an S3 Bucket Key at the object level using the REST API, AWS SDKs, or AWS CLI](configuring-bucket-key-object.md)
+ [Configuring your bucket to use an S3 Bucket Key with SSE\-KMS for new objects](configuring-bucket-key.md)

The following APIs support S3 Bucket Keys for SSE\-KMS:
+ [PutBucketEncryption](https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutBucketEncryption.html)
  + `ServerSideEncryptionRule` accepts the `BucketKeyEnabled` parameter for enabling and disabling an S3 Bucket Key\.
+ [GetBucketEncryption](https://docs.aws.amazon.com/AmazonS3/latest/API/API_GetBucketEncryption.html)
  + `ServerSideEncryptionRule` returns the settings for `BucketKeyEnabled`\.
+ [PutObject](https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutObject.html), [CopyObject](https://docs.aws.amazon.com/AmazonS3/latest/API/API_CopyObject.html), [CreateMutlipartUpload](https://docs.aws.amazon.com/AmazonS3/latest/API/API_CreateMultipartUpload.html), and [PostObject](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html)
  + `x-amz-server-side-encryption-bucket-key-enabled` request header enables or disables an S3 Bucket Key at the object level\.
+ [HeadObject](https://docs.aws.amazon.com/AmazonS3/latest/API/API_HeadObject.html), [GetObject](https://docs.aws.amazon.com/AmazonS3/latest/API/API_GetObject.html), [UploadPartCopy](https://docs.aws.amazon.com/AmazonS3/latest/API/API_UploadPartCopy.html), [UploadPart](https://docs.aws.amazon.com/AmazonS3/latest/API/API_UploadPart.html), and [CompleteMultipartUpload](https://docs.aws.amazon.com/AmazonS3/latest/API/API_CompleteMultipartUpload.html)
  + `x-amz-server-side-encryption-bucket-key-enabled` response header indicates if an S3 Bucket Key is enabled or disabled for an object\.

### Working with AWS CloudFormation<a name="configure-bucket-key-cfn"></a>

In AWS CloudFormation, the `AWS::S3::Bucket` resource includes an encryption property called `BucketKeyEnabled` that you can use to enable or disable an S3 Bucket Key\. 

For more information, see [Using AWS CloudFormation](configuring-bucket-key.md#enable-bucket-key-cloudformation)\.

## Changes to note before enabling an S3 Bucket Key<a name="bucket-key-changes"></a>

After you enable an S3 Bucket Key, key material is used for a time\-limited period within Amazon S3, and Amazon S3 decreases requests to AWS KMS\. Additionally, Amazon S3 uses the bucket Amazon Resource Name \(ARN\) as the encryption context instead of the object ARN\. 

After you enable an S3 Bucket Key, you see related changes in the following areas:

### IAM or KMS key policies<a name="bucket-key-policies"></a>

If your existing IAM policies or AWS KMS key policies use your object Amazon Resource Name \(ARN\) as the encryption context to refine or limit access to your AWS KMS CMKs, these policies won’t work with an S3 Bucket Key\. S3 Bucket Keys use the bucket ARN as encryption context\. Before you enable an S3 Bucket Key, update your IAM policies or AWS KMS key policies to use your bucket ARN as encryption context\.

For more information about encryption context and S3 Bucket Keys, see [Encryption context \(x\-amz\-server\-side\-encryption\-context\)](KMSUsingRESTAPI.md#s3-kms-encryption-context)\.

### AWS KMS CloudTrail events<a name="bucket-key-cloudtrail"></a>

After you enable an S3 Bucket Key, your AWS KMS CloudTrail events log your bucket ARN instead of your object ARN\. Additionally, you see fewer KMS CloudTrail events for SSE\-KMS objects in your logs\. Because key material is time\-limited in Amazon S3, fewer requests are made to AWS KMS\.  

## Using an S3 Bucket Key with replication<a name="bucket-key-replication"></a>

You can use S3 Bucket Keys with Same\-Region Replication \(SRR\) and Cross\-Region Replication \(CRR\)\.

When Amazon S3 replicates an encrypted object, it generally preserves the encryption settings of the replica object in the destination bucket\. However, if the source object is not encrypted and your destination bucket uses default encryption or an S3 Bucket Key, Amazon S3 encrypts the object with the destination bucket’s configuration\. 

The following examples illustrate how an S3 Bucket Key works with replication\. For more information, see [Replicating objects created with server\-side encryption \(SSE\) using encryption keys stored in AWS KMS](replication-config-for-kms-objects.md)\. 

**Example 1 – Source object uses S3 Bucket Keys, destination bucket uses default encryption**  
If your source object uses an S3 Bucket Key but your destination bucket uses default encryption with SSE\-KMS, the replica object maintains its S3 Bucket Key encryption settings in the destination bucket\. The destination bucket still uses default encryption with SSE\-KMS\.   
 

**Example 2 – Source object is not encrypted, destination bucket uses an S3 Bucket Key with SSE\-KMS**  
If your source object is not encrypted and the destination bucket uses an S3 Bucket Key with SSE\-KMS, the source object is encrypted with an S3 Bucket Key using SSE\-KMS in the destination bucket\. This results in the `ETag` of the source object being different from the `ETag` of the replica object\. You must update applications that use the `ETag` to accommodate for this difference\.

## Working with S3 Bucket Keys<a name="using-bucket-key"></a>

For more information about enabling and working with S3 Bucket Keys, see the following sections:
+ [Configuring your bucket to use an S3 Bucket Key with SSE\-KMS for new objects](configuring-bucket-key.md)
+ [Configuring an S3 Bucket Key at the object level using the REST API, AWS SDKs, or AWS CLI](configuring-bucket-key-object.md)
+ [Viewing settings for an S3 Bucket Key ](viewing-bucket-key-settings.md)