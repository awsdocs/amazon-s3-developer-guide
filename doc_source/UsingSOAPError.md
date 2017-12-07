# The SOAP Error Response<a name="UsingSOAPError"></a>

**Note**  
 SOAP support over HTTP is deprecated, but it is still available over HTTPS\. New Amazon S3 features will not be supported for SOAP\. We recommend that you use either the REST API or the AWS SDKs\. 

In SOAP, an error result is returned to the client as a SOAP fault, with the HTTP response code 500\. If you do not receive a SOAP fault, then your request was successful\. The Amazon S3 SOAP fault code is comprised of a standard SOAP 1\.1 fault code \(either "Server" or "Client"\) concatenated with the Amazon S3\-specific error code\. For example: "Server\.InternalError" or "Client\.NoSuchBucket"\. The SOAP fault string element contains a generic, human readable error message in English\. Finally, the SOAP fault detail element contains miscellaneous information relevant to the error\.

For example, if you attempt to delete the object "Fred", which does not exist, the body of the SOAP response contains a "NoSuchKey" SOAP fault\.

**Example**  

```
1. <soapenv:Body>
2.   <soapenv:Fault>
3.     <Faultcode>soapenv:Client.NoSuchKey</Faultcode>
4.     <Faultstring>The specified key does not exist.</Faultstring>
5.     <Detail>
6.       <Key>Fred</Key>
7.     </Detail>
8.   </soapenv:Fault>
9. </soapenv:Body>
```

For more information about Amazon S3 errors, go to [ErrorCodeList](http://docs.aws.amazon.com/AmazonS3/latest/API/ErrorResponses.html)\.