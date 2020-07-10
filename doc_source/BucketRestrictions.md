# Bucket restrictions and limitations<a name="BucketRestrictions"></a>

A bucket is owned by the AWS account that created it\. Bucket ownership is not transferable\.

When you create a bucket, you choose its name and the Region to create it in\. After you create a bucket, you can't change its name or Region\.

By default, you can create up to 100 buckets in each of your AWS accounts\. If you need additional buckets, you can increase your account bucket limit to a maximum of 1,000 buckets by submitting a service limit increase\. There is no difference in performance whether you use many buckets or just a few\. For information about how to increase your bucket limit, see [AWS service quotas](https://docs.aws.amazon.com/general/latest/gr/aws_service_limits.html) in the *AWS General Reference*\. 

**Reusing bucket names**  
If a bucket is empty, you can delete it\. After a bucket is deleted, the name becomes available for reuse\. However, after you delete the bucket, you might not be able to reuse the name for various reasons\. For example, when you delete the bucket and the name becomes available for reuse, another account might create a bucket with that name\. Additionally, some time might pass before you can reuse the name of a deleted bucket\. If you want to use the same bucket name, we recommend that you don't delete the bucket\. 

**Objects and buckets**  
There is no limit to the number of objects that you can store in a bucket\. You can store all of your objects in a single bucket, or you can organize them across several buckets\. However, you can't create a bucket from within another bucket\.

**Bucket operations**  
The high\-availability engineering of Amazon S3 is focused on get, put, list, and delete operations\. Because bucket operations work against a centralized, global resource space, it is not appropriate to create or delete buckets on the high\-availability code path of your application\. It is better to create or delete buckets in a separate initialization or setup routine that you run less often\. 

**Bucket naming and automatically created buckets**  
If your application automatically creates buckets, choose a bucket naming scheme that is unlikely to cause naming conflicts\. Ensure that your application logic will choose a different bucket name if a bucket name is already taken\.

## Rules for bucket naming<a name="bucketnamingrules"></a>

The following rules apply for naming S3 buckets:
+ Bucket names must be between 3 and 63 characters long\.
+ Bucket names can consist only of lowercase letters, numbers, dots \(\.\), and hyphens \(\-\)\.
+ Bucket names must begin and end with a letter or number\.
+ Bucket names must not be formatted as an IP address \(for example, 192\.168\.5\.4\)\.
+ Bucket names can't begin with `xn--` \(for buckets created after February 2020\)\.
+ Bucket names must be unique within a partition\. A partition is a grouping of Regions\. AWS currently has three partitions: `aws` \(Standard Regions\), `aws-cn` \(China Regions\), and `aws-us-gov` \(AWS GovCloud \[US\] Regions\)\.
+ Buckets used with Amazon S3 Transfer Acceleration can't have dots \(\.\) in their names\. For more information about transfer acceleration, see [Amazon S3 Transfer Acceleration](transfer-acceleration.md)\.

For best compatibility, we recommend that you avoid using dots \(\.\) in bucket names, except for buckets that are used only for static website hosting\. If you include dots in a bucket's name, you can't use virtual\-host\-style addressing over HTTPS, unless you perform your own certificate validation\. This is because the security certificates used for virtual hosting of buckets don't work for buckets with dots in their names\. 

This limitation doesn't affect buckets used for static website hosting, because static website hosting is only available over HTTP\. For more information about virtual\-host\-style addressing, see [Virtual hosting of buckets](VirtualHosting.md)\. For more information about static website hosting, see [Hosting a static website on Amazon S3](WebsiteHosting.md)\.

**Note**  
Before March 1, 2018, buckets created in the US East \(N\. Virginia\) Region could have names that were up to 255 characters long and included uppercase letters and underscores\. Beginning March 1, 2018, new buckets in US East \(N\. Virginia\) must conform to the same rules applied in all other Regions\.

**Example Bucket names**  
The following example bucket names are valid and follow the recommended naming guidelines:  
+ `awsexamplebucket1`
+ `log-delivery-march-2020`
+ `my-hosted-content`
The following example bucket names are valid but not recommended for uses other than static website hosting:  
+ `awsexamplewebsite.com`
+ `www.awsexamplewebsite.com`
+ `my.example.s3.bucket`
The following example bucket names are *not* valid:  
+ `aws_example_bucket` \(contains underscores\)
+ `AwsExampleBucket` \(contains uppercase letters\)
+ `aws-example-bucket-` \(ends with a hyphen\)