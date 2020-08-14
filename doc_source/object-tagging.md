# Object tagging<a name="object-tagging"></a>

Use object tagging to categorize storage\. Each tag is a key\-value pair\. Consider the following tagging examples:
+ Suppose that an object contains protected health information \(PHI\) data\. You might tag the object using the following key\-value pair\.

  ```
  PHI=True
  ```

  or

  ```
  Classification=PHI
  ```
+ Suppose that you store project files in your S3 bucket\. You might tag these objects with a key named `Project` and a value, as shown following\.

  ```
  Project=Blue
  ```
+ You can add multiple tags to an object, as shown following\.

  ```
  Project=x
  Classification=confidential
  ```

You can add tags to new objects when you upload them, or you can add them to existing objects\. Note the following:
+ You can associate up to 10 tags with an object\. Tags that are associated with an object must have unique tag keys\.
+ A tag key can be up to 128 Unicode characters in length, and tag values can be up to 256 Unicode characters in length\.
+ The key and values are case sensitive\.
+ For more information about tag restrictions, see [User\-Defined Tag Restrictions](https://docs.aws.amazon.com/awsaccountbilling/latest/aboutv2/allocation-tag-restrictions.html)\.

Object key name prefixes also enable you to categorize storage\. However, prefix\-based categorization is one\-dimensional\. Consider the following object key names:

```
photos/photo1.jpg
project/projectx/document.pdf
project/projecty/document2.pdf
```

These key names have the prefixes `photos/`, `project/projectx/`, and `project/projecty/`\. These prefixes enable one\-dimensional categorization\. That is, everything under a prefix is one category\. For example, the prefix `project/projectx` identifies all documents related to project x\.

With tagging, you now have another dimension\. If you want photo1 in project x category, you can tag the object accordingly\. In addition to data classification, tagging offers benefits such as the following:
+ Object tags enable fine\-grained access control of permissions\. For example, you could grant an IAM user permissions to read\-only objects with specific tags\.
+ Object tags enable fine\-grained object lifecycle management in which you can specify a tag\-based filter, in addition to a key name prefix, in a lifecycle rule\.
+ When using Amazon S3 analytics, you can configure filters to group objects together for analysis by object tags, by key name prefix, or by both prefix and tags\.
+ You can also customize Amazon CloudWatch metrics to display information by specific tag filters\. The following sections provide details\.

**Important**  
It is acceptable to use tags to label objects containing confidential data, such as personally identifiable information \(PII\) or protected health information \(PHI\)\. However, the tags themselves shouldn't contain any confidential information\. 

To add object tag sets to more than one Amazon S3 object with a single request, you can use S3 Batch Operations\. You provide S3 Batch Operations with a list of objects to operate on\. S3 Batch Operations call the respective API to perform the specified operation\. A single S3 Batch Operations job can perform the specified operation on billions of objects containing exabytes of data\. 

S3 Batch Operations track progress, send notifications, and store a detailed completion report of all actions, providing a fully managed, auditable, serverless experience\. You can use S3 Batch Operations through the AWS Management Console, AWS CLI, AWS SDKs, or REST API\. For more information, see [The basics: S3 Batch Operations](batch-ops-basics.md)\.

## API operations related to object tagging<a name="tagging-apis"></a>

Amazon S3 supports the following API operations that are specifically for object tagging:

**Object API Operations**
+  [PUT Object tagging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTtagging.html) – Replaces tags on an object\. You specify tags in the request body\.  There are two distinct scenarios of object tag management using this API\.
  + Object has no tags – Using this API you can add a set of tags to an object \(the object has no prior tags\)\.
  + Object has a set of existing tags – To modify the existing tag set, you must first retrieve the existing tag set, modify it on the client side, and then use this API to replace the tag set\.
**Note**  
 If you send this request with an empty tag set, Amazon S3 deletes the existing tag set on the object\. If you use this method, you will be charged for a Tier 1 Request \(PUT\)\. For more information, see [Amazon S3 Pricing](https://d0.awsstatic.com/whitepapers/aws_pricing_overview.pdf)\.  
The [DELETE Object tagging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectDELETEtagging.html) request is preferred because it achieves the same result without incurring charges\. 
+  [GET Object tagging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETtagging.html) – Returns the tag set associated with an object\. Amazon S3 returns object tags in the response body\.
+ [DELETE Object tagging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectDELETEtagging.html) – Deletes the tag set associated with an object\. 

**Other API Operations That Support Tagging**
+  [PUT Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html) and [Initiate Multipart Upload](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html)– You can specify tags when you create objects\. You specify tags using the `x-amz-tagging` request header\. 
+  [GET Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html) – Instead of returning the tag set, Amazon S3 returns the object tag count in the `x-amz-tag-count` header \(only if the requester has permissions to read tags\) because the header response size is limited to 8 K bytes\. If you want to view the tags, you make another request for the [GET Object tagging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETtagging.html) API operation\.
+ [POST Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html) – You can specify tags in your POST request\. 

  As long as the tags in your request don't exceed the 8 K byte HTTP request header size limit, you can use the `PUT Object `API to create objects with tags\. If the tags you specify exceed the header size limit, you can use this POST method in which you include the tags in the body\. 

   [PUT Object \- Copy](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html) – You can specify the `x-amz-tagging-directive` in your request to direct Amazon S3 to either copy \(default behavior\) the tags or replace tags by a new set of tags provided in the request\. 

Note the following:
+ Tagging follows the eventual consistency model\. That is, soon after adding tags to an object, if you try to retrieve the tags, you might get old tags, if any, on the objects\. However, a subsequent call will likely provide the updated tags\.

## Object tagging and additional information<a name="tagging-other-configs"></a>

This section explains how object tagging relates to other configurations\.

### Object tagging and lifecycle management<a name="tagging-and-lifecycle"></a>

In bucket lifecycle configuration, you can specify a filter to select a subset of objects to which the rule applies\. You can specify a filter based on the key name prefixes, object tags, or both\. 

Suppose that you store photos \(raw and the finished format\) in your Amazon S3 bucket\. You might tag these objects as shown following\. 

```
phototype=raw
or
phototype=finished
```

You might consider archiving the raw photos to S3 Glacier sometime after they are created\. You can configure a lifecycle rule with a filter that identifies the subset of objects with the key name prefix \(`photos/`\) that have a specific tag \(`phototype=raw`\)\. 

For more information, see [Object lifecycle management](object-lifecycle-mgmt.md)\. 

### Object tagging and replication<a name="tagging-and-replication"></a>

If you configured Replication on your bucket, Amazon S3 replicates tags, provided you grant Amazon S3 permission to read the tags\. For more information, see [Overview of setting up replication](replication-how-setup.md)\.

### Object tagging and access control policies<a name="tagging-and-policies"></a>

You can also use permissions policies \(bucket and user policies\) to manage permissions related to object tagging\. For policy actions see the following topics: 
+  [Example — Object Operations](using-with-s3-actions.md#using-with-s3-actions-related-to-objects) 
+  [Example — Bucket Operations](using-with-s3-actions.md#using-with-s3-actions-related-to-buckets)

Object tags enable fine\-grained access control for managing permissions\. You can grant conditional permissions based on object tags\. Amazon S3 supports the following condition keys that you can use to grant conditional permissions based on object tags:
+ `s3:ExistingObjectTag/<tag-key>` – Use this condition key to verify that an existing object tag has the specific tag key and value\. 
**Note**  
When granting permissions for the `PUT Object` and `DELETE Object` operations, this condition key is not supported\. That is, you cannot create a policy to grant or deny a user permissions to delete or overwrite an object based on its existing tags\. 
+ `s3:RequestObjectTagKeys` – Use this condition key to restrict the tag keys that you want to allow on objects\. This is useful when adding tags to objects using the PutObjectTagging and PutObject, and POST object requests\.
+ `s3:RequestObjectTag/<tag-key>` – Use this condition key to restrict the tag keys and values that you want to allow on objects\. This is useful when adding tags to objects using the PutObjectTagging and PutObject, and POST Bucket requests\.

For a complete list of Amazon S3 service\-specific condition keys, see [Amazon S3 Condition Keys](amazon-s3-policy-keys.md)\. The following permissions policies illustrate how object tagging enables fine grained access permissions management\.

**Example 1: Allow a user to read only the objects that have a specific tag**  
The following permissions policy grants a user permission to read objects, but the condition limits the read permission to only objects that have the following specific tag key and value\.  

```
security : public
```
Note that the policy uses the Amazon S3 condition key, `s3:ExistingObjectTag/<tag-key>` to specify the key and value\.  

```
 1. {
 2.   "Version": "2012-10-17",
 3.   "Statement": [
 4.     {
 5.       "Effect":     "Allow",
 6.       "Action":     "s3:GetObject",
 7.       "Resource":    "arn:aws:s3:::awsexamplebucket1/*",
 8.       "Principal":   "*",
 9.       "Condition": {  "StringEquals": {"s3:ExistingObjectTag/security": "public" } }
10.     }
11.   ]
12. }
```

**Example 2: Allow a user to add object tags with restrictions on the allowed tag keys**  
The following permissions policy grants a user permissions to perform the `s3:PutObjectTagging` action, which allows user to add tags to an existing object\. The condition limits the tag keys that the user is allowed to use\. The condition uses the `s3:RequestObjectTagKeys` condition key to specify the set of tag keys\.  

```
 1. {
 2.   "Version": "2012-10-17",
 3.   "Statement": [
 4.     {
 5.       "Effect": "Allow",
 6.       "Action": [
 7.         "s3:PutObjectTagging"
 8.       ],
 9.       "Resource": [
10.         "arn:aws:s3:::awsexamplebucket1/*"
11.       ],
12.       "Principal":{
13.         "CanonicalUser":[
14.             "64-digit-alphanumeric-value"
15.          ]
16.        },
17.       "Condition": {
18.         "ForAllValues:StringLike": {
19.           "s3:RequestObjectTagKeys": [
20.             "Owner",
21.             "CreationDate"
22.           ]
23.         }
24.       }
25.     }
26.   ]
27. }
```
The policy ensures that the tag set, if specified in the request, has the specified keys\. A user might send an empty tag set in `PutObjectTagging`, which is allowed by this policy \(an empty tag set in the request removes any existing tags on the object\)\. If you want to prevent a user from removing the tag set, you can add another condition to ensure that the user provides at least one value\. The `ForAnyValue` in the condition ensures at least one of the specified values must be present in the request\.  

```
 1. {
 2. 
 3.   "Version": "2012-10-17",
 4.   "Statement": [
 5.     {
 6.       "Effect": "Allow",
 7.       "Action": [
 8.         "s3:PutObjectTagging"
 9.       ],
10.       "Resource": [
11.         "arn:aws:s3:::awsexamplebucket1/*"
12.       ],
13.       "Principal":{
14.         "AWS":[
15.             "arn:aws:iam::account-number-without-hyphens:user/username"
16.          ]
17.        },
18.       "Condition": {
19.         "ForAllValues:StringLike": {
20.           "s3:RequestObjectTagKeys": [
21.             "Owner",
22.             "CreationDate"
23.           ]
24.         },
25.         "ForAnyValue:StringLike": {
26.           "s3:RequestObjectTagKeys": [
27.             "Owner",
28.             "CreationDate"
29.           ]
30.         }
31.       }
32.     }
33.   ]
34. }
```
For more information, see [Creating a Condition That Tests Multiple Key Values \(Set Operations\)](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_multi-value-conditions.html) in the *IAM User Guide*\.

**Example 3: Allow a user to add object tags that include a specific tag key and value**  
The following user policy grants a user permissions to perform the `s3:PutObjectTagging` action, which allows user to add tags on an existing object\. The condition requires the user to include a specific tag \(`Project`\) with value set to `X`\.   

```
 1. {
 2.   "Version": "2012-10-17",
 3.   "Statement": [
 4.     {
 5.       "Effect": "Allow",
 6.       "Action": [
 7.         "s3:PutObjectTagging"
 8.       ],
 9.       "Resource": [
10.         "arn:aws:s3:::awsexamplebucket1/*"
11.       ],
12.       "Principal":{
13.         "AWS":[
14.             "arn:aws:iam::account-number-without-hyphens:user/username"
15.          ]
16.        },
17.       "Condition": {
18.         "StringEquals": {
19.           "s3:RequestObjectTag/Project": "X"
20.         }
21.       }
22.     }
23.   ]
24. }
```

**Related Topics**  
[Managing object tags](tagging-managing.md)