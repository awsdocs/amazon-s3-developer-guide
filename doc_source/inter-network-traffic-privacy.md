# Internetwork traffic privacy<a name="inter-network-traffic-privacy"></a>

This topic describes how Amazon S3 secures connections from the service to other locations\.

## Traffic between service and on\-premises clients and applications<a name="inter-network-traffic-privacy-on-prem"></a>

You have two connectivity options between your private network and AWS: 
+ An AWS Site\-to\-Site VPN connection\. For more information, see [What is AWS Site\-to\-Site VPN?](https://docs.aws.amazon.com/vpn/latest/s2svpn/VPC_VPN.html)
+ An AWS Direct Connect connection\. For more information, see [What is AWS Direct Connect?](https://docs.aws.amazon.com/directconnect/latest/UserGuide/Welcome.html)

Access to Amazon S3 via the network is through AWS published APIs\. Clients must support Transport Layer Security \(TLS\) 1\.0\. We recommend TLS 1\.2 or above\. Clients must also support cipher suites with Perfect Forward Secrecy \(PFS\), such as Ephemeral Diffie\-Hellman \(DHE\) or Elliptic Curve Diffie\-Hellman Ephemeral \(ECDHE\)\. Most modern systems such as Java 7 and later support these modes\. Additionally, you must sign requests using an access key ID and a secret access key that are associated with an IAM principal, or you can use the [AWS Security Token Service \(STS\)](https://docs.aws.amazon.com/STS/latest/APIReference/Welcome.html) to generate temporary security credentials to sign requests\.

## Traffic between AWS resources in the same Region<a name="inter-network-traffic-privacy-within-region"></a>

An Amazon Virtual Private Cloud \(Amazon VPC\) endpoint for Amazon S3 is a logical entity within a VPC that allows connectivity only to Amazon S3\. The Amazon VPC routes requests to Amazon S3 and routes responses back to the VPC\. For more information, see [VPC Endpoints](https://docs.aws.amazon.com/vpc/latest/userguide/vpc-endpoints.html) in the *Amazon VPC User Guide*\. For example bucket policies that you can use to control S3 bucket access from VPC endpoints, see [Example Bucket Policies for VPC Endpoints for Amazon S3](example-bucket-policies-vpc-endpoint.md)\. 