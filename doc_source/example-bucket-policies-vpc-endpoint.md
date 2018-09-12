# Example Bucket Policies for VPC Endpoints for Amazon S3<a name="example-bucket-policies-vpc-endpoint"></a>

You can use Amazon S3 bucket policies to control access to buckets from specific Amazon Virtual Private Cloud \(Amazon VPC\) endpoints, or specific VPCs\. This section contains example bucket policies that can be used to control S3 bucket access from VPC endpoints\. To learn how to set up VPC endpoints, see [VPC Endpoints](http://docs.aws.amazon.com/AmazonVPC/latest/UserGuide/vpc-endpoints.html) in the *Amazon VPC User Guide*\. 

Amazon VPC enables you to launch Amazon Web Services \(AWS\) resources into a virtual network that you define\. A VPC endpoint enables you to create a private connection between your VPC and another AWS service without requiring access over the Internet, through a VPN connection, through a NAT instance, or through AWS Direct Connect\. 

A VPC endpoint for Amazon S3 is a logical entity within a VPC that allows connectivity only to Amazon S3\. The VPC endpoint routes requests to Amazon S3 and routes responses back to the VPC\. VPC endpoints change only how requests are routed\. Amazon S3 public endpoints and DNS names will continue to work with VPC endpoints\. For important information about using Amazon VPC endpoints with Amazon S3, see [Gateway VPC Endpoints](http://docs.aws.amazon.com/AmazonVPC/latest/UserGuide/vpce-gateway.html) and [Endpoints for Amazon S3](http://docs.aws.amazon.com/AmazonVPC/latest/UserGuide/vpc-endpoints-s3.html) in the *Amazon VPC User Guide*\. 

VPC endpoints for Amazon S3 provides two ways to control access to your Amazon S3 data: 
+ You can control the requests, users, or groups that are allowed through a specific VPC endpoint\. For information on this type of access control, see [Controlling Access to Services with VPC Endpoints](http://docs.aws.amazon.com/AmazonVPC/latest/UserGuide/vpc-endpoints-access.html) in the *Amazon VPC User Guide*\.
+ You can control which VPCs or VPC endpoints have access to your S3 buckets by using S3 bucket policies\. For examples of this type of bucket policy access control, see the following topics on restricting access\.

**Topics**
+ [Restricting Access to a Specific VPC Endpoint](#example-bucket-policies-restrict-accesss-vpc-endpoint)
+ [Restricting Access to a Specific VPC](#example-bucket-policies-restrict-access-vpc)
+ [Related Resources](#example-bucket-policies-restrict-access-vpc-related-resources)

**Important**  
When applying the S3 bucket polices for VPC endpoints described in this section, you might block your access to the bucket without intending to do so\. Bucket permissions intended to specifically limit bucket access to connections originating from your VPC endpoint can block all connections to the bucket\. For information about how to fix this issue, see [How do I regain access to an Amazon S3 bucket after applying a policy to the bucket that restricts access to my VPC endpoint?](https://aws.amazon.com/premiumsupport/knowledge-center/s3-regain-access/) in the *AWS Support Knowledge Center*\.

## Restricting Access to a Specific VPC Endpoint<a name="example-bucket-policies-restrict-accesss-vpc-endpoint"></a>

The following is an example of an S3 bucket policy that restricts access to a specific bucket, `examplebucket`, only from the VPC endpoint with the ID `vpce-1a2b3c4d`\.  The policy denies all access to the bucket if the specified endpoint is not being used\. The  `aws:sourceVpce` condition  is used to the specify the endpoint\. The `aws:sourceVpce` condition does not require an ARN for the VPC endpoint resource, only the VPC endpoint ID\. For more information about using conditions in a policy, see [Specifying Conditions in a Policy](amazon-s3-policy-keys.md)\.

```
{
   "Version": "2012-10-17",
   "Id": "Policy1415115909152",
   "Statement": [
     {
       "Sid": "Access-to-specific-VPCE-only",
       "Principal": "*",
       "Action": "s3:*",
       "Effect": "Deny",
       "Resource": ["arn:aws:s3:::examplebucket",
                    "arn:aws:s3:::examplebucket/*"],
       "Condition": {
         "StringNotEquals": {
           "aws:sourceVpce": "vpce-1a2b3c4d"
         }
       }
     }
   ]
}
```

## Restricting Access to a Specific VPC<a name="example-bucket-policies-restrict-access-vpc"></a>

You can create a bucket policy that restricts access to a specific VPC by using the `aws:sourceVpc` condition\. This is useful if you have multiple VPC endpoints configured in the same VPC, and you want to manage access to your S3 buckets for all of your endpoints\. The following is an example of a policy that allows VPC `vpc-111bbb22` to access `examplebucket` and its objects\. The policy denies all access to the bucket if the specified VPC is not being used\. The `vpc-111bbb22` condition key does not require an ARN for the VPC resource, only the VPC ID\.

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
10.        "Resource": ["arn:aws:s3:::examplebucket",
11.                     "arn:aws:s3:::examplebucket/*"],
12.        "Condition": {
13.          "StringNotEquals": {
14.            "aws:sourceVpc": "vpc-111bbb22"
15.          }
16.        }
17.      }
18.    ]
19. }
```

## Related Resources<a name="example-bucket-policies-restrict-access-vpc-related-resources"></a>
+ [Bucket Policy Examples](example-bucket-policies.md)
+ [VPC Endpoints](http://docs.aws.amazon.com/AmazonVPC/latest/UserGuide/vpc-endpoints.html) in the *Amazon VPC User Guide*
+ [How do I regain access to an Amazon S3 bucket after applying a policy to the bucket that restricts access to my VPC endpoint?](https://aws.amazon.com/premiumsupport/knowledge-center/s3-regain-access/)