# Operations on Objects<a name="ObjectOperations"></a>

Amazon S3 enables you to store, retrieve, and delete objects\. You can retrieve an entire object or a portion of an object\. If you have enabled versioning on your bucket, you can retrieve a specific version of the object\. You can also retrieve a subresource associated with your object and update it where applicable\. You can make a copy of your existing object\. Depending on the object size, the following upload and copy related considerations apply: 
+ **Uploading objects—**You can upload objects of up to 5 GB in size in a single operation\. For objects greater than 5 GB you must use the multipart upload API\. 

  Using the multipart upload API you can upload objects up to 5 TB each\. For more information, see [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)\.
+ **Copying objects—**The copy operation creates a copy of an object that is already stored in Amazon S3\. 

  You can create a copy of your object up to 5 GB in size in a single atomic operation\. However, for copying an object greater than 5 GB, you must use the multipart upload API\. For more information, see [Copying Objects](CopyingObjectsExamples.md)\.

You can use the REST API \(see [Making Requests Using the REST API](RESTAPI.md)\) to work with objects or use one of the following AWS SDK libraries:
+ [AWS SDK for Java](https://aws.amazon.com/sdk-for-java/)
+ [AWS SDK for \.NET](https://aws.amazon.com/sdk-for-net/)
+ [AWS SDK for PHP](https://aws.amazon.com/sdk-for-php/)

These libraries provide a high\-level abstraction that makes working with objects easy\. However, if your application requires, you can use the REST API directly\.