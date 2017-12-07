# Example Bucket Policies for VPC Endpoints for Amazon S3<a name="example-bucket-policies-vpc-endpoint"></a>

You can use Amazon S3 bucket policies to control access to buckets from specific Amazon Virtual Private Cloud \(Amazon VPC\) endpoints, or specific VPCs\. This section contains example bucket policies that can be used to control S3 bucket access from VPC endpoints\. To learn how to set up VPC endpoints, go to the [VPC Endpoints](http://docs.aws.amazon.com/AmazonVPC/latest/UserGuide/vpc-endpoints.html) topic in the *Amazon VPC User Guide*\. 

Amazon VPC enables you to launch Amazon Web Services \(AWS\) resources into a virtual network that you define\. A VPC endpoint enables you to create a private connection between your VPC and another AWS service without requiring access over the Internet, through a VPN connection, through a NAT instance, or through AWS Direct Connect\. 

A VPC endpoint for Amazon S3 is a logical entity within a VPC that allows connectivity only to Amazon S3\. The VPC endpoint routes requests to Amazon S3 and routes responses back to the VPC\. VPC endpoints only change how requests are routed, Amazon S3 public endpoints and DNS names will continue to work with VPC endpoints\. For important information about using Amazon VPC endpoints with Amazon S3, go to the [Endpoints for Amazon S3](http://docs.aws.amazon.com/AmazonVPC/latest/UserGuide/vpc-endpoints.html#vpc-endpoints-s3) topic in the *Amazon VPC User Guide*\. 

VPC endpoints for Amazon S3 provides two ways to control access to your Amazon S3 data: 

+ You can control what requests, users, or groups are allowed through a specific VPC endpoint\. For information on this type of access control, go to the [VPC Endpoints \- Controlling Access to Services](http://docs.aws.amazon.com/AmazonVPC/latest/UserGuide/vpc-endpoints.html#vpc-endpoints-access) topic in the *Amazon VPC User Guide*\.

+ You can control which VPCs or VPC endpoints have access to your S3 buckets by using S3 bucket policies\. For examples of this type of bucket policy access control, see the following topics on restricting access\.


+ [Restricting Access to a Specific VPC Endpoint](#example-bucket-policies-restrict-accesss-vpc-endpoint)
+ [Restricting Access to a Specific VPC](#example-bucket-policies-restrict-access-vpc)
+ [Related Resources](#example-bucket-policies-restrict-access-vpc-related-resources)

## Restricting Access to a Specific VPC Endpoint<a name="example-bucket-policies-restrict-accesss-vpc-endpoint"></a>

The following is an example of an S3 bucket policy that allows access to a specific bucket, `examplebucket`, only from the VPC endpoint with the ID `vpce-1a2b3c4d`\. The policy uses the `aws:sourceVpce` condition key to restrict access to the specified VPC endpoint\. The `aws:sourceVpce` condition key does not require an ARN for the VPC endpoint resource, only the VPC endpoint ID\. For more information about using conditions in a policy, see [Specifying Conditions in a Policy](amazon-s3-policy-keys.md)\.

```
 1. {
 2.    "Version": "2012-10-17",
 3.    "Id": "Policy1415115909152",
 4.    "Statement": [
 5.      {
 6.        "Sid": "Access-to-specific-VPCE-only",
 7.        "Action": "s3:*",
 8.        "Effect": "Deny",
 9.        "Resource": ["arn:aws:s3:::examplebucket",
10.                     "arn:aws:s3:::examplebucket/*"],
11.        "Condition": {
12.          "StringNotEquals": {
13.            "aws:sourceVpce": "vpce-1a2b3c4d"
14.          }
15.        },
16.        "Principal": "*"
17.      }
18.    ]
19. }
```

## Restricting Access to a Specific VPC<a name="example-bucket-policies-restrict-access-vpc"></a>

You can create a bucket policy that restricts access to a specific VPC by using the `aws:sourceVpc` condition key\. This is useful if you have multiple VPC endpoints configured in the same VPC, and you want to manage access to your S3 buckets for all of your endpoints\. The following is an example of a policy that allows VPC `vpc-111bbb22` to access `examplebucket`\. The `vpc-111bbb22` condition key does not require an ARN for the VPC resource, only the VPC ID\.

```
 1. {
 2.    "Version": "2012-10-17",
 3.    "Id": "Policy1415115909153",
 4.    "Statement": [
 5.      {
 6.        "Sid": "Access-to-specific-VPC-only",
 7.        "Action": "s3:*",
 8.        "Effect": "Deny",
 9.        "Resource": ["arn:aws:s3:::examplebucket",
10.                     "arn:aws:s3:::examplebucket/*"],
11.        "Condition": {
12.          "StringNotEquals": {
13.            "aws:sourceVpc": "vpc-111bbb22"
14.          }
15.        },
16.        "Principal": "*"
17.      }
18.    ]
19. }
```

## Related Resources<a name="example-bucket-policies-restrict-access-vpc-related-resources"></a>

+ [VPC Endpoints](http://docs.aws.amazon.com/AmazonVPC/latest/UserGuide/vpc-endpoints.html) in the *Amazon VPC User Guide*

+ [Bucket Policy Examples](example-bucket-policies.md)