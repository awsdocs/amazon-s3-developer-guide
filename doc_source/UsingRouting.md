# Request Routing<a name="UsingRouting"></a>

**Topics**
+ [Request Redirection and the REST API](Redirects.md)
+ [DNS Considerations](DNSConsiderations.md)

Programs that make requests against buckets created using the <CreateBucketConfiguration> API must support redirects\. Additionally, some clients that do not respect DNS TTLs might encounter issues\.

This section describes routing and DNS issues to consider when designing your service or application for use with Amazon S3\.