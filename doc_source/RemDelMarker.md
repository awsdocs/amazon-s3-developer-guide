# Removing Delete Markers<a name="RemDelMarker"></a>

To delete a delete marker, you must specify its version ID in a `DELETE Object versionId` request\. If you use a `DELETE` request to delete a delete marker \(without specifying the version ID of the delete marker\), Amazon S3 does not delete the delete marker, but instead, inserts another delete marker\.

The following figure shows how a simple `DELETE` on a delete marker removes nothing, but adds a new delete marker to a bucket\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_DELETE_deleteMarker.png)

In a versioning\-enabled bucket, this new delete marker would have a unique version ID\. So, it's possible to have multiple delete markers of the same object in one bucket\.

To permanently delete a delete marker, you must include its version ID in a `DELETE Object versionId` request\. The following figure shows how a `DELETE Object versionId` request permanently removes a delete marker\. Only the owner of a bucket can permanently remove a delete marker\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_DELETE_deleteMarkerVersioned.png)

The effect of removing the delete marker is that a simple `GET` request will now retrieve the current version \(121212\) of the object\.

**To permanently remove a delete marker**

1. Set `versionId` to the ID of the version to the delete marker you want to remove\.

1. Send a `DELETE Object versionId` request\.

**Example Removing a Delete Marker**  
The following example removes the delete marker for `photo.gif` version 4857693\.  

```
1. DELETE /photo.gif?versionId=4857693 HTTP/1.1
2. Host: bucket.s3.amazonaws.com
3. Date: Wed, 28 Oct 2009 22:32:00 GMT
4. Authorization: AWS AKIAIOSFODNN7EXAMPLE:0RQf4/cRonhpaBX5sCYVf1bNRuU=
```

When you delete a delete marker, Amazon S3 includes in the response:

```
1. 204 NoContent 
2. x-amz-version-id: versionID 
3. x-amz-delete-marker: true
```