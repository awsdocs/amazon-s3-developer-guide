# Example: Tracking an S3 Batch Operations job in Amazon EventBridge through AWS CloudTrail<a name="batch-ops-examples-event-bridge-cloud-trail"></a>

Amazon S3 Batch Operations job activity is recorded as events in AWS CloudTrail\. You can create a custom rule in Amazon EventBridge and send these events to the target notification resource of your choice, such as Amazon Simple Notification Service \(Amazon SNS\)\. 

**Note**  
Amazon EventBridge is the preferred way to manage your events\. Amazon CloudWatch Events and EventBridge are the same underlying service and API, but EventBridge provides more features\. Changes that you make in either CloudWatch or EventBridge appear in each console\. For more information, see the [Amazon EventBridge User Guide](https://docs.aws.amazon.com/eventbridge/latest/userguide/)\.

**Topics**
+ [S3 Batch Operations events emitted to CloudTrail](#batch-ops-examples-cloud-trail-events)
+ [Using an EventBridge rule for tracking S3 Batch Operations job events](#batch-ops-examples-event-bridge)

## S3 Batch Operations events emitted to CloudTrail<a name="batch-ops-examples-cloud-trail-events"></a>

When a Batch Operations job is created, it is recorded as a `JobCreated` event in CloudTrail\. As the job runs, it changes state during processing, and other `JobStatusChanged` events are recorded in CloudTrail\. You can view these events on the [CloudTrail console](https://console.aws.amazon.com/cloudtrail)\. For more information about CloudTrail, see the [AWS CloudTrail User Guide](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/how-cloudtrail-works.html)\.

**Note**  
Only S3 Batch Operations job `status-change` events are recorded in CloudTrail\.

**Example — S3 Batch Operations job completion event recorded by CloudTrail**  

```
{
    "eventVersion": "1.05",
    "userIdentity": {
        "accountId": "123456789012",
        "invokedBy": "s3.amazonaws.com"
    },
    "eventTime": "2020-02-05T18:25:30Z",
    "eventSource": "s3.amazonaws.com",
    "eventName": "JobStatusChanged",
    "awsRegion": "us-west-2",
    "sourceIPAddress": "s3.amazonaws.com",
    "userAgent": "s3.amazonaws.com",
    "requestParameters": null,
    "responseElements": null,
    "eventID": "f907577b-bf3d-4c53-b9ed-8a83a118a554",
    "readOnly": false,
    "eventType": "AwsServiceEvent",
    "recipientAccountId": "123412341234",
    "serviceEventDetails": {
        "jobId": "d6e58ec4-897a-4b6d-975f-10d7f0fb63ce",
        "jobArn": "arn:aws:s3:us-west-2:181572960644:job/d6e58ec4-897a-4b6d-975f-10d7f0fb63ce",
        "status": "Complete",
        "jobEventId": "b268784cf0a66749f1a05bce259804f5",
        "failureCodes": [],
        "statusChangeReason": []
    }
}
```

## Using an EventBridge rule for tracking S3 Batch Operations job events<a name="batch-ops-examples-event-bridge"></a>

The following example shows how to create a rule in Amazon EventBridge to capture S3 Batch Operations events recorded by AWS CloudTrail to a target of your choice\.

To do this, you create a rule by following all the steps in [Creating an EventBridge Rule That Triggers on an AWS API Call Using CloudTrail](https://docs.aws.amazon.com/eventbridge/latest/userguide/create-eventbridge-cloudtrail-rule.html)\. You paste the following S3 Batch Operations custom event pattern policy where applicable, and choose the target service of your choice\.

**S3 Batch Operations custom event pattern policy**

```
{
    "source": [
        "aws.s3"
    ],
    "detail-type": [
        "AWS Service Event via CloudTrail"
    ],
    "detail": {
        "eventSource": [
            "s3.amazonaws.com"
        ],
        "eventName": [
            "JobCreated",
            "JobStatusChanged"
        ]
    }
}
```

 The following examples are two Batch Operations events that were sent to Amazon Simple Queue Service \(Amazon SQS\) from an EventBridge event rule\. A Batch Operations job goes through many different states while processing \(`New`, `Preparing`, `Active`, etc\.\), so you can expect to receive several messages for each job\.

**Example — `JobCreated` sample event**  

```
{
    "version": "0",
    "id": "51dc8145-541c-5518-2349-56d7dffdf2d8",
    "detail-type": "AWS Service Event via CloudTrail",
    "source": "aws.s3",
    "account": "123456789012",
    "time": "2020-02-27T15:25:49Z",
    "region": "us-east-1",
    "resources": [],
    "detail": {
        "eventVersion": "1.05",
        "userIdentity": {
            "accountId": "11112223334444",
            "invokedBy": "s3.amazonaws.com"
        },
        "eventTime": "2020-02-27T15:25:49Z",
        "eventSource": "s3.amazonaws.com",
        "eventName": "JobCreated",
        "awsRegion": "us-east-1",
        "sourceIPAddress": "s3.amazonaws.com",
        "userAgent": "s3.amazonaws.com",
        "eventID": "7c38220f-f80b-4239-8b78-2ed867b7d3fa",
        "readOnly": false,
        "eventType": "AwsServiceEvent",
        "serviceEventDetails": {
            "jobId": "e849b567-5232-44be-9a0c-40988f14e80c",
            "jobArn": "arn:aws:s3:us-east-1:181572960644:job/e849b567-5232-44be-9a0c-40988f14e80c",
            "status": "New",
            "jobEventId": "f177ff24f1f097b69768e327038f30ac",
            "failureCodes": [],
            "statusChangeReason": []
        }
    }
}
```

**Example — `JobStatusChanged` sample event for when a job is complete**  

```
{
  "version": "0",
  "id": "c8791abf-2af8-c754-0435-fd869ce25233",
  "detail-type": "AWS Service Event via CloudTrail",
  "source": "aws.s3",
  "account": "123456789012",
  "time": "2020-02-27T15:26:42Z",
  "region": "us-east-1",
  "resources": [],
  "detail": {
    "eventVersion": "1.05",
    "userIdentity": {
      "accountId": "1111222233334444",
      "invokedBy": "s3.amazonaws.com"
    },
    "eventTime": "2020-02-27T15:26:42Z",
    "eventSource": "s3.amazonaws.com",
    "eventName": "JobStatusChanged",
    "awsRegion": "us-east-1",
    "sourceIPAddress": "s3.amazonaws.com",
    "userAgent": "s3.amazonaws.com",
    "eventID": "0238c1f7-c2b0-440b-8dbd-1ed5e5833afb",
    "readOnly": false,
    "eventType": "AwsServiceEvent",
    "serviceEventDetails": {
      "jobId": "e849b567-5232-44be-9a0c-40988f14e80c",
      "jobArn": "arn:aws:s3:us-east-1:181572960644:job/e849b567-5232-44be-9a0c-40988f14e80c",
      "status": "Complete",
      "jobEventId": "51f5ac17dba408301d56cd1b2c8d1e9e",
      "failureCodes": [],
      "statusChangeReason": []
    }
  }
}
```