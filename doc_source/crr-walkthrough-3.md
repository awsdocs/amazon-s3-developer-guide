# Example 3: Change Replica Owner When Source and Destination Buckets Are Owned by Different AWS Accounts<a name="crr-walkthrough-3"></a>

When *source* and *destination* buckets in a cross\-region replication \(CRR\) configuration are owned by different AWS account, you can tell Amazon S3 to change replica ownership to the AWS account that owns the *destination* bucket\. This example explains how to use the Amazon S3 console and the AWS CLI to change replica ownership\. For more information, see [CRR Additional Configuration: Changing the Replica Owner](crr-change-owner.md)\. 

**Topics**

## Change Replica Owner When Source and Destination Buckets Are Owned by Different AWS Accounts \(Console\)<a name="crr-ex3-console"></a>

For step\-by\-step instructions, see [Configuring a CRR Rule When the Destination Bucket is in a Different AWS Account](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-crr.html#enable-crr-cross-account-destination) in the *Amazon Simple Storage Service Console User Guide*\. 

## Change Replica Owner When Source and Destination Buckets Are Owned by Different AWS Accounts \(AWS CLI\)<a name="crr-ex3-cli"></a>

To change replica ownership using the AWS CLI, you create buckets, enable versioning on the buckets, create an IAM role that gives Amazon S3 permission to replicate objects, and add the replication configuration to the source bucket\. In the replication configuration you direct Amazon S3 to change replica owner\. You also test the setup\.

**To change replica ownership when source and destination buckets are owned by different AWS accounts \(AWS CLI\)**

1. In this example, you create the *source* and *destination* buckets in two different AWS accounts\. Configure AWS CLI with two named profiles\. In this example, we use profiles named `acctA` and `acctB`, respectively\. For more information about setting credential profiles, see [Named Profiles](https://docs.aws.amazon.com/cli/latest/userguide/cli-multiple-profiles.html) in the *AWS Command Line Interface User Guide\.*
**Important**  
The profiles you use for this exercise must have necessary permissions\. For example, in the replication configuration, you specify the IAM role that Amazon S3 can assume\. You can do this only if the profile you use has the `iam:PassRole` permission\. If you use an administrator user credentials to create a named profile then you can perform all the tasks\. For more information, see [Granting a User Permissions to Pass a Role to an AWS Service](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_use_passrole.html) in the *IAM User Guide*\. 

   You will need to make sure these profiles have necessary permissions\. For example, the replication configuration includes an IAM that Amazon S3 can assume\. The named profile you use to attach such configuration to a bucket can do so only if it has the `iam:PassRole` permission\. If you specify administrator user credentials when creating these named profiles, then they all the permissions\. For more information, see [Granting a User Permissions to Pass a Role to an AWS Service](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_use_passrole.html) in the *IAM User Guide*\. 

1. Create the *source* bucket and enable versioning\. In this example, we create the *source* bucket in the US East \(N\. Virginia\) \(us\-east\-1\) Region\.

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

1. Create a *destination* bucket and enable versioning\. In this example, we create the *destination* bucket in the US West \(Oregon\) \(us\-west\-2\) Region\. Use an AWS account profile different from the one you used for the *source* bucket\.

   ```
   aws s3api create-bucket \
   --bucket destination \
   --region us-west-2 \
   --create-bucket-configuration LocationConstraint=us-west-2 \
   --profile acctB
   ```

   ```
   aws s3api put-bucket-versioning \
   --bucket destination \
   --versioning-configuration Status=Enabled \
   --profile acctB
   ```

1. Create an IAM role\. You specify this role in the replication configuration that you add to the *source* bucket later\. Amazon S3 assumes this role to replicate objects on your behalf\. You create an IAM role in two steps:
   + Create a role
   + Attach a permissions policy to the role

   1. Create an IAM role\.

      1. Copy the following trust policy and save it to a file called `S3-role-trust-policy.json` in the current directory on your local computer\. This policy grants Amazon S3 permissions to assume the role\.

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

      1. Run the following AWS CLI command to create a role:

         ```
         $ aws iam create-role \
         --role-name crrRole \
         --assume-role-policy-document file://s3-role-trust-policy.json  \
         --profile acctA
         ```

   1. Attach a permission policy to the role\.

      1. Copy the following permissions policy and save it to a file named `s3-role-perm-pol-changeowner.json` in the current directory on your local computer\. This policy grants permissions for various Amazon S3 bucket and object actions\. In the following steps, you create an IAM role and attach this policy to the role\. 

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
                     "arn:aws:s3:::source/*"
                  ]
               },
               {
                  "Effect":"Allow",
                  "Action":[
                     "s3:ListBucket",
                     "s3:GetReplicationConfiguration"
                  ],
                  "Resource":[
                     "arn:aws:s3:::source"
                  ]
               },
               {
                  "Effect":"Allow",
                  "Action":[
                     "s3:ReplicateObject",
                     "s3:ReplicateDelete",
                     "s3:ObjectOwnerOverrideToBucketOwner",
                     "s3:ReplicateTags",
                     "s3:GetObjectVersionTagging"
                  ],
                  "Resource":"arn:aws:s3:::destination/*"
               }
            ]
         }
         ```

      1. To create a policy and attach it to the role, run the following command:

         ```
         $ aws iam put-role-policy \
         --role-name crrRole \
         --policy-document file://s3-role-perm-pol-changeowner.json \
         --policy-name crrRolechangeownerPolicy \
         --profile acctA
         ```

1. Add replication configuration to your source bucket\. 

   1. The AWS CLI requires specifying the replication configuration as JSON\. Save the following JSON in a file called `replication.json` in the current directory on your local computer\. In the configuration, the addition of `AccessControlTranslation` to indicate change in replica ownership\.

      ```
      {
         "Role":"IAM-role-ARN",
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
               "Status":"Enabled",
               "Destination":{
                  "Bucket":"arn:aws:s3:::destination",
                  "Account":"destination-bucket-owner-account-id",
                  "AccessControlTranslation":{
                     "Owner":"Destination"
                  }
               }
            }
         ]
      }
      ```

   1. Edit the JSON by providing values for the *destination* bucket owner account ID and *IAM\-role\-ARN*\. Save the changes\.

   1. To add the replication configuration to the source bucket, run the following command\. Provide the *source* bucket name\.

      ```
      $ aws s3api put-bucket-replication \
      --replication-configuration file://replication-changeowner.json \
      --bucket source \
      --profile acctA
      ```

1. Check replica ownership in the Amazon S3 console\.

   1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\. 

   1. In the *source* bucket, create a folder named `Tax`\. 

   1. Add objects to the folder in the *source* bucket\. Verify that the *destination* bucket contains the object replicas and that the ownership of the replicas has changed to the AWS account that owns the *destination* bucket\.

## Change Replica Owner When Source and Destination Buckets Are Owned by Different AWS Accounts \(AWS SDK\)<a name="crr-ex3-sdk"></a>

 For code example to add replication configuration, see [Configure CRR When Source and Destination Buckets Are Owned by the Same AWS Account \(AWS SDK\)](crr-walkthrough1.md#crr-ex1-sdk)\. You will need to modify replication configuration appropriately\. For conceptual information, see [CRR Additional Configuration: Changing the Replica Owner](crr-change-owner.md)\. 