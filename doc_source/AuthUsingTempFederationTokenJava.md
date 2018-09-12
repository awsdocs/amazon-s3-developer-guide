# Making Requests Using Federated User Temporary Credentials \- AWS SDK for Java<a name="AuthUsingTempFederationTokenJava"></a>

You can provide temporary security credentials for your federated users and applications so that they can send authenticated requests to access your AWS resources\. When requesting these temporary credentials, you must provide a user name and an IAM policy that describes the resource permissions that you want to grant\. By default, the session duration is one hour\. You can explicitly set a different duration value when requesting the temporary security credentials for federated users and applications\.

**Note**  
For added security when requesting temporary security credentials for federated users and applications, we recommend that you use a dedicated IAM user with only the necessary access permissions\. The temporary user you create can never get more permissions than the IAM user who requested the temporary security credentials\. For more information, see [ AWS Identity and Access Management FAQs ](https://aws.amazon.com/iam/faqs/#What_are_the_best_practices_for_using_temporary_security_credentials)\.

To provide security credentials and send authenticated request to access resources, do the following:
+ Create an instance of the `AWSSecurityTokenServiceClient` class\. For information about providing credentials, see [Using the AWS SDK for Java](UsingTheMPJavaAPI.md)\.
+ Start a session by calling the `getFederationToken()` method of the Security Token Service \(STS\) client\. Provide session information, including the user name and an IAM policy, that you want to attach to the temporary credentials\. You can provide an optional session duration\. This method returns your temporary security credentials\.
+ Package the temporary security credentials in an instance of the `BasicSessionCredentials` object\. You use this object to provide the temporary security credentials to your Amazon S3 client\.
+ Create an instance of the `AmazonS3Client` class using the temporary security credentials\. You send requests to Amazon S3 using this client\. If you send requests using expired credentials, Amazon S3 returns an error\. 

**Example**  
The example lists keys in the specified S3 bucket\. In the example, you obtain temporary security credentials for a two\-hour session for your federated user and use the credentials to send authenticated requests to Amazon S3\. To run the example, you need to create an IAM user with an attached policy that allows the user to request temporary security credentials and list your AWS resources\. The following policy accomplishes this:  

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
For more information about how to create an IAM user, see [Creating Your First IAM User and Administrators Group](http://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_create-admin-group.html) in the *IAM User Guide*\.   
After creating an IAM user and attaching the preceding policy, you can run the following example\. For instructions on creating and testing a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\.  

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

import java.io.IOException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Resource;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.Statement.Effect;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetFederationTokenRequest;
import com.amazonaws.services.securitytoken.model.GetFederationTokenResult;
import com.amazonaws.services.s3.model.ObjectListing;

public class MakingRequestsWithFederatedTempCredentials {

    public static void main(String[] args) throws IOException {
        String clientRegion = "*** Client region ***";
        String bucketName = "*** Specify bucket name ***";
        String federatedUser = "*** Federated user name ***";
        String resourceARN = "arn:aws:s3:::" + bucketName;

        try {
            AWSSecurityTokenService stsClient = AWSSecurityTokenServiceClientBuilder
                    .standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(clientRegion)
                    .build();
            
            GetFederationTokenRequest getFederationTokenRequest = new GetFederationTokenRequest();
            getFederationTokenRequest.setDurationSeconds(7200);
            getFederationTokenRequest.setName(federatedUser);
            
            // Define the policy and add it to the request.
            Policy policy = new Policy();
            policy.withStatements(new Statement(Effect.Allow)
                    .withActions(S3Actions.ListObjects)
                    .withResources(new Resource(resourceARN)));
            getFederationTokenRequest.setPolicy(policy.toJson());
            
            // Get the temporary security credentials.
            GetFederationTokenResult federationTokenResult = stsClient.getFederationToken(getFederationTokenRequest);
            Credentials sessionCredentials = federationTokenResult.getCredentials();
    
            // Package the session credentials as a BasicSessionCredentials
            // object for an Amazon S3 client object to use.
            BasicSessionCredentials basicSessionCredentials = new BasicSessionCredentials(
                                                                    sessionCredentials.getAccessKeyId(), 
                                                                    sessionCredentials.getSecretAccessKey(),
                                                                    sessionCredentials.getSessionToken());
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                                    .withCredentials(new AWSStaticCredentialsProvider(basicSessionCredentials))
                                    .withRegion(clientRegion)
                                    .build();
    
            // To verify that the client works, send a listObjects request using 
            // the temporary security credentials.
            ObjectListing objects = s3Client.listObjects(bucketName);
            System.out.println("No. of Objects = " + objects.getObjectSummaries().size());
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
}
```

## Related Resources<a name="RelatedResources005"></a>
+ [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)