# Retrieving Object Versions<a name="RetrievingObjectVersions"></a>

A simple `GET` request retrieves the current version of an object\. The following figure shows how `GET` returns the current version of the object, `photo.gif`\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_GET_NoVersionID.png)

To retrieve a specific version, you have to specify its version ID\. The following figure shows that a `GET versionId` request retrieves the specified version of the object \(not necessarily the current one\)\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_GET_Versioned.png)

## Using the Console<a name="retrieve-obj-versioning-enabled-console"></a>

For instructions see, [How Do I See the Versions of an S3 Object?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/view-object-versions.html) in the Amazon Simple Storage Service Console User Guide\.

## Using the AWS SDKs<a name="retrieve-obj-version-sdks"></a>

For examples of uploading objects using AWS SDKs for Java, \.NET, and PHP, see [Getting Objects](GettingObjectsUsingAPIs.md)\. The examples for uploading objects in a nonversioned and versioning\-enabled buckets are the same, although in the case of versioning\-enabled buckets, Amazon S3 assigns a version number\. Otherwise, the version number is null\. 

For information about using other AWS SDKs, see [Sample Code and Libraries](https://aws.amazon.com/code/)\. 

## Using REST<a name="retrieve-obj-version-rest"></a>

**To retrieve a specific object version**

1. Set `versionId` to the ID of the version of the object you want to retrieve\.

1. Send a `GET Object versionId `request\.

**Example Retrieving a Versioned Object**  
The following request retrieves version L4kqtJlcpXroDTDmpUMLUo of `my-image.jpg`\.  

```
1. GET /my-image.jpg?versionId=L4kqtJlcpXroDTDmpUMLUo HTTP/1.1
2. Host: bucket.s3.amazonaws.com
3. Date: Wed, 28 Oct 2009 22:32:00 GMT
4. Authorization: AWS AKIAIOSFODNN7EXAMPLE:0RQf4/cRonhpaBX5sCYVf1bNRuU=
```

## Related Topics<a name="retrieve-obj-versioning-enabled-related-topics"></a>

 [Retrieving the Metadata of an Object Version](RetMetaOfObjVersion.md) 