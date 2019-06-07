# Get an Object Using the AWS SDK for Java<a name="RetrievingObjectUsingJava"></a>

When you download an object through the AWS SDK for Java, Amazon S3 returns all of the object's metadata and an input stream from which to read the object's contents\.

To retrieve an object, you do the following:
+ Execute the `AmazonS3Client.getObject()` method, providing the bucket name and object key in the request\.
+ Execute one of the `S3Object` instance methods to process the input stream\.

**Note**  
Your network connection remains open until you read all of the data or close the input stream\. We recommend that you read the content of the stream as quickly as possible\.

The following are some variations you might use:
+ Instead of reading the entire object, you can read only a portion of the object data by specifying the byte range that you want in the request\.
+ You can optionally override the response header values \(see [Getting Objects](GettingObjectsUsingAPIs.md)\) by using a `ResponseHeaderOverrides` object and setting the corresponding request property\. For example, you can use this feature to indicate that the object should be downloaded into a file with a different file name than the object key name\.

The following example retrieves an object from an Amazon S3 bucket three ways: first, as a complete object, then as a range of bytes from the object, then as a complete object with overridden response header values\. For more information about getting objects from Amazon S3, see [GET Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html)\. For instructions on creating and testing a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\.

```
 require 'vendor/autoload.php';

use Aws\S3\S3Client;
use Aws\S3\Exception\S3Exception;

$bucket = '*** Your Bucket Name ***';
$keyname = '*** Your Object Key ***';

$s3 = new S3Client([
    'version' => 'latest',
    'region'  => 'us-east-1'
]);

try {
    // Get the object.
    $result = $s3->getObject([
        'Bucket' => $bucket,
        'Key'    => $keyname
    ]);

    // Display the object in the browser.
    header("Content-Type: {$result['ContentType']}");
    echo $result['Body'];
} catch (S3Exception $e) {
    echo $e->getMessage() . PHP_EOL;
}
```