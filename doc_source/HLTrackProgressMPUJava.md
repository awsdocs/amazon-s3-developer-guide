# Track Multipart Upload Progress<a name="HLTrackProgressMPUJava"></a>

The high\-level multipart upload API provides a listen interface, `ProgressListener`, to track the upload progress when uploading data using the `TransferManager` class\. To use the event in your code, you must import the `com.amazonaws.services.s3.model.ProgressEvent` and `com.amazonaws.services.s3.model.ProgressListener ` types\.

Progress events occurs periodically and notify the listener that bytes have been transferred\. 

The following Java code sample demonstrates how you can subscribe to the `ProgressEvent` event and write a handler\.

**Example**  

```
 1. TransferManager tm = new TransferManager(new ProfileCredentialsProvider());        
 2. 
 3. PutObjectRequest request = new PutObjectRequest(
 4.   		existingBucketName, keyName, new File(filePath));
 5. 
 6. // Subscribe to the event and provide event handler.        
 7. request.setProgressListener(new ProgressListener() {
 8. 			public void progressChanged(ProgressEvent event) {
 9. 				System.out.println("Transferred bytes: " + 
10. 						event.getBytesTransfered());
11.              }
12. });
```

**Example**  
The following Java code uploads a file and uses the `ProgressListener` to track the upload progress\. For instructions on how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\.   

```
import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

public class TrackMPUProgressUsingHighLevelAPI {

    public static void main(String[] args) throws Exception {
        String existingBucketName = "*** Provide bucket name ***";
        String keyName            = "*** Provide object key ***";
        String filePath           = "*** file to upload ***";  
        
        TransferManager tm = new TransferManager(new ProfileCredentialsProvider());        

        // For more advanced uploads, you can create a request object 
        // and supply additional request parameters (ex: progress listeners,
        // canned ACLs, etc.)
        PutObjectRequest request = new PutObjectRequest(
        		existingBucketName, keyName, new File(filePath));
        
        // You can ask the upload for its progress, or you can 
        // add a ProgressListener to your request to receive notifications 
        // when bytes are transferred.
        request.setGeneralProgressListener(new ProgressListener() {
			@Override
			public void progressChanged(ProgressEvent progressEvent) {
				System.out.println("Transferred bytes: " + 
						progressEvent.getBytesTransferred());
			}
		});

        // TransferManager processes all transfers asynchronously, 
        // so this call will return immediately.
        Upload upload = tm.upload(request);
        
        try {
        	// You can block and wait for the upload to finish
        	upload.waitForCompletion();
        } catch (AmazonClientException amazonClientException) {
        	System.out.println("Unable to upload file, upload aborted.");
        	amazonClientException.printStackTrace();
        }
    }
}
```