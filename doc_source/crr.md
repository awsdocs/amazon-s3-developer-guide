# Cross\-Region Replication<a name="crr"></a>

*Cross\-region replication* \(CRR\) enables automatic, asynchronous copying of objects across buckets in different AWS Regions\. Buckets configured for cross\-region replication can be owned by the same AWS account or by different accounts\. 

Cross\-region replication is enabled with a bucket\-level configuration\. You add the replication configuration to your source bucket\. In the minimum configuration, you provide the following:
+ The destination bucket, where you want Amazon S3 to replicate objects 
+ An AWS IAM  role that Amazon S3 can assume to replicate objects on your behalf 

Additional configuration options are available\. 

## When to Use CRR<a name="crr-scenario"></a>

Cross\-region replication can help you do the following:
+ **Comply with compliance requirements**—Although Amazon S3 stores your data across multiple geographically distant Availability Zones by default, compliance requirements might dictate that you store data at even greater distances\. Cross\-region replication allows you to replicate data between distant AWS Regions to satisfy these requirements\.

    
+ **Minimize latency**—If your customers are in two geographic locations, you can minimize latency in accessing objects by maintaining object copies in AWS Regions that are geographically closer to your users\.

   
+ **Increase operational efficiency**—If you have compute clusters in two different AWS Regions that analyze the same set of objects, you might choose to maintain object copies in those Regions\.

   
+ **Maintain object copies under different ownership**—Regardless of who owns the source object you can tell Amazon S3 to change replica ownership to the AWS account that owns the destination bucket\. This is referred to as the *owner override* option\. You might use this option restrict access to object replicas\. 

## Requirements for CRR<a name="crr-requirements"></a>

Cross\-region replication requires the following:
+ The source bucket owner must have the source and destination AWS Regions enabled for their account\. The destination bucket owner must have the destination Region enabled for their account\. For more information about enabling or disabling an AWS Region, see [AWS Regions and Endpoints](https://docs.aws.amazon.com/general/latest/gr/rande.html) in the *AWS General Reference*\.
+ Both source and destination buckets must have versioning enabled\. 
+ The source and destination buckets must be in different AWS Regions\. 
+ Amazon S3 must have permissions to replicate objects from the source bucket to the destination bucket on your behalf\. 
+ If the owner of the source bucket doesn't own the object in the bucket, the object owner must grant the bucket owner `READ` and `READ_ACP` permissions with the object access control list \(ACL\)\. For more information, see [Managing Access with ACLs](S3_ACLs_UsingACLs.md)\. 
+ If the source bucket has Amazon S3 object lock enabled, the destination bucket must also have object lock enabled\. For more information, see [Locking Objects Using Amazon S3 Object Lock](object-lock.md)\.

  To enable cross\-region replication on a bucket that has object lock enabled, contact the AWS Support team at [AWS Support](https://console.aws.amazon.com/support/home)\.

For more information, see [Overview of Setting Up CRR ](crr-how-setup.md)\. 

If you are setting the replication configuration in a *cross\-account scenario*, where source and destination buckets are owned by different AWS accounts, the following additional requirements apply:
+ The owner of the destination bucket must grant the owner of the source bucket permissions to replicate objects with a bucket policy\.  For more information, see [Granting Permissions When Source and Destination Buckets Are Owned by Different AWS Accounts](setting-repl-config-perm-overview.md#setting-repl-config-crossacct)\.

   