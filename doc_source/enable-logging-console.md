# Enabling logging using the console<a name="enable-logging-console"></a>

For information about enabling [Amazon S3 server access logging](ServerLogs.md) in the [AWS Management Console](https://console.aws.amazon.com/s3/), see [ How Do I Enable Server Access Logging for an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/server-access-logging.html) in the *Amazon Simple Storage Service Console User Guide*\. 

When you enable logging on a bucket, the console both enables logging on the source bucket and adds a grant in the target bucket's access control list \(ACL\) granting write permission to the Log Delivery group\. 

For information about how to enable logging programmatically, see [Enabling logging programmatically](enable-logging-programming.md)\.

For information about the log record format, including the list of fields and their descriptions, see [Amazon S3 Server Access Log Format](LogFormat.md)\.