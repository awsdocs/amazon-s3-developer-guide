# Managing S3 Object Lock Retention Dates<a name="batch-ops-retention-date"></a>

You can use S3 Object Lock with retention dates for your object using two modes; *governance* mode and *compliance* mode\. These retention modes apply different levels of protection to your objects\. You can apply either retention mode to any object version\. Retention dates, like legal holds, prevent an object from being overwritten or deleted\. Amazon S3 stores the *Retain Until Date* specified in the object’s metadata and protects the specified version of the object version until the retention period expires\.

Using S3 Batch Operations with Object Lock you can manage retention dates of many Amazon S3 objects at once\. You specify the list of target objects in your manifest and submit it to Batch Operations for completion\. For more information, see S3 Object Lock [Retention periods](object-lock-overview.md#object-lock-retention-periods)\. 

Your S3 Batch Operations job with retention dates will run *until completion, until cancelation, or until a failure state* is reached\. You should use S3 Batch Operations and the S3 Object Lock retention when you want to add, change, or remove the retention date for many objects with a single request\. Batch Operations verifies that Object Lock is enabled on your bucket before processing any keys in the manifest\. To perform the operations and validation, Batch Operations needs `s3:GetBucketObjectLockConfiguration`, and `s3:PutObjectRetention` in an IAM role allowing Batch Operations to call Object Lock on your behalf\. For more information on how to use this operation with the REST API, please see `S3PutObjectRetention` in the [CreateJob](https://docs.aws.amazon.com/AmazonS3/latest/API/API_control_CreateJob.html) in the Amazon Simple Storage Service API Reference\. For an AWS Command Line Interface example of using this operation, see [Using S3 Batch Operations with S3 Object Lock retention](batch-ops-examples-java.md#batch-ops-examples-java-object-lock-retention)\. For an AWS SDK for Java example, see [Example: Using S3 Batch Operations with S3 Object Lock retention](batch-ops-examples-cli.md#batch-ops-cli-object-lock-retention-example)\. 

## Restrictions and Limitations<a name="batch-ops-retention-date-restrictions"></a>
+ S3 Batch Operations will not make any bucket level changes\.
+ Versioning and S3 Object Lock need to be configured on the bucket where the job is performed\.
+ All objects listed in the manifest must be in the same bucket\.
+ The operation will work on the latest version of the object unless a version is explicitly specified in the manifest\.
+ You will need `s3:PutObjectRetention` permission in your IAM role to use this\.
+ `s3:GetBucketObjectLockConfiguration` IAM permission is required to confirm that Object Lock is enabled for the S3 bucket\. 
+ You can only extend the retention period of objects with `COMPLIANCE` mode retention dates applied, it can not be shortened\.