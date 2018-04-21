# How Amazon S3 Authorizes a Request<a name="how-s3-evaluates-access-control"></a>

**Topics**
+ [Related Topics](#access-control-how-s3-evaluates-related-topics)
+ [How Amazon S3 Authorizes a Request for a Bucket Operation](access-control-auth-workflow-bucket-operation.md)
+ [How Amazon S3 Authorizes a Request for an Object Operation](access-control-auth-workflow-object-operation.md)

When Amazon S3 receives a request—for example, a bucket or an object operation—it first verifies that the requester has the necessary permissions\. Amazon S3 evaluates all the relevant access policies, user policies, and resource\-based policies \(bucket policy, bucket ACL, object ACL\) in deciding whether to authorize the request\. The following are some of the example scenarios: 
+  If the requester is an IAM user, Amazon S3 must determine if the parent AWS account to which the user belongs has granted the user necessary permission to perform the operation\. In addition, if the request is for a bucket operation, such as a request to list the bucket content, Amazon S3 must verify that the bucket owner has granted permission for the requester to perform the operation\. 
**Note**  
To perform a specific operation on a resource, an IAM user needs permission from both the parent AWS account to which it belongs and the AWS account that owns the resource\.
+ If the request is for an operation on an object that the bucket owner does not own, in addition to making sure the requester has permissions from the object owner, Amazon S3 must also check the bucket policy to ensure the bucket owner has not set explicit deny on the object\. 
**Note**  
 A bucket owner \(who pays the bill\) can explicitly deny access to objects in the bucket regardless of who owns it\. The bucket owner can also delete any object in the bucket

In order to determine whether the requester has permission to perform the specific operation, Amazon S3 does the following, in order, when it receives a request:

1. Converts all the relevant access policies \(user policy, bucket policy, ACLs\) at run time into a set of policies for evaluation\.

1. Evaluates the resulting set of policies in the following steps\. In each step, Amazon S3 evaluates a subset of policies in a specific context, based on the context authority\. 

   1. **User context** – In the user context, the parent account to which the user belongs is the context authority\.

      Amazon S3 evaluates a subset of policies owned by the parent account\. This subset includes the user policy that the parent attaches to the user\. If the parent also owns the resource in the request \(bucket, object\), Amazon S3 also evaluates the corresponding resource policies \(bucket policy, bucket ACL, and object ACL\) at the same time\. 

      A user must have permission from the parent account to perform the operation\.

      This step applies only if the request is made by a user in an AWS account\. If the request is made using root credentials of an AWS account, Amazon S3 skips this step\.

   1. **Bucket context** – In the bucket context, Amazon S3 evaluates policies owned by the AWS account that owns the bucket\. 

      If the request is for a bucket operation, the requester must have permission from the bucket owner\. If the request is for an object, Amazon S3 evaluates all the policies owned by the bucket owner to check if the bucket owner has not explicitly denied access to the object\. If there is an explicit deny set, Amazon S3 does not authorize the request\. 

   1. **Object context** – If the request is for an object, Amazon S3 evaluates the subset of policies owned by the object owner\. 

 The following sections describe in detail and provide examples:
+ [How Amazon S3 Authorizes a Request for a Bucket Operation ](access-control-auth-workflow-bucket-operation.md)
+ [How Amazon S3 Authorizes a Request for an Object Operation ](access-control-auth-workflow-object-operation.md)

## Related Topics<a name="access-control-how-s3-evaluates-related-topics"></a>

 We recommend you first review the introductory topics that explain the options for managing access to your Amazon S3 resources\. For more information, see [Introduction to Managing Access Permissions to Your Amazon S3 Resources](intro-managing-access-s3-resources.md)\. You can then use the following topics for more information about specific access policy options\. 
+  [Using Bucket Policies and User Policies](using-iam-policies.md) 
+  [Managing Access with ACLs](S3_ACLs_UsingACLs.md) 