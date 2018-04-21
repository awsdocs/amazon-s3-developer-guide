# Making Requests Using the AWS SDKs<a name="MakingAuthenticatedRequests"></a>

**Topics**
+ [Making Requests Using AWS Account or IAM User Credentials](AuthUsingAcctOrUserCredentials.md)
+ [Making Requests Using IAM User Temporary Credentials](AuthUsingTempSessionToken.md)
+ [Making Requests Using Federated User Temporary Credentials](AuthUsingTempFederationToken.md)

You can send authenticated requests to Amazon S3 using either the AWS SDK or by making the REST API calls directly in your application\. The AWS SDK API uses the credentials that you provide to compute the signature for authentication\. If you use the REST API directly in your applications, you must write the necessary code to compute the signature for authenticating your request\. For a list of available AWS SDKs go to, [Sample Code and Libraries](https://aws.amazon.com/code/)\. 