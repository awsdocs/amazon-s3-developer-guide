# Permissions Required for Website Access<a name="WebsiteAccessPermissionsReqd"></a>

When you configure a bucket as a website, you must make the objects that you want to serve publicly readable\. To do this, you write a bucket policy that grants everyone `s3:GetObject` permission\. On the website endpoint, if a user requests an object that doesn't exist, Amazon S3 returns HTTP response code `404 (Not Found)`\. If the object exists but you haven't granted read permission on it, the website endpoint returns HTTP response code `403 (Access Denied)`\. The user can use the response code to infer whether a specific object exists\. If you don't want this behavior, you should not enable website support for your bucket\. 

The following sample bucket policy grants everyone access to the objects in the specified folder\. For more information about bucket policies, see [Using Bucket Policies and User Policies](using-iam-policies.md)\.

```
 1. {
 2.   "Version":"2012-10-17",
 3.   "Statement":[{
 4. 	"Sid":"PublicReadGetObject",
 5.         "Effect":"Allow",
 6. 	  "Principal": "*",
 7.       "Action":["s3:GetObject"],
 8.       "Resource":["arn:aws:s3:::example-bucket/*"
 9.       ]
10.     }
11.   ]
12. }
```

**Note**  
Keep the following in mind:  
To host a website, your bucket must have public read access\. It is intentional that everyone in the world will have read access to this bucket\.
The bucket policy applies only to objects that are owned by the bucket owner\. If your bucket contains objects that aren't owned by the bucket owner, public READ permission on those objects should be granted using the object access control list \(ACL\)\.

You can grant public read permission to your objects by using either a bucket policy or an object ACL\. To make an object publicly readable using an ACL, grant READ permission to the AllUsers group, as shown in the following grant element\. Add this grant element to the object ACL\. For information about managing ACLs, see [Managing Access with ACLs](S3_ACLs_UsingACLs.md)\.

```
1. <Grant>
2.   <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
3.           xsi:type="Group">
4.     <URI>http://acs.amazonaws.com/groups/global/AllUsers</URI>
5.   </Grantee>
6.   <Permission>READ</Permission>
7. </Grant>
```