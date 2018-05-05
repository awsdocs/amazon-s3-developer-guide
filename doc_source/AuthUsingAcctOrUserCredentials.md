# Making Requests Using AWS Account or IAM User Credentials<a name="AuthUsingAcctOrUserCredentials"></a>

You can use your AWS account or IAM user security credentials to send authenticated requests to Amazon S3\. This section provides examples of how you can send authenticated requests using the AWS SDK for Java, AWS SDK for \.NET, and AWS SDK for PHP\. For a list of available AWS SDKs, go to [Sample Code and Libraries](https://aws.amazon.com/code/)\. 

**Topics**
+ [Making Requests Using AWS Account or IAM User Credentials \- AWS SDK for Java](AuthUsingAcctOrUserCredJava.md)
+ [Making Requests Using AWS Account or IAM User Credentials \- AWS SDK for \.NET](AuthUsingAcctOrUserCredDotNet.md)
+ [Making Requests Using AWS Account or IAM User Credentials \- AWS SDK for PHP](AuthUsingAcctOrUserCredPHP3.md)
+ [Making Requests Using AWS Account or IAM User Credentials \- AWS SDK for Ruby](AuthUsingAcctOrUserCredRuby.md)

Each of these AWS SDKs uses an SDK\-specific credentials provider chain to find and use credentials and perform actions on behalf of the credentials owner\. What all these credentials provider chains have in common is that they all look for your local AWS credentials file\. 

The easiest way to configure credentials for your AWS SDKs is to use an AWS credentials file\. If you use the AWS Command Line Interface \(AWS CLI\), you may already have a local AWS credentials file configured\. Otherwise, use the following procedure to set up a credentials file:

**To create a local AWS credentials file**

1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.

1. Create a new user with permissions limited to the services and actions that you want your code to have access to\. For more information about creating a new IAM user, see [Creating IAM Users \(Console\)](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html#id_users_create_console), and follow the instructions through step 8\.

1. Choose **Download \.csv** to save a local copy of your AWS credentials\.

1. On your computer, navigate to your home directory, and create an `.aws` directory\. On Unix\-based systems, such as Linux or OS X, this is in the following location:

   ```
   ~/.aws
   ```

   On Windows, this is in the following location:

   ```
   %HOMEPATH%\.aws
   ```

1. In the `.aws` directory, create a new file named `credentials`\.

1. Open the credentials \.csv file that you downloaded from the IAM console, and copy its contents into the `credentials` file using the following format:

   ```
   [default]
   aws_access_key_id = your_access_key_id
   aws_secret_access_key = your_secret_access_key
   ```

1. Save the `credentials` file, and delete the \.csv file that you downloaded in step 3\.

Your shared credentials file is now configured on your local computer, and it's ready to be used with the AWS SDKs\.