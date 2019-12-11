# Making Requests Using the REST API<a name="RESTAPI"></a>

This section contains information on how to make requests to Amazon S3 endpoints by using the REST API\. For a list of Amazon S3 endpoints, see [Regions and Endpoints](https://docs.aws.amazon.com/general/latest/gr/rande.html#s3_region) in the *AWS General Reference*\.

**Topics**
+ [Making Requests to Dual\-Stack Endpoints by Using the REST API](#rest-api-dual-stack)
+ [Virtual Hosting of Buckets](VirtualHosting.md)
+ [Request Redirection and the REST API](RESTRedirect.md)

 When making requests by using the REST API, you can use virtual hosted–style or path\-style URIs for the Amazon S3 endpoints\. For more information, see [Working with Amazon S3 Buckets](UsingBucket.md)\.

**Example Virtual Hosted–Style Request**  
Following is an example of a virtual hosted–style request to delete the `puppy.jpg` file from the bucket named examplebucket\.  

```
1. DELETE /puppy.jpg HTTP/1.1
2. Host: examplebucket.s3.amazonaws.com
3. Date: Mon, 11 Apr 2016 12:00:00 GMT
4. x-amz-date: Mon, 11 Apr 2016 12:00:00 GMT
5. Authorization: authorization string
```

**Example Path\-Style Request**  
Following is an example of a path\-style version of the same request\.  

```
1. DELETE /examplebucket/puppy.jpg HTTP/1.1
2. Host: s3-us-west-2.amazonaws.com
3. Date: Mon, 11 Apr 2016 12:00:00 GMT
4. x-amz-date: Mon, 11 Apr 2016 12:00:00 GMT
5. Authorization: authorization string
```

 Currently Amazon S3 supports virtual hosted\-style and path\-style access in all Regions but this will be changing \(see the following **Important** note\.\) The path\-style syntax requires that you use the Region\-specific endpoint when attempting to access a bucket\. For example, if you have a bucket called `mybucket` that resides in the Europe \(Ireland\) Region, you want to use path\-style syntax, and the object is named `puppy.jpg`, the correct URI is `http://s3-eu-west-1.amazonaws.com/mybucket/puppy.jpg`\. 

You will receive an HTTP response code 307 Temporary Redirect error and a message indicating what the correct URI is for your resource if you try to access a bucket outside the US East \(N\. Virginia\) Region with path\-style syntax that uses either of the following: 
+  `http://s3.amazonaws.com` 
+  An endpoint for a Region different from the one where the bucket resides\. For example, if you use `http://s3-us-west-1.amazonaws.com` for a bucket that was created in the US West \(N\. California\) Region\.

**Important**  
Buckets created after September 30, 2020, will support only virtual hosted\-style requests\. Path\-style requests will continue to be supported for buckets created on or before this date\. For more information, see [ Amazon S3 Path Deprecation Plan – The Rest of the Story](https://aws.amazon.com/blogs/aws/amazon-s3-path-deprecation-plan-the-rest-of-the-story/)\.

## Making Requests to Dual\-Stack Endpoints by Using the REST API<a name="rest-api-dual-stack"></a>

When using the REST API, you can directly access a dual\-stack endpoint by using a virtual hosted–style or a path style endpoint name \(URI\)\. All Amazon S3 dual\-stack endpoint names include the region in the name\. Unlike the standard IPv4\-only endpoints, both virtual hosted–style and a path\-style endpoints use region\-specific endpoint names\. 

**Example Virtual Hosted–Style Dual\-Stack Endpoint Request**  
You can use a virtual hosted–style endpoint in your REST request as shown in the following example that retrieves the `puppy.jpg` object from the bucket named `examplebucket`\.  

```
1. GET /puppy.jpg HTTP/1.1
2. Host: examplebucket.s3.dualstack.us-west-2.amazonaws.com
3. Date: Mon, 11 Apr 2016 12:00:00 GMT
4. x-amz-date: Mon, 11 Apr 2016 12:00:00 GMT
5. Authorization: authorization string
```

**Example Path\-Style Dual\-Stack Endpoint Request**  
Or you can use a path\-style endpoint in your request as shown in the following example\.  

```
1. GET /examplebucket/puppy.jpg HTTP/1.1
2. Host: s3.dualstack.us-west-2.amazonaws.com
3. Date: Mon, 11 Apr 2016 12:00:00 GMT
4. x-amz-date: Mon, 11 Apr 2016 12:00:00 GMT
5. Authorization: authorization string
```

For more information about dual\-stack endpoints, see [Using Amazon S3 Dual\-Stack Endpoints](dual-stack-endpoints.md)\.