# Upload a File<a name="HLuploadFileJava"></a>

The following tasks guide you through using the high\-level Java classes to upload a file\. The API provides several variations, called *overloads*, of the `upload` method to easily upload your data\.


**High\-Level API File Uploading Process**  

|  |  | 
| --- |--- |
| 1 | Create an instance of the `TransferManager` class\.  | 
| 2 | Execute one of the `TransferManager.upload` overloads depending on whether you are uploading data from a file, or a stream\. | 

The following Java code example demonstrates the preceding tasks\.

**Example**  
The following Java code example uploads a file to an Amazon S3 bucket\. For instructions on how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\.   

```
import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

public class UploadObjectMultipartUploadUsingHighLevelAPI {

    public static void main(String[] args) throws Exception {
        String existingBucketName = "*** Provide existing bucket name ***";
        String keyName            = "*** Provide object key ***";
        String filePath           = "*** Path to and name of the file to upload ***";  
        
        TransferManager tm = new TransferManager(new ProfileCredentialsProvider());        
        System.out.println("Hello");
        // TransferManager processes all transfers asynchronously, 
        // so this call will return immediately.
        Upload upload = tm.upload(
        		existingBucketName, keyName, new File(filePath));
        System.out.println("Hello2");

        try {
        	// Or you can block and wait for the upload to finish
        	upload.waitForCompletion();
        	System.out.println("Upload complete.");
        } catch (AmazonClientException amazonClientException) {
        	System.out.println("Unable to upload file, upload was aborted.");
        	amazonClientException.printStackTrace();
        }
    }
}
```