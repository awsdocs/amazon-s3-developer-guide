# Using AWS Identity and Access Management with Amazon S3 on Outposts<a name="S3OutpostsIAM"></a>

AWS Identity and Access Management \(IAM\) is an AWS service that administrators can use to securely control access to AWS Outposts resources\. To allow IAM users to manage AWS Outposts resources, you create an IAM policy that explicitly grants them permissions\.You then attach the policy to the IAM users or groups that require those permissions\. For more information, see [Identity and Access Management for AWS Outposts](https://docs.aws.amazon.com/outposts/latest/userguide/identity-access-management.html) in the *AWS Outposts User Guide*\. 

Amazon S3 on Outposts supports both bucket and access point policies\. S3 on Outposts policies use a different IAM actions namespace from S3 \(`s3-outposts:*` vs\. `s3:*`\) to provide you with distinct controls for data stored on your Outpost\.

Requests made to S3 on Outposts control API in an AWS Region are authenticated using IAM and authorized against the `s3-outposts:*` IAM namespace\. Requests made to the object API endpoints on the Outpost are authenticated\.

 Configure your IAM users and authorize them against the `s3-outposts:*` IAM namespace\. Access point policies that are configured on the Outpost access point control authorization of object API requests in addition to IAM user policies\.

**Note**  
S3 on Outposts defaults to the bucket owner as object owner, to help ensure that the owner of a bucket can't be prevented from accessing or deleting objects\.
S3 on Outposts always has S3 Block Public Access enabled to help ensure that objects can never have public access\.
S3 on Outposts uses the service prefix `s3-outposts:<ACTION>`\. For more information, see [Actions, resources, and condition keys for Amazon S3](https://docs.aws.amazon.com/IAM/latest/UserGuide/list_amazons3.html) in the *IAM User Guide*\.

## ARNS for Amazon S3 on Outposts<a name="S3OutpostsARN"></a>

 S3 on Outposts have different Amazon Resource Names \(ARN\) then Amazon S3\. The following is the ARN format for S3 on Outposts buckets\. You must use this ARN format to access and perform actions on your Outposts buckets and objects\.


****  

| Amazon S3 on Outposts ARN | ARN format | Example | 
| --- | --- | --- | 
| Bucket ARN | arn:<partition>:s3\-outposts:<region>:<account\_id>:outpost/<outpost\_id>/bucket/<bucket\_name | arn:aws:s3\-outposts:us\-west\-2:123456789012:outpost/op\-01ac5d28a6a232904/bucket/DOC\-EXAMPLE\-BUCKET1 | 
| accesspoint ARN | arn:<partition>:s3\-outposts:<region>:<account\_id>:outpost/<outpost\_id>/accesspoint/<accesspoint\_name> | arn:aws:s3\-outposts:us\-west\-2:123456789012:outpost/op\-01ac5d28a6a232904/accesspoint/example\-access\-point | 
| Object ARN | arn:<partition>:s3\-outposts:<region>:<account\_id>:outpost/<outpost\_id>/bucket/<bucket\_name>/object/<object\_key> | arn:aws:s3\-outposts:us\-west\-2:123456789012:outpost/op\-01ac5d28a6a232904/bucket/DOC\-EXAMPLE\-BUCKET1/object/myobject | 
| S3 on Outposts AP object ARN \(used in policies\) | arn:<partition>:s3\-outposts:<region>:<account\_id>:outpost/<outpost\_id>/accesspoint/<accesspoint\_name>/object/<object\_key> | arn:aws:s3\-outposts:us\-west\-2:123456789012:outpost/op\-01ac5d28a6a232904/accesspoint/example\-access\-point/object/myobject | 
| S3 on Outposts ARN | arn:<partition>:s3\-outposts:<region>:<account\_id>:outpost/<outpost\_id> | arn:aws:s3\-outposts:us\-west\-2:123456789012:outpost/op\-01ac5d28a6a232904 | 