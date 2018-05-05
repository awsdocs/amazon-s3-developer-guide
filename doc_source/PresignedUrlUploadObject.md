# Uploading Objects Using Pre\-Signed URLs<a name="PresignedUrlUploadObject"></a>

**Topics**
+ [Upload an Object Using a Pre\-Signed URL \(AWS SDK for Java\)](PresignedUrlUploadObjectJavaSDK.md)
+ [Upload an Object to an S3 Bucket Using a Pre\-Signed URL \(AWS SDK for \.NET\)](UploadObjectPreSignedURLDotNetSDK.md)
+ [Upload an Object Using a Pre\-Signed URL \(AWS SDK for Ruby\)](UploadObjectPreSignedURLRubySDK.md)

A pre\-signed URL gives you access to the object identified in the URL, provided that the creator of the pre\-signed URL has permissions to access that object\. That is, if you receive a pre\-signed URL to upload an object, you can upload the object only if the creator of the pre\-signed URL has the necessary permissions to upload that object\. 

All objects and buckets by default are private\. The pre\-signed URLs are useful if you want your user/customer to be able to upload a specific object to your bucket, but you don't require them to have AWS security credentials or permissions\. When you create a pre\-signed URL, you must provide your security credentials and then specify a bucket name, an object key, an HTTP method \(PUT for uploading objects\), and an expiration date and time\. The pre\-signed URLs are valid only for the specified duration\. 

You can generate a pre\-signed URL programmatically using the AWS SDK for Java or the AWS SDK for \.NET\. If you are using Microsoft Visual Studio, you can also use AWS Explorer to generate a pre\-signed object URL without writing any code\. Anyone who receives a valid pre\-signed URL can then programmatically upload an object\.

For more information, go to [Using Amazon S3 from AWS Explorer](http://docs.aws.amazon.com/AWSToolkitVS/latest/UserGuide/using-s3.html)\. 

For instructions about how to install AWS Explorer, see [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)\.

**Note**  
Anyone with valid security credentials can create a pre\-signed URL\. However, in order for you to successfully upload an object, the pre\-signed URL must be created by someone who has permission to perform the operation that the pre\-signed URL is based upon\.