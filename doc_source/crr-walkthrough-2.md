# Walkthrough 2: Configure Cross\-Region Replication Where Source and Destination Buckets Are Owned by Different AWS Accounts<a name="crr-walkthrough-2"></a>

In this walkthrough, you set up cross\-region replication where source and destination buckets are owned by different AWS accounts\. 

Because buckets are owned by different AWS accounts, you have to perform one extra step to set up cross\-region replication—the destination bucket owner must create a bucket policy granting the source bucket owner permissions for replication actions\.

In this exercise, you perform all the steps using the console, except the creation of an IAM role and adding replication configuration to the source bucket for the following reasons:

+ The Amazon S3 console supports setting replication configuration when both buckets are owned by same AWS account\. However, in a cross\-account scenario, you must specify a destination bucket that is owned by another AWS account, and the Amazon S3 console UI shows only buckets in your account\.

+ In the IAM console, Amazon S3 is not in the list of **AWS Service Roles**\. You can optionally create an IAM role but select another service role type \(such as AWS Lambda\)\. After the role is created, you can modify the trust policy to specify Amazon S3 service principal \(instead of Lambda service principal\) who can assume the role\. For this exercise, you use the AWS CLI to create the role\. 

1. Create two buckets using two different AWS accounts\. In accordance with cross\-region replication requirements, you create these buckets in different AWS Regions and enable versioning on both buckets\.

   1. Create a source bucket in an AWS Region\. For example, US West \(Oregon\) \(us\-west\-2\) in account A\. For instructions, go to [How Do I Create an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-bucket.html) in the *Amazon Simple Storage Service Console User Guide*\.

   1. Create a destination bucket in another AWS Region\. For example, US East \(N\. Virginia\) \(us\-east\-1\) in account B\.

   1. Enable versioning on both buckets\. For instructions, see [How Do I Enable or Suspend Versioning for an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-versioning.html) in the *Amazon Simple Storage Service Console User Guide*\.  
**Important**  
If you have an object expiration lifecycle policy in your non\-versioned bucket and you want to maintain the same permanent delete behavior when you enable versioning, you must add a noncurrent expiration policy\. The noncurrent expiration lifecycle policy will manage the deletes of the noncurrent object versions in the version\-enabled bucket\. \(A version\-enabled bucket maintains one current and zero or more noncurrent object versions\.\) For more information, see [ How Do I Create a Lifecycle Policy for an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-lifecycle.html) in the *Amazon Simple Storage Service Console User Guide*\. 

1. Add bucket policy on the destination bucket to allow the source bucket owner to replicate objects\.

   ```
   {
      "Version":"2008-10-17",
      "Id":"",
      "Statement":[
         {
            "Sid":"Stmt123",
            "Effect":"Allow",
            "Principal":{
               "AWS":"arn:aws:iam::AWS-ID-Account-A:root"
            },
            "Action":["s3:ReplicateObject", "s3:ReplicateDelete"],
            "Resource":"arn:aws:s3:::destination-bucket/*"
         }
      ]
   }
   ```

   For instructions, see [How Do I Add an S3 Bucket Policy?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/add-bucket-policy.html) in the *Amazon Simple Storage Service Console User Guide*\.

1. Grant Amazon S3 permission to replicate objects on behalf of the source bucket owner\.

   After you configure cross\-region replication on the source bucket, Amazon S3 replicates objects on your behalf\. The source bucket owner can grant Amazon S3 necessary permissions using an IAM role\. In this step, you create an IAM role in account A\. 

   Use the AWS CLI to create this IAM role\. For information about how to set up the AWS CLI, see [Setting Up the Tools for the Example Walkthroughs](policy-eval-walkthrough-download-awscli.md)\. This exercise assumes that you have configured the AWS CLI with two profiles: `accountA` and `accountB`\. 

   1. Copy the following policy and save it to a file called `S3-role-trust-policy.json`\. This policy grants Amazon S3 permissions to assume the role\.

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

   1. Copy the following policy and save it to a file named `S3-role-permissions-policy.json`\. This access policy grants permissions for various Amazon S3 bucket and object actions\. In the following step, you add the policy to the IAM role you are creating\.

      ```
      {
         "Version":"2012-10-17",
         "Statement":[
            {
               "Effect":"Allow",
               "Action":[
                  "s3:GetObjectVersionForReplication",
                  "s3:GetObjectVersionAcl"
               ],
               "Resource":[
                  "arn:aws:s3:::source-bucket/*"
               ]
            },
            {
               "Effect":"Allow",
               "Action":[
                  "s3:ListBucket",
                  "s3:GetReplicationConfiguration"
               ],
               "Resource":[
                  "arn:aws:s3:::source-bucket"
               ]
            },
            {
               "Effect":"Allow",
               "Action":[
                  "s3:ReplicateObject",
                  "s3:ReplicateDelete"
               ],
               "Resource":"arn:aws:s3:::destination-bucket/*"
            }
         ]
      }
      ```

   1. Run the following AWS CLI command to create a role:

      ```
      $ aws iam create-role \
      --role-name crrRole \
      --assume-role-policy-document file://S3-role-trust-policy.json  \
      --profile accountA
      ```

   1. Run the following AWS CLI command to create a policy:

      ```
      $ aws iam create-policy \
      --policy-name crrRolePolicy  \
      --policy-document file://S3-role-permissions-policy.json  \
      --profile accountA
      ```

   1. Write down the policy Amazon Resource Name \(ARN\) that is returned by the `create-policy` command\.

   1. Run the following AWS CLI command to attach the policy to the role:

      ```
      $ aws iam attach-role-policy \
      --role-name crrRole \
      --policy-arn policy-arn \
      --profile accountA
      ```

      Now you have an IAM role in account A that Amazon S3 can assume\. It has permissions for necessary Amazon S3 actions so that Amazon S3 can replicate objects from a specific source bucket to a destination bucket\. You specify this role when you add cross\-region replication to the source bucket in account A\.

1. Add replication configuration on the source bucket in account A directing Amazon S3 to replicate objects with prefix `Tax/` to a destination bucket as shown in the following example configuration:

   ```
   <ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
     <Role>arn:aws:iam::AWS-ID-Account-A:role/role-name</Role>
     <Rule>
       <Status>Enabled</Status>
       <Prefix>Tax</Prefix>
       <Destination><Bucket>arn:aws:s3:::destination-bucket</Bucket></Destination>
     </Rule>
   </ReplicationConfiguration>
   ```

   In this example, you can use either the AWS CLI or the AWS SDK to add the replication configuration\. You can't use the console because the console doesn't support specifying a destination bucket that is in different AWS account\. 

   + Using AWS CLI\. 

     The AWS CLI requires you to specify the replication configuration as JSON\. Save the following JSON in a file \(`replication.json`\)\. 

     ```
     {
       "Role": "arn:aws:iam::AWS-ID-Account-A:role/role-name",
       "Rules": [
         {
           "Prefix": "Tax",
           "Status": "Enabled",
           "Destination": {
             "Bucket": "arn:aws:s3:::destination-bucket"
           }
         }
       ]
     }
     ```

     Update the JSON by providing the bucket name and role ARN\. Then, run the AWS CLI command to add replication configuration to your source bucket:

     ```
     $ aws s3api put-bucket-replication \
     --bucket source-bucket \
     --replication-configuration file://replication.json  \
     --profile accountA
     ```

     For instructions on how to set up the AWS CLI, see [Setting Up the Tools for the Example Walkthroughs](policy-eval-walkthrough-download-awscli.md)\.

     Account A can use the `get-bucket-replication` command to retrieve the replication configuration:

     ```
     $ aws s3api get-bucket-replication \
     --bucket source-bucket \
     --profile accountA
     ```

   + Using the AWS SDK for Java\.

     For a code example, see [Setting Up Cross\-Region Replication Using the AWS SDK for Java](crr-using-java.md)\. 

1. Test the setup\. In the console, do the following:

   + In the source bucket, create a folder named `Tax`\. 

   + Add objects to the folder in the source bucket\.

     + Verify that Amazon S3 replicated objects in the destination bucket owned by account B\.

     + In object properties, notice the **Replication Status** is set to "Replica" \(identifying this as a replica object\)\.

     + In object properties, the permission section shows no permissions \(the replica is still owned by the source bucket owner, and the destination bucket owner has no permission on the object replica\)\. You can add optional configuration to direct Amazon S3 to change the replica ownership\. For example, see [Walkthrough 3: Change Replica Owner to Destination Bucket Owner](crr-walkthrough-3.md)\.   
![\[Screenshot of object properties showing replication status (replica)
                        and permissions for a text file.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/crr-wt2-10.png)

     The amount of time it takes for Amazon S3 to replicate an object depends on the object size\. For information about finding replication status, see [Finding the Cross\-Region Replication Status ](crr-status.md)\. 

   + Update an object's ACL in the source bucket and verify that changes appear in the destination bucket\.

     For instructions, see [How Do I Set Permissions on an Object?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/set-object-permissions.html) in the *Amazon Simple Storage Service Console User Guide*\.

   + Update the object's metadata\. For example, make changes to the storage class\. Verify that the changes appear in the destination bucket\.

     For instructions, see [How Do I Add Metadata to an S3 Object?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/add-object-metadata.html) in the *Amazon Simple Storage Service Console User Guide*\.

   Remember that the replicas are exact copies of the objects in the source bucket\.

## Related Topics<a name="crr-wt2-related-topics"></a>

[Cross\-Region Replication \(CRR\)](crr.md)

[What Is and Is Not Replicated](crr-what-is-isnot-replicated.md)

[Finding the Cross\-Region Replication Status ](crr-status.md)

[Walkthrough 1: Configure Cross\-Region Replication Where Source and Destination Buckets Are Owned by the Same AWS Account](crr-walkthrough1.md)