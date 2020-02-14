# Specifying Permissions in a Policy<a name="using-with-s3-actions"></a>

Amazon S3 defines a set of permissions that you can specify in a policy\. These are keywords, each of which maps to specific Amazon S3 operations\. For more information, see [Actions](https://docs.aws.amazon.com/AmazonS3/latest/API/API_Operations.html) in the *Amazon Simple Storage Service API Reference*\.

**Topics**
+ [Permissions for Object Operations](#using-with-s3-actions-related-to-objects)
+ [Permissions Related to Bucket Operations](#using-with-s3-actions-related-to-buckets)
+ [Permissions Related to Bucket Subresource Operations](#using-with-s3-actions-related-to-bucket-subresources)
+ [Permissions Related to Account Operations](#using-with-s3-actions-related-to-accounts)

## Permissions for Object Operations<a name="using-with-s3-actions-related-to-objects"></a>

This section provides a list of the permissions for object operations that you can specify in a policy\.


**Amazon S3 Permissions for Object Operations**  

| Permissions | Amazon S3 Operations | 
| --- | --- | 
| s3:AbortMultipartUpload | [Abort Multipart Upload](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadAbort.html) | 
|  `s3:BypassGovernanceRetention`  | [PUT Object Retention](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTRetention.html), [PUT Object ](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html), [DELETE Object ](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectDELETE.html) | 
| s3:DeleteObject | [DELETE Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectDELETE.html) | 
|  `s3:DeleteObjectTagging`  | [DELETE Object tagging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectDELETEtagging.html)  | 
| s3:DeleteObjectVersion | [DELETE Object \(a Specific Version of the Object\)](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectDELETE.html) | 
|  `s3:DeleteObjectVersionTagging`  | [DELETE Object tagging \(for a Specific Version of the Object\)](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectDELETEtagging.html) | 
| s3:GetObject |   [GET Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html), [HEAD Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectHEAD.html), [SELECT Object Content](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectSELECTContent.html) When you grant this permission on a version\-enabled bucket, you always get the latest version data\.   | 
| s3:GetObjectAcl | [GET Object ACL](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETacl.html) | 
|  `s3:GetObjectLegalHold`  | [GET Object Legal Hold](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETLegalHold.html), [GET Object ](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html) | 
|  `s3:GetObjectRetention`  | [Get Object Retention](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETRetention.html), [GET Object ](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html) | 
|  `s3:GetObjectTagging`  | [GET Object tagging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETtagging.html)  | 
| s3:GetObjectTorrent | [GET Object torrent](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETtorrent.html) | 
| s3:GetObjectVersion |   [GET Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html), [HEAD Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectHEAD.html) To grant permission for version\-specific object data, you must grant this permission\. That is, when you specify version number when making any of these requests, you need this Amazon S3 permission\.  | 
| s3:GetObjectVersionAcl | [GET ACL ](https://docs.aws.amazon.com/AmazonS3/latest/API/objectGetAclVersions.html) \(for a Specific Version of the Object\) | 
|  `s3:GetObjectVersionTagging`  | [GET Object tagging \(for a Specific Version of the Object\)](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETtagging.html)  | 
| s3:GetObjectVersionTorrent | [GET Object Torrent versioning](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETtorrent.html) | 
| s3:ListMultipartUploadParts | [List Parts](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadListParts.html) | 
| s3:PutObject |   [PUT Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html), [POST Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html), [Initiate Multipart Upload](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html), [Upload Part](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPart.html), [Complete Multipart Upload](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadComplete.html), [PUT Object \- Copy](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPartCopy.html)   | 
| s3:PutObjectAcl |  [PUT Object ACL](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTacl.html) | 
|  `s3:PutObjectLegalHold`  | [PUT Object Legal Hold](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTLegalHold.html), [PUT Object ](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html) | 
|  `s3:PutObjectRetention`  | [PUT Object Retention](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTRetention.html), [PUT Object ](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html) | 
|  `s3:PutObjectTagging`  | [PUT Object tagging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTtagging.html) | 
| s3:PutObjectVersionAcl | [PUT Object ACL \(for a Specific Version of the Object\)](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTacl.html) | 
|  `s3:PutObjectVersionTagging`  | [PUT Object tagging \(for a Specific Version of the Object\)](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTtagging.html) | 
|  `s3:RestoreObject`  | [POST Object restore](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOSTrestore.html) | 

The following example bucket policy grants the `s3:PutObject` and the `s3:PutObjectAcl` permissions to a user \(Dave\)\. If you remove the `Principal` element, you can attach the policy to a user\. These are object operations, and accordingly the relative\-id portion of the `Resource` ARN identifies objects \(`examplebucket/*`\)\. For more information, see [Specifying Resources in a Policy](s3-arn-format.md)\.

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "statement1",
            "Effect": "Allow",
            "Principal": {
                "AWS": "arn:aws:iam::AccountB-ID:user/Dave"
            },
            "Action":   ["s3:PutObject","s3:PutObjectAcl"],
            "Resource": "arn:aws:s3:::examplebucket/*"
        }
    ]
}
```

You can use a wildcard to grant permission for all Amazon S3 actions\.

```
"Action":   "*"
```

## Permissions Related to Bucket Operations<a name="using-with-s3-actions-related-to-buckets"></a>

This section provides a list of the permissions related to bucket operations that you can specify in a policy\.


**Amazon S3 Permissions Related to Bucket Operations**  

| Permission Keywords | Amazon S3 Operation\(s\) Covered | 
| --- | --- | 
| s3:CreateBucket | [PUT Bucket](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUT.html) | 
| s3:DeleteBucket | [DELETE Bucket](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETE.html) | 
| s3:ListBucket | [GET Bucket \(List Objects\)](https://docs.aws.amazon.com/AmazonS3/latest/API/v2-RESTBucketGET.html), [HEAD Bucket](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketHEAD.html) | 
| s3:ListBucketVersions | [GET Bucket Object versions](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETVersion.html) | 
| s3:ListAllMyBuckets | [GET Service](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTServiceGET.html)  | 
| s3:ListBucketMultipartUploads | [List Multipart Uploads](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadListMPUpload.html) | 

The following example user policy grants the `s3:CreateBucket`, `s3:ListAllMyBuckets`, and the `s3:GetBucketLocation` permissions to a user\. Note that for all these permissions, you set the relative\-id part of the `Resource` ARN to "\*"\. For all other bucket actions, you must specify a bucket name\. For more information, see [Specifying Resources in a Policy](s3-arn-format.md)\.

```
{
   "Version":"2012-10-17",
   "Statement":[
      {
         "Sid":"statement1",
         "Effect":"Allow",
         "Action":[
            "s3:CreateBucket", 
            "s3:ListAllMyBuckets", 
            "s3:GetBucketLocation"  
         ],
         "Resource":[
            "arn:aws:s3:::*"
         ]
       }
    ]
}
```

If your user is going to use the console to view buckets and see the contents of any of these buckets, the user must have the `s3:ListAllMyBuckets` and `s3:GetBucketLocation` permissions\. For an example, see "Policy for Console Access" at [Writing IAM Policies: How to Grant Access to an S3 Bucket](https://aws.amazon.com/blogs/security/writing-iam-policies-how-to-grant-access-to-an-amazon-s3-bucket/)\.

## Permissions Related to Bucket Subresource Operations<a name="using-with-s3-actions-related-to-bucket-subresources"></a>

This section provides a list of the permissions related to bucket subresource operations that you can specify in a policy\.


**Amazon S3 Permissions Related to Bucket Subresource Operations**  

| Permissions | Amazon S3 Operation\(s\) Covered | 
| --- | --- | 
| s3:DeleteBucketPolicy | [DELETE Bucket policy](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEpolicy.html) | 
| s3:DeleteBucketWebsite | [DELETE Bucket website](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEwebsite.html) | 
| s3:GetAccelerateConfiguration | [GET Bucket accelerate ](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETaccelerate.html) | 
| s3:GetAnalyticsConfiguration | [GET Bucket analytics](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETAnalyticsConfig.html), [List Bucket Analytics Configurations ](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketListAnalyticsConfigs.html) | 
| s3:GetBucketAcl | [GET Bucket acl](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETacl.html) | 
| s3:GetBucketCORS | [GET Bucket cors](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETcors.html) | 
| s3:GetBucketLocation | [GET Bucket location](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlocation.html) | 
| s3:GetBucketLogging | [GET Bucket logging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlogging.html) | 
| s3:GetBucketNotification | [GET Bucket notification](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETnotification.html) | 
|  `s3:GetBucketObjectLockConfiguration`  | [GET Bucket Object Lock configuration](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETObjectLockConfiguration.html) | 
| s3:GetBucketPolicy | [GET Bucket policy](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETpolicy.html) | 
| s3:GetBucketPolicyStatus | [GET BucketPolicyStatus](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETPolicyStatus.html) | 
| s3:GetBucketPublicAccessBlock | [GET PublicAccessBlock](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETPublicAccessBlock.html) | 
| s3:GetBucketRequestPayment | [GET Bucket requestPayment](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTrequestPaymentGET.html) | 
| s3:GetBucketTagging  | [GET Bucket tagging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETtagging.html) | 
| s3:GetBucketVersioning | [GET Bucket versioning](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETversioningStatus.html) | 
| s3:GetBucketWebsite | [GET Bucket website](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETwebsite.html) | 
| s3:GetEncryptionConfiguration | [ GET Bucket encryption](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETencryption.html)  | 
| s3:GetInventoryConfiguration | [GET Bucket inventory](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETInventoryConfig.html), [List Bucket Inventory Configurations ](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketListInventoryConfigs.html) | 
| s3:GetLifecycleConfiguration | [GET Bucket lifecycle](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlifecycle.html) | 
| s3:GetMetricsConfiguration | [GET Bucket metrics](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETMetricConfiguration.html), [List Bucket Metrics Configurations ](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTListBucketMetricsConfiguration.html) | 
| s3:GetReplicationConfiguration | [GET Bucket replication](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETreplication.html) | 
| s3:PutAccelerateConfiguration | [PUT Bucket accelerate ](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTaccelerate.html) | 
| s3:PutAnalyticsConfiguration | [PUT Bucket analytics](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTAnalyticsConfig.html), [DELETE Bucket analytics ](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEAnalyticsConfiguration.html) | 
| s3:PutBucketAcl | [PUT Bucket acl](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTacl.html) | 
| s3:PutBucketCORS | [PUT Bucket cors](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTcors.html), [DELETE Bucket cors](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEcors.html) | 
| s3:PutBucketLogging | [PUT Bucket logging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTlogging.html) | 
| s3:PutBucketNotification | [PUT Bucket notification](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTnotification.html) | 
|  `s3:PutBucketObjectLockConfiguration`  | [PUT Bucket Object Lock configuration](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTObjectLockConfiguration.html) | 
| s3:PutBucketPolicy | [PUT Bucket policy](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTpolicy.html) | 
| s3:PutBucketPublicAccessBlock | [PUT PublicAccessBlock](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTPublicAccessBlock.html), [DELETE PublicAccessBlock](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEPublicAccessBlock.html)  | 
| s3:PutBucketRequestPayment | [PUT Bucket requestPayment](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTrequestPaymentPUT.html) | 
| s3:PutBucketTagging  | [DELETE Bucket tagging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEtagging.html), [PUT Bucket tagging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTtagging.html) | 
| s3:PutBucketVersioning | [PUT Bucket versioning](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTVersioningStatus.html) | 
| s3:PutBucketWebsite | [PUT Bucket website](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTwebsite.html) | 
| s3:PutEncryptionConfiguration | [PUT Bucket encryption](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTencryption.html), [ DELETE Bucket encryption](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEencryption.html)  | 
| s3:PutInventoryConfiguration | [PUT Bucket inventory](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTInventoryConfig.html), [DELETE Bucket inventory ](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEInventoryConfiguration.html) | 
| s3:PutLifecycleConfiguration | [PUT Bucket lifecycle](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTlifecycle.html), [DELETE Bucket lifecycle](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETElifecycle.html)  | 
| s3:PutMetricsConfiguration | [PUT Bucket metrics](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTMetricConfiguration.html), [DELETE Bucket metrics ](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTDeleteBucketMetricsConfiguration.html) | 
| s3:PutReplicationConfiguration | [PUT Bucket replication](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTreplication.html), [DELETE Bucket replication](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEreplication.html) | 

The following user policy grants the `s3:GetBucketAcl` permission on the `examplebucket` bucket to user Dave\.

```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "statement1",
      "Effect": "Allow",
      "Principal": {
        "AWS": "arn:aws:iam::Account-ID:user/Dave"
      },
      "Action": [
        "s3:GetObjectVersion",
        "s3:GetBucketAcl"
      ],
      "Resource": "arn:aws:s3:::examplebucket"
    }
  ]
}
```

You can delete objects either by explicitly calling the DELETE Object API or by configuring its lifecycle \(see [Object Lifecycle Management](object-lifecycle-mgmt.md)\) so that Amazon S3 can remove the objects when their lifetime expires\. To explicitly block users or accounts from deleting objects, you must explicitly deny them `s3:DeleteObject`, `s3:DeleteObjectVersion`, and `s3:PutLifecycleConfiguration` permissions\. By default, users have no permissions\. But as you create users, add users to groups, and grant them permissions, it is possible for users to get certain permissions that you did not intend to give\. That is where you can use explicit deny, which supersedes all other permissions a user might have and denies the user permissions for specific actions\.

## Permissions Related to Account Operations<a name="using-with-s3-actions-related-to-accounts"></a>

This section provides a list of the permissions related to account operations that you can specify in a policy\.


**Amazon S3 Permissions Related to Account Operations**  

| Permission Keywords | Amazon S3 Operation\(s\) Covered | 
| --- | --- | 
| s3:CreateJob | [CreateJob](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTAccountPOSTCreateJob.html) | 
| s3:DescribeJob | [DescribeJob](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTAccountGETDescribeJob.html) | 
| s3:GetAccountPublicAccessBlock | [GET PublicAccessBlock](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTAccountGETPublicAccessBlock.html) | 
| s3:ListJobs | [ListJobs](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTAccountGETListJobs.html) | 
| s3:PutAccountPublicAccessBlock | [PUT PublicAccessBlock](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTAccountPUTPublicAccessBlock.html), [DELETE PublicAccessBlock](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTAccountDELETEPublicAccessBlock.html) | 
| s3:UpdateJobPriority | [UpdateJobPriority](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTAccountPOSTUpdateJobPriority.html) | 
| s3:UpdateJobStatus | [UpdateJobStatus](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTAccountPOSTUpdateJobStatus.html) | 

The following example user policy grants the `s3:GetAccountPublicAccessBlock` permission to a user\. Note that for these permissions, you set the `Resource` value to `"*"`\. For more information, see [Specifying Resources in a Policy](s3-arn-format.md)\.

```
{
   "Version":"2012-10-17",
   "Statement":[
      {
         "Sid":"statement1",
         "Effect":"Allow",
         "Action":[
            "s3:GetAccountPublicAccessBlock" 
         ],
         "Resource":[
            "*"
         ]
       }
    ]
}
```