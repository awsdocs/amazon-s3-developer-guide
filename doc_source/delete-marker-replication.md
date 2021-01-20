# Replicating delete markers between buckets<a name="delete-marker-replication"></a>

By default, when Amazon S3 Replication is enabled and an object is deleted in the source bucket, Amazon S3 adds a delete marker in the source bucket only\. This action protects data from malicious deletions\. 

If you have *delete marker replication* enabled, these markers are copied to the destination buckets, and Amazon S3 behaves as if the object was deleted in both source and destination buckets\. For more information about how delete markers work, see [Working with delete markers](https://docs.aws.amazon.com/AmazonS3/latest/dev/DeleteMarker.html)\.

**Note**  
Delete marker replication is not supported for tag\-based replication rules\. Delete marker replication also does not adhere to the 15\-minute SLA granted when using S3 Replication Time Control\.

If you are not using the latest replication configuration version, delete operations will affect replication differently\. For more information, see [How delete operations affect replication](https://docs.aws.amazon.com/AmazonS3/latest/dev/Replication.html#replication-delete-op)\.

## Enabling delete marker replication<a name="enabling-delete-marker-replication"></a>

You can start using delete marker replication with a new or existing replication rule\. You can apply it to an entire S3 bucket or to Amazon S3 objects that have a specific prefix\.

To enable delete marker replication using the Amazon S3 console, see [How do I add a replication rule to an S3 bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-replication.html) in the *Amazon Simple Storage Service Console User Guide*\. This topic provides instructions for enabling delete marker replication in your replication configuration when buckets are owned by the same or different AWS accounts\.

To enable delete marker replication using the AWS Command Line Interface \(AWS CLI\), you must add a replication configuration to the source bucket with `DeleteMarkerReplication` enabled\. 

In the following example configuration, delete markers are replicated to the destination bucket *DOC\-EXAMPLE\-BUCKET* for objects under the prefix *Tax*\.

```
{
    "Rules": [
        {
            "Status": "Enabled",
            "Filter": {
                "Prefix": "Tax"
            },
            "DeleteMarkerReplication": {
                "Status": "Enabled"
            },
            "Destination": {
                "Bucket": "arn:aws:s3:::DOC-EXAMPLE-BUCKET"
            },
            "Priority": 1
        }
    ],
    "Role": "IAM-Role-ARN"
}
```

For full instructions on creating replication rules through the AWS CLI, see [Example 1: Configuring replication when the source and destination buckets are owned by the same account](https://docs.aws.amazon.com/AmazonS3/latest/dev/replication-walkthrough1.html) in the [Replication walkthroughs](https://docs.aws.amazon.com/AmazonS3/latest/dev/replication-example-walkthroughs.html) section\.