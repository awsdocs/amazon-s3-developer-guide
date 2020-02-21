# Website Endpoints<a name="WebsiteEndpoints"></a>

When you configure a bucket for website hosting, the website is available via the Region\-specific website endpoint\. Website endpoints are different from the endpoints where you send REST API requests\. For more information about the differences between the endpoints, see [Key Differences Between the Amazon Website and the REST API Endpoint](#WebsiteRestEndpointDiff)\.

**Note**  
The Amazon S3 website endpoints do not support HTTPS\. For information about using HTTPS with an Amazon S3 bucket, see the following:  
[How do I use CloudFront to serve HTTPS requests for my Amazon S3 bucket?](https://aws.amazon.com/premiumsupport/knowledge-center/cloudfront-https-requests-s3)
[Requiring HTTPS for Communication Between CloudFront and Your Amazon S3 Origin](https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/using-https-cloudfront-to-s3-origin.html)

Depending on your Region, Amazon S3 website endpoints follow one of these two formats:

```
http://bucket-name.s3-website.Region.amazonaws.com
```

```
http://bucket-name.s3-website-Region.amazonaws.com
```

For a complete list of Amazon S3 website endpoints, see [Amazon S3 Website Endpoints](https://docs.aws.amazon.com/general/latest/gr/s3.html#s3_website_region_endpoints)\.

For your customers to access content at the website endpoint, you must make all your content publicly readable\. To do so, you can edit block public access settings for the account\. Then, you can use a bucket policy or an access control list \(ACL\) on an object to grant the necessary permissions\. For more information, see [Permissions Required for Website Access](WebsiteAccessPermissionsReqd.md)\.

**Note**  
Requester Pays buckets  do not allow access through the website endpoint\. Any request to such a bucket receives a `403 Access Denied` response\. For more information, see [Requester Pays Buckets](RequesterPaysBuckets.md)\.

If you have a registered domain, you can add a DNS CNAME entry to point to the Amazon S3 website endpoint\. For example, if you have registered domain, `www.example-bucket.com`, you could create a bucket `www.example-bucket.com`, and add a DNS CNAME record that points to `www.example-bucket.com.s3-website.Region.amazonaws.com`\. All requests to `http://www.example-bucket.com` are routed to `www.example-bucket.com.s3-website.Region.amazonaws.com`\. For more information, see [Virtual Hosting of Buckets](VirtualHosting.md)\. 

## Key Differences Between the Amazon Website and the REST API Endpoint<a name="WebsiteRestEndpointDiff"></a>

The website endpoint is optimized for access from a web browser\. The following table describes the key differences between the Amazon REST API endpoint and the website endpoint\. 


| Key Difference | REST API Endpoint | Website Endpoint | 
| --- | --- | --- | 
| Access control |  Supports both public and private content\.  | Supports only publicly readable content\.  | 
| Error message handling |  Returns an XML\-formatted error response\.  | Returns an HTML document\. | 
| Redirection support |  Not applicable  | Supports both object\-level and bucket\-level redirects\. | 
| Requests supported  |  Supports all bucket and object operations   | Supports only GET and HEAD requests on objects\. | 
| Responses to GET and HEAD requests at the root of a bucket | Returns a list of the object keys in the bucket\. | Returns the index document that is specified in the website configuration\. | 
| Secure Sockets Layer \(SSL\) support | Supports SSL connections\. | Does not support SSL connections\. | 

For a list of the Amazon S3 endpoints, see [Request Endpoints](MakingRequests.md#RequestEndpoints)\.