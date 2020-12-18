# Replication status information<a name="replication-status"></a>

Replication status can help you determine the current state of an object being replicated\. The replication status of a source object will return either `PENDING`, `COMPLETED`, or `FAILED`\. The replication status of a replica will return `REPLICA`\.

**Topics**
+ [Replication status overview](#replication-status-overview)
+ [Replication status if replicating to multiple destination buckets](#replication-status-multiple-destinations)
+ [Replication status if Amazon S3 replica modification sync is enabled](#replication-status-multiple-destinations)
+ [Finding replication status](#replication-status-usage)

## Replication status overview<a name="replication-status-overview"></a>

In replication, you have a source bucket on which you configure replication and destination where Amazon S3 replicates objects\. When you request an object \(using `GET` object\) or object metadata \(using `HEAD` object\) from these buckets, Amazon S3 returns the `x-amz-replication-status` header in the response: 
+ When you request an object from the source bucket, Amazon S3 returns the `x-amz-replication-status` header if the object in your request is eligible for replication\. 

  For example, suppose that you specify the object prefix `TaxDocs` in your replication configuration to tell Amazon S3 to replicate only objects with the key name prefix `TaxDocs`\. Any objects that you upload that have this key name prefix—for example, `TaxDocs/document1.pdf`—will be replicated\. For object requests with this key name prefix, Amazon S3 returns the `x-amz-replication-status` header with one of the following values for the object's replication status: `PENDING`, `COMPLETED`, or `FAILED`\.
**Note**  
If object replication fails after you upload an object, you can't retry replication\. You must upload the object again\. Objects transition to a `FAILED` state for issues such as missing replication role permissions, AWS KMS permissions, or bucket permissions\. For temporary failures, such as if a bucket or Region is unavailable, replication status will not transition to `FAILED`, but will remain `PENDING`\. After the resource is back online, S3 will resume replicating those objects\.
+ When you request an object from a destination bucket, if the object in your request is a replica that Amazon S3 created, Amazon S3 returns the `x-amz-replication-status` header with the value `REPLICA`\.

## Replication status if replicating to multiple destination buckets<a name="replication-status-multiple-destinations"></a>

When you replicate objects to multiple destination buckets, the `x-amz-replication-status` header acts differently\. The header of the source object only returns a value of `COMPLETED` when replication is successful to all destinations\. The header remains at the `PENDING` value until replication has completed for all destinations\. If one or more destinations fail replication, the header returns `FAILED`\.

## Replication status if Amazon S3 replica modification sync is enabled<a name="replication-status-multiple-destinations"></a>

When your replication rules enable Amazon S3 replica modification sync replicas can report statuses other than `REPLICA`\. If metadata changes are in the process of replicating the `x-amz-replication-status` header will return `PENDING`\. replica modification sync fails to replicate metadata the header will return `FAILED`\. If metadata is replicated correctly the replicas will return header `REPLICA`\.

## Finding replication status<a name="replication-status-usage"></a>

To get the replication status of the objects in a bucket, you can use the Amazon S3 inventory tool\. Amazon S3 sends a CSV file to the destination bucket that you specify in the inventory configuration\. You can also use Amazon Athena to query the replication status in the inventory report\. For more information about Amazon S3 inventory, see [ Amazon S3 inventory](storage-inventory.md)\.

You can also find the object replication status using the console, the AWS Command Line Interface \(AWS CLI\), or the AWS SDK\. 
+ **Console** – Select the object, and under the **Overview** header, view the object properties, including replication status\. 
+ **AWS CLI** – Use the `head-object` command to retrieve object metadata, as follows\.

  ```
  aws s3api head-object --bucket source-bucket --key object-key --version-id object-version-id           
  ```

  The command returns object metadata, including the `ReplicationStatus` as shown in the following example response\.

  ```
  {
     "AcceptRanges":"bytes",
     "ContentType":"image/jpeg",
     "LastModified":"Mon, 23 Mar 2015 21:02:29 GMT",
     "ContentLength":3191,
     "ReplicationStatus":"COMPLETED",
     "VersionId":"jfnW.HIMOfYiD_9rGbSkmroXsFj3fqZ.",
     "ETag":"\"6805f2cfc46c0f04559748bb039d69ae\"",
     "Metadata":{
  
     }
  }
  ```
+ **AWS SDKs** – The following code examples get replication status using the AWS SDK for Java and AWS SDK for \.NET, respectively\. 
  + AWS SDK for Java

    ```
    GetObjectMetadataRequest metadataRequest = new GetObjectMetadataRequest(bucketName, key);
    ObjectMetadata metadata = s3Client.getObjectMetadata(metadataRequest);
    
    System.out.println("Replication Status : " + metadata.getRawMetadataValue(Headers.OBJECT_REPLICATION_STATUS));
    ```
  + AWS SDK for \.NET

    ```
    GetObjectMetadataRequest getmetadataRequest = new GetObjectMetadataRequest
        {
             BucketName = sourceBucket,
             Key        = objectKey
        };
    
    GetObjectMetadataResponse getmetadataResponse = client.GetObjectMetadata(getmetadataRequest);
    Console.WriteLine("Object replication status: {0}", getmetadataResponse.ReplicationStatus);
    ```

**Note**  
Before deleting an object from a source bucket that has replication enabled, check the object's replication status to ensure that the object has been replicated\.   
If lifecycle configuration is enabled on the source bucket, Amazon S3 suspends lifecycle actions until it marks the objects status as either `COMPLETED` or `FAILED`\.

### Related topics<a name="replication-status-related-topics"></a>

[Replication](replication.md)