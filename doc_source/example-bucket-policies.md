# Bucket Policy Examples<a name="example-bucket-policies"></a>

This section presents a few examples of typical use cases for bucket policies\. The policies use *bucket* and *examplebucket* strings in the resource value\. To test these policies, you need to replace these strings with your bucket name\. For information about access policy language, see [Access Policy Language Overview](access-policy-language-overview.md)\.

**Note**  
Bucket policies are limited to 20 KB in size\.

You can use the [AWS Policy Generator](https://awspolicygen.s3.amazonaws.com/policygen.html) to create a bucket policy for your Amazon S3 bucket\. You can then use the generated document to set your bucket policy by using the [Amazon S3 console](https://console.aws.amazon.com/s3/home), by a number of third\-party tools, or via your application\. 

**Important**  
When testing permissions using the Amazon S3 console, you will need to grant additional permissions that the console requires—`s3:ListAllMyBuckets`, `s3:GetBucketLocation`, and `s3:ListBucket` permissions\. For an example walkthrough that grants permissions to users and tests them using the console, see [An Example Walkthrough: Using user policies to control access to your bucket](walkthrough1.md)\.

**Topics**
+ [Granting Permissions to Multiple Accounts with Added Conditions](#example-bucket-policies-use-case-1)
+ [Granting Read\-Only Permission to an Anonymous User](#example-bucket-policies-use-case-2)
+ [Restricting Access to Specific IP Addresses](#example-bucket-policies-use-case-3)
+ [Restricting Access to a Specific HTTP Referrer](#example-bucket-policies-use-case-4)
+ [Granting Permission to an Amazon CloudFront Origin Identity](#example-bucket-policies-use-case-6)
+ [Adding a Bucket Policy to Require MFA](#example-bucket-policies-use-case-7)
+ [Granting Cross\-Account Permissions to Upload Objects While Ensuring the Bucket Owner Has Full Control](#example-bucket-policies-use-case-8)
+ [Granting Permissions for Amazon S3 Inventory and Amazon S3 Analytics](#example-bucket-policies-use-case-9)
+ [Example Bucket Policies for VPC Endpoints for Amazon S3](example-bucket-policies-vpc-endpoint.md)

## Granting Permissions to Multiple Accounts with Added Conditions<a name="example-bucket-policies-use-case-1"></a>

The following example policy grants the `s3:PutObject` and `s3:PutObjectAcl` permissions to multiple AWS accounts and requires that any request for these operations include the `public-read` canned ACL\. For more information, see [Specifying Permissions in a Policy](using-with-s3-actions.md) and [Specifying Conditions in a Policy](amazon-s3-policy-keys.md)\.

```
 1. {
 2.   "Version":"2012-10-17",
 3.   "Statement":[
 4.     {
 5.       "Sid":"AddCannedAcl",
 6.       "Effect":"Allow",
 7.       "Principal": {"AWS": ["arn:aws:iam::111122223333:root","arn:aws:iam::444455556666:root"]},
 8.       "Action":["s3:PutObject","s3:PutObjectAcl"],
 9.       "Resource":["arn:aws:s3:::examplebucket/*"],
10.       "Condition":{"StringEquals":{"s3:x-amz-acl":["public-read"]}}
11.     }
12.   ]
13. }
```

## Granting Read\-Only Permission to an Anonymous User<a name="example-bucket-policies-use-case-2"></a>

The following example policy grants the `s3:GetObject` permission to any public anonymous users\. \(For a list of permissions and the operations that they allow, see [Specifying Permissions in a Policy](using-with-s3-actions.md)\.\) This permission allows anyone to read the object data, which is useful for when you configure your bucket as a website and want everyone to be able to read objects in the bucket\.

```
 1. {
 2.   "Version":"2012-10-17",
 3.   "Statement":[
 4.     {
 5.       "Sid":"AddPerm",
 6.       "Effect":"Allow",
 7.       "Principal": "*",
 8.       "Action":["s3:GetObject"],
 9.       "Resource":["arn:aws:s3:::examplebucket/*"]
10.     }
11.   ]
12. }
```

**Warning**  
Use caution when granting anonymous access to your S3 bucket\. When you grant anonymous access, anyone in the world can access your bucket\. We highly recommend that you never grant any kind of anonymous write access to your S3 bucket\.

## Restricting Access to Specific IP Addresses<a name="example-bucket-policies-use-case-3"></a>

The following example grants permissions to any user to perform any Amazon S3 operations on objects in the specified bucket\. However, the request must originate from the range of IP addresses specified in the condition\. 

The condition in this statement identifies the 54\.240\.143\.\* range of allowed Internet Protocol version 4 \(IPv4\) IP addresses, with one exception: 54\.240\.143\.188\.

The `Condition` block uses the `IpAddress` and `NotIpAddress` conditions and the `aws:SourceIp` condition key, which is an AWS\-wide condition key\. For more information about these condition keys, see [Specifying Conditions in a Policy](amazon-s3-policy-keys.md)\. The `aws:sourceIp` IPv4 values use the standard CIDR notation\. For more information, see [ IP Address Condition Operators](http://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements.html#Conditions_IPAddress) in the *IAM User Guide*\. 

```
 1. {
 2.   "Version": "2012-10-17",
 3.   "Id": "S3PolicyId1",
 4.   "Statement": [
 5.     {
 6.       "Sid": "IPAllow",
 7.       "Effect": "Allow",
 8.       "Principal": "*",
 9.       "Action": "s3:*",
10.       "Resource": "arn:aws:s3:::examplebucket/*",
11.       "Condition": {
12.          "IpAddress": {"aws:SourceIp": "54.240.143.0/24"},
13.          "NotIpAddress": {"aws:SourceIp": "54.240.143.188/32"} 
14.       } 
15.     } 
16.   ]
17. }
```

### Allowing IPv4 and IPv6 Addresses<a name="example-bucket-policies-use-case-ipv6"></a>

When you start using IPv6 addresses, we recommend that you update all of your organization's policies with your IPv6 address ranges in addition to your existing IPv4 ranges to ensure that the policies continue to work as you make the transition to IPv6\.

The following example bucket policy shows how to mix IPv4 and IPv6 address ranges to cover all of your organization's valid IP addresses\. The example policy would allow access to the example IP addresses `54.240.143.1` and `2001:DB8:1234:5678::1` and would deny access to the addresses `54.240.143.129` and `2001:DB8:1234:5678:ABCD::1`\.

The IPv6 values for `aws:sourceIp` must be in standard CIDR format\. For IPv6 we support using :: to represent a range of 0s, for example, 2032001:DB8:1234:5678::/64\. For more information, see [ IP Address Condition Operators](http://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements.html#Conditions_IPAddress) in the *IAM User Guide*\.

```
 1. {
 2.   "Id":"PolicyId2",
 3.   "Version":"2012-10-17",
 4.   "Statement":[
 5.     {
 6.       "Sid":"AllowIPmix",
 7.       "Effect":"Allow",
 8.       "Principal":"*",
 9.       "Action":"s3:*",
10.       "Resource":"arn:aws:s3:::examplebucket/*",
11.       "Condition": {
12.         "IpAddress": {
13.           "aws:SourceIp": [
14.             "54.240.143.0/24",
15.             "2001:DB8:1234:5678::/64"
16.           ]
17.         },
18.         "NotIpAddress": {
19.           "aws:SourceIp": [
20.              "54.240.143.128/30",
21.              "2001:DB8:1234:5678:ABCD::/80"
22.           ]
23.         }
24.       }
25.     }
26.   ]
27. }
```

## Restricting Access to a Specific HTTP Referrer<a name="example-bucket-policies-use-case-4"></a>

Suppose you have a website with domain name \(`www.example.com` or `example.com`\) with links to photos and videos stored in your S3 bucket, `examplebucket`\. By default, all the S3 resources are private, so only the AWS account that created the resources can access them\. To allow read access to these objects from your website, you can add a bucket policy that allows `s3:GetObject` permission with a condition, using the `aws:referer` key, that the get request must originate from specific webpages\. The following policy specifies the `StringLike` condition with the `aws:Referer` condition key\.

```
 1. {
 2.   "Version":"2012-10-17",
 3.   "Id":"http referer policy example",
 4.   "Statement":[
 5.     {
 6.       "Sid":"Allow get requests originating from www.example.com and example.com.",
 7.       "Effect":"Allow",
 8.       "Principal":"*",
 9.       "Action":"s3:GetObject",
10.       "Resource":"arn:aws:s3:::examplebucket/*",
11.       "Condition":{
12.         "StringLike":{"aws:Referer":["http://www.example.com/*","http://example.com/*"]}
13.       }
14.     }
15.   ]
16. }
```

Make sure the browsers you use include the http `referer` header in the request\.

You can further secure access to objects in the `examplebucket` bucket by adding explicit deny to the bucket policy as shown in the following example\. Explicit deny supersedes any permission you might grant to objects in the `examplebucket` bucket using other means such as ACLs or user policies\.

```
{
   "Version": "2012-10-17",
   "Id": "http referer policy example",
   "Statement": [
     {
       "Sid": "Allow get requests referred by www.example.com and example.com.",
       "Effect": "Allow",
       "Principal": "*",
       "Action": "s3:GetObject",
       "Resource": "arn:aws:s3:::examplebucket/*",
       "Condition": {
         "StringLike": {"aws:Referer": ["http://www.example.com/*","http://example.com/*"]}
       }
     },
      {
        "Sid": "Explicit deny to ensure requests are allowed only from specific referer.",
        "Effect": "Deny",
        "Principal": "*",
        "Action": "s3:*",
        "Resource": "arn:aws:s3:::examplebucket/*",
        "Condition": {
          "StringNotLike": {"aws:Referer": ["http://www.example.com/*","http://example.com/*"]}
        }
      }
   ]
}
```

## Granting Permission to an Amazon CloudFront Origin Identity<a name="example-bucket-policies-use-case-6"></a>

The following example bucket policy grants a CloudFront Origin Identity permission to get \(list\) all objects in your Amazon S3 bucket\. The CloudFront Origin Identity is used to enable the CloudFront private content feature\. The policy uses the CanonicalUser prefix, instead of AWS, to specify a Canonical User ID\. To learn more about CloudFront support for serving private content, go to the [Serving Private Content](http://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/PrivateContent.html) topic in the *Amazon CloudFront Developer Guide*\. You must specify the canonical user ID for your CloudFront distribution's origin access identity\. For instructions about finding the canonical user ID, see [Specifying a Principal in a Policy](s3-bucket-user-policy-specifying-principal-intro.md)\.

```
 1. {
 2.    "Version":"2012-10-17",
 3.    "Id":"PolicyForCloudFrontPrivateContent",
 4.    "Statement":[
 5.      {
 6.        "Sid":" Grant a CloudFront Origin Identity access to support private content",
 7.        "Effect":"Allow",
 8.        "Principal":{"CanonicalUser":"CloudFront Origin Identity Canonical User ID"},
 9.        "Action":"s3:GetObject",
10.        "Resource":"arn:aws:s3:::examplebucket/*"
11.      }
12.    ]
13. }
```

## Adding a Bucket Policy to Require MFA<a name="example-bucket-policies-use-case-7"></a>

Amazon S3 supports MFA\-protected API access, a feature that can enforce multi\-factor authentication \(MFA\) for access to your Amazon S3 resources\. Multi\-factor authentication provides an extra level of security you can apply to your AWS environment\. It is a security feature that requires users to prove physical possession of an MFA device by providing a valid MFA code\. For more information, go to [AWS Multi\-Factor Authentication](https://aws.amazon.com/mfa/)\. You can require MFA authentication for any requests to access your Amazon S3 resources\. 

You can enforce the MFA authentication requirement using the `aws:MultiFactorAuthAge` key in a bucket policy\. IAM users can access Amazon S3 resources by using temporary credentials issued by the AWS Security Token Service \(STS\)\. You provide the MFA code at the time of the STS request\. 

When Amazon S3 receives a request with MFA authentication, the `aws:MultiFactorAuthAge` key provides a numeric value indicating how long ago \(in seconds\) the temporary credential was created\. If the temporary credential provided in the request was not created using an MFA device, this key value is null \(absent\)\. In a bucket policy, you can add a condition to check this value, as shown in the following example bucket policy\. The policy denies any Amazon S3 operation on the `/taxdocuments` folder in the `examplebucket` bucket if the request is not MFA authenticated\. To learn more about MFA authentication, see [Using Multi\-Factor Authentication \(MFA\) in AWS](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_mfa.html) in the *IAM User Guide*\.

```
{
    "Version": "2012-10-17",
    "Id": "123",
    "Statement": [
      {
        "Sid": "",
        "Effect": "Deny",
        "Principal": "*",
        "Action": "s3:*",
        "Resource": "arn:aws:s3:::examplebucket/taxdocuments/*",
        "Condition": { "Null": { "aws:MultiFactorAuthAge": true }}
      }
    ]
 }
```

The `Null` condition in the `Condition` block evaluates to true if the `aws:MultiFactorAuthAge` key value is null, indicating that the temporary security credentials in the request were created without the MFA key\. 

The following bucket policy is an extension of the preceding bucket policy\. It includes two policy statements\. One statement allows the `s3:GetObject` permission on a bucket \(`examplebucket`\) to everyone and another statement further restricts access to the `examplebucket/taxdocuments` folder in the bucket by requiring MFA authentication\. 

```
 1. {
 2.     "Version": "2012-10-17",
 3.     "Id": "123",
 4.     "Statement": [
 5.       {
 6.         "Sid": "",
 7.         "Effect": "Deny",
 8.         "Principal": "*",
 9.         "Action": "s3:*",
10.         "Resource": "arn:aws:s3:::examplebucket/taxdocuments/*",
11.         "Condition": { "Null": { "aws:MultiFactorAuthAge": true } }
12.       },
13.       {
14.         "Sid": "",
15.         "Effect": "Allow",
16.         "Principal": "*",
17.         "Action": ["s3:GetObject"],
18.         "Resource": "arn:aws:s3:::examplebucket/*"
19.       }
20.     ]
21.  }
```

You can optionally use a numeric condition to limit the duration for which the `aws:MultiFactorAuthAge` key is valid, independent of the lifetime of the temporary security credential used in authenticating the request\. For example, the following bucket policy, in addition to requiring MFA authentication, also checks how long ago the temporary session was created\. The policy denies any operation if the `aws:MultiFactorAuthAge` key value indicates that the temporary session was created more than an hour ago \(3,600 seconds\)\. 

```
{
    "Version": "2012-10-17",
    "Id": "123",
    "Statement": [
      {
        "Sid": "",
        "Effect": "Deny",
        "Principal": "*",
        "Action": "s3:*",
        "Resource": "arn:aws:s3:::examplebucket/taxdocuments/*",
        "Condition": {"Null": {"aws:MultiFactorAuthAge": true }}
      },
      {
        "Sid": "",
        "Effect": "Deny",
        "Principal": "*",
        "Action": "s3:*",
        "Resource": "arn:aws:s3:::examplebucket/taxdocuments/*",
        "Condition": {"NumericGreaterThan": {"aws:MultiFactorAuthAge": 3600 }}
       },
       {
         "Sid": "",
         "Effect": "Allow",
         "Principal": "*",
         "Action": ["s3:GetObject"],
         "Resource": "arn:aws:s3:::examplebucket/*"
       }
    ]
 }
```

## Granting Cross\-Account Permissions to Upload Objects While Ensuring the Bucket Owner Has Full Control<a name="example-bucket-policies-use-case-8"></a>

You can allow another AWS account to upload objects to your bucket\. However, you may decide that as a bucket owner you must have full control of the objects uploaded to your bucket\. The following policy enforces that a specific AWS account \(111111111111\) be denied the ability to upload objects unless that account grants full\-control access to the bucket owner identified by the email address \(xyz@amazon\.com\)\. The `StringNotEquals` condition in the policy specifies the `s3:x-amz-grant-full-control` condition key to express the requirement \(see [Specifying Conditions in a Policy](amazon-s3-policy-keys.md)\)\. 

```
 1. {
 2.    "Version":"2012-10-17",
 3.    "Statement":[
 4.      {
 5.        "Sid":"111",
 6.        "Effect":"Allow",
 7.        "Principal":{"AWS":"1111111111"},
 8.        "Action":"s3:PutObject",
 9.        "Resource":"arn:aws:s3:::examplebucket/*"
10.      },
11.      {
12.        "Sid":"112",
13.        "Effect":"Deny",
14.        "Principal":{"AWS":"1111111111" },
15.        "Action":"s3:PutObject",
16.        "Resource":"arn:aws:s3:::examplebucket/*",
17.        "Condition": {
18.          "StringNotEquals": {"s3:x-amz-grant-full-control":["emailAddress=xyz@amazon.com"]}
19.        }
20.      }
21.    ]
22. }
```

## Granting Permissions for Amazon S3 Inventory and Amazon S3 Analytics<a name="example-bucket-policies-use-case-9"></a>

Amazon S3 inventory creates lists of the objects in an S3 bucket and Amazon S3 analytics export creates output files of the data used in the analysis\. The bucket that the inventory lists the objects for is called the *source bucket*\. The bucket where the inventory file is written and the bucket where the analytics export file is written is called a *destination bucket*\. You must create a bucket policy for the destination bucket when setting up inventory for an S3 bucket and when setting up the analytics export\. For more information, see [ Amazon S3 Inventory](storage-inventory.md) and [Amazon S3 Analytics – Storage Class Analysis](analytics-storage-class.md)\.

The following example bucket policy grants Amazon S3 permission to write objects \(PUTs\) from the account for the source bucket to the destination bucket\. You use a bucket policy like this on the destination bucket when setting up Amazon S3 inventory and Amazon S3 analytics export\.

```
 1. {
 2.   "Version":"2012-10-17",
 3.   "Statement":[
 4.     {
 5.       "Sid":"InventoryAndAnalyticsExamplePolicy",
 6.       "Effect":"Allow",
 7.       "Principal": {"Service": "s3.amazonaws.com"},
 8.       "Action":["s3:PutObject"],
 9.       "Resource":["arn:aws:s3:::destination-bucket/*"],
10.       "Condition": {
11.           "ArnLike": {
12.               "aws:SourceArn": "arn:aws:s3:::source-bucket"
13.            },
14.          "StringEquals": {
15.              "aws:SourceAccount": "1234567890",
16.              "s3:x-amz-acl": "bucket-owner-full-control"
17.           }
18.        }
19.     }
20.   ]
21. }
```