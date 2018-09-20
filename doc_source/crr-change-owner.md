# CRR Additional Configuration: Changing the Replica Owner<a name="crr-change-owner"></a>

In cross\-region replication \(CRR\), the owner of the source object also owns the replica by default\. When source and destination buckets are owned by different AWS accounts, you can add optional configuration settings to change replica ownership to the AWS account that owns the destination bucket\. You might do this, for example, to restrict access to object replicas\. This is referred to as the *owner override* option of the replication configuration\. This section explains only the relevant additional configuration settings\. For information about setting the replication configuration see [Cross\-Region Replication ](crr.md)\. 

To configure the owner override, you do the following:
+ Add the owner override option to the replication configuration to tell Amazon S3 to change replica ownership\. 
+ Grant Amazon S3 permissions to change replica ownership\. 
+ Add permission in the destination bucket policy to allow changing replica ownership\. This allows the owner of the destination bucket to accept the ownership of object replicas\.

The following sections describe how to perform these tasks\. For a working example with step\-by\-step instructions, see [Example 3: Change Replica Owner When Source and Destination Buckets Are Owned by Different AWS Accounts](crr-walkthrough-3.md)\.

## Adding the Owner Override Option to the Replication Configuration<a name="repl-ownership-owneroverride-option"></a>

**Warning**  
Add the owner override option only when the source and destination buckets are owned by different AWS accounts\. Amazon S3 doesn't check if the buckets are owned by same or different accounts\. If you add owner override when both buckets are owned by same AWS account, Amazon S3 applies the owner override\. It grants full permissions to the owner of the destination bucket and doesn't replicate subsequent updates to the source object access control list \(ACL\)\. The replica owner can directly change the ACL associated with a replica with a `PUT ACL` request, but not through replication\.

To specify the owner override option, add the following to the Destination element: 
+ The `AccessControlTranslation` element, which tells Amazon S3 to change replica ownership
+ The `Account` element, which specifies the AWS account of the destination bucket owner 

```
<ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    ...
    <Destination>
      ...
      <AccessControlTranslation>
           <Owner>Destination</Owner>
       </AccessControlTranslation>
      <Account>destination-bucket-owner-account-id</Account>
    </Destination>
  </Rule>
</ReplicationConfiguration>
```

The following example replication configuration tells Amazon S3 to replicate objects that have the `Tax` key prefix to the destination bucket and change ownership of the replicas\.

```
<?xml version="1.0" encoding="UTF-8"?>
<ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
   <Role>arn:aws:iam::account-id:role/role-name</Role>
   <Rule>
      <ID>Rule-1</ID>
      <Priority>1</Priority>
      <Status>Enabled</Status>
      <Status>Enabled</Status>
      <DeleteMarkerReplication>
         <Status>Disabled</Status>
      </DeleteMarkerReplication>
      <Filter>
         <Prefix>Tax</Prefix>
      </Filter>
      <Destination>
         <Bucket>arn:aws:s3:::destination-bucket</Bucket>
         <Account>destination-bucket-owner-account-id</Account>
         <AccessControlTranslation>
            <Owner>Destination</Owner>
         </AccessControlTranslation>
      </Destination>
   </Rule>
</ReplicationConfiguration>
```

## Granting Amazon S3 Permission to Change Replica Ownership<a name="repl-ownership-add-role-permission"></a>

Grant Amazon S3 permissions to change replica ownership by adding permission for the `s3:ObjectOwnerOverrideToBucketOwner` action in the permission policy associated with the IAM role\. This is the IAM role that you specified in the replication configuration that allows Amazon S3 to assume and replicate objects on your behalf\. 

```
...
{
    "Effect":"Allow",
         "Action":[
       "s3:ObjectOwnerOverrideToBucketOwner"
    ],
    "Resource":"arn:aws:s3:::destination-bucket/*"
}
...
```

## Adding Permission in the Destination Bucket Policy to Allow Changing Replica Ownership<a name="repl-ownership-accept-ownership-b-policy"></a>

The owner of the destination bucket must grant the owner of the source bucket permission to change replica ownership\. The owner of the destination bucket grants the owner of the source bucket permission for the `s3:ObjectOwnerOverrideToBucketOwner` action\. This allows the source bucket owner to accept ownership of the object replicas\. The following example bucket policy statement shows how to do this:

```
...
{
    "Sid":"1",
    "Effect":"Allow",
    "Principal":{"AWS":"source-bucket-account-id"},
    "Action":["s3:ObjectOwnerOverrideToBucketOwner"],
    "Resource":"arn:aws:s3:::destination-bucket/*"
}
...
```

## Additional Considerations<a name="repl-ownership-considerations"></a>

When you configure the ownership override option, the following considerations apply:
+ By default, the owner of the source object also owns the replica\. Amazon S3 replicates the object version and the ACL associated with it\.

   

  If you add the owner override, Amazon S3 replicates only the object version, not the ACL\. In addition, Amazon S3 doesn't replicate subsequent changes to the source object ACL\. Amazon S3 sets the ACL on the replica that grants full control to the destination bucket owner\. 

   
+  When you update a replication configuration to enable, or disable,the owner override, the following occur: 

   
  + If you add the owner override option to the replication configuration

     

    When Amazon S3 replicates an object version, it discards the ACL that is associated with the source object\. Instead, it sets the ACL on the replica, giving full control to the owner of the destination bucket\. It doesn't replicate subsequent changes to the source object ACL\. However, this ACL change doesn't apply to object versions that were replicated before you set the owner override option\. ACL updates on source objects that were replicated before the owner override was set continue to be replicated \(because the object and its replicas continue to have the same owner\)\.

     
  + If you remove the owner override option from the replication configuration

     

    Amazon S3 replicates new objects that appear in the source bucket and the associated ACLs to the destination bucket\. For objects that were replicated before you removed the owner override, Amazon S3 doesn't replicate the ACLs because the object ownership change that Amazon S3 made remains in effect\. That is, ACLs put on the object version that were replicated when the owner override was set continue to be not replicated\.