# Making Requests Using Federated User Temporary Credentials \- AWS SDK for PHP<a name="AuthUsingTempFederationTokenPHP"></a>

This topic guides you through using classes from the AWS SDK for PHP to request temporary security credentials for federated users and applications and use them to access Amazon S3\. 

**Note**  
 This topic assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\. 

You can provide temporary security credentials to your federated users and applications \(see [Making Requests](MakingRequests.md)\) so they can send authenticated requests to access your AWS resources\. When requesting these temporary credentials, you must provide a user name and an IAM policy describing the resource permissions you want to grant\. These credentials expire when the session duration expires\. By default, the session duration is one hour\. You can explicitly set a different duration value when requesting the temporary security credentials for federated users and applications\. For more information about temporary security credentials, see [Temporary Security Credentials](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_temp.html) in the *IAM User Guide*\.

To request temporary security credentials for federated users and applications, for added security, you might want to use a dedicated IAM user with only the necessary access permissions\. The temporary user you create can never get more permissions than the IAM user who requested the temporary security credentials\. For information about identity federation, go to [AWS Identity and Access Management FAQs](https://aws.amazon.com/iam/faqs/#What_are_the_best_practices_for_using_temporary_security_credentials)\.


**Making Requests Using Federated User Temporary Credentials**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of an AWS Security Token Service \(AWS STS\) client by using the [Aws\\Sts\\StsClient](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.Sts.StsClient.html) class [factory\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.Sts.StsClient.html#_factory) method\.  | 
|  2  |  Execute the [Aws\\Sts\\StsClient::getFederationToken\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.Sts.StsClient.html#_getFederationToken) method by providing the name of the federated user in the `array` parameter's required `Name` key\. You can also add the optional `array` parameter's `Policy` and `DurationSeconds` keys\.  The method returns temporary security credentials that you can provide to your federated users\.  | 
|  3  |  Any federated user who has the temporary security credentials can send requests to Amazon S3 by creating an instance of an Amazon S3 client by using [Aws\\S3\\S3Client](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html) class [factory](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory) method with the temporary security credentials\. Any methods in the `S3Client` class that you call use the temporary security credentials to send authenticated requests to Amazon S3\.   | 

The following PHP code sample demonstrates obtaining temporary security credentials for a federated user and using the credentials to access Amazon S3\.

**Example**  

```
 1. use Aws\Sts\StsClient;
 2. use Aws\S3\S3Client;
 3. 
 4. // In real applications, the following code is part of your trusted code. It has 
 5. // your security credentials that you use to obtain temporary security credentials.
 6. $sts = StsClient::factory();
 7. 
 8. // Fetch the federated credentials.
 9. $result = $sts->getFederationToken(array(
10.     'Name'            => 'User1',
11.     'DurationSeconds' => 3600,
12.     'Policy'          => json_encode(array(
13.         'Statement' => array(
14.             array(
15.                 'Sid'      => 'randomstatementid' . time(),
16.                 'Action'   => array('s3:ListBucket'),
17.                 'Effect'   => 'Allow',
18.                 'Resource' => 'arn:aws:s3:::YourBucketName'
19.             )
20.         )
21.     ))
22. ));
23. 
24. // The following will be part of your less trusted code. You provide temporary
25. // security credentials so it can send authenticated requests to Amazon S3. 
26. $credentials = $result->get('Credentials');
27. $s3 = new S3Client::factory(array( 
28.     'key'    => $credentials['AccessKeyId'],
29.     'secret' => $credentials['SecretAccessKey'],
30.     'token'  => $credentials['SessionToken']
31. )); 
32. 
33. $result = $s3->listObjects();
```

**Example of a Federated User Making an Amazon S3 Request Using Temporary Security Credentials**  
The following PHP code example lists keys in the specified bucket\. In the code example, you first obtain temporary security credentials for an hour session for your federated user \(User1\) and use them to send authenticated requests to Amazon S3\. For information about running the PHP examples in this guide, go to [Running PHP Examples](UsingTheMPphpAPI.md#running-php-samples)\.  
When requesting temporary credentials for others, for added security, you use the security credentials of an IAM user who has permissions to request temporary security credentials\. You can also limit the access permissions of this IAM user to ensure that the IAM user grants only the minimum application\-specific permissions to the federated user\. This example only lists objects in a specific bucket\. Therefore, first create an IAM user with the following policy attached\.   

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
You can now use the IAM user security credentials to test the following example\. The example sends an authenticated request to Amazon S3 using temporary security credentials\. The example specifies the following policy when requesting temporary security credentials for the federated user \(User1\) which restricts access to list objects in a specific bucket\. You must update the policy with your own existing bucket name\.  

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
In the following example you must replace YourBucketName with your own existing bucket name when specifying the policy resource\.  

```
<?php

// Include the AWS SDK using the Composer autoloader.
require 'vendor/autoload.php';

$bucket = '*** Your Bucket Name ***';

use Aws\Sts\StsClient;
use Aws\S3\S3Client;
use Aws\S3\Exception\S3Exception;

// Instantiate the client.
$sts = StsClient::factory();

$result = $sts->getFederationToken(array(
    'Name'            => 'User1',
    'DurationSeconds' => 3600,
    'Policy'          => json_encode(array(
        'Statement' => array(
            array(
                'Sid'      => 'randomstatementid' . time(),
                'Action'   => array('s3:ListBucket'),
                'Effect'   => 'Allow',
                'Resource' => 'arn:aws:s3:::YourBucketName'
            )
        )
    ))
));

$credentials = $result->get('Credentials');
$s3 = S3Client::factory(array( 
    'key'    => $credentials['AccessKeyId'],
    'secret' => $credentials['SecretAccessKey'],
    'token'  => $credentials['SessionToken']
)); 

try {
    $objects = $s3->getIterator('ListObjects', array(
        'Bucket' => $bucket
    ));

    echo "Keys retrieved!\n";
    foreach ($objects as $object) {
        echo $object['Key'] . "\n";
    }
} catch (S3Exception $e) {
    echo $e->getMessage() . "\n";
}
```

## Related Resources<a name="RelatedResources-AuthUsingTempFederationTokenPHP"></a>

+  [AWS SDK for PHP for Amazon S3 Aws\\Sts\\StsClient Class](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.Sts.StsClient.html)

+ [AWS SDK for PHP for Amazon S3 Aws\\Sts\\StsClient::factory\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.Sts.StsClient.html#_factory)

+ [AWS SDK for PHP for Amazon S3 Aws\\Sts\\StsClient::getSessionToken\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.Sts.StsClient.html#_getSessionToken)

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html) 

+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::factory\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory) 

+ [AWS SDK for PHP for Amazon S3](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/service-s3.html)

+ [AWS SDK for PHP Documentation](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/index.html)