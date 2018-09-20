# Cross\-Region Replication: Additional Considerations<a name="crr-and-other-bucket-configs"></a>

Amazon S3 also supports bucket configurations for the following:
+ Versioning\. For more information, see [Using Versioning](Versioning.md)\.
+ Website hosting\. For more information, see [Hosting a Static Website on Amazon S3](WebsiteHosting.md)\.
+ Bucket access through a bucket policy or access control list \(ACL\)\. For more information, see [Using Bucket Policies and User Policies](using-iam-policies.md) and see [Managing Access with ACLs](S3_ACLs_UsingACLs.md)\.
+ Log storage\. For more information, [Amazon S3 Server Access Logging](ServerLogs.md)\.
+ Lifecycle management for objects in a bucket\. For more information, see [Object Lifecycle Management](object-lifecycle-mgmt.md)\.

This topic explains how bucket replication configuration affects the behavior of these bucket configurations\.

## Lifecycle Configuration and Object Replicas<a name="replica-and-lifecycle"></a>

The time it takes for Amazon S3 to replicate an object depends on the size of the object\. For large objects, it can take several hours\. Although it might take a while before a replica is available in the destination bucket, it takes the same amount of time to create the replica as it took to create the corresponding object in the source bucket\. If a lifecycle policy is enabled on the destination bucket, the lifecycle rules honor the original creation time of the object, not when the replica became available in the destination bucket\. 

If you have an object Expiration lifecycle policy in a non\-versioned bucket, and you want to maintain the same permanent delete behavior when you enable versioning, you must add a noncurrent expiration policy to manage the deletions of the noncurrent object versions in the version\-enabled bucket\.

Replication configuration requires the bucket to be versioning\-enabled\. When you enable versioning on a bucket, keep the following in mind:
+ If you have an object Expiration lifecycle policy, after you enable versioning, add a `NonCurrentVersionExpiration` policy to maintain the same permanent delete behavior as before you enabled versioning\.
+ If you have a Transition lifecycle policy, after you enable versioning, consider adding a `NonCurrentVersionTransition` policy\.

## Versioning Configuration and Replication Configuration<a name="crr-and-versioning"></a>

Both the source and destination buckets must be versioning\-enabled when you configure replication on a bucket\. After you enable versioning on both the source and destination buckets and configure replication on the source bucket, you will encounter the following issues:
+ If you attempt to disable versioning on the source bucket, Amazon S3 returns an error\. You must remove the replication configuration before you can disable versioning on the source bucket\.
+ If you disable versioning on the destination bucket, replication fails\. The source object has the replication status `Failed`\.

## Logging Configuration and Replication Configuration<a name="crr-and-logging"></a>

If Amazon S3 delivers logs to a bucket that has replication enabled, it replicates the log objects\.

If server access logs \([Amazon S3 Server Access Logging](ServerLogs.md)\) or AWS CloudTrail Logs \( [Logging Amazon S3 API Calls by Using AWS CloudTrail](cloudtrail-logging.md)\) are enabled on your source or destination bucket, Amazon S3 includes CRR\-related requests in the logs\. For example, Amazon S3 logs each object that it replicates\. 

## CRR and Destination Region<a name="crr-and-dest-region"></a>

In CRR configuration, the source and destination buckets must be in different AWS Regions\. You might choose the Region for you destination bucket based on either your business needs or cost considerations\. For example, inter\-region data transfer charges vary depending on the Regions that you choose\. Suppose that you chose US East \(N\. Virginia\) \(us\-east\-1\) as the Region for your source bucket\. If you choose US West \(Oregon\) \(us\-west\-2\) as the Region for your destination bucket, you pay more than if you choose the US East \(Ohio\) \(us\-east\-2\) Region\. For pricing information, see "Data Transfer Pricing" in [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

## Pausing Replication Configuration<a name="crr-pause"></a>

To temporarily pause replication, disable the relevant rule in the replication configuration\. 

If replication is enabled and you remove the IAM role that grants Amazon S3 the required permissions, replication fails\. Amazon S3 reports the replication status for affected objects as `Failed`\.

## Related Topics<a name="crr-other-config-related-topics"></a>

[Cross\-Region Replication ](crr.md)