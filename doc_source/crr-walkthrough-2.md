# Example 2: Configure CRR When Source and Destination Buckets Are Owned by Different AWS Accounts<a name="crr-walkthrough-2"></a>

Setting up cross\-region replication \(CRR\) when *source* and *destination* buckets are owned by different AWS accounts is similar to setting CRR when both buckets are owned by the same account\. The only difference is that the *destination* bucket owner must grant the *source* bucket owner permission to replicate objects by adding a bucket policy\. 

**To set up CRR when source and destination buckets are owned by different AWS accounts**

1. In this example, you create *source* and *destination* buckets in two different AWS accounts\. You need to have two credential profiles set for the AWS CLI \(in this example, we use `acctA` and `acctB` for profile names\)\. For more information about setting credential profiles, see [Named Profiles](https://docs.aws.amazon.com/cli/latest/userguide/cli-multiple-profiles.html) in the *AWS Command Line Interface User Guide*\. 

1. Follow the step\-by\-step instructions in [CRR Example 1: Same AWS Account](crr-walkthrough1.md)\.with the following changes:
   + For all CLI commands related to *source* bucket activities \(for creating the *source* bucket, enabling versioning, and creating the IAM role\), use the `acctA` profile\. Use the `acctB` profile to create the *destination* bucket\. 
   + Make sure that the permissions policy specifies the *source* and *destination* buckets that you created for this example\.

1. In the AWS console, add the following bucket policy on the *destination* bucket to allow the owner of the *source* bucket to replicate objects\. Be sure to edit the policy by providing the AWS account ID of the *source* bucket owner and the *destination* bucket name\.

   ```
   {
      "Version":"2008-10-17",
      "Id":"",
      "Statement":[
         {
            "Sid":"Stmt123",
            "Effect":"Allow",
            "Principal":{
               "AWS":"arn:aws:iam::source-bucket-owner-AWS-acct-ID:root"
            },
            "Action":["s3:ReplicateObject", "s3:ReplicateDelete"],
            "Resource":"arn:aws:s3:::destination/*"
         }
      ]
   }
   ```

Choose the bucket and add bucket policy\. For instructions, see [How Do I Add an S3 Bucket Policy?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/add-bucket-policy.html) in the *Amazon Simple Storage Service Console User Guide*\.