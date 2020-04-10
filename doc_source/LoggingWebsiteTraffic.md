# \(Optional\) Configuring Web Traffic Logging<a name="LoggingWebsiteTraffic"></a>

If you want to track the number of visitors accessing your website, you can optionally enable logging\. You enable server access logging for the bucket that you have configured as a static website\. For more information, see [Amazon S3 Server Access Logging](https://docs.aws.amazon.com/AmazonS3/latest/dev/ServerLogs.html)\. However, if you plan to use Amazon CloudFront to speed up your website, you can also use CloudFront logging\.

The following steps show you how to set up a log delivery bucket for server access logging and CloudFront logging and enable server access logging\. For more information about configuring CloudFront logging, see [Example: Speed Up Your Website with Amazon CloudFront](https://docs.aws.amazon.com/AmazonS3/latest/dev/website-hosting-cloudfront-walkthrough.html)\.

**To enable logging for your static website bucket**

1. Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. In the same Region where you created the bucket that is configured as a static website, create a bucket for logging, for example `logs.example.com`\.

1. Create a folder for the server access logging log files \(for example, `logs`\)\.

1. \(Optional\) If you want to use CloudFront to improve your website performance, create a folder for the CloudFront log files \(for example, `cdn`\)\.

1. In the **Bucket** list, choose your bucket\.

1. Choose **Properties**\.

1. Choose **Server access logging**\.

1. Choose **Enable logging**\.

1. For **Target bucket**, choose the bucket that you created for the log files, for example `logs-example-bucket`\.

1. For **Target prefix**, enter the name of the folder that you created for the log files followed by the delimiter \(/\), for example **logs/**\.

   When you set the **Target prefix**, you group your log data files in a folder so that they are easy to locate\.

1. Choose **Save**\.

   In your log bucket, you can now access your logs\. Amazon S3 writes website access logs to your log bucket every 2 hours\.

1. To view the logs, choose **Overview**, and choose the folder\. 