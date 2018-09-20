# Cross\-Region Replication: Status Information<a name="crr-status"></a>

To get the cross\-region replication \(CRR\) status of the objects in a bucket, use the Amazon S3 inventory tool\. Amazon S3 sends a \.csv file to the destination bucket that you specify in the inventory configuration\. You can also use Amazon Athena to query replication status in the inventory report\. For more information about Amazon S3 inventory, see [ Amazon S3 Inventory](storage-inventory.md)\.

In CRR, you have a source bucket on which you configure replication and a destination bucket where Amazon S3 replicates objects\. When you request an object \(using `GET` object\) or object metadata \(using `HEAD` object\) from these buckets, Amazon S3 returns the `x-amz-replication-status` header in the response: 
+ When you request an object from the source bucket, Amazon S3 returns the `x-amz-replication-status` header if the object in your request is eligible for replication\. 

  For example, suppose that you specify the object prefix `TaxDocs` in your replication configuration to tell Amazon S3 to replicate only objects with the key name prefix `TaxDocs`\. Any objects that you upload that have this key name prefix—for example, `TaxDocs/document1.pdf`—will be replicated\. For object requests with this key name prefix, Amazon S3 returns the `x-amz-replication-status` header with one of the following values for the object's replication status: `PENDING`, `COMPLETED`, or `FAILED`\.
**Note**  
If object replication fails after you upload an object, you can't retry replication\. You must upload the object again\. 
+ When you request an object from the destination bucket, if the object in your request is a replica that Amazon S3 created, Amazon S3 returns the `x-amz-replication-status` header with the value `REPLICA`\.

You can find the object replication status in the console, with the AWS Command Line Interface \(AWS CLI\), or with the AWS SDK\. 
+ Console: Choose the object,then choose **Properties** to view object properties, including replication status\. 
+ AWS CLI: Use the `head-object` AWS CLI command to retrieve object metadata:

  ```
  aws s3api head-object --bucket source-bucket --key object-key --version-id object-version-id           
  ```

  The command returns object metadata, including the `ReplicationStatus` as shown in the following example response:

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
+ AWS SDKs: The following code fragments get replication status with the AWS SDK for Java and AWS SDK for \.NET, respectively\. 
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
If lifecycle configuration is enabled on the source bucket, Amazon S3 puts suspends lifecycle actions until it marks the objects status as either `COMPLETED` or `FAILED`\.

## Related Topics<a name="crr-status-related-topics"></a>

[Cross\-Region Replication ](crr.md)