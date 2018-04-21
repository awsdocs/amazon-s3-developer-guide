# Virtual Hosting of Buckets<a name="VirtualHosting"></a>

**Topics**
+ [HTTP Host Header Bucket Specification](#VirtualHostingSpecifyBucket)
+ [Examples](#VirtualHostingExamples)
+ [Customizing Amazon S3 URLs with CNAMEs](#VirtualHostingCustomURLs)
+ [Limitations](#VirtualHostingLimitations)
+ [Backward Compatibility](#VirtualHostingBackwardsCompatibility)

In general, virtual hosting is the practice of serving multiple web sites from a single web server\. One way to differentiate sites is by using the apparent host name of the request instead of just the path name part of the URI\. An ordinary Amazon S3 REST request specifies a bucket by using the first slash\-delimited component of the Request\-URI path\. Alternatively, you can use Amazon S3 virtual hosting to address a bucket in a REST API call by using the HTTP `Host` header\. In practice, Amazon S3 interprets `Host` as meaning that most buckets are automatically accessible \(for limited types of requests\) at `http://bucketname.s3.amazonaws.com`\. Furthermore, by naming your bucket after your registered domain name and by making that name a DNS alias for Amazon S3, you can completely customize the URL of your Amazon S3 resources, for example, `http://my.bucketname.com/`\. 

Besides the attractiveness of customized URLs, a second benefit of virtual hosting is the ability to publish to the "root directory" of your bucket's virtual server\. This ability can be important because many existing applications search for files in this standard location\. For example, `favicon.ico`, `robots.txt`, `crossdomain.xml` are all expected to be found at the root\. 

**Important**  
 Amazon S3 supports virtual hosted\-style and path\-style access in all regions\. The path\-style syntax, however, requires that you use the region\-specific endpoint when attempting to access a bucket\. For example, if you have a bucket called `mybucket` that resides in the EU \(Ireland\) region, you want to use path\-style syntax, and the object is named `puppy.jpg`, the correct URI is `http://s3-eu-west-1.amazonaws.com/mybucket/puppy.jpg`\.   
You will receive an HTTP response code 307 Temporary Redirect error and a message indicating what the correct URI is for your resource if you try to access a bucket outside the US East \(N\. Virginia\) region with path\-style syntax that uses either of the following:   
 `http://s3.amazonaws.com` 
 An endpoint for a region different from the one where the bucket resides\. For example, if you use `http://s3-eu-west-1.amazonaws.com` for a bucket that was created in the US West \(N\. California\) region\.

**Note**  
Amazon S3 routes any virtual hosted–style requests to the US East \(N\. Virginia\) region by default if you use the US East \(N\. Virginia\) endpoint \(s3\.amazonaws\.com\), instead of the region\-specific endpoint \(for example, s3\-eu\-west\-1\.amazonaws\.com\)\. When you create a bucket, in any region, Amazon S3 updates DNS to reroute the request to the correct location, which might take time\. In the meantime, the default rule applies and your virtual hosted–style request goes to the US East \(N\. Virginia\) region, and Amazon S3 redirects it with HTTP 307 redirect to the correct region\. For more information, see [Request Redirection and the REST API](Redirects.md)\.  
When using virtual hosted–style buckets with SSL, the SSL wild card certificate only matches buckets that do not contain periods\. To work around this, use HTTP or write your own certificate verification logic\.

## HTTP Host Header Bucket Specification<a name="VirtualHostingSpecifyBucket"></a>

 As long as your `GET` request does not use the SSL endpoint, you can specify the bucket for the request by using the HTTP `Host` header\. The `Host` header in a REST request is interpreted as follows: 
+ If the `Host` header is omitted or its value is 's3\.amazonaws\.com', the bucket for the request will be the first slash\-delimited component of the Request\-URI, and the key for the request will be the rest of the Request\-URI\. This is the ordinary method, as illustrated by the first and second examples in this section\. Omitting the Host header is valid only for HTTP 1\.0 requests\. 
+ Otherwise, if the value of the `Host` header ends in '\.s3\.amazonaws\.com', the bucket name is the leading component of the `Host` header's value up to '\.s3\.amazonaws\.com'\. The key for the request is the Request\-URI\. This interpretation exposes buckets as subdomains of s3\.amazonaws\.com, as illustrated by the third and fourth examples in this section\. 
+ Otherwise, the bucket for the request is the lowercase value of the `Host` header, and the key for the request is the Request\-URI\. This interpretation is useful when you have registered the same DNS name as your bucket name and have configured that name to be a CNAME alias for Amazon S3\. The procedure for registering domain names and configuring DNS is beyond the scope of this guide, but the result is illustrated by the final example in this section\.

## Examples<a name="VirtualHostingExamples"></a>

This section provides example URLs and requests\.

**Example Path Style Method**  
This example uses `johnsmith.net` as the bucket name and `homepage.html` as the key name\.  
The URL is as follows:  

```
1. http://s3.amazonaws.com/johnsmith.net/homepage.html
```
The request is as follows:  

```
1. GET /johnsmith.net/homepage.html HTTP/1.1
2. Host: s3.amazonaws.com
```
The request with HTTP 1\.0 and omitting the `host` header is as follows:  

```
1. GET /johnsmith.net/homepage.html HTTP/1.0
```

For information about DNS\-compatible names, see [Limitations](#VirtualHostingLimitations)\. For more information about keys, see [Keys](Introduction.md#BasicsKeys)\.

**Example Virtual Hosted–Style Method**  
This example uses `johnsmith.net` as the bucket name and `homepage.html` as the key name\.  
The URL is as follows:  

```
1. http://johnsmith.net.s3.amazonaws.com/homepage.html
```
The request is as follows:  

```
1. GET /homepage.html HTTP/1.1
2. Host: johnsmith.net.s3.amazonaws.com
```
The virtual hosted–style method requires the bucket name to be DNS\-compliant\. 

**Example Virtual Hosted–Style Method for a Bucket in a Region Other Than US East \(N\. Virginia\) region**  
This example uses `johnsmith.eu` as the name for a bucket in the EU \(Ireland\) region and `homepage.html` as the key name\.  
The URL is as follows:  

```
1. http://johnsmith.eu.s3-eu-west-1.amazonaws.com/homepage.html
```
The request is as follows:  

```
1. GET /homepage.html HTTP/1.1
2. Host: johnsmith.eu.s3-eu-west-1.amazonaws.com
```
Note that, instead of using the region\-specific endpoint, you can also use the US East \(N\. Virginia\) region endpoint no matter what region the bucket resides\.  

```
http://johnsmith.eu.s3.amazonaws.com/homepage.html
```
The request is as follows:  

```
1. GET /homepage.html HTTP/1.1
2. Host: johnsmith.eu.s3.amazonaws.com
```

**Example CNAME Method**  
This example uses `www.johnsmith.net` as the bucket name and `homepage.html` as the key name\. To use this method, you must configure your DNS name as a CNAME alias for *bucketname*\.s3\.amazonaws\.com\.   
The URL is as follows:  

```
1. http://www.johnsmith.net/homepage.html
```
The example is as follows:  

```
1. GET /homepage.html HTTP/1.1
2. Host: www.johnsmith.net
```

## Customizing Amazon S3 URLs with CNAMEs<a name="VirtualHostingCustomURLs"></a>

Depending on your needs, you might not want "s3\.amazonaws\.com" to appear on your website or service\. For example, if you host your website images on Amazon S3, you might prefer `http://images.johnsmith.net/` instead of `http://johnsmith-images.s3.amazonaws.com/.`

The bucket name must be the same as the CNAME\. So `http://images.johnsmith.net/filename` would be the same as `http://images.johnsmith.net.s3.amazonaws.com/filename` if a CNAME were created to map `images.johnsmith.net` to `images.johnsmith.net.s3.amazonaws.com`\. 

Any bucket with a DNS\-compatible name can be referenced as follows: ` http://[BucketName].s3.amazonaws.com/[Filename]`, for example, `http://images.johnsmith.net.s3.amazonaws.com/mydog.jpg`\. By using CNAME, you can map `images.johnsmith.net` to an Amazon S3 host name so that the previous URL could become `http://images.johnsmith.net/mydog.jpg`\. 

The CNAME DNS record should alias your domain name to the appropriate virtual hosted–style host name\. For example, if your bucket name and domain name are `images.johnsmith.net`, the CNAME record should alias to `images.johnsmith.net.s3.amazonaws.com`\. 

```
1. images.johnsmith.net CNAME 			images.johnsmith.net.s3.amazonaws.com.
```

Setting the alias target to `s3.amazonaws.com` also works, but it may result in extra HTTP redirects\.

Amazon S3 uses the host name to determine the bucket name\. For example, suppose that you have configured `www.example.com` as a CNAME for `www.example.com.s3.amazonaws.com`\. When you access `http://www.example.com`, Amazon S3 receives a request similar to the following:

**Example**  

```
1. GET / HTTP/1.1
2. Host: www.example.com
3. Date: date
4. Authorization: signatureValue
```

Because Amazon S3 sees only the original host name `www.example.com` and is unaware of the CNAME mapping used to resolve the request, the CNAME and the bucket name must be the same\.

Any Amazon S3 endpoint can be used in a CNAME\. For example, `s3-ap-southeast-1.amazonaws.com` can be used in CNAMEs\. For more information about endpoints, see [Request Endpoints](MakingRequests.md#RequestEndpoints)\.

**To associate a host name with an Amazon S3 bucket using CNAMEs**

1. Select a host name that belongs to a domain you control\. This example uses the `images` subdomain of the `johnsmith.net` domain\.

1. Create a bucket that matches the host name\. In this example, the host and bucket names are `images.johnsmith.net`\. 
**Note**  
The bucket name must exactly match the host name\. 

1. Create a CNAME record that defines the host name as an alias for the Amazon S3 bucket\. For example:

   `images.johnsmith.net CNAME images.johnsmith.net.s3.amazonaws.com`
**Important**  
For request routing reasons, the CNAME record must be defined exactly as shown in the preceding example\. Otherwise, it might appear to operate correctly, but will eventually result in unpredictable behavior\.
**Note**  
The procedure for configuring DNS depends on your DNS server or DNS provider\. For specific information, see your server documentation or contact your provider\.

## Limitations<a name="VirtualHostingLimitations"></a>

Specifying the bucket for the request by using the HTTP `Host` header is supported for non\-SSL requests and when using the REST API\. You cannot specify the bucket in SOAP by using a different endpoint\. 

**Note**  
 SOAP support over HTTP is deprecated, but it is still available over HTTPS\. New Amazon S3 features will not be supported for SOAP\. We recommend that you use either the REST API or the AWS SDKs\. 

## Backward Compatibility<a name="VirtualHostingBackwardsCompatibility"></a>

Early versions of Amazon S3 incorrectly ignored the HTTP `Host` header\. Applications that depend on this undocumented behavior must be updated to set the `Host` header correctly\. Because Amazon S3 determines the bucket name from `Host` when it is present, the most likely symptom of this problem is to receive an unexpected `NoSuchBucket` error result code\.