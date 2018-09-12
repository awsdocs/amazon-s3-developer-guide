# Making Requests to Amazon S3 over IPv6<a name="ipv6-access"></a>

Amazon Simple Storage Service \(Amazon S3\) supports the ability to access S3 buckets using the Internet Protocol version 6 \(IPv6\), in addition to the IPv4 protocol\. Amazon S3 dual\-stack endpoints support requests to S3 buckets over IPv6 and IPv4\. There are no additional charges for accessing Amazon S3 over IPv6\. For more information about pricing, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

**Topics**
+ [Getting Started Making Requests over IPv6](#ipv6-access-getting-started)
+ [Using IPv6 Addresses in IAM Policies](#ipv6-access-iam)
+ [Testing IP Address Compatibility](#ipv6-access-test-compatabilty)
+ [Using Amazon S3 Dual\-Stack Endpoints](dual-stack-endpoints.md)

## Getting Started Making Requests over IPv6<a name="ipv6-access-getting-started"></a>

To make a request to an S3 bucket over IPv6, you need to use a dual\-stack endpoint\. The next section describes how to make requests over IPv6 by using dual\-stack endpoints\. 

The following are some things you should know before trying to access a bucket over IPv6: 
+ The client and the network accessing the bucket must be enabled to use IPv6\. 
+ Both virtual hosted\-style and path style requests are supported for IPv6 access\. For more information, see [Amazon S3 Dual\-Stack Endpoints](dual-stack-endpoints.md#dual-stack-endpoints-description)\.
+ If you use source IP address filtering in your AWS Identity and Access Management \(IAM\) user or bucket policies, you need to update the policies to include IPv6 address ranges\. For more information, see [Using IPv6 Addresses in IAM Policies](#ipv6-access-iam)\.
+ When using IPv6, server access log files output IP addresses in an IPv6 format\. You need to update existing tools, scripts, and software that you use to parse Amazon S3 log files so that they can parse the IPv6 formatted `Remote IP` addresses\. For more information, see [Server Access Log Format](LogFormat.md) and [Amazon S3 Server Access Logging](ServerLogs.md)\. 
**Note**  
If you experience issues related to the presence of IPv6 addresses in log files, contact [AWS Support](https://aws.amazon.com/premiumsupport/)\.

### Making Requests over IPv6 by Using Dual\-Stack Endpoints<a name="ipv6-access-api"></a>

You make requests with Amazon S3 API calls over IPv6 by using dual\-stack endpoints\. The Amazon S3 API operations work the same way whether you're accessing Amazon S3 over IPv6 or over IPv4\. Performance should be the same too\.

When using the REST API, you access a dual\-stack endpoint directly\. For more information, see [Dual\-Stack Endpoints](dual-stack-endpoints.md#dual-stack-endpoints-description)\.

When using the AWS Command Line Interface \(AWS CLI\) and AWS SDKs, you can use a parameter or flag to change to a dual\-stack endpoint\. You can also specify the dual\-stack endpoint directly as an override of the Amazon S3 endpoint in the config file\.

You can use a dual\-stack endpoint to access a bucket over IPv6 from any of the following:
+ The AWS CLI, see [Using Dual\-Stack Endpoints from the AWS CLI](dual-stack-endpoints.md#dual-stack-endpoints-cli)\.
+ The AWS SDKs, see [Using Dual\-Stack Endpoints from the AWS SDKs](dual-stack-endpoints.md#dual-stack-endpoints-sdks)\.
+ The REST API, see [Making Requests to Dual\-Stack Endpoints by Using the REST API](RESTAPI.md#rest-api-dual-stack)\.

### Features Not Available over IPv6<a name="ipv6-not-supported"></a>

The following features are not currently supported when accessing an S3 bucket over IPv6:
+ Static website hosting from an S3 bucket
+ BitTorrent

## Using IPv6 Addresses in IAM Policies<a name="ipv6-access-iam"></a>

Before trying to access a bucket using IPv6, you must ensure that any IAM user or S3 bucket polices that are used for IP address filtering are updated to include IPv6 address ranges\. IP address filtering policies that are not updated to handle IPv6 addresses may result in clients incorrectly losing or gaining access to the bucket when they start using IPv6\. For more information about managing access permissions with IAM, see [Managing Access Permissions to Your Amazon S3 Resources](s3-access-control.md)\.

IAM policies that filter IP addresses use [IP Address Condition Operators](http://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements.html#Conditions_IPAddress)\. The following bucket policy identifies the 54\.240\.143\.\* range of allowed IPv4 addresses by using IP address condition operators\. Any IP addresses outside of this range will be denied access to the bucket \(`examplebucket`\)\. Since all IPv6 addresses are outside of the allowed range, this policy prevents IPv6 addresses from being able to access `examplebucket`\. 

```
 1. {
 2.   "Version": "2012-10-17",
 3.   "Statement": [
 4.     {
 5.       "Sid": "IPAllow",
 6.       "Effect": "Allow",
 7.       "Principal": "*",
 8.       "Action": "s3:*",
 9.       "Resource": "arn:aws:s3:::examplebucket/*",
10.       "Condition": {
11.          "IpAddress": {"aws:SourceIp": "54.240.143.0/24"}
12.       } 
13.     } 
14.   ]
15. }
```

You can modify the bucket policy's `Condition` element to allow both IPv4 \(`54.240.143.0/24`\) and IPv6 \(`2001:DB8:1234:5678::/64`\) address ranges as shown in the following example\. You can use the same type of `Condition` block shown in the example to update both your IAM user and bucket policies\.

```
1.        "Condition": {
2.          "IpAddress": {
3.             "aws:SourceIp": [
4.               "54.240.143.0/24",
5.                "2001:DB8:1234:5678::/64"
6.              ]
7.           }
8.         }
```

Before using IPv6 you must update all relevant IAM user and bucket policies that use IP address filtering to allow IPv6 address ranges\. We recommend that you update your IAM policies with your organization's IPv6 address ranges in addition to your existing IPv4 address ranges\. For an example of a bucket policy that allows access over both IPv6 and IPv4, see [Restricting Access to Specific IP Addresses](example-bucket-policies.md#example-bucket-policies-use-case-3)\.

You can review your IAM user policies using the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\. For more information about IAM, see the [IAM User Guide](http://docs.aws.amazon.com/IAM/latest/UserGuide/)\. For information about editing S3 bucket policies, see [How Do I Add an S3 Bucket Policy?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/add-bucket-policy.html) in the *Amazon Simple Storage Service Console User Guide*\. 

## Testing IP Address Compatibility<a name="ipv6-access-test-compatabilty"></a>

If you are using use Linux/Unix or Mac OS X, you can test whether you can access a dual\-stack endpoint over IPv6 by using the `curl` command as shown in the following example:

**Example**  

```
curl -v  http://s3.dualstack.us-west-2.amazonaws.com/
```
You get back information similar to the following example\. If you are connected over IPv6 the connected IP address will be an IPv6 address\.   

```
* About to connect() to s3-us-west-2.amazonaws.com port 80 (#0)
*   Trying IPv6 address... connected
* Connected to s3.dualstack.us-west-2.amazonaws.com (IPv6 address) port 80 (#0)
> GET / HTTP/1.1
> User-Agent: curl/7.18.1 (x86_64-unknown-linux-gnu) libcurl/7.18.1 OpenSSL/1.0.1t zlib/1.2.3
> Host: s3.dualstack.us-west-2.amazonaws.com
```

If you are using Microsoft Windows 7 or Windows 10, you can test whether you can access a dual\-stack endpoint over IPv6 or IPv4 by using the `ping` command as shown in the following example\.

```
ping ipv6.s3.dualstack.us-west-2.amazonaws.com 
```