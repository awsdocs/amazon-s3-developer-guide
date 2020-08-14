# Managing Amazon S3 object locks<a name="object-lock-managing"></a>

S3 Object Lock lets you store objects in Amazon S3 using a *write once, read many* \(WORM\) model\. You can use it to view, configure, and manage the object lock status of your Amazon S3 objects\. For more information about S3 Object Lock capabilities, see [S3 Object Lock overview](object-lock-overview.md)\.

**Topics**
+ [Viewing the lock information for an object](#object-lock-managing-view)
+ [Bypassing governance mode](#object-lock-managing-bypass)
+ [Configuring events and notifications](#object-lock-managing-events)
+ [Setting retention limits](#object-lock-managing-retention-limits)
+ [Managing delete markers and object lifecycles](#object-lock-managing-lifecycle)
+ [Using S3 Object Lock with replication](#object-lock-managing-replication)

## Viewing the lock information for an object<a name="object-lock-managing-view"></a>

You can view the Object Lock status of an Amazon S3 object version using the `GET Object` or `HEAD Object` commands\. Both commands return the retention mode, `Retain Until Date`, and the legal\-hold status for the specified object version\. 

To view an object version's retention mode and retention period, you must have the `s3:GetObjectRetention` permission\. To view an object version's legal hold status, you must have the `s3:GetObjectLegalHold` permission\. If you `GET` or `HEAD` an object version but don't have the necessary permissions to view its lock status, the request succeeds\. However, it doesn't return information that you don't have permission to view\.

To view a bucket's default retention configuration \(if it has one\), request the bucket's Object Lock configuration\. To do this, you must have the `s3:GetBucketObjectLockConfiguration` permission\. If you make a request for an Object Lock configuration against a bucket that doesn't have S3 Object Lock enabled, Amazon S3 returns an error\. For more information about permissions, see [Example — Object Operations](using-with-s3-actions.md#using-with-s3-actions-related-to-objects)\. 

You can configure Amazon S3 inventory reports on your buckets to include the `Retain Until Date`, `object lock Mode`, and `Legal Hold Status` for all objects in a bucket\. For more information, see [ Amazon S3 inventory](storage-inventory.md)\.

## Bypassing governance mode<a name="object-lock-managing-bypass"></a>

You can perform operations on object versions that are locked in governance mode as if they were unprotected if you have the `s3:BypassGovernanceRetention` permission\. These operations include deleting an object version, shortening the retention period, or removing the Object Lock by placing a new lock with empty parameters\. To bypass governance mode, you must explicitly indicate in your request that you want to bypass this mode\. To do this, include the `x-amz-bypass-governance-retention:true` header with your request, or use the equivalent parameter with requests made through the AWS CLI, or AWS SDKs\. The AWS Management Console automatically applies this header for requests made through the console if you have the permission required to bypass governance mode\.

**Note**  
Bypassing governance mode doesn't affect an object version's legal hold status\. If an object version has a legal hold enabled, the legal hold remains in force and prevents requests to overwrite or delete the object version\.

## Configuring events and notifications<a name="object-lock-managing-events"></a>

You can configure Amazon S3 events for object\-level operations in an S3 Object Lock S3 bucket\. When `PUT Object`, `HEAD Object`, and `GET Object` calls include Object Lock metadata, events for these calls include those metadata values\. When Object Lock metadata is added to or updated for an object, those actions also trigger events\. These events occur whenever you `PUT` or `GET` object retention or legal\-hold information\.

For more information about Amazon S3 events, see [ Configuring Amazon S3 event notifications](NotificationHowTo.md)\.

You can use Amazon S3 event notifications to track access and changes to your Object Lock configurations and data using AWS CloudTrail\. For information about CloudTrail, see the [AWS CloudTrail Documentation](https://docs.aws.amazon.com/cloudtrail/index.html)\. 

You can also use Amazon CloudWatch to generate alerts based on this data\. For information about CloudWatch, see the [Amazon CloudWatch Documentation](https://docs.aws.amazon.com/cloudwatch/index.html)\.

## Setting retention limits<a name="object-lock-managing-retention-limits"></a>

You can set minimum and maximum allowable retention periods for a bucket using a bucket policy\. You do this using the `s3:object-lock-remaining-retention-days` condition key\. The following example shows a bucket policy that uses the `s3:object-lock-remaining-retention-days` condition key to set a maximum retention period of 10 days\.

```
{
    "Version": "2012-10-17",
    "Id": "<SetRetentionLimits",
    "Statement": [
        {
            "Sid": "<SetRetentionPeriod",
            "Effect": "Deny",
            "Principal": "*",
            "Action": [
                "s3:PutObjectRetention"
            ],
            "Resource": "arn:aws:s3:::<awsexamplebucket1>/*",
            "Condition": {
                "NumericGreaterThan": {
                    "s3:object-lock-remaining-retention-days": "10"
                }
            }
        }
    ]
}
```

**Note**  
If your bucket is the destination bucket for a replication policy and you want to set up minimum and maximum allowable retention periods for object replicas that are created using replication, you must include the `s3:ReplicateObject` action in your bucket policy\.

For more information, see the following topics:
+ [Actions, resources, and condition keys for Amazon S3](list_amazons3.md)
+ [Example — Object Operations](using-with-s3-actions.md#using-with-s3-actions-related-to-objects)
+ [Amazon S3 Condition Keys](amazon-s3-policy-keys.md)

## Managing delete markers and object lifecycles<a name="object-lock-managing-lifecycle"></a>

Although you can't delete a protected object version, you can still create a delete marker for that object\. Placing a delete marker on an object doesn't delete any object version\. However, it makes Amazon S3 behave in most ways as though the object has been deleted\. For more information, see [Working with delete markers](DeleteMarker.md)\.

**Note**  
Delete markers are not WORM\-protected, regardless of any retention period or legal hold in place on the underlying object\.

Object lifecycle management configurations continue to function normally on protected objects, including placing delete markers\. However, protected object versions remain safe from being deleted or overwritten by a lifecycle configuration\. For more information about managing object lifecycles, see [Object lifecycle management](object-lifecycle-mgmt.md)\.

## Using S3 Object Lock with replication<a name="object-lock-managing-replication"></a>

You can use S3 Object Lock with replication to enable automatic, asynchronous copying of locked objects and their retention metadata, across S3 buckets in different or the same AWS Regions\. When you use replication, objects in a *source bucket* are replicated to a *destination bucket*\. For more information, see [Replication](replication.md)\. 

To set up S3 Object Lock with replication, you can choose one of the following options\.

Option 1: Enable Object Lock first\.

1. Enable Object Lock on the destination bucket, or on both the source and the destination bucket\. 

1. Set up replication between the source and the destination buckets\.

Option 2: Set up replication first\.

1. Set up replication between the source and destination buckets\.

1. Enable Object Lock on just the destination bucket, or on both the source and destination buckets\.

To complete step 2 in the preceding options, you must contact [AWS Support](https://console.aws.amazon.com/support/home)\. This is required to make sure that replication is configured correctly\. 

Before you contact AWS Support, review the following requirements for setting up Object Lock with replication:
+ The Amazon S3 destination bucket must have Object Lock enabled on it\.
+ You must grant two new permissions on the source S3 bucket in the AWS Identity and Access Management \(IAM\) role that you use to set up replication\. The two new permissions are `s3:GetObjectRetention` and `s3:GetObjectLegalHold`\. If the role has an `s3:Get*` permission, it satisfies the requirement\. For more information, see [Setting up permissions for replication](setting-repl-config-perm-overview.md)\.

For more information about S3 Object Lock, see [Locking objects using S3 Object Lock](object-lock.md)\.