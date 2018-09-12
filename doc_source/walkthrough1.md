# An Example Walkthrough: Using user policies to control access to your bucket<a name="walkthrough1"></a>

This walkthrough explains how user permissions work with Amazon S3\. We will create a bucket with folders, and then we'll create AWS Identity and Access Management users in your AWS account and grant those users incremental permissions on your Amazon S3 bucket and the folders in it\. 

**Topics**
+ [Background: Basics of Buckets and Folders](#walkthrough-background1)
+ [Walkthrough Example](#walkthrough-scenario)
+ [Step 0: Preparing for the Walkthrough](#walkthrough-what-you-need)
+ [Step 1: Create a Bucket](#walkthrough1-create-bucket)
+ [Step 2: Create IAM Users and a Group](#walkthrough1-add-users)
+ [Step 3: Verify that IAM Users Have No Permissions](#walkthrough1-verify-no-user-permissions)
+ [Step 4: Grant Group\-Level Permissions](#walkthrough-group-policy)
+ [Step 5: Grant IAM User Alice Specific Permissions](#walkthrough-grant-user1-permissions)
+ [Step 6: Grant IAM User Bob Specific Permissions](#walkthrough1-grant-permissions-step5)
+ [Step 7: Secure the Private Folder](#walkthrough-secure-private-folder-explicit-deny)
+ [Cleanup](#walkthrough-cleanup)
+ [Related Resources](#RelatedResources-walkthrough1)

## Background: Basics of Buckets and Folders<a name="walkthrough-background1"></a>

The Amazon S3 data model is a flat structure: you create a bucket, and the bucket stores objects\. There is no hierarchy of subbuckets or subfolders; however, you can emulate a folder hierarchy\. Tools such as the Amazon S3 Console can present a view of these logical folders and subfolders in your bucket, as shown here:

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/walkthrough-10.png)

The console shows that a bucket named **companybucket** has three folders, **Private**, **Development**, and **Finance**, and an object, **s3\-dg\.pdf**\. The console uses the object names \(keys\) to create a logical hierarchy with folders and subfolders\. Consider the following examples:
+ When you create the **Development** folder, the console creates an object with the key `Development/`\. Note the trailing '/' delimiter\.
+ When you upload an object named **Projects1\.xls** in the **Development** folder, the console uploads the object and gives it the key **Development/Projects1\.xls**\. 

  In the key, `Development` is the prefix and `'/'` is the delimiter\. The Amazon S3 API supports prefixes and delimiters in its operations\. For example, you can get a list of all objects from a bucket with a specific prefix and delimiter\. In the console, when you double\-click the **Development** folder, the console lists the objects in that folder\. In the following example, the **Development** folder contains one object\.   
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/walkthrough-50.png)

   When the console lists the **Development** folder in the `companybucket` bucket, it sends a request to Amazon S3 in which it specifies a prefix of `Development` and a delimiter of `'/'` in the request\. The console's response looks just like a folder list in your computer's file system\. The preceding example shows that the bucket `companybucket` has an object with the key `Development/Projects1.xls`\.

The console is using object keys to infer a logical hierarchy; Amazon S3 has no physical hierarchy, only buckets that contain objects in a flat file structure\. When you create objects by using the Amazon S3 API, you can use object keys that imply a logical hierarchy\. 

When you create a logical hierarchy of objects, you can manage access to individual folders, as we will do in this walkthrough\. 

Before going into the walkthrough, you need to familiarize yourself with one more concept, the "root\-level" bucket content\. Suppose your `companybucket` bucket has the following objects:

Private/privDoc1\.txt

Private/privDoc2\.zip

Development/project1\.xls

Development/project2\.xls

Finance/Tax2011/document1\.pdf

Finance/Tax2011/document2\.pdf

s3\-dg\.pdf

These object keys create a logical hierarchy with `Private`, `Development` and the `Finance` as root\-level folders and `s3-dg.pdf` as a root\-level object\. When you click the bucket name in the Amazon S3 console, the root\-level items appear as shown\. The console shows the top\-level prefixes \(Private/, Development/ and Finance/\) as root\-level folders\. The object key s3\-dg\.pdf has no prefix, and so it appears as a root\-level item\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/walkthrough-10.png)

## Walkthrough Example<a name="walkthrough-scenario"></a>

The example for this walkthrough is as follows:
+ You create a bucket and then add three folders \(Private, Development, and Finance\) to it\. 
+ You have two users, Alice and Bob\. You want Alice to access only the Development folder and Bob to access only the Finance folder, and you want to keep the Private folder content private\. In the walkthrough, you manage access by creating AWS Identity and Access Management \(IAM\) users \(we will use the same user names, Alice and Bob\) and grant them the necessary permissions\. 

  IAM also supports creating user groups and granting group\-level permissions that apply to all users in the group\. This helps you better manage permissions\. For this exercise, both Alice and Bob will need some common permissions\. So you will also create a group named Consultants and then add both Alice and Bob to the group\. You will first grant permissions by attaching a group policy to the group\. Then you will add user\-specific permissions by attaching policies to specific users\.

**Note**  
The walkthrough uses `companybucket` as the bucket name, Alice and Bob as the IAM users, and Consultants as the group name\. Because Amazon S3 requires that bucket names be globally unique, you will need to replace the bucket name with a name that you create\.

## Step 0: Preparing for the Walkthrough<a name="walkthrough-what-you-need"></a>

 In this example, you will use your AWS account credentials to create IAM users\. Initially, these users have no permissions\. You will incrementally grant these users permissions to perform specific Amazon S3 actions\. To test these permissions, you will sign in to the console with each user's credentials\. As you incrementally grant permissions as an AWS account owner and test permissions as an IAM user, you need to sign in and out, each time using different credentials\. You can do this testing with one browser, but the process will go faster if you can use two different browsers: use one browser to connect to the AWS Management Console with your AWS account credentials and another to connect with the IAM user credentials\. 

 To sign into the AWS Management Console with your AWS account credentials, go to [https://console\.aws\.amazon\.com/](https://console.aws.amazon.com/)\.  An IAM user cannot sign in by using the same link\. An IAM user must use an IAM\-enabled sign\-in page\. As the account owner, you can provide this link to your users\. 

For more information about IAM, go to [The AWS Management Console Sign\-in Page](http://docs.aws.amazon.com/IAM/latest/UserGuide/console.html) in the *IAM User Guide*\.

### To Provide a Sign\-in Link for IAM Users<a name="walkthrough-sign-in-user-credentials"></a>

1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.

1. In the **Navigation** pane, click **IAM Dashboard **\.

1. Note the URL under **IAM users sign in link:**\. You will give this link to IAM users to sign in to the console with their IAM user name and password\.

## Step 1: Create a Bucket<a name="walkthrough1-create-bucket"></a>

In this step, you will sign in to the Amazon S3 console with your AWS account credentials, create a bucket, add folders \(Development, Finance, Private\) to the bucket, and upload one or two sample documents in each folder\. 

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. Create a bucket\. 

   For step\-by\-step instructions, see [How Do I Create an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-bucket.html) in the *Amazon Simple Storage Service Console User Guide*\.

1. Upload one document to the bucket\.

   This exercise assumes you have the `s3-dg.pdf` document at the root level of this bucket\. If you upload a different document, substitute its file name for `s3-dg.pdf`\.

1. Add three folders named Private, Finance, and Development to the bucket\.

1. Upload one or two documents to each folder\. 

   For this exercise, assume you have uploaded a couple of documents in each folder, resulting in the bucket having objects with the following keys:

   Private/privDoc1\.txt

   Private/privDoc2\.zip

   Development/project1\.xls

   Development/project2\.xls

   Finance/Tax2011/document1\.pdf

   Finance/Tax2011/document2\.pdf

   s3\-dg\.pdf

   For step\-by\-step instructions, see [How Do I Upload Files and Folders to an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/upload-objects.html) in the *Amazon Simple Storage Service Console User Guide*\. 

## Step 2: Create IAM Users and a Group<a name="walkthrough1-add-users"></a>

Now use the IAM console to add two IAM users, Alice and Bob, to your AWS account\. Also create an administrative group named Consultants, and then add both users to the group\. 

**Warning**  
When you add users and a group, do not attach any policies that grant permissions to these users\. At first, these users will not have any permissions\. In the following sections, you will incrementally grant permissions\. You must first ensure that you have assigned passwords to these IAM users\. You will use these user credentials to test Amazon S3 actions and verify that the permissions work as expected\.

For step\-by\-step instructions on creating a new IAM user, see [Creating an IAM User in Your AWS Account](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html) in the *IAM User Guide*\. When you create the users for this walkthrough, check "AWS Management Console access" and leave "Programmatic access" unchecked\.

For step\-by\-step instructions on creating an administrative group, see [Creating Your First IAM User and Administrators Group](http://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_create-admin-group.html) section in the *IAM User Guide*\.

## Step 3: Verify that IAM Users Have No Permissions<a name="walkthrough1-verify-no-user-permissions"></a>

If you are using two browsers, you can now use the second browser to sign into the console using one of the IAM user credentials\.

1. Using the IAM user sign\-in link \(see [To Provide a Sign\-in Link for IAM Users](#walkthrough-sign-in-user-credentials)\), sign into the AWS console using either of the IAM user credentials\.

1. Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

    Verify the following console message telling you that access is denied\.   
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/walkthrough-20.png)

Now, let's begin granting incremental permissions to the users\. First, you will attach a group policy that grants permissions that both users must have\. 

## Step 4: Grant Group\-Level Permissions<a name="walkthrough-group-policy"></a>

We want all our users to be able to do the following:
+ List all buckets owned by the parent account\. To do so, Bob and Alice must have permission for the `s3:ListAllMyBuckets` action\.
+ List root\-level items, folders, and objects, in the `companybucket` bucket\. To do so, Bob and Alice must have permission for the `s3:ListBucket` action on the `companybucket` bucket\.

Now we'll create a policy that grants these permissions and then we'll attach it to the Consultants group\. 

### Step 4\.1: Grant Permission to List All Buckets<a name="walkthrough1-grant-permissions-step1"></a>

In this step you'll create a managed policy that grants the users minimum permissions to enable them to list all buckets owned by the parent account and then you'll attach the policy to the Consultants group\. When you attach the managed policy to a user or a group, you allow the user or group permission to obtain a list of buckets owned by the parent AWS account\.

1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.
**Note**  
Since you'll be granting user permissions, sign in with your AWS account credentials, not as an IAM user\.

1. Create the managed policy\.

   1. In the navigation pane on the left, click **Policies** and then click **Create Policy**\.

   1. Click on the **JSON** tab\.

   1. Copy the following access policy and paste it into the  policy text field: 

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

      A policy is a JSON document\. In the document, a `Statement` is an array of objects, each describing a permission using a collection of name\-value pairs\. The preceding policy describes one specific permission\. The `Action` specifies the type of access\. In the policy, the `s3:ListAllMyBuckets` is a predefined Amazon S3 action\. This action covers the Amazon S3 GET Service operation, which returns list of all buckets owned by the authenticated sender\. The `Effect` element value determine if specific permission is allowed or denied\.

   1. Click **Review Policy**\. On the next page, enter `AllowGroupToSeeBucketListInTheConsole` in the **Name** field, and then click **Create policy**\.
**Note**  
The **Summary** entry will display a message stating that the policy does not grant any permissions\. For this walkthrough, you can safely ignore this message\.

1. Attach the `AllowGroupToSeeBucketListInTheConsole` managed policy that you created to the Consultants group\.

   For step\-by\-step instructions for attaching a managed policy, see [Adding and Removing IAM Policies \(Console\)](http://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_manage-attach-detach.html#attach-managed-policy-console) in the *IAM User Guide*\. 

   You attach policy documents to IAM users and groups in the IAM console\. Because we want both our users to be able to list the buckets, we attach the policy to the group\. 

1. Test the permission\.

   1. Using the IAM user sign\-in link \(see [To Provide a Sign\-in Link for IAM Users](#walkthrough-sign-in-user-credentials)\), sign into the AWS console using any one of IAM user credentials\.

   1. Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

      The console should now list all the buckets but not the objects in any of the buckets\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/walkthrough-30.png)

### Step 4\.2: Enable Users to List Root\-Level Content of a Bucket<a name="walkthrough1-grant-permissions-step2"></a>

Now let's allow all users in the Consultants group to list the root\-level `companybucket` bucket items\. When a user clicks the company bucket in the Amazon S3 console, he or she will be able to see the root\-level items in the bucket\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/walkthrough-10.png)

Remember, we are using `companybucket` for illustration\. You must use the name of the bucket that you created for this exercise\. 

To understand what request the console sends to Amazon S3 when you click a bucket name, the response Amazon S3 returns, and how the console interprets the response, it is necessary to take a little deep dive\.

When you click a bucket name, the console sends the [GET Bucket \(List Objects\)](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html) request to Amazon S3\. This request includes the following parameters:
+ `prefix` parameter with an empty string as its value\. 
+ `delimiter` parameter with `/` as its value\. 

The following is an example request:

```
GET ?prefix=&delimiter=/ HTTP/1.1 
Host: companybucket.s3.amazonaws.com
Date: Wed, 01 Aug  2012 12:00:00 GMT
Authorization: AWS AKIAIOSFODNN7EXAMPLE:xQE0diMbLRepdf3YB+FIEXAMPLE=
```

Amazon S3 returns a response that includes the following `<ListBucketResult/>` element:

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

The key `s3-dg.pdf `does not contain the `'/'` delimiter, and Amazon S3 returns the key in the `<Contents>` element\. However, all other keys in our example bucket contain the `'/'` delimiter\. Amazon S3 groups these keys and returns a `<CommonPrefixes>` element for each of the distinct prefix values `Development/`, `Finance/`, and `Private/` that is a substring from the beginning of these keys to the first occurrence of the specified `'/'` delimiter\. 

The console interprets this result and displays the root\-level items as three folders and one object key\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/walkthrough-10.png)

Now, if Bob or Alice double\-clicks the **Development** folder, the console sends the [GET Bucket \(List Objects\)](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html) request to Amazon S3 with the `prefix` and the `delimiter` parameters set to the following values:
+ `prefix` parameter with value `Development/`\.
+ `delimiter` parameter with` '/'` value\. 

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

The console shows the object keys: 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/walkthrough-60.png)

Now, let's return to granting users permission to list the root\-level bucket items\. To list bucket content, users need permission to call the `s3:ListBucket` action, as shown in the following policy statement\. To ensure that they see only the root\-level content, we add a condition that users must specify an empty `prefix` in the request—that is, they are not allowed to double\-click any of our root\-level folders\. Finally, we will add a condition to require folder\-style access by requiring user requests to include the `delimiter` parameter with value `'/'`\.  

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

When you use the Amazon S3 console, note that when you click a bucket, the console first sends the [GET Bucket location](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlocation.html) request to find the AWS region where the bucket is deployed\. Then the console uses the region\-specific endpoint for the bucket to send the [GET Bucket \(List Objects\)](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html) request\. As a result, if users are going to use the console, you must grant permission for the `s3:GetBucketLocation` action as shown in the following policy statement:

```
{
   "Sid": "RequiredByS3Console",
   "Action": ["s3:GetBucketLocation"],
   "Effect": "Allow",
   "Resource": ["arn:aws:s3:::*"]
}
```

**To enable users to list root\-level bucket content**

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

   Use your AWS account credentials, not the credentials of an IAM user, to sign in to the console\.

1. Replace the existing `AllowGroupToSeeBucketListInTheConsole` managed policy that is attached to the Consultants group with the following policy, which also allows the `s3:ListBucket` action\. Remember to replace `companybucket` in the policy `Resource` with the name of your bucket\. 

   For step\-by\-step instructions, see [Editing Customer Managed Policies](http://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_manage-edit.html) in the *IAM User Guide*\. When following the step\-by\-step instructions, make sure to follow the directions for applying your changes to all principal entities that the policy is attached to\. 

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

   1. Using the IAM user sign\-in link \(see [To Provide a Sign\-in Link for IAM Users](#walkthrough-sign-in-user-credentials)\), sign in to the AWS Management Console\. 

      Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

   1. Click the bucket that you created for this exercise, and the console will now show the root\-level bucket items\. If you click any folders in the bucket, you will not be able to see the folder content, because you have not yet granted those permissions\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/walkthrough-10.png)

This test succeeds when users use the Amazon S3 console because when you click a bucket in the console, the console implementation sends a request that includes the `prefix` parameter with an empty string as its value and the `delimiter` parameter with `'/'` as its value\.

### Step 4\.3: Summary of the Group Policy<a name="walkthrough-group-policy-summary"></a>

The net effect of the group policy that you added is to grant the IAM users Alice and Bob the following minimum permissions:
+ List all buckets owned by the parent account\.
+ See root\-level items in the `companybucket` bucket\. 

However, the users still cannot do much\. Let's grant user\-specific permissions, as follows:
+ Permit Alice to get and put objects in the Development folder\.
+ Permit Bob to get and put objects in the Finance folder\.

For user\-specific permissions, you attach a policy to the specific user, not to the group\. In the following section, you grant Alice permission to work in the Development folder\. You can repeat the steps to grant similar permission to Bob to work in the Finance folder\.

## Step 5: Grant IAM User Alice Specific Permissions<a name="walkthrough-grant-user1-permissions"></a>

Now we grant additional permissions to Alice so she can see the content of the Development folder and get and put objects in that folder\.

### Step 5\.1: Grant IAM User Alice Permission to List the Development Folder Content<a name="walkthrough-grant-user1-permissions-listbucket"></a>

For Alice to list the Development folder content, you must apply a policy to the Alice user that grants permission for the `s3:ListBucket` action on the `companybucket` bucket, provided the request includes the prefix `Development/`\. Because we want this policy to be applied only to the user Alice we'll use an inline policy\. For more information about inline policies, see [Managed Policies and Inline Policies](http://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_managed-vs-inline.html) in the *IAM User Guide*\.

1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.

   Use your AWS account credentials, not the credentials of an IAM user, to sign in to the console\.

1. Create an inline policy to grant the user Alice permission to list the Development folder content\.

   1. In the navigation pane on the left, click **Users**\.

   1. Click the user name Alice\.

   1. On the user details page, select the **Permissions** tab and then click **Add inline policy**\.

   1. Click the **JSON** tab\.

   1. Copy the following policy into the  policy text field:

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

   1. Click **Review Policy**\. On the next page, enter a name in the **Name** field, and then click **Create policy**\.

1. Test the change to Alice's permissions:

   1. Using the IAM user sign in link \(see [To Provide a Sign\-in Link for IAM Users](#walkthrough-sign-in-user-credentials)\), sign in to the AWS Management Console\. 

   1. Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

   1. In the Amazon S3 console, verify that Alice can see the list of objects in the `Development/` folder in the bucket\. 

      When the user clicks the `/Development` folder to see the list of objects in it, the Amazon S3 console sends the `ListObjects` request to Amazon S3 with the prefix `/Development`\. Because the user is granted permission to see the object list with the prefix `Development` and delimiter `'/'`, Amazon S3 returns the list of objects with the key prefix `Development/`, and the console displays the list\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/walkthrough-60.png)

### Step 5\.2: Grant IAM User Alice Permissions to Get and Put Objects in the Development Folder<a name="walkthrough-grant-user1-permissions-get-put-object"></a>

For Alice to get and put objects in the Development folder, she needs permission to call the `s3:GetObject` and `s3:PutObject` actions\. The following policy statements grant these permissions, provided the request includes the `prefix` parameter with a value of `Development/`\.

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

1. Edit the inline policy you created in the previous step\. 

   1. In the navigation pane on the left, click **Users**\.

   1. Click the user name Alice\.

   1. On the user details page, select the **Permissions** tab and then expand the **Inline Policies** section\.

   1. Click **Edit Policy** next to the name of the policy you created in the previous step\.

   1. Copy the following policy into the  policy text field, replacing the existing policy:

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

   1. Using the IAM user sign\-in link \(see [To Provide a Sign\-in Link for IAM Users](#walkthrough-sign-in-user-credentials)\), sign into the AWS Management Console\. 

   1. Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

   1. In the Amazon S3 console, verify that Alice can now add an object and download an object in the `Development` folder\. 

### Step 5\.3: Explicitly Deny IAM User Alice Permissions to Any Other Folders in the Bucket<a name="walkthrough-grant-user1-explicit-deny-other-access"></a>

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

Note that there are two conditional expressions in the `Condition` block\. The result of these conditional expressions is combined by using the logical AND\. If both conditions are true, the result of the combined condition is true\. Because the `Effect` in this policy is `Deny`, when the `Condition` evaluates to true, users will not be able to perform the specified `Action`\.
+ The `Null` conditional expression ensures that requests from Alice include the `prefix` parameter\. 

  The `prefix` parameter requires folder\-like access\. If you send a request without the `prefix` parameter, Amazon S3 returns all the object keys\. 

  If the request includes the `prefix` parameter with a null value, the expression will evaluate to true, and so the entire `Condition` will evaluate to true\. You must allow an empty string as value of the `prefix` parameter\. From the preceding discussion, recall that allowing the null string allows Alice to retrieve root\-level bucket items as the console does in the preceding discussion\. For more information, see [Step 4\.2: Enable Users to List Root\-Level Content of a Bucket](#walkthrough1-grant-permissions-step2)\. 
+ The `StringNotLike` conditional expression ensures that if the value of the `prefix` parameter is specified and is not `Development/*`, the request will fail\. 

Follow the steps in the preceding section and again update the inline policy you created for user Alice\.

Copy the following policy into the  policy text field, replacing the existing policy:

```
{
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

## Step 6: Grant IAM User Bob Specific Permissions<a name="walkthrough1-grant-permissions-step5"></a>

Now you want to grant Bob permission to the Finance folder\. Follow the steps you used earlier to grant permissions to Alice, but replace the Development folder with the Finance folder\. For step\-by\-step instructions, see [Step 5: Grant IAM User Alice Specific Permissions](#walkthrough-grant-user1-permissions)\. 

## Step 7: Secure the Private Folder<a name="walkthrough-secure-private-folder-explicit-deny"></a>

In this example, you have only two users\. You granted all the minimum required permissions at the group level and granted user\-level permissions only when you really need to permissions at the individual user level\. This approach helps minimize the effort of managing permissions\. As the number of users increases, managing permissions can become cumbersome\. For example, we don't want any of the users in this example to access the content of the Private folder\. How do you ensure you don't accidentally grant a user permission to it? You add a policy that explicitly denies access to the folder\. An explicit deny overrides any other permissions\. To ensure that the Private folder remains private, you can add the follow two deny statements to the group policy:
+ Add the following statement to explicitly deny any action on resources in the `Private` folder \(`companybucket/Private/*`\)\.

  ```
  {
    "Sid": "ExplictDenyAccessToPrivateFolderToEveryoneInTheGroup",
    "Action": ["s3:*"],
    "Effect": "Deny",
    "Resource":["arn:aws:s3:::companybucket/Private/*"]
  }
  ```
+ You also deny permission for the list objects action when the request specifies the `Private/` prefix\. In the console, if Bob or Alice double\-clicks the Private folder, this policy causes Amazon S3 to return an error response\.

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

Replace the Consultants group policy with an updated policy that includes the preceding deny statements\. After the updated policy is applied, none of the users in the group will be able to access the Private folder in your bucket\. 

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

   Use your AWS account credentials, not the credentials of an IAM user, to sign in to the console\.

1. Replace the existing `AllowGroupToSeeBucketListInTheConsole` managed policy that is attached to the Consultants group with the following policy\. Remember to replace `companybucket` in the policy with the name of your bucket\. 

   For instructions, see [Editing Customer Managed Policies](http://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_managed-using.html#edit-managed-policy-console) in the *IAM User Guide*\. When following the instructions, make sure to follow the directions for applying your changes to all principal entities that the policy is attached to\. 

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

## Cleanup<a name="walkthrough-cleanup"></a>

In order to clean up, go to the IAM console and remove the users Alice and Bob\. For step\-by\-step instructions, go to [Deleting an IAM User](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_manage.html#id_users_deleting) in the *IAM User Guide*\.

To ensure that you aren't charged further for storage, you should also delete the objects and the bucket that you created for this exercise \.

## Related Resources<a name="RelatedResources-walkthrough1"></a>
+ [Working with Policies](http://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_manage.html) in the *IAM User Guide*\.