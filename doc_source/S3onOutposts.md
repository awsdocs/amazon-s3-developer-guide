# Using Amazon S3 on Outposts<a name="S3onOutposts"></a>

AWS Outposts is a fully managed service that extends AWS infrastructure, services, APIs, and tools to your premises\. By providing local access to AWS managed infrastructure, AWS Outposts helps you build and run applications on\-premises using the same programming interfaces as in AWS Regions, while using local compute and storage resources for lower latency and local data processing needs\. For more information, see [What is AWS Outposts?](https://docs.aws.amazon.com/outposts/latest/userguide/what-is-outposts.htm) in the *AWS Outposts User Guide*\.

With Amazon S3 on Outposts, you can create S3 buckets on your AWS Outposts and easily store and retrieve objects on\-premises for applications that require local data access, local data processing, and data residency\. S3 on Outposts provides a new storage class, `OUTPOSTS`\. You communicate with your Outposts bucket using an access point and endpoint connection over a virtual private cloud \(VPC\)\. You can use the same APIs and features on Outposts as you do on Amazon S3, such as access policies, encryption, and tagging\. You can use S3 on Outposts through the AWS Management Console, AWS CLI, AWS SDKs, or REST API\.

S3 on Outposts is integrated with AWS DataSync\. So you can automate transferring data between your Outposts and AWS Regions, choosing what to transfer, when to transfer, and how much network bandwidth to use\. For more information about transferring data from your S3 on Outposts buckets using DataSync, see [Getting Started with AWS DataSync](https://docs.aws.amazon.com/datasync/latest/userguide/getting-started.html) in the *AWS DataSync User Guide*\.

**Topics**
+ [Getting started with Amazon S3 on Outposts](S3OutpostsGS.md)
+ [Amazon S3 on Outposts restrictions and limitations](S3OnOutpostsRestrictionsLimitations.md)
+ [Using AWS Identity and Access Management with Amazon S3 on Outposts](S3OutpostsIAM.md)
+ [Working with Amazon S3 on Outposts](WorkingWithS3Outposts.md)
+ [Amazon S3 on Outposts examples](S3OutpostsExamples.md)