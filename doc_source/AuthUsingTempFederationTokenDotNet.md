# Making Requests Using Federated User Temporary Credentials \- AWS SDK for \.NET<a name="AuthUsingTempFederationTokenDotNet"></a>

You can provide temporary security credentials for your federated users and applications \(see [Making Requests](MakingRequests.md)\) so they can send authenticated requests to access your AWS resources\. When requesting these temporary credentials, you must provide a user name and an IAM policy describing the resource permissions you want to grant\. By default, the session duration is one hour\. You can explicitly set a different duration value when requesting the temporary security credentials for federated users and applications\.

**Note**  
To request temporary security credentials for federated users and applications, for added security, you might want to use a dedicated IAM user with only the necessary access permissions\. The temporary user you create can never get more permissions than the IAM user who requested the temporary security credentials\. For more information, go to [ AWS Identity and Access Management FAQs ](https://aws.amazon.com/iam/faqs/#What_are_the_best_practices_for_using_temporary_security_credentials)\.


**Making Requests Using Federated User Temporary Credentials**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the AWS Security Token Service client, `AmazonSecurityTokenServiceClient` class\. For information about providing credentials, see [Using the AWS SDK for \.NET](UsingTheMPDotNetAPI.md)\.  | 
|  2  |  Start a session by calling the `GetFederationToken` method of the STS client\.  You will need to provide session information including the user name and an IAM policy that you want to attach to the temporary credentials\. You can provide an optional session duration\. This method returns your temporary security credentials\.  | 
|  3  |  Package the temporary security credentials in an instance of the `SessionAWSCredentials` object\. You use this object to provide the temporary security credentials to your Amazon S3 client\.  | 
|  4  |  Create an instance of the `AmazonS3Client` class by passing the temporary security credentials\.  You send requests to Amazon S3 using this client\. If you send requests using expired credentials, Amazon S3 returns an error\.   | 

The following C\# code sample demonstrates the preceding tasks\.

**Example**  

```
 1. // In real applications, the following code is part of your trusted code. It has 
 2. // your security credentials you use to obtain temporary security credentials.
 3. AmazonSecurityTokenServiceConfig config = new AmazonSecurityTokenServiceConfig();
 4. AmazonSecurityTokenServiceClient stsClient = 
 5.        new AmazonSecurityTokenServiceClient(config); 
 6.        
 7. GetFederationTokenRequest federationTokenRequest = 
 8.                                      new GetFederationTokenRequest();
 9. federationTokenRequest.Name            = "User1";
10. federationTokenRequest.Policy          =  "*** Specify policy ***";
11. federationTokenRequest.DurationSeconds = 7200; 
12. 
13. GetFederationTokenResponse federationTokenResponse = stsClient.GetFederationToken(federationTokenRequest);
14. GetFederationTokenResult federationTokenResult = federationTokenResponse.GetFederationTokenResult;
15. Credentials credentials = federationTokenResult.Credentials;
16. 
17. 
18. SessionAWSCredentials sessionCredentials =
19.                  new SessionAWSCredentials(credentials.AccessKeyId,
20.                                           credentials.SecretAccessKey,
21.                                           credentials.SessionToken);
22. 
23. // The following will be part of your less trusted code. You provide temporary security
24. // credentials so it can send authenticated requests to Amazon S3. 
25. // Create Amazon S3 client by passing in the basicSessionCredentials object.
26. AmazonS3Client s3Client = new AmazonS3Client(sessionCredentials);
27. // Test. For example, send list object keys in a bucket.
28. ListObjectsRequest listObjectRequest =  new ListObjectsRequest();
29. listObjectRequest.BucketName = bucketName;
30. ListObjectsResponse response = s3Client.ListObjects(listObjectRequest);
```

**Example**  
The following C\# code example lists keys in the specified bucket\. In the code example, you first obtain temporary security credentials for a two\-hour session for your federated user \(User1\) and use them to send authenticated requests to Amazon S3\.   
When requesting temporary credentials for others, for added security, you use the security credentials of an IAM user who has permissions to request temporary security credentials\. You can also limit the access permissions of this IAM user to ensure that the IAM user grants only the minimum application\-specific permissions to the federated user\. This sample only lists objects in a specific bucket\. Therefore, first create an IAM user with the following policy attached\.   

```
 1. {
 2.   "Statement":[{
 3.       "Action":["s3:ListBucket",
 4.         "sts:GetFederationToken*"
 5.       ],
 6.       "Effect":"Allow",
 7.       "Resource":"*"
 8.     }
 9.   ]
10. }
```
The policy allows the IAM user to request temporary security credentials and access permission only to list your AWS resources\. For more information about how to create an IAM user, see [Creating Your First IAM User and Administrators Group](http://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_create-admin-group.html) in the *IAM User Guide*\.   
You can now use the IAM user security credentials to test the following example\. The example sends authenticated request to Amazon S3 using temporary security credentials\. The example specifies the following policy when requesting temporary security credentials for the federated user \(User1\) which restricts access to list objects in a specific bucket \(YourBucketName\)\. You must update the policy and provide your own existing bucket name\.  

```
 1. {
 2.   "Statement":[
 3.     {
 4.       "Sid":"1",
 5.       "Action":["s3:ListBucket"],
 6.       "Effect":"Allow", 
 7.       "Resource":"arn:aws:s3:::YourBucketName"
 8.     }
 9.   ]
10. }
```
You must update the following sample and provide the bucket name that you specified in the preceding federated user access policy\. For instructions on how to create and test a working example, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.  

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
    class TempFederatedCredentials
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
                     GetTemporaryFederatedCredentials(accessKeyID, secretAccessKeyID);
                                
                // Create client by providing temporary security credentials.
                using (client = new AmazonS3Client(tempCredentials, Amazon.RegionEndpoint.USEast1))
                {

                    ListObjectsRequest listObjectRequest = new ListObjectsRequest();
                    listObjectRequest.BucketName = bucketName;

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

     private static SessionAWSCredentials GetTemporaryFederatedCredentials(
                                string accessKeyId, string secretAccessKeyId)
        {
            AmazonSecurityTokenServiceConfig config = new AmazonSecurityTokenServiceConfig();
            AmazonSecurityTokenServiceClient stsClient = 
                new AmazonSecurityTokenServiceClient(
                                             accessKeyId, secretAccessKeyId, config); 
       
            GetFederationTokenRequest federationTokenRequest = 
                                     new GetFederationTokenRequest();
            federationTokenRequest.DurationSeconds = 7200; 
            federationTokenRequest.Name   = "User1";
            federationTokenRequest.Policy = @"{
               ""Statement"":
               [
                 {
                   ""Sid"":""Stmt1311212314284"",
                   ""Action"":[""s3:ListBucket""],
                   ""Effect"":""Allow"",
                   ""Resource"":""arn:aws:s3:::YourBucketName""  
                  }
               ]
             }
            ";
            
            GetFederationTokenResponse federationTokenResponse = 
                         stsClient.GetFederationToken(federationTokenRequest);
            Credentials credentials = federationTokenResponse.Credentials;

            SessionAWSCredentials sessionCredentials =
                new SessionAWSCredentials(credentials.AccessKeyId,
                                          credentials.SecretAccessKey,
                                          credentials.SessionToken);
            return sessionCredentials;
        }
    }
}
```

## Related Resources<a name="RelatedResources006"></a>

+ [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)