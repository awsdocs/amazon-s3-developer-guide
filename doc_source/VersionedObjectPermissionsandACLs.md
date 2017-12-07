# Versioned Object Permissions<a name="VersionedObjectPermissionsandACLs"></a>

Permissions are set at the version level\. Each version has its own object owner; an AWS account that creates the object version is the owner\. So, you can set different permissions for different versions of the same object\. To do so, you must specify the version ID of the object whose permissions you want to set in a `PUT Object versionId acl` request\. For a detailed description and instructions on using ACLs, see [Managing Access Permissions to Your Amazon S3 Resources](s3-access-control.md)\.

**Example Setting Permissions for an Object Version**  
The following request sets the permission of the grantee, `BucketOwner@amazon.com`, to `FULL_CONTROL` on the key, `my-image.jpg`, version ID, 3HL4kqtJvjVBH40Nrjfkd\.  

```
 1. PUT /my-image.jpg?acl&versionId=3HL4kqtJvjVBH40Nrjfkd HTTP/1.1
 2. Host: bucket.s3.amazonaws.com
 3. Date: Wed, 28 Oct 2009 22:32:00 GMT
 4. Authorization: AWS AKIAIOSFODNN7EXAMPLE:0RQf4/cRonhpaBX5sCYVf1bNRuU=
 5. Content-Length: 124
 6.  
 7. <AccessControlPolicy>
 8.   <Owner>
 9.     <ID>75cc57f09aa0c8caeab4f8c24e99d10f8e7faeebf76c078efc7c6caea54ba06a</ID>
10.     <DisplayName>mtd@amazon.com</DisplayName>
11.   </Owner>
12.   <AccessControlList>
13.     <Grant>
14.       <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="CanonicalUser">
15.         <ID>a9a7b886d6fd24a52fe8ca5bef65f89a64e0193f23000e241bf9b1c61be666e9</ID>
16.         <DisplayName>BucketOwner@amazon.com</DisplayName>
17.       </Grantee>
18.       <Permission>FULL_CONTROL</Permission>
19.     </Grant>
20.   </AccessControlList>
21.   </AccessControlPolicy>
```

Likewise, to get the permissions of a specific object version, you must specify its version ID in a `GET Object versionId acl` request\. You need to include the version ID because, by default, `GET Object acl` returns the permissions of the current version of the object\. 

**Example Retrieving the Permissions for a Specified Object Version**  
In the following example, Amazon S3 returns the permissions for the key, `my-image.jpg`, version ID, DVBH40Nr8X8gUMLUo\.  

```
1. GET /my-image.jpg?versionId=DVBH40Nr8X8gUMLUo&acl HTTP/1.1
2. Host: bucket.s3.amazonaws.com
3. Date: Wed, 28 Oct 2009 22:32:00 GMT
4. Authorization: AWS AKIAIOSFODNN7EXAMPLE:0RQf4/cRonhpaBX5sCYVf1bNRuU
```

For more information, see [GET Object acl](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETacl.html)\.