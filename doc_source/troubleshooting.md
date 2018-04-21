# Troubleshooting Amazon S3<a name="troubleshooting"></a>

This section describes how to troubleshoot Amazon S3 and explains how to get request IDs that you'll need when you contact AWS Support\.

**Topics**
+ [Troubleshooting Amazon S3 by Symptom](#troubleshooting-by-symptom)
+ [Getting Amazon S3 Request IDs for AWS Support](#get-request-ids)
+ [Related Topics](#related-troubleshooting-topics)

## Troubleshooting Amazon S3 by Symptom<a name="troubleshooting-by-symptom"></a>

The following topics lists symptoms to help you troubleshoot some of the issues that you might encounter when working with Amazon S3\.

**Topics**
+ [Significant Increases in HTTP 503 Responses to Requests to Buckets with Versioning Enabled](#troubleshooting-by-symptom-increase-503-reponses)
+ [Unexpected Behavior When Accessing Buckets Set with CORS](#troubleshooting-by-symptom-increase)

### Significant Increases in HTTP 503 Responses to Amazon S3 Requests to Buckets with Versioning Enabled<a name="troubleshooting-by-symptom-increase-503-reponses"></a>

If you notice a significant increase in the number of HTTP 503\-slow down responses received for Amazon S3 PUT or DELETE object requests to a bucket that has versioning enabled, you might have one or more objects in the bucket for which there are millions of versions\. When you have objects with millions of versions, Amazon S3 automatically throttles requests to the bucket to protect the customer from an excessive amount of request traffic, which could potentially impede other requests made to the same bucket\. 

To determine which S3 objects have millions of versions, use the Amazon S3 inventory tool\. The inventory tool generates a report that provides a flat file list of the objects in a bucket\. For more information, see [ Amazon S3 Inventory](storage-inventory.md)\.

The Amazon S3 team encourages customers to investigate applications that repeatedly overwrite the same S3 object, potentially creating millions of versions for that object, to determine whether the application is working as intended\. If you have a use case that requires millions of versions for one or more S3 objects, contact the AWS Support team at [AWS Support](https://console.aws.amazon.com/support/home) to discuss your use case and to help us assist you in determining the optimal solution for your use case scenario\.

### Unexpected Behavior When Accessing Buckets Set with CORS<a name="troubleshooting-by-symptom-increase"></a>

 If you encounter unexpected behavior when accessing buckets set with the cross\-origin resource sharing \(CORS\) configuration, see [Troubleshooting CORS Issues](cors-troubleshooting.md)\.

## Getting Amazon S3 Request IDs for AWS Support<a name="get-request-ids"></a>

Whenever you need to contact AWS Support due to encountering errors or unexpected behavior in Amazon S3, you will need to get the request IDs associated with the failed action\. Getting these request IDs enables AWS Support to help you resolve the problems you're experiencing\. Request IDs come in pairs, are returned in every response that Amazon S3 processes \(even the erroneous ones\), and can be accessed through verbose logs\. There are a number of common methods for getting your request IDs\.

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

You can configure logging using PHP\. For more information, see [How can I see what data is sent over the wire?](http://docs.aws.amazon.com/aws-sdk-php/guide/latest/faq.html#how-can-i-see-what-data-is-sent-over-the-wire) in the FAQ for the *AWS SDK for PHP*\.

#### Using the SDK for Java to Obtain Request IDs<a name="java-request-id"></a>

You can enable logging for specific requests or responses, allowing you to catch and return only the relevant headers\. To do this, import the `com.amazonaws.services.s3.s3ResponseMetadata` class\. Afterwards, you can store the request in a variable before performing the actual request\. Call `getCachedResponseMetadata(AmazonWebServiceRequest request).getRequestID()` to get the logged request or response\.

**Example**  

```
PutObjectRequest req = new PutObjectRequest(bucketName, key, createSampleFile());
s3.putObject(req);
S3ResponseMetadata md = s3.getCachedResponseMetadata(req);
System.out.println("Host ID: " + md.getHostId() + " RequestID: " + md.getRequestId());
```

Alternatively, you can use verbose logging of every Java request and response\. For more information, see [Verbose Wire Logging](http://docs.aws.amazon.com/AWSSdkDocsJava/latest/DeveloperGuide/java-dg-logging.html#sdk-net-logging-verbose) in the Logging AWS SDK for Java Calls topic in the *AWS SDK for Java Developer Guide*\.

#### Using the AWS SDK for \.NET to Obtain Request IDs<a name="net-request-id"></a>

You can configure logging in AWS SDK for \.NET using the built\-in `System.Diagnostics` logging tool\. For more information, see the [ Logging with the AWS SDK for \.NET](http://aws.amazon.com/blogs/developer/logging-with-the-aws-sdk-for-net/) AWS Developer Blog post\.

**Note**  
By default, the returned log contains only error information\. The config file needs to have `AWSLogMetrics` \(and optionally, `AWSResponseLogging`\) added to get the request IDs\.

#### Using the SDK for Python to Obtain Request IDs<a name="python-request-id"></a>

You can configure logging in Python by adding the following lines to your code to output debug information to a file\.

```
import logging 
logging.basicConfig(filename="mylog.log", level=logging.DEBUG)
```

If you’re using the Boto Python interface for AWS, you can set the debug level to two as per the Boto docs, [here](http://docs.pythonboto.org/en/latest/boto_config_tut.html#boto)\.

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
+ [Troubleshooting CORS Issues](cors-troubleshooting.md)
+ [Handling REST and SOAP Errors](HandlingErrors.md)
+ [AWS Support Documentation](https://aws.amazon.com/documentation/aws-support/)

For troubleshooting information regarding third\-party tools, see [Getting Amazon S3 request IDs](https://forums.aws.amazon.com/thread.jspa?threadID=182409) in the AWS Developer Forums\.