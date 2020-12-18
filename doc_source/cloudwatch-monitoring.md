# Monitoring metrics with Amazon CloudWatch<a name="cloudwatch-monitoring"></a>

Amazon CloudWatch metrics for Amazon S3 can help you understand and improve the performance of applications that use Amazon S3\. There are several ways that you can use CloudWatch with Amazon S3\.
+ **Daily storage metrics for buckets** ‐ Monitor bucket storage using CloudWatch, which collects and processes storage data from Amazon S3 into readable, daily metrics\. These storage metrics for Amazon S3 are reported once per day and are provided to all customers at no additional cost\.
+ **Request metrics** ‐ Monitor Amazon S3 requests to quickly identify and act on operational issues\. The metrics are available at 1\-minute intervals after some latency to process\. These CloudWatch metrics are billed at the same rate as the Amazon CloudWatch custom metrics\. For information about CloudWatch pricing, see [Amazon CloudWatch pricing](https://aws.amazon.com/cloudwatch/pricing/)\. To learn how to opt in to getting these metrics, see [Metrics configurations for buckets](metrics-configurations.md)\.

  When enabled, request metrics are reported for all object operations\. By default, these 1\-minute metrics are available at the Amazon S3 bucket level\. You can also define a filter for the metrics collected using a shared prefix or object tag\. This allows you to align metrics filters to specific business applications, workflows, or internal organizations\.
+ **Replication metrics** ‐ Monitor the total number of S3 API operations that are pending replication, the total size of objects pending replication, and the maximum replication time to the destination Region\. Replication rules that have S3 Replication Time Control \(S3 RTC\) or S3 replication metrics enabled will publish replication metrics\. 

  For more information, see [Monitoring progress with replication metrics and Amazon S3 event notifications](replication-metrics.md) or [Meeting compliance requirements using S3 Replication Time Control \(S3 RTC\)](replication-time-control.md)\.

All CloudWatch statistics are retained for a period of 15 months so that you can access historical information and gain a better perspective on how your web application or service is performing\. For more information, see [What Is Amazon CloudWatch?](https://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/WhatIsCloudWatch.html) in the *Amazon CloudWatch User Guide*\.

## Metrics and dimensions<a name="metrics-dimensions"></a>

The storage metrics and dimensions that Amazon S3 sends to CloudWatch are listed below\.

## Amazon S3 CloudWatch Daily Storage Metrics for Buckets<a name="s3-cloudwatch-metrics"></a>

The `AWS/S3` namespace includes the following daily storage metrics for buckets\.


| Metric | Description | 
| --- | --- | 
| BucketSizeBytes |  The amount of data in bytes stored in a bucket in the STANDARD storage class, INTELLIGENT\_TIERING storage class, Standard \- Infrequent Access \(STANDARD\_IA\) storage class, OneZone \- Infrequent Access \(ONEZONE\_IA\), Reduced Redundancy Storage \(RRS\) class, Deep Archive Storage \(S3 Glacier Deep Archive\) class or, Glacier \(GLACIER\) storage class\. This value is calculated by summing the size of all objects in the bucket \(both current and noncurrent objects\), including the size of all parts for all incomplete multipart uploads to the bucket\.  Valid storage type filters: `StandardStorage`, `IntelligentTieringFAStorage`, `IntelligentTieringIAStorage`, `IntelligentTieringAAStorage`, `IntelligentTieringDAAStorage`, `StandardIAStorage`, `StandardIASizeOverhead`, `StandardIAObjectOverhead`, `OneZoneIAStorage`, `OneZoneIASizeOverhead`, `ReducedRedundancyStorage`, `GlacierStorage`, `GlacierStagingStorage`, `GlacierObjectOverhead`, `GlacierS3ObjectOverhead`, `DeepArchiveStorage`, `DeepArchiveObjectOverhead`, `DeepArchiveS3ObjectOverhead` and, `DeepArchiveStagingStorage` \(see the `StorageType` dimension\)  Units: Bytes Valid statistics: Average  | 
| NumberOfObjects |  The total number of objects stored in a bucket for all storage classes except for the GLACIER storage class\. This value is calculated by counting all objects in the bucket \(both current and noncurrent objects\) and the total number of parts for all incomplete multipart uploads to the bucket\. Valid storage type filters: `AllStorageTypes` \(see the `StorageType` dimension\) Units: Count Valid statistics: Average  | 

## Amazon S3 CloudWatch Request Metrics<a name="s3-request-cloudwatch-metrics"></a>

The `AWS/S3` namespace includes the following request metrics\.


| Metric | Description | 
| --- | --- | 
| AllRequests |  The total number of HTTP requests made to an Amazon S3 bucket, regardless of type\. If you're using a metrics configuration with a filter, then this metric only returns the HTTP requests made to the objects in the bucket that meet the filter's requirements\. Units: Count Valid statistics: Sum  | 
| GetRequests |  The number of HTTP GET requests made for objects in an Amazon S3 bucket\. This doesn't include list operations\. Units: Count Valid statistics: Sum  Paginated list\-oriented requests, like [List Multipart Uploads](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadListMPUpload.html), [List Parts](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadListParts.html), [Get Bucket Object versions](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETVersion.html), and others, are not included in this metric\.   | 
| PutRequests |  The number of HTTP PUT requests made for objects in an Amazon S3 bucket\. Units: Count Valid statistics: Sum  | 
| DeleteRequests |  The number of HTTP DELETE requests made for objects in an Amazon S3 bucket\. This also includes [Delete Multiple Objects](https://docs.aws.amazon.com/AmazonS3/latest/API/multiobjectdeleteapi.html) requests\. This metric shows the number of requests, not the number of objects deleted\. Units: Count Valid statistics: Sum  | 
| HeadRequests |  The number of HTTP HEAD requests made to an Amazon S3 bucket\. Units: Count Valid statistics: Sum  | 
| PostRequests |  The number of HTTP POST requests made to an Amazon S3 bucket\. Units: Count Valid statistics: Sum  [Delete Multiple Objects](https://docs.aws.amazon.com/AmazonS3/latest/API/multiobjectdeleteapi.html) and [SELECT Object Content](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectSELECTContent.html) requests are not included in this metric\.    | 
| SelectRequests |  The number of Amazon S3 [SELECT Object Content](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectSELECTContent.html) requests made for objects in an Amazon S3 bucket\.  Units: Count Valid statistics: Sum  | 
| SelectScannedBytes |  The number of bytes of data scanned with Amazon S3 [SELECT Object Content](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectSELECTContent.html) requests in an Amazon S3 bucket\.   Units: Bytes  Valid statistics: Average \(bytes per request\), Sum \(bytes per period\), Sample Count, Min, Max \(same as p100\), any percentile between p0\.0 and p99\.9  | 
| SelectReturnedBytes |  The number of bytes of data returned with Amazon S3 [SELECT Object Content](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectSELECTContent.html) requests in an Amazon S3 bucket\.   Units: Bytes  Valid statistics: Average \(bytes per request\), Sum \(bytes per period\), Sample Count, Min, Max \(same as p100\), any percentile between p0\.0 and p99\.9  | 
| ListRequests |  The number of HTTP requests that list the contents of a bucket\. Units: Count Valid statistics: Sum  | 
| BytesDownloaded |  The number of bytes downloaded for requests made to an Amazon S3 bucket, where the response includes a body\. Units: Bytes Valid statistics: Average \(bytes per request\), Sum \(bytes per period\), Sample Count, Min, Max \(same as p100\), any percentile between p0\.0 and p99\.9  | 
| BytesUploaded |  The number of bytes uploaded that contain a request body, made to an Amazon S3 bucket\. Units: Bytes Valid statistics: Average \(bytes per request\), Sum \(bytes per period\), Sample Count, Min, Max \(same as p100\), any percentile between p0\.0 and p99\.9  | 
| 4xxErrors |  The number of HTTP 4xx client error status code requests made to an Amazon S3 bucket with a value of either 0 or 1\. The `average` statistic shows the error rate, and the `sum` statistic shows the count of that type of error, during each period\. Units: Count Valid statistics: Average \(reports per request\), Sum \(reports per period\), Min, Max, Sample Count  | 
| 5xxErrors |  The number of HTTP 5xx server error status code requests made to an Amazon S3 bucket with a value of either 0 or 1\. The `average` statistic shows the error rate, and the `sum` statistic shows the count of that type of error, during each period\. Units: Counts Valid statistics: Average \(reports per request\), Sum \(reports per period\), Min, Max, Sample Count  | 
| FirstByteLatency |  The per\-request time from the complete request being received by an Amazon S3 bucket to when the response starts to be returned\. Units: Milliseconds Valid statistics: Average, Sum, Min, Max\(same as p100\), Sample Count, any percentile between p0\.0 and p100  | 
| TotalRequestLatency |  The elapsed per\-request time from the first byte received to the last byte sent to an Amazon S3 bucket\. This includes the time taken to receive the request body and send the response body, which is not included in `FirstByteLatency`\. Units: Milliseconds Valid statistics: Average, Sum, Min, Max\(same as p100\), Sample Count, any percentile between p0\.0 and p100  | 

## Amazon S3 CloudWatch replication metrics<a name="s3-cloudwatch-replication-metrics"></a>

You can monitor the progress of replication with S3 replication metrics by tracking bytes pending, operations pending, and replication latency\. For more information, see [Monitoring progress with replication metrics](https://docs.aws.amazon.com/AmazonS3/latest/dev/replication-metrics.html)

**Note**  
You can enable alarms for your replication metrics on Amazon CloudWatch\. When you set up alarms for your replication metrics, set the **Missing data treatment** field to **Treat missing data as ignore \(maintain the alarm state\)**\.




| Metric | Description | 
| --- | --- | 
| ReplicationLatency |  The maximum number of seconds by which the replication destination Region is behind the source Region for a given replication rule\.  Units: Seconds Valid statistics: Max  | 
| BytesPendingReplication |  The total number of bytes of objects pending replication for a given replication rule\. Units: Bytes Valid statistics: Max  | 
| OperationsPendingReplication |  The number of operations pending replication for a given replication rule\. Units: Counts Valid statistics: Max  | 

## Amazon S3 on Outposts CloudWatch metrics<a name="s3-outposts-cloudwatch-metrics"></a>

The `S3Outposts` namespace includes the following metrics for Amazon S3 on Outposts buckets\. You can monitor the total number of S3 on Outposts bytes provisioned, the total free bytes available for objects, and the total size of all objects for a given bucket\. 

**Note**  
S3 on Outposts only supports the following metrics and not other Amazon S3 metrics\.  
Since S3 on Outposts have limited capcity, you can create CloudWatch alerts that alert you when storage utilization exceeds a threshold\.




| Metric | Description | 
| --- | --- | 
| OutpostTotalBytes |  The total provisioned capacity in bytes for an outpost  Units: bytes Period: 5 minutes  | 
| OutpostFreeBytes |  The count of free bytes available on outposts to store customer data\. Units: Bytes Period: 5 minutes  | 
| BucketUsedBytes |  The total size of all objects for the given bucket\. Units: Counts Period: 5 minutes  | 

## Amazon S3 CloudWatch Dimensions<a name="s3-cloudwatch-dimensions"></a>

The following dimensions are used to filter Amazon S3 metrics\.


|  Dimension  |  Description  | 
| --- | --- | 
|  BucketName  |  This dimension filters the data you request for the identified bucket only\.  | 
|  StorageType  |  This dimension filters the data that you have stored in a bucket by the following types of storage:  [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/cloudwatch-monitoring.html)  | 
| FilterId | This dimension filters metrics configurations that you specify for request metrics on a bucket, for example, a prefix or a tag\. You specify a filter id when you create a metrics configuration\. For more information, see [Metrics Configurations for Buckets](https://docs.aws.amazon.com/AmazonS3/latest/dev/metrics-configurations.html)\. | 

## Accessing CloudWatch metrics<a name="cloudwatch-monitoring-accessing"></a>

 You can use the following procedures to view the storage metrics for Amazon S3\. To get the Amazon S3 metrics involved, you must set a start and end timestamp\. For metrics for any given 24\-hour period, set the time period to 86400 seconds, the number of seconds in a day\. Also, remember to set the `BucketName` and `StorageType` dimensions\.

For example, if you use the AWS CLI to get the average of a specific bucket's size in bytes, you could use the following command\.

```
aws cloudwatch get-metric-statistics --metric-name BucketSizeBytes --namespace AWS/S3 --start-time 2016-10-19T00:00:00Z --end-time 2016-10-20T00:00:00Z --statistics Average --unit Bytes --region us-west-2 --dimensions Name=BucketName,Value=ExampleBucket Name=StorageType,Value=StandardStorage --period 86400 --output json
```

This example produces the following output\.

```
{
    "Datapoints": [
        {
            "Timestamp": "2016-10-19T00:00:00Z", 
            "Average": 1025328.0, 
            "Unit": "Bytes"
        }
    ], 
    "Label": "BucketSizeBytes"
}
```

**To view metrics using the CloudWatch console**

1. Open the CloudWatch console at [https://console\.aws\.amazon\.com/cloudwatch/](https://console.aws.amazon.com/cloudwatch/)\.

1. In the navigation pane, choose **Metrics**\. 

1. Choose the **S3** namespace\.

1. \(Optional\) To view a metric, enter the metric name in the search box\.

1. \(Optional\) To filter by the **StorageType** dimension, enter the name of the storage class in the search box\.

**To view a list of valid metrics stored for your AWS account using the AWS CLI**
+ At a command prompt, use the following command\.

  ```
  1. aws cloudwatch list-metrics --namespace "AWS/S3"
  ```

## Related resources<a name="cloudwatch-monitoring-related-resources"></a>
+ [Amazon CloudWatch Logs API Reference](https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/)
+ [Amazon CloudWatch User Guide](https://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/)
+ [list\-metrics](https://docs.aws.amazon.com/cli/latest/reference/cloudwatch/list-metrics.html) action in the *AWS CLI Command Reference*\.
+ [get\-metric\-statistics](https://docs.aws.amazon.com/cli/latest/reference/cloudwatch/get-metric-statistics.html) action in the *AWS CLI Command Reference*\.
+  [Metrics configurations for buckets](metrics-configurations.md)\.