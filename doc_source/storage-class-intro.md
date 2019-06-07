# Amazon S3 Storage Classes<a name="storage-class-intro"></a>

Each object in Amazon S3 has a storage class associated with it\. For example, if you list the objects in an S3 bucket, the console shows the storage class for all the objects in the list\.

![\[Shows example of storage classes in the Amazon S3 console.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/ObjectStorageClass.png)

Amazon S3 offers a range of storage classes for the objects that you store\. You choose a class depending on your use case scenario and performance access requirements\. All of these storage classes offer high durability\. 

**Topics**
+ [Storage Classes for Frequently Accessed Objects](#sc-freq-data-access)
+ [Storage Class That Automatically Optimizes Frequently and Infrequently Accessed Objects](#sc-dynamic-data-access)
+ [Storage Classes for Infrequently Accessed Objects](#sc-infreq-data-access)
+ [Storage Classes for Archiving Objects](#sc-glacier)
+ [Comparing the Amazon S3 Storage Classes](#sc-compare)
+ [Setting the Storage Class of an Object](#sc-howtoset)

## Storage Classes for Frequently Accessed Objects<a name="sc-freq-data-access"></a>

For performance\-sensitive use cases \(those that require millisecond access time\) and frequently accessed data, Amazon S3 provides the following storage classes:
+ **STANDARD**—The default storage class\. If you don't specify the storage class when you upload an object, Amazon S3 assigns the STANDARD storage class\.

   
+ **REDUCED\_REDUNDANCY**—The Reduced Redundancy Storage \(RRS\) storage class is designed for noncritical, reproducible data that can be stored with less redundancy than the STANDARD storage class\.
**Important**  
We recommend that you not use this storage class\. The STANDARD storage class is more cost effective\. 

  For durability, RRS objects have an average annual expected loss of 0\.01% of objects\. If an RRS object is lost, when requests are made to that object, Amazon S3 returns a 405 error\.

## Storage Class That Automatically Optimizes Frequently and Infrequently Accessed Objects<a name="sc-dynamic-data-access"></a>

The **INTELLIGENT\_TIERING** storage class is designed to optimize storage costs by automatically moving data to the most cost\-effective storage access tier, without performance impact or operational overhead\. INTELLIGENT\_TIERING delivers automatic cost savings by moving data on a granular object level between two access tiers, a frequent access tier and a lower\-cost infrequent access tier, when access patterns change\. The INTELLIGENT\_TIERING storage class is ideal if you want to optimize storage costs automatically for long\-lived data when access patterns are unknown or unpredictable\.

The INTELLIGENT\_TIERING storage class stores objects in two access tiers: one tier that is optimized for frequent access and another lower\-cost tier that is optimized for infrequently accessed data\. For a small monthly monitoring and automation fee per object, Amazon S3 monitors access patterns of the objects in the INTELLIGENT\_TIERING storage class and moves objects that have not been accessed for 30 consecutive days to the infrequent access tier\. There are no retrieval fees when using the INTELLIGENT\_TIERING storage class\. If an object in the infrequent access tier is accessed, it is automatically moved back to the frequent access tier\. No additional tiering fees apply when objects are moved between access tiers within the INTELLIGENT\_TIERING storage class\. 

**Note**  
The INTELLIGENT\_TIERING storage class is suitable for objects larger than 128 KB that you plan to store for at least 30 days\. If the size of an object is less than 128 KB, it is not eligible for auto\-tiering\. Smaller objects can be stored, but they are always charged at the frequent access tier rates in the INTELLIGENT\_TIERING storage class\. If you delete an object before the end of the 30\-day minimum storage duration period, you are charged for 30 days\. For pricing information, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

## Storage Classes for Infrequently Accessed Objects<a name="sc-infreq-data-access"></a>

The **STANDARD\_IA** and **ONEZONE\_IA** storage classes are designed for long\-lived and infrequently accessed data\. \(IA stands for infrequent access\.\) STANDARD\_IA and ONEZONE\_IA objects are available for millisecond access \(similar to the STANDARD storage class\)\. Amazon S3 charges a retrieval fee for these objects, so they are most suitable for infrequently accessed data\. For pricing information, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\. 

For example, you might choose the STANDARD\_IA and ONEZONE\_IA storage classes:
+ For storing backups\. 

   
+ For older data that is accessed infrequently, but that still requires millisecond access\. For example, when you upload data, you might choose the STANDARD storage class, and use lifecycle configuration to tell Amazon S3 to transition the objects to the STANDARD\_IA or ONEZONE\_IA class\. For more information about lifecycle management, see [Object Lifecycle Management](object-lifecycle-mgmt.md)\.

**Note**  
The STANDARD\_IA and ONEZONE\_IA storage classes are suitable for objects larger than 128 KB that you plan to store for at least 30 days\. If an object is less than 128 KB, Amazon S3 charges you for 128 KB\. If you delete an object before the end of the 30\-day minimum storage duration period, you are charged for 30 days\. For pricing information, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

These storage classes differ as follows:
+ STANDARD\_IA—Amazon S3 stores the object data redundantly across multiple geographically separated Availability Zones \(similar to the STANDARD storage class\)\. STANDARD\_IA objects are resilient to the loss of an Availability Zone\. This storage class offers greater availability and resiliency than the ONEZONE\_IA class\. 

   
+ ONEZONE\_IA—Amazon S3 stores the object data in only one Availability Zone, which makes it less expensive than STANDARD\_IA\. However, the data is not resilient to the physical loss of the Availability Zone resulting from disasters, such as earth quakes and floods\. The ONEZONE\_IA storage class is as durable as STANDARD\_IA, but it is less available and less resilient\. For a comparison of storage class durability and availability, see the Durability and Availability table at the end of this section\. For pricing, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\. 

We recommend the following:
+ STANDARD\_IA—Use for your primary or only copy of data that can't be recreated\. 
+ ONEZONE\_IA—Use if you can recreate the data if the Availability Zone fails, and for object replicas when setting cross\-region replication \(CRR\)\. 

## Storage Classes for Archiving Objects<a name="sc-glacier"></a>

The **GLACIER** and **DEEP\_ARCHIVE** storage classes are designed for low\-cost data archiving\. These storage classes offer the same durability and resiliency as the STANDARD storage class\. For a comparison of storage class durability and availability, see the Durability and Availability table at the end of this section\.

These storage classes differ as follows:
+ GLACIER—Use for archives where portions of the data might need to be retrieved in minutes\. Data stored in the GLACIER storage class has a minimum storage duration period of 90 days and can be accessed in as little as 1\-5 minutes using expedited retrieval\. If you delete an object before the 90\-day minimum, you are charged for 90 days\. For pricing information, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\. 

   
+ DEEP\_ARCHIVE—Use for archiving data that rarely needs to be accessed\. Data stored in the DEEP\_ARCHIVE storage class has a minimum storage duration period of 180 days and a default retrieval time of 12 hours\. If you delete an object before the 180\-day minimum, you are charged for 180 days\. For pricing information, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\. 

  DEEP\_ARCHIVE is the lowest cost storage option in AWS\. Storage costs for DEEP\_ARCHIVE are less expensive than using the GLACIER storage class\. You can reduce DEEP\_ARCHIVE retrieval costs by using bulk retrieval, which returns data within 48 hours\. 

### Retrieving Archived Objects<a name="sc-glacier-restore"></a>

You can set the storage class of an object to GLACIER or DEEP\_ARCHIVE in the same ways that you do for the other storage classes as described in the section [Setting the Storage Class of an Object](#sc-howtoset)\. However, the GLACIER and DEEP\_ARCHIVE objects are not available for real\-time access\. You must first restore the GLACIER and DEEP\_ARCHIVE objects before you can access them \(STANDARD, RRS, STANDARD\_IA, ONEZONE\_IA, and INTELLIGENT\_TIERING objects are available for anytime access\)\. For more information about retrieving archived objects, see [Restoring Archived Objects](restoring-objects.md)\.

**Important**  
When you choose the GLACIER or DEEP\_ARCHIVE storage class, your objects remain in Amazon S3\. You cannot access them directly through the separate Amazon S3 Glacier service\. 

To learn more about the Amazon S3 Glacier service, see the [Amazon S3 Glacier Developer Guide](https://docs.aws.amazon.com/amazonglacier/latest/dev/)\.

## Comparing the Amazon S3 Storage Classes<a name="sc-compare"></a>

The following table compares the storage classes\. 




****  

| Storage Class | Designed for | Durability \(designed for\) | Availability \(designed for\) | Availability Zones | Min storage duration | Min billable object size | Other Considerations  | 
| --- | --- | --- | --- | --- | --- | --- | --- | 
|  STANDARD  |  Frequently accessed data  |  99\.999999999%   |  99\.99%  |  >= 3  |  None  |  None  |  None  | 
|  STANDARD\_IA  |  Long\-lived, infrequently accessed data  |  99\.999999999%   |  99\.9%  |  >= 3  |  30 days  |  128 KB  |  Per GB retrieval fees apply\.   | 
|  INTELLIGENT\_TIERING  |  Long\-lived data with changing or unknown access patterns  |  99\.999999999%  |  99\.9%  |  >= 3  |  30 days  |  None  |  Monitoring and automation fees per object apply\. No retrieval fees\.  | 
|  ONEZONE\_IA  |  Long\-lived, infrequently accessed, non\-critical data  |  99\.999999999%   |  99\.5%  |  1  |  30 days  |  128 KB  |  Per GB retrieval fees apply\. Not resilient to the loss of the Availability Zone\.  | 
|  GLACIER  | Long\-term data archiving with retrieval times ranging from minutes to hours | 99\.999999999%  |  99\.99% \(after you restore objects\)  |  >= 3  |  90 days  |  None  | Per GB retrieval fees apply\. You must first restore archived objects before you can access them\. For more information, see [Restoring Archived Objects](restoring-objects.md)\. | 
|  DEEP\_ARCHIVE  | Archiving rarely accessed data with a default retrieval time of 12 hours | 99\.999999999%  |  99\.99% \(after you restore objects\)  |  >= 3  |  180 days  |  None  | Per GB retrieval fees apply\. You must first restore archived objects before you can access them\. For more information, see [Restoring Archived Objects](restoring-objects.md)\. | 
|  RRS \(Not recommended\)  |  Frequently accessed, non\-critical data  |  99\.99%   |  99\.99%  |  >= 3  |  None  |  None  |  None  | 

All of the storage classes except for ONEZONE\_IA are designed to be resilient to simultaneous complete data loss in a single Availability Zone and partial loss in another Availability Zone\. 

In addition to the performance requirements of your application scenario, consider price\. For storage class pricing, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

## Setting the Storage Class of an Object<a name="sc-howtoset"></a>

Amazon S3 APIs support setting \(or updating\) the storage class of objects as follows:
+ When creating a new object, you can specify its storage class\. For example, when creating objects using the [PUT Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html), [POST Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html), and [Initiate Multipart Upload](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html) APIs, you add the `x-amz-storage-class` request header to specify a storage class\. If you don't add this header, Amazon S3 uses STANDARD, the default storage class\.

   
+ You can also change the storage class of an object that is already stored in Amazon S3 to any other storage class by making a copy of the object using the [PUT Object \- Copy](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html) API\. However, you cannot use [PUT Object \- Copy](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html) to copy objects that are stored in the GLACIER or DEEP\_ARCHIVE storage classes\.

  You copy the object in the same bucket using the same key name and specify request headers as follows:
  + Set the `x-amz-metadata-directive` header to COPY\.
  + Set the `x-amz-storage-class` to the storage class that you want to use\. 

  In a versioning\-enabled bucket, you cannot change the storage class of a specific version of an object\. When you copy it, Amazon S3 gives it a new version ID\.

   
+ You can direct Amazon S3 to change the storage class of objects by adding a lifecycle configuration to a bucket\. For more information, see [Object Lifecycle Management](object-lifecycle-mgmt.md)\.

   
+ When setting up a cross\-region replication \(CRR\) configuration, you can set the storage class for replicated objects to any other storage class\. However, you cannot replicate objects that are stored in the GLACIER or DEEP\_ARCHIVE storage classes\. For more information, see [Replication Configuration Overview](crr-add-config.md)\.

To create and update object storage classes, you can use the Amazon S3 console, AWS SDKs, or the AWS Command Line Interface \(AWS CLI\)\. Each uses the Amazon S3 APIs to send requests to Amazon S3\.