# Transitioning Objects: General Considerations<a name="lifecycle-transition-general-considerations"></a>

You can add rules in a lifecycle configuration to direct Amazon S3 to transition objects to another Amazon S3 [storage class](http://docs.aws.amazon.com/AmazonS3/latest/dev/storage-class-intro.html)\. For example:

+ You might transition objects to the STANDARD\_IA storage class when you know those objects are infrequently accessed\.

+ You might want to archive objects that don't need real\-time access to the GLACIER storage class\.

 The following sections describe supported transitions, related constraints, and transitioning to the GLACIER storage class\.

## Supported Transitions and Related Constraints<a name="lifecycle-general-considerations-transition-sc"></a>

In a lifecycle configuration you can define rules to transition objects from one storage class to another\. The following are supported transitions:

+ From the STANDARD or REDUCED\_REDUNDANCY storage classes to STANDARD\_IA\. The following constraints apply:

   

  + Cost benefits of transitioning to STANDARD\_IA can be realized for larger objects\. Amazon S3 does not transition objects less than 128 Kilobytes in size to the STANDARD\_IA storage class\. For smaller objects it is not cost effective and Amazon S3 will not transition them\.

     

  + Objects must be stored at least 30 days in the current storage class before you can transition them to STANDARD\_IA\. For example, you cannot create a lifecycle rule to transition objects to the STANDARD\_IA storage class one day after creation\. 

     

    Transitions before the first 30 days are not supported because often newer objects are accessed more frequently or deleted sooner than is suitable for STANDARD\_IA\.

     

  + If you are transitioning noncurrent objects \(versioned bucket scenario\), you can transition to STANDARD\_IA only objects that are at least 30 days noncurrent\. 

     

+ From any storage class to GLACIER\. 

You can combine these lifecycle rules to manage an object's complete lifecycle, including a first transition to STANDARD\_IA, a second transition to GLACIER for archival, and an expiration\.

For example, suppose the objects you create have a well\-defined lifecycle\. Initially the objects are frequently accessed for a period of 30 days\. After the initial period, the frequency of access diminishes where objects are infrequently accessed for up to 90 days\. After that, the objects are no longer needed\. You may choose to archive or delete them\. You can use a lifecycle configuration to define the transition and expiration of objects that matches this example scenario \(transition to STANDARD\_IA 30 days after creation and transition to GLACIER 90 days after creation, and perhaps expire them after a certain number of days\)\. As you tier down the object's storage class in the transition, you can benefit from the storage cost savings\. For more information about cost considerations, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

**Note**  
When configuring lifecycle, the API doesn't allow you to create a lifecycle policy in which you specify both the STANDARD\_IA and GLACIER transitions where the GLACIER transition occurs less than 30 days after the STANDARD\_IA transition\. This kind of lifecycle policy may increase costs because of the minimum 30 day storage charge associated with the STANDARD\_IA storage class\. For more information about cost considerations, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

You can think of lifecycle transitions as supporting [storage class tiers](http://docs.aws.amazon.com/AmazonS3/latest/dev/storage-class-intro.html), which offer different costs and benefits\. You may choose to transition an object to another storage class in the object's lifetime for cost saving considerations—lifecycle configuration enables you to do that\. For example, to manage storage costs, you might configure lifecycle to change an object's storage class from the STANDARD, which is the most available and durable storage class, to the STANDARD\_IA \(IA, for infrequent access\), and then to the GLACIER storage class \(where the objects are archived and only available after you restore them\)\. These transitions can lower your storage costs\.

The following transitions are not supported:

+ You can't transition from STANDARD\_IA storage class to the STANDARD or REDUCED\_REDUNDANCY classes\.

+ You can't transition from GLACIER to any other storage class\.

+ You can't transition from any storage class to REDUCED\_REDUNDANCY\.

## Transitioning to the GLACIER Storage Class \(Object Archival\)<a name="before-deciding-to-archive-objects"></a>

Using lifecycle configuration, you can transition objects to the GLACIER storage class—that is, archive data to Amazon Glacier, a lower\-cost storage solution\. Before you archive objects, review the following sections for relevant considerations\.

### General Considerations<a name="transition-glacier-general-considerations"></a>

The following are the general considerations for you to consider before you archive objects:

+ Encrypted objects remain encrypted throughout the storage class transition process\.

   

+ Objects in the GLACIER storage class are not available in real time\.

   

  Archived objects are Amazon S3 objects, but before you can access an archived object, you must first restore a temporary copy of it\. The restored object copy is available only for the duration you specify in the restore request\. After that, Amazon S3 deletes the temporary copy, and the object remains archived in Amazon Glacier\. 

   

  Note that object restoration from an archive can take up to five hours\. 

   

  You can restore an object by using the Amazon S3 console or programmatically by using the AWS SDKs wrapper libraries or the Amazon S3 REST API in your code\. For more information, see [POST Object restore](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOSTrestore.html)\.

   

+ The transition of objects to the GLACIER storage class is one\-way\.

   

  You cannot use a lifecycle configuration rule to convert the storage class of an object from GLACIER to STANDARD or REDUCED\_REDUNDANCY\. If you want to change the storage class of an already archived object to either STANDARD or REDUCED\_REDUNDANCY, you must use the restore operation to make a temporary copy first\. Then use the copy operation to overwrite the object as a STANDARD, STANDARD\_IA, or REDUCED\_REDUNDANCY object\.

   

+ The GLACIER storage class objects are visible and available only through Amazon S3, not through Amazon Glacier\.

   

  Amazon S3 stores the archived objects in Amazon Glacier\. However, these are Amazon S3 objects, and you can access them only by using the Amazon S3 console or the Amazon S3 API\. You cannot access the archived objects through the Amazon Glacier console or the Amazon Glacier API\.

### Cost Considerations<a name="glacier-pricing-considerations"></a>

If you are planning to archive infrequently accessed data for a period of months or years, the GLACIER storage class will usually reduce your storage costs\. You should, however, consider the following in order to ensure that the GLACIER storage class is appropriate for you:

+ **Storage overhead charges** – When you transition objects to the GLACIER storage class, a fixed amount of storage is added to each object to accommodate metadata for managing the object\.

   

  + For each object archived to Amazon Glacier, Amazon S3 uses 8 KB of storage for the name of the object and other metadata\. Amazon S3 stores this metadata so that you can get a real\-time list of your archived objects by using the Amazon S3 API\. For more information, see [Get Bucket \(List Objects\)](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html)\. You are charged standard Amazon S3 rates for this additional storage\.

      

  +  For each archived object, Amazon Glacier adds 32 KB of storage for index and related metadata\. This extra data is necessary to identify and restore your object\. You are charged Amazon Glacier rates for this additional storage\.

     

   If you are archiving small objects, consider these storage charges\. Also consider aggregating a large number of small objects into a smaller number of large objects in order to reduce overhead costs\.

   

+ **Number of days you plan to keep objects archived** – Amazon Glacier is a long\-term archival solution\. Deleting data that is archived to Amazon Glacier is free if the objects you delete are archived for three months or longer\. If you delete or overwrite an object within three months of archiving it, Amazon S3 charges a prorated early deletion fee\.

    

+ **Glacier archive request charges** – Each object that you transition to the GLACIER storage class constitutes one archive request\. There is a cost for each such request\. If you plan to transition a large number of objects, consider the request costs\. 

   

+ **Glacier data restore charges** – Amazon Glacier is designed for long\-term archival of data that you will access infrequently\. For information on data restoration charges, see [How much does it cost to retrieve data from Glacier?](https://aws.amazon.com/s3/faqs/#How_am_I_charged_for_deleting_objects_from_Amazon_Glacier_that_are_less_than_3_months_old) in the Amazon S3 FAQ\. For information on how to restore data from Glacier, see [Restoring Archived Objects](restoring-objects.md)\. 

When you archive objects to Amazon Glacier by using object lifecycle management, Amazon S3 transitions these objects asynchronously\. There may be a delay between the transition date in the lifecycle configuration rule and the date of the physical transition\. You are charged Amazon Glacier prices based on the transition date specified in the rule\.

The Amazon S3 product detail page provides pricing information and example calculations for archiving Amazon S3 objects\. For more information, see the following topics:

+  [How is my storage charge calculated for Amazon S3 objects archived to Amazon Glacier?](https://aws.amazon.com/s3/faqs/#How_is_my_storage_charge_calculated_for_Amazon_S3_objects_archived_to_Amazon_Glacier) 

+  [How am I charged for deleting objects from Amazon Glacier that are less than 3 months old?](https://aws.amazon.com/s3/faqs/#How_am_I_charged_for_deleting_objects_from_Amazon_Glacier_that_are_less_than_3_months_old) 

+  [ How much does it cost to retrieve data from Glacier?](https://aws.amazon.com/s3/faqs/#How_am_I_charged_for_deleting_objects_from_Amazon_Glacier_that_are_less_than_3_months_old) 

+  [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/) for storage costs for the Standard and `GLACIER` storage classes\. This page also provides Glacier Archive Request costs\. 

### Restoring Archived Objects<a name="restore-glacier-objects-concepts"></a>

Archived objects are not accessible in real time\. You must first initiate a restore request and then wait until a temporary copy of the object is available for the duration that you specify in the request\. Restore jobs are typically completed in three to five hours, so it is important that you archive only objects that you will not need to access in real time\. 

After you receive a temporary copy of the restored object, the object's storage class remains GLACIER \(a GET or HEAD request will return `GLACIER` as the storage class\)\. 

**Note**  
Note that when you restore an archive you are paying for both the archive \(GLACIER rate\) and a copy you restored temporarily \(REDUCED\_REDUNDANCY storage rate\)\. For information about pricing, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\. 

You can restore an object copy programmatically or by using the Amazon S3 console\. Amazon S3 processes only one restore request at a time per object\. For more information, see [Restoring Archived Objects](restoring-objects.md)\.