# Deleting Objects from Versioning\-Suspended Buckets<a name="DeletingObjectsfromVersioningSuspendedBuckets"></a>

If versioning is suspended, a `DELETE` request:
+ Can only remove an object whose version ID is `null`

  Doesn't remove anything if there isn't a null version of the object in the bucket\.
+ Inserts a delete marker into the bucket\.

The following figure shows how a simple `DELETE` removes a null version and Amazon S3 inserts a delete marker in its place with a version ID of `null`\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_DELETE_versioningSuspended.png)

Remember that a delete marker doesn't have content, so you lose the content of the null version when a delete marker replaces it\.

The following figure shows a bucket that doesn't have a null version\. In this case, the `DELETE` removes nothing; Amazon S3 just inserts a delete marker\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_DELETE_versioningSuspendedNoNull.png)

Even in a versioning\-suspended bucket, the bucket owner can permanently delete a specified version\. The following figure shows that deleting a specified object version permanently removes that object\. Only the bucket owner can delete a specified object version\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_DELETE_versioningEnabled2.png)