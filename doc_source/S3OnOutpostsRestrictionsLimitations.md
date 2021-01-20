# Amazon S3 on Outposts restrictions and limitations<a name="S3OnOutpostsRestrictionsLimitations"></a>

Consider the following restrictions and limitations as you set up Amazon S3 on Outposts\.

**Topics**
+ [Specifications](#S3OnOutpostsSpecifications)
+ [Data consistency model](#S3OnOutpostsDataConsistency)
+ [Supported API operations](#S3OnOutpostsAPILimitations)
+ [Unsupported Amazon S3 features](#S3OnOutpostsFeatureLimitations)
+ [Network restrictions](#S3OnOutpostsConnectivityRestrictions)

## Amazon S3 on Outposts specifications<a name="S3OnOutpostsSpecifications"></a>
+ Maximum Outposts bucket size is 50 TB\.
+ Maximum number of Outposts buckets per Outpost is 100\.
+ Outposts buckets can only be accessed using access points and endpoints\.
+ Maximum number of access points per Outposts bucket is 10\.
+ Access point policies are limited to 20 KB in size\.
+ The S3 on Outposts bucket owner account is always the owner of all objects in the bucket\.
+ Only the S3 on Outposts bucket owner account can perform operations on the bucket\.
+ Object size limitations are consistent with Amazon S3\.
+ All objects stored on S3 on Outposts are stored in the `OUTPOSTS` storage class\.
+ All objects stored in the `OUTPOSTS` storage class are stored using server\-side encryption with Amazon S3\-managed encryption keys \(SSE\-S3\) by default\. You can also explicity choose to store objects using server\-side encryption with customer\-provided encryption keys \(SSE\-C\)\.
+ If there is not enough space to store an object on your Outpost, the API will return an insufficient capacity exception \(ICE\)\. 

## Amazon S3 on Outposts data consistency model<a name="S3OnOutpostsDataConsistency"></a>

Amazon S3 on Outposts provides read\-after\-write consistency for PUTS of new objects in your Amazon S3 bucket with one caveat\. The caveat is that if you make a HEAD or GET request to a key name before the object is created, then create the object shortly after that, a subsequent GET might not return the object due to eventual consistency\.

Amazon S3 on Outposts offers eventual consistency for overwrite PUTS and DELETES in all Regions\.

Updates to a single key are atomic\. For example, if you PUT to an existing key, a subsequent read might return the old data or the updated data, but it never returns corrupted or partial data\. With the eventual consistency model, you might observe the following behaviors:
+ A process writes a new object to S3OutpostsLong; and immediately lists keys within its bucket\. Until the change is fully propagated, the object might not appear in the list\.
+ A process replaces an existing object and immediately tries to read it\. Until the change is fully propagated, Amazon S3 on Outposts might return the previous data\.
+ A process deletes an existing object and immediately tries to read it\. Until the deletion is fully propagated, Amazon S3 on Outposts might return the deleted data\.
+ A process deletes an existing object and immediately lists keys within its bucket\. Until the deletion is fully propagated, Amazon S3 on Outposts might list the deleted object\.

## API operations supported by Amazon S3 on Outposts<a name="S3OnOutpostsAPILimitations"></a>

Amazon S3 on Outposts is designed to use the same object APIs as Amazon S3\. Therefore, you can use many of your existing code and policies by passing the S3 on Outposts Amazon Resource Name \(ARN\) as your identifier\.

Amazon S3 on Outposts supports the following API operations:
+ `AbortMultipartUpload`
+ `CompleteMultipartUpload`
+ `CopyObject`
+ `CreateMultipartUpload`
+ `DeleteObject`
+ `DeleteObjects`
+ `DeleteObjectTagging`
+ `GetObject`
+ `GetObjectTagging`
+ `HeadObject`
+ `HeadBucket`
+ `ListMultipartUploads`
+ `ListObjects`
+ `ListObjectsV2`
+ `ListParts`
+ `PutObject`
+ `PutObjectTagging`
+ `UploadPart`
+ `UploadPartCopy`

## Amazon S3 features not supported by Amazon S3 on Outposts<a name="S3OnOutpostsFeatureLimitations"></a>

Several Amazon S3 features are currently not supported by Amazon S3 on Outposts\. Any attempts to use them are rejected\.
+ Access control list \(ACL\)
+ CORS
+ Batch Operations
+ Inventory reports
+ Changing the default bucket encryption
+ Public buckets
+ MFA Delete
+ Lifecycle transitions limited to object deletion and aborting incomplete multipart uploads
+ Object Lock legal hold
+ Object Lock retention
+ Object Versioning
+ SSE\-KMS
+ Replication
+ Replication Time Control
+ Amazon CloudWatch Request Metrics
+ Metrics configuration
+ Transfer acceleration
+ Event notifications
+ Requester Pays buckets
+ S3 Select
+ Torrent
+ Lambda events
+ Server access logging
+ Presigned URLs
+ HTTP POST requests
+ SOAP
+ Website access

## Amazon S3 on Outposts network restrictions<a name="S3OnOutpostsConnectivityRestrictions"></a>
+ You will need to create and configure an endpoint in order to route requests to an Amazon S3 on Outposts access point\. The following limits apply endpoints for S3 on Outposts:
  + Each virtual private cloud \(VPC\) on your AWS Outposts can have one associated endpoint, and you can have up to three endpoints per AWS Outposts\.
  + Multiple access point can be mapped to the same endpoint\.
  + endpoints can only be added to VPCs with CIDR blocks in the subspaces of the following CIDR ranges\.
    + 10\.0\.10\.0/24
    + 172\.16\.0\.0/12
    + 192\.168\.0\.0/16
+ An endpoint can only be created within a single CIDR block\.
+ Connections from peered VPCs to an endpoint are not supported\.
+ The subnet used to create an endpoint must contain four IP addresses for S3 on Outposts to use\.
+ A CIDR range used to create an endpoint for an outpost cannot be reused for another endpoint within that VPC\.