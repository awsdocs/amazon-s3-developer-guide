# Virtual hosting of buckets<a name="VirtualHosting"></a>

Virtual hosting is the practice of serving multiple websites from a single web server\. One way to differentiate sites is by using the apparent hostname of the request instead of just the path name part of the URI\. An ordinary Amazon S3 REST request specifies a bucket by using the first slash\-delimited component of the Request\-URI path\. Or, you can use Amazon S3 virtual hosting to address a bucket in a REST API call by using the HTTP `Host` header\. In practice, Amazon S3 interprets `Host` as meaning that most buckets are automatically accessible for limited types of requests at `https://bucketname.s3.Region.amazonaws.com`\. For a complete list of Amazon S3 Regions and endpoints, see [Amazon S3 Regions and Endpoints](https://docs.aws.amazon.com/general/latest/gr/s3.html) in the *AWS General Reference*\.

Virtual hosting also has other benefits\. By naming your bucket after your registered domain name and by making that name a DNS alias for Amazon S3, you can completely customize the URL of your Amazon S3 resources, for example, `http://my.bucketname.com/`\. You can also publish to the "root directory" of your bucket's virtual server\. This ability can be important because many existing applications search for files in this standard location\. For example, `favicon.ico`, `robots.txt`, `crossdomain.xml` are all expected to be found at the root\. 

**Important**  
When using virtual hosted–style buckets with SSL, the SSL wild\-card certificate only matches buckets that do not contain dots \("\."\)\. To work around this, use HTTP or write your own certificate verification logic\. For more information, see [Amazon S3 Path Deprecation Plan](http://aws.amazon.com/blogs/aws/amazon-s3-path-deprecation-plan-the-rest-of-the-story/)\.

**Topics**
+ [Path\-Style Requests](#path-style-access)
+ [Virtual Hosted\-Style Requests](#virtual-hosted-style-access)
+ [HTTP Host Header Bucket Specification](#VirtualHostingSpecifyBucket)
+ [Examples](#VirtualHostingExamples)
+ [Customizing Amazon S3 URLs with CNAMEs](#VirtualHostingCustomURLs)
+ [Limitations](#VirtualHostingLimitations)
+ [Backward Compatibility](#VirtualHostingBackwardsCompatibility)

## Path\-Style Requests<a name="path-style-access"></a>

Currently Amazon S3 supports virtual hosted\-style and path\-style access in all Regions, but this will be changing \(see the following **Important** note\.

In Amazon S3, path\-style URLs follow the format shown below\.

```
https://s3.Region.amazonaws.com/bucket-name/key name
```

For example, if you create a bucket named `mybucket` in the US West \(Oregon\) Region, and you want to access the `puppy.jpg` object in that bucket, you can use the following path\-style URL:

```
https://s3.us-west-2.amazonaws.com/mybucket/puppy.jpg
```

**Important**  
Buckets created after September 30, 2020, will support only virtual hosted\-style requests\. Path\-style requests will continue to be supported for buckets created on or before this date\. For more information, see [ Amazon S3 Path Deprecation Plan – The Rest of the Story](https://aws.amazon.com/blogs/aws/amazon-s3-path-deprecation-plan-the-rest-of-the-story/)\.

## Virtual Hosted\-Style Requests<a name="virtual-hosted-style-access"></a>

In a virtual\-hosted–style URI, the bucket name is part of the domain name in the URL\.

Amazon S3 virtual hosted style URLs follow the format shown below\.

```
https://bucket-name.s3.Region.amazonaws.com/key name
```

In this example, `my-bucket` is the bucket name, US West \(Oregon\) is the Region, and `puppy.png` is the key name:

```
https://my-bucket.s3.us-west-2.amazonaws.com/puppy.png
```

## HTTP Host Header Bucket Specification<a name="VirtualHostingSpecifyBucket"></a>

As long as your `GET` request does not use the SSL endpoint, you can specify the bucket for the request by using the HTTP `Host` header\. The `Host` header in a REST request is interpreted as follows: 
+ If the `Host` header is omitted or its value is `s3.Region.amazonaws.com`, the bucket for the request will be the first slash\-delimited component of the Request\-URI, and the key for the request will be the rest of the Request\-URI\. This is the ordinary method, as illustrated by the first and second examples in this section\. Omitting the Host header is valid only for HTTP 1\.0 requests\. 
+ Otherwise, if the value of the `Host` header ends in `.s3.Region.amazonaws.com`, the bucket name is the leading component of the `Host` header's value up to `.s3.Region.amazonaws.com`\. The key for the request is the Request\-URI\. This interpretation exposes buckets as subdomains of `.s3.Region.amazonaws.com`, as illustrated by the third and fourth examples in this section\. 
+ Otherwise, the bucket for the request is the lowercase value of the `Host` header, and the key for the request is the Request\-URI\. This interpretation is useful when you have registered the same DNS name as your bucket name and have configured that name to be a CNAME alias for Amazon S3\. The procedure for registering domain names and configuring DNS is beyond the scope of this guide, but the result is illustrated by the final example in this section\.

## Examples<a name="VirtualHostingExamples"></a>

This section provides example URLs and requests\.

**Example Path Style**  
This example uses the following:  
+ Bucket Name ‐ `awsexamplebucket1.net`
+ Region ‐ US East \(N\. Virginia\) 
+ Key Name ‐ `homepage.html`
The URL is as follows:  

```
1. http://s3.us-east-1.amazonaws.com/awsexamplebucket1.net/homepage.html
```
The request is as follows:  

```
1. GET /awsexamplebucket1.net/homepage.html HTTP/1.1
2. Host: s3.us-east-1.amazonaws.com
```
The request with HTTP 1\.0 and omitting the `host` header is as follows:  

```
1. GET /awsexamplebucket1.net/homepage.html HTTP/1.0
```

For information about DNS\-compatible names, see [Limitations](#VirtualHostingLimitations)\. For more information about keys, see [Keys](Introduction.md#BasicsKeys)\.

**Example Virtual Hosted–Style**  
This example uses the following:  
+ Bucket Name ‐ `awsexamplebucket1.eu` 
+ Region ‐ Europe \(Ireland\) 
+ Key Name ‐ `homepage.html`
The URL is as follows:  

```
1. http://awsexamplebucket1.eu.s3.eu-west-1.amazonaws.com/homepage.html
```
The request is as follows:  

```
1. GET /homepage.html HTTP/1.1
2. Host: awsexamplebucket1.eu.s3.eu-west-1.amazonaws.com
```

**Example CNAME Method**  
To use this method, you must configure your DNS name as a CNAME alias for `bucketname.s3.us-east-1.amazonaws.com`\. For more information, see [Customizing Amazon S3 URLs with CNAMEs](#VirtualHostingCustomURLs)\. This example uses the following:  
+ Bucket Name ‐ `awsexamplebucket1.net` 
+ Key Name ‐ `homepage.html`
The URL is as follows:  

```
1. http://www.awsexamplebucket1.net/homepage.html
```
The example is as follows:  

```
1. GET /homepage.html HTTP/1.1
2. Host: www.awsexamplebucket1.net
```

## Customizing Amazon S3 URLs with CNAMEs<a name="VirtualHostingCustomURLs"></a>

Depending on your needs, you might not want `s3.Region.amazonaws.com` to appear on your website or service\. For example, if you're hosting website images on Amazon S3, you might prefer `http://images.awsexamplebucket1.net/` instead of `http://images.awsexamplebucket1.net.s3.us-east-1.amazonaws.com/`\. Any bucket with a DNS\-compatible name can be referenced as follows: ` http://BucketName.s3.Region.amazonaws.com/[Filename]`, for example, `http://images.awsexamplebucket1.net.s3.us-east-1.amazonaws.com/mydog.jpg`\. By using CNAME, you can map `images.awsexamplebucket1.net` to an Amazon S3 hostname so that the previous URL could become `http://images.awsexamplebucket1.net/mydog.jpg`\. 

Your bucket name must be the same as the CNAME\. For example, if you create a CNAME to map `images.awsexamplebucket1.net` to `images.awsexamplebucket1.net.s3.us-east-1.amazonaws.com`, both `http://images.awsexamplebucket1.net/filename` and `http://images.awsexamplebucket1.net.s3.us-east-1.amazonaws.com/filename` will be the same\.

The CNAME DNS record should alias your domain name to the appropriate virtual hosted–style hostname\. For example, if your bucket name and domain name are `images.awsexamplebucket1.net` and your bucket is in the US East \(N\. Virginia\) Region, the CNAME record should alias to `images.awsexamplebucket1.net.s3.us-east-1.amazonaws.com`\. 

```
1. images.awsexamplebucket1.net CNAME 			images.awsexamplebucket1.net.s3.us-east-1.amazonaws.com.
```

Amazon S3 uses the hostname to determine the bucket name\. So the CNAME and the bucket name must be the same\. For example, suppose that you have configured `www.example.com` as a CNAME for `www.example.com.s3.us-east-1.amazonaws.com`\. When you access `http://www.example.com`, Amazon S3 receives a request similar to the following:

**Example**  

```
1. GET / HTTP/1.1
2. Host: www.example.com
3. Date: date
4. Authorization: signatureValue
```

Amazon S3 sees only the original hostname `www.example.com` and is unaware of the CNAME mapping used to resolve the request\. 

Any Amazon S3 endpoint can be used in a CNAME\. For example, `s3.ap-southeast-1.amazonaws.com` can be used in CNAMEs\. For more information about endpoints, see [Request Endpoints](MakingRequests.md#RequestEndpoints)\.

**To associate a hostname with an Amazon S3 bucket using CNAMEs**

1. Select a hostname that belongs to a domain you control\. 

   This example uses the `images` subdomain of the `awsexamplebucket1.net` domain\.

1. Create a bucket that matches the hostname\. 

   In this example, the host and bucket names are `images.awsexamplebucket1.net`\. The bucket name must *exactly* match the hostname\. 

1. Create a CNAME record that defines the hostname as an alias for the Amazon S3 bucket\. 

   For example:

   `images.awsexamplebucket1.net CNAME images.awsexamplebucket1.net.s3.us-west-2.amazonaws.com`
**Important**  
For request routing reasons, the CNAME record must be defined exactly as shown in the preceding example\. Otherwise, it might appear to operate correctly but eventually result in unpredictable behavior\.

   The procedure for configuring DNS depends on your DNS server or DNS provider\. For specific information, see your server documentation or contact your provider\.

## Limitations<a name="VirtualHostingLimitations"></a>

**SSL**  
 Virtual hosted URLs are supported for non\-SSL \(HTTP\) requests only\.

**SOAP**  
 SOAP support over HTTP is deprecated, but it is still available over HTTPS\. New Amazon S3 features will not be supported for SOAP\. We recommend that you use either the REST API or the AWS SDKs\. 

## Backward Compatibility<a name="VirtualHostingBackwardsCompatibility"></a>

### Legacy Endpoints<a name="s3-legacy-endpoints"></a>

Some Regions support legacy endpoints\. You might see these endpoints in your server access logs or CloudTrail logs\. For more information, review the information below\. For a complete list of Amazon S3 Regions and endpoints, see [Amazon S3 Regions and Endpoints](https://docs.aws.amazon.com/general/latest/gr/s3.html) in the *AWS General Reference*\.

**Important**  
Although you might see legacy endpoints in your logs, we recommend that you always use the standard endpoint syntax to access your buckets\.   
Amazon S3 virtual hosted style URLs follow the format shown below\.  

```
https://bucket-name.s3.Region.amazonaws.com/key name
```
In Amazon S3, path\-style URLs follow the format shown below\.  

```
https://s3.Region.amazonaws.com/bucket-name/key name
```

#### s3‐Region<a name="s3-dash-region"></a>

Some older Amazon S3 Regions support endpoints that contains a dash between S3 and the Region \(for example, `S3‐us-west-2`\), instead of a dot \(for example, `S3.us-west-2`\)\. If your bucket is in one of these Regions, you might see the following endpoint format in your server access logs or CloudTrail logs:

```
https://bucket-name.s3-Region.amazonaws.com
```

In this example, the bucket name is `my-bucket` and the Region is US West \(Oregon\):

```
https://my-bucket.s3-us-west-2.amazonaws.com
```

#### Legacy Global Endpoint<a name="deprecated-global-endpoint"></a>

For some Regions, the legacy global endpoint can be used to construct requests that do not specify a Region\-specific endpoint\. The legacy global endpoint point is as follows:

```
bucket-name.s3.amazonaws.com
```

In your server access logs or CloudTrail logs, you might see requests that use the legacy global endpoint\. In this example, the bucket name is `my-bucket` and the legacy global endpoint is shown: 

```
https://my-bucket.amazonaws.com
```

**Virtual Hosted\-Style Requests for US East \(N\. Virginia\)**  
Requests made with the legacy global endpoint go to US East \(N\. Virginia\) by default\. Therefore, the legacy global endpoint is sometimes used in place of the Regional endpoint for US East \(N\. Virginia\)\. If you create a bucket in US East \(N\. Virginia\) and use the global endpoint, Amazon S3 routes your request to this Region by default\. 

**Virtual Hosted\-Style Requests for Other Regions**  
The legacy global endpoint is also used for virtual hosted\-style requests in other supported Regions\. If you create a bucket in a Region that was launched before March 20, 2019 and use the legacy global endpoint, Amazon S3 updates the DNS to reroute the request to the correct location, which might take time\. In the meantime, the default rule applies, and your virtual hosted–style request goes to the US East \(N\. Virginia\) Region\. Amazon S3 then redirects it with an HTTP 307 redirect to the correct Region\. For S3 buckets in Regions launched after March 20, 2019, the DNS doesn't route your request directly to the AWS Region where your bucket resides\. It returns an HTTP 400 Bad Request error instead\. For more information, see [Request redirection and the REST API](Redirects.md)\.

**Path Style Requests**  
For the US East \(N\. Virginia\) Region, the legacy global endpoint can be used for path\-style requests\. 

For all other Regions, the path\-style syntax requires that you use the Region\-specific endpoint when attempting to access a bucket\. If you try to access a bucket with the legacy global endpoint or another endpoint that is different than the one for the Region where the bucket resides, you will receive an HTTP response code 307 Temporary Redirect error and a message indicating the correct URI for your resource\. For example, if you use `https://s3.amazonaws.com/bucket-name` for a bucket that was created in the US West \(Oregon\) Region, you will receive an HTTP 307 Temporary Redirect error\.