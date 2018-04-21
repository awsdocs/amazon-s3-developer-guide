# Making Requests Using Federated User Temporary Credentials \- AWS SDK for Java<a name="AuthUsingTempFederationTokenJava"></a>

You can provide temporary security credentials for your federated users and applications \(see [Making Requests](MakingRequests.md)\) so they can send authenticated requests to access your AWS resources\. When requesting these temporary credentials from the IAM service, you must provide a user name and an IAM policy describing the resource permissions you want to grant\. By default, the session duration is one hour\. However, if you are requesting temporary credentials using IAM user credentials, you can explicitly set a different duration value when requesting the temporary security credentials for federated users and applications\.

**Note**  
To request temporary security credentials for federated users and applications, for added security, you might want to use a dedicated IAM user with only the necessary access permissions\. The temporary user you create can never get more permissions than the IAM user who requested the temporary security credentials\. For more information, go to [ AWS Identity and Access Management FAQs ](https://aws.amazon.com/iam/faqs/#What_are_the_best_practices_for_using_temporary_security_credentials)\.


**Making Requests Using Federated User Temporary Security Credentials**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the AWS Security Token Service client `AWSSecurityTokenServiceClient`\.  | 
|  2  |  Start a session by calling the `getFederationToken` method of the STS client you created in the preceding step\.  You will need to provide session information including the user name and an IAM policy that you want to attach to the temporary credentials\. This method returns your temporary security credentials\.  | 
|  3  |  Package the temporary security credentials in an instance of the `BasicSessionCredentials` object\. You use this object to provide the temporary security credentials to your Amazon S3 client\.  | 
|  4  |  Create an instance of the `AmazonS3Client` class by passing the temporary security credentials\.  You send requests to Amazon S3 using this client\. If you send requests using expired credentials, Amazon S3 returns an error\.   | 

The following Java code sample demonstrates the preceding tasks\.

**Example**  

```
 1. // In real applications, the following code is part of your trusted code. It has 
 2. // your security credentials you use to obtain temporary security credentials.
 3. AWSSecurityTokenServiceClient stsClient = 
 4.                         new AWSSecurityTokenServiceClient(new ProfileCredentialsProvider());
 5. 
 6. GetFederationTokenRequest getFederationTokenRequest = 
 7.                         new GetFederationTokenRequest();
 8. getFederationTokenRequest.setDurationSeconds(7200);
 9. getFederationTokenRequest.setName("User1");
10. 
11. // Define the policy and add to the request.
12. Policy policy = new Policy();
13. // Define the policy here.
14. // Add the policy to the request.
15. getFederationTokenRequest.setPolicy(policy.toJson());
16. 
17. GetFederationTokenResult federationTokenResult = 
18.                      stsClient.getFederationToken(getFederationTokenRequest);
19. Credentials sessionCredentials = federationTokenResult.getCredentials();
20. 
21. // Package the session credentials as a BasicSessionCredentials object 
22. // for an S3 client object to use.
23. BasicSessionCredentials basicSessionCredentials = new BasicSessionCredentials(
24.       sessionCredentials.getAccessKeyId(), 
25.       sessionCredentials.getSecretAccessKey(), 
26.       sessionCredentials.getSessionToken());
27. 
28. // The following will be part of your less trusted code. You provide temporary security
29. // credentials so it can send authenticated requests to Amazon S3. 
30. // Create an Amazon S3 client by passing in the basicSessionCredentials object.
31. AmazonS3Client s3 = new AmazonS3Client(basicSessionCredentials);
32. 
33. // Test. For example, send list object keys in a bucket.
34. ObjectListing objects = s3.listObjects(bucketName);
```

 To set a condition in the policy, create a `Condition` object and associate it with the policy\. The following code sample shows a condition that allows users from a specified IP range to list objects\.

```
Policy policy = new Policy();

// Allow only a specified IP range.
Condition condition = new StringCondition(StringCondition.StringComparisonType.StringLike, 
    ConditionFactory.SOURCE_IP_CONDITION_KEY , "192.168.143.*"); 
        
policy.withStatements(new Statement(Effect.Allow)
    .withActions(S3Actions.ListObjects)
    .withConditions(condition)
    .withResources(new Resource("arn:aws:s3:::"+ bucketName))); 

getFederationTokenRequest.setPolicy(policy.toJson());
```

**Example**  
The following Java code example lists keys in the specified bucket\. In the code example, you first obtain temporary security credentials for a two\-hour session for your federated user \(User1\) and use them to send authenticated requests to Amazon S3\.   
When requesting temporary credentials for others, for added security, you use the security credentials of an IAM user who has permissions to request temporary security credentials\. You can also limit the access permissions of this IAM user to ensure that the IAM user grants only the minimum application\-specific permissions when requesting temporary security credentials\. This sample only lists objects in a specific bucket\. Therefore, first create an IAM user with the following policy attached\.   

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
The policy allows the IAM user to request temporary security credentials and access permission only to list your AWS resources\. For information about how to create an IAM user, see [Creating Your First IAM User and Administrators Group](http://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_create-admin-group.html) in the *IAM User Guide*\.   
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
You must update the following sample and provide the bucket name that you specified in the preceding federated user access policy\.  

```
 1. import java.io.IOException;
 2. import com.amazonaws.auth.BasicSessionCredentials;
 3. import com.amazonaws.auth.PropertiesCredentials;
 4. import com.amazonaws.auth.policy.Policy;
 5. import com.amazonaws.auth.policy.Resource;
 6. import com.amazonaws.auth.policy.Statement;
 7. import com.amazonaws.auth.policy.Statement.Effect;
 8. import com.amazonaws.auth.policy.actions.S3Actions;
 9. import com.amazonaws.services.s3.AmazonS3Client;
10. import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
11. import com.amazonaws.services.securitytoken.model.Credentials;
12. import com.amazonaws.services.securitytoken.model.GetFederationTokenRequest;
13. import com.amazonaws.services.securitytoken.model.GetFederationTokenResult;
14. import com.amazonaws.services.s3.model.ObjectListing;
15. 
16. public class S3Sample {
17. 	private static String bucketName = "*** Specify bucket name ***";
18.     public static void main(String[] args) throws IOException {        
19.         AWSSecurityTokenServiceClient stsClient = 
20.                         new AWSSecurityTokenServiceClient(new ProfileCredentialsProvider());
21. 
22.         GetFederationTokenRequest getFederationTokenRequest = 
23.                                          new GetFederationTokenRequest();
24.         getFederationTokenRequest.setDurationSeconds(7200);
25.         getFederationTokenRequest.setName("User1");
26.         
27.         // Define the policy and add to the request.
28.         Policy policy = new Policy();
29.         policy.withStatements(new Statement(Effect.Allow)
30.             .withActions(S3Actions.ListObjects) 
31.             .withResources(new Resource("arn:aws:s3:::ExampleBucket"))); 
32. 
33.         getFederationTokenRequest.setPolicy(policy.toJson());
34.  
35.         // Get the temporary security credentials.
36.         GetFederationTokenResult federationTokenResult = 
37.                         stsClient.getFederationToken(getFederationTokenRequest);
38.         Credentials sessionCredentials = federationTokenResult.getCredentials();
39.  
40.         // Package the session credentials as a BasicSessionCredentials
41.         // object for an S3 client object to use.
42.         BasicSessionCredentials basicSessionCredentials = 
43.               new BasicSessionCredentials(sessionCredentials.getAccessKeyId(), 
44.         		                          sessionCredentials.getSecretAccessKey(), 
45.         		                          sessionCredentials.getSessionToken());
46.         AmazonS3Client s3 = new AmazonS3Client(basicSessionCredentials);
47. 
48.         
49.         // Test. For example, send ListBucket request using the temporary security credentials. 
50.         ObjectListing objects = s3.listObjects(bucketName);
51.         System.out.println("No. of Objects = " + objects.getObjectSummaries().size());
52.     }
53. }
```

## Related Resources<a name="RelatedResources005"></a>
+ [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)