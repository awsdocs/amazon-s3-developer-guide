# Making Requests Using Federated User Temporary Credentials \- AWS SDK for \.NET<a name="AuthUsingTempFederationTokenDotNet"></a>

You can provide temporary security credentials for your federated users and applications so that they can send authenticated requests to access your AWS resources\. When requesting these temporary credentials, you must provide a user name and an IAM policy that describes the resource permissions that you want to grant\. By default, the duration of a session is one hour\. You can explicitly set a different duration value when requesting the temporary security credentials for federated users and applications\. For information about sending authenticated requests, see [Making Requests](MakingRequests.md)\.

**Note**  
When requesting temporary security credentials for federated users and applications, for added security, we suggest that you use a dedicated IAM user with only the necessary access permissions\. The temporary user you create can never get more permissions than the IAM user who requested the temporary security credentials\. For more information, see [ AWS Identity and Access Management FAQs ](https://aws.amazon.com/iam/faqs/#What_are_the_best_practices_for_using_temporary_security_credentials)\.

You do the following:
+ Create an instance of the AWS Security Token Service client, `AmazonSecurityTokenServiceClient` class\. For information about providing credentials, see [Using the AWS SDK for \.NET](UsingTheMPDotNetAPI.md)\.
+ Start a session by calling the `GetFederationToken` method of the STS client\. You need to provide session information, including the user name and an IAM policy that you want to attach to the temporary credentials\. Optionally, you can provide a session duration\. This method returns your temporary security credentials\.
+ Package the temporary security credentials in an instance of the `SessionAWSCredentials` object\. You use this object to provide the temporary security credentials to your Amazon S3 client\.
+ Create an instance of the `AmazonS3Client` class by passing the temporary security credentials\. You use this client to send requests to Amazon S3\. If you send requests using expired credentials, Amazon S3 returns an error\. 

**Example**  
The following C\# example lists the keys in the specified bucket\. In the example, you obtain temporary security credentials for a two\-hour session for your federated user \(User1\), and use the credentials to send authenticated requests to Amazon S3\.   
+ For this exercise, you create an IAM user with minimal permissions\. Using the credentials of this IAM user, you request temporary credentials for others\. This example lists only the objects in a specific bucket\. Create an IAM user with the following policy attached: 

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
+ Use the IAM user security credentials to test the following example\. The example sends authenticated request to Amazon S3 using temporary security credentials\. The example specifies the following policy when requesting temporary security credentials for the federated user \(User1\), which restricts access to listing objects in a specific bucket \(`YourBucketName`\)\. You must update the policy and provide your own existing bucket name\.

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
+   
**Example**  

  Update the following sample and provide the bucket name that you specified in the preceding federated user access policy\. For instructions on how to create and test a working example, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.

  ```
  // Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
  // SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)
  
  ﻿using Amazon.Runtime;
  using Amazon.S3;
  using Amazon.S3.Model;
  using Amazon.SecurityToken;
  using Amazon.SecurityToken.Model;
  using System;
  using System.Collections.Generic;
  using System.Threading.Tasks;
  
  namespace Amazon.DocSamples.S3
  {
      class TempFederatedCredentialsTest
      {
          private const string bucketName = "*** bucket name ***";
          // Specify your bucket region (an example region is shown).
          private static readonly RegionEndpoint bucketRegion = RegionEndpoint.USWest2;
          private static IAmazonS3 client;
  
          public static void Main()
          {
              ListObjectsAsync().Wait();
          }
  
          private static async Task ListObjectsAsync()
          {
              try
              {
                  Console.WriteLine("Listing objects stored in a bucket");
                  // Credentials use the default AWS SDK for .NET credential search chain. 
                  // On local development machines, this is your default profile.
                  SessionAWSCredentials tempCredentials =
                      await GetTemporaryFederatedCredentialsAsync();
  
                  // Create a client by providing temporary security credentials.
                  using (client = new AmazonS3Client(bucketRegion))
                  {
                      ListObjectsRequest listObjectRequest = new ListObjectsRequest();
                      listObjectRequest.BucketName = bucketName;
  
                      ListObjectsResponse response = await client.ListObjectsAsync(listObjectRequest);
                      List<S3Object> objects = response.S3Objects;
                      Console.WriteLine("Object count = {0}", objects.Count);
  
                      Console.WriteLine("Press any key to continue...");
                      Console.ReadKey();
                  }
              }
              catch (AmazonS3Exception e)
              {
                  Console.WriteLine("Error encountered ***. Message:'{0}' when writing an object", e.Message);
              }
              catch (Exception e)
              {
                  Console.WriteLine("Unknown encountered on server. Message:'{0}' when writing an object", e.Message);
              }
          }
  
          private static async Task<SessionAWSCredentials> GetTemporaryFederatedCredentialsAsync()
          {
              AmazonSecurityTokenServiceConfig config = new AmazonSecurityTokenServiceConfig();
              AmazonSecurityTokenServiceClient stsClient =
                  new AmazonSecurityTokenServiceClient(
                                               config);
  
              GetFederationTokenRequest federationTokenRequest =
                                       new GetFederationTokenRequest();
              federationTokenRequest.DurationSeconds = 7200;
              federationTokenRequest.Name = "User1";
              federationTokenRequest.Policy = @"{
                 ""Statement"":
                 [
                   {
                     ""Sid"":""Stmt1311212314284"",
                     ""Action"":[""s3:ListBucket""],
                     ""Effect"":""Allow"",
                     ""Resource"":""arn:aws:s3:::" + bucketName + @"""
                    }
                 ]
               }
              ";
  
              GetFederationTokenResponse federationTokenResponse =
                          await stsClient.GetFederationTokenAsync(federationTokenRequest);
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