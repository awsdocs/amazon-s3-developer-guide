# Uploading objects<a name="UploadingObjects"></a>

 Depending on the size of the data you are uploading, Amazon S3 offers the following options: 
+ **Upload an object in a single operation using the AWS SDKs, REST API, or AWS CLI—**With a single PUT operation, you can upload objects up to 5 GB in size\.

  For more information, see [Uploading an object in a single operation](UploadInSingleOp.md)\.
+ **Upload a single object using the Amazon S3 Console—**With the Amazon S3 Console, you can upload a single object up to 160 GB in size\. 

  For more information, see [How do I upload files and folders to an S3 bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/upload-objects.html) in the *Amazon Simple Storage Service Console User Guide*\.
+ **Upload objects in parts using the AWS SDKs, REST API, or AWS CLI—**Using the multipart upload API, you can upload large objects, up to 5 TB\.

  The multipart upload API is designed to improve the upload experience for larger objects\. You can upload objects in parts\. These object parts can be uploaded independently, in any order, and in parallel\. You can use a multipart upload for objects from 5 MB to 5 TB in size\. For more information, see [Uploading objects using multipart upload API](uploadobjusingmpu.md)\.

We recommend that you use multipart uploading in the following ways:
+ If you're uploading large objects over a stable high\-bandwidth network, use multipart uploading to maximize the use of your available bandwidth by uploading object parts in parallel for multi\-threaded performance\.
+ If you're uploading over a spotty network, use multipart uploading to increase resiliency to network errors by avoiding upload restarts\. When using multipart uploading, you need to retry uploading only parts that are interrupted during the upload\. You don't need to restart uploading your object from the beginning\.

For more information about multipart uploads, see [Multipart upload overview](mpuoverview.md)\.

**Topics**
+ [Uploading an object in a single operation](UploadInSingleOp.md)
+ [Uploading objects using multipart upload API](uploadobjusingmpu.md)
+ [Uploading objects using presigned URLs](PresignedUrlUploadObject.md)

When uploading an object, you can optionally request that Amazon S3 encrypt it before saving it to disk, and decrypt it when you download it\. For more information, see [Protecting data using encryption](UsingEncryption.md)\. 

**Related Topics**  
[Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)