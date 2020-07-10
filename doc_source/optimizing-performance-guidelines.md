# Performance Guidelines for Amazon S3<a name="optimizing-performance-guidelines"></a>

When building applications that upload and retrieve objects from Amazon S3, follow our best practices guidelines to optimize performance\. We also offer more detailed [Performance Design Patterns](optimizing-performance-design-patterns.md)\. 

To obtain the best performance for your application on Amazon S3, we recommend the following guidelines\.

**Topics**
+ [Measure Performance](#optimizing-performance-guidelines-measure)
+ [Scale Storage Connections Horizontally](#optimizing-performance-guidelines-scale)
+ [Use Byte\-Range Fetches](#optimizing-performance-guidelines-get-range)
+ [Retry Requests for Latency\-Sensitive Applications](#optimizing-performance-guidelines-retry)
+ [Combine Amazon S3 \(Storage\) and Amazon EC2 \(Compute\) in the Same AWS Region](#optimizing-performance-guidelines-combine)
+ [Use Amazon S3 Transfer Acceleration to Minimize Latency Caused by Distance](#optimizing-performance-guidelines-acceleration)
+ [Use the Latest Version of the AWS SDKs](#optimizing-performance-guidelines-sdk)

## Measure Performance<a name="optimizing-performance-guidelines-measure"></a>

When optimizing performance, look at network throughput, CPU, and DRAM requirements\. Depending on the mix of demands for these different resources, it might be worth evaluating different [Amazon EC2](https://docs.aws.amazon.com/ec2/index.html) instance types\. For more information about instance types, see [Instance Types](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/instance-types.html) in the *Amazon EC2 User Guide for Linux Instances*\. 

It’s also helpful to look at DNS lookup time, latency, and data transfer speed using HTTP analysis tools when measuring performance\.

## Scale Storage Connections Horizontally<a name="optimizing-performance-guidelines-scale"></a>

Spreading requests across many connections is a common design pattern to horizontally scale performance\. When you build high performance applications, think of Amazon S3 as a very large distributed system, not as a single network endpoint like a traditional storage server\. You can achieve the best performance by issuing multiple concurrent requests to Amazon S3\. Spread these requests over separate connections to maximize the accessible bandwidth from Amazon S3\. Amazon S3 doesn't have any limits for the number of connections made to your bucket\. 

## Use Byte\-Range Fetches<a name="optimizing-performance-guidelines-get-range"></a>

Using the `Range` HTTP header in a [GET Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html) request, you can fetch a byte\-range from an object, transferring only the specified portion\. You can use concurrent connections to Amazon S3 to fetch different byte ranges from within the same object\. This helps you achieve higher aggregate throughput versus a single whole\-object request\. Fetching smaller ranges of a large object also allows your application to improve retry times when requests are interrupted\. For more information, see [Getting objects](GettingObjectsUsingAPIs.md)\.

Typical sizes for byte\-range requests are 8 MB or 16 MB\. If objects are PUT using a multipart upload, it’s a good practice to GET them in the same part sizes \(or at least aligned to part boundaries\) for best performance\. GET requests can directly address individual parts; for example, `GET ?partNumber=N.`

## Retry Requests for Latency\-Sensitive Applications<a name="optimizing-performance-guidelines-retry"></a>

Aggressive timeouts and retries help drive consistent latency\. Given the large scale of Amazon S3, if the first request is slow, a retried request is likely to take a different path and quickly succeed\. The AWS SDKs have configurable timeout and retry values that you can tune to the tolerances of your specific application\.

## Combine Amazon S3 \(Storage\) and Amazon EC2 \(Compute\) in the Same AWS Region<a name="optimizing-performance-guidelines-combine"></a>

Although S3 bucket names are [globally unique](https://docs.aws.amazon.com/AmazonS3/latest/dev/UsingBucket.html), each bucket is stored in a Region that you select when you create the bucket\. To optimize performance, we recommend that you access the bucket from Amazon EC2 instances in the same AWS Region when possible\. This helps reduce network latency and data transfer costs\.

For more information about data transfer costs, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

## Use Amazon S3 Transfer Acceleration to Minimize Latency Caused by Distance<a name="optimizing-performance-guidelines-acceleration"></a>

[Amazon S3 Transfer Acceleration](transfer-acceleration.md) manages fast, easy, and secure transfers of files over long geographic distances between the client and an S3 bucket\. Transfer Acceleration takes advantage of the globally distributed edge locations in [Amazon CloudFront](https://docs.aws.amazon.com/cloudfront/index.html)\. As the data arrives at an edge location, it is routed to Amazon S3 over an optimized network path\. Transfer Acceleration is ideal for transferring gigabytes to terabytes of data regularly across continents\. It's also useful for clients that upload to a centralized bucket from all over the world\.

You can use the [Amazon S3 Transfer Acceleration Speed Comparison tool](https://s3-accelerate-speedtest.s3-accelerate.amazonaws.com/en/accelerate-speed-comparsion.html) to compare accelerated and non\-accelerated upload speeds across Amazon S3 Regions\. The Speed Comparison tool uses multipart uploads to transfer a file from your browser to various Amazon S3 Regions with and without using Amazon S3 Transfer Acceleration\.

## Use the Latest Version of the AWS SDKs<a name="optimizing-performance-guidelines-sdk"></a>

The AWS SDKs provide built\-in support for many of the recommended guidelines for optimizing Amazon S3 performance\. The SDKs provide a simpler API for taking advantage of Amazon S3 from within an application and are regularly updated to follow the latest best practices\. For example, the SDKs include logic to automatically retry requests on HTTP 503 errors and are investing in code to respond and adapt to slow connections\. 

The SDKs also provide the [Transfer Manager](https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/examples-s3-transfermanager.html), which automates horizontally scaling connections to achieve thousands of requests per second, using byte\-range requests where appropriate\. It’s important to use the latest version of the AWS SDKs to obtain the latest performance optimization features\.

You can also optimize performance when you are using HTTP REST API requests\. When using the REST API, you should follow the same best practices that are part of the SDKs\. Allow for timeouts and retries on slow requests, and multiple connections to allow fetching of object data in parallel\. For information about using the REST API, see the [Amazon Simple Storage Service API Reference](https://docs.aws.amazon.com/AmazonS3/latest/API/)\.