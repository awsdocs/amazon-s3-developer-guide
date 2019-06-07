# Managing Object Locks<a name="object-lock-managing"></a>

## Viewing Lock Information<a name="object-lock-managing-view"></a>

You can view an object version's Object Lock status using the GET Object or HEAD Object commands\. Both commands return the retention mode, Retain Until Date, and legal\-hold status for the specified object version\. In order to view an object version's retention mode and retention period, you must have the `s3:GetObjectRetention` permission\. In order to view an object version's legal\-hold status, you must have the `s3:GetObjectLegalHold` permission\. If you GET or HEAD an object version but don't have the necessary permissions to view its lock status, the request will succeed but won't return the information that you don't have permission to view\.

You can view a bucket's Object Lock default retention configuration, if it has one, by requesting the bucket's Object Lock configuration\. You must have the `s3:GetBucketObjectLockConfiguration` permission in order to view a bucket's configuration\. If you make a request for an object\-lock configuration against a bucket that doesn't have Object Lock enabled, Amazon S3 returns an error\.

S3 Inventory Reports can be configured on your buckets to include the Retain Until Date, Object Lock Mode, and Legal Hold Status for all objects in a bucket\. For more information about S3 Inventory Reports, see [ Amazon S3 Inventory](storage-inventory.md)\.

## Bypassing Governance Mode<a name="object-lock-managing-bypass"></a>

You can perform operations on object versions locked in Governance mode as though they were unprotected if you have the `s3:BypassGovernanceRetention` permission\. This includes deleting an object version, shortening the retention period, or removing the object lock by placing a new lock with empty parameters\. In order to bypass Governance mode, you must explicitly indicate in your request that you want to bypass Governance mode\. You do this by including the `x-amz-bypass-governance-retention:true` header with your request or using the equivalent parameter with requests made through the AWS CLI, or AWS SDKs\. The AWS Management Console automatically applies this header for requests made through the console if you have the permission required to bypass Governance mode\.

**Note**  
Bypassing Governance mode doesn't affect an object version's legal\-hold status\. If an object version has a legal hold enabled, the legal hold remains in force and prevents requests to overwrite or delete the object version\.

## Events and Notifications<a name="object-lock-managing-events"></a>

S3 Events can be configured for object\-level operations in an Object Lock bucket\. When `PUT Object`, `HEAD Object`, and `GET Object` calls include Object Lock metadata, events for these calls will include those metadata values\. When Object Lock metadata is added to or updated for an object, those actions will also trigger events\. These events occur whenever you PUT or GET object retention or legal\-hold information\.

You can use Amazon S3 Event Notifications to track access and changes to your Object Lock configurations and data using AWS CloudTrail\. You can also use Amazon CloudWatch to generate alerts based on this data\. For more information about S3 Events, see [ Configuring Amazon S3 Event Notifications](NotificationHowTo.md)\. For more information about AWS CloudTrail, see the [AWS CloudTrail Documentation](https://docs.aws.amazon.com/cloudtrail/index.html)\. For more information about Amazon CloudWatch, see the [AWS CloudWatch Documentation](https://docs.aws.amazon.com/cloudwatch/index.html)\.

## Setting Retention Limits<a name="object-lock-managing-retention-limits"></a>

You can set minimum and maximum allowable retention periods for a bucket using a bucket policy\. You do this using the `s3:object-lock-remaining-retention-days` condition key\. The following example shows a bucket policy that sets a minimum retention period of 10 days:

```
{"Version":"2012-10-17",
   "Id":"<Policy1436912751980>",
   "Statement":[{
      "Sid":"<Stmt1436912698057>",
      "Effect":"Deny",
      "Principal":"*",
      "Action":["s3:PutObjectRetention"],
      "Resource":"arn:aws:s3:::<example-bucket>/*",
      "Condition":{"NumericGreaterThan": {"s3:object-lock-remaining-retention-days": "10"}}
}]}
```

For more information about using bucket policies, see [Using Bucket Policies and User Policies](using-iam-policies.md)\.

## Delete Markers and Lifecycle Management<a name="object-lock-managing-lifecycle"></a>

Although you can't delete a protected object version, you can still create a delete marker for that object\. Placing a delete marker on an object doesn't delete any object version, but makes Amazon S3 behave in most ways as though the object has been deleted\. For more information about delete markers, see [Working with Delete Markers](DeleteMarker.md)\.

**Note**  
Delete markers are not WORM\-protected, regardless of any retention period or legal hold in place on the underlying object\.

Object Lifecycle Management configurations continue to function normally on protected objects, including placing delete markers\. However, protected object versions remain safe from being deleted or overwritten by a lifecycle configuration\. For more information about managing object lifecycles, see [Object Lifecycle Management](object-lifecycle-mgmt.md)\.

## Related Resources<a name="object-lock-managing-related-resources"></a>
+ [Introduction to Amazon S3 Object Lock](object-lock.md)
+ [Amazon S3 Object Lock Overview](object-lock-overview.md)