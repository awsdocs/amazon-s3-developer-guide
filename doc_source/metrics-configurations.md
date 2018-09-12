# Metrics Configurations for Buckets<a name="metrics-configurations"></a>

The CloudWatch request metrics for Amazon S3 enable you to receive 1\-minute CloudWatch metrics, set CloudWatch alarms, and access CloudWatch dashboards to view near real\-time operations and performance of your Amazon S3 storage\. For applications that depend on Cloud storage, these metrics let you quickly identify and act on operational issues\. When enabled, these 1\-minute metrics are available at the Amazon S3 bucket\-level, by default\.

You must create a metrics configuration for a bucket if you want to get the CloudWatch request metrics for the objects in that bucket\. You can also define a filter for the metrics collected –using a shared prefix or object tags– allowing you to align metrics filters to specific business applications, workflows, or internal organizations\.

For more information about the CloudWatch metrics that are available and the differences between storage and request metrics, see [Monitoring Metrics with Amazon CloudWatch](cloudwatch-monitoring.md)\.

Keep the following in mind when using metrics configurations:
+ You can have a maximum of 1000 metrics configurations per bucket\.
+ You can choose which objects in a bucket to include in metrics configurations by using filters\. Filtering on a shared prefix or object tag allows you to align metrics filters to specific business applications, workflows, or internal organizations\. To request metrics for the entire bucket, create a metrics configuration without a filter\.
+ Metrics configurations are necessary only to enable request metrics\. Bucket\-level daily storage metrics are always turned on, and are provided at no additional cost\. Currently, it's not possible to get daily storage metrics for a filtered subset of objects\.
+ Each metrics configuration enables the full set of [available request metrics](cloudwatch-monitoring.md#s3-request-cloudwatch-metrics)\. Operation\-specific metrics \(such as `PostRequests`\) will only be reported if there are requests of that type for your bucket or your filter\.
+ Request metrics are reported for object\-level operations, and are also reported for operations that list bucket contents, like [GET Bucket \(List Objects\)](http://docs.aws.amazon.com/AmazonS3/latest/API/v2-RESTBucketGET.html), [GET Bucket Object Versions](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETVersion.html), and [List Multipart Uploads](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadListMPUpload.html), but are not reported for other operations on buckets\.

## Best\-Effort CloudWatch Metrics Delivery<a name="metrics-configurations-delivery"></a>

 CloudWatch metrics are delivered on a best\-effort basis\. Most requests for an Amazon S3 object that have request metrics result in a data point being sent to CloudWatch\.

The completeness and timeliness of metrics is not guaranteed\. The data point for a particular request might be returned with a time stamp that is later than when the request was actually processed\. Or the data point for a minute might be delayed before being available through CloudWatch, or it might not be delivered at all\. CloudWatch request metrics give you an idea of the nature of traffic against your bucket in near real time\. It is not meant to be a complete accounting of all requests\.

It follows from the best\-effort nature of this feature that the reports available at the [Billing & Cost Management Dashboard](https://console.aws.amazon.com/billing/home?#/) might include one or more access requests that do not appear in the bucket metrics\.

## Filtering Metrics Configurations<a name="metrics-configurations-filter"></a>

When working with CloudWatch metric configurations, you have the option of filtering the configuration into groups of related objects within a single bucket\. You can filter objects in a bucket for inclusion in a metrics configuration based on one or more of the following elements:
+ **Object key name prefix** – While the Amazon S3 data model is a flat structure, you can infer hierarchy by using a prefix\. The Amazon S3 console supports these prefixes with the concept of folders\. If you filter by prefix, objects that have the same prefix are included in the metrics configuration\.
+ **Tag** – You can add tags, key value name pairs, to objects\. Tags allow you to find and organize objects easily\. These tags can also be used as a filter for metrics configurations\.

If you specify a filter, only requests that operate on single objects can match the filter and be included in the reported metrics\. Requests like [Delete Multiple Objects](http://docs.aws.amazon.com/AmazonS3/latest/API/multiobjectdeleteapi.html) and List requests don't return any metrics for configurations with filters\.

To request more complex filtering, choose two or more elements\. Only objects that have all of those elements are included in the metrics configuration\. If you don't set filters, all of the objects in the bucket are included in the metrics configuration\.

## How to Add Metrics Configurations<a name="add-metrics-configurations"></a>

You can add metrics configurations to a bucket through the Amazon S3 console, with the AWS CLI, or with the Amazon S3 REST API\. For information about how to do this in the AWS Management Console, see the [How Do I Configure Request Metrics for an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/configure-metrics.html) in the Amazon Simple Storage Service Console User Guide\.

**Add Metrics Configurations with the AWS CLI**

1. Install and set up the AWS CLI\. For instructions, see [Getting Set Up with the AWS Command Line Interface](http://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-set-up.html) in the *AWS Command Line Interface User Guide*\.

1. Open a terminal\.

1. Run the following command to add a metrics configuration:

   ```
   aws s3api put-bucket-metrics-configuration --endpoint http://s3-us-west-2.amazonaws.com --bucket bucket-name --id metrics-config-id --metrics-configuration '{"Id":" metrics-config-id ","Filter":{"Prefix":"prefix1"}}'
   ```

1. To verify that the configuration was added, execute the following command:

   ```
   aws s3api get-bucket-metrics-configuration --endpoint http://s3-us-west-2.amazonaws.com --bucket bucket-name --id metrics-config-id
   ```

   This returns the following response:

   ```
   {
       "MetricsConfiguration": {
           "Filter": {
               "Prefix": "prefix1"
           },
           "Id": "metrics-config-id"
       }
   }
   ```

You can also add metrics configurations programmatically with the Amazon S3 REST API\. For more information, see the following topics in the Amazon Simple Storage Service API Reference:
+ [PUT Bucket Metric Configuration](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTMetricConfiguration.html)
+ [GET Bucket Metric Configuration](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETMetricConfiguration.html)
+ [List Bucket Metric Configuration](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTListBucketMetricsConfiguration.html)
+ [DELETE Bucket Metric Configuration](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTDeleteBucketMetricsConfiguration.html)