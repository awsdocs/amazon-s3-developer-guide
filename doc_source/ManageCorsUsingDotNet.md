# Enabling Cross\-Origin Resource Sharing \(CORS\) Using the AWS SDK for \.NET<a name="ManageCorsUsingDotNet"></a>

You can use the AWS SDK for \.NET to manage cross\-origin resource sharing \(CORS\) for a bucket\. For more information about CORS, see [Cross\-Origin Resource Sharing \(CORS\)](cors.md)\.

This section provides sample code for the tasks in the following table, followed by a complete example program listing\.


**Managing Cross\-Origin Resource Sharing**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the `AmazonS3Client` class\.   | 
|  2  |  Create a new CORS configuration\.   | 
|  3  |  Retrieve and modify an existing CORS configuration\.   | 
|  4  |  Add the configuration to the bucket\.   | 


**Cross\-Origin Resource Sharing Methods**  

|  |  | 
| --- |--- |
|  [AmazonS3Client\(\)](http://docs.aws.amazon.com/sdkfornet/latest/apidocs/Index.html?page=TS3_S3NET4_5.html&tocid=Amazon_S3_AmazonS3Client)   |  Constructs `AmazonS3Client` with the credentials defined in the App\.config file\.   | 
|  [PutCORSConfiguration\(\)](http://docs.aws.amazon.com/sdkfornet/latest/apidocs/Index.html?page=TS3PutCORSConfigurationRequest_NET4_5.html&tocid=Amazon_S3_Model_PutCORSConfigurationRequest)   |  Sets the CORS configuration that should be applied to the bucket\. If a configuration already exists for the specified bucket, the new configuration will replace the existing one\.   | 
|  [GetCORSConfiguration\(\)](http://docs.aws.amazon.com/sdkfornet/latest/apidocs/Index.html?page=TS3GetCORSConfigurationRequest_NET4_5.html&tocid=Amazon_S3_Model_GetCORSConfigurationRequest)   |  Retrieves the CORS configuration for the specified bucket\. If no configuration has been set for the bucket, the `Configuration` header in the response will be null\.   | 
|  [DeleteCORSConfiguration\(\)](http://docs.aws.amazon.com/sdkfornet/latest/apidocs/Index.html?page=TS3DeleteCORSConfigurationRequest_NET4_5.html&tocid=Amazon_S3_Model_DeleteCORSConfigurationRequest)  |  Deletes the CORS configuration for the specified bucket\.   | 

For more information about the AWS SDK for \.NET API, go to [Using the AWS SDK for \.NET](UsingTheMPDotNetAPI.md)\.

**Creating an Instance of the AmazonS3 Class**  
The following sample creates an instance of the `AmazonS3Client` class\. 

**Example**  

```
1. static IAmazonS3 client;
2. using (client = new AmazonS3Client(Amazon.RegionEndpoint.USWest2))
```

**Adding a CORS Configuration to a Bucket**  
To add a CORS configuration to a bucket:

1. Create a `CORSConfiguration` object describing the rule\. 

1.  Create a `PutCORSConfigurationRequest` object that provides the bucket name and the CORS configuration\.

1.  Add the CORS configuration to the bucket by calling `client.PutCORSConfiguration`\. 

The following sample creates two rules, `CORSRule1` and `CORSRule2`, and then adds each rule to the `rules` array\. By using the `rules` array, it then adds the rules to the bucket `bucketName`\.

**Example**  

```
 1. // Add a sample configuration
 2. CORSConfiguration configuration = new CORSConfiguration
 3. {
 4.     Rules = new System.Collections.Generic.List<CORSRule>
 5.     {
 6.         new CORSRule
 7.         {
 8.             Id = "CORSRule1",
 9.             AllowedMethods = new List<string> {"PUT", "POST", "DELETE"},
10.             AllowedOrigins = new List<string> {"http://*.example.com"}
11.         },
12.         new CORSRule
13.         {
14.             Id = "CORSRule2",
15.             AllowedMethods = new List<string> {"GET"},
16.             AllowedOrigins = new List<string> {"*"},
17.             MaxAgeSeconds = 3000,
18.             ExposeHeaders = new List<string> {"x-amz-server-side-encryption"}
19.         }
20.     }
21. };
22. 
23. // Save the configuration
24. PutCORSConfiguration(configuration);
25. 
26. static void PutCORSConfiguration(CORSConfiguration configuration)
27. {
28. 
29.     PutCORSConfigurationRequest request = new PutCORSConfigurationRequest
30.     {
31.         BucketName = bucketName,
32.         Configuration = configuration
33.     };
34. 
35.     var response = client.PutCORSConfiguration(request);
36. }
```

****Updating an Existing CORS Configuration****  
To update an existing CORS configuration

1. Get a CORS configuration by calling the `client.GetCORSConfiguration` method\.

1. Update the configuration information by adding or deleting rules\.

1. Add the configuration to a bucket by calling the `client.PutCORSConfiguration` method\.

The following snippet gets an existing configuration and then adds a new rule with the ID `NewRule`\.

**Example**  

```
 1. // Get configuration.
 2. configuration = GetCORSConfiguration(); 
 3. // Add new rule.
 4. configuration.Rules.Add(new CORSRule
 5. {
 6.     Id = "NewRule",
 7.     AllowedMethods = new List<string> { "HEAD" },
 8.     AllowedOrigins = new List<string> { "http://www.example.com" }
 9. });
10. 
11. // Save configuration.
12. PutCORSConfiguration(configuration);
```

**Example Program Listing**  
The following C\# program incorporates the preceding tasks\.  
For information about creating and testing a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.   

```
using System;
using System.Configuration;
using System.Collections.Specialized;
using System.Net;
using Amazon.S3;
using Amazon.S3.Model;
using Amazon.S3.Util;
using System.Diagnostics;
using System.Collections.Generic;

namespace s3.amazon.com.docsamples
{
    class CORS
    {
        static string bucketName = "*** Provide bucket name ***"; 

        static IAmazonS3 client;

        public static void Main(string[] args)
        {
            try
            {
                using (client = new AmazonS3Client(Amazon.RegionEndpoint.USWest2))
                {
                    // Create a new configuration request and add two rules    
                    CORSConfiguration configuration = new CORSConfiguration
                    {
                        Rules = new System.Collections.Generic.List<CORSRule>
                        {
                          new CORSRule
                          {
                            Id = "CORSRule1",
                            AllowedMethods = new List<string> {"PUT", "POST", "DELETE"},
                            AllowedOrigins = new List<string> {"http://*.example.com"}
                          },
                          new CORSRule
                          {
                            Id = "CORSRule2",
                            AllowedMethods = new List<string> {"GET"},
                            AllowedOrigins = new List<string> {"*"},
                            MaxAgeSeconds = 3000,
                            ExposeHeaders = new List<string> {"x-amz-server-side-encryption"}
                          }
                        }
                    };

                    // Add the configuration to the bucket 
                    PutCORSConfiguration(configuration);

                    // Retrieve an existing configuration 
                    configuration = GetCORSConfiguration();

                    // Add a new rule.
                    configuration.Rules.Add(new CORSRule
                    {
                        Id = "CORSRule3",
                        AllowedMethods = new List<string> { "HEAD" },
                        AllowedOrigins = new List<string> { "http://www.example.com" }
                    });

                    // Add the configuration to the bucket 
                    PutCORSConfiguration(configuration);

                    // Verify that there are now three rules
                    configuration = GetCORSConfiguration();
                    Console.WriteLine(); 
                    Console.WriteLine("Expected # of rulest=3; found:{0}", configuration.Rules.Count);
                    Console.WriteLine();
                    Console.WriteLine("Pause before configuration delete. To continue, click Enter...");
                    Console.ReadKey();

                    // Delete the configuration
                    DeleteCORSConfiguration();

                    // Retrieve a nonexistent configuration
                    configuration = GetCORSConfiguration();
                    Debug.Assert(configuration == null);
                }

                Console.WriteLine("Example complete.");
            }
            catch (AmazonS3Exception amazonS3Exception)
            {
                Console.WriteLine("S3 error occurred. Exception: " + amazonS3Exception.ToString());
                Console.ReadKey();
            }
            catch (Exception e)
            {
                Console.WriteLine("Exception: " + e.ToString());
                Console.ReadKey();
            }
            
            Console.WriteLine("Press any key to continue...");
            Console.ReadKey();
        }

        static void PutCORSConfiguration(CORSConfiguration configuration)
        {

            PutCORSConfigurationRequest request = new PutCORSConfigurationRequest
            {
                BucketName = bucketName,
                Configuration = configuration
            };

            var response = client.PutCORSConfiguration(request);
        }

        static CORSConfiguration GetCORSConfiguration()
        {
            GetCORSConfigurationRequest request = new GetCORSConfigurationRequest
            {
                BucketName = bucketName

            };
            var response = client.GetCORSConfiguration(request);
            var configuration = response.Configuration;
            PrintCORSRules(configuration);
            return configuration;
        }

        static void DeleteCORSConfiguration()
        {
            DeleteCORSConfigurationRequest request = new DeleteCORSConfigurationRequest
            {
                BucketName = bucketName
            };
            client.DeleteCORSConfiguration(request);
        }

        static void PrintCORSRules(CORSConfiguration configuration)
        {
            Console.WriteLine();

            if (configuration == null)
            {
                Console.WriteLine("\nConfiguration is null");
                return;
            }

            Console.WriteLine("Configuration has {0} rules:", configuration.Rules.Count);
            foreach (CORSRule rule in configuration.Rules)
            {
                Console.WriteLine("Rule ID: {0}", rule.Id);
                Console.WriteLine("MaxAgeSeconds: {0}", rule.MaxAgeSeconds);
                Console.WriteLine("AllowedMethod: {0}", string.Join(", ", rule.AllowedMethods.ToArray()));
                Console.WriteLine("AllowedOrigins: {0}", string.Join(", ", rule.AllowedOrigins.ToArray()));
                Console.WriteLine("AllowedHeaders: {0}", string.Join(", ", rule.AllowedHeaders.ToArray()));
                Console.WriteLine("ExposeHeader: {0}", string.Join(", ", rule.ExposeHeaders.ToArray()));
            }
        }
    }
}
```