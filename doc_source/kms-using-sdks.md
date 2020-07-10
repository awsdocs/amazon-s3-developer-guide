# Specifying the AWS Key Management Service in Amazon S3 Using the AWS SDKs<a name="kms-using-sdks"></a>

When using AWS SDKs, you can request Amazon S3 to use AWS Key Management Service \(AWS KMS\) customer master keys \(CMKs\)\. This section provides examples of using the AWS SDKs for Java and \.NET\. For information about other SDKs, go to [Sample Code and Libraries](https://aws.amazon.com/code)\.

**Important**  
When you use an AWS KMS CMK for server\-side encryption in Amazon S3, you must choose a symmetric CMK\. Amazon S3 only supports symmetric CMKs and not asymmetric CMKs\. For more information, see [Using Symmetric and Asymmetric Keys](https://docs.aws.amazon.com/kms/latest/developerguide/symmetric-asymmetric.html) in the *AWS Key Management Service Developer Guide*\.

## AWS SDK for Java<a name="kms-using-sdks-java"></a>

This section explains various Amazon S3 operations using the AWS SDK for Java and how you use the AWS KMS CMKs\.

### Put Operation<a name="kms-using-sdks-java-put"></a>

When uploading an object using the AWS SDK for Java, you can request Amazon S3 to use an AWS KMS CMK by adding the `SSEAwsKeyManagementParams` property as shown in the following request\.

```
PutObjectRequest putRequest = new PutObjectRequest(bucketName,
   keyName, file).withSSEAwsKeyManagementParams(new SSEAwsKeyManagementParams());
```

In this case, Amazon S3 uses the AWS managed CMK \(see [Using Server\-Side Encryption with CMKs Stored in AWS KMS ](UsingKMSEncryption.md)\)\. You can optionally create a symmetric customer managed CMK and specify that in the request\.

```
PutObjectRequest putRequest = new PutObjectRequest(bucketName,
   keyName, file).withSSEAwsKeyManagementParams(new SSEAwsKeyManagementParams(keyID));
```

For more information about creating customer managed CMKs, see [Programming the AWS KMS API](https://docs.aws.amazon.com/kms/latest/developerguide/programming-top.html) in the *AWS Key Management Service Developer Guide*\.

For working code examples of uploading an object, see the following topics\. You will need to update those code examples and provide encryption information as shown in the preceding code fragment\.
+ For uploading an object in a single operation, see [Upload an object Using the AWS SDK for Java](UploadObjSingleOpJava.md)\.
+ For a multipart upload, see the following topics:
  + Using high\-level multipart upload API, see [Upload a file](HLuploadFileJava.md)\. 
  + If you are using the low\-level multipart upload API, see [Upload a file](llJavaUploadFile.md)\.

### Copy Operation<a name="kms-using-sdks-java-copy"></a>

When copying objects, you add the same request properties \(`ServerSideEncryptionMethod` and `ServerSideEncryptionKeyManagementServiceKeyId`\) to request Amazon S3 to use an AWS KMS CMK\. For more information about copying objects, see [Copying objects](CopyingObjectsExamples.md)\.

### Presigned URLs<a name="kms-using-sdks-java-presigned-url"></a>

When creating a presigned URL for an object encrypted using an AWS KMS CMK, you must explicitly specify Signature Version 4\.

```
ClientConfiguration clientConfiguration = new ClientConfiguration();
clientConfiguration.setSignerOverride("AWSS3V4SignerType");
AmazonS3Client s3client = new AmazonS3Client(
        new ProfileCredentialsProvider(), clientConfiguration);
...
```

For a code example, see [Generate a presigned object URL using the AWS SDK for Java](ShareObjectPreSignedURLJavaSDK.md)\. 

## AWS SDK for \.NET<a name="kms-using-sdks-dotnet"></a>

This section explains various Amazon S3 operations using the AWS SDK for \.NET and how you use the AWS KMS CMKs\.

### Put Operation<a name="kms-using-sdks-dotnet-put"></a>

When uploading an object using the AWS SDK for \.NET, you can request Amazon S3 to use an AWS KMS CMK by adding the `ServerSideEncryptionMethod` property as shown in the following request\.

```
PutObjectRequest putRequest = new PutObjectRequest
   {
       BucketName = bucketName,
       Key = keyName,
       // other properties.
       ServerSideEncryptionMethod = ServerSideEncryptionMethod.AWSKMS
   };
```

In this case, Amazon S3 uses the AWS managed CMK\. For more information, see [Protecting Data Using Server\-Side Encryption with CMKs Stored in AWS Key Management Service \(SSE\-KMS\)](UsingKMSEncryption.md)\. You can optionally create your own symmetric customer managed CMK and specify that in the request\. 

```
PutObjectRequest putRequest1 = new PutObjectRequest
{
    BucketName = bucketName,
    Key = keyName,
    // other properties.
    ServerSideEncryptionMethod = ServerSideEncryptionMethod.AWSKMS,
    ServerSideEncryptionKeyManagementServiceKeyId = keyId
};
```

For more information about creating customer managed CMKs, see [Programming the AWS KMS API](https://docs.aws.amazon.com/kms/latest/developerguide/programming-top.html) in the *AWS Key Management Service Developer Guide*\. 

For working code examples of uploading an object, see the following topics\. You will need to update these code examples and provide encryption information as shown in the preceding code fragment\.
+ For uploading an object in a single operation, see [Upload an object using the AWS SDK for \.NET](UploadObjSingleOpNET.md)\.
+ For multipart upload see the following topics:
  + Using high\-level multipart upload API, see [Upload a file to an S3 bucket using the AWS SDK for \.NET \(high\-level API\)](HLuploadFileDotNet.md)\. 
  + Using low\-level multipart upload API, see [Upload a file to an S3 Bucket using the AWS SDK for \.NET \(low\-level API\)](LLuploadFileDotNet.md)\.

### Copy Operation<a name="kms-using-sdks-dotnet-copy"></a>

When copying objects, you add the same request properties \(`ServerSideEncryptionMethod` and `ServerSideEncryptionKeyManagementServiceKeyId`\) to request Amazon S3 to use an AWS KMS CMK\. For more information about copying objects, see [Copying objects](CopyingObjectsExamples.md)\.

### Presigned URLs<a name="kms-using-sdks-dotnet-presigned-url"></a>

When creating a presigned URL for an object encrypted using an AWS KMS CMK, you must explicitly specify Signature Version 4\.

```
AWSConfigs.S3Config.UseSignatureVersion4 = true;
```

For a code example, see [Generate a presigned object URL using AWS SDK for \.NET](ShareObjectPreSignedURLDotNetSDK.md)\.