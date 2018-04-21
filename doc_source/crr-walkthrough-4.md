# CRR Walkthrough 4: Direct Amazon S3 to Replicate Objects Created with Server\-Side Encryption Using AWS KMS\-Managed Encryption Keys<a name="crr-walkthrough-4"></a>

You can have objects in your source bucket that are created using server\-side encryption using AWS KMS\-managed keys\. By default, Amazon S3 does not replicate these objects\. But you can add optional configuration to the bucket replication configuration to direct Amazon S3 to replicate these objects\. 

For this exercise, you first set up replication configuration in a cross\-account scenario \(source and destination buckets are owned by different AWS accounts\)\. This section then provides instructions for you to update the configuration to direct Amazon S3 to replicate objects encrypted with AWS KMS\-managed keys\.

**Note**  
Although this example uses an existing walkthrough to set up CRR in a cross\-account scenario, replication of SSE\-KMS encrypted objects can be also configured when both the source and destination buckets have the same owner\.

1. Complete CRR walkthrough 2\. For instructions, see [Walkthrough 2: Configure Cross\-Region Replication Where Source and Destination Buckets Are Owned by Different AWS Accounts](crr-walkthrough-2.md)\.

1. Replace the replication configuration on the source bucket with the following \(which adds the options that direct Amazon S3 to replicate source objects encrypted using AWS KMS keys\)\. 

   ```
    <ReplicationConfiguration>
     <Role>IAM role ARN</Role>
     <Rule>
       <Prefix>Tax</Prefix>
       <Status>Enabled</Status>
       <SourceSelectionCriteria>
         <SseKmsEncryptedObjects>
           <Status>Enabled</Status>
         </SseKmsEncryptedObjects>
       </SourceSelectionCriteria>
       <Destination>
         <Bucket>arn:aws:s3:::dest-bucket-name</Bucket>
         <EncryptionConfiguration>
           <ReplicaKmsKeyID>AWS KMS key ID to use for encrypting object replicas.</ReplicaKmsKeyID>
         </EncryptionConfiguration>
       </Destination>
     </Rule>
   </ReplicationConfiguration>
   ```

   In this example, you can use either the AWS CLI or the AWS SDK to add the replication configuration\. 
   + Using AWS CLI\. 

     The AWS CLI requires you to specify the replication configuration as JSON\. Save the following JSON in a file \(`replication.json`\)\. 

     ```
     {
       "Role": "IAM role ARN",
       "Rules": [
         {
           "Prefix": "Tax",
           "Status": "Enabled",
     	  "SourceSelectionCriteria": {
     	     "SseKmsEncryptedObjects" : {
     		    "Status" : "Enabled"
     		 }
     	  },
           "Destination": {
             "Bucket": "arn:aws:s3:::dest-bucket-name",
     		"EncryptionConfiguration" : {
     		   "ReplicaKmsKeyID": "AWS KMS key ARN(created in the same region as the destination bucket)."
     		}
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

1. Update the permission policy of the IAM role by adding the permissions for AWS KMS actions\. 

   ```
   {
      "Action":[
         "kms:Decrypt"
      ],
      "Effect":"Allow",
      "Condition":{
         "StringLike":{
            "kms:ViaService":"s3.source-bucket-region.amazonaws.com",
            "kms:EncryptionContext:aws:s3:arn":[
               "arn:aws:s3:::source-bucket-name/Tax"
            ]
         }
      },
      "Resource":[
         "List of AWS KMS key IDs used to encrypt source objects."
      ]
   },
   {
      "Action":[
         "kms:Encrypt"
      ],
      "Effect":"Allow",
      "Condition":{
         "StringLike":{
            "kms:ViaService":"s3.dest-bucket-region.amazonaws.com",
            "kms:EncryptionContext:aws:s3:arn":[
               "arn:aws:s3:::dest-bucket-name/Tax"
            ]
         }
      },
      "Resource":[
         "List of AWS KMS key IDs that you want S3 to use to encrypt object replicas."
      ]
   }
   ```

   ```
   {
       "Version": "2012-10-17",
       "Statement": [
           {
               "Effect": "Allow",
               "Action": [
                   "s3:GetObjectVersionForReplication",
                   "s3:GetObjectVersionAcl"
               ],
               "Resource": [
                   "arn:aws:s3:::source-bucket/Tax"
               ]
           },
           {
               "Effect": "Allow",
               "Action": [
                   "s3:ListBucket",
                   "s3:GetReplicationConfiguration"
               ],
               "Resource": [
                   "arn:aws:s3:::source-bucket"
               ]
           },
           {
               "Effect": "Allow",
               "Action": [
                   "s3:ReplicateObject",
                   "s3:ReplicateDelete"
               ],
               "Resource": "arn:aws:s3:::dest-bucket/*"
           },
           {
               "Action": [
                   "kms:Decrypt"
               ],
               "Effect": "Allow",
               "Condition": {
                   "StringLike": {
                       "kms:ViaService": "s3.source-bucket-region.amazonaws.com",
                       "kms:EncryptionContext:aws:s3:arn": [
                           "arn:aws:s3:::source-bucket/Tax*"
                       ]
                   }
               },
               "Resource": [
                   "List of AWS KMS key IDs used to encrypt source objects."
               ]
           },
           {
               "Action": [
                   "kms:Encrypt"
               ],
               "Effect": "Allow",
               "Condition": {
                   "StringLike": {
                       "kms:ViaService": "s3.dest-bucket-region.amazonaws.com",
                       "kms:EncryptionContext:aws:s3:arn": [
                           "arn:aws:s3:::dest-bucket/Tax*"
                       ]
                   }
               },
               "Resource": [
                   "List of AWS KMS key IDs that you want S3 to use to encrypt object replicas."
               ]
           }
       ]
   }
   ```

1. Test the setup\. In the console, upload an object to the source bucket \(in the `/Tax` folder\) using the AWS KMS\-managed key\. Verify that Amazon S3 replicated the object in the destination bucket\.