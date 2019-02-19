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

You can't copy from a bucket that has S3 Object Lock enabled using cross\-region replication \(CRR\)\. If you try to set up a CRR rule using a source bucket configured for S3 Object Lock, the request will fail\. You can use a bucket with S3 Object Lock enabled as the destination for a CRR rule, however\. This enables you to apply WORM protection to your replicated objects\. For more information about CRR, see [Cross\-Region Replication](crr.md)\.

## Related Resources<a name="object-lock-overview-related-resources"></a>
+ [Introduction to Amazon S3 Object Lock](object-lock.md)
+ [Managing Object Locks](object-lock-managing.md)