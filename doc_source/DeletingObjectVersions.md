# Deleting Object Versions<a name="DeletingObjectVersions"></a>

You can delete object versions whenever you want\. In addition, you can also define lifecycle configuration rules for objects that have a well\-defined lifecycle to request Amazon S3 to expire current object versions or permanently remove noncurrent object versions\. When your bucket is version\-enabled or versioning is suspended, the lifecycle configuration actions work as follows:
+ The `Expiration` action applies to the current object version and instead of deleting the current object version, Amazon S3 retains the current version as a noncurrent version by adding a delete marker, which then becomes the current version\.
+ The `NoncurrentVersionExpiration` action applies to noncurrent object versions, and Amazon S3 permanently removes these object versions\. You cannot recover permanently removed objects\.

For more information, see [Object Lifecycle Management](object-lifecycle-mgmt.md)\.

A `DELETE` request has the following use cases:
+ When versioning is enabled, a simple `DELETE` cannot permanently delete an object\. 

  Instead, Amazon S3 inserts a delete marker in the bucket, and that marker becomes the current version of the object with a new ID\. When you try to `GET` an object whose current version is a delete marker, Amazon S3 behaves as though the object has been deleted \(even though it has not been erased\) and returns a 404 error\. 

  The following figure shows that a simple `DELETE` does not actually remove the specified object\. Instead, Amazon S3 inserts a delete marker\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_DELETE_versioningEnabled.png)
+ To permanently delete versioned objects, you must use `DELETE Object versionId`\.

  The following figure shows that deleting a specified object version permanently removes that object\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_DELETE_versioningEnabled2.png)

## Using the Console<a name="delete-obj-version-version-enabled-console"></a>

For instructions see, [How Do I See the Versions of an S3 Object?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/view-object-versions.html) in the Amazon Simple Storage Service Console User Guide\. 

## Using the AWS SDKs<a name="delete-obj-version-version-enabled-bucket-sdks"></a>

For examples of uploading objects using the AWS SDKs for Java, \.NET, and PHP, see [Deleting Objects](DeletingObjects.md)\. The examples for uploading objects in nonversioned and versioning\-enabled buckets are the same, although in the case of versioning\-enabled buckets, Amazon S3 assigns a version number\. Otherwise, the version number is null\. 

For information about using other AWS SDKs, see [Sample Code and Libraries](https://aws.amazon.com/code/)\. 

## Using REST<a name="delete-obj-version-enabled-bucket-rest"></a>

**To a delete a specific version of an object**
+ In a `DELETE`, specify a version ID\.

**Example Deleting a Specific Version**  
The following example shows how to delete version UIORUnfnd89493jJFJ of `photo.gif`\.  

```
1. DELETE /photo.gif?versionId=UIORUnfnd89493jJFJ HTTP/1.1 
2. 
3. Host: bucket.s3.amazonaws.com
4. Date: Wed, 12 Oct 2009 17:50:00 GMT
5. Authorization: AWS AKIAIOSFODNN7EXAMPLE:xQE0diMbLRepdf3YB+FIEXAMPLE=
6. Content-Type: text/plain
7. Content-Length: 0
```

## Related Topics<a name="delete-obj-version-enabled-related-topics"></a>

 [Using MFA Delete](UsingMFADelete.md) 

 [Working with Delete Markers](DeleteMarker.md) 

 [Removing Delete Markers](RemDelMarker.md) 

 [Using Versioning](Versioning.md) 