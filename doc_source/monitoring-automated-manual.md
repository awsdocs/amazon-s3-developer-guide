# Monitoring Tools<a name="monitoring-automated-manual"></a>

AWS provides various tools that you can use to monitor Amazon S3\. You can configure some of these tools to do the monitoring for you, while some of the tools require manual intervention\. We recommend that you automate monitoring tasks as much as possible\.

## Automated Monitoring Tools<a name="monitoring-automated_tools"></a>

You can use the following automated monitoring tools to watch Amazon S3 and report when something is wrong:
+ **Amazon CloudWatch Alarms** – Watch a single metric over a time period that you specify, and perform one or more actions based on the value of the metric relative to a given threshold over a number of time periods\. The action is a notification sent to an Amazon Simple Notification Service \(Amazon SNS\) topic or Amazon EC2 Auto Scaling policy\. CloudWatch alarms do not invoke actions simply because they are in a particular state; the state must have changed and been maintained for a specified number of periods\. For more information, see [Monitoring Metrics with Amazon CloudWatch](cloudwatch-monitoring.md)\.
+ **AWS CloudTrail Log Monitoring** – Share log files between accounts, monitor CloudTrail log files in real time by sending them to CloudWatch Logs, write log processing applications in Java, and validate that your log files have not changed after delivery by CloudTrail\. For more information, see [Logging Amazon S3 API Calls by Using AWS CloudTrail](cloudtrail-logging.md)\.

## Manual Monitoring Tools<a name="monitoring-manual-tools"></a>

Another important part of monitoring Amazon S3 involves manually monitoring those items that the CloudWatch alarms don't cover\. The Amazon S3, CloudWatch, Trusted Advisor, and other AWS console dashboards provide an at\-a\-glance view of the state of your AWS environment\. You may want to enable server access logging, which tracks requests for access to your bucket\. Each access log record provides details about a single access request, such as the requester, bucket name, request time, request action, response status, and error code, if any\. For more information, see [Amazon S3 Server Access Logging](ServerLogs.md) in the *Amazon Simple Storage Service Developer Guide*\.
+ Amazon S3 dashboard shows:
  + Your buckets and the objects and properties they contain\.
+ CloudWatch home page shows:
  + Current alarms and status\.
  + Graphs of alarms and resources\.
  + Service health status\.

  In addition, you can use CloudWatch to do the following: 
  + Create [customized dashboards](http://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/CloudWatch_Dashboards.html) to monitor the services you care about\.
  + Graph metric data to troubleshoot issues and discover trends\.
  + Search and browse all your AWS resource metrics\.
  + Create and edit alarms to be notified of problems\.
+ AWS Trusted Advisor can help you monitor your AWS resources to improve performance, reliability, security, and cost effectiveness\. Four Trusted Advisor checks are available to all users; more than 50 checks are available to users with a Business or Enterprise support plan\. For more information, see [AWS Trusted Advisor](https://aws.amazon.com/premiumsupport/trustedadvisor/)\.

  Trusted Advisor has these checks that relate to Amazon S3: 
  + Checks of the logging configuration of Amazon S3 buckets\.
  + Security checks for Amazon S3 buckets that have open access permissions\.
  + Fault tolerance checks for Amazon S3 buckets that do not have versioning enabled, or have versioning suspended\.