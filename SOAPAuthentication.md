# Authenticating SOAP Requests<a name="SOAPAuthentication"></a>

**Note**  
 SOAP support over HTTP is deprecated, but it is still available over HTTPS\. New Amazon S3 features will not be supported for SOAP\. We recommend that you use either the REST API or the AWS SDKs\. 

Every non\-anonymous request must contain authentication information to establish the identity of the principal making the request\. In SOAP, the authentication information is put into the following elements of the SOAP request:
+ Your AWS Access Key ID
**Note**  
When making authenticated SOAP requests, temporary security credentials are not supported\. For more information about types of credentials, see [Making Requests](MakingRequests.md)\.
+ `Timestamp:` This must be a dateTime \(go to [http://www\.w3\.org/TR/xmlschema\-2/\#dateTime](http://www.w3.org/TR/xmlschema-2/#dateTime)\) in the Coordinated Universal Time \(Greenwich Mean Time\) time zone, such as `2009-01-01T12:00:00.000Z`\. Authorization will fail if this timestamp is more than 15 minutes away from the clock on Amazon S3 servers\.
+ `Signature:` The RFC 2104 HMAC\-SHA1 digest \(go to [http://www\.ietf\.org/rfc/rfc2104\.txt](http://www.ietf.org/rfc/rfc2104.txt)\) of the concatenation of "AmazonS3" \+ OPERATION \+ Timestamp, using your AWS Secret Access Key as the key\. For example, in the following CreateBucket sample request, the signature element would contain the HMAC\-SHA1 digest of the value "AmazonS3CreateBucket2009\-01\-01T12:00:00\.000Z":

For example, in the following CreateBucket sample request, the signature element would contain the HMAC\-SHA1 digest of the value "AmazonS3CreateBucket2009\-01\-01T12:00:00\.000Z":

**Example**  

```
1. <CreateBucket xmlns="http://doc.s3.amazonaws.com/2006-03-01">
2.   <Bucket>quotes</Bucket>
3.   <Acl>private</Acl>
4.   <AWSAccessKeyId>AKIAIOSFODNN7EXAMPLE</AWSAccessKeyId>
5.   <Timestamp>2009-01-01T12:00:00.000Z</Timestamp>
6.   <Signature>Iuyz3d3P0aTou39dzbqaEXAMPLE=</Signature>
7. </CreateBucket>
```

**Note**  
SOAP requests, both authenticated and anonymous, must be sent to Amazon S3 using SSL\. Amazon S3 returns an error when you send a SOAP request over HTTP\. 

**Important**  
Due to different interpretations regarding how extra time precision should be dropped, \.NET users should take care not to send Amazon S3 overly specific time stamps\. This can be accomplished by manually constructing `DateTime` objects with only millisecond precision\.