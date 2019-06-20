# How Amazon S3 Authorizes a Request for an Object Operation<a name="access-control-auth-workflow-object-operation"></a>

When Amazon S3 receives a request for an object operation, it converts all the relevant permissions—resource\-based  permissions \(object access control list \(ACL\), bucket policy, bucket ACL\) and IAM user policies—into a set of policies to be evaluated at run time\. It then evaluates the resulting set of policies in a series of steps\. In each step, it evaluates a subset of policies in three specific contexts—user context, bucket context, and object context\.

1. **User context** – If the requester is an IAM user, the user must have permission from the parent AWS account to which it belongs\. In this step, Amazon S3 evaluates a subset of policies owned by the parent account \(also referred as the context authority\)\. This subset of policies includes the user policy that the parent attaches to the user\. If the parent also owns the resource in the request \(bucket, object\), Amazon S3 evaluates the corresponding resource policies \(bucket policy, bucket ACL, and object ACL\) at the same time\. 
**Note**  
If the parent AWS account owns the resource \(bucket or object\), it can grant resource permissions to its IAM user by using either the user policy or the resource policy\. 

1. **Bucket context** – In this context, Amazon S3 evaluates policies owned by the AWS account that owns the bucket\.

   If the AWS account that owns the object in the request is not same as the bucket owner, in the bucket context Amazon S3 checks the policies if the bucket owner has explicitly denied access to the object\. If there is an explicit deny set on the object, Amazon S3 does not authorize the request\. 

1. **Object context** – The requester must have permissions from the object owner to perform a specific object operation\. In this step, Amazon S3 evaluates the object ACL\. 
**Note**  
If bucket and object owners are the same, access to the object can be granted in the bucket policy, which is evaluated at the bucket context\. If the owners are different, the object owners must use an object ACL to grant permissions\. If the AWS account that owns the object is also the parent account to which the IAM user belongs, it can configure object permissions in a user policy, which is evaluated at the user context\. For more information about using these access policy alternatives, see [Guidelines for Using the Available Access Policy Options](access-policy-alternatives-guidelines.md)\.

 The following is an illustration of the context\-based evaluation for an object operation\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/AccessControlAuthorizationFlowObjectResource.png)

## Example 1: Object Operation Request<a name="access-control-auth-workflow-object-operation-example1"></a>

In this example, IAM user Jill, whose parent AWS account is 1111\-1111\-1111, sends an object operation request \(for example, Get object\) for an object owned by AWS account 3333\-3333\-3333 in a bucket owned by AWS account 2222\-2222\-2222\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/example50-policy-eval-logic.png)

Jill will need permission from the parent AWS account, the bucket owner, and the object owner\. Amazon S3 evaluates the context as follows:

1. Because the request is from an IAM user, Amazon S3 evaluates the user context to verify that the parent AWS account 1111\-1111\-1111 has given Jill permission to perform the requested operation\. If she has that permission, Amazon S3 evaluates the bucket context\. Otherwise, Amazon S3 denies the request\.

1.  In the bucket context, the bucket owner, AWS account 2222\-2222\-2222, is the context authority\. Amazon S3 evaluates the bucket policy to determine if the bucket owner has explicitly denied Jill access to the object\. 

1. In the object context, the context authority is AWS account 3333\-3333\-3333, the object owner\. Amazon S3 evaluates the object ACL to determine if Jill has permission to access the object\. If she does, Amazon S3 authorizes the request\. 