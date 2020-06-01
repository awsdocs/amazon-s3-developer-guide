# Put object ACL<a name="batch-ops-put-object-acl"></a>

The Put Object Acl operation replaces the Amazon S3 access control lists \(ACLs\) for each object that is listed in the manifest\. Using ACLs, you can define who can access an object and what actions they can perform\.

S3 Batch Operations support custom ACLs that you define and canned ACLs that Amazon S3 provides with a predefined set of access permissions\.

If the objects in your manifest are in a versioned bucket, you can apply the ACLs to specific versions of each object\. You do this by specifying a version ID for each object in the manifest\. If you don't include a version ID for any object, then S3 Batch Operations applies the ACL to the latest version of the object\.

**Note**  
If you want to limit public access to all objects in a bucket, you should use Amazon S3 block public access instead of S3 Batch Operations\. Block public access can limit public access on a per\-bucket or account\-wide basis with a single, simple operation that takes effect quickly\. This make it a better choice when your goal is to control public access to all objects in a bucket or account\. Use S3 Batch Operations when you need to apply a customized ACL to each object in the manifest\. For more information about Amazon S3 block public access, see [Using Amazon S3 block public access](access-control-block-public-access.md)\.

## Restrictions and limitations<a name="batch-ops-put-object-acl-restrictions"></a>
+ The role that you specify to run the Put Object Acl job must have permissions to perform the underlying Amazon S3 PUT Object acl operation\. For more information about the permissions required, see [PUT Object acl](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTacl.html) in the *Amazon Simple Storage Service API Reference*\.
+ S3 Batch Operations use the Amazon S3 PUT Object acl operation to apply the specified ACL to each object in the manifest\. Therefore, all restrictions and limitations that apply to the underlying PUT Object acl operation also apply to S3 Batch Operations Put Object Acl jobs\. For more information, see the [Related resources](#batch-ops-put-object-acl-related-resources) section of this page\.

## Related resources<a name="batch-ops-put-object-acl-related-resources"></a>
+ [Managing Access with ACLs](S3_ACLs_UsingACLs.md)
+ [GET Object ACL](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETacl.html) in the *Amazon Simple Storage Service API Reference*