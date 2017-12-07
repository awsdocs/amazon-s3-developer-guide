# Specifying Server\-Side Encryption Using the AWS SDK for \.NET<a name="SSEUsingDotNetSDK"></a>

When using the AWS SDK for \.NET to upload an object, you can use the `WithServerSideEncryptionMethod` property of `PutObjectRequest` to set the `x-amz-server-side-encryption` request header \(see [Specifying Server\-Side Encryption Using the REST API](SSEUsingRESTAPI.md)\)\. When you call the `PutObject` method of the `AmazonS3` client as shown in the following C\# code example, Amazon S3 encrypts and saves the data\.

```
 1. static AmazonS3 client;
 2. client = new AmazonS3Client(accessKeyID, secretAccessKeyID);
 3. 
 4. PutObjectRequest request = new PutObjectRequest();
 5. request.WithContentBody("Object data for simple put.")
 6.     .WithBucketName(bucketName)
 7.     .WithKey(keyName)
 8.     .WithServerSideEncryptionMethod(ServerSideEncryptionMethod.AES256);
 9. 
10. S3Response response = client.PutObject(request);
11. 
12. // Check the response header to determine if the object is encrypted.
13. ServerSideEncryptionMethod destinationObjectEncryptionStatus = response.ServerSideEncryptionMethod;
```

In response, Amazon S3 returns the encryption algorithm that is used to encrypt your object data, which you can check using the `ServerSideEncryptionMethod` property\. 

For a working sample of how to upload an object, see [Upload an Object Using the AWS SDK for \.NET](UploadObjSingleOpNET.md)\. For server\-side encryption, set the `ServerSideEncryptionMethod` property by calling the `WithServerSideEncryptionMethod` method\. 

To upload large objects using the multipart upload API, you can specify server\-side encryption for the objects that you are uploading\. 

+ When using the low\-level multipart upload API \(see [Using the AWS \.NET SDK for Multipart Upload \(Low\-Level API\)](usingLLmpuDotNet.md)\) to upload a large object, you can specify server\-side encryption in your `InitiateMultipartUpload` request\. That is, you set the `ServerSideEncryptionMethod` property to your `InitiateMultipartUploadRequest` by calling the `WithServerSideEncryptionMethod` method\. 

+ When using the high\-level multipart upload API \(see [Using the AWS \.NET SDK for Multipart Upload \(High\-Level API\)](usingHLmpuDotNet.md)\), the `TransferUtility` class provides methods \(`Upload` and `UploadDirectory`\) to upload objects\. In this case, you can request server\-side encryption using the `TransferUtilityUploadRequest` and `TransferUtilityUploadDirectoryRequest` objects\. 

## Determining the Encryption Algorithm Used<a name="DeterminingEncryptionAlgorithmUsed02"></a>

To determine the encryption state of an existing object, you can retrieve the object metadata as shown in the following C\# code example\.

```
 1. AmazonS3 client;
 2. client = new AmazonS3Client(accessKeyID, secretAccessKeyID);
 3. 
 4. ServerSideEncryptionMethod objectEncryption;
 5. 
 6. GetObjectMetadataRequest metadataRequest = new GetObjectMetadataRequest()
 7.                                                .WithBucketName(bucketName)
 8.                                                .WithKey(keyName);
 9. 
10. objectEncryption = client.GetObjectMetadata(metadataRequest)
11.                                    .ServerSideEncryptionMethod;
```

The encryption algorithm is specified with an enum\. If the stored object is not encrypted \(default behavior\), then the `ServerSideEncryptionMethod` property of the object defaults to `None`\.

## Changing Server\-Side Encryption of an Existing Object \(Copy Operation\)<a name="ChangingServer-SideEncryptionofanExistingObjectCopyOperation02"></a>

To change the encryption state of an existing object, you can make a copy of the object and delete the source object\. By default, the copy API does not encrypt the target unless you explicitly request server\-side encryption of the destination object\. The following C\# code example makes a copy of an object\. The request explicitly specifies server\-side encryption for the destination object\.

```
 1. AmazonS3 client;
 2. client = new AmazonS3Client(accessKeyID, secretAccessKeyID);
 3. 
 4. CopyObjectResponse response = client.CopyObject(new CopyObjectRequest()
 5.             .WithSourceBucket(sourceBucketName)
 6.             .WithSourceKey(sourceObjetKey)
 7.             .WithDestinationBucket(targetBucketName)
 8.             .WithDestinationKey(targetObjectKey)
 9.             .WithServerSideEncryptionMethod(ServerSideEncryptionMethod.AES256)
10. );
11. // Check the response header to determine if the object is encrypted.
12. ServerSideEncryptionMethod destinationObjectEncryptionStatus = response.ServerSideEncryptionMethod;
```

For a working sample of how to copy an object, see [Copy an Object Using the AWS SDK for \.NET](CopyingObjectUsingNetSDK.md)\. You can specify server\-side encryption in the `CopyObjectRequest` object as shown in the preceding code example\.