# Access Control List \(ACL\) Overview<a name="acl-overview"></a>

**Topics**
+ [Who Is a Grantee?](#specifying-grantee)
+ [What Permissions Can I Grant?](#permissions)
+ [Sample ACL](#sample-acl)
+ [Canned ACL](#canned-acl)
+ [How to Specify an ACL](#setting-acls)

Amazon S3 access control lists \(ACLs\) enable you to manage access to buckets and objects\. Each bucket and object has an ACL attached to it as a subresource\. It defines which AWS accounts or groups are granted access and the type of access\. When a request is received against a resource, Amazon S3 checks the corresponding ACL to verify that the requester has the necessary access permissions\. 

When you create a bucket or an object, Amazon S3 creates a default ACL that grants the resource owner full control over the resource\. This is shown in the following sample bucket ACL \(the default object ACL has the same structure\):

**Example**  

```
 1. <?xml version="1.0" encoding="UTF-8"?>
 2. <AccessControlPolicy xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
 3.   <Owner>
 4.     <ID>*** Owner-Canonical-User-ID ***</ID>
 5.     <DisplayName>owner-display-name</DisplayName>
 6.   </Owner>
 7.   <AccessControlList>
 8.     <Grant>
 9.       <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
10.                xsi:type="Canonical User">
11.         <ID>*** Owner-Canonical-User-ID ***</ID>
12.         <DisplayName>display-name</DisplayName>
13.       </Grantee>
14.       <Permission>FULL_CONTROL</Permission>
15.     </Grant>
16.   </AccessControlList>
17. </AccessControlPolicy>
```

The sample ACL includes an `Owner` element that identifies the owner by the AWS account's canonical user ID\. For instructions on finding your canonical user id, see [Finding an AWS Account Canonical User ID](#finding-canonical-id)\. The `Grant` element identifies the grantee \(either an AWS account or a predefined group\) and the permission granted\. This default ACL has one `Grant` element for the owner\. You grant permissions by adding `Grant` elements, with each grant identifying the grantee and the permission\. 

**Note**  
An ACL can have up to 100 grants\.

## Who Is a Grantee?<a name="specifying-grantee"></a>

A grantee can be an AWS account or one of the predefined Amazon S3 groups\. You grant permission to an AWS account using the email address or the canonical user ID\. However, if you provide an email address in your grant request, Amazon S3 finds the canonical user ID for that account and adds it to the ACL\. The resulting ACLs always contain the canonical user ID for the AWS account, not the AWS account's email address\.

**Important**  
Using email addresses to specify a grantee is only supported in the following AWS Regions:  
US East \(N\. Virginia\)
US West \(N\. California\)
US West \(Oregon\)
Asia Pacific \(Singapore\)
Asia Pacific \(Sydney\)
Asia Pacific \(Tokyo\)
EU \(Ireland\)
South America \(São Paulo\)
For a list of all the Amazon S3 supported regions and endpoints, see [Regions and Endpoints](http://docs.aws.amazon.com/general/latest/gr/rande.html#s3_region) in the *AWS General Reference*\.

**Warning**  
When you grant other AWS accounts access to your resources, be aware that the AWS accounts can delegate their permissions to users under their accounts\. This is known as *cross\-account access*\. For information about using cross\-account access, see [ Creating a Role to Delegate Permissions to an IAM User](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-user.html) in the *IAM User Guide*\. 

### Finding an AWS Account Canonical User ID<a name="finding-canonical-id"></a>

The canonical user ID is associated with your AWS account\. It is a long string, such as `79a59df900b949e55d96a1e698fbacedfd6e09d98eacf8f8d5218e7cd47ef2be`\. For information about how to find the canonical user ID for your account, see [Finding Your Account Canonical User ID](http://docs.aws.amazon.com/general/latest/gr/acct-identifiers.html#FindingCanonicalId)\. 

You can also look up the canonical user ID of an AWS account by reading the ACL of a bucket or an object to which the AWS account has access permissions\. When an individual AWS account is granted permissions by a grant request, a grant entry is added to the ACL with the AWS account's canonical user ID\. 

**Note**  
If you make your bucket public \(not recommended\) any unauthenticated user can upload objects to the bucket\. These anonymous users don't have AWS account\. When an anonymous user uploads an object to your bucket Amazon S3 adds a special canonical user ID \(`65a011a29cdf8ec533ec3d1ccaae921c`\) as the object owner in the ACL\.

### Amazon S3 Predefined Groups<a name="specifying-grantee-predefined-groups"></a>

Amazon S3 has a set of predefined groups\. When granting account access to a group, you specify one of our URIs instead of a canonical user ID\. We provide the following predefined groups:
+ **Authenticated Users group** – Represented by `http://acs.amazonaws.com/groups/global/AuthenticatedUsers`\.

  This group represents all AWS accounts\. **Access permission to this group allows any AWS account to access the resource\.** However, all requests must be signed \(authenticated\)\.
**Warning**  
When you grant access to the **Authenticated Users group** any AWS authenticated user in the world can access your resource\.
+ **All Users group** – Represented by `http://acs.amazonaws.com/groups/global/AllUsers`\.

  **Access permission to this group allows anyone in the world access to the resource\.** The requests can be signed \(authenticated\) or unsigned \(anonymous\)\. Unsigned requests omit the Authentication header in the request\.
**Warning**  
We highly recommend that you never grant the **All Users group** `WRITE`, `WRITE_ACP`, or `FULL_CONTROL` permissions\. For example, `WRITE` permissions allow anyone to store objects in your bucket, for which you are billed\. It also allows others to delete objects that you might want to keep\. For more details about these permissions, see the following section [What Permissions Can I Grant?](#permissions)\.
+ **Log Delivery group** – Represented by `http://acs.amazonaws.com/groups/s3/LogDelivery`\.

  WRITE permission on a bucket enables this group to write server access logs \(see [Amazon S3 Server Access Logging](ServerLogs.md)\) to the bucket\.

**Note**  
When using ACLs, a grantee can be an AWS account or one of the predefined Amazon S3 groups\. However, the grantee cannot be an IAM user\. For more information about AWS users and permissions within IAM, go to [Using AWS Identity and Access Management](http://docs.aws.amazon.com/IAM/latest/UserGuide/)\.

## What Permissions Can I Grant?<a name="permissions"></a>

The following table lists the set of permissions that Amazon S3 supports in an ACL\. The set of ACL permissions is the same for an object ACL and a bucket ACL\. However, depending on the context \(bucket ACL or object ACL\), these ACL permissions grant permissions for specific buckets or object operations\. The table lists the permissions and describes what they mean in the context of objects and buckets\. 


| Permission | When granted on a bucket | When granted on an object | 
| --- | --- | --- | 
| READ | Allows grantee to list the objects in the bucket | Allows grantee to read the object data and its metadata | 
| WRITE | Allows grantee to create, overwrite, and delete any object in the bucket | Not applicable | 
| READ\_ACP | Allows grantee to read the bucket ACL | Allows grantee to read the object ACL | 
| WRITE\_ACP | Allows grantee to write the ACL for the applicable bucket | Allows grantee to write the ACL for the applicable object | 
| FULL\_CONTROL | Allows grantee the READ, WRITE, READ\_ACP, and WRITE\_ACP permissions on the bucket | Allows grantee the READ, READ\_ACP, and WRITE\_ACP permissions on the object | 

**Warning**  
Use caution when granting access permissions to your S3 buckets and objects\. For example, granting `WRITE` access to a bucket allows the grantee to create, overwrite, and delete any object in the bucket\. We highly recommend that you read through this entire [Access Control List \(ACL\) Overview](#acl-overview) section before granting permissions\.

### Mapping of ACL Permissions and Access Policy Permissions<a name="acl-access-policy-permission-mapping"></a>

As shown in the preceding table, an ACL allows only a finite set of permissions, compared to the number of permissions you can set in an access policy \(see [Specifying Permissions in a Policy](using-with-s3-actions.md)\)\. Each of these permissions allows one or more Amazon S3 operations\.

The following table shows how each ACL permission maps to the corresponding access policy permissions\. As you can see, access policy allows more permissions than ACL does\. You use ACL primarily to grant basic read/write permissions, similar to file system permissions\. For more information about when to use ACL, see [Guidelines for Using the Available Access Policy Options](access-policy-alternatives-guidelines.md)\.


| ACL permission | Corresponding access policy permissions when the ACL permission is granted on a bucket  | Corresponding access policy permissions when the ACL permission is granted on an object | 
| --- | --- | --- | 
| READ | s3:ListBucket, s3:ListBucketVersions, and s3:ListBucketMultipartUploads  | s3:GetObject, s3:GetObjectVersion, and s3:GetObjectTorrent | 
| WRITE |  `s3:PutObject` and `s3:DeleteObject`\. In addition, when the grantee is the bucket owner, granting `WRITE` permission in a bucket ACL allows the `s3:DeleteObjectVersion` action to be performed on any version in that bucket\.  | Not applicable | 
| READ\_ACP | s3:GetBucketAcl  | s3:GetObjectAcl and s3:GetObjectVersionAcl | 
| WRITE\_ACP | s3:PutBucketAcl | s3:PutObjectAcl and s3:PutObjectVersionAcl | 
| FULL\_CONTROL | Equivalent to granting READ, WRITE, READ\_ACP, and WRITE\_ACP ACL permissions\. Accordingly, this ACL permission maps to a combination of corresponding access policy permissions\. | Equivalent to granting READ, READ\_ACP, and WRITE\_ACP ACL permissions\. Accordingly, this ACL permission maps to a combination of corresponding access policy permissions\. | 

## Sample ACL<a name="sample-acl"></a>

The following sample ACL on a bucket identifies the resource owner and a set of grants\. The format is the XML representation of an ACL in the Amazon S3 REST API\. The bucket owner has `FULL_CONTROL` of the resource\. In addition, the ACL shows how permissions are granted on a resource to two AWS accounts, identified by canonical user ID, and two of the predefined Amazon S3 groups discussed in the preceding section\.

**Example**  

```
 1. <?xml version="1.0" encoding="UTF-8"?>
 2. <AccessControlPolicy xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
 3.   <Owner>
 4.     <ID>Owner-canonical-user-ID</ID>
 5.     <DisplayName>display-name</DisplayName>
 6.   </Owner>
 7.   <AccessControlList>
 8.     <Grant>
 9.       <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="CanonicalUser">
10.         <ID>Owner-canonical-user-ID</ID>
11.         <DisplayName>display-name</DisplayName>
12.       </Grantee>
13.       <Permission>FULL_CONTROL</Permission>
14.     </Grant>
15.     
16.     <Grant>
17.       <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="CanonicalUser">
18.         <ID>user1-canonical-user-ID</ID>
19.         <DisplayName>display-name</DisplayName>
20.       </Grantee>
21.       <Permission>WRITE</Permission>
22.     </Grant>
23. 
24.     <Grant>
25.       <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="CanonicalUser">
26.         <ID>user2-canonical-user-ID</ID>
27.         <DisplayName>display-name</DisplayName>
28.       </Grantee>
29.       <Permission>READ</Permission>
30.     </Grant>
31. 
32.     <Grant>
33.       <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="Group">
34.         <URI>http://acs.amazonaws.com/groups/global/AllUsers</URI> 
35.       </Grantee>
36.       <Permission>READ</Permission>
37.     </Grant>
38.     <Grant>
39.       <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="Group">
40.         <URI>http://acs.amazonaws.com/groups/s3/LogDelivery</URI>
41.       </Grantee>
42.       <Permission>WRITE</Permission>
43.     </Grant>
44. 
45.   </AccessControlList>
46. </AccessControlPolicy>
```

## Canned ACL<a name="canned-acl"></a>

Amazon S3 supports a set of predefined grants, known as canned ACLs\. Each canned ACL has a predefined set of grantees and permissions\. The following table lists the set of canned ACLs and the associated predefined grants\. 


| Canned ACL | Applies to | Permissions added to ACL | 
| --- | --- | --- | 
| private | Bucket and object | Owner gets FULL\_CONTROL\. No one else has access rights \(default\)\. | 
| public\-read | Bucket and object | Owner gets FULL\_CONTROL\. The AllUsers group \(see [Who Is a Grantee?](#specifying-grantee)\) gets READ access\.  | 
| public\-read\-write | Bucket and object | Owner gets FULL\_CONTROL\. The AllUsers group gets READ and WRITE access\. Granting this on a bucket is generally not recommended\. | 
| aws\-exec\-read | Bucket and object | Owner gets FULL\_CONTROL\. Amazon EC2 gets READ access to GET an Amazon Machine Image \(AMI\) bundle from Amazon S3\. | 
| authenticated\-read | Bucket and object | Owner gets FULL\_CONTROL\. The AuthenticatedUsers group gets READ access\. | 
| bucket\-owner\-read | Object | Object owner gets FULL\_CONTROL\. Bucket owner gets READ access\. If you specify this canned ACL when creating a bucket, Amazon S3 ignores it\. | 
| bucket\-owner\-full\-control | Object  | Both the object owner and the bucket owner get FULL\_CONTROL over the object\. If you specify this canned ACL when creating a bucket, Amazon S3 ignores it\. | 
| log\-delivery\-write | Bucket  | The LogDelivery group gets WRITE and READ\_ACP permissions on the bucket\. For more information about logs, see \([Amazon S3 Server Access Logging](ServerLogs.md)\)\. | 

**Note**  
You can specify only one of these canned ACLs in your request\.

You specify a canned ACL in your request using the `x-amz-acl` request header\. When Amazon S3 receives a request with a canned ACL in the request, it adds the predefined grants to the ACL of the resource\. 

## How to Specify an ACL<a name="setting-acls"></a>

Amazon S3 APIs enable you to set an ACL when you create a bucket or an object\. Amazon S3 also provides API to set an ACL on an existing bucket or an object\. These APIs provide the following methods to set an ACL:
+ **Set ACL using request headers—** When you send a request to create a resource \(bucket or object\), you set an ACL using the request headers\. Using these headers, you can either specify a canned ACL or specify grants explicitly \(identifying grantee and permissions explicitly\)\. 
+ **Set ACL using request body—** When you send a request to set an ACL on an existing resource, you can set the ACL either in the request header or in the body\. 

 For more information, see [Managing ACLs](managing-acls.md)\.