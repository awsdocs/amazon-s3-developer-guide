# \(Optional\) Logging web traffic<a name="LoggingWebsiteTraffic"></a>

You can optionally enable Amazon S3 server access logging for a bucket that is configured as a static website\. Server access logging provides detailed records for the requests that are made to your bucket\. For more information, see [Amazon S3 server access logging](ServerLogs.md)\. If you plan to use Amazon CloudFront to [speed up your website](website-hosting-cloudfront-walkthrough.md), you can also use CloudFront logging\. For more information, see [Configuring and Using Access Logs](https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/AccessLogs.html) in the *Amazon CloudFront Developer Guide*\.

**To enable server access logging for your static website bucket**

1. Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. In the same Region where you created the bucket that is configured as a static website, create a bucket for logging, for example `logs.example.com`\.

1. Create a folder for the server access logging log files \(for example, `logs`\)\.

   When you group your log data files in a folder, they are easier to locate\.

1. \(Optional\) If you want to use CloudFront to improve your website performance, create a folder for the CloudFront log files \(for example, `cdn`\)\.

1. In the **Buckets** list, choose your bucket\.

1. Choose **Properties**\.

1. Under **Server access logging**, choose **Edit**\.

1. Choose **Enable**\.

1. Under the **Target bucket**, choose the bucket and folder destination for the server access logs:
   + Browse to the folder and bucket location:

     1. Choose **Browse S3**\.

     1. Choose the bucket name, and then choose the logs folder\. 

     1. Choose **Choose path**\.
   + Enter the S3 bucket path, for example, **s3://logs\.example\.com/logs/**\.

1. Choose **Save changes**\.

   In your log bucket, you can now access your logs\. Amazon S3 writes website access logs to your log bucket every 2 hours\.