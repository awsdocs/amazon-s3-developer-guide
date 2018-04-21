# HTML Forms \(AWS Signature Version 2\)<a name="HTTPPOSTForms"></a>

**Topics**
+ [HTML Form Encoding](#HTTPPOSTFormEncoding)
+ [HTML Form Declaration](#HTTPPOSTFormDeclaration)
+ [HTML Form Fields](#HTTPPOSTFormFields)
+ [Policy Construction](#HTTPPOSTConstructPolicy)
+ [Constructing a Signature](#HTTPPOSTConstructingPolicySignature)
+ [Redirection](#HTTPPOSTConstructingPolicyRedirection)

When you communicate with Amazon S3, you normally use the REST or SOAP API to perform put, get, delete, and other operations\. With POST, users upload data directly to Amazon S3 through their browsers, which cannot process the SOAP API or create a REST `PUT` request\.

**Note**  
 SOAP support over HTTP is deprecated, but it is still available over HTTPS\. New Amazon S3 features will not be supported for SOAP\. We recommend that you use either the REST API or the AWS SDKs\. 

To allow users to upload content to Amazon S3 by using their browsers, you use HTML forms\. HTML forms consist of a form declaration and form fields\. The form declaration contains high\-level information about the request\. The form fields contain detailed information about the request, as well as the policy that is used to authenticate it and ensure that it meets the conditions that you specify\.

**Note**  
The form data and boundaries \(excluding the contents of the file\) cannot exceed 20 KB\.

This section explains how to use HTML forms\.

## HTML Form Encoding<a name="HTTPPOSTFormEncoding"></a>

The form and policy must be UTF\-8 encoded\. You can apply UTF\-8 encoding to the form by specifying it in the HTML heading or as a request header\.

**Note**  
 The HTML form declaration does not accept query string authentication parameters\. 

The following is an example of UTF\-8 encoding in the HTML heading:

```
1. <html>
2.   <head>
3.     ...
4.     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
5.     ...
6.   </head>
7.   <body>
```

The following is an example of UTF\-8 encoding in a request header:

```
1. Content-Type: text/html; charset=UTF-8
```

## HTML Form Declaration<a name="HTTPPOSTFormDeclaration"></a>

The form declaration has three components: the action, the method, and the enclosure type\. If any of these values is improperly set, the request fails\.

The action specifies the URL that processes the request, which must be set to the URL of the bucket\. For example, if the name of your bucket is "johnsmith", the URL is "http://johnsmith\.s3\.amazonaws\.com/"\.

**Note**  
The key name is specified in a form field\.

The method must be POST\.

The enclosure type \(enctype\) must be specified and must be set to multipart/form\-data for both file uploads and text area uploads\. For more information, go to [RFC 1867](http://www.ietf.org/rfc/rfc1867.txt)\.

**Example**  
The following example is a form declaration for the bucket "johnsmith"\.  

```
1. <form action="http://johnsmith.s3.amazonaws.com/" method="post"
2. 
3. enctype="multipart/form-data">
```

## HTML Form Fields<a name="HTTPPOSTFormFields"></a>

The following table describes fields that can be used within an HTML form\.

**Note**  
The variable `${filename}` is automatically replaced with the name of the file provided by the user and is recognized by all form fields\. If the browser or client provides a full or partial path to the file, only the text following the last slash \(/\) or backslash \(\\\) will be used\. For example, "C:\\Program Files\\directory1\\file\.txt" will be interpreted as "file\.txt"\. If no file or file name is provided, the variable is replaced with an empty string\.


| Field Name | Description | Required | 
| --- | --- | --- | 
| AWSAccessKeyId |  The AWS Access Key ID of the owner of the bucket who grants an anonymous user access for a request that satisfies the set of constraints in the policy\. This field is required if the request includes a policy document\.  |  Conditional  | 
| acl |  An Amazon S3 access control list \(ACL\)\. If an invalid access control list is specified, an error is generated\. For more information on ACLs, see [Access Control Lists](Introduction.md#S3_ACLs)\. Type: String Default: private  Valid Values: `private \| public-read \| public-read-write \| aws-exec-read \| authenticated-read \| bucket-owner-read \| bucket-owner-full-control `   |  No  | 
| Cache\-Control, Content\-Type, Content\-Disposition, Content\-Encoding, Expires |  REST\-specific headers\. For more information, see [PUT Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html)\.  |  No  | 
| key |  The name of the uploaded key\. To use the filename provided by the user, use the $\{filename\} variable\. For example, if user Betty uploads the file lolcatz\.jpg and you specify /user/betty/$\{filename\}, the file is stored as /user/betty/lolcatz\.jpg\. For more information, see [Object Key and Metadata](UsingMetadata.md)\.  |  Yes  | 
| policy |  Security policy describing what is permitted in the request\. Requests without a security policy are considered anonymous and will succeed only on publicly writable buckets\.   |  No  | 
| success\_action\_redirect, redirect |  The URL to which the client is redirected upon successful upload\. Amazon S3 appends the bucket, key, and etag values as query string parameters to the URL\. If success\_action\_redirect is not specified, Amazon S3 returns the empty document type specified in the success\_action\_status field\. If Amazon S3 cannot interpret the URL, it ignores the field\. If the upload fails, Amazon S3 displays an error and does not redirect the user to a URL\. For more information, see [Redirection](#HTTPPOSTConstructingPolicyRedirection)\.    The redirect field name is deprecated and support for the redirect field name will be removed in the future\.    |  No  | 
| success\_action\_status |  The status code returned to the client upon successful upload if success\_action\_redirect is not specified\. Valid values are 200, 201, or 204 \(default\)\. If the value is set to 200 or 204, Amazon S3 returns an empty document with a 200 or 204 status code\. If the value is set to 201, Amazon S3 returns an XML document with a 201 status code\. For information about the content of the XML document, see [POST Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html)\. If the value is not set or if it is set to an invalid value, Amazon S3 returns an empty document with a 204 status code\.   Some versions of the Adobe Flash player do not properly handle HTTP responses with an empty body\. To support uploads through Adobe Flash, we recommend setting `success_action_status` to 201\.   |  No  | 
| signature |   The HMAC signature constructed by using the secret access key that corresponds to the provided AWSAccessKeyId\. This field is required if a policy document is included with the request\.  For more information, see [Using Auth Access](http://docs.aws.amazon.com/AmazonS3/latest/dev/s3-access-control.html) \.  |  Conditional  | 
| x\-amz\-security\-token |  A security token used by  session credentials  If the request is using Amazon DevPay then it requires two `x-amz-security-token` form fields: one for the product token and one for the user token\.  If the request is using session credentials, then it requires one `x-amz-security-token` form\. For more information, see [Temporary Security Credentials](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_temp.html) in the *IAM User Guide*\.  |  No  | 
| Other field names prefixed with x\-amz\-meta\- |  User\-specified metadata\.  Amazon S3 does not validate or use this data\. For more information, see [ PUT Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html)\.  |  No  | 
| file |  File or text content\.  The file or content must be the last field in the form\. Any fields below it are ignored\. You cannot upload more than one file at a time\.  |  Yes  | 

## Policy Construction<a name="HTTPPOSTConstructPolicy"></a>

**Topics**
+ [Expiration](#HTTPPOSTExpiration)
+ [Conditions](#PolicyConditions)
+ [Condition Matching](#ConditionMatching)
+ [Character Escaping](#HTTPPOSTEscaping)

The policy is a UTF\-8 and Base64\-encoded JSON document that specifies conditions that the request must meet and is used to authenticate the content\. Depending on how you design your policy documents, you can use them per upload, per user, for all uploads, or according to other designs that meet your needs\.

**Note**  
 Although the policy document is optional, we highly recommend it over making a bucket publicly writable\. 

The following is an example of a policy document:

```
 1. { "expiration": "2007-12-01T12:00:00.000Z",
 2. 
 3.   "conditions": [
 4. 
 5.     {"acl": "public-read" },
 6. 
 7.     {"bucket": "johnsmith" },
 8. 
 9.     ["starts-with", "$key", "user/eric/"],
10. 
11.   ]
12. 
13. }
```

The policy document contains the expiration and conditions\.

### Expiration<a name="HTTPPOSTExpiration"></a>

The expiration element specifies the expiration date of the policy in ISO 8601 UTC date format\. For example, "2007\-12\-01T12:00:00\.000Z" specifies that the policy is not valid after midnight UTC on 2007\-12\-01\. Expiration is required in a policy\.

### Conditions<a name="PolicyConditions"></a>

The conditions in the policy document validate the contents of the uploaded object\. Each form field that you specify in the form \(except AWSAccessKeyId, signature, file, policy, and field names that have an x\-ignore\- prefix\) must be included in the list of conditions\. 

**Note**  
If you have multiple fields with the same name, the values must be separated by commas\. For example, if you have two fields named "x\-amz\-meta\-tag" and the first one has a value of "Ninja" and second has a value of "Stallman", you would set the policy document to `Ninja,Stallman`\.  
 All variables within the form are expanded before the policy is validated\. Therefore, all condition matching should be performed against the expanded fields\. For example, if you set the key field to `user/betty/${filename}`, your policy might be `[ "starts-with", "$key", "user/betty/" ]`\. Do not enter `[ "starts-with", "$key", "user/betty/${filename}" ]`\. For more information, see [Condition Matching](#ConditionMatching)\. 

The following table describes policy document conditions\.


| Element Name | Description | 
| --- | --- | 
| acl |  Specifies conditions that the ACL must meet\.  Supports exact matching and `starts-with`\.   | 
| content\-length\-range |  Specifies the minimum and maximum allowable size for the uploaded content\.  Supports range matching\.  | 
| Cache\-Control, Content\-Type, Content\-Disposition, Content\-Encoding, Expires |  REST\-specific headers\.  Supports exact matching and `starts-with`\.  | 
| key |  The name of the uploaded key\. Supports exact matching and `starts-with`\.   | 
| success\_action\_redirect, redirect  |  The URL to which the client is redirected upon successful upload\. Supports exact matching and `starts-with`\.   | 
| success\_action\_status |  The status code returned to the client upon successful upload if success\_action\_redirect is not specified\. Supports exact matching\.   | 
| x\-amz\-security\-token |  Amazon DevPay security token\.  Each request that uses Amazon DevPay requires two `x-amz-security-token` form fields: one for the product token and one for the user token\. As a result, the values must be separated by commas\. For example, if the user token is `eW91dHViZQ==` and the product token is `b0hnNVNKWVJIQTA=`, you set the policy entry to: `{ "x-amz-security-token": "eW91dHViZQ==,b0hnNVNKWVJIQTA=" }`\.   | 
| Other field names prefixed with x\-amz\-meta\- |  User\-specified metadata\.  Supports exact matching and `starts-with`\.   | 

**Note**  
 If your toolkit adds additional fields \(e\.g\., Flash adds filename\), you must add them to the policy document\. If you can control this functionality, prefix `x-ignore-` to the field so Amazon S3 ignores the feature and it won't affect future versions of this feature\. 

### Condition Matching<a name="ConditionMatching"></a>

The following table describes condition matching types\. Although you must specify one condition for each form field that you specify in the form, you can create more complex matching criteria by specifying multiple conditions for a form field\.


|  Condition  |  Description  | 
| --- | --- | 
|  Exact Matches  |  Exact matches verify that fields match specific values\. This example indicates that the ACL must be set to public\-read: <pre>{"acl": "public-read" }</pre> This example is an alternate way to indicate that the ACL must be set to public\-read: <pre>[ "eq", "$acl", "public-read" ]</pre>  | 
|  Starts With  |  If the value must start with a certain value, use starts\-with\. This example indicates that the key must start with user/betty: <pre>["starts-with", "$key", "user/betty/"]</pre>  | 
|  Matching Any Content  |  To configure the policy to allow any content within a field, use starts\-with with an empty value\. This example allows any success\_action\_redirect: <pre>["starts-with", "$success_action_redirect", ""]</pre>  | 
|  Specifying Ranges  |  For fields that accept ranges, separate the upper and lower ranges with a comma\. This example allows a file size from 1 to 10 megabytes: <pre>["content-length-range", 1048579, 10485760]</pre>  | 

### Character Escaping<a name="HTTPPOSTEscaping"></a>

The following table describes characters that must be escaped within a policy document\.


|  Escape Sequence  |  Description  | 
| --- | --- | 
|  \\\\  |  Backslash  | 
|  \\$  |  Dollar sign  | 
|  \\b  |  Backspace  | 
|  \\f  |  Form feed  | 
|  \\n  |  New line  | 
|  \\r  |  Carriage return  | 
|  \\t  |  Horizontal tab  | 
|  \\v  |  Vertical tab  | 
|  \\u*xxxx*  |  All Unicode characters  | 

## Constructing a Signature<a name="HTTPPOSTConstructingPolicySignature"></a>


| Step | Description | 
| --- | --- | 
| 1 |  Encode the policy by using UTF\-8\.  | 
| 2 |  Encode those UTF\-8 bytes by using Base64\.  | 
| 3 |  Sign the policy with your secret access key by using HMAC SHA\-1\.  | 
| 4 |  Encode the SHA\-1 signature by using Base64\.  | 

For general information about authentication, see [Using Auth Access](http://docs.aws.amazon.com/AmazonS3/latest/dev/s3-access-control.html) \.

## Redirection<a name="HTTPPOSTConstructingPolicyRedirection"></a>

This section describes how to handle redirects\.

### General Redirection<a name="HTTPPOSTGeneralRedirection"></a>

On completion of the POST request, the user is redirected to the location that you specified in the `success_action_redirect` field\. If Amazon S3 cannot interpret the URL, it ignores the `success_action_redirect` field\.

If `success_action_redirect` is not specified, Amazon S3 returns the empty document type specified in the `success_action_status` field\.

If the POST request fails, Amazon S3 displays an error and does not provide a redirect\.

### Pre\-Upload Redirection<a name="HTTPPOSTPreUpload"></a>

If your bucket was created using <CreateBucketConfiguration>, your end users might require a redirect\. If this occurs, some browsers might handle the redirect incorrectly\. This is relatively rare but is most likely to occur right after a bucket is created\.