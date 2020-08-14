# Amazon S3 Actions<a name="using-with-s3-actions"></a>

Amazon S3 defines a set of permissions that you can specify in a policy\. These are keywords, each of which maps to a specific Amazon S3 operation\. For more information about Amazon S3 operations, see [Actions](https://docs.aws.amazon.com/AmazonS3/latest/API/API_Operations.html) in the *Amazon Simple Storage Service API Reference*\. 

To see how to specify permissions in an Amazon S3 policy, review the following example policies\. For a list of Amazon S3 actions, resources, and condition keys for use in policies, see [Actions, resources, and condition keys for Amazon S3](list_amazons3.md)\. For a complete list of Amazon S3 actions, see [Actions](https://docs.aws.amazon.com/AmazonS3/latest/API/API_Operations.html)\.

**Topics**
+ [Example — Object Operations](#using-with-s3-actions-related-to-objects)
+ [Example — Bucket Operations](#using-with-s3-actions-related-to-buckets)
+ [Example — Bucket Subresource Operations](#using-with-s3-actions-related-to-bucket-subresources)
+ [Example — Account Operations](#using-with-s3-actions-related-to-accounts)

## Example — Object Operations<a name="using-with-s3-actions-related-to-objects"></a>

The following example bucket policy grants the `s3:PutObject` and the `s3:PutObjectAcl` permissions to a user \(Dave\)\. If you remove the `Principal` element, you can attach the policy to a user\. These are object operations\. Accordingly, the `relative-id` portion of the `Resource` ARN identifies objects \(`awsexamplebucket1/*`\)\. For more information, see [Amazon S3 Resources](s3-arn-format.md)\.

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "statement1",
            "Effect": "Allow",
            "Principal": {
                "AWS": "arn:aws:iam::12345678901:user/Dave"
            },
            "Action": [
            "s3:PutObject",
            "s3:PutObjectAcl"
            ],
            "Resource": "arn:aws:s3:::awsexamplebucket1/*"
        }
    ]
}
```

**Permissions for All Amazon S3 Actions**  
You can use a wildcard to grant permission for all Amazon S3 actions\.

```
"Action":   "*"
```

## Example — Bucket Operations<a name="using-with-s3-actions-related-to-buckets"></a>

The following example user policy grants the `s3:CreateBucket`, `s3:ListAllMyBuckets`, and the `s3:GetBucketLocation` permissions to a user\. For all these permissions, you set the `relative-id` part of the `Resource` ARN to "\*"\. For all other bucket actions, you must specify a bucket name\. For more information, see [Amazon S3 Resources](s3-arn-format.md)\.

```
{
   "Version":"2012-10-17",
   "Statement":[
      {
         "Sid":"statement1",
         "Effect":"Allow",
         "Action":[
            "s3:CreateBucket", 
            "s3:ListAllMyBuckets", 
            "s3:GetBucketLocation"  
         ],
         "Resource":[
            "arn:aws:s3:::*"
         ]
       }
    ]
}
```

**Policy for Console Access**  
If a user wants to use the AWS Management Console to view buckets and the contents of any of those buckets, the user must have the `s3:ListAllMyBuckets` and `s3:GetBucketLocation` permissions\. For an example, see *Policy for Console Access* in the blog post [Writing IAM Policies: How to Grant Access to an S3 Bucket](https://aws.amazon.com/blogs/security/writing-iam-policies-how-to-grant-access-to-an-amazon-s3-bucket/)\.

## Example — Bucket Subresource Operations<a name="using-with-s3-actions-related-to-bucket-subresources"></a>

The following user policy grants the `s3:GetBucketAcl` permission on the `examplebucket` bucket to user Dave\.

```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "statement1",
      "Effect": "Allow",
      "Principal": {
        "AWS": "arn:aws:iam::123456789012:user/Dave"
      },
      "Action": [
        "s3:GetObjectVersion",
        "s3:GetBucketAcl"
      ],
      "Resource": "arn:aws:s3:::awsexamplebucket1"
    }
  ]
}
```

**DELETE Object Permissions**  
You can delete objects either by explicitly calling the DELETE Object API or by configuring its lifecycle \(see [Object lifecycle management](object-lifecycle-mgmt.md)\) so that Amazon S3 can remove the objects when their lifetime expires\. To explicitly block users or accounts from deleting objects, you must explicitly deny them `s3:DeleteObject`, `s3:DeleteObjectVersion`, and `s3:PutLifecycleConfiguration` permissions\. 

**Explicit Deny**  
By default, users have no permissions\. But as you create users, add users to groups, and grant them permissions, they might get certain permissions that you didn't intend to grant\. That is where you can use explicit deny, which supersedes all other permissions a user might have and denies the user permissions for specific actions\.

## Example — Account Operations<a name="using-with-s3-actions-related-to-accounts"></a>

The following example user policy grants the `s3:GetAccountPublicAccessBlock` permission to a user\. For these permissions, you set the `Resource` value to `"*"`\. For more information, see [Amazon S3 Resources](s3-arn-format.md)\.

```
{
   "Version":"2012-10-17",
   "Statement":[
      {
         "Sid":"statement1",
         "Effect":"Allow",
         "Action":[
            "s3:GetAccountPublicAccessBlock" 
         ],
         "Resource":[
            "*"
         ]
       }
    ]
}
```