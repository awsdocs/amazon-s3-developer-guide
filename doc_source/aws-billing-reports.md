# AWS Billing reports for Amazon S3<a name="aws-billing-reports"></a>

Your monthly bill from AWS separates your usage information and cost by AWS service and function\. There are several AWS billing reports available, the monthly report, the cost allocation report, and detailed billing reports\. For information about how to see your billing reports, see [Viewing Your Bill](https://docs.aws.amazon.com/awsaccountbilling/latest/aboutv2/getting-viewing-bill.html) in the *AWS Billing and Cost Management User Guide*\.

You can also download a usage report that gives more detail about your Amazon S3 storage usage than the billing reports\. For more information, see [AWS usage report for Amazon S3](aws-usage-report.md)\.

The following table lists the charges associated with Amazon S3 usage\. 


**Amazon S3 usage charges**  

| Charge | Comments | 
| --- | --- | 
|  Storage  |  You pay for storing objects in your S3 buckets\. The rate you’re charged depends on your objects' size, how long you stored the objects during the month, and the storage class—STANDARD, INTELLIGENT\_TIERING, STANDARD\_IA \(IA for infrequent access\), ONEZONE\_IA, S3 Glacier, S3 Glacier Deep Archive or Reduced Redundancy Storage \(RRS\)\. For more information about storage classes, see [Amazon S3 storage classes](storage-class-intro.md)\.  | 
| Monitoring and Automation | You pay a monthly monitoring and automation fee per object stored in the INTELLIGENT\_TIERING storage class to monitor access patterns and move objects between access tiers in INTELLIGENT\_TIERING\. | 
|  Requests  |  You pay for requests, for example, GET requests, made against your S3 buckets and objects\. This includes lifecycle requests\. The rates for requests depend on what kind of request you’re making\. For information about request pricing, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.  | 
|  Retrievals  |  You pay for retrieving objects that are stored in STANDARD\_IA, ONEZONE\_IA, S3 Glacier and S3 Glacier Deep Archive storage\.  | 
|  Early Deletes  |  If you delete an object stored in INTELLIGENT\_TIERING, STANDARD\_IA, ONEZONE\_IA, S3 Glacier, or S3 Glacier Deep Archive storage before the minimum storage commitment has passed, you pay an early deletion fee for that object\.  | 
|  Storage Management  |  You pay for the storage management features \(Amazon S3 inventory, analytics, and object tagging\) that are enabled on your account’s buckets\.  | 
|  Bandwidth  |  You pay for all bandwidth into and out of Amazon S3, except for the following: [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/aws-billing-reports.html) You also pay a fee for any data transferred using Amazon S3 Transfer Acceleration\.   | 

For detailed information on Amazon S3 usage charges for storage, data transfer, and services, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/) and the [Amazon S3 FAQ](https://aws.amazon.com/s3/faqs/#billing)\.

For information on understanding codes and abbreviations used in the billing and usage reports for Amazon S3, see [Understanding your AWS billing and usage reports for Amazon S3](aws-usage-report-understand.md)\.

## More Info<a name="aws-billing-reports-more-info"></a>
+ [AWS usage report for Amazon S3](aws-usage-report.md)
+ [Using cost allocation S3 bucket tags](CostAllocTagging.md)
+ [AWS Billing and Cost Management](https://docs.aws.amazon.com/awsaccountbilling/latest/aboutv2/billing-what-is.html)
+ [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)
+ [Amazon S3 FAQ](https://aws.amazon.com/s3/faqs/#billing)
+ [S3 Glacier Pricing](https://aws.amazon.com/glacier/pricing/)