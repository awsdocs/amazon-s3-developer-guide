# Replicating metadata changes with Amazon S3 replica modification sync<a name="replication-for-metadata-changes"></a>

Amazon S3 replica modification sync can help you keep object metadata such as tags, ACLs, and Object Lock settings replicated between replicas and source objects\. By default, Amazon S3 replicates metadata from the source objects to the replicas only\. When replica modification sync is enabled, Amazon S3 replicates metadata changes made to the replica copies back to the source object, making the replication bidirectional\.

## Enabling replica modification sync<a name="enabling-replication-for-metadata-changes"></a>

You can use Amazon S3 replica modification sync with new or existing replication rules\. You can apply it to an entire S3 bucket or to Amazon S3 objects that have a specific prefix\.

To enable replica modification sync using the Amazon S3 console, see [How do I add a replication rule to an S3 bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-replication.html) in the *Amazon Simple Storage Service Console User Guide*\. This topic provides instructions for enabling replica modification sync in your replication configuration when buckets are owned by the same or different AWS accounts\.

To enable replica modification sync using the AWS Command Line Interface \(AWS CLI\), you must add a replication configuration to the bucket containing the replicas with `ReplicaModifications` enabled\. To make replication bidirectional enable replica modification sync on the bucket containing the replicas and the bucket containing the source objects\. 

In the following example configuration, Amazon S3 replicates metadata changes under the prefix *Tax* to the bucket *DOC\-EXAMPLE\-BUCKET*, which would contain the source objects\.

```
{
    "Rules": [
        {
            "Status": "Enabled",
            "Filter": {
                "Prefix": "Tax"
            },
            "SourceSelectionCriteria": {
                "ReplicaModifications":{
                    "Status": "Enabled"
                }
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

For full instructions on creating replication rules using the AWS CLI, see [Example 1: Configuring replication when the source and destination buckets are owned by the same account](https://docs.aws.amazon.com/AmazonS3/latest/dev/replication-walkthrough1.html) in the [Replication walkthroughs](https://docs.aws.amazon.com/AmazonS3/latest/dev/replication-example-walkthroughs.html) section\.