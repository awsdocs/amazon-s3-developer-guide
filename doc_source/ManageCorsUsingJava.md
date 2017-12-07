# Enabling Cross\-Origin Resource Sharing \(CORS\) Using the AWS SDK for Java<a name="ManageCorsUsingJava"></a>

You can use the AWS SDK for Java to manage cross\-origin resource sharing \(CORS\) for a bucket\. For more information about CORS, see [Cross\-Origin Resource Sharing \(CORS\)](cors.md)\.

This section provides sample code snippets for following tasks, followed by a complete example program demonstrating all tasks\.

+ Creating an instance of the Amazon S3 client class

+ Creating and adding a CORS configuration to a bucket

+ Updating an existing CORS configuration


**Cross\-Origin Resource Sharing Methods**  

|  |  | 
| --- |--- |
|  [AmazonS3Client\(\)](http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/AmazonS3Client.html)   |  Constructs an `AmazonS3Client` object\.   | 
|  [setBucketCrossOriginConfiguration\(\)](http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/AmazonS3Client.html#setBucketCrossOriginConfiguration(java.lang.String,%20com.amazonaws.services.s3.model.BucketCrossOriginConfiguration))   |  Sets the CORS configuration that to be applied to the bucket\. If a configuration already exists for the specified bucket, the new configuration will replace the existing one\.   | 
|  [getBucketCrossOriginConfiguration\(\)](http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/AmazonS3Client.html#getBucketCrossOriginConfiguration(java.lang.String))   |  Retrieves the CORS configuration for the specified bucket\. If no configuration has been set for the bucket, the `Configuration` header in the response will be null\.   | 
|  [deleteBucketCrossOriginConfiguration\(\)](http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/AmazonS3Client.html#deleteBucketCrossOriginConfiguration(java.lang.String))  |  Deletes the CORS configuration for the specified bucket\.   | 

For more information about the AWS SDK for Java API, go to [AWS SDK for Java API Reference ](http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/)\.

**Creating an Instance of the Amazon S3 Client Class**  
The following snippet creates a new `AmazonS3Client` instance for a class called `CORS_JavaSDK`\. This example retrieves the values for `accessKey` and `secretKey` from the AwsCredentials\.properties file\.

**Example**  

```
1. AmazonS3Client client;
2. client = new AmazonS3Client(new ProfileCredentialsProvider());
```

**Creating and Adding a CORS Configuration to a Bucket**  
To add a CORS configuration to a bucket:

1. Create a `CORSRule` object that describes the rule\. 

1. Create a `BucketCrossOriginConfiguration` object, and then add the rule to the configuration object\.

1. Add the CORS configuration to the bucket by calling the `client.setBucketCrossOriginConfiguration` method\. 

The following snippet creates two rules, `CORSRule1` and `CORSRule2`, and then adds each rule to the `rules` array\. By using the `rules` array, it then adds the rules to the bucket `bucketName`\.

**Example**  

```
 1. // Add a sample configuration
 2. BucketCrossOriginConfiguration configuration = new BucketCrossOriginConfiguration();
 3. 
 4. List<CORSRule> rules = new ArrayList<CORSRule>();
 5. 
 6. CORSRule rule1 = new CORSRule()
 7.     .withId("CORSRule1")
 8.     .withAllowedMethods(Arrays.asList(new CORSRule.AllowedMethods[] { 
 9.             CORSRule.AllowedMethods.PUT, CORSRule.AllowedMethods.POST, CORSRule.AllowedMethods.DELETE}))
10.     .withAllowedOrigins(Arrays.asList(new String[] {"http://*.example.com"}));
11. 
12. CORSRule rule2 = new CORSRule()
13. .withId("CORSRule2")
14. .withAllowedMethods(Arrays.asList(new CORSRule.AllowedMethods[] { 
15.         CORSRule.AllowedMethods.GET}))
16. .withAllowedOrigins(Arrays.asList(new String[] {"*"}))
17. .withMaxAgeSeconds(3000)
18. .withExposedHeaders(Arrays.asList(new String[] {"x-amz-server-side-encryption"}));
19. 
20. configuration.setRules(Arrays.asList(new CORSRule[] {rule1, rule2}));
21. 
22. // Save the configuration
23. client.setBucketCrossOriginConfiguration(bucketName, configuration);
```

****Updating an Existing CORS Configuration****  
To update an existing CORS configuration

1. Get a CORS configuration by calling the `client.getBucketCrossOriginConfiguration` method\.

1. Update the configuration information by adding or deleting rules to the list of rules\.

1. Add the configuration to a bucket by calling the `client.getBucketCrossOriginConfiguration` method\.

The following snippet gets an existing configuration and then adds a new rule with the ID `NewRule`\.

**Example**  

```
 1. // Get configuration.
 2. BucketCrossOriginConfiguration configuration = client.getBucketCrossOriginConfiguration(bucketName);
 3.  
 4. // Add new rule.
 5. CORSRule rule3 = new CORSRule()
 6. .withId("CORSRule3")
 7. .withAllowedMethods(Arrays.asList(new CORSRule.AllowedMethods[] { 
 8.         CORSRule.AllowedMethods.HEAD}))
 9. .withAllowedOrigins(Arrays.asList(new String[] {"http://www.example.com"}));
10. 
11. List<CORSRule> rules = configuration.getRules();
12. rules.add(rule3);
13. configuration.setRules(rules);
14. 
15. // Save configuration.
16. client.setBucketCrossOriginConfiguration(bucketName, configuration);
```

**Example Program Listing**  
The following Java program incorporates the preceding tasks\.   
For information about creating and testing a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\.  

```
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.BucketCrossOriginConfiguration;
import com.amazonaws.services.s3.model.CORSRule;

public class Cors {

    /**
     * @param args
     * @throws IOException 
     */
    public static AmazonS3Client client;
    public static String bucketName = "***provide bucket name***";
    
    public static void main(String[] args) throws IOException {
        client = new AmazonS3Client(new ProfileCredentialsProvider());

        // Create a new configuration request and add two rules
        BucketCrossOriginConfiguration configuration = new BucketCrossOriginConfiguration();
        
        List<CORSRule> rules = new ArrayList<CORSRule>();
        
        CORSRule rule1 = new CORSRule()
            .withId("CORSRule1")
            .withAllowedMethods(Arrays.asList(new CORSRule.AllowedMethods[] { 
                    CORSRule.AllowedMethods.PUT, CORSRule.AllowedMethods.POST, CORSRule.AllowedMethods.DELETE}))
            .withAllowedOrigins(Arrays.asList(new String[] {"http://*.example.com"}));
        
        CORSRule rule2 = new CORSRule()
        .withId("CORSRule2")
        .withAllowedMethods(Arrays.asList(new CORSRule.AllowedMethods[] { 
                CORSRule.AllowedMethods.GET}))
        .withAllowedOrigins(Arrays.asList(new String[] {"*"}))
        .withMaxAgeSeconds(3000)
        .withExposedHeaders(Arrays.asList(new String[] {"x-amz-server-side-encryption"}));
        
        configuration.setRules(Arrays.asList(new CORSRule[] {rule1, rule2}));
        
         // Add the configuration to the bucket. 
        client.setBucketCrossOriginConfiguration(bucketName, configuration);

        // Retrieve an existing configuration. 
        configuration = client.getBucketCrossOriginConfiguration(bucketName);
        printCORSConfiguration(configuration);
        
        // Add a new rule.
        CORSRule rule3 = new CORSRule()
        .withId("CORSRule3")
        .withAllowedMethods(Arrays.asList(new CORSRule.AllowedMethods[] { 
                CORSRule.AllowedMethods.HEAD}))
        .withAllowedOrigins(Arrays.asList(new String[] {"http://www.example.com"}));

        rules = configuration.getRules();
        rules.add(rule3);
        configuration.setRules(rules);
        client.setBucketCrossOriginConfiguration(bucketName, configuration);
        System.out.format("Added another rule: %s\n", rule3.getId());
        
        // Verify that the new rule was added.
        configuration = client.getBucketCrossOriginConfiguration(bucketName);
        System.out.format("Expected # of rules = 3, found %s", configuration.getRules().size());

        // Delete the configuration.
        client.deleteBucketCrossOriginConfiguration(bucketName);
        
        // Try to retrieve configuration.
        configuration = client.getBucketCrossOriginConfiguration(bucketName);
        System.out.println("\nRemoved CORS configuration.");
        printCORSConfiguration(configuration);
    }
    
    static void printCORSConfiguration(BucketCrossOriginConfiguration configuration)
    {

        if (configuration == null)
        {
            System.out.println("\nConfiguration is null.");
            return;
        }

        System.out.format("\nConfiguration has %s rules:\n", configuration.getRules().size());
        for (CORSRule rule : configuration.getRules())
        {
            System.out.format("Rule ID: %s\n", rule.getId());
            System.out.format("MaxAgeSeconds: %s\n", rule.getMaxAgeSeconds());
            System.out.format("AllowedMethod: %s\n", rule.getAllowedMethods().toArray());
            System.out.format("AllowedOrigins: %s\n", rule.getAllowedOrigins());
            System.out.format("AllowedHeaders: %s\n", rule.getAllowedHeaders());
            System.out.format("ExposeHeader: %s\n", rule.getExposedHeaders());
        }
    }
}
```