# \(Optional\) configuring a custom error document<a name="CustomErrorDocSupport"></a>

After you configure your bucket as a static website, when an error occurs, Amazon S3 returns an HTML error document\. You can optionally configure your bucket with a custom error document so that Amazon S3 returns that document when an error occurs\. 

**Note**  
Some browsers display their own error message when an error occurs, ignoring the error document that Amazon S3 returns\. For example, when an HTTP 404 Not Found error occurs, Google Chrome might ignore the error document that Amazon S3 returns and display its own error\.

**Topics**
+ [Amazon S3 HTTP response codes](#s3-http-error-codes)
+ [Configuring a custom error document](#custom-error-document)

## Amazon S3 HTTP response codes<a name="s3-http-error-codes"></a>

The following table lists the subset of HTTP response codes that Amazon S3 returns when an error occurs\. 


| HTTP error code | Description | 
| --- | --- | 
| 301 Moved Permanently | When a user sends a request directly to the Amazon S3 website endpoint \(http://s3\-website\.Region\.amazonaws\.com/\), Amazon S3 returns a 301 Moved Permanently response and redirects those requests to https://aws\.amazon\.com/s3/\. | 
| 302 Found |  When Amazon S3 receives a request for a key `x`, `http://bucket-name.s3-website.Region.amazonaws.com/x`, without a trailing slash, it first looks for the object with the key name `x`\. If the object is not found, Amazon S3 determines that the request is for subfolder `x` and redirects the request by adding a slash at the end, and returns **302 Found**\.   | 
| 304 Not Modified |  Amazon S3 users request headers `If-Modified-Since`, `If-Unmodified-Since`, `If-Match` and/or `If-None-Match` to determine whether the requested object is same as the cached copy held by the client\. If the object is the same, the website endpoint returns a **304 Not Modified** response\.  | 
| 400 Malformed Request |  The website endpoint responds with a **400 Malformed Request** when a user attempts to access a bucket through the incorrect regional endpoint\.   | 
| 403 Forbidden |  The website endpoint responds with a **403 Forbidden** when a user request translates to an object that is not publicly readable\. The object owner must make the object publicly readable using a bucket policy or an ACL\.   | 
| 404 Not Found |  The website endpoint responds with **404 Not Found** for the following reasons: [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/CustomErrorDocSupport.html) You can create a custom document that is returned for **404 Not Found**\. Make sure that the document is uploaded to the bucket configured as a website, and that the website hosting configuration is set to use the document\. For information on how Amazon S3 interprets the URL as a request for an object or an index document, see [Configuring an index document](IndexDocumentSupport.md)\.   | 
| 500 Service Error |  The website endpoint responds with a **500 Service Error** when an internal server error occurs\.  | 
| 503 Service Unavailable |  The website endpoint responds with a **503 Service Unavailable** when Amazon S3 determines that you need to reduce your request rate\.   | 

 For each of these errors, Amazon S3 returns a predefined HTML message\. The following is an example HTML message that is returned for a **403 Forbidden** response\.

![\[403 Forbidden error message example\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/WebsiteErrorExample403.png)

## Configuring a custom error document<a name="custom-error-document"></a>

When you configure your bucket as a static website, you can optionally provide a custom error document that contains a user\-friendly error message and additional help\. Amazon S3 returns your custom error document for only the HTTP 4XX class of error codes\. 

**To configure a custom error document**

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. In the **Buckets** list, choose your bucket name\.

1. Choose **Properties**\.

1. Choose **Static website hosting**\.

   If your bucket is already configured as a static website, you can follow the next step to update or add error document information\. If you have not configured your bucket as a static website, you must first set up the required configuration\. For more information, see [Enabling website hosting](EnableWebsiteHosting.md)\.

1. In the **Error document** box, enter your error document name\.

1. Choose **Save**\.

For more information about using the REST API to configure your bucket as a static website with a custom error document, see [PutBucketWebsite](https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutBucketWebsite.html) in the *Amazon Simple Storage Service API Reference*\.