# Making Requests Using IAM User Temporary Credentials \- AWS SDK for \.NET<a name="AuthUsingTempSessionTokenDotNet"></a>

An IAM user or an AWS Account can request temporary security credentials \(see [Making Requests](MakingRequests.md)\) using the AWS SDK for \.NET and use them to access Amazon S3\. These credentials expire after the session duration\. By default, the session duration is one hour\. If you use IAM user credentials, you can specify duration, between 1 and 36 hours, when requesting the temporary security credentials\. 


**Making Requests Using IAM User Temporary Security Credentials**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the AWS Security Token Service client, `AmazonSecurityTokenServiceClient`\. For information about providing credentials, see [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)\.  | 
|  2  |  Start a session by calling the `GetSessionToken` method of the STS client you created in the preceding step\. You provide session information to this method using a `GetSessionTokenRequest` object\. The method returns you temporary security credentials\.  | 
|  3  |  Package up the temporary security credentials in an instance of the `SessionAWSCredentials` object\. You use this object to provide the temporary security credentials to your Amazon S3 client\.  | 
|  4  |  Create an instance of the `AmazonS3Client` class by passing in the temporary security credentials\.  You send requests to Amazon S3 using this client\. If you send requests using expired credentials, Amazon S3 returns an error\.   | 

The following C\# code sample demonstrates the preceding tasks\.

**Example**  

```
 1. // In real applications, the following code is part of your trusted code. It has 
 2. // your security credentials you use to obtain temporary security credentials.
 3. AmazonSecurityTokenServiceConfig config = new AmazonSecurityTokenServiceConfig();
 4.  AmazonSecurityTokenServiceClient stsClient = 
 5.            new AmazonSecurityTokenServiceClient(config);
 6. 
 7. GetSessionTokenRequest getSessionTokenRequest = new GetSessionTokenRequest();
 8. // Following duration can be set only if temporary credentials are requested by an IAM user.
 9. getSessionTokenRequest.DurationSeconds = 7200; // seconds.
10. Credentials credentials = 
11.      stsClient.GetSessionToken(getSessionTokenRequest).GetSessionTokenResult.Credentials;
12. 
13. SessionAWSCredentials sessionCredentials = 
14.                           new SessionAWSCredentials(credentials.AccessKeyId,
15.                                                     credentials.SecretAccessKey,
16.                                                     credentials.SessionToken);
17. 
18. // The following will be part of your less trusted code. You provide temporary security
19. // credentials so it can send authenticated requests to Amazon S3. 
20. // Create Amazon S3 client by passing in the basicSessionCredentials object.
21. AmazonS3Client s3Client = new AmazonS3Client(sessionCredentials); 
22. 
23. // Test. For example, send request to list object key in a bucket.
24. var response = s3Client.ListObjects(bucketName);
```

**Note**  
If you obtain temporary security credentials using your AWS account security credentials, the temporary security credentials are valid for only one hour\. You can specify session duration only if you use IAM user credentials to request a session\.

The following C\# code example lists object keys in the specified bucket\. For illustration, the code example obtains temporary security credentials for a default one hour session and uses them to send authenticated request to Amazon S3\. 

If you want to test the sample using IAM user credentials, you will need to create an IAM user under your AWS Account\. For more information about how to create an IAM user, see [Creating Your First IAM User and Administrators Group](http://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_create-admin-group.html) in the *IAM User Guide*\. 

 For instructions on how to create and test a working example, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\. 

```
using System;
using System.Configuration;
using System.Collections.Specialized;
using Amazon.S3;
using Amazon.SecurityToken;
using Amazon.SecurityToken.Model;
using Amazon.Runtime;
using Amazon.S3.Model;
using System.Collections.Generic;

namespace s3.amazon.com.docsamples
{
    class TempCredExplicitSessionStart
    {
        static string bucketName = "*** Provide bucket name ***"; 
        static IAmazonS3 client;

        public static void Main(string[] args)
        {
            NameValueCollection appConfig = ConfigurationManager.AppSettings;
            string accessKeyID = appConfig["AWSAccessKey"];
            string secretAccessKeyID = appConfig["AWSSecretKey"];

            try
            {
                Console.WriteLine("Listing objects stored in a bucket");
                SessionAWSCredentials tempCredentials =
                     GetTemporaryCredentials(accessKeyID, secretAccessKeyID);

                // Create client by providing temporary security credentials.
                using (client = new AmazonS3Client(tempCredentials, Amazon.RegionEndpoint.USEast1))
                {  
                    ListObjectsRequest listObjectRequest =
                                      new ListObjectsRequest();
                    listObjectRequest.BucketName = bucketName;

                    // Send request to Amazon S3.
                    ListObjectsResponse response = client.ListObjects(listObjectRequest);
                    List<S3Object> objects = response.S3Objects;
                    Console.WriteLine("Object count = {0}", objects.Count);

                    Console.WriteLine("Press any key to continue...");
                    Console.ReadKey();
                } 
            }
            catch (AmazonS3Exception s3Exception)
            {
                Console.WriteLine(s3Exception.Message,
                                  s3Exception.InnerException);
            }
            catch (AmazonSecurityTokenServiceException stsException)
            {
                Console.WriteLine(stsException.Message,
                                 stsException.InnerException);
            }
        }

        private static SessionAWSCredentials GetTemporaryCredentials(
                         string accessKeyId, string secretAccessKeyId)
        {
            AmazonSecurityTokenServiceClient stsClient =
                new AmazonSecurityTokenServiceClient(accessKeyId,
                                                     secretAccessKeyId); 

            GetSessionTokenRequest getSessionTokenRequest = 
                                             new GetSessionTokenRequest();
            getSessionTokenRequest.DurationSeconds = 7200; // seconds

            GetSessionTokenResponse sessionTokenResponse = 
                          stsClient.GetSessionToken(getSessionTokenRequest);
            Credentials credentials = sessionTokenResponse.Credentials;

            SessionAWSCredentials sessionCredentials = 
                new SessionAWSCredentials(credentials.AccessKeyId,
                                          credentials.SecretAccessKey,
                                          credentials.SessionToken);

            return sessionCredentials;
        }
    }
}
```

## Related Resources<a name="RelatedResources009"></a>

+ [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)