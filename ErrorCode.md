# Error Code<a name="ErrorCode"></a>

The error code is a string that uniquely identifies an error condition\. It is meant to be read and understood by programs that detect and handle errors by type\. Many error codes are common across SOAP and REST APIs, but some are API\-specific\. For example, NoSuchKey is universal, but UnexpectedContent can occur only in response to an invalid REST request\. In all cases, SOAP fault codes carry a prefix as indicated in the table of error codes, so that a NoSuchKey error is actually returned in SOAP as Client\.NoSuchKey\.

**Note**  
 SOAP support over HTTP is deprecated, but it is still available over HTTPS\. New Amazon S3 features will not be supported for SOAP\. We recommend that you use either the REST API or the AWS SDKs\. 