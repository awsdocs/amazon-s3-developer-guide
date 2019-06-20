# Transitioning Objects Using Amazon S3 Lifecycle<a name="lifecycle-transition-general-considerations"></a>

You can add rules in a lifecycle configuration to tell Amazon S3 to transition objects to another Amazon S3 [storage class](https://docs.aws.amazon.com/AmazonS3/latest/dev/storage-class-intro.html)\. For example:
+ When you know that objects are infrequently accessed, you might transition them to the STANDARD\_IA storage class\.
+ You might want to archive objects that you don't need to access in real time to the GLACIER storage class\.

 The following sections describe supported transitions, related constraints, and transitioning to the GLACIER storage class\.

## Supported Transitions and Related Constraints<a name="lifecycle-general-considerations-transition-sc"></a>

In a lifecycle configuration, you can define rules to transition objects from one storage class to another to save on storage costs\. When you don't know the access patterns of your objects, or your access patterns are changing over time, you can transition the objects to the INTELLIGENT\_TIERING storage class for automatic cost savings\. For information about storage classes, see [Amazon S3 Storage Classes](storage-class-intro.md)\. 

Amazon S3 supports a waterfall model for transitioning between storage classes, as shown in the following diagram\. 

![\[Amazon S3 storage class waterfall graphic.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/SupportedTransitionsWaterfallModel.png)

**Note**  
The diagram does not mention the REDUCED\_REDUNDANCY storage class because we don't recommend using it\. 

Amazon S3 supports the following lifecycle transitions between storage classes using a lifecycle configuration:
+ You can transition from the STANDARD storage class to any other storage class\.
+ You can transition from any storage class to the GLACIER or DEEP\_ARCHIVE storage classes\. 
+ You can transition from the STANDARD\_IA storage class to the INTELLIGENT\_TIERING or ONEZONE\_IA storage classes\.
+ You can transition from the INTELLIGENT\_TIERING storage class to the ONEZONE\_IA storage class\.
+ You can transition from the GLACIER storage class to the DEEP\_ARCHIVE storage class\.

The following lifecycle transitions are not supported:
+ You can't transition from any storage class to the STANDARD storage class\.
+ You can't transition from any storage class to the REDUCED\_REDUNDANCY storage class\.
+ You can't transition from the INTELLIGENT\_TIERING storage class to the STANDARD\_IA storage class\.
+ You can't transition from the ONEZONE\_IA storage class to the STANDARD\_IA or INTELLIGENT\_TIERING storage classes\.
+ You can transition from the GLACIER storage class to the DEEP\_ARCHIVE storage class only\.
+ You can't transition from the DEEP\_ARCHIVE storage class to any other storage class\.

The lifecycle storage class transitions have the following constraints:
+ From the STANDARD or STANDARD\_IA storage class to INTELLIGENT\_TIERING\. The following constraints apply:
  + For larger objects, there is a cost benefit for transitioning to INTELLIGENT\_TIERING\. Amazon S3 does not transition objects that are smaller than 128 KB to the INTELLIGENT\_TIERING storage class because it's not cost effective\.

     
+ From the STANDARD storage classes to STANDARD\_IA or ONEZONE\_IA\. The following constraints apply:

   
  + For larger objects, there is a cost benefit for transitioning to STANDARD\_IA or ONEZONE\_IA\. Amazon S3 does not transition objects that are smaller than 128 KB to the STANDARD\_IA or ONEZONE\_IA storage classes because it's not cost effective\.

     
  + Objects must be stored at least 30 days in the current storage class before you can transition them to STANDARD\_IA or ONEZONE\_IA\. For example, you cannot create a lifecycle rule to transition objects to the STANDARD\_IA storage class one day after you create them\. 

     

    Amazon S3 doesn't transition objects within the first 30 days because newer objects are often accessed more frequently or deleted sooner than is suitable for STANDARD\_IA or ONEZONE\_IA storage\.

     
  + If you are transitioning noncurrent objects \(in versioned buckets\), you can transition only objects that are at least 30 days noncurrent to STANDARD\_IA or ONEZONE\_IA storage\. 

     
+ From the STANDARD\_IA storage class to ONEZONE\_IA\. The following constraints apply:
  + Objects must be stored at least 30 days in the STANDARD\_IA storage class before you can transition them to the ONEZONE\_IA class\.

     

You can combine these lifecycle actions to manage an object's complete lifecycle\.  For example, suppose that the objects you create have a well\-defined lifecycle\. Initially, the objects are frequently accessed for a period of 30 days\. Then, objects are infrequently accessed for up to 90 days\. After that, the objects are no longer needed, so you might choose to archive or delete them\. 

In this scenario, you can create a lifecycle rule in which you specify the initial transition action to INTELLIGENT\_TIERING, STANDARD\_IA, or ONEZONE\_IA storage, another transition action to GLACIER storage for archiving, and an expiration action\. As you move the objects from one storage class to another, you save on storage cost\. For more information about cost considerations, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

**Important**  
You can't specify a single lifecycle rule for both INTELLIGENT\_TIERING \(or STANDARD\_IA or ONEZONE\_IA\) and GLACIER or DEEP\_ARCHIVE transitions when the GLACIER or DEEP\_ARCHIVE transition occurs less than 30 days after the INTELLIGENT\_TIERING, STANDARD\_IA, or ONEZONE\_IA transition\. That's because there is a minimum 30\-day storage charge associated with the INTELLIGENT\_TIERING, STANDARD\_IA, and ONEZONE\_IA storage classes\.   
The same 30\-day minimum applies when you specify a transition from STANDARD\_IA storage to ONEZONE\_IA or INTELLIGENT\_TIERING storage\. You can specify two rules to accomplish this, but you pay minimum storage charges\. For more information about cost considerations, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\. 

## Transitioning to the GLACIER and DEEP ARCHIVE Storage Classes \(Object Archival\)<a name="before-deciding-to-archive-objects"></a>

Using lifecycle configuration, you can transition objects to the GLACIER or DEEP\_ARCHIVE storage classes for archiving\. When you choose the GLACIER or DEEP\_ARCHIVE storage class, your objects remain in Amazon S3\. You cannot access them directly through the separate Amazon S3 Glacier service\. 

Before you archive objects, review the following sections for relevant considerations\.

### General Considerations<a name="transition-glacier-general-considerations"></a>

The following are the general considerations for you to consider before you archive objects:
+ Encrypted objects remain encrypted throughout the storage class transition process\.

   
+ Objects that are stored in the GLACIER or DEEP\_ARCHIVE storage classes are not available in real time\.

   

  Archived objects are Amazon S3 objects, but before you can access an archived object, you must first restore a temporary copy of it\. The restored object copy is available only for the duration you specify in the restore request\. After that, Amazon S3 deletes the temporary copy, and the object remains archived in Amazon S3 Glacier\. 

   

  You can restore an object by using the Amazon S3 console or programmatically by using the AWS SDKs wrapper libraries or the Amazon S3 REST API in your code\. For more information, see [Restoring Archived Objects](restoring-objects.md)\.

   
+ Objects that are stored in the GLACIER storage class can only be transitioned to the DEEP\_ARCHIVE storage class\.

   

  You can use a lifecycle configuration rule to convert the storage class of an object from GLACIER to the DEEP\_ARCHIVE storage class only\. If you want to change the storage class of an object that is stored in GLACIER to a storage class other than DEEP\_ARCHIVE, you must use the restore operation to make a temporary copy of the object first\. Then use the copy operation to overwrite the object specifying STANDARD, INTELLIGENT\_TIERING, STANDARD\_IA, ONEZONE\_IA, or REDUCED\_REDUNDANCY as the storage class\.

   
+ The transition of objects to the DEEP\_ARCHIVE storage class can go only one way\.

   

  You cannot use a lifecycle configuration rule to convert the storage class of an object from DEEP\_ARCHIVE to any other storage class\. If you want to change the storage class of an archived object to another storage class, you must use the restore operation to make a temporary copy of the object first\. Then use the copy operation to overwrite the object specifying STANDARD, INTELLIGENT\_TIERING, STANDARD\_IA, ONEZONE\_IA, GLACIER, or REDUCED\_REDUNDANCY as the storage class\.

   
+ The objects that are stored in the GLACIER and DEEP\_ARCHIVE storage classes are visible and available only through Amazon S3\. They are not available through the separate Amazon S3 Glacier service\.

   

  These are Amazon S3 objects, and you can access them only by using the Amazon S3 console or the Amazon S3 API\. You cannot access the archived objects through the separate Amazon S3 Glacier console or the Amazon S3 Glacier API\.

### Cost Considerations<a name="glacier-pricing-considerations"></a>

If you are planning to archive infrequently accessed data for a period of months or years, the GLACIER and DEEP\_ARCHIVE storage classes can reduce your storage costs\. However, to ensure that the GLACIER or DEEP\_ARCHIVE storage class is appropriate for you, consider the following:
+ **Storage overhead charges** – When you transition objects to the GLACIER or DEEP\_ARCHIVE storage class, a fixed amount of storage is added to each object to accommodate metadata for managing the object\.

   
  + For each object archived to GLACIER or DEEP\_ARCHIVE, Amazon S3 uses 8 KB of storage for the name of the object and other metadata\. Amazon S3 stores this metadata so that you can get a real\-time list of your archived objects by using the Amazon S3 API\. For more information, see [Get Bucket \(List Objects\)](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html)\. You are charged Amazon S3 STANDARD rates for this additional storage\.

      
  +  For each object that is archived to GLACIER or DEEP\_ARCHIVE, Amazon S3 adds 32 KB of storage for index and related metadata\. This extra data is necessary to identify and restore your object\. You are charged GLACIER or DEEP\_ARCHIVE rates for this additional storage\.

     

  If you are archiving small objects, consider these storage charges\. Also consider aggregating many small objects into a smaller number of large objects to reduce overhead costs\.

   
+ **Number of days you plan to keep objects archived**—GLACIER and DEEP\_ARCHIVE are long\-term archival solutions\. The minimal storage duration period is 90 days for the GLACIER storage class and 180 days for DEEP\_ARCHIVE\. Deleting data that is archived to Amazon S3 Glacier is free if the objects you delete are archived for more than the minimal storage duration period\. If you delete or overwrite an archived object within the minimal duration period, Amazon S3 charges a prorated early deletion fee\.

    
+ **Amazon S3 GLACIER and DEEP\_ARCHIVE transition request charges**— Each object that you transition to the GLACIER or DEEP\_ARCHIVE storage class constitutes one transition request\. There is a cost for each such request\. If you plan to transition a large number of objects, consider the request costs\. If you are archiving small objects, consider aggregating many small objects into a smaller number of large objects to reduce transition request costs\.

   
+ **Amazon S3 GLACIER and DEEP\_ARCHIVE data restore charges**—GLACIER and DEEP\_ARCHIVE are designed for long\-term archival of data that you access infrequently\. For information about data restoration charges, see [How much does it cost to retrieve data from Amazon S3 Glacier?](https://aws.amazon.com/s3/faqs/#How_am_I_charged_for_deleting_objects_from_Amazon_Glacier_that_are_less_than_3_months_old) in the Amazon S3 FAQ\. For information about how to restore data from Amazon S3 Glacier, see [Restoring Archived Objects](restoring-objects.md)\. 

When you archive objects to Amazon S3 Glacier by using object lifecycle management, Amazon S3 transitions these objects asynchronously\. There might be a delay between the transition date in the lifecycle configuration rule and the date of the physical transition\. You are charged Amazon S3 Glacier prices based on the transition date specified in the rule\.

The Amazon S3 product detail page provides pricing information and example calculations for archiving Amazon S3 objects\. For more information, see the following topics:
+  [How is my storage charge calculated for Amazon S3 objects archived to Amazon S3 Glacier?](https://aws.amazon.com/s3/faqs/#How_is_my_storage_charge_calculated_for_Amazon_S3_objects_archived_to_Amazon_Glacier) 
+  [How am I charged for deleting objects from Amazon S3 Glacier that are less than 3 months old?](https://aws.amazon.com/s3/faqs/#How_am_I_charged_for_deleting_objects_from_Amazon_Glacier_that_are_less_than_3_months_old) 
+  [ How much does it cost to retrieve data from Amazon S3 Glacier?](https://aws.amazon.com/s3/faqs/#How_am_I_charged_for_deleting_objects_from_Amazon_Glacier_that_are_less_than_3_months_old) 
+  [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/) for storage costs for the different storage classes\. 

### Restoring Archived Objects<a name="restore-glacier-objects-concepts"></a>

Archived objects are not accessible in real time\. You must first initiate a restore request and then wait until a temporary copy of the object is available for the duration that you specify in the request\. After you receive a temporary copy of the restored object, the object's storage class remains GLACIER or DEEP\_ARCHIVE\. \(A [HEAD Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectHEAD.html) or [GET Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html) API operation request will return GLACIER or DEEP\_ARCHIVE as the storage class\.\) 

**Note**  
When you restore an archive, you are paying for both the archive \(GLACIER or DEEP\_ARCHIVE rate\) and a copy that you restored temporarily \(REDUCED\_REDUNDANCY storage rate\)\. For information about pricing, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\. 

You can restore an object copy programmatically or by using the Amazon S3 console\. Amazon S3 processes only one restore request at a time per object\. For more information, see [Restoring Archived Objects](restoring-objects.md)\.