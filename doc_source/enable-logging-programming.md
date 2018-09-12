# Enabling Logging Programmatically<a name="enable-logging-programming"></a>

You can enable or disable logging programmatically by using either the Amazon S3 API or the AWS SDKs\. To do so, you both enable logging on the bucket and grant the Log Delivery group permission to write logs to the target bucket\.

**Topics**
+ [Enabling Logging](#enabling-logging-general)
+ [Granting the Log Delivery Group WRITE and READ\_ACP Permissions](#grant-log-delivery-permissions-general)
+ [Example: AWS SDK for \.NET](#enable-logging-dotnetsdk-exmaple)
+ [More Info](#enable-logging-programming-more-info)

## Enabling Logging<a name="enabling-logging-general"></a>

To enable logging, you submit a [PUT Bucket logging](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTlogging.html) request to add the logging configuration on the source bucket\. The request specifies the target bucket and, optionally, the prefix to be used with all log object keys\. The following example identifies `logbucket` as the target bucket and `logs/` as the prefix\. 

```
<BucketLoggingStatus xmlns="http://doc.s3.amazonaws.com/2006-03-01">
  <LoggingEnabled>
    <TargetBucket>logbucket</TargetBucket>
    <TargetPrefix>logs/</TargetPrefix>
  </LoggingEnabled>
</BucketLoggingStatus>
```

The log objects are written and owned by the Log Delivery account, and the bucket owner is granted full permissions on the log objects\. In addition, you can optionally grant permissions to other users so that they can access the logs\. For more information, see [PUT Bucket logging](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTlogging.html)\. 

Amazon S3 also provides the [GET Bucket logging](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlogging.html) API to retrieve logging configuration on a bucket\. To delete the logging configuration, you send the PUT Bucket logging request with an empty `BucketLoggingStatus`\. 

```
<BucketLoggingStatus xmlns="http://doc.s3.amazonaws.com/2006-03-01">
</BucketLoggingStatus>
```

You can use either the Amazon S3 API or the AWS SDK wrapper libraries to enable logging on a bucket\.

## Granting the Log Delivery Group WRITE and READ\_ACP Permissions<a name="grant-log-delivery-permissions-general"></a>

Amazon S3 writes the log files to the target bucket as a member of the predefined Amazon S3 group Log Delivery\. These writes are subject to the usual access control restrictions\. You must grant `s3:GetObjectAcl` and `s3:PutObject` permissions to this group by adding grants to the access control list \(ACL\) of the target bucket\. The Log Delivery group is represented by the following URL\. 

```
http://acs.amazonaws.com/groups/s3/LogDelivery
```

 To grant `WRITE` and `READ_ACP` permissions, add the following grants\. For information about ACLs, see [Managing Access with ACLs](S3_ACLs_UsingACLs.md)\.

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

For examples of adding ACL grants programmatically using the AWS SDKs, see [Managing ACLs Using the AWS SDK for JavaConfiguring ACL Grants on an Existing Object](acl-using-java-sdk.md) and [Managing ACLs Using the AWS SDK for \.NET ](acl-using-dot-net-sdk.md)\.

## Example: AWS SDK for \.NET<a name="enable-logging-dotnetsdk-exmaple"></a>

The following C\# example enables logging on a bucket\. You need to create two buckets, a source bucket and a target bucket\. The example first grants the Log Delivery group the necessary permission to write logs to the target bucket and then enables logging on the source bucket\. For more information, see [Enabling Logging Programmatically](#enable-logging-programming)\. For instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.

**Example**  

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

﻿using Amazon.S3;
using Amazon.S3.Model;
using System;
using System.Threading.Tasks;

namespace Amazon.DocSamples.S3
{
    class ServerAccesLoggingTest
    {
        private const string bucketName = "*** bucket name for which to enable logging ***"; 
        private const string targetBucketName = "*** bucket name where you want access logs stored ***"; 
        private const string logObjectKeyPrefix = "Logs";
        // Specify your bucket region (an example region is shown).
        private static readonly RegionEndpoint bucketRegion = RegionEndpoint.USWest2;
        private static IAmazonS3 client;

        public static void Main()
        {
            client = new AmazonS3Client(bucketRegion);
            EnableLoggingAsync().Wait();
        }

        private static async Task EnableLoggingAsync()
        {
            try
            {
                // Step 1 - Grant Log Delivery group permission to write log to the target bucket.
                await GrantPermissionsToWriteLogsAsync();
                // Step 2 - Enable logging on the source bucket.
                await EnableDisableLoggingAsync();
            }
            catch (AmazonS3Exception e)
            {
                Console.WriteLine("Error encountered on server. Message:'{0}' when writing an object", e.Message);
            }
            catch (Exception e)
            {
                Console.WriteLine("Unknown encountered on server. Message:'{0}' when writing an object", e.Message);
            }
        }

        private static async Task GrantPermissionsToWriteLogsAsync()
        {
            var bucketACL = new S3AccessControlList();
            var aclResponse = client.GetACL(new GetACLRequest { BucketName = targetBucketName });
            bucketACL = aclResponse.AccessControlList;
            bucketACL.AddGrant(new S3Grantee { URI = "http://acs.amazonaws.com/groups/s3/LogDelivery" }, S3Permission.WRITE);
            bucketACL.AddGrant(new S3Grantee { URI = "http://acs.amazonaws.com/groups/s3/LogDelivery" }, S3Permission.READ_ACP);
            var setACLRequest = new PutACLRequest
            {
                AccessControlList = bucketACL,
                BucketName = targetBucketName
            };
            await client.PutACLAsync(setACLRequest);
        }

        private static async Task EnableDisableLoggingAsync()
        {
            var loggingConfig = new S3BucketLoggingConfig
            {
                TargetBucketName = targetBucketName,
                TargetPrefix = logObjectKeyPrefix
            };

            // Send request.
            var putBucketLoggingRequest = new PutBucketLoggingRequest
            {
                BucketName = bucketName,
                LoggingConfig = loggingConfig
            };
            await client.PutBucketLoggingAsync(putBucketLoggingRequest);
        }
    }
}
```

## More Info<a name="enable-logging-programming-more-info"></a>
+ [Amazon S3 Server Access Logging](ServerLogs.md)
+ [AWS::S3::Bucket](http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-s3-bucket.html) in the AWS CloudFormation User Guide