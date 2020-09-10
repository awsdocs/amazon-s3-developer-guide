# Security Best Practices for Amazon S3<a name="security-best-practices"></a>

Amazon S3 provides a number of security features to consider as you develop and implement your own security policies\. The following best practices are general guidelines and don’t represent a complete security solution\. Because these best practices might not be appropriate or sufficient for your environment, treat them as helpful considerations rather than prescriptions\. 

**Topics**
+ [Amazon S3 Preventative Security Best Practices](#security-best-practices-prevent)
+ [Amazon S3 Monitoring and Auditing Best Practices](#security-best-practices-detect)

## Amazon S3 Preventative Security Best Practices<a name="security-best-practices-prevent"></a>

The following best practices for Amazon S3 can help prevent security incidents\.

**Ensure that your Amazon S3 buckets use the correct policies and are not publicly accessible**  
Unless you explicitly require anyone on the internet to be able to read or write to your S3 bucket, you should ensure that your S3 bucket is not public\. The following are some of the steps you can take:  
+ Use Amazon S3 block public access\. With Amazon S3 block public access, account administrators and bucket owners can easily set up centralized controls to limit public access to their Amazon S3 resources that are enforced regardless of how the resources are created\. For more information, see [Using Amazon S3 block public access](access-control-block-public-access.md)\.
+ Identify Amazon S3 bucket policies that allow a wildcard identity such as Principal “\*” \(which effectively means “anyone”\) or allows a wildcard action “\*” \(which effectively allows the user to perform any action in the Amazon S3 bucket\)\.
+ Similarly, note Amazon S3 bucket access control lists \(ACLs\) that provide read, write, or full\-access to “Everyone” or “Any authenticated AWS user\.” 
+ Use the `ListBuckets` API to scan all of your Amazon S3 buckets\. Then use `GetBucketAcl`, `GetBucketWebsite`, and `GetBucketPolicy` to determine whether the bucket has compliant access controls and configuration\.
+ Use [AWS Trusted Advisor](https://docs.aws.amazon.com/awssupport/latest/user/getting-started.html#trusted-advisor) to inspect your Amazon S3 implementation\.
+ Consider implementing on\-going detective controls using the [s3\-bucket\-public\-read\-prohibited](https://docs.aws.amazon.com/config/latest/developerguide/s3-bucket-public-read-prohibited.html) and [s3\-bucket\-public\-write\-prohibited](https://docs.aws.amazon.com/config/latest/developerguide/s3-bucket-public-write-prohibited.html) managed AWS Config Rules\.
For more information, see [Setting Bucket and Object Access Permissions](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/set-permissions.html) in the *Amazon Simple Storage Service Console User Guide*\. 

**Implement least privilege access**  
When granting permissions, you decide who is getting what permissions to which Amazon S3 resources\. You enable specific actions that you want to allow on those resources\. Therefore you should grant only the permissions that are required to perform a task\. Implementing least privilege access is fundamental in reducing security risk and the impact that could result from errors or malicious intent\.   
The following tools are available to implement least privilege access:  
+ [IAM user policies](https://docs.aws.amazon.com/AmazonS3/latest/dev/using-with-s3-actions.html) and [Permissions Boundaries for IAM Entities](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_boundaries.html)
+ [Amazon S3 bucket policies](https://docs.aws.amazon.com/AmazonS3/latest/dev/using-iam-policies.html)
+ [Amazon S3 access control lists \(ACLs\)](https://docs.aws.amazon.com/AmazonS3/latest/dev/S3_ACLs_UsingACLs.html)
+ [Service Control Policies](https://docs.aws.amazon.com/organizations/latest/userguide/orgs_manage_policies_scp.html)
For guidance on what to consider when choosing one or more of the preceding mechanisms, see [Introduction to managing access to Amazon S3 resources](s3-access-control.md#intro-managing-access-s3-resources)\.

**Use IAM roles for applications and AWS services that require Amazon S3 access**  
 For applications on Amazon EC2 or other AWS services to access Amazon S3 resources, they must include valid AWS credentials in their AWS API requests\. You should not store AWS credentials directly in the application or Amazon EC2 instance\. These are long\-term credentials that are not automatically rotated and could have a significant business impact if they are compromised\.  
Instead, you should use an IAM role to manage temporary credentials for applications or services that need to access Amazon S3\. When you use a role, you don't have to distribute long\-term credentials \(such as a user name and password or access keys\) to an Amazon EC2 instance or AWS service such as AWS Lambda\. The role supplies temporary permissions that applications can use when they make calls to other AWS resources\.  
For more information, see the following topics in the *IAM User Guide*:  
+ [IAM Roles](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html)
+ [Common Scenarios for Roles: Users, Applications, and Services](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_common-scenarios.html)

   

**Enable multi\-factor authentication \(MFA\) Delete**  
MFA Delete can help prevent accidental bucket deletions\. If MFA Delete is not enabled, any user with the password of a sufficiently privileged root or IAM user could permanently delete an Amazon S3 object\.  
MFA Delete requires additional authentication for either of the following operations:  
+ Changing the versioning state of your bucket
+ Permanently deleting an object version
For more information, see [MFA delete](Versioning.md#MultiFactorAuthenticationDelete)\.

**Consider encryption of data at rest**  
You have the following options for protecting data at rest in Amazon S3:  
+ **Server\-Side Encryption** – Request Amazon S3 to encrypt your object before saving it on disks in its data centers and then decrypt it when you download the objects\. Server\-side encryption can help reduce risk to your data by encrypting the data with a key that is stored in a different mechanism than the mechanism that stores the data itself\. 

  Amazon S3 provides these server\-side encryption options:
  + Server\-side encryption with Amazon S3‐managed keys \(SSE\-S3\)\.
  + Server\-side encryption with customer master keys stored in AWS Key Management Service \(SSE\-KMS\)\.
  + Server\-side encryption with customer\-provided keys \(SSE\-C\)\.

  For more information, see [Protecting data using server\-side encryption](serv-side-encryption.md)\.
+ **Client\-Side Encryption** – Encrypt data client\-side and upload the encrypted data to Amazon S3\. In this case, you manage the encryption process, the encryption keys, and related tools\. As with server\-side encryption, client\-side encryption can help reduce risk by encrypting the data with a key that is stored in a different mechanism than the mechanism that stores the data itself\. 

  Amazon S3 provides multiple client\-side encryption options\. For more information, see [Protecting data using client\-side encryption](UsingClientSideEncryption.md)\.

**Enforce encryption of data in transit**  
You can use HTTPS \(TLS\) to help prevent potential attackers from eavesdropping on or manipulating network traffic using person\-in\-the\-middle or similar attacks\. You should allow only encrypted connections over HTTPS \(TLS\) using the [aws:SecureTransport](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements_condition_operators.html#Conditions_Boolean) condition on Amazon S3 bucket policies\.  
Also consider implementing on\-going detective controls using the [s3\-bucket\-ssl\-requests\-only](https://docs.aws.amazon.com/config/latest/developerguide/s3-bucket-ssl-requests-only.html) managed AWS Config rule\. 

**Consider S3 Object Lock**  
[S3 Object Lock](https://docs.aws.amazon.com/AmazonS3/latest/dev/object-lock.html) enables you to store objects using a "Write Once Read Many" \(WORM\) model\. S3 Object Lock can help prevent accidental or inappropriate deletion of data\. For example, you could use S3 Object Lock to help protect your AWS CloudTrail logs\.

**Enable versioning**  
Versioning is a means of keeping multiple variants of an object in the same bucket\. You can use versioning to preserve, retrieve, and restore every version of every object stored in your Amazon S3 bucket\. With versioning, you can easily recover from both unintended user actions and application failures\.   
Also consider implementing on\-going detective controls using the [s3\-bucket\-versioning\-enabled](https://docs.aws.amazon.com/config/latest/developerguide/s3-bucket-versioning-enabled.html) managed AWS Config rule\.  
For more information, see [Using versioning](Versioning.md)\. 

**Consider Amazon S3 cross\-region replication**  
Although Amazon S3 stores your data across multiple geographically diverse Availability Zones by default, compliance requirements might dictate that you store data at even greater distances\. Cross\-region replication \(CRR\) allows you to replicate data between distant AWS Regions to help satisfy these requirements\. CRR enables automatic, asynchronous copying of objects across buckets in different AWS Regions\. For more information, see [Replication](replication.md)\.  
CRR requires that both source and destination S3 buckets have versioning enabled\.
Also consider implementing on\-going detective controls using the [s3\-bucket\-replication\-enabled](https://docs.aws.amazon.com/config/latest/developerguide/s3-bucket-replication-enabled.html) managed AWS Config rule\.

**Consider VPC endpoints for Amazon S3 access**  
A VPC endpoint for Amazon S3 is a logical entity within an Amazon Virtual Private Cloud \(Amazon VPC\) that allows connectivity only to Amazon S3\. You can use Amazon S3 bucket policies to control access to buckets from specific Amazon VPC endpoints, or specific VPCs\. A VPC endpoint can help prevent traffic from potentially traversing the open internet and being subject to open internet environment\.  
VPC endpoints for Amazon S3 provide multiple ways to control access to your Amazon S3 data:  
+ You can control the requests, users, or groups that are allowed through a specific VPC endpoint\.
+ You can control which VPCs or VPC endpoints have access to your S3 buckets by using S3 bucket policies\.
+ You can help prevent data exfiltration by using a VPC that does not have an internet gateway\.
For more information, see [Example Bucket Policies for VPC Endpoints for Amazon S3](example-bucket-policies-vpc-endpoint.md)\. 

## Amazon S3 Monitoring and Auditing Best Practices<a name="security-best-practices-detect"></a>

The following best practices for Amazon S3 can help detect potential security weaknesses and incidents\.

**Identify and audit all your Amazon S3 buckets**  
Identification of your IT assets is a crucial aspect of governance and security\. You need to have visibility of all your Amazon S3 resources to assess their security posture and take action on potential areas of weakness\.  
Use Tag Editor to identify security\-sensitive or audit\-sensitive resources, then use those tags when you need to search for these resources\. For more information, see [Searching for Resources to Tag](https://docs.aws.amazon.com/ARG/latest/userguide/tag-editor.html)\.   
Use Amazon S3 inventory to audit and report on the replication and encryption status of your objects for business, compliance, and regulatory needs\. For more information, see [ Amazon S3 inventory](storage-inventory.md)\.  
Create resource groups for your Amazon S3 resources\. For more information, see [What Is AWS Resource Groups?](https://docs.aws.amazon.com/ARG/latest/userguide/welcome.html) 

**Implement monitoring using AWS monitoring tools**  
Monitoring is an important part of maintaining the reliability, security, availability, and performance of Amazon S3 and your AWS solutions\. AWS provides several tools and services to help you monitor Amazon S3 and your other AWS services\. For example, you can monitor CloudWatch metrics for Amazon S3, particularly `PutRequests`, `GetRequests`, `4xxErrors`, and `DeleteRequests`\. For more information, see [Monitoring metrics with Amazon CloudWatch](cloudwatch-monitoring.md) and, [Monitoring Amazon S3](monitoring-overview.md)\.  
For a second example, see [Example: Amazon S3 Bucket Activity](http://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudwatch-alarms-for-cloudtrail.html#cloudwatch-alarms-for-cloudtrail-s3-bucket-activity)\. This example describes how to create an Amazon CloudWatch alarm that is triggered when an Amazon S3 API call is made to PUT or DELETE bucket policy, bucket lifecycle, or bucket replication, or to PUT a bucket ACL\.

**Enable Amazon S3 server access logging**  
Server access logging provides detailed records of the requests that are made to a bucket\. Server access logs can assist you in security and access audits, help you learn about your customer base, and understand your Amazon S3 bill\. For instructions on enabling server access logging, see [Amazon S3 server access logging](ServerLogs.md)\.  
Also consider implementing on\-going detective controls using the [s3\-bucket\-logging\-enabled](https://docs.aws.amazon.com/config/latest/developerguide/s3-bucket-logging-enabled.html) AWS Config managed rule\. 

**Use AWS CloudTrail**  
AWS CloudTrail provides a record of actions taken by a user, a role, or an AWS service in Amazon S3\. You can use information collected by CloudTrail to determine the request that was made to Amazon S3, the IP address from which the request was made, who made the request, when it was made, and additional details\. For example, you can identify CloudTrail entries for Put actions that impact data access, in particular `PutBucketAcl`, `PutObjectAcl`, `PutBucketPolicy`, and `PutBucketWebsite`\. When you set up your AWS account, CloudTrail is enabled by default\. You can view recent events in the CloudTrail console\. To create an ongoing record of activity and events for your Amazon S3 buckets, you can create a trail in the CloudTrail console\. For more information, see [Logging Data Events for Trails](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/logging-data-events-with-cloudtrail.html) in the *AWS CloudTrail User Guide*\.  
When you create a trail, you can configure CloudTrail to log data events\. Data events are records of resource operations performed on or within a resource\. In Amazon S3, data events record object\-level API activity for individual buckets\. CloudTrail supports a subset of Amazon S3 object\-level API operations such as `GetObject`, `DeleteObject`, and `PutObject`\. For more information about how CloudTrail works with Amazon S3, see [Logging Amazon S3 API calls using AWS CloudTrail](cloudtrail-logging.md)\. In the Amazon S3 console, you can also configure your S3 buckets to [enable object\-level logging](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-cloudtrail-events.html) for CloudTrail\.  
AWS Config provides a managed rule \(`cloudtrail-s3-dataevents-enabled`\) that you can use to confirm that at least one CloudTrail trail is logging data events for your S3 buckets\. For more information, see [https://docs.aws.amazon.com/config/latest/developerguide/cloudtrail-s3-dataevents-enabled.html](https://docs.aws.amazon.com/config/latest/developerguide/cloudtrail-s3-dataevents-enabled.html) in the *AWS Config Developer Guide*\.

**Enable AWS Config**  
Several of the best practices listed in this topic suggest creating AWS Config rules\. AWS Config enables you to assess, audit, and evaluate the configurations of your AWS resources\. AWS Config monitors resource configurations, allowing you to evaluate the recorded configurations against the desired secure configurations\. Using AWS Config, you can review changes in configurations and relationships between AWS resources, investigate detailed resource configuration histories, and determine your overall compliance against the configurations specified in your internal guidelines\. This can help you simplify compliance auditing, security analysis, change management, and operational troubleshooting\. For more information, see [Setting Up AWS Config with the Console](https://docs.aws.amazon.com/config/latest/developerguide/gs-console.html) in the *AWS Config Developer Guide*\. When specifying the resource types to record, ensure that you include Amazon S3 resources\.  
For an example of how to use AWS Config to monitor for and respond to Amazon S3 buckets that allow public access, see [How to Use AWS Config to Monitor for and Respond to Amazon S3 Buckets Allowing Public Access](https://aws.amazon.com/blogs/security/how-to-use-aws-config-to-monitor-for-and-respond-to-amazon-s3-buckets-allowing-public-access/) on the *AWS Security Blog*\. 

**Consider using Amazon Macie with Amazon S3**  
Macie uses machine learning to automatically discover, classify, and protect sensitive data in AWS\. Macie recognizes sensitive data such as personally identifiable information \(PII\) or intellectual property\. It provides you with dashboards and alerts that give visibility into how this data is being accessed or moved\. For more information, see [What Is Amazon Macie?](https://docs.aws.amazon.com/macie/latest/userguide/what-is-macie.html)

**Monitor AWS security advisories**  
You should regularly check security advisories posted in Trusted Advisor for your AWS account\. In particular, note warnings about Amazon S3 buckets with “open access permissions\.” You can do this programmatically using [describe\-trusted\-advisor\-checks](https://docs.aws.amazon.com/cli/latest/reference/support/describe-trusted-advisor-checks.html)\.  
Further, actively monitor the primary email address registered to each of your AWS accounts\. AWS will contact you, using this email address, about emerging security issues that might affect you\.  
AWS operational issues with broad impact are posted on the [AWS Service Health Dashboard](https://status.aws.amazon.com/)\. Operational issues are also posted to individual accounts via the Personal Health Dashboard\. For more information, see the [AWS Health Documentation](https://docs.aws.amazon.com/health/)\.