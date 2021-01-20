# Troubleshooting Amazon S3<a name="troubleshooting"></a>

This section describes how to troubleshoot Amazon S3 and explains how to get request IDs that you'll need when you contact AWS Support\.

**Topics**
+ [Troubleshooting Amazon S3 by Symptom](troubleshooting-by-symptom.md)
+ [Getting Amazon S3 Request IDs for AWS Support](#get-request-ids)
+ [Related Topics](#related-troubleshooting-topics)

For other troubleshooting and support topics, see the following:
+ [Troubleshooting CORS issues](cors-troubleshooting.md)
+ [Handling REST and SOAP errors](HandlingErrors.md)
+ [AWS Support Documentation](https://aws.amazon.com/documentation/aws-support/)

For troubleshooting information regarding third\-party tools, see [Getting Amazon S3 request IDs](https://forums.aws.amazon.com/thread.jspa?threadID=182409) in the AWS Developer Forums\.

## Getting Amazon S3 Request IDs for AWS Support<a name="get-request-ids"></a>

Whenever you need to contact AWS Support due to encountering errors or unexpected behavior in Amazon S3, you will need to get the request IDs associated with the failed action\. Getting these request IDs enables AWS Support to help you resolve the problems you're experiencing\. Request IDs come in pairs, are returned in every response that Amazon S3 processes \(even the erroneous ones\), and can be accessed through verbose logs\. There are a number of common methods for getting your request IDs including, S3 access logs and CloudTrail events/data events\.

After you've recovered these logs, copy and retain those two values, because you'll need them when you contact AWS Support\. For information about contacting AWS Support, see [Contact Us](https://aws.amazon.com/contact-us/)\.

**Topics**
+ [Using HTTP to Obtain Request IDs](#http-request-id)
+ [Using a Web Browser to Obtain Request IDs](#browser-request-id)
+ [Using AWS SDKs to Obtain Request IDs](#sdk-request-ids)
+ [Using the AWS CLI to Obtain Request IDs](#cli-request-id)

### Using HTTP to Obtain Request IDs<a name="http-request-id"></a>

You can obtain your request IDs, `x-amz-request-id` and `x-amz-id-2` by logging the bits of an HTTP request before it reaches the target application\. There are a variety of third\-party tools that can be used to recover verbose logs for HTTP requests\. Choose one you trust, and run the tool, listening on the port that your Amazon S3 traffic travels on, as you send out another Amazon S3 HTTP request\.

For HTTP requests, the pair of request IDs will look like the following examples\.

```
x-amz-request-id: 79104EXAMPLEB723 
x-amz-id-2: IOWQ4fDEXAMPLEQM+ey7N9WgVhSnQ6JEXAMPLEZb7hSQDASK+Jd1vEXAMPLEa3Km
```

**Note**  
HTTPS requests are encrypted and hidden in most packet captures\.

### Using a Web Browser to Obtain Request IDs<a name="browser-request-id"></a>

Most web browsers have developer tools that allow you to view request headers\.

For web browser\-based requests that return an error, the pair of requests IDs will look like the following examples\.

```
<Error><Code>AccessDenied</Code><Message>Access Denied</Message>
<RequestId>79104EXAMPLEB723</RequestId><HostId>IOWQ4fDEXAMPLEQM+ey7N9WgVhSnQ6JEXAMPLEZb7hSQDASK+Jd1vEXAMPLEa3Km</HostId></Error>
```

For obtaining the request ID pair from successful requests, you'll need to use the developer tools to look at the HTTP response headers\. For information about developer tools for specific browsers, see **Amazon S3 Troubleshooting \- How to recover your S3 request IDs** in the AWS Developer Forums\.

### Using AWS SDKs to Obtain Request IDs<a name="sdk-request-ids"></a>

The following sections include information for configuring logging using an AWS SDK\. While you can enable verbose logging on every request and response, you should not enable logging in production systems since large requests/responses can cause significant slowdown in an application\.

For AWS SDK requests, the pair of request IDs will look like the following examples\.

```
Status Code: 403, AWS Service: Amazon S3, AWS Request ID: 79104EXAMPLEB723  
AWS Error Code: AccessDenied  AWS Error Message: Access Denied  
S3 Extended Request ID: IOWQ4fDEXAMPLEQM+ey7N9WgVhSnQ6JEXAMPLEZb7hSQDASK+Jd1vEXAMPLEa3Km
```

#### Using the SDK for PHP to Obtain Request IDs<a name="php-request-id"></a>

You can configure logging using PHP\. For more information, see [How can I see what data is sent over the wire?](https://docs.aws.amazon.com/aws-sdk-php/guide/latest/faq.html#how-can-i-see-what-data-is-sent-over-the-wire) in the FAQ for the *AWS SDK for PHP*\.

#### Using the SDK for Java to Obtain Request IDs<a name="java-request-id"></a>

You can enable logging for specific requests or responses, allowing you to catch and return only the relevant headers\. To do this, import the `com.amazonaws.services.s3.S3ResponseMetadata` class\. Afterwards, you can store the request in a variable before performing the actual request\. Call `getCachedResponseMetadata(AmazonWebServiceRequest request).getRequestID()` to get the logged request or response\.

**Example**  

```
PutObjectRequest req = new PutObjectRequest(bucketName, key, createSampleFile());
s3.putObject(req);
S3ResponseMetadata md = s3.getCachedResponseMetadata(req);
System.out.println("Host ID: " + md.getHostId() + " RequestID: " + md.getRequestId());
```

Alternatively, you can use verbose logging of every Java request and response\. For more information, see [Verbose Wire Logging](https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/java-dg-logging.html#sdk-net-logging-verbose) in the Logging AWS SDK for Java Calls topic in the *AWS SDK for Java Developer Guide*\.

#### Using the AWS SDK for \.NET to Obtain Request IDs<a name="net-request-id"></a>

You can configure logging in AWS SDK for \.NET using the built\-in `System.Diagnostics` logging tool\. For more information, see the [ Logging with the AWS SDK for \.NET](https://aws.amazon.com/blogs/developer/logging-with-the-aws-sdk-for-net/) AWS Developer Blog post\.

**Note**  
By default, the returned log contains only error information\. The config file needs to have `AWSLogMetrics` \(and optionally, `AWSResponseLogging`\) added to get the request IDs\.

#### Using the SDK for Python \(Boto3\) to Obtain Request IDs<a name="python-request-id"></a>

With SDK for Python \(Boto3\), you can log specific responses, which enables you to capture only the relevant headers\. The following code shows you how to log parts of the response to a file:

```
import logging
import boto3
logging.basicConfig(filename='logfile.txt', level=logging.INFO)
logger = logging.getLogger(__name__)
s3 = boto3.resource('s3')
response = s3.Bucket(bucket_name).Object(object_key).put()
logger.info("HTTPStatusCode: %s", response['ResponseMetadata']['HTTPStatusCode'])
logger.info("RequestId: %s", response['ResponseMetadata']['RequestId'])
logger.info("HostId: %s", response['ResponseMetadata']['HostId'])
logger.info("Date: %s", response['ResponseMetadata']['HTTPHeaders']['date'])
```

You can also catch exceptions and log relevant information when an exception is raised\. For details, see [Discerning useful information from error responses](https://boto3.amazonaws.com/v1/documentation/api/latest/guide/error-handling.html#discerning-useful-information-from-error-responses) in the *Boto3 developer guide*

Additionally, you can configure Boto3 to output verbose debugging logs using the following code:

```
import boto3
boto3.set_stream_logger('', logging.DEBUG)
```

For more information, see [https://boto3.amazonaws.com/v1/documentation/api/latest/reference/core/boto3.html#boto3.set_stream_logger](https://boto3.amazonaws.com/v1/documentation/api/latest/reference/core/boto3.html#boto3.set_stream_logger) in the *Boto3 reference*\.

#### Using the SDK for Ruby to Obtain Request IDs<a name="ruby-request-id"></a>

You can get your request IDs using either the SDK for Ruby \- Version 1, Version 2, or Version 3\.
+ **Using the SDK for Ruby \- Version 1**– You can enable HTTP wire logging globally with the following line of code\.

  ```
  s3 = AWS::S3.new(:logger => Logger.new($stdout), :http_wire_trace => true)
  ```
+ **Using the SDK for Ruby \- Version 2 or Version 3**– You can enable HTTP wire logging globally with the following line of code\.

  ```
  s3 = Aws::S3::Client.new(:logger => Logger.new($stdout), :http_wire_trace => true)
  ```

### Using the AWS CLI to Obtain Request IDs<a name="cli-request-id"></a>

You can get your request IDs in the AWS CLI by adding `--debug` to your command\.

## Related Topics<a name="related-troubleshooting-topics"></a>

For other troubleshooting and support topics, see the following:
+ [Troubleshooting CORS issues](cors-troubleshooting.md)
+ [Handling REST and SOAP errors](HandlingErrors.md)
+ [AWS Support Documentation](https://aws.amazon.com/documentation/aws-support/)

For troubleshooting information regarding third\-party tools, see [Getting Amazon S3 request IDs](https://forums.aws.amazon.com/thread.jspa?threadID=182409) in the AWS Developer Forums\.