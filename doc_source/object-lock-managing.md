# Managing Amazon S3 Object Locks<a name="object-lock-managing"></a>

Amazon S3 object lock lets you store objects in Amazon S3 using a *write\-once\-read\-many* \(WORM\) model\. You can use it to view, configure, and manage the object lock status of your Amazon S3 objects\. For more information about Amazon S3 object lock capabilities, see [Amazon S3 Object Lock Overview](object-lock-overview.md)\.

**Topics**
+ [Viewing the Lock Information for an Object](#object-lock-managing-view)
+ [Bypassing Governance Mode](#object-lock-managing-bypass)
+ [Configuring Events and Notifications](#object-lock-managing-events)
+ [Setting Retention Limits](#object-lock-managing-retention-limits)
+ [Managing Delete Markers and Object Lifecycles](#object-lock-managing-lifecycle)
+ [Using Object Lock with Cross\-Region Replication](#object-lock-managing-replication)

## Viewing the Lock Information for an Object<a name="object-lock-managing-view"></a>

You can view the object lock status of an Amazon S3 object version using the `GET Object` or `HEAD Object` commands\. Both commands return the retention mode, `Retain Until Date`, and the legal\-hold status for the specified object version\. 

To view an object version's retention mode and retention period, you must have the `s3:GetObjectRetention` permission\. To view an object version's legal hold status, you must have the `s3:GetObjectLegalHold` permission\. If you `GET` or `HEAD` an object version but don't have the necessary permissions to view its lock status, the request succeeds\. However, it doesn't return information that you don't have permission to view\.

To view a bucket's default retention configuration \(if it has one\), request the bucket's object lock configuration\. To do this, you must have the `s3:GetBucketObjectLockConfiguration` permission\. If you make a request for an object lock configuration against a bucket that doesn't have Amazon S3 object lock enabled, Amazon S3 returns an error\.

You can configure Amazon S3 inventory reports on your buckets to include the `Retain Until Date`, `object lock Mode`, and `Legal Hold Status` for all objects in a bucket\. For more information, see [ Amazon S3 Inventory](storage-inventory.md)\.

## Bypassing Governance Mode<a name="object-lock-managing-bypass"></a>

You can perform operations on object versions that are locked in governance mode as if they were unprotected if you have the `s3:BypassGovernanceRetention` permission\. These operations include deleting an object version, shortening the retention period, or removing the object lock by placing a new lock with empty parameters\. To bypass governance mode, you must explicitly indicate in your request that you want to bypass this mode\. To do this, include the `x-amz-bypass-governance-retention:true` header with your request, or use the equivalent parameter with requests made through the AWS CLI, or AWS SDKs\. The AWS Management Console automatically applies this header for requests made through the console if you have the permission required to bypass governance mode\.

**Note**  
Bypassing governance mode doesn't affect an object version's legal hold status\. If an object version has a legal hold enabled, the legal hold remains in force and prevents requests to overwrite or delete the object version\.

## Configuring Events and Notifications<a name="object-lock-managing-events"></a>

You can configure Amazon S3 events for object\-level operations in an object lock bucket\. When `PUT Object`, `HEAD Object`, and `GET Object` calls include object lock metadata, events for these calls include those metadata values\. When object lock metadata is added to or updated for an object, those actions also trigger events\. These events occur whenever you `PUT` or `GET` object retention or legal\-hold information\.

For more information about Amazon S3 events, see [ Configuring Amazon S3 Event Notifications](NotificationHowTo.md)\.

You can use Amazon S3 event notifications to track access and changes to your object lock configurations and data using AWS CloudTrail\. For information about CloudTrail, see the [AWS CloudTrail Documentation](https://docs.aws.amazon.com/cloudtrail/index.html)\. 

You can also use Amazon CloudWatch to generate alerts based on this data\. For information about CloudWatch, see the [Amazon CloudWatch Documentation](https://docs.aws.amazon.com/cloudwatch/index.html)\.

## Setting Retention Limits<a name="object-lock-managing-retention-limits"></a>

You can set minimum and maximum allowable retention periods for a bucket using a bucket policy\. You do this using the `s3:object-lock-remaining-retention-days` condition key\. The following example shows a bucket policy that sets a maximum retention period of 10 days\.

```
{
    "Version": "2012-10-17",
    "Id": "<Policy1436912751980>",
    "Statement": [
        {
            "Sid": "<Stmt1436912698057>",
            "Effect": "Deny",
            "Principal": "*",
            "Action": [
                "s3:PutObjectRetention"
            ],
            "Resource": "arn:aws:s3:::<example-bucket>/*",
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
If your bucket is the destination bucket for a cross\-region replication \(CRR\) policy and you want to set up minimum and maximum allowable retention periods for object replicas that are created using CRR, you must include the `s3:ReplicateObject` action in your bucket policy\.

For more information about using bucket policies, see [Using Bucket Policies and User Policies](using-iam-policies.md)\.

## Managing Delete Markers and Object Lifecycles<a name="object-lock-managing-lifecycle"></a>

Although you can't delete a protected object version, you can still create a delete marker for that object\. Placing a delete marker on an object doesn't delete any object version\. However, it makes Amazon S3 behave in most ways as though the object has been deleted\. For more information, see [Working with Delete Markers](DeleteMarker.md)\.

**Note**  
Delete markers are not WORM\-protected, regardless of any retention period or legal hold in place on the underlying object\.

Object lifecycle management configurations continue to function normally on protected objects, including placing delete markers\. However, protected object versions remain safe from being deleted or overwritten by a lifecycle configuration\. For more information about managing object lifecycles, see [Object Lifecycle Management](object-lifecycle-mgmt.md)\.

## Using Object Lock with Cross\-Region Replication<a name="object-lock-managing-replication"></a>

You can use Amazon S3 object lock with cross\-region replication \(CRR\) to enable automatic, asynchronous copying of locked objects and their retention metadata, across S3 buckets in different AWS Regions\. When you use CRR, objects in a *source bucket* are replicated to a *destination bucket*\. For more information, see [Cross\-Region Replication](crr.md)\. 

To set up object lock with cross\-region replication, you can choose one of the following options\.

Option 1: Enable object lock first\.

1. Enable object lock on the destination bucket, or on both the source and the destination bucket\. 

1. Set up CRR between the source and the destination buckets\.

Option 2: Set up CRR first\.

1. Set up CRR between the source and destination buckets\.

1. Enable object lock on just the destination bucket, or on both the source and destination buckets\.

To complete step 2 in the preceding options, you must contact [AWS Support](https://console.aws.amazon.com//support/home)\. This is required to make sure cross\-region replication is configured correctly\. 

Before you contact AWS Support, review the following requirements for setting up object lock with cross\-region replication:
+ The Amazon S3 destination bucket must have object lock enabled on it\.
+ You must grant two new permissions on the source S3 bucket in the AWS Identity and Access Management \(IAM\) role that you use to set up CRR\. The two new permissions are `s3:GetObjectRetention` and `s3:GetObjectLegalHold`\. If the role has an `s3:Get*` permission, it satisfies the requirement\. For more information, see [Setting Up Permissions for Cross\-Region Replication](setting-repl-config-perm-overview.md)\.

For more information about Amazon S3 object lock, see [Locking Objects Using Amazon S3 Object Lock](object-lock.md)\.