# Making Requests Using Federated User Temporary Credentials \- AWS SDK for Ruby<a name="AuthUsingTempFederationTokenRuby"></a>

You can provide temporary security credentials for your federated users and applications \(see [Making Requests](MakingRequests.md)\) so that they can send authenticated requests to access your AWS resources\. When requesting these temporary credentials from the IAM service, you must provide a user name and an IAM policy describing the resource permissions you want to grant\. By default, the session duration is one hour\. However, if you are requesting temporary credentials using IAM user credentials, you can explicitly set a different duration value when requesting the temporary security credentials for federated users and applications\.

**Note**  
To request temporary security credentials for federated users and applications, for added security, you might want to use a dedicated IAM user with only the necessary access permissions\. The temporary user you create can never get more permissions than the IAM user who requested the temporary security credentials\. For more information, go to [ AWS Identity and Access Management FAQs ](https://aws.amazon.com/iam/faqs/#What_are_the_best_practices_for_using_temporary_security_credentials)\.


**Making Requests Using Federated User Temporary Security Credentials**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of the AWS Security Token Service \(AWS STS\) client `AWS::STS::Session`\.  | 
|  2  |  Start a session by calling the `new_federated_session` method of the STS client you created in the preceding step\.  You need to provide session information, including the user name and an IAM policy that you want to attach to the temporary credentials\. This method returns your temporary security credentials\.  | 
|  3  |  Create an instance of the `AWS::S3` class by passing the temporary security credentials\.  You send requests to Amazon S3 using this client\. If you send requests using expired credentials, Amazon S3 returns an error\.   | 

The following Ruby code example demonstrates the preceding tasks\.

**Example**  

```
 1. # Start a session with restricted permissions.
 2. sts = AWS::STS.new()
 3. policy = AWS::STS::Policy.new
 4. policy.allow(
 5.   :actions => ["s3:ListBucket"],
 6.   :resources => "arn:aws:s3:::#{bucket_name}")
 7.   
 8. session = sts.new_federated_session(
 9.   'User1',
10.   :policy => policy,
11.   :duration => 2*60*60)
12. 
13. puts "Policy: #{policy.to_json}"
14. 
15. # Get an instance of the S3 interface using the session credentials.
16. s3 = AWS::S3.new(session.credentials)
17. 
18. # Get a list of all object keys in a bucket.
19. bucket = s3.buckets[bucket_name].objects.collect(&:key)
```

**Example**  
The following Ruby code example lists keys in the specified bucket\. In the code example, you first obtain temporary security credentials for a two\-hour session for your federated user \(User1\) and use them to send authenticated requests to Amazon S3\.   
When requesting temporary credentials for others, for added security, you use the security credentials of an IAM user who has permissions to request temporary security credentials\. You can also limit the access permissions of this IAM user to ensure that the IAM user grants only the minimum application\-specific permissions when requesting temporary security credentials\. This example only lists objects in a specific bucket\. Therefore, first create an IAM user with the following policy attached:   

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
You can now use the IAM user security credentials to test the following example\. The example sends an authenticated request to Amazon S3 using temporary security credentials\. The example specifies the following policy when requesting temporary security credentials for the federated user \(User1\), which restricts access to listing objects in a specific bucket \(`YourBucketName`\)\. To use this example in your code, update the policy and provide your own bucket name\.  

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
To use this example in your code, provide your access key ID and secret key and the bucket name that you specified in the preceding federated user access policy\.  

```
 1. require 'rubygems'
 2. require 'aws-sdk'
 3. 
 4. # In real applications, the following code is part of your trusted code. It has 
 5. # your security credentials that you use to obtain temporary security credentials.
 6. 
 7. bucket_name = '*** Provide bucket name ***'
 8. 
 9. # Start a session with restricted permissions.
10. sts = AWS::STS.new()
11. policy = AWS::STS::Policy.new
12. policy.allow(
13.   :actions => ["s3:ListBucket"],
14.   :resources => "arn:aws:s3:::#{bucket_name}")
15. 
16. session = sts.new_federated_session(
17.   'User1',
18.   :policy => policy,
19.   :duration => 2*60*60)
20. 
21. puts "Policy: #{policy.to_json}"
22. 
23. # Get an instance of the S3 interface using the session credentials.
24. s3 = AWS::S3.new(session.credentials)
25. 
26. # Get a list of all object keys in a bucket.
27. bucket = s3.buckets[bucket_name].objects.collect(&:key)
28. puts "No. of Objects = #{bucket.count.to_s}" 
29. puts bucket
```