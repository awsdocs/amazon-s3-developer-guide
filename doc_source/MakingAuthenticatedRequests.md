# Making requests using the AWS SDKs<a name="MakingAuthenticatedRequests"></a>

**Topics**
+ [Making requests using AWS account or IAM user credentials](AuthUsingAcctOrUserCredentials.md)
+ [Making requests using IAM user temporary credentials](AuthUsingTempSessionToken.md)
+ [Making requests using federated user temporary credentials](AuthUsingTempFederationToken.md)

You can send authenticated requests to Amazon S3 using either the AWS SDK or by making the REST API calls directly in your application\. The AWS SDK API uses the credentials that you provide to compute the signature for authentication\. If you use the REST API directly in your applications, you must write the necessary code to compute the signature for authenticating your request\. For a list of available AWS SDKs go to, [Sample Code and Libraries](https://aws.amazon.com/code/)\. 