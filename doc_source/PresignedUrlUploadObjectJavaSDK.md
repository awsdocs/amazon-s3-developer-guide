# Upload an Object Using a Pre\-Signed URL \(AWS SDK for Java\)<a name="PresignedUrlUploadObjectJavaSDK"></a>

The following tasks guide you through using the Java classes to upload an object using a pre\-signed URL\.


**Uploading Objects**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `AmazonS3` class\.   | 
|  2  |  Generate a pre\-signed URL by executing the `AmazonS3.generatePresignedUrl` method\. You provide a bucket name, an object key, and an expiration date by creating an instance of the `GeneratePresignedUrlRequest` class\. You must specify the HTTP verb PUT when creating this URL if you want to use it to upload an object\.  | 
|  3  |  Anyone with the pre\-signed URL can upload an object\.  The upload creates an object or replaces any existing object with the same key that is specified in the pre\-signed URL\.  | 

The following Java code example demonstrates the preceding tasks\.

**Example**  

```
 1. AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider()); 
 2. 
 3. java.util.Date expiration = new java.util.Date();
 4. long msec = expiration.getTime();
 5. msec += 1000 * 60 * 60; // Add 1 hour.
 6. expiration.setTime(msec);
 7. 
 8. GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey);
 9. generatePresignedUrlRequest.setMethod(HttpMethod.PUT); 
10. generatePresignedUrlRequest.setExpiration(expiration);
11.              
12. URL s = s3client.generatePresignedUrl(generatePresignedUrlRequest); 
13. 
14. // Use the pre-signed URL to upload an object.
```

**Example**  
The following Java code example generates a pre\-signed URL\. The example code then uses the pre\-signed URL to upload sample data as an object\. For instructions about how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\.  

```
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

public class GeneratePresignedUrlAndUploadObject {
	private static String bucketName = "*** bucket name ***"; 
	private static String objectKey  = "*** object key ***";

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
			generatePresignedUrlRequest.setMethod(HttpMethod.PUT); 
			generatePresignedUrlRequest.setExpiration(expiration);

			URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest); 

			UploadObject(url);

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

	public static void UploadObject(URL url) throws IOException
	{
		HttpURLConnection connection=(HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("PUT");
		OutputStreamWriter out = new OutputStreamWriter(
				connection.getOutputStream());
		out.write("This text uploaded as object.");
		out.close();
		int responseCode = connection.getResponseCode();
		System.out.println("Service returned response code " + responseCode);

	}
}
```