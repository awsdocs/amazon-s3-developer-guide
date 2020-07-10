# Example Bucket Policies for VPC Endpoints for Amazon S3<a name="example-bucket-policies-vpc-endpoint"></a>

You can use Amazon S3 bucket policies to control access to buckets from specific Amazon Virtual Private Cloud \(Amazon VPC\) endpoints, or specific VPCs\. This section contains example bucket policies that can be used to control Amazon S3 bucket access from VPC endpoints\. To learn how to set up VPC endpoints, see [VPC Endpoints](https://docs.aws.amazon.com/vpc/latest/userguide/vpc-endpoints.html) in the *Amazon VPC User Guide*\. 

Amazon VPC enables you to launch AWS resources into a virtual network that you define\. A VPC endpoint enables you to create a private connection between your VPC and another AWS service without requiring access over the internet, through a VPN connection, through a NAT instance, or through AWS Direct Connect\. 

A VPC endpoint for Amazon S3 is a logical entity within a VPC that allows connectivity only to Amazon S3\. The VPC endpoint routes requests to Amazon S3 and routes responses back to the VPC\. VPC endpoints change only how requests are routed\. Amazon S3 public endpoints and DNS names will continue to work with VPC endpoints\. For important information about using Amazon VPC endpoints with Amazon S3, see [Gateway VPC Endpoints](https://docs.aws.amazon.com/vpc/latest/userguide/vpce-gateway.html) and [Endpoints for Amazon S3](https://docs.aws.amazon.com/vpc/latest/userguide/vpc-endpoints-s3.html) in the *Amazon VPC User Guide*\. 

VPC endpoints for Amazon S3 provide two ways to control access to your Amazon S3 data: 
+ You can control the requests, users, or groups that are allowed through a specific VPC endpoint\. For information about this type of access control, see [Controlling Access to Services with VPC Endpoints](https://docs.aws.amazon.com/vpc/latest/userguide/vpc-endpoints-access.html) in the *Amazon VPC User Guide*\.
+ You can control which VPCs or VPC endpoints have access to your buckets by using Amazon S3 bucket policies\. For examples of this type of bucket policy access control, see the following topics on restricting access\.

**Topics**
+ [Restricting Access to a Specific VPC Endpoint](#example-bucket-policies-restrict-accesss-vpc-endpoint)
+ [Restricting Access to a Specific VPC](#example-bucket-policies-restrict-access-vpc)
+ [Related Resources](#example-bucket-policies-restrict-access-vpc-related-resources)

**Important**  
When applying the Amazon S3 bucket policies for VPC endpoints described in this section, you might block your access to the bucket without intending to do so\. Bucket permissions that are intended to specifically limit bucket access to connections originating from your VPC endpoint can block all connections to the bucket\. For information about how to fix this issue, see [My bucket policy has the wrong VPC or VPC endpoint ID\. How can I fix the policy so that I can access the bucket?](https://aws.amazon.com/premiumsupport/knowledge-center/s3-regain-access/) in the *AWS Support Knowledge Center*\.

## Restricting Access to a Specific VPC Endpoint<a name="example-bucket-policies-restrict-accesss-vpc-endpoint"></a>

The following is an example of an Amazon S3 bucket policy that restricts access to a specific bucket, `awsexamplebucket1`, only from the VPC endpoint with the ID `vpce-1a2b3c4d`\. The policy denies all access to the bucket if the specified endpoint is not being used\. The `aws:SourceVpce` condition is used to specify the endpoint\. The `aws:SourceVpce` condition does not require an Amazon Resource Name \(ARN\) for the VPC endpoint resource, only the VPC endpoint ID\. For more information about using conditions in a policy, see [Amazon S3 Condition Keys](amazon-s3-policy-keys.md)\.

**Important**  
Before using the following example policy, replace the VPC endpoint ID with an appropriate value for your use case\. Otherwise, you won't be able to access your bucket\.
This policy disables console access to the specified bucket, because console requests don't originate from the specified VPC endpoint\.

```
 1. {
 2.    "Version": "2012-10-17",
 3.    "Id": "Policy1415115909152",
 4.    "Statement": [
 5.      {
 6.        "Sid": "Access-to-specific-VPCE-only",
 7.        "Principal": "*",
 8.        "Action": "s3:*",
 9.        "Effect": "Deny",
10.        "Resource": ["arn:aws:s3:::awsexamplebucket1",
11.                     "arn:aws:s3:::awsexamplebucket1/*"],
12.        "Condition": {
13.          "StringNotEquals": {
14.            "aws:SourceVpce": "vpce-1a2b3c4d"
15.          }
16.        }
17.      }
18.    ]
19. }
```

## Restricting Access to a Specific VPC<a name="example-bucket-policies-restrict-access-vpc"></a>

You can create a bucket policy that restricts access to a specific VPC by using the `aws:SourceVpc` condition\. This is useful if you have multiple VPC endpoints configured in the same VPC, and you want to manage access to your Amazon S3 buckets for all of your endpoints\. The following is an example of a policy that allows VPC `vpc-111bbb22` to access `awsexamplebucket1` and its objects\. The policy denies all access to the bucket if the specified VPC is not being used\. The `vpc-111bbb22` condition key does not require an ARN for the VPC resource, only the VPC ID\.

**Important**  
Before using the following example policy, replace the VPC ID with an appropriate value for your use case\. Otherwise, you won't be able to access your bucket\.
This policy disables console access to the specified bucket, because console requests don't originate from the specified VPC\.

```
 1. {
 2.    "Version": "2012-10-17",
 3.    "Id": "Policy1415115909153",
 4.    "Statement": [
 5.      {
 6.        "Sid": "Access-to-specific-VPC-only",
 7.        "Principal": "*",
 8.        "Action": "s3:*",
 9.        "Effect": "Deny",
10.        "Resource": ["arn:aws:s3:::awsexamplebucket1",
11.                     "arn:aws:s3:::awsexamplebucket1/*"],
12.        "Condition": {
13.          "StringNotEquals": {
14.            "aws:SourceVpc": "vpc-111bbb22"
15.          }
16.        }
17.      }
18.    ]
19. }
```

## Related Resources<a name="example-bucket-policies-restrict-access-vpc-related-resources"></a>
+ [Bucket Policy Examples](example-bucket-policies.md)
+ [VPC Endpoints](https://docs.aws.amazon.com/vpc/latest/userguide/vpc-endpoints.html) in the *Amazon VPC User Guide*