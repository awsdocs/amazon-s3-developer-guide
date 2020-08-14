# Enabling S3 Replication Time Control<a name="enabling-replication-time-control"></a>

You can start using S3 Replication Time Control \(S3 RTC\) with a new or existing replication rule\. You can choose to apply your replication rule to an entire S3 bucket, or to Amazon S3 objects with a specific prefix or tag\. When you enable S3 RTC, replication metrics are also enabled on your replication rule\. 

 Replication configurations with S3 Replication Time Control \(S3 RTC\) specified are written in the latest XML V2\. XML V2 replication configurations are those that contain the `Filter` element for rules\. In V2 replication configurations, Amazon S3 doesn't replicate delete markers\. Therefore, you must set the `DeleteMarkerReplication` element to `Disabled`\.

**Note**  
 Replication metrics are billed at the same rate as Amazon CloudWatch custom metrics\. For information, see [Amazon CloudWatch pricing](https://aws.amazon.com/cloudwatch/pricing/)\. 

You can configure Replication Time Control using the [Amazon S3 console](https://console.aws.amazon.com/s3/), the [Amazon S3 API](https://docs.aws.amazon.com/AmazonS3/latest/API/), the [AWS SDKs](https://docs.aws.amazon.com/AmazonS3/latest/dev/UsingAWSSDK.html), and the [AWS Command Line Interface \(AWS CLI\)](https://docs.aws.amazon.com/cli/latest/reference/)\.

For more information, see [Replication configuration overview](replication-add-config.md)\.