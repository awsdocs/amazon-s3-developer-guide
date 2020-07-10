# Hosting a static website on Amazon S3<a name="WebsiteHosting"></a>

You can use Amazon S3 to host a static website\. On a *static* website, individual webpages include static content\. They might also contain client\-side scripts\.

By contrast, a *dynamic* website relies on server\-side processing, including server\-side scripts such as PHP, JSP, or ASP\.NET\. Amazon S3 does not support server\-side scripting, but AWS has other resources for hosting dynamic websites\. To learn more about website hosting on AWS, see [Web Hosting](https://aws.amazon.com/websites/)\. 

**Note**  
You can use the AWS Amplify Console to host a single page web app\. The AWS Amplify Console supports single page apps built with single page app frameworks \(for example, React JS, Vue JS, Angular JS, and Nuxt\) and static site generators \(for example, Gatsby JS, React\-static, Jekyll, and Hugo\)\. For more information, see [Getting Started](https://docs.aws.amazon.com/amplify/latest/userguide/getting-started.html) in the *AWS Amplify Console User Guide*\.

To configure your bucket for static website hosting, you can use the AWS Management Console without writing any code\. You can also create, update, and delete the website configuration *programmatically* by using the AWS SDKs\. The SDKs provide wrapper classes around the Amazon S3 REST API\. If your application requires it, you can send REST API requests directly from your application\.

To host a static website on Amazon S3, you configure an Amazon S3 bucket for website hosting and then upload your website content to the bucket\. When you configure a bucket as a static website, you must [enable website hosting](EnableWebsiteHosting.md), [set permissions](WebsiteAccessPermissionsReqd.md), and [create and add an index document](IndexDocumentSupport.md)\. Depending on your website requirements, you can also configure [redirects](how-to-page-redirect.md), [web traffic logging](LoggingWebsiteTraffic.md), and a [custom error document](CustomErrorDocSupport.md)\.

After you configure your bucket as a static website, you can access the bucket through the AWS Region\-specific Amazon S3 website endpoints for your bucket\. Website endpoints are different from the endpoints where you send REST API requests\. For more information, see [Website endpoints](WebsiteEndpoints.md)\. Amazon S3 doesn't support HTTPS access for website endpoints\. If you want to use HTTPS, you can use CloudFront to serve a static website hosted on Amazon S3\. For more information, see [Speeding up your website with Amazon CloudFront](website-hosting-cloudfront-walkthrough.md)\.

For more information about hosting a static website on Amazon S3, including instructions and step\-by\-step walkthroughs, see the following topics:

**Topics**
+ [Website endpoints](WebsiteEndpoints.md)
+ [Configuring a bucket as a static website using the AWS Management Console](HowDoIWebsiteConfiguration.md)
+ [Programmatically configuring a bucket as a static website](ManagingBucketWebsiteConfig.md)
+ [Example walkthroughs \- hosting websites on Amazon S3](hosting-websites-on-s3-examples.md)