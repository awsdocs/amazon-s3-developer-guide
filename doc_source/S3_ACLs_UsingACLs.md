# Managing Access with ACLs<a name="S3_ACLs_UsingACLs"></a>

**Topics**
+ [Access Control List \(ACL\) Overview](acl-overview.md)
+ [Managing ACLs](managing-acls.md)

 Access control lists \(ACLs\) are one of the resource\-based access policy options \(see [Overview of Managing Access](access-control-overview.md)\) that you can use to manage access to your buckets and objects\. You can use ACLs to grant basic read/write permissions to other AWS accounts\. There are limits to managing permissions using ACLs\. For example, you can grant permissions only to other AWS accounts; you cannot grant permissions to users in your account\. You cannot grant conditional permissions, nor can you explicitly deny permissions\. ACLs are suitable for specific scenarios\. For example, if a bucket owner allows other AWS accounts to upload objects, permissions to these objects can only be managed using object ACL by the AWS account that owns the object\.

The following introductory topics explain the basic concepts and options that are available for you to manage access to your Amazon S3 resources, and provide guidelines for when to use which access policy options\. 
+ [Introduction to Managing Access Permissions to Your Amazon S3 Resources](intro-managing-access-s3-resources.md)
+ [Guidelines for Using the Available Access Policy Options](access-policy-alternatives-guidelines.md)