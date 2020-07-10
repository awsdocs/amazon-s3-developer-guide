# Working with Amazon S3 Objects<a name="UsingObjects"></a>

Amazon S3 is a simple key\-value store designed to store as many objects as you want\. You store these objects in one or more buckets, and each object can be up to 5 TB in size\. An object consists of the following:
+ ****Key**** – The name that you assign to an object\. You use the object key to retrieve the object\.

  For more information, see [Object Key and Metadata](UsingMetadata.md)\.
+ ****Version ID** **– Within a bucket, a key and version ID uniquely identify an object\. 

  The version ID is a string that Amazon S3 generates when you add an object to a bucket\. For more information, see [Object Versioning](ObjectVersioning.md)\.
+ ****Value**** – The content that you are storing\.

  An object value can be any sequence of bytes\. Objects can range in size from zero to 5 TB\. For more information, see [Uploading objects](UploadingObjects.md)\. 
+ ****Metadata**** – A set of name\-value pairs with which you can store information regarding the object\.

  You can assign metadata, referred to as user\-defined metadata, to your objects in Amazon S3\. Amazon S3 also assigns system\-metadata to these objects, which it uses for managing objects\. For more information, see [Object key and metadata](UsingMetadata.md)\.
+ ****Subresources** **– Amazon S3 uses the subresource mechanism to store object\-specific additional information\. 

  Because subresources are subordinates to objects, they are always associated with some other entity such as an object or a bucket\. For more information, see [Object subresources](ObjectAndSubResource.md)\.
+ ****Access Control Information** **– You can control access to the objects you store in Amazon S3\.

  Amazon S3 supports both the resource\-based access control, such as an access control list \(ACL\) and bucket policies, and user\-based access control\. For more information, see [Identity and access management in Amazon S3](s3-access-control.md)\.

For more information about working with objects, see the following sections\. Your Amazon S3 resources \(for example, buckets and objects\) are private by default\. You must explicitly grant permission for others to access these resources\. For example, you might want to share a video or a photo stored in your Amazon S3 bucket on your website\. That works only if you either make the object public or use a presigned URL on your website\. For more information about sharing objects, see [Share an object with others](ShareObjectPreSignedURL.md)\.

**Topics**
+ [Object key and metadata](UsingMetadata.md)
+ [Amazon S3 storage classes](storage-class-intro.md)
+ [Object subresources](ObjectAndSubResource.md)
+ [Object Versioning](ObjectVersioning.md)
+ [Object tagging](object-tagging.md)
+ [Object lifecycle management](object-lifecycle-mgmt.md)
+ [Cross\-origin resource sharing \(CORS\)](cors.md)
+ [Operations on objects](ObjectOperations.md)