# Programmatically Configuring a Bucket as a Static Website<a name="ManagingBucketWebsiteConfig"></a>

To host a static website on Amazon S3, you configure an Amazon S3 bucket for website hosting and then upload your website content to the bucket\. You can also use the AWS SDKs to create, update, and delete the website configuration programmatically\. The SDKs provide wrapper classes around the Amazon S3 REST API\. If your application requires it, you can send REST API requests directly from your application\. 

For more information about configuring your bucket for static website hosting using the AWS Management Console, see [Configuring a Bucket As a Static Website Using the AWS Management Console](HowDoIWebsiteConfiguration.md)\.

For more information about using the AWS CLI to configure an S3 bucket as a static website, see [website](https://docs.aws.amazon.com/cli/latest/reference/s3/website.html) in the *AWS CLI Command Reference*\. For more information about programmatically configuring an S3 bucket as a static website, see the following topics\. 

**Topics**
+ [Managing Websites with the AWS SDK for Java](ConfigWebSiteJava.md)
+ [Managing Websites with the AWS SDK for \.NET](ConfigWebSiteDotNet.md)
+ [Managing Websites with the AWS SDK for PHP](ConfigWebSitePHP.md)
+ [Managing Websites with the REST API](ConfigWebSiteREST.md)