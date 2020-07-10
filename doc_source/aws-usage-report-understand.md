# Understanding your AWS billing and usage reports for Amazon S3<a name="aws-usage-report-understand"></a>

Amazon S3 billing and usage reports use codes and abbreviations\. For example, for usage type, which is defined in the following table, *region* is replaced with one of the following abbreviations:
+ **APE1:** Asia Pacific \(Hong Kong\)
+ **APN1:** Asia Pacific \(Tokyo\)
+ **APN2:** Asia Pacific \(Seoul\)
+ **APS1:** Asia Pacific \(Singapore\)
+ **APS2:** Asia Pacific \(Sydney\)
+ **APS3:** Asia Pacific \(Mumbai\)
+ **CAN1:** Canada \(Central\)
+ **CPT:** Africa \(Cape Town\)
+ **EUN1:** Europe \(Stockholm\)
+ **EUC1:** Europe \(Frankfurt\)
+ **EU:** Europe \(Ireland\)
+ **EUW2:** Europe \(London\)
+ **EUW3:** Europe \(Paris\)
+ **MES1:** Middle East \(Bahrain\)
+ **SAE1:** South America \(São Paulo\)
+ **UGW1:** AWS GovCloud \(US\-West\)
+ **UGE1:** AWS GovCloud \(US\-East\)
+ **USE1 \(or no prefix\):** US East \(N\. Virginia\)
+ **USE2:** US East \(Ohio\)
+ **USW1:** US West \(N\. California\)
+ **USW2:** US West \(Oregon\)

For information about pricing by AWS Region, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

The first column in the following table lists usage types that appear in your billing and usage reports\.


**Usage Types**  

| Usage Type | Units | Granularity | Description | 
| --- | --- | --- | --- | 
|  *region1*\-*region2*\-AWS\-In\-Bytes  |  Bytes  |  Hourly  |  The amount of data transferred in to AWS Region1 from AWS Region2  | 
|  *region1*\-*region2*\-AWS\-Out\-Bytes  |  Bytes  |  Hourly  |  The amount of data transferred from AWS Region1 to AWS Region2  | 
|  *region*\-BatchOperations\-Jobs   |  Count   |  Hourly  |  The number of S3 Batch Operations jobs performed\.  | 
|  *region*\-BatchOperations\-Objects   |  Count   |  Hourly  |  The number of object operations performed by S3 Batch Operations\.  | 
|  *region*\-DataTransfer\-In\-Bytes  |  Bytes  |  Hourly  |  The amount of data transferred into Amazon S3 from the internet  | 
|  *region*\-DataTransfer\-Out\-Bytes  |  Bytes  |  Hourly  |  The amount of data transferred from Amazon S3 to the internet1  | 
|  *region*\-C3DataTransfer\-In\-Bytes  |  Bytes  |  Hourly  |  The amount of data transferred into Amazon S3 from Amazon EC2 within the same AWS Region  | 
|  *region*\-C3DataTransfer\-Out\-Bytes  |  Bytes  |  Hourly  |  The amount of data transferred from Amazon S3 to Amazon EC2 within the same AWS Region  | 
|  *region*\-S3G\-DataTransfer\-In\-Bytes  |  Bytes  |  Hourly  |  The amount of data transferred into Amazon S3 to restore objects from S3 Glacier or S3 Glacier Deep Archive storage  | 
|  *region*\-S3G\-DataTransfer\-Out\-Bytes  |  Bytes  |  Hourly  |  The amount of data transferred from Amazon S3 to transition objects to S3 Glacier or S3 Glacier Deep Archive storage  | 
|  *region*\-DataTransfer\-Regional\-Bytes  |  Bytes  |  Hourly  |  The amount of data transferred from Amazon S3 to AWS resources within the same AWS Region  | 
|  StorageObjectCount  |  Count  |  Daily  |  The number of objects stored within a given bucket  | 
|  *region*\-CloudFront\-In\-Bytes  |  Bytes  |  Hourly  |  The amount of data transferred into an AWS Region from a CloudFront distribution  | 
|  *region*\-CloudFront\-Out\-Bytes  |  Bytes  |  Hourly  |  The amount of data transferred from an AWS Region to a CloudFront distribution  | 
|  *region*\-EarlyDelete\-ByteHrs  |  Byte\-Hours2  |  Hourly  |  Prorated storage usage for objects deleted from, S3 Glacier storage before the 90\-day minimum commitment ended3  | 
|  *region*\-EarlyDelete\-GDA  |  Byte\-Hours2  |  Hourly  |  Prorated storage usage for objects deleted from S3 Glacier Deep Archive storage before the 180\-day minimum commitment ended 3  | 
|  *region*\-EarlyDelete\-SIA  |  Byte\-Hours  |  Hourly  |  Prorated storage usage for objects deleted from STANDARD\_IA before the 30\-day minimum commitment ended4  | 
|  *region*\-EarlyDelete\-ZIA  |  Byte\-Hours  |  Hourly  |  Prorated storage usage for objects deleted from ONEZONE\_IA before the 30\-day minimum commitment ended4  | 
|  *region*\-EarlyDelete\-SIA\-SmObjects  |  Byte\-Hours  |  Hourly  |  Prorated storage usage for small objects \(smaller than 128 KB\) that were deleted from STANDARD\_IA before the 30\-day minimum commitment ended4  | 
|  *region*\-EarlyDelete\-ZIA\-SmObjects  |  Byte\-Hours  |  Hourly  |  Prorated storage usage for small objects \(smaller than 128 KB\) that were deleted from ONEZONE\_IA before the 30\-day minimum commitment ended4  | 
|  *region*\-Inventory\-ObjectsListed  |  Objects  |  Hourly  |  The number of objects listed for an object group \(objects are grouped by bucket or prefix\) with an inventory list  | 
|  *region*\-Requests\-S3 Glacier\-Tier1  |  Count  |  Hourly  |  The number of PUT, COPY, POST, InitiateMultipartUpload, UploadPart, or CompleteMultipartUpload requests on S3 Glacier objects  | 
|  *region*\-Requests\-S3 Glacier\-Tier2  |  Count  |  Hourly  | The number of GET and all other requests not listed on S3 Glacier objects | 
|  *region*\-Requests\-SIA\-Tier1  |  Count  |  Hourly  |  The number of PUT, COPY, POST, or LIST requests on STANDARD\_IA objects  | 
|  *region*\-Requests\-ZIA\-Tier1  |  Count  |  Hourly  |  The number of PUT, COPY, POST, or LIST requests on ONEZONE\_IA objects  | 
|  *region*\-Requests\-SIA\-Tier2  |  Count  |  Hourly  |  The number of GET and all other non\-SIA\-Tier1 requests on STANDARD\_IA objects  | 
|  *region*\-Requests\-ZIA\-Tier2  |  Count  |  Hourly  |  The number of GET and all other non\-ZIA\-Tier1 requests on ONEZONE\_IA objects  | 
|  *region*\-Requests\-Tier1  |  Count  |  Hourly  |  The number of PUT, COPY, POST, or LIST requests for STANDARD, RRS, and tags  | 
|  *region*\-Requests\-Tier2  |  Count  |  Hourly  |  The number of GET and all other non\-Tier1 requests  | 
|  *region*\-Requests\-Tier3  |  Count  |  Hourly  |  The number of lifecycle requests to S3 Glacier or S3 Glacier Deep Archive and standard S3 Glacier restore requests  | 
|  *region*\-Requests\-Tier4  |  Count  |  Hourly  |  The number of lifecycle transitions to INTELLIGENT\_TIERING, STANDARD\_IA, or ONEZONE\_IA storage  | 
|  *region*\-Requests\-Tier5  |  Count  |  Hourly  |  The number of Bulk S3 Glacier restore requests  | 
|  *region*\-Requests\-GDA\-Tier1  |  Count  |  Hourly  |  The number of PUT, COPY, POST, InitiateMultipartUpload, UploadPart, or CompleteMultipartUpload requests on DEEP Archive objects  | 
|  *region*\-Requests\-GDA\-Tier2  |  Count  |  Hourly  |  The number of GET, HEAD, and LIST requests  | 
|  *region*\-Requests\-GDA\-Tier3  |  Count  |  Hourly  |  The number of S3 Glacier Deep Archive standard restore requests  | 
|  *region*\-Requests\-GDA\-Tier5  |  Count  |  Hourly  |  The number of Bulk S3 Glacier Deep Archive restore requests  | 
|  *region*\-Requests\-Tier6  |  Count  |  Hourly  |  The number of Expedited S3 Glacier restore requests  | 
|  *region*\-Bulk\-Retrieval\-Bytes  |  Bytes  |  Hourly  |  The number of bytes of data retrieved with Bulk S3 Glacier or S3 Glacier Deep Archive requests  | 
|  *region*\-Requests\-INT\-Tier1  |  Count  |  Hourly  |  The number of PUT, COPY, POST, or LIST requests on INTELLIGENT\_TIERING objects  | 
|  *region*\-Requests\-INT\-Tier2  |  Count  |  Hourly  |  The number of GET and all other non\-Tier1 requests for INTELLIGENT\_TIERING objects  | 
|  *region*\-Select\-Returned\-INT\-Bytes  |  Bytes  |  Hourly  |  The number of bytes of data returned with Select requests from INTELLIGENT\_TIERING storage  | 
|  *region*\-Select\-Scanned\-INT\-Bytes  |  Bytes  |  Hourly  |  The number of bytes of data scanned with Select requests from INTELLIGENT\_TIERING storage  | 
|  *region*\-EarlyDelete\-INT  |  Byte\-Hours  |  Hourly  |  Prorated storage usage for objects deleted from INTELLIGENT\_TIERING before the 30\-day minimum commitment ended  | 
|  *region*\-Monitoring\-Automation\-INT  |  Objects  |  Hourly  |  The number of unique objects monitored and auto\-tiered in the INTELLIGENT\_TIERING storage class  | 
|  *region*\-Expedited\-Retrieval\-Bytes  |  Bytes  |  Hourly  |  The number of bytes of data retrieved with Expedited S3 Glacier requests  | 
|  *region*\-Standard\-Retrieval\-Bytes  |  Bytes  |  Hourly  |  The number of bytes of data retrieved with standard S3 Glacier or S3 Glacier Deep Archive requests  | 
|  *region*\-Retrieval\-SIA  |  Bytes  |  Hourly  |  The number of bytes of data retrieved from STANDARD\_IA storage  | 
|  *region*\-Retrieval\-ZIA  |  Bytes  |  Hourly  |  The number of bytes of data retrieved from ONEZONE\_IA storage  | 
|  *region*\-StorageAnalytics\-ObjCount  |  Objects  |  Hourly  |  The number of unique objects in each object group \(where objects are grouped by bucket or prefix\) tracked by storage analytics  | 
|  *region*\-Select\-Scanned\-Bytes  |  Bytes  |  Hourly  |  The number of bytes of data scanned with Select requests from STANDARD storage  | 
|  *region*\-Select\-Scanned\-SIA\-Bytes  |  Bytes  |  Hourly  |  The number of bytes of data scanned with Select requests from STANDARD\_IA storage  | 
|  *region*\-Select\-Scanned\-ZIA\-Bytes  |  Bytes  |  Hourly  |  The number of bytes of data scanned with Select requests from ONEZONE\_IA storage  | 
|  *region*\-Select\-Returned\-Bytes  |  Bytes  |  Hourly  |  The number of bytes of data returned with Select requests from STANDARD storage  | 
|  *region*\-Select\-Returned\-SIA\-Bytes  |  Bytes  |  Hourly  |  The number of bytes of data returned with Select requests from STANDARD\_IA storage  | 
|  *region*\-Select\-Returned\-ZIA\-Bytes  |  Bytes  |  Hourly  |  The number of bytes of data returned with Select requests from ONEZONE\_IA storage  | 
|  *region*\-TagStorage\-TagHrs  |  Tag\-Hours  |  Daily  |  The total of tags on all objects in the bucket reported by hour  | 
|  *region*\-TimedStorage\-ByteHrs  |  Byte\-Hours  |  Daily  |  The number of byte\-hours that data was stored in STANDARD storage  | 
|  *region*\-TimedStorage\-S3 GlacierByteHrs  |  Byte\-Hours  |  Daily  |  The number of byte\-hours that data was stored in S3 Glacier storage  | 
|  *region*\-TimedStorage\-GlacierStaging  |  Byte\-Hours  |  Daily  |  The number of byte\-hours that data was stored in S3 Glacier staging storage  | 
|  *region*\-TimedStorage\-GDA\-ByteHrs  |  Byte\-Hours  |  Daily  |  The number of byte\-hours that data was stored in S3 Glacier Deep Archive storage  | 
|  *region*\-TimedStorage\-GDA\-Staging  |  Byte\-Hours  |  Daily  |  The number of byte\-hours that data was stored in S3 Glacier Deep Archive staging storage  | 
|  *region*\-TimedStorage\-INT\-FA\-ByteHrs  |  Byte\-Hours  |  Daily  |  The number of byte\-hours that data was stored in the frequent access tier of INTELLIGENT\_TIERING storage  | 
|  *region*\-TimedStorage\-INT\-IA\-ByteHrs  |  Byte\-Hours  |  Daily  |  The number of byte\-hours that data was stored in the infrequent access tier of INTELLIGENT\_TIERING storage  | 
|  *region*\-TimedStorage\-RRS\-ByteHrsb  |  Byte\-Hours  |  Daily  |  The number of byte\-hours that data was stored in Reduced Redundancy Storage \(RRS\) storage  | 
|  *region*\-TimedStorage\-SIA\-ByteHrs  |  Byte\-Hours  |  Daily  |  The number of byte\-hours that data was stored in STANDARD\_IA storage  | 
|  *region*\-TimedStorage\-ZIA\-ByteHrs  |  Byte\-Hours  |  Daily  |  The number of byte\-hours that data was stored in ONEZONE\_IA storage  | 
|  *region*\-TimedStorage\-SIA\-SmObjects  |  Byte\-Hours  |  Daily  |  The number of byte\-hours that small objects \(smaller than 128 KB\) were stored in STANDARD\_IA storage  | 
|  *region*\-TimedStorage\-ZIA\-SmObjects  |  Byte\-Hours  |  Daily  |  The number of byte\-hours that small objects \(smaller than 128 KB\) were stored in ONEZONE\_IA storage  | 

**Notes:**

1. If you terminate a transfer before completion, the amount of data that is transferred might exceed the amount of data that your application receives\. This discrepancy can occur because a transfer termination request cannot be executed instantaneously, and some amount of data might be in transit pending execution of the termination request\. This data in transit is billed as data transferred “out\.”

1. For more information on the byte\-hours unit, see [Converting Usage Byte\-Hours to Billed GB\-Months](#aws-usage-report-understand-converting-byte-hours)\.

1. When objects that are archived to the S3 Glacier or S3 Glacier Deep Archive storage class are deleted, overwritten, or transitioned to a different storage class before the minimum storage commitment has passed, which is 90 days for S3 Glacier or 180\-days for S3 Glacier Deep Archive, there is a prorated charge per gigabyte for the remaining days\.

1. For objects that are in INTELLIGENT\_TIERING, STANDARD\_IA, or ONEZONE\_IA storage, when they are deleted, overwritten, or transitioned to a different storage class prior to 30 days, there is a prorated charge per gigabyte for the remaining days\.

1. For small objects \(smaller than 128 KB\) that are in STANDARD\_IA or ONEZONE\_IA storage, when they are deleted, overwritten, or transitioned to a different storage class prior to 30 days, there is a prorated charge per gigabyte for the remaining days\.

1. There is no minimum billable object size for objects in the INTELLIGENT\_TIERING storage class, but objects that are smaller than 128 KB are not eligible for auto\-tiering and are always charged at the rate for the INTELLIGENT\_TIERING frequent access tier\. 

## Tracking Operations in Your Usage Reports<a name="aws-usage-report-understand-operations"></a>

Operations describe the action taken on your AWS object or bucket by the specified usage type\. Operations are indicated by self\-explanatory codes, such as `PutObject` or `ListBucket`\. To see which actions on your bucket generated a specific type of usage, use these codes\. When you create a usage report, you can choose to include **All Operations**, or a specific operation, for example, **GetObject**, to report on\.

## Converting Usage Byte\-Hours to Billed GB\-Months<a name="aws-usage-report-understand-converting-byte-hours"></a>

The volume of storage that we bill you for each month is based on the average amount of storage you used throughout the month\. You are billed for all of the object data and metadata stored in buckets that you created under your AWS account\. For more information about metadata, see [Object key and metadata](UsingMetadata.md)\. 

We measure your storage usage in TimedStorage\-ByteHrs, which are totaled up at the end of the month to generate your monthly charges\. The usage report reports your storage usage in byte\-hours and the billing reports report storage usage in GB\-months\. To correlate your usage report to your billing reports, you need to convert byte\-hours into GB\-months\.

For example, if you store 100 GB \(107,374,182,400 bytes\) of STANDARD Amazon S3 storage data in your bucket for the first 15 days in March, and 100 TB \(109,951,162,777,600 bytes\) of STANDARD Amazon S3 storage data for the final 16 days in March, you will have used 42,259,901,212,262,400 byte\-hours\.

First, calculate the total byte\-hour usage:

```
[107,374,182,400 bytes x 15 days x (24 hours/day)] 
    + [109,951,162,777,600 bytes x 16 days x (24 hours/day)]
    = 42,259,901,212,262,400 byte-hours
```

Then convert the byte\-hours to GB\-Months:

```
42,259,901,212,262,400 byte-hours/1,073,741,824 bytes per GB/24 hours per day
     /31 days in March 
     =52,900 GB-Months
```

## More Info<a name="aws-usage-report-understand-more-info"></a>
+ [AWS usage report for Amazon S3](aws-usage-report.md)
+ [AWS Billing reports for Amazon S3](aws-billing-reports.md)
+ [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)
+ [Amazon S3 FAQ](https://aws.amazon.com/s3/faqs/#billing)
+ [S3 Glacier Pricing](https://aws.amazon.com/glacier/pricing/)
+ [S3 Glacier FAQs](https://aws.amazon.com/glacier/faqs/)