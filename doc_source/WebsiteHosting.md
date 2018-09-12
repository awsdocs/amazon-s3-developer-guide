# Hosting a Static Website on Amazon S3<a name="WebsiteHosting"></a>

You can host a static website on Amazon Simple Storage Service \(Amazon S3\)\. On a static website, individual webpages include static content\. They might also contain client\-side scripts\. By contrast, a dynamic website relies on server\-side processing, including server\-side scripts such as PHP, JSP, or ASP\.NET\. Amazon S3 does not support server\-side scripting\. Amazon Web Services \(AWS\) also has resources for hosting dynamic websites\. To learn more about website hosting on AWS, go to [Websites and Website Hosting](https://aws.amazon.com/websites/)\. 

**Topics**
+ [Website Endpoints](WebsiteEndpoints.md)
+ [Configuring a Bucket for Website Hosting](HowDoIWebsiteConfiguration.md)
+ [Example Walkthroughs \- Hosting Websites on Amazon S3](hosting-websites-on-s3-examples.md)

To host a static website, you configure an Amazon S3 bucket for website hosting, and then upload your website content to the bucket\. This bucket must have public read access\. It is intentional that everyone in the world will have read access to this bucket\. The website is then available at the AWS Region\-specific website endpoint of the bucket, which is in one of the following formats:

```
<bucket-name>.s3-website-<AWS-region>.amazonaws.com
```

```
<bucket-name>.s3-website.<AWS-region>.amazonaws.com
```

For a list of AWS Region\-specific website endpoints for Amazon S3, see [Website Endpoints](WebsiteEndpoints.md)\. For example, suppose you create a bucket called `examplebucket` in the US West \(Oregon\) Region, and configure it as a website\.  The following example URLs provide access to your website content: 
+ This URL returns a default index document that you configured for the website\.

  ```
  http://examplebucket.s3-website-us-west-2.amazonaws.com/
  ```
+ This URL requests the photo\.jpg object, which is stored at the root level in the bucket\.

  ```
  http://examplebucket.s3-website-us-west-2.amazonaws.com/photo.jpg
  ```
+ This URL requests the `docs/doc1.html` object in your bucket\. 

  ```
  http://examplebucket.s3-website-us-west-2.amazonaws.com/docs/doc1.html
  ```

**Using Your Own Domain**  
Instead of accessing the website by using an Amazon S3 website endpoint, you can use your own domain, such as `example.com` to serve your content\. Amazon S3, along with Amazon Route 53, supports hosting a website at the root domain\. For example, if you have the root domain `example.com` and you host your website on Amazon S3, your website visitors can access the site from their browser by typing either `http://www.example.com` or `http://example.com`\. For an example walkthrough, see [Example: Setting up a Static Website Using a Custom Domain](website-hosting-custom-domain-walkthrough.md)\. 

To configure a bucket for website hosting, you add website configuration to the bucket\. For more information, see [Configuring a Bucket for Website Hosting](HowDoIWebsiteConfiguration.md)\.