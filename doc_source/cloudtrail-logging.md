# Logging Amazon S3 API Calls by Using AWS CloudTrail<a name="cloudtrail-logging"></a>

Amazon S3 is integrated with AWS CloudTrail, a service that provides a record of actions taken by a user, role, or an AWS service in Amazon S3\. CloudTrail captures a subset of API calls for Amazon S3 as events, including calls from the Amazon S3 console and from code calls to the Amazon S3 APIs\. If you create a trail, you can enable continuous delivery of CloudTrail events to an Amazon S3 bucket, including events for Amazon S3\. If you don't configure a trail, you can still view the most recent events in the CloudTrail console in **Event history**\. Using the information collected by CloudTrail, you can determine the request that was made to Amazon S3, the IP address from which the request was made, who made the request, when it was made, and additional details\. 

To learn more about CloudTrail, including how to configure and enable it, see the [AWS CloudTrail User Guide](http://docs.aws.amazon.com/awscloudtrail/latest/userguide/)\.

## Amazon S3 Information in CloudTrail<a name="cloudtrail-logging-s3-info"></a>

CloudTrail is enabled on your AWS account when you create the account\. When supported event activity occurs in Amazon S3, that activity is recorded in a CloudTrail event along with other AWS service events in **Event history**\. You can view, search, and download recent events in your AWS account\. For more information, see [Viewing Events with CloudTrail Event History](http://docs.aws.amazon.com/awscloudtrail/latest/userguide/view-cloudtrail-events.html)\. 

For an ongoing record of events in your AWS account, including events for Amazon S3, create a trail\. A trail enables CloudTrail to deliver log files to an Amazon S3 bucket\. By default, when you create a trail in the console, the trail applies to all regions\. The trail logs events from all regions in the AWS partition and delivers the log files to the Amazon S3 bucket that you specify\. Additionally, you can configure other AWS services to further analyze and act upon the event data collected in CloudTrail logs\. For more information, see: 
+ [Overview for Creating a Trail](http://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-create-and-update-a-trail.html)
+ [CloudTrail Supported Services and Integrations](http://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-aws-service-specific-topics.html#cloudtrail-aws-service-specific-topics-integrations)
+ [Configuring Amazon SNS Notifications for CloudTrail](http://docs.aws.amazon.com/awscloudtrail/latest/userguide/getting_notifications_top_level.html)
+ [Receiving CloudTrail Log Files from Multiple Regions](http://docs.aws.amazon.com/awscloudtrail/latest/userguide/receive-cloudtrail-log-files-from-multiple-regions.html) and [Receiving CloudTrail Log Files from Multiple Accounts](http://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-receive-logs-from-multiple-accounts.html)

Every event or log entry contains information about who generated the request\. The identity information helps you determine the following: 
+ Whether the request was made with root or IAM user credentials\.
+ Whether the request was made with temporary security credentials for a role or federated user\.
+ Whether the request was made by another AWS service\.

For more information, see the [CloudTrail userIdentity Element](http://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-event-reference-user-identity.html)\.

You can store your log files in your bucket for as long as you want, but you can also define Amazon S3 lifecycle rules to archive or delete log files automatically\. By default, your log files are encrypted by using Amazon S3 server\-side encryption \(SSE\)\.

### Amazon S3 Bucket\-Level Actions Tracked by CloudTrail Logging<a name="cloudtrail-bucket-level-tracking"></a>

By default, CloudTrail logs bucket\-level actions\. Amazon S3 records are written together with other AWS service records in a log file\. CloudTrail determines when to create and write to a new file based on a time period and file size\. 

The tables in this section list the Amazon S3 bucket\-level actions that are supported for logging by CloudTrail\.


**Amazon S3 Bucket\-Level Actions Tracked by CloudTrail Logging**  

| REST API Name | API Event Name Used in CloudTrail Log | 
| --- | --- | 
|  [DELETE Bucket](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETE.html)  | DeleteBucket | 
|  [DELETE Bucket cors](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEcors.html)  | DeleteBucketCors | 
|  [DELETE Bucket encryption](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEencryption.html)  | DeleteBucketEncryption | 
|  [DELETE Bucket lifecycle](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETElifecycle.html)  | DeleteBucketLifecycle | 
|  [DELETE Bucket policy](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEpolicy.html)  | DeleteBucketPolicy | 
|  [DELETE Bucket replication](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEreplication.html)  | DeleteBucketReplication  | 
|  [DELETE Bucket tagging](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEtagging.html)  | DeleteBucketTagging | 
|  [DELETE Bucket website](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEwebsite.html)  | DeleteBucketWebsite | 
|  [GET Bucket acl](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETacl.html)  | GetBucketAcl | 
|  [GET Bucket cors](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETcors.html)  | GetBucketCors | 
|  [GET Bucket encryption](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETencryption.html)  | GetBucketEncryption | 
|  [GET Bucket lifecycle](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlifecycle.html)  | GetBucketLifecycle  | 
|  [GET Bucket location](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlocation.html)  | GetBucketLocation  | 
|  [GET Bucket logging](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlogging.html)  | GetBucketLogging  | 
|  [GET Bucket notification](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETnotification.html)  | GetBucketNotification | 
|  [GET Bucket policy](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETpolicy.html)  | GetBucketPolicy | 
|  [GET Bucket replication](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETreplication.html)  | GetBucketReplication | 
|  [GET Bucket requestPayment](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTrequestPaymentGET.html)  | GetBucketRequestPay  | 
|  [GET Bucket tagging](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETtagging.html)  | GetBucketTagging  | 
|  [GET Bucket versioning](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETversioningStatus.html)  | GetBucketVersioning  | 
|  [GET Bucket website](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETwebsite.html)  | GetBucketWebsite  | 
|  [GET Service \(List all buckets\)](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTServiceGET.html)  | ListBuckets  | 
|  [PUT Bucket](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUT.html)  | CreateBucket  | 
|  [PUT Bucket acl](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTacl.html)  | PutBucketAcl  | 
|  [PUT Bucket cors](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTcors.html)  | PutBucketCors  | 
|  [PUT Bucket encryption](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTencryption.html)  | PutBucketEncryption | 
|  [PUT Bucket lifecycle](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTlifecycle.html)  | PutBucketLifecycle  | 
|  [PUT Bucket logging](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTlogging.html)  | PutBucketLogging | 
|  [PUT Bucket notification](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTnotification.html)  | PutBucketNotification  | 
|  [PUT Bucket policy](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTpolicy.html)  | PutBucketPolicy  | 
|  [PUT Bucket replication](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTreplication.html)  | PutBucketReplication  | 
|  [PUT Bucket requestPayment](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTrequestPaymentPUT.html)  | PutBucketRequestPay  | 
|  [PUT Bucket tagging](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTtagging.html)  | PutBucketTagging  | 
|  [PUT Bucket versioning](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTVersioningStatus.html)  | PutBucketVersioning | 
|  [PUT Bucket website](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTwebsite.html)  | PutBucketWebsite  | 

In addition to these API operations, you can also use the [OPTIONS object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTOPTIONSobject.html) object\-level action\. This action is treated like a bucket\-level action in CloudTrail logging because the action checks the cors configuration of a bucket\.

### Amazon S3 Object\-Level Actions Tracked by CloudTrail Logging<a name="cloudtrail-object-level-tracking"></a>

You can also get CloudTrail logs for object\-level Amazon S3 actions\. To do this, specify the Amazon S3 object for your trail\. When an object\-level action occurs in your account, CloudTrail evaluates your trail settings\. If the event matches the object that you specified in a trail, the event is logged\. For more information, see [How Do I Enable Object\-Level Logging for an S3 Bucket with AWS CloudTrail Data Events?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-cloudtrail-events.html) in the *Amazon Simple Storage Service Console User Guide* and [Data Events](http://docs.aws.amazon.com/awscloudtrail/latest/userguide/logging-management-and-data-events-with-cloudtrail.html#logging-data-events) in the *AWS CloudTrail User Guide*\. The following table lists the object\-level actions that CloudTrail can log:


| REST API Name | API Event Name Used in CloudTrail Log | 
| --- | --- | 
|  [Abort Multipart Upload](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadAbort.html)  | AbortMultipartUpload | 
|  [Complete Multipart Upload](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadComplete.html)  | CompleteMultipartUpload | 
|  [DELETE Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectDELETE.html)  | DeleteObject | 
|  [GET Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html)  | GetObject | 
|  [GET Object ACL](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETacl.html)  | GetObjectAcl | 
|  [GET Object tagging](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETtagging.html)  | GetObjectTagging | 
|  [GET Object torrent](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETtorrent.html)  | GetObjectTorrent | 
|  [HEAD Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectHEAD.html)  | HeadObject | 
|  [Initiate Multipart Upload](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html)  | CreateMultipartUpload | 
|  [List Parts](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadListParts.html)  | ListParts | 
|  [POST Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html)  | PostObject | 
|  [POST Object restore](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOSTrestore.html)  | RestoreObject | 
|  [PUT Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html)  | PutObject | 
|  [PUT Object acl](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTacl.html)  | PutObjectAcl | 
|  [PUT Object tagging](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTtagging.html)  | PutObjectTagging | 
|  [PUT Object \- Copy](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html)  | CopyObject | 
|  [SELECT Object Content](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectSELECTContent.html)  | SelectObjectContent | 
|  [Upload Part](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPart.html)  | UploadPart | 
|  [Upload Part \- Copy](http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPartCopy.html)  | UploadPartCopy | 

In addition to these operations, you can use the following bucket\-level operations to get CloudTrail logs as object\-level Amazon S3 actions under certain conditions:
+ [GET Bucket \(List Objects\) Version 2](http://docs.aws.amazon.com/AmazonS3/latest/API/v2-RESTBucketGET.html) – Select a prefix specified in the trail\.
+ [GET Bucket Object versions](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETVersion.html) – Select a prefix specified in the trail\.
+ [HEAD Bucket](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketHEAD.html) – Specify a bucket and an empty prefix\.
+ [Delete Multiple Objects](http://docs.aws.amazon.com/AmazonS3/latest/API/multiobjectdeleteapi.html) – Specify a bucket and an empty prefix\.

#### Object\-Level Actions in Cross\-Account Scenarios<a name="cloudtrail-object-level-crossaccount"></a>

The following are special use cases involving the object\-level API calls in cross\-account scenarios and how CloudTrail logs are reported\. CloudTrail always delivers logs to the requester \(who made the API call\)\. When setting up cross\-account access, consider the examples in this section\.

**Note**  
The examples assume CloudTrail logs are appropriately configured\. 

##### Example 1: CloudTrail Delivers Access Logs to the Bucket Owner<a name="cloudtrail-crossaccount-example1"></a>

CloudTrail delivers access logs to the bucket owner only if the bucket owner has permissions for the same object API\. Consider the following cross\-account scenario:
+ Account\-A owns the bucket\.
+ Account\-B \(the requester\) attempts to access an object in that bucket\.

CloudTrail always delivers object\-level API access logs to the requester\. In addition, CloudTrail also delivers the same logs to the bucket owner only if the bucket owner has permissions for the same API actions on that object\. 

**Note**  
If the bucket owner is also the object owner, the bucket owner gets the object access logs\. Otherwise, the bucket owner must get permissions, through the object ACL, for the same object API to get the same object\-access API logs\.

##### Example 2: CloudTrail Does Not Proliferate Email Addresses Used in Setting Object ACLs<a name="cloudtrail-crossaccount-example2"></a>

Consider the following cross\-account scenario:
+ Account\-A owns the bucket\.
+  Account\-B \(the requester\) sends a request to set an object ACL grant using an email address\. For information about ACLs, see [Access Control List \(ACL\) Overview](acl-overview.md)\.

The request gets the logs along with the email information\. However, the bucket owner—if they eligible to receive logs as in example 1—gets the CloudTrail log reporting the event\. However, the bucket owner doesn't get the ACL configuration information, specifically the grantee email and the grant\. The only information the log tells the bucket owner is that an ACL API call was made by Account\-B\.

### CloudTrail Tracking with Amazon S3 SOAP API Calls<a name="cloudtrail-s3-soap"></a>

CloudTrail tracks Amazon S3 SOAP API calls\. Amazon S3 SOAP support over HTTP is deprecated, but it is still available over HTTPS\. For more information about Amazon S3 SOAP support, see [Appendix A: Using the SOAP API](SOAPAPI3.md)\. 

**Important**  
Newer Amazon S3 features are not supported for SOAP\. We recommend that you use either the REST API or the AWS SDKs\.


**Amazon S3 SOAP Actions Tracked by CloudTrail Logging**  

| SOAP API Name | API Event Name Used in CloudTrail Log | 
| --- | --- | 
|  [ListAllMyBuckets](http://docs.aws.amazon.com/AmazonS3/latest/API/SOAPListAllMyBuckets.html)  | ListBuckets | 
|  [CreateBucket](http://docs.aws.amazon.com/AmazonS3/latest/API/SOAPCreateBucket.html)  | CreateBucket | 
|  [DeleteBucket](http://docs.aws.amazon.com/AmazonS3/latest/API/SOAPDeleteBucket.html)  | DeleteBucket | 
|  [GetBucketAccessControlPolicy](http://docs.aws.amazon.com/AmazonS3/latest/API/SOAPGetBucketAccessControlPolicy.html)  | GetBucketAcl | 
|  [SetBucketAccessControlPolicy](http://docs.aws.amazon.com/AmazonS3/latest/API/SOAPSetBucketAccessControlPolicy.html)  | PutBucketAcl | 
|  [GetBucketLoggingStatus](http://docs.aws.amazon.com/AmazonS3/latest/API/SOAPGetBucketLoggingStatus.html)  | GetBucketLogging | 
|  [SetBucketLoggingStatus](http://docs.aws.amazon.com/AmazonS3/latest/API/SOAPSetBucketLoggingStatus.html)  | PutBucketLogging | 

## Using CloudTrail Logs with Amazon S3 Server Access Logs and CloudWatch Logs<a name="cloudtrail-logging-vs-server-logs"></a>

You can use AWS CloudTrail logs together with server access logs for Amazon S3\. CloudTrail logs provide you with detailed API tracking for Amazon S3 bucket\-level and object\-level operations, while server access logs for Amazon S3 provide you visibility into object\-level operations on your data in Amazon S3\. For more information about server access logs, see [Amazon S3 Server Access Logging](ServerLogs.md)\.

You can also use CloudTrail logs together with CloudWatch for Amazon S3\. CloudTrail integration with CloudWatch logs delivers S3 bucket\-level API activity captured by CloudTrail to a CloudWatch log stream in the CloudWatch log group you specify\. You can create CloudWatch alarms for monitoring specific API activity and receive email notifications when the specific API activity occurs\. For more information about CloudWatch alarms for monitoring specific API activity, see the [AWS CloudTrail User Guide](http://docs.aws.amazon.com/awscloudtrail/latest/userguide/)\. For more information about using CloudWatch with Amazon S3, see [Monitoring Metrics with Amazon CloudWatch](cloudwatch-monitoring.md)\.

## Example: Amazon S3 Log File Entries<a name="cloudtrail-logging-understanding-s3-entries"></a>

 A trail is a configuration that enables delivery of events as log files to an Amazon S3 bucket that you specify\. CloudTrail log files contain one or more log entries\. An event represents a single request from any source and includes information about the requested action, the date and time of the action, request parameters, and so on\. CloudTrail log files are not an ordered stack trace of the public API calls, so they do not appear in any specific order\.

The following example shows a CloudTrail log entry that demonstrates the [DELETE Bucket policy](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEpolicy.html), [PUT Bucket acl](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTacl.html), and [GET Bucket versioning](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETversioningStatus.html) actions\.

```
{
    "Records": [
    {
        "eventVersion": "1.03",
        "userIdentity": {
            "type": "IAMUser",
            "principalId": "111122223333",
            "arn": "arn:aws:iam::111122223333:user/myUserName",
            "accountId": "111122223333",
            "accessKeyId": "AKIAIOSFODNN7EXAMPLE",
            "userName": "myUserName"
        },
        "eventTime": "2015-08-26T20:46:31Z",
        "eventSource": "s3.amazonaws.com",
        "eventName": "DeleteBucketPolicy",
        "awsRegion": "us-west-2",
        "sourceIPAddress": "127.0.0.1",
        "userAgent": "[]",
        "requestParameters": {
            "bucketName": "myawsbucket"
        },
        "responseElements": null,
        "requestID": "47B8E8D397DCE7A6",
        "eventID": "cdc4b7ed-e171-4cef-975a-ad829d4123e8",
        "eventType": "AwsApiCall",
        "recipientAccountId": "111122223333"
    },
    {
       "eventVersion": "1.03",
       "userIdentity": {
            "type": "IAMUser",
            "principalId": "111122223333",
            "arn": "arn:aws:iam::111122223333:user/myUserName",
            "accountId": "111122223333",
            "accessKeyId": "AKIAIOSFODNN7EXAMPLE",
            "userName": "myUserName"
        },
      "eventTime": "2015-08-26T20:46:31Z",
      "eventSource": "s3.amazonaws.com",
      "eventName": "PutBucketAcl",
      "awsRegion": "us-west-2",
      "sourceIPAddress": "",
      "userAgent": "[]",
      "requestParameters": {
          "bucketName": "",
          "AccessControlPolicy": {
              "AccessControlList": {
                  "Grant": {
                      "Grantee": {
                          "xsi:type": "CanonicalUser",
                          "xmlns:xsi": "http://www.w3.org/2001/XMLSchema-instance",
                          "ID": "d25639fbe9c19cd30a4c0f43fbf00e2d3f96400a9aa8dabfbbebe1906Example"
                       },
                      "Permission": "FULL_CONTROL"
                   }
              },
              "xmlns": "http://s3.amazonaws.com/doc/2006-03-01/",
              "Owner": {
                  "ID": "d25639fbe9c19cd30a4c0f43fbf00e2d3f96400a9aa8dabfbbebe1906Example"
              }
          }
      },
      "responseElements": null,
      "requestID": "BD8798EACDD16751",
      "eventID": "607b9532-1423-41c7-b048-ec2641693c47",
      "eventType": "AwsApiCall",
      "recipientAccountId": "111122223333"
    },
    {
      "eventVersion": "1.03",
      "userIdentity": {
          "type": "IAMUser",
          "principalId": "111122223333",
          "arn": "arn:aws:iam::111122223333:user/myUserName",
          "accountId": "111122223333",
          "accessKeyId": "AKIAIOSFODNN7EXAMPLE",
          "userName": "myUserName"
        },
      "eventTime": "2015-08-26T20:46:31Z",
      "eventSource": "s3.amazonaws.com",
      "eventName": "GetBucketVersioning",
      "awsRegion": "us-west-2",
      "sourceIPAddress": "",
      "userAgent": "[]",
      "requestParameters": {
          "bucketName": "myawsbucket"
      },
      "responseElements": null,
      "requestID": "07D681279BD94AED",
      "eventID": "f2b287f3-0df1-4961-a2f4-c4bdfed47657",
      "eventType": "AwsApiCall",
      "recipientAccountId": "111122223333"
    }
  ]
}
```

## Related Resources<a name="cloudtrail-logging-related-resources"></a>
+ [AWS CloudTrail User Guide](http://docs.aws.amazon.com/awscloudtrail/latest/userguide/)
+ [CloudTrail Event Reference](http://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-event-reference.html)