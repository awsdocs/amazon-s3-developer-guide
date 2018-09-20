# CRR Additional Configuration: Replicating Objects Created with Server\-Side Encryption \(SSE\) Using AWS KMS\-Managed Encryption Keys<a name="crr-replication-config-for-kms-objects"></a>

By default, Amazon S3 doesn't replicate objects that are stored at rest using server\-side encryption with AWS KMS\-managed keys\. This section explains additional configuration you add to direct Amazon S3 to replicate these objects\. 

For an example with step\-by\-step instructions, see [Example 4: Replicating Encrypted Objects](crr-walkthrough-4.md)\. For information about creating a replication configuration, see [Cross\-Region Replication ](crr.md)\. 

**Topics**
+ [Specifying Additional Information in the Replication Configuration](#crr-kms-extra-config)
+ [Granting Additional Permissions for the IAM Role](#crr-kms-extra-permissions)
+ [Granting Additional Permissions for Cross\-Account Scenarios](#crr-kms-cross-acct-scenario)
+ [AWS KMS Transaction Limit Considerations](#crr-kms-considerations)

## Specifying Additional Information in the Replication Configuration<a name="crr-kms-extra-config"></a>

In the replication configuration, you do the following:
+ In the Destination configuration, add the AWS KMS key that you want Amazon S3 to use to encrypt object replicas\. 
+ Explicitly opt in by enabling replication of objects encrypted using the AWS KMS keys by adding the SourceSelectionCriteria element\.

```
<ReplicationConfiguration>
   <Rule>
      ...
      <SourceSelectionCriteria>
         <SseKmsEncryptedObjects>
           <Status>Enabled</Status>
         </SseKmsEncryptedObjects>
      </SourceSelectionCriteria>

      <Destination>
          ...
          <EncryptionConfiguration>
             <ReplicaKmsKeyID>AWS KMS key ID for the AWS region of the destination bucket.</ReplicaKmsKeyID>
          </EncryptionConfiguration>
       </Destination>
      ...
   </Rule>
</ReplicationConfiguration>
```

**Important**  
The AWS KMS key must have been created in the same AWS Region as the destination bucket\.   
The AWS KMS key *must* be valid\. The `PUT` Bucket replication API doesn't check the validity of AWS KMS keys\. If you use an invalid key, you will receive the 200 OK status code in response, but replication fails\.

The following example of a cross\-region replication configuration includes the optional configuration elements:

```
<?xml version="1.0" encoding="UTF-8"?>
<ReplicationConfiguration>
   <Role>arn:aws:iam::account-id:role/role-name</Role>
   <Rule>
      <ID>Rule-1</ID>
      <Priority>1</Priority>
      <Status>Enabled</Status>
      <DeleteMarkerReplication>
         <Status>Disabled</Status>
      </DeleteMarkerReplication>
      <Filter>
         <Prefix>Tax</Prefix>
      </Filter>
      <Destination>
         <Bucket>arn:aws:s3:::destination-bucket</Bucket>
         <EncryptionConfiguration>
            <ReplicaKmsKeyID>The AWS KMS key ID for the AWS region of the destination bucket (S3 uses it to encrypt object replicas).</ReplicaKmsKeyID>
         </EncryptionConfiguration>
      </Destination>
      <SourceSelectionCriteria>
         <SseKmsEncryptedObjects>
            <Status>Enabled</Status>
         </SseKmsEncryptedObjects>
      </SourceSelectionCriteria>
   </Rule>
</ReplicationConfiguration>
```

This replication configuration has one rule\. The rule applies to objects with the `Tax` key prefix\. Amazon S3 uses the AWS KMS key ID to encrypt these object replicas\.

## Granting Additional Permissions for the IAM Role<a name="crr-kms-extra-permissions"></a>

To replicate objects created using server\-side encryption with AWS KMS\-managed keys, grant the following additional permissions to the IAM role you specify in the replication configuration\. You grant these permissions by updating the permission policy associated with the IAM role:
+ Permission for the `s3:GetObjectVersionForReplication` action for source objects\. Permission for this action allows Amazon S3 to replicate both unencrypted objects and objects created with server\-side encryption using Amazon S3\-managed encryption \(SSE\-S3 \) keys or AWS KMS–managed encryption \(SSE\-KMS\) keys\.
**Note**  
We recommend that you use the `s3:GetObjectVersionForReplication` action instead of the `s3:GetObjectVersion` action because it provides Amazon S3 with only the minimum permissions necessary for cross\-region replication\. In addition, permission for the `s3:GetObjectVersion` action allows replication of unencrypted and SSE\-S3\-encrypted objects, but not of objects created using an AWS KMS\-managed encryption key\. 
+ Permissions for the following AWS KMS actions:
  + `kms:Decrypt` permissions for the AWS KMS key that was used to encrypt the source object
  + `kms:Encrypt` permissions for the AWS KMS key used to encrypt the object replica

  We recommend that you restrict these permissions to specific buckets and objects using AWS KMS condition keys; as shown in the following example policy statements: 

  ```
  {
      "Action": ["kms:Decrypt"],
      "Effect": "Allow",
      "Condition": {
          "StringLike": {
              "kms:ViaService": "s3.source-bucket-region.amazonaws.com",
              "kms:EncryptionContext:aws:s3:arn": [
                  "arn:aws:s3:::source-bucket-name/key-prefix1*",
              ]
          }
      },
      "Resource": [
          "List of AWS KMS key IDs used to encrypt source objects.", 
      ]
  },
  {
      "Action": ["kms:Encrypt"],
      "Effect": "Allow",
      "Condition": {
          "StringLike": {
              "kms:ViaService": "s3.destination-bucket-region.amazonaws.com",
              "kms:EncryptionContext:aws:s3:arn": [
                  "arn:aws:s3:::destination-bucket-name/key-prefix1*",
              ]
          }
      },
      "Resource": [
           "AWS KMS key IDs (for the AWS region of the destination bucket). S3 uses it to encrypt object replicas", 
      ]
  }
  ```

  The AWS account that owns the IAM role must have permissions for these AWS KMS actions \(`kms:Encrypt` and `kms:Decrypt`\) for AWS KMS keys listed in the policy\. If the AWS KMS keys are owned by another AWS account, the key owner must grant these permissions to the AWS account that owns the IAM role\. For more information about managing access to these keys, see [Using IAM Policies with AWS KMS](http://docs.aws.amazon.com/kms/latest/developerguide/control-access-overview.html#overview-policy-elements) in the* AWS Key Management Service Developer Guide*\.

  The following is a complete IAM policy that grants the necessary permissions to replicate unencrypted objects, objects created with server\-side encryption using Amazon S3\-managed encryption keys, and AWS KMS\-managed encryption keys\.
**Note**  
Objects created with server\-side encryption using customer\-provided \(SSE\-C\) encryption keys are not replicated\.

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
            "arn:aws:s3:::source-bucket/key-prefix1*"
         ]
      },
      {
         "Effect":"Allow",
         "Action":[
            "s3:ReplicateObject",
            "s3:ReplicateDelete"
         ],
         "Resource":"arn:aws:s3:::destination-bucket/key-prefix1*"
      },
      {
         "Action":[
            "kms:Decrypt"
         ],
         "Effect":"Allow",
         "Condition":{
            "StringLike":{
               "kms:ViaService":"s3.source-bucket-region.amazonaws.com",
               "kms:EncryptionContext:aws:s3:arn":[
                  "arn:aws:s3:::source-bucket-name/key-prefix1*"
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
               "kms:ViaService":"s3.destination-bucket-region.amazonaws.com",
               "kms:EncryptionContext:aws:s3:arn":[
                  "arn:aws:s3:::destination-bucket-name/prefix1*"
               ]
            }
         },
         "Resource":[
            "AWS KMS key IDs (for the AWS region of the destination bucket) to use for encrypting object replicas"
         ]
      }
   ]
}
```

## Granting Additional Permissions for Cross\-Account Scenarios<a name="crr-kms-cross-acct-scenario"></a>

In a cross\-account scenario, where *source* and *destination* buckets are owned by different AWS accounts, the AWS KMS key to encrypt object replicas must be a customer master key \(CMK\)\. The key owner must grant the source bucket owner permission to use the key\. <a name="cross-acct-kms-key-permission"></a>

**To grant the source bucket owner permission to use the key \(IAM console\)**

1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.

1. Choose **Encryption keys**\.

1. Choose the AWS KMS key\.

1. In **Key Policy**, **Key Users**, **External Accounts**, choose **Add External Account**\. 

1. For the **arn:aws:iam::**, enter the source bucket account ID\.

1. Choose **Save Changes**\.

**To grant the source bucket owner permission to use the key \(AWS CLI\)**
+ For information, see [put\-key\-policy](http://docs.aws.amazon.com/cli/latest/reference/kms/put-key-policy.html) in the* AWS CLI Command Reference*\. For information about the underlying API, see [PutKeyPolicy](http://docs.aws.amazon.com/kms/latest/APIReference/API_PutKeyPolicy.html) in the *[AWS Key Management Service API Reference](http://docs.aws.amazon.com/kms/latest/APIReference/)\.*

## AWS KMS Transaction Limit Considerations<a name="crr-kms-considerations"></a>

When you add many new objects with AWS KMS encryption after enabling cross\-region replication \(CRR\), you might experience throttling \(HTTP 503 Slow Down errors\)\. Throttling occurs when the number of KMS transactions per second exceeds the current limit\. For more information, see [Limits]( http://docs.aws.amazon.com/kms/latest/developerguide/limits.html) in the *AWS Key Management Service Developer Guide*\.

We recommend that you request an increase in your AWS KMS API rate limit by creating a case in the AWS Support Center\. For more information, see https://console\.aws\.amazon\.com/support/home\#/\.