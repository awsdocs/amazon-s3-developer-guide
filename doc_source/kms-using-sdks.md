# Specifying the AWS Key Management Service in Amazon S3 Using the AWS SDKs<a name="kms-using-sdks"></a>

**Topics**
+ [AWS SDK for Java](#kms-using-sdks-java)
+ [AWS SDK for \.NET](#kms-using-sdks-dotnet)

When using AWS SDKs, you can request Amazon S3 to use AWS Key Management Service \(AWS KMS\)–managed encryption keys\. This section provides examples of using the AWS SDKs for Java and \.NET\. For information about other SDKs, go to [Sample Code and Libraries](https://aws.amazon.com/code)\.

## AWS SDK for Java<a name="kms-using-sdks-java"></a>

This section explains various Amazon S3 operations using the AWS SDK for Java and how you use the AWS KMS–managed encryption keys\.

### Put Operation<a name="kms-using-sdks-java-put"></a>

When uploading an object using the AWS SDK for Java, you can request Amazon S3 to use an AWS KMS–managed encryption key by adding the `SSEAwsKeyManagementParams` property as shown in the following request:

```
PutObjectRequest putRequest = new PutObjectRequest(bucketName,
   keyName, file).withSSEAwsKeyManagementParams(new SSEAwsKeyManagementParams());
```

In this case, Amazon S3 uses the default master key \(see [Protecting Data Using Server\-Side Encryption with AWS KMS–Managed Keys \(SSE\-KMS\)](UsingKMSEncryption.md)\)\. You can optionally create your own key and specify that in the request\.

```
PutObjectRequest putRequest = new PutObjectRequest(bucketName,
   keyName, file).withSSEAwsKeyManagementParams(new SSEAwsKeyManagementParams(keyID));
```

For more information about creating keys, go to [Programming the AWS KMS API](http://docs.aws.amazon.com/kms/latest/developerguide/programming-top.html) in the *AWS Key Management Service Developer Guide*\.

For working code examples of uploading an object, see the following topics\. You will need to update those code examples and provide encryption information as shown in the preceding code fragment\.
+ For uploading an object in a single operation, see [Upload an Object Using the AWS SDK for Java](UploadObjSingleOpJava.md)
+ For a multipart upload, see the following topics:
  + Using high\-level multipart upload API, see [Upload a File](HLuploadFileJava.md) 
  + If you are using the low\-level multipart upload API, see [Upload a File](llJavaUploadFile.md)

### Copy Operation<a name="kms-using-sdks-java-copy"></a>

When copying objects, you add the same request properties \(`ServerSideEncryptionMethod` and `ServerSideEncryptionKeyManagementServiceKeyId`\) to request Amazon S3 to use an AWS KMS–managed encryption key\. For more information about copying objects, see [Copying Objects](CopyingObjectsExamples.md)\.

### Pre\-signed URLs<a name="kms-using-sdks-java-presigned-url"></a>

When creating a pre\-signed URL for an object encrypted using an AWS KMS–managed encryption key, you must explicitly specify Signature Version 4:

```
ClientConfiguration clientConfiguration = new ClientConfiguration();
clientConfiguration.setSignerOverride("AWSS3V4SignerType");
AmazonS3Client s3client = new AmazonS3Client(
        new ProfileCredentialsProvider(), clientConfiguration);
...
```

For a code example, see [Generate a Pre\-signed Object URL Using the AWS SDK for Java](ShareObjectPreSignedURLJavaSDK.md)\. 

## AWS SDK for \.NET<a name="kms-using-sdks-dotnet"></a>

This section explains various Amazon S3 operations using the AWS SDK for \.NET and how you use the AWS KMS–managed encryption keys\.

### Put Operation<a name="kms-using-sdks-dotnet-put"></a>

When uploading an object using the AWS SDK for \.NET, you can request Amazon S3 to use an AWS KMS–managed encryption key by adding the `ServerSideEncryptionMethod` property as shown in the following request:

```
PutObjectRequest putRequest = new PutObjectRequest
   {
       BucketName = bucketName,
       Key = keyName,
       // other properties.
       ServerSideEncryptionMethod = ServerSideEncryptionMethod.AWSKMS
   };
```

In this case, Amazon S3 uses the default master key \(see [Protecting Data Using Server\-Side Encryption with AWS KMS–Managed Keys \(SSE\-KMS\)](UsingKMSEncryption.md)\)\. You can optionally create your own key and specify that in the request\. 

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

For more information about creating keys, see [Programming the AWS KMS API](http://docs.aws.amazon.com/kms/latest/developerguide/programming-top.html) in the *AWS Key Management Service Developer Guide*\. 

For working code examples of uploading an object, see the following topics\. You will need to update these code examples and provide encryption information as shown in the preceding code fragment\.
+ For uploading an object in a single operation, see [Upload an Object Using the AWS SDK for \.NET](UploadObjSingleOpNET.md)
+ For multipart upload see the following topics:
  + Using high\-level multipart upload API, see [Upload a File to an S3 Bucket Using the AWS SDK for \.NET \(High\-Level API\)](HLuploadFileDotNet.md) 
  + Using low\-level multipart upload API, see [Upload a File to an S3 Bucket Using the AWS SDK for \.NET \(Low\-Level API\)](LLuploadFileDotNet.md)

### Copy Operation<a name="kms-using-sdks-dotnet-copy"></a>

When copying objects, you add the same request properties \(`ServerSideEncryptionMethod` and `ServerSideEncryptionKeyManagementServiceKeyId`\) to request Amazon S3 to use an AWS KMS–managed encryption key\. For more information about copying objects, see [Copying Objects](CopyingObjectsExamples.md)\.

### Pre\-signed URLs<a name="kms-using-sdks-dotnet-presigned-url"></a>

When creating a pre\-signed URL for an object encrypted using an AWS KMS–managed encryption key, you must explicitly specify Signature Version 4:

```
AWSConfigs.S3Config.UseSignatureVersion4 = true;
```

For a code example, see [Generate a Pre\-signed Object URL Using AWS SDK for \.NET](ShareObjectPreSignedURLDotNetSDK.md)\.