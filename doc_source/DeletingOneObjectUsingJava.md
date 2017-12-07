# Deleting an Object Using the AWS SDK for Java<a name="DeletingOneObjectUsingJava"></a>

The following tasks guide you through using the AWS SDK for Java classes to delete an object\. 


**Deleting an Object \(Non\-Versioned Bucket\)**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `AmazonS3Client` class\.   | 
|  2  |  Execute one of the `AmazonS3Client.deleteObject` methods\. You can provide a bucket name and an object name as parameters or provide the same information in a `DeleteObjectRequest` object and pass the object as a parameter\. If you have not enabled versioning on the bucket, the operation deletes the object\. If you have enabled versioning, the operation adds a delete marker\. For more information, see [Deleting One Object Per Request](DeletingOneObject.md)\.  | 

The following Java sample demonstrates the preceding steps\. The sample uses the `DeleteObjectRequest` class to provide a bucket name and an object key\.

**Example**  

```
1. AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());        
2. s3client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
```


**Deleting a Specific Version of an Object \(Version\-Enabled Bucket\)**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `AmazonS3Client` class\.   | 
|  2  |  Execute one of the `AmazonS3Client.deleteVersion` methods\. You can provide a bucket name and an object key directly as parameters or use the `DeleteVersionRequest` to provide the same information\.  | 

The following Java sample demonstrates the preceding steps\. The sample uses the `DeleteVersionRequest` class to provide a bucket name, an object key, and a version Id\.

**Example**  

```
1. AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());        
2. s3client.deleteObject(new DeleteVersionRequest(bucketName, keyName, versionId));
```

**Example 1: Deleting an Object \(Non\-Versioned Bucket\)**  
The following Java example deletes an object from a bucket\. If you have not enabled versioning on the bucket, Amazon S3 deletes the object\. If you have enabled versioning, Amazon S3 adds a delete marker and the object is not deleted\. For information about how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\.   

```
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;

public class DeleteAnObjectNonVersionedBucket  {

    private static String bucketName = "*** Provide a Bucket Name ***";
    private static String keyName    = "*** Provide a Key Name ****"; 

    public static void main(String[] args) throws IOException {
        AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
}
```

**Example 2: Deleting an Object \(Versioned Bucket\)**  
The following Java example deletes a specific version of an object from a versioned bucket\. The `deleteObject` request removes the specific object version from the bucket\.   
To test the sample, you must provide a bucket name\. The code sample performs the following tasks:  

1. Enable versioning on the bucket\.

1. Add a sample object to the bucket\. In response, Amazon S3 returns the version ID of the newly added object\.

1. Delete the sample object using the `deleteVersion` method\. The `DeleteVersionRequest` class specifies both an object key name and a version ID\.
For information about how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\.   

```
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Random;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.BucketVersioningConfiguration;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteVersionRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.SetBucketVersioningConfigurationRequest;

public class DeleteAnObjectVersionEnabledBucket  {

    static String bucketName = "*** Provide a Bucket Name ***";
    static String keyName    = "*** Provide a Key Name ****"; 
    static AmazonS3Client s3Client;
    
    public static void main(String[] args) throws IOException {
        s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
        try {
            // Make the bucket version-enabled.
            enableVersioningOnBucket(s3Client, bucketName);
            
            // Add a sample object.
            String versionId = putAnObject(keyName);

            s3Client.deleteVersion(
                    new DeleteVersionRequest(
                            bucketName, 
                            keyName,
                            versionId));
            
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException.");
            System.out.println("Error Message: " + ace.getMessage());
        }

    }
    
    static void enableVersioningOnBucket(AmazonS3Client s3Client,
            String bucketName) {
        BucketVersioningConfiguration config = new BucketVersioningConfiguration()
                .withStatus(BucketVersioningConfiguration.ENABLED);
        SetBucketVersioningConfigurationRequest setBucketVersioningConfigurationRequest = new SetBucketVersioningConfigurationRequest(
                bucketName, config);
        s3Client.setBucketVersioningConfiguration(setBucketVersioningConfigurationRequest);
    }
    
    static String putAnObject(String keyName) {
        String content = "This is the content body!";
        String key = "ObjectToDelete-" + new Random().nextInt();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setHeader("Subject", "Content-As-Object");
        metadata.setHeader("Content-Length", content.length());
        PutObjectRequest request = new PutObjectRequest(bucketName, key,
                new ByteArrayInputStream(content.getBytes()), metadata)
                .withCannedAcl(CannedAccessControlList.AuthenticatedRead);
        PutObjectResult response = s3Client.putObject(request);
        return response.getVersionId();
    }
}
```