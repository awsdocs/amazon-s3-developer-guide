# Get an Object Using the AWS SDK for Java<a name="RetrievingObjectUsingJava"></a>

When you download an object through the AWS SDK for Java, Amazon S3 returns all of the object's metadata and an input stream from which to read the object's contents\. Your network connection remains open until you read all of the data or close the input stream, so we recommend that you read the content of the stream as quickly as possible\.

To retrieve an object, you do the following:
+ Execute the `AmazonS3Client.getObject()` method, providing the bucket name and object key in the request\.
+ Execute one of the `S3Object` methods to process the input stream\.

The following are some variations you might use:
+ Instead of reading the entire object, you can read only a portion of the object data by specifying the desired byte range in the request\.
+ You can optionally override the response header values \(see [Getting Objects](GettingObjectsUsingAPIs.md)\) by using a `ResponseHeaderOverrides` object and setting the corresponding request property\. For example, you can use this feature to indicate that the object should be downloaded into a file with a different filename than the object key name\.

The following example retrieves an object from an Amazon S3 bucket three ways: first, as a complete object, then as a range of bytes from the object, then as a complete object with overridden response header values\. For more information about getting objects from Amazon S3, see [GET Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html)\. For instructions on creating and testing a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\.

```
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.S3Object;

public class GetObject {

    public static void main(String[] args) throws IOException {
        String clientRegion = "*** Client region ***";
        String bucketName = "*** Bucket name ***";
        String key = "*** Object key ***";

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new ProfileCredentialsProvider())
                    .build();

            // Get an object and print its contents.
            System.out.println("Downloading an object");
            S3Object s3object = s3Client.getObject(new GetObjectRequest(bucketName, key));
            System.out.println("Content-Type: " + s3object.getObjectMetadata().getContentType());
            System.out.println("Content: ");
            displayTextInputStream(s3object.getObjectContent());
    
            // Get a range of bytes from an object and print the bytes.
            GetObjectRequest rangeObjectRequest = new GetObjectRequest(bucketName, key)
                                                        .withRange(0,9);
            S3Object objectPortion = s3Client.getObject(rangeObjectRequest);
            System.out.println("Printing bytes retrieved.");
            displayTextInputStream(objectPortion.getObjectContent());
            
            // Get an entire object, overriding the specified response headers, and print the object's content.
            ResponseHeaderOverrides headerOverrides = new ResponseHeaderOverrides()
                                                            .withCacheControl("No-cache")
                                                            .withContentDisposition("attachment; filename=example.txt");
            GetObjectRequest getObjectRequestHeaderOverride = new GetObjectRequest(bucketName, key)
                                                            .withResponseHeaders(headerOverrides);
            S3Object headerOverrideObject = s3Client.getObject(getObjectRequestHeaderOverride);
            displayTextInputStream(headerOverrideObject.getObjectContent());
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }

    private static void displayTextInputStream(InputStream input) throws IOException {
        // Read the text input stream one line at a time and display each line.
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = null;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        System.out.println();
    }
}
```