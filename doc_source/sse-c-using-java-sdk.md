# Specifying Server\-Side Encryption with Customer\-Provided Encryption Keys Using the AWS Java SDK<a name="sse-c-using-java-sdk"></a>

The following Java code example illustrates server\-side encryption with customer\-provided keys \(SSE\-C\) \(see [Protecting Data Using Server\-Side Encryption with Customer\-Provided Encryption Keys \(SSE\-C\)](ServerSideEncryptionCustomerKeys.md)\)\. The example performs the following operations; each operation shows how you specify SSE\-C related headers in the request:
+ **Put object** – upload an object requesting server\-side encryption using a customer\-provided encryption key\.
+ **Get object** – download the object that you uploaded in the previous step\. The example shows that in the Get request you must provide the same encryption information that you provided at the time you uploaded the object, so that Amazon S3 can decrypt the object before returning it\.
+ **Get object metadata** – The request shows the same encryption information that you specified when creating the object is required to retrieve the object's metadata\.
+ **Copy object** – This example makes a copy of the previously uploaded object\. Because the source object is stored using SSE\-C, you must provide the encryption information in your copy request\. By default, the object copy will not be encrypted\. But in this example, you request that Amazon S3 store the object copy encrypted by using SSE\-C, and therefore you must provide SSE\-C encryption information for the target as well\. 

**Note**  
This example shows how to upload an object in a single operation\. When using the multipart upload API to upload large objects, you provide the same encryption information that you provide in your request, as shown in the following example\. For multipart upload AWS SDK for Java examples, see [Using the AWS Java SDK for Multipart Upload \(High\-Level API\)](usingHLmpuJava.md) and [Using the AWS Java SDK for Multipart Upload \(Low\-Level API\)](mpListPartsJavaAPI.md)\.

The AWS SDK for Java provides the `SSECustomerKey` class for you to add the required encryption information \(see [Using SSE\-C](ServerSideEncryptionCustomerKeys.md#sse-c-how-to-programmatically-intro)\) in your request\. You are required to provide only the encryption key\. The Java SDK sets the values for the MD5 digest of the encryption key and the algorithm\.

For information about how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\. 

**Example**  

```
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.SSECustomerKey;

public class ServerSideEncryptionUsingClientSideEncryptionKey {
    private static String bucketName     = "*** Provide bucket name ***";
    private static String keyName        = "*** Provide key ***";
    private static String uploadFileName = "*** Provide file name ***";
    private static String targetKeyName  = "*** provide target key ***";
    private static AmazonS3 s3client;

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        s3client = new AmazonS3Client(new ProfileCredentialsProvider());
        try {
            System.out.println("Uploading a new object to S3 from a file\n");
            File file = new File(uploadFileName);
            // Create encryption key.
            SecretKey secretKey = generateSecretKey();
            SSECustomerKey sseKey = new SSECustomerKey(secretKey);

            // 1. Upload object.
            uploadObject(file, sseKey);

            // 2. Download object.
            downloadObject(sseKey);

            // 3. Get object metadata (and verify AES256 encryption).
            retrieveObjectMetadata(sseKey);

            // 4. Copy object (both source and object use SSE-C).
            copyObject(sseKey);

         } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which " +
                    "means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which " +
                    "means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

    private static void copyObject(SSECustomerKey sseKey) {
        // Create new encryption key for target so it is saved using sse-c
        SecretKey secretKey2 = generateSecretKey();
        SSECustomerKey newSseKey = new SSECustomerKey(secretKey2);

        CopyObjectRequest copyRequest = new CopyObjectRequest(bucketName, keyName, bucketName, targetKeyName)
                .withSourceSSECustomerKey(sseKey)
                .withDestinationSSECustomerKey(newSseKey);

        s3client.copyObject(copyRequest);
        System.out.println("Object copied");
    }

    private static void retrieveObjectMetadata(SSECustomerKey sseKey) {
        GetObjectMetadataRequest getMetadataRequest = new GetObjectMetadataRequest(bucketName, keyName)
                .withSSECustomerKey(sseKey);

        ObjectMetadata objectMetadata =  s3client.getObjectMetadata(getMetadataRequest);
        System.out.println("object size " + objectMetadata.getContentLength());
        System.out.println("Metadata retrieved");
    }

    private static PutObjectRequest uploadObject(File file, SSECustomerKey sseKey) {
        // 1. Upload Object.
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, keyName, file)
                .withSSECustomerKey(sseKey);

        s3client.putObject(putObjectRequest);
        System.out.println("Object uploaded");
        return putObjectRequest;
    }

    private static void downloadObject(SSECustomerKey sseKey) throws IOException {
        // Get a range of bytes from an object.
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, keyName)
                .withSSECustomerKey(sseKey);

        S3Object s3Object = s3client.getObject(getObjectRequest);

        System.out.println("Printing bytes retrieved.");
        displayTextInputStream(s3Object.getObjectContent());
    }

    private static void displayTextInputStream(S3ObjectInputStream input)
    throws IOException {
        // Read one text line at a time and display.
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;

            System.out.println("    " + line);
        }
        System.out.println();
    }

    private static SecretKey generateSecretKey() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(256, new SecureRandom());
            return generator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
            return null;
        }
    }
}
```

## Other Amazon S3 Operations and SSE\-C<a name="sse-c-java-other-api"></a>

The example in the preceding section shows how to request server\-side encryption with customer\-provided keys \(SSE\-C\) in the PUT, GET, Head, and Copy operations\. This section describes other APIs that support SSE\-C\.

To upload large objects, you can use multipart upload API \(see [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)\)\. You can use either high\-level or low\-level APIs to upload large objects\. These APIs support encryption\-related headers in the request\.
+ When using the high\-level Transfer\-Utility API, you provide the encryption\-specific headers in the `TransferManager` \(see [Using the AWS Java SDK for Multipart Upload \(High\-Level API\)](usingHLmpuJava.md)\)\.
+ When using the low\-level API, you provide encryption\-related information in the initiate multipart upload request, followed by identical encryption information in the subsequent upload part requests\. You do not need to provide any encryption\-specific headers in your complete multipart upload request\. For examples, see [Using the AWS Java SDK for Multipart Upload \(Low\-Level API\)](mpListPartsJavaAPI.md)\.

  The following example uses `TransferManager` to create objects and shows how to provide SSE\-C related information\. The example does the following:
  + Create an object using the `TransferManager.upload` method\. In the `PutObjectRequest` instance, you provide encryption key information to request that Amazon S3 store the object encrypted using the customer\-provided encryption key\.
  + Make a copy of the object by calling the `TransferManager.copy` method\. In the `CopyObjectRequest`, this example requests Amazon S3 to store the object copy also encrypted using a customer\-provided encryption key\. Because the source object is encrypted using SSE\-C, the `CopyObjectRequest` also provides the encryption key of the source object so Amazon S3 can decrypt the object before it can copy\.   
**Example**  

  ```
  import java.io.File;
  import java.security.SecureRandom;
  
  import javax.crypto.KeyGenerator;
  import javax.crypto.SecretKey;
  
  import com.amazonaws.AmazonClientException;
  import com.amazonaws.auth.profile.ProfileCredentialsProvider;
  import com.amazonaws.services.s3.model.CopyObjectRequest;
  import com.amazonaws.services.s3.model.PutObjectRequest;
  import com.amazonaws.services.s3.model.SSECustomerKey;
  import com.amazonaws.services.s3.transfer.Copy;
  import com.amazonaws.services.s3.transfer.TransferManager;
  import com.amazonaws.services.s3.transfer.Upload;
  
  public class ServerSideEncryptionCopyObjectUsingHLwithSSEC {
  
      public static void main(String[] args) throws Exception {
          String existingBucketName = "*** Provide existing bucket name ***";
          String fileToUpload       = "*** file path ***";
          String keyName            = "*** New object key ***";
          String targetKeyName      = "*** Key name for object copy ***";
          
          TransferManager tm = new TransferManager(new ProfileCredentialsProvider());  
          
          // 1. first create an object from a file. 
          PutObjectRequest putObjectRequest = new PutObjectRequest(existingBucketName, keyName, new File(fileToUpload));
          
          // we want object stored using SSE-C. So we create encryption key.
          SecretKey secretKey1 = generateSecretKey();
          SSECustomerKey sseCustomerEncryptionKey1 = new SSECustomerKey(secretKey1);
          
          putObjectRequest.setSSECustomerKey(sseCustomerEncryptionKey1);
          // now create object.
          //Upload upload = tm.upload(existingBucketName, keyName, new File(sourceFile));
          Upload upload = tm.upload(putObjectRequest);
          try {
          	// Or you can block and wait for the upload to finish
          	upload.waitForCompletion();
          	//tm.getAmazonS3Client().putObject(putObjectRequest);
          	System.out.println("Object created.");
          } catch (AmazonClientException amazonClientException) {
          	System.out.println("Unable to upload file, upload was aborted.");
          	amazonClientException.printStackTrace();
          }
  
          // 2. Now make object copy (in the same bucket). Store target using sse-c.
          CopyObjectRequest copyObjectRequest = new CopyObjectRequest(existingBucketName, keyName, existingBucketName, targetKeyName);
          
          SecretKey secretKey2 = generateSecretKey();
          SSECustomerKey sseTargetObjectEncryptionKey = new SSECustomerKey(secretKey2);
          
          
          copyObjectRequest.setSourceSSECustomerKey(sseCustomerEncryptionKey1);
          copyObjectRequest.setDestinationSSECustomerKey(sseTargetObjectEncryptionKey);
          
          
          // TransferManager processes all transfers asynchronously, 
          // so this call will return immediately.
          Copy copy = tm.copy(copyObjectRequest);
          try {
          	// Or you can block and wait for the upload to finish
          	copy.waitForCompletion();
          	System.out.println("Copy complete.");
          } catch (AmazonClientException amazonClientException) {
          	System.out.println("Unable to upload file, upload was aborted.");
          	amazonClientException.printStackTrace();
          }
      }
      
      private static SecretKey generateSecretKey() {
          KeyGenerator generator;
          try {
              generator = KeyGenerator.getInstance("AES");
              generator.init(256, new SecureRandom());
              return generator.generateKey();
          } catch (Exception e) {
              e.printStackTrace();
              System.exit(-1);
              return null;
          }
      }
      
  }
  ```