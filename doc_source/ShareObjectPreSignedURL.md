# Share an Object with Others<a name="ShareObjectPreSignedURL"></a>

**Topics**
+ [Generate a Pre\-signed Object URL using AWS Explorer for Visual Studio](ShareObjectPreSignedURLVSExplorer.md)
+ [Generate a Pre\-signed Object URL Using the AWS SDK for Java](ShareObjectPreSignedURLJavaSDK.md)
+ [Generate a Pre\-signed Object URL Using AWS SDK for \.NET](ShareObjectPreSignedURLDotNetSDK.md)

All objects by default are private\. Only the object owner has permission to access these objects\. However, the object owner can optionally share objects with others by creating a pre\-signed URL, using their own security credentials, to grant time\-limited permission to download the objects\. 

When you create a pre\-signed URL for your object, you must provide your security credentials, specify a bucket name, an object key, specify the HTTP method \(GET to download the object\) and expiration date and time\. The pre\-signed URLs are valid only for the specified duration\. 

Anyone who receives the pre\-signed URL can then access the object\. For example, if you have a video in your bucket and both the bucket and the object are private, you can share the video with others by generating a pre\-signed URL\. 

**Note**  
Anyone with valid security credentials can create a pre\-signed URL\. However, in order to successfully access an object, the pre\-signed URL must be created by someone who has permission to perform the operation that the pre\-signed URL is based upon\.

You can generate pre\-signed URL programmatically using the AWS SDK for Java and \.NET\. 