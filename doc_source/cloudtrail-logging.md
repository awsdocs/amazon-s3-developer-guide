# Logging Amazon S3 API calls using AWS CloudTrail<a name="cloudtrail-logging"></a>

Amazon S3 is integrated with AWS CloudTrail, a service that provides a record of actions taken by a user, role, or an AWS service in Amazon S3\. CloudTrail captures a subset of API calls for Amazon S3 as events, including calls from the Amazon S3 console and from code calls to the Amazon S3 APIs\. If you create a trail, you can enable continuous delivery of CloudTrail events to an Amazon S3 bucket, including events for Amazon S3\. If you don't configure a trail, you can still view the most recent events in the CloudTrail console in **Event history**\. Using the information collected by CloudTrail, you can determine the request that was made to Amazon S3, the IP address from which the request was made, who made the request, when it was made, and additional details\. 

To learn more about CloudTrail, including how to configure and enable it, see the [AWS CloudTrail User Guide](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/)\.

## Amazon S3 information in CloudTrail<a name="cloudtrail-logging-s3-info"></a>

CloudTrail is enabled on your AWS account when you create the account\. When supported event activity occurs in Amazon S3, that activity is recorded in a CloudTrail event along with other AWS service events in **Event history**\. You can view, search, and download recent events in your AWS account\. For more information, see [Viewing Events with CloudTrail Event History](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/view-cloudtrail-events.html)\. 

For an ongoing record of events in your AWS account, including events for Amazon S3, create a trail\. A trail enables CloudTrail to deliver log files to an Amazon S3 bucket\. By default, when you create a trail in the console, the trail applies to all Regions\. The trail logs events from all Regions in the AWS partition and delivers the log files to the Amazon S3 bucket that you specify\. Additionally, you can configure other AWS services to further analyze and act upon the event data collected in CloudTrail logs\. For more information, see the following: 
+ [Overview for Creating a Trail](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-create-and-update-a-trail.html)
+ [CloudTrail Supported Services and Integrations](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-aws-service-specific-topics.html#cloudtrail-aws-service-specific-topics-integrations)
+ [Configuring Amazon SNS Notifications for CloudTrail](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/getting_notifications_top_level.html)
+ [Receiving CloudTrail Log Files from Multiple Regions](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/receive-cloudtrail-log-files-from-multiple-regions.html) and [Receiving CloudTrail Log Files from Multiple Accounts](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-receive-logs-from-multiple-accounts.html)

Every event or log entry contains information about who generated the request\. The identity information helps you determine the following: 
+ Whether the request was made with root or IAM user credentials\.
+ Whether the request was made with temporary security credentials for a role or federated user\.
+ Whether the request was made by another AWS service\.

For more information, see the [CloudTrail userIdentity Element](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-event-reference-user-identity.html)\.

You can store your log files in your bucket for as long as you want, but you can also define Amazon S3 lifecycle rules to archive or delete log files automatically\. By default, your log files are encrypted by using Amazon S3 server\-side encryption \(SSE\)\.

### Amazon S3 account\-level actions tracked by CloudTrail logging<a name="cloudtrail-account-level-tracking"></a>

CloudTrail logs account\-level actions\. Amazon S3 records are written together with other AWS service records in a log file\. CloudTrail determines when to create and write to a new file based on a time period and file size\. 

The tables in this section list the Amazon S3 account\-level actions that are supported for logging by CloudTrail\.

**Amazon S3 account\-level API actions tracked by CloudTrail logging will show up as the following event names:**
+ [ DeletePublicAccessBlock](https://docs.aws.amazon.com/AmazonS3/latest/API/API_control_DeletePublicAccessBlock.html)
+ [ GetPublicAccessBlock](https://docs.aws.amazon.com/AmazonS3/latest/API/API_control_GetPublicAccessBlock.html)
+ [ PutPublicAccessBlock](https://docs.aws.amazon.com/AmazonS3/latest/API/API_control_PutPublicAccessBlock.html)

### Amazon S3 bucket\-level actions tracked by CloudTrail logging<a name="cloudtrail-bucket-level-tracking"></a>

By default, CloudTrail logs bucket\-level actions\. Amazon S3 records are written together with other AWS service records in a log file\. CloudTrail determines when to create and write to a new file based on a time period and file size\. 

The tables in this section list the Amazon S3 bucket\-level actions that are supported for logging by CloudTrail\.

**Amazon S3 bucket\-level API actions tracked by CloudTrail logging will show up as the following event names:**
+ [CreateBucket](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUT.html) 
+ [DeleteBucket](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETE.html)
+ [DeleteBucketCors](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEcors.html)
+ [DeleteBucketEncryption](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEencryption.html)
+ [DeleteBucketLifecycle](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETElifecycle.html)
+ [DeleteBucketPolicy](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEpolicy.html)
+ [DeleteBucketReplication ](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEreplication.html)
+ [DeleteBucketTagging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEtagging.html)
+ [DeleteBucketWebsite](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEwebsite.html)
+ [DeleteBucketWebsite](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEwebsite.html)
+ [ DeletePublicAccessBlock](https://docs.aws.amazon.com/AmazonS3/latest/API/API_DeletePublicAccessBlock.html)
+ [GetBucketCors](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETcors.html)
+ [GetBucketEncryption](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETencryption.html)
+ [GetBucketLifecycle](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETencryption.html) 
+ [GetBucketLocation](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlocation.html) 
+ [GetBucketLogging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlogging.html) 
+ [GetBucketNotification](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETnotification.html)
+ [GetBucketPolicy](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETpolicy.html)
+ [GetBucketReplication](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETreplication.html)
+ [GetBucketRequestPayment](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTrequestPaymentGET.html)
+ [GetBucketTagging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETtagging.html)
+ [GetBucketVersioning](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETversioningStatus.html) 
+ [GetBucketWebsite](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETwebsite.html) 
+ [ GetPublicAccessBlock](https://docs.aws.amazon.com/AmazonS3/latest/API/API_control_GetPublicAccessBlock.html) 
+ [ListBuckets](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTServiceGET.html) 
+ [PutBucketAcl](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTacl.html) 
+ [PutBucketCors](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTcors.html) 
+ [PutBucketEncryption](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTencryption.html)
+ [PutBucketLifecycle](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTlifecycle.html) 
+ [PutBucketLogging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTlogging.html)
+ [PutBucketNotification ](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTnotification.html)
+ [PutBucketPolicy](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTpolicy.html) 
+ [PutBucketReplication](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTreplication.html) 
+ [PutBucketRequestPayment](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTrequestPaymentPUT.html)
+ [PutBucketTagging ](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTtagging.html) 
+ [PutBucketVersioning](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTVersioningStatus.html)
+ [PutBucketWebsite](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTwebsite.html) 
+ [ PutPublicAccessBlock](https://docs.aws.amazon.com/AmazonS3/latest/API/API_control_PutPublicAccessBlock.html) 

In addition to these API operations, you can also use the [OPTIONS object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTOPTIONSobject.html) object\-level action\. This action is treated like a bucket\-level action in CloudTrail logging because the action checks the cors configuration of a bucket\.

### Amazon S3 object\-level actions tracked by CloudTrail logging<a name="cloudtrail-object-level-tracking"></a>

You can also get CloudTrail logs for object\-level Amazon S3 actions\. To do this, specify the Amazon S3 object for your trail\. When an object\-level action occurs in your account, CloudTrail evaluates your trail settings\. If the event matches the object that you specified in a trail, the event is logged\. For more information, see [How Do I Enable Object\-Level Logging for an S3 Bucket with AWS CloudTrail Data Events?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-cloudtrail-events.html) in the *Amazon Simple Storage Service Console User Guide* and [Logging Data Events for Trails](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/logging-data-events-with-cloudtrail.html) in the *AWS CloudTrail User Guide*\. 

The following table lists the object\-level API actions that are logged as CloudTrail events:
+ [AbortMultipartUpload](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadAbort.html)
+ [CompleteMultipartUpload](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadComplete.html)
+ [DeleteObjects](https://docs.aws.amazon.com/AmazonS3/latest/API/multiobjectdeleteapi.html)
+ [DeleteObject](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectDELETE.html)
+ [GetObject](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html)
+ [GetObjectAcl](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETacl.html)
+ [GetObjectTagging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETtagging.html)
+ [GetObjectTorrent](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETtorrent.html)
+ [HeadObject](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectHEAD.html)
+ [CreateMultipartUpload](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html)
+ [ListParts](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadListParts.html)
+ [PostObject](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html)
+ [RestoreObject](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOSTrestore.html)
+ [PutObject](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html)
+ [PutObjectAcl](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTacl.html)
+ [PutObjectTagging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTtagging.html)
+ [CopyObject](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html)
+ [UploadPart](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPart.html)
+ [UploadPartCopy](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPartCopy.html)

In addition to these operations, you can use the following bucket\-level operations to get CloudTrail logs as object\-level Amazon S3 actions under certain conditions:
+ [GET Bucket \(List Objects\) Version 2](https://docs.aws.amazon.com/AmazonS3/latest/API/v2-RESTBucketGET.html) – Select a prefix specified in the trail\.
+ [GET Bucket Object versions](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETVersion.html) – Select a prefix specified in the trail\.
+ [HEAD Bucket](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketHEAD.html) – Specify a bucket and an empty prefix\.
+ [Delete Multiple Objects](https://docs.aws.amazon.com/AmazonS3/latest/API/multiobjectdeleteapi.html) – Specify a bucket and an empty prefix\. 
**Note**  
CloudTrail does not log key names for the keys that are deleted using the Delete Multiple Objects operation\.

#### Object\-level actions in cross\-account scenarios<a name="cloudtrail-object-level-crossaccount"></a>

The following are special use cases involving the object\-level API calls in cross\-account scenarios and how CloudTrail logs are reported\. CloudTrail always delivers logs to the requester \(who made the API call\)\. When setting up cross\-account access, consider the examples in this section\.

**Note**  
The examples assume that CloudTrail logs are appropriately configured\. 

##### Example 1: CloudTrail delivers access logs to the bucket owner<a name="cloudtrail-crossaccount-example1"></a>

CloudTrail delivers access logs to the bucket owner only if the bucket owner has permissions for the same object API\. Consider the following cross\-account scenario:
+ Account\-A owns the bucket\.
+ Account\-B \(the requester\) tries to access an object in that bucket\.

CloudTrail always delivers object\-level API access logs to the requester\. In addition, CloudTrail also delivers the same logs to the bucket owner only if the bucket owner has permissions for the same API actions on that object\. 

**Note**  
If the bucket owner is also the object owner, the bucket owner gets the object access logs\. Otherwise, the bucket owner must get permissions, through the object ACL, for the same object API to get the same object\-access API logs\.

##### Example 2: CloudTrail does not proliferate email addresses used in setting object ACLs<a name="cloudtrail-crossaccount-example2"></a>

Consider the following cross\-account scenario:
+ Account\-A owns the bucket\.
+  Account\-B \(the requester\) sends a request to set an object ACL grant using an email address\. For information about ACLs, see [Access Control List \(ACL\) Overview](acl-overview.md)\.

The request gets the logs along with the email information\. However, the bucket owner—if they are eligible to receive logs, as in example 1—gets the CloudTrail log reporting the event\. However, the bucket owner doesn't get the ACL configuration information, specifically the grantee email and the grant\. The only information that the log tells the bucket owner is that an ACL API call was made by Account\-B\.

### CloudTrail tracking with Amazon S3 SOAP API calls<a name="cloudtrail-s3-soap"></a>

CloudTrail tracks Amazon S3 SOAP API calls\. Amazon S3 SOAP support over HTTP is deprecated, but it is still available over HTTPS\. For more information about Amazon S3 SOAP support, see [Appendix a: Using the SOAP API](SOAPAPI3.md)\. 

**Important**  
Newer Amazon S3 features are not supported for SOAP\. We recommend that you use either the REST API or the AWS SDKs\.


**Amazon S3 SOAP actions tracked by CloudTrail logging**  

| SOAP API name | API event name used in CloudTrail log | 
| --- | --- | 
|  [ListAllMyBuckets](https://docs.aws.amazon.com/AmazonS3/latest/API/SOAPListAllMyBuckets.html)  | ListBuckets | 
|  [CreateBucket](https://docs.aws.amazon.com/AmazonS3/latest/API/SOAPCreateBucket.html)  | CreateBucket | 
|  [DeleteBucket](https://docs.aws.amazon.com/AmazonS3/latest/API/SOAPDeleteBucket.html)  | DeleteBucket | 
|  [GetBucketAccessControlPolicy](https://docs.aws.amazon.com/AmazonS3/latest/API/SOAPGetBucketAccessControlPolicy.html)  | GetBucketAcl | 
|  [SetBucketAccessControlPolicy](https://docs.aws.amazon.com/AmazonS3/latest/API/SOAPSetBucketAccessControlPolicy.html)  | PutBucketAcl | 
|  [GetBucketLoggingStatus](https://docs.aws.amazon.com/AmazonS3/latest/API/SOAPGetBucketLoggingStatus.html)  | GetBucketLogging | 
|  [SetBucketLoggingStatus](https://docs.aws.amazon.com/AmazonS3/latest/API/SOAPSetBucketLoggingStatus.html)  | PutBucketLogging | 

## Using CloudTrail logs with Amazon S3 server access logs and CloudWatch Logs<a name="cloudtrail-logging-vs-server-logs"></a>

AWS CloudTrail logs provide a record of actions taken by a user, role, or an AWS service in Amazon S3, while Amazon S3 server access logs provide detailed records for the requests that are made to an S3 bucket\. For more information about how the different logs work, and their properties, performance and costs, see [Logging with Amazon S3](logging-with-S3.md)\. 

You can use AWS CloudTrail logs together with server access logs for Amazon S3\. CloudTrail logs provide you with detailed API tracking for Amazon S3 bucket\-level and object\-level operations\. Server access logs for Amazon S3 provide you visibility into object\-level operations on your data in Amazon S3\. For more information about server access logs, see [Amazon S3 server access logging](ServerLogs.md)\.

You can also use CloudTrail logs together with CloudWatch for Amazon S3\. CloudTrail integration with CloudWatch Logs delivers S3 bucket\-level API activity captured by CloudTrail to a CloudWatch log stream in the CloudWatch log group that you specify\. You can create CloudWatch alarms for monitoring specific API activity and receive email notifications when the specific API activity occurs\. For more information about CloudWatch alarms for monitoring specific API activity, see the [AWS CloudTrail User Guide](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/)\. For more information about using CloudWatch with Amazon S3, see [Monitoring metrics with Amazon CloudWatch](cloudwatch-monitoring.md)\.

## Example: Amazon S3 log file entries<a name="cloudtrail-logging-understanding-s3-entries"></a>

 A trail is a configuration that enables delivery of events as log files to an Amazon S3 bucket that you specify\. CloudTrail log files contain one or more log entries\. An event represents a single request from any source\. It includes information about the requested action, the date and time of the action, request parameters, and so on\. CloudTrail log files are not an ordered stack trace of the public API calls, so they do not appear in any specific order\.

The following example shows a CloudTrail log entry that demonstrates the [GET Service](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTServiceGET.html), [PUT Bucket acl](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTacl.html), and [GET Bucket versioning](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETversioningStatus.html) actions\.

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
        "eventTime": "2019-02-01T03:18:19Z",
        "eventSource": "s3.amazonaws.com",
        "eventName": "ListBuckets",
        "awsRegion": "us-west-2",
        "sourceIPAddress": "127.0.0.1",
        "userAgent": "[]",
        "requestParameters": {
            "host": [
                "s3.us-west-2.amazonaws.com"
            ]
        },
        "responseElements": null,
        "additionalEventData": {
            "SignatureVersion": "SigV2",
            "AuthenticationMethod": "QueryString"
    },
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
      "eventTime": "2019-02-01T03:22:33Z",
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
          "host": [
              "s3.us-west-2.amazonaws.com"
          ],
          "acl": [
              ""
          ]
      },
      "responseElements": null,
      "additionalEventData": {
          "SignatureVersion": "SigV4",
          "CipherSuite": "ECDHE-RSA-AES128-SHA",
          "AuthenticationMethod": "AuthHeader"
      },
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
      "eventTime": "2019-02-01T03:26:37Z",
      "eventSource": "s3.amazonaws.com",
      "eventName": "GetBucketVersioning",
      "awsRegion": "us-west-2",
      "sourceIPAddress": "",
      "userAgent": "[]",
      "requestParameters": {
          "host": [
              "s3.us-west-2.amazonaws.com"
          ],
          "bucketName": "DOC-EXAMPLE-BUCKET1",
          "versioning": [
              ""
          ]
      },
      "responseElements": null,
      "additionalEventData": {
          "SignatureVersion": "SigV4",
          "CipherSuite": "ECDHE-RSA-AES128-SHA",
          "AuthenticationMethod": "AuthHeader",
    },
      "requestID": "07D681279BD94AED",
      "eventID": "f2b287f3-0df1-4961-a2f4-c4bdfed47657",
      "eventType": "AwsApiCall",
      "recipientAccountId": "111122223333"
    }
  ]
}
```

## Related resources<a name="cloudtrail-logging-related-resources"></a>
+ [AWS CloudTrail User Guide](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/)
+ [CloudTrail Event Reference](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-event-reference.html)
+ [ Using AWS CloudTrail to identify Amazon S3 requests](cloudtrail-request-identification.md)