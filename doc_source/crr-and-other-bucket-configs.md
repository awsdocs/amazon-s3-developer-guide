# Cross\-Region Replication: Additional Considerations<a name="crr-and-other-bucket-configs"></a>

In addition to replication configuration, Amazon S3 supports several other bucket configuration options including:
+ Configure versioning on a bucket\. For more information, see [Using Versioning](Versioning.md)\.
+ Configure a bucket for website hosting\. For more information, see [Hosting a Static Website on Amazon S3](WebsiteHosting.md)\.
+ Configure bucket access via a bucket policy or ACL\. For more information, see [Using Bucket Policies and User Policies](using-iam-policies.md) and see [Managing Access with ACLs](S3_ACLs_UsingACLs.md)\.
+ Configure a bucket to store access logs\. For more information, [Server Access Logging](ServerLogs.md)\.
+ Configure the lifecycle for objects in the bucket\. For more information, see [Object Lifecycle Management](object-lifecycle-mgmt.md)\.

This topic explains how bucket replication configuration influences the behavior of other bucket configurations\.

## Lifecycle Configuration and Object Replicas<a name="replica-and-lifecycle"></a>

The time it takes for Amazon S3 to replicate an object depends on object size\. For large objects, it can take several hours\. Even though it might take some time before a replica is available in the destination bucket, creation time of the replica remains the same as the corresponding object in the source bucket\. Therefore, if you have a lifecycle policy on the destination bucket, note that lifecycle rules honor the original creation time of the object, not when the replica became available in the destination bucket\. 

If you have an object expiration lifecycle policy in your non\-versioned bucket, and you want to maintain the same permanent delete behavior when you enable versioning, you must add a noncurrent expiration policy to manage the deletions of the noncurrent object versions in the version\-enabled bucket\.

Replication configuration requires the bucket to be versioning\-enabled\. When you enable versioning on a bucket, keep the following in mind:
+ If you have an object `Expiration` lifecycle policy, after you enable versioning, you should add a `NonCurrentVersionExpiration` policy to maintain the same permanent delete behavior \(that was the case prior enabling versioning\)\.
+ If you have a `Transition` lifecycle policy, after you enable versioning, you should consider adding `NonCurrentVersionTransition` policy\.

## Versioning Configuration and Replication Configuration<a name="crr-and-versioning"></a>

Both the source and destination buckets must be versioning\-enabled when you configure replication on a bucket\. After you enable versioning on both the source and destination buckets and configure replication on the source bucket, note the following:
+ If you attempt to disable versioning on the source bucket, Amazon S3 returns an error\. You must remove the replication configuration before you can disable versioning on the source bucket\.
+ If you disable versioning on the destination bucket, Amazon S3 stops replication\.

## Logging Configuration and Replication Configuration<a name="crr-and-logging"></a>

Note the following:
+ If you have Amazon S3 delivering logs to a bucket that also has replication enabled, Amazon S3 replicates the log objects\.
+ If you have server access logs \([Server Access Logging](ServerLogs.md)\) or AWS CloudTrail Logs \( [Logging Amazon S3 API Calls by Using AWS CloudTrail](cloudtrail-logging.md)\) enabled on your source or destination bucket, Amazon S3 includes the CRR\-related requests in the logs\. For example, Amazon S3 logs each object that it replicates\. 

## CRR and Destination Region<a name="crr-and-dest-region"></a>

In CRR configuration, the source and destination buckets must be in different AWS Regions\. You might choose destination bucket Regions either based on your business needs or cost considerations\. For example, inter\-region data transfer charges vary depending on the Region pairing\. For example, suppose US East \(N\. Virginia\) \(us\-east\-1\) is your source bucket Region\. If you choose US West \(Oregon\) \(us\-west\-2\) as the destination bucket Region, you pay more than if you choose the US East \(Ohio\) \(us\-east\-2\) Region\. For pricing information, see the Data Transfer Pricing section on [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

## Pausing Replication Configuration<a name="crr-pause"></a>

If you want Amazon S3 to temporarily pause replication, you can disable the specific rule in replication configuration\. If replication is enabled and you remove the IAM role that grants Amazon S3 necessary permissions, Amazon S3 fails replicating objects, and reports replication status for those objects as failed\.

## Related Topics<a name="crr-other-config-related-topics"></a>

[Cross\-Region Replication \(CRR\)](crr.md)