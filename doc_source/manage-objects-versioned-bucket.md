# Managing objects in a versioning\-enabled bucket<a name="manage-objects-versioned-bucket"></a>

**Topics**
+ [Adding objects to versioning\-enabled buckets](AddingObjectstoVersioningEnabledBuckets.md)
+ [Listing objects in a versioning\-enabled bucket](list-obj-version-enabled-bucket.md)
+ [Retrieving object versions](RetrievingObjectVersions.md)
+ [Deleting object versions](DeletingObjectVersions.md)
+ [Transitioning object versions](transitioning-object-versions.md)
+ [Restoring previous versions](RestoringPreviousVersions.md)
+ [Versioned object permissions](VersionedObjectPermissionsandACLs.md)

Objects that are stored in your bucket before you set the versioning state have a version ID of `null`\. When you enable versioning, existing objects in your bucket do not change\. What changes is how Amazon S3 handles the objects in future requests\. The topics in this section explain various object operations in a versioning\-enabled bucket\.