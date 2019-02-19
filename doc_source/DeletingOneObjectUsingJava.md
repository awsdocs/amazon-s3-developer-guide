# Deleting an Object Using the AWS SDK for Java<a name="DeletingOneObjectUsingJava"></a>

You can delete an object from a bucket\. If you have versioning enabled on the bucket, you have the following options:
+ Delete a specific object version by specifying a version ID\.
+ Delete an object without specifying a version ID, in which case S3 adds a delete marker to the object\.

For more information about versioning, see [Object Versioning](ObjectVersioning.md)\. 

**Example Example 1: Deleting an Object \(Non\-Versioned Bucket\)**  
The following example deletes an object from a bucket\. The example assumes that the bucket is not versioning\-enabled and the object doesn't have any version IDs\. In the delete request, you specify only the object key and not a version ID\. For instructions on creating and testing a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\.  

```
 require 'vendor/autoload.php';

use Aws\S3\S3Client;

$bucket = '*** Your Bucket Name ***';
$keyname = '*** Your Object Key ***';

$s3 = new S3Client([
    'version' => 'latest',
    'region'  => 'us-east-1'
]);

// Delete an object from the bucket.
$s3->deleteObject([
    'Bucket' => $bucket,
    'Key'    => $keyname
]);
```

**Example Example 2: Deleting an Object \(Versioned Bucket\)**  
The following example deletes an object from a versioned bucket\. The example deletes a specific object version by specifying the object key name and version ID\. The example does the following:  

1. Adds a sample object to the bucket\. Amazon S3 returns the version ID of the newly added object\. The example uses this version ID in the delete request\.

1. Deletes the object version by specifying both the object key name and a version ID\. If there are no other versions of that object, Amazon S3 deletes the object entirely\. Otherwise, Amazon S3 only deletes the specified version\.
**Note**  
You can get the version IDs of an object by sending a `ListVersions` request\.

```
/**
 * Copyright 2018-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * This file is licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License. A copy of
 * the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
*/

// snippet-sourcedescription:[DeleteObjectVersionEnabledBucket.java demonstrates how to delete an S3 object version from a version-enabled bucket.]
// snippet-service:[s3]
// snippet-keyword:[Java]
// snippet-keyword:[Amazon S3]
// snippet-keyword:[Code Sample]
// snippet-keyword:[DELETE Object]
// snippet-sourcetype:[full-example]
// snippet-sourcedate:[2019-01-28]
// snippet-sourceauthor:[AWS]
// snippet-start:[s3.java.delete_object_version_enabled_bucket.complete]

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

// snippet-end:[s3.java.delete_object_version_enabled_bucket.complete]
```