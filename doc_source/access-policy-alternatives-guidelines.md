# Guidelines for Using the Available Access Policy Options<a name="access-policy-alternatives-guidelines"></a>

Amazon S3 supports resource\-based policies and user policies to manage access to your Amazon S3 resources \(see [Managing Access to Resources \(Access Policy Options\)](access-control-overview.md#access-control-resources-manage-permissions-basics)\)\. Resource\-based policies include bucket policies, bucket ACLs, and object ACLs\. This section describes specific scenarios for using resource\-based access policies to manage access to your Amazon S3 resources\. 

## When to Use an ACL\-based Access Policy \(Bucket and Object ACLs\)<a name="when-to-use-acl"></a>

Both buckets and objects have associated ACLs that you can use to grant permissions\. The following sections describe scenarios for using object ACLs and bucket ACLs\.

### When to Use an Object ACL<a name="when-to-use-object-acl"></a>

In addition to an object ACL, there are other ways an object owner can manage object permissions\. For example:
+ If the AWS account that owns the object also owns the bucket, then it can write a bucket policy to manage the object permissions\.
+ If the AWS account that owns the object wants to grant permission to a user in its account, it can use a user policy\.

So when do you use object ACLs to manage object permissions? The following are the scenarios when you use object ACLs to manage object permissions\.
+ **An object ACL is the only way to manage access to objects not owned by the bucket owner** – An AWS account that owns the bucket can grant another AWS account permission to upload objects\. The bucket owner does not own these objects\. The AWS account that created the object must grant permissions using object ACLs\. 
**Note**  
A bucket owner cannot grant permissions on objects it does not own\. For example, a bucket policy granting object permissions applies only to objects owned by the bucket owner\. However, the bucket owner, who pays the bills, can write a bucket policy to deny access to any objects in the bucket, regardless of who owns it\. The bucket owner can also delete any objects in the bucket\. 
+ **Permissions vary by object and you need to manage permissions at the object level** – You can write a single policy statement granting an AWS account read permission on millions of objects with a specific key name prefix\. For example, grant read permission on objects starting with key name prefix "logs"\. However, if your access permissions vary by object, granting permissions to individual objects using a bucket policy may not be practical\. Also the bucket policies are limited to 20 KB in size\. 

  In this case, you may find using object ACLs a suitable alternative\. Although, even an object ACL is also limited to a maximum of 100 grants \(see [Access Control List \(ACL\) Overview](acl-overview.md)\)\. 
+ **Object ACLs control only object\-level permissions** –  There is a single bucket policy for the entire bucket, but object ACLs are specified per object\.

  An AWS account that owns a bucket can grant another AWS account permission to manage access policy\. It allows that account to change anything in the policy\. To better manage permissions, you may choose not to give such a broad permission, and instead grant only the READ\-ACP and WRITE\-ACP permissions on a subset of objects\. This limits the account to manage permissions only on specific objects by updating individual object ACLs\.

### When to Use a Bucket ACL<a name="when-to-use-bucket-acl"></a>

The only recommended use case for the bucket ACL is to grant write permission to the Amazon S3 Log Delivery group to write access log objects to your bucket \(see [Amazon S3 Server Access Logging](ServerLogs.md)\)\. If you want Amazon S3 to deliver access logs to your bucket, you will need to grant write permission on the bucket to the Log Delivery group\. The only way you can grant necessary permissions to the Log Delivery group is via a bucket ACL, as shown in the following bucket ACL fragment\.

```
<?xml version="1.0" encoding="UTF-8"?>
<AccessControlPolicy xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Owner>
   ...
  </Owner>
  <AccessControlList>
    <Grant>
        ...
    </Grant>  
    <Grant>
       <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="Group">
        <URI>http://acs.amazonaws.com/groups/s3/LogDelivery</URI>
       </Grantee>
       <Permission>WRITE</Permission>
    </Grant>  
  </AccessControlList>
</AccessControlPolicy>
```

## When to Use a Bucket Policy<a name="when-to-use-bucket-policy"></a>

If an AWS account that owns a bucket wants to grant permission to users in its account, it can use either a bucket policy or a user policy\. But in the following scenarios, you will need to use a bucket policy\.
+ **You want to manage cross\-account permissions for all Amazon S3 permissions** – You can use ACLs to grant cross\-account permissions to other accounts, but ACLs support only a finite set of permission \([What Permissions Can I Grant?](acl-overview.md#permissions)\), these don't include all Amazon S3 permissions\. For example, you cannot grant permissions on bucket subresources \(see [Managing Access Permissions to Your Amazon S3 Resources](s3-access-control.md)\) using an ACL\. 

  Although both bucket and user policies support granting permission for all Amazon S3 operations \(see [Specifying Permissions in a Policy](using-with-s3-actions.md)\), the user policies are for managing permissions for users in your account\. For cross\-account permissions to other AWS accounts or users in another account, you must use a bucket policy\.

## When to Use a User Policy<a name="when-to-use-user-policy"></a>

In general, you can use either a user policy or a bucket policy to manage permissions\. You may choose to manage permissions by creating users and managing permissions individually by attaching policies to users \(or user groups\), or you may find that resource\-based policies, such as a bucket policy, work better for your scenario\.

Note that AWS Identity and Access Management \(IAM\) enables you to create multiple users within your AWS account and manage their permissions via user policies\. An IAM user must have permissions from the parent account to which it belongs, and from the AWS account that owns the resource the user wants to access\. The permissions can be granted as follows:
+ **Permission from the parent account** – The parent account can grant permissions to its user by attaching a user policy\.
+ **Permission from the resource owner** – The resource owner can grant permission to either the IAM user \(using a bucket policy\) or the parent account \(using a bucket policy, bucket ACL, or object ACL\)\.

This is akin to a child who wants to play with a toy that belongs to someone else\. In this case, the child must get permission from a parent to play with the toy and permission from the toy owner\.

### Permission Delegation<a name="permission-delegation"></a>

If an AWS account owns a resource, it can grant those permissions to another AWS account\. That account can then delegate those permissions, or a subset of them, to users in the account\. This is referred to as permission delegation\. But an account that receives permissions from another account cannot delegate permission cross\-account to another AWS account\. 

## Related Topics<a name="access-control-guidelines-related-topics"></a>

 We recommend you first review all introductory topics that explain how you manage access to your Amazon S3 resources and related guidelines\. For more information, see [Introduction to Managing Access Permissions to Your Amazon S3 Resources](intro-managing-access-s3-resources.md)\. You can then use the following topics for more information about specific access policy options\. 
+ [Using Bucket Policies and User Policies](using-iam-policies.md)
+ [Managing Access with ACLs](S3_ACLs_UsingACLs.md)