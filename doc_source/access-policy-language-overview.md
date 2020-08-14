# Policies and Permissions in Amazon S3<a name="access-policy-language-overview"></a>

This page provides an overview of bucket and user policies in Amazon S3 and describes the basic elements of a policy\. Each listed element links to more details about that element and examples of how to use it\. 

For a complete list of Amazon S3 actions, resources, and conditions, see [Actions, resources, and condition keys for Amazon S3](list_amazons3.md)\.

## Policy Language Overview<a name="policy-elements-overview"></a>

In its most basic sense, a policy contains the following elements:
+ [Resources](s3-arn-format.md) – Buckets, objects, access points, and jobs are the Amazon S3 resources for which you can allow or deny permissions\. In a policy, you use the Amazon Resource Name \(ARN\) to identify the resource\. For more information, see [Amazon S3 Resources](s3-arn-format.md)\.
+ [Actions](using-with-s3-actions.md) – For each resource, Amazon S3 supports a set of operations\. You identify resource operations that you will allow \(or deny\) by using action keywords\. 

  For example, the `s3:ListBucket` permission allows the user to use the Amazon S3 [GET Bucket \(List Objects\)](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html) operation\. For more information, see [Amazon S3 Actions](using-with-s3-actions.md)\.
+ [Effect](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements_effect.html) – What the effect will be when the user requests the specific action—this can be either *allow* or *deny*\. 

  If you do not explicitly grant access to \(allow\) a resource, access is implicitly denied\. You can also explicitly deny access to a resource\.You might do this to make sure that a user can't access the resource, even if a different policy grants access\. For more information, see [IAM JSON Policy Elements: Effect](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements_effect.html)\.
+ [Principal](s3-bucket-user-policy-specifying-principal-intro.md) – The account or user who is allowed access to the actions and resources in the statement\. In a bucket policy, the principal is the user, account, service, or other entity that is the recipient of this permission\. For more information, see [Principals](s3-bucket-user-policy-specifying-principal-intro.md)\.
+ [Condition](amazon-s3-policy-keys.md) – Conditions for when a policy is in effect\. You can use AWS‐wide keys and Amazon S3‐specific keys to specify conditions in an Amazon S3 access policy\. For more information, see [Amazon S3 Condition Keys](amazon-s3-policy-keys.md)\.

The following example bucket policy shows the preceding policy elements\. The policy allows Dave, a user in account *Account\-ID*, `s3:GetObject`, `s3:GetBucketLocation`, and `s3:ListBucket` Amazon S3 permissions on the `awsexamplebucket1` bucket\.

```
{
    "Version": "2012-10-17",
    "Id": "ExamplePolicy01",
    "Statement": [
        {
            "Sid": "ExampleStatement01",
            "Effect": "Allow",
            "Principal": {
                "AWS": "arn:aws:iam::123456789012:user/Dave"
            },
            "Action": [
                "s3:GetObject",
                "s3:GetBucketLocation",
                "s3:ListBucket"
            ],
            "Resource": [
                "arn:aws:s3:::awsexamplebucket1/*",
                "arn:aws:s3:::awsexamplebucket1"
            ]
        }
    ]
}
```

For complete policy language information, see [Policies and Permissions](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies.html) and [IAM JSON Policy Reference](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies.html) in the *IAM User Guide*\.