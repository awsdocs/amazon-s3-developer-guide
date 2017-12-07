# Abort Multipart Uploads<a name="HLAbortMPUploadsJava"></a>

The `TransferManager` class provides a method, `abortMultipartUploads`, to abort multipart uploads in progress\. An upload is considered to be in progress once you initiate it and until you complete it or abort it\. You provide a `Date` value and this API aborts all the multipart uploads, on that bucket, that were initiated before the specified `Date` and are still in progress\. 

Because you are billed for all storage associated with uploaded parts \(see [Multipart Upload and Pricing](mpuoverview.md#mpuploadpricing)\), it is important that you either complete the multipart upload to have the object created or abort the multipart upload to remove any uploaded parts\.

The following tasks guide you through using the high\-level Java classes to abort multipart uploads\.


**High\-Level API Multipart Uploads Aborting Process**  

|  |  | 
| --- |--- |
| 1 | Create an instance of the `TransferManager` class\.  | 
| 2 | Execute the `TransferManager.abortMultipartUploads` method by passing the bucket name and a `Date` value\. | 

The following Java code example demonstrates the preceding tasks\.

**Example**  
The following Java code aborts all multipart uploads in progress that were initiated on a specific bucket over a week ago\. For instructions on how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\.   

```
import java.util.Date;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.transfer.TransferManager;

public class AbortMPUUsingHighLevelAPI {

    public static void main(String[] args) throws Exception {
        String existingBucketName = "*** Provide existing bucket name ***";
        
        TransferManager tm = new TransferManager(new ProfileCredentialsProvider());        

        int sevenDays = 1000 * 60 * 60 * 24 * 7;
		Date oneWeekAgo = new Date(System.currentTimeMillis() - sevenDays);
        
        try {
        	tm.abortMultipartUploads(existingBucketName, oneWeekAgo);
        } catch (AmazonClientException amazonClientException) {
        	System.out.println("Unable to upload file, upload was aborted.");
        	amazonClientException.printStackTrace();
        }
    }
}
```

**Note**  
You can also abort a specific multipart upload\. For more information, see [Abort a Multipart Upload](LLAbortMPUJava.md)\. 