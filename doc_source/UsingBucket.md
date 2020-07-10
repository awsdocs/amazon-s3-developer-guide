# Working with Amazon S3 Buckets<a name="UsingBucket"></a>

To upload your data \(photos, videos, documents etc\.\) to Amazon S3, you must first create an S3 bucket in one of the AWS Regions\. You can then upload any number of objects to the bucket\. 

In terms of implementation, buckets and objects are resources, and Amazon S3 provides APIs for you to manage them\. For example, you can create a bucket and upload objects using the Amazon S3 API\. You can also use the Amazon S3 console to perform these operations\. The console uses the Amazon S3 APIs to send requests to Amazon S3\. 

This section explains how to work with buckets\. For information about working with objects, see [Working with Amazon S3 Objects](UsingObjects.md)\.

An Amazon S3 bucket name is globally unique, and the namespace is shared by all AWS accounts\. This means that after a bucket is created, the name of that bucket cannot be used by another AWS account in any AWS Region until the bucket is deleted\. You should not depend on specific bucket naming conventions for availability or security verification purposes\. For bucket naming guidelines, see [Bucket restrictions and limitations](BucketRestrictions.md)\.

Amazon S3 creates buckets in a Region you specify\. To optimize latency, minimize costs, or address regulatory requirements, choose any AWS Region that is geographically close to you\. For example, if you reside in Europe, you might find it advantageous to create buckets in the Europe \(Ireland\) or Europe \(Frankfurt\) Regions\. For a list of Amazon S3 Regions, see [Regions and Endpoints](https://docs.aws.amazon.com/general/latest/gr/s3.html) in the *AWS General Reference*\.

**Note**  
Objects that belong to a bucket that you create in a specific AWS Region never leave that Region, unless you explicitly transfer them to another Region\. For example, objects that are stored in the Europe \(Ireland\) Region never leave it\. 

**Topics**
+ [Creating a bucket](#create-bucket-intro)
+ [Managing public access to buckets](#block-public-access-intro)
+ [Accessing a bucket](#access-bucket-intro)
+ [Bucket configuration options](#bucket-config-options-intro)
+ [Bucket restrictions and limitations](BucketRestrictions.md)
+ [Examples of creating a bucket](create-bucket-get-location-example.md)
+ [Deleting or emptying a bucket](delete-or-empty-bucket.md)
+ [Amazon S3 default encryption for S3 buckets](bucket-encryption.md)
+ [Amazon S3 Transfer Acceleration](transfer-acceleration.md)
+ [Requester Pays buckets](RequesterPaysBuckets.md)
+ [Buckets and access control](BucketAccess.md)
+ [Billing and usage reporting for S3 buckets](BucketBilling.md)

## Creating a bucket<a name="create-bucket-intro"></a>

Amazon S3 provides APIs for creating and managing buckets\. By default, you can create up to 100 buckets in each of your AWS accounts\. If you need more buckets, you can increase your account bucket limit to a maximum of 1,000 buckets by submitting a service limit increase\. To learn how to submit a bucket limit increase, see [AWS Service Limits](https://docs.aws.amazon.com/general/latest/gr/aws_service_limits.html) in the *AWS General Reference*\. You can store any number of objects in a bucket\. 

When you create a bucket, you provide a name and the AWS Region where you want to create the bucket\. For information about naming buckets, see [Rules for bucket naming](BucketRestrictions.md#bucketnamingrules)\.

You can use any of the methods listed below to create a bucket\. For examples, see [Examples of creating a bucket](create-bucket-get-location-example.md)\.

### Amazon S3 console<a name="create-bucket-s3-console"></a>

You can create a bucket in the Amazon S3 console\. For more information, see [Creating a bucket](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-bucket.html) in the *Amazon Simple Storage Service Console User Guide*

### REST API<a name="create-bucket-rest-api"></a>

Creating a bucket using the REST API can be cumbersome because it requires you to write code to authenticate your requests\. For more information, see [PUT Bucket](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUT.html) in the *Amazon Simple Storage Service API Reference*\. We recommend that you use the AWS Management Console or AWS SDKs instead\. 

### AWS SDK<a name="create-bucket-aws-sdk"></a>

When you use the AWS SDKs to create a bucket, you first create a client and then use the client to send a request to create a bucket\. If you don't specify a Region when you create a client or a bucket, Amazon S3 uses US East \(N\. Virginia\), the default Region\. You can also specify a specific Region\. For a list of available AWS Regions, see [Regions and Endpoints](https://docs.aws.amazon.com/general/latest/gr/s3.html) in the *AWS General Reference*\. For more information about enabling or disabling an AWS Region, see [Managing AWS Regions](https://docs.aws.amazon.com/general/latest/gr/rande-manage.html) in the *AWS General Reference*\. 

As a best practice, you should create your client and bucket in the same Region\. If your Region launched *after March 20, 2019*, your client and bucket must be in the same Region\. However, you can use a client in the US East \(N\. Virginia\) Region to create a bucket in any Region that launched *before March 20, 2019*\. For more information, see [Legacy Endpoints](VirtualHosting.md#s3-legacy-endpoints)\.

**Creating a client**  
When you create the client, you should specify an AWS Region, to create the client in\. If you don’t specify a Region, Amazon S3 creates the client in US East \(N\. Virginia\) by default Region\. To create a client to access a dual\-stack endpoint, you must specify an AWS Region,\. For more information, see [Dual\-stack endpoints](dual-stack-endpoints.md#dual-stack-endpoints-description)\.

When you create a client, the Region maps to the Region\-specific endpoint\. The client uses this endpoint to communicate with Amazon S3:

```
s3.<region>.amazonaws.com
```

For example, if you create a client by specifying the eu\-west\-1 Region, it maps to the following Region\-specific endpoint: 

```
s3.eu-west-1.amazonaws.com
```

**Creating a bucket**  
If you don't specify a Region when you create a bucket, Amazon S3 creates the bucket in the US East \(N\. Virginia\) Region\. Therefore, if you want to create a bucket in a specific Region, you must specify the Region when you create the bucket\.

Buckets created after September 30, 2020, will support only virtual hosted\-style requests\. Path\-style requests will continue to be supported for buckets created on or before this date\. For more information, see [ Amazon S3 Path Deprecation Plan – The Rest of the Story](https://aws.amazon.com/blogs/aws/amazon-s3-path-deprecation-plan-the-rest-of-the-story/)\.

### About permissions<a name="about-access-permissions-create-bucket"></a>

You can use your AWS account root credentials to create a bucket and perform any other Amazon S3 operation\. However, AWS recommends not using the root credentials of your AWS account to make requests such as to create a bucket\. Instead, create an IAM user, and grant that user full access \(users by default have no permissions\)\. We refer to these users as administrator users\. You can use the administrator user credentials, instead of the root credentials of your account, to interact with AWS and perform tasks, such as create a bucket, create users, and grant them permissions\. 

For more information, see [Root Account Credentials vs\. IAM User Credentials](https://docs.aws.amazon.com/general/latest/gr/root-vs-iam.html) in the *AWS General Reference* and [IAM Best Practices](https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html) in the *IAM User Guide*\.

The AWS account that creates a resource owns that resource\. For example, if you create an IAM user in your AWS account and grant the user permission to create a bucket, the user can create a bucket\. But the user does not own the bucket; the AWS account to which the user belongs owns the bucket\. The user will need additional permission from the resource owner to perform any other bucket operations\. For more information about managing permissions for your Amazon S3 resources, see [Identity and access management in Amazon S3](s3-access-control.md)\.

## Managing public access to buckets<a name="block-public-access-intro"></a>

Public access is granted to buckets and objects through access control lists \(ACLs\), bucket policies, or both\. To help you manage public access to Amazon S3 resources, Amazon S3 provides *block public access* settings\. Amazon S3 block public access settings can override ACLs and bucket policies so that you can enforce uniform limits on public access to these resources\. You can apply block public access settings to individual buckets or to all buckets in your account\.

To help ensure that all of your Amazon S3 buckets and objects have their public access blocked, we recommend that you turn on all four settings for block public access for your account\. These settings block public access for all current and future buckets\.

Before applying these settings, verify that your applications will work correctly without public access\. If you require some level of public access to your buckets or objects, for example to host a static website as described at [Hosting a static website on Amazon S3](WebsiteHosting.md), you can customize the individual settings to suit your storage use cases\. For more information, see [Using Amazon S3 block public access](access-control-block-public-access.md)\.

## Accessing a bucket<a name="access-bucket-intro"></a>

You can access your bucket using the Amazon S3 console\. Using the console UI, you can perform almost all bucket operations without having to write any code\. 

If you access a bucket programmatically, note that Amazon S3 supports RESTful architecture in which your buckets and objects are resources, each with a resource URI that uniquely identifies the resource\. 

Amazon S3 supports both virtual\-hosted–style and path\-style URLs to access a bucket\. Because buckets can be accessed using path\-style and virtual\-hosted–style URLs, we recommend that you create buckets with DNS\-compliant bucket names\. For more information, see [Bucket restrictions and limitations](BucketRestrictions.md)\.

**Note**  
Virtual hosted style and path\-style requests use the S3 dot Region endpoint structure \(`s3.Region`\), for example, `https://my-bucket.s3.us-west-2.amazonaws.com`\. However, some older Amazon S3 Regions also support S3 dash Region endpoints `s3-Region`, for example, `https://my-bucket.s3-us-west-2.amazonaws.com`\. If your bucket is in one of these Regions, you might see `s3-Region` endpoints in your server access logs or CloudTrail logs\. We recommend that you do not use this endpoint structure in your requests\. 

### Virtual hosted style access<a name="virtual-host-style-url-ex"></a>

In a virtual\-hosted–style request, the bucket name is part of the domain name in the URL\.

Amazon S3 virtual hosted style URLs follow the format shown below\.

```
https://bucket-name.s3.Region.amazonaws.com/key name
```

In this example, `my-bucket` is the bucket name, US West \(Oregon\) is the Region, and `puppy.png` is the key name:

```
https://my-bucket.s3.us-west-2.amazonaws.com/puppy.png
```

For more information about virtual hosted style access, see [Virtual Hosted\-Style Requests](VirtualHosting.md#virtual-hosted-style-access)\.

### Path\-style access<a name="path-style-url-ex"></a>

In Amazon S3, path\-style URLs follow the format shown below\.

```
https://s3.Region.amazonaws.com/bucket-name/key name
```

For example, if you create a bucket named `mybucket` in the US West \(Oregon\) Region, and you want to access the `puppy.jpg` object in that bucket, you can use the following path\-style URL:

```
https://s3.us-west-2.amazonaws.com/mybucket/puppy.jpg
```

 For more information, see [Path\-Style Requests](VirtualHosting.md#path-style-access)\.

**Important**  
Buckets created after September 30, 2020, will support only virtual hosted\-style requests\. Path\-style requests will continue to be supported for buckets created on or before this date\. For more information, see [ Amazon S3 Path Deprecation Plan – The Rest of the Story](https://aws.amazon.com/blogs/aws/amazon-s3-path-deprecation-plan-the-rest-of-the-story/)\.

### Accessing an S3 bucket over IPv6<a name="accessing-bucket-s3-ipv6"></a>

Amazon S3 has a set of dual\-stack endpoints, which support requests to S3 buckets over both Internet Protocol version 6 \(IPv6\) and IPv4\. For more information, see [Making requests over IPv6](ipv6-access.md)\.

### Accessing a bucket through an S3 access point<a name="accessing-bucket-through-s3-access-point"></a>

In addition to accessing a bucket directly, you can access a bucket through an S3 access point\. For more information about S3 access points, see [Managing data access with Amazon S3 access points ](access-points.md)\.

S3 access points only support virtual\-host\-style addressing\. To address a bucket through an access point, use this format:

```
https://AccessPointName-AccountId.s3-accesspoint.region.amazonaws.com.
```

**Note**  
If your access point name includes dash \(\-\) characters, include the dashes in the URL and insert another dash before the account ID\. For example, to use an access point named `finance-docs` owned by account `123456789012` in Region `us-west-2`, the appropriate URL would be `https://finance-docs-123456789012.s3-accesspoint.us-west-2.amazonaws.com`\.
S3 access points don't support access by HTTP, only secure access by HTTPS\.

### Accessing a Bucket using S3://<a name="accessing-a-bucket-using-S3-format"></a>

Some AWS services require specifying an Amazon S3 bucket using `S3://bucket`\. The correct format is shown below\. Be aware that when using this format, the bucket name does not include the region\.

```
s3://bucket-name/key-name
```

For example, using the sample bucket described in the earlier path\-style section:

```
s3://mybucket/puppy.jpg
```

## Bucket configuration options<a name="bucket-config-options-intro"></a>

Amazon S3 supports various options for you to configure your bucket\. For example, you can configure your bucket for website hosting, add configuration to manage lifecycle of objects in the bucket, and configure the bucket to log all access to the bucket\. Amazon S3 supports subresources for you to store and manage the bucket configuration information\. You can use the Amazon S3 API to create and manage these subresources\. However, you can also use the console or the AWS SDKs\. 

**Note**  
There are also object\-level configurations\. For example, you can configure object\-level permissions by configuring an access control list \(ACL\) specific to that object\.

These are referred to as subresources because they exist in the context of a specific bucket or object\. The following table lists subresources that enable you to manage bucket\-specific configurations\. 


| Subresource | Description | 
| --- | --- | 
|   *cors* \(cross\-origin resource sharing\)   |   You can configure your bucket to allow cross\-origin requests\. For more information, see [Enabling Cross\-Origin Resource Sharing](https://docs.aws.amazon.com/AmazonS3/latest/dev/cors.html)\.  | 
|   *event notification*   |  You can enable your bucket to send you notifications of specified bucket events\.  For more information, see [ Configuring Amazon S3 event notifications](NotificationHowTo.md)\.  | 
| lifecycle |  You can define lifecycle rules for objects in your bucket that have a well\-defined lifecycle\. For example, you can define a rule to archive objects one year after creation, or delete an object 10 years after creation\.  For more information, see [Object Lifecycle Management](https://docs.aws.amazon.com/AmazonS3/latest/dev/object-lifecycle-mgmt.html)\.   | 
|   *location*   |   When you create a bucket, you specify the AWS Region where you want Amazon S3 to create the bucket\. Amazon S3 stores this information in the location subresource and provides an API for you to retrieve this information\.   | 
|   *logging*   |  Logging enables you to track requests for access to your bucket\. Each access log record provides details about a single access request, such as the requester, bucket name, request time, request action, response status, and error code, if any\. Access log information can be useful in security and access audits\. It can also help you learn about your customer base and understand your Amazon S3 bill\.   For more information, see [Amazon S3 server access logging](ServerLogs.md)\.   | 
|   *object locking*   |  To use S3 Object Lock, you must enable it for a bucket\. You can also optionally configure a default retention mode and period that applies to new objects that are placed in the bucket\.  For more information, see [Bucket configuration](object-lock-overview.md#object-lock-bucket-config)\.   | 
|   *policy* and *ACL* \(access control list\)   |  All your resources \(such as buckets and objects\) are private by default\. Amazon S3 supports both bucket policy and access control list \(ACL\) options for you to grant and manage bucket\-level permissions\. Amazon S3 stores the permission information in the *policy* and *acl* subresources\. For more information, see [Identity and access management in Amazon S3](s3-access-control.md)\.  | 
|   *replication*   |  Replication is the automatic, asynchronous copying of objects across buckets in different or the same AWS Regions\. For more information, see [Replication](replication.md)\.  | 
|   *requestPayment*   |  By default, the AWS account that creates the bucket \(the bucket owner\) pays for downloads from the bucket\. Using this subresource, the bucket owner can specify that the person requesting the download will be charged for the download\. Amazon S3 provides an API for you to manage this subresource\. For more information, see [Requester Pays buckets](RequesterPaysBuckets.md)\.  | 
|   *tagging*   |  You can add cost allocation tags to your bucket to categorize and track your AWS costs\. Amazon S3 provides the *tagging* subresource to store and manage tags on a bucket\. Using tags you apply to your bucket, AWS generates a cost allocation report with usage and costs aggregated by your tags\.  For more information, see [Billing and usage reporting for S3 buckets](BucketBilling.md)\.   | 
|   *transfer acceleration*   |  Transfer Acceleration enables fast, easy, and secure transfers of files over long distances between your client and an S3 bucket\. Transfer Acceleration takes advantage of Amazon CloudFront’s globally distributed edge locations\.  For more information, see [Amazon S3 Transfer Acceleration](transfer-acceleration.md)\.  | 
| versioning |  Versioning helps you recover accidental overwrites and deletes\.  We recommend versioning as a best practice to recover objects from being deleted or overwritten by mistake\.  For more information, see [Using versioning](Versioning.md)\.  | 
|  website |   You can configure your bucket for static website hosting\. Amazon S3 stores this configuration by creating a *website* subresource\. For more information, see [Hosting a Static Website on Amazon S3](https://docs.aws.amazon.com/AmazonS3/latest/dev/WebsiteHosting.html)\.   | 