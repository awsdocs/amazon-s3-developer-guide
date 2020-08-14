# Creating access points<a name="creating-access-points"></a>

Amazon S3 provides functionality for creating and managing access points\. By default, you can create up to 1,000 access points per Region for each of your AWS accounts\. If you need more than 1,000 access points for a single account in a single Region, you can request a service quota increase\. For more information about service quotas and requesting an increase, see [AWS Service Quotas](https://docs.aws.amazon.com/general/latest/gr/aws_service_limits.html) in the *AWS General Reference*\.

You can create S3 access points using the AWS Management Console, AWS Command Line Interface \(AWS CLI\), AWS SDKs, or Amazon S3 REST API\. The following examples demonstrate how to create an access point with the AWS CLI and AWS SDK for Java\.

For information about how to create access points using the AWS Management Console, see [Introduction to Amazon S3 Access Points](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/access-points.html) in the *Amazon Simple Storage Service Console User Guide*\. For more information about how to create access points using the REST API, see [CreateAccessPoint](https://docs.aws.amazon.com/AmazonS3/latest/API/API_control_CreateAccessPoint.html) in the *Amazon Simple Storage Service API Reference*\.

**Note**  
Because you might want to publicize your access point name in order to allow users to use the access point, we recommend that you avoid including sensitive information in the access point name\.

## Rules for naming Amazon S3 access points<a name="access-points-names"></a>

Access point names must meet the following conditions:
+ Must be unique within a single AWS account and Region
+ Must comply with DNS naming restrictions
+ Must begin with a number or lowercase letter
+ Must be between 3 and 50 characters long
+ Can't begin or end with a dash
+ Can't contain underscores, uppercase letters, or periods

**Example**  
***Example: Create an Access Point***  
The following example creates an access point named `example-ap` for bucket `example-bucket` in account `123456789012`\. To create the access point, you send a request to Amazon S3, specifying the access point name, the name of the bucket that you want to associate the access point with, and the account ID for the AWS account that owns the bucket\. For information about naming rules, see [Rules for naming Amazon S3 access points](#access-points-names)\.  

```
aws s3control create-access-point --name example-ap --account-id 123456789012 --bucket example-bucket
```

## Creating access points restricted to a virtual private cloud<a name="access-points-vpc"></a>

When you create an access point, you can choose to make the access point accessible from the internet, or you can specify that all requests made through that access point must originate from a specific virtual private cloud \(VPC\)\. An access point that's accessible from the internet is said to have a network origin of `Internet`\. It can be used from anywhere on the internet, subject to any other access restrictions in place for the access point, underlying bucket, and related resources, such as the requested objects\. An access point that's only accessible from a specified VPC has a network origin of `VPC`, and Amazon S3 rejects any request made to the access point that doesn't originate from that VPC\.

**Important**  
You can only specify an access point's network origin when you create the access point\. After you create the access point, you can't change its network origin\.

To restrict an access point to VPC\-only access, you include the `VpcConfiguration` parameter with the request to create the access point\. In the `VpcConfiguration` parameter, you specify the VPC ID that you want to be able to use the access point\. Amazon S3 then rejects requests made through the access point unless they originate from that VPC\.

You can retrieve an access point's network origin using the AWS CLI, AWS SDKs, or REST APIs\. If an access point has a VPC configuration specified, its network origin is `VPC`\. Otherwise, the access point's network origin is `Internet`\.

**Example**  
***Example: Create an Access Point Restricted to VPC Access***  
The following example creates an access point named `example-vpc-ap` for bucket `example-bucket` in account `123456789012` that allows access only from VPC `vpc-1a2b3c`\. The example then verifies that the new access point has a network origin of `VPC`\.  

```
aws s3control create-access-point --name example-vpc-ap --account-id 123456789012 --bucket example-bucket --vpc-configuration VpcId=vpc-1a2b3c
```

```
aws s3control get-access-point --name example-vpc-ap --account-id 123456789012

{
    "Name": "example-vpc-ap",
    "Bucket": "example-bucket",
    "NetworkOrigin": "VPC",
    "VpcConfiguration": {
        "VpcId": "vpc-1a2b3c"
    },
    "PublicAccessBlockConfiguration": {
        "BlockPublicAcls": true,
        "IgnorePublicAcls": true,
        "BlockPublicPolicy": true,
        "RestrictPublicBuckets": true
    },
    "CreationDate": "2019-11-27T00:00:00Z"
}
```

To use an access point with a VPC, you must modify the access policy for your VPC endpoint\. VPC endpoints allow traffic to flow from your VPC to Amazon S3\. They have access\-control policies that control how resources within the VPC are allowed to interact with S3\. Requests from your VPC to S3 only succeed through an access point if the VPC endpoint policy grants access to both the access point and the underlying bucket\.

The following example policy statement configures a VPC endpoint to allow calls to `GetObject` for a bucket named `awsexamplebucket1` and an access point named `example-vpc-ap`\.

```
{
    "Version": "2012-10-17",
    "Statement": [
    {
        "Principal": "*",
        "Action": [
            "s3:GetObject"
        ],
        "Effect": "Allow",
        "Resource": [
            "arn:aws:s3:::awsexamplebucket1/*",
            "arn:aws:s3:us-west-2:123456789012:accesspoint/example-vpc-ap/object/*"
        ]
    }]
}
```

**Note**  
The `"Resource"` declaration in this example uses an Amazon Resource Name \(ARN\) to specify the access point\. For more information about access point ARNs, see [Using access points](using-access-points.md)\. 

For more information about VPC endpoint policies, see [Using Endpoint Policies for Amazon S3](https://docs.aws.amazon.com/vpc/latest/userguide/vpc-endpoints-s3.html#vpc-endpoints-policies-s3) in the *Amazon Virtual Private Cloud User Guide*\.

## Managing public access to access points<a name="access-points-bpa-settings"></a>

Amazon S3 access points support independent *block public access* settings for each access point\. When you create an access point, you can specify block public access settings that apply to that access point\. For any request made through an access point, Amazon S3 evaluates the block public access settings for that access point, the underlying bucket, and the bucket owner's account\. If any of these settings indicate that the request should be blocked, Amazon S3 rejects the request\.

For more information about the S3 Block Public Access feature, see [Using Amazon S3 block public access](access-control-block-public-access.md)\.

**Important**  
All block public access settings are enabled by default for access points\. You must explicitly disable any settings that you don't want to apply to an access point\.
Amazon S3 currently doesn't support changing an access point's block public access settings after the access point has been created\.

**Example**  
***Example: Create an Access Point with Custom Block Public Access Settings***  
This example creates an access point named `example-ap` for bucket `example-bucket` in account `123456789012` with non\-default Block Public Access settings\. The example then retrieves the new access point's configuration to verify its Block Public Access settings\.  

```
aws s3control create-access-point --name example-ap --account-id 123456789012 --bucket example-bucket --public-access-block-configuration BlockPublicAcls=false,IgnorePublicAcls=false,BlockPublicPolicy=true,RestrictPublicBuckets=true
```

```
aws s3control get-access-point --name example-ap --account-id 123456789012

{
    "Name": "example-ap",
    "Bucket": "example-bucket",
    "NetworkOrigin": "Internet",
    "PublicAccessBlockConfiguration": {
        "BlockPublicAcls": false,
        "IgnorePublicAcls": false,
        "BlockPublicPolicy": true,
        "RestrictPublicBuckets": true
    },
    "CreationDate": "2019-11-27T00:00:00Z"
}
```

## Configuring IAM policies for using access points<a name="access-points-policies"></a>

Amazon S3 access points support AWS Identity and Access Management \(IAM\) resource policies that allow you to control the use of the access point by resource, user, or other conditions\. For an application or user to be able to access objects through an access point, both the access point and the underlying bucket must permit the request\.

**Important**  
Adding an S3 access point to a bucket doesn't change the bucket's behavior when accessed through the existing bucket name or ARN\. All existing operations against the bucket will continue to work as before\. Restrictions that you include in an access point policy apply only to requests made through that access point\. 

### Condition keys<a name="access-points-condition-keys"></a>

S3 access points introduce three new condition keys that can be used in IAM policies to control access to your resources:

**s3:DataAccessPointArn**  
This is a string that you can use to match on an access point ARN\. The following example matches all access points for AWS account `123456789012` in Region `us-west-2`:  

```
"Condition" : {
    "StringLike": {
        "s3:DataAccessPointArn": "arn:aws:s3:us-west-2:123456789012:accesspoint/*"
    }
}
```

**s3:DataAccessPointAccount**  
This is a string operator that you can use to match on the account ID of the owner of an access point\. The following example matches all access points owned by AWS account `123456789012`\.  

```
"Condition" : {
    "StringEquals": {
        "s3:DataAccessPointAccount": "123456789012"
    }
}
```

**s3:AccessPointNetworkOrigin**  
This is a string operator that you can use to match on the network origin, either `Internet` or `VPC`\. The following example matches only access points with a VPC origin\.  

```
"Condition" : {
    "StringEquals": {
        "s3:AccessPointNetworkOrigin": "VPC"
    }
}
```

For more information about using condition keys with Amazon S3, see [Actions, resources, and condition keys for Amazon S3](list_amazons3.md)\.

### Delegating access control to access points<a name="access-points-delegating-control"></a>

You can delegate access control for a bucket to the bucket's access points\. The following example bucket policy allows full access to all access points owned by the bucket owner's account\. Thus, all access to this bucket is controlled by the policies attached to its access points\. We recommend configuring your buckets this way for all use cases that don't require direct access to the bucket\.

**Example Bucket policy delegating access control to access points**  

```
{
    "Version": "2012-10-17",
    "Statement" : [
    {
        "Effect": "Allow",
        "Principal" : { "AWS": "*" },
        "Action" : "*",
        "Resource" : [ "Bucket ARN", "Bucket ARN/*"],
        "Condition": {
            "StringEquals" : { "s3:DataAccessPointAccount" : "Bucket owner's account ID" }
        }
    }]
}
```

### Access point policy examples<a name="access-point-policy-examples"></a>

The following examples demonstrate how to create IAM policies to control requests made through an access point\.

**Note**  
Permissions granted in an access point policy are only effective if the underlying bucket also allows the same access\. You can accomplish this in two ways:  
\(Recommended\) Delegate access control from the bucket to the access point as described in [Delegating access control to access points](#access-points-delegating-control)\.
Add the same permissions contained in the access point policy to the underlying bucket's policy\. The first access point policy example demonstrates how to modify the underlying bucket policy to allow the necessary access\.

**Example Access point policy grant**  
The following access point policy grants IAM user `Alice` in account `123456789012` permissions to `GET` and `PUT` objects with the prefix `Alice/` through access point `my-access-point` in account `123456789012`\.  

```
{
    "Version":"2012-10-17",
    "Statement": [
    {
        "Effect": "Allow",
        "Principal": {
            "AWS": "arn:aws:iam::123456789012:user/Alice"
        },
        "Action": ["s3:GetObject", "s3:PutObject"],
        "Resource": "arn:aws:s3:us-west-2:123456789012:accesspoint/my-access-point/object/Alice/*"
    }]
}
```

**Note**  
For the access point policy to effectively grant access to `Alice`, the underlying bucket must also allow the same access to `Alice`\. You can delegate access control from the bucket to the access point as described in [Delegating access control to access points](#access-points-delegating-control)\. Or, you can add the following policy to the underlying bucket to grant the necessary permissions to Alice\. Note that the `Resource` entry differs between the access point and bucket policies\.   

```
{
    "Version": "2012-10-17",
    "Statement": [
    {
        "Effect": "Allow",
        "Principal": {
            "AWS": "arn:aws:iam::123456789012:user/Alice"
        },
        "Action": ["s3:GetObject", "s3:PutObject"],
        "Resource": "arn:aws:s3:::awsexamplebucket1/Alice/*"
    }]    
}
```

**Example Access point policy with tag condition**  
The following access point policy grants IAM user `Bob` in account `123456789012` permissions to `GET` objects through access point `my-access-point` in account `123456789012` that have the tag key `data` set with a value of `finance`\.  

```
{
    "Version":"2012-10-17",
    "Statement": [
    {
        "Effect":"Allow",
        "Principal" : {
            "AWS": "arn:aws:iam::123456789012:user/Bob"
        },
        "Action":"s3:GetObject",
        "Resource" : "arn:aws:s3:us-west-2:123456789012:accesspoint/my-access-point/object/*",
        "Condition" : {
            "StringEquals": {
                "s3:ExistingObjectTag/data": "finance"
            }
        }
    }]
}
```

**Example Access point policy allowing bucket listing**  
The following access point policy allows IAM user `Charles` in account `123456789012` permission to view the objects contained in the bucket underlying access point `my-access-point` in account `123456789012`\.  

```
{
    "Version":"2012-10-17",
    "Statement": [
    {
        "Effect": "Allow",
        "Principal": {
            "AWS": "arn:aws:iam::123456789012:user/Charles"
        },
        "Action": "s3:ListBucket",
        "Resource": "arn:aws:s3:us-west-2:123456789012:accesspoint/my-access-point"
    }]
}
```

**Example Service control policy**  
The following service control policy requires all new access points to be created with a VPC network origin\. With this policy in place, users in your organization can't create new access points that are accessible from the internet\.  

```
{
    "Version": "2012-10-17",
    "Statement": [
    {
        "Effect": "Deny",
        "Action": "s3:CreateAccessPoint",
        "Resource": "*",
        "Condition": {
            "StringNotEquals": {
                "s3:AccessPointNetworkOrigin": "VPC"
            }
        }
    }]
}
```

**Example Bucket policy to limit S3 operations to VPC network origins**  
The following bucket policy limits access to all S3 object operations for bucket `examplebucket` to access points with a VPC network origin\.  
Before using a statement like this example, make sure you don't need to use features that aren't supported by access points, such as Cross\-Region Replication\.

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Deny",
            "Principal": "*",
            "Action": [
                "s3:AbortMultipartUpload",
                "s3:BypassGovernanceRetention",
                "s3:DeleteObject",
                "s3:DeleteObjectTagging",
                "s3:DeleteObjectVersion",
                "s3:DeleteObjectVersionTagging",
                "s3:GetObject",
                "s3:GetObjectAcl",
                "s3:GetObjectLegalHold",
                "s3:GetObjectRetention",
                "s3:GetObjectTagging",
                "s3:GetObjectVersion",
                "s3:GetObjectVersionAcl",
                "s3:GetObjectVersionTagging",
                "s3:ListMultipartUploadParts",
                "s3:PutObject",
                "s3:PutObjectAcl",
                "s3:PutObjectLegalHold",
                "s3:PutObjectRetention",
                "s3:PutObjectTagging",
                "s3:PutObjectVersionAcl",
                "s3:PutObjectVersionTagging",
                "s3:RestoreObject"
            ],
            "Resource": "arn:aws:s3:::examplebucket/*",
            "Condition": {
                "StringNotEquals": {
                    "s3:AccessPointNetworkOrigin": "VPC"
                }
            }
        }
    ]
}
```