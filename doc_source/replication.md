# Replication<a name="replication"></a>

Replication enables automatic, asynchronous copying of objects across Amazon S3 buckets\. Buckets that are configured for object replication can be owned by the same AWS account or by different accounts\. You can copy objects between different AWS Regions or within the same Region\.

To enable object replication, you add a replication configuration to your source bucket\. The minimum configuration must provide the following:
+ The destination bucket where you want Amazon S3 to replicate objects 
+ An AWS Identity and Access Management \(IAM\)  role that Amazon S3 can assume to replicate objects on your behalf 

Additional configuration options are available\. For more information, see [Additional replication configurations](replication-additional-configs.md)\.

## Types of object replication<a name="replication-types"></a>

You can replicate objects between different AWS Regions or within the same AWS Region\.
+ **Cross\-Region replication** \(CRR\) is used to copy objects across Amazon S3 buckets in different AWS Regions\.
+ **Same\-Region replication** \(SRR\) is used to copy objects across Amazon S3 buckets in the same AWS Region\.

## Why use replication<a name="replication-scenario"></a>

Replication can help you do the following:
+ **Replicate objects while retaining metadata** — You can use replication to make copies of your objects that retain all metadata, such as the original object creation time and version IDs\. This capability is important if you need to ensure that your replica is identical to the source object

   
+ **Replicate objects into different storage classes** — You can use replication to directly put objects into S3 Glacier, S3 Glacier Deep Archive, or another storage class in the destination bucket\. You can also replicate your data to the same storage class and use lifecycle policies on the destination bucket to move your objects to a colder storage class as it ages\.

   
+ **Maintain object copies under different ownership** — Regardless of who owns the source object, you can tell Amazon S3 to change replica ownership to the AWS account that owns the destination bucket\. This is referred to as the *owner override* option\. You can use this option to restrict access to object replicas\.

   
+ **Replicate objects within 15 minutes** — You can use S3 Replication Time Control \(S3 RTC\) to replicate your data in the same AWS Region or across different Regions in a predictable time frame\. S3 RTC replicates 99\.99 percent of new objects stored in Amazon S3 within 15 minutes \(backed by a service level agreement\)\. For more information, see [Meet compliance requirements using S3 Replication Time Control \(S3 RTC\)](replication-time-control.md)\.

## When to use CRR<a name="crr-scenario"></a>

Cross\-Region replication can help you do the following:
+ **Meet compliance requirements** — Although Amazon S3 stores your data across multiple geographically distant Availability Zones by default, compliance requirements might dictate that you store data at even greater distances\. Cross\-Region replication allows you to replicate data between distant AWS Regions to satisfy these requirements\.

    
+ **Minimize latency** — If your customers are in two geographic locations, you can minimize latency in accessing objects by maintaining object copies in AWS Regions that are geographically closer to your users\.

   
+ **Increase operational efficiency** — If you have compute clusters in two different AWS Regions that analyze the same set of objects, you might choose to maintain object copies in those Regions\.

## When to use SRR<a name="srr-scenario"></a>

Same\-Region replication can help you do the following:
+ **Aggregate logs into a single bucket** — If you store logs in multiple buckets or across multiple accounts, you can easily replicate logs into a single, in\-Region bucket\. This allows for simpler processing of logs in a single location\.

   
+ **Configure live replication between production and test accounts** — If you or your customers have production and test accounts that use the same data, you can replicate objects between those multiple accounts, while maintaining  object metadata, by implementing SRR rules\.

   
+ **Abide by data sovereignty laws** — You might be required to store multiple copies of your data in separate AWS accounts within a certain Region\. Same\-Region replication can help you automatically replicate critical data when compliance regulations don't allow the data to leave your country\.

## Requirements for replication<a name="replication-requirements"></a>

Replication requires the following:
+ The source bucket owner must have the source and destination AWS Regions enabled for their account\. The destination bucket owner must have the destination Region\-enabled for their account\. For more information about enabling or disabling an AWS Region, see [AWS Service Endpoints](https://docs.aws.amazon.com/general/latest/gr/rande.html) in the *AWS General Reference*\.
+ Both source and destination buckets must have versioning enabled\. 
+ Amazon S3 must have permissions to replicate objects from the source bucket to the destination bucket on your behalf\. 
+ If the owner of the source bucket doesn't own the object in the bucket, the object owner must grant the bucket owner `READ` and `READ_ACP` permissions with the object access control list \(ACL\)\. For more information, see [Managing Access with ACLs](S3_ACLs_UsingACLs.md)\. 
+ If the source bucket has S3 Object Lock enabled, the destination bucket must also have S3 Object Lock enabled\. For more information, see [Locking objects using S3 Object Lock](object-lock.md)\.

  To enable replication on a bucket that has Object Lock enabled, contact [AWS Support](https://console.aws.amazon.com/support/home)\.

For more information, see [Overview of setting up replication](replication-how-setup.md)\. 

If you are setting the replication configuration in a *cross\-account scenario*, where source and destination buckets are owned by different AWS accounts, the following additional requirement applies:
+ The owner of the destination bucket must grant the owner of the source bucket permissions to replicate objects with a bucket policy\. For more information, see [Granting permissions when source and destination buckets are owned by different AWS accounts](setting-repl-config-perm-overview.md#setting-repl-config-crossacct)\.

   