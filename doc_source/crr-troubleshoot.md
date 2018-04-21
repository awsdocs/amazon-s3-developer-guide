# Troubleshooting Cross\-Region Replication in Amazon S3<a name="crr-troubleshoot"></a>

After configuring cross\-region replication, if you don't see the object replica created in the destination bucket, try the following troubleshooting methods:
+ The time it takes for Amazon S3 to replicate an object depends on the object size\. For large objects, it can take up to several hours\. If the object in question is large, check to see if the replicated object appears in the destination bucket again at a later time\.
+ In the replication configuration on the source bucket:
  + Verify that the destination bucket Amazon Resource Name \(ARN\) is correct\.
  + Verify that the key name prefix is correct\. For example, if you set the configuration to replicate objects with the prefix `Tax`, then only objects with key names such as `Tax/document1` or `Tax/document2` are replicated\. An object with the key name `document3` is not replicated\.
  + Verify that the status is `enabled`\.
+ If the destination bucket is owned by another AWS account, verify that the bucket owner has a bucket policy on the destination bucket that allows the source bucket owner to replicate objects\.
+ If an object replica does not appear in the destination bucket, note the following:
  + An object in a source bucket that is itself a replica created by another replication configuration, Amazon S3 does not replicate the replica\. For example, if you set replication configuration from bucket A to bucket B to bucket C, Amazon S3 does not replicate object replicas in bucket B\.
  + A bucket owner can grant other AWS accounts permission to upload objects\. By default, the bucket owner does not have any permissions on the objects created by the other account\. And the replication configuration replicates only the objects for which the bucket owner has access permissions\. The bucket owner can grant other AWS accounts permissions to create objects conditionally requiring explicit access permissions on those objects\. For an example policy, see [Granting Cross\-Account Permissions to Upload Objects While Ensuring the Bucket Owner Has Full Control](example-bucket-policies.md#example-bucket-policies-use-case-8)\.

## Related Topics<a name="crr-troubleshoot-related-topics"></a>

[Cross\-Region Replication \(CRR\)](crr.md)