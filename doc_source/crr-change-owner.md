# Cross\-Region Replication Additional Configuration: Change Replica Owner<a name="crr-change-owner"></a>

Regardless of who owns the source bucket or the source object, you can direct Amazon S3 to change replica ownership to the AWS account that owns the destination bucket\. You might choose to do this to restrict access to object replicas\. This is also referred to as the *owner override* option of the replication configuration\.

**Warning**  
Add the owner override option only when the source and destination buckets are owned by different AWS accounts\.

For information about setting replication configuration in cross\-account scenario, see [Setting Up Cross\-Region Replication for Buckets Owned by Different AWS Accounts](crr-how-setup.md#setting-repl-config-crossacct)\.This section provides only the additional information to direct Amazon S3 to change the replica ownership to the AWS account that owns the destination bucket\. 
+ Add the `<Account>` and `<AccessControlTranslation>` elements as the child element of the `<Destination>` element, as shown in the following example:

  ```
  <?xml version="1.0" encoding="UTF-8"?>
  <ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Role>arn:aws:iam::account-id:role/role-name</Role>
    <Rule>
      <Status>Enabled</Status>
      <Prefix></Prefix>
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
+ Add more permissions to the IAM role to allow Amazon S3 to change replica ownership\.

  Allow the IAM role permission for the `s3:ObjectOwnerOverrideToBucketOwner` action on all replicas in the destination bucket, as shown in the following policy statement\.

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
+ In the bucket policy of the destination bucket, add permission for the `s3:ObjectOwnerOverrideToBucketOwner` action to allow the AWS account that owns the source bucket permission to change in replica ownership \(in effect, accepting the ownership of the object replicas\)\. You can add the following policy statement to your bucket policy\.

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

**Warning**  
Add this owner override option to the replication configuration only when the two buckets are owned by different AWS accounts\. Amazon S3 does not check if the buckets are owned by same or different accounts\. If you add this option when both buckets are owned by same AWS account, the owner override still applies\. That is, Amazon S3 grants full permissions to the destination bucket owner, and does not replicate subsequent updates to the source object access control list \(ACL\)\. The replica owner can make changes directly to the ACL associated with a replica with a `PUT ACL` request, but not via replication\.

For an example, see [Walkthrough 3: Change Replica Owner to Destination Bucket Owner](crr-walkthrough-3.md)\. 

In a cross\-account scenario, where source and destination buckets are owned by different AWS accounts, the following apply:
+ Creating replication configuration with the optional owner override option \- By default, the source object owner also owns the replica\. And accordingly, along with the object version, Amazon S3 also replicates the ACL associated with the object version\.

   

   You can add optional owner override configuration directing Amazon S3 to change the replica owner to the AWS account that owns the destination bucket\. In this case, because the owners are not the same, Amazon S3 replicates only the object version and not the ACL \(also, Amazon S3 does not replicate any subsequent changes to the source object ACL\)\. Amazon S3 sets the ACL on the replica granting full\-control to the destination bucket owner\. 

   
+ Updating replication configuration \(enabling/disabling owner override option\) – Suppose that you have replication configuration added to a bucket\. Amazon S3 replicates object versions to the destination bucket\. Along with it, Amazon S3 also copies the object ACL and associates it with the object replica\. 

   
  + Now suppose that you update the replication configuration and add the owner override option\. When Amazon S3 replicates the object version, it discards the ACL that is associated with the source object\. It instead sets the ACL on the replica, giving full\-control to the destination bucket owner\. Any subsequent changes to the source object ACL are not replicated\.

    This change does not apply to object versions that were replicated before you set the owner override option\. That is, any ACL updates on the source objects that were replicated before the owner override was set continue to be replicated \(because the object and its replicas continue to have the same owner\)\.

     
  + Now suppose that you later disable the owner override configuration\. Amazon S3 continues to replicate any new object versions and the associated object ACLs to the destination\. When you disable the owner override, it does not apply to objects that were replicated when you had the owner override set in the replication configuration \(the object ownership change that Amazon S3 made remains in effect\)\. That is, ACLs put on the object version that were replicated when you had owner override set continue to be not replicated\.