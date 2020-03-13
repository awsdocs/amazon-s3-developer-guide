# Enabling Website Hosting<a name="EnableWebsiteHosting"></a>

Follow these steps to enable website hosting for your Amazon S3 bucket using the [Amazon S3 console](https://console.aws.amazon.com/s3/home)\. For example walkthroughs that show you how to set up your website with optional configurations such as a custom domain, see [Example Walkthroughs \- Hosting Websites on Amazon S3](hosting-websites-on-s3-examples.md)\.

**To enable website hosting for an S3 bucket**

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. In the **Bucket name** list, choose the bucket that you want to use for your static website\.

1. Choose **Properties**\.

1. Choose **Static website hosting**\.

1. Choose **Use this bucket to host a website**\.

1. Enter the name of your index document\. 

   The index document name is typically `index.html`\. For information, see [Configuring Index Document Support](IndexDocumentSupport.md)\.

1. \(Optional\) If you want to add a custom error document, in the **Error document** field, enter the error document name \(for example, **error\.html**\)\. 

   For more information, see [\(Optional\) Configuring Custom Error Document Support](CustomErrorDocSupport.md)\.

1. \(Optional\) If you want to specify advanced redirection rules, in **Edit redirection rules**, use XML to describe the rules\.

   For more information, see [Advanced Conditional Redirects](how-to-page-redirect.md#advanced-conditional-redirects)\.

1. Choose **Save**\.