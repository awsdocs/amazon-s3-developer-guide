# Object Versioning<a name="ObjectVersioning"></a>

Use Amazon S3 Versioning to keep multiple versions of an object in one bucket\. For example, you could store `my-image.jpg` \(version 111111\) and `my-image.jpg` \(version 222222\) in a single bucket\. S3 Versioning protects you from the consequences of unintended overwrites and deletions\. You can also use it to archive objects so that you have access to previous versions\. 

**Note**  
The SOAP API does not support S3 Versioning\. SOAP support over HTTP is deprecated, but it is still available over HTTPS\. New Amazon S3 features are not supported for SOAP\.

To customize your data retention approach and control storage costs, use object versioning with [Object lifecycle management](object-lifecycle-mgmt.md)\. For information about creating S3 Lifecycle policies using the AWS Management Console, see [How Do I Create a Lifecycle Policy for an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-lifecycle.html) in the *Amazon Simple Storage Service Console User Guide*\.

If you have an object expiration lifecycle policy in your non\-versioned bucket and you want to maintain the same permanent delete behavior when you enable versioning, you must add a noncurrent expiration policy\. The noncurrent expiration lifecycle policy will manage the deletes of the noncurrent object versions in the version\-enabled bucket\. \(A version\-enabled bucket maintains one current and zero or more noncurrent object versions\.\)

You must explicitly enable S3 Versioning on your bucket\. By default, S3 Versioning is disabled\. Regardless of whether you have enabled Versioning, each object in your bucket has a version ID\. If you have not enabled Versioning, Amazon S3 sets the value of the version ID to null\. If S3 Versioning is enabled, Amazon S3 assigns a version ID value for the object\. This value distinguishes it from other versions of the same key\.

Enabling and suspending versioning is done at the bucket level\. When you enable versioning on an existing bucket, objects that are already stored in the bucket are unchanged\. The version IDs \(null\), contents, and permissions remain the same\. After you enable S3 Versioning for a bucket, each object that is added to the bucket gets a version ID, which distinguishes it from other versions of the same key\. 

Only Amazon S3 generates version IDs, and they can’t be edited\. Version IDs are Unicode, UTF\-8 encoded, URL\-ready, opaque strings that are no more than 1,024 bytes long\. The following is an example: `3/L4kqtJlcpXroDTDmJ+rmSpXd3dIbrHY+MTRCxf3vjVBH40Nr8X8gdRQBpUMLUo`\.

**Note**  
For simplicity, all the examples use much shorter IDs\.

When you `PUT` an object in a versioning\-enabled bucket, the noncurrent version is not overwritten\. The following figure shows that when a new version of `photo.gif` is `PUT` into a bucket that already contains an object with the same name, the original object \(ID = 111111\) remains in the bucket, Amazon S3 generates a new version ID \(121212\), and adds the newer version to the bucket\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_PUT_versionEnabled3.png)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)

This functionality prevents you from accidentally overwriting or deleting objects and affords you the opportunity to retrieve a previous version of an object\. 

When you `DELETE` an object, all versions remain in the bucket and Amazon S3 inserts a delete marker, as shown in the following figure\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_DELETE_versioningEnabled.png)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)

The delete marker becomes the current version of the object\. By default, `GET` requests retrieve the most recently stored version\. Performing a simple `GET Object` request when the current version is a delete marker returns a `404 Not Found` error, as shown in the following figure\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_DELETE_NoObjectFound2.png)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)

However, you can `GET` a noncurrent version of an object by specifying its version ID\. In the following figure, you `GET` a specific object version, 111111\. Amazon S3 returns that object version even though it's not the current version\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_GET_Versioned3.png)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)

You can permanently delete an object by specifying the version you want to delete\. Only the owner of an Amazon S3 bucket can permanently delete a version\. The following figure shows how `DELETE versionId` permanently deletes an object from a bucket and that Amazon S3 doesn't insert a delete marker\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_DELETE_versioningEnabled2.png)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)

You can add additional security by configuring a bucket to enable MFA \(multi\-factor authentication\) Delete\. When you do, the bucket owner must include two forms of authentication in any request to delete a version or change the versioning state of the bucket\. For more information, see [MFA Delete](Versioning.md#MultiFactorAuthenticationDelete)\.

**Important**  
If you notice a significant increase in the number of HTTP 503\-slow down responses received for Amazon S3 PUT or DELETE object requests to a bucket that has S3 Versioning enabled, you might have one or more objects in the bucket for which there are millions of versions\. For more information, see [Troubleshooting Amazon S3](troubleshooting.md)\.

For more information, see [Using Versioning](Versioning.md)\.