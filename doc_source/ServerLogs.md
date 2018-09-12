# Amazon S3 Server Access Logging<a name="ServerLogs"></a>

Server access logging provides detailed records for the requests that are made to a bucket\. Server access logs are useful for many applications\. For example, access log information can be useful in security and access audits\. It can also help you learn about your customer base and understand your Amazon S3 bill\.

**Topics**
+ [How to Enable Server Access Logging](#server-access-logging-overview)
+ [Log Object Key Format](#server-log-keyname-format)
+ [How Are Logs Delivered?](#how-logs-delivered)
+ [Best Effort Server Log Delivery](#LogDeliveryBestEffort)
+ [Bucket Logging Status Changes Take Effect Over Time](#BucketLoggingStatusChanges)
+ [Enabling Logging Using the Console](enable-logging-console.md)
+ [Enabling Logging Programmatically](enable-logging-programming.md)
+ [Server Access Log Format](LogFormat.md)
+ [Deleting Amazon S3 Log Files](deleting-log-files-lifecycle.md)

## How to Enable Server Access Logging<a name="server-access-logging-overview"></a>

To track requests for access to your bucket, you can enable server access logging\. Each access log record provides details about a single access request, such as the requester, bucket name, request time, request action, response status, and an error code, if relevant\. 

**Note**  
There is no extra charge for enabling server access logging on an Amazon S3 bucket; however, any log files that the system delivers to you accrue the usual charges for storage\. \(You can delete the log files at any time\.\) No data transfer charges are assessed for log file delivery, but access to the delivered log files is charged the same as any other data transfer\. 

 By default, logging is disabled\. When logging is enabled, logs are saved to a bucket in the same AWS Region as the source bucket\. 

To enable access logging, you must do the following: 
+  Turn on the log delivery by adding logging configuration on the bucket for which you want Amazon S3 to deliver access logs\. We refer to this bucket as the *source bucket*\. 
+  Grant the Amazon S3 Log Delivery group write permission on the bucket where you want the access logs saved\. We refer to this bucket as the *target bucket*\. 

To turn on log delivery, you provide the following logging configuration information:
+ The name of the target bucket where you want Amazon S3 to save the access logs as objects\. You can have logs delivered to any bucket that you own that is in the same Region as the source bucket, including the source bucket itself\. 

  We recommend that you save access logs in a different bucket so that you can easily manage the logs\. If you choose to save access logs in the source bucket, we recommend that you specify a prefix for all log object keys so that the object names begin with a common string and the log objects are easier to identify\. 

  When your source bucket and target bucket are the same bucket, additional logs are created for the logs that are written to the bucket\. This behavior might not be ideal for your use case because it could result in a small increase in your storage billing\. In addition, the extra logs about logs might make it harder to find the log that you're looking for\.
**Note**  
Both the source and target buckets must be owned by the same AWS account, and the buckets must both be in the same Region\.
+  \(Optional\) A prefix for Amazon S3 to assign to all log object keys\. The prefix makes it simpler for you to locate the log objects\. 

   For example, if you specify the prefix value `logs/`, each log object that Amazon S3 creates begins with the `logs/` prefix in its key, as in this example:

  ```
  logs/2013-11-01-21-32-16-E568B2907131C0C0
  ```

   The key prefix can help when you delete the logs\. For example, you can set a lifecycle configuration rule for Amazon S3 to delete objects with a specific key prefix\. For more information, see [Deleting Amazon S3 Log Files](deleting-log-files-lifecycle.md)\.
+  \(Optional\) Permissions so that others can access the generated logs\. By default, the bucket owner always has full access to the log objects\. You can optionally grant access to other users\. 

For more information about enabling server access logging, see [Enabling Logging Using the Console](enable-logging-console.md) and [Enabling Logging Programmatically](enable-logging-programming.md)\. 

## Log Object Key Format<a name="server-log-keyname-format"></a>

Amazon S3 uses the following object key format for the log objects it uploads in the target bucket:

```
TargetPrefixYYYY-mm-DD-HH-MM-SS-UniqueString 
```

 In the key, `YYYY`, `mm`, `DD`, `HH`, `MM`, and `SS` are the digits of the year, month, day, hour, minute, and seconds \(respectively\) when the log file was delivered\. 

A log file delivered at a specific time can contain records written at any point before that time\. There is no way to know whether all log records for a certain time interval have been delivered or not\. 

 The `UniqueString` component of the key is there to prevent overwriting of files\. It has no meaning, and log processing software should ignore it\. 

## How Are Logs Delivered?<a name="how-logs-delivered"></a>

Amazon S3 periodically collects access log records, consolidates the records in log files, and then uploads log files to your target bucket as log objects\. If you enable logging on multiple source buckets that identify the same target bucket, the target bucket will have access logs for all those source buckets\. However, each log object reports access log records for a specific source bucket\. 

Amazon S3 uses a special log delivery account, called the Log Delivery group, to write access logs\. These writes are subject to the usual access control restrictions\. You must grant the Log Delivery group write permission on the target bucket by adding a grant entry in the bucket's access control list \(ACL\)\. If you use the Amazon S3 console to enable logging on a bucket, the console both enables logging on the source bucket and updates the ACL on the target bucket to grant write permission to the Log Delivery group\.

## Best Effort Server Log Delivery<a name="LogDeliveryBestEffort"></a>

Server access log records are delivered on a best effort basis\. Most requests for a bucket that is properly configured for logging result in a delivered log record\. Most log records are delivered within a few hours of the time that they are recorded, but they can be delivered more frequently\. 

The completeness and timeliness of server logging is not guaranteed\. The log record for a particular request might be delivered long after the request was actually processed, or it might not be delivered at all\. The purpose of server logs is to give you an idea of the nature of traffic against your bucket\. It is rare to lose log records, but server logging is not meant to be a complete accounting of all requests\. 

It follows from the best\-effort nature of the server logging feature that the usage reports available at the AWS portal \(Billing and Cost Management reports on the [AWS Management Console](https://console.aws.amazon.com/)\) might include one or more access requests that do not appear in a delivered server log\. 

## Bucket Logging Status Changes Take Effect Over Time<a name="BucketLoggingStatusChanges"></a>

 Changes to the logging status of a bucket take time to actually affect the delivery of log files\. For example, if you enable logging for a bucket, some requests made in the following hour might be logged, while others might not\. If you change the target bucket for logging from bucket A to bucket B, some logs for the next hour might continue to be delivered to bucket A, while others might be delivered to the new target bucket B\. In all cases, the new settings eventually take effect without any further action on your part\. 