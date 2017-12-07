# Bucket Restrictions and Limitations<a name="BucketRestrictions"></a>

 A bucket is owned by the AWS account that created it\. By default, you can create up to 100 buckets in each of your AWS accounts\. If you need additional buckets, you can increase your bucket limit by submitting a service limit increase\. For information about how to increase your bucket limit, see [AWS Service Limits](http://docs.aws.amazon.com/general/latest/gr/aws_service_limits.html) in the *AWS General Reference*\. 

 Bucket ownership is not transferable; however, if a bucket is empty, you can delete it\. After a bucket is deleted, the name becomes available to reuse, but the name might not be available for you to reuse for various reasons\. For example, some other account could create a bucket with that name\. Note, too, that it might take some time before the name can be reused\. So if you want to use the same bucket name, don't delete the bucket\. 

 There is no limit to the number of objects that can be stored in a bucket and no difference in performance whether you use many buckets or just a few\. You can store all of your objects in a single bucket, or you can organize them across several buckets\.

You cannot create a bucket within another bucket\.

The high\-availability engineering of Amazon S3 is focused on get, put, list, and delete operations\. Because bucket operations work against a centralized, global resource space, it is not appropriate to create or delete buckets on the high\-availability code path of your application\. It is better to create or delete buckets in a separate initialization or setup routine that you run less often\. 

**Note**  
 If your application automatically creates buckets, choose a bucket naming scheme that is unlikely to cause naming conflicts\. Ensure that your application logic will choose a different bucket name if a bucket name is already taken\.

## Rules for Bucket Naming<a name="bucketnamingrules"></a>

 We recommend that all bucket names comply with DNS naming conventions\. These conventions are enforced in all Regions except for the US East \(N\. Virginia\) Region\.

**Note**  
If you use the AWS Management Console, bucket names must be DNS\-compliant in all Regions\. 

 DNS\-compliant bucket names allow customers to benefit from new features and operational improvements, as well as providing support for virtual\-host style access to buckets\. While the US East \(N\. Virginia\) Region currently allows non\-compliant DNS bucket naming, we are moving to the same DNS\-compliant bucket naming convention for the US East \(N\. Virginia\) Region in the coming months\. This ensures a single, consistent naming approach for Amazon S3 buckets\. The rules for DNS\-compliant bucket names are:

+ Bucket names must be at least 3 and no more than 63 characters long\.

+ Bucket names must be a series of one or more labels\. Adjacent labels are separated by a single period \(\.\)\. Bucket names can contain lowercase letters, numbers, and hyphens\. Each label must start and end with a lowercase letter or a number\.

+ Bucket names must not be formatted as an IP address \(e\.g\., 192\.168\.5\.4\)\.

+ When using virtual hosted–style buckets with SSL, the SSL wildcard certificate only matches buckets that do not contain periods\. To work around this, use HTTP or write your own certificate verification logic\. We recommend that you do not use periods \("\."\) in bucket names\. 

The following examples are invalid bucket names:


| Invalid Bucket Name | Comment | 
| --- | --- | 
| \.myawsbucket | Bucket name cannot start with a period \(\.\)\. | 
| myawsbucket\. | Bucket name cannot end with a period \(\.\)\. | 
| my\.\.examplebucket | There can be only one period between labels\.  | 

### Challenges with Non–DNS\-Compliant Bucket Names<a name="non-dns-compliant-bucketname-challenges"></a>

The US East \(N\. Virginia\) Region currently allows more relaxed standards for bucket naming, which can result in a bucket name that is not DNS\-compliant\. For example,  `MyAWSBucket` is a valid bucket name, even though it contains uppercase letters\. If you try to access this bucket by using a virtual\-hosted–style request \(`http://MyAWSBucket.s3.amazonaws.com/yourobject`\), the URL resolves to the bucket `myawsbucket` and not the bucket  `MyAWSBucket`\. In response, Amazon S3 will return a "bucket not found" error\.

To avoid this problem, we recommend as a best practice that you always use DNS\-compliant bucket names regardless of the Region in which you create the bucket\. For more information about virtual\-hosted–style access to your buckets, see [Virtual Hosting of Buckets](VirtualHosting.md)\.

The name of the bucket used for Amazon S3 Transfer Acceleration must be DNS\-compliant and must not contain periods \("\."\)\. For more information about transfer acceleration, see [Amazon S3 Transfer Acceleration](transfer-acceleration.md)\.

 The rules for bucket names in the US East \(N\. Virginia\) Region allow bucket names to be as long as 255 characters, and bucket names can contain any combination of uppercase letters, lowercase letters, numbers, periods \(\.\), hyphens \(\-\), and underscores \(\_\)\. 