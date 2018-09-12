# Storage Classes<a name="storage-class-intro"></a>

**Topics**
+ [Storage Classes for Frequently Accessed Objects](#sc-freq-data-access)
+ [Storage Classes for Infrequently Accessed Objects](#sc-infreq-data-access)
+ [GLACIER Storage Class](#sc-glacier)
+ [Storage Classes: Comparing Durability and Availability](#sc-compare)
+ [Setting the Storage Class of an Object](#sc-howtoset)

Each object in Amazon S3 has a storage class associated with it\. For example, if you list all objects in the bucket, the console shows the storage class for all the objects in the list\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/ObjectStorageClass.png)

Amazon S3 offers the following storage classes for the objects that you store\. You choose one depending on your use case scenario and performance access requirements\. All of these storage classes offer high durability\. 

## Storage Classes for Frequently Accessed Objects<a name="sc-freq-data-access"></a>

For performance\-sensitive use cases \(those that require millisecond access time\) and frequently accessed data, Amazon S3 provides the following storage classes:
+ **STANDARD**—The default storage class\. If you don't specify the storage class when you upload an object, Amazon S3 assigns the STANDARD storage class\.

   
+ **REDUCED\_REDUNDANCY**—The Reduced Redundancy Storage \(RRS\) storage class is designed for noncritical, reproducible data that can be stored with less redundancy than the STANDARD storage class\.
**Important**  
We recommend that you not use this storage class\. The STANDARD storage class is more cost effective\. 

  For durability, RRS objects have an average annual expected loss of 0\.01% of objects\. If an RRS object is lost, when requests are made to that object, Amazon S3 returns a 405 error\. 

## Storage Classes for Infrequently Accessed Objects<a name="sc-infreq-data-access"></a>

The **STANDARD\_IA** and **ONEZONE\_IA** storage classes are designed for long\-lived and infrequently accessed data\. \(IA stands for infrequent access\.\) STANDARD\_IA and ONEZONE\_IA objects are available for millisecond access \(similar to the STANDARD storage class\)\. Amazon S3 charges a retrieval fee for these objects, so they are most suitable for infrequently accessed data\. For pricing information, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\. 

For example, you might choose the **STANDARD\_IA** and **ONEZONE\_IA** storage classes:
+ For storing backups\. 
+ For older data that is accessed infrequently, but that still requires millisecond access\. For example, when you upload data, you might choose the STANDARD storage class, and use lifecycle configuration to tell Amazon S3 to transition the objects to the **STANDARD\_IA** or **ONEZONE\_IA** class\. For more information about lifecycle management, see [Object Lifecycle Management](object-lifecycle-mgmt.md)\.

**Note**  
The **STANDARD\_IA** and **ONEZONE\_IA** storage classes are suitable for objects larger than 128 KB that you plan to store for at least 30 days\. If an object is less than 128 KB, Amazon S3 charges you for 128 KB\. If you delete an object before the 30\-day minimum, you are charged for 30 days\. For pricing information, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

These storage classes differ as follows:
+ STANDARD\_IA—Amazon S3 stores the object data redundantly across multiple geographically separated Availability Zones \(similar to STANDARD storage class\)\. STANDARD\_IA objects are resilient to the loss of an Availability Zone\. This storage class offers greater availability, durability, and resiliency than the ONEZONE\_IA class\. 

   
+ ONEZONE\_IA—Amazon S3 stores the object data in only one Availability Zone, which makes it less expensive than STANDARD\_IA\. However, the data is not resilient to the physical loss of the Availability Zone resulting from disasters, such as earth quakes and floods\. The ONEZONE\_IA storage class is as durable as STANDARD\_IA, but it is less available and less resilient\. For a comparison of storage class durability and availability, see the Durability and Availability table at the end of this section\. For pricing, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\. 

We recommend the following:
+ STANDARD\_IA—Use for your primary or only copy of data that can't be recreated\. 
+ ONEZONE\_IA—Use if you can recreate the data if the Availability Zone fails, and for object replicas when setting cross\-region replication \(CRR\)\. 

## GLACIER Storage Class<a name="sc-glacier"></a>

The `GLACIER` storage class is suitable for archiving data where data access is infrequent\. Archived objects are not available for real\-time access\. You must first restore the objects before you can access them\. For more information, see [Restoring Archived Objects](restoring-objects.md)\. The storage class offers same durability, resiliency as the STANDARD storage class\.  

**Important**  
When you choose the GLACIER storage class, Amazon S3 uses the low\-cost Amazon Glacier service to store the objects\. Although the objects are stored in Amazon Glacier, these remain Amazon S3 objects that you manage in Amazon S3, and you cannot access them directly through Amazon Glacier\.

Note the following about the GLACIER storage class:
+ You cannot specify GLACIER as the storage class at the time that you create an object\. You create GLACIER objects by first uploading objects using STANDARD, RRS, STANDARD\_IA, or ONEZONE\_IA as the storage class\. Then you transition these objects to the GLACIER storage class using lifecycle management\. For more information, see [Object Lifecycle Management](object-lifecycle-mgmt.md)\.

   
+ You must first restore the GLACIER objects before you can access them \(STANDARD, RRS, STANDARD\_IA, and ONEZONE\_IA objects are available for anytime access\)\. For more information, [Transitioning to the GLACIER Storage Class \(Object Archival\)](lifecycle-transition-general-considerations.md#before-deciding-to-archive-objects)\.

To learn more about the Amazon Glacier service, see the [Amazon Glacier Developer Guide](http://docs.aws.amazon.com/amazonglacier/latest/dev/)\.

## Storage Classes: Comparing Durability and Availability<a name="sc-compare"></a>

The following table summarizes the durability and availability offered by each of the storage classes\. 


****  

| Storage Class | Durability \(designed for\) | Availability \(designed for\) |  **Other Considerations**  | 
| --- | --- | --- | --- | 
|  STANDARD  |  99\.999999999%   |  99\.99%  |  None  | 
|  STANDARD\_IA  |  99\.999999999%   |  99\.9%  |  There is a retrieval fee for STANDARD\_IA objects\. This class is most suitable for infrequently accessed data\. For pricing information, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.  | 
|  ONEZONE\_IA  |  99\.999999999%   |  99\.5%  |  Not resilient to the loss of the Availability Zone\.  | 
|  GLACIER  |  99\.999999999%   |  99\.99% \(after you restore objects\)  | GLACIER objects are not available for real\-time access\. You must first restore archived objects before you can access them\. For more information, see [Restoring Archived Objects](restoring-objects.md)\. | 
|  RRS  |  99\.99%   |  99\.99%  |  None  | 

All of the storage classes except for ONEZONE\_IA are designed to be resilient to simultaneous complete data loss in a single Availability Zone and partial loss in another Availability Zone\. 

In addition to the performance requirements of your application scenario, consider price\. For storage class pricing, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

## Setting the Storage Class of an Object<a name="sc-howtoset"></a>

Amazon S3 APIs support setting \(or updating\) the storage class of objects as follows:
+ When creating a new object, you can specify its storage class\. For example, when creating objects using the [PUT Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html), [POST Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html), and [Initiate Multipart Upload](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html) APIs, you add the `x-amz-storage-class` request header to specify a storage class\. If you don't add this header, Amazon S3 uses STANDARD, the default storage class\.

   
+ You can also change the storage class of an object that is already stored in Amazon S3 by making a copy of the object using the [PUT Object \- Copy](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html) API\. You copy the object in the same bucket using the same key name and specify request headers as follows:
  + Set the `x-amz-metadata-directive` header to COPY\.
  + Set the `x-amz-storage-class` to the storage class that you want to use\. 

  In a versioning\-enabled bucket, you cannot change the storage class of a specific version of an object\. When you copy it, Amazon S3 gives it a new version ID\.

   
+ You can direct Amazon S3 to change the storage class of objects by adding lifecycle configuration to a bucket\. For more information, see [Object Lifecycle Management](object-lifecycle-mgmt.md)\.

To create and update object storage classes, you can use the Amazon S3 console, AWS SDKs, or the AWS Command Line Interface \(AWS CLI\)\. Each uses the Amazon S3 APIs to send requests to Amazon S3\.