# Getting started with Amazon S3 on Outposts<a name="S3OutpostsGS"></a>

With Amazon S3 on Outposts, you can use the Amazon S3 APIs and features, such as object storage, access policies, encryption, and tagging, on AWS Outposts as you do on Amazon S3\. For information about AWS Outposts, see [ What is AWS Outposts?](https://docs.aws.amazon.com/outposts/latest/userguide/what-is-outposts.html) in the *AWS Outposts User Guide*\. 

**Topics**
+ [Order an Outpost](#OrderOutposts)
+ [Setting up S3 on Outposts](#SettingUpS3Outposts)

## Ordering your AWS Outpost<a name="OrderOutposts"></a>

To get started with Amazon S3 on Outposts, you need an Outpost with Amazon S3 capacity deployed at your facility\. For information about options for ordering an Outpost and S3 capacity, see [AWS Outposts](http://aws.amazon.com/outposts)\. For specifications, restrictions, and limitations, see [Amazon S3 on Outposts restrictions and limitations](S3OnOutpostsRestrictionsLimitations.md)\.

### Do you need a new AWS Outpost?<a name="SettingUpS3OutpostsNewOutpost"></a>

If you need to order a new Outpost with S3 capacity, see [AWS Outposts pricing](http://aws.amazon.com/outposts/pricing/) to understand the capacity option for Amazon EC2, Amazon EBS, and Amazon S3\. Current options for Amazon S3 on Outposts capacity are 48 TB and 96 TB\. 

After you select your configuration, follow the steps in [Create an Outpost and order Outpost capacity](https://docs.aws.amazon.com/outposts/latest/userguide/order-outpost-capacity.html) in the *AWS Outposts User Guide\.* 

### Do you already have an AWS Outpost?<a name="SettingUpS3OutpostsExistingOutpost"></a>

If AWS Outposts is already on your site, depending on your current Outpost configuration and storage capacity, you may be able to add Amazon S3 storage to an existing Outpost, or you may need to work with your AWS account team to add additional hardware to support Amazon S3 on Outposts\.

## Setting up S3 on Outposts<a name="SettingUpS3Outposts"></a>

After your S3 on Outposts capacity is provisioned, you can create buckets and S3 access points on your Outpost using the [ AWS Outposts console](https://console.aws.amazon.com/outposts), the Amazon S3 on Outposts REST API, the AWS Command Line Interface \(AWS CLI\), or the AWS SDKs\. You can then use APIs to store and retrieve objects from these buckets\. You can also use AWS DataSync to transfer data between your Outpost and the AWS Region\. For more information, see [Working with Amazon S3 on Outposts](WorkingWithS3Outposts.md)\.

You can manage your Amazon S3 storage on Outposts using the same services that you use in\-Region today\. These include AWS Identity and Access Management \(IAM\) and Amazon S3 Access Points to control access to objects and buckets, Amazon CloudWatch to monitor operational health, and AWS CloudTrail to track and report on object\-level and bucket\-level activity\.

After AWS enables your S3 on Outposts capacity, you can access S3 on Outposts using the AWS Outposts or Amazon S3 consoles, the Amazon S3 REST API, the AWS CLI, or the AWS SDKs\.