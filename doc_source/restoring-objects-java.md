# Restore an Archived Object Using the AWS SDK for Java<a name="restoring-objects-java"></a>

The following tasks guide you through use the AWS SDK for Java to initiate a restoration of an archived object\.


**Downloading Objects**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `AmazonS3Client` class\.   | 
|  2  |  Create an instance of `RestoreObjectRequest` class by providing bucket name, object key to restore and the number of days for which you the object copy restored\.  | 
|  3  |  Execute one of the `AmazonS3.RestoreObject` methods to initiate the archive restoration\.  | 

The following Java code sample demonstrates the preceding tasks\.

**Example**  

```
1. String bucketName = "examplebucket";
2. String objectkey = "examplekey";
3. AmazonS3Client s3Client = new AmazonS3Client();
4. 
5. RestoreObjectRequest request = new RestoreObjectRequest(bucketName, objectkey, 2);
6. s3Client.restoreObject(request);
```

Amazon S3 maintains the restoration status in the object metadata\. You can retrieve object metadata and check the value of the `RestoreInProgress` property as shown in the following Java code snippet\.

```
 1. String bucketName = "examplebucket";
 2. String objectkey = "examplekey";
 3. AmazonS3Client s3Client = new AmazonS3Client();
 4. 
 5. client = new AmazonS3Client();
 6. 
 7. GetObjectMetadataRequest request = new GetObjectMetadataRequest(bucketName, objectKey);
 8.   
 9. ObjectMetadata response = s3Client.getObjectMetadata(request);
10.   
11. Boolean restoreFlag = response.getOngoingRestore();
12. System.out.format("Restoration status: %s.\n", 
13.           (restoreFlag == true) ? "in progress" : "finished");
```

**Example**  
The following Java code example initiates a restoration request for the specified archived object\. You must update the code and provide a bucket name and an archived object key name\. For instructions on how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\.  

```
import java.io.IOException;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.RestoreObjectRequest;
    
public class RestoreArchivedObject {

    public static String bucketName = "*** Provide bucket name ***"; 
    public static String objectKey =  "*** Provide object key name ***";
    public static AmazonS3Client s3Client;

    public static void main(String[] args) throws IOException {
        AmazonS3Client s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
        
        try {

          RestoreObjectRequest requestRestore = new RestoreObjectRequest(bucketName, objectKey, 2);
          s3Client.restoreObject(requestRestore);
          
          GetObjectMetadataRequest requestCheck = new GetObjectMetadataRequest(bucketName, objectKey);          
          ObjectMetadata response = s3Client.getObjectMetadata(requestCheck);
          
          Boolean restoreFlag = response.getOngoingRestore();
          System.out.format("Restoration status: %s.\n", 
                  (restoreFlag == true) ? "in progress" : "finished");
            
        } catch (AmazonS3Exception amazonS3Exception) {
            System.out.format("An Amazon S3 error occurred. Exception: %s", amazonS3Exception.toString());
        } catch (Exception ex) {
            System.out.format("Exception: %s", ex.toString());
        }        
    }
}
```