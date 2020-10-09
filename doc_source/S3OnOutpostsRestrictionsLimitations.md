# Amazon S3 on Outposts restrictions and limitations<a name="S3OnOutpostsRestrictionsLimitations"></a>

Consider the following restrictions and limitations as you set up Amazon S3 on Outposts\.

**Topics**
+ [Specifications](#S3OnOutpostsSpecifications)
+ [Supported API operations](#S3OnOutpostsAPILimitations)
+ [Unsupported Amazon S3 features](#S3OnOutpostsFeatureLimitations)

## Amazon S3 on Outposts specifications<a name="S3OnOutpostsSpecifications"></a>
+ Maximum Outposts bucket size is 50 TB\.
+ Maximum number of Outposts buckets per Outpost is 100\.
+ Outposts buckets can only be accessed using access points and endpoints\.
+ Maximum number of access points per Outposts bucket is 10\.
+ Access point policies are limited to 20 KB in size\.
+ The Outposts bucket owner account must own all objects in the bucket\.
+ The Outposts bucket owner account can only perform operations on an Outposts bucket\.
+ Object size limitations are consistent with Amazon S3\.
+ All objects stored on S3 on Outposts are stored in the `OUTPOSTS` storage class\.
+ All objects that are created must be owned by the Outposts bucket owner\.

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
+ `ListObjectsv2`
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
+ Lifecycle transitions
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