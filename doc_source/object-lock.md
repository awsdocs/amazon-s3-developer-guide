# Locking objects using S3 Object Lock<a name="object-lock"></a>

With S3 Object Lock, you can store objects using a *write\-once\-read\-many* \(WORM\) model\. You can use it to prevent an object from being deleted or overwritten for a fixed amount of time or indefinitely\. Object Lock helps you meet regulatory requirements that require WORM storage, or simply add another layer of protection against object changes and deletion\.

S3 Object Lock has been assessed by Cohasset Associates for use in environments that are subject to SEC 17a\-4, CTCC, and FINRA regulations\. For more information about how Object Lock relates to these regulations, see the [Cohasset Associates Compliance Assessment](https://d1.awsstatic.com/r2018/b/S3-Object-Lock/Amazon-S3-Compliance-Assessment.pdf)\.

Object Lock provides two ways to manage object retention: retention periods and legal holds\.
+ A *retention period* specifies a fixed period of time during which an object remains locked\. During this period, your object is WORM\-protected and can't be overwritten or deleted\.
+ A *legal hold* provides the same protection as a retention period, but it has no expiration date\. Instead, a legal hold remains in place until you explicitly remove it\. Legal holds are independent from retention periods\.

An object version can have both a retention period and a legal hold, one but not the other, or neither\. For more information, see [S3 Object Lock overview](object-lock-overview.md)\. 

Object Lock works only in versioned buckets, and retention periods and legal holds apply to individual object versions\. When you lock an object version, Amazon S3 stores the lock information in the metadata for that object version\. Placing a retention period or legal hold on an object protects only the version specified in the request\. It doesn't prevent new versions of the object from being created\. If you put an object into a bucket that has the same key name as an existing, protected object, Amazon S3 creates a new version of that object, stores it in the bucket as requested, and reports the request as completed successfully\. The existing, protected version of the object remains locked according to its retention configuration\.

To use S3 Object Lock, follow these basic steps:

1. Create a new bucket with Object Lock enabled\.

1. \(Optional\) Configure a default retention period for objects placed in the bucket\.

1. Place the objects that you want to lock in the bucket\.

1. Apply a retention period, a legal hold, or both, to the objects that you want to protect\.

For information about using Object Lock on the AWS Management Console, see [How Do I Lock an Amazon S3 Object?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/object-lock.html) in the *Amazon Simple Storage Service Console User Guide*\.

**Topics**
+ [S3 Object Lock overview](object-lock-overview.md)
+ [Managing Amazon S3 object locks](object-lock-managing.md)