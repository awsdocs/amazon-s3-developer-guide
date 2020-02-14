# Transitioning Objects Using Amazon S3 Lifecycle<a name="lifecycle-transition-general-considerations"></a>

You can add rules in a lifecycle configuration to tell Amazon S3 to transition objects to another Amazon S3 [storage class](https://docs.aws.amazon.com/AmazonS3/latest/dev/storage-class-intro.html)\. For example:
+ When you know that objects are infrequently accessed, you might transition them to the STANDARD\_IA storage class\.
+ You might want to archive objects that you don't need to access in real time to the GLACIER storage class\.

 The following sections describe supported transitions, related constraints, and transitioning to the GLACIER storage class\.

## Supported Transitions and Related Constraints<a name="lifecycle-general-considerations-transition-sc"></a>

In a lifecycle configuration, you can define rules to transition objects from one storage class to another to save on storage costs\. When you don't know the access patterns of your objects, or your access patterns are changing over time, you can transition the objects to the INTELLIGENT\_TIERING storage class for automatic cost savings\. For information about storage classes, see [Amazon S3 Storage Classes](storage-class-intro.md)\. 

Amazon S3 supports a waterfall model for transitioning between storage classes, as shown in the following diagram\. 

![\[Amazon S3 storage class waterfall graphic.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/SupportedTransitionsWaterfallModel.png)

### Supported Lifecycle Transitions<a name="supported-lifecycle-transitions"></a>

Amazon S3 supports the following lifecycle transitions between storage classes using a lifecycle configuration\. 

You *can transition* from:
+ The STANDARD storage class to any other storage class\.
+ Any storage class to the GLACIER or DEEP ARCHIVE storage classes\. 
+ The STANDARD\_IA storage class to the INTELLIGENT\_TIERING or ONEZONE\_IA storage classes\.
+ The INTELLIGENT\_TIERING storage class to the ONEZONE\_IA storage class\.
+ The GLACIER storage class to the DEEP ARCHIVE storage class\.

### Unsupported Lifecycle Transitions<a name="unsupported-lifecycle-transitions"></a>

Amazon S3 does not support any of the following lifecycle transitions\. 

You *can't transition* from:
+ Any storage class to the STANDARD storage class\.
+ Any storage class to the REDUCED\_REDUNDANCY storage class\.
+ The INTELLIGENT\_TIERING storage class to the STANDARD\_IA storage class\.
+ The ONEZONE\_IA storage class to the STANDARD\_IA or INTELLIGENT\_TIERING storage classes\.

### Constraints<a name="lifecycle-configuration-constraints"></a>

Lifecycle storage class transitions have the following constraints:

**Object Size and Transitions from STANDARD or STANDARD\_IA to INTELLIGENT\_TIERING, STANDARD\_IA, or ONEZONE\_IA**  
When you transition objects from the STANDARD or STANDARD\_IA storage classes to INTELLIGENT\_TIERING, STANDARD\_IA, or ONEZONE\_IA, the following object size constraints apply:
+ **Larger objects** ‐ For the following transitions, there is a cost benefit to transitioning larger objects:
  + From the STANDARD or STANDARD\_IA storage classes to INTELLIGENT\_TIERING\.
  + From the STANDARD storage class to STANDARD\_IA or ONEZONE\_IA\.
+  **Objects smaller than 128 KB ** ‐ For the following transitions, Amazon S3 does not transition objects that are smaller than 128 KB because it's not cost effective:
  + From the STANDARD or STANDARD\_IA storage classes to INTELLIGENT\_TIERING\.
  + From the STANDARD storage class to STANDARD\_IA or ONEZONE\_IA\.

**Minimum Days for Transition from STANDARD or STANDARD\_IA to STANDARD\_IA or ONEZONE\_IA**  
Before you transition objects from the STANDARD or STANDARD\_IA storages classes to STANDARD\_IA or ONEZONE\_IA, you must store them at least 30 days in the STANDARD storage class\. For example, you cannot create a lifecycle rule to transition objects to the STANDARD\_IA storage class one day after you create them\. Amazon S3 doesn't transition objects within the first 30 days because newer objects are often accessed more frequently or deleted sooner than is suitable for STANDARD\_IA or ONEZONE\_IA storage\.

Similarly, if you are transitioning noncurrent objects \(in versioned buckets\), you can transition only objects that are at least 30 days noncurrent to STANDARD\_IA or ONEZONE\_IA storage\. 

**Minimum 30\-Day Storage Charge for INTELLIGENT\_TIERING, STANDARD\_IA, and ONEZONE\_IA**  
The INTELLIGENT\_TIERING, STANDARD\_IA, and ONEZONE\_IA storage classes have a minimum 30\-day storage charge\. Therefore, you can't specify a single lifecycle rule for both an INTELLIGENT\_TIERING, STANDARD\_IA, or ONEZONE\_IA transition and a GLACIER or DEEP ARCHIVE transition when the GLACIER or DEEP ARCHIVE transition occurs less than 30 days after the INTELLIGENT\_TIERING, STANDARD\_IA, or ONEZONE\_IA transition\.

The same 30\-day minimum applies when you specify a transition from STANDARD\_IA storage to ONEZONE\_IA or INTELLIGENT\_TIERING storage\. You can specify two rules to accomplish this, but you pay minimum storage charges\. For more information about cost considerations, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

### Manage an Object's Complete Lifecycle<a name="manage-complete-object-lifecycle"></a>

You can combine these lifecycle actions to manage an object's complete lifecycle\.  For example, suppose that the objects you create have a well\-defined lifecycle\. Initially, the objects are frequently accessed for a period of 30 days\. Then, objects are infrequently accessed for up to 90 days\. After that, the objects are no longer needed, so you might choose to archive or delete them\. 

In this scenario, you can create a lifecycle rule in which you specify the initial transition action to INTELLIGENT\_TIERING, STANDARD\_IA, or ONEZONE\_IA storage, another transition action to GLACIER storage for archiving, and an expiration action\. As you move the objects from one storage class to another, you save on storage cost\. For more information about cost considerations, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

## Transitioning to the GLACIER and DEEP ARCHIVE Storage Classes \(Object Archival\)<a name="before-deciding-to-archive-objects"></a>

Using lifecycle configuration, you can transition objects to the GLACIER or DEEP ARCHIVE storage classes for archiving\. When you choose the GLACIER or DEEP ARCHIVE storage class, your objects remain in Amazon S3\. You cannot access them directly through the separate Amazon S3 Glacier service\. 

Before you archive objects, review the following sections for relevant considerations\.

### General Considerations<a name="transition-glacier-general-considerations"></a>

The following are the general considerations for you to consider before you archive objects:
+ Encrypted objects remain encrypted throughout the storage class transition process\.
+ Objects that are stored in the GLACIER or DEEP ARCHIVE storage classes are not available in real time\.

  Archived objects are Amazon S3 objects, but before you can access an archived object, you must first restore a temporary copy of it\. The restored object copy is available only for the duration you specify in the restore request\. After that, Amazon S3 deletes the temporary copy, and the object remains archived in Amazon S3 Glacier\. 

  You can restore an object by using the Amazon S3 console or programmatically by using the AWS SDKs wrapper libraries or the Amazon S3 REST API in your code\. For more information, see [Restoring Archived Objects](restoring-objects.md)\.
+ Objects that are stored in the GLACIER storage class can only be transitioned to the DEEP ARCHIVE storage class\.

  You can use a lifecycle configuration rule to convert the storage class of an object from GLACIER to the DEEP ARCHIVE storage class only\. If you want to change the storage class of an object that is stored in GLACIER to a storage class other than DEEP ARCHIVE, you must use the restore operation to make a temporary copy of the object first\. Then use the copy operation to overwrite the object specifying STANDARD, INTELLIGENT\_TIERING, STANDARD\_IA, ONEZONE\_IA, or REDUCED\_REDUNDANCY as the storage class\.
+ The transition of objects to the DEEP ARCHIVE storage class can go only one way\.

  You cannot use a lifecycle configuration rule to convert the storage class of an object from DEEP ARCHIVE to any other storage class\. If you want to change the storage class of an archived object to another storage class, you must use the restore operation to make a temporary copy of the object first\. Then use the copy operation to overwrite the object specifying STANDARD, INTELLIGENT\_TIERING, STANDARD\_IA, ONEZONE\_IA, GLACIER, or REDUCED\_REDUNDANCY as the storage class\.
+ The objects that are stored in the GLACIER and DEEP ARCHIVE storage classes are visible and available only through Amazon S3\. They are not available through the separate Amazon S3 Glacier service\.

  These are Amazon S3 objects, and you can access them only by using the Amazon S3 console or the Amazon S3 API\. You cannot access the archived objects through the separate Amazon S3 Glacier console or the Amazon S3 Glacier API\.

### Cost Considerations<a name="glacier-pricing-considerations"></a>

If you are planning to archive infrequently accessed data for a period of months or years, the GLACIER and DEEP ARCHIVE storage classes can reduce your storage costs\. However, to ensure that the GLACIER or DEEP ARCHIVE storage class is appropriate for you, consider the following:
+ **Storage overhead charges** – When you transition objects to the GLACIER or DEEP ARCHIVE storage class, a fixed amount of storage is added to each object to accommodate metadata for managing the object\.
  + For each object archived to GLACIER or DEEP ARCHIVE, Amazon S3 uses 8 KB of storage for the name of the object and other metadata\. Amazon S3 stores this metadata so that you can get a real\-time list of your archived objects by using the Amazon S3 API\. For more information, see [Get Bucket \(List Objects\)](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html)\. You are charged Amazon S3 STANDARD rates for this additional storage\.
  +  For each object that is archived to GLACIER or DEEP ARCHIVE, Amazon S3 adds 32 KB of storage for index and related metadata\. This extra data is necessary to identify and restore your object\. You are charged GLACIER or DEEP ARCHIVE rates for this additional storage\.

  If you are archiving small objects, consider these storage charges\. Also consider aggregating many small objects into a smaller number of large objects to reduce overhead costs\.
+ **Number of days you plan to keep objects archived**—GLACIER and DEEP ARCHIVE are long\-term archival solutions\. The minimal storage duration period is 90 days for the GLACIER storage class and 180 days for DEEP ARCHIVE\. Deleting data that is archived to Amazon S3 Glacier is free if the objects you delete are archived for more than the minimal storage duration period\. If you delete or overwrite an archived object within the minimal duration period, Amazon S3 charges a prorated early deletion fee\.
+ **Amazon S3 GLACIER and DEEP ARCHIVE transition request charges**— Each object that you transition to the GLACIER or DEEP ARCHIVE storage class constitutes one transition request\. There is a cost for each such request\. If you plan to transition a large number of objects, consider the request costs\. If you are archiving small objects, consider aggregating many small objects into a smaller number of large objects to reduce transition request costs\.
+ **Amazon S3 GLACIER and DEEP ARCHIVE data restore charges**—GLACIER and DEEP ARCHIVE are designed for long\-term archival of data that you access infrequently\. For information about data restoration charges, see [How much does it cost to retrieve data from Amazon S3 Glacier?](https://aws.amazon.com/s3/faqs/#How_am_I_charged_for_deleting_objects_from_Amazon_Glacier_that_are_less_than_3_months_old) in the Amazon S3 FAQ\. For information about how to restore data from Amazon S3 Glacier, see [Restoring Archived Objects](restoring-objects.md)\. 

When you archive objects to Amazon S3 Glacier by using object lifecycle management, Amazon S3 transitions these objects asynchronously\. There might be a delay between the transition date in the lifecycle configuration rule and the date of the physical transition\. You are charged Amazon S3 Glacier prices based on the transition date specified in the rule\.

The Amazon S3 product detail page provides pricing information and example calculations for archiving Amazon S3 objects\. For more information, see the following topics:
+  [How is my storage charge calculated for Amazon S3 objects archived to Amazon S3 Glacier?](https://aws.amazon.com/s3/faqs/#How_is_my_storage_charge_calculated_for_Amazon_S3_objects_archived_to_Amazon_Glacier) 
+  [How am I charged for deleting objects from Amazon S3 Glacier that are less than 3 months old?](https://aws.amazon.com/s3/faqs/#How_am_I_charged_for_deleting_objects_from_Amazon_Glacier_that_are_less_than_3_months_old) 
+  [ How much does it cost to retrieve data from Amazon S3 Glacier?](https://aws.amazon.com/s3/faqs/#How_am_I_charged_for_deleting_objects_from_Amazon_Glacier_that_are_less_than_3_months_old) 
+  [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/) for storage costs for the different storage classes\. 

### Restoring Archived Objects<a name="restore-glacier-objects-concepts"></a>

Archived objects are not accessible in real time\. You must first initiate a restore request and then wait until a temporary copy of the object is available for the duration that you specify in the request\. After you receive a temporary copy of the restored object, the object's storage class remains GLACIER or DEEP ARCHIVE\. \(A [HEAD Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectHEAD.html) or [GET Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html) API operation request will return GLACIER or DEEP ARCHIVE as the storage class\.\) 

**Note**  
When you restore an archive, you are paying for both the archive \(GLACIER or DEEP ARCHIVE rate\) and a copy that you restored temporarily \(REDUCED\_REDUNDANCY storage rate\)\. For information about pricing, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\. 

You can restore an object copy programmatically or by using the Amazon S3 console\. Amazon S3 processes only one restore request at a time per object\. For more information, see [Restoring Archived Objects](restoring-objects.md)\.