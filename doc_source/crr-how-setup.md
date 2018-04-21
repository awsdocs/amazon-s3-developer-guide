# Setting Up Cross\-Region Replication<a name="crr-how-setup"></a>

To set up cross\-region replication, you need two buckets—source and destination\. These buckets must be versioning\-enabled and in different AWS Regions\. For a list of AWS Regions where you can create a bucket, see [Regions and Endpoints](http://docs.aws.amazon.com/general/latest/gr/rande.html#s3_region) in the *AWS General Reference*\. You can replicate objects from a source bucket to only one destination bucket\. 

**Important**  
If you have an object expiration lifecycle policy in your non\-versioned bucket and you want to maintain the same permanent delete behavior when you enable versioning, you must add a noncurrent expiration policy\. The noncurrent expiration lifecycle policy will manage the deletes of the noncurrent object versions in the version\-enabled bucket\. \(A version\-enabled bucket maintains one current and zero or more noncurrent object versions\.\) For more information, see [ How Do I Create a Lifecycle Policy for an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-lifecycle.html) in the *Amazon Simple Storage Service Console User Guide*\. 

**Topics**
+ [Setting Up Cross\-Region Replication for Buckets Owned by the Same AWS Account](#setting-repl-config-same-acctowner)
+ [Setting Up Cross\-Region Replication for Buckets Owned by Different AWS Accounts](#setting-repl-config-crossacct)
+ [Related Topics](#crr-setup-related-topics)

## Setting Up Cross\-Region Replication for Buckets Owned by the Same AWS Account<a name="setting-repl-config-same-acctowner"></a>

If both buckets are owned by the same AWS account, do the following to set up cross\-region replication from the source to the destination bucket:
+ Create an IAM role in the account\. This role grants Amazon S3 permission to replicate objects on your behalf\. 
+ Add a replication configuration on the source bucket\. 

### Create an IAM Role<a name="replication-iam-role-intro"></a>

Amazon S3 replicates objects from the source bucket to the destination bucket\. You must grant Amazon S3 necessary permissions via an IAM role\.

**Note**  
By default, all Amazon S3 resources—buckets, objects, and related subresources—are private: only the resource owner can access the resource\. So, Amazon S3 needs permissions to read objects from the source bucket and replicate them to the destination bucket\. 

When you create an IAM role, you attach the following policies to the role:
+ A trust policy in which you identify Amazon S3 as the service principal who can assume the role, as shown: 

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

  For more information about IAM roles, see [IAM Roles](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html) in the *IAM User Guide*\.
+ An access policy in which you grant the role permissions to perform the replication task on your behalf\. When Amazon S3 assumes the role, it has the permissions you specify in this policy\.

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
              "arn:aws:s3:::source-bucket"
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
              "arn:aws:s3:::source-bucket/*"
           ]
        },
        {
           "Effect":"Allow",
           "Action":[
              "s3:ReplicateObject",
              "s3:ReplicateDelete",
              "s3:ReplicateTags"
           ],
           "Resource":"arn:aws:s3:::destination-bucket/*"
        }
     ]
  }
  ```

  The access policy grants permissions for these actions:
  +  `s3:GetReplicationConfiguration` and `s3:ListBucket` \- Permissions for these actions on the *source* bucket enable Amazon S3 to retrieve replication configuration and list bucket \(the current permission model requires the `s3:ListBucket` permission to access the delete markers\)\.
  + `s3:GetObjectVersion` and `s3:GetObjectVersionAcl` \- Permissions for these actions granted on all objects enable Amazon S3 to get a specific object version and access control list \(ACL\) on it\. 
  + `s3:ReplicateObject` and `s3:ReplicateDelete` \- Permissions for these actions on objects in the *destination* bucket enable Amazon S3 to replicate objects or delete markers to the destination bucket\. For information about delete markers, see [Delete Operation and Cross\-Region Replication](crr-what-is-isnot-replicated.md#crr-delete-op)\. 
**Note**  
Permission for the `s3:ReplicateObject` action on the *destination* bucket also allows replication of object tags\. Therefore, Amazon S3 also replicates object tags \(you don't need to explicitly grant permission for the `s3:ReplicateTags` action\)\.
  + `s3:GetObjectVersionTagging` \- Permission for this action on objects in the *source* bucket allows Amazon S3 to read object tags for replication \(see [Object Tagging](object-tagging.md)\)\. If Amazon S3 does not get this permission, it replicates the objects but not the object tags, if any\.

  For a list of Amazon S3 actions, see [Specifying Permissions in a Policy](using-with-s3-actions.md)\.
**Important**  
You can grant permissions only on resources that you have permissions for\. More specifically, the AWS account that owns the IAM role must have permissions for the actions that it grants to the IAM role\.   
For example, suppose that the source bucket contains objects owned by another AWS account\. The object owner must explicitly grant the AWS account that owns the IAM role necessary permissions via the object ACL\. Otherwise, cross\-region replication of these objects fails \(because Amazon S3 cannot access these objects as per the permissions granted in the role policy\)\. For information about ACL permissions, see [Access Control List \(ACL\) Overview](acl-overview.md)\.  
As you learn more about additional CRR configurations, you might grant Amazon S3 permissions for additional resources\. The general rule still applies, that the AWS account that owns the IAM role must have permissions for the actions that it grants to the IAM role\.

### Add Replication Configuration<a name="crr-add-config"></a>

When you add a replication configuration to a bucket, Amazon S3 stores the configuration as XML\. The following are sample configurations\. For more information about the XML structure, see [PUT Bucket replication](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTreplication.html) in the *Amazon Simple Storage Service API Reference*\.

**Example 1: Replication Configuration with One Rule**  
Consider the following replication configuration:  

```
<?xml version="1.0" encoding="UTF-8"?>


<ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Role>arn:aws:iam::AcctID:role/role-name</Role>
  <Rule>
    <Status>Enabled</Status>
    <Prefix></Prefix>

    <Destination><Bucket>arn:aws:s3:::destinationbucket</Bucket></Destination>

  </Rule>
</ReplicationConfiguration>
```
In addition to the IAM role for Amazon S3 to assume, the configuration specifies one rule as follows:  
+ Rule status, indicating that the rule is in effect\.
+ Empty prefix, indicating that the rule applies to all objects in the bucket\.
+ Destination bucket, where objects are replicated\.
You can optionally specify a storage class for the object replicas as shown:  

```
<?xml version="1.0" encoding="UTF-8"?>

<ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Role>arn:aws:iam::account-id:role/role-name</Role>
  <Rule>
    <Status>Enabled</Status>
    <Prefix></Prefix>
    <Destination>
       <Bucket>arn:aws:s3:::destinationbucket</Bucket>
       <StorageClass>storage-class</StorageClass>
    </Destination>
  </Rule>
</ReplicationConfiguration>
```
If the `<Rule>` does not specify a storage class, Amazon S3 uses the storage class of the source object to create an object replica\.   
You can specify any storage class that Amazon S3 supports, except the `GLACIER` storage class\. If you want to transition objects to the `GLACIER` storage class, you use lifecycle configuration\. For more information about lifecycle management, see [Object Lifecycle Management](object-lifecycle-mgmt.md)\. For more information about storage classes, see [Storage Classes](storage-class-intro.md)\.

**Example 2: Replication Configuration with Two Rules**  
Consider the following replication configuration:   

```
<?xml version="1.0" encoding="UTF-8"?>

<ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Role>arn:aws:iam::account-id:role/role-name</Role>
  <Rule>
    <Prefix>Tax</Prefix>
    <Status>Enabled</Status>
    <Destination>
      <Bucket>arn:aws:s3:::destinationbucket</Bucket>
    </Destination>
     ...
  </Rule>
 <Rule>
    <Prefix>Project</Prefix>
    <Status>Enabled</Status>
    <Destination>
      <Bucket>arn:aws:s3:::destinationbucket</Bucket>
    </Destination>
     ...
  </Rule>


</ReplicationConfiguration>
```
In the replication configuration:  
+ Each rule specifies a different key name prefix, identifying a separate set of objects in the source bucket to which the rule applies\. Amazon S3 then replicates only objects with specific key prefixes\. For example, Amazon S3 replicates objects with key names `Tax/doc1.pdf` and `Project/project1.txt`, but it does not replicate any object with the key name `PersonalDoc/documentA`\. 
+ Both rules specify the same destination bucket\.
+ Both rules are enabled\.
You cannot specify overlapping prefixes as shown:   

```
<ReplicationConfiguration>

  <Role>arn:aws:iam::AcctID:role/role-name</Role>

  <Rule>
    <Prefix>TaxDocs</Prefix>
    <Status>Enabled</Status>
    <Destination>
      <Bucket>arn:aws:s3:::destinationbucket</Bucket>
    </Destination>
  </Rule>
  <Rule>
    <Prefix>TaxDocs/2015</Prefix>
    <Status>Enabled</Status>
    <Destination>
      <Bucket>arn:aws:s3:::destinationbucket</Bucket>
    </Destination>
  </Rule>
</ReplicationConfiguration>
```
The two rules specify overlapping prefixes `Tax/` and `Tax/2015`, which is not allowed\.

**Example 3: Example Walkthrough**  
When both the source and destination buckets are owned by the same AWS account, you can use the Amazon S3 console to set up cross\-region replication\. Assuming you have source and destination buckets that are both versioning\-enabled, you can use the console to add replication configuration on the source bucket\. For more information, see the following topics:  
+ [Walkthrough 1: Configure Cross\-Region Replication Where Source and Destination Buckets Are Owned by the Same AWS Account](crr-walkthrough1.md)
+  [Enabling Cross\-Region Replication](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/CreatingaBucket.html) in the *Amazon Simple Storage Service Console User Guide*

## Setting Up Cross\-Region Replication for Buckets Owned by Different AWS Accounts<a name="setting-repl-config-crossacct"></a>

When setting up replication configuration in a cross\-account scenario, in addition to doing the same configuration as outlined in the [preceding section](http://docs.aws.amazon.com/AmazonS3/latest/dev/crr-how-setup.html#setting-repl-config-same-acctowner), the destination bucket owner must also add a bucket policy to grant the source bucket owner permissions to perform replication actions\. 

```
{
   "Version":"2008-10-17",
   "Id":"PolicyForDestinationBucket",
   "Statement":[
      {
         "Sid":"1",
         "Effect":"Allow",
         "Principal":{
            "AWS":"SourceBucket-AcctID"
         },
         "Action":[
            "s3:ReplicateDelete",
            "s3:ReplicateObject"
         ],
         "Resource":"arn:aws:s3:::destinationbucket/*"
      },
      {
         "Sid":"2",
         "Effect":"Allow",
         "Principal":{
            "AWS":"SourceBucket-AcctID"
         },
         "Action":"s3:List*",
         "Resource":"arn:aws:s3:::destinationbucket"
      }
   ]
}
```

For an example, see [Walkthrough 2: Configure Cross\-Region Replication Where Source and Destination Buckets Are Owned by Different AWS Accounts](crr-walkthrough-2.md)\.

If objects in the source bucket are tagged, note the following:
+ If the source bucket owner grants Amazon S3 permission for the `s3:GetObjectVersionTagging` and `s3:ReplicateTags` actions to replicate object tags \(via the IAM role\), Amazon S3 replicates the tags along with the objects\. For information about the IAM role, see [Create an IAM Role](#replication-iam-role-intro)\. 
+ If the destination bucket owner does not want the tags replicated, the owner can add the following statement to the destination bucket policy to explicitly deny permission for the `s3:ReplicateTags` action\.

  ```
  ...
     "Statement":[
        {
           "Effect":"Deny",
           "Principal":{
              "AWS":"arn:aws:iam::SourceBucket-AcctID:root"
           },
           "Action":["s3:ReplicateTags"],
           "Resource":"arn:aws:s3:::destinationbucket/*"
        }
     ]
  ...
  ```

### Change Replica Ownership<a name="change-replica-ownership"></a>

You can also optionally direct Amazon S3 to change the replica ownership to the AWS account that owns the destination bucket\. This is also referred to as the *owner override* option of the replication configuration\. For more information, see, [Cross\-Region Replication Additional Configuration: Change Replica Owner](crr-change-owner.md)\.

## Related Topics<a name="crr-setup-related-topics"></a>

[Cross\-Region Replication \(CRR\)](crr.md)

[What Is and Is Not Replicated](crr-what-is-isnot-replicated.md)

[Walkthrough 1: Configure Cross\-Region Replication Where Source and Destination Buckets Are Owned by the Same AWS Account](crr-walkthrough1.md)

[Walkthrough 2: Configure Cross\-Region Replication Where Source and Destination Buckets Are Owned by Different AWS Accounts](crr-walkthrough-2.md)

[Finding the Cross\-Region Replication Status ](crr-status.md)

[Troubleshooting Cross\-Region Replication in Amazon S3](crr-troubleshoot.md)