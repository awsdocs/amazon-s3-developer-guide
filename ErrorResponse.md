# Error Response<a name="ErrorResponse"></a>

**Topics**
+ [Error Code](ErrorCode.md)
+ [Error Message](ErrorMessage.md)
+ [Further Details](ErrorDetails.md)

When an Amazon S3 request is in error, the client receives an error response\. The exact format of the error response is API specific: For example, the REST error response differs from the SOAP error response\. However, all error responses have common elements\.

**Note**  
 SOAP support over HTTP is deprecated, but it is still available over HTTPS\. New Amazon S3 features will not be supported for SOAP\. We recommend that you use either the REST API or the AWS SDKs\. 