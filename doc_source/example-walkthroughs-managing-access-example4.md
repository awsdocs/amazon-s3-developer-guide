# Example 4: Bucket Owner Granting Cross\-account Permission to Objects It Does Not Own<a name="example-walkthroughs-managing-access-example4"></a>

**Topics**
+ [Background: Cross\-Account Permissions and Using IAM Roles](#access-policies-walkthrough-example4-overview)
+ [Step 0: Preparing for the Walkthrough](#access-policies-walkthrough-example4-step0)
+ [Step 1: Do the Account A Tasks](#access-policies-walkthrough-example4-step1)
+ [Step 2: Do the Account B Tasks](#access-policies-walkthrough-example4-step2)
+ [Step 3: Do the Account C Tasks](#access-policies-walkthrough-example4-step3)
+ [Step 4: Clean Up](#access-policies-walkthrough-example4-step6)
+ [Related Resources](#RelatedResources-managing-access-example4)

 In this example scenario, you own a bucket and you have enabled other AWS accounts to upload objects\. That is, your bucket can have objects that other AWS accounts own\. 

Now, suppose as a bucket owner, you need to grant cross\-account permission on objects, regardless of who the owner is, to a user in another account\. For example, that user could be a billing application that needs to access object metadata\. There are two core issues:
+ The bucket owner has no permissions on those objects created by other AWS accounts\. So for the bucket owner to grant permissions on objects it does not own, the object owner, the AWS account that created the objects, must first grant permission to the bucket owner\. The bucket owner can then delegate those permissions\.
+ Bucket owner account can delegate permissions to users in its own account \(see [Example 3: Bucket Owner Granting Its Users Permissions to Objects It Does Not Own ](example-walkthroughs-managing-access-example3.md)\), but it cannot delegate permissions to other AWS accounts, because cross\-account delegation is not supported\. 

In this scenario, the bucket owner can create an AWS Identity and Access Management \(IAM\) role with permission to access objects, and grant another AWS account permission to assume the role temporarily enabling it to access objects in the bucket\. 

## Background: Cross\-Account Permissions and Using IAM Roles<a name="access-policies-walkthrough-example4-overview"></a>

 IAM roles enable several scenarios to delegate access to your resources, and cross\-account access is one of the key scenarios\. In this example, the bucket owner, Account A, uses an IAM role to temporarily delegate object access cross\-account to users in another AWS account, Account C\. Each IAM role you create has two policies attached to it:
+ A trust policy identifying another AWS account that can assume the role\.
+ An access policy defining what permissions—for example, `s3:GetObject`—are allowed when someone assumes the role\. For a list of permissions you can specify in a policy, see [Specifying Permissions in a Policy](using-with-s3-actions.md)\.

The AWS account identified in the trust policy then grants its user permission to assume the role\. The user can then do the following to access objects:
+ Assume the role and, in response, get temporary security credentials\. 
+ Using the temporary security credentials, access the objects in the bucket\.

For more information about IAM roles, go to [IAM Roles](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html) in *IAM User Guide*\. 

The following is a summary of the walkthrough steps:

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/access-policy-ex4.png)

1. Account A administrator user attaches a bucket policy granting Account B conditional permission to upload objects\.

1. Account A administrator creates an IAM role, establishing trust with Account C, so users in that account can access Account A\. The access policy attached to the role limits what user in Account C can do when the user accesses Account A\.

1. Account B administrator uploads an object to the bucket owned by Account A, granting full\-control permission to the bucket owner\.

1. Account C administrator creates a user and attaches a user policy that allows the user to assume the role\.

1. User in Account C first assumes the role, which returns the user temporary security credentials\. Using those temporary credentials, the user then accesses objects in the bucket\.

For this example, you need three accounts\. The following table shows how we refer to these accounts and the administrator users in these accounts\. Per IAM guidelines \(see [About Using an Administrator User to Create Resources and Grant Permissions](example-walkthroughs-managing-access.md#about-using-root-credentials)\) we do not use the account root credentials in this walkthrough\. Instead, you create an administrator user in each account and use those credentials in creating resources and granting them permissions


| AWS Account ID | Account Referred To As | Administrator User in the Account  | 
| --- | --- | --- | 
|  *1111\-1111\-1111*  |  Account A  |  AccountAadmin  | 
|  *2222\-2222\-2222*  |  Account B  |  AccountBadmin  | 
|  *3333\-3333\-3333*  |  Account C  |  AccountCadmin  | 

## Step 0: Preparing for the Walkthrough<a name="access-policies-walkthrough-example4-step0"></a>

**Note**  
You may want to open a text editor and write down some of the information as you walk through the steps\. In particular, you will need account IDs, canonical user IDs, IAM User Sign\-in URLs for each account to connect to the console, and Amazon Resource Names \(ARNs\) of the IAM users, and roles\. 

1. Make sure you have three AWS accounts and each account has one administrator user as shown in the table in the preceding section\.

   1. Sign up for AWS accounts, as needed\. We refer to these accounts as Account A, Account B, and Account C\.

      1.  Go to [https://aws\.amazon\.com/s3/](https://aws.amazon.com/s3/) and click **Create an AWS Account**\. 

      1. Follow the on\-screen instructions\.

         AWS will notify you by email when your account is active and available for you to use\.

   1. Using Account A credentials, sign in to the [IAM console](https://console.aws.amazon.com/iam/home?#home) and do the following to create an administrator user:
      + Create user AccountAadmin and note down security credentials\. For more information about adding users, see [Creating an IAM User in Your AWS Account](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html) in the *IAM User Guide*\. 
      + Grant AccountAadmin administrator privileges by attaching a user policy giving full access\. For instructions, see [Working with Policies](http://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_manage.html) in the *IAM User Guide*\. 
      + In the IAM Console **Dashboard**, note down the** IAM User Sign\-In URL**\. Users in this account must use this URL when signing in to the AWS Management Console\. For more information, go to [How Users Sign In to Your Account](http://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_how-users-sign-in.html) in *IAM User Guide*\. 

   1. Repeat the preceding step to create administrator users in Account B and Account C\.

1. For Account C, note down the account ID\. 

   When you create an IAM role in Account A, the trust policy grants Account C permission to assume the role by specifying the account ID\. You can find account information as follows:

   1. Go to [https://aws\.amazon\.com/](https://aws.amazon.com/) and from the **My Account/Console** drop\-down menu, select **Security Credentials**\. 

   1. Sign in using appropriate account credentials\.

   1. Click **Account Identifiers** and note down the **AWS Account ID** and the **Canonical User ID**\.

1. When creating a bucket policy, you will need the following information\. Note down these values:
   + **Canonical user ID of Account A** – When the Account A administrator grants conditional upload object permission to the Account B administrator, the condition specifies the canonical user ID of the Account A user that must get full\-control of the objects\. 
**Note**  
The canonical user ID is the Amazon S3–only concept\. It is a 64\-character obfuscated version of the account ID\. 
   + **User ARN for Account B administrator** – You can find the user ARN in the IAM console\. You will need to select the user and find the user's ARN in the **Summary** tab\.

     In the bucket policy, you grant AccountBadmin permission to upload objects and you specify the user using the ARN\. Here's an example ARN value:

     ```
     arn:aws:iam::AccountB-ID:user/AccountBadmin
     ```

1. Set up either the AWS Command Line Interface \(CLI\) or the AWS Tools for Windows PowerShell\. Make sure you save administrator user credentials as follows:
   + If using the AWS CLI, create profiles, AccountAadmin and AccountBadmin, in the config file\.
   + If using the AWS Tools for Windows PowerShell, make sure you store credentials for the session as AccountAadmin and AccountBadmin\.

   For instructions, see [Setting Up the Tools for the Example Walkthroughs](policy-eval-walkthrough-download-awscli.md)\.

## Step 1: Do the Account A Tasks<a name="access-policies-walkthrough-example4-step1"></a>

In this example, Account A is the bucket owner\. So user AccountAadmin in Account A will create a bucket, attach a bucket policy granting the Account B administrator permission to upload objects, create an IAM role granting Account C permission to assume the role so it can access objects in the bucket\. 

### Step 1\.1: Sign In to the AWS Management Console<a name="access-policies-walkthrough-cross-account-permissions-acctA-tasks-sign-in-example4"></a>

Using the IAM User Sign\-in URL for Account A, first sign in to the AWS Management Console as AccountAadmin user\. This user will create a bucket and attach a policy to it\. 

### Step 1\.2: Create a Bucket and Attach a Bucket Policy<a name="access-policies-walkthrough-example2d-step1-1"></a>

In the Amazon S3 console, do the following:

1. Create a bucket\. This exercise assumes the bucket name is `examplebucket`\.

   For instructions, see [How Do I Create an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-bucket.html) in the *Amazon Simple Storage Service Console User Guide*\. 

1. Attach the following bucket policy granting conditional permission to the Account B administrator permission to upload objects\.

   You need to update the policy by providing your own values for *examplebucket*, *AccountB\-ID*, and the *CanonicalUserId\-of\-AWSaccountA\-BucketOwner*\. 

   ```
   {
       "Version": "2012-10-17",
       "Statement": [
           {
               "Sid": "111",
               "Effect": "Allow",
               "Principal": {
                   "AWS": "arn:aws:iam::AccountB-ID:user/AccountBadmin"
               },
               "Action": "s3:PutObject",
               "Resource": "arn:aws:s3:::examplebucket/*"
           },
           {
               "Sid": "112",
               "Effect": "Deny",
               "Principal": {
                   "AWS": "arn:aws:iam::AccountB-ID:user/AccountBadmin"
               },
               "Action": "s3:PutObject",
               "Resource": "arn:aws:s3:::examplebucket/*",
               "Condition": {
                   "StringNotEquals": {
                       "s3:x-amz-grant-full-control": "id=CanonicalUserId-of-AWSaccountA-BucketOwner"
                   }
               }
           }
       ]
   }
   ```

### Step 1\.3: Create an IAM Role to Allow Account C Cross\-Account Access in Account A<a name="access-policies-walkthrough-example2d-step1-2"></a>

In the IAM console, create an IAM role \("examplerole"\) that grants Account C permission to assume the role\. Make sure you are still signed in as the Account A administrator because the role must be created in Account A\.

1. Before creating the role, prepare the managed policy that defines the permissions that the role requires\. You attach this policy to the role in a later step\.

   1. In the navigation pane on the left, click **Policies** and then click **Create Policy**\.

   1. Next to **Create Your Own Policy**, click **Select**\.

   1. Enter `access-accountA-bucket` in the **Policy Name** field\.

   1. Copy the following access policy and paste it into the **Policy Document** field\. The access policy grants the role `s3:GetObject` permission so when Account C user assumes the role, it can only perform the `s3:GetObject` operation\.

      ```
      {
        "Version": "2012-10-17",
        "Statement": [
          {
            "Effect": "Allow",
            "Action": "s3:GetObject",
            "Resource": "arn:aws:s3:::examplebucket/*"
          }
        ]
      }
      ```

   1. Click **Create Policy**\.

      The new policy appears in the list of managed policies\.

1. In the navigation pane on the left, click **Roles** and then click **Create New Role**\.

1. Enter `examplerole` for the role name, and then click **Next Step**\.

1. Under **Select Role Type**, select **Role for Cross\-Account Access**, and then click the **Select** button next to **Provide access between AWS accounts you own**\.

1. Enter the Account C account ID\.

   For this walkthrough you do not need to require users to have multi\-factor authentication \(MFA\) to assume the role, so leave that option unselected\.

1. Click **Next Step** to set the permissions that will be associated with the role\.

1. Select the box next to the `access-accountA-bucket` policy that you created and then click **Next Step**\.

   The Review page appears so you can confirm the settings for the role before it's created\. One very important item to note on this page is the link that you can send to your users who need to use this role\. Users who click the link go straight to the Switch Role page with the Account ID and Role Name fields already filled in\. You can also see this link later on the Role Summary page for any cross\-account role\.

1. After reviewing the role, click **Create Role**\.

   The `examplerole` role is displayed in the list of roles\.

1. Click the role name `examplerole`\.

1. Select the **Trust Relationships** tab\.

1. Click **Show policy document** and verify the trust policy shown matches the following policy\.

   The following trust policy establishes trust with Account C, by allowing it the `sts:AssumeRole` action\. For more information, go to [AssumeRole](http://docs.aws.amazon.com/STS/latest/APIReference/API_AssumeRole.html) in the *AWS Security Token Service API Reference*\.

   ```
   {
     "Version": "2012-10-17",
     "Statement": [
       {
         "Sid": "",
         "Effect": "Allow",
         "Principal": {
           "AWS": "arn:aws:iam::AccountC-ID:root"
         },
         "Action": "sts:AssumeRole"
       }
     ]
   }
   ```

1. Note down the Amazon Resource Name \(ARN\) of the `examplerole` role you created\. 

   Later in the following steps, you attach a user policy to allow an IAM user to assume this role, and you identify the role by the ARN value\. 

## Step 2: Do the Account B Tasks<a name="access-policies-walkthrough-example4-step2"></a>

The examplebucket owned by Account A needs objects owned by other accounts\. In this step, the Account B administrator uploads an object using the command line tools\.
+ Using the put\-object AWS CLI command, upload an object to the `examplebucket`\. 

  ```
  aws s3api put-object --bucket examplebucket --key HappyFace.jpg --body HappyFace.jpg --grant-full-control id="canonicalUserId-ofTheBucketOwner" --profile AccountBadmin
  ```

  Note the following:
  + The `--Profile` parameter specifies AccountBadmin profile, so the object is owned by Account B\.
  + The parameter `grant-full-control` grants the bucket owner full\-control permission on the object as required by the bucket policy\.
  + The `--body` parameter identifies the source file to upload\. For example, if the file is on the C: drive of a Windows computer, you specify `c:\HappyFace.jpg`\. 

## Step 3: Do the Account C Tasks<a name="access-policies-walkthrough-example4-step3"></a>

In the preceding steps, Account A has already created a role, `examplerole`, establishing trust with Account C\. This allows users in Account C to access Account A\. In this step, Account C administrator creates a user \(Dave\) and delegates him the `sts:AssumeRole` permission it received from Account A\. This will allow Dave to assume the `examplerole` and temporarily gain access to Account A\. The access policy that Account A attached to the role will limit what Dave can do when he accesses Account A—specifically, get objects in `examplebucket`\.

### Step 3\.1: Create a User in Account C and Delegate Permission to Assume `examplerole`<a name="cross-acct-access-using-role-step3-1"></a>

1. Using the IAM user sign\-in URL for Account C, first sign in to the AWS Management Console as AccountCadmin user\. 

1. In the IAM console, create a user Dave\. 

   For instructions, see [Creating IAM Users \(AWS Management Console\)](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html#id_users_create_console) in the *IAM User Guide*\. 

1. Note down the Dave credentials\. Dave will need these credentials to assume the `examplerole` role\.

1. Create an inline policy for the Dave IAM user to delegate the `sts:AssumeRole` permission to Dave on the `examplerole` role in account A\. 

   1. In the navigation pane on the left, click **Users**\.

   1. Click the user name Dave\.

   1. On the user details page, select the **Permissions** tab and then expand the **Inline Policies** section\.

   1. Choose **click here** \(or **Create User Policy**\)\.

   1. Click **Custom Policy**, and then click **Select**\.

   1. Enter a name for the policy in the **Policy Name** field\.

   1. Copy the following policy into the **Policy Document** field\.

      You will need to update the policy by providing the Account A ID\.

      ```
      {
        "Version": "2012-10-17",
        "Statement": [
          {
            "Effect": "Allow",
            "Action": ["sts:AssumeRole"],
            "Resource": "arn:aws:iam::AccountA-ID:role/examplerole"
          }
        ]
      }
      ```

   1. Click **Apply Policy**

1. Save Dave's credentials to the config file of the AWS CLI by adding another profile, AccountCDave\.

   ```
   [profile AccountCDave]
   aws_access_key_id = UserDaveAccessKeyID
   aws_secret_access_key = UserDaveSecretAccessKey
   region = us-west-2
   ```

### Step 3\.2: Assume Role \(examplerole\) and Access Objects<a name="cross-acct-access-using-role-step3-2"></a>

Now Dave can access objects in the bucket owned by Account A as follows:
+ Dave first assumes the `examplerole` using his own credentials\. This will return temporary credentials\.
+ Using the temporary credentials, Dave will then access objects in Account A's bucket\.

1. At the command prompt, execute the following AWS CLI `assume-role` command using the AccountCDave profile\. 

   You will need to update the ARN value in the command by providing the Account A ID where `examplerole` is defined\.

   ```
   aws sts assume-role --role-arn arn:aws:iam::accountA-ID:role/examplerole --profile AccountCDave --role-session-name test
   ```

   In response, AWS Security Token Service \(STS\) returns temporary security credentials \(access key ID, secret access key, and a session token\)\.

1. Save the temporary security credentials in the AWS CLI config file under the `TempCred` profile\.

   ```
   [profile TempCred]
   aws_access_key_id = temp-access-key-ID
   aws_secret_access_key = temp-secret-access-key
   aws_session_token = session-token
   region = us-west-2
   ```

1. At the command prompt, execute the following AWS CLI command to access objects using the temporary credentials\. For example, the command specifies the head\-object API to retrieve object metadata for the `HappyFace.jpg` object\.

   ```
   aws s3api get-object --bucket examplebucket --key HappyFace.jpg SaveFileAs.jpg --profile TempCred
   ```

   Because the access policy attached to `examplerole` allows the actions, Amazon S3 processes the request\. You can try any other action on any other object in the bucket\.

   If you try any other action—for example, `get-object-acl`—you will get permission denied because the role is not allowed that action\.

   ```
   aws s3api get-object-acl --bucket examplebucket --key HappyFace.jpg --profile TempCred
   ```

   We used user Dave to assume the role and access the object using temporary credentials\. It could also be an application in Account C that accesses objects in `examplebucket`\. The application can obtain temporary security credentials, and Account C can delegate the application permission to assume `examplerole`\.

## Step 4: Clean Up<a name="access-policies-walkthrough-example4-step6"></a>

1. After you are done testing, you can do the following to clean up\.

   1. Sign in to the AWS Management Console \([AWS Management Console](https://console.aws.amazon.com/)\) using account A credentials, and do the following:
     + In the Amazon S3 console, remove the bucket policy attached to *examplebucket*\. In the bucket **Properties**, delete the policy in the **Permissions** section\. 
     + If the bucket is created for this exercise, in the Amazon S3 console, delete the objects and then delete the bucket\. 
     + In the IAM console, remove the `examplerole` you created in Account A\. 
     + In the IAM console, remove the AccountAadmin user\.

1. Sign in to the AWS Management Console \([AWS Management Console](https://console.aws.amazon.com/)\) using Account B credentials\. In the IAM console, delete user AccountBadmin\.

1. Sign in to the AWS Management Console \([AWS Management Console](https://console.aws.amazon.com/)\) using Account C credentials\. In the IAM console, delete user AccountCadmin and user Dave\.

## Related Resources<a name="RelatedResources-managing-access-example4"></a>
+ [Creating a Role to Delegate Permissions to an IAM User](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-user.html) in the *IAM User Guide*\.
+ [Tutorial: Delegate Access Across AWS Accounts Using IAM Roles](http://docs.aws.amazon.com/IAM/latest/UserGuide/tutorial-cross-account-with-roles.html) in the *IAM User Guide*\.
+ [Working with Policies](http://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_manage.html) in the *IAM User Guide*\.