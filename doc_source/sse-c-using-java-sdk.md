# Specifying Server\-Side Encryption with Customer\-Provided Encryption Keys Using the AWS SDK for Java<a name="sse-c-using-java-sdk"></a>

The following example shows how to request server\-side encryption with customer\-provided keys \(SSE\-C\) for objects\. The example performs the following operations\. Each operation shows how to specify SSE\-C\-related headers in the request:
+ **Put object**—Uploads an object and requests server\-side encryption using a customer\-provided encryption key\.
+ **Get object**—Downloads the object uploaded in the previous step\. In the request, you provide the same encryption information that you provided when you uploaded the object\. Amazon S3 needs this information to decrypt the object so that it can return it to you\.
+ **Get object metadata**—Retrieves the object's metadata\. You provide the same encryption information used when the object was created\.
+ **Copy object**—Makes a copy of the previously uploaded object\. Because the source object is stored using SSE\-C, you must provide its encryption information in your copy request\. By default, Amazon S3 encrypts the copy of the object only if you explicitly request it\. This example directs Amazon S3 to store an encrypted copy of the object using a new SSE\-C key\.

**Note**  
This example shows how to upload an object in a single operation\. When using the Multipart Upload API to upload large objects, you provide encryption information in the same way shown in this example\. For examples of multipart uploads that use the AWS SDK for Java, see [Using the AWS Java SDK for Multipart Upload \(High\-Level API\)](usingHLmpuJava.md) and [Using the AWS Java SDK for a Multipart Upload \(Low\-Level API\)](mpListPartsJavaAPI.md)\.

To add the required encryption information, you include an `SSECustomerKey` in your request\. For more information about the `SSECustomerKey` class, see [Using SSE\-C](ServerSideEncryptionCustomerKeys.md#sse-c-how-to-programmatically-intro)\.

For information about SSE\-C, see [Protecting Data Using Server\-Side Encryption with Customer\-Provided Encryption Keys \(SSE\-C\)](ServerSideEncryptionCustomerKeys.md)\. For instructions on creating and testing a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\. 

**Example**  

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.SSECustomerKey;

public class ServerSideEncryptionUsingClientSideEncryptionKey {
    private static SSECustomerKey SSE_KEY;
    private static AmazonS3 S3_CLIENT;
    private static KeyGenerator KEY_GENERATOR;

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        String clientRegion = "*** Client region ***";
        String bucketName = "*** Bucket name ***";
        String keyName = "*** Key name ***";
        String uploadFileName = "*** File path ***";
        String targetKeyName = "*** Target key name ***";
        
        // Create an encryption key.
        KEY_GENERATOR = KeyGenerator.getInstance("AES");
        KEY_GENERATOR.init(256, new SecureRandom());
        SSE_KEY = new SSECustomerKey(KEY_GENERATOR.generateKey());

        try {
            S3_CLIENT = AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(clientRegion)
                    .build();

            // Upload an object.
            uploadObject(bucketName, keyName, new File(uploadFileName));
    
            // Download the object.
            downloadObject(bucketName, keyName);
    
            // Verify that the object is properly encrypted by attempting to retrieve it
            // using the encryption key.
            retrieveObjectMetadata(bucketName, keyName);
    
            // Copy the object into a new object that also uses SSE-C.
            copyObject(bucketName, keyName, targetKeyName);
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

    private static void uploadObject(String bucketName, String keyName, File file) {
        PutObjectRequest putRequest = new PutObjectRequest(bucketName, keyName, file).withSSECustomerKey(SSE_KEY);
        S3_CLIENT.putObject(putRequest);
        System.out.println("Object uploaded");
    }

    private static void downloadObject(String bucketName, String keyName) throws IOException {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, keyName).withSSECustomerKey(SSE_KEY);
        S3Object object = S3_CLIENT.getObject(getObjectRequest);

        System.out.println("Object content: ");
        displayTextInputStream(object.getObjectContent());
    }

    private static void retrieveObjectMetadata(String bucketName, String keyName) {
        GetObjectMetadataRequest getMetadataRequest = new GetObjectMetadataRequest(bucketName, keyName)
                                                                .withSSECustomerKey(SSE_KEY);
        ObjectMetadata objectMetadata = S3_CLIENT.getObjectMetadata(getMetadataRequest);
        System.out.println("Metadata retrieved. Object size: " + objectMetadata.getContentLength());
    }
    
    private static void copyObject(String bucketName, String keyName, String targetKeyName)
            throws NoSuchAlgorithmException {
        // Create a new encryption key for target so that the target is saved using SSE-C.
        SSECustomerKey newSSEKey = new SSECustomerKey(KEY_GENERATOR.generateKey());

        CopyObjectRequest copyRequest = new CopyObjectRequest(bucketName, keyName, bucketName, targetKeyName)
                .withSourceSSECustomerKey(SSE_KEY)
				.withDestinationSSECustomerKey(newSSEKey);

        S3_CLIENT.copyObject(copyRequest);
        System.out.println("Object copied");
    }

    private static void displayTextInputStream(S3ObjectInputStream input) throws IOException {
        // Read one line at a time from the input stream and display each line.
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        System.out.println();
    }
}
```

## Other Amazon S3 Operations with SSE\-C using the AWS SDK for Java<a name="sse-c-java-other-api"></a>

The example in the preceding section shows how to request server\-side encryption with customer\-provided keys \(SSE\-C\) in the PUT, GET, Head, and Copy operations\. This section describes other APIs that support SSE\-C\.

To upload large objects, you can use multipart upload API \(see [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)\)\. You can use either high\-level or low\-level APIs to upload large objects\. These APIs support encryption\-related headers in the request\.
+ When using the high\-level `TransferManager` API, you provide the encryption\-specific headers in the `PutObjectRequest` \(see [Using the AWS Java SDK for Multipart Upload \(High\-Level API\)](usingHLmpuJava.md)\)\. 
+ When using the low\-level API, you provide encryption\-related information in the `InitiateMultipartUploadRequest`, followed by identical encryption information in each `UploadPartRequest`\. You do not need to provide any encryption\-specific headers in your `CompleteMultipartUploadRequest`\. For examples, see [Using the AWS Java SDK for a Multipart Upload \(Low\-Level API\)](mpListPartsJavaAPI.md)\. 

The following example uses `TransferManager` to create objects and shows how to provide SSE\-C related information\. The example does the following:
+ Creates an object using the `TransferManager.upload()` method\. In the `PutObjectRequest` instance, you provide encryption key information to request\. Amazon S3 encrypts the object using the customer\-provided encryption key\.
+ Makes a copy of the object by calling the `TransferManager.copy()` method\. The example directs Amazon S3 to encrypt the object copy using a new `SSECustomerKey`\. Because the source object is encrypted using SSE\-C, the `CopyObjectRequest` also provides the encryption key of the source object so that Amazon S3 can decrypt the object before copying it\. 

**Example**  

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

import java.io.File;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.SSECustomerKey;
import com.amazonaws.services.s3.transfer.Copy;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

public class ServerSideEncryptionCopyObjectUsingHLwithSSEC {

    public static void main(String[] args) throws Exception {
        String clientRegion = "*** Client region ***";
        String bucketName = "*** Bucket name ***";
        String fileToUpload = "*** File path ***";
        String keyName = "*** New object key name ***";
        String targetKeyName = "*** Key name for object copy ***";

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                                    .withRegion(clientRegion)
                                    .withCredentials(new ProfileCredentialsProvider())
                                    .build();
            TransferManager tm = TransferManagerBuilder.standard()
                                    .withS3Client(s3Client)
                                    .build();

            // Create an object from a file.
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, keyName, new File(fileToUpload));
    
            // Create an encryption key.
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256, new SecureRandom());
            SSECustomerKey sseCustomerEncryptionKey = new SSECustomerKey(keyGenerator.generateKey());
    
            // Upload the object. TransferManager uploads asynchronously, so this call returns immediately.
            putObjectRequest.setSSECustomerKey(sseCustomerEncryptionKey);
            Upload upload = tm.upload(putObjectRequest);
            
            // Optionally, wait for the upload to finish before continuing.
            upload.waitForCompletion();
            System.out.println("Object created.");
    
            // Copy the object and store the copy using SSE-C with a new key.
            CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucketName, keyName, bucketName, targetKeyName);
            SSECustomerKey sseTargetObjectEncryptionKey = new SSECustomerKey(keyGenerator.generateKey());
            copyObjectRequest.setSourceSSECustomerKey(sseCustomerEncryptionKey);
            copyObjectRequest.setDestinationSSECustomerKey(sseTargetObjectEncryptionKey);
    
            // Copy the object. TransferManager copies asynchronously, so this call returns immediately.
            Copy copy = tm.copy(copyObjectRequest);
            
            // Optionally, wait for the upload to finish before continuing.
            copy.waitForCompletion();
            System.out.println("Copy complete.");
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
}
```