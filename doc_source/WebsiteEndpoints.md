# Website Endpoints<a name="WebsiteEndpoints"></a>

When you configure a bucket for website hosting, the website is available via the region\-specific website endpoint\. Website endpoints are different from the endpoints where you send REST API requests\. For more information about the differences between the endpoints, see [Key Differences Between the Amazon Website and the REST API Endpoint](#WebsiteRestEndpointDiff)\.

The two general forms of an Amazon S3 website endpoint are as follows: 

```
1. bucket-name.s3-website-region.amazonaws.com
```

```
1. bucket-name.s3-website.region.amazonaws.com
```

Which form is used for the endpoint depends on what Region the bucket is in\. For example, if your bucket is named `example-bucket` and it resides in the US West \(Oregon\) region, the website is available at the following Amazon S3 website endpoint: 

```
1. http://example-bucket.s3-website-us-west-2.amazonaws.com/
```

Or, if your bucket is named `example-bucket` and it resides in the EU \(Frankfurt\) region, the website is available at the following Amazon S3 website endpoint: 

```
1. http://example-bucket.s3-website.eu-central-1.amazonaws.com/
```

For a list of the Amazon S3 website endpoints by Region, see [Amazon Simple Storage Service Website Endpoints](http://docs.aws.amazon.com/general/latest/gr/rande.html#s3_website_region_endpoints) in the * AWS General Reference*\. 

 In order for your customers to access content at the website endpoint, you must make all your content publicly readable\. To do so, you can use a bucket policy or an ACL on an object to grant the necessary permissions\. 

**Note**  
Requester Pays buckets  do not allow access through the website endpoint\. Any request to such a bucket receives a `403 Access Denied` response\. For more information, see [Requester Pays Buckets](RequesterPaysBuckets.md)\.

If you have a registered domain, you can add a DNS CNAME entry to point to the Amazon S3 website endpoint\. For example, if you have registered domain, `www.example-bucket.com`, you could create a bucket `www.example-bucket.com`, and add a DNS CNAME record that points to `www.example-bucket.com.s3-website-<region>.amazonaws.com`\. All requests to `http://www.example-bucket.com` are routed to `www.example-bucket.com.s3-website-<region>.amazonaws.com`\. For more information, see [Virtual Hosting of Buckets](VirtualHosting.md)\. 

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