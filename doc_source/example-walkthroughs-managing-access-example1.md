# Example 1: Bucket Owner Granting Its Users Bucket Permissions<a name="example-walkthroughs-managing-access-example1"></a>

**Topics**
+ [Step 0: Preparing for the Walkthrough](#grant-permissions-to-user-in-your-account-step0)
+ [Step 1: Create Resources \(a Bucket and an IAM User\) in Account A and Grant Permissions](#grant-permissions-to-user-in-your-account-step1)
+ [Step 2: Test Permissions](#grant-permissions-to-user-in-your-account-test)

In this exercise, an AWS account owns a bucket, and it has an IAM user in the account\. The user by default has no permissions\. The parent account must grant permissions to the user to perform any tasks\. Both the bucket owner and the parent account to which the user belongs are the same\. Therefore, the AWS account can use a bucket policy, a user policy, or both to grant its user permissions on the bucket\. You will grant some permissions using a bucket policy and grant other permissions using a user policy\.

The following steps summarize the walkthrough:

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/access-policy-ex1.png)

1. Account administrator creates a bucket policy granting a set of permissions to the user\.

1. Account administrator attaches a user policy to the user granting additional permissions\.

1. User then tries permissions granted via both the bucket policy and the user policy\.

For this example, you will need an AWS account\. Instead of using the root credentials of the account, you will create an administrator user \(see [About Using an Administrator User to Create Resources and Grant Permissions](example-walkthroughs-managing-access.md#about-using-root-credentials)\)\. We refer to the AWS account and the administrator user as follows:


| Account ID | Account Referred To As | Administrator User in the Account | 
| --- | --- | --- | 
|  *1111\-1111\-1111*  |  Account A  |  AccountAadmin  | 

All the tasks of creating users and granting permissions are done in the AWS Management Console\. To verify permissions, the walkthrough uses the command line tools, AWS Command Line Interface \(CLI\) and AWS Tools for Windows PowerShell, to verify the permissions, so you don't need to write any code\.

## Step 0: Preparing for the Walkthrough<a name="grant-permissions-to-user-in-your-account-step0"></a>

1. Make sure you have an AWS account and that it has a user with administrator privileges\.

   1. Sign up for an account, if needed\. We refer to this account as Account A\.

      1.  Go to [https://aws\.amazon\.com/s3](https://aws.amazon.com/s3) and click **Sign Up**\. 

      1. Follow the on\-screen instructions\.

         AWS will notify you by email when your account is active and available for you to use\.

   1. In Account A, create an administrator user AccountAadmin\. Using Account A credentials, sign in to the [IAM console](https://console.aws.amazon.com/iam/home?#home) and do the following: 

      1. Create user AccountAadmin and note down the user security credentials\. 

         For instructions, see [Creating an IAM User in Your AWS Account](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html) in the *IAM User Guide*\. 

      1. Grant AccountAadmin administrator privileges by attaching a user policy giving full access\. 

         For instructions, see [Working with Policies](http://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_manage.html) in the *IAM User Guide*\. 

      1. Note down the **IAM User Sign\-In URL** for AccountAadmin\. You will need to use this URL when signing in to the AWS Management Console\. For more information about where to find it, see [How Users Sign in to Your Account](http://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_how-users-sign-in.html) in *IAM User Guide*\. Note down the URL for each of the accounts\.

1. Set up either the AWS Command Line Interface \(CLI\) or the AWS Tools for Windows PowerShell\. Make sure you save administrator user credentials as follows:
   + If using the AWS CLI, create a profile, AccountAadmin, in the config file\.
   + If using the AWS Tools for Windows PowerShell, make sure you store credentials for the session as AccountAadmin\.

   For instructions, see [Setting Up the Tools for the Example Walkthroughs](policy-eval-walkthrough-download-awscli.md)\. 

## Step 1: Create Resources \(a Bucket and an IAM User\) in Account A and Grant Permissions<a name="grant-permissions-to-user-in-your-account-step1"></a>

Using the credentials of user AccountAadmin in Account A, and the special IAM user sign\-in URL, sign in to the AWS Management Console and do the following:

1. Create Resources \(a bucket and an IAM user\)

   1. In the Amazon S3 console create a bucket\. Note down the AWS region in which you created it\. For instructions, see [How Do I Create an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-bucket.html) in the *Amazon Simple Storage Service Console User Guide*\. 

   1. In the IAM console, do the following: 

      1. Create a user, Dave\.

         For instructions, see [Creating IAM Users \(AWS Management Console\)](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html#id_users_create_console) in the *IAM User Guide*\. 

      1. Note down the UserDave credentials\.

      1. Note down the Amazon Resource Name \(ARN\) for user Dave\. In the IAM console, select the user, and the **Summary** tab provides the user ARN\.

1. Grant Permissions\. 

   Because the bucket owner and the parent account to which the user belongs are the same, the AWS account can grant user permissions using a bucket policy, a user policy, or both\. In this example, you do both\. If the object is also owned by the same account, the bucket owner can grant object permissions in the bucket policy \(or an IAM policy\)\.

   1. In the Amazon S3 console, attach the following bucket policy to *examplebucket*\. 

      The policy has two statements\. 
      + The first statement grants Dave the bucket operation permissions `s3:GetBucketLocation` and `s3:ListBucket`\.
      + The second statement grants the `s3:GetObject` permission\. Because Account A also owns the object, the account administrator is able to grant the `s3:GetObject` permission\. 

      In the `Principal` statement, Dave is identified by his user ARN\. For more information about policy elements, see [Access Policy Language Overview](access-policy-language-overview.md)\.

      ```
      {
         "Version": "2012-10-17",
         "Statement": [
            {
               "Sid": "statement1",
               "Effect": "Allow",
               "Principal": {
                  "AWS": "arn:aws:iam::AccountA-ID:user/Dave"
               },
               "Action": [
                  "s3:GetBucketLocation",
                  "s3:ListBucket"
               ],
               "Resource": [
                  "arn:aws:s3:::examplebucket"
               ]
            },
            {
               "Sid": "statement2",
               "Effect": "Allow",
               "Principal": {
                  "AWS": "arn:aws:iam::AccountA-ID:user/Dave"
               },
               "Action": [
                   "s3:GetObject"
               ],
               "Resource": [
                  "arn:aws:s3:::examplebucket/*"
               ]
            }
         ]
      }
      ```

   1. Create an inline policy for the user Dave by using the following policy\. The policy grants Dave the `s3:PutObject` permission\. You need to update the policy by providing your bucket name\.

      ```
      {
         "Version": "2012-10-17",
         "Statement": [
            {
               "Sid": "PermissionForObjectOperations",
               "Effect": "Allow",
               "Action": [
                  "s3:PutObject"
               ],
               "Resource": [
                  "arn:aws:s3:::examplebucket/*"
               ]
            }
         ]
      }
      ```

      For instructions, see [Working with Inline Policies](http://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_inline-using.html) in the *IAM User Guide*\. Note you need to sign in to the console using Account A credentials\.

## Step 2: Test Permissions<a name="grant-permissions-to-user-in-your-account-test"></a>

Using Dave's credentials, verify that the permissions work\. You can use either of the following two procedures\.

**Test Using the AWS CLI**

1. Update the AWS CLI config file by adding the following UserDaveAccountA profile\. For more information, see [Setting Up the Tools for the Example Walkthroughs](policy-eval-walkthrough-download-awscli.md)\.

   ```
   [profile UserDaveAccountA]
   aws_access_key_id = access-key
   aws_secret_access_key = secret-access-key
   region = us-east-1
   ```

1. Verify that Dave can perform the operations as granted in the user policy\. Upload a sample object using the following AWS CLI `put-object` command\. 

   The `--body` parameter in the command identifies the source file to upload\. For example, if the file is in the root of the C: drive on a Windows machine, you specify `c:\HappyFace.jpg`\. The `--key` parameter provides the key name for the object\.

   ```
   aws s3api put-object --bucket examplebucket --key HappyFace.jpg --body HappyFace.jpg --profile UserDaveAccountA
   ```

   Execute the following AWS CLI command to get the object\. 

   ```
   aws s3api get-object --bucket examplebucket --key HappyFace.jpg OutputFile.jpg --profile UserDaveAccountA
   ```

**Test Using the AWS Tools for Windows PowerShell**

1. Store Dave's credentials as AccountADave\. You then use these credentials to PUT and GET an object\.

   ```
   set-awscredentials -AccessKey AccessKeyID -SecretKey SecretAccessKey -storeas AccountADave
   ```

1. Upload a sample object using the AWS Tools for Windows PowerShell `Write-S3Object` command using user Dave's stored credentials\. 

   ```
   Write-S3Object -bucketname examplebucket -key HappyFace.jpg -file HappyFace.jpg -StoredCredentials AccountADave
   ```

   Download the previously uploaded object\.

   ```
   Read-S3Object -bucketname examplebucket -key HappyFace.jpg -file Output.jpg -StoredCredentials AccountADave
   ```