# Cross\-Region Replication \(CRR\)<a name="crr"></a>

Cross\-region replication is a bucket\-level configuration that enables automatic, asynchronous copying of objects across buckets in different AWS Regions\. We refer to these buckets as *source* bucket and *destination* bucket\. These buckets can be owned by different AWS accounts\. 

To activate this feature, you add a replication configuration to your source bucket to direct Amazon S3 to replicate objects according to the configuration\. In the replication configuration, you provide information such as the following:
+ The destination bucket where you want Amazon S3 to replicate the objects\.

   
+ The objects you want to replicate\. You can request Amazon S3 to replicate all or a subset of objects by providing a key name prefix in the configuration\. For example, you can configure cross\-region replication to replicate only objects with the key name prefix `Tax/`\. This causes Amazon S3 to replicate objects with a key such as `Tax/doc1` or `Tax/doc2`, but not an object with the key `Legal/doc3`\. 

   
+ By default, Amazon S3 uses the storage class of the source object to create an object replica\. You can optionally specify a storage class to use for object replicas in the destination bucket\.

There are additional optional configurations that you can specify\. For more information, see [Additional Cross\-Region Replication Configurations](crr-additional-configs.md)\.

Unless you make specific requests in the replication configuration, the object replicas in the destination bucket are exact replicas of the objects in the source bucket\. For example:
+ Replicas have the same key names and the same metadata—for example, creation time, user\-defined metadata, and version ID\.

   
+ Amazon S3 stores object replicas using the same storage class as the source object, unless you explicitly specify a different storage class in the replication configuration\.

   
+ Assuming that the object replica continues to be owned by the source object owner, when Amazon S3 initially replicates objects, it also replicates the corresponding object access control list \(ACL\)\. 

Amazon S3 encrypts all data in transit across AWS Regions using Secure Sockets Layer \(SSL\)\. 

You can replicate objects from a source bucket to only one destination bucket\. After Amazon S3 replicates an object, the object cannot be replicated again\. For example, you might change the destination bucket in an existing replication configuration, but Amazon S3 does not replicate it again\.

## Use\-Case Scenarios<a name="crr-scenario"></a>

You might configure cross\-region replication on a bucket for various reasons, including the following:
+ **Compliance requirements** – Although, by default, Amazon S3 stores your data across multiple geographically distant Availability Zones, compliance requirements might dictate that you store data at even further distances\. Cross\-region replication allows you to replicate data between distant AWS Regions to satisfy these compliance requirements\.

    
+ **Minimize latency** – Your customers are in two geographic locations\. To minimize latency in accessing objects, you can maintain object copies in AWS Regions that are geographically closer to your users\.

   
+ **Operational reasons** – You have compute clusters in two different AWS Regions that analyze the same set of objects\. You might choose to maintain object copies in those Regions\.

   
+ **Maintain object copies under different ownership** – Regardless of who owns the source bucket or the source object, you can direct Amazon S3 to change replica ownership to the AWS account that owns the destination bucket\. You might choose to do this to restrict access to object replicas\. This is also referred to as the *owner override* option of the replication configuration\.

## Requirements<a name="crr-requirements"></a>

Requirements for cross\-region replication:
+ The source and destination buckets must have versioning enabled\. For more information about versioning, see [Using Versioning](Versioning.md)\.

   
+ The source and destination buckets must be in different AWS Regions\. For a list of AWS Regions where you can create a bucket, see [Regions and Endpoints](http://docs.aws.amazon.com/general/latest/gr/rande.html#s3_region) in the *AWS General Reference*\.

   
+ Amazon S3 must have permissions to replicate objects from that source bucket to the destination bucket on your behalf\. 

   

  You can grant these permissions by creating an IAM role\. For more information about IAM roles, see [Create an IAM Role](crr-how-setup.md#replication-iam-role-intro)\.

   
+ If the source bucket owner also owns the object, the bucket owner has full permissions to replicate the object\. If not, the object owner must grant the bucket owner the `READ`  and `READ_ACP`  permissions via the object ACL\. For more information about Amazon S3 actions, see [Specifying Permissions in a Policy](using-with-s3-actions.md)\. For more information about resources and ownership, see [Amazon S3 Resources](access-control-overview.md#access-control-resources-basics)\.

If you are setting replication configuration in a cross\-account scenario, where source and destination buckets are owned by different AWS accounts, the following additional requirements apply:
+ The IAM role must have permissions to replicate objects in the destination bucket\. The destination bucket owner can grant these permissions via a bucket policy\. For an example, see [Walkthrough 2: Configure Cross\-Region Replication Where Source and Destination Buckets Are Owned by Different AWS Accounts](crr-walkthrough-2.md)\.

   
+ In the replication configuration, you can optionally direct Amazon S3 to change the ownership of the object replica to the AWS account that owns the destination bucket\. For related additional requirements, see [Cross\-Region Replication Additional Configuration: Change Replica Owner](crr-change-owner.md)\.

## Related Topics<a name="crr-related-topics"></a>

[What Is and Is Not Replicated](crr-what-is-isnot-replicated.md)

[Setting Up Cross\-Region Replication](crr-how-setup.md)

[Finding the Cross\-Region Replication Status ](crr-status.md)

[Cross\-Region Replication: Additional Considerations](crr-and-other-bucket-configs.md)

[Walkthrough 1: Configure Cross\-Region Replication Where Source and Destination Buckets Are Owned by the Same AWS Account](crr-walkthrough1.md)

[Walkthrough 2: Configure Cross\-Region Replication Where Source and Destination Buckets Are Owned by Different AWS Accounts](crr-walkthrough-2.md)