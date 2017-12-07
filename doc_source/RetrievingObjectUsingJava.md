# Get an Object Using the AWS SDK for Java<a name="RetrievingObjectUsingJava"></a>

When you download an object, you get all of object's metadata and a stream from which to read the contents\. You should read the content of the stream as quickly as possible because the data is streamed directly from Amazon S3 and your network connection will remain open until you read all the data or close the input stream\.


**Downloading Objects**  

|  |  | 
| --- |--- |
| 1 | Create an instance of the `AmazonS3Client` class\.  | 
| 2 | Execute one of the `AmazonS3Client.getObject()` method\. You need to provide the request information, such as bucket name, and key name\. You provide this information by creating an instance of the `GetObjectRequest` class\.  | 
| 3 | Execute one of the `getObjectContent()` methods on the object returned to get a stream on the object data and process the response\.  | 

The following Java code example demonstrates the preceding tasks\.

**Example**  

```
1. AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());        
2. S3Object object = s3Client.getObject(
3.                   new GetObjectRequest(bucketName, key));
4. InputStream objectData = object.getObjectContent();
5. // Process the objectData stream.
6. objectData.close();
```

The `GetObjectRequest` object provides several options, including conditional downloading of objects based on modification times, ETags, and selectively downloading a range of an object\. The following Java code example demonstrates how you can specify a range of data bytes to retrieve from an object\.

**Example**  

```
 1. AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());        
 2. 
 3. GetObjectRequest rangeObjectRequest = new GetObjectRequest(
 4. 		bucketName, key);
 5. rangeObjectRequest.setRange(0, 10); // retrieve 1st 11 bytes.
 6. S3Object objectPortion = s3Client.getObject(rangeObjectRequest);
 7. 
 8. InputStream objectData = objectPortion.getObjectContent();
 9. // Process the objectData stream.
10. objectData.close();
```

When retrieving an object, you can optionally override the response header values \(see [Getting Objects](GettingObjectsUsingAPIs.md)\) by using the `ResponseHeaderOverrides` object and setting the corresponding request property, as shown in the following Java code example\.

**Example**  

```
1. GetObjectRequest request = new GetObjectRequest(bucketName, key);
2.             
3. ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
4. responseHeaders.setCacheControl("No-cache");
5. responseHeaders.setContentDisposition("attachment; filename=testing.txt");
6. 
7. // Add the ResponseHeaderOverides to the request.
8. request.setResponseHeaders(responseHeaders);
```

**Example**  
The following Java code example retrieves an object from a specified Amazon S3 bucket\. For instructions on how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\.  

```
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;


public class GetObject {
	private static String bucketName = "*** provide bucket name ***"; 
	private static String key        = "*** provide object key ***";      
	
	public static void main(String[] args) throws IOException {
        AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
        try {
            System.out.println("Downloading an object");
            S3Object s3object = s3Client.getObject(new GetObjectRequest(
            		bucketName, key));
            System.out.println("Content-Type: "  + 
            		s3object.getObjectMetadata().getContentType());
            displayTextInputStream(s3object.getObjectContent());
            
           // Get a range of bytes from an object.
            
            GetObjectRequest rangeObjectRequest = new GetObjectRequest(
            		bucketName, key);
            rangeObjectRequest.setRange(0, 10);
            S3Object objectPortion = s3Client.getObject(rangeObjectRequest);
            
            System.out.println("Printing bytes retrieved.");
            displayTextInputStream(objectPortion.getObjectContent());
            
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which" +
            		" means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means"+
            		" the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

    private static void displayTextInputStream(InputStream input)
    throws IOException {
    	// Read one text line at a time and display.
        BufferedReader reader = new BufferedReader(new 
        		InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;

            System.out.println("    " + line);
        }
        System.out.println();
    }
}
```