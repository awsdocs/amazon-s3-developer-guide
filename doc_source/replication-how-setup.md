# Overview of setting up replication<a name="replication-how-setup"></a>

To enable replication, you simply add a replication configuration to your source bucket\. The configuration tells Amazon S3 to replicate objects as specified\. In the replication configuration, you must provide the following:
+ The destination bucket — The bucket where you want Amazon S3 to replicate the objects\.

   
+ The objects that you want to replicate — You can replicate all of the objects in the source bucket or a subset\. You identify a subset by providing a [key name prefix](https://docs.aws.amazon.com/general/latest/gr/glos-chap.html#keyprefix), one or more object tags, or both in the configuration\.

   For example, if you configure a replication rule to replicate only objects with the key name prefix `Tax/`, Amazon S3 replicates objects with keys such as `Tax/doc1` or `Tax/doc2`\. But it doesn't replicate an object with the key `Legal/doc3`\. If you specify both prefix and one or more tags, Amazon S3 replicates only objects having the specific key prefix and tags\.

A replica has the same key names and metadata \(for example, creation time, user\-defined metadata, and version ID\) as the original object\. Amazon S3 encrypts all data in transit using Secure Sockets Layer \(SSL\)\. 

In addition to these minimum requirements, you can choose the following options: 
+ By default, Amazon S3 stores object replicas using the same storage class as the source object\. You can specify a different storage class for the replicas\.

   
+ Amazon S3 assumes that an object replica continues to be owned by the owner of the source object\. So when it replicates objects, it also replicates the corresponding object access control list \(ACL\)\. If the source and destination buckets are owned by different AWS accounts, you can configure replication to change the owner of a replica to the AWS account that owns the destination bucket\.

Additional configuration options are available\. For more information, see [Additional replication configurations](replication-additional-configs.md)\.

Amazon S3 also provides APIs to support setting up replication rules\. For more information, see the following topics in the *Amazon Simple Storage Service API Reference*:
+  [PUT Bucket replication](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTreplication.html) 
+  [GET Bucket replication](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETreplication.html) 
+  [DELETE Bucket replication](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEreplication.html) 

Instead of making these API calls directly from your code, you can add a replication configuration to a bucket with the AWS SDK, AWS CLI, or the Amazon S3 console\. It's easiest to use the console\. For examples with step\-by\-step instructions, see [Replication walkthroughs](replication-example-walkthroughs.md)\. 

If you are new to replication configurations, we recommend that you read the following sections before exploring the examples and optional configurations\. For examples that provide step\-by\-step instructions for setting up basic replication configurations, see [Replication configuration overview](replication-add-config.md)\. 

**Topics**
+ [Replication configuration overview](replication-add-config.md)
+ [Setting up permissions for replication](setting-repl-config-perm-overview.md)