# Share an object with others<a name="ShareObjectPreSignedURL"></a>

All objects by default are private\. Only the object owner has permission to access these objects\. However, the object owner can optionally share objects with others by creating a presigned URL, using their own security credentials, to grant time\-limited permission to download the objects\. 

When you create a presigned URL for your object, you must provide your security credentials, specify a bucket name, an object key, specify the HTTP method \(GET to download the object\) and expiration date and time\. The presigned URLs are valid only for the specified duration\. 

Anyone who receives the presigned URL can then access the object\. For example, if you have a video in your bucket and both the bucket and the object are private, you can share the video with others by generating a presigned URL\. 

**Note**  
Anyone with valid security credentials can create a presigned URL\. However, in order to successfully access an object, the presigned URL must be created by someone who has permission to perform the operation that the presigned URL is based upon\.
The credentials that you can use to create a presigned URL include:  
IAM instance profile: Valid up to 6 hours
AWS Security Token Service : Valid up to 36 hours when signed with permanent credentials, such as the credentials of the AWS account root user or an IAM user
IAM user: Valid up to 7 days when using AWS Signature Version 4  
To create a presigned URL that's valid for up to 7 days, first designate IAM user credentials \(the access key and secret access key\) to the SDK that you're using\. Then, generate a presigned URL using AWS Signature Version 4\.
If you created a presigned URL using a temporary token, then the URL expires when the token expires, even if the URL was created with a later expiration time\.

You can generate a presigned URL programmatically using the [REST API](https://docs.aws.amazon.com/AmazonS3/latest/API/sigv4-query-string-auth.html#query-string-auth-v4-signing-example), the [AWS Command Line Interface](https://docs.aws.amazon.com/cli/latest/reference/s3/presign.html), and the AWS SDK for Java, \.NET, [Ruby](https://docs.aws.amazon.com/sdk-for-ruby/v3/api/Aws/S3/Presigner.html), [PHP](https://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.S3.S3Client.html#_createPresignedRequest), [Node\.js](https://docs.aws.amazon.com/AWSJavaScriptSDK/latest/AWS/S3.html#getSignedUrl-property), and [Python](http://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/s3.html#S3.Client.generate_presigned_url)\.

**Topics**
+ [Generate a presigned object URL using AWS explorer for Visual Studio](ShareObjectPreSignedURLVSExplorer.md)
+ [Generate a presigned object URL using the AWS SDK for Java](ShareObjectPreSignedURLJavaSDK.md)
+ [Generate a presigned object URL using AWS SDK for \.NET](ShareObjectPreSignedURLDotNetSDK.md)