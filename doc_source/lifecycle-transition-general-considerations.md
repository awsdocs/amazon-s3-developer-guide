# Transitioning Objects<a name="lifecycle-transition-general-considerations"></a>

You can add rules in a lifecycle configuration to tell Amazon S3 to transition objects to another Amazon S3 [storage class](http://docs.aws.amazon.com/AmazonS3/latest/dev/storage-class-intro.html)\. For example:
+ When you know objects are infrequently accessed, you might transition them to the STANDARD\_IA storage class\.
+ You might want to archive objects that you don't need to access in real time to the GLACIER storage class\.

 The following sections describe supported transitions, related constraints, and transitioning to the GLACIER storage class\.

## Supported Transitions and Related Constraints<a name="lifecycle-general-considerations-transition-sc"></a>

In a lifecycle configuration, you can define rules to transition objects from one storage class to another and save on storage class\. The following digram shows supported storage class transitions\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/SupportedTransitionsWaterfallModel.png)

Amazon S3 supports a waterfall model for transitioning between storage class with a lifecycle configuration:
+ From the STANDARD storage classes to STANDARD\_IA or ONEZONE\_IA\. The following constraints apply:

   
  + For larger objects, there is a cost benefit for transitioning to STANDARD\_IA or ONEZONE\_IA\. Amazon S3 does not transition objects that are less than 128 KB to the STANDARD\_IA or ONEZONE\_IA storage classes because it's not cost effective\.

     
  + Objects must be stored at least 30 days in the current storage class before you can transition them to STANDARD\_IA or ONEZONE\_IA\. For example, you cannot create a lifecycle rule to transition objects to the STANDARD\_IA storage class one day after you create them\. 

     

    Amazon S3 doesn't transition objects within the first 30 days because newer objects are often accessed more frequently or deleted sooner than is suitable for STANDARD\_IA or ONEZONE\_IA storage\.

     
  + If you are transitioning noncurrent objects \(in versioned buckets\), you can transition only objects that are at least 30 days noncurrent to STANDARD\_IA or ONEZONE\_IA storage\. 
**Note**  
The preceding diagram does not mention the REDUCED\_REDUNDANCY storage class because we don't recommend using it\. For information about storage classes, see [Storage Classes](storage-class-intro.md)\. 
+ From STANDARD\_IA to ONEZONE\_IA\. The following constraints apply:
  + Objects must be stored at least 30 days in the STANDARD\_IA storage class before you can transition them to the ONEZONE\_IA class\.

     
+ From any storage class to GLACIER\. 

You can combine these lifecycle actions to manage an object's complete lifecycle\.  For example, suppose that the objects you create have a well\-defined lifecycle\. Initially, the objects are frequently accessed for a period of 30 days\. Then, objects are infrequently accessed for up to 90 days\. After that, the objects are no longer needed, so you might choose to archive or delete them\. In this scenario, you can create a lifecycle rule in which you specify the initial transition action to STANDARD\_IA \(or ONEZONE\_IA\) storage, another transition action to GLACIER storage for archiving, and an expiration action\. As you move the objects from one storage class to another, you save on storage cost\. For more information about cost considerations, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

**Note**  
You can't specify a single lifecycle rule for both STANDARD\_IA \(or ONEZONE\_IA\) and GLACIER transitions when the GLACIER transition occurs less than 30 days after the STANDARD\_IA \(or ONEZONE\_IA\) transition\. That's because there is a minimum 30\-day storage charge associated with STANDARD\_IA and ONEZONE\_IA storage classes\. The same 30\-day minimum applies when you specify a transition from STANDARD\_IA storage to ONEZONE\_IA storage\. You can specify two rules to accomplish this, but you pay minimum storage charges\. For more information about cost considerations, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\. 

The following transitions are not supported:
+ You can't transition from the STANDARD\_IA \(or ONEZONE\_IA\) storage class to the STANDARD or REDUCED\_REDUNDANCY classes\.
+ You can't transition from the ONEZONE\_IA storage class to the STANDARD\_IA storage class\.
+ You can't transition from the GLACIER storage class to any other storage class\.
+ You can't transition from any storage class to REDUCED\_REDUNDANCY\.

## Transitioning to the GLACIER Storage Class \(Object Archival\)<a name="before-deciding-to-archive-objects"></a>

Using lifecycle configuration, you can transition objects to the GLACIER storage class—that is, archive data to Amazon Glacier, a lower\-cost storage solution\. 

**Important**  
When you choose the GLACIER storage class, Amazon S3 uses the low\-cost Amazon Glacier service to store the objects\. Although the objects are stored in Amazon Glacier, these remain Amazon S3 objects that you manage in Amazon S3, and you cannot access them directly through Amazon Glacier\.

Before you archive objects, review the following sections for relevant considerations\.

### General Considerations<a name="transition-glacier-general-considerations"></a>

The following are the general considerations for you to consider before you archive objects:
+ Encrypted objects remain encrypted throughout the storage class transition process\.

   
+ Objects in the GLACIER storage class are not available in real time\.

   

  Archived objects are Amazon S3 objects, but before you can access an archived object, you must first restore a temporary copy of it\. The restored object copy is available only for the duration you specify in the restore request\. After that, Amazon S3 deletes the temporary copy, and the object remains archived in Amazon Glacier\. 

   

  You can restore an object by using the Amazon S3 console or programmatically by using the AWS SDKs wrapper libraries or the Amazon S3 REST API in your code\. For more information, see [Restoring Archived Objects](restoring-objects.md)\.

   
+ The transition of objects to the GLACIER storage class is one\-way\.

   

  You cannot use a lifecycle configuration rule to convert the storage class of an object from GLACIER to STANDARD or REDUCED\_REDUNDANCY storage classes\. If you want to change the storage class of an archived object to either STANDARD or REDUCED\_REDUNDANCY, you must use the restore operation to make a temporary copy first\. Then use the copy operation to overwrite the object as a STANDARD, STANDARD\_IA, ONEZONE\_IA, or REDUCED\_REDUNDANCY object\.

   
+ The GLACIER storage class objects are visible and available only through Amazon S3, not through Amazon Glacier\.

   

  Amazon S3 stores the archived objects in Amazon Glacier\. However, these are Amazon S3 objects, and you can access them only by using the Amazon S3 console or the Amazon S3 API\. You cannot access the archived objects through the Amazon Glacier console or the Amazon Glacier API\.

### Cost Considerations<a name="glacier-pricing-considerations"></a>

If you are planning to archive infrequently accessed data for a period of months or years, the GLACIER storage class will usually reduce your storage costs\. You should, however, consider the following in order to ensure that the GLACIER storage class is appropriate for you:
+ **Storage overhead charges** – When you transition objects to the GLACIER storage class, a fixed amount of storage is added to each object to accommodate metadata for managing the object\.

   
  + For each object archived to Amazon Glacier, Amazon S3 uses 8 KB of storage for the name of the object and other metadata\. Amazon S3 stores this metadata so that you can get a real\-time list of your archived objects by using the Amazon S3 API\. For more information, see [Get Bucket \(List Objects\)](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html)\. You are charged standard Amazon S3 rates for this additional storage\.

      
  +  For each archived object, Amazon Glacier adds 32 KB of storage for index and related metadata\. This extra data is necessary to identify and restore your object\. You are charged Amazon Glacier rates for this additional storage\.

     

  If you are archiving small objects, consider these storage charges\. Also consider aggregating many small objects into a smaller number of large objects to reduce overhead costs\.

   
+ **Number of days you plan to keep objects archived**—Amazon Glacier is a long\-term archival solution\. Deleting data that is archived to Amazon Glacier is free if the objects you delete are archived for three months or longer\. If you delete or overwrite an object within three months of archiving it, Amazon S3 charges a prorated early deletion fee\.

    
+ **Glacier archive request charges**— Each object that you transition to the GLACIER storage class constitutes one archive request\. There is a cost for each such request\. If you plan to transition a large number of objects, consider the request costs\. 

   
+ **Glacier data restore charges**—Amazon Glacier is designed for long\-term archival of data that you will access infrequently\. For information on data restoration charges, see [How much does it cost to retrieve data from Glacier?](https://aws.amazon.com/s3/faqs/#How_am_I_charged_for_deleting_objects_from_Amazon_Glacier_that_are_less_than_3_months_old) in the Amazon S3 FAQ\. For information on how to restore data from Glacier, see [Restoring Archived Objects](restoring-objects.md)\. 

When you archive objects to Amazon Glacier by using object lifecycle management, Amazon S3 transitions these objects asynchronously\. There might be a delay between the transition date in the lifecycle configuration rule and the date of the physical transition\. You are charged Amazon Glacier prices based on the transition date specified in the rule\.

The Amazon S3 product detail page provides pricing information and example calculations for archiving Amazon S3 objects\. For more information, see the following topics:
+  [How is my storage charge calculated for Amazon S3 objects archived to Amazon Glacier?](https://aws.amazon.com/s3/faqs/#How_is_my_storage_charge_calculated_for_Amazon_S3_objects_archived_to_Amazon_Glacier) 
+  [How am I charged for deleting objects from Amazon Glacier that are less than 3 months old?](https://aws.amazon.com/s3/faqs/#How_am_I_charged_for_deleting_objects_from_Amazon_Glacier_that_are_less_than_3_months_old) 
+  [ How much does it cost to retrieve data from Glacier?](https://aws.amazon.com/s3/faqs/#How_am_I_charged_for_deleting_objects_from_Amazon_Glacier_that_are_less_than_3_months_old) 
+  [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/) for storage costs for the Standard and `GLACIER` storage classes\. 

### Restoring Archived Objects<a name="restore-glacier-objects-concepts"></a>

Archived objects are not accessible in real time\. You must first initiate a restore request and then wait until a temporary copy of the object is available for the duration that you specify in the request\. After you receive a temporary copy of the restored object, the object's storage class remains GLACIER \(a GET or HEAD request will return `GLACIER` as the storage class\)\. 

**Note**  
When you restore an archive, you are paying for both the archive \(GLACIER rate\) and a copy you restored temporarily \(REDUCED\_REDUNDANCY storage rate\)\. For information about pricing, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\. 

You can restore an object copy programmatically or by using the Amazon S3 console\. Amazon S3 processes only one restore request at a time per object\. For more information, see [Restoring Archived Objects](restoring-objects.md)\.