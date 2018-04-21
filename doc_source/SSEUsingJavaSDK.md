# Specifying Server\-Side Encryption Using the AWS SDK for Java<a name="SSEUsingJavaSDK"></a>

When using the AWS SDK for Java to upload an object, you can use the `ObjectMetadata` property of the `PutObjectRequest` to set the `x-amz-server-side-encryption` request header \(see [Specifying Server\-Side Encryption Using the REST API](SSEUsingRESTAPI.md)\)\. When you call the `PutObject` method of the `AmazonS3` client as shown in the following Java code example, Amazon S3 encrypts and saves the data\.

```
 1. File file = new File(uploadFileName);
 2. PutObjectRequest putRequest = new PutObjectRequest(
 3.                                       bucketName, keyName, file);
 4.             
 5. // Request server-side encryption.
 6. ObjectMetadata objectMetadata = new ObjectMetadata();
 7. objectMetadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);     
 8. putRequest.setMetadata(objectMetadata);
 9. 
10. PutObjectResult response = s3client.putObject(putRequest);
11. System.out.println("Uploaded object encryption status is " + 
12.                   response.getSSEAlgorithm());
```

In response, Amazon S3 returns the encryption algorithm used for encrypting your object data, which you can check using the `getSSEAlgorithm` method\. 

For a working sample that shows how to upload an object, see [Upload an Object Using the AWS SDK for Java](UploadObjSingleOpJava.md)\. For server\-side encryption, add the `ObjectMetadata` property to your request\. 

When uploading large objects using multipart upload API, you can request server\-side encryption for the object that you are uploading\. 
+ When using the low\-level multipart upload API \(see [Upload a File](llJavaUploadFile.md)\) to upload a large object, you can specify server\-side encryption when you initiate the multipart upload\. That is, you add the `ObjectMetadata` property by calling the `InitiateMultipartUploadRequest.setObjectMetadata` method\. 
+ When using the high\-level multipart upload API \(see [Using the AWS Java SDK for Multipart Upload \(High\-Level API\)](usingHLmpuJava.md)\), the `TransferManager` class provides methods to upload objects\. You can call any of the upload methods that take `ObjectMetadata` as a parameter\.

## Determining the Encryption Algorithm Used<a name="DeterminingEncryptionAlgorithmUsed01"></a>

To determine the encryption state of an existing object, you can retrieve the object metadata as shown in the following Java code example\.

```
1. GetObjectMetadataRequest request2 = 
2.                 new GetObjectMetadataRequest(bucketName, keyName);
3.          
4. ObjectMetadata metadata = s3client.getObjectMetadata(request2);
5. 
6. System.out.println("Encryption algorithm used: " + 
7.             metadata.getSSEAlgorithm());
```

If server\-side encryption is not used for the object that is stored in Amazon S3, the method returns null\.

## Changing Server\-Side Encryption of an Existing Object \(Copy Operation\)<a name="ChangingServer-SideEncryptionofanExistingObjectCopyOperation01"></a>

To change the encryption state of an existing object, you make a copy of the object and delete the source object\. By default, the copy API does not encrypt the target unless you explicitly request server\-side encryption\. You can request the encryption of the target object by using the `ObjectMetadata` property to specify server\-side encryption in the `CopyObjectRequest` as shown in the following Java code example\. 

```
 1. CopyObjectRequest copyObjRequest = new CopyObjectRequest(
 2. sourceBucket, sourceKey, targetBucket, targetKey);
 3.             
 4. // Request server-side encryption.
 5. ObjectMetadata objectMetadata = new ObjectMetadata();
 6. objectMetadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION); 
 7.             
 8. copyObjRequest.setNewObjectMetadata(objectMetadata);
 9.          
10. CopyObjectResult response =  s3client.copyObject(copyObjRequest);
11. System.out.println("Copied object encryption status is " + 
12.                   response.getSSEAlgorithm());
```

For a working sample of how to copy an object, see [Copy an Object Using the AWS SDK for Java](CopyingObjectUsingJava.md)\. You can specify server\-side encryption in the `CopyObjectRequest` object as shown in the preceding code example\.