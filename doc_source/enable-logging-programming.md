# Enabling Logging Programmatically<a name="enable-logging-programming"></a>

You can enable or disable logging programmatically by using either the Amazon S3 API or the AWS SDKs\. To do so, you both enable logging on the bucket and grant the Log Delivery group permission to write logs to the target bucket\.


+ [Enabling logging](#enabling-logging-general)
+ [Granting the Log Delivery Group WRITE and READ\_ACP Permissions](#grant-log-delivery-permissions-general)
+ [Example: AWS SDK for \.NET](#enable-logging-dotnetsdk-exmaple)

## Enabling logging<a name="enabling-logging-general"></a>

To enable logging, you submit a [PUT Bucket logging](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTlogging.html) request to add the logging configuration on source bucket\. The request specifies the target bucket and, optionally, the prefix to be used with all log object keys\. The following example identifies `logbucket` as the target bucket and `logs/` as the prefix\. 

```
<BucketLoggingStatus xmlns="http://doc.s3.amazonaws.com/2006-03-01">
  <LoggingEnabled>
    <TargetBucket>logbucket</TargetBucket>
    <TargetPrefix>logs/</TargetPrefix>
  </LoggingEnabled>
</BucketLoggingStatus>
```

The log objects are written and owned by the Log Delivery account and the bucket owner is granted full permissions on the log objects\. In addition, you can optionally grant permissions to other users so that they may access the logs\. For more information, see [PUT Bucket logging](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTlogging.html)\. 

Amazon S3 also provides the [GET Bucket logging](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlogging.html) API to retrieve logging configuration on a bucket\. To delete logging configuration you send the PUT Bucket logging request with empty <BucketLoggingStatus> empty\. 

```
<BucketLoggingStatus xmlns="http://doc.s3.amazonaws.com/2006-03-01">
</BucketLoggingStatus>
```

You can use either the Amazon S3 API or the AWS SDK wrapper libraries to enable logging on a bucket\.

## Granting the Log Delivery Group WRITE and READ\_ACP Permissions<a name="grant-log-delivery-permissions-general"></a>

Amazon S3 writes the log files to the target bucket as a member of the predefined Amazon S3 group Log Delivery\. These writes are subject to the usual access control restrictions\. You will need to grant s3:GetObjectAcl and s3:PutObject permissions to this group by adding grants to the access control list \(ACL\) of the target bucket\. The Log Delivery group is represented by the following URL\. 

```
http://acs.amazonaws.com/groups/s3/LogDelivery
```

 To grant WRITE and READ\_ACP permissions, you have to add the following grants\. For information about ACLs, see [Managing Access with ACLs ](S3_ACLs_UsingACLs.md)\.

```
<Grant>
    <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  xsi:type="Group">
        <URI>http://acs.amazonaws.com/groups/s3/LogDelivery</URI> 
    </Grantee>
    <Permission>WRITE</Permission>
</Grant>
<Grant>
    <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  xsi:type="Group">
        <URI>http://acs.amazonaws.com/groups/s3/LogDelivery</URI> 
    </Grantee>
    <Permission>READ_ACP</Permission>
</Grant>
```

For examples of adding ACL grants programmatically using AWS SDKs, see [Managing ACLs Using the AWS SDK for Java](acl-using-java-sdk.md) and [Managing ACLs Using the AWS SDK for \.NET ](acl-using-dot-net-sdk.md)\.

## Example: AWS SDK for \.NET<a name="enable-logging-dotnetsdk-exmaple"></a>

The following C\# example enables logging on a bucket\. You will need to create two buckets, source bucket and target bucket\. The example first grants the Log Delivery group necessary permission to write logs to the target bucket and then enable logging on the source bucket\. For more information, see [Enabling Logging Programmatically](#enable-logging-programming)\. For instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.

**Example**  

```
using System;
using Amazon.S3;
using Amazon.S3.Model;

namespace s3.amazon.com.docsamples
{
    class ServerAccesLogging
    {
        static string sourceBucket = "*** Provide bucket name ***"; // On which to enable logging.
        static string targetBucket = "*** Provide bucket name ***"; // Where access logs can be stored.
        static string logObjectKeyPrefix = "Logs";
        static IAmazonS3 client;

        public static void Main(string[] args)
        {
            using (client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1))
            {
                Console.WriteLine("Enabling logging on source bucket...");
                try
                {
                    // Step 1 - Grant Log Delivery group permission to write log to the target bucket.
                    GrantLogDeliveryPermissionToWriteLogsInTargetBucket();
                    // Step 2 - Enable logging on the source bucket.
                    EnableDisableLogging();
                }
                catch (AmazonS3Exception amazonS3Exception)
                {
                    if (amazonS3Exception.ErrorCode != null &&
                        (amazonS3Exception.ErrorCode.Equals("InvalidAccessKeyId")
                        ||
                        amazonS3Exception.ErrorCode.Equals("InvalidSecurity")))
                    {
                        Console.WriteLine("Check the provided AWS Credentials.");
                        Console.WriteLine(
                        "To sign up for service, go to http://aws.amazon.com/s3");
                    }
                    else
                    {
                        Console.WriteLine(
                         "Error occurred. Message:'{0}' when enabling logging",
                         amazonS3Exception.Message);
                    }
                }
            }

            Console.WriteLine("Press any key to continue...");
            Console.ReadKey();
        }

        static void GrantLogDeliveryPermissionToWriteLogsInTargetBucket()
        {
            S3AccessControlList bucketACL = new S3AccessControlList();
            GetACLResponse aclResponse = client.GetACL(new GetACLRequest { BucketName = targetBucket });
            bucketACL = aclResponse.AccessControlList;
            bucketACL.AddGrant(new S3Grantee { URI = "http://acs.amazonaws.com/groups/s3/LogDelivery" }, S3Permission.WRITE);
            bucketACL.AddGrant(new S3Grantee { URI = "http://acs.amazonaws.com/groups/s3/LogDelivery" }, S3Permission.READ_ACP);
            PutACLRequest setACLRequest = new PutACLRequest
            {
                AccessControlList = bucketACL,
                BucketName = targetBucket
            };
            client.PutACL(setACLRequest);
        }

        static void EnableDisableLogging()
        {
            S3BucketLoggingConfig loggingConfig = new S3BucketLoggingConfig
            {
                TargetBucketName = targetBucket,
                TargetPrefix = logObjectKeyPrefix
            };

            // Send request.
            PutBucketLoggingRequest putBucketLoggingRequest = new PutBucketLoggingRequest
            {
                BucketName = sourceBucket,
                LoggingConfig = loggingConfig
            };
            PutBucketLoggingResponse response = client.PutBucketLogging(putBucketLoggingRequest);
        }
    }
}
```