# Enabling website hosting<a name="EnableWebsiteHosting"></a>

When you configure a bucket as a static website, you must enable static website hosting, configure an index document, and set permissions:

Follow these steps to enable website hosting for your Amazon S3 bucket using the [Amazon S3 console](https://console.aws.amazon.com/s3/home)\. You can also enable static website hosting using the REST API, the AWS SDKs, the AWS CLI, or AWS CloudFormation\. For more information, see the following:
+ [Configuring a static website using the REST API and AWS SDKs](https://docs.aws.amazon.com/AmazonS3/latest/dev/ManagingBucketWebsiteConfig.html)
+ [AWS::S3::Bucket WebsiteConfiguration](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-s3-websiteconfiguration.html) in the AWS CloudFormation User Guide
+ [put\-bucket\-website](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/s3api/put-bucket-website.html) in the AWS CLI Command Reference

To configure your website with a custom domain, see [Example walkthroughs \- Hosting websites on Amazon S3](hosting-websites-on-s3-examples.md)\.

**To enable static website hosting**

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. In the **Buckets** list, choose the name of the bucket that you want to enable static website hosting for\.

1. Choose **Properties**\.

1. Under **Static website hosting**, choose **Edit**\.

1. Choose **Use this bucket to host a website**\. 

1. Under **Static website hosting**, choose **Enable**\.

1. In **Index document**, enter the file name of the index document, typically `index.html`\. 

   The index document name is case sensitive and must exactly match the file name of the HTML index document that you plan to upload to your S3 bucket\. When you configure a bucket for website hosting, you must specify an index document\. Amazon S3 returns this index document when requests are made to the root domain or any of the subfolders\. For more information, see [Configuring an index document](https://docs.aws.amazon.com/AmazonS3/latest/dev/IndexDocumentSupport.html)\.

1. \(Optional\) If you want to provide your own custom error document for 4XX class errors, in **Error document**, enter the custom error document file name\. 

   The error document name is case sensitive and must exactly match the file name of the HTML error document that you plan to upload to your S3 bucket\. If you don't specify a custom error document and an error occurs, Amazon S3 returns a default HTML error document\. For more information, see [Configuring a custom error document](https://docs.aws.amazon.com/AmazonS3/latest/dev/CustomErrorDocSupport.html)\.

1. \(Optional\) If you want to specify advanced redirection rules, in **Redirection rules**, enter XML to describe the rules\.

   For example, you can conditionally route requests according to specific object key names or prefixes in the request\. For more information, see [Configuring advanced conditional redirects](https://docs.aws.amazon.com/AmazonS3/latest/dev/how-to-page-redirect.html#advanced-conditional-redirects)\.

1. Choose **Save changes**\.

   Amazon S3 enables static website hosting for your bucket\. At the bottom of the page, under **Static website hosting**, you see the website endpoint for your bucket\.

1. Under **Static website hosting**, note the **Endpoint**\.

   The **Endpoint** is the Amazon S3 website endpoint for your bucket\. After you finish configuring your bucket as a static website, you can use this endpoint to test your website\.

Next, you must configure your index document and set permissions\. For more information, see [Configuring an index document](IndexDocumentSupport.md) and [Setting permissions for website access](WebsiteAccessPermissionsReqd.md)\. You can also optionally configure an [error document](CustomErrorDocSupport.md), [web traffic logging](LoggingWebsiteTraffic.md), or a [redirect](how-to-page-redirect.md)\.