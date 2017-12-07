# Generate a Pre\-signed Object URL using AWS SDK for Java<a name="ShareObjectPreSignedURLJavaSDK"></a>

The following tasks guide you through using the Java classes to generate a pre\-signed URL\.


**Downloading Objects**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `AmazonS3` class\. For information about providing credentials, see [Using the AWS SDK for Java](UsingTheMPDotJavaAPI.md)\. These credentials are used in creating a signature for authentication when you generate a pre\-signed URL\.  | 
|  2  |  Execute the `AmazonS3.generatePresignedUrl` method to generate a pre\-signed URL\. You provide information including a bucket name, an object key, and an expiration date by creating an instance of the `GeneratePresignedUrlRequest` class\. The request by default sets the verb to GET\. To use the pre\-signed URL for other operations, for example PUT, you must explicitly set the `verb` when you create the request\.  | 

The following Java code sample demonstrates the preceding tasks\.

**Example**  

```
 1. AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider()); 
 2.        
 3. java.util.Date expiration = new java.util.Date();
 4. long msec = expiration.getTime();
 5. msec += 1000 * 60 * 60; // 1 hour.
 6. expiration.setTime(msec);
 7.              
 8. GeneratePresignedUrlRequest generatePresignedUrlRequest = 
 9.               new GeneratePresignedUrlRequest(bucketName, objectKey);
10. generatePresignedUrlRequest.setMethod(HttpMethod.GET); // Default.
11. generatePresignedUrlRequest.setExpiration(expiration);
12.              
13. URL s = s3client.generatePresignedUrl(generatePresignedUrlRequest);
```

**Example**  
The following Java code example generates a pre\-signed URL that you can give to others so that they can retrieve the object\. You can use the generated pre\-signed URL to retrieve the object\. To use the pre\-signed URL for other operations, such as put an object, you must explicitly set the `verb` in the `GetPreSignedUrlRequest`\. For instructions about how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\.  

```
import java.io.IOException;
import java.net.URL;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

public class GeneratePreSignedUrl {
	private static String bucketName = "*** Provide a bucket name ***"; 
	private static String objectKey  =  "*** Provide an object key ***";

	public static void main(String[] args) throws IOException {
		AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());

		try {
			System.out.println("Generating pre-signed URL.");
			java.util.Date expiration = new java.util.Date();
			long milliSeconds = expiration.getTime();
			milliSeconds += 1000 * 60 * 60; // Add 1 hour.
			expiration.setTime(milliSeconds);

			GeneratePresignedUrlRequest generatePresignedUrlRequest = 
				    new GeneratePresignedUrlRequest(bucketName, objectKey);
			generatePresignedUrlRequest.setMethod(HttpMethod.GET); 
			generatePresignedUrlRequest.setExpiration(expiration);

			URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest); 

			System.out.println("Pre-Signed URL = " + url.toString());
		} catch (AmazonServiceException exception) {
			System.out.println("Caught an AmazonServiceException, " +
					"which means your request made it " +
					"to Amazon S3, but was rejected with an error response " +
			"for some reason.");
			System.out.println("Error Message: " + exception.getMessage());
			System.out.println("HTTP  Code: "    + exception.getStatusCode());
			System.out.println("AWS Error Code:" + exception.getErrorCode());
			System.out.println("Error Type:    " + exception.getErrorType());
			System.out.println("Request ID:    " + exception.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, " +
					"which means the client encountered " +
					"an internal error while trying to communicate" +
					" with S3, " +
			"such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}
}
```