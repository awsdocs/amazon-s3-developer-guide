# Making Requests Using Federated User Temporary Credentials \- AWS SDK for PHP<a name="AuthUsingTempFederationTokenPHP"></a>

This topic explains how to use classes from version 3 of the AWS SDK for PHP to request temporary security credentials for federated users and applications and use them to access resources stored in Amazon S3\. It assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\. 

You can provide temporary security credentials to your federated users and applications so they can send authenticated requests to access your AWS resources\. When requesting these temporary credentials, you must provide a user name and an IAM policy that describes the resource permissions that you want to grant\. These credentials expire when the session duration expires\. By default, the session duration is one hour\. You can explicitly set a different value for the duration when requesting the temporary security credentials for federated users and applications\. For more information about temporary security credentials, see [Temporary Security Credentials](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_temp.html) in the *IAM User Guide*\. For information about providing temporary security credentials to your federated users and applications, see [Making Requests](MakingRequests.md)\.

For added security when requesting temporary security credentials for federated users and applications, we recommend using a dedicated IAM user with only the necessary access permissions\. The temporary user you create can never get more permissions than the IAM user who requested the temporary security credentials\. For information about identity federation, see [AWS Identity and Access Management FAQs](https://aws.amazon.com/iam/faqs/#What_are_the_best_practices_for_using_temporary_security_credentials)\.

For information about running the PHP examples in this guide, see [Running PHP Examples](UsingTheMPphpAPI.md#running-php-samples)\.

**Example**  
The following PHP example lists keys in the specified bucket\. In the example, you obtain temporary security credentials for an hour session for your federated user \(User1\)\. Then you use the temporary security credentials to send authenticated requests to Amazon S3\.   
For added security when requesting temporary credentials for others, you use the security credentials of an IAM user who has permissions to request temporary security credentials\. To ensure that the IAM user grants only the minimum application\-specific permissions to the federated user, you can also limit the access permissions of this IAM user\. This example lists only objects in a specific bucket\. Create an IAM user with the following policy attached:   

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
You can now use the IAM user security credentials to test the following example\. The example sends an authenticated request to Amazon S3 using temporary security credentials\. When requesting temporary security credentials for the federated user \(User1\), the example specifies the following policy, which restricts access to list objects in a specific bucket\. Update the policy with your bucket name\.  

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
In the following example, when specifying the policy resource, replace `YourBucketName` with the name of your bucket\.:  

```
<?php

require 'vendor/autoload.php';

use Aws\Sts\StsClient;
use Aws\S3\S3Client;
use Aws\S3\Exception\S3Exception;

$bucket = '*** Your Bucket Name ***';

// In real applications, the following code is part of your trusted code. It has
// the security credentials that you use to obtain temporary security credentials.
$sts = new StsClient(
    [
    'version' => 'latest',
    'region' => 'us-east-1']
);

// Fetch the federated credentials.
$sessionToken = $sts->getFederationToken([
    'Name'              => 'User1',
    'DurationSeconds'    => '3600',
    'Policy'            => json_encode([
        'Statement' => [
            'Sid'              => 'randomstatementid' . time(),
            'Action'           => ['s3:ListBucket'],
            'Effect'           => 'Allow',
            'Resource'         => 'arn:aws:s3:::' . $bucket
        ]
    ])
]);

// The following will be part of your less trusted code. You provide temporary
// security credentials so the code can send authenticated requests to Amazon S3.

$s3 = new S3Client([
    'region' => 'us-east-1',
    'version' => 'latest',
    'credentials' => [
        'key'    => $sessionToken['Credentials']['AccessKeyId'],
        'secret' => $sessionToken['Credentials']['SecretAccessKey'],
        'token'  => $sessionToken['Credentials']['SessionToken']
    ]
]);

try {
    $result = $s3->listObjects([
        'Bucket' => $bucket
    ]);
} catch (S3Exception $e) {
    echo $e->getMessage() . PHP_EOL;
}
```

## Related Resources<a name="RelatedResources-AuthUsingTempFederationTokenPHP"></a>
+ [ AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.S3.S3Client.html) 
+ [AWS SDK for PHP Documentation](http://aws.amazon.com/documentation/sdk-for-php/)