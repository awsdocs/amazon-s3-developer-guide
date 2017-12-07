# Upload a File<a name="llJavaUploadFile"></a>

 The following tasks guide you through using the low\-level Java classes to upload a file\. 


**Low\-Level API File Uploading Process**  

|  |  | 
| --- |--- |
| 1 | Create an instance of the `AmazonS3Client` class\. | 
| 2 | Initiate multipart upload by executing the `AmazonS3Client.initiateMultipartUpload` method\. You will need to provide the required information, i\.e\., bucket name and key name, to initiate the multipart upload by creating an instance of the `InitiateMultipartUploadRequest` class\.  | 
| 3 |  Save the upload ID that the `AmazonS3Client.initiateMultipartUpload` method returns\. You will need to provide this upload ID for each subsequent multipart upload operation\. | 
| 4 | Upload parts\. For each part upload, execute the `AmazonS3Client.uploadPart` method\. You need to provide part upload information, such as upload ID, bucket name, and the part number\. You provide this information by creating an instance of the `UploadPartRequest` class\.  | 
| 5 | Save the response of the `AmazonS3Client.uploadPart` method in a list\. This response includes the ETag value and the part number you will need to complete the multipart upload\.  | 
| 6 | Repeat tasks 4 and 5 for each part\. | 
| 7 | Execute the AmazonS3Client\.completeMultipartUpload method to complete the multipart upload\.  | 

The following Java code sample demonstrates the preceding tasks\.

**Example**  

```
 1. AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
 2. 
 3. // Create a list of UploadPartResponse objects. You get one of these for
 4. // each part upload.
 5. List<PartETag> partETags = new ArrayList<PartETag>();
 6. 
 7. // Step 1: Initialize.
 8. InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(
 9.                                                     existingBucketName, keyName);
10. InitiateMultipartUploadResult initResponse = 
11.                               s3Client.initiateMultipartUpload(initRequest);
12. 
13. File file = new File(filePath);
14. long contentLength = file.length();
15. long partSize = 5 * 1024 * 1024; // Set part size to 5 MB.
16. 
17. try {
18.     // Step 2: Upload parts.
19.     long filePosition = 0;
20.     for (int i = 1; filePosition < contentLength; i++) {
21.         // Last part can be less than 5 MB. Adjust part size.
22.     	partSize = Math.min(partSize, (contentLength - filePosition));
23.     	
24.         // Create request to upload a part.
25.         UploadPartRequest uploadRequest = new UploadPartRequest()
26.             .withBucketName(existingBucketName).withKey(keyName)
27.             .withUploadId(initResponse.getUploadId()).withPartNumber(i)
28.             .withFileOffset(filePosition)
29.             .withFile(file)
30.             .withPartSize(partSize);
31. 
32.         // Upload part and add response to our list.
33.         partETags.add(s3Client.uploadPart(uploadRequest).getPartETag());
34. 
35.         filePosition += partSize;
36.     }
37. 
38.     // Step 3: Complete.
39.     CompleteMultipartUploadRequest compRequest = new 
40.                 CompleteMultipartUploadRequest(existingBucketName, 
41.                                                keyName, 
42.                                                initResponse.getUploadId(), 
43.                                                partETags);
44. 
45.     s3Client.completeMultipartUpload(compRequest);
46. } catch (Exception e) {
47.     s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(
48.               existingBucketName, keyName, initResponse.getUploadId()));
49. }
```

**Example**  
The following Java code example uploads a file to an Amazon S3 bucket\. For instructions on how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\.   

```
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.UploadPartRequest;

public class UploadObjectMPULowLevelAPI {

    public static void main(String[] args) throws IOException {
        String existingBucketName  = "*** Provide-Your-Existing-BucketName ***"; 
        String keyName             = "*** Provide-Key-Name ***";
        String filePath            = "*** Provide-File-Path ***";   
        
        AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());        

        // Create a list of UploadPartResponse objects. You get one of these
        // for each part upload.
        List<PartETag> partETags = new ArrayList<PartETag>();

        // Step 1: Initialize.
        InitiateMultipartUploadRequest initRequest = new 
             InitiateMultipartUploadRequest(existingBucketName, keyName);
        InitiateMultipartUploadResult initResponse = 
        	                   s3Client.initiateMultipartUpload(initRequest);

        File file = new File(filePath);
        long contentLength = file.length();
        long partSize = 5242880; // Set part size to 5 MB.

        try {
            // Step 2: Upload parts.
            long filePosition = 0;
            for (int i = 1; filePosition < contentLength; i++) {
                // Last part can be less than 5 MB. Adjust part size.
            	partSize = Math.min(partSize, (contentLength - filePosition));
            	
                // Create request to upload a part.
                UploadPartRequest uploadRequest = new UploadPartRequest()
                    .withBucketName(existingBucketName).withKey(keyName)
                    .withUploadId(initResponse.getUploadId()).withPartNumber(i)
                    .withFileOffset(filePosition)
                    .withFile(file)
                    .withPartSize(partSize);

                // Upload part and add response to our list.
                partETags.add(
                		s3Client.uploadPart(uploadRequest).getPartETag());

                filePosition += partSize;
            }

            // Step 3: Complete.
            CompleteMultipartUploadRequest compRequest = new 
                         CompleteMultipartUploadRequest(
                                    existingBucketName, 
                                    keyName, 
                                    initResponse.getUploadId(), 
                                    partETags);

            s3Client.completeMultipartUpload(compRequest);
        } catch (Exception e) {
            s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(
                    existingBucketName, keyName, initResponse.getUploadId()));
        }
    }
}
```