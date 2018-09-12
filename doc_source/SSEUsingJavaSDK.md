# Specifying Server\-Side Encryption Using the AWS SDK for Java<a name="SSEUsingJavaSDK"></a>

When you use the AWS SDK for Java to upload an object, you can use server\-side encryption to encrypt it\. To request server\-side encryption, use the `ObjectMetadata` property of the `PutObjectRequest` to set the `x-amz-server-side-encryption` request header\. When you call the `putObject()` method of the `AmazonS3Client`, Amazon S3 encrypts and saves the data\.

You can also request server\-side encryption when uploading objects with the multipart upload API: 
+ When using the high\-level multipart upload API, you use the `TransferManager` methods to apply server\-side encryption to objects as you upload them\. You can use any of the upload methods that take `ObjectMetadata` as a parameter\. For more information, see [Using the AWS Java SDK for Multipart Upload \(High\-Level API\)](usingHLmpuJava.md)\.
+ When using the low\-level multipart upload API, you specify server\-side encryption when you initiate the multipart upload\. You add the `ObjectMetadata` property by calling the `InitiateMultipartUploadRequest.setObjectMetadata()` method\. For more information, see [Upload a File](llJavaUploadFile.md)\.

You can't directly change the encryption state of an object \(encrypting an unencrypted object or decrypting an encrypted object\)\. To change an object's encryption state, you make a copy of the object, specifying the desired encryption state for the copy, and then delete the original object\. Amazon S3 encrypts the copied object only if you explicitly request server\-side encryption\. To request encryption of the copied object through the Java API, use the `ObjectMetadata` property to specify server\-side encryption in the `CopyObjectRequest`\.

**Example Example**  
The following example shows how to set server\-side encryption using the AWS SDK for Java\. It shows how to perform the following tasks:  
+ Upload a new object using server\-side encryption
+ Change an object's encryption state \(in this example, encrypting a previously unencrypted object\) by making a copy of the object
+ Check the encryption state of the object
For more information about server\-side encryption, see [Specifying Server\-Side Encryption Using the REST API](SSEUsingRESTAPI.md)\. For instructions on creating and testing a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\.   

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

import java.io.ByteArrayInputStream;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.internal.SSEResultBase;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

public class SpecifyServerSideEncryption {

    public static void main(String[] args) {
           String clientRegion = "*** Client region ***";
           String bucketName = "*** Bucket name ***";
           String keyNameToEncrypt = "*** Key name for an object to upload and encrypt ***";
           String keyNameToCopyAndEncrypt = "*** Key name for an unencrypted object to be encrypted by copying ***";
           String copiedObjectKeyName = "*** Key name for the encrypted copy of the unencrypted object ***";
           
           try {
               AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                                       .withRegion(clientRegion)
                                       .withCredentials(new ProfileCredentialsProvider())
                                       .build();
           
               // Upload an object and encrypt it with SSE.
               uploadObjectWithSSEEncryption(s3Client, bucketName, keyNameToEncrypt);
               
               // Upload a new unencrypted object, then change its encryption state
               // to encrypted by making a copy.
               changeSSEEncryptionStatusByCopying(s3Client, 
                                                  bucketName, 
                                                  keyNameToCopyAndEncrypt, 
                                                  copiedObjectKeyName);
           }
           catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }

    private static void uploadObjectWithSSEEncryption(AmazonS3 s3Client, String bucketName, String keyName) {
        String objectContent = "Test object encrypted with SSE";
                
        // Specify server-side encryption.
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(objectContent.length());
        objectMetadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
        PutObjectRequest putRequest = new PutObjectRequest(bucketName, 
                                                           keyName, 
                                                           new ByteArrayInputStream(objectContent.getBytes()), 
                                                           objectMetadata);

        // Upload the object and check its encryption status.
        PutObjectResult putResult = s3Client.putObject(putRequest);
        System.out.println("Object \"" + keyName + "\" uploaded with SSE.");
        printEncryptionStatus(putResult);
    }
    
    private static void changeSSEEncryptionStatusByCopying(AmazonS3 s3Client, 
                                                           String bucketName, 
                                                           String sourceKey,
                                                           String destKey) {
        // Upload a new, unencrypted object.
        PutObjectResult putResult = s3Client.putObject(bucketName, sourceKey, "Object example to encrypt by copying");
        System.out.println("Unencrypted object \"" + sourceKey + "\" uploaded.");
        printEncryptionStatus(putResult);
        
        // Make a copy of the object and use server-side encryption when storing the copy.
        CopyObjectRequest request = new CopyObjectRequest(bucketName,
                                                          sourceKey,
                                                          bucketName,
                                                          destKey);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
        request.setNewObjectMetadata(objectMetadata);
        
        // Perform the copy operation and display the copy's encryption status.
        CopyObjectResult response = s3Client.copyObject(request);
        System.out.println("Object \"" + destKey + "\" uploaded with SSE.");
        printEncryptionStatus(response);
        
        // Delete the original, unencrypted object, leaving only the encrypted copy in Amazon S3.
        s3Client.deleteObject(bucketName, sourceKey);
        System.out.println("Unencrypted object \"" + sourceKey + "\" deleted.");
    }
    
    private static void printEncryptionStatus(SSEResultBase response) {
        String encryptionStatus = response.getSSEAlgorithm();
        if(encryptionStatus == null) {
            encryptionStatus = "Not encrypted with SSE"; 
        }
        System.out.println("Object encryption status is: " + encryptionStatus);
    }
}
```