# Copy an Object Using the AWS SDK for Java<a name="CopyingObjectUsingJava"></a>

The following tasks guide you through using the Java classes to copy an object in Amazon S3\. 


**Copying Objects**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `AmazonS3Client` class\.   | 
|  2  |  Execute one of the `AmazonS3Client.copyObject` methods\. You need to provide the request information, such as source bucket name, source key name, destination bucket name, and destination key\. You provide this information by creating an instance of the `CopyObjectRequest` class or optionally providing this information directly with the `AmazonS3Client.copyObject` method\.  | 

The following Java code example demonstrates the preceding tasks\.

**Example**  

```
1. AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
2. s3client.copyObject(sourceBucketName, sourceKey, 
3.                     destinationBucketName, destinationKey);
```

**Example**  
The following Java code example makes a copy of an object\. The copied object with a different key is saved in the same source bucket\. For instructions on how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\.  

```
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CopyObjectRequest;

public class CopyObjectSingleOperation {
	private static String bucketName     = "*** Provide bucket name ***";
	private static String key            = "*** Provide key ***  ";
	private static String destinationKey = "*** Provide dest. key ***";

    public static void main(String[] args) throws IOException {
        AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
        try {
            // Copying object
            CopyObjectRequest copyObjRequest = new CopyObjectRequest(
            		bucketName, key, bucketName, destinationKey);
            System.out.println("Copying object.");
            s3client.copyObject(copyObjRequest);

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, " +
            		"which means your request made it " + 
            		"to Amazon S3, but was rejected with an error " +
                    "response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, " +
            		"which means the client encountered " +
                    "an internal error while trying to " +
                    " communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
}
```