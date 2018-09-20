# Overview of Setting Up CRR<a name="crr-how-setup"></a>

To enable cross\-region replication \(CRR\), you add a replication configuration to your source bucket\. The configuration tells Amazon S3 to replicate objects as specified\. In the replication configuration, you must provide the following:
+ The destination bucket—The bucket where you want Amazon S3 to replicate the objects\.

   
+ The objects that you want to replicate—You can replicate all of the objects in the source bucket or a subset\. You identify subset by providing a key name prefix, one or more object tags, or both in the configuration\. For example, if you configure cross\-region replication to replicate only objects with the key name prefix `Tax/`, Amazon S3 replicates objects with keys such as `Tax/doc1` or `Tax/doc2`, but not an object with the key `Legal/doc3`\. If you specify both prefix and one or more tags, Amazon S3 replicates only objects having specific key prefix and the tags\.

A replica has the same key names and metadata \(for example, creation time, user\-defined metadata, and version ID\) as the original object\. Amazon S3 encrypts all data in transit across AWS Regions using Secure Sockets Layer \(SSL\)\. 

In addition to these minimum requirements, you can choose the following options: 
+ By default, Amazon S3 stores object replicas using the same storage class as the source object\. You can specify a different storage class for the replicas\.

   
+ Because it assumes that an object replica continues to be owned by the owner of the source object, when Amazon S3 replicates objects, it also replicates the corresponding object access control list \(ACL\)\. If the source and destination buckets are owned by different AWS accounts, you can configure CRR to change the owner of a replica to the AWS account that owns the destination bucket\.

Additional configuration options are available\. For more information, see [Additional CRR Configurations](crr-additional-configs.md)\.

**Important**  
If you have an object expiration lifecycle policy in your non\-versioned bucket and you want to maintain the same permanent delete behavior when you enable versioning, you must add a noncurrent expiration policy\. The noncurrent expiration lifecycle policy will manage the deletes of the noncurrent object versions in the version\-enabled bucket\. \(A version\-enabled bucket maintains one current and zero or more noncurrent object versions\.\) For more information, see [ How Do I Create a Lifecycle Policy for an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-lifecycle.html) in the *Amazon Simple Storage Service Console User Guide*\. 

Amazon S3 provides APIs in support of the cross\-region replication\. For more information, see the following topics in the *Amazon Simple Storage Service API Reference*\.:
+  [PUT Bucket replication](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTreplication.html) 
+  [GET Bucket replication](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETreplication.html) 
+  [DELETE Bucket replication](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEreplication.html) 

Instead of making these API calls directly from your code, you can add a replication configuration to a bucket with the AWS SDK, AWS CLI, or the Amazon S3 console\. It's easiest to use the console\. For examples with step\-by\-step instructions, see [Cross\-Region Replication \(CRR\) Walkthroughs](crr-example-walkthroughs.md)\.

If you are new to CRR configuration, we recommend that you read the following overviews before  exploring the examples and optional configurations\. The examples provide step\-by\-step instructions for setting up basic CRR configurations\. For more information, see [Cross\-Region Replication \(CRR\) Walkthroughs](crr-example-walkthroughs.md)\. 

**Topics**
+ [Replication Configuration Overview](crr-add-config.md)
+ [Setting Up Permissions for CRR](setting-repl-config-perm-overview.md)