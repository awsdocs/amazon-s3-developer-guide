# Setting Up the Tools for the Example Walkthroughs<a name="policy-eval-walkthrough-download-awscli"></a>

The introductory examples \(see [Example Walkthroughs: Managing Access to Your Amazon S3 Resources ](example-walkthroughs-managing-access.md)\) use the AWS Management Console to create resources and grant permissions\. And to test permissions, the examples use the command line tools, AWS Command Line Interface \(CLI\) and AWS Tools for Windows PowerShell, so you don't need to write any code\. To test permissions, you must set up one of these tools\. 

**To set up the AWS CLI**

1. Download and configure the AWS CLI\. For instructions, see the following topics in the *AWS Command Line Interface User Guide*\. 

    [Getting Set Up with the AWS Command Line Interface](http://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-set-up.html) 

    [Installing the AWS Command Line Interface](http://docs.aws.amazon.com/cli/latest/userguide/installing.html) 

   [Configuring the AWS Command Line Interface](http://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html)

1. Set the default profile\. 

   You will store user credentials in the AWS CLI config file\. Create a default profile in the config file using your AWS account credentials\. See [Configuration and Credential Files](http://docs.aws.amazon.com/cli/latest/userguide/cli-config-files.html) for instructions on finding and editing your AWS CLI config file\.

   ```
   [default]
   aws_access_key_id = access key ID
   aws_secret_access_key = secret access key
   region = us-west-2
   ```

1. Verify the setup by entering the following command at the command prompt\. Both these commands don't provide credentials explicitly, so the credentials of the default profile are used\.
   + Try the help command

     ```
     aws help
     ```
   + Use `aws s3 ls` to get a list of buckets on the configured account\.

     ```
     aws s3 ls
     ```

As you go through the example walkthroughs, you will create users, and you will save user credentials in the config files by creating profiles, as the following example shows\. Note that these profiles have names \(AccountAadmin and AccountBadmin\):

```
[profile AccountAadmin]
aws_access_key_id = User AccountAadmin access key ID
aws_secret_access_key = User AccountAadmin secret access key
region = us-west-2

[profile AccountBadmin]
aws_access_key_id = Account B access key ID
aws_secret_access_key = Account B secret access key
region = us-east-1
```

To execute a command using these user credentials, you add the `--profile` parameter specifying the profile name\. The following AWS CLI command retrieves a listing of objects in `examplebucket` and specifies the `AccountBadmin` profile\. 

```
aws s3 ls s3://examplebucket --profile AccountBadmin
```

Alternatively, you can configure one set of user credentials as the default profile by changing the `AWS_DEFAULT_PROFILE` environment variable from the command prompt\. Once you've done this, whenever you execute AWS CLI commands without the `--profile` parameter, the AWS CLI will use the profile you set in the environment variable as the default profile\.

```
$ export AWS_DEFAULT_PROFILE=AccountAadmin
```

**To set up AWS Tools for Windows PowerShell**

1. Download and configure the AWS Tools for Windows PowerShell\. For instructions, go to [Download and Install the AWS Tools for Windows PowerShell](http://docs.aws.amazon.com/powershell/latest/userguide/pstools-getting-set-up.html#pstools-installing-download) in the *AWS Tools for Windows PowerShell User Guide*\. 
**Note**  
 In order to load the AWS Tools for Windows PowerShell module, you need to enable PowerShell script execution\. For more information, go to [Enable Script Execution](http://docs.aws.amazon.com/powershell/latest/userguide/pstools-getting-set-up.html#enable-script-execution) in the *AWS Tools for Windows PowerShell User Guide*\.

1. For these exercises, you will specify AWS credentials per session using the `Set-AWSCredentials` command\. The command saves the credentials to a persistent store \(`-StoreAs `parameter\)\.

   ```
   Set-AWSCredentials -AccessKey AccessKeyID -SecretKey SecretAccessKey -storeas string
   ```

1. Verify the setup\.
   + Execute the `Get-Command` to retrieve a list of available commands you can use for Amazon S3 operations\. 

     ```
     Get-Command -module awspowershell -noun s3* -StoredCredentials string
     ```
   + Execute the `Get-S3Object` command to retrieve a list of objects in a bucket\.

     ```
     Get-S3Object -BucketName bucketname -StoredCredentials string
     ```

For a list of commands, go to [Amazon Simple Storage Service Cmdlets](http://docs.aws.amazon.com/powershell/latest/reference/Index.html)\. 

Now you are ready to try the exercises\. Follow the links provided at the beginning of the section\.