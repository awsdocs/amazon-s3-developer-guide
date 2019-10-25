# Enabling Website Hosting<a name="EnableWebsiteHosting"></a>

Follow these steps to enable website hosting for your Amazon S3 buckets using the [Amazon S3 console](https://console.aws.amazon.com/s3/home):

**To enable website hosting for an S3 bucket**

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. In the **Bucket name** list, choose the bucket that you want to use for your static website\.

1. Choose **Properties**\.

1. Choose **Static website hosting**\.

1. Choose **Use this bucket to host a website**\.

1. Enter the name of your index document\. 

   For information about what an index document is, see [Configuring Index Document Support](IndexDocumentSupport.md)\.

1. \(Optional\) If you want to add a custom error document, in the **Error document** field, type the error document name, for example, `error.html`\. 

1. \(Optional\) If you want to specify advanced redirection rules, in **Edit redirection rules**, use XML to describe the rules\.

1. Choose **Save**\.