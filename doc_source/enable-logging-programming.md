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
 1. using Amazon.S3;
 2. using Amazon.S3.Model;
 3. using System;
 4. using System.Threading.Tasks;
 5. 
 6. namespace Amazon.DocSamples.S3
 7. {
 8.     class ServerAccesLoggingTest
 9.     {
10.         private const string bucketName = "*** bucket name for which to enable logging ***"; 
11.         private const string targetBucketName = "*** bucket name where you want access logs stored ***"; 
12.         private const string logObjectKeyPrefix = "Logs";
13.         // Specify your bucket region (an example region is shown).
14.         private static readonly RegionEndpoint bucketRegion = RegionEndpoint.USWest2;
15.         private static IAmazonS3 client;
16. 
17.         public static void Main()
18.         {
19.             client = new AmazonS3Client(bucketRegion);
20.             EnableLoggingAsync().Wait();
21.         }
22. 
23.         private static async Task EnableLoggingAsync()
24.         {
25.             try
26.             {
27.                 // Step 1 - Grant Log Delivery group permission to write log to the target bucket.
28.                 await GrantPermissionsToWriteLogsAsync();
29.                 // Step 2 - Enable logging on the source bucket.
30.                 await EnableDisableLoggingAsync();
31.             }
32.             catch (AmazonS3Exception e)
33.             {
34.                 Console.WriteLine("Error encountered on server. Message:'{0}' when writing an object", e.Message);
35.             }
36.             catch (Exception e)
37.             {
38.                 Console.WriteLine("Unknown encountered on server. Message:'{0}' when writing an object", e.Message);
39.             }
40.         }
41. 
42.         private static async Task GrantPermissionsToWriteLogsAsync()
43.         {
44.             var bucketACL = new S3AccessControlList();
45.             var aclResponse = client.GetACL(new GetACLRequest { BucketName = targetBucketName });
46.             bucketACL = aclResponse.AccessControlList;
47.             bucketACL.AddGrant(new S3Grantee { URI = "http://acs.amazonaws.com/groups/s3/LogDelivery" }, S3Permission.WRITE);
48.             bucketACL.AddGrant(new S3Grantee { URI = "http://acs.amazonaws.com/groups/s3/LogDelivery" }, S3Permission.READ_ACP);
49.             var setACLRequest = new PutACLRequest
50.             {
51.                 AccessControlList = bucketACL,
52.                 BucketName = targetBucketName
53.             };
54.             await client.PutACLAsync(setACLRequest);
55.         }
56. 
57.         private static async Task EnableDisableLoggingAsync()
58.         {
59.             var loggingConfig = new S3BucketLoggingConfig
60.             {
61.                 TargetBucketName = targetBucketName,
62.                 TargetPrefix = logObjectKeyPrefix
63.             };
64. 
65.             // Send request.
66.             var putBucketLoggingRequest = new PutBucketLoggingRequest
67.             {
68.                 BucketName = bucketName,
69.                 LoggingConfig = loggingConfig
70.             };
71.             await client.PutBucketLoggingAsync(putBucketLoggingRequest);
72.         }
73.     }
74. }
```

## Related resources<a name="enable-logging-programming-more-info"></a>
+ [Amazon S3 server access logging](ServerLogs.md)
+ [AWS::S3::Bucket](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-s3-bucket.html) in the *AWS CloudFormation User Guide*