# Enabling website hosting<a name="EnableWebsiteHosting"></a>

When you configure a bucket as a static website, you must enable static website hosting, configure an index document, and set permissions:

Follow these steps to enable website hosting for your Amazon S3 bucket using the [Amazon S3 console](https://console.aws.amazon.com/s3/home)\. For more information about next steps, see [Configuring an index document](IndexDocumentSupport.md) and [Setting permissions for website access](WebsiteAccessPermissionsReqd.md)\. To configure your website with a custom domain, see [Example walkthroughs \- hosting websites on Amazon S3](hosting-websites-on-s3-examples.md)\.

**To enable static website hosting**

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. In the **Bucket name** list, choose the bucket that you want to use for your static website\.

1. Choose **Properties**\.

1. Choose **Static website hosting**\.

1. Choose **Use this bucket to host a website**\.

1. Enter the name of your index document\. 

   The index document name is typically `index.html`\. The index document name is case sensitive and must exactly match the file name of the HTML index document that you plan to upload to your S3 bucket\. For more information, see [Configuring an index document](IndexDocumentSupport.md)\.

1. \(Optional\) If you want to add a custom error document, in the **Error document** box, enter the key name for the error document \(for example, **error\.html**\)\. 

   The error document name is case sensitive and must exactly match the file name of the HTML error document that you plan to upload to your S3 bucket\. For more information, see [\(Optional\) configuring a custom error document](CustomErrorDocSupport.md)\.

1. \(Optional\) If you want to specify advanced redirection rules, in **Edit redirection rules**, use XML to describe the rules\.

   For more information, see [Configuring advanced conditional redirects](how-to-page-redirect.md#advanced-conditional-redirects)\.

1. Under **Static website hosting**, note the **Endpoint**\.

   The **Endpoint** is the Amazon S3 website endpoint for your bucket\. After you finish configuring your bucket as a static website, you can use this endpoint to test your website\.

1. Choose **Save**\.

Next, you must configure your index document and set permissions\. For more information, see [Configuring an index document](IndexDocumentSupport.md) and [Setting permissions for website access](WebsiteAccessPermissionsReqd.md)\. You can also optionally configure an [error document](CustomErrorDocSupport.md), [web traffic logging](LoggingWebsiteTraffic.md), or a [redirect](how-to-page-redirect.md)\.