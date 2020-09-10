# Best practices and guidelines for S3 RTC<a name="rtc-best-practices"></a>

When replicating data in Amazon S3 using S3 Replication Time Control \(S3 RTC\), follow these best practice guidelines to optimize replication performance for your workloads\. 

**Topics**
+ [Amazon S3 Replication and request rate performance guidelines](#rtc-request-rate-performance)
+ [Estimating your replication request rates](#estimating-replication-request-rates)
+ [Exceeding S3 RTC data transfer rate limits](#exceed-rtc-data-transfer-limits)
+ [AWS KMS encrypted object replication request rates](#kms-object-replication-request-rates)

## Amazon S3 Replication and request rate performance guidelines<a name="rtc-request-rate-performance"></a>

When uploading and retrieving storage from Amazon S3, your applications can achieve thousands of transactions per second in request performance\. For example, an application can achieve at least 3,500 PUT/COPY/POST/DELETE or 5,500 GET/HEAD requests per second per prefix in an S3 bucket, including the requests that S3 replication makes on your behalf\. There are no limits to the number of prefixes in a bucket\. You can increase your read or write performance by parallelizing reads\. For example, if you create 10 prefixes in an S3 bucket to parallelize reads, you could scale your read performance to 55,000 read requests per second\. 

Amazon S3 automatically scales in response to sustained request rates above these guidelines, or sustained request rates concurrent with LIST requests\. While Amazon S3 is internally optimizing for the new request rate, you might receive HTTP 503 request responses temporarily until the optimization is complete\. This might occur with increases in request per second rates, or when you first enable S3 RTC\. During these periods, your replication latency might increase\. The S3 RTC service level agreement \(SLA\) doesn’t apply to time periods when Amazon S3 performance guidelines on requests per second are exceeded\. 

The S3 RTC SLA also doesn’t apply during time periods where your replication data transfer rate exceeds the default 1 Gbps limit\. If you expect your replication transfer rate to exceed 1 Gbps, you can contact [AWS Support Center](https://console.aws.amazon.com/support/home#/) or use [Service Quotas](https://docs.aws.amazon.com/general/latest/gr/aws_service_limits.html) to request an increase in your limit\. 

## Estimating your replication request rates<a name="estimating-replication-request-rates"></a>

Your total request rate including the requests that Amazon S3 replication makes on your behalf should be within the Amazon S3 request rate guidelines for both the replication source and destination buckets\. For each object replicated, Amazon S3 replication makes up to five GET/HEAD requests and one PUT request to the source bucket, and one PUT request to the destination bucket\.

For example, if you expect to replicate 100 objects per second, Amazon S3 replication might perform an additional 100 PUT requests on your behalf for a total of 200 PUTs per second to the source S3 bucket\. Amazon S3 replication also might perform up to 500 GET/HEAD \(5 GET/HEAD requests for each object replicated\.\) 

**Note**  
You incur costs for only one PUT request per object replicated\. For more information, see the pricing information in the [Amazon S3 FAQ on replication](https://aws.amazon.com/s3/faqs/#Replication)\. 

## Exceeding S3 RTC data transfer rate limits<a name="exceed-rtc-data-transfer-limits"></a>

If you expect your S3 Replication Time Control data transfer rate to exceed the default 1 Gbps limit, contact [AWS Support Center](https://console.aws.amazon.com/support/home#/) or use [Service Quotas](https://docs.aws.amazon.com/general/latest/gr/aws_service_limits.html) to request an increase in your limit\. 

## AWS KMS encrypted object replication request rates<a name="kms-object-replication-request-rates"></a>

When you replicate objects encrypted with server\-side encryption \(SSE\-KMS\) using Amazon S3 replication, AWS Key Management Service \(AWS KMS\) requests per second limits apply\. AWS KMS might reject an otherwise valid request because your request rate exceeds the limit for the number of requests per second\. When a request is throttled, AWS KMS returns a `ThrottlingException` error\. The AWS KMS request rate limit applies to requests you make directly and to requests made by Amazon S3 replication on your behalf\. 

For example, if you expect to replicate 1,000 objects per second, you can subtract 2,000 requests from your AWS KMS request rate limit\. The resulting request rate per second is available for your AWS KMS workloads excluding replication\. You can use [AWS KMS request metrics in Amazon CloudWatch](https://docs.aws.amazon.com/kms/latest/developerguide/monitoring-cloudwatch.html) to monitor the total AWS KMS request rate on your AWS account\.