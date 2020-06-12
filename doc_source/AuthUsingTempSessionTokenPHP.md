# Making requests using AWS account or IAM user temporary credentials \- AWS SDK for PHP<a name="AuthUsingTempSessionTokenPHP"></a>

This topic guides explains how to use classes from version 3 of the AWS SDK for PHP to request temporary security credentials and use them to access Amazon S3\. It assumes that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\. 

An IAM user or an AWS account can request temporary security credentials using version 3 of the AWS SDK for PHP\. It can then use the temporary credentials to access Amazon S3\. The credentials expire when the session duration expires\. 

By default, the session duration is one hour\. If you use IAM user credentials, you can specify the duration when requesting the temporary security credentials from 15 minutes to the maximum session duration for the role\. For more information about temporary security credentials, see [Temporary Security Credentials](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_temp.html) in the *IAM User Guide*\. For more information about making requests, see [Making requests](MakingRequests.md)\.

**Note**  
If you obtain temporary security credentials using your AWS account security credentials, the temporary security credentials are valid for only one hour\. You can specify the session duration only if you use IAM user credentials to request a session\.

**Example**  
The following PHP example lists object keys in the specified bucket using temporary security credentials\. The example obtains temporary security credentials for a default one\-hour session, and uses them to send authenticated request to Amazon S3\. For information about running the PHP examples in this guide, see [Running PHP Examples](UsingTheMPphpAPI.md#running-php-samples)\.  
If you want to test the example using IAM user credentials, you need to create an IAM user under your AWS account\. For information about how to create an IAM user, see [Creating Your First IAM User and Administrators Group](https://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started_create-admin-group.html) in the *IAM User Guide*\. For an example of setting the session duration when using IAM user credentials to request a session, see [Making requests using federated user temporary credentials \- AWS SDK for PHP](AuthUsingTempFederationTokenPHP.md)\.   

```
 require 'vendor/autoload.php';

use Aws\Sts\StsClient;
use Aws\S3\S3Client;
use Aws\S3\Exception\S3Exception;

$bucket = '*** Your Bucket Name ***';

$sts = new StsClient([
    'version' => 'latest',
    'region' => 'us-east-1'
]);
    
$sessionToken = $sts->getSessionToken();

$s3 = new S3Client([
    'region' => 'us-east-1',
    'version' => 'latest',
    'credentials' => [
        'key'    => $sessionToken['Credentials']['AccessKeyId'],
        'secret' => $sessionToken['Credentials']['SecretAccessKey'],
        'token'  => $sessionToken['Credentials']['SessionToken']
    ]
]);

$result = $s3->listBuckets();


try {
    // Retrieve a paginator for listing objects.
    $objects = $s3->getPaginator('ListObjects', [
        'Bucket' => $bucket
    ]);
    
    echo "Keys retrieved!" . PHP_EOL;
    
    // List objects
    foreach ($objects as $object) {
        echo $object['Key'] . PHP_EOL;
    }
} catch (S3Exception $e) {
    echo $e->getMessage() . PHP_EOL;
}
```

## Related resources<a name="RelatedResources-AuthUsingTempSessionTokenPHP"></a>
+ [ AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](https://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.S3.S3Client.html) 
+ [AWS SDK for PHP Documentation](http://aws.amazon.com/documentation/sdk-for-php/)