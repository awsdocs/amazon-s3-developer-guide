# Restoring Archived Objects<a name="restoring-objects"></a>

Objects archived to Amazon Glacier are not accessible in real\-time\. You must first initiate a restore request and then wait until a temporary copy of the object is available for the duration \(number of days\) that you specify in the request\. The time it takes restore jobs to complete depends on which retrieval option you specify `Standard`, `Expedited`, or `Bulk`\. For more information about archiving objects to Amazon Glacier, see [Transitioning to the GLACIER Storage Class \(Object Archival\)](lifecycle-transition-general-considerations.md#before-deciding-to-archive-objects)\.

After you receive a temporary copy of the restored object, the object's storage class remains `GLACIER` \(a GET or HEAD request will return `GLACIER` as the storage class\)\. Note that when you restore an archive you pay for both the archive \(`GLACIER` rate\) and a copy you restored temporarily \(`RRS` rate\)\. For information about pricing, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\. 

The following topics provide more information\.

**Topics**
+ [Archive Retrieval Options](#restoring-objects-retrieval-options)
+ [Restore an Archived Object Using the Amazon S3 Console](restoring-objects-console.md)
+ [Restore an Archived Object Using the AWS SDK for Java](restoring-objects-java.md)
+ [Restore an Archived Object Using the AWS SDK for \.NET](restore-object-dotnet.md)
+ [Restore an Archived Object Using the REST API](restoring-objects-rest.md)

## Archive Retrieval Options<a name="restoring-objects-retrieval-options"></a>

You can specify one of the following when restoring an archived object: 
+ **`Expedited`** \- Expedited retrievals allow you to quickly access your data when occasional urgent requests for a subset of archives are required\. For all but the largest archived objects \(250 MB\+\), data accessed using Expedited retrievals are typically made available within 1–5 minutes\. There are two types of Expedited retrievals: On\-Demand and Provisioned\. On\-Demand requests are similar to EC2 On\-Demand instances and are available most of the time\. Provisioned requests are guaranteed to be available when you need them\. For more information, see [Provisioned Capacity](#restoring-objects-expedited-capacity)\. 
+ **`Standard`** \- Standard retrievals allow you to access any of your archived objects within several hours\. Standard retrievals typically complete within 3–5 hours\. This is the default option for retrieval requests that do not specify the retrieval option\.
+ **`Bulk`** \- Bulk retrievals are Amazon Glacier’s lowest\-cost retrieval option, enabling you to retrieve large amounts, even petabytes, of data inexpensively in a day\. Bulk retrievals typically complete within 5–12 hours\.

To make an `Expedited`, `Standard`, or `Bulk` retrieval, set the `Tier` request element in the [POST Object restore](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOSTrestore.html) REST API request to the option you want, or the equivalent in the AWS CLI, or AWS SDKs\. For Expedited retrievals, there is no need to designate whether an expedited retrieval is On\-Demand or Provisioned\. If you have purchased provisioned capacity, all Expedited retrievals are automatically served through your provisioned capacity\. 

You can restore an archived object programmatically or by using the Amazon S3 console\. Amazon S3 processes only one restore request at a time per object\. You can use both the console and the Amazon S3 API to check the restoration status and to find out when Amazon S3 will delete the restored copy\. 

### Provisioned Capacity<a name="restoring-objects-expedited-capacity"></a>

Provisioned capacity guarantees that your retrieval capacity for expedited retrievals is available when you need it\. Each unit of capacity ensures that at least three expedited retrievals can be performed every five minutes and provides up to 150 MB/s of retrieval throughput\.

You should purchase provisioned retrieval capacity if your workload requires highly reliable and predictable access to a subset of your data in minutes\. Without provisioned capacity Expedited retrievals are accepted, except for rare situations of unusually high demand\. However, if you require access to Expedited retrievals under all circumstances, you must purchase provisioned retrieval capacity\. You can purchase provisioned capacity using the Amazon S3 console, the Amazon Glacier console, the [Purchase Provisioned Capacity](http://docs.aws.amazon.com/amazonglacier/latest/dev/api-PurchaseProvisionedCapacity.html) REST API, the AWS SDKs, or the AWS CLI\. For provisioned capacity pricing information, see the [Amazon Glacier Pricing](https://aws.amazon.com/glacier/pricing/)\. 