# Walkthrough 3: Change Replica Owner to Destination Bucket Owner<a name="crr-walkthrough-3"></a>

In this exercise, you update replication configuration in exercise 2 \([Walkthrough 2: Configure Cross\-Region Replication Where Source and Destination Buckets Are Owned by Different AWS Accounts](crr-walkthrough-2.md)\) to direct Amazon S3 to change the replica owner to the AWS account that owns the destination bucket\. For more information about optionally changing the replica ownership, see [Cross\-Region Replication Additional Configuration: Change Replica Owner](crr-change-owner.md)\.

1. Complete walkthrough 2\. For instructions, see [Walkthrough 2: Configure Cross\-Region Replication Where Source and Destination Buckets Are Owned by Different AWS Accounts](crr-walkthrough-2.md)\.

1. Update the replication configuration rule by adding the `<AccessControlTranslation>` element, as shown following: 

   ```
   <ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
     <Role>arn:aws:iam::account-id:role/role-name</Role>
     <Rule>
       <Status>Enabled</Status>
       <Prefix></Prefix>
       <Destination>
          <Bucket>arn:aws:s3:::destinationbucket</Bucket>
          <Account>destination-bucket-owner-account-id</Account>
          <StorageClass>storage-class</StorageClass>
          <AccessControlTranslation>
              <Owner>Destination</Owner>
          </AccessControlTranslation>
       </Destination>
     </Rule>
   </ReplicationConfiguration>
   ```

   In this example, you can use either the AWS CLI or the AWS SDK to add the replication configuration\. You cannot use the console because the console does not support specifying a destination bucket that is in different AWS account\. 
   + Using the AWS CLI\. 

     The AWS CLI requires you to specify the replication configuration as JSON\. Save the following JSON in a file \(`replication.json`\)\. 

     ```
     {
       "Role": "arn:aws:iam::AWS-ID-Account-A:role/role-name",
       "Rules": [
         {
           "Prefix": "Tax",
           "Status": "Enabled",
           "Destination": {
             "Bucket": "arn:aws:s3:::destination-bucket",
             "AccessControlTranslation" : {
     		   "Owner" : "Destination"
     	   }
           }
         }
       ]
     }
     ```

     Update the JSON by providing the bucket name and role Amazon Resource Name \(ARN\)\. Then, run the AWS CLI command to add replication configuration to your source bucket:

     ```
     $ aws s3api put-bucket-replication \
     --bucket source-bucket \
     --replication-configuration file://replication.json  \
     --profile accountA
     ```

     For instructions on how to set up the AWS CLI, see [Setting Up the Tools for the Example Walkthroughs](policy-eval-walkthrough-download-awscli.md)\.

     You can use the `get-bucket-replication` command to retrieve the replication configuration:

     ```
     $ aws s3api get-bucket-replication \
     --bucket source-bucket \
     --profile accountA
     ```
   + Using the AWS SDK for Java\.

     For a code example, see [Setting Up Cross\-Region Replication Using the AWS SDK for Java](crr-using-java.md)\. 

1. In the IAM console, select the IAM role you created, and update the associated permission policy by adding permissions for the `s3:ObjectOwnerOverrideToBucketOwner` action\. 

   The updated policy is shown:

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
               "s3:ReplicateObject",
               "s3:ReplicateDelete",
              "s3:ObjectOwnerOverrideToBucketOwner"
            ],
            "Resource":"arn:aws:s3:::destination-bucket/*"
         }
      ]
   }
   ```

1. In the Amazon S3 console, select the destination bucket, and update the bucket policy as follows:
   + Grant the source object owner permission for the `s3:ObjectOwnerOverrideToBucketOwner` action\.
   + Grant the source bucket owner permission for the `s3:ListBucket` and the `s3:ListBucketVersions` actions\. 

   The following bucket policy shows the additional permissions\.

   ```
   {
      "Version":"2008-10-17",
      "Id":"PolicyForDestinationBucket",
      "Statement":[
         {
            "Sid":"1",
            "Effect":"Allow",
            "Principal":{
               "AWS":"source-bucket-owner-aws-account-id"
            },
            "Action":[
               "s3:ReplicateDelete",
               "s3:ReplicateObject",
               "s3:ObjectOwnerOverrideToBucketOwner"
            ],
            "Resource":"arn:aws:s3:::destinationbucket/*"
         },
         {
            "Sid":"2",
            "Effect":"Allow",
            "Principal":{
               "AWS":"source-bucket-owner-aws-account-id"
            },
            "Action":[
               "s3:ListBucket",
               "s3:ListBucketVersions"
            ],
            "Resource":"arn:aws:s3:::destinationbucket"
         }
      ]
   }
   ```

1. Test the replication configuration in the Amazon S3 console:

   1. Upload the object to the source bucket \(in the `Tax` folder\)\.

   1. Verify that the replica is created in the destination bucket\. For the replica, verify the permissions\. Notice that the destination bucket owner now has full permissions on the object replica\.