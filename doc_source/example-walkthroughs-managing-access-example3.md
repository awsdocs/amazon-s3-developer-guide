# Example 3: Bucket Owner Granting Its Users Permissions to Objects It Does Not Own<a name="example-walkthroughs-managing-access-example3"></a>

**Topics**
+ [Step 0: Preparing for the Walkthrough](#access-policies-walkthrough-cross-account-acl-step0)
+ [Step 1: Do the Account A Tasks](#access-policies-walkthrough-cross-account-acl-acctA-tasks)
+ [Step 2: Do the Account B Tasks](#access-policies-walkthrough-cross-account-acl-acctB-tasks)
+ [Step 3: Test Permissions](#access-policies-walkthrough-cross-account-acl-verify)
+ [Step 4: Clean Up](#access-policies-walkthrough-cross-account-acl-cleanup)

The scenario for this example is that a bucket owner wants to grant permission to access objects, but not all objects in the bucket are owned by the bucket owner\. How can a bucket owner grant permission on objects it does not own? For this example, the bucket owner is trying to grant permission to users in its own account\.

A bucket owner can enable other AWS accounts to upload objects\. These objects are owned by the accounts that created them\. The bucket owner does not own objects that were not created by the bucket owner\. Therefore, for the bucket owner to grant access to these objects, the object owner must first grant permission to the bucket owner using an object ACL\. The bucket owner can then delegate those permissions via a bucket policy\. In this example, the bucket owner delegates permission to users in its own account\.

The following is a summary of the walkthrough steps:

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/access-policy-ex3.png)

1. Account A administrator user attaches a bucket policy with two statements\.
   + Allow cross\-account permission to Account B to upload objects\.
   + Allow a user in its own account to access objects in the bucket\.

1. Account B administrator user uploads objects to the bucket owned by Account A\.

1. Account B administrator updates the object ACL adding grant that gives the bucket owner full\-control permission on the object\.

1. User in Account A verifies by accessing objects in the bucket, regardless of who owns them\.

For this example, you need two accounts\. The following table shows how we refer to these accounts and the administrator users in these accounts\. Per IAM guidelines \(see [About Using an Administrator User to Create Resources and Grant Permissions](example-walkthroughs-managing-access.md#about-using-root-credentials)\) we do not use the account root credentials in this walkthrough\. Instead, you create an administrator user in each account and use those credentials in creating resources and granting them permissions\.


| AWS Account ID | Account Referred To As | Administrator User in the Account  | 
| --- | --- | --- | 
|  *1111\-1111\-1111*  |  Account A  |  AccountAadmin  | 
|  *2222\-2222\-2222*  |  Account B  |  AccountBadmin  | 

All the tasks of creating users and granting permissions are done in the AWS Management Console\. To verify permissions, the walkthrough uses the command line tools, AWS Command Line Interface \(CLI\) and AWS Tools for Windows PowerShell, so you don't need to write any code\. 

## Step 0: Preparing for the Walkthrough<a name="access-policies-walkthrough-cross-account-acl-step0"></a>

1. Make sure you have two AWS accounts and each account has one administrator user as shown in the table in the preceding section\.

   1. Sign up for an AWS account, if needed\. 

      1.  Go to [https://aws\.amazon\.com/s3/](https://aws.amazon.com/s3/) and click **Create an AWS Account**\. 

      1. Follow the on\-screen instructions\. AWS will notify you by email when your account is active and available for you to use\.

   1. Using Account A credentials, sign in to the [IAM console](https://console.aws.amazon.com/iam/home?#home) and do the following to create an administrator user:
      + Create user AccountAadmin and note down security credentials\. For more information about adding users, see [Creating an IAM User in Your AWS Account](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html) in the *IAM User Guide*\. 
      + Grant AccountAadmin administrator privileges by attaching a user policy giving full access\. For instructions, see [Working with Policies](http://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_manage.html) in the *IAM User Guide*\. 
      + In the IAM console **Dashboard**, note down the** IAM User Sign\-In URL**\. Users in this account must use this URL when signing in to the AWS Management Console\. For more information, see [How Users Sign in to Your Account](http://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_how-users-sign-in.html) in *IAM User Guide*\. 

   1. Repeat the preceding step using Account B credentials and create administrator user AccountBadmin\.

1. Set up either the AWS Command Line Interface \(CLI\) or the AWS Tools for Windows PowerShell\. Make sure you save administrator user credentials as follows:
   + If using the AWS CLI, create two profiles, AccountAadmin and AccountBadmin, in the config file\.
   + If using the AWS Tools for Windows PowerShell, make sure you store credentials for the session as AccountAadmin and AccountBadmin\.

   For instructions, see [Setting Up the Tools for the Example Walkthroughs](policy-eval-walkthrough-download-awscli.md)\. 

## Step 1: Do the Account A Tasks<a name="access-policies-walkthrough-cross-account-acl-acctA-tasks"></a>

### Step 1\.1: Sign In to the AWS Management Console<a name="access-policies-walkthrough-cross-account-permissions-acctA-tasks-sign-in-example3"></a>

Using the IAM user sign\-in URL for Account A first sign in to the AWS Management Console as AccountAadmin user\. This user will create a bucket and attach a policy to it\. 

### Step 1\.2: Create a Bucket, a User, and Add a Bucket Policy Granting User Permissions<a name="access-policies-walkthrough-cross-account-acl-create-bucket"></a>

1. In the Amazon S3 console, create a bucket\. This exercise assumes the bucket is created in the US East \(N\. Virginia\) region and the name is `examplebucket`\.

   For instructions, see [How Do I Create an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-bucket.html) in the *Amazon Simple Storage Service Console User Guide*\. 

1. In the IAM console, create a user Dave\. 

   For instructions, see [Creating IAM Users \(AWS Management Console\)](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html#id_users_create_console) in the *IAM User Guide*\. 

1. Note down the Dave credentials\. 

1. In the Amazon S3 console, attach the following bucket policy to `examplebucket` bucket\. For instructions, see [How Do I Add an S3 Bucket Policy?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/add-bucket-policy.html) in the *Amazon Simple Storage Service Console User Guide*\. Follow the steps to add a bucket policy\. For information about how to find account IDs, see [Finding Your AWS Account ID](http://docs.aws.amazon.com//general/latest/gr/acct-identifiers.html#FindingYourAccountIdentifiers)\. 

   The policy grants Account B the `s3:PutObject` and `s3:ListBucket` permissions\. The policy also grants user Dave the `s3:GetObject` permission\. 

   ```
   {
      "Version": "2012-10-17",
      "Statement": [
         {
            "Sid": "Statement1",
            "Effect": "Allow",
            "Principal": {
               "AWS": "arn:aws:iam::AccountB-ID:root"
            },
            "Action": [
               "s3:PutObject"
            ],
            "Resource": [
               "arn:aws:s3:::examplebucket/*"
            ]
         },
         {
            "Sid": "Statement3",
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

## Step 2: Do the Account B Tasks<a name="access-policies-walkthrough-cross-account-acl-acctB-tasks"></a>

Now that Account B has permissions to perform operations on Account A's bucket, the Account B administrator will do the following;
+ Upload an object to Account A's bucket\. 
+ Add a grant in the object ACL to allow Account A, the bucket owner, full control\. 

**Using the AWS CLI**

1. Using the `put-object` AWS CLI command, upload an object\. The \-`-body` parameter in the command identifies the source file to upload\. For example, if the file is on `C:` drive of a Windows machine, you would specify `c:\HappyFace.jpg`\. The `--key` parameter provides the key name for the object\. 

   ```
   aws s3api put-object --bucket examplebucket --key HappyFace.jpg --body HappyFace.jpg --profile AccountBadmin
   ```

1. Add a grant to the object ACL to allow the bucket owner full control of the object\. For information about how to find a canonical user ID, see [Finding Your Account Canonical User ID](http://docs.aws.amazon.com//general/latest/gr/acct-identifiers.html#FindingCanonicalId)\.

   ```
   aws s3api put-object-acl --bucket examplebucket --key HappyFace.jpg --grant-full-control id="AccountA-CanonicalUserID" --profile AccountBadmin
   ```

**Using the AWS Tools for Windows PowerShell**

1. Using the `Write-S3Object` AWS Tools for Windows PowerShell command, upload an object\. 

   ```
   Write-S3Object -BucketName examplebucket -key HappyFace.jpg -file HappyFace.jpg -StoredCredentials AccountBadmin
   ```

1. Add a grant to the object ACL to allow the bucket owner full control of the object\.

   ```
   Set-S3ACL -BucketName examplebucket -Key HappyFace.jpg -CannedACLName "bucket-owner-full-control" -StoredCreden
   ```

## Step 3: Test Permissions<a name="access-policies-walkthrough-cross-account-acl-verify"></a>

Now verify user Dave in Account A can access the object owned by Account B\.

**Using the AWS CLI**

1. Add user Dave credentials to the AWS CLI config file and create a new profile, `UserDaveAccountA`\. For more information, see [Setting Up the Tools for the Example Walkthroughs](policy-eval-walkthrough-download-awscli.md)\.

   ```
   [profile UserDaveAccountA]
   aws_access_key_id = access-key
   aws_secret_access_key = secret-access-key
   region = us-east-1
   ```

1. Execute the `get-object` AWS CLI command to download `HappyFace.jpg` and save it locally\. You provide user Dave credentials by adding the `--profile` parameter\.

   ```
   aws s3api get-object --bucket examplebucket --key HappyFace.jpg Outputfile.jpg --profile UserDaveAccountA
   ```

**Using the AWS Tools for Windows PowerShell**

1. Store user Dave AWS credentials, as UserDaveAccountA, to persistent store\. 

   ```
   Set-AWSCredentials -AccessKey UserDave-AccessKey -SecretKey UserDave-SecretAccessKey -storeas UserDaveAccountA
   ```

1. Execute the Read\-S3Object command to download the `HappyFace.jpg` object and save it locally\. You provide user Dave credentials by adding the `-StoredCredentials` parameter\. 

   ```
   Read-S3Object -BucketName examplebucket -Key HappyFace.jpg -file HappyFace.jpg  -StoredCredentials UserDaveAccountA
   ```

## Step 4: Clean Up<a name="access-policies-walkthrough-cross-account-acl-cleanup"></a>

1. After you are done testing, you can do the following to clean up\.

   1. Sign in to the AWS Management Console \([AWS Management Console](https://console.aws.amazon.com/)\) using Account A credentials, and do the following:
     + In the Amazon S3 console, remove the bucket policy attached to *examplebucket*\. In the bucket **Properties**, delete the policy in the **Permissions** section\. 
     + If the bucket is created for this exercise, in the Amazon S3 console, delete the objects and then delete the bucket\. 
     + In the IAM console, remove the AccountAadmin user\.

1. Sign in to the AWS Management Console \([AWS Management Console](https://console.aws.amazon.com/)\) using Account B credentials\. In the IAM console, delete user AccountBadmin\.