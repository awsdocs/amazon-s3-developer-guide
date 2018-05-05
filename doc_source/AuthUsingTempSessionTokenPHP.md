# Making Requests Using AWS Account or IAM User Temporary Credentials \- AWS SDK for PHP<a name="AuthUsingTempSessionTokenPHP"></a>

This topic guides explains how to use classes from version 3 of the AWS SDK for PHP to request temporary security credentials and use them to access Amazon S3\. It assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\. 

An IAM user or an AWS account can request temporary security credentials using version 3 of the AWS SDK for PHP\. It can then use the temporary credentials to access Amazon S3\. The credentials expire when the session duration expires\. By default, the session duration is one hour\. If you use IAM user credentials, you can specify the duration \(from 1 to 36 hours\) when requesting the temporary security credentials\. For more information about temporary security credentials, see [Temporary Security Credentials](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_temp.html) in the *IAM User Guide*\. For more information about making requests, see [Making Requests](MakingRequests.md)\.

**Note**  
If you obtain temporary security credentials using your AWS account security credentials, the temporary security credentials are valid for only one hour\. You can specify the session duration only if you use IAM user credentials to request a session\.

**Example**  
The following PHP example lists object keys in the specified bucket using temporary security credentials\. The example obtains temporary security credentials for a default one\-hour session, and uses them to send authenticated request to Amazon S3\. For information about running the PHP examples in this guide, see [Running PHP Examples](UsingTheMPphpAPI.md#running-php-samples)\.  
If you want to test the example using IAM user credentials, you need to create an IAM user under your AWS account\. For information about how to create an IAM user, see [Creating Your First IAM User and Administrators Group](http://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_create-admin-group.html) in the *IAM User Guide*\. For an example of setting the session duration when using IAM user credentials to request a session, see [Making Requests Using Federated User Temporary Credentials \- AWS SDK for PHP](AuthUsingTempFederationTokenPHP.md)\.   

```
 1. <?php
 2. 
 3. require 'vendor/autoload.php';
 4. 
 5. use Aws\Sts\StsClient;
 6. use Aws\S3\S3Client;
 7. use Aws\S3\Exception\S3Exception;
 8. 
 9. $bucket = '*** Your Bucket Name ***';
10. 
11. $sts = new StsClient([
12.     'version' => 'latest',
13.     'region' => 'us-east-1'
14. ]);
15.     
16. $sessionToken = $sts->getSessionToken();
17. 
18. $s3 = new S3Client([
19.     'region' => 'us-east-1',
20.     'version' => 'latest',
21.     'credentials' => [
22.         'key'    => $sessionToken['Credentials']['AccessKeyId'],
23.         'secret' => $sessionToken['Credentials']['SecretAccessKey'],
24.         'token'  => $sessionToken['Credentials']['SessionToken']
25.     ]
26. ]);
27. 
28. $result = $s3->listBuckets();
29. 
30. 
31. try {
32.     // Retrieve a paginator for listing objects.
33.     $objects = $s3->getPaginator('ListObjects', [
34.         'Bucket' => $bucket
35.     ]);
36.     
37.     echo "Keys retrieved!" . PHP_EOL;
38.     
39.     // List objects
40.     foreach ($objects as $object) {
41.         echo $object['Key'] . PHP_EOL;
42.     }
43. } catch (S3Exception $e) {
44.     echo $e->getMessage() . PHP_EOL;
45. }
```

## Related Resources<a name="RelatedResources-AuthUsingTempSessionTokenPHP"></a>
+ [ AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.S3.S3Client.html) 
+ [AWS SDK for PHP Documentation](http://aws.amazon.com/documentation/sdk-for-php/)