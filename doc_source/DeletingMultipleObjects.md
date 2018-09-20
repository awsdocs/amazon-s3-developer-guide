# Deleting Multiple Objects Per Request<a name="DeletingMultipleObjects"></a>

**Topics**
+ [Deleting Multiple Objects Using the AWS SDK for Java](DeletingMultipleObjectsUsingJava.md)
+ [Deleting Multiple Objects Using the AWS SDK for \.NET](DeletingMultipleObjectsUsingNetSDK.md)
+ [Deleting Multiple Objects Using the AWS SDK for PHP](DeletingMultipleObjectsUsingPHPSDK.md)
+ [Deleting Multiple Objects Using the REST API](DeletingMultipleObjectsUsingREST.md)

Amazon S3 provides the Multi\-Object Delete API \(see [Delete \- Multi\-Object Delete](http://docs.aws.amazon.com/AmazonS3/latest/API/multiobjectdeleteapi.html)\), which enables you to delete multiple objects in a single request\. The API supports two modes for the response: verbose and quiet\. By default, the operation uses verbose mode\. In verbose mode, the response includes the result of the deletion of each key that is specified in your request\. In quiet mode, the response includes only keys for which the delete operation encountered an error\. If all keys are successfully deleted when you're using quiet mode, Amazon S3 returns an empty response\.

To learn more about object deletion, see [Deleting Objects](DeletingObjects.md)\. 

You can use the REST API directly or use the AWS SDKs\. 