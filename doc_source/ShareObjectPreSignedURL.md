# Share an Object with Others<a name="ShareObjectPreSignedURL"></a>

**Topics**
+ [Generate a Presigned Object URL using AWS Explorer for Visual Studio](ShareObjectPreSignedURLVSExplorer.md)
+ [Generate a presigned Object URL Using the AWS SDK for Java](ShareObjectPreSignedURLJavaSDK.md)
+ [Generate a Presigned Object URL Using AWS SDK for \.NET](ShareObjectPreSignedURLDotNetSDK.md)

All objects by default are private\. Only the object owner has permission to access these objects\. However, the object owner can optionally share objects with others by creating a presigned URL, using their own security credentials, to grant time\-limited permission to download the objects\. 

When you create a presigned URL for your object, you must provide your security credentials, specify a bucket name, an object key, specify the HTTP method \(GET to download the object\) and expiration date and time\. The presigned URLs are valid only for the specified duration\. 

Anyone who receives the presigned URL can then access the object\. For example, if you have a video in your bucket and both the bucket and the object are private, you can share the video with others by generating a presigned URL\. 

**Note**  
Anyone with valid security credentials can create a presigned URL\. However, in order to successfully access an object, the presigned URL must be created by someone who has permission to perform the operation that the presigned URL is based upon\.

You can generate presigned URL programmatically using the AWS SDK for Java and \.NET\. 