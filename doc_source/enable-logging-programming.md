# Enabling logging programmatically<a name="enable-logging-programming"></a>

You can enable or disable logging programmatically by using either the Amazon S3 API or the AWS SDKs\. To do so, you both enable logging on the bucket and grant the Log Delivery group permission to write logs to the target bucket\.

**Topics**
+ [Enabling logging](#enabling-logging-general)
+ [Granting the log delivery group WRITE and READ\_ACP permissions](#grant-log-delivery-permissions-general)
+ [Example: AWS SDK for \.NET](#enable-logging-dotnetsdk-exmaple)
+ [Related resources](#enable-logging-programming-more-info)

## Enabling logging<a name="enabling-logging-general"></a>

To enable logging, you submit a [PUT Bucket logging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTlogging.html) request to add the logging configuration on the source bucket\. The request specifies the target bucket and, optionally, the prefix to be used with all log object keys\. The following example identifies `logbucket` as the target bucket and `logs/` as the prefix\. 

```
1. <BucketLoggingStatus xmlns="http://doc.s3.amazonaws.com/2006-03-01">
2.   <LoggingEnabled>
3.     <TargetBucket>logbucket</TargetBucket>
4.     <TargetPrefix>logs/</TargetPrefix>
5.   </LoggingEnabled>
6. </BucketLoggingStatus>
```

The log objects are written and owned by the Log Delivery account, and the bucket owner is granted full permissions on the log objects\. In addition, you can optionally grant permissions to other users so that they can access the logs\. For more information, see [PUT Bucket logging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTlogging.html)\. 

Amazon S3 also provides the [GET Bucket logging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlogging.html) API to retrieve logging configuration on a bucket\. To delete the logging configuration, you send the PUT Bucket logging request with an empty `BucketLoggingStatus`\. 

```
1. <BucketLoggingStatus xmlns="http://doc.s3.amazonaws.com/2006-03-01">
2. </BucketLoggingStatus>
```

You can use either the Amazon S3 API or the AWS SDK wrapper libraries to enable logging on a bucket\.

## Granting the log delivery group WRITE and READ\_ACP permissions<a name="grant-log-delivery-permissions-general"></a>

Amazon S3 writes the log files to the target bucket as a member of the predefined Amazon S3 group Log Delivery\. These writes are subject to the usual access control restrictions\. You must grant `s3:GetObjectAcl` and `s3:PutObject` permissions to this group by adding grants to the access control list \(ACL\) of the target bucket\. The Log Delivery group is represented by the following URL\. 

```
1. http://acs.amazonaws.com/groups/s3/LogDelivery
```

To grant `WRITE` and `READ_ACP` permissions, add the following grants\. For information about ACLs, see [Managing Access with ACLs](S3_ACLs_UsingACLs.md)\.

```
 1. <Grant>
 2.     <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  xsi:type="Group">
 3.         <URI>http://acs.amazonaws.com/groups/s3/LogDelivery</URI> 
 4.     </Grantee>
 5.     <Permission>WRITE</Permission>
 6. </Grant>
 7. <Grant>
 8.     <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  xsi:type="Group">
 9.         <URI>http://acs.amazonaws.com/groups/s3/LogDelivery</URI> 
10.     </Grantee>
11.     <Permission>READ_ACP</Permission>
12. </Grant>
```

For examples of adding ACL grants programmatically using the AWS SDKs, see [Managing ACLs Using the AWS SDK for JavaConfiguring ACL Grants on an Existing Object](acl-using-java-sdk.md) and [Managing ACLs Using the AWS SDK for \.NET ](acl-using-dot-net-sdk.md)\.

## Example: AWS SDK for \.NET<a name="enable-logging-dotnetsdk-exmaple"></a>

The following C\# example enables logging on a bucket\. You need to create two buckets, a source bucket and a target bucket\. The example first grants the Log Delivery group the necessary permission to write logs to the target bucket and then enables logging on the source bucket\. For more information, see [Enabling logging programmatically](#enable-logging-programming)\. For instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.

**Example**  

```
 1. using Amazon;
 2. using Amazon.S3;
 3. using Amazon.S3.Model;
 4. using System;
 5. using System.Threading.Tasks;
 6. 
 7. namespace Amazon.DocSamples.S3
 8. {
 9.     class ServerAccesLoggingTest
10.     {
11.         private const string bucketName = "*** bucket name for which to enable logging ***"; 
12.         private const string targetBucketName = "*** bucket name where you want access logs stored ***"; 
13.         private const string logObjectKeyPrefix = "Logs";
14.         // Specify your bucket region (an example region is shown).
15.         private static readonly RegionEndpoint bucketRegion = RegionEndpoint.USWest2;
16.         private static IAmazonS3 client;
17. 
18.         public static void Main()
19.         {
20.             client = new AmazonS3Client(bucketRegion);
21.             EnableLoggingAsync().Wait();
22.         }
23. 
24.         private static async Task EnableLoggingAsync()
25.         {
26.             try
27.             {
28.                 // Step 1 - Grant Log Delivery group permission to write log to the target bucket.
29.                 await GrantPermissionsToWriteLogsAsync();
30.                 // Step 2 - Enable logging on the source bucket.
31.                 await EnableDisableLoggingAsync();
32.             }
33.             catch (AmazonS3Exception e)
34.             {
35.                 Console.WriteLine("Error encountered on server. Message:'{0}' when writing an object", e.Message);
36.             }
37.             catch (Exception e)
38.             {
39.                 Console.WriteLine("Unknown encountered on server. Message:'{0}' when writing an object", e.Message);
40.             }
41.         }
42. 
43.         private static async Task GrantPermissionsToWriteLogsAsync()
44.         {
45.             var bucketACL = new S3AccessControlList();
46.             var aclResponse = client.GetACL(new GetACLRequest { BucketName = targetBucketName });
47.             bucketACL = aclResponse.AccessControlList;
48.             bucketACL.AddGrant(new S3Grantee { URI = "http://acs.amazonaws.com/groups/s3/LogDelivery" }, S3Permission.WRITE);
49.             bucketACL.AddGrant(new S3Grantee { URI = "http://acs.amazonaws.com/groups/s3/LogDelivery" }, S3Permission.READ_ACP);
50.             var setACLRequest = new PutACLRequest
51.             {
52.                 AccessControlList = bucketACL,
53.                 BucketName = targetBucketName
54.             };
55.             await client.PutACLAsync(setACLRequest);
56.         }
57. 
58.         private static async Task EnableDisableLoggingAsync()
59.         {
60.             var loggingConfig = new S3BucketLoggingConfig
61.             {
62.                 TargetBucketName = targetBucketName,
63.                 TargetPrefix = logObjectKeyPrefix
64.             };
65. 
66.             // Send request.
67.             var putBucketLoggingRequest = new PutBucketLoggingRequest
68.             {
69.                 BucketName = bucketName,
70.                 LoggingConfig = loggingConfig
71.             };
72.             await client.PutBucketLoggingAsync(putBucketLoggingRequest);
73.         }
74.     }
75. }
```

## Related resources<a name="enable-logging-programming-more-info"></a>
+ [Amazon S3 server access logging](ServerLogs.md)
+ [AWS::S3::Bucket](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-s3-bucket.html) in the *AWS CloudFormation User Guide*