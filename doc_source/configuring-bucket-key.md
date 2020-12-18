# Configuring your bucket to use an S3 Bucket Key with SSE\-KMS for new objects<a name="configuring-bucket-key"></a>

When you configure server\-side encryption using SSE\-KMS, you can configure your bucket to use an S3 Bucket Key for SSE\-KMS on new objects\. S3 Bucket Keys decrease the request traffic from Amazon S3 to AWS Key Management Service \(AWS KMS\) and reduce the cost of SSE\-KMS\. For more information, see [Reducing the cost of SSE\-KMS with Amazon S3 Bucket Keys](bucket-key.md)\.

You can configure your bucket to use an S3 Bucket Key for SSE\-KMS on new objects by using the Amazon S3 console, REST API, AWS SDK, AWS CLI, or AWS CloudFormation\. If you want to enable or disable an S3 Bucket Key for existing objects, you can use a COPY operation\. For more information, see [Configuring an S3 Bucket Key at the object level using the REST API, AWS SDKs, or AWS CLI](configuring-bucket-key-object.md)\. 

**Prerequisite:**  
Before you configure your bucket to use an S3 Bucket Key, review [Changes to note before enabling an S3 Bucket Key](bucket-key.md#bucket-key-changes)\.

**Topics**
+ [Using the Amazon S3 console](#enable-bucket-key-console)
+ [Using the REST API](#enable-bucket-key-rest)
+ [Using the AWS SDKs](#enable-bucket-key-sdk)
+ [Using the CLI](#enable-bucket-key-cli)
+ [Using AWS CloudFormation](#enable-bucket-key-cloudformation)

## Using the Amazon S3 console<a name="enable-bucket-key-console"></a>

You can use the Amazon S3 console to enable an S3 Bucket Key for a new or existing bucket\. For more information, see [Configuring an S3 Bucket Key in the S3 console](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/s3-bucket-key.html)\.

## Using the REST API<a name="enable-bucket-key-rest"></a>

You can use [PutBucketEncryption](https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutBucketEncryption.html) to enable or disable an S3 Bucket Key for your bucket\. To configure an S3 Bucket Key with `PutBucketEncryption`, specify the [ServerSideEncryptionRule](https://docs.aws.amazon.com/AmazonS3/latest/API/API_ServerSideEncryptionRule.html), which includes default encryption with server\-side encryption using AWS KMS customer master keys \(CMKs\)\. You can also optionally use a customer managed CMK by specifying the KMS key ID for the CMK\.  

For more information and example syntax, see [PutBucketEncryption](https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutBucketEncryption.html)\. 

## Using the AWS SDKs<a name="enable-bucket-key-sdk"></a>

The following example enables default bucket encryption with SSE\-KMS and an S3 Bucket Key using the AWS SDK for Java\.

------
#### [ Java ]

```
AmazonS3 s3client = AmazonS3ClientBuilder.standard()
    .withRegion(Regions.DEFAULT_REGION)
    .build();
    
ServerSideEncryptionByDefault serverSideEncryptionByDefault = new ServerSideEncryptionByDefault()
    .withSSEAlgorithm(SSEAlgorithm.KMS);
ServerSideEncryptionRule rule = new ServerSideEncryptionRule()
    .withApplyServerSideEncryptionByDefault(serverSideEncryptionByDefault)
    .withBucketKeyEnabled(true);
ServerSideEncryptionConfiguration serverSideEncryptionConfiguration =
    new ServerSideEncryptionConfiguration().withRules(Collections.singleton(rule));

SetBucketEncryptionRequest setBucketEncryptionRequest = new SetBucketEncryptionRequest()
    .withServerSideEncryptionConfiguration(serverSideEncryptionConfiguration)
    .withBucketName(bucketName);
            
s3client.setBucketEncryption(setBucketEncryptionRequest);
```

------

## Using the CLI<a name="enable-bucket-key-cli"></a>

The following example enables default bucket encryption with SSE\-KMS and an S3 Bucket Key using the AWS CLI\.

```
aws s3api put-bucket-encryption --bucket <bucket-name> --server-side-encryption-configuration '{
        "Rules": [
            {
                "ApplyServerSideEncryptionByDefault": {
                    "SSEAlgorithm": "aws:kms",
                    "KMSMasterKeyID": "<KMS-Key-ARN>"
                },
                "BucketKeyEnabled": true
            }
        ]
    }'
```

## Using AWS CloudFormation<a name="enable-bucket-key-cloudformation"></a>

When you use the [AWS::S3::Bucket](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-s3-bucket.html) resource to create a new bucket and specify default encryption with SSE\-KMS, you can configure your bucket to use an S3 Bucket Key for new objects in the bucket\. 

You configure the `BucketKeyEnabled` property as part of the [AWS::S3::Bucket ServerSideEncryptionRule](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-s3-bucket-serversideencryptionrule.html), which specifies the default server\-side encryption configuration for a bucket\.

**BucketKeyEnabled**  
Specifies whether Amazon S3 should use an S3 Bucket Key with server\-side encryption using AWS KMS \(SSE\-KMS\) for new objects in the bucket\. Existing objects are not affected\. Setting the `BucketKeyEnabled` element to `TRUE` causes Amazon S3 to use an S3 Bucket Key\. By default, it is not enabled\. For more information, see [Reducing the cost of SSE\-KMS with Amazon S3 Bucket Keys](bucket-key.md)\.  

**Required: **No

**Type:** [ServerSideEncryptionByDefault](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-s3-bucket-serversideencryptionbydefault.html) 

**Update requires:** [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

**Example \- Create a bucket that uses an S3 Bucket Key with SSE\-KMS default encryption**  

```
{
  "AWSTemplateFormatVersion": "2010-09-09",
  "Description": "S3 bucket with default encryption",
  "Resources": {
    "EncryptedS3Bucket": {
      "Type": "AWS::S3::Bucket",
      "Properties": {
        "BucketName": {
          "Fn::Sub": "encryptedbucket-${AWS::Region}-${AWS::AccountId}"
        },
        "BucketEncryption": {
          "ServerSideEncryptionConfiguration": [
            {
              "ServerSideEncryptionByDefault": {
                "SSEAlgorithm": "aws:kms",
                "KMSMasterKeyID": "KMS-KEY-ARN"
              },
              "BucketKeyEnabled": true
            }
          ]
        }
      },
      "DeletionPolicy": "Delete"
    }
  }
}
```

```
AWSTemplateFormatVersion: 2010-09-09
Description: S3 bucket with default encryption
Resources:
  EncryptedS3Bucket:
    Type: AWS::S3::Bucket
    Properties:
      BucketName: !Sub 'encryptedbucket-${AWS::Region}-${AWS::AccountId}'
      BucketEncryption:
        ServerSideEncryptionConfiguration:
          - ServerSideEncryptionByDefault:
              SSEAlgorithm: aws:kms
              KMSMasterKeyID: KMS-KEY-ARN
            BucketKeyEnabled: true
    DeletionPolicy: Delete
```