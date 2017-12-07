# What Is and Is Not Replicated<a name="crr-what-is-isnot-replicated"></a>

This section explains what Amazon S3 replicates and what it does not replicate after you add a replication configuration on a bucket\.

## What Is Replicated<a name="crr-what-is-replicated"></a>

Amazon S3 replicates the following:

+ Any new objects created after you add a replication configuration, with exceptions described in the next section\.

   

+ In addition to unencrypted objects, Amazon S3 replicates objects created with server\-side encryption using the Amazon S3\-managed encryption key\. The replicated copy of the object is also encrypted using server\-side encryption with the Amazon S3\-managed encryption key\. 

   

+ Along with the objects, Amazon S3 also replicates object metadata\.

   

+ Amazon S3 replicates only objects in the source bucket for which the bucket owner has permissions to read objects and read access control lists \(ACLs\)\. For more information about resource ownership, see [About the Resource Owner](access-control-overview.md#about-resource-owner)\.

   

+ Any object ACL updates are replicated, unless you directed Amazon S3 to change the replica ownership in a cross\-account scenario \(see [Cross\-Region Replication Additional Configuration: Change Replica Owner](crr-change-owner.md)\)\. 

   

  There can be some delay before Amazon S3 can bring the two ACLs in sync\. This applies only to objects created after you add a replication configuration to the bucket\.

   

+ Amazon S3 replicates object tags, if any\.

### Delete Operation and Cross\-Region Replication<a name="crr-delete-op"></a>

If you delete an object from the source bucket, the cross\-region replication behavior is as follows:

+ If a DELETE request is made without specifying an object version ID, Amazon S3 adds a delete marker, which cross\-region replication replicates to the destination bucket\. For more information about versioning and delete markers, see [Using Versioning](Versioning.md)\.

   

+ If a DELETE request specifies a particular object version ID to delete, Amazon S3 deletes that object version in the source bucket, but it does not replicate the deletion in the destination bucket \(in other words, it does not delete the same object version from the destination bucket\)\. This behavior protects data from malicious deletions\. 

## What Is Not Replicated<a name="crr-what-is-not-replicated"></a>

Amazon S3 does not replicate the following:

+ Amazon S3 does not retroactively replicate objects that existed before you added replication configuration\.

   

+ The following encrypted objects are not replicated:

  + Objects created with server\-side encryption using customer\-provided \(SSE\-C\) encryption keys\.

  + Objects created with server\-side encryption using AWS KMS–managed encryption \(SSE\-KMS\) keys, unless you explicitly enable this option\. 

   For more information about server\-side encryption, see [Protecting Data Using Server\-Side Encryption](serv-side-encryption.md)\. 

   

+ Objects in the source bucket for which the bucket owner does not have permissions\. This can happen when the object owner is different from the bucket owner\. For information about how an object owner can grant permissions to the bucket owner, see [Granting Cross\-Account Permissions to Upload Objects While Ensuring the Bucket Owner Has Full Control](example-bucket-policies.md#example-bucket-policies-use-case-8)\.

   

+ Updates to bucket\-level subresources are not replicated\. For example, you might change lifecycle configuration on your source bucket or add notification configuration to your source bucket\. These changes are not applied to the destination bucket\. This allows you to have different bucket configurations on the source and destination buckets\. 

   

+ Only customer actions are replicated\. Actions performed by lifecycle configuration are not replicated\. For more information about lifecycle configuration, see [Object Lifecycle Management](object-lifecycle-mgmt.md)\.

   

  For example, if lifecycle configuration is enabled only on your source bucket, Amazon S3 creates delete markers for expired objects, but it does not replicate those markers\. However, you can have the same lifecycle configuration on both the source and destination buckets if you want the same lifecycle configuration applied to both buckets\. 

   

+ Objects in the source bucket that are replicas, created by another cross\-region replication, are not replicated\.

   

  Suppose that you configure cross\-region replication where bucket A is the source and bucket B is the destination\. Now suppose that you add another cross\-region replication where bucket B is the source and bucket C is the destination\. In this case, objects in bucket B that are replicas of objects in bucket A are not replicated to bucket C\. 

## Related Topics<a name="crr-whatis-isnot-related-topics"></a>

[Cross\-Region Replication \(CRR\)](crr.md)

[Setting Up Cross\-Region Replication](crr-how-setup.md)

[Finding the Cross\-Region Replication Status ](crr-status.md)