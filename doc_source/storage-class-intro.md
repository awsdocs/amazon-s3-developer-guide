# Amazon S3 storage classes<a name="storage-class-intro"></a>

Each object in Amazon S3 has a storage class associated with it\. For example, if you list the objects in an S3 bucket, the console shows the storage class for all the objects in the list\.

![\[Shows example of storage classes in the Amazon S3 console.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/ObjectStorageClass.png)

Amazon S3 offers a range of storage classes for the objects that you store\. You choose a class depending on your use case scenario and performance access requirements\. All of these storage classes offer high durability\. 

**Topics**
+ [Storage classes for frequently accessed objects](#sc-freq-data-access)
+ [Storage class for automatically optimizing frequently and infrequently accessed objects](#sc-dynamic-data-access)
+ [Storage classes for infrequently accessed objects](#sc-infreq-data-access)
+ [Storage classes for archiving objects](#sc-glacier)
+ [Comparing the Amazon S3 storage classes](#sc-compare)
+ [Setting the storage class of an object](#sc-howtoset)

## Storage classes for frequently accessed objects<a name="sc-freq-data-access"></a>

For performance\-sensitive use cases \(those that require millisecond access time\) and frequently accessed data, Amazon S3 provides the following storage classes:
+ **S3 Standard**—The default storage class\. If you don't specify the storage class when you upload an object, Amazon S3 assigns the S3 Standard storage class\.
+ **Reduced Redundancy**—The Reduced Redundancy Storage \(RRS\) storage class is designed for noncritical, reproducible data that can be stored with less redundancy than the S3 Standard storage class\.
**Important**  
We recommend that you not use this storage class\. The S3 Standard storage class is more cost effective\. 

  For durability, RRS objects have an average annual expected loss of 0\.01 percent of objects\. If an RRS object is lost, when requests are made to that object, Amazon S3 returns a 405 error\.

## Storage class for automatically optimizing frequently and infrequently accessed objects<a name="sc-dynamic-data-access"></a>

The **S3 Intelligent\-Tiering** storage class is designed to optimize storage costs by automatically moving data to the most cost\-effective storage access tier, without performance impact or operational overhead\. S3 Intelligent\-Tiering delivers automatic cost savings by moving data on a granular object level between two access tiers, a frequent access tier and a lower\-cost infrequent access tier, when access patterns change\. The Intelligent\-Tiering storage class is ideal if you want to optimize storage costs automatically for long\-lived data when access patterns are unknown or unpredictable\.

The S3 Intelligent\-Tiering storage class stores objects in two access tiers: one tier that is optimized for frequent access and another lower\-cost tier that is optimized for infrequently accessed data\. For a small monthly monitoring and automation fee per object, Amazon S3 monitors access patterns of the objects in the S3 Intelligent\-Tiering storage class and moves objects that have not been accessed for 30 consecutive days to the infrequent access tier\. 

There are no retrieval fees when using the S3 Intelligent\-Tiering storage class\. If an object in the infrequent access tier is accessed, it is automatically moved back to the frequent access tier\. No additional tiering fees apply when objects are moved between access tiers within the S3 Intelligent\-Tiering storage class\. 

**Note**  
The S3 Intelligent\-Tiering storage class is suitable for objects larger than 128 KB that you plan to store for at least 30 days\. If the size of an object is less than 128 KB, it is not eligible for auto\-tiering\. Smaller objects can be stored, but they are always charged at the frequent access tier rates in the S3 Intelligent\-Tiering storage class\.   
If you delete an object before the end of the 30\-day minimum storage duration period, you are charged for 30 days\. For pricing information, see [Amazon S3 pricing](https://aws.amazon.com/s3/pricing/)\.

## Storage classes for infrequently accessed objects<a name="sc-infreq-data-access"></a>

The **S3 Standard\-IA** and **S3 One Zone\-IA** storage classes are designed for long\-lived and infrequently accessed data\. \(IA stands for *infrequent access*\.\) S3 Standard\-IA and S3 One Zone\-IA objects are available for millisecond access \(same as the S3 Standard storage class\)\. Amazon S3 charges a retrieval fee for these objects, so they are most suitable for infrequently accessed data\. For pricing information, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\. 

For example, you might choose the S3 Standard\-IA and S3 One Zone\-IA storage classes:
+ For storing backups\. 
+ For older data that is accessed infrequently, but that still requires millisecond access\. For example, when you upload data, you might choose the S3 Standard storage class, and use lifecycle configuration to tell Amazon S3 to transition the objects to the S3 Standard\-IA or S3 One Zone\-IA class\. 

  For more information about lifecycle management, see [Object lifecycle management](object-lifecycle-mgmt.md)\.

**Note**  
The S3 Standard\-IA and S3 One Zone\-IA storage classes are suitable for objects larger than 128 KB that you plan to store for at least 30 days\. If an object is less than 128 KB, Amazon S3 charges you for 128 KB\. If you delete an object before the end of the 30\-day minimum storage duration period, you are charged for 30 days\. For pricing information, see [Amazon S3 pricing](https://aws.amazon.com/s3/pricing/)\.

These storage classes differ as follows:
+ S3 Standard\-IA—Amazon S3 stores the object data redundantly across multiple geographically separated Availability Zones \(similar to the S3 Standard storage class\)\. S3 Standard\-IA objects are resilient to the loss of an Availability Zone\. This storage class offers greater availability and resiliency than the S3 One Zone\-IA class\. 
+ S3 One Zone\-IA—Amazon S3 stores the object data in only one Availability Zone, which makes it less expensive than S3 Standard\-IA\. However, the data is not resilient to the physical loss of the Availability Zone resulting from disasters, such as earthquakes and floods\. The S3 One Zone\-IA storage class is as durable as Standard\-IA, but it is less available and less resilient\. For a comparison of storage class durability and availability, see the Durability and Availability table at the end of this section\. For pricing, see [Amazon S3 pricing](https://aws.amazon.com/s3/pricing/)\. 

We recommend the following:
+ S3 Standard\-IA—Use for your primary or only copy of data that can't be re\-created\. 
+ S3 One Zone\-IA—Use if you can re\-create the data if the Availability Zone fails, and for object replicas when setting S3 Cross\-Region Replication \(CRR\)\. 

## Storage classes for archiving objects<a name="sc-glacier"></a>

The **S3 Glacier** and **S3 Glacier Deep Archive** storage classes are designed for low\-cost data archiving\. These storage classes offer the same durability and resiliency as the S3 Standard storage class\. For a comparison of storage class durability and availability, see the table at the end of this section\.

These storage classes differ as follows:
+ **S3 Glacier**—Use for archives where portions of the data might need to be retrieved in minutes\. Data stored in the S3 Glacier storage class has a minimum storage duration period of 90 days and can be accessed in as little as 1\-5 minutes using expedited retrieval\. If you have deleted, overwritten, or transitioned to a different storage class an object before the 90\-day minimum, you are charged for 90 days\. For pricing information, see [Amazon S3 pricing](https://aws.amazon.com/s3/pricing/)\. 
+ **S3 Glacier Deep Archive**—Use for archiving data that rarely needs to be accessed\. Data stored in the S3 Glacier Deep Archive storage class has a minimum storage duration period of 180 days and a default retrieval time of 12 hours\. If you have deleted, overwritten, or transitioned to a different storage class an object before the 180\-day minimum, you are charged for 180 days\. For pricing information, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\. 

  S3 Glacier Deep Archive is the lowest cost storage option in AWS\. Storage costs for S3 Glacier Deep Archive are less expensive than using the S3 Glacier storage class\. You can reduce S3 Glacier Deep Archive retrieval costs by using bulk retrieval, which returns data within 48 hours\. 

### Retrieving archived objects<a name="sc-glacier-restore"></a>

You can set the storage class of an object to S3 Glacier or S3 Glacier Deep Archive in the same ways that you do for the other storage classes as described in the section [Setting the storage class of an object](#sc-howtoset)\. However, the S3 Glacier and S3 Glacier Deep Archive objects are not available for real\-time access\. You must first restore the S3 Glacier and S3 Glacier Deep Archive objects before you can access them\. \(S3 Standard, RRS, S3 Standard\-IA, S3 One Zone\-IA, and S3 Intelligent\-Tiering objects are available for anytime access\.\) For more information about retrieving archived objects, see [Restoring Archived Objects](restoring-objects.md)\.

**Important**  
When you choose the S3 Glacier or S3 Glacier Deep Archive storage class, your objects remain in Amazon S3\. You cannot access them directly through the separate Amazon S3 Glacier service\. 

To learn more about the Amazon S3 Glacier service, see the [Amazon S3 Glacier Developer Guide](https://docs.aws.amazon.com/amazonglacier/latest/dev/)\.

## Comparing the Amazon S3 storage classes<a name="sc-compare"></a>

The following table compares the storage classes\. 




****  

| Storage class | Designed for | Durability \(designed for\) | Availability \(designed for\) | Availability Zones | Min storage duration | Min billable object size | Other considerations  | 
| --- | --- | --- | --- | --- | --- | --- | --- | 
|  S3 Standard  |  Frequently accessed data  |  99\.999999999%   |  99\.99%  |  >= 3  |  None  |  None  |  None  | 
|  S3 Standard\-IA  |  Long\-lived, infrequently accessed data  |  99\.999999999%   |  99\.9%  |  >= 3  |  30 days  |  128 KB  |  Per GB retrieval fees apply\.   | 
|  S3 Intelligent\-Tiering  |  Long\-lived data with changing or unknown access patterns  |  99\.999999999%  |  99\.9%  |  >= 3  |  30 days  |  None  |  Monitoring and automation fees per object apply\. No retrieval fees\.  | 
|  S3 One Zone\-IA  |  Long\-lived, infrequently accessed, non\-critical data  |  99\.999999999%   |  99\.5%  |  1  |  30 days  |  128 KB  |  Per GB retrieval fees apply\. Not resilient to the loss of the Availability Zone\.  | 
|  S3 Glacier  | Long\-term data archiving with retrieval times ranging from minutes to hours | 99\.999999999%  |  99\.99% \(after you restore objects\)  |  >= 3  |  90 days  |  40 KB  | Per GB retrieval fees apply\. You must first restore archived objects before you can access them\. For more information, see [Restoring Archived Objects](restoring-objects.md)\. | 
|  S3 Glacier Deep Archive  | Archiving rarely accessed data with a default retrieval time of 12 hours | 99\.999999999%  |  99\.99% \(after you restore objects\)  |  >= 3  |  180 days  |  40 KB  | Per GB retrieval fees apply\. You must first restore archived objects before you can access them\. For more information, see [Restoring Archived Objects](restoring-objects.md)\. | 
|  RRS \(Not recommended\)  |  Frequently accessed, non\-critical data  |  99\.99%   |  99\.99%  |  >= 3  |  None  |  None  |  None  | 

All of the storage classes except for S3 One Zone\-IA are designed to be resilient to simultaneous complete data loss in a single Availability Zone and partial loss in another Availability Zone\. 

In addition to the performance requirements of your application scenario, consider price\. For storage class pricing, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

## Setting the storage class of an object<a name="sc-howtoset"></a>

Amazon S3 APIs support setting \(or updating\) the storage class of objects as follows:
+ When creating a new object, you can specify its storage class\. For example, when creating objects using the [PUT Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html), [POST Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html), and [Initiate Multipart Upload](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html) APIs, you add the `x-amz-storage-class` request header to specify a storage class\. If you don't add this header, Amazon S3 uses Standard, the default storage class\.
+ You can also change the storage class of an object that is already stored in Amazon S3 to any other storage class by making a copy of the object using the [PUT Object \- Copy](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html) API\. However, you cannot use [PUT Object \- Copy](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html) to copy objects that are stored in the S3 Glacier or S3 Glacier Deep Archive storage classes\.

  You copy the object in the same bucket using the same key name and specify request headers as follows:
  + Set the `x-amz-metadata-directive` header to COPY\.
  + Set the `x-amz-storage-class` to the storage class that you want to use\. 

  In a versioning\-enabled bucket, you cannot change the storage class of a specific version of an object\. When you copy it, Amazon S3 gives it a new version ID\.
+ You can direct Amazon S3 to change the storage class of objects by adding an S3 Lifecycle configuration to a bucket\. For more information, see [Object lifecycle management](object-lifecycle-mgmt.md)\.
+ When setting up a replication configuration, you can set the storage class for replicated objects to any other storage class\. However, you cannot replicate objects that are stored in the S3 Glacier or S3 Glacier Deep Archive storage classes\. For more information, see [Replication configuration overview](replication-add-config.md)\.

To create and update object storage classes, you can use the Amazon S3 console, AWS SDKs, or the AWS Command Line Interface \(AWS CLI\)\. Each uses the Amazon S3 APIs to send requests to Amazon S3\.

### Restricting Access Policy Permissions to a Specific Storage Class<a name="restricting-storage-class"></a>

When you grant access policy permissions for Amazon S3 operations, you can use the `s3:x-amz-storage-class` condition key to restrict which storage class to use when storing uploaded objects\. For example, when you grant `s3:PUTObject` permission, you can restrict object uploads to a specific storage class\. For an example policy, see [Example 5: Restricting Object Uploads to Objects with a Specific Storage Class](amazon-s3-policy-keys.md#example-storage-class-condition-key)\. For more information about using conditions in policies and a complete list of Amazon S3 condition keys, see the following:
+ [Actions, resources, and condition keys for Amazon S3](list_amazons3.md)
+ [Amazon S3 Condition Keys](amazon-s3-policy-keys.md)