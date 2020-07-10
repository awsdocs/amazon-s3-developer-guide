# Copying objects<a name="CopyingObjectsExamples"></a>

The copy operation creates a copy of an object that is already stored in Amazon S3\. You can create a copy of your object up to 5 GB in a single atomic operation\. However, for copying an object that is greater than 5 GB, you must use the multipart upload API\. Using the `copy` operation, you can:
+ Create additional copies of objects 
+  Rename objects by copying them and deleting the original ones 
+  Move objects across Amazon S3 locations \(e\.g\., us\-west\-1 and Europe\) 
+ Change object metadata

  Each Amazon S3 object has metadata\. It is a set of name\-value pairs\. You can set object metadata at the time you upload it\. After you upload the object, you cannot modify object metadata\. The only way to modify object metadata is to make a copy of the object and set the metadata\. In the copy operation you set the same object as the source and target\. 

Each object has metadata\. Some of it is system metadata and other user\-defined\. Users control some of the system metadata such as storage class configuration to use for the object, and configure server\-side encryption\. When you copy an object, user\-controlled system metadata and user\-defined metadata are also copied\. Amazon S3 resets the system\-controlled metadata\. For example, when you copy an object, Amazon S3 resets the creation date of the copied object\. You don't need to set any of these values in your copy request\. 

When copying an object, you might decide to update some of the metadata values\. For example, if your source object is configured to use standard storage, you might choose to use reduced redundancy storage for the object copy\. You might also decide to alter some of the user\-defined metadata values present on the source object\. Note that if you choose to update any of the object's user\-configurable metadata \(system or user\-defined\) during the copy, then you must explicitly specify all of the user\-configurable metadata present on the source object in your request, even if you are only changing only one of the metadata values\.

For more information about the object metadata, see [Object key and metadata](UsingMetadata.md)\.

**Note**  
Copying objects across locations incurs bandwidth charges\. 
 If the source object is archived in `S3 Glacier` or `S3 Glacier Deep Archive`, you must first restore a temporary copy before you can copy the object to another bucket\. For information about archiving objects, see [Transitioning to the S3 Glacier and S3 Glacier Deep Archive storage classes \(object archival\)](lifecycle-transition-general-considerations.md#before-deciding-to-archive-objects)\. 

When copying objects, you can request Amazon S3 to save the target object encrypted with an AWS Key Management Service \(AWS KMS\) customer master key \(CMK\), an Amazon S3\-managed encryption key, or a customer\-provided encryption key\. Accordingly, you must specify encryption information in your request\. If the copy source is an object that is stored in Amazon S3 using server\-side encryption with customer provided key, you will need to provide encryption information in your request so Amazon S3 can decrypt the object for copying\. For more information, see [Protecting data using encryption](UsingEncryption.md)\.

To copy more than one Amazon S3 object with a single request, you can use Amazon S3 batch operations\. You provide S3 Batch Operations with a list of objects to operate on\. S3 Batch Operations call the respective API to perform the specified operation\. A single S3 Batch Operations job can perform the specified operation on billions of objects containing exabytes of data\. 

S3 Batch Operations track progress, send notifications, and store a detailed completion report of all actions, providing a fully managed, auditable, serverless experience\. You can use S3 Batch Operations through the AWS Management Console, AWS CLI, AWS SDKs, or REST API\. For more information, see [The basics: S3 Batch Operations](batch-ops-basics.md)\.

## Related Resources<a name="RelatedResources015"></a>
+ [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)