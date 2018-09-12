# Deleting or Emptying a Bucket<a name="delete-or-empty-bucket"></a>

It is easy to delete an empty bucket\. However, in some situations, you may need to delete or empty a bucket that contains objects\. In this section, we'll explain how to delete objects in an unversioned bucket, and how to delete object versions and delete markers in a bucket that has versioning enabled\. For more information about versioning, see [Using Versioning](Versioning.md)\. In some situations, you may choose to empty a bucket instead of deleting it\. This section explains various options you can use to delete or empty a bucket that contains objects\.

**Topics**
+ [Delete a Bucket](#delete-bucket)
+ [Empty a Bucket](#empty-bucket)

## Delete a Bucket<a name="delete-bucket"></a>

You can delete a bucket and its content programmatically using the AWS SDKs\. You can also use lifecycle configuration on a bucket to empty its content and then delete the bucket\. There are additional options, such as using Amazon S3 console and AWS CLI, but there are limitations on these methods based on the number of objects in your bucket and the bucket's versioning status\.

### Delete a Bucket: Using the Amazon S3 Console<a name="delete-bucket-console"></a>

The Amazon S3 console supports deleting a bucket that may or may not be empty\. For information about using the Amazon S3 console to delete a bucket, see [How Do I Delete an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/delete-bucket.html) in the *Amazon Simple Storage Service Console User Guide*\.

### Delete a Bucket: Using the AWS CLI<a name="delete-bucket-awscli"></a>

You can delete a bucket that contains objects using the AWS CLI only if the bucket does not have versioning enabled\. If your bucket does not have versioning enabled, you can use the `rb` \(remove bucket\) AWS CLI command with `--force` parameter to remove a non\-empty bucket\. This command deletes all objects first and then deletes the bucket\.

```
  $ aws s3 rb s3://bucket-name --force  
```

For more information, see [Using High\-Level S3 Commands with the AWS Command Line Interface](http://docs.aws.amazon.com/cli/latest/userguide/using-s3-commands.html) in the AWS Command Line Interface User Guide\.

To delete a non\-empty bucket that does not have versioning enabled, you have the following options:
+ Delete the bucket programmatically using the AWS SDK\. 
+ Delete all of the objects using the bucket's lifecycle configuration and then delete the empty bucket using the Amazon S3 console\. 

### Delete a Bucket: Using Lifecycle Configuration<a name="delete-bucket-lifecycle"></a>

You can configure lifecycle on your bucket to expire objects, Amazon S3 then deletes expired objects\. You can add lifecycle configuration rules to expire all or a subset of objects with a specific key name prefix\. For example, to remove all objects in a bucket, you can set a lifecycle rule to expire objects one day after creation\.

If your bucket has versioning enabled, you can also configure the rule to expire noncurrent objects\. 

After Amazon S3 deletes all of the objects in your bucket, you can delete the bucket or keep it\. 

**Important**  
If you just want to empty the bucket and not delete it, make sure you remove the lifecycle configuration rule you added to empty the bucket so that any new objects you create in the bucket will remain in the bucket\.

For more information, see [Object Lifecycle Management](object-lifecycle-mgmt.md) and [Configuring Object Expiration](lifecycle-expire-general-considerations.md)\. 

### Delete a Bucket: Using the AWS SDKs<a name="delete-bucket-awssdks"></a>

You can use the AWS SDKs to delete a bucket\. The following sections provide examples of how to delete a bucket using the AWS SDK for Java and \.NET\. First, the code deletes objects in the bucket and then it deletes the bucket\. For information about other AWS SDKs, see [Tools for Amazon Web Services](https://aws.amazon.com/tools/)\.

#### Delete a Bucket Using the AWS SDK for Java<a name="delete-bucket-sdk-java"></a>

The following Java example deletes a bucket that contains objects\. The example deletes all objects, and then it deletes the bucket\. The example works for buckets with or without versioning enabled\.

**Note**  
For buckets without versioning enabled, you can delete all objects directly and then delete the bucket\. For buckets with versioning enabled, you must delete all object versions before deleting the bucket\.

For instructions on creating and testing a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\. 

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

import java.util.Iterator;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListVersionsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.VersionListing;

public class DeleteBucket {

    public static void main(String[] args) {
        String clientRegion = "*** Client region ***";
        String bucketName = "*** Bucket name ***";

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(clientRegion)
                    .build();
    
            // Delete all objects from the bucket. This is sufficient
            // for unversioned buckets. For versioned buckets, when you attempt to delete objects, Amazon S3 inserts
            // delete markers for all objects, but doesn't delete the object versions.
            // To delete objects from versioned buckets, delete all of the object versions before deleting
            // the bucket (see below for an example).
            ObjectListing objectListing = s3Client.listObjects(bucketName);
            while (true) {
                Iterator<S3ObjectSummary> objIter = objectListing.getObjectSummaries().iterator();
                while (objIter.hasNext()) {
                    s3Client.deleteObject(bucketName, objIter.next().getKey());
                }
    
                // If the bucket contains many objects, the listObjects() call
                // might not return all of the objects in the first listing. Check to
                // see whether the listing was truncated. If so, retrieve the next page of objects 
                // and delete them.
                if (objectListing.isTruncated()) {
                    objectListing = s3Client.listNextBatchOfObjects(objectListing);
                } else {
                    break;
                }
            }
    
            // Delete all object versions (required for versioned buckets).
            VersionListing versionList = s3Client.listVersions(new ListVersionsRequest().withBucketName(bucketName));
            while (true) {
                Iterator<S3VersionSummary> versionIter = versionList.getVersionSummaries().iterator();
                while (versionIter.hasNext()) {
                    S3VersionSummary vs = versionIter.next();
                    s3Client.deleteVersion(bucketName, vs.getKey(), vs.getVersionId());
                }
    
                if (versionList.isTruncated()) {
                    versionList = s3Client.listNextBatchOfVersions(versionList);
                } else {
                    break;
                }
            }
    
            // After all objects and object versions are deleted, delete the bucket.
            s3Client.deleteBucket(bucketName);
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client couldn't
            // parse the response from Amazon S3.
            e.printStackTrace();
        }
    }
}
```

## Empty a Bucket<a name="empty-bucket"></a>

You can empty a bucket's content \(that is, delete all content, but keep the bucket\) programmatically using the AWS SDK\. You can also specify lifecycle configuration on a bucket to expire objects so that Amazon S3 can delete them\. There are additional options, such as using Amazon S3 console and AWS CLI, but there are limitations on this method based on the number of objects in your bucket and the bucket's versioning status\.

**Topics**
+ [Empty a Bucket: Using the Amazon S3 console](#empty-bucket-console)
+ [Empty a Bucket: Using the AWS CLI](#empty-bucket-awscli)
+ [Empty a Bucket: Using Lifecycle Configuration](#empty-bucket-lifecycle)
+ [Empty a Bucket: Using the AWS SDKs](#empty-bucket-awssdks)

### Empty a Bucket: Using the Amazon S3 console<a name="empty-bucket-console"></a>

For information about using the Amazon S3 console to empty a bucket, see [How Do I Empty an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/empty-bucket.html) in the *Amazon Simple Storage Service Console User Guide*

### Empty a Bucket: Using the AWS CLI<a name="empty-bucket-awscli"></a>

You can empty a bucket using the AWS CLI only if the bucket does not have versioning enabled\. If your bucket does not have versioning enabled, you can use the `rm` \(remove\) AWS CLI command with the `--recursive` parameter to empty a bucket \(or remove a subset of objects with a specific key name prefix\)\. 

The following `rm` command removes objects with key name prefix `doc`, for example, `doc/doc1` and `doc/doc2`\.

```
$ aws s3 rm s3://bucket-name/doc --recursive
```

Use the following command to remove all objects without specifying a prefix\.

```
$ aws s3 rm s3://bucket-name --recursive
```

For more information, see [Using High\-Level S3 Commands with the AWS Command Line Interface](http://docs.aws.amazon.com/cli/latest/userguide/using-s3-commands.html) in the AWS Command Line Interface User Guide\.

**Note**  
You cannot remove objects from a bucket with versioning enabled\. Amazon S3 adds a delete marker when you delete an object, which is what this command will do\. For more information about versioning, see [Using Versioning](Versioning.md)\.

To empty a bucket with versioning enabled, you have the following options:
+ Delete the bucket programmatically using the AWS SDK\. 
+ Use the bucket's lifecycle configuration to request that Amazon S3 delete the objects\. 
+ Use the Amazon S3 console \(see [How Do I Empty an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/empty-bucket.html) in the *Amazon Simple Storage Service Console User Guide*\)\.

### Empty a Bucket: Using Lifecycle Configuration<a name="empty-bucket-lifecycle"></a>

You can configure lifecycle on your bucket to expire objects and request that Amazon S3 delete expired objects\. You can add lifecycle configuration rules to expire all or a subset of objects with a specific key name prefix\. For example, to remove all objects in a bucket, you can set lifecycle rule to expire objects one day after creation\.

If your bucket has versioning enabled, you can also configure the rule to expire noncurrent objects\. 

**Warning**  
After your objects expire, Amazon S3 deletes the expired objects\. If you just want to empty the bucket and not delete it, make sure you remove the lifecycle configuration rule you added to empty the bucket so that any new objects you create in the bucket will remain in the bucket\.

For more information, see [Object Lifecycle Management](object-lifecycle-mgmt.md) and [Configuring Object Expiration](lifecycle-expire-general-considerations.md)\.

### Empty a Bucket: Using the AWS SDKs<a name="empty-bucket-awssdks"></a>

You can use the AWS SDKs to empty a bucket or remove a subset of objects with a specific key name prefix\.

For an example of how to empty a bucket using AWS SDK for Java, see [Delete a Bucket Using the AWS SDK for Java](#delete-bucket-sdk-java)\. The code deletes all objects, regardless of whether the bucket has versioning enabled or not, and then it deletes the bucket\. To just empty the bucket, make sure you remove the statement that deletes the bucket\. 

For more information about using other AWS SDKs, see [Tools for Amazon Web Services](https://aws.amazon.com/tools/)\.