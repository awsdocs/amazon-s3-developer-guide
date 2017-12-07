# Copy an Object Using the AWS SDK for Java Multipart Upload API<a name="CopyingObjctsUsingLLJavaMPUapi"></a>

The following task guides you through using the Java SDK to copy an Amazon S3 object from one source location to another, such as from one bucket to another\. You can use the code demonstrated here to copy objects greater than 5 GB\. For objects less than 5 GB, use the single operation copy described in [Copy an Object Using the AWS SDK for Java](CopyingObjectUsingJava.md)\. 


**Copying Objects**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `AmazonS3Client` class by providing your AWS credentials\.  | 
|  2  |  Initiate a multipart copy by executing the `AmazonS3Client.initiateMultipartUpload` method\. Create an instance of `InitiateMultipartUploadRequest`\. You will need to provide a bucket name and a key name\.  | 
|  3  |  Save the upload ID from the response object that the `AmazonS3Client.initiateMultipartUpload` method returns\. You will need to provide this upload ID for each subsequent multipart upload operation\.  | 
|  4  |  Copy all the parts\. For each part copy, create a new instance of the `CopyPartRequest` class and provide part information including source bucket, destination bucket, object key, uploadID, first byte of the part, last byte of the part, and the part number\.   | 
|  5  |  Save the response of the `CopyPartRequest` method in a list\. The response includes the ETag value and the part number\. You will need the part number to complete the multipart upload\.   | 
|  6  |  Repeat tasks 4 and 5 for each part\.  | 
|  7  | Execute the AmazonS3Client\.completeMultipartUpload method to complete the copy\.  | 

The following Java code sample demonstrates the preceding tasks\.

**Example**  

```
 1. // Step 1: Create instance and provide credentials.	
 2. AmazonS3Client s3Client = new AmazonS3Client(new 
 3.     PropertiesCredentials(
 4.       	LowLevel_LargeObjectCopy.class.getResourceAsStream(
 5.        		"AwsCredentials.properties")));    
 6. 
 7. // Create lists to hold copy responses
 8. List<CopyPartResult> copyResponses =
 9.         new ArrayList<CopyPartResult>();
10. 
11. // Step 2: Initialize
12. InitiateMultipartUploadRequest initiateRequest = 
13.       	new InitiateMultipartUploadRequest(targetBucketName, targetObjectKey);
14.         
15. InitiateMultipartUploadResult initResult = 
16.        	s3Client.initiateMultipartUpload(initiateRequest);
17. 
18. // Step 3: Save upload Id.
19. String uploadId = initResult.getUploadId();
20. 
21. try {
22. 	
23.     // Get object size.
24.     GetObjectMetadataRequest metadataRequest = 
25.     	new GetObjectMetadataRequest(sourceBucketName, sourceObjectKey);
26. 
27.     ObjectMetadata metadataResult = s3Client.getObjectMetadata(metadataRequest);
28.     long objectSize = metadataResult.getContentLength(); // in bytes
29. 
30.      // Step 4. Copy parts.
31.     long partSize = 5 * (long)Math.pow(2.0, 20.0); // 5 MB
32.     long bytePosition = 0;
33.     for (int i = 1; bytePosition < objectSize; i++)
34.     {
35.         // Step 5. Save copy response.
36.     	CopyPartRequest copyRequest = new CopyPartRequest()
37.            .withDestinationBucketName(targetBucketName)
38.            .withDestinationKey(targetObjectKey)
39.            .withSourceBucketName(sourceBucketName)
40.            .withSourceKey(sourceObjectKey)
41.            .withUploadId(initResult.getUploadId())
42.            .withFirstByte(bytePosition)
43.            .withLastByte(bytePosition + partSize -1 >= objectSize ? objectSize - 1 : bytePosition + partSize - 1) 
44.            .withPartNumber(i);
45. 
46.         copyResponses.add(s3Client.copyPart(copyRequest));
47.         bytePosition += partSize;
48.     }
49.     // Step 7. Complete copy operation.
50.     CompleteMultipartUploadResult completeUploadResponse =
51.         s3Client.completeMultipartUpload(completeRequest);
52. } catch (Exception e) {
53.     System.out.println(e.getMessage());
54. }
```

**Example**  
The following Java code example copies an object from one Amazon S3 bucket to another\. For instructions on how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\.  

```
 1. import java.io.IOException;
 2. import java.util.ArrayList;
 3. import java.util.List;
 4. 
 5. import com.amazonaws.auth.PropertiesCredentials;
 6. import com.amazonaws.services.s3.*;
 7. import com.amazonaws.services.s3.model.*;
 8. 
 9. public class LowLevel_LargeObjectCopy {
10. 
11.     public static void main(String[] args) throws IOException {
12.         String sourceBucketName = "*** Source-Bucket-Name ***";
13.         String targetBucketName = "*** Target-Bucket-Name ***";
14.         String sourceObjectKey  = "*** Source-Object-Key ***";
15.         String targetObjectKey  = "*** Target-Object-Key ***";   
16.         AmazonS3Client s3Client = new AmazonS3Client(new 
17.         		PropertiesCredentials(
18.         				LowLevel_LargeObjectCopy.class.getResourceAsStream(
19.         						"AwsCredentials.properties")));    
20.         
21.         // List to store copy part responses.
22. 
23.         List<CopyPartResult> copyResponses =
24.                   new ArrayList<CopyPartResult>();
25.                           
26.         InitiateMultipartUploadRequest initiateRequest = 
27.         	new InitiateMultipartUploadRequest(targetBucketName, targetObjectKey);
28.         
29.         InitiateMultipartUploadResult initResult = 
30.         	s3Client.initiateMultipartUpload(initiateRequest);
31. 
32.         try {
33.             // Get object size.
34.             GetObjectMetadataRequest metadataRequest = 
35.             	new GetObjectMetadataRequest(sourceBucketName, sourceObjectKey);
36. 
37.             ObjectMetadata metadataResult = s3Client.getObjectMetadata(metadataRequest);
38.             long objectSize = metadataResult.getContentLength(); // in bytes
39. 
40.             // Copy parts.
41.             long partSize = 5 * (long)Math.pow(2.0, 20.0); // 5 MB
42. 
43.             long bytePosition = 0;
44.             for (int i = 1; bytePosition < objectSize; i++)
45.             {
46.             	CopyPartRequest copyRequest = new CopyPartRequest()
47.                    .withDestinationBucketName(targetBucketName)
48.                    .withDestinationKey(targetObjectKey)
49.                    .withSourceBucketName(sourceBucketName)
50.                    .withSourceKey(sourceObjectKey)
51.                    .withUploadId(initResult.getUploadId())
52.                    .withFirstByte(bytePosition)
53.                    .withLastByte(bytePosition + partSize -1 >= objectSize ? objectSize - 1 : bytePosition + partSize - 1) 
54.                    .withPartNumber(i);
55. 
56.                 copyResponses.add(s3Client.copyPart(copyRequest));
57.                 bytePosition += partSize;
58. 
59.             }
60.             CompleteMultipartUploadRequest completeRequest = new 
61.             	CompleteMultipartUploadRequest(
62.             			targetBucketName,
63.             			targetObjectKey,
64.             			initResult.getUploadId(),
65.             			GetETags(copyResponses));
66. 
67.             CompleteMultipartUploadResult completeUploadResponse =
68.                 s3Client.completeMultipartUpload(completeRequest);
69.         } catch (Exception e) {
70.         	System.out.println(e.getMessage());
71.         }
72.      }
73.      
74.     // Helper function that constructs ETags.
75.     static List<PartETag> GetETags(List<CopyPartResult> responses)
76.     {
77.         List<PartETag> etags = new ArrayList<PartETag>();
78.         for (CopyPartResult response : responses)
79.         {
80.             etags.add(new PartETag(response.getPartNumber(), response.getETag()));
81.         }
82.         return etags;
83.     }   
84. }
```