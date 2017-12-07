# Managing Websites with the AWS SDK for Java<a name="ConfigWebSiteJava"></a>

The following tasks guide you through using the Java classes to manage website configuration to your bucket\. For more information about the Amazon S3 website feature, see [Hosting a Static Website on Amazon S3](WebsiteHosting.md)\.


**Managing Website Configuration**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `AmazonS3` class\.   | 
|  2  |  To add website configuration to a bucket, execute the `AmazonS3.setBucketWebsiteConfiguration` method\. You need to provide the bucket name and the website configuration information, including the index document and the error document names\. You must provide the index document, but the error document is optional\. You provide website configuration information by creating a `BucketWebsiteConfiguration` object\. To retrieve website configuration, execute the `AmazonS3.getBucketWebsiteConfiguration` method by providing the bucket name\. To delete your bucket website configuration, execute the `AmazonS3.deleteBucketWebsiteConfiguration` method by providing the bucket name\. After you remove the website configuration, the bucket is no longer available from the website endpoint\. For more information, see [Website Endpoints](WebsiteEndpoints.md)\.   | 

The following Java code sample demonstrates the preceding tasks\.

**Example**  

```
 1. AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
 2. // Add website configuration.
 3. s3Client.setBucketWebsiteConfiguration(bucketName, 
 4.     		new BucketWebsiteConfiguration(indexDoc , errorDoc));
 5.  
 6. // Get website configuration.
 7. BucketWebsiteConfiguration bucketWebsiteConfiguration = 
 8. 		       s3Client.getBucketWebsiteConfiguration(bucketName);
 9. 	
10. // Delete website configuration.
11. s3Client.deleteBucketWebsiteConfiguration(bucketName);
```

**Example**  
The following Java code example adds a website configuration to the specified bucket, retrieves it, and deletes the website configuration\. For instructions on how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\.  

```
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.BucketWebsiteConfiguration;



public class WebsiteConfiguration {
	private static String bucketName = "*** bucket name ***";
	private static String indexDoc   = "*** index document name ***";
	private static String errorDoc   = "*** error document name ***";
	
	public static void main(String[] args) throws IOException {
        AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
   
        try {
        	// Get existing website configuration, if any.
            getWebsiteConfig(s3Client);
    		
    		// Set new website configuration.
    		s3Client.setBucketWebsiteConfiguration(bucketName, 
    		   new BucketWebsiteConfiguration(indexDoc, errorDoc));
    		
    		// Verify (Get website configuration again).
            getWebsiteConfig(s3Client);
            
            // Delete
            s3Client.deleteBucketWebsiteConfiguration(bucketName);

       		// Verify (Get website configuration again)
              getWebsiteConfig(s3Client);
            
  
            
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
                    "a serious internal problem while trying to " +
                    "communicate with Amazon S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

	private static BucketWebsiteConfiguration getWebsiteConfig(
	                                              AmazonS3 s3Client) {
		System.out.println("Get website config");   
		
		// 1. Get website config.
		BucketWebsiteConfiguration bucketWebsiteConfiguration = 
			  s3Client.getBucketWebsiteConfiguration(bucketName);
		if (bucketWebsiteConfiguration == null)
		{
			System.out.println("No website config.");
		}
		else
		{
		     System.out.println("Index doc:" + 
		       bucketWebsiteConfiguration.getIndexDocumentSuffix());
		     System.out.println("Error doc:" + 
		       bucketWebsiteConfiguration.getErrorDocument());
		}
		return bucketWebsiteConfiguration;
	}
}
```