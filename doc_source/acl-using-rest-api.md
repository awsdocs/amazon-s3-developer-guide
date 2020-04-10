# Managing ACLs Using the REST API<a name="acl-using-rest-api"></a>

Amazon S3 APIs enable you to set an ACL when you create a bucket or an object\. Amazon S3 also provides API to set an ACL on an existing bucket or an object\. These APIs provide the following methods to set an ACL:
+ **Set ACL using request headers—** When you send a request to create a resource \(bucket or object\), you set an ACL using the request headers\. Using these headers, you can either specify a canned ACL or specify grants explicitly \(identifying grantee and permissions explicitly\)\. 
+ **Set ACL using request body—** When you send a request to set an ACL on an existing resource, you can set the ACL either in the request header or in the body\. 

For information on the REST API support for managing ACLs, see the following sections in the *Amazon Simple Storage Service API Reference*:
+  [GET Bucket acl](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETacl.html) 
+  [PUT Bucket acl](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTacl.html) 
+  [GET Object acl](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETacl.html) 
+  [PUT Object acl](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTacl.html) 
+  [PUT Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html) 
+  [PUT Bucket](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUT.html) 
+  [PUT Object \- Copy](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html) 
+  [Initiate Multipart Upload](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html) 

## Access Control List \(ACL\)\-Specific Request Headers<a name="acl-headers-rest-api"></a>

You can use headers to grant access control list \(ACL\) based permissons\. By default, all objects are private\. Only the owner has full access control\. When adding a new object, you can grant permissions to individual AWS accounts or to predefined groups defined by Amazon S3\. These permissions are then added to the Access Control List \(ACL\) on the object\. For more information, see [Using ACLs](https://docs.aws.amazon.com/AmazonS3/latest/dev/S3_ACLs_UsingACLs.html)\. 

With this operation, you can grant access permissions using one these two methods:
+ **Canned ACL \(`x-amz-acl`\)** — Amazon S3 supports a set of predefined ACLs, known as canned ACLs\. Each canned ACL has a predefined set of grantees and permissions\. For more information, see [Canned ACL](acl-overview.md#canned-acl)\.
+ **Access Permissions** — To explicitly grant access permissions to specific AWS accounts or groups, use the following headers\. Each header maps to specific permissions that Amazon S3 supports in an ACL\. For more information, see [Access Control List \(ACL\) Overview](acl-overview.md)\. In the header, you specify a list of grantees who get the specific permission\. 
  + x\-amz\-grant\-read
  + x\-amz\-grant\-write
  + x\-amz\-grant\-read\-acp
  + x\-amz\-grant\-write\-acp
  + x\-amz\-grant\-full\-control