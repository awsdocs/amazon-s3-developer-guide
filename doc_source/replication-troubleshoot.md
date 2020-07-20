# Troubleshooting replication<a name="replication-troubleshoot"></a>

If object replicas don't appear in the destination bucket after you configure replication, use these troubleshooting tips to identify and fix issues\.
+ The majority of objects replicate within 15 minutes, but they can sometimes take a couple of hours\. In rare cases, the replication can take longer\. The time it takes Amazon S3 to replicate an object depends on several factors, including source and destination Region pair, and the size of the object\. For large objects, replication can take up to several hours\. If the object that is being replicated is large, wait a while before checking to see whether it appears in the destination bucket\. You can also check the source object replication status\. If the object replication status is `pending`, then you know that Amazon S3 has not completed the replication\. If the object replication status is `failed`, check the replication configuration set on the source bucket\.
+ In the replication configuration on the source bucket, verify the following:
  + The Amazon Resource Name \(ARN\) of the destination bucket is correct\.
  + The key name prefix is correct\. For example, if you set the configuration to replicate objects with the prefix `Tax`, then only objects with key names such as `Tax/document1` or `Tax/document2` are replicated\. An object with the key name `document3` is not replicated\.
  + The status is `enabled`\.
+ If the destination bucket is owned by another AWS account, verify that the bucket owner has a bucket policy on the destination bucket that allows the source bucket owner to replicate objects\. For an example, see [Example 2: Configuring replication when the source and destination buckets are owned by different accounts](replication-walkthrough-2.md)\.
+ If an object replica doesn't appear in the destination bucket, the following might have prevented replication:
  + Amazon S3 doesn't replicate an object in a source bucket that is a replica created by another replication configuration\. For example, if you set replication configuration from bucket A to bucket B to bucket C, Amazon S3 doesn't replicate object replicas in bucket B to bucket C\.
  + A source bucket owner can grant other AWS accounts permission to upload objects\. By default, the source bucket owner doesn't have permissions for the objects created by other accounts\. The replication configuration replicates only the objects for which the source bucket owner has access permissions\. The source bucket owner can grant other AWS accounts permissions to create objects conditionally, requiring explicit access permissions on those objects\. For an example policy, see [Granting Cross\-Account Permissions to Upload Objects While Ensuring the Bucket Owner Has Full Control](example-bucket-policies.md#example-bucket-policies-use-case-8)\.
+ Suppose that in the replication configuration, you add a rule to replicate a subset of objects having a specific tag\. In this case, you must assign the specific tag key and value at the time of creating the object for Amazon S3 to replicate the object\. If you first create an object and then add the tag to the existing object, Amazon S3 does not replicate the object\.
+ Replication fails if the bucket policy denies access to the replication role for any of the following actions:

  Source bucket:

  ```
  1.            "s3:GetReplicationConfiguration",
  2.            "s3:ListBucket",
  3.            "s3:GetObjectVersion",
  4.            "s3:GetObjectVersionAcl",
  5.            "s3:GetObjectVersionTagging"
  ```

  Destination bucket:

  ```
  1.            "s3:ReplicateObject",
  2.            "s3:ReplicateDelete",
  3.            "s3:ReplicateTags"
  ```

## Related topics<a name="replication-troubleshoot-related-topics"></a>

[Replication](replication.md)