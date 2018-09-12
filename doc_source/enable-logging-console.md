# Enabling Logging Using the Console<a name="enable-logging-console"></a>

For information about enabling [Amazon S3 Server Access Logging](ServerLogs.md) in the [AWS Management Console](https://console.aws.amazon.com/s3/), see [ How Do I Enable Server Access Logging for an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/server-access-logging.html) in the *Amazon Simple Storage Service Console User Guide*\. 

When you enable logging on a bucket, the console both enables logging on the source bucket and adds a grant in the target bucket's access control list \(ACL\) granting write permission to the Log Delivery group\. 

For information about how to enable logging programmatically, see [Enabling Logging Programmatically](enable-logging-programming.md)\.

For information about the log record format, including the list of fields and their descriptions, see [Server Access Log Format](LogFormat.md)\.