# Setting up permissions for replication<a name="setting-repl-config-perm-overview"></a>

When setting up replication, you must acquire necessary permissions as follows:
+ Create an IAM role—Amazon S3 needs permissions to replicate objects on your behalf\. You grant these permissions by creating an IAM role and specify the role in your replication configuration\.
+ When source and destination buckets aren't owned by the same accounts, the owner of the destination bucket must grant the source bucket owner permissions to store the replicas\.

**Topics**
+ [Creating an IAM role](#setting-repl-config-same-acctowner)
+ [Granting permissions when source and destination buckets are owned by different AWS accounts](#setting-repl-config-crossacct)

## Creating an IAM role<a name="setting-repl-config-same-acctowner"></a>

By default, all Amazon S3 resources—buckets, objects, and related subresources—are private: Only the resource owner can access the resource\. To read objects from the source bucket and replicate them to the destination bucket, Amazon S3 needs permissions to perform these tasks\. You grant these permissions by creating an IAM role and specifying the role in your replication configuration\. 

This section explains the trust policy and minimum required permissions policy\. The example walkthroughs provide step\-by\-step instructions to create an IAM role\. For more information, see [Replication walkthroughs](replication-example-walkthroughs.md)\.
+ The following shows a *trust policy*, where you identify Amazon S3 as the service principal who can assume the role\.

  ```
  {
  
     "Version":"2012-10-17",
     "Statement":[
        {
           "Effect":"Allow",
           "Principal":{
              "Service":"s3.amazonaws.com"
           },
           "Action":"sts:AssumeRole"
        }
     ]
  }
  ```

  For more information about IAM roles, see [IAM Roles](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html) in the *IAM User Guide*\.
+ The following shows an *access policy*, where you grant the role permissions to perform replication tasks on your behalf\. When Amazon S3 assumes the role, it has the permissions that you specify in this policy\.

  ```
  {
  
     "Version":"2012-10-17",
     "Statement":[
        {
           "Effect":"Allow",
           "Action":[
              "s3:GetReplicationConfiguration",
              "s3:ListBucket"
           ],
           "Resource":[
              "arn:aws:s3:::SourceBucket"
           ]
        },
        {
           "Effect":"Allow",
           "Action":[
  
              "s3:GetObjectVersion",
              "s3:GetObjectVersionAcl",
              "s3:GetObjectVersionTagging"
  
           ],
           "Resource":[
              "arn:aws:s3:::SourceBucket/*"
           ]
        },
        {
           "Effect":"Allow",
           "Action":[
              "s3:ReplicateObject",
              "s3:ReplicateDelete",
              "s3:ReplicateTags"
           ],
           "Resource":"arn:aws:s3:::DestinationBucket/*"
        }
     ]
  }
  ```

  The access policy grants permissions for the following actions:
  +  `s3:GetReplicationConfiguration` and `s3:ListBucket`—Permissions for these actions on the *source* bucket allow Amazon S3 to retrieve the replication configuration and list bucket content \(the current permissions model requires the `s3:ListBucket` permission for accessing delete markers\)\.
  + `s3:GetObjectVersion` and `s3:GetObjectVersionAcl`— Permissions for these actions granted on all objects allow Amazon S3 to get a specific object version and access control list \(ACL\) associated with objects\. 
  + `s3:ReplicateObject` and `s3:ReplicateDelete`—Permissions for these actions on objects in the *destination* bucket allow Amazon S3 to replicate objects or delete markers to the destination bucket\. For information about delete markers, see [How delete operations affect replication](replication-what-is-isnot-replicated.md#replication-delete-op)\. 
**Note**  
Permissions for the `s3:ReplicateObject` action on the *destination* bucket also allow replication of object tags, so you don't need to explicitly grant permission for the `s3:ReplicateTags` action\.
  + `s3:GetObjectVersionTagging`—Permissions for this action on objects in the *source* bucket allow Amazon S3 to read object tags for replication \(see [Object tagging](object-tagging.md)\)\. If Amazon S3 doesn't have these permissions, it replicates the objects, but not the object tags\.

  For a list of Amazon S3 actions, see [Amazon S3 Actions](using-with-s3-actions.md)\.
**Important**  
The AWS account that owns the IAM role must have permissions for the actions that it grants to the IAM role\.   
For example, suppose that the source bucket contains objects owned by another AWS account\. The owner of the objects must explicitly grant the AWS account that owns the IAM role the required permissions through the object ACL\. Otherwise, Amazon S3 can't access the objects, and replication of the objects fails\. For information about ACL permissions, see [Access Control List \(ACL\) Overview](acl-overview.md)\.  
The permissions described here are related to minimum replication configuration\. If you choose to add optional replication configurations, you must grant additional permissions to Amazon S3\. For more information, see [Additional replication configurations](replication-additional-configs.md)\. 

## Granting permissions when source and destination buckets are owned by different AWS accounts<a name="setting-repl-config-crossacct"></a>

When source and destination buckets aren't owned by the same accounts, the owner of the destination bucket must also add a bucket policy to grant the owner of the source bucket permissions to perform replication actions, as follows\. 

```
{
   "Version":"2012-10-17",
   "Id":"PolicyForDestinationBucket",
   "Statement":[
      {
         "Sid":"Permissions on objects",
         "Effect":"Allow",
         "Principal":{
            "AWS":"arn:aws:iam::SourceBucket-AcctID:root"
         },
         "Action":[
            "s3:ReplicateDelete",
            "s3:ReplicateObject"
         ],
         "Resource":"arn:aws:s3:::destinationbucket/*"
      },
      {
         "Sid":"Permissions on bucket",
         "Effect":"Allow",
         "Principal":{
            "AWS":"arn:aws:iam::SourceBucket-AcctID:root"
         },
         "Action": [
            "s3:List*",
            "s3:GetBucketVersioning",
            "s3:PutBucketVersioning"
         ],
         "Resource":"arn:aws:s3:::destinationbucket"
      }
   ]
}
```

For an example, see [Example 2: Configuring replication when the source and destination buckets are owned by different accounts](replication-walkthrough-2.md)\.

If objects in the source bucket are tagged, note the following:
+ If the source bucket owner grants Amazon S3 permission for the `s3:GetObjectVersionTagging` and `s3:ReplicateTags` actions to replicate object tags \(through the IAM role\), Amazon S3 replicates the tags along with the objects\. For information about the IAM role, see [Creating an IAM role](#setting-repl-config-same-acctowner)\. 
+ If the owner of the destination bucket doesn't want to replicate the tags, they can add the following statement to the destination bucket policy to explicitly deny permission for the `s3:ReplicateTags` action\.

  ```
  ...
     "Statement":[
        {
           "Effect":"Deny",
           "Principal":{
              "AWS":"arn:aws:iam::SourceBucket-AcctID:root"
           },
           "Action":"s3:ReplicateTags",
           "Resource":"arn:aws:s3:::DestinationBucket/*"
        }
     ]
  ...
  ```

### Changing replica ownership<a name="change-replica-ownership"></a>

When different AWS accounts own the source and destination buckets, you can tell Amazon S3 to change the ownership of the replica to the AWS account that owns the destination bucket\. This is called the *owner override* option\. For more information, see [Changing the replica owner](replication-change-owner.md)\.