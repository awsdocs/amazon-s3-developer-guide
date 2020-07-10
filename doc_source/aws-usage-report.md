# AWS usage report for Amazon S3<a name="aws-usage-report"></a>

For more detail about your Amazon S3 storage usage, download dynamically generated AWS usage reports\. You can choose which usage type, operation, and time period to include\. You can also choose how the data is aggregated\. 

When you download a usage report, you can choose to aggregate usage data by hour, day, or month\. The Amazon S3 usage report lists operations by usage type and AWS Region, for example, the amount of data transferred out of the Asia Pacific \(Sydney\) Region\.

The Amazon S3 usage report includes the following information:
+ **Service** – `Amazon Simple Storage Service`
+ **Operation** – The operation performed on your bucket or object\. For a detailed explanation of Amazon S3 operations, see [Tracking Operations in Your Usage Reports](aws-usage-report-understand.md#aws-usage-report-understand-operations)\.
+ **UsageType** – One of the following values:
  + A code that identifies the type of storage
  + A code that identifies the type of request
  + A code that identifies the type of retrieval
  + A code that identifies the type of data transfer
  + A code that identifies early deletions from INTELLIGENT\_TIERING, STANDARD\_IA, ONEZONE\_IA, S3 Glacier, or S3 Glacier Deep Archive storage
  + `StorageObjectCount` – The count of objects stored within a given bucket

  For a detailed explanation of Amazon S3 usage types, see [Understanding your AWS billing and usage reports for Amazon S3](aws-usage-report-understand.md)\.
+ **Resource** – The name of the bucket associated with the listed usage\.
+ **StartTime** – Start time of the day that the usage applies to, in Coordinated Universal Time \(UTC\)\.
+ **EndTime ** – End time of the day that the usage applies to, in Coordinated Universal Time \(UTC\)\. 
+ **UsageValue** – One of the following volume values:
  + The number of requests during the specified time period
  + The amount of data transferred, in bytes
  + The amount of data stored, in byte\-hours, which is the number of bytes stored in a given hour
  + The amount of data associated with restorations from S3 Glacier Deep Archive, S3 Glacier, STANDARD\_IA, or ONEZONE\_IA storage, in bytes

**Tip**  
For detailed information about every request that Amazon S3 receives for your objects, turn on server access logging for your buckets\. For more information, see [Amazon S3 server access logging](ServerLogs.md)\. 

You can download a usage report as an XML or a comma\-separated values \(CSV\) file\. The following is an example CSV usage report opened in a spreadsheet application\.

![\[Screenshot of a CSV usage report in a spreadsheet application.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/s3-usage-report.png)

For information on understanding the usage report, see [Understanding your AWS billing and usage reports for Amazon S3](aws-usage-report-understand.md)\.

## Downloading the AWS Usage Report<a name="aws-usage-report-download"></a>

You can download a usage report as an \.xml or a \.csv file\.

**To download the usage report**

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. In the title bar, choose your AWS Identity and Access Management \(IAM\) user name, and then choose **My Billing Dashboard**\. 

1. In the navigation pane, choose **AWS Cost and Usage Reports**\.

1. In the **Other Reports** section, choose **AWS Usage Report**\.

1. For **Services:**, choose **Amazon Simple Storage Service**\.

1. For **Download Usage Report**, choose the following settings:
   + ****Usage Types **** – For a detailed explanation of Amazon S3 usage types, see [Understanding your AWS billing and usage reports for Amazon S3](aws-usage-report-understand.md)\.
   + ****Operation **** – For a detailed explanation of Amazon S3 operations, see [Tracking Operations in Your Usage Reports](aws-usage-report-understand.md#aws-usage-report-understand-operations)\.
   + ****Time Period **** – The time period that you want the report to cover\. 
   + ****Report Granularity**** – Whether you want the report to include subtotals by the hour, by the day, or by the month\.

1. To choose the format for the report, choose the **Download** for that format, and then follow the prompts to see or save the report\.

## More Info<a name="aws-usage-report-more-info"></a>
+ [Understanding your AWS billing and usage reports for Amazon S3](aws-usage-report-understand.md)
+ [AWS Billing reports for Amazon S3](aws-billing-reports.md)