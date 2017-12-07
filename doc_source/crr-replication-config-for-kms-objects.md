# CRR Additional Configuration: Replicating Objects Created with Server\-Side Encryption \(SSE\) Using AWS KMS\-Managed Encryption Keys<a name="crr-replication-config-for-kms-objects"></a>

You might have objects in your source bucket that are created using server\-side encryption using AWS KMS\-managed keys\. By default, Amazon S3 does not replicate AWS KMS\-encrypted objects\. If you want Amazon S3 to replicate these objects, in addition to the [basic replication configuration](http://docs.aws.amazon.com/AmazonS3/latest/dev/crr-how-setup.html), you must do the following:

+ Provide the AWS KMS\-managed key for the destination bucket Region that you want Amazon S3 to use to encrypt object replicas\.

+ Grant additional permissions to the IAM role so that Amazon S3 can access the objects using the AWS KMS key\.


+ [Specifying Additional Information in the Replication Configuration](#crr-kms-extra-config)
+ [IAM Role Additional Permissions](#crr-kms-extra-permissions)
+ [Cross\-Account Scenario: Additional Permissions](#crr-kms-cross-acct-scenario)
+ [Related Considerations](#crr-kms-considerations)

## Specifying Additional Information in the Replication Configuration<a name="crr-kms-extra-config"></a>

In the [basic replication configuration](http://docs.aws.amazon.com/AmazonS3/latest/dev/crr-how-setup.html), add the following additional information\.

+ This feature \(for Amazon S3 to replicate objects that are encrypted using AWS KMS\-managed keys\) requires customer must explicitly opt in by adding the `<SourceSelectionCriteria>` element\.

  ```
  <SourceSelectionCriteria>
     <SseKmsEncryptedObjects>
       <Status>Enabled</Status>
     </SseKmsEncryptedObjects>
  </SourceSelectionCriteria>
  ```

+ Provide the AWS KMS key that you want Amazon S3 to use to encrypt object replicas by adding the `<EncryptionConfiguration>` element:

  ```
  <EncryptionConfiguration>
     <ReplicaKmsKeyID>The AWS KMS key ID (S3 can use to encrypt object replicas).</ReplicaKmsKeyID>
  </EncryptionConfiguration>
  ```
**Important**  
Note that the AWS KMS key Region must be the same as the Region of the destination bucket\. Make sure that the AWS KMS key is valid\. The `PUT` Bucket replication API does not check for invalid AWS KMS keys\. You get 200 OK response, but if the AWS KMS key is invalid, replication fails\.

Following is an example of a cross\-region replication configuration that includes the optional configuration elements:

```
<ReplicationConfiguration>
  <Role>arn:aws:iam::account-id:role/role-name</Role>
  <Rule>
    <Prefix>prefix1</Prefix>
    <Status>Enabled</Status>
    <SourceSelectionCriteria>
      <SseKmsEncryptedObjects>
        <Status>Enabled</Status>
      </SseKmsEncryptedObjects>
    </SourceSelectionCriteria>
    <Destination>
      <Bucket>arn:aws:s3:::destination-bucket</Bucket>
      <EncryptionConfiguration>
        <ReplicaKmsKeyID>The AWS KMS key ID (that S3 can use to encrypt object replicas).</ReplicaKmsKeyID>
      </EncryptionConfiguration>
    </Destination>
  </Rule>
</ReplicationConfiguration>
```

This replication configuration has one rule\. The rule applies to objects with the specified key prefix\. Amazon S3 uses the AWS KMS key ID to encrypt these object replicas\.

## IAM Role Additional Permissions<a name="crr-kms-extra-permissions"></a>

Amazon S3 needs additional permissions to replicate objects created using server\-side encryption using AWS KMS\-managed keys\. You must grant the following additional permissions to the IAM role:

+ Grant permission for the `s3:GetObjectVersionForReplication` action for source objects\. Permission for this action allows Amazon S3 to replicate the unencrypted object and the objects created with server\-side encryption using SSE\-S3 \(Amazon S3\-managed encryption key\) or AWS KMS–managed encryption \(SSE\-KMS\) keys\.
**Note**  
The permission for the `s3:GetObjectVersion` action allows replication of unencrypted and SSE\-S3 encrypted objects\. However, it does not allow replication of objects created using an AWS KMS\-managed encryption key\. 
**Note**  
We recommend that you use the `s3:GetObjectVersionForReplication` action instead of the `s3:GetObjectVersion` action because it provides Amazon S3 with only the minimum permissions necessary for cross\-region replication\.

+ Grant permissions for the following AWS KMS actions:

  + `kms:Decrypt` permissions for the AWS KMS key that was used to encrypt the source object\.

  + `kms:Encrypt` permissions for the AWS KMS key used to encrypt the object replica\.

  We recommend that you restrict these permissions to specific buckets and objects using the AWS KMS condition keys as shown in the following example policy statements: 

  ```
  {
      "Action": ["kms:Decrypt"],
      "Effect": "Allow",
      "Condition": {
          "StringLike": {
              "kms:ViaService": "s3.source-bucket-region.amazonaws.com",
              "kms:EncryptionContext:aws:s3:arn": [
                  "arn:aws:s3:::source-bucket-name/prefix1*",
              ]
          }
      },
      "Resource": [
          "List of AWS KMS key IDs that was used to encrypt source objects.", 
      ]
  },
  {
      "Action": ["kms:Encrypt"],
      "Effect": "Allow",
      "Condition": {
          "StringLike": {
              "kms:ViaService": "s3.destination-bucket-region.amazonaws.com",
              "kms:EncryptionContext:aws:s3:arn": [
                  "arn:aws:s3:::destination-bucket-name/prefix1*",
              ]
          }
      },
      "Resource": [
           "List of AWS KMS key IDs, that you want S3 to use to encrypt object replicas.", 
      ]
  }
  ```

  The AWS account that owns the IAM role must have permissions for these AWS KMS actions \(`kms:Encrypt` and `kms:Decrypt`\) for AWS KMS keys listed in the policy\. If the AWS KMS keys are owned by another AWS account, the key owner must grant these permissions to the AWS account that owns the IAM role\. For more information about managing access to these keys, see [Using IAM Policies with AWS KMS](http://docs.aws.amazon.com/kms/latest/developerguide/control-access-overview.html#overview-policy-elements) in the AWS Key Management Service Developer Guide\.

  The following is a complete IAM policy that grants the necessary permissions to replicate unencrypted objects, objects created with server\-side encryption using the Amazon S3\-managed encryption keys, and AWS KMS\-managed encryption keys\.
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
            "arn:aws:s3:::source-bucket/prefix1*"
         ]
      },
      {
         "Effect":"Allow",
         "Action":[
            "s3:ReplicateObject",
            "s3:ReplicateDelete"
         ],
         "Resource":"arn:aws:s3:::destination-bucket/prefix1*"
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
                  "arn:aws:s3:::source-bucket-name/prefix1*"
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
            "List of AWS KMS key IDs that you want S3 to use to encrypt object replicas."
         ]
      }
   ]
}
```

## Cross\-Account Scenario: Additional Permissions<a name="crr-kms-cross-acct-scenario"></a>

In a cross\-account scenario, the destination AWS KMS key must be a customer master key \(CMK\)\. The key owner must grant the source bucket owner permission to use the key, using one of the following methods:

+ Use the IAM console\.

  1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.

  1. Choose **Encryption keys**\.

  1. Select the AWS KMS key\.

  1. In **Key Policy**, **Key Users**, **External Accounts**, choose **Add External Account**\. 

  1. Specify source bucket account ID in the **arn:aws:iam::** box\.

  1. Choose **Save Changes**\.

+ Use the AWS CLI\. For more information, see [put\-key\-policy](http://docs.aws.amazon.com/cli/latest/reference/kms/put-key-policy.html) in the AWS CLI Command Reference\. For information about the underlying API, see [PutKeyPolicy](http://docs.aws.amazon.com/kms/latest/APIReference/API_PutKeyPolicy.html) in the [AWS Key Management Service API Reference](http://docs.aws.amazon.com/kms/latest/APIReference/)\. 

## Related Considerations<a name="crr-kms-considerations"></a>

After you enable CRR, as you add a large number of new objects with AWS KMS encryption, you might experience throttling \(HTTP 503 Slow Down errors\)\. This is related to the KMS transactions per second limit supported by AWS KMS\. For more information, see [Limits]( http://docs.aws.amazon.com/kms/latest/developerguide/limits.html) in the AWS Key Management Service Developer Guide\.

In this case, we recommend that you request an increase in your AWS KMS API rate limit by creating a case in the AWS Support Center\.  For more information, see https://console\.aws\.amazon\.com/support/home\#/\.