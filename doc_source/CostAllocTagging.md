# Using Cost Allocation S3 Bucket Tags<a name="CostAllocTagging"></a>

To track the storage cost or other criteria for individual projects or groups of projects, label your Amazon S3 buckets using cost allocation tags\. A *cost allocation tag* is a key\-value pair that you associate with an S3 bucket\. After you activate cost allocation tags, AWS uses the tags to organize your resource costs on your cost allocation report\. Cost allocation tags can only be used to label buckets\. For information about tags used for labeling objects, see [Object Tagging](object-tagging.md)\.

The *cost allocation report* lists the AWS usage for your account by product category and AWS Identity and Access Management \(IAM\) user\. The report contains the same line items as the detailed billing report \(see [Understanding Your AWS Billing and Usage Reports for Amazon S3](aws-usage-report-understand.md)\) and additional columns for your tag keys\.

AWS provides two types of cost allocation tags, an AWS\-generated tag and user\-defined tags\. AWS defines, creates, and applies the AWS\-generated `createdBy` tag for you after an Amazon S3 CreateBucket event\. You define, create, and apply *user\-defined* tags to your S3 bucket\.

You must activate both types of tags separately in the Billing and Cost Management console before they can appear in your billing reports\. For more information about AWS\-generated tags, see [ AWS\-Generated Cost Allocation Tags](http://docs.aws.amazon.com/awsaccountbilling/latest/aboutv2//aws-tags.html)\. For more information about activating tags, see [Using Cost Allocation Tags](http://docs.aws.amazon.com/awsaccountbilling/latest/aboutv2//cost-alloc-tags.html) in the *AWS Billing and Cost Management User Guide*\.

A user\-defined cost allocation tag has the following components:
+ The tag key\. The tag key is the name of the tag\. For example, in the tag project/Trinity, project is the key\. The tag key is a case\-sensitive string that can contain 1 to 128 Unicode characters\. 
+ The tag value\. The tag value is a required string\. For example, in the tag project/Trinity, Trinity is the value\. The tag value is a case\-sensitive string that can contain from 0 to 256 Unicode characters\.

For details on the allowed characters for user\-defined tags and other restrictions, see [User\-Defined Tag Restrictions](http://docs.aws.amazon.com/awsaccountbilling/latest/aboutv2//allocation-tag-restrictions.html) in the *AWS Billing and Cost Management User Guide*\.

Each S3 bucket has a tag set\. A *tag set* contains all of the tags that are assigned to that bucket\. A tag set can contain as many as 10 tags, or it can be empty\. Keys must be unique within a tag set, but values in a tag set don't have to be unique\. For example, you can have the same value in tag sets named project/Trinity and cost\-center/Trinity\.

Within a bucket, if you add a tag that has the same key as an existing tag, the new value overwrites the old value\.

AWS doesn't apply any semantic meaning to your tags\. We interpret tags strictly as character strings\. 

To add, list, edit, or delete tags, you can use the Amazon S3 console, the AWS Command Line Interface \(AWS CLI\), or the Amazon S3 API\. 

For more information about creating tags, see the appropriate topic: 
+ To create tags in the console, see [How Do I View the Properties for an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/view-bucket-properties.html) in the *Amazon Simple Storage Service Console User Guide*\.
+ To create tags using the Amazon S3 API, see [PUT Bucket tagging](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTtagging.html) in the *Amazon Simple Storage Service API Reference*\.
+ To create tags using the AWS CLI, see [put\-bucket\-tagging](http://docs.aws.amazon.com/cli/latest/reference/s3api/put-bucket-tagging.html) in the AWS CLI Command Reference\.

For more information about user\-defined tags, see [User\-Defined Cost Allocation Tags](http://docs.aws.amazon.com/awsaccountbilling/latest/aboutv2//custom-tags.html) in the *AWS Billing and Cost Management User Guide*\.

## More Info<a name="CostAllocTagging-more-info"></a>
+ [ Using Cost Allocation Tags](http://docs.aws.amazon.com/awsaccountbilling/latest/aboutv2//cost-alloc-tags.html) in the *AWS Billing and Cost Management User Guide*
+ [Understanding Your AWS Billing and Usage Reports for Amazon S3](aws-usage-report-understand.md)
+ [AWS Billing Reports for Amazon S3](aws-billing-reports.md)