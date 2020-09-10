# Introduction to Amazon S3<a name="Introduction"></a>

This introduction to Amazon Simple Storage Service \(Amazon S3\) provides a detailed summary of this web service\. After reading this section, you should have a good idea of what it offers and how it can fit in with your business\.

**Topics**
+ [Overview of Amazon S3 and this guide](#overview)
+ [Advantages of using Amazon S3](#features)
+ [Amazon S3 concepts](#CoreConcepts)
+ [Amazon S3 features](#S3Features)
+ [Amazon S3 application programming interfaces \(API\)](#API)
+ [Paying for Amazon S3](#PayingforStorage)
+ [Related services](#RelatedAmazonWebServices)

## Overview of Amazon S3 and this guide<a name="overview"></a>

Amazon S3 has a simple web services interface that you can use to store and retrieve any amount of data, at any time, from anywhere on the web\.

This guide describes how you send requests to create buckets, store and retrieve your objects, and manage permissions on your resources\. The guide also describes access control and the authentication process\. Access control defines who can access objects and buckets within Amazon S3, and the type of access \(for example, READ and WRITE\)\. The authentication process verifies the identity of a user who is trying to access Amazon Web Services \(AWS\)\.

## Advantages of using Amazon S3<a name="features"></a>

Amazon S3 is intentionally built with a minimal feature set that focuses on simplicity and robustness\. Following are some of the advantages of using Amazon S3:
+ **Creating buckets** – Create and name a bucket that stores data\. Buckets are the fundamental containers in Amazon S3 for data storage\.
+ **Storing data** – Store an infinite amount of data in a bucket\. Upload as many objects as you like into an Amazon S3 bucket\. Each object can contain up to 5 TB of data\. Each object is stored and retrieved using a unique developer\-assigned key\.
+ **Downloading data** – Download your data or enable others to do so\. Download your data anytime you like, or allow others to do the same\.
+ **Permissions** – Grant or deny access to others who want to upload or download data into your Amazon S3 bucket\. Grant upload and download permissions to three types of users\. Authentication mechanisms can help keep data secure from unauthorized access\.
+ **Standard interfaces** – Use standards\-based REST and SOAP interfaces designed to work with any internet\-development toolkit\.
**Note**  
 SOAP support over HTTP is deprecated, but it is still available over HTTPS\. New Amazon S3 features will not be supported for SOAP\. We recommend that you use either the REST API or the AWS SDKs\. 

## Amazon S3 concepts<a name="CoreConcepts"></a>

This section describes key concepts and terminology you need to understand to use Amazon S3 effectively\. They are presented in the order that you will most likely encounter them\.

**Topics**
+ [Buckets](#BasicsBucket)
+ [Objects](#BasicsObjects)
+ [Keys](#BasicsKeys)
+ [Regions](#Regions)
+ [Amazon S3 data consistency model](#ConsistencyModel)

### Buckets<a name="BasicsBucket"></a>

 A bucket is a container for objects stored in Amazon S3\. Every object is contained in a bucket\. For example, if the object named `photos/puppy.jpg` is stored in the `awsexamplebucket1` bucket in the US West \(Oregon\) Region, then it is addressable using the URL `https://awsexamplebucket1.s3.us-west-2.amazonaws.com/photos/puppy.jpg`\.

 Buckets serve several purposes: 
+ They organize the Amazon S3 namespace at the highest level\.
+ They identify the account responsible for storage and data transfer charges\.
+ They play a role in access control\.
+ They serve as the unit of aggregation for usage reporting\.

You can configure buckets so that they are created in a specific AWS Region\. For more information, see [Accessing a Bucket](UsingBucket.md#access-bucket-intro)\. You can also configure a bucket so that every time an object is added to it, Amazon S3 generates a unique version ID and assigns it to the object\. For more information, see [Using Versioning](Versioning.md)\.

 For more information about buckets, see [Working with Amazon S3 Buckets](UsingBucket.md)\. 

### Objects<a name="BasicsObjects"></a>

Objects are the fundamental entities stored in Amazon S3\. Objects consist of object data and metadata\. The data portion is opaque to Amazon S3\. The metadata is a set of name\-value pairs that describe the object\. These include some default metadata, such as the date last modified, and standard HTTP metadata, such as `Content-Type`\. You can also specify custom metadata at the time the object is stored\.

An object is uniquely identified within a bucket by a key \(name\) and a version ID\. For more information, see [Keys](#BasicsKeys) and [Using Versioning](Versioning.md)\.

### Keys<a name="BasicsKeys"></a>

A key is the unique identifier for an object within a bucket\. Every object in a bucket has exactly one key\. The combination of a bucket, key, and version ID uniquely identify each object\. So you can think of Amazon S3 as a basic data map between "bucket \+ key \+ version" and the object itself\. Every object in Amazon S3 can be uniquely addressed through the combination of the web service endpoint, bucket name, key, and optionally, a version\. For example, in the URL `https://doc.s3.amazonaws.com/2006-03-01/AmazonS3.wsdl`, "`doc`" is the name of the bucket and "`2006-03-01/AmazonS3.wsdl`" is the key\.

 For more information about object keys, see [Object Keys](https://docs.aws.amazon.com/AmazonS3/latest/dev/UsingMetadata.html#object-keys)\. 

### Regions<a name="Regions"></a>

You can choose the geographical AWS Region where Amazon S3 will store the buckets that you create\. You might choose a Region to optimize latency, minimize costs, or address regulatory requirements\. Objects stored in a Region never leave the Region unless you explicitly transfer them to another Region\. For example, objects stored in the Europe \(Ireland\) Region never leave it\. 

**Note**  
You can only access Amazon S3 and its features in AWS Regions that are enabled for your account\.

 For a list of Amazon S3 Regions and endpoints, see [Regions and Endpoints](https://docs.aws.amazon.com/general/latest/gr/rande.html#s3_region) in the *AWS General Reference*\. 

### Amazon S3 data consistency model<a name="ConsistencyModel"></a>

Amazon S3 provides read\-after\-write consistency for PUTS of new objects in your S3 bucket in all Regions with one caveat\. The caveat is that if you make a HEAD or GET request to a key name before the object is created, then create the object shortly after that, a subsequent GET might not return the object due to eventual consistency\. 

Amazon S3 offers eventual consistency for overwrite PUTS and DELETES in all Regions\. 

 Updates to a single key are atomic\. For example, if you PUT to an existing key, a subsequent read might return the old data or the updated data, but it never returns corrupted or partial data\. 

Amazon S3 achieves high availability by replicating data across multiple servers within AWS data centers\. If a PUT request is successful, your data is safely stored\. However, information about the changes must replicate across Amazon S3, which can take some time, and so you might observe the following behaviors:
+  A process writes a new object to Amazon S3 and immediately lists keys within its bucket\. Until the change is fully propagated, the object might not appear in the list\. 
+  A process replaces an existing object and immediately tries to read it\. Until the change is fully propagated, Amazon S3 might return the previous data\. 
+  A process deletes an existing object and immediately tries to read it\. Until the deletion is fully propagated, Amazon S3 might return the deleted data\. 
+  A process deletes an existing object and immediately lists keys within its bucket\. Until the deletion is fully propagated, Amazon S3 might list the deleted object\. 

**Note**  
Amazon S3 does not currently support object locking for concurrent updates\. If two PUT requests are simultaneously made to the same key, the request with the latest timestamp wins\. If this is an issue, you will need to build an object\-locking mechanism into your application\.   
Object locking is different from the S3 Object Lock feature\. With S3 Object Lock, you can store objects using a write\-once\-read\-many \(WORM\) model and prevent an object from being deleted or overwritten for a fixed amount of time or indefinitely\. For more information, see [Locking objects using S3 Object Lock](object-lock.md)\. 
Updates are key\-based\. There is no way to make atomic updates across keys\. For example, you cannot make the update of one key dependent on the update of another key unless you design this functionality into your application\.

Bucket configurations have a similar eventual consistency model, with the same caveats\. For example, if you delete a bucket and immediately list all buckets, the deleted bucket might still appear in the list\.

The following table describes the characteristics of an *eventually consistent read* and a *consistent read*\.


| Eventually consistent read | Consistent read | 
| --- | --- | 
| Stale reads possible | No stale reads | 
| Lowest read latency | Potential higher read latency | 
| Highest read throughput | Potential lower read throughput | 

#### Concurrent applications<a name="ApplicationConcurrency"></a>

This section provides examples of eventually consistent and consistent read requests when multiple clients are writing to the same items\.

In this example, both W1 \(write 1\) and W2 \(write 2\) complete before the start of R1 \(read 1\) and R2 \(read 2\)\. For a consistent read, R1 and R2 both return `color = ruby`\. For an eventually consistent read, R1 and R2 might return `color = red` or `color = ruby` depending on the amount of time that has elapsed\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/consistency1.png)

In the next example, W2 does not complete before the start of R1\. Therefore, R1 might return `color = ruby` or `color = garnet` for either a consistent read or an eventually consistent read\. Also, depending on the amount of time that has elapsed, an eventually consistent read might return no results\.

For a consistent read, R2 returns `color = garnet`\. For an eventually consistent read, R2 might return `color = ruby` or `color = garnet` depending on the amount of time that has elapsed\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/consistency2.png)

In the last example, Client 2 performs W2 before Amazon S3 returns a success for W1, so the outcome of the final value is unknown \(`color = garnet` or `color = brick`\)\. Any subsequent reads \(consistent read or eventually consistent\) might return either value\. Also, depending on the amount of time that has elapsed, an eventually consistent read might return no results\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/consistency3.png)

## Amazon S3 features<a name="S3Features"></a>

This section describes important Amazon S3 features\.

**Topics**
+ [Storage classes](#RRS)
+ [Bucket policies](#BucketPolicies)
+ [AWS identity and access management](#AWSIdentityandAccessManagement)
+ [Access control lists](#S3_ACLs)
+ [Versioning](#Versions)
+ [Operations](#BasicsOperations)

### Storage classes<a name="RRS"></a>

 Amazon S3 offers a range of storage classes designed for different use cases\. These include Amazon S3 STANDARD for general\-purpose storage of frequently accessed data, Amazon S3 STANDARD\_IA for long\-lived, but less frequently accessed data, and S3 Glacier for long\-term archive\.

For more information, see [Amazon S3 storage classes](storage-class-intro.md)\.

### Bucket policies<a name="BucketPolicies"></a>

Bucket policies provide centralized access control to buckets and objects based on a variety of conditions, including Amazon S3 operations, requesters, resources, and aspects of the request \(for example, IP address\)\. The policies are expressed in the *access policy language* and enable centralized management of permissions\. The permissions attached to a bucket apply to all of the objects in that bucket\. 

Both individuals and companies can use bucket policies\. When companies register with Amazon S3, they create an *account*\. Thereafter, the company becomes synonymous with the account\. Accounts are financially responsible for the AWS resources that they \(and their employees\) create\. Accounts have the power to grant bucket policy permissions and assign employees permissions based on a variety of conditions\. For example, an account could create a policy that gives a user write access:
+ To a particular S3 bucket
+ From an account's corporate network
+ During business hours

An account can grant one user limited read and write access, but allow another to create and delete buckets also\. An account could allow several field offices to store their daily reports in a single bucket\. It could allow each office to write only to a certain set of names \(for example, "Nevada/\*" or "Utah/\*"\) and only from the office's IP address range\.

Unlike access control lists \(described later\), which can add \(grant\) permissions only on individual objects, policies can either add or deny permissions across all \(or a subset\) of objects within a bucket\. With one request, an account can set the permissions of any number of objects in a bucket\. An account can use wildcards \(similar to regular expression operators\) on Amazon Resource Names \(ARNs\) and other values\. The account could then control access to groups of objects that begin with a common [prefix](https://docs.aws.amazon.com/general/latest/gr/glos-chap.html#keyprefix) or end with a given extension, such as *\.html*\.

Only the bucket owner is allowed to associate a policy with a bucket\. Policies \(written in the access policy language\) *allow* or *deny* requests based on the following:
+ Amazon S3 bucket operations \(such as `PUT ?acl`\), and object operations \(such as `PUT Object`, or `GET Object`\)
+ Requester
+ Conditions specified in the policy

An account can control access based on specific Amazon S3 operations, such as `GetObject`, `GetObjectVersion`, `DeleteObject`, or `DeleteBucket`\.

The conditions can be such things as IP addresses, IP address ranges in CIDR notation, dates, user agents, HTTP referrer, and transports \(HTTP and HTTPS\)\. 

For more information, see [Using Bucket Policies and User Policies](using-iam-policies.md)\.

### AWS identity and access management<a name="AWSIdentityandAccessManagement"></a>

You can use AWS Identity and Access Management \(IAM\) to manage access to your Amazon S3 resources\.

For example, you can use IAM with Amazon S3 to control the type of access a user or group of users has to specific parts of an Amazon S3 bucket your AWS account owns\. 

For more information about IAM, see the following:
+ [AWS Identity and Access Management \(IAM\)](https://aws.amazon.com/iam/)
+ [Getting Started](https://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started.html)
+ [IAM User Guide](https://docs.aws.amazon.com/IAM/latest/UserGuide/)

### Access control lists<a name="S3_ACLs"></a>

You can control access to each of your buckets and objects using an access control list \(ACL\)\. For more information, see [Managing Access with ACLs](S3_ACLs_UsingACLs.md)\.

### Versioning<a name="Versions"></a>

You can use *versioning* to keep multiple versions of an object in the same bucket\. For more information, see [Object Versioning](ObjectVersioning.md)\.

### Operations<a name="BasicsOperations"></a>

Following are the most common operations that you'll run through the API\.

**Common operations**
+ **Create a bucket** – Create and name your own bucket in which to store your objects\.
+ **Write an object** – Store data by creating or overwriting an object\. When you write an object, you specify a unique key in the namespace of your bucket\. This is also a good time to specify any access control you want on the object\.
+ **Read an object** – Read data back\. You can download the data via HTTP or BitTorrent\.
+ **Delete an object** – Delete some of your data\.
+ **List keys** – List the keys contained in one of your buckets\. You can filter the key list based on a prefix\.

These operations and all other functionality are described in detail throughout this guide\.

## Amazon S3 application programming interfaces \(API\)<a name="API"></a>

The Amazon S3 architecture is designed to be programming language\-neutral, using AWS supported interfaces to store and retrieve objects\. 

Amazon S3 provides a REST and a SOAP interface\. They are similar, but there are some differences\. For example, in the REST interface, metadata is returned in HTTP headers\. Because we only support HTTP requests of up to 4 KB \(not including the body\), the amount of metadata you can supply is restricted\. 

**Note**  
 SOAP support over HTTP is deprecated, but it is still available over HTTPS\. New Amazon S3 features will not be supported for SOAP\. We recommend that you use either the REST API or the AWS SDKs\. 

### The REST interface<a name="UsingRESTAPI"></a>

The REST API is an HTTP interface to Amazon S3\. Using REST, you use standard HTTP requests to create, fetch, and delete buckets and objects\.

You can use any toolkit that supports HTTP to use the REST API\. You can even use a browser to fetch objects, as long as they are anonymously readable\.

The REST API uses the standard HTTP headers and status codes, so that standard browsers and toolkits work as expected\. In some areas, we have added functionality to HTTP \(for example, we added headers to support access control\)\. In these cases, we have done our best to add the new functionality in a way that matched the style of standard HTTP usage\.

### The SOAP interface<a name="UsingSOAPAPI"></a>

**Note**  
 SOAP support over HTTP is deprecated, but it is still available over HTTPS\. New Amazon S3 features will not be supported for SOAP\. We recommend that you use either the REST API or the AWS SDKs\. 

The SOAP API provides a SOAP 1\.1 interface using document literal encoding\. The most common way to use SOAP is to download the WSDL \(see [https://doc\.s3\.amazonaws\.com/2006\-03\-01/AmazonS3\.wsdl](https://doc.s3.amazonaws.com/2006-03-01/AmazonS3.wsdl)\), use a SOAP toolkit such as Apache Axis or Microsoft \.NET to create bindings, and then write code that uses the bindings to call Amazon S3\.

## Paying for Amazon S3<a name="PayingforStorage"></a>

Pricing for Amazon S3 is designed so that you don't have to plan for the storage requirements of your application\. Most storage providers force you to purchase a predetermined amount of storage and network transfer capacity: If you exceed that capacity, your service is shut off or you are charged high overage fees\. If you do not exceed that capacity, you pay as though you used it all\. 

Amazon S3 charges you only for what you actually use, with no hidden fees and no overage charges\. This gives developers a variable\-cost service that can grow with their business while enjoying the cost advantages of the AWS infrastructure\.

Before storing anything in Amazon S3, you must register with the service and provide a payment method that is charged at the end of each month\. There are no setup fees to begin using the service\. At the end of the month, your payment method is automatically charged for that month's usage\.

For information about paying for Amazon S3 storage, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

## Related services<a name="RelatedAmazonWebServices"></a>

After you load your data into Amazon S3, you can use it with other AWS services\. The following are the services you might use most frequently:
+ **Amazon Elastic Compute Cloud \(Amazon EC2\)** – This service provides virtual compute resources in the cloud\. For more information, see the [Amazon EC2 product details page](https://aws.amazon.com/ec2/)\.
+ **Amazon EMR** – This service enables businesses, researchers, data analysts, and developers to easily and cost\-effectively process vast amounts of data\. It uses a hosted Hadoop framework running on the web\-scale infrastructure of Amazon EC2 and Amazon S3\. For more information, see the [Amazon EMR product details page](https://aws.amazon.com/elasticmapreduce/)\.
+ **AWS Snowball** – This service accelerates transferring large amounts of data into and out of AWS using physical storage devices, bypassing the internet\. Each AWS Snowball device type can transport data at faster\-than internet speeds\. This transport is done by shipping the data in the devices through a regional carrier\. For more information, see the [AWS Snowball product details page](https://aws.amazon.com/snowball)\. 