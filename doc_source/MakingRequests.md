# Making Requests<a name="MakingRequests"></a>

**Topics**
+ [About Access Keys](#TypesofSecurityCredentials)
+ [Request Endpoints](#RequestEndpoints)
+ [Making Requests to Amazon S3 over IPv6](ipv6-access.md)
+ [Making Requests Using the AWS SDKs](MakingAuthenticatedRequests.md)
+ [Making Requests Using the REST API](RESTAPI.md)

Amazon S3 is a REST service\. You can send requests to Amazon S3 using the REST API or the AWS SDK \(see [Sample Code and Libraries](https://aws.amazon.com/code)\) wrapper libraries that wrap the underlying Amazon S3 REST API, simplifying your programming tasks\. 

Every interaction with Amazon S3 is either authenticated or anonymous\. Authentication is a process of verifying the identity of the requester trying to access an Amazon Web Services \(AWS\) product\. Authenticated requests must include a signature value that authenticates the request sender\. The signature value is, in part, generated from the requester's AWS access keys \(access key ID and secret access key\)\. For more information about getting access keys, see [How Do I Get Security Credentials?](http://docs.aws.amazon.com/general/latest/gr/getting-aws-sec-creds.html) in the *AWS General Reference*\. 

If you are using the AWS SDK, the libraries compute the signature from the keys you provide\. However, if you make direct REST API calls in your application, you must write the code to compute the signature and add it to the request\. 

## About Access Keys<a name="TypesofSecurityCredentials"></a>

The following sections review the types of access keys that you can use to make authenticated requests\.

### AWS Account Access Keys<a name="requestsUsingAcctCred"></a>

The account access keys provide full access to the AWS resources owned by the account\. The following are examples of access keys:
+ Access key ID \(a 20\-character, alphanumeric string\)\. For example: AKIAIOSFODNN7EXAMPLE
+ Secret access key \(a 40\-character string\)\. For example: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY

The access key ID uniquely identifies an AWS account\. You can use these access keys to send authenticated requests to Amazon S3\. 

### IAM User Access Keys<a name="requestsUsingIAMUserCred"></a>

You can create one AWS account for your company; however, there may be several employees in the organization who need access to your organization's AWS resources\. Sharing your AWS account access keys reduces security, and creating individual AWS accounts for each employee might not be practical\. Also, you cannot easily share resources such as buckets and objects because they are owned by different accounts\. To share resources, you must grant permissions, which is additional work\.

In such scenarios, you can use AWS Identity and Access Management \(IAM\) to create users under your AWS account with their own access keys and attach IAM user policies granting appropriate resource access permissions to them\. To better manage these users, IAM enables you to create groups of users and grant group\-level permissions that apply to all users in that group\. 

These users are referred to as IAM users that you create and manage within AWS\. The parent account controls a user's ability to access AWS\. Any resources an IAM user creates are under the control of and paid for by the parent AWS account\. These IAM users can send authenticated requests to Amazon S3 using their own security credentials\. For more information about creating and managing users under your AWS account, go to the [AWS Identity and Access Management product details page](https://aws.amazon.com/iam/)\. 

### Temporary Security Credentials<a name="requestsUsingTempCred"></a>

In addition to creating IAM users with their own access keys, IAM also enables you to grant temporary security credentials \(temporary access keys and a security token\) to any IAM user to enable them to access your AWS services and resources\. You can also manage users in your system outside AWS\. These are referred to as federated users\. Additionally, users can be applications that you create to access your AWS resources\.

IAM provides the AWS Security Token Service API for you to request temporary security credentials\. You can use either the AWS STS API or the AWS SDK to request these credentials\. The API returns temporary security credentials \(access key ID and secret access key\), and a security token\. These credentials are valid only for the duration you specify when you request them\. You use the access key ID and secret key the same way you use them when sending requests using your AWS account or IAM user access keys\. In addition, you must include the token in each request you send to Amazon S3\. 

An IAM user can request these temporary security credentials for their own use or hand them out to federated users or applications\. When requesting temporary security credentials for federated users, you must provide a user name and an IAM policy defining the permissions you want to associate with these temporary security credentials\. The federated user cannot get more permissions than the parent IAM user who requested the temporary credentials\. 

You can use these temporary security credentials in making requests to Amazon S3\. The API libraries compute the necessary signature value using those credentials to authenticate your request\. If you send requests using expired credentials, Amazon S3 denies the request\.

For information on signing requests using temporary security credentials in your REST API requests, see [Signing and Authenticating REST Requests](RESTAuthentication.md)\. For information about sending requests using AWS SDKs, see [Making Requests Using the AWS SDKs](MakingAuthenticatedRequests.md)\. 

For more information about IAM support for temporary security credentials, see [Temporary Security Credentials](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_temp.html) in the *IAM User Guide*\.

For added security, you can require multifactor authentication \(MFA\) when accessing your Amazon S3 resources by configuring a bucket policy\. For information, see [Adding a Bucket Policy to Require MFA](example-bucket-policies.md#example-bucket-policies-use-case-7)\. After you require MFA to access your Amazon S3 resources, the only way you can access these resources is by providing temporary credentials that are created with an MFA key\. For more information, see the [AWS Multi\-Factor Authentication](https://aws.amazon.com/mfa/) detail page and [Configuring MFA\-Protected API Access](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_mfa_configure-api-require.html) in the *IAM User Guide*\.

## Request Endpoints<a name="RequestEndpoints"></a>

You send REST requests to the service's predefined endpoint\. For a list of all AWS services and their corresponding endpoints, go to [Regions and Endpoints](http://docs.aws.amazon.com/general/latest/gr/rande.html) in the *AWS General Reference*\.