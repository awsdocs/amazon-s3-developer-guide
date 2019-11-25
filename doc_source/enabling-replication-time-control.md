# Enabling S3 Replication Time Control<a name="enabling-replication-time-control"></a>

You can start using S3 Replication Time Control \(S3 RTC\) with a new or existing replication rule\. You can choose to apply your replication rule to an entire S3 bucket, or to Amazon S3 objects with a specific prefix or tag\. When you enable S3 RTC, replication metrics are also enabled on your replication rule\. 

**Note**  
 Replication metrics are billed at the same rate as Amazon CloudWatch custom metrics\. For information, see [Amazon CloudWatch pricing](https://aws.amazon.com/cloudwatch/pricing/)\. 

You can configure replication time control using the [AWS Management Console](https://console.aws.amazon.com/s3/), the [Amazon Simple Storage Service API Reference](https://docs.aws.amazon.com/AmazonS3/latest/API/), [AWS SDK](https://docs.aws.amazon.com/AmazonS3/latest/dev/UsingAWSSDK.html), and the [AWS Command Line Interface \(AWS CLI\)](https://docs.aws.amazon.com/cli/latest/reference/)\.