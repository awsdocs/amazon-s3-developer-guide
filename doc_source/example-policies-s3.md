# User Policy Examples<a name="example-policies-s3"></a>

This section shows several IAM user policies for controlling user access to Amazon S3\. For information about access policy language, see [Access Policy Language Overview](access-policy-language-overview.md)\.

The following example policies will work if you test them programmatically; however, in order to use them with the Amazon S3 console, you will need to grant additional permissions that are required by the console\. For information about using policies such as these with the Amazon S3 console, see [An Example Walkthrough: Using user policies to control access to your bucket](walkthrough1.md)\. 

**Topics**
+ [Example: Allow an IAM user access to one of your buckets](#iam-policy-ex0)
+ [Example: Allow each IAM user access to a folder in a bucket](#iam-policy-ex1)
+ [Example: Allow a group to have a shared folder in Amazon S3](#iam-policy-ex2)
+ [Example: Allow all your users to read objects in a portion of the corporate bucket](#iam-policy-ex3)
+ [Example: Allow a partner to drop files into a specific portion of the corporate bucket](#iam-policy-ex4)
+ [An Example Walkthrough: Using user policies to control access to your bucket](walkthrough1.md)

## Example: Allow an IAM user access to one of your buckets<a name="iam-policy-ex0"></a>

In this example, you want to grant an IAM user in your AWS account access to one of your buckets, `examplebucket`, and allow the user to add, update, and delete objects\. 

In addition to granting the `s3:PutObject`, `s3:GetObject`, and `s3:DeleteObject` permissions to the user, the policy also grants the `s3:ListAllMyBuckets`, `s3:GetBucketLocation`, and `s3:ListBucket` permissions\. These are the additional permissions required by the console\. Also, the `s3:PutObjectAcl` and the `s3:GetObjectAcl` actions are required to be able to copy, cut, and paste objects in the console\. For an example walkthrough that grants permissions to users and tests them using the console, see [An Example Walkthrough: Using user policies to control access to your bucket](walkthrough1.md)\. 

```
{
   "Version":"2012-10-17",
   "Statement":[
      {
         "Effect":"Allow",
         "Action":[
            "s3:ListAllMyBuckets"
         ],
         "Resource":"arn:aws:s3:::*"
      },
      {
         "Effect":"Allow",
         "Action":[
            "s3:ListBucket",
            "s3:GetBucketLocation"
         ],
         "Resource":"arn:aws:s3:::examplebucket"
      },
      {
         "Effect":"Allow",
         "Action":[
            "s3:PutObject",
            "s3:PutObjectAcl",
            "s3:GetObject",
            "s3:GetObjectAcl",
            "s3:DeleteObject"
         ],
         "Resource":"arn:aws:s3:::examplebucket/*"
      }
   ]
}
```

## Example: Allow each IAM user access to a folder in a bucket<a name="iam-policy-ex1"></a>

In this example, you want two IAM users, Alice and Bob, to have access to your bucket, `examplebucket`, so they can add, update, and delete objects\. However, you want to restrict each user’s access to a single folder in the bucket\. You might create folders with names that match the user names\. 

```
examplebucket
   Alice/
   Bob/
```

To grant each user access only to his or her folder, you can write a policy for each user and attach it individually\. For example, you can attach the following policy to user Alice to allow her specific Amazon S3 permissions on the `examplebucket/Alice` folder\.

```
{
   "Version":"2012-10-17",
   "Statement":[
      {
         "Effect":"Allow",
         "Action":[
            "s3:PutObject",
            "s3:GetObject",
            "s3:GetObjectVersion",
            "s3:DeleteObject",
            "s3:DeleteObjectVersion"
         ],
         "Resource":"arn:aws:s3:::examplebucket/Alice/*"
      }
   ]
}
```

You then attach a similar policy to user `Bob`, identifying folder `Bob` in the `Resource` value\. 

Instead of attaching policies to individual users, though, you can write a single policy that uses a policy variable and attach the policy to a group\. You will first need to create a group and add both Alice and Bob to the group\. The following example policy allows a set of Amazon S3 permissions in the `examplebucket/${aws:username}` folder\. When the policy is evaluated, the policy variable `${aws:username}` is replaced by the requester's user name\. For example, if Alice sends a request to put an object, the operation is allowed only if Alice is uploading the object to the `examplebucket/Alice` folder\.

```
 1. {
 2.    "Version":"2012-10-17",
 3.    "Statement":[
 4.       {
 5.          "Effect":"Allow",
 6.          "Action":[
 7.             "s3:PutObject",
 8.             "s3:GetObject",
 9.             "s3:GetObjectVersion",
10.             "s3:DeleteObject",
11.             "s3:DeleteObjectVersion"
12.          ],
13.          "Resource":"arn:aws:s3:::examplebucket/${aws:username}/*"
14.       }
15.    ]
16. }
```

**Note**  
When using policy variables, you must explicitly specify version `2012-10-17` in the policy\. The default version of the access policy language, 2008\-10\-17, does not support policy variables\. 

 If you want to test the preceding policy on the Amazon S3 console, the console requires permission for additional Amazon S3 permissions, as shown in the following policy\. For information about how the console uses these permissions, see [An Example Walkthrough: Using user policies to control access to your bucket](walkthrough1.md)\. 

```
{
 "Version":"2012-10-17",
  "Statement": [
    {
      "Sid": "AllowGroupToSeeBucketListInTheConsole",
      "Action": [ "s3:ListAllMyBuckets", "s3:GetBucketLocation" ],
      "Effect": "Allow",
      "Resource": [ "arn:aws:s3:::*"  ]
    },
    {
      "Sid": "AllowRootLevelListingOfTheBucket",
      "Action": ["s3:ListBucket"],
      "Effect": "Allow",
      "Resource": ["arn:aws:s3:::examplebucket"],
      "Condition":{ 
            "StringEquals":{
                    "s3:prefix":[""], "s3:delimiter":["/"]
                           }
                 }
    },
    {
      "Sid": "AllowListBucketOfASpecificUserPrefix",
      "Action": ["s3:ListBucket"],
      "Effect": "Allow",
      "Resource": ["arn:aws:s3:::examplebucket"],
      "Condition":{  "StringLike":{"s3:prefix":["${aws:username}/*"] }
       }
    },
      {
     "Sid": "AllowUserSpecificActionsOnlyInTheSpecificUserPrefix",
         "Effect":"Allow",
         "Action":[
            "s3:PutObject",
            "s3:GetObject",
            "s3:GetObjectVersion",
            "s3:DeleteObject",
            "s3:DeleteObjectVersion"
         ],
         "Resource":"arn:aws:s3:::examplebucket/${aws:username}/*"
      }
  ]
}
```

**Note**  
In the 2012\-10\-17 version of the policy, policy variables start with `$`\. This change in syntax can potentially create a conflict if your object key includes a `$`\. For example, to include an object key `my$file` in a policy, you specify the `$` character with `${$}`, `my${$}file`\.

Although IAM user names are friendly, human\-readable identifiers, they are not required to be globally unique\. For example, if user Bob leaves the organization and another Bob joins, then new Bob could access old Bob's information\. Instead of using user names, you could create folders based on user IDs\. Each user ID is unique\. In this case, you must modify the preceding policy to use the `${aws:userid}` policy variable\. For more information about user identifiers, see [IAM Identifiers](http://docs.aws.amazon.com/IAM/latest/UserGuide/reference_identifiers.html) in the *IAM User Guide*\.

```
{
   "Version":"2012-10-17",
   "Statement":[
      {
         "Effect":"Allow",
         "Action":[
            "s3:PutObject",
            "s3:GetObject",
            "s3:GetObjectVersion",
            "s3:DeleteObject",
            "s3:DeleteObjectVersion"
         ],
         "Resource":"arn:aws:s3:::my_corporate_bucket/home/${aws:userid}/*"
      }
   ]
}
```

### Allow non\-IAM users \(mobile app users\) access to folders in a bucket<a name="non-iam-mobile-app-user-access"></a>

Suppose that you want to develop a mobile app, a game that stores users' data in an S3 bucket\. For each app user, you want to create a folder in your bucket\. You also want to limit each user’s access to his or her own folder\.  But you cannot create folders before someone downloads your app and starts playing the game, because you don’t have a user ID\. 

In this case, you can require users to sign in to your app by using public identity providers such as Login with Amazon, Facebook, or Google\. After users have signed in to your app through one of these providers, they have a user ID that you can use to create user\-specific folders at run time\. 

You can then use web identity federation in AWS Security Token Service to integrate information from the identity provider with your app and to get temporary security credentials for each user\. You can then create IAM policies that allow the app to access your bucket and perform such operations as creating user\-specific folders and uploading data\. For more information about web identity federation, see [About Web Identity Federation](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_providers_oidc.html) in the *IAM User Guide*\.

## Example: Allow a group to have a shared folder in Amazon S3<a name="iam-policy-ex2"></a>

 Attaching the following policy to the group grants everybody in the group access to the following folder in Amazon S3: `my_corporate_bucket/share/marketing`\. Group members are allowed to access only the specific Amazon S3 permissions shown in the policy and only for objects in the specified folder\. 

```
 1. {
 2.    "Version":"2012-10-17",
 3.    "Statement":[
 4.       {
 5.          "Effect":"Allow",
 6.          "Action":[
 7.             "s3:PutObject",
 8.             "s3:GetObject",
 9.             "s3:GetObjectVersion",
10.             "s3:DeleteObject",
11.             "s3:DeleteObjectVersion"
12.          ],
13.          "Resource":"arn:aws:s3:::my_corporate_bucket/share/marketing/*"
14.       }
15.    ]
16. }
```

## Example: Allow all your users to read objects in a portion of the corporate bucket<a name="iam-policy-ex3"></a>

 In this example, you create a group called `AllUsers`, which contains all the IAM users that are owned by the AWS account\. You then attach a policy that gives the group access to `GetObject` and `GetObjectVersion`, but only for objects in the `my_corporate_bucket/readonly` folder\. 

```
 1. {
 2.    "Version":"2012-10-17",
 3.    "Statement":[
 4.       {
 5.          "Effect":"Allow",
 6.          "Action":[
 7.             "s3:GetObject",
 8.             "s3:GetObjectVersion"
 9.          ],
10.          "Resource":"arn:aws:s3:::my_corporate_bucket/readonly/*"
11.       }
12.    ]
13. }
```

## Example: Allow a partner to drop files into a specific portion of the corporate bucket<a name="iam-policy-ex4"></a>

 In this example, you create a group called `WidgetCo` that represents a partner company\. You create an IAM user for the specific person or application at the partner company that needs access, and then you put the user in the group\. 

You then attach a policy that gives the group `PutObject` access to the following folder in the corporate bucket: `my_corporate_bucket/uploads/widgetco`\. 

You want to prevent the `WidgetCo` group from doing anything else with the bucket, so you add a statement that explicitly denies permission to any Amazon S3 permissions except `PutObject` on any Amazon S3 resource in the AWS account\. This step is necessary only if there's a broad policy in use elsewhere in your AWS account that gives users wide access to Amazon S3 resources\.

```
 1. {
 2.    "Version":"2012-10-17",
 3.    "Statement":[
 4.       {
 5.          "Effect":"Allow",
 6.          "Action":"s3:PutObject",
 7.          "Resource":"arn:aws:s3:::my_corporate_bucket/uploads/widgetco/*"
 8.       },
 9.       {
10.          "Effect":"Deny",
11.          "NotAction":"s3:PutObject",
12.          "Resource":"arn:aws:s3:::my_corporate_bucket/uploads/widgetco/*"
13.       },
14.       {
15.          "Effect":"Deny",
16.          "Action":"s3:*",
17.          "NotResource":"arn:aws:s3:::my_corporate_bucket/uploads/widgetco/*"
18.       }
19.    ]
20. }
```