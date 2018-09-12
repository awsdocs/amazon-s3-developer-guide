# Copy an Object Using the AWS SDK for Java Multipart Upload API<a name="CopyingObjctsUsingLLJavaMPUapi"></a>

To copy an Amazon S3 object that is larger than 5 GB with the AWS SDK for Java, use the low\-level Java API \. For objects smaller than 5 GB, use the single\-operation copy described in [Copy an Object Using the AWS SDK for Java](CopyingObjectUsingJava.md)\. 

To copy an object using the low\-level Java API, do the following:
+ Initiate a multipart upload by executing the `AmazonS3Client.initiateMultipartUpload()` method\.
+ Save the upload ID from the response object that the `AmazonS3Client.initiateMultipartUpload()` method returns\. You provide this upload ID for each part\-upload operation\.
+ Copy all of the parts\. For each part that you need to copy, create a new instance of the `CopyPartRequest` class\. Provide the part information, including the source and destination bucket names, source and destination object keys, upload ID, locations of the first and last bytes of the part, and part number\. 
+ Save the responses of the `AmazonS3Client.copyPart()` method calls\. Each response includes the `ETag` value and part number for the uploaded part\. You need this information to complete the multipart upload\. 
+ Call the `AmazonS3Client.completeMultipartUpload()` method to complete the copy operation\. 

**Example**  
The following example shows how to use the Amazon S3 low\-level Java API to perform a multipart copy\. For instructions on creating and testing a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\.  

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.*;

public class LowLevelMultipartCopy {

    public static void main(String[] args) throws IOException {
        String clientRegion = "*** Client region ***";
        String sourceBucketName = "*** Source bucket name ***";
        String sourceObjectKey = "*** Source object key ***";
        String destBucketName = "*** Target bucket name ***";
        String destObjectKey = "*** Target object key ***";

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(clientRegion)
                    .build();

            // Initiate the multipart upload.
            InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(destBucketName, destObjectKey);
            InitiateMultipartUploadResult initResult = s3Client.initiateMultipartUpload(initRequest);
    
            // Get the object size to track the end of the copy operation.
            GetObjectMetadataRequest metadataRequest = new GetObjectMetadataRequest(sourceBucketName, sourceObjectKey);
            ObjectMetadata metadataResult = s3Client.getObjectMetadata(metadataRequest);
            long objectSize = metadataResult.getContentLength();
    
            // Copy the object using 5 MB parts.
            long partSize = 5 * 1024 * 1024;
            long bytePosition = 0;
            int partNum = 1;
            List<CopyPartResult> copyResponses = new ArrayList<CopyPartResult>();
            while (bytePosition < objectSize) {
                // The last part might be smaller than partSize, so check to make sure
                // that lastByte isn't beyond the end of the object.
                long lastByte = Math.min(bytePosition + partSize - 1, objectSize - 1);
                
                // Copy this part.
                CopyPartRequest copyRequest = new CopyPartRequest()
                        .withSourceBucketName(sourceBucketName)
                        .withSourceKey(sourceObjectKey)
                        .withDestinationBucketName(destBucketName)
                        .withDestinationKey(destObjectKey)
                        .withUploadId(initResult.getUploadId())
                        .withFirstByte(bytePosition)
                        .withLastByte(lastByte)
                        .withPartNumber(partNum++);
                copyResponses.add(s3Client.copyPart(copyRequest));
                bytePosition += partSize;
            }
    
            // Complete the upload request to concatenate all uploaded parts and make the copied object available.
            CompleteMultipartUploadRequest completeRequest = new CompleteMultipartUploadRequest(
                                                                        destBucketName,
                                                                        destObjectKey, 
                                                                        initResult.getUploadId(),
                                                                        getETags(copyResponses));
            s3Client.completeMultipartUpload(completeRequest);
            System.out.println("Multipart copy complete.");
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client  
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }

    // This is a helper function to construct a list of ETags.
    private static List<PartETag> getETags(List<CopyPartResult> responses) {
        List<PartETag> etags = new ArrayList<PartETag>();
        for (CopyPartResult response : responses) {
            etags.add(new PartETag(response.getPartNumber(), response.getETag()));
        }
        return etags;
    }
}
```