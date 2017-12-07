# Making Requests Using IAM User Temporary Credentials \- AWS SDK for Java<a name="AuthUsingTempSessionTokenJava"></a>

An IAM user or an AWS Account can request temporary security credentials \(see [Making Requests](MakingRequests.md)\) using AWS SDK for Java and use them to access Amazon S3\. These credentials expire after the session duration\. By default, the session duration is one hour\.  If you use IAM user credentials, you can specify duration, between 1 and 36 hours, when requesting the temporary security credentials\. 


**Making Requests Using IAM User Temporary Security Credentials**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the AWS Security Token Service client `AWSSecurityTokenServiceClient`\.  | 
|  2  |  Start a session by calling the `GetSessionToken` method of the STS client you created in the preceding step\. You provide session information to this method using a `GetSessionTokenRequest` object\. The method returns your temporary security credentials\.  | 
|  3  |  Package the temporary security credentials in an instance of the `BasicSessionCredentials` object so you can provide the credentials to your Amazon S3 client\.  | 
|  4  |  Create an instance of the `AmazonS3Client` class by passing in the temporary security credentials\.  You send the requests to Amazon S3 using this client\. If you send requests using expired credentials, Amazon S3 returns an error\.   | 

The following Java code sample demonstrates the preceding tasks\.

**Example**  

```
 1. // In real applications, the following code is part of your trusted code. It has 
 2. // your security credentials you use to obtain temporary security credentials.
 3. AWSSecurityTokenServiceClient stsClient = 
 4.                         new AWSSecurityTokenServiceClient(new ProfileCredentialsProvider());
 5.         
 6. //
 7. // Manually start a session.
 8. GetSessionTokenRequest getSessionTokenRequest = new GetSessionTokenRequest();
 9. // Following duration can be set only if temporary credentials are requested by an IAM user.
10. getSessionTokenRequest.setDurationSeconds(7200); 
11. 
12. GetSessionTokenResult sessionTokenResult = 
13.                            stsClient.getSessionToken(getSessionTokenRequest);
14. Credentials sessionCredentials = sessionTokenResult.getCredentials();
15.   
16. // Package the temporary security credentials as 
17. // a BasicSessionCredentials object, for an Amazon S3 client object to use.
18. BasicSessionCredentials basicSessionCredentials = 
19.                new BasicSessionCredentials(sessionCredentials.getAccessKeyId(), 
20.         		                           sessionCredentials.getSecretAccessKey(), 
21.         		                            sessionCredentials.getSessionToken());
22. 
23. // The following will be part of your less trusted code. You provide temporary security
24. // credentials so it can send authenticated requests to Amazon S3. 
25. // Create Amazon S3 client by passing in the basicSessionCredentials object.
26. AmazonS3Client s3 = new AmazonS3Client(basicSessionCredentials);
27.             
28. // Test. For example, get object keys in a bucket.
29. ObjectListing objects = s3.listObjects(bucketName);
```

**Note**  
If you obtain temporary security credentials using your AWS account credentials, the temporary security credentials are valid for only one hour\. You can specify session duration only if you use IAM user credentials to request a session\.

The following Java code example lists the object keys in the specified bucket\. For illustration, the code example obtains temporary security credentials for a default one hour session and uses them to send an authenticated request to Amazon S3\. 

If you want to test the sample using IAM user credentials, you will need to create an IAM user under your AWS Account\. For more information about how to create an IAM user, see [Creating Your First IAM User and Administrators Group](http://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_create-admin-group.html) in the *IAM User Guide*\. 

```
 1. import java.io.IOException;
 2. import com.amazonaws.auth.BasicSessionCredentials;
 3. import com.amazonaws.auth.PropertiesCredentials;
 4. import com.amazonaws.services.s3.AmazonS3Client;
 5. import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
 6. import com.amazonaws.services.securitytoken.model.Credentials;
 7. import com.amazonaws.services.securitytoken.model.GetSessionTokenRequest;
 8. import com.amazonaws.services.securitytoken.model.GetSessionTokenResult;
 9. import com.amazonaws.services.s3.model.ObjectListing;
10. 
11. public class S3Sample {
12. 	private static String bucketName = "*** Provide bucket name ***";
13. 
14.     public static void main(String[] args) throws IOException {        
15.         AWSSecurityTokenServiceClient stsClient = 
16.                                new AWSSecurityTokenServiceClient(new ProfileCredentialsProvider());        
17.         //
18.         // Start a session.
19.         GetSessionTokenRequest getSessionTokenRequest = 
20.                                              new GetSessionTokenRequest();
21. 
22.         GetSessionTokenResult sessionTokenResult = 
23.                             stsClient.getSessionToken(getSessionTokenRequest);
24.         Credentials sessionCredentials = sessionTokenResult.getCredentials();
25.         System.out.println("Session Credentials: " 
26.                                                + sessionCredentials.toString());
27.   
28.         
29.         // Package the session credentials as a BasicSessionCredentials 
30.         // object for an S3 client object to use.
31.         BasicSessionCredentials basicSessionCredentials = 
32.              new BasicSessionCredentials(sessionCredentials.getAccessKeyId(), 
33.         		                         sessionCredentials.getSecretAccessKey(), 
34.         		                         sessionCredentials.getSessionToken());
35.         AmazonS3Client s3 = new AmazonS3Client(basicSessionCredentials);
36. 
37.         // Test. For example, get object keys for a given bucket. 
38.         ObjectListing objects = s3.listObjects(bucketName);
39.         System.out.println("No. of Objects = " + 
40.                                            objects.getObjectSummaries().size());
41.     }
42. }
```

## Related Resources<a name="RelatedResources008"></a>

+ [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)