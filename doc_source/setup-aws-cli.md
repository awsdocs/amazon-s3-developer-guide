# Setting Up the AWS CLI<a name="setup-aws-cli"></a>

Follow the steps to download and configure AWS Command Line Interface \(AWS CLI\)\.

**Note**  
Services in AWS, such as Amazon S3, require that you provide credentials when you access them\. The service can then determine whether you have permissions to access the resources that it owns\. The console requires your password\. You can create access keys for your AWS account to access the AWS CLI or API\. However, we don't recommend that you access AWS using the credentials for your AWS account\. Instead, we recommend that you use AWS Identity and Access Management \(IAM\)\. Create an IAM user, add the user to an IAM group with administrative permissions, and then grant administrative permissions to the IAM user that you created\. You can then access AWS using a special URL and that IAM user's credentials\. For instructions, go to [Creating Your First IAM User and Administrators Group](http://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_create-admin-group.html) in the *IAM User Guide*\.

**To set up the AWS CLI**

1.  Download and configure the AWS CLI\. For instructions, see the following topics in the *AWS Command Line Interface User Guide*: 
   +  [Getting Set Up with the AWS Command Line Interface](http://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-set-up.html) 
   +  [Configuring the AWS Command Line Interface](http://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html) 

1.  Add a named profile for the administrator user in the AWS CLI config file\. You use this profile when executing the AWS CLI commands\. 

   ```
   [adminuser] 
   aws_access_key_id = adminuser access key ID 
   aws_secret_access_key = adminuser secret access key 
   region = aws-region
   ```

    For a list of available AWS Regions, see [Regions and Endpoints](http://docs.aws.amazon.com/general/latest/gr/rande.html) in the *AWS General Reference*\. 

1.  Verify the setup by typing the following commands at the command prompt\. 
   +  Try the `help` command to verify that the AWS CLI is installed on your computer: 

     ```
     aws help  
     ```
   +  Try an `S3` command to verify that the user can reach Amazon S3\. This command lists buckets in your account\. The AWS CLI uses the `adminuser` credentials to authenticate the request\. 

     ```
      aws s3 ls --profile adminuser
     ```