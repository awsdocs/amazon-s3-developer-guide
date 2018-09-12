# Request Redirection and the REST API<a name="Redirects"></a>

Amazon S3 uses the Domain Name System \(DNS\) to route requests to facilities that can process them\. This system works effectively, but temporary routing errors can occur\. If a request arrives at the wrong Amazon S3 location, Amazon S3 responds with a temporary redirect that tells the requester to resend the request to a new endpoint\. If a request is incorrectly formed, Amazon S3 uses permanent redirects to provide direction on how to perform the request correctly\.

**Important**  
To use this feature, you must have an application that can handle Amazon S3 redirect responses\. The only exception is for applications that work exclusively with buckets that were created without `<CreateBucketConfiguration>`\. For more information about location constraints, see [Accessing a Bucket](UsingBucket.md#access-bucket-intro)\.

**Topics**
+ [DNS Routing](#DNSRouting)
+ [Temporary Request Redirection](#TemporaryRedirection)
+ [Permanent Request Redirection](#RedirectsPermanentRedirection)
+ [Request Redirection Examples](#redirect-examples)

## DNS Routing<a name="DNSRouting"></a>

DNS routing routes requests to appropriate Amazon S3 facilities\. The following figure and procedure show an example of DNS routing\.

![\[Diagram showing steps that occur when a DNS server routes requests from the client to facility B.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/DNS_virthost.png)

**DNS routing request steps**

1. The client makes a DNS request to get an object stored on Amazon S3\.

1. The client receives one or more IP addresses for facilities that can process the request\. In this example, the IP address is for Facility B\.

1. The client makes a request to Amazon S3 Facility B\.

1. Facility B returns a copy of the object to the client\.

## Temporary Request Redirection<a name="TemporaryRedirection"></a>

A temporary redirect is a type of error response that signals to the requester that they should resend the request to a different endpoint\. Due to the distributed nature of Amazon S3, requests can be temporarily routed to the wrong facility\. This is most likely to occur immediately after buckets are created or deleted\.

For example, if you create a new bucket and immediately make a request to the bucket, you might receive a temporary redirect, depending on the location constraint of the bucket\. If you created the bucket in the US East \(N\. Virginia\) AWS Region, you will not see the redirect because this is also the default Amazon S3 endpoint\.

However, if the bucket is created in any other Region, any requests for the bucket go to the default endpoint while the bucket's DNS entry is propagated\. The default endpoint redirects the request to the correct endpoint with an HTTP 302 response\. Temporary redirects contain a URI to the correct facility, which you can use to immediately resend the request\.

**Important**  
Don't reuse an endpoint provided by a previous redirect response\. It might appear to work \(even for long periods of time\), but it might provide unpredictable results and will eventually fail without notice\.

The following figure and procedure shows an example of a temporary redirect\.

![\[Diagram showing steps that occur when a client sends a request to B and is redirected to C.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/DNS_virthost_redirect.png)

**Temporary request redirection steps**

1. The client makes a DNS request to get an object stored on Amazon S3\.

1. The client receives one or more IP addresses for facilities that can process the request\.

1. The client makes a request to Amazon S3 Facility B\.

1. Facility B returns a redirect indicating the object is available from Location C\.

1. The client resends the request to Facility C\.

1. Facility C returns a copy of the object\.

## Permanent Request Redirection<a name="RedirectsPermanentRedirection"></a>

A permanent redirect indicates that your request addressed a resource inappropriately\. For example, permanent redirects occur if you use a path\-style request to access a bucket that was created using `<CreateBucketConfiguration>`\. For more information, see [Accessing a Bucket](UsingBucket.md#access-bucket-intro)\.

To help you find these errors during development, this type of redirect does not contain a Location HTTP header that allows you to automatically follow the request to the correct location\. Consult the resulting XML error document for help using the correct Amazon S3 endpoint\.

## Request Redirection Examples<a name="redirect-examples"></a>

The following are examples of temporary request redirection responses\.

### REST API Temporary Redirect Response<a name="RedirectsTemporaryRedirection-response-rest-ex1"></a>

```
 1. HTTP/1.1 307 Temporary Redirect
 2. Location: http://johnsmith.s3-gztb4pa9sq.amazonaws.com/photos/puppy.jpg?rk=e2c69a31
 3. Content-Type: application/xml
 4. Transfer-Encoding: chunked
 5. Date: Fri, 12 Oct 2007 01:12:56 GMT
 6. Server: AmazonS3
 7. 
 8. <?xml version="1.0" encoding="UTF-8"?>
 9. <Error>
10.   <Code>TemporaryRedirect</Code>
11.   <Message>Please re-send this request to the specified temporary endpoint.
12.   Continue to use the original request endpoint for future requests.</Message>
13.   <Endpoint>johnsmith.s3-gztb4pa9sq.amazonaws.com</Endpoint>
14. </Error>
```

### SOAP API Temporary Redirect Response<a name="RedirectsTemporaryRedirection-respose-soap-ex2"></a>

**Note**  
 SOAP support over HTTP is deprecated, but it is still available over HTTPS\. New Amazon S3 features will not be supported for SOAP\. We recommend that you use either the REST API or the AWS SDKs\. 

```
 1. <soapenv:Body>
 2.   <soapenv:Fault>
 3.     <Faultcode>soapenv:Client.TemporaryRedirect</Faultcode>
 4.     <Faultstring>Please re-send this request to the specified temporary endpoint.
 5.     Continue to use the original request endpoint for future requests.</Faultstring>
 6.     <Detail>
 7.       <Bucket>images</Bucket>
 8.       <Endpoint>s3-gztb4pa9sq.amazonaws.com</Endpoint>
 9.     </Detail>
10.   </soapenv:Fault>
11. </soapenv:Body>
```