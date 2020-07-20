# What does Amazon S3 replicate?<a name="replication-what-is-isnot-replicated"></a>

Amazon S3 replicates only specific items in buckets that are configured for replication\. 

## What is replicated?<a name="replication-what-is-replicated"></a>

By default Amazon S3 replicates the following:
+ Objects created after you add a replication configuration\.

   
+ Unencrypted objects\. 

   
+ Objects encrypted at rest under Amazon S3 managed keys \(SSE\-S3\) or customer master keys \(CMKs\) stored in AWS Key Management Service \(SSE\-KMS\)\. To replicate objects encrypted with CMKs stored in AWS KMS, you must explicitly enable the option\. The replicated copy of the object is encrypted using the same type of server\-side encryption that was used for the source object\. For more information about server\-side encryption, see [Protecting data using server\-side encryption](serv-side-encryption.md)\.

   
+ Object metadata\.

   
+ Only objects in the source bucket for which the bucket owner has permissions to read objects and access control lists \(ACLs\)\. For more information about resource ownership, see [Amazon S3 bucket and object ownership](access-control-overview.md#about-resource-owner)\.

   
+ Object ACL updates, unless you direct Amazon S3 to change the replica ownership when source and destination buckets aren't owned by the same accounts\. For more information, see [Changing the replica owner](replication-change-owner.md)\. 

   

  It can take a while until Amazon S3 can bring the two ACLs in sync\. This applies only to objects created after you add a replication configuration to the bucket\.

   
+  Object tags, if there are any\.

   
+ S3 Object Lock retention information, if there is any\. When Amazon S3 replicates objects that have retention information applied, it applies those same retention controls to your replicas, overriding the default retention period configured on your destination bucket\. If you don't have retention controls applied to the objects in your source bucket, and you replicate into a destination bucket that has a default retention period set, the destination bucket's default retention period is applied to your object replicas\. For more information, see [Locking objects using S3 Object Lock](object-lock.md)\.

### How delete operations affect replication<a name="replication-delete-op"></a>

If you delete an object from the source bucket, the following occurs:
+ If you make a DELETE request without specifying an object version ID, Amazon S3 adds a delete marker\. Amazon S3 deals with the delete marker as follows:
  + If you are using the latest version of the replication configuration \(that is, you specify the `Filter` element in a replication configuration rule\), Amazon S3 does not replicate the delete marker\.
  + If you don't specify the `Filter` element, Amazon S3 assumes that the replication configuration is an earlier version V1\. In the earlier version, Amazon S3 handled replication of delete markers differently\. For more information, see [Backward compatibility](replication-add-config.md#replication-backward-compat-considerations)\. 
+ If you specify an object version ID to delete in a DELETE request, Amazon S3 deletes that object version in the source bucket\. But it doesn't replicate the deletion in the destination bucket\. In other words, it doesn't delete the same object version from the destination bucket\. This protects data from malicious deletions\. 

## What isn't replicated?<a name="replication-what-is-not-replicated"></a>

By default Amazon S3 doesn't replicate the following:
+  Objects that existed before you added the replication configuration to the bucket\. In other words, Amazon S3 doesn't replicate objects retroactively\.

   
+ The following encrypted objects:
  + Objects created with server\-side encryption using customer\-provided \(SSE\-C\) encryption keys\.
  + Objects created with server\-side encryption using CMKs stored in AWS KMS\. By default, Amazon S3 does not replicate objects encrypted using KMS CMKs\. However, you can explicitly enable replication of these objects in the replication configuration, and provide relevant information so that Amazon S3 can replicate these objects\.

   For more information about server\-side encryption, see [Protecting data using server\-side encryption](serv-side-encryption.md)\. 

   
+ Objects that are stored in S3 Glacier or S3 Glacier Deep Archive storage class\. To learn more about the Amazon S3 Glacier service, see the [Amazon S3 Glacier Developer Guide](https://docs.aws.amazon.com/amazonglacier/latest/dev/)\.

   
+ Objects in the source bucket that the bucket owner doesn't have permissions for \(when the bucket owner is not the owner of the object\)\. For information about how an object owner can grant permissions to a bucket owner, see [Granting Cross\-Account Permissions to Upload Objects While Ensuring the Bucket Owner Has Full Control](example-bucket-policies.md#example-bucket-policies-use-case-8)\.

   
+ Updates to bucket\-level subresources\. For example, if you change the lifecycle configuration or add a notification configuration to your source bucket, these changes are not applied to the destination bucket\. This makes it possible to have different configurations on source and destination buckets\. 

   
+ Actions performed by lifecycle configuration\. 

  For example, if lifecycle configuration is enabled only on your source bucket, Amazon S3 creates delete markers for expired objects but doesn't replicate those markers\. If you want the same lifecycle configuration applied to both source and destination buckets, enable the same lifecycle configuration on both\.

  For more information about lifecycle configuration, see [Object lifecycle management](object-lifecycle-mgmt.md)\.
**Note**  
If using the latest version of the replication configuration \(the XML specifies `Filter` as the child of `Rule`\), delete markers created either by a user action or by Amazon S3 as part of the lifecycle action are not replicated\. However, if you are using an earlier version of the replication configuration \(the XML specifies `Prefix` as the child of `Rule`\), delete markers resulting from user actions are replicated\. For more information, see [Backward compatibility](replication-add-config.md#replication-backward-compat-considerations)\.
+ Objects in the source bucket that are replicas that were created by another replication rule\.

  You can replicate objects from a source bucket to *only one* destination bucket\. After Amazon S3 replicates an object, the object can't be replicated again\. For example, if you change the destination bucket in an existing replication configuration, Amazon S3 won't replicate the object again\.

  Another example: Suppose that you configure replication where bucket A is the source and bucket B is the destination\. Now suppose that you add another replication configuration where bucket B is the source and bucket C is the destination\. In this case, objects in bucket B that are replicas of objects in bucket A are not replicated to bucket C\. 

## Replicating existing objects<a name="existing-object-replication"></a>

 To enable existing object replication for your account, you must contact [AWS Support](https://console.aws.amazon.com/support/home#/case/create?issueType=customer-service&serviceCode=general-info&getting-started&categoryCode=using-aws&services)\. To prevent your request from being delayed, title your AWS Support case "Replication for Existing Objects" and be sure to include the following information:
+ Source bucket
+ Destination bucket
+ Estimated storage volume to replicate \(in terabytes\) 
+ Estimated storage object count to replicate

## Related topics<a name="replication-whatis-isnot-related-topics"></a>
+ [Replication](replication.md)
+ [Overview of setting up replication](replication-how-setup.md)
+ [Replication status information](replication-status.md)