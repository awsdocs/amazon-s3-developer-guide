# Using AWS CloudTrail to identify Amazon S3 requests<a name="cloudtrail-request-identification"></a>

Amazon S3 lets you identify requests using an AWS CloudTrail event log\. AWS CloudTrail is the preferred way of identifying Amazon S3 requests, but if you are using Amazon S3 server access logs, see [ Using Amazon S3 access logs to identify requests](using-s3-access-logs-to-identify-requests.md)\.

**Topics**
+ [How CloudTrail captures requests made to Amazon S3](#cloudtrail-logging-s3-requests)
+ [Enabling CloudTrail event logging for S3 buckets and objects](#enable-cloudtrail-logging-for-s3)
+ [Identifying requests made to Amazon S3 in a CloudTrail log](#identify-S3-requests-using-in-CTlog)
+ [Using AWS CloudTrail to identify Amazon S3 signature version 2 requests](#cloudtrail-identification-sigv2-requests)
+ [Using AWS CloudTrail to identify access to Amazon S3 objects](#cloudtrail-identification-object-access)
+ [Related resources](#cloudtrail-logging-related-resources)

## How CloudTrail captures requests made to Amazon S3<a name="cloudtrail-logging-s3-requests"></a>

By default, CloudTrail logs S3 bucket\-level API calls that were made in the last 90 days, but not log requests made to objects\. Bucket\-level calls include events like `CreateBucket`, `DeleteBucket`, `PutBucketLifeCycle`, `PutBucketPolicy`, etc\. You can see bucket\-level events on the CloudTrail console\. However, you can't view data events \(Amazon S3 object\-level calls\) there—you must parse or query CloudTrail logs for them\. 

For information about what Amazon S3 API calls are captured by CloudTrail, see [Amazon S3 information in CloudTrail](cloudtrail-logging.md#cloudtrail-logging-s3-info)\. 

## Enabling CloudTrail event logging for S3 buckets and objects<a name="enable-cloudtrail-logging-for-s3"></a>

CloudTrail data events allow you to get information about bucket and object\-level requests\. To enable CloudTrail data events for a specific bucket, see [How Do I Enable Object\-Level Logging for an S3 Bucket with AWS CloudTrail Data Events?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-cloudtrail-events.html) in the *Amazon Simple Storage Service Console User Guide*\.

To enable CloudTrail data events for all your buckets or for a list of specific buckets, you must [create a trail manually in CloudTrail](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-create-a-trail-using-the-console-first-time.html)\. 

**Note**  
The default setting for CloudTrail is to find only management events\. Check to ensure that you have the data events enabled for your account\.
 With an S3 bucket that is generating a high workload, you could quickly generate thousands of logs in a short amount of time\. Be mindful of how long you choose to enable CloudTrail data events for a busy bucket\. 

 CloudTrail stores Amazon S3 data event logs in an S3 bucket of your choosing\. You should consider using a bucket in a separate AWS account to better organize events from multiple buckets you might own into a central place for easier querying and analysis\. AWS Organizations makes it easy to create an AWS account that is linked to the account owning the bucket that you are monitoring\. For more information, see [What Is AWS Organizations](https://docs.aws.amazon.com/organizations/latest/userguide/orgs_introduction.html) in the *AWS Organizations User Guide*\.

When you create a trail in CloudTrail, in the data events section, you can select the **Select all S3 buckets in your account** check box to log all object level events\. 

**Note**  
It's a best practice to [create an Amazon S3 lifecycle policy](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-lifecycle.html) for your AWS CloudTrail data event bucket\. Configure the lifecycle policy to periodically remove log files after the period of time you believe you need to audit them\. Doing so reduces the amount of data that Athena analyzes for each query\.
For information about logging format, see [Logging Amazon S3 API Calls by Using AWS CloudTrail](https://docs.aws.amazon.com/AmazonS3/latest/dev/cloudtrail-logging.html)\.
For examples of how to query CloudTrail logs, see [Analyze Security, Compliance, and Operational Activity Using AWS CloudTrail and Amazon Athena](http://aws.amazon.com/blogs/big-data/aws-cloudtrail-and-amazon-athena-dive-deep-to-analyze-security-compliance-and-operational-activity/)\. 

## Identifying requests made to Amazon S3 in a CloudTrail log<a name="identify-S3-requests-using-in-CTlog"></a>

Events logged by CloudTrail are stored as compressed, GZipped JSON objects in your S3 bucket\. To efficiently find requests, you should use a service like Amazon Athena to index and query the CloudTrail logs\. For more information about CloudTrail and Athena, see [Querying AWS CloudTrail Logs](https://docs.aws.amazon.com/athena/latest/ug/cloudtrail-logs.html)\.

### Using Athena with CloudTrail logs<a name="using-athena"></a>

After you set up CloudTrail to deliver events to a bucket, you should start to see objects go to your destination bucket on the Amazon S3 console\. These are formatted as follows: `s3://<myawsexamplebucket1>/AWSLogs/<111122223333>/CloudTrail/<Region>/<yyyy>/<mm>/<dd>` 

**Example — use Athena to query CloudTrail event logs for specific requests**  

Locate your CloudTrail event logs:

`s3://myawsexamplebucket1/AWSLogs/111122223333/CloudTrail/us-east-2/2019/04/14`

With CloudTrail event logs, you can now create an Athena database and table to query them as follows:

1. Open the Athena console at [https://console\.aws\.amazon\.com/athena/](https://console.aws.amazon.com/athena/home)\.

1. Change the AWS Region to be the same as your CloudTrail destination S3 bucket\.

1. In the query window, create an Athena database for your CloudTrail events\.

   ```
   CREATE DATABASE s3_cloudtrail_events_db
   ```

1. Use the following query to create a table for all of your CloudTrail events in the bucket\. Be sure to change the bucket name from *<CloudTrail\_myawsexamplebucket1>* to your bucket's name\. Also provide the *AWS\_account\_ID* CloudTrail that is used in your bucket\. 

   ```
   CREATE EXTERNAL TABLE s3_cloudtrail_events_db.cloudtrail_myawsexamplebucket1_table(
           eventversion STRING,
           useridentity STRUCT<
               type:STRING,
               principalid:STRING,
               arn:STRING,
               accountid:STRING,
               invokedby:STRING,
               accesskeyid:STRING,
               userName:STRING,
               sessioncontext:STRUCT<
                   attributes:STRUCT<
                           mfaauthenticated:STRING,
                           creationdate:STRING>,
                           sessionissuer:STRUCT<  
                           type:STRING,
                           principalId:STRING,
                           arn:STRING, 
                           accountId:STRING,
                           userName:STRING>
                   >
                >,
           eventtime STRING,
           eventsource STRING,
           eventname STRING,
           awsregion STRING,
           sourceipaddress STRING,
           useragent STRING,
           errorcode STRING,
           errormessage STRING,
           requestparameters STRING,
           responseelements STRING,
           additionaleventdata STRING,
           requestid STRING,
           eventid STRING,
           resources ARRAY<STRUCT<
               ARN:STRING,
               accountId:STRING,
               type:STRING>>,
           eventtype STRING,
           apiversion STRING,
           readonly STRING,
           recipientaccountid STRING,
           serviceeventdetails STRING,
           sharedeventid STRING,
           vpcendpointid STRING
       )
       ROW FORMAT SERDE 'com.amazon.emr.hive.serde.CloudTrailSerde'
       STORED AS INPUTFORMAT 'com.amazon.emr.cloudtrail.CloudTrailInputFormat'
       OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
       LOCATION 's3://<myawsexamplebucket1>/AWSLogs/<111122223333>/';
   ```

1. Test Athena to ensure that the query works\.

   ```
   SELECT * FROM s3_cloudtrail_events_db.cloudtrail_myawsexamplebucket1_table
   WHERE eventsource='s3.amazonaws.com'
   LIMIT 2;
   ```

## Using AWS CloudTrail to identify Amazon S3 signature version 2 requests<a name="cloudtrail-identification-sigv2-requests"></a>

Amazon S3 lets you identify what API signature version was used to sign a request using an AWS CloudTrail event log\. This capability is important because support for Signature Version 2 will be turned off \(deprecated\)\. After that, Amazon S3 will no longer accept requests that use Signature Version 2, and all requests must use *Signature Version 4* signing\. 

We *strongly* recommend that you use CloudTrail to help determine whether any of your workflows are using Signature Version 2 signing\. Remediate them by upgrading your libraries and code to use Signature Version 4 instead to prevent any impact to your business\. 

For more information, see [Announcement: AWS CloudTrail for Amazon S3 adds new fields for enhanced security auditing](https://forums.aws.amazon.com/ann.jspa?annID=6551) in the AWS Discussion Forums\.

**Note**  
CloudTrail events for Amazon S3 include the signature version in the request details under the key name of '`additionalEventData`'\. To find the signature version on requests made for objects in Amazon S3 like GETs, PUTs, and DELETEs, you must enable CloudTrail data events because it is turned off by default\. 

AWS CloudTrail is the preferred method for identifying Signature Version 2 requests, if you are using Amazon S3 server access logs, see [ Using Amazon S3 access logs to identify signature version 2 requests ](using-s3-access-logs-to-identify-requests.md#using-s3-access-logs-to-identify-sigv2-requests)

### Athena query examples for identifying Amazon S3 signature version 2 requests<a name="ct-examples-identify-sigv2-requests"></a>

**Example — select all events that are signature version 2, and print only EventTime, S3 action, Request\_Parameters, Region, SourceIP, and UserAgent**  
In the following Athena query, replace *<s3\_cloudtrail\_events\_db\.cloudtrail\_myawsexamplebucket1\_table>* with your Athena details and increase or remove the limit as needed\.   

```
SELECT EventTime, EventName as S3_Action, requestParameters as Request_Parameters, awsregion as AWS_Region, sourceipaddress as Source_IP, useragent as User_Agent
FROM s3_cloudtrail_events_db.cloudtrail_myawsexamplebucket1_table
WHERE eventsource='s3.amazonaws.com'
AND json_extract_scalar(additionalEventData, '$.SignatureVersion')='SigV2'
LIMIT 10;
```

**Example — select all requesters that are sending signature version 2 traffic**  

```
SELECT useridentity.arn, Count(requestid) as RequestCount
FROM s3_cloudtrail_events_db.cloudtrail_myawsexamplebucket1_table
WHERE eventsource='s3.amazonaws.com'
    and json_extract_scalar(additionalEventData, '$.SignatureVersion')='SigV2'
Group by useridentity.arn
```

### Partitioning signature version 2 data<a name="partitioning-sigv2-data"></a>

If you have a large amount of data that you need to query, you can reduce the costs and runtime of Athena by creating a partitioned table\. 

To do this, create a new table with partitions as follows\.

```
   CREATE EXTERNAL TABLE s3_cloudtrail_events_db.cloudtrail_myawsexamplebucket1_table_partitioned(
        eventversion STRING,
        userIdentity STRUCT<
            type:STRING,
            principalid:STRING,
            arn:STRING,
            accountid:STRING,
            invokedby:STRING,
            accesskeyid:STRING,
            userName:STRING,
         sessioncontext:STRUCT<
                    attributes:STRUCT< 
                    mfaauthenticated:STRING,
                    creationdate:STRING>,
                    sessionIssuer:STRUCT<
                    type:STRING,
                    principalId:STRING,
                    arn:STRING,
                    accountId:STRING,
                    userName:STRING>
                >
             >,
        eventTime STRING,
        eventSource STRING,
        eventName STRING,
        awsRegion STRING,
        sourceIpAddress STRING,
        userAgent STRING,
        errorCode STRING,
        errorMessage STRING,
        requestParameters STRING,
        responseElements STRING,
        additionalEventData STRING,
        requestId STRING,
        eventId STRING,
        resources ARRAY<STRUCT<ARN:STRING,accountId: STRING,type:STRING>>, 
        eventType STRING,
        apiVersion STRING,
        readOnly STRING,
        recipientAccountId STRING,
        serviceEventDetails STRING,
        sharedEventID STRING,
        vpcEndpointId STRING
    )   
    PARTITIONED BY (region string, year string, month string, day string)
    ROW FORMAT SERDE 'com.amazon.emr.hive.serde.CloudTrailSerde'
    STORED AS INPUTFORMAT 'com.amazon.emr.cloudtrail.CloudTrailInputFormat'
    OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
    LOCATION 's3://myawsexamplebucket1/AWSLogs/111122223333/';
```

Then, create the partitions individually\. You can't get results from dates that you have not created\. 

```
ALTER TABLE s3_cloudtrail_events_db.cloudtrail_myawsexamplebucket1_table_partitioned ADD
    PARTITION (region= 'us-east-1', year= '2019', month= '02', day= '19') LOCATION 's3://myawsexamplebucket1/AWSLogs/111122223333/CloudTrail/us-east-1/2019/02/19/'
    PARTITION (region= 'us-west-1', year= '2019', month= '02', day= '19') LOCATION 's3://myawsexamplebucket1/AWSLogs/111122223333/CloudTrail/us-west-1/2019/02/19/'
    PARTITION (region= 'us-west-2', year= '2019', month= '02', day= '19') LOCATION 's3://myawsexamplebucket1/AWSLogs/111122223333/CloudTrail/us-west-2/2019/02/19/'
    PARTITION (region= 'ap-southeast-1', year= '2019', month= '02', day= '19') LOCATION 's3://myawsexamplebucket1/AWSLogs/111122223333/CloudTrail/ap-southeast-1/2019/02/19/'
    PARTITION (region= 'ap-southeast-2', year= '2019', month= '02', day= '19') LOCATION 's3://myawsexamplebucket1/AWSLogs/111122223333/CloudTrail/ap-southeast-2/2019/02/19/'
    PARTITION (region= 'ap-northeast-1', year= '2019', month= '02', day= '19') LOCATION 's3://myawsexamplebucket1/AWSLogs/111122223333/CloudTrail/ap-northeast-1/2019/02/19/'
    PARTITION (region= 'eu-west-1', year= '2019', month= '02', day= '19') LOCATION 's3://myawsexamplebucket1/AWSLogs/111122223333/CloudTrail/eu-west-1/2019/02/19/'
    PARTITION (region= 'sa-east-1', year= '2019', month= '02', day= '19') LOCATION 's3://myawsexamplebucket1/AWSLogs/111122223333/CloudTrail/sa-east-1/2019/02/19/';
```

You can then make the request based on these partitions, and you don't need to load the full bucket\. 

```
SELECT useridentity.arn,
Count(requestid) AS RequestCount
FROM s3_cloudtrail_events_db.cloudtrail_myawsexamplebucket1_table_partitioned
WHERE eventsource='s3.amazonaws.com'
AND json_extract_scalar(additionalEventData, '$.SignatureVersion')='SigV2'
AND region='us-east-1'
AND year='2019'
AND month='02'
AND day='19'
Group by useridentity.arn
```

## Using AWS CloudTrail to identify access to Amazon S3 objects<a name="cloudtrail-identification-object-access"></a>

You can use your AWS CloudTrail event log to identify Amazon S3 object access requests for data events such as GetObject, DeleteObject, and PutObject, and discover further information about those requests\.

The following example shows how to get all PUT object requests for Amazon S3 from the AWS CloudTrail event log\. 

### Athena query example for identifying Amazon S3 object access requests<a name="ct-examples-identify-object-access-requests"></a>

In the following Athena query examples, replace *<s3\_cloudtrail\_events\_db\.cloudtrail\_myawsexamplebucket1\_table>* with your Athena details, and modify the date range as needed\. 

**Example — select all events that have PUT object access requests, and print only EventTime, EventSource, SourceIP, UserAgent, BucketName, object, and UserARN**  

```
SELECT
  eventTime, 
  eventName, 
  eventSource, 
  sourceIpAddress, 
  userAgent, 
  json_extract_scalar(requestParameters, '$.bucketName') as bucketName, 
  json_extract_scalar(requestParameters, '$.key') as object,
  userIdentity.arn as userArn
FROM
  s3_cloudtrail_events_db.cloudtrail_myawsexamplebucket_table
WHERE
  eventName = 'PutObject'
  AND eventTime BETWEEN '2019-07-05T00:00:00Z' and '2019-07-06T00:00:00Z'
```

**Example — select all events that have GET object access requests, and print only EventTime, EventSource, SourceIP, UserAgent, BucketName, object, and UserARN**  

```
SELECT
  eventTime, 
  eventName, 
  eventSource, 
  sourceIpAddress, 
  userAgent, 
  json_extract_scalar(requestParameters, '$.bucketName') as bucketName, 
  json_extract_scalar(requestParameters, '$.key') as object,
  userIdentity.arn as userArn
FROM
  s3_cloudtrail_events_db.cloudtrail_myawsexamplebucket_table
WHERE
  eventName = 'GetObject'
  AND eventTime BETWEEN '2019-07-05T00:00:00Z' and '2019-07-06T00:00:00Z'
```

**Example — select all anonymous requester events to a bucket in a certain period and print only EventTime, EventSource, SourceIP, UserAgent, BucketName, UserIdentity, and UserARN**  

```
SELECT
  eventTime, 
  eventName, 
  eventSource, 
  sourceIpAddress, 
  userAgent, 
  json_extract_scalar(requestParameters, '$.bucketName') as bucketName, 
  userIdentity.arn as userArn,
  userIdentity.principalId 
FROM
  s3_cloudtrail_events_db.cloudtrail_myawsexamplebucket_table
WHERE
  userIdentity.principalId='ANONYMOUS_PRINCIPAL'
  AND eventTime BETWEEN '2019-07-05T00:00:00Z' and '2019-07-06T00:00:00Z'
```

**Note**  
These query examples may also be useful for security monitoring\. You can review the results for `PutObject` or `GetObject` calls from unexpected or unauthorized IP addresses/requesters and for identifying any anonymous requests to your buckets\.
 This query only retrieves information from the time at which logging was enabled\. 

If you are using Amazon S3 server access logs, see [ Using Amazon S3 access logs to identify object access requests](using-s3-access-logs-to-identify-requests.md#using-s3-access-logs-to-identify-objects-access)\.

## Related resources<a name="cloudtrail-logging-related-resources"></a>
+ [AWS CloudTrail User Guide](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/)
+ [CloudTrail Event Reference](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-event-reference.html)