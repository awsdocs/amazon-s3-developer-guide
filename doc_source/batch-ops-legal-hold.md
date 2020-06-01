# Managing S3 Object Lock Legal Hold<a name="batch-ops-legal-hold"></a>

S3 Object Lock enables you to place a legal hold on an object version\. Like setting a retention period, a legal hold prevents an object version from being overwritten or deleted\. However, a legal hold doesn't have an associated retention period and remains in effect until removed\. S3 Batch Operations with object lock allows you to add legal holds to many Amazon S3 objects at once\. You can do this by listing the target objects in your manifest and submitting that list to Batch Operations\. Your S3 Batch Operations job with Object Lock legal hold will run until completion, until cancelation, or until a failure state is reached\. 

S3 Batch Operations verifies that Object Lock is enabled on your S3 bucket before processing any keys in the manifest\. To perform the object operations and bucket level validation, S3 Batch Operations needs `s3:GetBucketObjectLockConfiguration`, and `s3:PutObjectLegalHold` in an IAM role allowing S3 Batch Operations to call S3 Object Lock on your behalf\. 

When you create the S3 Batch Operations job to remove the legal hold, you’ll just need to specify *Off* as the legal hold status\. For more information, see [Managing Amazon S3 object locks](object-lock-managing.md)\.

For more information on how to use this operation with the REST API, please see `S3PutObjectRetention` in the [CreateJob](https://docs.aws.amazon.com/AmazonS3/latest/API/API_control_CreateJob.html) in the Amazon Simple Storage Service API Reference\. For an AWS Command Line Interface example of using this operation, see [Example: Using S3 Batch Operations with S3 Object Lock legal hold](batch-ops-examples-java.md#batch-ops-examples-java-object-lock-legalhold)\. For an AWS SDK for Java example, see [Example: Using S3 Batch Operations with S3 Object Lock legal hold](batch-ops-examples-cli.md#batch-ops-cli-object-lock-legalhold-example)\.

## Restrictions and Limitations<a name="batch-ops-legal-hold-restrictions"></a>
+ S3 Batch Operations will not make any bucket level changes\.
+ All objects listed in the manifest must be in the same bucket\.
+ Versioning and S3 Object Lock need to be configured on the bucket where the job is performed\.
+ The operation will work on latest version of the object unless a version is explicitly specified in the manifest\.
+ `s3:PutObjectLegalHold` permission is required in your IAM role to add or remove legal hold from objects\.
+ `s3:GetBucketObjectLockConfiguration` IAM permission is required to confirm that S3 Object Lock is enabled for the S3 bucket\. 