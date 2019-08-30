# PUT Object Copy<a name="batch-ops-copy-object"></a>

The PUT object copy operation copies each object specified in the manifest\. You can copy objects to a different bucket in the same AWS Region or to a bucket in a different Region\. Amazon S3 batch operations support most options available through Amazon S3 for copying objects\. These options include setting object metadata, setting permissions, and changing an object's storage class\. For more information about the functionality available through Amazon S3 for copying objects, see [Copying Objects](CopyingObjectsExamples.md)\. 

## Restrictions and Limitations<a name="batch-ops-copy-object-restrictions"></a>
+ All source objects must be in one bucket\.
+ All destination objects must be in one bucket\.
+ You must have read permissions for the source bucket and write permissions for the destination bucket\.
+ Objects to be copied can be up to 5 GB in size\.
+ All PUT Object Copy options are supported except for conditional checks on ETags and server\-side encryption with customer\-provided encryption keys\.
+ If the buckets are unversioned, you will overwrite objects with the same key names\.
+ Objects are not necessarily copied in the same order as they are listed in the manifest\. So for versioned buckets, if preserving current/non\-current version order is important, you should copy all non\-current versions first and later copy the current versions in a subsequent job after the first job is complete\.