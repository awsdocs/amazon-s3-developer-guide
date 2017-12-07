# Deleting Multiple Objects Using the AWS SDK for Java<a name="DeletingMultipleObjectsUsingJava"></a>

The following tasks guide you through using the AWS SDK for Java classes to delete multiple objects in a single HTTP request\. 


**Deleting Multiple Objects \(Non\-Versioned Bucket\)**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `AmazonS3Client` class\.   | 
|  2  |  Create an instance of the `DeleteObjectsRequest` class and provide a list of objects keys you want to delete\.   | 
|  3  |  Execute the `AmazonS3Client.deleteObjects` method\.  | 

The following Java code sample demonstrates the preceding steps\. 

**Example**  

```
 1. DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(bucketName);
 2. 
 3. List<KeyVersion> keys = new ArrayList<KeyVersion>();
 4. keys.add(new KeyVersion(keyName1));
 5. keys.add(new KeyVersion(keyName2));
 6. keys.add(new KeyVersion(keyName3));
 7.         
 8. multiObjectDeleteRequest.setKeys(keys);
 9. 
10. try {
11.     DeleteObjectsResult delObjRes = s3Client.deleteObjects(multiObjectDeleteRequest);
12.     System.out.format("Successfully deleted all the %s items.\n", delObjRes.getDeletedObjects().size());
13.     			
14. } catch (MultiObjectDeleteException e) {
15.     // Process exception.
16. }
```

In the event of an exception, you can review the `MultiObjectDeleteException` to determine which objects failed to delete and why as shown in the following Java example\. 

```
1. System.out.format("%s \n", e.getMessage());
2. System.out.format("No. of objects successfully deleted = %s\n", e.getDeletedObjects().size());
3. System.out.format("No. of objects failed to delete = %s\n", e.getErrors().size());
4. System.out.format("Printing error data...\n");
5. for (DeleteError deleteError : e.getErrors()){
6.     System.out.format("Object Key: %s\t%s\t%s\n", 
7.             deleteError.getKey(), deleteError.getCode(), deleteError.getMessage());
8. }
```

 The following tasks guide you through deleting objects from a version\-enabled bucket\. 


**Deleting Multiple Objects \(Version\-Enabled Bucket\)**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `AmazonS3Client` class\.   | 
|  2  |  Create an instance of the `DeleteObjectsRequest` class and provide a list of objects keys and optionally the version IDs of the objects that you want to delete\. If you specify the version ID of the object that you want to delete, Amazon S3 deletes the specific object version\. If you don't specify the version ID of the object that you want to delete, Amazon S3 adds a delete marker\. For more information, see [Deleting One Object Per Request](DeletingOneObject.md)\.   | 
|  3  |  Execute the `AmazonS3Client.deleteObjects` method\.  | 

The following Java code sample demonstrates the preceding steps\. 

```
 1. List<KeyVersion> keys = new ArrayList<KeyVersion>();
 2. // Provide a list of object keys and versions.
 3. 
 4. DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(bucketName)
 5. .withKeys(keys);
 6.          
 7. try {
 8.     DeleteObjectsResult delObjRes = s3Client.deleteObjects(multiObjectDeleteRequest);
 9.     System.out.format("Successfully deleted all the %s items.\n", delObjRes.getDeletedObjects().size());
10.     			
11. } catch (MultiObjectDeleteException e) {
12.     // Process exception.
13. }
```

**Example 1: Multi\-Object Delete \(Non\-Versioned Bucket\)**  
The following Java code example uses the Multi\-Object Delete API to delete objects from a non\-versioned bucket\. The example first uploads the sample objects to the bucket and then uses the `deleteObjects` method to delete the objects in a single request\.   
For information about how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\.  

```
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.MultiObjectDeleteException;
import com.amazonaws.services.s3.model.MultiObjectDeleteException.DeleteError;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

public class DeleteMultipleObjectsNonVersionedBucket {

    static String bucketName = "*** Provide a bucket name ***";
    static AmazonS3Client s3Client;

    public static void main(String[] args) throws IOException {

        try {
            s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
            // Upload sample objects.Because the bucket is not version-enabled, 
            // the KeyVersions list returned will have null values for version IDs.
            List<KeyVersion> keysAndVersions1 = putObjects(3);

            // Delete specific object versions.
            multiObjectNonVersionedDelete(keysAndVersions1);

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

    static List<KeyVersion> putObjects(int number) {
        List<KeyVersion> keys = new ArrayList<KeyVersion>();
        String content = "This is the content body!";
        for (int i = 0; i < number; i++) {
            String key = "ObjectToDelete-" + new Random().nextInt();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setHeader("Subject", "Content-As-Object");
            metadata.setHeader("Content-Length", (long)content.length());
            PutObjectRequest request = new PutObjectRequest(bucketName, key,
                    new ByteArrayInputStream(content.getBytes()), metadata)
                    .withCannedAcl(CannedAccessControlList.AuthenticatedRead);
            PutObjectResult response = s3Client.putObject(request);
            KeyVersion keyVersion = new KeyVersion(key, response.getVersionId());
            keys.add(keyVersion);
        }
        return keys;
    }

    static void multiObjectNonVersionedDelete(List<KeyVersion> keys) {

        // Multi-object delete by specifying only keys (no version ID).
        DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(
                bucketName).withQuiet(false);

        // Create request that include only object key names.
        List<KeyVersion> justKeys = new ArrayList<KeyVersion>();
        for (KeyVersion key : keys) {
            justKeys.add(new KeyVersion(key.getKey()));
        }
        multiObjectDeleteRequest.setKeys(justKeys);
        // Execute DeleteObjects - Amazon S3 add delete marker for each object
        // deletion. The objects no disappear from your bucket (verify).
        DeleteObjectsResult delObjRes = null;
        try {
            delObjRes = s3Client.deleteObjects(multiObjectDeleteRequest);
            System.out.format("Successfully deleted all the %s items.\n", delObjRes.getDeletedObjects().size());
        } catch (MultiObjectDeleteException mode) {
            printDeleteResults(mode);
        }
    }
    static void printDeleteResults(MultiObjectDeleteException mode) {
        System.out.format("%s \n", mode.getMessage());
        System.out.format("No. of objects successfully deleted = %s\n", mode.getDeletedObjects().size());
        System.out.format("No. of objects failed to delete = %s\n", mode.getErrors().size());
        System.out.format("Printing error data...\n");
        for (DeleteError deleteError : mode.getErrors()){
            System.out.format("Object Key: %s\t%s\t%s\n", 
                    deleteError.getKey(), deleteError.getCode(), deleteError.getMessage());
        }
    }
}
```

**Example 2: Multi\-Object Delete \(Version\-Enabled Bucket\)**  
The following Java code example uses the Multi\-Object Delete API to delete objects from a version\-enabled bucket\.   
Before you can test the sample, you must create a sample bucket and provide the bucket name in the example\. You can use the AWS Management Console to create a bucket\.   
The example performs the following actions:  

1.  Enable versioning on the bucket\. 

1.  Perform a versioned\-delete\.

   The example first uploads the sample objects\. In response, Amazon S3 returns the version IDs for each sample object that you uploaded\. The example then deletes these objects using the Multi\-Object Delete API\. In the request, it specifies both the object keys and the version IDs \(that is, versioned delete\)\. 

1. Perform a non\-versioned delete\. 

   The example uploads the new sample objects\. Then, it deletes the objects using the Multi\-Object API\. However, in the request, it specifies only the object keys\. In this case, Amazon S3 adds the delete markers and the objects disappear from your bucket\.

1. Delete the delete markers\. 

   To illustrate how the delete markers work, the sample deletes the delete markers\. In the Multi\-Object Delete request, it specifies the object keys and the version IDs of the delete markers it received in the response in the preceding step\. This action makes the objects reappear in your bucket\.
For information about how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\.  

```
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.BucketVersioningConfiguration;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.DeleteObjectsResult.DeletedObject;
import com.amazonaws.services.s3.model.MultiObjectDeleteException;
import com.amazonaws.services.s3.model.MultiObjectDeleteException.DeleteError;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.SetBucketVersioningConfigurationRequest;

public class DeleteMultipleObjectsVersionEnabledBucket {

    static String bucketName = "*** Provide a bucket name ***";
    static AmazonS3Client s3Client;

    public static void main(String[] args) throws IOException {

        try {
            s3Client = new AmazonS3Client(new ProfileCredentialsProvider());

            // 1. Enable versioning on the bucket.
            enableVersioningOnBucket(s3Client, bucketName);

            // 2a. Upload sample objects.
            List<KeyVersion> keysAndVersions1 = putObjects(3);
            // 2b. Delete specific object versions.
            multiObjectVersionedDelete(keysAndVersions1);

            // 3a. Upload samples objects. 
            List<KeyVersion> keysAndVersions2 = putObjects(3);
            // 3b. Delete objects using only keys. Amazon S3 creates a delete marker and 
            // returns its version Id in the response.            
            DeleteObjectsResult response = multiObjectNonVersionedDelete(keysAndVersions2);
            // 3c. Additional exercise - using multi-object versioned delete, remove the 
            // delete markers received in the preceding response. This results in your objects 
            // reappear in your bucket
            multiObjectVersionedDeleteRemoveDeleteMarkers(response);
            
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

    static List<KeyVersion> putObjects(int number) {
        List<KeyVersion> keys = new ArrayList<KeyVersion>();
        String content = "This is the content body!";
        for (int i = 0; i < number; i++) {
            String key = "ObjectToDelete-" + new Random().nextInt();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setHeader("Subject", "Content-As-Object");
            metadata.setHeader("Content-Length", (long)content.length());
            PutObjectRequest request = new PutObjectRequest(bucketName, key,
                    new ByteArrayInputStream(content.getBytes()), metadata)
                    .withCannedAcl(CannedAccessControlList.AuthenticatedRead);
            PutObjectResult response = s3Client.putObject(request);
            KeyVersion keyVersion = new KeyVersion(key, response.getVersionId());
            keys.add(keyVersion);
        }
        return keys;
    }

    static void multiObjectVersionedDelete(List<KeyVersion> keys) {
        DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(
                bucketName).withKeys(keys);

        DeleteObjectsResult delObjRes = null;
        try {
            delObjRes = s3Client.deleteObjects(multiObjectDeleteRequest);
            System.out.format("Successfully deleted all the %s items.\n", delObjRes.getDeletedObjects().size());
        } catch(MultiObjectDeleteException mode) {
            printDeleteResults(mode);
        }
    }

    static DeleteObjectsResult multiObjectNonVersionedDelete(List<KeyVersion> keys) {

        // Multi-object delete by specifying only keys (no version ID).
        DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(
                bucketName);

        // Create request that include only object key names.
        List<KeyVersion> justKeys = new ArrayList<KeyVersion>();
        for (KeyVersion key : keys) {
            justKeys.add(new KeyVersion(key.getKey()));
        }

        multiObjectDeleteRequest.setKeys(justKeys);
        // Execute DeleteObjects - Amazon S3 add delete marker for each object
        // deletion. The objects no disappear from your bucket (verify).
        DeleteObjectsResult delObjRes = null;
        try {
            delObjRes = s3Client.deleteObjects(multiObjectDeleteRequest);
            System.out.format("Successfully deleted all the %s items.\n", delObjRes.getDeletedObjects().size());
        } catch (MultiObjectDeleteException mode) {
            printDeleteResults(mode);
        }
        return delObjRes;
    }
    static void multiObjectVersionedDeleteRemoveDeleteMarkers(
            DeleteObjectsResult response) {

        List<KeyVersion> keyVersionList = new ArrayList<KeyVersion>();
        for (DeletedObject deletedObject : response.getDeletedObjects()) {
            keyVersionList.add(new KeyVersion(deletedObject.getKey(),
                    deletedObject.getDeleteMarkerVersionId()));
        }
        // Create a request to delete the delete markers.
        DeleteObjectsRequest multiObjectDeleteRequest2 = new DeleteObjectsRequest(
                bucketName).withKeys(keyVersionList);

        // Now delete the delete marker bringing your objects back to the bucket.
        DeleteObjectsResult delObjRes = null;
        try {
            delObjRes = s3Client.deleteObjects(multiObjectDeleteRequest2);
            System.out.format("Successfully deleted all the %s items.\n", delObjRes.getDeletedObjects().size());
        } catch (MultiObjectDeleteException mode) {
            printDeleteResults(mode);
        }
    }
    static void printDeleteResults(MultiObjectDeleteException mode) {
        System.out.format("%s \n", mode.getMessage());
        System.out.format("No. of objects successfully deleted = %s\n", mode.getDeletedObjects().size());
        System.out.format("No. of objects failed to delete = %s\n", mode.getErrors().size());
        System.out.format("Printing error data...\n");
        for (DeleteError deleteError : mode.getErrors()){
            System.out.format("Object Key: %s\t%s\t%s\n", 
                    deleteError.getKey(), deleteError.getCode(), deleteError.getMessage());
        }
    }
}
```