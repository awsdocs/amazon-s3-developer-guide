# Example 4: Replicating Encrypted Objects<a name="crr-walkthrough-4"></a>

By default, Amazon S3 doesn't replicate objects that are stored at rest using server\-side encryption with AWS KMS\-managed keys\. To replicate encrypted objects, you modify the bucket replication configuration to tell Amazon S3 to replicate these objects\. This example explains how to use the Amazon S3 console and the AWS Command Line Interface \(AWS CLI\) to change the bucket replication configuration to enable replicating encrypted objects\. For more information, see [CRR Additional Configuration: Replicating Objects Created with Server\-Side Encryption \(SSE\) Using AWS KMS\-Managed Encryption Keys](crr-replication-config-for-kms-objects.md)\. 

**Topics**

## Replicating Encrypted Objects \(Console\)<a name="crr-ex4-console"></a>

For step\-by\-step instructions, see [How Do I Add a Cross\-Region Replication \(CRR\) Rule to an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-crr.html) in the *Amazon Simple Storage Service Console User Guide*\. This topic provides instructions for setting replication configuration when buckets are owned by same and different AWS accounts\.

## Replicating Encrypted Objects \(AWS CLI\)<a name="crr-ex4-cli"></a>

To replicate encrypted objects with the AWS CLI, you create buckets, enable versioning on the buckets, create an IAM role that gives Amazon S3 permission to replicate objects, and add the replication configuration to the source bucket\. The replication configuration provides information related to replicating objects encrypted using KMS keys\. The IAM role permission include necessary permissions to replicate the encrypted objects\. You also test the setup\.

**To replicate encrypted objects \(AWS CLI\)**

1. In this example, we create both the *source* and *destination* buckets in the same AWS account\. Set a credentials profile for the AWS CLI\. In this example, we use the profile name `acctA`\. For more information about setting credential profiles, see [Named Profiles](https://docs.aws.amazon.com/cli/latest/userguide/cli-multiple-profiles.html) in the AWS Command Line Interface User Guide\. 

1. Create the *source* bucket and enable versioning on it\. In this example, we create the *source* bucket in the US East \(N\. Virginia\) \(us\-east\-1\) Region\.

   ```
   aws s3api create-bucket \
   --bucket source \
   --region us-east-1 \
   --profile acctA
   ```

   ```
   aws s3api put-bucket-versioning \
   --bucket source \
   --versioning-configuration Status=Enabled \
   --profile acctA
   ```

1. Create the *destination* bucket and enable versioning on it\. In this example, we create the *destination* bucket in the US West \(Oregon\) \(us\-west\-2\) Region\. 
**Note**  
To set up replication configuration when both *source* and *destination* buckets are in the same AWS account, you use the same profile\. In this example, we use `acctA`\. To test replication configuration when the buckets are owned by different AWS accounts, you specify different profiles for each\. In this example, we use the `acctB` profile for the *destination* bucket\.

   ```
   aws s3api create-bucket \
   --bucket destination \
   --region us-west-2 \
   --create-bucket-configuration LocationConstraint=us-west-2 \
   --profile acctA
   ```

   ```
   aws s3api put-bucket-versioning \
   --bucket destination \
   --versioning-configuration Status=Enabled \
   --profile acctA
   ```

1. Create an IAM role\. You specify this role in the replication configuration that you add to the *source* bucket later\. Amazon S3 assumes this role to replicate objects on your behalf\. You create an IAM role in two steps:
   + Create a role
   + Attach a permissions policy to the role

   1. Create an IAM role\.

      1. Copy the following trust policy and save it to a file called `s3-role-trust-policy-kmsobj.json` in the current directory on your local computer\. This policy grants Amazon S3 service principal permissions to assume the role so Amazon S3 can perform tasks on your behalf\.

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

      1. Create a role:

         ```
         $ aws iam create-role \
         --role-name crrRolekmsobj \
         --assume-role-policy-document file://s3-role-trust-policy-kmsobj.json  \
         --profile acctA
         ```

   1. Attach a permissions policy to the role\. This policy grants permissions for various Amazon S3 bucket and object actions\. 

      1. Copy the following permissions policy and save it to a file named `s3-role-permissions-policykmsobj.json` in the current directory on your local computer\. You create an IAM role and attach the policy to it later\. 

         ```
         {
            "Version":"2012-10-17",
            "Statement":[
               {
                  "Action":[
                     "s3:ListBucket",
                     "s3:GetReplicationConfiguration",
                     "s3:GetObjectVersionForReplication",
                     "s3:GetObjectVersionAcl"
                  ],
                  "Effect":"Allow",
                  "Resource":[
                     "arn:aws:s3:::source",
                     "arn:aws:s3:::source/*"
                  ]
               },
               {
                  "Action":[
                     "s3:ReplicateObject",
                     "s3:ReplicateDelete",
                     "s3:ReplicateTags",
                     "s3:GetObjectVersionTagging"
                  ],
                  "Effect":"Allow",
                  "Condition":{
                     "StringLikeIfExists":{
                        "s3:x-amz-server-side-encryption":[
                           "aws:kms",
                           "AES256"
                        ],
                        "s3:x-amz-server-side-encryption-aws-kms-key-id":[
                           "AWS KMS key IDs to use for encrypting object replicas"  
                        ]
                     }
                  },
                  "Resource":"arn:aws:s3:::destination/*"
               },
               {
                  "Action":[
                     "kms:Decrypt"
                  ],
                  "Effect":"Allow",
                  "Condition":{
                     "StringLike":{
                        "kms:ViaService":"s3.us-east-1.amazonaws.com",
                        "kms:EncryptionContext:aws:s3:arn":[
                           "arn:aws:s3:::source/*"
                        ]
                     }
                  },
                  "Resource":[
                     "AWS KMS key IDs used to encrypt source objects." 
                  ]
               },
               {
                  "Action":[
                     "kms:Encrypt"
                  ],
                  "Effect":"Allow",
                  "Condition":{
                     "StringLike":{
                        "kms:ViaService":"s3.us-west-2.amazonaws.com",
                        "kms:EncryptionContext:aws:s3:arn":[
                           "arn:aws:s3:::destination/*"
                        ]
                     }
                  },
                  "Resource":[
                     "AWS KMS key IDs to use for encrypting object replicas" 
                  ]
               }
            ]
         }
         ```

      1. Create a policy and attach it to the role:

         ```
         $ aws iam put-role-policy \
         --role-name crrRolekmsobj \
         --policy-document file://s3-role-permissions-policykmsobj.json \
         --policy-name crrRolechangeownerPolicy \
         --profile acctA
         ```

1. Add the following replication configuration to the *source* bucket\. It tells Amazon S3 to replicate objects with the `Tax/` prefix to the *destination* bucket\. 
**Important**  
In the replication configuration you specify the IAM role that Amazon S3 can assume\. You can do this only if you have the `iam:PassRole` permission\. The profile you specify in the CLI command must have the permission\. For more information, see [Granting a User Permissions to Pass a Role to an AWS Service](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_use_passrole.html) in the *IAM User Guide*\.

   ```
    <ReplicationConfiguration>
     <Role>IAM-Role-ARN</Role>
     <Rule>
       <Status>Enabled</Status>
       <Priority>1</Priority>
       <DeleteMarkerReplication>
          <Status>Disabled</Status>
       </DeleteMarkerReplication>
       <Filter>
          <Prefix>Tax</Prefix>
       </Filter>
       <Status>Enabled</Status>
       <SourceSelectionCriteria>
         <SseKmsEncryptedObjects>
           <Status>Enabled</Status>
         </SseKmsEncryptedObjects>
       </SourceSelectionCriteria>
       <Destination>
         <Bucket>arn:aws:s3:::dest-bucket-name</Bucket>
         <EncryptionConfiguration>
           <ReplicaKmsKeyID>AWS KMS key IDs to use for encrypting object replicas</ReplicaKmsKeyID>
         </EncryptionConfiguration>
       </Destination>
     </Rule>
   </ReplicationConfiguration>
   ```

   To add replication configuration to the *source* bucket, do the following:

   1. The AWS CLI requires you to specify the replication configuration as JSON\. Save the following JSON in a file \(`replication.json`\) in the current directory on your local computer\. 

      ```
      {
         "Role":"IAM-Role-ARN",
         "Rules":[
            {
               "Status":"Enabled",
               "Priority":"1",
               "DeleteMarkerReplication":{
                  "Status":"Disabled"
               },
               "Filter":{
                  "Prefix":"Tax"
               },
               "Destination":{
                  "Bucket":"arn:aws:s3:::destination",
                  "EncryptionConfiguration":{
                     "ReplicaKmsKeyID":"AWS KMS key IDs to use for encrypting object replicas"
                  }
               },
               "SourceSelectionCriteria":{
                  "SseKmsEncryptedObjects":{
                     "Status":"Enabled"
                  }
               },
               "Status":"Enabled"
            }
         ]
      }
      ```

   1. Edit the JSON to provide values for the *destination* bucket and *IAM\-role\-ARN*\. Save the changes\.

   1. Add the replication configuration to your *source* bucket\. Be sure to provide the *source* bucket name\.

      ```
      $ aws s3api put-bucket-replication \
      --replication-configuration file://replicationkmsobj.json \
      --bucket source \
      --profile acctA
      ```

1. Test the setup to verify that encrypted objects are replicated\. In the Amazon S3 console:

   1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

   1. In the *source* bucket, create a folder named `Tax`\. 

   1. Add sample objects to the folder\. Be sure to choose the encryption option and specify your KMS key to encrypt the objects\. 

   1. Verify that the *destination* bucket contains the object replicas and that they are encrypted using the KMS encryption key that you specified in the configuration\.

## Replicating Encrypted Objects \(AWS SDK\)<a name="crr-ex4-sdk"></a>

 For code example to add replication configuration, see [Configure CRR When Source and Destination Buckets Are Owned by the Same AWS Account \(AWS SDK\)](crr-walkthrough1.md#crr-ex1-sdk)\. You will need to modify replication configuration appropriately\. For conceptual information, see [CRR Additional Configuration: Replicating Objects Created with Server\-Side Encryption \(SSE\) Using AWS KMS\-Managed Encryption Keys](crr-replication-config-for-kms-objects.md)\. 