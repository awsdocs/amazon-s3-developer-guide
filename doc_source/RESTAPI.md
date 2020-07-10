# Making requests using the REST API<a name="RESTAPI"></a>

This section contains information on how to make requests to Amazon S3 endpoints by using the REST API\. For a list of Amazon S3 endpoints, see [Regions and Endpoints](https://docs.aws.amazon.com/general/latest/gr/s3.html) in the *AWS General Reference*\.

**Topics**
+ [Constructing S3 hostnames for REST API requests](#constructing-hostname-rest-api-requests)
+ [Virtual hosted‐style and path‐style requests](#virtual-hosted-path-style-requests)
+ [Making requests to dual\-stack endpoints by using the REST API](#rest-api-dual-stack)
+ [Virtual hosting of buckets](VirtualHosting.md)
+ [Request redirection and the REST API](RESTRedirect.md)

## Constructing S3 hostnames for REST API requests<a name="constructing-hostname-rest-api-requests"></a>

Amazon S3 endpoints follow the structure shown below:

```
s3.Region.amazonaws.com
```

Amazon S3 Access Points endpoints and dual\-stack endpoints also follow the standard structure:
+ **Amazon S3 Access Points** ‐`s3-accesspoint.Region.amazonaws.com`
+ **Dual\-stack** ‐ `s3.dualstack.Region.amazonaws.com` 

For a complete list of Amazon S3 Regions and endpoints, see [Amazon S3 Regions and Endpoints](https://docs.aws.amazon.com/general/latest/gr/s3.html) in the *AWS General Reference*\.

## Virtual hosted‐style and path‐style requests<a name="virtual-hosted-path-style-requests"></a>

When making requests by using the REST API, you can use virtual hosted–style or path\-style URIs for the Amazon S3 endpoints\. For more information, see [Virtual hosting of buckets](VirtualHosting.md)\.

**Example Virtual hosted–Style request**  
Following is an example of a virtual hosted–style request to delete the `puppy.jpg` file from the bucket named `examplebucket` in the US West \(Oregon\) Region\. For more information about virtual hosted\-style requests, see [Virtual Hosted\-Style Requests](VirtualHosting.md#virtual-hosted-style-access)\.  

```
1. DELETE /puppy.jpg HTTP/1.1
2. Host: examplebucket.s3.us-west-2.amazonaws.com
3. Date: Mon, 11 Apr 2016 12:00:00 GMT
4. x-amz-date: Mon, 11 Apr 2016 12:00:00 GMT
5. Authorization: authorization string
```

**Example Path\-style request**  
Following is an example of a path\-style version of the same request\.  

```
1. DELETE /examplebucket/puppy.jpg HTTP/1.1
2. Host: s3.us-west-2.amazonaws.com
3. Date: Mon, 11 Apr 2016 12:00:00 GMT
4. x-amz-date: Mon, 11 Apr 2016 12:00:00 GMT
5. Authorization: authorization string
```
Currently Amazon S3 supports virtual hosted\-style and path\-style access in all Regions, but this will be changing \(see the following **Important** note\.  
For more information about path\-style requests, see [Path\-Style Requests](VirtualHosting.md#path-style-access)\.  
Buckets created after September 30, 2020, will support only virtual hosted\-style requests\. Path\-style requests will continue to be supported for buckets created on or before this date\. For more information, see [ Amazon S3 Path Deprecation Plan – The Rest of the Story](https://aws.amazon.com/blogs/aws/amazon-s3-path-deprecation-plan-the-rest-of-the-story/)\.

## Making requests to dual\-stack endpoints by using the REST API<a name="rest-api-dual-stack"></a>

When using the REST API, you can directly access a dual\-stack endpoint by using a virtual hosted–style or a path style endpoint name \(URI\)\. All Amazon S3 dual\-stack endpoint names include the region in the name\. Unlike the standard IPv4\-only endpoints, both virtual hosted–style and a path\-style endpoints use region\-specific endpoint names\. 

**Example Virtual hosted–Style dual\-stack endpoint request**  
You can use a virtual hosted–style endpoint in your REST request as shown in the following example that retrieves the `puppy.jpg` object from the bucket named `examplebucket` in the US West \(Oregon\) Region\.  

```
1. GET /puppy.jpg HTTP/1.1
2. Host: examplebucket.s3.dualstack.us-west-2.amazonaws.com
3. Date: Mon, 11 Apr 2016 12:00:00 GMT
4. x-amz-date: Mon, 11 Apr 2016 12:00:00 GMT
5. Authorization: authorization string
```

**Example Path\-style dual\-stack endpoint request**  
Or you can use a path\-style endpoint in your request as shown in the following example\.  

```
1. GET /examplebucket/puppy.jpg HTTP/1.1
2. Host: s3.dualstack.us-west-2.amazonaws.com
3. Date: Mon, 11 Apr 2016 12:00:00 GMT
4. x-amz-date: Mon, 11 Apr 2016 12:00:00 GMT
5. Authorization: authorization string
```

For more information about dual\-stack endpoints, see [Using Amazon S3 dual\-stack endpoints](dual-stack-endpoints.md)\.