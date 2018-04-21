# Access Policy Language Overview<a name="access-policy-language-overview"></a>

The topics in this section describe the basic elements used in bucket and user policies as used in Amazon S3\. For complete policy language information, see the [Overview of IAM Policies](http://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies.html) and the [AWS IAM Policy Reference](http://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies.html) topics in the *IAM User Guide*\.

**Note**  
Bucket policies are limited to 20 KB in size\.

## Common Elements in an Access Policy<a name="access-policy-language-s3-specific-details"></a>

In its most basic sense, a policy contains the following elements:
+ **Resources** – Buckets and objects are the Amazon S3 resources for which you can allow or deny permissions\. In a policy, you use the Amazon Resource Name \(ARN\) to identify the resource\. 
+ **Actions** – For each resource, Amazon S3 supports a set of operations\. You identify resource operations that you will allow \(or deny\) by using action keywords \(see [Specifying Permissions in a Policy](using-with-s3-actions.md)\)\. 

  For example, the `s3:ListBucket` permission allows the user permission to the Amazon S3 [GET Bucket \(List Objects\)](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html) operation\. 
+ **Effect** – What the effect will be when the user requests the specific action—this can be either allow or deny\. 

  If you do not explicitly grant access to \(allow\) a resource, access is implicitly denied\. You can also explicitly deny access to a resource, which you might do in order to make sure that a user cannot access it, even if a different policy grants access\.
+ **Principal** – The account or user who is allowed access to the actions and resources in the statement\. In a bucket policy, the principal is the user, account, service, or other entity who is the recipient of this permission\.

The following example bucket policy shows the preceding common policy elements\. The policy allows Dave, a user in account *Account\-ID*, `s3:GetObject`, `s3:GetBucketLocation`, and `s3:ListBucket` Amazon S3 permissions on the `examplebucket` bucket\.

```
{
    "Version": "2012-10-17",
    "Id": "ExamplePolicy01",
    "Statement": [
        {
            "Sid": "ExampleStatement01",
            "Effect": "Allow",
            "Principal": {
                "AWS": "arn:aws:iam::Account-ID:user/Dave"
            },
            "Action": [
                "s3:GetObject",
                "s3:GetBucketLocation",
                "s3:ListBucket"
            ],
            "Resource": [
                "arn:aws:s3:::examplebucket/*",
                "arn:aws:s3:::examplebucket"
            ]
        }
    ]
}
```

For more information about the access policy elements, see the following topics:
+ [Specifying Resources in a Policy](s3-arn-format.md)
+ [Specifying a Principal in a Policy](s3-bucket-user-policy-specifying-principal-intro.md)
+ [Specifying Permissions in a Policy](using-with-s3-actions.md)
+ [Specifying Conditions in a Policy](amazon-s3-policy-keys.md)

The following topics provide additional policy examples:
+ [Bucket Policy Examples](example-bucket-policies.md)
+ [User Policy Examples](example-policies-s3.md)