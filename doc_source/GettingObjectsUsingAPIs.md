# Getting Objects<a name="GettingObjectsUsingAPIs"></a>

**Topics**
+ [Related Resources](#RelatedResources013)
+ [Get an Object Using the AWS SDK for Java](RetrievingObjectUsingJava.md)
+ [Get an Object Using the AWS SDK for \.NET](RetrievingObjectUsingNetSDK.md)
+ [Get an Object Using the AWS SDK for PHP](RetrieveObjSingleOpPHP.md)
+ [Get an Object Using the REST API](RetrieveObjSingleOpREST.md)
+ [Share an Object with Others](ShareObjectPreSignedURL.md)

 You can retrieve objects directly from Amazon S3\. You have the following options when retrieving an object: 
+ **Retrieve an entire object—**A single GET operation can return you the entire object stored in Amazon S3\. 
+ **Retrieve object in parts—**Using the `Range` HTTP header in a GET request, you can retrieve a specific range of bytes in an object stored in Amazon S3\. 

  You resume fetching other parts of the object whenever your application is ready\. This resumable download is useful when you need only portions of your object data\. It is also useful where network connectivity is poor and you need to react to failures\.
**Note**  
Amazon S3 doesn't support retrieving multiple ranges of data per GET request\.

 When you retrieve an object, its metadata is returned in the response headers\. There are times when you want to override certain response header values returned in a GET response\. For example, you might override the `Content-Disposition` response header value in your GET request\. The REST GET Object API \(see [GET Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html)\) allows you to specify query string parameters in your GET request to override these values\. 

The AWS SDKs for Java, \.NET, and PHP also provide necessary objects you can use to specify values for these response headers in your GET request\. 

When retrieving objects that are stored encrypted using server\-side encryption you will need to provide appropriate request headers\. For more information, see [Protecting Data Using Encryption](UsingEncryption.md)\.

## Related Resources<a name="RelatedResources013"></a>
+ [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)