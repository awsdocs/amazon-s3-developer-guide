# Monitoring Metrics with Amazon CloudWatch<a name="cloudwatch-monitoring"></a>

Amazon CloudWatch metrics for Amazon S3 can help you understand and improve the performance of applications that use Amazon S3\. There are two ways that you can use CloudWatch with Amazon S3\.
+ **Daily Storage Metrics for Buckets** ‐ You can monitor bucket storage using CloudWatch, which collects and processes storage data from Amazon S3 into readable, daily metrics\. These storage metrics for Amazon S3 are reported once per day and are provided to all customers at no additional cost\.
+ **Request metrics** ‐ You can choose to monitor Amazon S3 requests to quickly identify and act on operational issues\. The metrics are available at 1 minute intervals after some latency to process\. These CloudWatch metrics are billed at the same rate as the Amazon CloudWatch Custom Metrics\. For information on CloudWatch pricing, see [Amazon CloudWatch Pricing](https://aws.amazon.com/cloudwatch/pricing/)\. To learn more about how to opt\-in to getting these metrics, see [Metrics Configurations for Buckets](metrics-configurations.md)\.

  When enabled, request metrics are reported for all object operations\. By default, these 1\-minute metrics are available at the Amazon S3 bucket level\. You can also define a filter for the metrics collected –using a shared prefix or object tag– allowing you to align metrics filters to specific business applications, workflows, or internal organizations\.

All CloudWatch statistics are retained for a period of fifteen months, so that you can access historical information and gain a better perspective on how your web application or service is performing\. For more information on CloudWatch, see [What Are Amazon CloudWatch, Amazon CloudWatch Events, and Amazon CloudWatch Logs?](http://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/WhatIsCloudWatch.html) in the *Amazon CloudWatch User Guide*\.

## Metrics and Dimensions<a name="metrics-dimensions"></a>

The storage metrics and dimensions that Amazon S3 sends to CloudWatch are listed below\.

## Amazon S3 CloudWatch Daily Storage Metrics for Buckets<a name="s3-cloudwatch-metrics"></a>

The `AWS/S3` namespace includes the following daily storage metrics for buckets\.


| Metric | Description | 
| --- | --- | 
| BucketSizeBytes |  The amount of data in bytes stored in a bucket in the Standard storage class, Standard \- Infrequent Access \(Standard\_IA\) storage class, OneZone \- Infraquent Access \(OneZone\_IA\), Reduced Redundancy Storage \(RRS\) class, or Glacier \(GLACIER\) storage class Valid storage type filters: `StandardStorage`, `GlacierS3ObjectOverhead`, `StandardIAStorage`, `StandardIAObjectOverhead`, `OneZoneIAStorage`, `OneZoneIAObjectOverhead`, `ReducedRedundancyStorage`, `GlacierStorage`, and `GlacierObjectOverhead` \(see the `StorageType` dimension\) Units: Bytes Valid statistics: Average  | 
| NumberOfObjects |  The total number of objects stored in a bucket for all storage classes Valid storage type filters: `AllStorageTypes` \(see the `StorageType` dimension\) Units: Count Valid statistics: Average  | 

The `AWS/S3` namespace includes the following request metrics\.

## Amazon S3 CloudWatch Request Metrics<a name="s3-request-cloudwatch-metrics"></a>


| Metric | Description | 
| --- | --- | 
| AllRequests |  The total number of HTTP requests made to an Amazon S3 bucket, regardless of type\. If you’re using a metrics configuration with a filter, then this metric only returns the HTTP requests made to the objects in the bucket that meet the filter's requirements\. Units: Count Valid statistics: Sum  | 
| GetRequests |  The number of HTTP GET requests made for objects in an Amazon S3 bucket\. This doesn't include list operations\. Units: Count Valid statistics: Sum  Paginated list\-oriented requests, like [List Multipart Uploads](http://docs.aws.amazon.com/AmazonS3/latest/API//mpUploadListMPUpload.html), [List Parts](http://docs.aws.amazon.com/AmazonS3/latest/API//mpUploadListParts.html), [Get Bucket Object versions](http://docs.aws.amazon.com/AmazonS3/latest/API//RESTBucketGETVersion.html), and others, are not included in this metric\.   | 
| PutRequests |  The number of HTTP PUT requests made for objects in an Amazon S3 bucket\. Units: Count Valid statistics: Sum  | 
| DeleteRequests |  The number of HTTP DELETE requests made for objects in an Amazon S3 bucket\. This also includes [Delete Multiple Objects](http://docs.aws.amazon.com/AmazonS3/latest/API/multiobjectdeleteapi.html) requests\. This metric shows the number of requests, not the number of objects deleted\. Units: Count Valid statistics: Sum  | 
| HeadRequests |  The number of HTTP HEAD requests made to an Amazon S3 bucket\. Units: Count Valid statistics: Sum  | 
| PostRequests |  The number of HTTP POST requests made to an Amazon S3 bucket\. Units: Count Valid statistics: Sum  [Delete Multiple Objects](http://docs.aws.amazon.com/AmazonS3/latest/API/multiobjectdeleteapi.html) and [SELECT Object Content](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectSELECTContent.html) requests are not included in this metric\.    | 
| SelectRequests |  The number of Amazon S3 [SELECT Object Content](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectSELECTContent.html) requests made for objects in an Amazon S3 bucket\.  Units: Count Valid statistics: Sum  | 
| SelectScannedBytes |  The number of bytes of data scanned with Amazon S3 [SELECT Object Content](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectSELECTContent.html) requests in an Amazon S3 bucket\.   Units: Bytes  Valid statistics: Average \(bytes per request\), Sum \(bytes per period\), Sample Count, Min, Max  | 
| SelectReturnedBytes |  The number of bytes of data returned with Amazon S3 [SELECT Object Content](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectSELECTContent.html) requests in an Amazon S3 bucket\.   Units: Bytes  Valid statistics: Average \(bytes per request\), Sum \(bytes per period\), Sample Count, Min, Max  | 
| ListRequests |  The number of HTTP requests that list the contents of a bucket\. Units: Count Valid statistics: Sum  | 
| BytesDownloaded |  The number bytes downloaded for requests made to an Amazon S3 bucket, where the response includes a body\. Units: Bytes Valid statistics: Average \(bytes per request\), Sum \(bytes per period\), Sample Count, Min, Max  | 
| BytesUploaded |  The number bytes uploaded that contain a request body, made to an Amazon S3 bucket\. Units: Bytes Valid statistics: Average \(bytes per request\), Sum \(bytes per period\), Sample Count, Min, Max  | 
| 4xxErrors |  The number of HTTP 4xx client error status code requests made to an Amazon S3 bucket with a value of either 0 or 1\. The `average` statistic shows the error rate, and the `sum` statistic shows the count of that type of error, during each period\. Units: Count Valid statistics: Average \(reports per request\), Sum \(reports per period\), Min, Max, Sample Count  | 
| 5xxErrors |  The number of HTTP 5xx server error status code requests made to an Amazon S3 bucket with a value of either 0 or 1\. The `average` statistic shows the error rate, and the `sum` statistic shows the count of that type of error, during each period\. Units: Counts Valid statistics: Average \(reports per request\), Sum \(reports per period\), Min, Max, Sample Count  | 
| FirstByteLatency |  The per\-request time from the complete request being received by an Amazon S3 bucket to when the response starts to be returned\. Units: Milliseconds Valid statistics: Average, Sum, Min, Max, Sample Count  | 
| TotalRequestLatency |  The elapsed per\-request time from the first byte received to the last byte sent to an Amazon S3 bucket\. This includes the time taken to receive the request body and send the response body, which is not included in `FirstByteLatency`\. Units: Milliseconds Valid statistics: Average, Sum, Min, Max, Sample Count  | 

## Amazon S3 CloudWatch Dimensions<a name="s3-cloudwatch-dimensions"></a>

The following dimensions are used to filter Amazon S3 metrics\.


|  Dimension  |  Description  | 
| --- | --- | 
|  BucketName  |  This dimension filters the data you request for the identified bucket only\.  | 
|  StorageType  |  This dimension filters the data that you have stored in a bucket by the type of storage\. The types are `StandardStorage` for the STANDARD storage class, `StandardIAStorage` for the STANDARD\_IA storage class, `OneZoneIAStorage` for the ONEZONE\_IA storage class, `ReducedRedundancyStorage` for the REDUCED\_REDUNDANCY storage class, `GlacierStorage` for the GLACIER storage class, and `AllStorageTypes`\. The `AllStorageTypes` type includes the STANDARD, STANDARD\_IA, ONEZONE\_IA, REDUCED\_REDUNDANCY, and GLACIER storage classes\.   | 
| FilterId | This dimension filters metrics configurations that you specify for request metrics on a bucket, for example, a prefix or a tag\. You specify a filter id when you create a metrics configuration\. For more information, see [Metrics Configurations for Buckets](http://docs.aws.amazon.com/AmazonS3/latest/dev/metrics-configurations.html)\. | 

## Accessing CloudWatch Metrics<a name="cloudwatch-monitoring-accessing"></a>

 You can use the following procedures to view the storage metrics for Amazon S3\. Note that to get the Amazon S3 metrics involved, you must set a start and end time stamp\. For metrics for any given 24\-hour period, set the time period to 86400 seconds, the number of seconds in a day\. Also, remember to set the `BucketName` and `StorageType` dimensions\.

For example, if you use the AWS CLI to get the average of a specific bucket's size, in bytes, you could use the following command:

```
aws cloudwatch get-metric-statistics --metric-name BucketSizeBytes --namespace AWS/S3 --start-time 2016-10-19T00:00:00Z --end-time 2016-10-20T00:00:00Z --statistics Average --unit Bytes --region us-west-2 --dimensions Name=BucketName,Value=ExampleBucket Name=StorageType,Value=StandardStorage --period 86400 --output json
```

This example produces the following output:

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

1. \(Optional\) To view a metric, type the metric name in the search field\.

1. \(Optional\) To filter by the **StorageType** dimension, type the name of the storage class in the search field\.

**To view a list of valid metrics stored for your AWS account using the AWS CLI**
+ At a command prompt, use the following command:

  ```
  1. aws cloudwatch list-metrics --namespace "AWS/S3"
  ```

## Related Resources<a name="cloudwatch-monitoring-related-resources"></a>
+ [Amazon CloudWatch Logs API Reference](http://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/)
+ [Amazon CloudWatch User Guide](http://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/)
+ [list\-metrics](http://docs.aws.amazon.com/cli/latest/reference/cloudwatch/list-metrics.html) action in the *AWS CLI Command Reference*\.
+ [get\-metric\-statistics](http://docs.aws.amazon.com/cli/latest/reference/cloudwatch/get-metric-statistics.html) action in the *AWS CLI Command Reference*\.
+  [Metrics Configurations for Buckets](metrics-configurations.md)\.