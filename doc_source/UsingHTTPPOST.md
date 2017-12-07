# Browser\-Based Uploads Using POST \(AWS Signature Version 2\)<a name="UsingHTTPPOST"></a>

Amazon S3 supports POST, which allows your users to upload content directly to Amazon S3\. POST is designed to simplify uploads, reduce upload latency, and save you money on applications where users upload data to store in Amazon S3\.

**Note**  
The request authentication discussed in this section is based on AWS Signature Version 2, a protocol for authenticating inbound API requests to AWS services\.   
Amazon S3 now supports Signature Version 4, a protocol for authenticating inbound API requests to AWS services, in all AWS regions\. At this time, AWS regions created before January 30, 2014 will continue to support the previous protocol, Signature Version 2\. Any new regions after January 30, 2014 will support only Signature Version 4 and therefore all requests to those regions must be made with Signature Version 4\. For more information, see [Authenticating Requests in Browser\-Based Uploads Using POST \(AWS Signature Version 4\)](http://docs.aws.amazon.com/AmazonS3/latest/API/sigv4-authentication-HTTPPOST.html) in the *Amazon Simple Storage Service API Reference*\. 

The following figure shows an upload using Amazon S3 POST\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/s3_post.png)


**Uploading Using POST**  

|  |  | 
| --- |--- |
| 1 | The user opens a web browser and accesses your web page\. | 
| 2 | Your web page contains an HTTP form that contains all the information necessary for the user to upload content to Amazon S3\. | 
| 3 | The user uploads content directly to Amazon S3\. | 

**Note**  
Query string authentication is not supported for POST\.