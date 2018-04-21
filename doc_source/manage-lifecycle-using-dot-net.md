# Manage an Object's Lifecycle Using the AWS SDK for \.NET<a name="manage-lifecycle-using-dot-net"></a>

You can use the AWS SDK for \.NET to manage lifecycle configuration on a bucket\. For more information about managing lifecycle configuration, see [Object Lifecycle Management](object-lifecycle-mgmt.md)\. 

**Example \.NET Code Example**  
The following C\# code example adds lifecycle configuration to a bucket\. The example shows two lifecycle configurations:  
+ Lifecycle configuration that uses only prefix to select a subset of objects to which the rule applies\.
+ Lifecycle configuration that uses a prefix and object tags to select a subset of objects to which the rule applies\.
The lifecycle rule transitions objects to the GLACIER storage class soon after the objects are created\.  
The following code works with the latest version of the \.NET SDK\.  
For instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.  

```
  1. using System;
  2. using System.Collections.Generic;
  3. using System.Diagnostics;
  4. using Amazon.S3;
  5. using Amazon.S3.Model;
  6. 
  7. namespace aws.amazon.com.s3.documentation
  8. {
  9.     class LifeCycleTest
 10.     {
 11.         static string bucketName = "*** bucket name ***";
 12. 
 13.         public static void Main(string[] args)
 14.         {
 15.             try
 16.             {
 17.                 using (var client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1))
 18.                 {
 19.                     // 1. Add lifecycle config with prefix only.
 20.                     var lifeCycleConfigurationA = LifecycleConfig1();
 21. 
 22.                     // Add the configuration to the bucket 
 23.                     PutLifeCycleConfiguration(client, lifeCycleConfigurationA);
 24. 
 25.                     // Retrieve an existing configuration 
 26.                     var lifeCycleConfiguration = GetLifeCycleConfiguration(client);
 27. 
 28. 
 29.                     // 2. Add lifecycle config with prefix and tags.
 30.                     var lifeCycleConfigurationB = LifecycleConfig2();
 31. 
 32.                     // Add the configuration to the bucket 
 33.                     PutLifeCycleConfiguration(client, lifeCycleConfigurationB);
 34. 
 35.                     // Retrieve an existing configuration 
 36.                     lifeCycleConfiguration = GetLifeCycleConfiguration(client);
 37. 
 38.                     // 3. Delete lifecycle config.
 39.                     DeleteLifecycleConfiguration(client);
 40. 
 41.                     // 4. Retrieve a nonexistent configuration
 42.                     lifeCycleConfiguration = GetLifeCycleConfiguration(client);
 43.                     Debug.Assert(lifeCycleConfiguration == null);
 44.                 }
 45. 
 46.                 Console.WriteLine("Example complete. To continue, click Enter...");
 47.                 Console.ReadKey();
 48.             }
 49.             catch (AmazonS3Exception amazonS3Exception)
 50.             {
 51.                 Console.WriteLine("S3 error occurred. Exception: " + amazonS3Exception.ToString());
 52.             }
 53.             catch (Exception e)
 54.             {
 55.                 Console.WriteLine("Exception: " + e.ToString());
 56.             }
 57.         }
 58. 
 59.         private static LifecycleConfiguration LifecycleConfig1()
 60.         {
 61.             var lifeCycleConfiguration = new LifecycleConfiguration()
 62.             {
 63.                 Rules = new List<LifecycleRule>
 64.                         {
 65.                              new LifecycleRule
 66.                             {
 67.                                 Id = "Rule-1",
 68.                                 Filter = new LifecycleFilter()
 69.                                 {
 70.                                     LifecycleFilterPredicate = new LifecyclePrefixPredicate
 71.                                     { 
 72.                                         Prefix = "glacier/"
 73.                                     }
 74.                                 },
 75.                                 Status = LifecycleRuleStatus.Enabled,
 76.                                 Transitions = new List<LifecycleTransition>
 77.                                 {
 78.                                     new LifecycleTransition
 79.                                     {
 80.                                         Days = 0,
 81.                                         StorageClass = S3StorageClass.Glacier
 82.                                     }
 83.                                 },
 84.                             }
 85.                         }
 86.             };
 87.             return lifeCycleConfiguration;
 88.         }
 89. 
 90.         private static LifecycleConfiguration LifecycleConfig2()
 91.         {
 92.             var lifeCycleConfiguration = new LifecycleConfiguration()
 93.             {
 94.                 Rules = new List<LifecycleRule>
 95.                         {
 96.                              new LifecycleRule
 97.                             {
 98.                                 Id = "Rule-1",
 99.                                 Filter = new LifecycleFilter()
100.                                 {
101.                                     LifecycleFilterPredicate  = new LifecycleAndOperator
102.                                     {
103.                                         Operands = new List<LifecycleFilterPredicate>
104.                                         {
105.                                             new LifecyclePrefixPredicate
106.                                             {
107.                                                 Prefix = "glacierobjects/"
108.                                             },
109.                                             new LifecycleTagPredicate
110.                                             {
111.                                                 Tag = new Tag()
112.                                                 {
113.                                                     Key = "tagKey1",
114.                                                     Value = "tagValue1"
115.                                                 }
116.                                             },
117.                                              new LifecycleTagPredicate
118.                                             {
119.                                                 Tag = new Tag()
120.                                                 {
121.                                                     Key = "tagKey2",
122.                                                     Value = "tagValue2"
123.                                                 }
124.                                             }
125.                                         }
126.                                     }
127.                                 },
128.                                 Status = LifecycleRuleStatus.Enabled,
129.                                 Transitions = new List<LifecycleTransition>
130.                                                  {
131.                                                       new LifecycleTransition
132.                                                       {
133.                                                            Days = 0,
134.                                                            StorageClass = S3StorageClass.Glacier
135.                                                       }
136.                                                   },
137.                             }
138.                         }
139.             };
140.             return lifeCycleConfiguration;
141.         }
142. 
143.         static void PutLifeCycleConfiguration(IAmazonS3 client, LifecycleConfiguration configuration)
144.         {
145. 
146.             PutLifecycleConfigurationRequest request = new PutLifecycleConfigurationRequest
147.             {
148.                 BucketName = bucketName,
149.                 Configuration = configuration
150.             };
151. 
152.             var response = client.PutLifecycleConfiguration(request);
153.         }
154. 
155.         static LifecycleConfiguration GetLifeCycleConfiguration(IAmazonS3 client)
156.         {
157.             GetLifecycleConfigurationRequest request = new GetLifecycleConfigurationRequest
158.             {
159.                 BucketName = bucketName
160. 
161.             };
162.             var response = client.GetLifecycleConfiguration(request);
163.             var configuration = response.Configuration;
164.             return configuration;
165.         }
166. 
167.         static void DeleteLifecycleConfiguration(IAmazonS3 client)
168.         {
169.             DeleteLifecycleConfigurationRequest request = new DeleteLifecycleConfigurationRequest
170.             {
171.                 BucketName = bucketName
172.             };
173.             client.DeleteLifecycleConfiguration(request);
174.         }
175.     }
176. }
```