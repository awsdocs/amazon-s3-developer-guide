# Example 2: Bucket owner granting cross\-account bucket permissions<a name="example-walkthroughs-managing-access-example2"></a>

**Topics**
+ [Step 0: Preparing for the walkthrough](#cross-acct-access-step0)
+ [Step 1: Do the account a tasks](#access-policies-walkthrough-cross-account-permissions-acctA-tasks)
+ [Step 2: Do the account b tasks](#access-policies-walkthrough-cross-account-permissions-acctB-tasks)
+ [Step 3: Extra credit: Try explicit deny](#access-policies-walkthrough-example2-explicit-deny)
+ [Step 4: Clean up](#access-policies-walkthrough-example2-cleanup-step)

An AWS account—for example, Account A—can grant another AWS account, Account B, permission to access its resources such as buckets and objects\. Account B can then delegate those permissions to users in its account\. In this example scenario, a bucket owner grants cross\-account permission to another account to perform specific bucket operations\.

**Note**  
Account A can also directly grant a user in Account B permissions using a bucket policy\. But the user will still need permission from the parent account, Account B, to which the user belongs, even if Account B does not have permissions from Account A\. As long as the user has permission from both the resource owner and the parent account, the user will be able to access the resource\.

The following is a summary of the walkthrough steps:

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/access-policy-ex2.png)

1. Account A administrator user attaches a bucket policy granting cross\-account permissions to Account B to perform specific bucket operations\.

   Note that administrator user in Account B will automatically inherit the permissions\.

1. Account B administrator user attaches user policy to the user delegating the permissions it received from Account A\.

1. User in Account B then verifies permissions by accessing an object in the bucket owned by Account A\.

For this example, you need two accounts\. The following table shows how we refer to these accounts and the administrator users in them\. Per IAM guidelines \(see [About using an administrator user to create resources and grant permissions](example-walkthroughs-managing-access.md#about-using-root-credentials)\) we do not use the account root credentials in this walkthrough\. Instead, you create an administrator user in each account and use those credentials in creating resources and granting them permissions\.


| AWS account ID | Account referred to as | Administrator user in the account  | 
| --- | --- | --- | 
|  *1111\-1111\-1111*  |  Account A  |  AccountAadmin  | 
|  *2222\-2222\-2222*  |  Account B  |  AccountBadmin  | 

All the tasks of creating users and granting permissions are done in the AWS Management Console\. To verify permissions, the walkthrough uses the command line tools, AWS Command Line Interface \(CLI\) and AWS Tools for Windows PowerShell, so you don't need to write any code\.

## Step 0: Preparing for the walkthrough<a name="cross-acct-access-step0"></a>

1. Make sure you have two AWS accounts and that each account has one administrator user as shown in the table in the preceding section\.

   1. Sign up for an AWS account, if needed\. 

      1.  Go to [https://aws\.amazon\.com/s3/](https://aws.amazon.com/s3/) and click **Create an AWS Account**\. 

      1. Follow the on\-screen instructions\.

         AWS will notify you by email when your account is active and available for you to use\.

   1. Using Account A credentials, sign in to the [IAM console](https://console.aws.amazon.com/iam/home?#home) to create the administrator user:

      1. Create user AccountAadmin and note down the security credentials\. For instructions, see [Creating an IAM User in Your AWS Account](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html) in the *IAM User Guide*\. 

      1. Grant AccountAadmin administrator privileges by attaching a user policy giving full access\. For instructions, see [Working with Policies](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_manage.html) in the *IAM User Guide*\. 

   1. While you are in the IAM console, note down the **IAM User Sign\-In URL** on the **Dashboard**\. All users in the account must use this URL when signing in to the AWS Management Console\.

      For more information, see [How Users Sign in to Your Account](https://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_how-users-sign-in.html) in *IAM User Guide*\. 

   1. Repeat the preceding step using Account B credentials and create administrator user AccountBadmin\.

1. Set up either the AWS Command Line Interface \(CLI\) or the AWS Tools for Windows PowerShell\. Make sure you save administrator user credentials as follows:
   + If using the AWS CLI, create two profiles, AccountAadmin and AccountBadmin, in the config file\.
   + If using the AWS Tools for Windows PowerShell, make sure you store credentials for the session as AccountAadmin and AccountBadmin\.

   For instructions, see [Setting up the tools for the example walkthroughs](policy-eval-walkthrough-download-awscli.md)\. 

1. Save the administrator user credentials, also referred to as profiles\. You can use the profile name instead of specifying credentials for each command you enter\. For more information, see [Setting up the tools for the example walkthroughs](policy-eval-walkthrough-download-awscli.md)\. 

   1. Add profiles in the AWS CLI credentials file for each of the administrator users in the two accounts\. 

      ```
      [AccountAadmin]
      aws_access_key_id = access-key-ID
      aws_secret_access_key = secret-access-key
      region = us-east-1
      
      [AccountBadmin]
      aws_access_key_id = access-key-ID
      aws_secret_access_key = secret-access-key
      region = us-east-1
      ```

   1. If you are using the AWS Tools for Windows PowerShell

      ```
      set-awscredentials –AccessKey AcctA-access-key-ID –SecretKey AcctA-secret-access-key –storeas AccountAadmin
      set-awscredentials –AccessKey AcctB-access-key-ID –SecretKey AcctB-secret-access-key –storeas AccountBadmin
      ```

## Step 1: Do the account a tasks<a name="access-policies-walkthrough-cross-account-permissions-acctA-tasks"></a>

### Step 1\.1: Sign in to the AWS Management Console<a name="access-policies-walkthrough-cross-account-permissions-acctA-tasks-sign-in"></a>

Using the IAM user sign\-in URL for Account A first sign in to the AWS Management Console as AccountAadmin user\. This user will create a bucket and attach a policy to it\. 

### Step 1\.2: Create a bucket<a name="access-policies-walkthrough-example2a-create-bucket"></a>

1. In the Amazon S3 console, create a bucket\. This exercise assumes the bucket is created in the US East \(N\. Virginia\) region and is named `examplebucket`\.

   For instructions, see [How Do I Create an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-bucket.html) in the *Amazon Simple Storage Service Console User Guide*\. 

1. Upload a sample object to the bucket\.

   For instructions, go to [Add an Object to a Bucket](https://docs.aws.amazon.com/AmazonS3/latest/gsg/PuttingAnObjectInABucket.html) in the *Amazon Simple Storage Service Getting Started Guide*\. 

### Step 1\.3: Attach a bucket policy to grant cross\-account permissions to account b<a name="access-policies-walkthrough-example2a"></a>

The bucket policy grants the `s3:GetBucketLocation` and `s3:ListBucket` permissions to Account B\. It is assumed you are still signed into the console using AccountAadmin user credentials\.

1. Attach the following bucket policy to `examplebucket`\. The policy grants Account B permission for the `s3:GetBucketLocation` and `s3:ListBucket` actions\.

   For instructions, see [How Do I Add an S3 Bucket Policy?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/add-bucket-policy.html) in the *Amazon Simple Storage Service Console User Guide*\. 

   ```
   {
      "Version": "2012-10-17",
      "Statement": [
         {
            "Sid": "Example permissions",
            "Effect": "Allow",
            "Principal": {
               "AWS": "arn:aws:iam::AccountB-ID:root"
            },
            "Action": [
               "s3:GetBucketLocation",
               "s3:ListBucket"
            ],
            "Resource": [
               "arn:aws:s3:::awsexamplebucket1"
            ]
         }
      ]
   }
   ```

1. Verify Account B \(and thus its administrator user\) can perform the operations\.
   + Using the AWS CLI

     ```
     aws s3 ls s3://examplebucket --profile AccountBadmin
     aws s3api get-bucket-location --bucket examplebucket --profile AccountBadmin
     ```
   + Using the AWS Tools for Windows PowerShell

     ```
     get-s3object -BucketName example2bucket -StoredCredentials AccountBadmin 
     get-s3bucketlocation -BucketName example2bucket -StoredCredentials AccountBadmin
     ```

## Step 2: Do the account b tasks<a name="access-policies-walkthrough-cross-account-permissions-acctB-tasks"></a>

Now the Account B administrator creates a user, Dave, and delegates the permissions received from Account A\. 

### Step 2\.1: Sign in to the AWS Management Console<a name="access-policies-walkthrough-cross-account-permissions-acctB-tasks-sign-in"></a>

Using the IAM user sign\-in URL for Account B, first sign in to the AWS Management Console as AccountBadmin user\. 

### Step 2\.2: Create user dave in account b<a name="access-policies-walkthrough-example2b-create-user"></a>

In the IAM console, create a user, Dave\. 

For instructions, see [Creating IAM Users \(AWS Management Console\)](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html#id_users_create_console) in the *IAM User Guide*\. 

### Step 2\.3: Delegate permissions to user dave<a name="access-policies-walkthrough-example2-delegate-perm-userdave"></a>

Create an inline policy for the user Dave by using the following policy\. You will need to update the policy by providing your bucket name\.

It is assumed you are signed in to the console using AccountBadmin user credentials\.

```
{
   "Version": "2012-10-17",
   "Statement": [
      {
         "Sid": "Example",
         "Effect": "Allow",
         "Action": [
            "s3:ListBucket"
         ],
         "Resource": [
            "arn:aws:s3:::awsexamplebucket1"
         ]
      }
   ]
}
```

For instructions, see [Working with Inline Policies](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_inline-using.html) in the *IAM User Guide*\.

### Step 2\.4: Test permissions<a name="access-policies-walkthrough-example2b-user-dave-access"></a>

Now Dave in Account B can list the contents of `examplebucket` owned by Account A\. You can verify the permissions using either of the following procedures\. 

**Test using the AWS CLI**

1. Add the UserDave profile to the AWS CLI config file\. For more information about the config file, see [Setting up the tools for the example walkthroughs](policy-eval-walkthrough-download-awscli.md)\.

   ```
   [profile UserDave]
   aws_access_key_id = access-key
   aws_secret_access_key = secret-access-key
   region = us-east-1
   ```

1. At the command prompt, enter the following AWS CLI command to verify Dave can now get an object list from the `examplebucket` owned by Account A\. Note the command specifies the UserDave profile\.

   ```
   aws s3 ls s3://examplebucket --profile UserDave
   ```

   Dave does not have any other permissions\. So if he tries any other operation—for example, the following get bucket location—Amazon S3 returns permission denied\. 

   ```
   aws s3api get-bucket-location --bucket examplebucket --profile UserDave
   ```

**Test using AWS Tools for Windows PowerShell**

1. Store Dave's credentials as AccountBDave\.

   ```
   set-awscredentials -AccessKey AccessKeyID -SecretKey SecretAccessKey -storeas AccountBDave
   ```

1. Try the List Bucket command\.

   ```
   get-s3object -BucketName example2bucket -StoredCredentials AccountBDave
   ```

   Dave does not have any other permissions\. So if he tries any other operation—for example, the following get bucket location—Amazon S3 returns permission denied\. 

   ```
   get-s3bucketlocation -BucketName example2bucket -StoredCredentials AccountBDave
   ```

## Step 3: Extra credit: Try explicit deny<a name="access-policies-walkthrough-example2-explicit-deny"></a>

You can have permissions granted via an ACL, a bucket policy, and a user policy\. But if there is an explicit deny set via either a bucket policy or a user policy, the explicit deny takes precedence over any other permissions\. For testing, let's update the bucket policy and explicitly deny Account B the `s3:ListBucket` permission\. The policy also grants `s3:ListBucket` permission, but explicit deny takes precedence, and Account B or users in Account B will not be able to list objects in `examplebucket`\.

1. Using credentials of user AccountAadmin in Account A, replace the bucket policy by the following\. 

   ```
   {
      "Version": "2012-10-17",
      "Statement": [
         {
            "Sid": "Example permissions",
            "Effect": "Allow",
            "Principal": {
               "AWS": "arn:aws:iam::AccountB-ID:root"
            },
            "Action": [
               "s3:GetBucketLocation",
               "s3:ListBucket"
            ],
            "Resource": [
               "arn:aws:s3:::awsexamplebucket1"
            ]
         },
         {
            "Sid": "Deny permission",
            "Effect": "Deny",
            "Principal": {
               "AWS": "arn:aws:iam::AccountB-ID:root"
            },
            "Action": [
               "s3:ListBucket"
            ],
            "Resource": [
               "arn:aws:s3:::awsexamplebucket1"
            ]
         }
      ]
   }
   ```

1. Now if you try to get a bucket list using AccountBadmin credentials, you will get access denied\.
   + Using the AWS CLI:

     ```
     aws s3 ls s3://examplebucket --profile AccountBadmin
     ```
   + Using the AWS Tools for Windows PowerShell:

     ```
     get-s3object -BucketName example2bucket -StoredCredentials AccountBDave
     ```

## Step 4: Clean up<a name="access-policies-walkthrough-example2-cleanup-step"></a>

1. After you are done testing, you can do the following to clean up\.

   1. Sign in to the AWS Management Console \([AWS Management Console](https://console.aws.amazon.com/)\) using Account A credentials, and do the following:
     + In the Amazon S3 console, remove the bucket policy attached to *examplebucket*\. In the bucket **Properties**, delete the policy in the **Permissions** section\. 
     + If the bucket is created for this exercise, in the Amazon S3 console, delete the objects and then delete the bucket\. 
     + In the IAM console, remove the AccountAadmin user\.

1. Sign in to the AWS Management Console \([AWS Management Console](https://console.aws.amazon.com/)\) using Account B credentials\. In the IAM console, delete user AccountBadmin\.