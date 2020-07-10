# Using Amazon S3 access logs to identify requests<a name="using-s3-access-logs-to-identify-requests"></a>

You can identify Amazon S3 requests with Amazon S3 access logs\. 

**Note**  
We recommend that you use AWS CloudTrail data events instead of Amazon S3 access logs\. CloudTrail data events are easier to set up and contain more information\. For more information, see [ Using AWS CloudTrail to identify Amazon S3 requests](cloudtrail-request-identification.md)\.
Depending on how many access requests you get, it may require more resources and/or more time to analyze your logs\.

**Topics**
+ [Enabling Amazon S3 access logs for requests](#enabling-s3-access-logs-for-requests)
+ [Querying Amazon S3 access logs for requests using Amazon Athena](#querying-s3-access-logs-for-requests)
+ [Using Amazon S3 access logs to identify signature version 2 requests](#using-s3-access-logs-to-identify-sigv2-requests)
+ [Using Amazon S3 access logs to identify object access requests](#using-s3-access-logs-to-identify-objects-access)
+ [Related resources](#s3-access-logs-requests-more-info)

## Enabling Amazon S3 access logs for requests<a name="enabling-s3-access-logs-for-requests"></a>

We recommend that you create a dedicated logging bucket in each AWS Region that you have S3 buckets in\. Then have the Amazon S3 access log delivered to that S3 bucket\.

**Example — enable access logs with five buckets across two Regions**  
In this example, you have the following five buckets:  
+ `1-awsexamplebucket1-us-east-1`
+ `2-awsexamplebucket1-us-east-1`
+ `3-awsexamplebucket1-us-east-1`
+ `1-awsexamplebucket1-us-west-2`
+ `2-awsexamplebucket1-us-west-2`

1. Create two logging buckets in the following Regions:
   + `awsexamplebucket1-logs-us-east-1`
   + `awsexamplebucket1-logs-us-west-2`

1. Then enable the Amazon S3 access logs as follows:
   + `1-awsexamplebucket1-us-east-1` logs to the S3 bucket `awsexamplebucket1-logs-us-east-1` with prefix `1-awsexamplebucket1-us-east-1`
   + `2-awsexamplebucket1-us-east-1` logs to the S3 bucket `awsexamplebucket1-logs-us-east-1` with prefix `2-awsexamplebucket1-us-east-1`
   + `1-awsexamplebucket1-us-east-1` logs to the S3 bucket `awsexamplebucket1-logs-us-east-1` with prefix `3-awsexamplebucket1-us-east-1`
   + `1-awsexamplebucket1-us-west-2` logs to the S3 bucket `awsexamplebucket1-logs-us-west-2` with prefix `1-awsexamplebucket1-us-west-2`
   + `2-awsexamplebucket1-us-west-2` logs to the S3 bucket `awsexamplebucket1-logs-us-west-2` with prefix `2-awsexamplebucket1-us-west-2`

1. You can then enable the Amazon S3 access logs using the following methods:
   + Using the [AWS Management Console](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/server-access-logging.html) or,
   + [Enabling logging programmatically](enable-logging-programming.md) or, 
   + Using the [AWS CLI put\-bucket\-logging command](https://docs.aws.amazon.com/cli/latest/reference/s3api/put-bucket-logging.html) to programmatically enable access logs on a bucket using the following commands:

     1. First, grant Amazon S3 permission using `put-bucket-acl`:

        ```
        1.  aws s3api put-bucket-acl --bucket awsexamplebucket1-logs  --grant-write URI=http://acs.amazonaws.com/groups/s3/LogDelivery --grant-read-acp URI=http://acs.amazonaws.com/groups/s3/LogDelivery 
        ```

     1. Then, apply the logging policy:

        ```
        1. aws s3api put-bucket-logging --bucket awsexamplebucket1 --bucket-logging-status file://logging.json 
        ```

        `Logging.json` is a JSON document in the current folder that contains the logging policy:

        ```
          {
              "LoggingEnabled": {
                  "TargetBucket": "awsexamplebucket1-logs",
                  "TargetPrefix": "awsexamplebucket1/",
                  "TargetGrants": [
                       {
                          "Grantee": {
                              "Type": "AmazonCustomerByEmail",
                              "EmailAddress": "user@example.com"
                           },
                          "Permission": "FULL_CONTROL"
                       }
                   ]
               }
           }
        ```
**Note**  
The `put-bucket-acl` command is required to grant the Amazon S3 log delivery system the necessary permissions \(write and read\-acp permissions\)\. 

     1. Use a bash script to add access logging for all the buckets in your account:

        ```
          loggingBucket='awsexamplebucket1-logs'
          region='us-west-2'
          
          
          # Create Logging bucket
          aws s3 mb s3://$loggingBucket --region $region
          
          aws s3api put-bucket-acl --bucket $loggingBucket --grant-write URI=http://acs.amazonaws.com/groups/s3/LogDelivery --grant-read-acp URI=http://acs.amazonaws.com/groups/s3/LogDelivery
          
          # List buckets in this account
          buckets="$(aws s3 ls | awk '{print $3}')"
          
          # Put bucket logging on each bucket
          for bucket in $buckets
              do printf '{
             "LoggingEnabled": {
                 "TargetBucket": "%s",
                 "TargetPrefix": "%s/"
                  }
              }' "$loggingBucket" "$bucket"  > logging.json
              aws s3api put-bucket-logging --bucket $bucket --bucket-logging-status file://logging.json
              echo "$bucket done"
          done
          
          rm logging.json
          
          echo "Complete"
        ```
**Note**  
This only works if all your buckets are in the same Region\. If you have buckets in multiple Regions, you must adjust the script\. 

## Querying Amazon S3 access logs for requests using Amazon Athena<a name="querying-s3-access-logs-for-requests"></a>

You can identify Amazon S3 requests with Amazon S3 access logs using Amazon Athena\. 

Amazon S3 stores server access logs as objects in an S3 bucket\. It is often easier to use a tool that can analyze the logs in Amazon S3\. Athena supports analysis of S3 objects and can be used to query Amazon S3 access logs\.

**Example**  
The following example shows how you can query Amazon S3 server access logs in Amazon Athena\.   
To specify the Amazon S3 location in an Athena query, you need the source bucket name and the source prefix, as follows: `s3://awsexamplebucket1-logs/prefix/` 

1. Open the Athena console at [https://console\.aws\.amazon\.com/athena/](https://console.aws.amazon.com/athena/home)\.

1. In the Query Editor, run a command similar to the following:

   ```
   create database s3_access_logs_db
   ```
**Note**  
It's a best practice to create the database in the same AWS Region as your S3 bucket\. 

1. In the Query Editor, run a command similar to the following to create a table schema in the database that you created in step 2\. The `STRING` and `BIGINT` data type values are the access log properties\. You can query these properties in Athena\. For `LOCATION`, enter the S3 bucket and prefix path as noted earlier\.

   ```
   CREATE EXTERNAL TABLE `s3_access_logs_db.mybucket_logs`(
     `bucketowner` STRING, 
     `bucket_name` STRING, 
     `requestdatetime` STRING, 
     `remoteip` STRING, 
     `requester` STRING, 
     `requestid` STRING, 
     `operation` STRING, 
     `key` STRING, 
     `request_uri` STRING, 
     `httpstatus` STRING, 
     `errorcode` STRING, 
     `bytessent` BIGINT, 
     `objectsize` BIGINT, 
     `totaltime` STRING, 
     `turnaroundtime` STRING, 
     `referrer` STRING, 
     `useragent` STRING, 
     `versionid` STRING, 
     `hostid` STRING, 
     `sigv` STRING, 
     `ciphersuite` STRING, 
     `authtype` STRING, 
     `endpoint` STRING, 
     `tlsversion` STRING)
   ROW FORMAT SERDE 
     'org.apache.hadoop.hive.serde2.RegexSerDe' 
   WITH SERDEPROPERTIES ( 
     'input.regex'='([^ ]*) ([^ ]*) \\[(.*?)\\] ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) (\"[^\"]*\"|-) (-|[0-9]*) ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) (\"[^\"]*\"|-) ([^ ]*)(?: ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*))?.*$') 
   STORED AS INPUTFORMAT 
     'org.apache.hadoop.mapred.TextInputFormat' 
   OUTPUTFORMAT 
     'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
   LOCATION
     's3://awsexamplebucket1-logs/prefix/'
   ```

1. In the navigation pane, under **Database**, choose your database\.

1. Under **Tables**, choose **Preview table** next to your table name\.

   In the **Results** pane, you should see data from the server access logs, such as `bucketowner`, `bucket`, `requestdatetime`, and so on\. This means that you successfully created the Athena table\. You can now query the Amazon S3 server access logs\.

**Example — show who deleted an object and when \(timestamp, IP address, and IAM user\)**  

```
SELECT RequestDateTime, RemoteIP, Requester, Key 
FROM s3_access_logs_db.mybucket_logs 
WHERE key = 'images/picture.jpg' AND operation like '%DELETE%';
```

**Example — show all operations executed by an IAM user**  

```
SELECT * 
FROM s3_access_logs_db.mybucket_logs 
WHERE requester='arn:aws:iam::123456789123:user/user_name';
```

**Example — show all operations that were performed on an object in a specific time period**  

```
SELECT *
FROM s3_access_logs_db.mybucket_logs
WHERE Key='prefix/images/picture.jpg' 
    AND parse_datetime(RequestDateTime,'dd/MMM/yyyy:HH:mm:ss Z')
    BETWEEN parse_datetime('2017-02-18:07:00:00','yyyy-MM-dd:HH:mm:ss')
    AND parse_datetime('2017-02-18:08:00:00','yyyy-MM-dd:HH:mm:ss');
```

**Example — show how much data was transferred by a specific IP address in a specific time period**  

```
SELECT SUM(bytessent) AS uploadTotal,
      SUM(objectsize) AS downloadTotal,
      SUM(bytessent + objectsize) AS Total
FROM s3_access_logs_db.mybucket_logs
WHERE RemoteIP='1.2.3.4'
AND parse_datetime(RequestDateTime,'dd/MMM/yyyy:HH:mm:ss Z')
BETWEEN parse_datetime('2017-06-01','yyyy-MM-dd')
AND parse_datetime('2017-07-01','yyyy-MM-dd');
```

**Note**  
To reduce the time that you retain your log, you can [create an Amazon S3 lifecycle policy](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-lifecycle.html) for your server access logs bucket\. Configure the lifecycle policy to remove log files periodically\. Doing so reduces the amount of data that Athena analyzes for each query\.

## Using Amazon S3 access logs to identify signature version 2 requests<a name="using-s3-access-logs-to-identify-sigv2-requests"></a>

Amazon S3 support for Signature Version 2 will be turned off \(deprecated\)\. After that, Amazon S3 will no longer accept requests that use Signature Version 2, and all requests must use *Signature Version 4* signing\. You can identify Signature Version 2 access requests using Amazon S3 access logs\. 

**Note**  
We recommend that you use AWS CloudTrail data events instead of Amazon S3 access logs\. CloudTrail data events are easier to set up and contain more information\. For more information, see [Using AWS CloudTrail to identify Amazon S3 signature version 2 requests ](cloudtrail-request-identification.md#cloudtrail-identification-sigv2-requests)\.

**Example — show all requesters that are sending signature version 2 traffic**  

```
                   SELECT requester, Sigv, Count(Sigv) as SigCount 
                   FROM s3_access_logs_db.mybucket_logs
                   GROUP BY requester, Sigv;
```

## Using Amazon S3 access logs to identify object access requests<a name="using-s3-access-logs-to-identify-objects-access"></a>

You can use queries on Amazon S3 server access logs to identify Amazon S3 object access requests, for operations such as GET, PUT, and DELETE, and discover further information about those requests\.

The following Amazon Athena query example shows how to get all PUT object requests for Amazon S3 from the server access log\. 

**Example — show all requesters that are sending PUT object requests in a certain period**  

```
SELECT Bucket, Requester, RemoteIP, Key, HTTPStatus, ErrorCode, RequestDateTime
FROM s3_access_logs_db
WHERE Operation='REST.PUT.OBJECT' AND
parse_datetime(RequestDateTime,'dd/MMM/yyyy:HH:mm:ss Z') 
BETWEEN parse_datetime('2019-07-01:00:42:42','yyyy-MM-dd:HH:mm:ss')
AND 
parse_datetime('2019-07-02:00:42:42','yyyy-MM-dd:HH:mm:ss')
```

The following Amazon Athena query example shows how to get all GET object requests for Amazon S3 from the server access log\. 

**Example — show all requesters that are sending GET object requests in a certain period**  

```
SELECT Bucket, Requester, RemoteIP, Key, HTTPStatus, ErrorCode, RequestDateTime
FROM s3_access_logs_db
WHERE Operation='REST.GET.OBJECT' AND
parse_datetime(RequestDateTime,'dd/MMM/yyyy:HH:mm:ss Z') 
BETWEEN parse_datetime('2019-07-01:00:42:42','yyyy-MM-dd:HH:mm:ss')
AND 
parse_datetime('2019-07-02:00:42:42','yyyy-MM-dd:HH:mm:ss')
```

The following Amazon Athena query example shows how to get all anonymous requests to your S3 buckets from the server access log\. 

**Example — show all anonymous requesters that are making requests to a bucket in a certain period**  

```
SELECT Bucket, Requester, RemoteIP, Key, HTTPStatus, ErrorCode, RequestDateTime
FROM s3_access_logs_db.mybucket_logs
WHERE Requester IS NULL AND
parse_datetime(RequestDateTime,'dd/MMM/yyyy:HH:mm:ss Z') 
BETWEEN parse_datetime('2019-07-01:00:42:42','yyyy-MM-dd:HH:mm:ss')
AND 
parse_datetime('2019-07-02:00:42:42','yyyy-MM-dd:HH:mm:ss')
```

**Note**  
You can modify the date range as needed to suit your needs\.
These query examples may also be useful for security monitoring\. You can review the results for `PutObject` or `GetObject` calls from unexpected or unauthorized IP addresses/requesters and for identifying any anonymous requests to your buckets\.
This query only retrieves information from the time at which logging was enabled\. 
If you are using Amazon S3 AWS CloudTrail logs, see [Using AWS CloudTrail to identify access to Amazon S3 objects](cloudtrail-request-identification.md#cloudtrail-identification-object-access)\. 

## Related resources<a name="s3-access-logs-requests-more-info"></a>
+ [Amazon S3 Server Access Log Format](LogFormat.md)
+ [Querying AWS Service Logs](https://docs.aws.amazon.com/athena/latest/ug/querying-AWS-service-logs.html)