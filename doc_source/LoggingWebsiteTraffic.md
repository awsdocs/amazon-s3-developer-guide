# \(Optional\) Configuring Web Traffic Logging<a name="LoggingWebsiteTraffic"></a>

If you want to track the number of visitors who access your website, enable logging for the root domain bucket\. Enabling logging is optional\.

**To enable logging for your root domain bucket**

1. Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. Create a bucket for logging named `logs.example.com` in the same AWS Region that the `example.com` and `www.example.com` buckets were created in\.

1. Create two folders in the `logs.example.com` bucket; one named `root`, and the other named `cdn`\. If you configure Amazon CloudFront to speed up your website, you will use the `cdn` folder\.

1. In the **Bucket name** list, choose your root domain bucket, choose **Properties**, and then choose **Server access logging**\.

1. Choose **Enable logging**\.

1. For **Target bucket**, choose the bucket that you created for the log files, `logs.example.com`\.

1. For **Target prefix**, type **root/**\. This setting groups the log data files in the bucket in a folder named `root` so that they are easy to locate\.

1. Choose **Save**\.