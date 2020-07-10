# Uploading objects using presigned URLs<a name="PresignedUrlUploadObject"></a>

A presigned URL gives you access to the object identified in the URL, provided that the creator of the presigned URL has permissions to access that object\. That is, if you receive a presigned URL to upload an object, you can upload the object only if the creator of the presigned URL has the necessary permissions to upload that object\. 

All objects and buckets by default are private\. The presigned URLs are useful if you want your user/customer to be able to upload a specific object to your bucket, but you don't require them to have AWS security credentials or permissions\. When you create a presigned URL, you must provide your security credentials and then specify a bucket name, an object key, an HTTP method \(PUT for uploading objects\), and an expiration date and time\. The presigned URLs are valid only for the specified duration\. That is, you must start the action before the expiration date and time\. If the action consists of multiple steps, such as a multipart upload, all steps must be started before the expiration, otherwise you will receive an error when Amazon S3 attempts to start a step with an expired URL\.

You can use the presigned URL multiple times, up to the expiration date and time\.

**Note**  
Anyone with valid security credentials can create a presigned URL\. However, in order for you to successfully upload an object, the presigned URL must be created by someone who has permission to perform the operation that the presigned URL is based upon\.

You can generate a presigned URL programmatically using the [REST API](https://docs.aws.amazon.com/AmazonS3/latest/API/sigv4-query-string-auth.html#query-string-auth-v4-signing-example), the [AWS Command Line Interface](https://docs.aws.amazon.com/cli/latest/reference/s3/presign.html), and the AWS SDK for Java, \.NET, Ruby, PHP, [Node\.js](https://docs.aws.amazon.com/AWSJavaScriptSDK/latest/AWS/S3.html#getSignedUrl-property), and [Python](http://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/s3.html#S3.Client.generate_presigned_url)\.

 If you are using Microsoft Visual Studio, you can also use AWS Explorer to generate a presigned object URL without writing any code\. Anyone who receives a valid presigned URL can then programmatically upload an object\. For more information, go to [Using Amazon S3 from AWS Explorer](https://docs.aws.amazon.com/AWSToolkitVS/latest/UserGuide/using-s3.html)\. For instructions about how to install AWS Explorer, see [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)\.

**Topics**
+ [Upload an object using a presigned URL \(AWS SDK for Java\)](PresignedUrlUploadObjectJavaSDK.md)
+ [Upload an object using a presigned URL \(AWS SDK for \.NET\)](UploadObjectPreSignedURLDotNetSDK.md)
+ [Upload an object using a presigned URL \(AWS SDK for Ruby\)](UploadObjectPreSignedURLRubySDK.md)
+ [Upload an object using a presigned URL \(AWS SDK for PHP\)](https://docs.aws.amazon.com/sdk-for-php/v3/developer-guide/s3-presigned-url.html)