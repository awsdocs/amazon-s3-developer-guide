# Storage Classes<a name="storage-class-intro"></a>

Each object in Amazon S3 has a storage class associated with it\. For example, if you list all objects in the bucket, the console shows the storage class for all the objects in the list\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/ObjectStorageClass.png)

Amazon S3 offers the following storage classes for the objects that you store\. You choose one depending on your use case scenario and performance access requirements\. All of these storage classes offer high durability: 

+ **STANDARD** – This storage class is ideal for performance\-sensitive use cases and frequently accessed data\. 

  STANDARD is the default storage class; if you don't specify storage class at the time that you upload an object, Amazon S3 assumes the STANDARD storage class\.

+ **STANDARD\_IA** – This storage class \(IA, for infrequent access\) is optimized for long\-lived and less frequently accessed data, for example backups and older data where frequency of access has diminished, but the use case still demands high performance\. 
**Note**  
There is a retrieval fee associated with STANDARD\_IA objects which makes it most suitable for infrequently accessed data\. For pricing information, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

  For example, initially you might upload objects using the STANDARD storage class, and then use a bucket lifecycle configuration rule to transition objects \(see [Object Lifecycle Management](object-lifecycle-mgmt.md)\) to the STANDARD\_IA \(or GLACIER\) storage class at some point in the object's lifetime\. For more information about lifecycle management, see [Object Lifecycle Management](object-lifecycle-mgmt.md)\.

  The STANDARD\_IA objects are available for real\-time access\. The table at the end of this section highlights some of the differences in these storage classes\.

  The STANDARD\_IA storage class is suitable for larger objects greater than 128 Kilobytes that you want to keep for at least 30 days\. For example, bucket lifecycle configuration has minimum object size limit for Amazon S3 to transition objects\. For more information, see [Supported Transitions and Related Constraints](lifecycle-transition-general-considerations.md#lifecycle-general-considerations-transition-sc)\. 

+ **GLACIER** – The `GLACIER` storage class is suitable for archiving data where data access is infrequent\. Archived objects are not available for real\-time access\. You must first restore the objects before you can access them\. For more information, see [Restoring Archived Objects](restoring-objects.md)\.

  The `GLACIER` storage class uses the very low\-cost Amazon Glacier storage service, but you still manage objects in this storage class through Amazon S3\. Note the following about the GLACIER storage class:

  + You cannot specify GLACIER as the storage class at the time that you create an object\. You create GLACIER objects by first uploading objects using STANDARD, RRS, or STANDARD\_IA as the storage class\. Then, you transition these objects to the GLACIER storage class using lifecycle management\. For more information, see [Object Lifecycle Management](object-lifecycle-mgmt.md)\.

  + You must first restore the GLACIER objects before you can access them \(STANDARD, RRS, and STANDARD\_IA objects are available for anytime access\)\. For more information, [Transitioning to the GLACIER Storage Class \(Object Archival\)](lifecycle-transition-general-considerations.md#before-deciding-to-archive-objects)\.

  To learn more about the Amazon Glacier service, see the [Amazon Glacier Developer Guide](http://docs.aws.amazon.com/amazonglacier/latest/dev/)\.

All the preceding storage classes are designed to sustain the concurrent loss of data in two facilities \(for details, see the following availability and durability table\)\. 

In addition to the performance requirements of your application scenario, there is also price/performance considerations\. For the Amazon S3 storage classes and pricing, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

Amazon S3 also offers the following storage class that maintains fewer redundant copies of your data\.

+ **REDUCED\_REDUNDANCY** – The Reduced Redundancy Storage \(RRS\) storage class is designed for noncritical, reproducible data stored at lower levels of redundancy than the STANDARD storage class\.

  The durability level \(see the following table\) corresponds to an average annual expected loss of 0\.01% of objects\. For example, if you store 10,000 objects using the RRS option, you can, on average, expect to incur an annual loss of a single object per year \(0\.01% of 10,000 objects\)\. 
**Note**  
This annual loss represents an expected average and does not guarantee the loss of less than 0\.01% of objects in a given year\. 

  If an RRS object is lost, Amazon S3 returns a 405 error on requests made to that object\. 

  Amazon S3 can send an event notification to alert a user or start a workflow when it detects that an RRS object is lost\. To receive notifications, you need to add notification configuration to your bucket\. For more information, see [ Configuring Amazon S3 Event Notifications](NotificationHowTo.md)\.

The following table summarizes the durability and availability offered by each of the storage classes\. 


****  

| Storage Class | Durability \(designed for\) | Availability \(designed for\) |  **Other Considerations**  | 
| --- | --- | --- | --- | 
|  STANDARD  |  99\.999999999%   |  99\.99%  |  None  | 
|  STANDARD\_IA  |  99\.999999999%   |  99\.9%  |  There is a retrieval fee associated with STANDARD\_IA objects which makes it most suitable for infrequently accessed data\. For pricing information, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.  | 
|  GLACIER  |  99\.999999999%   |  99\.99% \(after you restore objects\)  | GLACIER objects are not available for real\-time access\. You must first restore archived objects before you can access them\. For more information, see [Restoring Archived Objects](restoring-objects.md)\. | 
|  RRS  |  99\.99%   |  99\.99%  |  None  | 