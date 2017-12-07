# Finding the Cross\-Region Replication Status<a name="crr-status"></a>

You can use the Amazon S3 inventory feature to get replication status of all objects in a bucket\. Amazon S3 then delivers a \.csv file to the configured destination bucket\. For more information about Amazon S3 inventory, see [ Amazon S3 Inventory](storage-inventory.md)\.

If you want to get CRR status of a single object, read the following:

In cross\-region replication, you have a source bucket on which you configure replication and a destination bucket where Amazon S3 replicates objects\. When you request an object \(`GET` object\) or object metadata \(`HEAD` object\) from these buckets, Amazon S3 returns the `x-amz-replication-status` header in the response as follows: 

+ If requesting an object from the source bucket – Amazon S3 returns the `x-amz-replication-status` header if the object in your request is eligible for replication\. 

  For example, suppose that in your replication configuration, you specify the object prefix `TaxDocs` requesting Amazon S3 to replicate objects with the key name prefix `TaxDocs`\. Then, any objects you upload with this key name prefix—for example, `TaxDocs/document1.pdf`—are eligible for replication\. For any object request with this key name prefix, Amazon S3 returns the `x-amz-replication-status` header with one of the following values for the object's replication status: `PENDING`, `COMPLETED`, or `FAILED`\.

+ If requesting an object from the destination bucket – Amazon S3 returns the `x-amz-replication-status` header with value `REPLICA` if the object in your request is a replica that Amazon S3 created\.

You can find the object replication state in the console using the AWS CLI, or programmatically using the AWS SDK\. 

+ In the console, you choose the object and choose **Properties** to view object properties, including the replication status\. 

+ You can use the `head-object` AWS CLI command as shown to retrieve object metadata information:

  ```
  aws s3api head-object --bucket source-bucket --key object-key --version-id object-version-id           
  ```

  The command returns object metadata information including the `ReplicationStatus` as shown in the following example response:

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

+ You can use the AWS SDKs to retrieve the replication state of an object\. Following are code fragments using AWS SDK for Java and AWS SDK for \.NET\. 

  + AWS SDK for Java

    ```
    GetObjectMetadataRequest metadataRequest = new GetObjectMetadataRequest(bucketName, bucketName);
    metadataRequest.setKey(key);
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
If you decide to delete an object from a source bucket that has replication enabled, you should check the replication status of the object before deletion to ensure that the object has been replicated\.   
If lifecycle configuration is enabled on the source bucket, Amazon S3 puts any lifecycle actions on hold until it marks the objects status as either `COMPLETED` or `FAILED`\.

## Related Topics<a name="crr-status-related-topics"></a>

[Cross\-Region Replication \(CRR\)](crr.md)