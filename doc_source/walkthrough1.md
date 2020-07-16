# Walkthrough: Controlling access to a bucket with user policies<a name="walkthrough1"></a>

This walkthrough explains how user permissions work with Amazon S3\. In this example, you create a bucket with folders\. You then create AWS Identity and Access Management \(IAM\) users in your AWS account and grant those users incremental permissions on your Amazon S3 bucket and the folders in it\. 

**Topics**
+ [The basics of buckets and folders](#walkthrough-background1)
+ [Walkthrough summary](#walkthrough-scenario)
+ [Preparing for the walkthrough](#walkthrough-what-you-need)
+ [Step 1: Create a bucket](#walkthrough1-create-bucket)
+ [Step 2: Create IAM users and a group](#walkthrough1-add-users)
+ [Step 3: Verify that IAM users have no permissions](#walkthrough1-verify-no-user-permissions)
+ [Step 4: Grant group\-level permissions](#walkthrough-group-policy)
+ [Step 5: Grant IAM user alice specific permissions](#walkthrough-grant-user1-permissions)
+ [Step 6: Grant IAM user bob specific permissions](#walkthrough1-grant-permissions-step5)
+ [Step 7: Secure the private folder](#walkthrough-secure-private-folder-explicit-deny)
+ [Step 8: Clean up](#walkthrough-cleanup)
+ [Related resources](#RelatedResources-walkthrough1)

## The basics of buckets and folders<a name="walkthrough-background1"></a>

The Amazon S3 data model is a flat structure: You create a bucket, and the bucket stores objects\. There is no hierarchy of subbuckets or subfolders, but you can emulate a folder hierarchy\. Tools like the Amazon S3 console can present a view of these logical folders and subfolders in your bucket, as shown in the following image\.

![\[Console screenshot showing a hierarchy of buckets, folders, and objects.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/walkthrough-10.png)

The console shows that a bucket named `companybucket` has three folders, `Private`, `Development`, and `Finance`, and an object, `s3-dg.pdf`\. The console uses the object names \(keys\) to create a logical hierarchy with folders and subfolders\. Consider the following examples:
+ When you create the `Development` folder, the console creates an object with the key `Development/`\. Note the trailing slash \(`/`\) delimiter\.
+ When you upload an object named `Projects1.xls` in the `Development` folder, the console uploads the object and gives it the key `Development/Projects1.xls`\. 

  In the key, `Development` is the [prefix](https://docs.aws.amazon.com/general/latest/gr/glos-chap.html#keyprefix) and `/` is the delimiter\. The Amazon S3 API supports prefixes and delimiters in its operations\. For example, you can get a list of all objects from a bucket with a specific prefix and delimiter\. On the console, when you open the `Development` folder, the console lists the objects in that folder\. In the following example, the `Development` folder contains one object\. 

     
![\[Console screenshot showing a hierarchy of buckets, folders, and objects.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/walkthrough-50.png)

   

  When the console lists the `Development` folder in the `companybucket` bucket, it sends a request to Amazon S3 in which it specifies a prefix of `Development` and a delimiter of `/` in the request\. The console's response looks just like a folder list in your computer's file system\. The preceding example shows that the bucket `companybucket` has an object with the key `Development/Projects1.xls`\.

The console is using object keys to infer a logical hierarchy\. Amazon S3 has no physical hierarchy; it only has buckets that contain objects in a flat file structure\. When you create objects using the Amazon S3 API, you can use object keys that imply a logical hierarchy\. When you create a logical hierarchy of objects, you can manage access to individual folders, as this walkthrough demonstrates\.

Before you start, be sure that you are familiar with the concept of the *root\-level* bucket content\. Suppose that your `companybucket` bucket has the following objects:
+ `Private/privDoc1.txt`
+ `Private/privDoc2.zip`
+ `Development/project1.xls`
+ `Development/project2.xls`
+ `Finance/Tax2011/document1.pdf`
+ `Finance/Tax2011/document2.pdf`
+ `s3-dg.pdf`

These object keys create a logical hierarchy with `Private`, `Development`, and the `Finance` as root\-level folders and `s3-dg.pdf` as a root\-level object\. When you choose the bucket name on the Amazon S3 console, the root\-level items appear as shown in the following image\. The console shows the top\-level prefixes \(`Private/`, `Development/`, and `Finance/`\) as root\-level folders\. The object key `s3-dg.pdf` has no prefix, and so it appears as a root\-level item\.

![\[Console screenshot of the objects tab with the s3-dg.pdf object in the list.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/walkthrough-10.png)

## Walkthrough summary<a name="walkthrough-scenario"></a>

In this walkthrough, you create a bucket with three folders \(`Private`, `Development`, and `Finance`\) in it\. 

You have two users, Alice and Bob\. You want Alice to access only the `Development` folder, and you want Bob to access only the `Finance` folder\. You want to keep the `Private` folder content private\. In the walkthrough, you manage access by creating IAM users \(the example uses the user names Alice and Bob\) and granting them the necessary permissions\. 

IAM also supports creating user groups and granting group\-level permissions that apply to all users in the group\. This helps you better manage permissions\. For this exercise, both Alice and Bob need some common permissions\. So you also create a group named `Consultants` and then add both Alice and Bob to the group\. You first grant permissions by attaching a group policy to the group\. Then you add user\-specific permissions by attaching policies to specific users\.

**Note**  
The walkthrough uses `companybucket` as the bucket name, Alice and Bob as the IAM users, and `Consultants` as the group name\. Because Amazon S3 requires that bucket names be globally unique, you must replace the bucket name with a name that you create\.

## Preparing for the walkthrough<a name="walkthrough-what-you-need"></a>

 In this example, you use your AWS account credentials to create IAM users\. Initially, these users have no permissions\. You incrementally grant these users permissions to perform specific Amazon S3 actions\. To test these permissions, you sign in to the console with each user's credentials\. As you incrementally grant permissions as an AWS account owner and test permissions as an IAM user, you need to sign in and out, each time using different credentials\. You can do this testing with one browser, but the process will go faster if you can use two different browsers\. Use one browser to connect to the AWS Management Console with your AWS account credentials and another to connect with the IAM user credentials\. 

 To sign in to the AWS Management Console with your AWS account credentials, go to [https://console\.aws\.amazon\.com/](https://console.aws.amazon.com/)\.  An IAM user cannot sign in using the same link\. An IAM user must use an IAM\-enabled sign\-in page\. As the account owner, you can provide this link to your users\. 

For more information about IAM, see [The AWS Management Console Sign\-in Page](https://docs.aws.amazon.com/IAM/latest/UserGuide/console.html) in the *IAM User Guide*\.

### To provide a sign\-in link for IAM users<a name="walkthrough-sign-in-user-credentials"></a>

1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.

1. In the **Navigation** pane, choose **IAM Dashboard **\.

1. Note the URL under **IAM users sign in link:**\. You will give this link to IAM users to sign in to the console with their IAM user name and password\.

## Step 1: Create a bucket<a name="walkthrough1-create-bucket"></a>

In this step, you sign in to the Amazon S3 console with your AWS account credentials, create a bucket, add folders \(`Development`, `Finance`, and `Private`\) to the bucket, and upload one or two sample documents in each folder\. 

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. Create a bucket\. 

   For step\-by\-step instructions, see [How Do I Create an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-bucket.html) in the *Amazon Simple Storage Service Console User Guide*\.

1. Upload one document to the bucket\.

   This exercise assumes that you have the `s3-dg.pdf` document at the root level of this bucket\. If you upload a different document, substitute its file name for `s3-dg.pdf`\.

1. Add three folders named `Private`, `Finance`, and `Development` to the bucket\.

   For step\-by\-step instructions to create a folder, see [Creating a Folder](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-folder.html) in the *Amazon Simple Storage Service Console User Guide*\.

1. Upload one or two documents to each folder\. 

   For this exercise, assume that you have uploaded a couple of documents in each folder, resulting in the bucket having objects with the following keys:
   + `Private/privDoc1.txt`
   + `Private/privDoc2.zip`
   + `Development/project1.xls`
   + `Development/project2.xls`
   + `Finance/Tax2011/document1.pdf`
   + `Finance/Tax2011/document2.pdf`
   + `s3-dg.pdf`

   For step\-by\-step instructions, see [How Do I Upload Files and Folders to an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/upload-objects.html) in the *Amazon Simple Storage Service Console User Guide*\. 

## Step 2: Create IAM users and a group<a name="walkthrough1-add-users"></a>

Now use the IAM console to add two IAM users, Alice and Bob, to your AWS account\. Also create an administrative group named `Consultants`, and then add both users to the group\. 

**Warning**  
When you add users and a group, do not attach any policies that grant permissions to these users\. At first, these users don't have any permissions\. In the following sections, you grant permissions incrementally\. First you must ensure that you have assigned passwords to these IAM users\. You use these user credentials to test Amazon S3 actions and verify that the permissions work as expected\.

For step\-by\-step instructions for creating a new IAM user, see [Creating an IAM User in Your AWS Account](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html) in the *IAM User Guide*\. When you create the users for this walkthrough, select **AWS Management Console access** and clear **Programmatic access**\.

For step\-by\-step instructions for creating an administrative group, see [Creating Your First IAM Admin User and Group](https://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_create-admin-group.html) in the *IAM User Guide*\.

## Step 3: Verify that IAM users have no permissions<a name="walkthrough1-verify-no-user-permissions"></a>

If you are using two browsers, you can now use the second browser to sign in to the console using one of the IAM user credentials\.

1. Using the IAM user sign\-in link \(see [To provide a sign\-in link for IAM users](#walkthrough-sign-in-user-credentials)\), sign in to the AWS Management Console using either of the IAM user credentials\.

1. Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

    Verify the following console message telling you that access is denied\.   
![\[Console screenshot showing an access denied error message.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/walkthrough-20.png)

Now, you can begin granting incremental permissions to the users\. First, you attach a group policy that grants permissions that both users must have\. 

## Step 4: Grant group\-level permissions<a name="walkthrough-group-policy"></a>

You want the users to be able to do the following:
+ List all buckets owned by the parent account\. To do so, Bob and Alice must have permission for the `s3:ListAllMyBuckets` action\.
+ List root\-level items, folders, and objects in the `companybucket` bucket\. To do so, Bob and Alice must have permission for the `s3:ListBucket` action on the `companybucket` bucket\.

First, you create a policy that grants these permissions, and then you attach it to the `Consultants` group\. 

### Step 4\.1: Grant permission to list all buckets<a name="walkthrough1-grant-permissions-step1"></a>

In this step, you create a managed policy that grants the users minimum permissions to enable them to list all buckets owned by the parent account\. Then you attach the policy to the `Consultants` group\. When you attach the managed policy to a user or a group, you grant the user or group permission to obtain a list of buckets owned by the parent AWS account\.

1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.
**Note**  
Because you are granting user permissions, sign in using your AWS account credentials, not as an IAM user\.

1. Create the managed policy\.

   1. In the navigation pane on the left, choose **Policies**, and then choose **Create Policy**\.

   1. Choose the **JSON** tab\.

   1. Copy the following access policy and paste it into the policy text field\.

      ```
      {
        "Version": "2012-10-17",
        "Statement": [
          {
            "Sid": "AllowGroupToSeeBucketListInTheConsole",
            "Action": ["s3:ListAllMyBuckets"],
            "Effect": "Allow",
            "Resource": ["arn:aws:s3:::*"]
          }
        ]
      }
      ```

      A policy is a JSON document\. In the document, a `Statement` is an array of objects, each describing a permission using a collection of name\-value pairs\. The preceding policy describes one specific permission\. The `Action` specifies the type of access\. In the policy, the `s3:ListAllMyBuckets` is a predefined Amazon S3 action\. This action covers the Amazon S3 GET Service operation, which returns list of all buckets owned by the authenticated sender\. The `Effect` element value determines whether specific permission is allowed or denied\.

   1. Choose **Review Policy**\. On the next page, enter `AllowGroupToSeeBucketListInTheConsole` in the **Name** field, and then choose **Create policy**\.
**Note**  
The **Summary** entry displays a message stating that the policy does not grant any permissions\. For this walkthrough, you can safely ignore this message\.

1. Attach the `AllowGroupToSeeBucketListInTheConsole` managed policy that you created to the `Consultants` group\.

   For step\-by\-step instructions for attaching a managed policy, see [Adding and Removing IAM Identity Permissions](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_manage-attach-detach.html#attach-managed-policy-console) in the *IAM User Guide*\. 

   You attach policy documents to IAM users and groups in the IAM console\. Because you want both users to be able to list the buckets, you attach the policy to the group\. 

1. Test the permission\.

   1. Using the IAM user sign\-in link \(see [To provide a sign\-in link for IAM users](#walkthrough-sign-in-user-credentials)\), sign in to the console using any one of IAM user credentials\.

   1. Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

      The console should now list all the buckets but not the objects in any of the buckets\.  
![\[Console screenshot showing a list of buckets.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/walkthrough-30.png)

### Step 4\.2: Enable users to list root\-level content of a bucket<a name="walkthrough1-grant-permissions-step2"></a>

Next, you allow all users in the `Consultants` group to list the root\-level `companybucket` bucket items\. When a user chooses the company bucket on the Amazon S3 console, the user can see the root\-level items in the bucket\.

![\[Console screenshot showing the contents of companybucket.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/walkthrough-10.png)

**Note**  
This example uses `companybucket` for illustration\. You must use the name of the bucket that you created\.

To understand the request that the console sends to Amazon S3 when you choose a bucket name, the response that Amazon S3 returns, and how the console interprets the response, it is necessary to examine it a little more closely\.

When you choose a bucket name, the console sends the [GET Bucket \(List Objects\)](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html) request to Amazon S3\. This request includes the following parameters:
+ The `prefix` parameter with an empty string as its value\. 
+ The `delimiter` parameter with `/` as its value\. 

The following is an example request\.

```
GET ?prefix=&delimiter=/ HTTP/1.1 
Host: companybucket.s3.amazonaws.com
Date: Wed, 01 Aug  2012 12:00:00 GMT
Authorization: AWS AKIAIOSFODNN7EXAMPLE:xQE0diMbLRepdf3YB+FIEXAMPLE=
```

Amazon S3 returns a response that includes the following `<ListBucketResult/>` element\.

```
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>companybucket</Name>
  <Prefix></Prefix>
  <Delimiter>/</Delimiter>
   ...
  <Contents>
    <Key>s3-dg.pdf</Key>
    ...
  </Contents>
  <CommonPrefixes>
    <Prefix>Development/</Prefix>
  </CommonPrefixes>
  <CommonPrefixes>
    <Prefix>Finance/</Prefix>
  </CommonPrefixes>
  <CommonPrefixes>
    <Prefix>Private/</Prefix>
  </CommonPrefixes>
</ListBucketResult>
```

The key `s3-dg.pdf` object does not contain the slash \(`/`\) delimiter, and Amazon S3 returns the key in the `<Contents>` element\. However, all other keys in the example bucket contain the `/` delimiter\. Amazon S3 groups these keys and returns a `<CommonPrefixes>` element for each of the distinct prefix values `Development/`, `Finance/`, and `Private/` that is a substring from the beginning of these keys to the first occurrence of the specified `/` delimiter\. 

The console interprets this result and displays the root\-level items as three folders and one object key\. 

![\[Console screenshot showing the contents of companybucket with three folders and one pdf file.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/walkthrough-10.png)

If Bob or Alice opens the **Development** folder, the console sends the [GET Bucket \(List Objects\)](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html) request to Amazon S3 with the `prefix` and the `delimiter` parameters set to the following values:
+ The `prefix` parameter with the value `Development/`\.
+ The `delimiter` parameter with the "`/`" value\. 

In response, Amazon S3 returns the object keys that start with the specified prefix\. 

```
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Name>companybucket</Name>
  <Prefix>Development</Prefix>
  <Delimiter>/</Delimiter>
   ...
  <Contents>
    <Key>Project1.xls</Key>
    ...
  </Contents>
  <Contents>
    <Key>Project2.xls</Key>
    ...
  </Contents> 
</ListBucketResult>
```

The console shows the object keys\.

![\[Console screenshot showing the development folder containing two xls files.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/walkthrough-60.png)

Now, return to granting users permission to list the root\-level bucket items\. To list bucket content, users need permission to call the `s3:ListBucket` action, as shown in the following policy statement\. To ensure that they see only the root\-level content, you add a condition that users must specify an empty `prefix` in the request—that is, they are not allowed to double\-click any of the root\-level folders\. Finally, you add a condition to require folder\-style access by requiring user requests to include the `delimiter` parameter with the value "`/`"\. 

```
{
  "Sid": "AllowRootLevelListingOfCompanyBucket",
  "Action": ["s3:ListBucket"],
  "Effect": "Allow",
  "Resource": ["arn:aws:s3:::companybucket"],
  "Condition":{ 
         "StringEquals":{
             "s3:prefix":[""], "s3:delimiter":["/"]
                        }
              }
}
```

When you choose a bucket on the Amazon S3 console, the console first sends the [GET Bucket location](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlocation.html) request to find the AWS Region where the bucket is deployed\. Then the console uses the Region\-specific endpoint for the bucket to send the [GET Bucket \(List Objects\)](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html) request\. As a result, if users are going to use the console, you must grant permission for the `s3:GetBucketLocation` action as shown in the following policy statement\.

```
{
   "Sid": "RequiredByS3Console",
   "Action": ["s3:GetBucketLocation"],
   "Effect": "Allow",
   "Resource": ["arn:aws:s3:::*"]
}
```

**To enable users to list root\-level bucket content**

1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.

   Use your AWS account credentials, not the credentials of an IAM user, to sign in to the console\.

1. Replace the existing `AllowGroupToSeeBucketListInTheConsole` managed policy that is attached to the `Consultants` group with the following policy, which also allows the `s3:ListBucket` action\. Remember to replace *companybucket* in the policy `Resource` with the name of your bucket\. 

   For step\-by\-step instructions, see [Editing IAM Policies](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_manage-edit.html) in the *IAM User Guide*\. When following the step\-by\-step instructions, be sure to follow the steps for applying your changes to all principal entities that the policy is attached to\. 

   ```
   {
     "Version": "2012-10-17",                 
     "Statement": [
        {
          "Sid": "AllowGroupToSeeBucketListAndAlsoAllowGetBucketLocationRequiredForListBucket",
          "Action": [ "s3:ListAllMyBuckets", "s3:GetBucketLocation" ],
          "Effect": "Allow",
          "Resource": [ "arn:aws:s3:::*"  ]
        },
        {
          "Sid": "AllowRootLevelListingOfCompanyBucket",
          "Action": ["s3:ListBucket"],
          "Effect": "Allow",
          "Resource": ["arn:aws:s3:::companybucket"],
          "Condition":{ 
                "StringEquals":{
                       "s3:prefix":[""], "s3:delimiter":["/"]
                              }
                      }
        }
     ] 
   }
   ```

1. Test the updated permissions\.

   1. Using the IAM user sign\-in link \(see [To provide a sign\-in link for IAM users](#walkthrough-sign-in-user-credentials)\), sign in to the AWS Management Console\. 

      Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

   1. Choose the bucket that you created, and the console shows the root\-level bucket items\. If you choose any folders in the bucket, you won't be able to see the folder content because you haven't yet granted those permissions\.  
![\[Console screenshot showing the company bucket containing three folders.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/walkthrough-10.png)

This test succeeds when users use the Amazon S3 console\. When you choose a bucket on the console, the console implementation sends a request that includes the `prefix` parameter with an empty string as its value and the `delimiter` parameter with "`/`" as its value\.

### Step 4\.3: Summary of the group policy<a name="walkthrough-group-policy-summary"></a>

The net effect of the group policy that you added is to grant the IAM users Alice and Bob the following minimum permissions:
+ List all buckets owned by the parent account\.
+ See root\-level items in the `companybucket` bucket\. 

However, the users still can't do much\. Next, you grant user\-specific permissions, as follows:
+ Allow Alice to get and put objects in the `Development` folder\.
+ Allow Bob to get and put objects in the `Finance` folder\.

For user\-specific permissions, you attach a policy to the specific user, not to the group\. In the following section, you grant Alice permission to work in the `Development` folder\. You can repeat the steps to grant similar permission to Bob to work in the `Finance` folder\.

## Step 5: Grant IAM user alice specific permissions<a name="walkthrough-grant-user1-permissions"></a>

Now you grant additional permissions to Alice so that she can see the content of the `Development` folder and get and put objects in that folder\.

### Step 5\.1: Grant IAM user alice permission to list the development folder content<a name="walkthrough-grant-user1-permissions-listbucket"></a>

For Alice to list the `Development` folder content, you must apply a policy to the Alice user that grants permission for the `s3:ListBucket` action on the `companybucket` bucket, provided the request includes the prefix `Development/`\. You want this policy to be applied only to the user Alice, so you use an inline policy\. For more information about inline policies, see [Managed Policies and Inline Policies](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_managed-vs-inline.html) in the *IAM User Guide*\.

1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.

   Use your AWS account credentials, not the credentials of an IAM user, to sign in to the console\.

1. Create an inline policy to grant the user Alice permission to list the `Development` folder content\.

   1. In the navigation pane on the left, choose **Users**\.

   1. Choose the user name **Alice**\.

   1. On the user details page, choose the **Permissions** tab and then choose **Add inline policy**\.

   1. Choose the **JSON** tab\.

   1. Copy the following policy and paste it into the policy text field\.

      ```
      {
          "Version": "2012-10-17",  
          "Statement": [
          {
            "Sid": "AllowListBucketIfSpecificPrefixIsIncludedInRequest",
            "Action": ["s3:ListBucket"],
            "Effect": "Allow",
            "Resource": ["arn:aws:s3:::companybucket"],
            "Condition":{  "StringLike":{"s3:prefix":["Development/*"] }
             }
          }
        ]
      }
      ```

   1. Choose **Review Policy**\. On the next page, enter a name in the **Name** field, and then choose **Create policy**\.

1. Test the change to Alice's permissions:

   1. Using the IAM user sign\-in link \(see [To provide a sign\-in link for IAM users](#walkthrough-sign-in-user-credentials)\), sign in to the AWS Management Console\. 

   1. Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

   1. On the Amazon S3 console, verify that Alice can see the list of objects in the `Development/` folder in the bucket\. 

      When the user chooses the `/Development` folder to see the list of objects in it, the Amazon S3 console sends the `ListObjects` request to Amazon S3 with the prefix `/Development`\. Because the user is granted permission to see the object list with the prefix `Development` and delimiter `/`, Amazon S3 returns the list of objects with the key prefix `Development/`, and the console displays the list\.  
![\[Console screenshot showing the development folder containing two xls files.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/walkthrough-60.png)

### Step 5\.2: Grant IAM user alice permissions to get and put objects in the development folder<a name="walkthrough-grant-user1-permissions-get-put-object"></a>

For Alice to get and put objects in the `Development` folder, she needs permission to call the `s3:GetObject` and `s3:PutObject` actions\. The following policy statements grant these permissions, provided that the request includes the `prefix` parameter with a value of `Development/`\.

```
{
    "Sid":"AllowUserToReadWriteObjectData",
    "Action":["s3:GetObject", "s3:PutObject"],
    "Effect":"Allow",
    "Resource":["arn:aws:s3:::companybucket/Development/*"]
 }
```

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

   Use your AWS account credentials, not the credentials of an IAM user, to sign in to the console\.

1. Edit the inline policy that you created in the previous step\. 

   1. In the navigation pane on the left, choose **Users**\.

   1. Choose the user name Alice\.

   1. On the user details page, choose the **Permissions** tab and expand the **Inline Policies** section\.

   1. Next to the name of the policy that you created in the previous step, choose **Edit Policy**\.

   1. Copy the following policy and paste it into the policy text field, replacing the existing policy\.

      ```
      {
           "Version": "2012-10-17",
           "Statement":[
            {
               "Sid":"AllowListBucketIfSpecificPrefixIsIncludedInRequest",
               "Action":["s3:ListBucket"],
               "Effect":"Allow",
               "Resource":["arn:aws:s3:::companybucket"],
               "Condition":{
                  "StringLike":{"s3:prefix":["Development/*"]
                  }
               }
            },
            {
              "Sid":"AllowUserToReadWriteObjectDataInDevelopmentFolder", 
              "Action":["s3:GetObject", "s3:PutObject"],
              "Effect":"Allow",
              "Resource":["arn:aws:s3:::companybucket/Development/*"]
            }
         ]
      }
      ```

1. Test the updated policy:

   1. Using the IAM user sign\-in link \(see [To provide a sign\-in link for IAM users](#walkthrough-sign-in-user-credentials)\), sign into the AWS Management Console\. 

   1. Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

   1. On the Amazon S3 console, verify that Alice can now add an object and download an object in the `Development` folder\. 

### Step 5\.3: Explicitly deny IAM user alice permissions to any other folders in the bucket<a name="walkthrough-grant-user1-explicit-deny-other-access"></a>

User Alice can now list the root\-level content in the `companybucket` bucket\. She can also get and put objects in the `Development` folder\. If you really want to tighten the access permissions, you could explicitly deny Alice access to any other folders in the bucket\. If there is any other policy \(bucket policy or ACL\) that grants Alice access to any other folders in the bucket, this explicit deny overrides those permissions\. 

You can add the following statement to the user Alice policy that requires all requests that Alice sends to Amazon S3 to include the `prefix` parameter, whose value can be either `Development/*` or an empty string\. 

```
{
   "Sid": "ExplicitlyDenyAnyRequestsForAllOtherFoldersExceptDevelopment",
   "Action": ["s3:ListBucket"],
   "Effect": "Deny",
   "Resource": ["arn:aws:s3:::companybucket"],
   "Condition":{  "StringNotLike": {"s3:prefix":["Development/*",""] },
                  "Null"         : {"s3:prefix":false }
    }
}
```

There are two conditional expressions in the `Condition` block\. The result of these conditional expressions is combined by using the logical `AND`\. If both conditions are true, the result of the combined condition is true\. Because the `Effect` in this policy is `Deny`, when the `Condition` evaluates to true, users can't perform the specified `Action`\.
+ The `Null` conditional expression ensures that requests from Alice include the `prefix` parameter\. 

  The `prefix` parameter requires folder\-like access\. If you send a request without the `prefix` parameter, Amazon S3 returns all the object keys\. 

  If the request includes the `prefix` parameter with a null value, the expression evaluates to true, and so the entire `Condition` evaluates to true\. You must allow an empty string as value of the `prefix` parameter\. From the preceding discussion, recall that allowing the null string allows Alice to retrieve root\-level bucket items as the console does in the preceding discussion\. For more information, see [Step 4\.2: Enable users to list root\-level content of a bucket](#walkthrough1-grant-permissions-step2)\. 
+ The `StringNotLike` conditional expression ensures that if the value of the `prefix` parameter is specified and is not `Development/*`, the request fails\. 

Follow the steps in the preceding section and again update the inline policy that you created for user Alice\.

Copy the following policy and paste it into the policy text field, replacing the existing policy\.

```
{
   "Version": "2012-10-17",
   "Statement":[
      {
         "Sid":"AllowListBucketIfSpecificPrefixIsIncludedInRequest",
         "Action":["s3:ListBucket"],
         "Effect":"Allow",
         "Resource":["arn:aws:s3:::companybucket"],
         "Condition":{
            "StringLike":{"s3:prefix":["Development/*"]
            }
         }
      },
      {
        "Sid":"AllowUserToReadWriteObjectDataInDevelopmentFolder", 
        "Action":["s3:GetObject", "s3:PutObject"],
        "Effect":"Allow",
        "Resource":["arn:aws:s3:::companybucket/Development/*"]
      },
      {
         "Sid": "ExplicitlyDenyAnyRequestsForAllOtherFoldersExceptDevelopment",
         "Action": ["s3:ListBucket"],
         "Effect": "Deny",
         "Resource": ["arn:aws:s3:::companybucket"],
         "Condition":{  "StringNotLike": {"s3:prefix":["Development/*",""] },
                        "Null"         : {"s3:prefix":false }
          }
      }
   ]
}
```

## Step 6: Grant IAM user bob specific permissions<a name="walkthrough1-grant-permissions-step5"></a>

Now you want to grant Bob permission to the `Finance` folder\. Follow the steps that you used earlier to grant permissions to Alice, but replace the `Development` folder with the `Finance` folder\. For step\-by\-step instructions, see [Step 5: Grant IAM user alice specific permissions](#walkthrough-grant-user1-permissions)\. 

## Step 7: Secure the private folder<a name="walkthrough-secure-private-folder-explicit-deny"></a>

In this example, you have only two users\. You granted all the minimum required permissions at the group level and granted user\-level permissions only when you really need to permissions at the individual user level\. This approach helps minimize the effort of managing permissions\. As the number of users increases, managing permissions can become cumbersome\. For example, you don't want any of the users in this example to access the content of the `Private` folder\. How do you ensure that you don't accidentally grant a user permission to it? You add a policy that explicitly denies access to the folder\. An explicit deny overrides any other permissions\. 

To ensure that the `Private` folder remains private, you can add the following two deny statements to the group policy:
+ Add the following statement to explicitly deny any action on resources in the `Private` folder \(`companybucket/Private/*`\)\.

  ```
  {
    "Sid": "ExplictDenyAccessToPrivateFolderToEveryoneInTheGroup",
    "Action": ["s3:*"],
    "Effect": "Deny",
    "Resource":["arn:aws:s3:::companybucket/Private/*"]
  }
  ```
+ You also deny permission for the list objects action when the request specifies the `Private/` prefix\. On the console, if Bob or Alice opens the `Private` folder, this policy causes Amazon S3 to return an error response\.

  ```
  {
    "Sid": "DenyListBucketOnPrivateFolder",
    "Action": ["s3:ListBucket"],
    "Effect": "Deny",
    "Resource": ["arn:aws:s3:::*"],
    "Condition":{
        "StringLike":{"s3:prefix":["Private/"]}
     }
  }
  ```

Replace the `Consultants` group policy with an updated policy that includes the preceding deny statements\. After the updated policy is applied, none of the users in the group can access the `Private` folder in your bucket\. 

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

   Use your AWS account credentials, not the credentials of an IAM user, to sign in to the console\.

1. Replace the existing `AllowGroupToSeeBucketListInTheConsole` managed policy that is attached to the `Consultants` group with the following policy\. Remember to replace `companybucket` in the policy with the name of your bucket\. 

   For instructions, see [Editing Customer Managed Policies](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_manage-edit.html#edit-managed-policy-console) in the *IAM User Guide*\. When following the instructions, make sure to follow the directions for applying your changes to all principal entities that the policy is attached to\. 

   ```
   {
     "Version": "2012-10-17",
     "Statement": [
       {
         "Sid": "AllowGroupToSeeBucketListAndAlsoAllowGetBucketLocationRequiredForListBucket",
         "Action": ["s3:ListAllMyBuckets", "s3:GetBucketLocation"],
         "Effect": "Allow",
         "Resource": ["arn:aws:s3:::*"]
       },
       {
         "Sid": "AllowRootLevelListingOfCompanyBucket",
         "Action": ["s3:ListBucket"],
         "Effect": "Allow",
         "Resource": ["arn:aws:s3:::companybucket"],
         "Condition":{
             "StringEquals":{"s3:prefix":[""]}
          }
       },
       {
         "Sid": "RequireFolderStyleList",
         "Action": ["s3:ListBucket"],
         "Effect": "Deny",
         "Resource": ["arn:aws:s3:::*"],
         "Condition":{
             "StringNotEquals":{"s3:delimiter":"/"}
          }
        },
       {
         "Sid": "ExplictDenyAccessToPrivateFolderToEveryoneInTheGroup",
         "Action": ["s3:*"],
         "Effect": "Deny",
         "Resource":["arn:aws:s3:::companybucket/Private/*"]
       },
       {
         "Sid": "DenyListBucketOnPrivateFolder",
         "Action": ["s3:ListBucket"],
         "Effect": "Deny",
         "Resource": ["arn:aws:s3:::*"],
         "Condition":{
             "StringLike":{"s3:prefix":["Private/"]}
          }
       }
     ]
   }
   ```

## Step 8: Clean up<a name="walkthrough-cleanup"></a>

To clean up, open the IAM console and remove the users Alice and Bob\. For step\-by\-step instructions, see [Deleting an IAM User](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_manage.html#id_users_deleting) in the *IAM User Guide*\.

To ensure that you aren't charged further for storage, you should also delete the objects and the bucket that you created for this exercise\.

## Related resources<a name="RelatedResources-walkthrough1"></a>
+ [Managing IAM Policies](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_manage.html) in the *IAM User Guide*\.