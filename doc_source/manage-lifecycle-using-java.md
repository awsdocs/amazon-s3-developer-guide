# Manage Object Lifecycle Using the AWS SDK for Java<a name="manage-lifecycle-using-java"></a>

You can use the AWS SDK for Java to manage lifecycle configuration on a bucket\. For more information about managing lifecycle configuration, see [Object Lifecycle Management](object-lifecycle-mgmt.md)\.

The example code in this topic does the following: 

+ Add lifecycle configuration with the following two rules:

  + A rule that applies to objects with the `glacierobjects/` key name prefix\. The rule specifies a transition action that directs Amazon S3 to transition these objects to the GLACIER storage class\. Because the number of days specified is 0, the objects become eligible for archival immediately\.

  + A rule that applies to objects having tags with tag key `archive` and value `true`\. The rule specifies two transition actions, directing Amazon S3 to first transition objects to the STANDARD\_IA \(IA, for infrequent access\) storage class 30 days after creation, and then transition to the GLACIER storage class 365 days after creation\. The rule also specifies expiration action directing Amazon S3 to delete these objects 3650 days after creation\.

+ Retrieves the lifecycle configuration\.

+ Updates the configuration by adding another rule that applies to objects with the `YearlyDocuments/` key name prefix\. The expiration action in this rule directs Amazon S3 to delete these objects 3650 days after creation\.
**Note**  
When you add a lifecycle configuration to a bucket, any existing lifecycle configuration is replaced\. To update existing lifecycle configuration, you must first retrieve the existing lifecycle configuration, make changes and then add the revised lifecycle configuration to the bucket\.

**Example Java Code Example**  
The following Java code example provides a complete code listing that adds, updates, and deletes a lifecycle configuration to a bucket\. You need to update the code and provide your bucket name to which the code can add the example lifecycle configuration\.  
For instructions on how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\.  

```
 1. import java.io.IOException;
 2. import java.util.Arrays;
 3. 
 4. import com.amazonaws.auth.profile.ProfileCredentialsProvider;
 5. import com.amazonaws.services.s3.AmazonS3Client;
 6. import com.amazonaws.services.s3.model.AmazonS3Exception;
 7. import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
 8. import com.amazonaws.services.s3.model.BucketLifecycleConfiguration.Transition;
 9. import com.amazonaws.services.s3.model.StorageClass;
10. import com.amazonaws.services.s3.model.Tag;
11. import com.amazonaws.services.s3.model.lifecycle.LifecycleAndOperator;
12. import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;
13. import com.amazonaws.services.s3.model.lifecycle.LifecyclePrefixPredicate;
14. import com.amazonaws.services.s3.model.lifecycle.LifecycleTagPredicate;
15. 
16. public class LifecycleConfiguration {
17.     public static String bucketName = "*** Provide bucket name ***";
18.     public static AmazonS3Client s3Client;
19. 
20.     public static void main(String[] args) throws IOException {
21.         s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
22.         try {
23. 
24.             BucketLifecycleConfiguration.Rule rule1 =
25.             new BucketLifecycleConfiguration.Rule()
26.             .withId("Archive immediately rule")
27.             .withFilter(new LifecycleFilter(
28.                     new LifecyclePrefixPredicate("glacierobjects/")))
29.             .addTransition(new Transition()
30.                     .withDays(0)
31.                     .withStorageClass(StorageClass.Glacier))
32.             .withStatus(BucketLifecycleConfiguration.ENABLED.toString());
33. 
34.             BucketLifecycleConfiguration.Rule rule2 =
35.                 new BucketLifecycleConfiguration.Rule()
36.                 .withId("Archive and then delete rule")
37.                 .withFilter(new LifecycleFilter(
38.                         new LifecycleTagPredicate(new Tag("archive", "true"))))
39.                 .addTransition(new Transition()
40.                         .withDays(30)
41.                         .withStorageClass(StorageClass.StandardInfrequentAccess))
42.                 .addTransition(new Transition()
43.                         .withDays(365)
44.                         .withStorageClass(StorageClass.Glacier))
45.                 .withExpirationInDays(3650)
46.                 .withStatus(BucketLifecycleConfiguration.ENABLED.toString());
47. 
48.             BucketLifecycleConfiguration configuration =
49.             new BucketLifecycleConfiguration()
50.                 .withRules(Arrays.asList(rule1, rule2));
51. 
52.             // Save configuration.
53.             s3Client.setBucketLifecycleConfiguration(bucketName, configuration);
54. 
55.             // Retrieve configuration.
56.             configuration = s3Client.getBucketLifecycleConfiguration(bucketName);
57. 
58.             // Add a new rule.
59.             configuration.getRules().add(
60.                 new BucketLifecycleConfiguration.Rule()
61.                     .withId("NewRule")
62.                     .withFilter(new LifecycleFilter(
63.                         new LifecycleAndOperator(Arrays.asList(
64.                             new LifecyclePrefixPredicate("YearlyDocuments/"),
65.                             new LifecycleTagPredicate(new Tag("expire_after", "ten_years"))))))
66.                     .withExpirationInDays(3650)
67.                     .withStatus(BucketLifecycleConfiguration.
68.                         ENABLED.toString())
69.                 );
70. 
71.             // Save configuration.
72.             s3Client.setBucketLifecycleConfiguration(bucketName, configuration);
73. 
74.             // Retrieve configuration.
75.             configuration = s3Client.getBucketLifecycleConfiguration(bucketName);
76. 
77.             // Verify there are now three rules.
78.             configuration = s3Client.getBucketLifecycleConfiguration(bucketName);
79.             System.out.format("Expected # of rules = 3; found: %s\n",
80.                 configuration.getRules().size());
81. 
82.             System.out.println("Deleting lifecycle configuration. Next, we verify deletion.");
83.             // Delete configuration.
84.             s3Client.deleteBucketLifecycleConfiguration(bucketName);
85. 
86.             // Retrieve nonexistent configuration.
87.             configuration = s3Client.getBucketLifecycleConfiguration(bucketName);
88.             String s = (configuration == null) ? "No configuration found." : "Configuration found.";
89.             System.out.println(s);
90. 
91.         } catch (AmazonS3Exception amazonS3Exception) {
92.             System.out.format("An Amazon S3 error occurred. Exception: %s", amazonS3Exception.toString());
93.         } catch (Exception ex) {
94.             System.out.format("Exception: %s", ex.toString());
95.         }
96.     }
97. }
```