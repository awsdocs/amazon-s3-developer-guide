# The REST Error Response<a name="UsingRESTError"></a>

**Topics**
+ [Response Headers](#UsingRESTErrorResponseHeaders)
+ [Error Response](ErrorResponse.md)

If a REST request results in an error, the HTTP reply has: 
+ An XML error document as the response body 
+ Content\-Type: application/xml
+ An appropriate 3xx, 4xx, or 5xx HTTP status code

Following is an example of a REST Error Response\.

```
1. <?xml version="1.0" encoding="UTF-8"?>
2. <Error>
3.   <Code>NoSuchKey</Code>
4.   <Message>The resource you requested does not exist</Message>
5.   <Resource>/mybucket/myfoto.jpg</Resource> 
6.   <RequestId>4442587FB7D0A2F9</RequestId>
7. </Error>
```

For more information about Amazon S3 errors, go to [ErrorCodeList](http://docs.aws.amazon.com/AmazonS3/latest/API/ErrorResponses.html)\.

## Response Headers<a name="UsingRESTErrorResponseHeaders"></a>

Following are response headers returned by all operations:
+ `x-amz-request-id:` A unique ID assigned to each request by the system\. In the unlikely event that you have problems with Amazon S3, Amazon can use this to help troubleshoot the problem\.
+ `x-amz-id-2:` A special token that will help us to troubleshoot problems\.