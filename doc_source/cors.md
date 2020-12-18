# Cross\-origin resource sharing \(CORS\)<a name="cors"></a>

Cross\-origin resource sharing \(CORS\) defines a way for client web applications that are loaded in one domain to interact with resources in a different domain\. With CORS support, you can build rich client\-side web applications with Amazon S3 and selectively allow cross\-origin access to your Amazon S3 resources\. 

This section provides an overview of CORS\. The subtopics describe how you can enable CORS using the Amazon S3 console, or programmatically by using the Amazon S3 REST API and the AWS SDKs\. 

**Topics**
+ [Cross\-origin resource sharing: Use\-case scenarios](#example-scenarios-cors)
+ [How do I configure CORS on my bucket?](#how-do-i-enable-cors)
+ [How does Amazon S3 evaluate the CORS configuration on a bucket?](#cors-eval-criteria)
+ [Enabling cross\-origin resource sharing \(CORS\)](ManageCorsUsing.md)
+ [Troubleshooting CORS issues](cors-troubleshooting.md)

## Cross\-origin resource sharing: Use\-case scenarios<a name="example-scenarios-cors"></a>

The following are example scenarios for using CORS\.

**Scenario 1**  
Suppose that you are hosting a website in an Amazon S3 bucket named `website` as described in [Hosting a static website using Amazon S3](WebsiteHosting.md)\. Your users load the website endpoint:

```
http://website.s3-website.us-east-1.amazonaws.com
```

Now you want to use JavaScript on the webpages that are stored in this bucket to be able to make authenticated GET and PUT requests against the same bucket by using the Amazon S3 API endpoint for the bucket, `website.s3.us-east-1.amazonaws.com`\. A browser would normally block JavaScript from allowing those requests, but with CORS you can configure your bucket to explicitly enable cross\-origin requests from `website.s3-website.us-east-1.amazonaws.com`\.

**Scenario 2**  
Suppose that you want to host a web font from your S3 bucket\. Again, browsers require a CORS check \(also called a preflight check\) for loading web fonts\. You would configure the bucket that is hosting the web font to allow any origin to make these requests\.

## How do I configure CORS on my bucket?<a name="how-do-i-enable-cors"></a>

To configure your bucket to allow cross\-origin requests, you create a CORS configuration\. The CORS configuration is a document with rules that identify the origins that you will allow to access your bucket, the operations \(HTTP methods\) that will support for each origin, and other operation\-specific information\. You can add up to 100 rules to the configuration\. You can add the CORS configuration as the `cors` subresource to the bucket\.

If you are configuring CORS in the S3 console, you must use JSON to create a CORS configuration\. The new S3 console only supports JSON CORS configurations\. 

**Important**  
In the new S3 console, the CORS configuration must be JSON\.

Instead of accessing a website by using an Amazon S3 website endpoint, you can use your own domain, such as `example1.com` to serve your content\. For information about using your own domain, see [Configuring a static website using a custom domain registered with Route 53](website-hosting-custom-domain-walkthrough.md)\. 

**Example 1**  
The following example `cors` configuration has three rules, which are specified as `CORSRule` elements:  
+ The first rule allows cross\-origin PUT, POST, and DELETE requests from the `http://www.example1.com` origin\. The rule also allows all headers in a preflight OPTIONS request through the `Access-Control-Request-Headers` header\. In response to preflight OPTIONS requests, Amazon S3 returns requested headers\.
+ The second rule allows the same cross\-origin requests as the first rule, but the rule applies to another origin, `http://www.example2.com`\. 
+ The third rule allows cross\-origin GET requests from all origins\. The `*` wildcard character refers to all origins\. 

```
<CORSConfiguration>
 <CORSRule>
   <AllowedOrigin>http://www.example1.com</AllowedOrigin>

   <AllowedMethod>PUT</AllowedMethod>
   <AllowedMethod>POST</AllowedMethod>
   <AllowedMethod>DELETE</AllowedMethod>

   <AllowedHeader>*</AllowedHeader>
 </CORSRule>
 <CORSRule>
   <AllowedOrigin>http://www.example2.com</AllowedOrigin>

   <AllowedMethod>PUT</AllowedMethod>
   <AllowedMethod>POST</AllowedMethod>
   <AllowedMethod>DELETE</AllowedMethod>

   <AllowedHeader>*</AllowedHeader>
 </CORSRule>
 <CORSRule>
   <AllowedOrigin>*</AllowedOrigin>
   <AllowedMethod>GET</AllowedMethod>
 </CORSRule>
</CORSConfiguration>
```

```
[
    {
        "AllowedHeaders": [
            "*"
        ],
        "AllowedMethods": [
            "PUT",
            "POST",
            "DELETE"
        ],
        "AllowedOrigins": [
            "http://www.example1.com"
        ],
        "ExposeHeaders": []
    },
    {
        "AllowedHeaders": [
            "*"
        ],
        "AllowedMethods": [
            "PUT",
            "POST",
            "DELETE"
        ],
        "AllowedOrigins": [
            "http://www.example2.com"
        ],
        "ExposeHeaders": []
    },
    {
        "AllowedHeaders": [],
        "AllowedMethods": [
            "GET"
        ],
        "AllowedOrigins": [
            "*"
        ],
        "ExposeHeaders": []
    }
]
```

**Example 2**  
The CORS configuration also allows optional configuration parameters, as shown in the following CORS configuration\. In this example, the CORS configuration allows cross\-origin PUT, POST, and DELETE requests from the `http://www.example.com` origin\.  

```
<CORSConfiguration>
 <CORSRule>
   <AllowedOrigin>http://www.example.com</AllowedOrigin>
   <AllowedMethod>PUT</AllowedMethod>
   <AllowedMethod>POST</AllowedMethod>
   <AllowedMethod>DELETE</AllowedMethod>
   <AllowedHeader>*</AllowedHeader>
  <MaxAgeSeconds>3000</MaxAgeSeconds>
  <ExposeHeader>x-amz-server-side-encryption</ExposeHeader>
  <ExposeHeader>x-amz-request-id</ExposeHeader>
  <ExposeHeader>x-amz-id-2</ExposeHeader>
 </CORSRule>
</CORSConfiguration>
```

```
[
    {
        "AllowedHeaders": [
            "*"
        ],
        "AllowedMethods": [
            "PUT",
            "POST",
            "DELETE"
        ],
        "AllowedOrigins": [
            "http://www.example.com"
        ],
        "ExposeHeaders": [
            "x-amz-server-side-encryption",
            "x-amz-request-id",
            "x-amz-id-2"
        ],
        "MaxAgeSeconds": 3000
    }
]
```
The `CORSRule` element in the preceding configuration includes the following optional elements:  
+ `MaxAgeSeconds`—Specifies the amount of time in seconds \(in this example, 3000\) that the browser caches an Amazon S3 response to a preflight OPTIONS request for the specified resource\. By caching the response, the browser does not have to send preflight requests to Amazon S3 if the original request will be repeated\. 
+ `ExposeHeader`—Identifies the response headers \(in this example, `x-amz-server-side-encryption`, `x-amz-request-id`, and `x-amz-id-2`\) that customers are able to access from their applications \(for example, from a JavaScript `XMLHttpRequest` object\)\.

### AllowedMethod element<a name="cors-allowed-methods"></a>

In the CORS configuration, you can specify the following values for the `AllowedMethod` element\.
+ GET
+ PUT
+ POST
+ DELETE
+ HEAD

### AllowedOrigin element<a name="cors-allowed-origin"></a>

In the `AllowedOrigin` element, you specify the origins that you want to allow cross\-domain requests from, for example,` http://www.example.com`\. The origin string can contain only one `*` wildcard character, such as `http://*.example.com`\. You can optionally specify `*` as the origin to enable all the origins to send cross\-origin requests\. You can also specify `https` to enable only secure origins\.

### AllowedHeader element<a name="cors-allowed-headers"></a>

The `AllowedHeader` element specifies which headers are allowed in a preflight request through the `Access-Control-Request-Headers` header\. Each header name in the `Access-Control-Request-Headers` header must match a corresponding entry in the rule\. Amazon S3 will send only the allowed headers in a response that were requested\. For a sample list of headers that can be used in requests to Amazon S3, go to [Common Request Headers](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTCommonRequestHeaders.html) in the *Amazon Simple Storage Service API Reference* guide\.

Each AllowedHeader string in the rule can contain at most one \* wildcard character\. For example, `<AllowedHeader>x-amz-*</AllowedHeader>` will enable all Amazon\-specific headers\.

### ExposeHeader element<a name="cors-expose-headers"></a>

Each `ExposeHeader` element identifies a header in the response that you want customers to be able to access from their applications \(for example, from a JavaScript `XMLHttpRequest` object\)\. For a list of common Amazon S3 response headers, go to [Common Response Headers](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTCommonResponseHeaders.html) in the *Amazon Simple Storage Service API Reference* guide\.

### MaxAgeSeconds element<a name="cors-max-age"></a>

The `MaxAgeSeconds` element specifies the time in seconds that your browser can cache the response for a preflight request as identified by the resource, the HTTP method, and the origin\.

## How does Amazon S3 evaluate the CORS configuration on a bucket?<a name="cors-eval-criteria"></a>

When Amazon S3 receives a preflight request from a browser, it evaluates the CORS configuration for the bucket and uses the first `CORSRule` rule that matches the incoming browser request to enable a cross\-origin request\. For a rule to match, the following conditions must be met:
+ The request's `Origin` header must match an `AllowedOrigin` element\.
+ The request method \(for example, GET or PUT\) or the `Access-Control-Request-Method` header in case of a preflight `OPTIONS` request must be one of the `AllowedMethod` elements\. 
+ Every header listed in the request's `Access-Control-Request-Headers` header on the preflight request must match an `AllowedHeader` element\. 

**Note**  
The ACLs and policies continue to apply when you enable CORS on the bucket\.