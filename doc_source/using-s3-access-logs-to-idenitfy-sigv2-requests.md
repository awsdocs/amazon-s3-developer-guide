# Using Amazon S3 Access Logs to Identify Signature Version 2 Requests<a name="using-s3-access-logs-to-idenitfy-sigv2-requests"></a>

Amazon S3 support for Signature Version 2 will be turned off \(deprecated\)\. After that, Amazon S3 will no longer accept requests that use Signature Version 2, and all requests must use *Signature Version 4* signing\. You can identify Signature Version 2 access requests using Amazon S3 access logs\. 

**Note**  
We recommend that you use AWS CloudTrail data events instead of Amazon S3 access logs\. CloudTrail data events are easier to set up and contain more information\. For more information, see [Using AWS CloudTrail to Identify Amazon S3 Signature Version 2 Requests](cloudtrail-identification-sigV2.md)\.
Depending on how many access requests you get, you might get too much data to analyze easily\.

**Topics**
+ [Enabling Amazon S3 Access Logs for Signature Version 2 Requests](#enabling-s3-access-logs-for-sigv2)
+ [Querying Amazon S3 Access Logs for Signature Version 2 Requests](#enabling-s3-access-logs-for-sigv2)
+ [Related Resources](#s3-access-logs-sigv2-more-info)

## Enabling Amazon S3 Access Logs for Signature Version 2 Requests<a name="enabling-s3-access-logs-for-sigv2"></a>

We recommend that you create a dedicated logging bucket in each AWS Region that you have S3 buckets in\. Then have the Amazon S3 access log delivered to that S3 bucket\.

**Example — Enabling access logs with five buckets across two Regions**  
In this example, you have the following five buckets:  
+ `1-awsexamplebucket-us-east-1`
+ `2-awsexamplebucket-us-east-1`
+ `3-awsexamplebucket-us-east-1`
+ `1-awsexamplebucket-us-west-2`
+ `2-awsexamplebucket-us-west-2`

1. Create two logging buckets in the following Regions:
   + `awsexamplebucket-logs-us-east-1`
   + `awsexamplebucket-logs-us-west-2`

1. Then enable the Amazon S3 access logs as follows:
   + `1-awsexamplebucket-us-east-1` logs to `s3://awsexamplebucket-logs-us-east-1/1-awsexamplebucket-us-east-1`
   + `2-awsexamplebucket-us-east-1` logs to `s3://awsexamplebucket-logs-us-east-1/2-awsexamplebucket-us-east-1`
   + `1-awsexamplebucket-us-east-1` logs to `s3://awsexamplebucket-logs-us-east-1/3-awsexamplebucket-us-east-1`
   + `1-awsexamplebucket-us-west-2` logs to ` s3://awsexamplebucket-logs-us-west-2/1-awsexamplebucket-us-west-2`
   + `2-awsexamplebucket-us-west-2` logs to `s3://awsexamplebucket-logs-us-west-2/2-awsexamplebucket-us-west-2`

1. You can then enable the Amazon S3 access logs using the following methods:
   + Using the [AWS Management Console](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/server-access-logging.html)
   + [Enabling Logging Programmatically](enable-logging-programming.md) 
   + Using the [AWS CLI put\-bucket\-logging command](https://docs.aws.amazon.com//cli/latest/reference/s3api/put-bucket-logging.html) to programmatically enable access logs on a bucket using the following commands:

     1. First, grant Amazon S3 permission using `put-bucket-acl`:

        ```
                                                aws s3api put-bucket-acl --bucket awsexamplebucket-logs  --grant-write URI=http://acs.amazonaws.com/groups/s3/LogDelivery --grant-read-acp URI=http://acs.amazonaws.com/groups/s3/LogDelivery 
        ```

     1. Then, apply the logging policy:

        ```
                                                aws s3api put-bucket-logging --bucket awsexamplebucket --bucket-logging-status file://logging.json 
        ```

        `Logging.json` is a JSON document in the current folder that contains the logging policy:

        ```
                                                {
                                                    "LoggingEnabled": {
                                                        "TargetBucket": "awsexamplebucket-logs",
                                                        "TargetPrefix": "awsexamplebucket/",
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
                                                    loggingBucket='awsexamplebucket-logs'
                                                    region='us-west-2'
                                                    
                                                    
                                                    # Create Logging bucket
                                                    aws s3 mb s3://$loggingBucket --region $region
                                                    
                                                    aws s3api put-bucket-acl --bucket $loggingBucket --grant-write URI=http://acs.amazonaws.com/groups/s3/LogDelivery --grant-read-acp URI=http://acs.amazonaws.com/groups/s3/LogDelivery
                                                    
                                                    # List buckets in this account
                                                    buckets="$(aws s3 ls | awk '{print $3}')"
                                                    
                                                    # Put bucket logging on each bucket
                                                    for bucket in $bucenable-logging-programmingkets
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

## Querying Amazon S3 Access Logs for Signature Version 2 Requests<a name="enabling-s3-access-logs-for-sigv2"></a>

Amazon S3 stores server access logs as objects in an S3 bucket\. Logs can't be analyzed easily in Amazon S3\. Athena supports analysis of S3 objects and can be used to query Amazon S3 access logs\.

**Example**  
The following example shows how you can query Amazon S3 server access logs in Amazon Athena\.   
To specify the Amazon S3 location in an Athena query, you need the target bucket name and the target prefix, as follows: `s3://awsexamplebucket-logs/prefix/` 

1. Open the Athena console at [https://console\.aws\.amazon\.com/athena/](https://console.aws.amazon.com/athena/home)\.

1. In the Query Editor, run a command similar to the following:

   ```
   create database s3_access_logs_db
   ```
**Note**  
It's a best practice to create the database in the same AWS Region as your S3 bucket\. 

1. In the Query Editor, run a command similar to the following to create a table schema in the database that you created in step 2\. The `STRING` and `BIGINT` data type values are the access log properties\. You can query these properties in Athena\. For `LOCATION`, enter the S3 bucket and prefix path as noted earlier\.

   ```
                                CREATE EXTERNAL TABLE IF NOT EXISTS s3_access_logs_db.mybucket_logs(
                                   BucketOwner STRING,
                                   Bucket STRING,
                                   RequestDateTime STRING,
                                   RemoteIP STRING,
                                   Requester STRING,
                                   RequestID STRING,
                                   Operation STRING,
                                   Key STRING,
                                   RequestURI_operation STRING,
                                   RequestURI_key STRING,
                                   RequestURI_httpProtoversion STRING,
                                   HTTPstatus STRING,
                                   ErrorCode STRING,
                                   BytesSent BIGINT,
                                   ObjectSize BIGINT,
                                   TotalTime STRING,
                                   TurnAroundTime STRING,
                                   Referrer STRING,
                                   UserAgent STRING,
                                   VersionId STRING,
                                   HostId STRING,
                                   SigV STRING,
                                   CipherSuite STRING,
                                   AuthType STRING,
                                   EndPoint STRING
                               ) 
                               ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.RegexSerDe'
                               WITH SERDEPROPERTIES (
                                   'serialization.format' = '1', 'input.regex' = '([^ ]*) ([^ ]*) 
                                   \\[(.*?)\\] ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) 
                                   \\\"([^ ]*) ([^ ]*) (- |[^ ]*)\\\" (-|[0-9]*) ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) 
                                   ([^ ]*) (\"[^\"]*\") ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*).*$' )
                                   LOCATION 's3://awsexamplebucket-logs/prefix'
   ```

1. In the navigation pane, under **Database**, choose your database\.

1. Under **Tables**, choose **Preview table** next to your table name\.

   In the **Results** pane, you should see data from the server access logs, such as `bucketowner`, `bucket`, `requestdatetime`, and so on\. This means that you successfully created the Athena table\. You can now query the Amazon S3 server access logs\.

**Example — Find the log for a deleted object**  

```
                    SELECT * 
                    FROM s3_access_logs_db.mybucket_logs 
                    WHERE key = 'images/picture.jpg' AND operation like '%DELETE%';
```

**Example — Show who deleted an object and when \(timestamp, IP address, and IAM user\):**  

```
                    SELECT RequestDateTime, RemoteIP, Requester, Key 
                    FROM s3_access_logs_db.mybucket_logs 
                    WHERE key = 'images/picture.jpg' AND operation like '%DELETE%';
```

**Example — Show all operations executed by an IAM user**  

```
                    SELECT * 
                    FROM s3_access_logs_db.mybucket_logs 
                    WHERE requester='arn:aws:iam::123456789123:user/user_name';
```

**Example — Show all operations that were performed on an object in a specific time period**  

```
                    SELECT *
                    FROM s3_access_logs_db.mybucket_logs
                    WHERE Key='prefix/images/picture.jpg' 
                        AND parse_datetime(RequestDateTime,'dd/MMM/yyyy:HH:mm:ss Z')
                        BETWEEN parse_datetime('2017-02-18:07:00:00','yyyy-MM-dd:HH:mm:ss')
                        AND parse_datetime('2017-02-18:08:00:00','yyyy-MM-dd:HH:mm:ss');
```

**Example — Show how much data was transferred by a specific IP address in a specific time period**  

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

**Example — Show all requesters that are sending Signature Version 2 traffic**  

```
                   SELECT requester, Sigv, Count(Sigv) as SigCount 
                   FROM s3_access_logs_db.mybucket_logs
                   GROUP BY requester, Sigv;
```

To reduce the time that you retain your log, you can [create an Amazon S3 lifecycle policy](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-lifecycle.html) for your server access logs bucket\. Configure the lifecycle policy to remove log files periodically\. Doing so reduces the amount of data that Athena analyzes for each query\.

## Related Resources<a name="s3-access-logs-sigv2-more-info"></a>
+ [Amazon S3 Server Access Log Format](LogFormat.md)
+ [Querying AWS Service Logs](https://docs.aws.amazon.com/athena/latest/ug/querying-AWS-service-logs.html)