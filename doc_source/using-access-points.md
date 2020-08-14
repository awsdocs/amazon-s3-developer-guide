# Using access points<a name="using-access-points"></a>

You can access the objects in an Amazon S3 bucket with an *access point* using the AWS Management Console, AWS CLI, AWS SDKs, or the S3 REST APIs\.

Access points have Amazon Resource Names \(ARNs\)\. Access point ARNs are similar to bucket ARNs, but they are explicitly typed and encode the access point's Region and the AWS account ID of the access point's owner\. For more information about ARNs, see [Amazon Resource Names \(ARNs\)](https://docs.aws.amazon.com/general/latest/gr/aws-arns-and-namespaces.html) in the *AWS General Reference*\.

Access point ARNs use the format `arn:aws:s3:region:account-id:accesspoint/resource`\. For example:
+ **arn:aws:s3:us\-west\-2:123456789012:accesspoint/test** represents the access point named `test`, owned by account `123456789012` in Region `us-west-2`\.
+ **arn:aws:s3:us\-west\-2:123456789012:accesspoint/\*** represents all access points under account `123456789012` in Region `us-west-2`\.

ARNs for objects accessed through an access point use the format `arn:aws:s3:region:account-id:accesspoint/access-point-name/object/resource`\. For example:
+ **arn:aws:s3:us\-west\-2:123456789012:accesspoint/test/object/unit\-01** represents the object `unit-01`, accessed through the access point named `test`, owned by account `123456789012` in Region `us-west-2`\.
+ **arn:aws:s3:us\-west\-2:123456789012:accesspoint/test/object/\*** represents all objects for access point `test`, in account `123456789012` in Region `us-west-2`\.
+ **arn:aws:s3:us\-west\-2:123456789012:accesspoint/test/object/unit\-01/finance/\*** represents all objects under prefix `unit-01/finance/` for access point `test`, in account `123456789012` in Region `us-west-2`\.

## Access point compatibility with S3 operations and AWS services<a name="access-points-service-api-support"></a>

Access points in Amazon S3 are compatible with a subset of S3 operations and other AWS services\. The following sections list the compatible services and S3 operations\.

**AWS Services**

You can use S3 Access Points with AWS CloudFormation\.

For more information about AWS CloudFormation, see [What is AWS CloudFormation?](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/Welcome.html) in the *AWS CloudFormation User Guide*\.

**S3 operations**

You can use access points to access a bucket using the following subset of Amazon S3 APIs:
+ `[AbortMultipartUpload](https://docs.aws.amazon.com/AmazonS3/latest/API/API_AbortMultipartUpload.html)`
+ `[CompleteMultipartUpload](https://docs.aws.amazon.com/AmazonS3/latest/API/API_CompleteMultipartUpload.html)`
+ `[CopyObject](https://docs.aws.amazon.com/AmazonS3/latest/API/API_CopyObject.html)` \(same\-region copies only\)
+ `[CreateMultipartUpload](https://docs.aws.amazon.com/AmazonS3/latest/API/API_CreateMultipartUpload.html)`
+ `[DeleteObject](https://docs.aws.amazon.com/AmazonS3/latest/API/API_DeleteObject.html)`
+ `[DeleteObjectTagging](https://docs.aws.amazon.com/AmazonS3/latest/API/API_DeleteObjectTagging.html)`
+ `[GetObject](https://docs.aws.amazon.com/AmazonS3/latest/API/API_GetObject.html)`
+ `[GetObjectAcl](https://docs.aws.amazon.com/AmazonS3/latest/API/API_GetObjectAcl.html)`
+ `[GetObjectLegalHold](https://docs.aws.amazon.com/AmazonS3/latest/API/API_GetObjectLegalHold.html)`
+ `[GetObjectRetention](https://docs.aws.amazon.com/AmazonS3/latest/API/API_GetObjectRetention.html)`
+ `[GetObjectTagging](https://docs.aws.amazon.com/AmazonS3/latest/API/API_GetObjectTagging.html)`
+ `[HeadObject](https://docs.aws.amazon.com/AmazonS3/latest/API/API_HeadObject.html)`
+ `[ListMultipartUploads](https://docs.aws.amazon.com/AmazonS3/latest/API/API_ListMultipartUploads.html)`
+ `[ListObjectsV2](https://docs.aws.amazon.com/AmazonS3/latest/API/API_ListObjectsV2.html)`
+ `[ListParts](https://docs.aws.amazon.com/AmazonS3/latest/API/API_ListParts.html)`
+ `[PutObject](https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutObject.html)`
+ `[PutObjectLegalHold](https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutObjectLegalHold.html)`
+ `[PutObjectRetention](https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutObjectRetention.html)`
+ `[PutObjectAcl](https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutObjectAcl.html)`
+ `[PutObjectTagging](https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutObjectTagging.html)`
+ `[RestoreObject](https://docs.aws.amazon.com/AmazonS3/latest/API/API_RestoreObject.html)`
+ `[UploadPart](https://docs.aws.amazon.com/AmazonS3/latest/API/API_UploadPart.html)`
+ `[UploadPartCopy](https://docs.aws.amazon.com/AmazonS3/latest/API/API_UploadPartCopy.html)` \(same\-region copies only\)

## Monitoring and logging<a name="access-points-monitoring-logging"></a>

Amazon S3 logs requests made through access points and requests made to the APIs that manage access points, such as `CreateAccessPoint` and `GetAccessPointPolicy`\.

Requests made to Amazon S3 through an access point appear in your S3 server access logs and AWS CloudTrail logs with the access point's hostname\. An access point's hostname takes the form `access_point_name-account_id.s3-accesspoint.Region.amazonaws.com`\. For example, suppose that you have the following bucket and access point configuration:
+ A bucket named `my-bucket` in Region `us-west-2` that contains object `my-image.jpg`
+ An access point named `my-bucket-ap` that is associated with `my-bucket`
+ Your AWS account ID is `123456789012`

A request made to retrieve `my-image.jpg` directly through the bucket appears in your logs with a hostname of `my-bucket.s3.us-west-2.amazonaws.com`\. If you make the request through the access point instead, Amazon S3 retrieves the same object but logs the request with a hostname of `my-bucket-ap-123456789012.s3-accesspoint.us-west-2.amazonaws.com`\.

For more information about S3 Server Access Logs, see [Amazon S3 server access logging](ServerLogs.md)\. For more information about AWS CloudTrail, see [What is AWS CloudTrail?](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-user-guide.html) in the *AWS CloudTrail User Guide*\.

**Note**  
S3 access points aren't currently compatible with Amazon CloudWatch metrics\.

## Examples<a name="access-points-usage-examples"></a>

The following examples demonstrate how to use access points with compatible operations in Amazon S3\.

**Example**  
***Example: Request an object through an access point***  
The following example requests the object `my-image.jpg` through the access point `prod` owned by account ID `123456789012` in Region `us-west-2`, and saves the downloaded file as `download.jpg`\.  

```
aws s3api get-object --key my-image.jpg --bucket arn:aws:s3:us-west-2:123456789012:accesspoint/prod download.jpg
```

**Example**  
***Example: Upload an object through an access point***  
The following example uploads the object `my-image.jpg` through the access point `prod` owned by account ID `123456789012` in Region `us-west-2`\.  

```
aws s3api put-object --bucket arn:aws:s3:us-west-2:123456789012:accesspoint/prod --key my-image.jpg --body my-image.jpg
```

**Example**  
***Example: Delete an object through an access point***  
The following example deletes the object `my-image.jpg` through the access point `prod` owned by account ID `123456789012` in Region `us-west-2`\.  

```
aws s3api delete-object --bucket arn:aws:s3:us-west-2:123456789012:accesspoint/prod --key my-image.jpg
```

**Example**  
***Example: List objects through an access point***  
The following example lists objects through the access point `prod` owned by account ID `123456789012` in Region `us-west-2`\.  

```
aws s3api list-objects-v2 --bucket arn:aws:s3:us-west-2:123456789012:accesspoint/prod
```

**Example**  
***Example: Add a tag set to an object through an access point***  
The following example adds a tag set to the existing object `my-image.jpg` through the access point `prod` owned by account ID `123456789012` in Region `us-west-2`\.  

```
aws s3api put-object-tagging --bucket arn:aws:s3:us-west-2:123456789012:accesspoint/prod --key my-image.jpg --tagging TagSet=[{Key="finance",Value="true"}]
```

**Example**  
***Example: Grant access permissions through an access point using an ACL***  
The following example applies an ACL to an existing object `my-image.jpg` through the access point `prod` owned by account ID `123456789012` in Region `us-west-2`\.  

```
aws s3api put-object-acl --bucket arn:aws:s3:us-west-2:123456789012:accesspoint/prod --key my-image.jpg --acl private
```