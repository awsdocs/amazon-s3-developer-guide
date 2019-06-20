# Working with Delete Markers<a name="DeleteMarker"></a>

A delete marker is a placeholder \(marker\) for a versioned object that was named in a simple `DELETE` request\. Because the object was in a versioning\-enabled bucket, the object was not deleted\. The delete marker, however, makes Amazon S3 behave as if it had been deleted\. 

A delete marker has a key name \(or key\) and version ID like any other object\. However, a delete marker differs from other objects in the following ways:
+ It does not have data associated with it\.
+ It is not associated with an access control list \(ACL\) value\.
+ It does not retrieve anything from a `GET` request because it has no data; you get a 404 error\.
+ The only operation you can use on a delete marker is `DELETE`, and only the bucket owner can issue such a request\.

Delete markers accrue a nominal charge for storage in Amazon S3\. The storage size of a delete marker is equal to the size of the key name of the delete marker\. A key name is a sequence of Unicode characters\. The UTF\-8 encoding adds from 1 to 4 bytes of storage to your bucket for each character in the name\. For more information about key names, see [Object Keys](UsingMetadata.md#object-keys)\. For information about deleting a delete marker, see [Removing Delete Markers](RemDelMarker.md)\.  

Only Amazon S3 can create a delete marker, and it does so whenever you send a `DELETE Object` request on an object in a versioning\-enabled or suspended bucket\. The object named in the `DELETE` request is not actually deleted\. Instead, the delete marker becomes the current version of the object\. \(The object's key name \(or key\) becomes the key of the delete marker\.\) If you try to get an object and its current version is a delete marker, Amazon S3 responds with:
+ A 404 \(Object not found\) error
+ A response header, x\-amz\-delete\-marker: true

The response header tells you that the object accessed was a delete marker\. This response header never returns `false`; if the value is `false`, Amazon S3 does not include this response header in the response\.

The following figure shows how a simple `GET` on an object, whose current version is a delete marker, returns a 404 No Object Found error\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_DELETE_NoObjectFound.png)

The only way to list delete markers \(and other versions of an object\) is by using the `versions` subresource in a `GET Bucket versions` request\. A simple `GET` does not retrieve delete marker objects\. The following figure shows that a `GET Bucket` request does not return objects whose current version is a delete marker\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_GETBucketwithDeleteMarkers.png)