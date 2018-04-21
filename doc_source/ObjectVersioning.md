# Object Versioning<a name="ObjectVersioning"></a>

Use versioning to keep multiple versions of an object in one bucket\. For example, you could store `my-image.jpg` \(version 111111\) and `my-image.jpg` \(version 222222\) in a single bucket\. Versioning protects you from the consequences of unintended overwrites and deletions\. You can also use versioning to archive objects so you have access to previous versions\. 

**Note**  
The SOAP API does not support versioning\. SOAP support over HTTP is deprecated, but it is still available over HTTPS\. New Amazon S3 features are not supported for SOAP\.

To customize your data retention approach and control storage costs, use object versioning with [Object Lifecycle Management](object-lifecycle-mgmt.md)\. For information about creating lifecycle policies using the AWS Management Console, see [ How Do I Create a Lifecycle Policy for an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-lifecycle.html) in the *Amazon Simple Storage Service Console User Guide*\.

If you have an object expiration lifecycle policy in your non\-versioned bucket and you want to maintain the same permanent delete behavior when you enable versioning, you must add a noncurrent expiration policy\. The noncurrent expiration lifecycle policy will manage the deletes of the noncurrent object versions in the version\-enabled bucket\. \(A version\-enabled bucket maintains one current and zero or more noncurrent object versions\.\)

You must explicitly enable versioning on your bucket\. By default, versioning is disabled\. Regardless of whether you have enabled versioning, each object in your bucket has a version ID\. If you have not enabled versioning, Amazon S3 sets the value of the version ID to null\. If you have enabled versioning, Amazon S3 assigns a unique version ID value for the object\. When you enable versioning on a bucket, objects already stored in the bucket are unchanged\. The version IDs \(null\), contents, and permissions remain the same\.

Enabling and suspending versioning is done at the bucket level\. When you enable versioning for a bucket, all objects added to it will have a unique version ID\. Unique version IDs are randomly generated, Unicode, UTF\-8 encoded, URL\-ready, opaque strings that are at most 1024 bytes long\. An example version ID is `3/L4kqtJlcpXroDTDmJ+rmSpXd3dIbrHY+MTRCxf3vjVBH40Nr8X8gdRQBpUMLUo`\. Only Amazon S3 generates version IDs\. They cannot be edited\. 

**Note**  
For simplicity, we will use much shorter IDs in all our examples\.

When you `PUT` an object in a versioning\-enabled bucket, the noncurrent version is not overwritten\. The following figure shows that when a new version of `photo.gif` is `PUT` into a bucket that already contains an object with the same name, the original object \(ID = 111111\) remains in the bucket, Amazon S3 generates a new version ID \(121212\), and adds the newer version to the bucket\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_PUT_versionEnabled3.png)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)

This functionality prevents you from accidentally overwriting or deleting objects and affords you the opportunity to retrieve a previous version of an object\. 

When you `DELETE` an object, all versions remain in the bucket and Amazon S3 inserts a delete marker, as shown in the following figure\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_DELETE_versioningEnabled.png)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)

The delete marker becomes the current version of the object\. By default, `GET` requests retrieve the most recently stored version\. Performing a simple `GET Object` request when the current version is a delete marker returns a `404 Not Found` error, as shown in the following figure\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_DELETE_NoObjectFound2.png)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)

You can, however, `GET` a noncurrent version of an object by specifying its version ID\. In the following figure, we `GET` a specific object version, 111111\. Amazon S3 returns that object version even though it's not the current version\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_GET_Versioned3.png)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)

You can permanently delete an object by specifying the version you want to delete\. Only the owner of an Amazon S3 bucket can permanently delete a version\. The following figure shows how `DELETE versionId` permanently deletes an object from a bucket and that Amazon S3 doesn't insert a delete marker\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_DELETE_versioningEnabled2.png)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)

You can add additional security by configuring a bucket to enable MFA \(multi\-factor authentication\) Delete\. When you do, the bucket owner must include two forms of authentication in any request to delete a version or change the versioning state of the bucket\. For more information, see [MFA Delete](Versioning.md#MultiFactorAuthenticationDelete)\.

**Important**  
If you notice a significant increase in the number of HTTP 503\-slow down responses received for Amazon S3 PUT or DELETE object requests to a bucket that has versioning enabled, you might have one or more objects in the bucket for which there are millions of versions\. For more information, see [Troubleshooting Amazon S3](troubleshooting.md)\.

For more information, see [Using Versioning](Versioning.md)\.