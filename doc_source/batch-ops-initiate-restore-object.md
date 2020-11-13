# Initiate restore object<a name="batch-ops-initiate-restore-object"></a>

You can use S3 Batch Operations to perform large\-scale operations on Amazon S3 objects\. S3 Batch Operations can run a single operation on a list of Amazon S3 objects that you specify, including initiating restores for archived objects\. For more information, see [Performing S3 Batch Operations](batch-ops.md)\. 

If you archive objects to the S3 Glacier or S3 Glacier Deep Archive storage classes, or if they are archived through S3 Intelligent\-Tiering storage class in the Archive Access and Deep Archive Access tiers, the objects are not accessible in real time\. Using the `InitiateRestore` operation in your S3 Batch Operations sends a restore for each object that is specified in the manifest\. 

**Note**  
 S3 Intelligent\-Tiering archived objects do not accept the `ExpirationInDays` element that is required for S3 Glacier and S3 Glacier Deep Archive objects\. This means that you must initiate separate Batch Operations jobs if you're restoring objects from both storage classes\. 

For more information about object restoration, see [Restoring archived objects](restoring-objects.md)\. 

To create an `Initiate Restore Object` job, you can include two elements with your request:
+ **ExpirationInDays**
  + This element specifies how long the object will remain available in Amazon S3\. When you restore from S3 Glacier or S3 Glacier Deep Archive, a temporary copy of the object is created\. Amazon S3 deletes this copy after a fixed period of time\. After that, you can only retrieve the object by initiating another restore request\. 

    The `ExpirationInDays` element is not accepted when restoring objects from the S3 Intelligent\-Tiering\. When you restore from S3 Intelligent\-Tiering Archive Access or S3 Intelligent\-Tiering Deep Archive Access, the object moves back into the S3 Intelligent\-Tiering Frequent Access tier\. The object automatically moves down into the Archive Access tier after a minimum of 90 consecutive days of no access, and moves into the Deep Archive Access tier after a minimum of 180 consecutive days of no access\.
+ **Tier**
  + Amazon S3 can restore objects using one of three different retrieval tiers: *Expedited*, *Standard*, and *Bulk*\. The S3 Batch Operations feature supports only the Standard and Bulk tiers\. For more information about the differences between retrieval tiers, see [Archive retrieval options](restoring-objects.md#restoring-objects-retrieval-options)\. For more information about pricing for each tier, see the "Requests and data retrievals" section on [Amazon S3 pricing](https://aws.amazon.com/s3/pricing/)\.

**Important**  
The `Initiate Restore Object` job only initiates the request to restore objects\. S3 Batch Operations will report the job as complete for each object after the request has been initiated for that object\. Amazon S3 doesn't update the job or otherwise notify you when the objects have been restored\. However, you can use event notifications to receive notifications when the objects are available in Amazon S3\. For more information, see [ Configuring Amazon S3 event notifications](NotificationHowTo.md)\.

## Overlapping restores<a name="batch-ops-initiate-restore-object-in-progress"></a>

If your `Initiate Restore Object` job tries to restore an object that is already in the process of being restored, S3 Batch Operations will behave as follows:

The restore operation succeeds for the object if either of the following conditions is true:
+ Compared to the restoration request already in progress, this job's `ExpirationInDays` is the same and `Tier` is faster\.
+ The previous restoration request has already completed, and the object is currently available\. In this case, Batch Operations update the expiration date of the restored object to match the `ExpirationInDays` specified in this job\.

The restore operation fails for the object if any of the following conditions are true:
+ The restoration request already in progress has not yet completed and the restoration duration for this job \(specified by `ExpirationInDays`\) is different than the restoration duration that is specified in the in\-progress restoration request\.
+ The restoration tier for this job \(specified by `Tier`\) is the same or slower than the restoration tier that is specified in the in\-progress restoration request\.

## Limitations<a name="batch-ops-initiate-restore-object-limitations"></a>

`Initiate Restore Object` jobs have the following limitations:
+ You must create an `Initiate Restore Object` job in the same Region as the archived objects\.
+ S3 Batch Operations do not support S3 Glacier SELECT\.
+ S3 Batch Operations do not support the expedited retrieval tier\.