# Making Requests Using AWS Account or IAM User Temporary Credentials \- AWS SDK for PHP<a name="AuthUsingTempSessionTokenPHP"></a>

This topic guides you through using classes from the AWS SDK for PHP to request temporary security credentials and use them to access Amazon S3\. 

**Note**  
 This topic assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\. 

An IAM user or an AWS Account can request temporary security credentials \(see [Making Requests](MakingRequests.md)\) using the AWS SDK for PHP and use them to access Amazon S3\. These credentials expire when the session duration expires\. By default, the session duration is one hour\. If you use IAM user credentials, you can specify the duration, between 1 and 36 hours, when requesting the temporary security credentials\. For more information about temporary security credentials, see [Temporary Security Credentials](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_temp.html) in the *IAM User Guide*\.


**Making Requests Using AWS Account or IAM User Temporary Security Credentials**  

|  |  | 
| --- |--- |
|  1  |  Create an instance of an AWS Security Token Service \(AWS STS\) client by using the [Aws\\Sts\\StsClient](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.Sts.StsClient.html) class [factory\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.Sts.StsClient.html#_factory) method\.  | 
|  2  |  Execute the [Aws\\Sts\\StsClient::getSessionToken\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.Sts.StsClient.html#_getSessionToken) method to start a session\. The method returns you temporary security credentials\.  | 
|  3  |  Create an instance of an Amazon S3 client by using the [Aws\\S3\\S3Client](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html) class [factory\(\)](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory) method with the temporary security credentials you obtained in the preceding step\. Any methods in the `S3Client` class that you call use the temporary security credentials to send authenticated requests to Amazon S3\.  | 

The following PHP code sample demonstrates how to request temporary security credentials and use them to access Amazon S3\.

**Example**  

```
 1. use Aws\Sts\StsClient;
 2. use Aws\S3\S3Client;
 3. 
 4. // In real applications, the following code is part of your trusted code. 
 5. // It has your security credentials that you use to obtain temporary 
 6. // security credentials.
 7. $sts = StsClient::factory();
 8. 
 9. $result = $sts->getSessionToken();
10. 
11. // The following will be part of your less trusted code. You provide temporary
12. // security credentials so it can send authenticated requests to Amazon S3. 
13. // Create an Amazon S3 client using temporary security credentials.
14. $credentials = $result->get('Credentials');
15. $s3 = S3Client::factory(array(
16.     'key'    => $credentials['AccessKeyId'],
17.     'secret' => $credentials['SecretAccessKey'],
18.     'token'  => $credentials['SessionToken']
19. ));
20. 
21. $result = $s3->listBuckets();
```

**Note**  
If you obtain temporary security credentials using your AWS account security credentials, the temporary security credentials are valid for only one hour\. You can specify the session duration only if you use IAM user credentials to request a session\.

**Example of Making an Amazon S3 Request Using Temporary Security Credentials**  
The following PHP code example lists object keys in the specified bucket using temporary security credentials\. The code example obtains temporary security credentials for a default one hour session and uses them to send authenticated request to Amazon S3\. For information about running the PHP examples in this guide, go to [Running PHP Examples](UsingTheMPphpAPI.md#running-php-samples)\.  
If you want to test the example using IAM user credentials, you will need to create an IAM user under your AWS Account\. For information about how to create an IAM user, see [Creating Your First IAM User and Administrators Group](http://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_create-admin-group.html) in the *IAM User Guide*\. For an example of setting session duration when using IAM user credentials to request a session, see [Making Requests Using Federated User Temporary Credentials \- AWS SDK for PHP](AuthUsingTempFederationTokenPHP.md)\.   

```
 1. <?php
 2. 
 3. // Include the AWS SDK using the Composer autoloader.
 4. require 'vendor/autoload.php';
 5. 
 6. use Aws\Sts\StsClient;
 7. use Aws\S3\S3Client;
 8. use Aws\S3\Exception\S3Exception;
 9. 
10. $bucket = '*** Your Bucket Name ***';
11. 
12. $sts = StsClient::factory();
13. 
14. $credentials = $sts->getSessionToken()->get('Credentials');
15. $s3 = S3Client::factory(array(
16.     'key'    => $credentials['AccessKeyId'],
17.     'secret' => $credentials['SecretAccessKey'],
18.     'token'  => $credentials['SessionToken']
19. ));
20. 
21. try {
22.     $objects = $s3->getIterator('ListObjects', array(
23.         'Bucket' => $bucket
24.     ));
25. 
26.     echo "Keys retrieved!\n";
27.     foreach ($objects as $object) {
28.         echo $object['Key'] . "\n";
29.     }
30. } catch (S3Exception $e) {
31.     echo $e->getMessage() . "\n";
32. }
```

## Related Resources<a name="RelatedResources-AuthUsingTempSessionTokenPHP"></a>
+  [AWS SDK for PHP for Amazon S3 Aws\\Sts\\StsClient Class](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.Sts.StsClient.html)
+ [AWS SDK for PHP for Amazon S3 Aws\\Sts\\StsClient::factory\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.Sts.StsClient.html#_factory)
+ [AWS SDK for PHP for Amazon S3 Aws\\Sts\\StsClient::getSessionToken\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.Sts.StsClient.html#_getSessionToken)
+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html) 
+ [AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client::factory\(\) Method](http://docs.aws.amazon.com/aws-sdk-php-2/latest/class-Aws.S3.S3Client.html#_factory) 
+ [AWS SDK for PHP for Amazon S3](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/service-s3.html)
+ [AWS SDK for PHP Documentation](http://docs.aws.amazon.com/aws-sdk-php-2/guide/latest/index.html)