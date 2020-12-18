# Using an AWS KMS CMK to encrypt your metrics exports<a name="storage_lens_encrypt_permissions"></a>

To grant Amazon S3 Storage Lens permission to encrypt using a customer managed AWS Key Management Service \(AWS KMS\) customer master key \(CMK\), you must use a key policy\. To update your key policy so that you can use an AWS KMS CMK to encrypt your S3 Storage Lens metrics exports, follow these steps\. 

**To grant permissions to encrypt using your AWS KMS CMK**

1. Sign into the AWS Management Console using the AWS account that owns the customer managed CMK\.

1. Open the AWS KMS console at [https://console\.aws\.amazon\.com/kms](https://console.aws.amazon.com/kms)\.

1. To change the AWS Region, use the **Region selector** in the upper\-right corner of the page\.

1. In the navigation pane, choose **Customer managed keys**\.

1. Under **Customer managed keys**, choose the key that you want to use to encrypt the metrics exports\. CMKs are Region\-specific and must be in the same Region as the metrics export destination S3 bucket\.

1. Under **Key policy**, choose **Switch to policy view**\.

1. To update the key policy, choose **Edit**\.

1. Under **Edit key policy**, add the following key policy to the existing key policy\.

   ```
   {
       "Sid": "Allow Amazon S3 Storage Lens use of the CMK",
       "Effect": "Allow",
       "Principal": {
           "Service": "storage-lens.s3.amazonaws.com"
       },
       "Action": [
           "kms:GenerateDataKey"
       ],
       "Resource": "*"
   }
   ```

1. Choose **Save changes**\.

For more information about creating AWS KMS customer managed CMKs and using key policies, see the following topics in the *AWS Key Management Service Developer Guide*:
+ [Getting started](https://docs.aws.amazon.com/kms/latest/developerguide/getting-started.html)
+ [Using key policies in AWS KMS](https://docs.aws.amazon.com/kms/latest/developerguide/key-policies.html)

You can also use the AWS KMS PUT key policy \([ PutKeyPolicy](http://amazonaws.com/kms/latest/APIReference/API_PutKeyPolicy.html)\) to copy the key policy to the customer managed CMK that you want to use to encrypt the metrics exports using the REST API, AWS CLI, and SDKs\.