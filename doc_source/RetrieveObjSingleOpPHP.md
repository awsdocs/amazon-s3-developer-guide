# Get an Object Using the AWS SDK for PHP<a name="RetrieveObjSingleOpPHP"></a>

This topic explains how to use a class from the AWS SDK for PHP to retrieve an Amazon S3 object\. You can retrieve an entire object or a byte range from the object\. We assume that you are already following the instructions for [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md) and have the AWS SDK for PHP properly installed\. 

When retrieving an object, you can optionally override the response header values by adding the response keys, `ResponseContentType`, `ResponseContentLanguage`, `ResponseContentDisposition`, `ResponseCacheControl`, and `ResponseExpires`, to the `getObject()` method, as shown in the following PHP code example:

**Example**  

```
1. $result = $s3->getObject([
2.     'Bucket'                     => $bucket,
3.     'Key'                        => $keyname,
4.     'ResponseContentType'        => 'text/plain',
5.     'ResponseContentLanguage'    => 'en-US',
6.     'ResponseContentDisposition' => 'attachment; filename=testing.txt',
7.     'ResponseCacheControl'       => 'No-cache',
8.     'ResponseExpires'            => gmdate(DATE_RFC2822, time() + 3600),
9. ]);
```

For more information about retrieving objects, see [Getting Objects](GettingObjectsUsingAPIs.md)\. 

The following PHP example retrieves an object and displays the content of the object in the browser\. The example shows how to use the `getObject()` method\. For information about running the PHP examples in this guide, see [Running PHP Examples](UsingTheMPphpAPI.md#running-php-samples)\. 

```
 1. <?php
 2. 
 3. require 'vendor/autoload.php';
 4. 
 5. use Aws\S3\S3Client;
 6. use Aws\S3\Exception\S3Exception;
 7. 
 8. $bucket = '*** Your Bucket Name ***';
 9. $keyname = '*** Your Object Key ***';
10. 
11. $s3 = new S3Client([
12.     'version' => 'latest',
13.     'region'  => 'us-east-1'
14. ]);
15. 
16. try {
17.     // Get the object.
18.     $result = $s3->getObject([
19.         'Bucket' => $bucket,
20.         'Key'    => $keyname
21.     ]);
22. 
23.     // Display the object in the browser.
24.     header("Content-Type: {$result['ContentType']}");
25.     echo $result['Body'];
26. } catch (S3Exception $e) {
27.     echo $e->getMessage() . PHP_EOL;
28. }
```

## Related Resources<a name="RelatedResources-RetrieveObjSingleOpPHP"></a>
+ [ AWS SDK for PHP for Amazon S3 Aws\\S3\\S3Client Class](http://docs.aws.amazon.com/aws-sdk-php/v3/api/class-Aws.S3.S3Client.html) 
+ [AWS SDK for PHP Documentation](http://aws.amazon.com/documentation/sdk-for-php/)