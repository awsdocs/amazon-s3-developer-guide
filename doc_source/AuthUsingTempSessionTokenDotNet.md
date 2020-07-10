# Making requests using IAM user temporary credentials \- AWS SDK for \.NET<a name="AuthUsingTempSessionTokenDotNet"></a>

An IAM user or an AWS account can request temporary security credentials using the AWS SDK for \.NET and use them to access Amazon S3\. These credentials expire after the session duration\. 

By default, the session duration is one hour\. If you use IAM user credentials, you can specify the duration when requesting the temporary security credentials from 15 minutes to the maximum session duration for the role\. For more information about temporary security credentials, see [Temporary Security Credentials](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_temp.html) in the *IAM User Guide*\. For more information about making requests, see [Making requests](MakingRequests.md)\.

**To get temporary security credentials and access Amazon S3**

1. Create an instance of the AWS Security Token Service client, `AmazonSecurityTokenServiceClient`\. For information about providing credentials, see [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)\.

1. Start a session by calling the `GetSessionToken` method of the STS client you created in the preceding step\. You provide session information to this method using a `GetSessionTokenRequest` object\. 

   The method returns your temporary security credentials\.

1. Package the temporary security credentials in an instance of the `SessionAWSCredentials` object\. You use this object to provide the temporary security credentials to your Amazon S3 client\.

1. Create an instance of the `AmazonS3Client` class by passing in the temporary security credentials\. You send requests to Amazon S3 using this client\. If you send requests using expired credentials, Amazon S3 returns an error\.

**Note**  
If you obtain temporary security credentials using your AWS account security credentials, those credentials are valid for only one hour\. You can specify a session duration only if you use IAM user credentials to request a session\.

The following C\# example lists object keys in the specified bucket\. For illustration, the example obtains temporary security credentials for a default one\-hour session and uses them to send authenticated request to Amazon S3\. 

If you want to test the sample using IAM user credentials, you need to create an IAM user under your AWS account\. For more information about how to create an IAM user, see [Creating Your First IAM User and Administrators Group](https://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_create-admin-group.html) in the *IAM User Guide*\. For more information about making requests, see [Making requests](MakingRequests.md)\.

 For instructions on creating and testing a working example, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\. 

```
using Amazon;
using Amazon.Runtime;
using Amazon.S3;
using Amazon.S3.Model;
using Amazon.SecurityToken;
using Amazon.SecurityToken.Model;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Amazon.DocSamples.S3
{
    class TempCredExplicitSessionStartTest
    {
        private const string bucketName = "*** bucket name ***";
        // Specify your bucket region (an example region is shown).
        private static readonly RegionEndpoint bucketRegion = RegionEndpoint.USWest2;
        private static IAmazonS3 s3Client;
        public static void Main()
        {
            ListObjectsAsync().Wait();
        }

        private static async Task ListObjectsAsync()
        {
            try
            {
                // Credentials use the default AWS SDK for .NET credential search chain. 
                // On local development machines, this is your default profile.
                Console.WriteLine("Listing objects stored in a bucket");
                SessionAWSCredentials tempCredentials = await GetTemporaryCredentialsAsync();

                // Create a client by providing temporary security credentials.
                using (s3Client = new AmazonS3Client(tempCredentials, bucketRegion))
                {
                    var listObjectRequest = new ListObjectsRequest
                    {
                        BucketName = bucketName
                    };
                    // Send request to Amazon S3.
                    ListObjectsResponse response = await s3Client.ListObjectsAsync(listObjectRequest);
                    List<S3Object> objects = response.S3Objects;
                    Console.WriteLine("Object count = {0}", objects.Count);
                }
            }
            catch (AmazonS3Exception s3Exception)
            {
                Console.WriteLine(s3Exception.Message, s3Exception.InnerException);
            }
            catch (AmazonSecurityTokenServiceException stsException)
            {
                Console.WriteLine(stsException.Message, stsException.InnerException);
            }
        }

        private static async Task<SessionAWSCredentials> GetTemporaryCredentialsAsync()
        {
            using (var stsClient = new AmazonSecurityTokenServiceClient())
            {
                var getSessionTokenRequest = new GetSessionTokenRequest
                {
                    DurationSeconds = 7200 // seconds
                };

                GetSessionTokenResponse sessionTokenResponse =
                              await stsClient.GetSessionTokenAsync(getSessionTokenRequest);

                Credentials credentials = sessionTokenResponse.Credentials;

                var sessionCredentials =
                    new SessionAWSCredentials(credentials.AccessKeyId,
                                              credentials.SecretAccessKey,
                                              credentials.SessionToken);
                return sessionCredentials;
            }
        }
    }
}
```

## Related resources<a name="RelatedResources009"></a>
+ [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)