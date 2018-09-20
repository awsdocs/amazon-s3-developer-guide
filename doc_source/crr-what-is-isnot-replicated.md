# What Does Amazon S3 Replicate?<a name="crr-what-is-isnot-replicated"></a>

Amazon S3 replicates only specific items in buckets that are configured for cross\-region replication\. 

## What Is Replicated?<a name="crr-what-is-replicated"></a>

Amazon S3 replicates the following:
+ Objects created after you add a replication configuration, with exceptions described in the next section\.

   
+ Both unencrypted objects and objects encrypted using Amazon S3 managed keys \(SSE\-S3\) or AWS KMS managed keys \(SSE\-KMS\), although you must explicitly enable the option to replicate objects encrypted using KMS keys\. The replicated copy of the object is encrypted using the same type of server\-side encryption that was used for the source object\. For more information about server\-side encryption, see [Protecting Data Using Server\-Side Encryption](serv-side-encryption.md)\.

   
+ Object metadata\.

   
+ Only objects in the source bucket for which the bucket owner has permissions to read objects and access control lists \(ACLs\)\. For more information about resource ownership, see [About the Resource Owner](access-control-overview.md#about-resource-owner)\.

   
+ Object ACL updates, unless you direct Amazon S3 to change the replica ownership when source and destination buckets aren't owned by the same accounts 

   \(see [CRR Additional Configuration: Changing the Replica Owner](crr-change-owner.md)\)\. 

   

  It can take awhile until Amazon S3 can bring the two ACLs in sync\. This applies only to objects created after you add a replication configuration to the bucket\.

   
+  Object tags, if there are any\.

### How Delete Operations Affect CRR<a name="crr-delete-op"></a>

If you delete an object from the source bucket, the following occurs:
+ If you make a DELETE request without specifying an object version ID, Amazon S3 adds a delete marker\. Amazon S3 deals with the delete marker as follows:
  + If using latest version of the replication configuration, that is you specify the `Filter` element in a replication configuration rule, Amazon S3 does not replicate the delete marker\.
  + If don't specify the `Filter` element, Amazon S3 assumes replication configuration is a prior version V1\. In the earlier version, Amazon S3 handled replication of delete markers differently\. For more information, see [Backward Compatibility ](crr-add-config.md#crr-backward-compat-considerations)\. 
+ If you specify an object version ID to delete in a DELETE request, Amazon S3 deletes that object version in the source bucket, but it doesn't replicate the deletion in the destination bucket\. In other words, it doesn't delete the same object version from the destination bucket\. This protects data from malicious deletions\. 

## What Isn't Replicated?<a name="crr-what-is-not-replicated"></a>

Amazon S3 doesn't replicate the following:
+  Objects that existed before you added the replication configuration to the bucket\. In other words, Amazon S3 doesn't replicate objects retroactively\.

   
+ The following encrypted objects:
  + Objects created with server\-side encryption using customer\-provided \(SSE\-C\) encryption keys\.
  + Objects created with server\-side encryption using AWS KMS–managed encryption \(SSE\-KMS\) keys\. By default, Amazon S3 does not replicate objects encrypted using KMS keys\. However, you can explicitly enable replication of these objects in the replication configuration, and provide relevant information so that Amazon S3 can replicate these objects\. 

   For more information about server\-side encryption, see [Protecting Data Using Server\-Side Encryption](serv-side-encryption.md)\. 

   
+ Objects in the source bucket that the bucket owner doesn't have permissions for \(when the bucket owner is not the owner of the object\)\. For information about how an object owner can grant permissions to a bucket owner, see [Granting Cross\-Account Permissions to Upload Objects While Ensuring the Bucket Owner Has Full Control](example-bucket-policies.md#example-bucket-policies-use-case-8)\.

   
+ Updates to bucket\-level subresources\. For example, if you change the lifecycle configuration or add a notification configuration to your source bucket, these changes are not applied to the destination bucket\. This makes it possible to have different configurations on source and destination buckets\. 

   
+ Actions performed by lifecycle configuration\. 

  For example, if lifecycle configuration is enabled only on your source bucket, Amazon S3 creates delete markers for expired objects, but it does not replicate those markers\. If you want the same lifecycle configuration applied to both source and destination buckets, enable the same lifecycle configuration on both\.

  For more information about lifecycle configuration, see [Object Lifecycle Management](object-lifecycle-mgmt.md)\.
**Note**  
If using the latest version of the replication configuration \(the XML specifies `Filter` as the child of `Rule`\), delete makers created either by a user action or by Amazon S3 as part of the lifecyle action are not replicated\. However, if using earlier version of the replication configuration \(the XML specifies `Prefix` as the child of `Rule`\), delete markers resulting from user actions are replicated\. For more information, see [Backward Compatibility ](crr-add-config.md#crr-backward-compat-considerations)\.
+ Objects in the source bucket that are replicas that were created by another cross\-region replication\.

  You can replicate objects from a source bucket to *only one* destination bucket\. After Amazon S3 replicates an object, the object can't be replicated again\. For example, if you change the destination bucket in an existing replication configuration, Amazon S3 won't replicate the object again\.

  Another example: suppose that you configure cross\-region replication where bucket A is the source and bucket B is the destination\. Now suppose that you add another cross\-region replication configuration where bucket B is the source and bucket C is the destination\. In this case, objects in bucket B that are replicas of objects in bucket A are not replicated to bucket C\. 

## Related Topics<a name="crr-whatis-isnot-related-topics"></a>

[Cross\-Region Replication ](crr.md)

[Overview of Setting Up CRR ](crr-how-setup.md)

[Cross\-Region Replication: Status Information](crr-status.md)