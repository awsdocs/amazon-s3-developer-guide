# Restoring Archived Objects<a name="restoring-objects"></a>

Objects that you archive to the S3 Glacier or S3 Glacier Deep Archive storage classes are not accessible in real time\. You must first initiate a restore request, and then wait until a temporary copy of the object is available for the duration \(number of days\) that you specify in the request\. For more information about how the S3 Glacier, S3 Glacier Deep Archive, and other Amazon S3 storage classes compare, see [Amazon S3 storage classes](storage-class-intro.md)\. 

Amazon S3 restores a temporary copy of the object only for the specified duration\. After that, it deletes the restored object copy\. You can modify the expiration period of a restored copy by reissuing a restore\. In this case, Amazon S3 updates the expiration period relative to the current time\. 

Amazon S3 calculates the expiration time of the restored object copy by adding the number of days specified in the restoration request to the current time\. It then rounds the resulting time to the next day at midnight Universal Coordinated Time \(UTC\)\. For example, suppose that an object was created on October 15, 2012 10:30 AM UTC, and the restoration period was specified as three days\. In this case, the restored copy expires on October 19, 2012 00:00 UTC, at which time Amazon S3 deletes the object copy\. 

After you receive a temporary copy of the restored object, the object's storage class remains S3 Glacier or S3 Glacier Deep Archive\. \(A [HEAD Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectHEAD.html) or the [GET Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html) API operations request returns S3 Glacier or S3 Glacier Deep Archive as the storage class\.\) 

The time it takes a restore job to finish depends on which archive storage class you use and which retrieval option you specify: `Expedited` \(only available for S3 Glacier\), `Standard`, or `Bulk`\. You can be notified when your restore is complete using Amazon S3 event notifications\. For more information, see [ Configuring Amazon S3 event notifications](NotificationHowTo.md)\.

You can restore an object copy for any number of days\. However you should restore objects only for the duration that you need because of the storage costs associated with the object copy\. When you restore an archive, you pay for both the archive \(at the S3 Glacier or S3 Glacier Deep Archive rate\) and a copy that you restored temporarily \(Reduced Redundancy Storage \(RRS\) or Standard, whichever is the lower cost storage in the region\)\. For information about pricing, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

When required, you can restore large segments of the data stored in the S3 Glacier and S3 Glacier Deep Archive storage classes\. For example, you might want to restore data for a secondary copy\. However, if you need to restore a large amount of data, keep in mind that the S3 Glacier and S3 Glacier Deep Archive storage classes are designed for 35 random restore requests per pebibyte \(PiB\) stored per day\.

For information about using lifecycle transitions to move objects to the S3 Glacier or S3 Glacier Deep Archive storage classes, see [Transitioning to the S3 Glacier and S3 Glacier Deep Archive storage classes \(object archival\)](lifecycle-transition-general-considerations.md#before-deciding-to-archive-objects)\.

To restore more than one Amazon S3 object with a single request, you can use S3 Batch Operations\. You provide S3 Batch Operations with a list of objects to operate on\. S3 Batch Operations call the respective API to perform the specified operation\. A single S3 Batch Operations job can perform the specified operation on billions of objects containing exabytes of data\. 

S3 Batch Operations track progress, send notifications, and store a detailed completion report of all actions, providing a fully managed, auditable, serverless experience\. You can use S3 Batch Operations through the AWS Management Console, AWS CLI, AWS SDKs, or REST API\. For more information, see [The basics: S3 Batch Operations](batch-ops-basics.md)\.

The following sections provide more information about restoring objects\.

**Topics**
+ [Archive Retrieval Options](#restoring-objects-retrieval-options)
+ [Upgrading the Speed of an In\-Progress Restore](#restoring-objects-upgrade-tier)
+ [Restore an Archived Object Using the Amazon S3 Console](restoring-objects-console.md)
+ [Restore an Archived Object Using the AWS SDK for Java](restoring-objects-java.md)
+ [Restore an Archived Object Using the AWS SDK for \.NET](restore-object-dotnet.md)
+ [Restore an Archived Object Using the REST API](restoring-objects-rest.md)

## Archive Retrieval Options<a name="restoring-objects-retrieval-options"></a>

The following are the available retrieval options when restoring an archived object: 
+ **`Expedited`** \- Expedited retrievals allow you to quickly access your data stored in the S3 Glacier storage class when occasional urgent requests for a subset of archives are required\. For all but the largest archived objects \(250 MB\+\), data accessed using Expedited retrievals is typically made available within 1–5 minutes\. Provisioned capacity ensures that retrieval capacity for Expedited retrievals is available when you need it\. For more information, see [Provisioned Capacity](#restoring-objects-expedited-capacity)\. Expedited retrievals and provisioned capacity are not available for objects stored in the S3 Glacier Deep Archive storage class\.
+ **`Standard`** \- Standard retrievals allow you to access any of your archived objects within several hours\. This is the default option for the S3 Glacier and S3 Glacier Deep Archive retrieval requests that do not specify the retrieval option\. Standard retrievals typically finish within 3–5 hours for objects stored in the S3 Glacier storage class\. They typically finish within 12 hours for objects stored in the S3 Glacier Deep Archive storage class\. 
+ **`Bulk`** \- Bulk retrievals are the lowest\-cost retrieval option in Amazon S3 Glacier, enabling you to retrieve large amounts, even petabytes, of data inexpensively\. Bulk retrievals typically finish within 5–12 hours for objects stored in the S3 Glacier storage class\. They typically finish within 48 hours for objects stored in the S3 Glacier Deep Archive storage class\.

The following table summarizes the archival retrieval options\.


**Retrieval Options**  

| Storage Class | Expedited | Standard | Bulk | 
| --- | --- | --- | --- | 
|  S3 Glacier  |  1–5 minutes  |  3–5 hours  |  5–12 hours  | 
|  S3 Glacier Deep Archive  |  Not available  |  Within 12 hours  |  Within 48 hours  | 

To make an `Expedited`, `Standard`, or `Bulk` retrieval, set the `Tier` request element in the [POST Object restore](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOSTrestore.html) REST API request to the option you want, or the equivalent in the AWS CLI or AWS SDKs\. If you purchased provisioned capacity, all Expedited retrievals are automatically served through your provisioned capacity\. 

You can restore an archived object programmatically or by using the Amazon S3 console\. Amazon S3 processes only one restore request at a time per object\. You can use both the console and the Amazon S3 API to check the restoration status and to find out when Amazon S3 will delete the restored copy\. 

### Provisioned Capacity<a name="restoring-objects-expedited-capacity"></a>

Provisioned capacity ensures that your retrieval capacity for expedited retrievals is available when you need it\. Each unit of capacity provides that at least three expedited retrievals can be performed every 5 minutes, and it provides up to 150 MB/s of retrieval throughput\.

If your workload requires highly reliable and predictable access to a subset of your data in minutes, you should purchase provisioned retrieval capacity\. Without provisioned capacity, Expedited retrievals might not be accepted during periods of high demand\. If you require access to Expedited retrievals under all circumstances, we recommend that you purchase provisioned retrieval capacity\. 

You can purchase provisioned capacity using the Amazon S3 console, the Amazon S3 Glacier console, the [Purchase Provisioned Capacity](https://docs.aws.amazon.com/amazonglacier/latest/dev/api-PurchaseProvisionedCapacity.html) REST API, the AWS SDKs, or the AWS CLI\. For provisioned capacity pricing information, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\. 

Expedited retrievals using provisioned capacity still incur request and retrieval charges, and are not available for the S3 Glacier Deep Archive storage class\.

## Upgrading the Speed of an In\-Progress Restore<a name="restoring-objects-upgrade-tier"></a>

Using Amazon S3 restore speed upgrade, you can change the restore speed to a faster speed while the restore is in progress\. A restore speed upgrade overrides an in\-progress restore with a faster restore tier\. You cannot slow down an in\-progress restore\.

To upgrade the speed of an in\-progress restoration, issue another restore request to the same object that sets a new `Tier` request element in the [POST Object restore](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOSTrestore.html) REST API, or the equivalent in the AWS CLI or AWS SDKs\. When issuing a request to upgrade the restore tier, you must choose a tier that is faster than the tier that the in\-progress restore is using\. You must not change any other parameters, such as the `Days` request element\. 

You can be notified of the completion of the restore by using Amazon S3 event notifications\. Restores are charged at the price of the upgraded tier\. For information about restore pricing, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.