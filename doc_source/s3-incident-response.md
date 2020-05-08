# Logging and monitoring in Amazon S3<a name="s3-incident-response"></a>

Monitoring is an important part of maintaining the reliability, availability, and performance of Amazon S3 and your AWS solutions\. You should collect monitoring data from all of the parts of your AWS solution so that you can more easily debug a multi\-point failure if one occurs\. AWS provides several tools for monitoring your Amazon S3 resources and responding to potential incidents:

**Amazon CloudWatch Alarms**  
Using Amazon CloudWatch alarms, you watch a single metric over a time period that you specify\. If the metric exceeds a given threshold, a notification is sent to an Amazon SNS topic or AWS Auto Scaling policy\. CloudWatch alarms do not invoke actions because they are in a particular state\. Rather the state must have changed and been maintained for a specified number of periods\. For more information, see [Monitoring metrics with Amazon CloudWatch](cloudwatch-monitoring.md)\.

**AWS CloudTrail Logs**  
CloudTrail provides a record of actions taken by a user, role, or an AWS service in Amazon S3\. Using the information collected by CloudTrail, you can determine the request that was made to Amazon S3, the IP address from which the request was made, who made the request, when it was made, and additional details\. For more information, see [Logging Amazon S3 API calls using AWS CloudTrail](cloudtrail-logging.md)\.

**Amazon S3 Access Logs**  
Server access logs provide detailed records about requests that are made to a bucket\. Server access logs are useful for many applications\. For example, access log information can be useful in security and access audits\. For more information, see [Amazon S3 server access logging](ServerLogs.md)\.

**AWS Trusted Advisor**  
Trusted Advisor draws upon best practices learned from serving hundreds of thousands of AWS customers\. Trusted Advisor inspects your AWS environment and then makes recommendations when opportunities exist to save money, improve system availability and performance, or help close security gaps\. All AWS customers have access to five Trusted Advisor checks\. Customers with a Business or Enterprise support plan can view all Trusted Advisor checks\.   
Trusted Advisor has the following Amazon S3\-related checks:  
+ Logging configuration of Amazon S3 buckets\.
+ Security checks for Amazon S3 buckets that have open access permissions\.
+ Fault tolerance checks for Amazon S3 buckets that don't have versioning enabled, or have versioning suspended\.
For more information, see [AWS Trusted Advisor](https://docs.aws.amazon.com/awssupport/latest/user/getting-started.html#trusted-advisor) in the *AWS Support User Guide*\.

The following security best practices also address logging and monitoring:
+ [Identify and audit all your Amazon S3 buckets](security-best-practices.md#audit)
+ [Implement monitoring using AWS monitoring tools](security-best-practices.md#tools)
+ [Enable AWS Config](security-best-practices.md#config)
+ [Enable Amazon S3 server access logging](security-best-practices.md#serverlog)
+ [Use AWS CloudTrail](security-best-practices.md#objectlog)
+ [Monitor AWS security advisories](security-best-practices.md#advisories)