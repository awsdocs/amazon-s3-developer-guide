# Managing Websites with the AWS SDK for Java<a name="ConfigWebSiteJava"></a>

The following example shows how to use the AWS SDK for Java to manage website configuration for a bucket\. To add a website configuration to a bucket, you provide a bucket name and a website configuration\. The website configuration must include an index document and can include an optional error document\. These documents must already exist in the bucket\. For more information, see [PUT Bucket website](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTwebsite.html)\. For more information about the Amazon S3 website feature, see [Hosting a Static Website on Amazon S3](WebsiteHosting.md)\. 

**Example**  
The following example uses the AWS SDK for Java to add a website configuration to a bucket, retrieve and print the configuration, and then delete the configuration and verify the deletion\. For instructions on how to create and test a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\.   

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

import java.io.IOException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketWebsiteConfiguration;

public class WebsiteConfiguration {

    public static void main(String[] args) throws IOException {
        String clientRegion = "*** Client region ***";
        String bucketName = "*** Bucket name ***";
        String indexDocName = "*** Index document name ***";
        String errorDocName = "*** Error document name ***";

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new ProfileCredentialsProvider())
                    .build();

            // Print the existing website configuration, if it exists.
            printWebsiteConfig(s3Client, bucketName);
    
            // Set the new website configuration.
            s3Client.setBucketWebsiteConfiguration(bucketName, new BucketWebsiteConfiguration(indexDocName, errorDocName));
    
            // Verify that the configuration was set properly by printing it.
            printWebsiteConfig(s3Client, bucketName);
    
            // Delete the website configuration.
            s3Client.deleteBucketWebsiteConfiguration(bucketName);
    
            // Verify that the website configuration was deleted by printing it.
            printWebsiteConfig(s3Client, bucketName);
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

    private static void printWebsiteConfig(AmazonS3 s3Client, String bucketName) {
        System.out.println("Website configuration: ");
        BucketWebsiteConfiguration bucketWebsiteConfig = s3Client.getBucketWebsiteConfiguration(bucketName);
        if (bucketWebsiteConfig == null) {
            System.out.println("No website config.");
        } else {
            System.out.println("Index doc: " + bucketWebsiteConfig.getIndexDocumentSuffix());
            System.out.println("Error doc: " + bucketWebsiteConfig.getErrorDocument());
        }
    }
}
```