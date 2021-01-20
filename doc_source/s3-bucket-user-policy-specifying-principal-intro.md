# Principals<a name="s3-bucket-user-policy-specifying-principal-intro"></a>

The `Principal` element specifies the user, account, service, or other entity that is allowed or denied access to a resource\. The following are examples of specifying `Principal`\. For more information, see [Principal](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements_principal.html) in the *IAM User Guide*\.

## Grant Permissions to an AWS Account<a name="s3-aws-account-permissions"></a>

To grant permissions to an AWS account, identify the account using the following format\.

```
"AWS":"account-ARN"
```

The following are examples\.

```
"Principal":{"AWS":"arn:aws:iam::AccountNumber-WithoutHyphens:root"}
```

```
"Principal":{"AWS":["arn:aws:iam::AccountNumber1-WithoutHyphens:root","arn:aws:iam::AccountNumber2-WithoutHyphens:root"]}
```

Amazon S3 also supports a canonical user ID, which is an obfuscated form of the AWS account ID\. You can specify this ID using the following format\.

```
"CanonicalUser":"64-digit-alphanumeric-value"
```

The following is an example\.

```
"Principal":{"CanonicalUser":"64-digit-alphanumeric-value"}
```

For information about how to find the canonical user ID for your account, see [Finding Your Account Canonical User ID](https://docs.aws.amazon.com/general/latest/gr/acct-identifiers.html#FindingCanonicalId)\.

**Important**  
When you use a canonical user ID in a policy, Amazon S3 might change the canonical ID to the corresponding AWS account ID\. This does not impact the policy because both of these IDs identify the same account\.   
 

## Grant Permissions to an IAM User<a name="s3-aws-user-permissions"></a>

To grant permission to an IAM user within your account, you must provide an `"AWS":"user-ARN"` name\-value pair\.

```
"Principal":{"AWS":"arn:aws:iam::account-number-without-hyphens:user/username"}
```

## Grant Anonymous Permissions<a name="s3-anonymous-permissions"></a>

To grant permission to everyone, also referred as anonymous access, you set the wildcard \(`"*"`\) as the `Principal` value\. For example, if you configure your bucket as a website, you want all the objects in the bucket to be publicly accessible\. The following are equivalent\.

```
"Principal":"*"
```

```
"Principal":{"AWS":"*"}
```

**Warning**  
Use caution when granting anonymous access to your S3 bucket\. When you grant anonymous access, anyone in the world can access your bucket\. We highly recommend that you never grant any kind of anonymous write access to your S3 bucket\.

## Require Access Through CloudFront URLs<a name="require-cloudfront-urls"></a>

You can require that your users access your Amazon S3 content by using Amazon CloudFront URLs instead of Amazon S3 URLs\. To do this, create a CloudFront origin access identity \(OAI\)\. Then, change the permissions either on your bucket or on the objects in your bucket\. The format for specifying the OAI in a `Principal` statement is as follows\.

```
"Principal":{"CanonicalUser":"Amazon S3 Canonical User ID assigned to origin access identity"}
```

For more information, see [ Using an Origin Access Identity to Restrict Access to Your Amazon S3 Content](https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/private-content-restricting-access-to-s3.html) in the *Amazon CloudFront Developer Guide*\. 