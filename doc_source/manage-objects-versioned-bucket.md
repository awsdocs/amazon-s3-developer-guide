# Managing Objects in a Versioning\-Enabled Bucket<a name="manage-objects-versioned-bucket"></a>

**Topics**
+ [Adding Objects to Versioning\-Enabled Buckets](AddingObjectstoVersioningEnabledBuckets.md)
+ [Listing Objects in a Versioning\-Enabled Bucket](list-obj-version-enabled-bucket.md)
+ [Retrieving Object Versions](RetrievingObjectVersions.md)
+ [Deleting Object Versions](DeletingObjectVersions.md)
+ [Transitioning Object Versions](transitioning-object-versions.md)
+ [Restoring Previous Versions](RestoringPreviousVersions.md)
+ [Versioned Object Permissions](VersionedObjectPermissionsandACLs.md)

Objects that are stored in your bucket before you set the versioning state have a version ID of `null`\. When you enable versioning, existing objects in your bucket do not change\. What changes is how Amazon S3 handles the objects in future requests\. The topics in this section explain various object operations in a versioning\-enabled bucket\.