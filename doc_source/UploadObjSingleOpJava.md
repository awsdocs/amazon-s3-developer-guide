# Upload an Object Using the AWS SDK for Java<a name="UploadObjSingleOpJava"></a>

The following tasks guide you through using the Java classes to upload a file\. The API provides several variations, called *overloads*, of the `putObject` method to easily upload your data\.


**Uploading Objects**  

|  |  | 
| --- |--- |
| 1 | Create an instance of the `AmazonS3Client`\.  | 
| 2 | Execute one of the `AmazonS3Client.putObject` overloads depending on whether you are uploading data from a file, or a stream\. | 

The following Java code example demonstrates the preceding tasks\.

**Example**  

```
1. AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());        
2. s3client.putObject(new PutObjectRequest(bucketName, keyName, file));
```

**Example**  
The following Java code example uploads a file to an Amazon S3 bucket\. For instructions on how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\.  

```
import java.io.File;
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class UploadObjectSingleOperation {
	private static String bucketName     = "*** Provide bucket name ***";
	private static String keyName        = "*** Provide key ***";
	private static String uploadFileName = "*** Provide file name ***";
	
	public static void main(String[] args) throws IOException {
        AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
        try {
            System.out.println("Uploading a new object to S3 from a file\n");
            File file = new File(uploadFileName);
            s3client.putObject(new PutObjectRequest(
            		                 bucketName, keyName, file));

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
}
```