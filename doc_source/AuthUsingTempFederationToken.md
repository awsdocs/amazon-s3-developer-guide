# Making Requests Using Federated User Temporary Credentials<a name="AuthUsingTempFederationToken"></a>


+ [Making Requests Using Federated User Temporary Credentials \- AWS SDK for Java](AuthUsingTempFederationTokenJava.md)
+ [Making Requests Using Federated User Temporary Credentials \- AWS SDK for \.NET](AuthUsingTempFederationTokenDotNet.md)
+ [Making Requests Using Federated User Temporary Credentials \- AWS SDK for PHP](AuthUsingTempFederationTokenPHP.md)
+ [Making Requests Using Federated User Temporary Credentials \- AWS SDK for Ruby](AuthUsingTempFederationTokenRuby.md)

You can request temporary security credentials and provide them to your federated users or applications who need to access your AWS resources\. This section provides examples of how you can use the AWS SDK to obtain temporary security credentials for your federated users or applications and send authenticated requests to Amazon S3 using those credentials\. For a list of available AWS SDKs go to, [Sample Code and Libraries](https://aws.amazon.com/code/)\. 

**Note**  
Both the AWS account and an IAM user can request temporary security credentials for federated users\. However, for added security, only an IAM user with the necessary permissions should request these temporary credentials to ensure that the federated user gets at most the permissions of the requesting IAM user\. In some applications, you might find suitable to create an IAM user with specific permissions for the sole purpose of granting temporary security credentials to your federated users and applications\.