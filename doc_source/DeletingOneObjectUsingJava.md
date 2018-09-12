# Deleting an Object Using the AWS SDK for Java<a name="DeletingOneObjectUsingJava"></a>

You can delete an object from a bucket\. If you have versioning enabled on the bucket, you have the following options:
+ Delete a specific object version by specifying a version ID\.
+ Delete an object without specifying a version ID, in which case S3 adds a delete marker to the object\.

For more information about versioning, see [Object Versioning](ObjectVersioning.md)\. 

**Example Example 1: Deleting an Object \(Non\-Versioned Bucket\)**  
The following example deletes an object from a bucket\. The example assumes that the bucket is not versioning\-enabled and the object doesn't have any version IDs\. In the delete request, you specify only the object key and not a version ID\. For instructions on creating and testing a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\.  

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

import java.io.IOException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;

public class DeleteObjectNonVersionedBucket {

    public static void main(String[] args) throws IOException {
        String clientRegion = "*** Client region ***";
        String bucketName = "*** Bucket name ***";
        String keyName = "*** Key name ****";

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(clientRegion)
                    .build();

            s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
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
}
```

**Example Example 2: Deleting an Object \(Versioned Bucket\)**  
The following example deletes an object from a versioned bucket\. The example deletes a specific object version by specifying the object key name and version ID\. The example does the following:  

1. Adds a sample object to the bucket\. Amazon S3 returns the version ID of the newly added object\. The example uses this version ID in the delete request\.

1. Deletes the object version by specifying both the object key name and a version ID\. If there are no other versions of that object, Amazon S3 deletes the object entirely\. Otherwise, Amazon S3 only deletes the specified version\.
**Note**  
You can get the version IDs of an object by sending a `ListVersions` request\.

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

import java.io.IOException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketVersioningConfiguration;
import com.amazonaws.services.s3.model.DeleteVersionRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

public class DeleteObjectVersionEnabledBucket {

    public static void main(String[] args) throws IOException {
        String clientRegion = "*** Client region ***";
        String bucketName = "*** Bucket name ***";
        String keyName = "*** Key name ****";

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(clientRegion)
                    .build();

            // Check to ensure that the bucket is versioning-enabled.
            String bucketVersionStatus = s3Client.getBucketVersioningConfiguration(bucketName).getStatus();
            if(!bucketVersionStatus.equals(BucketVersioningConfiguration.ENABLED)) {
                System.out.printf("Bucket %s is not versioning-enabled.", bucketName);
            }
            else {
                // Add an object.
                PutObjectResult putResult = s3Client.putObject(bucketName, keyName, "Sample content for deletion example.");
                System.out.printf("Object %s added to bucket %s\n", keyName, bucketName);
        
                // Delete the version of the object that we just created.
                System.out.println("Deleting versioned object " + keyName);
                s3Client.deleteVersion(new DeleteVersionRequest(bucketName, keyName, putResult.getVersionId()));
                System.out.printf("Object %s, version %s deleted\n", keyName, putResult.getVersionId());
            }
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
}
```