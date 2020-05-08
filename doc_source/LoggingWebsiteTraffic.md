# \(Optional\) Logging web traffic<a name="LoggingWebsiteTraffic"></a>

You can optionally enable Amazon S3 server access logging for a bucket that is configured as a static website\. Server access logging provides detailed records for the requests that are made to your bucket\. For more information, see [Amazon S3 server access logging](ServerLogs.md)\. If you plan to use Amazon CloudFront to [speed up your website](website-hosting-cloudfront-walkthrough.md), you can also use CloudFront logging\. For more information, see [Configuring and Using Access Logs](https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/AccessLogs.html) in the *Amazon CloudFront Developer Guide*\.

**To enable server access logging for your static website bucket**

1. Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. In the same Region where you created the bucket that is configured as a static website, create a bucket for logging, for example `logs.example.com`\.

1. Create a folder for the server access logging log files \(for example, `logs`\)\.

1. \(Optional\) If you want to use CloudFront to improve your website performance, create a folder for the CloudFront log files \(for example, `cdn`\)\.

1. In the **Bucket** list, choose your bucket\.

1. Choose **Properties**\.

1. Choose **Server access logging**\.

1. Choose **Enable logging**\.

1. For **Target bucket**, choose the bucket that you created for the log files, for example `logs.example.com`\.

1. For **Target prefix**, enter the name of the folder that you created for the log files followed by the delimiter \(/\), for example **logs/**\.

   When you set the **Target prefix**, you group your log data files in a folder so that they are easy to locate\.

1. Choose **Save**\.

   In your log bucket, you can now access your logs\. Amazon S3 writes website access logs to your log bucket every 2 hours\.

1. To view the logs, choose **Overview**, and choose the folder\. 