# Setting Access Policy with SOAP<a name="SOAPAccessPolicy"></a>

**Note**  
 SOAP support over HTTP is deprecated, but it is still available over HTTPS\. New Amazon S3 features will not be supported for SOAP\. We recommend that you use either the REST API or the AWS SDKs\. 

Access control can be set at the time a bucket or object is written by including the "AccessControlList" element with the request to `CreateBucket`, `PutObjectInline`, or `PutObject`\. The AccessControlList element is described in [Managing Access Permissions to Your Amazon S3 Resources](s3-access-control.md)\. If no access control list is specified with these operations, the resource is created with a default access policy that gives the requester FULL\_CONTROL access \(this is the case even if the request is a PutObjectInline or PutObject request for an object that already exists\)\.

Following is a request that writes data to an object, makes the object readable by anonymous principals, and gives the specified user FULL\_CONTROL rights to the bucket \(Most developers will want to give themselves FULL\_CONTROL access to their own bucket\)\.

**Example**  
Following is a request that writes data to an object and makes the object readable by anonymous principals\.  
 `Sample Request`   

```
 1. <PutObjectInline xmlns="http://doc.s3.amazonaws.com/2006-03-01">
 2.   <Bucket>quotes</Bucket>
 3.   <Key>Nelson</Key>
 4.   <Metadata>
 5.     <Name>Content-Type</Name>
 6.     <Value>text/plain</Value>
 7.   </Metadata>
 8.   <Data>aGEtaGE=</Data>
 9.   <ContentLength>5</ContentLength>
10.   <AccessControlList>
11.     <Grant>
12.       <Grantee xsi:type="CanonicalUser">
13.         <ID>75cc57f09aa0c8caeab4f8c24e99d10f8e7faeebf76c078efc7c6caea54ba06a</ID>
14.         <DisplayName>chriscustomer</DisplayName>
15.       </Grantee>
16.       <Permission>FULL_CONTROL</Permission>
17.     </Grant>
18.     <Grant>
19.       <Grantee xsi:type="Group">
20.         <URI>http://acs.amazonaws.com/groups/global/AllUsers<URI>
21.       </Grantee>
22.       <Permission>READ</Permission>
23.     </Grant>
24.   </AccessControlList>
25.   <AWSAccessKeyId>AKIAIOSFODNN7EXAMPLE</AWSAccessKeyId>
26.   <Timestamp>2009-03-01T12:00:00.183Z</Timestamp>
27.   <Signature>Iuyz3d3P0aTou39dzbqaEXAMPLE=</Signature>
28. </PutObjectInline>
```
 `Sample Response`   

```
1. <PutObjectInlineResponse xmlns="http://s3.amazonaws.com/doc/2006-03-01">
2.   <PutObjectInlineResponse>
3.     <ETag>&quot828ef3fdfa96f00ad9f27c383fc9ac7f&quot</ETag>
4.     <LastModified>2009-01-01T12:00:00.000Z</LastModified>
5.   </PutObjectInlineResponse>
6. </PutObjectInlineResponse>
```

The access control policy can be read or set for an existing bucket or object using the `GetBucketAccessControlPolicy`, `GetObjectAccessControlPolicy`, `SetBucketAccessControlPolicy`, and `SetObjectAccessControlPolicy` methods\. For more information, see the detailed explanation of these methods\.