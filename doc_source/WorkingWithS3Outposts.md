# Working with Amazon S3 on Outposts<a name="WorkingWithS3Outposts"></a>

Amazon S3 supports global buckets, the bucket name needs to be unique across all Regions within a partition\. You can access a bucket using just its name\. In S3 on Outposts, bucket names are unique to an Outpost and require the Outpost\-id along with the bucket name to identify them\. 

**Topics**
+ [Accessing Amazon S3 on Outposts](#AccessingS3Outposts)

Access Points simplify managing data access at scale for shared datasets in S3\. Access points are named network endpoints that are attached to buckets that you can use to perform Amazon S3 object operations, such as GetObject and PutObject\. With S3 on Outposts bucket endpoint and object API endpoint being different, you must use access points to access any object in a Outposts bucket, unlike S3 buckets in S3 that can be accessed directly\. Access points only support virtual\-host\-style addressing\. The ARN format for S3 on Outposts buckets will be: `arn:aws:s3-outposts:<region>:<account>:outpost/<outpost-id>/bucket/<bucket-name>`\. The ARN format for S3 on Outposts access points will be: `arn:aws:s3-outposts:<region>:<account>:outpost/<outpost-id>/accesspoint/<accesspoint-name>`\.

The existing bucket management APIs do not support the concept of location beyond Regions\. Thus, these APIs cannot be used to create and manage buckets that are scoped to account, Outposts and Region\. S3 on Outposts will host a separate endpoint for management of Outposts bucket APIs distinct from S3\. This will be `s3-outposts.<region>.amazonaws.com`\. You must create these endpoints to be able to access your Outposts bucket and perform object operations\. This will also allow the API model and behaviors to be the same by allowing the same actions to work in S3 and S3 on Outposts\. This is done by signing the bucket and objects using the correct ARNs\.

The reason to pass ARNs for the API is to Amazon S3 to determine if the request is for S3 \(s3\-control\.<region>\.amazonaws\.com\) or S3 on Outposts \(s3\-outposts\.<region>\.amazonaws\.com\) allowing it sign and route the request appropriately\. Whenever the request is sent to the Amazon S3 control plane, the SDK will extract the components from the ARN and include an additional header “x\-amz\-outpost\-id” with the value of the “outpost\-id” extracted from the ARN\. The service name from the ARN will be used to sign the request before it is routed to the S3 on Outposts endpoint\. This is applicable for all APIs handled by the s3control client\. 



The list of extended APIs for Amazon S3 on Outposts and their changes relative to S3 is in the table below\.


|  API |  S3 parameter value |  S3 on Outposts parameter value | 
| --- | --- | --- | 
|  *CreateBucket*  |  *bucket name*  |  *name as ARN, outpost\-id*  | 
|  *ListRegionalBuckets \(new API\)*  |  *NA*  |  *outpost\-id*  | 
|  *DeleteBucket*  |  *bucket name*  |  *name as ARN*  | 
|  *DeleteBucketLifecycleConfiguration*  |  *bucket name*  |  *bucket name as ARN*  | 
|  *GetBucketLifecycleConfiguration*  |  *bucket name*  |  *bucket name as ARN*  | 
|  * PutBucketLifecycleConfiguration*  |  *bucket name*  |  *bucket name as ARN*  | 
|  *GetBucketPolicy*  |  *bucket name*  |  *bucket name as ARN*  | 
|  *PutBucketPolicy*  |  *bucket name*  |  *bucket name as ARN*  | 
|  *DeleteBucketPolicy*  |  *bucket name*  |  *bucket name as ARN*  | 
|  *GetBucketTagging*  |  *bucket name*  |  *bucket name as ARN*  | 
|  *PutBucketTagging*  |  *bucket name*  |  *bucket name as ARN*  | 
|  *DeleteBucketTagging*  |  *bucket name*  |  *bucket name as ARN*  | 
|  *CreateAccessPoint*  |  *access point name*  |  *access point name as ARN*  | 
|  *DeleteAccessPoint*  |  *access point name*  |  *access point name as ARN*  | 
|  *GetAccessPoint*  |  *access point name*  |  *access point name as ARN*  | 
|  *GetAccessPoint*  |  *access point name*  |  *access point name as ARN*  | 
|  *ListAccessPoints*  |  *access point name*  |  *access point name as ARN*  | 
|  *PutAccessPointPolicy*  |  *access point name*  |  *access point name as ARN*  | 
|  *GetAccessPointPolicy*  |  *access point name*  |  *access point name as ARN*  | 
|  *DeleteAccessPointPolicy*  |  *access point name*  |  *access point name as ARN*  | 



## Accessing Amazon S3 on Outposts<a name="AccessingS3Outposts"></a>

Amazon S3 on Outposts supports VPC\-only access points as the only means to access Outposts buckets\. S3 on Outposts endpoints enable you to privately connect your VPC to your Outposts bucket without requiring an internet gateway, NAT device, VPN connection, or AWS Direct Connect connection\. Instances in your VPC do not require public IP addresses to communicate with resources in your Outposts keeping traffic between your VPC and your S3 on Outposts buckets within the Amazon network\. S3 on Outposts endpoints are virtual devices\. They are horizontally scaled, redundant, and highly available VPC components\. They allow communication between instances in your VPC and S3 on Outposts without imposing availability risks or bandwidth constraints on your network traffic\. 

**Note**  
You will not be able to access your S3 on Outpost bucket and objects until:  
The access point is created for the VPC\.
An Endpoint exists for the same VPC\.

**Topics**
+ [Connection management for Amazon S3 on Outposts through Cross\-account elastic network interfaces](#S3OutpostsXENI)
+ [Permissions required for endpoints](#S3OutpostsClusters)
+ [Encryption options with Amazon S3 on Outposts](#S3OutpostsEncryption)
+ [Managing capacity on S3 on Outposts](#S3OutpostsCapacity)

### Connection management for Amazon S3 on Outposts through Cross\-account elastic network interfaces<a name="S3OutpostsXENI"></a>

S3 on Outposts endpoints will be named resources with proper ARNs\. During creation of these endpoints, Outposts will setup four cross\-account Elastic Network Interfaces \(X\-ENI\)\. X\-ENI are like other ENI with one exception: S3 on Outposts attaches this X\-ENI to instances it runs in the service account and has a presence in your VPC\. S3 on Outposts will DNS load balance your requests over the X\-ENI\. S3 on Outposts creates the X\-ENI in your account that is visible to from the ENI console\. 

### Permissions required for endpoints<a name="S3OutpostsClusters"></a>

To attach the X\-ENI to cluster accounts, S3 on Amazon will additionally need to modify the cross\-account Elastic Network Interfaces \(X\-ENI\) during creation to be used with the account ID of the cluster account\. Due to the CIDR restrictions, each ENI is unique and on a unique IP\. The source VPC for the IP and ENI id will be recorded and will be associated with the cluster id\. 

### Encryption options with Amazon S3 on Outposts<a name="S3OutpostsEncryption"></a>

 By default, all data stored in S3 on Outposts is encrypted using server\-side encryption with SSE\-S3\. You can optionally use server\-side encryption with customer\-provided encryption keys \(SSE\-C\) by specifying an encryption key as part of your object API requests\. Server\-side encryption encrypts only the object data, not object metadata\. 

### Managing capacity on S3 on Outposts<a name="S3OutpostsCapacity"></a>

If there is not enough space to store an object on your Outpost, the API will return an insufficient capacity exemption \(ICE\)\. To avoid this, you can create [CloudWatch alerts](https://docs.aws.amazon.com/AmazonS3/latest/dev/cloudwatch-monitoring.html#s3-outposts-cloudwatch-metrics) that alert you when storage utilization exceeds a threshold\. You can use this to free up space by explicitly deleting data, using a lifecycle expiration policy, or copying data from your Outposts bucket to an S3 bucket in an AWS Region using AWS DataSync\. For more information about transferring data from your S3 on Outposts buckets using DataSync, see [Getting Started with AWS DataSync](https://docs.aws.amazon.com/datasync/latest/userguide/getting-started.html) in the *AWS DataSync User Guide* 