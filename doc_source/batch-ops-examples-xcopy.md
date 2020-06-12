# Example: Copying objects across AWS accounts using S3 Batch Operations<a name="batch-ops-examples-xcopy"></a>

You can use S3 Batch Operations to create a PUT copy job to copy objects to a different AWS account \(the *destination account*\)\. The following sections explain how to store and use a manifest that is in a different AWS account\. In the first section, you can use Amazon S3 Inventory to deliver the inventory report to the destination account for use during job creation or, you can use a comma\-separated values \(CSV\) manifest in the source or destination account as shown in the second section\. 

 \.

**Topics**
+ [Using an inventory report delivered to the destination account to copy objects across AWS accounts](#specify-batchjob-manifest-xaccount-inventory)
+ [Using a CSV manifest stored in the source account to copy objects across AWS accounts](#specify-batchjob-manifest-xaccount-csv)

## Using an inventory report delivered to the destination account to copy objects across AWS accounts<a name="specify-batchjob-manifest-xaccount-inventory"></a>

You can use Amazon S3 Inventory to deliver the inventory report to the destination account for use during job creation\. For using a CSV manifest in the source or destination account, see [Using a CSV manifest stored in the source account to copy objects across AWS accounts](#specify-batchjob-manifest-xaccount-csv)\.

Amazon S3 Inventory generates inventories of the objects in a bucket\. The resulting list is published to an output file\. The bucket that is inventoried is called the *source bucket*, and the bucket where the inventory report file is stored is called the *destination bucket*\. 

The Amazon S3 inventory report can be configured to be delivered to another AWS account\. This allows S3 Batch Operations to read the inventory report when the job is created in the destination AWS account\. 

For more information about Amazon S3 Inventory source and destination buckets, see [How do I set up Amazon S3 inventory?](storage-inventory.md#storage-inventory-how-to-set-up)\. 

The easiest way to set up an inventory is by using the AWS Management Console, but you can also use the REST API, AWS Command Line Interface \(AWS CLI\), or AWS SDKs\.

The following console procedure contains the high\-level steps for setting up permissions for an S3 Batch Operations job\. In this procedure, you copy objects from a source account to a destination account, with the inventory report stored in the destination AWS account\.

**To set up Amazon S3 inventory for source and destination buckets owned by different accounts**

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. Choose a destination bucket to store the inventory report in\.

   Decide on a destination manifest bucket for storing the inventory report\. In this procedure, the *destination account* is the account that owns both the destination manifest bucket and the bucket that the objects are copied to\. 

1. Configure an inventory to list the objects in a source bucket and publish the list to the destination manifest bucket\.

   Configure an inventory list for a source bucket\. When you do this, you specify the destination bucket where you want the list to be stored\. The inventory report for the source bucket is published to the destination bucket\. In this procedure, the *source account* is the account that owns the source bucket\. 

   For information about how to use the console to configure an inventory, see [How Do I Configure Amazon S3 Inventory?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/configure-inventory.html) in the *Amazon Simple Storage Service Console User Guide*\.

   Choose **CSV** for the output format\. 

   When you enter information for the destination bucket, choose **Buckets in another account**\. Then enter the name of the destination manifest bucket\. Optionally, you can enter the account ID of the destination account\.

   After the inventory configuration is saved, the console displays a message similar to the following: 

   Amazon S3 could not create a bucket policy on the destination bucket\. Ask the destination bucket owner to add the following bucket policy to allow Amazon S3 to place data in that bucket\.

   The console then displays a bucket policy that you can use for the destination bucket\.

1. Copy the destination bucket policy that appears on the console\.

1. In the destination account, add the copied bucket policy to the destination manifest bucket where the inventory report is stored\.

1. Create a role in the destination account that is based on the S3 Batch Operations trust policy\. For more information about the trust policy, see [Trust policy](batch-ops-iam-role-policies.md#batch-ops-iam-role-policies-trust)\. 

   For more information about creating a role, see [ Creating a Role to Delegate Permissions to an AWS Service ](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-service.html) in the *IAM User Guide*\.

   Enter a name for the role \(the example role uses the name `BatchOperationsDestinationRoleCOPY`\)\. Choose the **S3** service, and then choose the **S3 bucket Batch Operations** use case, which applies the trust policy to the role\. 

   Then choose **Create policy** to attach the following policy to the role\.

   ```
   {
           "Version": "2012-10-17",
           "Statement": [
               {
                   "Sid": "AllowBatchOperationsDestinationObjectCOPY",
                   "Effect": "Allow",
                   "Action": [
                       "s3:PutObject",
                       "s3:PutObjectVersionAcl",
                       "s3:PutObjectAcl",
                       "s3:PutObjectVersionTagging",
                       "s3:PutObjectTagging",
                       "s3:GetObject",
                       "s3:GetObjectVersion",
                       "s3:GetObjectAcl",
                       "s3:GetObjectTagging",
                       "s3:GetObjectVersionAcl",
                       "s3:GetObjectVersionTagging"
                   ],
                   "Resource": [
                       "arn:aws:s3:::ObjectDestinationBucket/*",
                       "arn:aws:s3:::ObjectSourceBucket/*",
                       "arn:aws:s3:::ObjectDestinationManifestBucket/*"
                   ]
               }
           ]
   }
   ```

   The role uses the policy to grant `batchoperations.s3.amazonaws.com` permission to read the manifest in the destination bucket\. It also grants permissions to GET objects, access control lists \(ACLs\), tags, and versions in the source object bucket\. And it grants permissions to PUT objects, ACLs, tags, and versions into the destination object bucket\.

1. In the source account, create a bucket policy for the source bucket that grants the role that you created in the previous step to GET objects, ACLs, tags, and versions in the source bucket\. This step allows S3 Batch Operations to get objects from the source bucket through the trusted role\.

   The following is an example of the bucket policy for the source account\.

   ```
   {
       "Version": "2012-10-17",
       "Statement": [
           {
               "Sid": "AllowBatchOperationsSourceObjectCOPY",
               "Effect": "Allow",
               "Principal": {
                   "AWS": "arn:aws:iam::DestinationAccountNumber:role/BatchOperationsDestinationRoleCOPY"
               },
               "Action": [
                   "s3:GetObject",
                   "s3:GetObjectVersion",
                   "s3:GetObjectAcl",
                   "s3:GetObjectTagging",
                   "s3:GetObjectVersionAcl",
                   "s3:GetObjectVersionTagging"
               ],
               "Resource": "arn:aws:s3:::ObjectSourceBucket/*"
           }
       ]
   }
   ```

1. After the inventory report is available, create an S3 Batch Operations PUT object copy job in the destination account, choosing the inventory report from the destination manifest bucket\. You need the ARN for the role that you created in the destination account\. 

   For general information about creating a job, see [Creating an S3 Batch Operations job](batch-ops-create-job.md)\. 

   For information about creating a job using the console, see [ Creating an S3 Batch Operations Job](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/batch-ops-create-job.html) in the *Amazon Simple Storage Service Console User Guide*\. 

## Using a CSV manifest stored in the source account to copy objects across AWS accounts<a name="specify-batchjob-manifest-xaccount-csv"></a>

You can use a CSV file that is stored in a different AWS account as a manifest for an S3 Batch Operations job\. For using an S3 Inventory Report, see [Using an inventory report delivered to the destination account to copy objects across AWS accounts](#specify-batchjob-manifest-xaccount-inventory)\.

The following procedure shows how to set up permissions when using an S3 Batch Operations job to copy objects from a source account to a destination account with the CSV manifest file stored in the source account\.

**To set up a CSV manifest stored in a different AWS account**

1. Create a role in the destination account that is based on the S3 Batch Operations trust policy\. In this procedure, the *destination account* is the account that the objects are being copied to\. 

   For more information about the trust policy, see [Trust policy](batch-ops-iam-role-policies.md#batch-ops-iam-role-policies-trust)\. 

   For more information about creating a role, see [Creating a Role to Delegate Permissions to an AWS Service ](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-service.html) in the *IAM User Guide*\.

   If you create the role using the console, enter a name for the role \(the example role uses the name `BatchOperationsDestinationRoleCOPY`\)\. Choose the **S3** service, and then choose the **S3 bucket Batch Operations** use case, which applies the trust policy to the role\. 

   Then choose **Create policy** to attach the following policy to the role\.

   ```
   {
           "Version": "2012-10-17",
           "Statement": [
               {
                   "Sid": "AllowBatchOperationsDestinationObjectCOPY",
                   "Effect": "Allow",
                   "Action": [
                       "s3:PutObject",
                       "s3:PutObjectVersionAcl",
                       "s3:PutObjectAcl",
                       "s3:PutObjectVersionTagging",
                       "s3:PutObjectTagging",
                       "s3:GetObject",
                       "s3:GetObjectVersion",
                       "s3:GetObjectAcl",
                       "s3:GetObjectTagging",
                       "s3:GetObjectVersionAcl",
                       "s3:GetObjectVersionTagging"
                   ],
                   "Resource": [
                       "arn:aws:s3:::ObjectDestinationBucket/*",
                       "arn:aws:s3:::ObjectSourceBucket/*",
                       "arn:aws:s3:::ObjectSourceManifestBucket/*"
                   ]
               }
           ]
   }
   ```

   Using the policy, the role grants `batchoperations.s3.amazonaws.com` permission to read the manifest in the source manifest bucket\. It grants permissions to GET objects, ACLs, tags, and versions in the source object bucket\. It also grants permissions to PUT objects, ACLs, tags, and versions into the destination object bucket\.

1. In the source account, create a bucket policy for the bucket that contains the manifest to grant the role that you created in the previous step to GET objects and versions in the source manifest bucket\. 

   This step allows S3 Batch Operations to read the manifest using the trusted role\. Apply the bucket policy to the bucket that contains the manifest\. 

   The following is an example of the bucket policy to apply to the source manifest bucket\.

   ```
   {
       "Version": "2012-10-17",
       "Statement": [
           {
               "Sid": "AllowBatchOperationsSourceManfiestRead",
               "Effect": "Allow",
               "Principal": {
                   "AWS": [
                       "arn:aws:iam::DestinationAccountNumber:user/ConsoleUserCreatingJob",
                       "arn:aws:iam::DestinationAccountNumber:role/BatchOperationsDestinationRoleCOPY"
                   ]
               },
               "Action": [
                   "s3:GetObject",
                   "s3:GetObjectVersion"
               ],
               "Resource": "arn:aws:s3:::ObjectSourceManifestBucket/*"
           }
       ]
   }
   ```

   This policy also grants permissions to allow a console user who is creating a job in the destination account the same permissions in the source manifest bucket through the same bucket policy\.

1. In the source account, create a bucket policy for the source bucket that grants the role you created to GET objects, ACLs, tags, and versions in the source object bucket\. S3 Batch Operations can then get objects from the source bucket through the trusted role\.

   The following is an example of the bucket policy for the bucket that contains the source objects\.

   ```
   {
       "Version": "2012-10-17",
       "Statement": [
           {
               "Sid": "AllowBatchOperationsSourceObjectCOPY",
               "Effect": "Allow",
               "Principal": {
                   "AWS": "arn:aws:iam::DestinationAccountNumber:role/BatchOperationsDestinationRoleCOPY"
               },
               "Action": [
                   "s3:GetObject",
                   "s3:GetObjectVersion",
                   "s3:GetObjectAcl",
                   "s3:GetObjectTagging",
                   "s3:GetObjectVersionAcl",
                   "s3:GetObjectVersionTagging"
               ],
               "Resource": "arn:aws:s3:::ObjectSourceBucket/*"
           }
       ]
   }
   ```

1. Create an S3 Batch Operations job in the destination account\. You need the Amazon Resource Name \(ARN\) for the role that you created in the destination account\. 

   For general information about creating a job, see [Creating an S3 Batch Operations job](batch-ops-create-job.md)\. 

   For information about creating a job using the console, see [ Creating an S3 Batch Operations Job](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/batch-ops-create-job.html) in the *Amazon Simple Storage Service Console User Guide*\. 