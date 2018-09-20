# Working with Amazon S3 Buckets<a name="UsingBucket"></a>

Amazon S3 is cloud storage for the internet\. To upload your data \(photos, videos, documents etc\.\), you first create a bucket in one of the AWS Regions\. You can then upload any number of objects to the bucket\. 

In terms of implementation, buckets and objects are resources, and Amazon S3 provides APIs for you to manage them\. For example, you can create a bucket and upload objects using the Amazon S3 API\. You can also use the Amazon S3 console to perform these operations\. The console uses the Amazon S3 APIs to send requests to Amazon S3\. 

This section explains how to work with buckets\. For information about working with objects, see [Working with Amazon S3 Objects](UsingObjects.md)\.

An Amazon S3 bucket name is globally unique, and the namespace is shared by all AWS accounts\. This means that after a bucket is created, the name of that bucket cannot be used by another AWS account in any AWS Region until the bucket is deleted\. You should not depend on specific bucket naming conventions for availability or security verification purposes\. For bucket naming guidelines, see [Bucket Restrictions and Limitations](BucketRestrictions.md)\.

Amazon S3 creates buckets in a region you specify\. To optimize latency, minimize costs, or address regulatory requirements, choose any AWS Region that is geographically close to you\. For example, if you reside in Europe, you might find it advantageous to create buckets in the EU \(Ireland\) or EU \(Frankfurt\) regions\. For a list of Amazon S3 regions, see [Regions and Endpoints](http://docs.aws.amazon.com/general/latest/gr/rande.html#s3_region) in the *AWS General Reference*\.

**Note**  
 Objects belonging to a bucket that you create in a specific AWS Region never leave that region, unless you explicitly transfer them to another region\. For example, objects stored in the EU \(Ireland\) region never leave it\. 

**Topics**
+ [Creating a Bucket](#create-bucket-intro)
+ [Accessing a Bucket](#access-bucket-intro)
+ [Bucket Configuration Options](#bucket-config-options-intro)
+ [Bucket Restrictions and Limitations](BucketRestrictions.md)
+ [Examples of Creating a Bucket](create-bucket-get-location-example.md)
+ [Deleting or Emptying a Bucket](delete-or-empty-bucket.md)
+ [Amazon S3 Default Encryption for S3 Buckets](bucket-encryption.md)
+ [Managing Bucket Website Configuration](ManagingBucketWebsiteConfig.md)
+ [Amazon S3 Transfer Acceleration](transfer-acceleration.md)
+ [Requester Pays Buckets](RequesterPaysBuckets.md)
+ [Buckets and Access Control](BucketAccess.md)
+ [Billing and Usage Reporting for S3 Buckets](BucketBilling.md)

## Creating a Bucket<a name="create-bucket-intro"></a>

Amazon S3 provides APIs for creating and managing buckets\. By default, you can create up to 100 buckets in each of your AWS accounts\. If you need more buckets, you can increase your bucket limit by submitting a service limit increase\. To learn how to submit a bucket limit increase, see [AWS Service Limits](http://docs.aws.amazon.com/general/latest/gr/aws_service_limits.html) in the *AWS General Reference*\. 

When you create a bucket, you provide a name and the AWS Region where you want to create the bucket\. For information about naming buckets, see [Rules for Bucket Naming](BucketRestrictions.md#bucketnamingrules)\.

You can store any number of objects in a bucket\.

You can create a bucket using any of the following methods:
+ With the console\.
+ Programmatically, using the AWS SDKs\.
**Note**  
If you need to, you can also make the Amazon S3 REST API calls directly from your code\. However, this can be cumbersome because it requires you to write code to authenticate your requests\. For more information, see [PUT Bucket](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUT.html) in the *Amazon Simple Storage Service API Reference*\.

  When using the AWS SDKs, you first create a client and then use the client to send a request to create a bucket\.  When you create the client, you can specify an AWS Region\. US East \(N\. Virginia\) is the default Region\. Note the following: 
  + If you create a client by specifying the US East \(N\. Virginia\) Region, the client uses the following endpoint to communicate with Amazon S3: 

    ```
    s3.amazonaws.com
    ```

     You can use this client to create a bucket in any AWS Region\. In your create bucket request:
    + If you don’t specify a Region, Amazon S3 creates the bucket in the US East \(N\. Virginia\) Region\.
    + If you specify an AWS Region, Amazon S3 creates the bucket in the specified Region\. 
  +  If you create a client by specifying any other AWS Region, each of these Regions maps to the Region\-specific endpoint: 

    ```
    s3-<region>.amazonaws.com
    ```

    For example, if you create a client by specifying the eu\-west\-1 Region, it maps to the following region\-specific endpoint: 

    ```
    s3-eu-west-1.amazonaws.com
    ```

    In this case, you can use the client to create a bucket only in the eu\-west\-1 Region\. Amazon S3 returns an error if you specify any other Region in your request to create a bucket\.
  +  If you create a client to access a dual\-stack endpoint, you must specify an AWS Region\. For more information, see [Dual\-Stack Endpoints](dual-stack-endpoints.md#dual-stack-endpoints-description)\.

  For a list of available AWS Regions, see [Regions and Endpoints](http://docs.aws.amazon.com/general/latest/gr/rande.html#s3_region) in the *AWS General Reference*\.

For examples, see [Examples of Creating a Bucket](create-bucket-get-location-example.md)\.

### About Permissions<a name="about-access-permissions-create-bucket"></a>

You can use your AWS account root credentials to create a bucket and perform any other Amazon S3 operation\. However, AWS recommends not using the root credentials of your AWS account to make requests such as to create a bucket\. Instead, create an IAM user, and grant that user full access \(users by default have no permissions\)\. We refer to these users as administrator users\. You can use the administrator user credentials, instead of the root credentials of your account, to interact with AWS and perform tasks, such as create a bucket, create users, and grant them permissions\. 

For more information, see [Root Account Credentials vs\. IAM User Credentials](http://docs.aws.amazon.com/general/latest/gr/root-vs-iam.html) in the *AWS General Reference* and [IAM Best Practices](http://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html) in the *IAM User Guide*\.

The AWS account that creates a resource owns that resource\. For example, if you create an IAM user in your AWS account and grant the user permission to create a bucket, the user can create a bucket\. But the user does not own the bucket; the AWS account to which the user belongs owns the bucket\. The user will need additional permission from the resource owner to perform any other bucket operations\. For more information about managing permissions for your Amazon S3 resources, see [Managing Access Permissions to Your Amazon S3 Resources](s3-access-control.md)\.

## Accessing a Bucket<a name="access-bucket-intro"></a>

You can access your bucket using the Amazon S3 console\. Using the console UI, you can perform almost all bucket operations without having to write any code\. 

If you access a bucket programmatically, note that Amazon S3 supports RESTful architecture in which your buckets and objects are resources, each with a resource URI that uniquely identifies the resource\. 

Amazon S3 supports both virtual\-hosted–style and path\-style URLs to access a bucket\. 
+ In a virtual\-hosted–style URL, the bucket name is part of the domain name in the URL\. For example:  
  + `http://bucket.s3.amazonaws.com`
  + `http://bucket.s3-aws-region.amazonaws.com`\.

  In a virtual\-hosted–style URL, you can use either of these endpoints\. If you make a request to the `http://bucket.s3.amazonaws.com` endpoint, the DNS has sufficient information to route your request directly to the Region where your bucket resides\. 

  For more information, see [Virtual Hosting of Buckets](VirtualHosting.md)\.

   
+  In a path\-style URL, the bucket name is not part of the domain \(unless you use a Region\-specific endpoint\)\. For example:
  + US East \(N\. Virginia\) Region endpoint, `http://s3.amazonaws.com/bucket `
  + Region\-specific endpoint, `http://s3-aws-region.amazonaws.com/bucket`

   In a path\-style URL, the endpoint you use must match the Region in which the bucket resides\. For example, if your bucket is in the South America \(São Paulo\) Region, you must use the `http://s3-sa-east-1.amazonaws.com/bucket` endpoint\. If your bucket is in the US East \(N\. Virginia\) Region, you must use the `http://s3.amazonaws.com/bucket` endpoint\.

**Important**  
Because buckets can be accessed using path\-style and virtual\-hosted–style URLs, we recommend you create buckets with DNS\-compliant bucket names\. For more information, see [Bucket Restrictions and Limitations](BucketRestrictions.md)\.

**Accessing an S3 Bucket over IPv6**  
Amazon S3 has a set of dual\-stack endpoints, which support requests to S3 buckets over both Internet Protocol version 6 \(IPv6\) and IPv4\. For more information, see [Making Requests over IPv6](ipv6-access.md)\.

## Bucket Configuration Options<a name="bucket-config-options-intro"></a>

Amazon S3 supports various options for you to configure your bucket\. For example, you can configure your bucket for website hosting, add configuration to manage lifecycle of objects in the bucket, and configure the bucket to log all access to the bucket\. Amazon S3 supports subresources for you to store, and manage the bucket configuration information\. That is, using the Amazon S3 API, you can create and manage these subresources\. You can also use the console or the AWS SDKs\. 

**Note**  
There are also object\-level configurations\. For example, you can configure object\-level permissions by configuring an access control list \(ACL\) specific to that object\.

These are referred to as subresources because they exist in the context of a specific bucket or object\. The following table lists subresources that enable you to manage bucket\-specific configurations\. 


| Subresource | Description | 
| --- | --- | 
|   *location*   |   When you create a bucket, you specify the AWS Region where you want Amazon S3 to create the bucket\. Amazon S3 stores this information in the location subresource and provides an API for you to retrieve this information\.   | 
|   *policy* and *ACL* \(access control list\)   |  All your resources \(such as buckets and objects\) are private by default\. Amazon S3 supports both bucket policy and access control list \(ACL\) options for you to grant and manage bucket\-level permissions\. Amazon S3 stores the permission information in the *policy* and *acl* subresources\. For more information, see [Managing Access Permissions to Your Amazon S3 Resources](s3-access-control.md)\.  | 
|   *cors* \(cross\-origin resource sharing\)   |   You can configure your bucket to allow cross\-origin requests\. For more information, see [Enabling Cross\-Origin Resource Sharing](http://docs.aws.amazon.com/AmazonS3/latest/dev/cors.html)\.  | 
|  website |   You can configure your bucket for static website hosting\. Amazon S3 stores this configuration by creating a *website* subresource\. For more information, see [Hosting a Static Website on Amazon S3](http://docs.aws.amazon.com/AmazonS3/latest/dev/WebsiteHosting.html)\.   | 
|   *logging*   |  Logging enables you to track requests for access to your bucket\. Each access log record provides details about a single access request, such as the requester, bucket name, request time, request action, response status, and error code, if any\. Access log information can be useful in security and access audits\. It can also help you learn about your customer base and understand your Amazon S3 bill\.   For more information, see [Amazon S3 Server Access Logging](ServerLogs.md)\.   | 
|   *event notification*   |  You can enable your bucket to send you notifications of specified bucket events\.  For more information, see [ Configuring Amazon S3 Event Notifications](NotificationHowTo.md)\.  | 
| versioning |  Versioning helps you recover accidental overwrites and deletes\.  We recommend versioning as a best practice to recover objects from being deleted or overwritten by mistake\.  For more information, see [Using Versioning](Versioning.md)\.  | 
| lifecycle |  You can define lifecycle rules for objects in your bucket that have a well\-defined lifecycle\. For example, you can define a rule to archive objects one year after creation, or delete an object 10 years after creation\.  For more information, see [Object Lifecycle Management](http://docs.aws.amazon.com/AmazonS3/latest/dev/object-lifecycle-mgmt.html)\.   | 
| cross\-region replication |  Cross\-region replication is the automatic, asynchronous copying of objects across buckets in different AWS Regions\. For more information, see [Cross\-Region Replication ](crr.md)\.  | 
|   *tagging*   |  You can add cost allocation tags to your bucket to categorize and track your AWS costs\. Amazon S3 provides the *tagging* subresource to store and manage tags on a bucket\. Using tags you apply to your bucket, AWS generates a cost allocation report with usage and costs aggregated by your tags\.  For more information, see [Billing and Usage Reporting for S3 Buckets](BucketBilling.md)\.   | 
|   *requestPayment*   |  By default, the AWS account that creates the bucket \(the bucket owner\) pays for downloads from the bucket\. Using this subresource, the bucket owner can specify that the person requesting the download will be charged for the download\. Amazon S3 provides an API for you to manage this subresource\. For more information, see [Requester Pays Buckets](RequesterPaysBuckets.md)\.  | 
|   *transfer acceleration*   |  Transfer Acceleration enables fast, easy, and secure transfers of files over long distances between your client and an S3 bucket\. Transfer Acceleration takes advantage of Amazon CloudFront’s globally distributed edge locations\.  For more information, see [Amazon S3 Transfer Acceleration](transfer-acceleration.md)\.  | 