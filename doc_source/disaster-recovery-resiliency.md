# Resilience in Amazon S3<a name="disaster-recovery-resiliency"></a>

The AWS global infrastructure is built around Regions and Availability Zones\. AWS Regions provide multiple, physically separated and isolated Availability Zones that are connected with low latency, high throughput, and highly redundant networking\. These Availability Zones offer you an effective way to design and operate applications and databases\. They are more highly available, fault tolerant, and scalable than traditional single data center infrastructures or multi\-data center infrastructures\. If you specifically need to replicate your data over greater geographic distances, you can use [Replication](replication.md), which enables automatic, asynchronous copying of objects across buckets in different AWS Regions\.

Each AWS Region has multiple Availability Zones\. You can deploy your applications across multiple Availability Zones in the same Region for fault tolerance and low latency\. Availability Zones are connected to each other with fast, private fiber\-optic networking, enabling you to easily architect applications that automatically fail over between Availability Zones without interruption\.

For more information about AWS Regions and Availability Zones, see [AWS Global Infrastructure](https://aws.amazon.com/about-aws/global-infrastructure/)\.

In addition to the AWS global infrastructure, Amazon S3 offers several features to help support your data resiliency and backup needs\.

**Lifecycle configuration**  
A lifecycle configuration is a set of rules that define actions that Amazon S3 applies to a group of objects\. With lifecycle configuration rules, you can tell Amazon S3 to transition objects to less expensive storage classes, archive them, or delete them\. For more information, see [Object lifecycle management](object-lifecycle-mgmt.md)\.

**Versioning**  
Versioning is a means of keeping multiple variants of an object in the same bucket\. You can use versioning to preserve, retrieve, and restore every version of every object stored in your Amazon S3 bucket\. With versioning, you can easily recover from both unintended user actions and application failures\. For more information, see [Using versioning](Versioning.md)\.

**S3 Object Lock**  
You can use S3 Object Lock to store objects using a *write once, read many* \(WORM\) model\. Using S3 Object Lock, you can prevent an object from being deleted or overwritten for a fixed amount of time or indefinitely\. S3 Object Lock enables you to meet regulatory requirements that require WORM storage or simply to add an additional layer of protection against object changes and deletion\. For more information, see [Locking objects using S3 Object Lock](object-lock.md)\.

**Storage classes**  
Amazon S3 offers a range of storage classes for the objects that you store\. Two of these storage classes \(STANDARD\_IA and ONEZONE\_IA\) are designed for long\-lived and infrequently accessed data, such as backups\. You can also use the S3 Glacier storage class to archive objects that you don't need to access in real time\. For more information, see [Amazon S3 storage classes](storage-class-intro.md)\.

The following security best practices also address resilience:
+ [Enable versioning](security-best-practices.md#versioning)
+ [Consider Amazon S3 cross-region replication](security-best-practices.md#cross-region)
+ [Identify and audit all your Amazon S3 buckets](security-best-practices.md#audit)

## Encryption of Amazon S3 backups<a name="backup-encryption"></a>

If you are storing backups using Amazon S3, the encryption of your backups depends on the configuration of those buckets\. Amazon S3 provides a way to set the default encryption behavior for an S3 bucket\. You can set default encryption on a bucket so that all objects are encrypted when they are stored in the bucket\. The default encryption supports keys stored in AWS KMS \(SSE\-KMS\)\. For more information, see [ Amazon S3 default encryption for S3 buckets](bucket-encryption.md)\.