# Common SOAP API Elements<a name="UsingSOAPOperations"></a>

**Note**  
 SOAP support over HTTP is deprecated, but it is still available over HTTPS\. New Amazon S3 features will not be supported for SOAP\. We recommend that you use either the REST API or the AWS SDKs\. 

You can interact with Amazon S3 using SOAP 1\.1 over HTTP\. The Amazon S3 WSDL, which describes the Amazon S3 API in a machine\-readable way, is available at: [http://doc\.s3\.amazonaws\.com/2006\-03\-01/AmazonS3\.wsdl](http://doc.s3.amazonaws.com/2006-03-01/AmazonS3.wsdl)\. The Amazon S3 schema is available at [http://doc\.s3\.amazonaws\.com/2006\-03\-01/AmazonS3\.xsd](http://doc.s3.amazonaws.com/2006-03-01/AmazonS3.xsd)\.

Most users will interact with Amazon S3 using a SOAP toolkit tailored for their language and development environment\. Different toolkits will expose the Amazon S3 API in different ways\. Please refer to your specific toolkit documentation to understand how to use it\. This section illustrates the Amazon S3 SOAP operations in a toolkit\-independent way by exhibiting the XML requests and responses as they appear "on the wire\."

## Common Elements<a name="SOAPCommon"></a>

You can include the following authorization\-related elements with any SOAP request:
+ `AWSAccessKeyId:` The AWS Access Key ID of the requester
+ `Timestamp:` The current time on your system
+ `Signature:` The signature for the request