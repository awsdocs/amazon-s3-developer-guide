# Example 5: S3 Replication Time Control \(S3 RTC\) configuration<a name="replication-walkthrough-5"></a>

S3 Replication Time Control \(S3 RTC\) helps you meet compliance or business requirements for data replication and provides visibility into Amazon S3 replication times\. S3 RTC replicates most objects that you upload to Amazon S3 in seconds, and 99\.99 percent of those objects within 15 minutes\. 

With S3 RTC, you can monitor the total number and size of objects that are pending replication, and the maximum replication time to the destination Region\. Replication metrics are available through the [AWS Management Console](https://console.aws.amazon.com/s3/) and [Amazon CloudWatch User Guide](https://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/);\. For more information, see [Amazon S3 CloudWatch replication metrics](cloudwatch-monitoring.md#s3-cloudwatch-replication-metrics) 

**Topics**

## Enable S3 RTC on the Amazon S3 console<a name="replication-ex5-console"></a>

For step\-by\-step instructions, see [How Do I Add a Replication Rule to an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-replication.html) in the *Amazon Simple Storage Service Console User Guide*\. This topic provides instructions for enabling S3 RTC in your replication configuration when buckets are owned by same and different AWS accounts\.

## Replicate objects with Amazon S3 replication time Control\(AWS CLI\)<a name="replication-ex5-cli"></a>

To replicate objects with S3 RTC enabled onwith the AWS CLI, you create buckets, enable versioning on the buckets, create an IAM role that gives Amazon S3 permission to replicate objects, and add the replication configuration to the source bucket\. The replication configuration needs to have S3 Replication Time Control \(S3 RTC\) enabled\. 

**To replicate with S3 RTC enabled \(AWS CLI\)**
+ In this example, we set `ReplicationTime` and `Metric` and add replication configuration to the source bucket\.

  ```
  {
      "Rules": [
          {
              "Status": "Enabled",
              "Filter": {
                  "Prefix": "Tax"
              },
              "DeleteMarkerReplication": {
                  "Status": "Disabled"
              },
              "Destination": {
                  "Bucket": "arn:aws:s3:::destination",
                  "Metrics": {
                      "Status": "Enabled",
                      "EventThreshold": {
                          "Minutes": 15
                      }
                  },
                  "ReplicationTime": {
                      "Status": "Enabled",
                      "Time": {
                          "Minutes": 15
                      }
                  }
              },
              "Priority": 1
          }
      ],
      "Role": "IAM-Role-ARN"
  }
  ```
**Important**  
 `Metrics:EventThreshold:Minutes` and `ReplicationTime:Time:Minutes` can only have 15 is a valid value\. 

## Replicate objects with Amazon S3 replication time control \(AWS SDK\)<a name="replication-ex5-sdk"></a>

 Below is a java example to add replication configuration with S3 Replication Time Control \(S3 RTC\):

```
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.DeleteMarkerReplication;
import software.amazon.awssdk.services.s3.model.Destination;
import software.amazon.awssdk.services.s3.model.Metrics;
import software.amazon.awssdk.services.s3.model.MetricsStatus;
import software.amazon.awssdk.services.s3.model.PutBucketReplicationRequest;
import software.amazon.awssdk.services.s3.model.ReplicationConfiguration;
import software.amazon.awssdk.services.s3.model.ReplicationRule;
import software.amazon.awssdk.services.s3.model.ReplicationRuleFilter;
import software.amazon.awssdk.services.s3.model.ReplicationTime;
import software.amazon.awssdk.services.s3.model.ReplicationTimeStatus;
import software.amazon.awssdk.services.s3.model.ReplicationTimeValue;

public class Main {

  public static void main(String[] args) {
    S3Client s3 = S3Client.builder()
      .region(Region.US_EAST_1)
      .credentialsProvider(() -> AwsBasicCredentials.create(
          "AWS_ACCESS_KEY_ID",
          "AWS_SECRET_ACCESS_KEY")
      )
      .build();

    ReplicationConfiguration replicationConfig = ReplicationConfiguration
      .builder()
      .rules(
          ReplicationRule
            .builder()
            .status("Enabled")
            .priority(1)
            .deleteMarkerReplication(
                DeleteMarkerReplication
                    .builder()
                    .status("Disabled")
                    .build()
            )
            .destination(
                Destination
                    .builder()
                    .bucket("destination_bucket_arn")
                    .replicationTime(
                        ReplicationTime.builder().time(
                            ReplicationTimeValue.builder().minutes(15).build()
                        ).status(
                            ReplicationTimeStatus.ENABLED
                        ).build()
                    )
                    .metrics(
                        Metrics.builder().eventThreshold(
                            ReplicationTimeValue.builder().minutes(15).build()
                        ).status(
                            MetricsStatus.ENABLED
                        ).build()
                    )
                    .build()
            )
            .filter(
                ReplicationRuleFilter
                    .builder()
                    .prefix("testtest")
                    .build()
            )
        .build())
        .role("role_arn")
        .build();

    // Put replication configuration
    PutBucketReplicationRequest putBucketReplicationRequest = PutBucketReplicationRequest
      .builder()
      .bucket("source_bucket")
      .replicationConfiguration(replicationConfig)
      .build();

    s3.putBucketReplication(putBucketReplicationRequest);
  }
}
```

For more information, see [Meet compliance requirements using S3 Replication Time Control \(S3 RTC\)](replication-time-control.md)\. 