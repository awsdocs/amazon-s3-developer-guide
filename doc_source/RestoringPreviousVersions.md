# Restoring Previous Versions<a name="RestoringPreviousVersions"></a>

One of the value propositions of versioning is the ability to retrieve previous versions of an object\. There are two approaches to doing so:
+ Copy a previous version of the object into the same bucket

  The copied object becomes the current version of that object and all object versions are preserved\.
+ Permanently delete the current version of the object

  When you delete the current object version, you, in effect, turn the previous version into the current version of that object\.

Because all object versions are preserved, you can make any earlier version the current version by copying a specific version of the object into the same bucket\. In the following figure, the source object \(ID = 111111\) is copied into the same bucket\. Amazon S3 supplies a new ID \(88778877\) and it becomes the current version of the object\. So, the bucket has both the original object version \(111111\) and its copy \(88778877\)\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_COPY2.png)

A subsequent `GET` will retrieve version 88778877\.

The following figure shows how deleting the current version \(121212\) of an object, which leaves the previous version \(111111\) as the current object\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_COPY_delete2.png)

A subsequent `GET` will retrieve version 111111\.