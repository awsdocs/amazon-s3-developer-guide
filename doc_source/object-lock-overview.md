# S3 Object Lock overview<a name="object-lock-overview"></a>

You can use S3 Object Lock to store objects using a *write\-once\-read\-many* \(WORM\) model\. It can help you prevent objects from being deleted or overwritten for a fixed amount of time or indefinitely\. You can use S3 Object Lock to meet regulatory requirements that require WORM storage, or add an extra layer of protection against object changes and deletion\. 

For information about managing the lock status of your Amazon S3 objects, see [Managing Amazon S3 object locks](object-lock-managing.md)\.

**Note**  
 S3 buckets with S3 Object Lock cannot be used as destination buckets for [Amazon S3 server access logging](ServerLogs.md)

The following sections describe the main features of S3 Object Lock\.

**Topics**
+ [Retention modes](#object-lock-retention-modes)
+ [Retention periods](#object-lock-retention-periods)
+ [Legal holds](#object-lock-legal-holds)
+ [Bucket configuration](#object-lock-bucket-config)
+ [Required permissions](#object-lock-permissions)

## Retention modes<a name="object-lock-retention-modes"></a>

S3 Object Lock provides two *retention modes*:
+ Governance mode
+ Compliance mode

These retention modes apply different levels of protection to your objects\. You can apply either retention mode to any object version that is protected by Object Lock\.

### <a name="object-lock-governance-mode"></a>

In *governance* mode, users can't overwrite or delete an object version or alter its lock settings unless they have special permissions\. With governance mode, you protect objects against being deleted by most users, but you can still grant some users permission to alter the retention settings or delete the object if necessary\. You can also use governance mode to test retention\-period settings before creating a compliance\-mode retention period\. To override or remove governance\-mode retention settings, a user must have the `s3:BypassGovernanceRetention` permission and must explicitly include `x-amz-bypass-governance-retention:true` as a request header with any request that requires overriding governance mode\. 

**Note**  
The Amazon S3 console by default includes the `x-amz-bypass-governance-retention:true` header\. If you try to delete objects protected by *governance* mode and have `s3:BypassGovernanceRetention` or `s3:GetBucketObjectLockConfiguration` permissions, the operation will succeed\. 

### <a name="object-lock-compliance-mode"></a>

In *compliance* mode, a protected object version can't be overwritten or deleted by any user, including the root user in your AWS account\. When an object is locked in compliance mode, its retention mode can't be changed, and its retention period can't be shortened\. Compliance mode ensures that an object version can't be overwritten or deleted for the duration of the retention period\.

**Note**  
Updating an object version's metadata, as occurs when you place or alter an Object Lock, doesn't overwrite the object version or reset its `Last-Modified` timestamp\.

## Retention periods<a name="object-lock-retention-periods"></a>

A *retention period* protects an object version for a fixed amount of time\. When you place a retention period on an object version, Amazon S3 stores a timestamp in the object version's metadata to indicate when the retention period expires\. After the retention period expires, the object version can be overwritten or deleted unless you also placed a legal hold on the object version\.

You can place a retention period on an object version either explicitly or through a bucket default setting\. When you apply a retention period to an object version explicitly, you specify a *Retain Until Date* for the object version\. Amazon S3 stores the Retain Until Date setting in the object version's metadata and protects the object version until the retention period expires\.

When you use bucket default settings, you don't specify a Retain Until Date\. Instead, you specify a duration, in either days or years, for which every object version placed in the bucket should be protected\. When you place an object in the bucket, Amazon S3 calculates a Retain Until Date for the object version by adding the specified duration to the object version's creation timestamp\. It stores the Retain Until Date in the object version's metadata\. The object version is then protected exactly as though you explicitly placed a lock with that retention period on the object version\.

**Note**  
If your request to place an object version in a bucket contains an explicit retention mode and period, those settings override any bucket default settings for that object version\.

Like all other Object Lock settings, retention periods apply to individual object versions\. Different versions of a single object can have different retention modes and periods\.

For example, suppose that you have an object that is 15 days into a 30\-day retention period, and you `PUT` an object into Amazon S3 with the same name and a 60\-day retention period\. In this case, your `PUT` succeeds, and Amazon S3 creates a new version of the object with a 60\-day retention period\. The older version maintains its original retention period and becomes deletable in 15 days\.

You can extend a retention period after you've applied a retention setting to an object version\. To do this, submit a new lock request for the object version with a `Retain Until Date` that is later than the one currently configured for the object version\. Amazon S3 replaces the existing retention period with the new, longer period\. Any user with permissions to place an object retention period can extend a retention period for an object version locked in either mode\.

## Legal holds<a name="object-lock-legal-holds"></a>

Object Lock also enables you to place a *legal hold* on an object version\. Like a retention period, a legal hold prevents an object version from being overwritten or deleted\. However, a legal hold doesn't have an associated retention period and remains in effect until removed\. Legal holds can be freely placed and removed by any user who has the `s3:PutObjectLegalHold` permission\. For a complete list of Amazon S3 permissions, see [Actions, resources, and condition keys for Amazon S3](list_amazons3.md)\.

Legal holds are independent from retention periods\. As long as the bucket that contains the object has Object Lock enabled, you can place and remove legal holds regardless of whether the specified object version has a retention period set\. Placing a legal hold on an object version doesn't affect the retention mode or retention period for that object version\. For example, suppose that you place a legal hold on an object version while the object version is also protected by a retention period\. If the retention period expires, the object doesn't lose its WORM protection\. Rather, the legal hold continues to protect the object until an authorized user explicitly removes it\. Similarly, if you remove a legal hold while an object version has a retention period in effect, the object version remains protected until the retention period expires\.

## Bucket configuration<a name="object-lock-bucket-config"></a>

To use Object Lock, you must enable it for a bucket\. You can also optionally configure a default retention mode and period that applies to new objects that are placed in the bucket\.

### Enabling S3 Object Lock<a name="object-lock-bucket-config-enable"></a>

Before you can lock any objects, you have to configure a bucket to use S3 Object Lock\. To do this, you specify when you create the bucket that you want to enable Object Lock\. After you configure a bucket for Object Lock, you can lock objects in that bucket using retention periods, legal holds, or both\.

**Note**  
You can only enable Object Lock for new buckets\. If you want to turn on Object Lock for an existing bucket, contact AWS Support\.
When you create a bucket with Object Lock enabled, Amazon S3 automatically enables versioning for the bucket\.
Once you create a bucket with Object Lock enabled, you can't disable Object Lock or suspend versioning for the bucket\.

For information about enabling Object Lock on the console, see [How Do I Lock an Amazon S3 Object?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/object-lock.html) in the *Amazon Simple Storage Service Console User Guide*\.

### Default retention settings<a name="object-lock-bucket-config-defaults"></a>

When you turn on Object Lock for a bucket, the bucket can store protected objects\. However, the setting doesn't automatically protect objects that you put into the bucket\. If you want to automatically protect object versions that are placed in the bucket, you can configure a default retention period\. Default settings apply to all new objects that are placed in the bucket, unless you explicitly specify a different retention mode and period for an object when you create it\.

**Tip**  
If you want to enforce the bucket default retention mode and period for all new object versions placed in a bucket, set the bucket defaults and deny users permission to configure object retention settings\. Amazon S3 then applies the default retention mode and period to new object versions placed in the bucket, and rejects any request to put an object that includes a retention mode and setting\.

Bucket default settings require both a mode and a period\. A bucket default mode is either *governance* or *compliance*\. For more information, see [Retention modes](#object-lock-retention-modes)\. 

A default retention period is described not as a timestamp, but as a period either in days or in years\. When you place an object version in a bucket with a default retention period, Object Lock calculates a *Retain Until Date*\. It does this by adding the default retention period to the creation timestamp for the object version\. Amazon S3 stores the resulting timestamp as the object version's Retain Until Date, as if you had calculated the timestamp manually and placed it on the object version yourself\.

Default settings apply only to new objects that are placed in the bucket\. Placing a default retention setting on a bucket doesn't place any retention settings on objects that already exist in the bucket\.

**Important**  
Object locks apply to individual object versions only\. If you place an object in a bucket that has a default retention period, and you don't explicitly specify a retention period for that object, Amazon S3 creates the object with a retention period that matches the bucket default\. After the object is created, its retention period is independent from the bucket's default retention period\. Changing a bucket's default retention period doesn't change the existing retention period for any objects in that bucket\.

**Note**  
If you configure a default retention period on a bucket, requests to upload objects in such a bucket must include the `Content-MD5` header\. For more information, see [Put Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html) in the *Amazon Simple Storage Service API Reference*\. 

## Required permissions<a name="object-lock-permissions"></a>

 Object Lock operations require specific permissions\. For more information about required permissions, see [Example — Object Operations](using-with-s3-actions.md#using-with-s3-actions-related-to-objects)\. For information about using conditions with permissions, see [Amazon S3 Condition Keys](amazon-s3-policy-keys.md)\.