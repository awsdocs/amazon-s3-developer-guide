# Hosting a Static Website on Amazon S3<a name="WebsiteHosting"></a>

You can host a static website on Amazon S3\. On a static website, individual webpages include static content\. They might also contain client\-side scripts\. By contrast, a dynamic website relies on server\-side processing, including server\-side scripts such as PHP, JSP, or ASP\.NET\. Amazon S3 does not support server\-side scripting\. AWS also has resources for hosting dynamic websites\. To learn more about website hosting on AWS, see [Web Hosting](https://aws.amazon.com/websites/)\. 

**Topics**
+ [Website Endpoints](WebsiteEndpoints.md)
+ [Configuring a Bucket for Website Hosting](HowDoIWebsiteConfiguration.md)
+ [Example Walkthroughs \- Hosting Websites on Amazon S3](hosting-websites-on-s3-examples.md)

To host a static website, you configure an Amazon S3 bucket for website hosting and then upload your website content to the bucket\. For more information, see [Configuring a Bucket for Website Hosting](HowDoIWebsiteConfiguration.md)\. This bucket must have public read access\. It is intentional that everyone in the world will have read access to this bucket\. To learn how to configure public read access for your bucket, see [Permissions Required for Website Access](WebsiteAccessPermissionsReqd.md)\. The website is then available at the AWS Region\-specific website endpoint of the bucket\. For a complete list of Amazon S3 endpoints, see [Amazon S3 Website Endpoints](https://docs.aws.amazon.com/general/latest/gr/s3.html#s3_website_region_endpoint)\.

Amazon S3 Region\-specific website endpoints follow this format:

```
http://bucket-name.s3-website.Region.amazonaws.com
```

For example, if you create a bucket named `example-bucket` in the US West \(Oregon\) Region, your website is available at the following URL:

```
http://example-bucket.s3-website.us-west-2.amazonaws.com
```

This URL will return the default index document that you configured for the website\.

**Example Requesting an Object at the Root Level**  
To request a specific object that is stored at the root level in the bucket, use the following URL structure:  

```
http://bucket-name.s3-website.Region.amazonaws.com/object-name
```
For example, this URL requests the `photo.jpg` object that is stored at the root level in the bucket:  

```
http://example-bucket.s3-website.us-west-2.amazonaws.com/photo.jpg
```

**Example Requesting an Object in a Prefix**  
To request an object that is stored in a folder in your bucket, use this URL structure:  

```
http://bucket-name.s3-website.Region.amazonaws.com/folder-name/object-name
```
This URL requests the `docs/doc1.html` object in your bucket\.   

```
http://example-bucket.s3-website.us-west-2.amazonaws.com/docs/doc1.html
```

Using Your Own Domain Instead of accessing the website by using an Amazon S3 website endpoint, you can use your own domain, such as `example.com`, to serve your content\. Amazon S3, along with Amazon Route 53, supports hosting a website at the root domain\. For example, if you have the root domain `example.com` and you host your website on Amazon S3, your website visitors can access the site from their browser by typing either `http://www.example.com` or `http://example.com`\. For an example walkthrough, see [Example: Setting Up a Static Website Using a Custom Domain](website-hosting-custom-domain-walkthrough.md)\. 

**Note**  
The Amazon S3 website endpoints do not support HTTPS\. For information about using HTTPS with an Amazon S3 bucket, see the following:   
[How do I use CloudFront to serve HTTPS requests for my Amazon S3 bucket?](https://aws.amazon.com/premiumsupport/knowledge-center/cloudfront-https-requests-s3)
[Requiring HTTPS for Communication Between CloudFront and Your Amazon S3 Origin](https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/using-https-cloudfront-to-s3-origin.html)