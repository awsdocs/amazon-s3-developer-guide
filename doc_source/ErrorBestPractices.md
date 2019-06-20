# Amazon S3 Error Best Practices<a name="ErrorBestPractices"></a>

When designing an application for use with Amazon S3, it is important to handle Amazon S3 errors appropriately\. This section describes issues to consider when designing your application\.

## Retry InternalErrors<a name="UsingErrorsRetry"></a>

Internal errors are errors that occur within the Amazon S3 environment\. 

Requests that receive an InternalError response might not have processed\. For example, if a PUT request returns InternalError, a subsequent GET might retrieve the old value or the updated value\. 

If Amazon S3 returns an InternalError response, retry the request\.

## Tune Application for Repeated SlowDown errors<a name="UsingErrorsSlowDown"></a>

As with any distributed system, S3 has protection mechanisms which detect intentional or unintentional resource over\-consumption and react accordingly\. SlowDown errors can occur when a high request rate triggers one of these mechanisms\. Reducing your request rate will decrease or eliminate errors of this type\. Generally speaking, most users will not experience these errors regularly; however, if you would like more information or are experiencing high or unexpected SlowDown errors, please post to our Amazon S3 developer forum  [https://forums\.aws\.amazon\.com/](https://forums.aws.amazon.com/) or sign up for AWS Premium Support [https://aws\.amazon\.com/premiumsupport/](https://aws.amazon.com/premiumsupport/)\.

## Isolate Errors<a name="UsingErrorsIsolate"></a>

**Note**  
 SOAP support over HTTP is deprecated, but it is still available over HTTPS\. New Amazon S3 features will not be supported for SOAP\. We recommend that you use either the REST API or the AWS SDKs\. 

 Amazon S3 provides a set of error codes that are used by both the SOAP and REST API\. The SOAP API returns standard Amazon S3 error codes\. The REST API is designed to look like a standard HTTP server and interact with existing HTTP clients \(e\.g\., browsers, HTTP client libraries, proxies, caches, and so on\)\. To ensure the HTTP clients handle errors properly, we map each Amazon S3 error to an HTTP status code\. 

 HTTP status codes are less expressive than Amazon S3 error codes and contain less information about the error\. For example, the `NoSuchKey` and `NoSuchBucket` Amazon S3 errors both map to the `HTTP 404 Not Found` status code\. 

 Although the HTTP status codes contain less information about the error, clients that understand HTTP, but not the Amazon S3 API, will usually handle the error correctly\. 

 Therefore, when handling errors or reporting Amazon S3 errors to end users, use the Amazon S3 error code instead of the HTTP status code as it contains the most information about the error\. Additionally, when debugging your application, you should also consult the human readable <Details> element of the XML error response\. 