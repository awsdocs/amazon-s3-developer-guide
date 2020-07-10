# Transitioning objects using Amazon S3 Lifecycle<a name="lifecycle-transition-general-considerations"></a>

You can add rules in an S3 Lifecycle configuration to tell Amazon S3 to transition objects to another Amazon S3 [storage class](https://docs.aws.amazon.com/AmazonS3/latest/dev/storage-class-intro.html)\. For example:
+ When you know that objects are infrequently accessed, you might transition them to the S3 Standard\-IA storage class\.
+ You might want to archive objects that you don't need to access in real time to the S3 Glacier storage class\.

 The following sections describe supported transitions, related constraints, and transitioning to the S3 Glacier storage class\.

## Supported transitions and related constraints<a name="lifecycle-general-considerations-transition-sc"></a>

In an S3 Lifecycle configuration, you can define rules to transition objects from one storage class to another to save on storage costs\. When you don't know the access patterns of your objects, or your access patterns are changing over time, you can transition the objects to the S3 Intelligent\-Tiering storage class for automatic cost savings\. For information about storage classes, see [Amazon S3 storage classes](storage-class-intro.md)\. 

Amazon S3 supports a waterfall model for transitioning between storage classes, as shown in the following diagram\. 

![\[Amazon S3 storage class waterfall graphic.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/lifecycle-transitions-v2.png)

### Supported lifecycle transitions<a name="supported-lifecycle-transitions"></a>

Amazon S3 supports the following lifecycle transitions between storage classes using an S3 Lifecycle configuration\. 

You *can transition* from the following:
+ The S3 Standard storage class to any other storage class\.
+ Any storage class to the S3 Glacier or S3 Glacier Deep Archive storage classes\. 
+ The S3 Standard\-IA storage class to the S3 Intelligent\-Tiering or S3 One Zone\-IA storage classes\.
+ The S3 Intelligent\-Tiering storage class to the S3 One Zone\-IA storage class\.
+ The S3 Glacier storage class to the S3 Glacier Deep Archive storage class\.

### Unsupported lifecycle transitions<a name="unsupported-lifecycle-transitions"></a>

Amazon S3 does not support any of the following lifecycle transitions\. 

You *can't transition* from the following:
+ Any storage class to the S3 Standard storage class\.
+ Any storage class to the Reduced Redundancy storage class\.
+ The S3 Intelligent\-Tiering storage class to the S3 Standard\-IA storage class\.
+ The S3 One Zone\-IA storage class to the S3 Standard\-IA or S3 Intelligent\-Tiering storage classes\.

### Constraints<a name="lifecycle-configuration-constraints"></a>

Lifecycle storage class transitions have the following constraints:

**Object size and transitions from S3 Standard or S3 Standard\-IA to S3 Intelligent\-Tiering, S3 Standard\-IA, or S3 One Zone\-IA**  
When you transition objects from the S3 Standard or S3 Standard\-IA storage classes to S3 Intelligent\-Tiering, S3 Standard\-IA, or S3 One Zone\-IA, the following object size constraints apply:
+ **Larger objects** ‐ For the following transitions, there is a cost benefit to transitioning larger objects:
  + From the S3 Standard or S3 Standard\-IA storage classes to S3 Intelligent\-Tiering\.
  + From the S3 Standard storage class to S3 Standard\-IA or S3 One Zone\-IA\.
+  **Objects smaller than 128 KB ** ‐ For the following transitions, Amazon S3 does not transition objects that are smaller than 128 KB because it's not cost effective:
  + From the S3 Standard or S3 Standard\-IA storage classes to S3 Intelligent\-Tiering\.
  + From the S3 Standard storage class to S3 Standard\-IA or S3 One Zone\-IA\.

**Minimum days for transition from S3 Standard or S3 Standard\-IA to S3 Standard\-IA or S3 One Zone\-IA**  
Before you transition objects from the S3 Standard or S3 Standard\-IA storages classes to S3 Standard\-IA or S3 One Zone\-IA, you must store them at least 30 days in the S3 Standard storage class\. For example, you cannot create a Lifecycle rule to transition objects to the S3 Standard\-IA storage class one day after you create them\. Amazon S3 doesn't transition objects within the first 30 days because newer objects are often accessed more frequently or deleted sooner than is suitable for S3 Standard\-IA or S3 One Zone\-IA storage\.

Similarly, if you are transitioning noncurrent objects \(in versioned buckets\), you can transition only objects that are at least 30 days noncurrent to S3 Standard\-IA or S3 One Zone\-IA storage\. 

**Minimum 30\-Day storage charge for S3 Intelligent\-Tiering, S3 Standard\-IA, and S3 One Zone\-IA**  
The S3 Intelligent\-Tiering, S3 Standard\-IA, and S3 One Zone\-IA storage classes have a minimum 30\-day storage charge\. Therefore, you can't specify a single Lifecycle rule for both an S3 Intelligent\-Tiering, S3 Standard\-IA, or S3 One Zone\-IA transition and a S3 Glacier or S3 Glacier Deep Archive transition when the S3 Glacier or S3 Glacier Deep Archive transition occurs less than 30 days after the S3 Intelligent\-Tiering, S3 Standard\-IA, or S3 One Zone\-IA transition\.

The same 30\-day minimum applies when you specify a transition from S3 Standard\-IA storage to S3 One Zone\-IA or S3 Intelligent\-Tiering storage\. You can specify two rules to accomplish this, but you pay minimum storage charges\. For more information about cost considerations, see [Amazon S3 pricing](https://aws.amazon.com/s3/pricing/)\.

### Manage an object's complete lifecycle<a name="manage-complete-object-lifecycle"></a>

You can combine these S3 Lifecycle actions to manage an object's complete lifecycle\.  For example, suppose that the objects you create have a well\-defined lifecycle\. Initially, the objects are frequently accessed for a period of 30 days\. Then, objects are infrequently accessed for up to 90 days\. After that, the objects are no longer needed, so you might choose to archive or delete them\. 

In this scenario, you can create an S3 Lifecycle rule in which you specify the initial transition action to S3 Intelligent\-Tiering, S3 Standard\-IA, or S3 One Zone\-IA storage, another transition action to S3 Glacier storage for archiving, and an expiration action\. As you move the objects from one storage class to another, you save on storage cost\. For more information about cost considerations, see [Amazon S3 pricing](https://aws.amazon.com/s3/pricing/)\.

## Transitioning to the S3 Glacier and S3 Glacier Deep Archive storage classes \(object archival\)<a name="before-deciding-to-archive-objects"></a>

Using S3 Lifecycle configuration, you can transition objects to the S3 Glacier or S3 Glacier Deep Archive storage classes for archiving\. When you choose the S3 Glacier or S3 Glacier Deep Archive storage class, your objects remain in Amazon S3\. You cannot access them directly through the separate Amazon S3 Glacier service\. 

Before you archive objects, review the following sections for relevant considerations\.

### General considerations<a name="transition-glacier-general-considerations"></a>

The following are the general considerations for you to consider before you archive objects:
+ Encrypted objects remain encrypted throughout the storage class transition process\.
+ Objects that are stored in the S3 Glacier or S3 Glacier Deep Archive storage classes are not available in real time\.

  Archived objects are Amazon S3 objects, but before you can access an archived object, you must first restore a temporary copy of it\. The restored object copy is available only for the duration you specify in the restore request\. After that, Amazon S3 deletes the temporary copy, and the object remains archived in Amazon S3 Glacier\. 

  You can restore an object by using the Amazon S3 console or programmatically by using the AWS SDK wrapper libraries or the Amazon S3 REST API in your code\. For more information, see [Restoring Archived Objects](restoring-objects.md)\.
+ Objects that are stored in the S3 Glacier storage class can only be transitioned to the S3 Glacier Deep Archive storage class\.

  You can use an S3 Lifecycle configuration rule to convert the storage class of an object from S3 Glacier to the S3 Glacier Deep Archive storage class only\. If you want to change the storage class of an object that is stored in S3 Glacier to a storage class other than S3 Glacier Deep Archive, you must use the restore operation to make a temporary copy of the object first\. Then use the copy operation to overwrite the object specifying S3 Standard, S3 Intelligent\-Tiering, S3 Standard\-IA, S3 One Zone\-IA, or Reduced Redundancy as the storage class\.
+ The transition of objects to the S3 Glacier Deep Archive storage class can go only one way\.

  You cannot use an S3 Lifecycle configuration rule to convert the storage class of an object from S3 Glacier Deep Archive to any other storage class\. If you want to change the storage class of an archived object to another storage class, you must use the restore operation to make a temporary copy of the object first\. Then use the copy operation to overwrite the object specifying S3 Standard, S3 Intelligent\-Tiering, S3 Standard\-IA, S3 One Zone\-IA, S3 Glacier, or Reduced Redundancy as the storage class\.
+ The objects that are stored in the S3 Glacier and S3 Glacier Deep Archive storage classes are visible and available only through Amazon S3\. They are not available through the separate Amazon S3 Glacier service\.

  These are Amazon S3 objects, and you can access them only by using the Amazon S3 console or the Amazon S3 API\. You cannot access the archived objects through the separate Amazon S3 Glacier console or the Amazon S3 Glacier API\.

### Cost considerations<a name="glacier-pricing-considerations"></a>

If you are planning to archive infrequently accessed data for a period of months or years, the S3 Glacier and S3 Glacier Deep Archive storage classes can reduce your storage costs\. However, to ensure that the S3 Glacier or S3 Glacier Deep Archive storage class is appropriate for you, consider the following:
+ **Storage overhead charges** – When you transition objects to the S3 Glacier or S3 Glacier Deep Archive storage class, a fixed amount of storage is added to each object to accommodate metadata for managing the object\.
  + For each object archived to S3 Glacier or S3 Glacier Deep Archive, Amazon S3 uses 8 KB of storage for the name of the object and other metadata\. Amazon S3 stores this metadata so that you can get a real\-time list of your archived objects by using the Amazon S3 API\. For more information, see [Get Bucket \(List Objects\)](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html)\. You are charged Amazon S3 Standard rates for this additional storage\.
  +  For each object that is archived to S3 Glacier or S3 Glacier Deep Archive, Amazon S3 adds 32 KB of storage for index and related metadata\. This extra data is necessary to identify and restore your object\. You are charged S3 Glacier or S3 Glacier Deep Archive rates for this additional storage\.

  If you are archiving small objects, consider these storage charges\. Also consider aggregating many small objects into a smaller number of large objects to reduce overhead costs\.
+ **Number of days you plan to keep objects archived**—S3 Glacier and S3 Glacier Deep Archive are long\-term archival solutions\. The minimal storage duration period is 90 days for the S3 Glacier storage class and 180 days for S3 Glacier Deep Archive\. Deleting data that is archived to Amazon S3 Glacier is free if the objects you delete are archived for more than the minimal storage duration period\. If you delete or overwrite an archived object within the minimal duration period, Amazon S3 charges a prorated early deletion fee\.
+ ** S3 Glacier and S3 Glacier Deep Archive transition request charges**— Each object that you transition to the S3 Glacier or S3 Glacier Deep Archive storage class constitutes one transition request\. There is a cost for each such request\. If you plan to transition a large number of objects, consider the request costs\. If you are archiving small objects, consider aggregating many small objects into a smaller number of large objects to reduce transition request costs\.
+ ** S3 Glacier and S3 Glacier Deep Archive data restore charges**—S3 Glacier and S3 Glacier Deep Archive are designed for long\-term archival of data that you access infrequently\. For information about data restoration charges, see [How much does it cost to retrieve data from Amazon S3 Glacier?](https://aws.amazon.com/s3/faqs/#How_am_I_charged_for_deleting_objects_from_Amazon_Glacier_that_are_less_than_3_months_old) in the Amazon S3 FAQ\. For information about how to restore data from Amazon S3 Glacier, see [Restoring Archived Objects](restoring-objects.md)\. 

When you archive objects to Amazon S3 Glacier by using S3 Lifecycle management, Amazon S3 transitions these objects asynchronously\. There might be a delay between the transition date in the Lifecycle configuration rule and the date of the physical transition\. You are charged Amazon S3 Glacier prices based on the transition date specified in the rule\. For more information, see the Amazon S3 Glacier section of the [Amazon S3 FAQ](https://aws.amazon.com/s3/faqs/)\.

The Amazon S3 product detail page provides pricing information and example calculations for archiving Amazon S3 objects\. For more information, see the following topics:
+  [How is my storage charge calculated for Amazon S3 objects archived to Amazon S3 Glacier?](https://aws.amazon.com/s3/faqs/#How_is_my_storage_charge_calculated_for_Amazon_S3_objects_archived_to_Amazon_Glacier) 
+  [How am I charged for deleting objects from Amazon S3 Glacier that are less than 3 months old?](https://aws.amazon.com/s3/faqs/#How_am_I_charged_for_deleting_objects_from_Amazon_Glacier_that_are_less_than_3_months_old) 
+  [ How much does it cost to retrieve data from Amazon S3 Glacier?](https://aws.amazon.com/s3/faqs/#How_am_I_charged_for_deleting_objects_from_Amazon_Glacier_that_are_less_than_3_months_old) 
+  [Amazon S3 pricing](https://aws.amazon.com/s3/pricing/) for storage costs for the different storage classes\. 

### Restoring archived objects<a name="restore-glacier-objects-concepts"></a>

Archived objects are not accessible in real time\. You must first initiate a restore request and then wait until a temporary copy of the object is available for the duration that you specify in the request\. After you receive a temporary copy of the restored object, the object's storage class remains S3 Glacier or S3 Glacier Deep Archive\. \(A [HEAD Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectHEAD.html) or [GET Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html) API operation request will return S3 Glacier or S3 Glacier Deep Archive as the storage class\.\) 

**Note**  
When you restore an archive, you are paying for both the archive \(S3 Glacier or S3 Glacier Deep Archive rate\) and a copy that you restored temporarily \(Reduced Redundancy storage rate\)\. For information about pricing, see [Amazon S3 pricing](https://aws.amazon.com/s3/pricing/)\. 

You can restore an object copy programmatically or by using the Amazon S3 console\. Amazon S3 processes only one restore request at a time per object\. For more information, see [Restoring Archived Objects](restoring-objects.md)\.