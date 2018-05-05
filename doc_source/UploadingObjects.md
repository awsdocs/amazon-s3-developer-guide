# Uploading Objects<a name="UploadingObjects"></a>

 Depending on the size of the data you are uploading, Amazon S3 offers the following options: 
+ **Upload objects in a single operation—**With a single PUT operation, you can upload objects up to 5 GB in size\. 

  For more information, see [Uploading Objects in a Single Operation](UploadInSingleOp.md)\.
+ **Upload objects in parts—**Using the multipart upload API, you can upload large objects, up to 5 TB\. 

  The multipart upload API is designed to improve the upload experience for larger objects\. You can upload objects in parts\. These object parts can be uploaded independently, in any order, and in parallel\. You can use a multipart upload for objects from 5 MB to 5 TB in size\. For more information, see [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)\.

We recommend that you use multipart uploading in the following ways:
+ If you're uploading large objects over a stable high\-bandwidth network, use multipart uploading to maximize the use of your available bandwidth by uploading object parts in parallel for multi\-threaded performance\.
+ If you're uploading over a spotty network, use multipart uploading to increase resiliency to network errors by avoiding upload restarts\. When using multipart uploading, you need to retry uploading only parts that are interrupted during the upload\. You don't need to restart uploading your object from the beginning\.

For more information about multipart uploads, see [Multipart Upload Overview](mpuoverview.md)\.

**Topics**
+ [Uploading Objects in a Single Operation](UploadInSingleOp.md)
+ [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)
+ [Uploading Objects Using Pre\-Signed URLs](PresignedUrlUploadObject.md)

When uploading an object, you can optionally request that Amazon S3 encrypt it before saving it to disk, and decrypt it when you download it\. For more information, see [Protecting Data Using Encryption](UsingEncryption.md)\. 

**Related Topics**  
[Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)