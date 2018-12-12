# Amazon S3 Object Lock Overview<a name="object-lock-overview"></a>

## Retention Modes<a name="object-lock-retention-modes"></a>

Amazon S3 Object Lock provides two retention modes: Governance and Compliance\. These retention modes apply different levels of protection to your objects\. You can apply either retention mode to any object version that is protected by S3 Object Lock\.

### <a name="object-lock-governance-mode"></a>

In **Governance** mode, users can't overwrite or delete an object version or alter its lock settings unless they have special permissions\. Governance mode enables you to protect objects against deletion by most users while still allowing you to grant some users permission to alter the retention settings or delete the object if necessary\. You can also use Governance mode to test retention\-period settings before creating a Compliance\-mode retention period\. In order to override or remove Governance\-mode retention settings, a user must have the `s3:BypassGovernanceMode` permission and must explicitly include `x-amz-bypass-governance-retention:true` as a request header with any request that requires overriding Governance mode\.

### <a name="object-lock-compliance-mode"></a>

In **Compliance** mode, a protected object version can't be overwritten or deleted by any user, including the root user in your AWS account\. Once an object is locked in Compliance mode, its retention mode can't be changed and its retention period can't be shortened\. Compliance mode ensures that an object version can't be overwritten or deleted for the duration of the retention period\.

**Note**  
Updating an object version's metadata, as occurs when you place or alter an object lock, doesn't overwrite the object version or reset its `Last-Modified` timestamp\.

## Retention Periods<a name="object-lock-retention-periods"></a>

A retention period protects an object version for a fixed amount of time\. When you place a retention period on an object version, Amazon S3 stores a timestamp in the object version's metadata to indicate when the retention period expires\. After the retention period expires, the object version can be overwritten or deleted unless you also placed a legal hold on the object version\.

A retention period can be placed on an object version either explicitly or through a bucket default setting\. When you apply a retention period to an object version explicitly, you specify a Retain Until Date for the object version\. Amazon S3 stores the Retain Until Date in the object version's metadata and protects the object version until the retention period expires\.

When you use bucket default settings, you don't specify a Retain Until Date\. Instead, you specify a duration, in either days or years, for which every object version placed in the bucket should be protected\. When you place an object in the bucket, Amazon S3 calculates a Retain Until Date for the object version by adding the specified duration to the object version's creation timestamp and stores the Retain Until Date in the object version's metadata\. The object version is then protected exactly as though you explicitly placed a lock with that retention period on the object version\.

**Note**  
If your request to place an object version in a bucket contains an explicit retention mode and period, those settings override any bucket default settings for that object version\.

Like all other S3 Object Lock settings, retention periods apply to individual object versions\. Different versions of a single object can have different retention modes and periods\.

For example, if you have an object that's 15 days into a 30\-day retention period, and you PUT an object into S3 with the same name and a 60\-day retention period, your PUT will succeed and Amazon S3 will create a new version of the object with a 60\-day retention period\. The older version maintains its original retention period and becomes deletable in 15 days\.

You can extend a retention period after you've applied a retention setting to an object version\. To do this, you submit a new lock request for the object version with a later Retain Until Date than the one currently configured for the object version\. Amazon S3 replaces the existing retention period with the new, longer period\. Any user with permissions to place an object retention period can extend a retention period for an object version locked in either mode\.

## Legal Holds<a name="object-lock-legal-holds"></a>

S3 Object Lock also enables you to place a legal hold on an object version\. Like a retention period, a legal hold prevents an object version from being overwritten or deleted\. However, a legal hold doesn't have an associated retention period and remains in effect until removed\. Legal holds can be freely placed and removed by any user with the `s3:PutObjectLegalHold` permission\.

Legal holds are independent from retention periods\. As long as the bucket that contains the object has S3 Object Lock enabled, you can place and remove legal holds regardless of whether the specified object version has a retention period set\. Placing a legal hold on an object version doesn't affect the retention mode or retention period for that object version\. For example, if you place a legal hold on an object version while the object version is also protected by a retention period, and then the retention period expires, the object doesn't lose its WORM protection\. Rather, the legal hold continues to protect the object until an authorized user explicitly removes it\. Similarly, if you remove a legal hold while an object version has a retention period in effect, the object version will remain protected until the retention period expires\.

## Bucket Configuration<a name="object-lock-bucket-config"></a>

In order to use S3 Object Lock, you first enable Object Lock for a bucket\. You can also optionally configure a default retention mode and period that will apply to new objects placed in the bucket\.

### Enabling Object Lock<a name="object-lock-bucket-config-enable"></a>

Before you lock any objects, you have to configure a bucket to use Amazon S3 Object Lock\. To configure a bucket for S3 Object Lock, you specify when you create the bucket that you want to enable S3 Object Lock\. Once you configure a bucket for S3 Object Lock, you can lock objects in that bucket with retention periods, legal holds, or both\.

**Note**  
You can only enable S3 Object Lock for new buckets\. If you need to turn on S3 Object Lock for an existing bucket, please contact AWS Support\.
When you create a bucket with S3 Object Lock enabled, Amazon S3 automatically enables versioning for the bucket\.
Once you create a bucket with S3 Object Lock enabled, you can't disable Object Lock or suspend versioning for the bucket\.

### Default Retention Settings<a name="object-lock-bucket-config-defaults"></a>

Turning on S3 Object Lock for a bucket enables the bucket to store protected objects, but doesn't automatically protect objects that you put in the bucket\. If you want to automatically protect object versions placed in the bucket, you can configure a default retention period\. Default settings apply to all new objects placed in the bucket unless you explicitly specify a different retention mode and period for an object when you create it\.

**Tip**  
If you want to enforce the bucket default retention mode and period for all new object versions placed in a bucket, you can set the bucket defaults and deny users permission to put object retention settings\. Amazon S3 then applies the default retention mode and period to new object versions placed in the bucket, and rejects any request to put an object that includes a retention mode and setting\.

Bucket default settings require both a mode and a period\. A bucket default mode is either Governance or Compliance, as described in [Retention Modes](#object-lock-retention-modes)\. A default retention period is described not as a timestamp, but as a period either in days or in years\. When you place an object version in a bucket with a default retention period, S3 Object Lock calculates a Retain Until Date by adding the default retention period to the creation timestamp for the object version\. Amazon S3 stores the resulting timestamp as the object version's Retain Until Date, just as though you had calculated the timestamp manually and placed it on the object version yourself\.

Default settings apply only to new objects placed in the bucket\. Placing a default retention setting on a bucket doesn't place any retention settings on objects that already exist in the bucket\.

**Important**  
Object locks apply to individual object versions only\. If you place an object in a bucket that has a default retention period and you don't explicitly specify a retention period for that object, then Amazon S3 creates the object with a retention period that match the bucket default\. After the object is created, its retention period is independent from the bucket's default retention period\. Changing a bucket's default retention period won't alter the existing retention period for any objects in that bucket\.

## Required Permissions<a name="object-lock-permissions"></a>

 S3 Object Lock operations require the permissions listed in the following table\.


**S3 Object Lock Permissions**  

| Operation | Permissions required | 
| --- | --- | 
| Create or modify an object version's retention mode and period | s3:PutObjectRetention | 
| Create or modify a legal hold for an object version | s3:PutObjectLegalHold | 
| Get an object version's retention mode and period | s3:GetObjectRetention | 
| Get an object version's legal hold status | s3:GetObjectLegalHold | 
| Bypass governance retention mode | s3:BypassGovernanceRetention | 
| Get a bucket's Object Lock configuration | s3:GetBucketObjectLockConfiguration | 
| Create or modify a bucket's Object Lock configuration | s3:PutBucketObjectLockConfiguration | 

## Restrictions and Limitations<a name="object-lock-restrictions"></a>

You can't copy from a bucket that has S3 Object Lock enabled using cross\-region replication \(CRR\)\. If you try to set up a CRR rule using a source bucket configured for S3 Object Lock, the request will fail\. You can use a bucket with S3 Object Lock enabled as the destination for a CRR rule, however\. This enables you to apply WORM protection to your replicated objects\. For more information about CRR, see [ Cross\-Region Replication   Set up and configure cross\-region replication to allow automatic, asynchronous copying of objects across Amazon S3 buckets in different AWS Regions\.   *Cross\-region replication* \(CRR\) enables automatic, asynchronous copying of objects across buckets in different AWS Regions\. Buckets configured for cross\-region replication can be owned by the same AWS account or by different accounts\.  Cross\-region replication is enabled with a bucket\-level configuration\. You add the replication configuration to your source bucket\. In the minimum configuration, you provide the following:   The destination bucket, where you want Amazon S3 to replicate objects    An AWS IAM  role that Amazon S3 can assume to replicate objects on your behalf    Additional configuration options are available\.   When to Use CRR  Cross\-region replication can help you do the following:   **Comply with compliance requirements**—Although Amazon S3 stores your data across multiple geographically distant Availability Zones by default, compliance requirements might dictate that you store data at even greater distances\. Cross\-region replication allows you to replicate data between distant AWS Regions to satisfy these requirements\.      **Minimize latency**—If your customers are in two geographic locations, you can minimize latency in accessing objects by maintaining object copies in AWS Regions that are geographically closer to your users\.     **Increase operational efficiency**—If you have compute clusters in two different AWS Regions that analyze the same set of objects, you might choose to maintain object copies in those Regions\.     **Maintain object copies under different ownership**—Regardless of who owns the source object you can tell Amazon S3 to change replica ownership to the AWS account that owns the destination bucket\. This is referred to as the *owner override* option\. You might use this option restrict access to object replicas\.       Requirements for CRR  Cross\-region replication requires the following:   Both source and destination buckets must have versioning enabled\.    The source and destination buckets must be in different AWS Regions\.    Amazon S3 must have permissions to replicate objects from the source bucket to the destination bucket on your behalf\.     If the owner of the source bucket doesn't own the object in the bucket, the object owner must grant the bucket owner `READ` and `READ_ACP` permissions with the object ACL\. For more information, see [Managing Access with ACLs](S3_ACLs_UsingACLs.md)\.    For more information, see [Overview of Setting Up CRR ](crr-how-setup.md)\.  If you are setting the replication configuration in a *cross\-account scenario*, where source and destination buckets are owned by different AWS accounts, the following additional requirements apply:   The owner of the destination bucket must grant the owner of the source bucket permissions to replicate objects with a bucket policy\.  For more information, see [Granting Permissions When Source and Destination Buckets Are Owned by Different AWS Accounts](setting-repl-config-perm-overview.md#setting-repl-config-crossacct)\.        What Does Amazon S3 Replicate?  Describes what is and what is not replicated in Amazon S3 cross\-region replication\.   Amazon S3 replicates only specific items in buckets that are configured for cross\-region replication\.   What Is Replicated?  Amazon S3 replicates the following:   Objects created after you add a replication configuration, with exceptions described in the next section\.     Both unencrypted objects and objects encrypted using Amazon S3 managed keys \(SSE\-S3\) or AWS KMS managed keys \(SSE\-KMS\), although you must explicitly enable the option to replicate objects encrypted using KMS keys\. The replicated copy of the object is encrypted using the same type of server\-side encryption that was used for the source object\. For more information about server\-side encryption, see [Protecting Data Using Server\-Side Encryption](serv-side-encryption.md)\.     Object metadata\.     Only objects in the source bucket for which the bucket owner has permissions to read objects and access control lists \(ACLs\)\. For more information about resource ownership, see [About the Resource Owner](access-control-overview.md#about-resource-owner)\.     Object ACL updates, unless you direct Amazon S3 to change the replica ownership when source and destination buckets aren't owned by the same accounts   \(see [CRR Additional Configuration: Changing the Replica Owner](crr-change-owner.md)\)\.    It can take awhile until Amazon S3 can bring the two ACLs in sync\. This applies only to objects created after you add a replication configuration to the bucket\.      Object tags, if there are any\.    How Delete Operations Affect CRR  If you delete an object from the source bucket, the following occurs:   If you make a DELETE request without specifying an object version ID, Amazon S3 adds a delete marker\. Amazon S3 deals with the delete marker as follows:   If using latest version of the replication configuration, that is you specify the `Filter` element in a replication configuration rule, Amazon S3 does not replicate the delete marker\.   If don't specify the `Filter` element, Amazon S3 assumes replication configuration is a prior version V1\. In the earlier version, Amazon S3 handled replication of delete markers differently\. For more information, see [Backward Compatibility ](crr-add-config.md#crr-backward-compat-considerations)\.      If you specify an object version ID to delete in a DELETE request, Amazon S3 deletes that object version in the source bucket, but it doesn't replicate the deletion in the destination bucket\. In other words, it doesn't delete the same object version from the destination bucket\. This protects data from malicious deletions\.       What Isn't Replicated?  Amazon S3 doesn't replicate the following:    Objects that existed before you added the replication configuration to the bucket\. In other words, Amazon S3 doesn't replicate objects retroactively\.     The following encrypted objects:   Objects created with server\-side encryption using customer\-provided \(SSE\-C\) encryption keys\.   Objects created with server\-side encryption using AWS KMS–managed encryption \(SSE\-KMS\) keys\. By default, Amazon S3 does not replicate objects encrypted using KMS keys\. However, you can explicitly enable replication of these objects in the replication configuration, and provide relevant information so that Amazon S3 can replicate these objects\.     For more information about server\-side encryption, see [Protecting Data Using Server\-Side Encryption](serv-side-encryption.md)\.       Objects in the source bucket that the bucket owner doesn't have permissions for \(when the bucket owner is not the owner of the object\)\. For information about how an object owner can grant permissions to a bucket owner, see [Granting Cross\-Account Permissions to Upload Objects While Ensuring the Bucket Owner Has Full Control](example-bucket-policies.md#example-bucket-policies-use-case-8)\.     Updates to bucket\-level subresources\. For example, if you change the lifecycle configuration or add a notification configuration to your source bucket, these changes are not applied to the destination bucket\. This makes it possible to have different configurations on source and destination buckets\.      Actions performed by lifecycle configuration\.  For example, if lifecycle configuration is enabled only on your source bucket, Amazon S3 creates delete markers for expired objects, but it does not replicate those markers\. If you want the same lifecycle configuration applied to both source and destination buckets, enable the same lifecycle configuration on both\. For more information about lifecycle configuration, see [Object Lifecycle Management](object-lifecycle-mgmt.md)\.  If using the latest version of the replication configuration \(the XML specifies `Filter` as the child of `Rule`\), delete makers created either by a user action or by Amazon S3 as part of the lifecycle action are not replicated\. However, if using earlier version of the replication configuration \(the XML specifies `Prefix` as the child of `Rule`\), delete markers resulting from user actions are replicated\. For more information, see [Backward Compatibility ](crr-add-config.md#crr-backward-compat-considerations)\.    Objects in the source bucket that are replicas that were created by another cross\-region replication\. You can replicate objects from a source bucket to *only one* destination bucket\. After Amazon S3 replicates an object, the object can't be replicated again\. For example, if you change the destination bucket in an existing replication configuration, Amazon S3 won't replicate the object again\. Another example: suppose that you configure cross\-region replication where bucket A is the source and bucket B is the destination\. Now suppose that you add another cross\-region replication configuration where bucket B is the source and bucket C is the destination\. In this case, objects in bucket B that are replicas of objects in bucket A are not replicated to bucket C\.      Related Topics  [Cross\-Region Replication ](crr.md) [Overview of Setting Up CRR ](crr-how-setup.md)  [Cross\-Region Replication Status Information](crr-status.md)    Overview of Setting Up CRR Overview of Setting Up CRR   How to set up cross\-region replication for Amazon S3\.   To enable cross\-region replication \(CRR\), you add a replication configuration to your source bucket\. The configuration tells Amazon S3 to replicate objects as specified\. In the replication configuration, you must provide the following:   The destination bucket—The bucket where you want Amazon S3 to replicate the objects\.     The objects that you want to replicate—You can replicate all of the objects in the source bucket or a subset\. You identify subset by providing a key name prefix, one or more object tags, or both in the configuration\. For example, if you configure cross\-region replication to replicate only objects with the key name prefix `Tax/`, Amazon S3 replicates objects with keys such as `Tax/doc1` or `Tax/doc2`, but not an object with the key `Legal/doc3`\. If you specify both prefix and one or more tags, Amazon S3 replicates only objects having specific key prefix and the tags\.   A replica has the same key names and metadata \(for example, creation time, user\-defined metadata, and version ID\) as the original object\. Amazon S3 encrypts all data in transit across AWS Regions using Secure Sockets Layer \(SSL\)\.  In addition to these minimum requirements, you can choose the following options:    By default, Amazon S3 stores object replicas using the same storage class as the source object\. You can specify a different storage class for the replicas\.     Because it assumes that an object replica continues to be owned by the owner of the source object, when Amazon S3 replicates objects, it also replicates the corresponding object access control list \(ACL\)\. If the source and destination buckets are owned by different AWS accounts, you can configure CRR to change the owner of a replica to the AWS account that owns the destination bucket\.   Additional configuration options are available\. For more information, see [Additional CRR Configurations](crr-additional-configs.md)\.  If you have an object expiration lifecycle policy in your non\-versioned bucket and you want to maintain the same permanent delete behavior when you enable versioning, you must add a noncurrent expiration policy\. The noncurrent expiration lifecycle policy will manage the deletes of the noncurrent object versions in the version\-enabled bucket\. \(A version\-enabled bucket maintains one current and zero or more noncurrent object versions\.\) For more information, see [ How Do I Create a Lifecycle Policy for an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-lifecycle.html) in the *Amazon Simple Storage Service Console User Guide*\.   Amazon S3 provides APIs in support of the cross\-region replication\. For more information, see the following topics in the *Amazon Simple Storage Service API Reference*\.:    [PUT Bucket replication](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTreplication.html)     [GET Bucket replication](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETreplication.html)     [DELETE Bucket replication](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEreplication.html)    Instead of making these API calls directly from your code, you can add a replication configuration to a bucket with the AWS SDK, AWS CLI, or the Amazon S3 console\. It's easiest to use the console\. For examples with step\-by\-step instructions, see [Cross\-Region Replication \(CRR\) Walkthroughs](crr-example-walkthroughs.md)\. If you are new to CRR configuration, we recommend that you read the following overviews before  exploring the examples and optional configurations\. The examples provide step\-by\-step instructions for setting up basic CRR configurations\. For more information, see [Cross\-Region Replication \(CRR\) Walkthroughs](crr-example-walkthroughs.md)\.    Replication Configuration Overview  Amazon S3 stores a replication configuration as XML\. In the replication configuration XML file, you specify an AWS Identity and Access Management \(IAM\) role and one or more rules\.  

```
<ReplicationConfiguration>
    <Role>IAM-role-ARN</Role>
    <Rule>
        ...
    </Rule>
    <Rule>
         ...
    </Rule>
     ...
</ReplicationConfiguration>
``` Amazon S3 can't replicate objects without your permission\. You grant permissions with the IAM role that you specify in the replication configuration\. Amazon S3 assumes the IAM role to replicate objects on your behalf\. You must grant the required permissions to the IAM role first\. For more information about managing permissions, see [Setting Up Permissions for CRR ](setting-repl-config-perm-overview.md)\. You add one rule in replication configuration in the following scenarios:   You want to replicate all objects\.   You want to replicate a subset of objects\. You identify the object subset by adding a filter in the rule\. In the filter, you specify an object key prefix, tags, or a combination of both, to identify the subset of objects that the rule applies to\.    You add multiple rules in a replication configuration if you want to select a different subset of objects\. In each rule, you specify a filter that selects a different subset of objects\. For example, you might choose to replicates objects that have either `tax/` or` document/` key prefixes\. You would add two rules and specify the `tax/` key prefix filter in one rule and the `document/` key prefix in the other\. The following sections provide additional information\.   The Basic Rule Configuration   Each rule must include the rule's status and priority, and indicate whether to replicate delete makers\.    `Status` indicates whether the rule is enabled or disabled\. If a rule is disabled, Amazon S3 doesn't perform the actions specified in the rule\.    `Priority` indicates which rule has priority when multiple rules apply to an object\.    Currently, delete markers aren't replicated, so you must set `DeleteMarkerReplication` to `Disabled`\.   In the destination configuration, you must provide the name of the bucket where you want Amazon S3 to replicate objects\.   The following code shows the minimum requirements for a rule: 

```
...
    <Rule>
        <ID>Rule-1</ID>
        <Status>rule-Enabled-or-Diasbled</Priority>
        <Priority>integer</Status>
        <DeleteMarkerReplication>
           <Status>Disabled</Status>
        </DeleteMarkerReplication>
        <Destination>        
           <Bucket>arn:aws:s3:::bucket-name</Bucket> 
        </Destination>    
    </Rule>
    <Rule>
         ...
    </Rule>
     ...
...
``` You can also specify other configuration options\. For example, you might choose to use a storage class for object replicas that differs from the class for the source object\.    Optional: Specifying a Filter  To choose a subset of objects that the rule applies to, add an optional filter\. You can filter by object key prefix, object tags, or combination of both\. If you filter on both a key prefix and object tags, Amazon S3 combines the filters using a logical AND operator\. In other words, the rule applies to a subset of objects with a specific key prefix and specific tags\.  To specify a rule with a filter based on an object key prefix, use the following code\. You can specify only one prefix\. 

```
<Rule>
    ...
    <Filter>
        <Prefix>key-prefix</Prefix>   
    </Filter>
    ...
</Rule>
...
``` To specify a rule with a filter based on object tags, use the following code\. You can specify one or more object tags\. 

```
<Rule>
    ...
    <Filter>
        <And>
            <Tag>
                <Key>key1</Key>
                <Value>value1</Value>
            </Tag>
            <Tag>
                <Key>key2</Key>
                <Value>value2</Value>
            </Tag>
             ...
        </And>
    </Filter>
    ...
</Rule>
...
``` To specify a rule filter with a combination of a key prefix and object tags, use this code\. You warp these filters in a AND parent element\. Amazon S3 performs logical AND operation to combine these filters\. In other words, the rule applies to a subset of objects with a specific key prefix and specific tags\.  

```
<Rule>
    ...
    <Filter>
        <And>
            <Prefix>key-prefix</Prefix>
            <Tag>
                <Key>key1</Key>
                <Value>value1</Value>
            </Tag>
            <Tag>
                <Key>key2</Key>
                <Value>value2</Value>
            </Tag>
             ...
    </Filter>
    ...
</Rule>
...
```   Additional Destination Configurations  In the destination configuration, you specify the bucket where you want Amazon S3 to replicate objects\. You can configure CRR to replicate objects from one source bucket to one destination bucket\. If you add multiple rules in a replication configuration, all of the rules must identify the same destination bucket\.  

```
...
<Destination>        
    <Bucket>arn:aws:s3:::destination-bucket</Bucket>
</Destination>
...
``` You have the following options you can add in the <Destination> element:   You can specify the storage class for the object replicas\. By default, Amazon S3 uses the storage class of the source object to create object replicas\. For example,  

  ```
  ...
  <Destination>
         <Bucket>arn:aws:s3:::destinationbucket</Bucket>
         <StorageClass>storage-class</StorageClass>
  </Destination>
  ...
  ```   When source and destination buckets aren't owned by the same accounts, you can change the ownership of the replica to the AWS account that owns the destination bucket by adding the `AccessControlTranslation` element: 

  ```
  ...
  <Destination>
     <Bucket>arn:aws:s3:::destinationbucket</Bucket>
     <Account>destination-bucket-owner-account-id</Account>
     <AccessControlTranslation>
         <Owner>Destination</Owner>
     </AccessControlTranslation>
  </Destination>
  ...
  ``` If you don't add this element to the replication configuration, the replicas are owned by same AWS account that owns the source object\. For more information, see [CRR Additional Configuration: Changing the Replica Owner](crr-change-owner.md)\.   Your source bucket might contain objects that were created with server\-side encryption using AWS KMS\-managed keys\. By default, Amazon S3 doesn't replicate these objects\. You can optionally direct Amazon S3 to replicate these objects by first explicitly opting into this feature by adding the SourceSelectionCriteria element and then providing the AWS KMS key \(for the AWS Region of the destination bucket\) to use for encrypting object replicas\.  

  ```
  ...
  <SourceSelectionCriteria>
    <SseKmsEncryptedObjects>
      <Status>Enabled</Status>
    </SseKmsEncryptedObjects>
  </SourceSelectionCriteria>
  <Destination>
    <Bucket>arn:aws:s3:::dest-bucket-name</Bucket>
    <EncryptionConfiguration>
      <ReplicaKmsKeyID>AWS KMS key IDs to use for encrypting object replicas</ReplicaKmsKeyID>
    </EncryptionConfiguration>
  </Destination>
  ...
  ``` For more information, see [CRR Additional Configuration: Replicating Objects Created with Server\-Side Encryption \(SSE\) Using AWS KMS\-Managed Encryption Keys](crr-replication-config-for-kms-objects.md)\.     Example Replication Configurations  To get started, you can add the following example replication configurations to your bucket, as appropriate\.  To add a replication configuration to a bucket, you must have the `iam:PassRole` permission\. This permission allows you to pass the IAM role that grants Amazon S3 replication permissions\. You specify the IAM role by providing the Amazon Resource Name \(ARN\) that is used in the `Role` element in the replication configuration XML\. For more information, see [Granting a User Permissions to Pass a Role to an AWS Service](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_use_passrole.html) in the *IAM User Guide*\.  

**Example 1: Replication Configuration with One Rule**  
The following basic replication configuration specifies one rule\. The rule specifies an IAM role that Amazon S3 can assume and a destination bucket for object replicas\. The rule `Status` indicates that the rule is in effect\.  

```
<?xml version="1.0" encoding="UTF-8"?>
<ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Role>arn:aws:iam::AcctID:role/role-name</Role>
  <Rule>
    <Status>Enabled</Status>

    <Destination><Bucket>arn:aws:s3:::destinationbucket</Bucket></Destination>

  </Rule>
</ReplicationConfiguration>
```
To choose a subset of objects to replicate, you can add a filter\. In the following configuration, the filter specifies an object key prefix\. This rule applies to objects that have the prefix `Tax/` in their key names\.   

```
<?xml version="1.0" encoding="UTF-8"?>
<ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Role>arn:aws:iam::AcctID:role/role-name</Role>
  <Rule>
    <Status>Enabled</Status>
    <Priority>1</Priority>
    <DeleteMarkerReplication>
       <Status>string</Status>
    </DeleteMarkerReplication>

    <Filter>
       <Prefix>Tax/</Prefix>
    </Filter>

    <Destination><Bucket>arn:aws:s3:::destinationbucket</Bucket></Destination>

  </Rule>
</ReplicationConfiguration>
```
If you specify the `Filter` element, you must also include the `Priority` and `DeleteMarkerReplication` elements\. In this example, priority is irrelevant because there is only one rule\.  
In the following configuration, the filter specifies one prefix and two tags\. The rule applies to the subset of objects that have the specified key prefix and tags\. Specifically, it applies to object that have the `Tax/` prefix in their key names and the two specified object tags\. Priority doesn't apply because there is only one rule\.  

```
<?xml version="1.0" encoding="UTF-8"?>
<ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Role>arn:aws:iam::AcctID:role/role-name</Role>
  <Rule>
    <Status>Enabled</Status>
    <Priority>1</Priority>
    <DeleteMarkerReplication>
       <Status>string</Status>
    </DeleteMarkerReplication>

    <Filter>
        <And>
          <Prefix>Tax/</Prefix>
          <Tag>
             <Tag>
                <Key>tagA</Key>
                <Value>valueA</Value>
             </Tag>
          </Tag>
          <Tag>
             <Tag>
                <Key>tagB</Key>
                <Value>valueB</Value>
             </Tag>
          </Tag>
       </And>

    </Filter>

    <Destination><Bucket>arn:aws:s3:::destinationbucket</Bucket></Destination>

  </Rule>
</ReplicationConfiguration>
```
You can specify a storage class for the object replicas as follows:  

```
<?xml version="1.0" encoding="UTF-8"?>

<ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Role>arn:aws:iam::account-id:role/role-name</Role>
  <Rule>
    <Status>Enabled</Status>
    <Destination>
       <Bucket>arn:aws:s3:::destinationbucket</Bucket>
       <StorageClass>storage-class</StorageClass>
    </Destination>
  </Rule>
</ReplicationConfiguration>
```
You can specify any storage class that Amazon S3 supports\. 

**Example 2: Replication Configuration with Two Rules**  

**Example**  
In the following replication configuration:  
+ Each rule filters on a different key prefix so that each rule applies to a distinct subset of objects\. Amazon S3 replicates objects with key names `Tax/doc1.pdf` and `Project/project1.txt`, but it doesn't replicate objects with the key name `PersonalDoc/documentA`\. 
+  Rule priority is irrelevant because the rules apply to two distinct sets of objects\. The next example shows what happens when rule priority is applied\. 
+ The second rule specifies a storage class for object replicas\. Amazon S3 uses the specified storage class for those object replicas\.
+ Both rules specify the same destination bucket\. You can specify only one destination bucket, regardless of how many rules you specify\.

```
<?xml version="1.0" encoding="UTF-8"?>

<ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Role>arn:aws:iam::account-id:role/role-name</Role>
  <Rule>
    <Status>Enabled</Status>
    <Priority>1</Priority>
    <DeleteMarkerReplication>
       <Status>string</Status>
    </DeleteMarkerReplication>
    <Filter>
        <Prefix>Tax</Prefix>
    </Filter>
    <Status>Enabled</Status>
    <Destination>
      <Bucket>arn:aws:s3:::destinationbucket</Bucket>
    </Destination>
     ...
  </Rule>
 <Rule>
    <Status>Enabled</Status>
    <Priority>2</Priority>
    <DeleteMarkerReplication>
       <Status>string</Status>
    </DeleteMarkerReplication>
    <Filter>
        <Prefix>Project</Prefix>
    </Filter>
    <Status>Enabled</Status>
    <Destination>
      <Bucket>arn:aws:s3:::destinationbucket</Bucket>
     <StorageClass>STANDARD_IA</StorageClass>
    </Destination>
     ...
  </Rule>


</ReplicationConfiguration>
``` 

**Example 3: Replication Configuration with Two Rules with Overlapping Prefixes**  <a name="overlap-rule-example"></a>
In this configuration, the two rules specify filters with overlapping key prefixes, `star/` and `starship`\. Both rules apply to objects with the keyname `starship-x`\. In this case, Amazon S3 uses the rule priority to determine which rule to apply\.   

```
<ReplicationConfiguration>

  <Role>arn:aws:iam::AcctID:role/role-name</Role>

  <Rule>
    <Status>Enabled</Status>
    <Priority>1</Priority>
    <DeleteMarkerReplication>
       <Status>string</Status>
    </DeleteMarkerReplication>
    <Filter>
        <Prefix>star</Prefix>
    </Filter>
    <Destination>
      <Bucket>arn:aws:s3:::destinationbucket</Bucket>
    </Destination>
  </Rule>
  <Rule>
    <Status>Enabled</Status>
    <Priority>1</Priority>
    <DeleteMarkerReplication>
       <Status>string</Status>
    </DeleteMarkerReplication>
    <Filter>
        <Prefix>starship</Prefix>
    </Filter>    
    <Destination>
      <Bucket>arn:aws:s3:::destinationbucket</Bucket>
    </Destination>
  </Rule>
</ReplicationConfiguration>
``` 

**Example 4: Example Walkthroughs**  
For example walkthroughs, see [Cross\-Region Replication \(CRR\) Walkthroughs](crr-example-walkthroughs.md)\. For more information about the XML structure of replication configuration, see [PutBucketReplication](https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutBucketReplication.html) in the *Amazon Simple Storage Service API Reference*\.    Backward Compatibility   The latest version of the replication configuration XML is V2\. For backward compatibility, `Amazon S3 ` continues to support the V1 configuration\. If you have used replication configuration XML V1, consider the following issues that affect backward compatibility:   Replication configuration XML V2 includes the `Filter` element for rules\. With the `Filter` element, you can specify object filters based on the object key prefix, tags, or both to scope the objects that the rule applies to\. Replication configuration XML V1 supported filtering based on only the key prefix, in which case you add the `Prefix` directly as a child element of the `Rule` element\. For example, 

  ```
  <?xml version="1.0" encoding="UTF-8"?>
  <ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Role>arn:aws:iam::AcctID:role/role-name</Role>
    <Rule>
      <Status>Enabled</Status>
      <Prefix>key-prefix</Prefix>
      <Destination><Bucket>arn:aws:s3:::destinationbucket</Bucket></Destination>
  
    </Rule>
  </ReplicationConfiguration>
  ``` For backward compatibility, `Amazon S3 ` continues to support the V1 configuration\.    When you delete an object from your source bucket without specifying an object version ID, Amazon S3 adds a delete marker\. If you use V1 of the replication configuration XML, Amazon S3 replicates delete markers that resulted from user actions\. In other words, if the user deleted the object, and not if Amazon S3 deleted it because the object expired as part of lifecycle action\. In V2, Amazon S3 doesn't replicate delete markers and therefore you must set the `DeleteMarkerReplication` element to `Disabled`\.  

  ```
  ...
      <Rule>
          <ID>Rule-1</ID>
          <Status>rule-Enabled-or-Diasbled</Priority>
          <Priority>integer</Status>
          <DeleteMarkerReplication>
             <Status>Disabled</Status>
          </DeleteMarkerReplication>        
          <Destination>        
             <Bucket>arn:aws:s3:::bucket-name</Bucket> 
          </Destination>    
      </Rule>
  ...
  ```      Setting Up Permissions for CRR   When setting up cross\-region replication, you must acquire necessary permissions as follows:   Create an IAM role—Amazon S3 needs permissions to replicate objects on your behalf\. You grant these permissions by creating an IAM role and specify the role in your replication configuration\.   When source and destination buckets aren't owned by the same accounts, the owner of the destination bucket must grant the source bucket owner permissions to store the replicas\.     Creating an IAM Role    By default, all Amazon S3 resources—buckets, objects, and related subresources—are private: Only the resource owner can access the resource\. To read objects from the source bucket and replicate them to the destination bucket, Amazon S3 needs permissions to perform these tasks\. You grant these permissions by creating an IAM role, then specify the role in your replication configuration\.  This section explains the trust policy and minimum required permission policy\. The example walkthroughs provide step\-by\-step instructions to create an IAM role\. For more information, see [Cross\-Region Replication \(CRR\) Walkthroughs](crr-example-walkthroughs.md)\.   A *trust policy*, where you identify Amazon S3 as the service principal who can assume the role:  

  ```
  {
  
     "Version":"2012-10-17",
     "Statement":[
        {
           "Effect":"Allow",
           "Principal":{
              "Service":"s3.amazonaws.com"
           },
           "Action":"sts:AssumeRole"
        }
     ]
  }
  ``` For more information about IAM roles, see [IAM Roles](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html) in the *IAM User Guide*\.   An *access policy*, where you grant the role permissions to perform replication tasks on your behalf\. When Amazon S3 assumes the role, it has the permissions that you specify in this policy\. 

  ```
  {
  
  
     "Version":"2012-10-17",
     "Statement":[
        {
           "Effect":"Allow",
           "Action":[
              "s3:GetReplicationConfiguration",
              "s3:ListBucket"
           ],
           "Resource":[
              "arn:aws:s3:::source-bucket"
           ]
        },
        {
           "Effect":"Allow",
           "Action":[
  
              "s3:GetObjectVersion",
              "s3:GetObjectVersionAcl",
              "s3:GetObjectVersionTagging"
  
           ],
           "Resource":[
              "arn:aws:s3:::source-bucket/*"
           ]
        },
        {
           "Effect":"Allow",
           "Action":[
              "s3:ReplicateObject",
              "s3:ReplicateDelete",
              "s3:ReplicateTags"
           ],
           "Resource":"arn:aws:s3:::destination-bucket/*"
        }
     ]
  }
  ``` The access policy grants permissions for these actions:    `s3:GetReplicationConfiguration` and `s3:ListBucket`—Permissions for these actions on the *source* bucket allow Amazon S3 to retrieve the replication configuration and list bucket content \(the current permissions model requires the `s3:ListBucket` permission for accessing delete markers\)\.   `s3:GetObjectVersion` and `s3:GetObjectVersionAcl`— Permissions for these actions granted on all objects allow Amazon S3 to get a specific object version and access control list \(ACL\) associated with objects\.    `s3:ReplicateObject` and `s3:ReplicateDelete`—Permissions for these actions on objects in the *destination* bucket allow Amazon S3 to replicate objects or delete markers to the destination bucket\. For information about delete markers, see [How Delete Operations Affect CRR](crr-what-is-isnot-replicated.md#crr-delete-op)\.   Permissions for the `s3:ReplicateObject` action on the *destination* bucket also allow replication of object tags, so you don't need to explicitly grant permission for the `s3:ReplicateTags` action\.    `s3:GetObjectVersionTagging`—Permissions for this action on objects in the *source* bucket allow Amazon S3 to read object tags for replication \(see [Object Tagging](object-tagging.md)\)\. If Amazon S3 doesn't have these permissions, it replicates the objects, but not the object tags\.   For a list of Amazon S3 actions, see [Specifying Permissions in a Policy](using-with-s3-actions.md)\.  The AWS account that owns the IAM role must have permissions for the actions that it grants to the IAM role\.  For example, suppose that the source bucket contains objects owned by another AWS account\. The owner of the objects must explicitly grant the AWS account that owns the IAM role the required permissions through the object ACL\. Otherwise, Amazon S3 can't access the objects and cross\-region replication of the objects fails\. For information about ACL permissions, see [Access Control List \(ACL\) Overview](acl-overview.md)\. The permissions described here are related to minimum replication configuration\. If you choose to add optional replication configurations, you will need to grant additional permissions Amazon S3\. For more information, see [Additional CRR Configurations](crr-additional-configs.md)\.       Granting Permissions When Source and Destination Buckets Are Owned by Different AWS Accounts  When source and destination buckets aren't owned by the same accounts, the owner of the destination bucket must also add a bucket policy to grant the owner of the source bucket permissions to perform replication actions, as follows: \.  

```
{
   "Version":"2008-10-17",
   "Id":"PolicyForDestinationBucket",
   "Statement":[
      {
         "Sid":"1",
         "Effect":"Allow",
         "Principal":{
            "AWS":"SourceBucket-AcctID"
         },
         "Action":[
            "s3:ReplicateDelete",
            "s3:ReplicateObject"
         ],
         "Resource":"arn:aws:s3:::destinationbucket/*"
      },
      {
         "Sid":"2",
         "Effect":"Allow",
         "Principal":{
            "AWS":"SourceBucket-AcctID"
         },
         "Action":"s3:List*",
         "Resource":"arn:aws:s3:::destinationbucket"
      }
   ]
}
``` For an example, see [Example 2: Configure CRR When Source and Destination Buckets Are Owned by Different AWS Accounts](crr-walkthrough-2.md)\. If objects in the source bucket are tagged, note the following:   If the source bucket owner grants Amazon S3 permission for the `s3:GetObjectVersionTagging` and `s3:ReplicateTags` actions to replicate object tags \(through the IAM role\), Amazon S3 replicates the tags along with the objects\. For information about the IAM role, see [Creating an IAM Role ](#setting-repl-config-same-acctowner)\.    If the owner of the destination bucket doesn't want to replicate the tags, she or he can add the following statement to the destination bucket policy to explicitly deny permission for the `s3:ReplicateTags` action: 

  ```
  ...
     "Statement":[
        {
           "Effect":"Deny",
           "Principal":{
              "AWS":"arn:aws:iam::SourceBucket-AcctID:root"
           },
           "Action":["s3:ReplicateTags"],
           "Resource":"arn:aws:s3:::destinationbucket/*"
        }
     ]
  ...
  ```    Changing Replica Ownership  When different AWS accounts own the source and destination buckets, you can tell Amazon S3 to change the ownership of the replica to the AWS account that owns the destination bucket\. This is called the *owner override* option\. For more information, see [CRR Additional Configuration: Changing the Replica Owner](crr-change-owner.md)\.        Additional CRR ConfigurationsAdditional CRR Configurations  This section describes additional cross\-region replication configurations\. For information about core replication, see [Overview of Setting Up CRR ](crr-how-setup.md)\.   CRR Additional Configuration: Changing the Replica OwnerCRR Additional Configuration: Changing Replica Owner  In cross\-region replication \(CRR\), the owner of the source object also owns the replica by default\. When source and destination buckets are owned by different AWS accounts, you can add optional configuration settings to change replica ownership to the AWS account that owns the destination bucket\. You might do this, for example, to restrict access to object replicas\. This is referred to as the *owner override* option of the replication configuration\. This section explains only the relevant additional configuration settings\. For information about setting the replication configuration see [Cross\-Region Replication ](crr.md)\.  To configure the owner override, you do the following:   Add the owner override option to the replication configuration to tell Amazon S3 to change replica ownership\.    Grant Amazon S3 permissions to change replica ownership\.    Add permission in the destination bucket policy to allow changing replica ownership\. This allows the owner of the destination bucket to accept the ownership of object replicas\.   The following sections describe how to perform these tasks\. For a working example with step\-by\-step instructions, see [Example 3: Change Replica Owner When Source and Destination Buckets Are Owned by Different AWS Accounts](crr-walkthrough-3.md)\.  Adding the Owner Override Option to the Replication Configuration   Add the owner override option only when the source and destination buckets are owned by different AWS accounts\. Amazon S3 doesn't check if the buckets are owned by same or different accounts\. If you add owner override when both buckets are owned by same AWS account, Amazon S3 applies the owner override\. It grants full permissions to the owner of the destination bucket and doesn't replicate subsequent updates to the source object access control list \(ACL\)\. The replica owner can directly change the ACL associated with a replica with a `PUT ACL` request, but not through replication\.  To specify the owner override option, add the following to the Destination element:    The `AccessControlTranslation` element, which tells Amazon S3 to change replica ownership   The `Account` element, which specifies the AWS account of the destination bucket owner    

```
<ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    ...
    <Destination>
      ...
      <AccessControlTranslation>
           <Owner>Destination</Owner>
       </AccessControlTranslation>
      <Account>destination-bucket-owner-account-id</Account>
    </Destination>
  </Rule>
</ReplicationConfiguration>
``` The following example replication configuration tells Amazon S3 to replicate objects that have the `Tax` key prefix to the destination bucket and change ownership of the replicas\. 

```
<?xml version="1.0" encoding="UTF-8"?>
<ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
   <Role>arn:aws:iam::account-id:role/role-name</Role>
   <Rule>
      <ID>Rule-1</ID>
      <Priority>1</Priority>
      <Status>Enabled</Status>
      <Status>Enabled</Status>
      <DeleteMarkerReplication>
         <Status>Disabled</Status>
      </DeleteMarkerReplication>
      <Filter>
         <Prefix>Tax</Prefix>
      </Filter>
      <Destination>
         <Bucket>arn:aws:s3:::destination-bucket</Bucket>
         <Account>destination-bucket-owner-account-id</Account>
         <AccessControlTranslation>
            <Owner>Destination</Owner>
         </AccessControlTranslation>
      </Destination>
   </Rule>
</ReplicationConfiguration>
```   Granting Amazon S3 Permission to Change Replica Ownership  Grant Amazon S3 permissions to change replica ownership by adding permission for the `s3:ObjectOwnerOverrideToBucketOwner` action in the permission policy associated with the IAM role\. This is the IAM role that you specified in the replication configuration that allows Amazon S3 to assume and replicate objects on your behalf\.  

```
...
{
    "Effect":"Allow",
         "Action":[
       "s3:ObjectOwnerOverrideToBucketOwner"
    ],
    "Resource":"arn:aws:s3:::destination-bucket/*"
}
...
```   Adding Permission in the Destination Bucket Policy to Allow Changing Replica Ownership  The owner of the destination bucket must grant the owner of the source bucket permission to change replica ownership\. The owner of the destination bucket grants the owner of the source bucket permission for the `s3:ObjectOwnerOverrideToBucketOwner` action\. This allows the source bucket owner to accept ownership of the object replicas\. The following example bucket policy statement shows how to do this: 

```
...
{
    "Sid":"1",
    "Effect":"Allow",
    "Principal":{"AWS":"source-bucket-account-id"},
    "Action":["s3:ObjectOwnerOverrideToBucketOwner"],
    "Resource":"arn:aws:s3:::destination-bucket/*"
}
...
```   Additional Considerations  When you configure the ownership override option, the following considerations apply:   By default, the owner of the source object also owns the replica\. Amazon S3 replicates the object version and the ACL associated with it\.   If you add the owner override, Amazon S3 replicates only the object version, not the ACL\. In addition, Amazon S3 doesn't replicate subsequent changes to the source object ACL\. Amazon S3 sets the ACL on the replica that grants full control to the destination bucket owner\.       When you update a replication configuration to enable, or disable,the owner override, the following occur:      If you add the owner override option to the replication configuration   When Amazon S3 replicates an object version, it discards the ACL that is associated with the source object\. Instead, it sets the ACL on the replica, giving full control to the owner of the destination bucket\. It doesn't replicate subsequent changes to the source object ACL\. However, this ACL change doesn't apply to object versions that were replicated before you set the owner override option\. ACL updates on source objects that were replicated before the owner override was set continue to be replicated \(because the object and its replicas continue to have the same owner\)\.     If you remove the owner override option from the replication configuration   Amazon S3 replicates new objects that appear in the source bucket and the associated ACLs to the destination bucket\. For objects that were replicated before you removed the owner override, Amazon S3 doesn't replicate the ACLs because the object ownership change that Amazon S3 made remains in effect\. That is, ACLs put on the object version that were replicated when the owner override was set continue to be not replicated\.        CRR Additional Configuration: Replicating Objects Created with Server\-Side Encryption \(SSE\) Using AWS KMS\-Managed Encryption KeysCRR Additional Configuration: Replicating Encrypted Objects   By default, Amazon S3 doesn't replicate objects that are stored at rest using server\-side encryption with AWS KMS\-managed keys\. This section explains additional configuration you add to direct Amazon S3 to replicate these objects\.   For an example with step\-by\-step instructions, see [Example 4: Replicating Encrypted Objects](crr-walkthrough-4.md)\. For information about creating a replication configuration, see [Cross\-Region Replication ](crr.md)\.    Specifying Additional Information in the Replication Configuration  In the replication configuration, you do the following:   In the Destination configuration, add the AWS KMS key that you want Amazon S3 to use to encrypt object replicas\.    Explicitly opt in by enabling replication of objects encrypted using the AWS KMS keys by adding the SourceSelectionCriteria element\.   

```
<ReplicationConfiguration>
   <Rule>
      ...
      <SourceSelectionCriteria>
         <SseKmsEncryptedObjects>
           <Status>Enabled</Status>
         </SseKmsEncryptedObjects>
      </SourceSelectionCriteria>

      <Destination>
          ...
          <EncryptionConfiguration>
             <ReplicaKmsKeyID>AWS KMS key ID for the AWS region of the destination bucket.</ReplicaKmsKeyID>
          </EncryptionConfiguration>
       </Destination>
      ...
   </Rule>
</ReplicationConfiguration>
```  The AWS KMS key must have been created in the same AWS Region as the destination bucket\.  The AWS KMS key *must* be valid\. The `PUT` Bucket replication API doesn't check the validity of AWS KMS keys\. If you use an invalid key, you will receive the 200 OK status code in response, but replication fails\.  The following example of a cross\-region replication configuration includes the optional configuration elements: 

```
<?xml version="1.0" encoding="UTF-8"?>
<ReplicationConfiguration>
   <Role>arn:aws:iam::account-id:role/role-name</Role>
   <Rule>
      <ID>Rule-1</ID>
      <Priority>1</Priority>
      <Status>Enabled</Status>
      <DeleteMarkerReplication>
         <Status>Disabled</Status>
      </DeleteMarkerReplication>
      <Filter>
         <Prefix>Tax</Prefix>
      </Filter>
      <Destination>
         <Bucket>arn:aws:s3:::destination-bucket</Bucket>
         <EncryptionConfiguration>
            <ReplicaKmsKeyID>The AWS KMS key ID for the AWS region of the destination bucket (S3 uses it to encrypt object replicas).</ReplicaKmsKeyID>
         </EncryptionConfiguration>
      </Destination>
      <SourceSelectionCriteria>
         <SseKmsEncryptedObjects>
            <Status>Enabled</Status>
         </SseKmsEncryptedObjects>
      </SourceSelectionCriteria>
   </Rule>
</ReplicationConfiguration>
``` This replication configuration has one rule\. The rule applies to objects with the `Tax` key prefix\. Amazon S3 uses the AWS KMS key ID to encrypt these object replicas\.   Granting Additional Permissions for the IAM Role   To replicate objects created using server\-side encryption with AWS KMS\-managed keys, grant the following additional permissions to the IAM role you specify in the replication configuration\. You grant these permissions by updating the permission policy associated with the IAM role:   Permission for the `s3:GetObjectVersionForReplication` action for source objects\. Permission for this action allows Amazon S3 to replicate both unencrypted objects and objects created with server\-side encryption using Amazon S3\-managed encryption \(SSE\-S3 \) keys or AWS KMS–managed encryption \(SSE\-KMS\) keys\.  We recommend that you use the `s3:GetObjectVersionForReplication` action instead of the `s3:GetObjectVersion` action because it provides Amazon S3 with only the minimum permissions necessary for cross\-region replication\. In addition, permission for the `s3:GetObjectVersion` action allows replication of unencrypted and SSE\-S3\-encrypted objects, but not of objects created using an AWS KMS\-managed encryption key\.     Permissions for the following AWS KMS actions:   `kms:Decrypt` permissions for the AWS KMS key that was used to encrypt the source object   `kms:Encrypt` permissions for the AWS KMS key used to encrypt the object replica   We recommend that you restrict these permissions to specific buckets and objects using AWS KMS condition keys; as shown in the following example policy statements:  

  ```
  {
      "Action": ["kms:Decrypt"],
      "Effect": "Allow",
      "Condition": {
          "StringLike": {
              "kms:ViaService": "s3.source-bucket-region.amazonaws.com",
              "kms:EncryptionContext:aws:s3:arn": [
                  "arn:aws:s3:::source-bucket-name/key-prefix1*",
              ]
          }
      },
      "Resource": [
          "List of AWS KMS key IDs used to encrypt source objects.", 
      ]
  },
  {
      "Action": ["kms:Encrypt"],
      "Effect": "Allow",
      "Condition": {
          "StringLike": {
              "kms:ViaService": "s3.destination-bucket-region.amazonaws.com",
              "kms:EncryptionContext:aws:s3:arn": [
                  "arn:aws:s3:::destination-bucket-name/key-prefix1*",
              ]
          }
      },
      "Resource": [
           "AWS KMS key IDs (for the AWS region of the destination bucket). S3 uses it to encrypt object replicas", 
      ]
  }
  ``` The AWS account that owns the IAM role must have permissions for these AWS KMS actions \(`kms:Encrypt` and `kms:Decrypt`\) for AWS KMS keys listed in the policy\. If the AWS KMS keys are owned by another AWS account, the key owner must grant these permissions to the AWS account that owns the IAM role\. For more information about managing access to these keys, see [Using IAM Policies with AWS KMS](https://docs.aws.amazon.com/kms/latest/developerguide/control-access-overview.html#overview-policy-elements) in the* AWS Key Management Service Developer Guide*\. The following is a complete IAM policy that grants the necessary permissions to replicate unencrypted objects, objects created with server\-side encryption using Amazon S3\-managed encryption keys, and AWS KMS\-managed encryption keys\.  Objects created with server\-side encryption using customer\-provided \(SSE\-C\) encryption keys are not replicated\.    

```
{
   "Version":"2012-10-17",
   "Statement":[
      {
         "Effect":"Allow",
         "Action":[
            "s3:GetReplicationConfiguration",
            "s3:ListBucket"
         ],
         "Resource":[
            "arn:aws:s3:::source-bucket"
         ]
      },
      {
         "Effect":"Allow",
         "Action":[
            "s3:GetObjectVersionForReplication",
            "s3:GetObjectVersionAcl"
         ],
         "Resource":[
            "arn:aws:s3:::source-bucket/key-prefix1*"
         ]
      },
      {
         "Effect":"Allow",
         "Action":[
            "s3:ReplicateObject",
            "s3:ReplicateDelete"
         ],
         "Resource":"arn:aws:s3:::destination-bucket/key-prefix1*"
      },
      {
         "Action":[
            "kms:Decrypt"
         ],
         "Effect":"Allow",
         "Condition":{
            "StringLike":{
               "kms:ViaService":"s3.source-bucket-region.amazonaws.com",
               "kms:EncryptionContext:aws:s3:arn":[
                  "arn:aws:s3:::source-bucket-name/key-prefix1*"
               ]
            }
         },
         "Resource":[
           "List of AWS KMS key IDs used to encrypt source objects."
         ]
      },
      {
         "Action":[
            "kms:Encrypt"
         ],
         "Effect":"Allow",
         "Condition":{
            "StringLike":{
               "kms:ViaService":"s3.destination-bucket-region.amazonaws.com",
               "kms:EncryptionContext:aws:s3:arn":[
                  "arn:aws:s3:::destination-bucket-name/prefix1*"
               ]
            }
         },
         "Resource":[
            "AWS KMS key IDs (for the AWS region of the destination bucket) to use for encrypting object replicas"
         ]
      }
   ]
}
```   Granting Additional Permissions for Cross\-Account Scenarios  In a cross\-account scenario, where *source* and *destination* buckets are owned by different AWS accounts, the AWS KMS key to encrypt object replicas must be a customer master key \(CMK\)\. The key owner must grant the source bucket owner permission to use the key\.  To grant the source bucket owner permission to use the key \(IAM console\) Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.  Choose **Encryption keys**\.   Choose the AWS KMS key\.   In **Key Policy**, **Key Users**, **External Accounts**, choose **Add External Account**\.    For the **arn:aws:iam::**, enter the source bucket account ID\.   Choose **Save Changes**\.   To grant the source bucket owner permission to use the key \(AWS CLI\)  For information, see [put\-key\-policy](http://docs.aws.amazon.com/cli/latest/reference/kms/put-key-policy.html) in the* AWS CLI Command Reference*\. For information about the underlying API, see [PutKeyPolicy](http://docs.aws.amazon.com/kms/latest/APIReference/API_PutKeyPolicy.html) in the *[AWS Key Management Service API Reference](https://docs.aws.amazon.com/kms/latest/APIReference/)\.*     AWS KMS Transaction Limit Considerations  When you add many new objects with AWS KMS encryption after enabling cross\-region replication \(CRR\), you might experience throttling \(HTTP 503 Slow Down errors\)\. Throttling occurs when the number of KMS transactions per second exceeds the current limit\. For more information, see [Limits]( http://docs.aws.amazon.com/kms/latest/developerguide/limits.html) in the *AWS Key Management Service Developer Guide*\. We recommend that you request an increase in your AWS KMS API rate limit by creating a case in the AWS Support Center\. For more information, see https://console\.aws\.amazon\.com/support/home\#/\.     Cross\-Region Replication \(CRR\) WalkthroughsCRR Walkthroughs  The following examples show how to set up cross\-region replication\.   The following examples show how to configure cross\-region replication \(CRR\) for common use cases\. The examples show setting replication configuration using the Amazon S3 console, AWS Command Line Interface \(CLI\), and AWS SDKs \(example of Java and \.NET SDK are shown\)\. For information about installing and configuring AWS CLI, see the following topics in the AWS Command Line Interface User Guide\.    [Installing the AWS Command Line Interface](https://docs.aws.amazon.com/cli/latest/userguide/installing.html)     [Configuring the AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html) \- You will need to set up at least one profile\. You will need to set two profiles if you are exploring cross\-account scenarios\.   For information about AWS SDK, see [AWS SDK for Java](https://aws.amazon.com/sdk-for-java/) and [AWS SDK for \.NET](https://aws.amazon.com/sdk-for-net/)\.    Example 1: Configure CRR When Source and Destination Buckets Are Owned by the Same AWS AccountCRR Example 1: Same AWS Account  TBD   In this example, you set up cross\-region replication \(CRR\) where source and destination buckets are owned by the same AWS accounts\. Examples are provided for using the Amazon S3 console, the AWS Command Line Interface \(AWS CLI\), and the AWS SDK for Java and AWS SDK for \.NET\.    Configure CRR When Source and Destination Buckets Are Owned by the Same AWS Account \(Console\)  For step\-by\-step instructions, see [How Do I Add a Cross\-Region Replication \(CRR\) Rule to an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-crr.html) in the *Amazon Simple Storage Service Console User Guide*\. This topic provides instructions for setting replication configuration when buckets are owned by same and different AWS accounts\.   Configure CRR When Source and Destination Buckets Are Owned by the Same AWS Account \(AWS CLI\)  To set up CRR when the source and destination buckets are owned by the same AWS account with the AWS CLI, you create source and destination buckets, enable versioning on the buckets, create an IAM role that gives Amazon S3 permission to replicate objects, and add the replication configuration to the source bucket\. To verify your setup, you test it\. To set up CRR replication when source and destination buckets are owned by the same AWS account   Set a credentials profile for the AWS CLI\. In this example, we use the profile name `acctA`\. For information about setting credential profiles, see [Named Profiles](https://docs.aws.amazon.com/cli/latest/userguide/cli-multiple-profiles.html) in the *AWS Command Line Interface User Guide*\.   The profile you use for this exercise must have necessary permissions\. For example, in the replication configuration, you specify the IAM role that Amazon S3 can assume\. You can do this only if the profile you use has the `iam:PassRole` permission\. For more information, see [Granting a User Permissions to Pass a Role to an AWS Service](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_use_passrole.html) in the *IAM User Guide*\. If you use an administrator user credentials to create a named profile then you can perform all the tasks\.     Create a *source* bucket and enable versioning on it\. The following code creates a *source* bucket in the US East \(N\. Virginia\) \(us\-east\-1\) Region\. 

   ```
   aws s3api create-bucket \
   --bucket source \
   --region us-east-1 \
   --profile acctA
   ``` 

   ```
   aws s3api put-bucket-versioning \
   --bucket source \
   --versioning-configuration Status=Enabled \
   --profile acctA
   ```   Create a *destination* bucket and enable versioning on it\. The following code creates a *destination* bucket in the US West \(Oregon\) \(us\-west\-2\) Region\.   To set up replication configuration when both source and destination buckets are in the same AWS account, you use the same profile\. In this example, we use `acctA`\. To test replication configuration when the buckets are owned by different AWS accounts, you specify different profiles for each\. In this example, we use the `acctB` profile for the destination bucket\.  

   ```
   aws s3api create-bucket \
   --bucket destination \
   --region us-west-2 \
   --create-bucket-configuration LocationConstraint=us-west-2 \
   --profile acctA
   ``` 

   ```
   aws s3api put-bucket-versioning \
   --bucket destination \
   --versioning-configuration Status=Enabled \
   --profile acctA
   ```   Create an IAM role\. You specify this role in the replication configuration that you add to the *source* bucket later\. Amazon S3 assumes this role to replicate objects on your behalf\. You create an IAM role in two steps:   Create a role   Attach a permissions policy to the role\.     Create the IAM role\.   Copy the following trust policy and save it to a to a file called `S3-role-trust-policy.json` in the current directory on your local computer\. This policy grants Amazon S3 service principal permissions to assume the role\. 

         ```
         {
            "Version":"2012-10-17",
            "Statement":[
               {
                  "Effect":"Allow",
                  "Principal":{
                     "Service":"s3.amazonaws.com"
                  },
                  "Action":"sts:AssumeRole"
               }
            ]
         }
         ```   Run the following command to create a role: 

         ```
         $ aws iam create-role \
         --role-name crrRole \
         --assume-role-policy-document file://s3-role-trust-policy.json  \
         --profile acctA
         ```     Attach a permissions policy to the role\.   Copy the following permissions policy and save it to a file named `S3-role-permissions-policy.json` in the current directory on your local computer\. This policy grants permissions for various Amazon S3 bucket and object actions\.  

         ```
         {
            "Version":"2012-10-17",
            "Statement":[
               {
                  "Effect":"Allow",
                  "Action":[
                     "s3:GetObjectVersionForReplication",
                     "s3:GetObjectVersionAcl"
                  ],
                  "Resource":[
                     "arn:aws:s3:::source-bucket/*"
                  ]
               },
               {
                  "Effect":"Allow",
                  "Action":[
                     "s3:ListBucket",
                     "s3:GetReplicationConfiguration"
                  ],
                  "Resource":[
                     "arn:aws:s3:::source-bucket"
                  ]
               },
               {
                  "Effect":"Allow",
                  "Action":[
                     "s3:ReplicateObject",
                     "s3:ReplicateDelete",
                     "s3:ReplicateTags",
                     "s3:GetObjectVersionTagging"
         
                  ],
                  "Resource":"arn:aws:s3:::destination-bucket/*"
               }
            ]
         }
         ```   Run the following command to create a policy and attach it to the role: 

         ```
         $ aws iam put-role-policy \
         --role-name crrRole \
         --policy-document file://s3-role-permissions-policy.json \
         --policy-name crrRolePolicy \
         --profile acctA
         ```        Add replication configuration to the *source* bucket\.    Although the Amazon S3 API requires replication configuration as XML, the AWS CLI requires that you specify the replication configuration as JSON\. Save the following JSON in a file called `replication.json` to the local directory on your computer\.  

      ```
      {
        "Role": "IAM-role-ARN",
        "Rules": [
          {
            "Status": "Enabled",
            "Priority": "1",
            "DeleteMarkerReplication": { "Status": "Disabled" },
            "Filter" : { "Prefix": "Tax"},
            "Destination": {
              "Bucket": "arn:aws:s3:::destination-bucket"
            }
          }
        ]
      }
      ```   Update the JSON by providing values for the *destination\-bucket* and *IAM\-role\-ARN*\. Save the changes\.   Run the following command to add the replication configuration to your source bucket\. Be sure to provide *source\-bucket* name\. 

      ```
      $ aws s3api put-bucket-replication \
      --replication-configuration file://replication.json \
      --bucket source \
      --profile acctA
      ```   To retrieve the replication configuration, use the `get-bucket-replication` command:  

   ```
   $ aws s3api get-bucket-replication \
   --bucket source \
   --profile acctA
   ```   Test the setup in the Amazon S3 console:     Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)    In the *source* bucket, create a folder named `Tax`\.    Add sample objects to the `Tax` folder in the *source* bucket\.   The amount of time it takes for Amazon S3 to replicate an object depends on the size of the object\. For information about how to see the status of replication, see [Cross\-Region Replication Status Information](crr-status.md)\.  In the *destination* bucket, verify the following:   That Amazon S3 replicated the objects\.   In object **properties**, that the **Replication Status** is set to `Replica` \(identifying this as a replica object\)\.   In object **properties**, that the permission section shows no permissions\. This means that the replica is still owned by the *source* bucket owner, and the *destination* bucket owner has no permission on the object replica\. You can add optional configuration to tell Amazon S3 to change the replica ownership\. For an example, see [Example 3: Change Replica Owner When Source and Destination Buckets Are Owned by Different AWS Accounts](crr-walkthrough-3.md)\.  

![\[Screen shot of object properties showing the replication status and permissions.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/crr-wt2-10.png)     Update an object's ACL in the *source* bucket and verify that changes appear in the *destination* bucket\. For instructions, see [How Do I Set Permissions on an Object?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/set-object-permissions.html) in the *Amazon Simple Storage Service Console User Guide*\.        Configure CRR When Source and Destination Buckets Are Owned by the Same AWS Account \(AWS SDK\)  Use the following code examples to add a replication configuration to a bucket with the AWS SDK for Java and AWS SDK for \.NET\., respectively\.   Java  The following example adds a replication configuration to a bucket and then retrieves and verifies the configuration\. For instructions on creating and testing a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\.  

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketReplicationConfiguration;
import com.amazonaws.services.s3.model.ReplicationDestinationConfig;
import com.amazonaws.services.s3.model.ReplicationRule;
import com.amazonaws.services.s3.model.ReplicationRuleStatus;
import com.amazonaws.services.s3.model.StorageClass;

public class CrossRegionReplication {

    public static void main(String[] args) throws IOException {
        String clientRegion = "*** Client region ***";
        String accountId = "*** Account ID ***";
        String roleName = "*** Role name ***";
        String sourceBucketName = "*** Source bucket name ***";
        String destBucketName = "*** Destination bucket name ***";
        String prefix = "Tax/";
        
        String roleARN = String.format("arn:aws:iam::%s:role/%s", accountId, roleName);
        String destinationBucketARN = "arn:aws:s3:::" + destBucketName;
   
        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(clientRegion)
                    .build();

            // Create the replication rule.
            List<ReplicationFilterPredicate> andOperands = new ArrayList<ReplicationFilterPredicate>();
            andOperands.add(new ReplicationPrefixPredicate("prefix"));

            
            Map<String, ReplicationRule> replicationRules = new HashMap<String, ReplicationRule>();
            replicationRules.put("ReplicationRule1",
                                 new ReplicationRule()
                                     .withPriority(0)
                                     .withStatus(ReplicationRuleStatus.Enabled)
                                     .withDeleteMarkerReplication(new DeleteMarkerReplication().withStatus(DeleteMarkerReplicationStatus.DISABLED))
                                     .withFilter(new ReplicationFilter().withPredicate(new ReplicationAndOperator(andOperands)))
                                     .withDestinationConfig(new ReplicationDestinationConfig()
                                                                     .withBucketARN(destinationBucketARN)
                                                                     .withStorageClass(StorageClass.Standard)));
            
            // Save the replication rule to the source bucket.
            s3Client.setBucketReplicationConfiguration(sourceBucketName,
                                                       new BucketReplicationConfiguration()
                                                               .withRoleARN(roleARN)
                                                               .withRules(replicationRules));
    
            // Retrieve the replication configuration and verify that the configuration
            // matches the rule we just set.
            BucketReplicationConfiguration replicationConfig = s3Client.getBucketReplicationConfiguration(sourceBucketName);
            ReplicationRule rule = replicationConfig.getRule("ReplicationRule1");
            System.out.println("Retrieved destination bucket ARN: " + rule.getDestinationConfig().getBucketARN());
            System.out.println("Retrieved source-bucket replication rule prefix: " + rule.getPrefix());
            System.out.println("Retrieved source-bucket replication rule status: " + rule.getStatus());
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }
}
```    C\#  The following AWS SDK for \.NET code example adds a replication configuration to a bucket and then retrieves it\. To use this code, provide the names for your buckets and the Amazon Resource Name \(ARN\) for your IAM role\. For instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\. 

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

﻿using Amazon.S3;
using Amazon.S3.Model;
using System;
using System.Threading.Tasks;

namespace Amazon.DocSamples.S3
{
    class CrossRegionReplicationTest
    {
        private const string sourceBucket = "*** source bucket ***";
        // Bucket ARN example - arn:aws:s3:::destinationbucket
        private const string destinationBucketArn = "*** destination bucket ARN ***";
        private const string roleArn = "*** IAM Role ARN ***";
        // Specify your bucket region (an example region is shown).
        private static readonly RegionEndpoint sourceBucketRegion = RegionEndpoint.USWest2;
        private static IAmazonS3 s3Client;
        public static void Main()
        {
            s3Client = new AmazonS3Client(sourceBucketRegion);
            EnableReplicationAsync().Wait();
        }
        static async Task EnableReplicationAsync()
        {
            try
            {
                ReplicationConfiguration replConfig = new ReplicationConfiguration
                {
                    Role = roleArn,
                    Rules =
                        {
                            new ReplicationRule
                            {
                                Prefix = "Tax",
                                Status = ReplicationRuleStatus.Enabled,
                                Destination = new ReplicationDestination
                                {
                                    BucketArn = destinationBucketArn
                                }
                            }
                        }
                };

                PutBucketReplicationRequest putRequest = new PutBucketReplicationRequest
                {
                    BucketName = sourceBucket,
                    Configuration = replConfig
                };

                PutBucketReplicationResponse putResponse = await s3Client.PutBucketReplicationAsync(putRequest);

                // Verify configuration by retrieving it.
                await RetrieveReplicationConfigurationAsync(s3Client);
            }
            catch (AmazonS3Exception e)
            {
                Console.WriteLine("Error encountered on server. Message:'{0}' when writing an object", e.Message);
            }
            catch (Exception e)
            {
                Console.WriteLine("Unknown encountered on server. Message:'{0}' when writing an object", e.Message);
            }
        }
        private static async Task RetrieveReplicationConfigurationAsync(IAmazonS3 client)
        {
            // Retrieve the configuration.
            GetBucketReplicationRequest getRequest = new GetBucketReplicationRequest
            {
                BucketName = sourceBucket
            };
            GetBucketReplicationResponse getResponse = await client.GetBucketReplicationAsync(getRequest);
            // Print.
            Console.WriteLine("Printing replication configuration information...");
            Console.WriteLine("Role ARN: {0}", getResponse.Configuration.Role);
            foreach (var rule in getResponse.Configuration.Rules)
            {
                Console.WriteLine("ID: {0}", rule.Id);
                Console.WriteLine("Prefix: {0}", rule.Prefix);
                Console.WriteLine("Status: {0}", rule.Status);
            }
        }
    }
}
```        Example 2: Configure CRR When Source and Destination Buckets Are Owned by Different AWS AccountsCRR Example 2: Different AWS Accounts  Example of configuring Amazon S3 cross\-region replication \(CRR\) when source and destination buckets are owned by a different AWS accounts\.   Setting up cross\-region replication \(CRR\) when *source* and *destination* buckets are owned by different AWS accounts is similar to setting CRR when both buckets are owned by the same account\. The only difference is that the *destination* bucket owner must grant the *source* bucket owner permission to replicate objects by adding a bucket policy\.  To set up CRR when source and destination buckets are owned by different AWS accounts  In this example, you create *source* and *destination* buckets in two different AWS accounts\. You need to have two credential profiles set for the AWS CLI \(in this example, we use `acctA` and `acctB` for profile names\)\. For more information about setting credential profiles, see [Named Profiles](https://docs.aws.amazon.com/cli/latest/userguide/cli-multiple-profiles.html) in the *AWS Command Line Interface User Guide*\.    Follow the step\-by\-step instructions in [CRR Example 1: Same AWS Account](crr-walkthrough1.md)\.with the following changes:   For all CLI commands related to *source* bucket activities \(for creating the *source* bucket, enabling versioning, and creating the IAM role\), use the `acctA` profile\. Use the `acctB` profile to create the *destination* bucket\.    Make sure that the permissions policy specifies the *source* and *destination* buckets that you created for this example\.     In the AWS console, add the following bucket policy on the *destination* bucket to allow the owner of the *source* bucket to replicate objects\. Be sure to edit the policy by providing the AWS account ID of the *source* bucket owner and the *destination* bucket name\. 

   ```
   {
      "Version":"2008-10-17",
      "Id":"",
      "Statement":[
         {
            "Sid":"Stmt123",
            "Effect":"Allow",
            "Principal":{
               "AWS":"arn:aws:iam::source-bucket-owner-AWS-acct-ID:root"
            },
            "Action":["s3:ReplicateObject", "s3:ReplicateDelete"],
            "Resource":"arn:aws:s3:::destination/*"
         }
      ]
   }
   ```   Choose the bucket and add bucket policy\. For instructions, see [How Do I Add an S3 Bucket Policy?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/add-bucket-policy.html) in the *Amazon Simple Storage Service Console User Guide*\.   Example 3: Change Replica Owner When Source and Destination Buckets Are Owned by Different AWS AccountsCRR Example 3: Change Replica Owner   TBD   When *source* and *destination* buckets in a cross\-region replication \(CRR\) configuration are owned by different AWS account, you can tell Amazon S3 to change replica ownership to the AWS account that owns the *destination* bucket\. This example explains how to use the Amazon S3 console and the AWS CLI to change replica ownership\. For more information, see [CRR Additional Configuration: Changing the Replica Owner](crr-change-owner.md)\.     Change Replica Owner When Source and Destination Buckets Are Owned by Different AWS Accounts \(Console\)  For step\-by\-step instructions, see [Configuring a CRR Rule When the Destination Bucket is in a Different AWS Account](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-crr.html#enable-crr-cross-account-destination) in the *Amazon Simple Storage Service Console User Guide*\.     Change Replica Owner When Source and Destination Buckets Are Owned by Different AWS Accounts \(AWS CLI\)  To change replica ownership using the AWS CLI, you create buckets, enable versioning on the buckets, create an IAM role that gives Amazon S3 permission to replicate objects, and add the replication configuration to the source bucket\. In the replication configuration you direct Amazon S3 to change replica owner\. You also test the setup\. To change replica ownership when source and destination buckets are owned by different AWS accounts \(AWS CLI\)  In this example, you create the *source* and *destination* buckets in two different AWS accounts\. Configure AWS CLI with two named profiles\. In this example, we use profiles named `acctA` and `acctB`, respectively\. For more information about setting credential profiles, see [Named Profiles](https://docs.aws.amazon.com/cli/latest/userguide/cli-multiple-profiles.html) in the *AWS Command Line Interface User Guide\.*  The profiles you use for this exercise must have necessary permissions\. For example, in the replication configuration, you specify the IAM role that Amazon S3 can assume\. You can do this only if the profile you use has the `iam:PassRole` permission\. If you use an administrator user credentials to create a named profile then you can perform all the tasks\. For more information, see [Granting a User Permissions to Pass a Role to an AWS Service](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_use_passrole.html) in the *IAM User Guide*\.   You will need to make sure these profiles have necessary permissions\. For example, the replication configuration includes an IAM that Amazon S3 can assume\. The named profile you use to attach such configuration to a bucket can do so only if it has the `iam:PassRole` permission\. If you specify administrator user credentials when creating these named profiles, then they all the permissions\. For more information, see [Granting a User Permissions to Pass a Role to an AWS Service](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_use_passrole.html) in the *IAM User Guide*\.    Create the *source* bucket and enable versioning\. In this example, we create the *source* bucket in the US East \(N\. Virginia\) \(us\-east\-1\) Region\. 

   ```
   aws s3api create-bucket \
   --bucket source \
   --region us-east-1 \
   --profile acctA
   ``` 

   ```
   aws s3api put-bucket-versioning \
   --bucket source \
   --versioning-configuration Status=Enabled \
   --profile acctA
   ```   Create a *destination* bucket and enable versioning\. In this example, we create the *destination* bucket in the US West \(Oregon\) \(us\-west\-2\) Region\. Use an AWS account profile different from the one you used for the *source* bucket\. 

   ```
   aws s3api create-bucket \
   --bucket destination \
   --region us-west-2 \
   --create-bucket-configuration LocationConstraint=us-west-2 \
   --profile acctB
   ``` 

   ```
   aws s3api put-bucket-versioning \
   --bucket destination \
   --versioning-configuration Status=Enabled \
   --profile acctB
   ```   Create an IAM role\. You specify this role in the replication configuration that you add to the *source* bucket later\. Amazon S3 assumes this role to replicate objects on your behalf\. You create an IAM role in two steps:   Create a role   Attach a permissions policy to the role     Create an IAM role\.   Copy the following trust policy and save it to a file called `S3-role-trust-policy.json` in the current directory on your local computer\. This policy grants Amazon S3 permissions to assume the role\. 

         ```
         {
            "Version":"2012-10-17",
            "Statement":[
               {
                  "Effect":"Allow",
                  "Principal":{
                     "Service":"s3.amazonaws.com"
                  },
                  "Action":"sts:AssumeRole"
               }
            ]
         }
         ```   Run the following AWS CLI command to create a role: 

         ```
         $ aws iam create-role \
         --role-name crrRole \
         --assume-role-policy-document file://s3-role-trust-policy.json  \
         --profile acctA
         ```     Attach a permission policy to the role\.   Copy the following permissions policy and save it to a file named `s3-role-perm-pol-changeowner.json` in the current directory on your local computer\. This policy grants permissions for various Amazon S3 bucket and object actions\. In the following steps, you create an IAM role and attach this policy to the role\.  

         ```
         {
            "Version":"2012-10-17",
            "Statement":[
               {
                  "Effect":"Allow",
                  "Action":[
                     "s3:GetObjectVersionForReplication",
                     "s3:GetObjectVersionAcl"
                  ],
                  "Resource":[
                     "arn:aws:s3:::source/*"
                  ]
               },
               {
                  "Effect":"Allow",
                  "Action":[
                     "s3:ListBucket",
                     "s3:GetReplicationConfiguration"
                  ],
                  "Resource":[
                     "arn:aws:s3:::source"
                  ]
               },
               {
                  "Effect":"Allow",
                  "Action":[
                     "s3:ReplicateObject",
                     "s3:ReplicateDelete",
                     "s3:ObjectOwnerOverrideToBucketOwner",
                     "s3:ReplicateTags",
                     "s3:GetObjectVersionTagging"
                  ],
                  "Resource":"arn:aws:s3:::destination/*"
               }
            ]
         }
         ```   To create a policy and attach it to the role, run the following command: 

         ```
         $ aws iam put-role-policy \
         --role-name crrRole \
         --policy-document file://s3-role-perm-pol-changeowner.json \
         --policy-name crrRolechangeownerPolicy \
         --profile acctA
         ```        Add replication configuration to your source bucket\.     The AWS CLI requires specifying the replication configuration as JSON\. Save the following JSON in a file called `replication.json` in the current directory on your local computer\. In the configuration, the addition of `AccessControlTranslation` to indicate change in replica ownership\. 

      ```
      {
         "Role":"IAM-role-ARN",
         "Rules":[
            {
               "Status":"Enabled",
               "Priority":"1",
               "DeleteMarkerReplication":{
                  "Status":"Disabled"
               },
               "Filter":{
                  "Prefix":"Tax"
               },
               "Status":"Enabled",
               "Destination":{
                  "Bucket":"arn:aws:s3:::destination",
                  "Account":"destination-bucket-owner-account-id",
                  "AccessControlTranslation":{
                     "Owner":"Destination"
                  }
               }
            }
         ]
      }
      ```   Edit the JSON by providing values for the *destination* bucket owner account ID and *IAM\-role\-ARN*\. Save the changes\.   To add the replication configuration to the source bucket, run the following command\. Provide the *source* bucket name\. 

      ```
      $ aws s3api put-bucket-replication \
      --replication-configuration file://replication-changeowner.json \
      --bucket source \
      --profile acctA
      ```     Check replica ownership in the Amazon S3 console\.   Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.    In the *source* bucket, create a folder named `Tax`\.    Add objects to the folder in the *source* bucket\. Verify that the *destination* bucket contains the object replicas and that the ownership of the replicas has changed to the AWS account that owns the *destination* bucket\.       Change Replica Owner When Source and Destination Buckets Are Owned by Different AWS Accounts \(AWS SDK\)   For code example to add replication configuration, see [Configure CRR When Source and Destination Buckets Are Owned by the Same AWS Account \(AWS SDK\)](crr-walkthrough1.md#crr-ex1-sdk)\. You will need to modify replication configuration appropriately\. For conceptual information, see [CRR Additional Configuration: Changing the Replica Owner](crr-change-owner.md)\.      Example 4: Replicating Encrypted ObjectsCRR Example 4: Replicating Encrypted Objects  TBD   By default, Amazon S3 doesn't replicate objects that are stored at rest using server\-side encryption with AWS KMS\-managed keys\. To replicate encrypted objects, you modify the bucket replication configuration to tell Amazon S3 to replicate these objects\. This example explains how to use the Amazon S3 console and the AWS Command Line Interface \(AWS CLI\) to change the bucket replication configuration to enable replicating encrypted objects\. For more information, see [CRR Additional Configuration: Replicating Objects Created with Server\-Side Encryption \(SSE\) Using AWS KMS\-Managed Encryption Keys](crr-replication-config-for-kms-objects.md)\.     Replicating Encrypted Objects \(Console\)  For step\-by\-step instructions, see [How Do I Add a Cross\-Region Replication \(CRR\) Rule to an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-crr.html) in the *Amazon Simple Storage Service Console User Guide*\. This topic provides instructions for setting replication configuration when buckets are owned by same and different AWS accounts\.   Replicating Encrypted Objects \(AWS CLI\)  To replicate encrypted objects with the AWS CLI, you create buckets, enable versioning on the buckets, create an IAM role that gives Amazon S3 permission to replicate objects, and add the replication configuration to the source bucket\. The replication configuration provides information related to replicating objects encrypted using KMS keys\. The IAM role permission include necessary permissions to replicate the encrypted objects\. You also test the setup\. To replicate encrypted objects \(AWS CLI\)  In this example, we create both the *source* and *destination* buckets in the same AWS account\. Set a credentials profile for the AWS CLI\. In this example, we use the profile name `acctA`\. For more information about setting credential profiles, see [Named Profiles](https://docs.aws.amazon.com/cli/latest/userguide/cli-multiple-profiles.html) in the AWS Command Line Interface User Guide\.    Create the *source* bucket and enable versioning on it\. In this example, we create the *source* bucket in the US East \(N\. Virginia\) \(us\-east\-1\) Region\. 

   ```
   aws s3api create-bucket \
   --bucket source \
   --region us-east-1 \
   --profile acctA
   ``` 

   ```
   aws s3api put-bucket-versioning \
   --bucket source \
   --versioning-configuration Status=Enabled \
   --profile acctA
   ```   Create the *destination* bucket and enable versioning on it\. In this example, we create the *destination* bucket in the US West \(Oregon\) \(us\-west\-2\) Region\.   To set up replication configuration when both *source* and *destination* buckets are in the same AWS account, you use the same profile\. In this example, we use `acctA`\. To test replication configuration when the buckets are owned by different AWS accounts, you specify different profiles for each\. In this example, we use the `acctB` profile for the *destination* bucket\.  

   ```
   aws s3api create-bucket \
   --bucket destination \
   --region us-west-2 \
   --create-bucket-configuration LocationConstraint=us-west-2 \
   --profile acctA
   ``` 

   ```
   aws s3api put-bucket-versioning \
   --bucket destination \
   --versioning-configuration Status=Enabled \
   --profile acctA
   ```   Create an IAM role\. You specify this role in the replication configuration that you add to the *source* bucket later\. Amazon S3 assumes this role to replicate objects on your behalf\. You create an IAM role in two steps:   Create a role   Attach a permissions policy to the role     Create an IAM role\.   Copy the following trust policy and save it to a file called `s3-role-trust-policy-kmsobj.json` in the current directory on your local computer\. This policy grants Amazon S3 service principal permissions to assume the role so Amazon S3 can perform tasks on your behalf\. 

         ```
         {
            "Version":"2012-10-17",
            "Statement":[
               {
                  "Effect":"Allow",
                  "Principal":{
                     "Service":"s3.amazonaws.com"
                  },
                  "Action":"sts:AssumeRole"
               }
            ]
         }
         ```   Create a role: 

         ```
         $ aws iam create-role \
         --role-name crrRolekmsobj \
         --assume-role-policy-document file://s3-role-trust-policy-kmsobj.json  \
         --profile acctA
         ```     Attach a permissions policy to the role\. This policy grants permissions for various Amazon S3 bucket and object actions\.    Copy the following permissions policy and save it to a file named `s3-role-permissions-policykmsobj.json` in the current directory on your local computer\. You create an IAM role and attach the policy to it later\.  

         ```
         {
            "Version":"2012-10-17",
            "Statement":[
               {
                  "Action":[
                     "s3:ListBucket",
                     "s3:GetReplicationConfiguration",
                     "s3:GetObjectVersionForReplication",
                     "s3:GetObjectVersionAcl"
                  ],
                  "Effect":"Allow",
                  "Resource":[
                     "arn:aws:s3:::source",
                     "arn:aws:s3:::source/*"
                  ]
               },
               {
                  "Action":[
                     "s3:ReplicateObject",
                     "s3:ReplicateDelete",
                     "s3:ReplicateTags",
                     "s3:GetObjectVersionTagging"
                  ],
                  "Effect":"Allow",
                  "Condition":{
                     "StringLikeIfExists":{
                        "s3:x-amz-server-side-encryption":[
                           "aws:kms",
                           "AES256"
                        ],
                        "s3:x-amz-server-side-encryption-aws-kms-key-id":[
                           "AWS KMS key IDs to use for encrypting object replicas"  
                        ]
                     }
                  },
                  "Resource":"arn:aws:s3:::destination/*"
               },
               {
                  "Action":[
                     "kms:Decrypt"
                  ],
                  "Effect":"Allow",
                  "Condition":{
                     "StringLike":{
                        "kms:ViaService":"s3.us-east-1.amazonaws.com",
                        "kms:EncryptionContext:aws:s3:arn":[
                           "arn:aws:s3:::source/*"
                        ]
                     }
                  },
                  "Resource":[
                     "AWS KMS key IDs used to encrypt source objects." 
                  ]
               },
               {
                  "Action":[
                     "kms:Encrypt"
                  ],
                  "Effect":"Allow",
                  "Condition":{
                     "StringLike":{
                        "kms:ViaService":"s3.us-west-2.amazonaws.com",
                        "kms:EncryptionContext:aws:s3:arn":[
                           "arn:aws:s3:::destination/*"
                        ]
                     }
                  },
                  "Resource":[
                     "AWS KMS key IDs to use for encrypting object replicas" 
                  ]
               }
            ]
         }
         ```   Create a policy and attach it to the role: 

         ```
         $ aws iam put-role-policy \
         --role-name crrRolekmsobj \
         --policy-document file://s3-role-permissions-policykmsobj.json \
         --policy-name crrRolechangeownerPolicy \
         --profile acctA
         ```        Add the following replication configuration to the *source* bucket\. It tells Amazon S3 to replicate objects with the `Tax/` prefix to the *destination* bucket\.   In the replication configuration you specify the IAM role that Amazon S3 can assume\. You can do this only if you have the `iam:PassRole` permission\. The profile you specify in the CLI command must have the permission\. For more information, see [Granting a User Permissions to Pass a Role to an AWS Service](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_use_passrole.html) in the *IAM User Guide*\.  

   ```
    <ReplicationConfiguration>
     <Role>IAM-Role-ARN</Role>
     <Rule>
       <Status>Enabled</Status>
       <Priority>1</Priority>
       <DeleteMarkerReplication>
          <Status>Disabled</Status>
       </DeleteMarkerReplication>
       <Filter>
          <Prefix>Tax</Prefix>
       </Filter>
       <Status>Enabled</Status>
       <SourceSelectionCriteria>
         <SseKmsEncryptedObjects>
           <Status>Enabled</Status>
         </SseKmsEncryptedObjects>
       </SourceSelectionCriteria>
       <Destination>
         <Bucket>arn:aws:s3:::dest-bucket-name</Bucket>
         <EncryptionConfiguration>
           <ReplicaKmsKeyID>AWS KMS key IDs to use for encrypting object replicas</ReplicaKmsKeyID>
         </EncryptionConfiguration>
       </Destination>
     </Rule>
   </ReplicationConfiguration>
   ``` To add replication configuration to the *source* bucket, do the following:   The AWS CLI requires you to specify the replication configuration as JSON\. Save the following JSON in a file \(`replication.json`\) in the current directory on your local computer\.  

      ```
      {
         "Role":"IAM-Role-ARN",
         "Rules":[
            {
               "Status":"Enabled",
               "Priority":"1",
               "DeleteMarkerReplication":{
                  "Status":"Disabled"
               },
               "Filter":{
                  "Prefix":"Tax"
               },
               "Destination":{
                  "Bucket":"arn:aws:s3:::destination",
                  "EncryptionConfiguration":{
                     "ReplicaKmsKeyID":"AWS KMS key IDs to use for encrypting object replicas"
                  }
               },
               "SourceSelectionCriteria":{
                  "SseKmsEncryptedObjects":{
                     "Status":"Enabled"
                  }
               },
               "Status":"Enabled"
            }
         ]
      }
      ```   Edit the JSON to provide values for the *destination* bucket and *IAM\-role\-ARN*\. Save the changes\.   Add the replication configuration to your *source* bucket\. Be sure to provide the *source* bucket name\. 

      ```
      $ aws s3api put-bucket-replication \
      --replication-configuration file://replicationkmsobj.json \
      --bucket source \
      --profile acctA
      ```     Test the setup to verify that encrypted objects are replicated\. In the Amazon S3 console:   Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.   In the *source* bucket, create a folder named `Tax`\.    Add sample objects to the folder\. Be sure to choose the encryption option and specify your KMS key to encrypt the objects\.    Verify that the *destination* bucket contains the object replicas and that they are encrypted using the KMS encryption key that you specified in the configuration\.       Replicating Encrypted Objects \(AWS SDK\)   For code example to add replication configuration, see [Configure CRR When Source and Destination Buckets Are Owned by the Same AWS Account \(AWS SDK\)](crr-walkthrough1.md#crr-ex1-sdk)\. You will need to modify replication configuration appropriately\. For conceptual information, see [CRR Additional Configuration: Replicating Objects Created with Server\-Side Encryption \(SSE\) Using AWS KMS\-Managed Encryption Keys](crr-replication-config-for-kms-objects.md)\.       Cross\-Region Replication Status InformationCRR: Status Information  How to find the cross\-region replication status of an Amazon S3 object\.   To get the cross\-region replication \(CRR\) status of the objects in a bucket, use the Amazon S3 inventory tool\. Amazon S3 sends a \.csv file to the destination bucket that you specify in the inventory configuration\. You can also use Amazon Athena to query replication status in the inventory report\. For more information about Amazon S3 inventory, see [ Amazon S3 Inventory](storage-inventory.md)\. In CRR, you have a source bucket on which you configure replication and a destination bucket where Amazon S3 replicates objects\. When you request an object \(using `GET` object\) or object metadata \(using `HEAD` object\) from these buckets, Amazon S3 returns the `x-amz-replication-status` header in the response:    When you request an object from the source bucket, Amazon S3 returns the `x-amz-replication-status` header if the object in your request is eligible for replication\.  For example, suppose that you specify the object prefix `TaxDocs` in your replication configuration to tell Amazon S3 to replicate only objects with the key name prefix `TaxDocs`\. Any objects that you upload that have this key name prefix—for example, `TaxDocs/document1.pdf`—will be replicated\. For object requests with this key name prefix, Amazon S3 returns the `x-amz-replication-status` header with one of the following values for the object's replication status: `PENDING`, `COMPLETED`, or `FAILED`\.  If object replication fails after you upload an object, you can't retry replication\. You must upload the object again\.     When you request an object from the destination bucket, if the object in your request is a replica that Amazon S3 created, Amazon S3 returns the `x-amz-replication-status` header with the value `REPLICA`\.   You can find the object replication status in the console, with the AWS Command Line Interface \(AWS CLI\), or with the AWS SDK\.    Console: Choose the object,then choose **Properties** to view object properties, including replication status\.    AWS CLI: Use the `head-object` AWS CLI command to retrieve object metadata: 

  ```
  aws s3api head-object --bucket source-bucket --key object-key --version-id object-version-id           
  ``` The command returns object metadata, including the `ReplicationStatus` as shown in the following example response: 

  ```
  {
     "AcceptRanges":"bytes",
     "ContentType":"image/jpeg",
     "LastModified":"Mon, 23 Mar 2015 21:02:29 GMT",
     "ContentLength":3191,
     "ReplicationStatus":"COMPLETED",
     "VersionId":"jfnW.HIMOfYiD_9rGbSkmroXsFj3fqZ.",
     "ETag":"\"6805f2cfc46c0f04559748bb039d69ae\"",
     "Metadata":{
  
     }
  }
  ```   AWS SDKs: The following code fragments get replication status with the AWS SDK for Java and AWS SDK for \.NET, respectively\.    AWS SDK for Java 

    ```
    GetObjectMetadataRequest metadataRequest = new GetObjectMetadataRequest(bucketName, key);
    ObjectMetadata metadata = s3Client.getObjectMetadata(metadataRequest);
    
    System.out.println("Replication Status : " + metadata.getRawMetadataValue(Headers.OBJECT_REPLICATION_STATUS));
    ```   AWS SDK for \.NET 

    ```
    GetObjectMetadataRequest getmetadataRequest = new GetObjectMetadataRequest
        {
             BucketName = sourceBucket,
             Key        = objectKey
        };
    
    GetObjectMetadataResponse getmetadataResponse = client.GetObjectMetadata(getmetadataRequest);
    Console.WriteLine("Object replication status: {0}", getmetadataResponse.ReplicationStatus);
    ```      Before deleting an object from a source bucket that has replication enabled, check the object's replication status to ensure that the object has been replicated\.  If lifecycle configuration is enabled on the source bucket, Amazon S3 puts suspends lifecycle actions until it marks the objects status as either `COMPLETED` or `FAILED`\.   Related Topics  [Cross\-Region Replication ](crr.md)    Troubleshooting Cross\-Region Replication CRR: Troubleshooting  How to troubleshoot problems with cross\-region replication for Amazon S3\.   If object replicas don't appear in the destination bucket after you configure cross\-region replication, use these troubleshooting tips to identify and fix issues\.   The time it takes Amazon S3 to replicate an object depends on the size of the object\. For large objects, replication can take up to several hours\. If the object that is being replicated is large, check later to see if it appears in the destination bucket\. You can also check the source object replication status\. If object replication status is pending, then you know Amazon S3 has not completed the replication\. If object replication status is failed, you should check the replication configuration set on the source bucket\.   In the replication configuration on the source bucket, verify the following:   The Amazon Resource Name \(ARN\) of the destination bucket is correct\.   The key name prefix is correct\. For example, if you set the configuration to replicate objects with the prefix `Tax`, then only objects with key names such as `Tax/document1` or `Tax/document2` are replicated\. An object with the key name `document3` is not replicated\.   The status is `enabled`\.     If the destination bucket is owned by another AWS account, verify that the bucket owner has a bucket policy on the destination bucket that allows the source bucket owner to replicate objects\. For an example, see [Example 2: Configure CRR When Source and Destination Buckets Are Owned by Different AWS Accounts](crr-walkthrough-2.md)   If an object replica doesn't appear in the destination bucket, the following might have prevented replication:   Amazon S3 doesn't replicate an object in a source bucket that is a replica created by another replication configuration, \. For example, if you set replication configuration from bucket A to bucket B to bucket C, Amazon S3 doesn't replicate object replicas in bucket B to bucket C\.   A source bucket owner can grant other AWS accounts permission to upload objects\. By default, the source bucket owner doesn't have permissions for the objects created by other accounts\. The replication configuration replicates only the objects for which the source bucket owner has access permissions\. The source bucket owner can grant other AWS accounts permissions to create objects conditionally, requiring explicit access permissions on those objects\. For an example policy, see [Granting Cross\-Account Permissions to Upload Objects While Ensuring the Bucket Owner Has Full Control](example-bucket-policies.md#example-bucket-policies-use-case-8)\.     Suppose in the replication configuration you add a rule to replicate a subset of objects having a specific tag\. In this case, you must assign the specific tag key and value at the time of creating the object for Amazon S3 to replicate the object\. If you first create an object and then add the tag to the existing object, Amazon S3 will not replicate the object\.    Related Topics  [Cross\-Region Replication ](crr.md)    Cross\-Region Replication Additional ConsiderationsCRR: Additional Considerations  Using cross\-region replication for Amazon S3 buckets with different bucket configurations\.   Amazon S3 also supports bucket configurations for the following:   Versioning\. For more information, see [Using Versioning](Versioning.md)\.   Website hosting\. For more information, see [Hosting a Static Website on Amazon S3](WebsiteHosting.md)\.   Bucket access through a bucket policy or access control list \(ACL\)\. For more information, see [Using Bucket Policies and User Policies](using-iam-policies.md) and see [Managing Access with ACLs](S3_ACLs_UsingACLs.md)\.   Log storage\. For more information, [Amazon S3 Server Access Logging](ServerLogs.md)\.   Lifecycle management for objects in a bucket\. For more information, see [Object Lifecycle Management](object-lifecycle-mgmt.md)\.   This topic explains how bucket replication configuration affects the behavior of these bucket configurations\.  Lifecycle Configuration and Object Replicas  The time it takes for Amazon S3 to replicate an object depends on the size of the object\. For large objects, it can take several hours\. Although it might take a while before a replica is available in the destination bucket, it takes the same amount of time to create the replica as it took to create the corresponding object in the source bucket\. If a lifecycle policy is enabled on the destination bucket, the lifecycle rules honor the original creation time of the object, not when the replica became available in the destination bucket\.  If you have an object Expiration lifecycle policy in a non\-versioned bucket, and you want to maintain the same permanent delete behavior when you enable versioning, you must add a noncurrent expiration policy to manage the deletions of the noncurrent object versions in the version\-enabled bucket\. Replication configuration requires the bucket to be versioning\-enabled\. When you enable versioning on a bucket, keep the following in mind:   If you have an object Expiration lifecycle policy, after you enable versioning, add a `NonCurrentVersionExpiration` policy to maintain the same permanent delete behavior as before you enabled versioning\.   If you have a Transition lifecycle policy, after you enable versioning, consider adding a `NonCurrentVersionTransition` policy\.     Versioning Configuration and Replication Configuration  Both the source and destination buckets must be versioning\-enabled when you configure replication on a bucket\. After you enable versioning on both the source and destination buckets and configure replication on the source bucket, you will encounter the following issues:   If you attempt to disable versioning on the source bucket, Amazon S3 returns an error\. You must remove the replication configuration before you can disable versioning on the source bucket\.   If you disable versioning on the destination bucket, replication fails\. The source object has the replication status `Failed`\.     Logging Configuration and Replication Configuration  If Amazon S3 delivers logs to a bucket that has replication enabled, it replicates the log objects\. If server access logs \([Amazon S3 Server Access Logging](ServerLogs.md)\) or AWS CloudTrail Logs \( [Logging Amazon S3 API Calls by Using AWS CloudTrail](cloudtrail-logging.md)\) are enabled on your source or destination bucket, Amazon S3 includes CRR\-related requests in the logs\. For example, Amazon S3 logs each object that it replicates\.    CRR and Destination Region  In CRR configuration, the source and destination buckets must be in different AWS Regions\. You might choose the Region for you destination bucket based on either your business needs or cost considerations\. For example, inter\-region data transfer charges vary depending on the Regions that you choose\. Suppose that you chose US East \(N\. Virginia\) \(us\-east\-1\) as the Region for your source bucket\. If you choose US West \(Oregon\) \(us\-west\-2\) as the Region for your destination bucket, you pay more than if you choose the US East \(Ohio\) \(us\-east\-2\) Region\. For pricing information, see "Data Transfer Pricing" in [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.   Pausing Replication Configuration  To temporarily pause replication, disable the relevant rule in the replication configuration\.  If replication is enabled and you remove the IAM role that grants Amazon S3 the required permissions, replication fails\. Amazon S3 reports the replication status for affected objects as `Failed`\.   Related Topics  [Cross\-Region Replication ](crr.md)   ](crr.md)\.

## Related Resources<a name="object-lock-overview-related-resources"></a>
+ [Introduction to Amazon S3 Object Lock](object-lock.md)
+ [Managing Object Locks](object-lock-managing.md)