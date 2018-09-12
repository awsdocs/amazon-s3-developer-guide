# Request Rate and Performance Guidelines<a name="request-rate-perf-considerations"></a>

Amazon S3 automatically scales to high request rates\. For example, your application can achieve at least 3,500 PUT/POST/DELETE and 5,500 GET requests per second per prefix in a bucket\. There are no limits to the number of prefixes in a bucket\. It is simple to increase your read or write performance exponentially\. For example, if you create 10 prefixes in an Amazon S3 bucket to parallelize reads, you could scale your read performance to 55,000 read requests per second\.

If your Amazon S3 workload uses server\-side encryption with AWS Key Management Service \(SSE\-KMS\), see [AWS KMS Limits](http://docs.aws.amazon.com/kms/latest/developerguide/limits.html) in the *AWS Key Management Service Developer Guide* for information about the request rates supported for your use case\.

## GET\-Intensive Workloads<a name="get-workload-considerations"></a>

If your workload is mainly sending GET requests, in addition to the preceding guidelines, you should consider using Amazon CloudFront for performance optimization\. By integrating CloudFront with Amazon S3, you can distribute content to your users with low latency and a high data transfer rate\. You also send fewer direct requests to Amazon S3, which reduces your costs\. 

For example, suppose that you have a few objects that are very popular\. CloudFront fetches those objects from Amazon S3 and caches them\. CloudFront can then serve future requests for the objects from its cache, reducing the number of GET requests it sends to Amazon S3\. For more information, see the [Amazon CloudFront](https://aws.amazon.com/cloudfront/) product detail page\.