# Bucket Restrictions and Limitations<a name="BucketRestrictions"></a>

 A bucket is owned by the AWS account that created it\. By default, you can create up to 100 buckets in each of your AWS accounts\. If you need additional buckets, you can increase your bucket limit by submitting a service limit increase\. For information about how to increase your bucket limit, see [AWS Service Limits](http://docs.aws.amazon.com/general/latest/gr/aws_service_limits.html) in the *AWS General Reference*\. 

 Bucket ownership is not transferable; however, if a bucket is empty, you can delete it\. After a bucket is deleted, the name becomes available to reuse, but the name might not be available for you to reuse for various reasons\. For example, some other account could create a bucket with that name\. Note, too, that it might take some time before the name can be reused\. So if you want to use the same bucket name, don't delete the bucket\. 

There is no limit to the number of objects that can be stored in a bucket and no difference in performance whether you use many buckets or just a few\. You can store all of your objects in a single bucket, or you can organize them across several buckets\.

After you have created a bucket, you can't change its Region\.

If you explicitly specify an AWS Region in your create bucket request that is different from the Region that you specified when you created the client, you might get an error\.

You cannot create a bucket within another bucket\.

The high\-availability engineering of Amazon S3 is focused on get, put, list, and delete operations\. Because bucket operations work against a centralized, global resource space, it is not appropriate to create or delete buckets on the high\-availability code path of your application\. It is better to create or delete buckets in a separate initialization or setup routine that you run less often\. 

**Note**  
 If your application automatically creates buckets, choose a bucket naming scheme that is unlikely to cause naming conflicts\. Ensure that your application logic will choose a different bucket name if a bucket name is already taken\.

## Rules for Bucket Naming<a name="bucketnamingrules"></a>

After you create an S3 bucket, you can't change the bucket name, so choose the name wisely\. 

**Important**  
On March 1, 2018, we updated our naming conventions for S3 buckets in the US East \(N\. Virginia\) Region to match the naming conventions that we use in all other worldwide AWS Regions\. Amazon S3 no longer supports creating bucket names that contain uppercase letters or underscores\. This change ensures that each bucket can be addressed using virtual host style addressing, such as `https://myawsbucket.s3.amazonaws.com`\. We highly recommend that you review your existing bucket\-creation processes to ensure that you follow these DNS\-compliant naming conventions\.

The following are the rules for naming S3 buckets in all AWS Regions:
+ Bucket names must be unique across all existing bucket names in Amazon S3\.
+ Bucket names must comply with DNS naming conventions\. 
+ Bucket names must be at least 3 and no more than 63 characters long\.
+ Bucket names must not contain uppercase characters or underscores\.
+ Bucket names must start with a lowercase letter or number\.
+ Bucket names must be a series of one or more labels\. Adjacent labels are separated by a single period \(\.\)\. Bucket names can contain lowercase letters, numbers, and hyphens\. Each label must start and end with a lowercase letter or a number\.
+ Bucket names must not be formatted as an IP address \(for example, 192\.168\.5\.4\)\.
+ When you use virtual hosted–style buckets with Secure Sockets Layer \(SSL\), the SSL wildcard certificate only matches buckets that don't contain periods\. To work around this, use HTTP or write your own certificate verification logic\. We recommend that you do not use periods \("\."\) in bucket names when using virtual hosted–style buckets\. 

### Legacy Non–DNS\-Compliant Bucket Names<a name="non-dns-compliant-bucketname-challenges"></a>

 Beginning on March 1, 2018, we updated our naming conventions for S3 buckets in the US East \(N\. Virginia\) Region to require DNS\-compliant names\.

The US East \(N\. Virginia\) Region previously allowed more relaxed standards for bucket naming, which could have resulted in a bucket name that is not DNS\-compliant\. For example, `MyAWSbucket` was a valid bucket name, even though it contains uppercase letters\. If you try to access this bucket by using a virtual\-hosted–style request \(`http://MyAWSbucket.s3.amazonaws.com/yourobject`\), the URL resolves to the bucket `myawsbucket` and not the bucket `MyAWSbucket`\. In response, Amazon S3 returns a "bucket not found" error\. For more information about virtual\-hosted–style access to your buckets, see [Virtual Hosting of Buckets](VirtualHosting.md)\.

The legacy rules for bucket names in the US East \(N\. Virginia\) Region allowed bucket names to be as long as 255 characters, and bucket names could contain any combination of uppercase letters, lowercase letters, numbers, periods \(\.\), hyphens \(\-\), and underscores \(\_\)\. 

The name of the bucket used for Amazon S3 Transfer Acceleration must be DNS\-compliant and must not contain periods \("\."\)\. For more information about transfer acceleration, see [Amazon S3 Transfer Acceleration](transfer-acceleration.md)\.