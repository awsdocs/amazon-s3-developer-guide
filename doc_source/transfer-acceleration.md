# Amazon S3 Transfer Acceleration<a name="transfer-acceleration"></a>

Amazon S3 Transfer Acceleration enables fast, easy, and secure transfers of files over long distances between your client and an S3 bucket\. Transfer Acceleration takes advantage of Amazon CloudFront’s globally distributed edge locations\. As the data arrives at an edge location, data is routed to Amazon S3 over an optimized network path\.

When using Transfer Acceleration, additional data transfer charges may apply\. For more information about pricing, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

**Topics**
+ [Why Use Amazon S3 Transfer Acceleration?](#transfer-acceleration-why-use)
+ [Getting Started with Amazon S3 Transfer Acceleration](#transfer-acceleration-getting-started)
+ [Requirements for Using Amazon S3 Transfer Acceleration](#transfer-acceleration-requirements)
+ [Amazon S3 Transfer Acceleration Examples](transfer-acceleration-examples.md)

## Why Use Amazon S3 Transfer Acceleration?<a name="transfer-acceleration-why-use"></a>

You might want to use Transfer Acceleration on a bucket for various reasons, including the following:
+ You have customers that upload to a centralized bucket from all over the world\.
+ You transfer gigabytes to terabytes of data on a regular basis across continents\.
+ You are unable to utilize all of your available bandwidth over the Internet when uploading to Amazon S3\.

For more information about when to use Transfer Acceleration, see [Amazon S3 FAQs](https://aws.amazon.com/s3/faqs/#s3ta)\.

### Using the Amazon S3 Transfer Acceleration Speed Comparison Tool<a name="transfer-acceleration-speed-comparison"></a>

You can use the [Amazon S3 Transfer Acceleration Speed Comparison tool](http://s3-accelerate-speedtest.s3-accelerate.amazonaws.com/en/accelerate-speed-comparsion.html) to compare accelerated and non\-accelerated upload speeds across Amazon S3 regions\. The Speed Comparison tool uses multipart uploads to transfer a file from your browser to various Amazon S3 regions with and without using Transfer Acceleration\.

You can access the Speed Comparison tool using either of the following methods:
+ Copy the following URL into your browser window, replacing *region* with the region that you are using \(for example, us\-west\-2\) and *yourBucketName* with the name of the bucket that you want to evaluate: 

  `http://s3-accelerate-speedtest.s3-accelerate.amazonaws.com/en/accelerate-speed-comparsion.html?region=region&origBucketName=yourBucketName` 

   

  For a list of the regions supported by Amazon S3, see [Regions and Endpoints](http://docs.aws.amazon.com/general/latest/gr/rande.html#s3_region) in the *Amazon Web Services General Reference*\.
+ Use the Amazon S3 console\. For details, see [Enabling Transfer Acceleration](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-transfer-acceleration.html) in the *Amazon Simple Storage Service Console User Guide*\.

## Getting Started with Amazon S3 Transfer Acceleration<a name="transfer-acceleration-getting-started"></a>

To get started using Amazon S3 Transfer Acceleration, perform the following steps:

1. **Enable Transfer Acceleration on a bucket** – For your bucket to work with transfer acceleration, the bucket name must conform to DNS naming requirements and must not contain periods \("\."\)\. 

   You can enable Transfer Acceleration on a bucket any of the following ways:
   + Use the Amazon S3 console\. For more information, see [Enabling Transfer Acceleration](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-transfer-acceleration.html) in the *Amazon Simple Storage Service Console User Guide*\.
   + Use the REST API [PUT Bucket accelerate](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTaccelerate.html) operation\.
   + Use the AWS CLI and AWS SDKs\. For more information, see [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)\. 

1. **Transfer data to and from the acceleration\-enabled bucket by using one of the following s3\-accelerate endpoint domain names**:
   + `bucketname.s3-accelerate.amazonaws.com` – to access an acceleration\-enabled bucket\. 
   + `bucketname.s3-accelerate.dualstack.amazonaws.com` – to access an acceleration\-enabled bucket over IPv6\. Amazon S3 dual\-stack endpoints support requests to S3 buckets over IPv6 and IPv4\. The Transfer Acceleration dual\-stack endpoint only uses the virtual hosted\-style type of endpoint name\. For more information, see [Getting Started Making Requests over IPv6](ipv6-access.md#ipv6-access-getting-started) and [Using Amazon S3 Dual\-Stack Endpoints](dual-stack-endpoints.md)\. 
**Important**  
Support for the dual\-stack accelerated endpoint currently is only available from the AWS Java SDK\. Support for the AWS CLI and other AWS SDKs is coming soon\.
**Note**  
You can continue to use the regular endpoint in addition to the accelerate endpoints\.

   You can point your Amazon S3 PUT object and GET object requests to the s3\-accelerate endpoint domain name after you enable Transfer Acceleration\. For example, let's say you currently have a REST API application using [PUT Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html) that uses the host name **mybucket\.s3\.amazonaws\.com ** in the `PUT` request\. To accelerate the `PUT` you simply change the host name in your request to **mybucket\.s3\-accelerate\.amazonaws\.com**\. To go back to using the standard upload speed, simply change the name back to **mybucket\.s3\.amazonaws\.com**\.

   After Transfer Acceleration is enabled, it can take up to 20 minutes for you to realize the performance benefit\. However, the accelerate endpoint will be available as soon as you enable Transfer Acceleration\.

   You can use the accelerate endpoint in the AWS CLI, AWS SDKs, and other tools that transfer data to and from Amazon S3\. If you are using the AWS SDKs, some of the supported languages use an accelerate endpoint client configuration flag so you don't need to explicitly set the endpoint for Transfer Acceleration to *bucketname*\.s3\-accelerate\.amazonaws\.com\. For examples of how to use an accelerate endpoint client configuration flag, see [Amazon S3 Transfer Acceleration Examples](transfer-acceleration-examples.md)\.

You can use all of the Amazon S3 operations through the transaction acceleration endpoints, except for the following the operations: [GET Service \(list buckets\)](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTServiceGET.html), [PUT Bucket \(create bucket\)](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUT.html), and [DELETE Bucket](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETE.html)\. Also, Amazon S3 Transfer Acceleration does not support cross region copies using [PUT Object \- Copy](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html)\. 

## Requirements for Using Amazon S3 Transfer Acceleration<a name="transfer-acceleration-requirements"></a>

The following are the requirements for using Transfer Acceleration on an S3 bucket:
+ Transfer Acceleration is only supported on virtual style requests\. For more information about virtual style requests, see [Making Requests Using the REST API](RESTAPI.md)\. 
+ The name of the bucket used for Transfer Acceleration must be DNS\-compliant and must not contain periods \("\."\)\.
+ Transfer Acceleration must be enabled on the bucket\. After enabling Transfer Acceleration on a bucket it might take up to thirty minutes before the data transfer speed to the bucket increases\.
+ To access the bucket that is enabled for Transfer Acceleration, you must use the endpoint `bucketname.s3-accelerate.amazonaws.com`\. or the dual\-stack endpoint `bucketname.s3-accelerate.dualstack.amazonaws.com` to connect to the enabled bucket over IPv6\. 
+ You must be the bucket owner to set the transfer acceleration state\. The bucket owner can assign permissions to other users to allow them to set the acceleration state on a bucket\. The `s3:PutAccelerateConfiguration` permission permits users to enable or disable Transfer Acceleration on a bucket\. The `s3:GetAccelerateConfiguration` permission permits users to return the Transfer Acceleration state of a bucket, which is either `Enabled` or `Suspended.` For more information about these permissions, see [Permissions Related to Bucket Subresource Operations](using-with-s3-actions.md#using-with-s3-actions-related-to-bucket-subresources) and [Managing Access Permissions to Your Amazon S3 Resources](s3-access-control.md)\.

### More Info<a name="transfer-acceleration-moreinfo"></a>
+ [GET Bucket accelerate](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETaccelerate.html)
+ [PUT Bucket accelerate](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTaccelerate.html)