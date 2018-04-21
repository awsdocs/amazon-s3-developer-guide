# Signing and Authenticating REST Requests<a name="RESTAuthentication"></a>

**Topics**
+ [Using Temporary Security Credentials](#UsingTemporarySecurityCredentials)
+ [The Authentication Header](#ConstructingTheAuthenticationHeader)
+ [Request Canonicalization for Signing](#RESTAuthenticationRequestCanonicalization)
+ [Constructing the CanonicalizedResource Element](#ConstructingTheCanonicalizedResourceElement)
+ [Constructing the CanonicalizedAmzHeaders Element](#RESTAuthenticationConstructingCanonicalizedAmzHeaders)
+ [Positional versus Named HTTP Header StringToSign Elements](#RESTAuthenticationStringToSign)
+ [Time Stamp Requirement](#RESTAuthenticationTimeStamp)
+ [Authentication Examples](#RESTAuthenticationExamples)
+ [REST Request Signing Problems](#RESTAuthenticationDebugging)
+ [Query String Request Authentication Alternative](#RESTAuthenticationQueryStringAuth)

**Note**  
This topic explains authenticating requests using Signature Version 2\. Amazon S3 now supports the latest Signature Version 4\. This latest signature version is supported in all regions and any new regions after January 30, 2014 will support only Signature Version 4\. For more information, go to [Authenticating Requests \(AWS Signature Version 4\)](http://docs.aws.amazon.com/AmazonS3/latest/API/sig-v4-authenticating-requests.html) in the *Amazon Simple Storage Service API Reference*\.

 Authentication is the process of proving your identity to the system\. Identity is an important factor in Amazon S3 access control decisions\. Requests are allowed or denied in part based on the identity of the requester\. For example, the right to create buckets is reserved for registered developers and \(by default\) the right to create objects in a bucket is reserved for the owner of the bucket in question\. As a developer, you'll be making requests that invoke these privileges, so you'll need to prove your identity to the system by authenticating your requests\. This section shows you how\. 

**Note**  
 The content in this section does not apply to HTTP POST\. For more information, see [Browser\-Based Uploads Using POST \(AWS Signature Version 2\)](UsingHTTPPOST.md)\. 

 The Amazon S3 REST API uses a custom HTTP scheme based on a keyed\-HMAC \(Hash Message Authentication Code\) for authentication\. To authenticate a request, you first concatenate selected elements of the request to form a string\. You then use your AWS secret access key to calculate the HMAC of that string\. Informally, we call this process "signing the request," and we call the output of the HMAC algorithm the signature, because it simulates the security properties of a real signature\. Finally, you add this signature as a parameter of the request by using the syntax described in this section\. 

 When the system receives an authenticated request, it fetches the AWS secret access key that you claim to have and uses it in the same way to compute a signature for the message it received\. It then compares the signature it calculated against the signature presented by the requester\. If the two signatures match, the system concludes that the requester must have access to the AWS secret access key and therefore acts with the authority of the principal to whom the key was issued\. If the two signatures do not match, the request is dropped and the system responds with an error message\. 

**Example Authenticated Amazon S3 REST Request**  

```
1. GET /photos/puppy.jpg HTTP/1.1
2. Host: johnsmith.s3.amazonaws.com
3. Date: Mon, 26 Mar 2007 19:37:58 +0000
4. 
5. Authorization: AWS AKIAIOSFODNN7EXAMPLE:frJIUN8DYpKDtOLCwo//yllqDzg=
```

## Using Temporary Security Credentials<a name="UsingTemporarySecurityCredentials"></a>

If you are signing your request using temporary security credentials \(see [Making Requests](MakingRequests.md)\), you must include the corresponding security token in your request by adding the `x-amz-security-token` header\. 

When you obtain temporary security credentials using the AWS Security Token Service API, the response includes temporary security credentials and a session token\. You provide the session token value in the `x-amz-security-token` header when you send requests to Amazon S3\. For information about the AWS Security Token Service API provided by IAM, go to [Action](http://docs.aws.amazon.com/STS/latest/APIReference/API_Operations.html) in the *AWS Security Token Service API Reference Guide *\.

## The Authentication Header<a name="ConstructingTheAuthenticationHeader"></a>

The Amazon S3 REST API uses the standard HTTP `Authorization` header to pass authentication information\. \(The name of the standard header is unfortunate because it carries authentication information, not authorization\.\) Under the Amazon S3 authentication scheme, the Authorization header has the following form:

```
1. Authorization: AWS AWSAccessKeyId:Signature
```

Developers are issued an AWS access key ID and AWS secret access key when they register\. For request authentication, the `AWSAccessKeyId` element identifies the access key ID that was used to compute the signature and, indirectly, the developer making the request\.

The `Signature` element is the RFC 2104 HMAC\-SHA1 of selected elements from the request, and so the `Signature` part of the Authorization header will vary from request to request\. If the request signature calculated by the system matches the `Signature` included with the request, the requester will have demonstrated possession of the AWS secret access key\. The request will then be processed under the identity, and with the authority, of the developer to whom the key was issued\.

Following is pseudogrammar that illustrates the construction of the `Authorization` request header\. \(In the example, `\n` means the Unicode code point `U+000A`, commonly called newline\)\. 

```
 1. Authorization = "AWS" + " " + AWSAccessKeyId + ":" + Signature;
 2. 
 3. Signature = Base64( HMAC-SHA1( YourSecretAccessKeyID, UTF-8-Encoding-Of( StringToSign ) ) );
 4. 
 5. StringToSign = HTTP-Verb + "\n" +
 6. 	Content-MD5 + "\n" +
 7. 	Content-Type + "\n" +
 8. 	Date + "\n" +
 9. 	CanonicalizedAmzHeaders +
10. 	CanonicalizedResource;
11. 
12. CanonicalizedResource = [ "/" + Bucket ] +
13. 	<HTTP-Request-URI, from the protocol name up to the query string> +
14. 	[ subresource, if present. For example "?acl", "?location", "?logging", or "?torrent"];
15. 
16. CanonicalizedAmzHeaders = <described below>
```

 HMAC\-SHA1 is an algorithm defined by [ RFC 2104 \- Keyed\-Hashing for Message Authentication ](http://www.ietf.org/rfc/rfc2104.txt)\. The algorithm takes as input two byte\-strings, a key and a message\. For Amazon S3 request authentication, use your AWS secret access key \(`YourSecretAccessKeyID`\) as the key, and the UTF\-8 encoding of the `StringToSign` as the message\. The output of HMAC\-SHA1 is also a byte string, called the digest\. The `Signature` request parameter is constructed by Base64 encoding this digest\. 

## Request Canonicalization for Signing<a name="RESTAuthenticationRequestCanonicalization"></a>

 Recall that when the system receives an authenticated request, it compares the computed request signature with the signature provided in the request in `StringToSign`\. For that reason, you must compute the signature by using the same method used by Amazon S3\. We call the process of putting a request in an agreed\-upon form for signing *canonicalization*\. 

## Constructing the CanonicalizedResource Element<a name="ConstructingTheCanonicalizedResourceElement"></a>

 `CanonicalizedResource` represents the Amazon S3 resource targeted by the request\. Construct it for a REST request as follows: 


**Launch Process**  

|  |  | 
| --- |--- |
|  1  |  Start with an empty string \(`""`\)\.  | 
|  2  |  If the request specifies a bucket using the HTTP Host header \(virtual hosted\-style\), append the bucket name preceded by a `"/"` \(e\.g\., "/bucketname"\)\. For path\-style requests and requests that don't address a bucket, do nothing\. For more information about virtual hosted\-style requests, see [Virtual Hosting of Buckets](VirtualHosting.md)\.  For a virtual hosted\-style request "https://johnsmith\.s3\.amazonaws\.com/photos/puppy\.jpg", the `CanonicalizedResource` is "/johnsmith"\.  For the path\-style request, "https://s3\.amazonaws\.com/johnsmith/photos/puppy\.jpg", the `CanonicalizedResource` is ""\.  | 
|  3  |  Append the path part of the un\-decoded HTTP Request\-URI, up\-to but not including the query string\. For a virtual hosted\-style request "https://johnsmith\.s3\.amazonaws\.com/photos/puppy\.jpg", the `CanonicalizedResource` is "/johnsmith/photos/puppy\.jpg"\. For a path\-style request, "https://s3\.amazonaws\.com/johnsmith/photos/puppy\.jpg", the `CanonicalizedResource` is "/johnsmith/photos/puppy\.jpg"\. At this point, the `CanonicalizedResource` is the same for both the virtual hosted\-style and path\-style request\. For a request that does not address a bucket, such as [GET Service](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTServiceGET.html), append "/"\.  | 
|  4  |  If the request addresses a subresource, such as `?versioning`, `?location`, `?acl`, `?torrent`, `?lifecycle`, or `?versionid`, append the subresource, its value if it has one, and the question mark\. Note that in case of multiple subresources, subresources must be lexicographically sorted by subresource name and separated by '&', e\.g\., ?acl&versionId=*value*\.  The subresources that must be included when constructing the CanonicalizedResource Element are acl, lifecycle, location, logging, notification, partNumber, policy, requestPayment, torrent, uploadId, uploads, versionId, versioning, versions, and website\.  If the request specifies query string parameters overriding the response header values \(see [Get Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html)\), append the query string parameters and their values\. When signing, you do not encode these values; however, when making the request, you must encode these parameter values\. The query string parameters in a GET request include `response-content-type`, `response-content-language`, `response-expires`, `response-cache-control`, `response-content-disposition`, and `response-content-encoding`\. The `delete` query string parameter must be included when you create the CanonicalizedResource for a multi\-object Delete request\.  | 

Elements of the CanonicalizedResource that come from the HTTP Request\-URI should be signed literally as they appear in the HTTP request, including URL\-Encoding meta characters\. 

The `CanonicalizedResource` might be different than the HTTP Request\-URI\. In particular, if your request uses the HTTP `Host` header to specify a bucket, the bucket does not appear in the HTTP Request\-URI\. However, the `CanonicalizedResource` continues to include the bucket\. Query string parameters might also appear in the Request\-URI but are not included in `CanonicalizedResource`\. For more information, see [Virtual Hosting of Buckets](VirtualHosting.md)\. 

## Constructing the CanonicalizedAmzHeaders Element<a name="RESTAuthenticationConstructingCanonicalizedAmzHeaders"></a>

To construct the CanonicalizedAmzHeaders part of `StringToSign`, select all HTTP request headers that start with 'x\-amz\-' \(using a case\-insensitive comparison\), and use the following process\. 


**CanonicalizedAmzHeaders Process**  

|  |  | 
| --- |--- |
| 1 | Convert each HTTP header name to lowercase\. For example, 'X\-Amz\-Date' becomes 'x\-amz\-date'\. | 
| 2 | Sort the collection of headers lexicographically by header name\. | 
| 3 | Combine header fields with the same name into one "header\-name:comma\-separated\-value\-list" pair as prescribed by RFC 2616, section 4\.2, without any whitespace between values\. For example, the two metadata headers 'x\-amz\-meta\-username: fred' and 'x\-amz\-meta\-username: barney' would be combined into the single header 'x\-amz\-meta\-username: fred,barney'\. | 
| 4 | "Unfold" long headers that span multiple lines \(as allowed by RFC 2616, section 4\.2\) by replacing the folding whitespace \(including new\-line\) by a single space\. | 
| 5 | Trim any whitespace around the colon in the header\. For example, the header 'x\-amz\-meta\-username: fred,barney' would become 'x\-amz\-meta\-username:fred,barney'  | 
| 6 |  Finally, append a newline character \(U\+000A\) to each canonicalized header in the resulting list\. Construct the CanonicalizedResource element by concatenating all headers in this list into a single string\. | 

## Positional versus Named HTTP Header StringToSign Elements<a name="RESTAuthenticationStringToSign"></a>

 The first few header elements of `StringToSign` \(Content\-Type, Date, and Content\-MD5\) are positional in nature\. `StringToSign` does not include the names of these headers, only their values from the request\. In contrast, the '`x-amz-`' elements are named\. Both the header names and the header values appear in `StringToSign`\. 

 If a positional header called for in the definition of `StringToSign` is not present in your request \(for example, `Content-Type` or `Content-MD5` are optional for PUT requests and meaningless for GET requests\), substitute the empty string \(""\) for that position\. 

## Time Stamp Requirement<a name="RESTAuthenticationTimeStamp"></a>

A valid time stamp \(using either the HTTP `Date` header or an `x-amz-date` alternative\) is mandatory for authenticated requests\. Furthermore, the client timestamp included with an authenticated request must be within 15 minutes of the Amazon S3 system time when the request is received\. If not, the request will fail with the `RequestTimeTooSkewed` error code\. The intention of these restrictions is to limit the possibility that intercepted requests could be replayed by an adversary\. For stronger protection against eavesdropping, use the HTTPS transport for authenticated requests\. 

**Note**  
The validation constraint on request date applies only to authenticated requests that do not use query string authentication\. For more information, see [Query String Request Authentication Alternative](#RESTAuthenticationQueryStringAuth)\.

Some HTTP client libraries do not expose the ability to set the `Date` header for a request\. If you have trouble including the value of the 'Date' header in the canonicalized headers, you can set the timestamp for the request by using an '`x-amz-date`' header instead\. The value of the `x-amz-date` header must be in one of the RFC 2616 formats \([http://www\.ietf\.org/rfc/rfc2616\.txt](http://www.ietf.org/rfc/rfc2616.txt)\)\. When an `x-amz-date` header is present in a request, the system will ignore any `Date` header when computing the request signature\. Therefore, if you include the `x-amz-date` header, use the empty string for the `Date` when constructing the `StringToSign`\. See the next section for an example\. 

## Authentication Examples<a name="RESTAuthenticationExamples"></a>

 The examples in this section use the \(non\-working\) credentials in the following table\. 


| Parameter | Value | 
| --- | --- | 
| AWSAccessKeyId | AKIAIOSFODNN7EXAMPLE | 
| AWSSecretAccessKey | wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY | 

In the example `StringToSign`s, formatting is not significant, and `\n` means the Unicode code point `U+000A`, commonly called newline\. Also, the examples use "\+0000" to designate the time zone\. You can use "GMT" to designate timezone instead, but the signatures shown in the examples will be different\.

### Object GET<a name="RESTAuthenticationExamples-1"></a>

This example gets an object from the johnsmith bucket\.


| Request | StringToSign | 
| --- | --- | 
|  <pre>GET /photos/puppy.jpg HTTP/1.1<br />Host: johnsmith.s3.amazonaws.com<br />Date: Tue, 27 Mar 2007 19:36:42 +0000<br /><br />Authorization: AWS AKIAIOSFODNN7EXAMPLE:<br />bWq2s1WEIj+Ydj0vQ697zp+IXMU=</pre>  |  <pre>GET\n<br />\n<br />\n<br />Tue, 27 Mar 2007 19:36:42 +0000\n<br />/johnsmith/photos/puppy.jpg</pre>  | 

 Note that the CanonicalizedResource includes the bucket name, but the HTTP Request\-URI does not\. \(The bucket is specified by the Host header\.\) 

### Object PUT<a name="RESTAuthenticationExamples-2"></a>

This example puts an object into the johnsmith bucket\.


| Request | StringToSign | 
| --- | --- | 
|  <pre>PUT /photos/puppy.jpg HTTP/1.1<br />Content-Type: image/jpeg<br />Content-Length: 94328<br />Host: johnsmith.s3.amazonaws.com<br />Date: Tue, 27 Mar 2007 21:15:45 +0000<br /><br />Authorization: AWS AKIAIOSFODNN7EXAMPLE:<br />MyyxeRY7whkBe+bq8fHCL/2kKUg=<br /></pre>  |  <pre>PUT\n<br />\n<br />image/jpeg\n<br />Tue, 27 Mar 2007 21:15:45 +0000\n<br />/johnsmith/photos/puppy.jpg</pre>  | 

 Note the Content\-Type header in the request and in the StringToSign\. Also note that the Content\-MD5 is left blank in the StringToSign, because it is not present in the request\. 

### List<a name="RESTAuthenticationExamples-3"></a>

This example lists the content of the johnsmith bucket\.


| Request | StringToSign | 
| --- | --- | 
|  <pre>GET /?prefix=photos&max-keys=50&marker=puppy HTTP/1.1<br />User-Agent: Mozilla/5.0<br />Host: johnsmith.s3.amazonaws.com<br />Date: Tue, 27 Mar 2007 19:42:41 +0000<br /><br />Authorization: AWS AKIAIOSFODNN7EXAMPLE:<br />htDYFYduRNen8P9ZfE/s9SuKy0U=</pre>  |  <pre>GET\n<br />\n<br />\n<br />Tue, 27 Mar 2007 19:42:41 +0000\n<br />/johnsmith/</pre>  | 

 Note the trailing slash on the CanonicalizedResource and the absence of query string parameters\. 

### Fetch<a name="RESTAuthenticationExamples-4"></a>

This example fetches the access control policy subresource for the 'johnsmith' bucket\.


| Request | StringToSign | 
| --- | --- | 
|  <pre>GET /?acl HTTP/1.1<br />Host: johnsmith.s3.amazonaws.com<br />Date: Tue, 27 Mar 2007 19:44:46 +0000<br /><br />Authorization: AWS AKIAIOSFODNN7EXAMPLE:<br />c2WLPFtWHVgbEmeEG93a4cG37dM=</pre>  |  <pre>GET\n<br />\n<br />\n<br />Tue, 27 Mar 2007 19:44:46 +0000\n<br />/johnsmith/?acl</pre>  | 

 Notice how the subresource query string parameter is included in the CanonicalizedResource\. 

### Delete<a name="RESTAuthenticationExamples-5"></a>

This example deletes an object from the 'johnsmith' bucket using the path\-style and Date alternative\.


| Request | StringToSign | 
| --- | --- | 
|  <pre>DELETE /johnsmith/photos/puppy.jpg HTTP/1.1<br />User-Agent: dotnet<br />Host: s3.amazonaws.com<br />Date: Tue, 27 Mar 2007 21:20:27 +0000<br /><br />x-amz-date: Tue, 27 Mar 2007 21:20:26 +0000<br />Authorization: AWS AKIAIOSFODNN7EXAMPLE:lx3byBScXR6KzyMaifNkardMwNk=</pre>  |  <pre>DELETE\n<br />\n<br />\n<br />Tue, 27 Mar 2007 21:20:26 +0000\n<br />/johnsmith/photos/puppy.jpg</pre>  | 

 Note how we used the alternate 'x\-amz\-date' method of specifying the date \(because our client library prevented us from setting the date, say\)\. In this case, the `x-amz-date` takes precedence over the `Date` header\. Therefore, date entry in the signature must contain the value of the `x-amz-date` header\. 

### Upload<a name="RESTAuthenticationExamples-6"></a>

This example uploads an object to a CNAME style virtual hosted bucket with metadata\.


| Request | StringToSign | 
| --- | --- | 
|  <pre>PUT /db-backup.dat.gz HTTP/1.1<br />User-Agent: curl/7.15.5<br />Host: static.johnsmith.net:8080<br />Date: Tue, 27 Mar 2007 21:06:08 +0000<br /><br />x-amz-acl: public-read<br />content-type: application/x-download<br />Content-MD5: 4gJE4saaMU4BqNR0kLY+lw==<br />X-Amz-Meta-ReviewedBy: joe@johnsmith.net<br />X-Amz-Meta-ReviewedBy: jane@johnsmith.net<br />X-Amz-Meta-FileChecksum: 0x02661779<br />X-Amz-Meta-ChecksumAlgorithm: crc32<br />Content-Disposition: attachment; filename=database.dat<br />Content-Encoding: gzip<br />Content-Length: 5913339<br /><br />Authorization: AWS AKIAIOSFODNN7EXAMPLE:<br />ilyl83RwaSoYIEdixDQcA4OnAnc=</pre>  |  <pre>PUT\n<br />4gJE4saaMU4BqNR0kLY+lw==\n<br />application/x-download\n<br />Tue, 27 Mar 2007 21:06:08 +0000\n<br /><br />x-amz-acl:public-read\n<br />x-amz-meta-checksumalgorithm:crc32\n<br />x-amz-meta-filechecksum:0x02661779\n<br />x-amz-meta-reviewedby:<br />joe@johnsmith.net,jane@johnsmith.net\n<br />/static.johnsmith.net/db-backup.dat.gz</pre>  | 

 Notice how the 'x\-amz\-' headers are sorted, trimmed of whitespace, and converted to lowercase\. Note also that multiple headers with the same name have been joined using commas to separate values\. 

 Note how only the `Content-Type` and `Content-MD5` HTTP entity headers appear in the `StringToSign`\. The other `Content-*` entity headers do not\. 

 Again, note that the `CanonicalizedResource` includes the bucket name, but the HTTP Request\-URI does not\. \(The bucket is specified by the Host header\.\) 

### List All My Buckets<a name="RESTAuthenticationExamples-7"></a>


| Request | StringToSign | 
| --- | --- | 
|  <pre>GET / HTTP/1.1<br />Host: s3.amazonaws.com<br />Date: Wed, 28 Mar 2007 01:29:59 +0000<br /><br />Authorization: AWS AKIAIOSFODNN7EXAMPLE:qGdzdERIC03wnaRNKh6OqZehG9s=</pre>  |  <pre>GET\n<br />\n<br />\n<br />Wed, 28 Mar 2007 01:29:59 +0000\n<br />/</pre>  | 

### Unicode Keys<a name="RESTAuthenticationExamples-8"></a>


| Request | StringToSign | 
| --- | --- | 
|  <pre>GET /dictionary/fran%C3%A7ais/pr%c3%a9f%c3%a8re HTTP/1.1<br />Host: s3.amazonaws.com<br />Date: Wed, 28 Mar 2007 01:49:49 +0000<br />Authorization: AWS AKIAIOSFODNN7EXAMPLE:DNEZGsoieTZ92F3bUfSPQcbGmlM=</pre>  |  <pre>GET\n<br />\n<br />\n<br />Wed, 28 Mar 2007 01:49:49 +0000\n<br />/dictionary/fran%C3%A7ais/pr%c3%a9f%c3%a8re</pre>  | 

**Note**  
The elements in `StringToSign` that were derived from the Request\-URI are taken literally, including URL\-Encoding and capitalization\. 

## REST Request Signing Problems<a name="RESTAuthenticationDebugging"></a>

 When REST request authentication fails, the system responds to the request with an XML error document\. The information contained in this error document is meant to help developers diagnose the problem\. In particular, the `StringToSign` element of the `SignatureDoesNotMatch` error document tells you exactly what request canonicalization the system is using\. 

Some toolkits silently insert headers that you do not know about beforehand, such as adding the header `Content-Type` during a PUT\. In most of these cases, the value of the inserted header remains constant, allowing you to discover the missing headers by using tools such as Ethereal or tcpmon\. 

## Query String Request Authentication Alternative<a name="RESTAuthenticationQueryStringAuth"></a>

You can authenticate certain types of requests by passing the required information as query\-string parameters instead of using the `Authorization` HTTP header\. This is useful for enabling direct third\-party browser access to your private Amazon S3 data without proxying the request\. The idea is to construct a "pre\-signed" request and encode it as a URL that an end\-user's browser can retrieve\. Additionally, you can limit a pre\-signed request by specifying an expiration time\. 

**Note**  
For examples of using the AWS SDKs to generating pre\-signed URLs, see [Share an Object with Others](ShareObjectPreSignedURL.md)\. 

### Creating a Signature<a name="CreatingASignature"></a>

Following is an example query string authenticated Amazon S3 REST request\.

```
1. GET /photos/puppy.jpg
2. ?AWSAccessKeyId=AKIAIOSFODNN7EXAMPLE&Expires=1141889120&Signature=vjbyPxybdZaNmGa%2ByT272YEAiv4%3D HTTP/1.1
3. Host: johnsmith.s3.amazonaws.com
4. Date: Mon, 26 Mar 2007 19:37:58 +0000
```

The query string request authentication method doesn't require any special HTTP headers\. Instead, the required authentication elements are specified as query string parameters: 


| Query String Parameter Name | Example Value | Description | 
| --- | --- | --- | 
| AWSAccessKeyId | AKIAIOSFODNN7EXAMPLE | Your AWS access key ID\. Specifies the AWS secret access key used to sign the request and, indirectly, the identity of the developer making the request\. | 
| Expires | 1141889120 | The time when the signature expires, specified as the number of seconds since the epoch \(00:00:00 UTC on January 1, 1970\)\. A request received after this time \(according to the server\) will be rejected\.  | 
| Signature | vjbyPxybdZaNmGa%2ByT272YEAiv4%3D | The URL encoding of the Base64 encoding of the HMAC\-SHA1 of StringToSign\. | 

The query string request authentication method differs slightly from the ordinary method but only in the format of the `Signature` request parameter and the `StringToSign` element\. Following is pseudo\-grammar that illustrates the query string request authentication method\. 

```
1. Signature = URL-Encode( Base64( HMAC-SHA1( YourSecretAccessKeyID, UTF-8-Encoding-Of( StringToSign ) ) ) );
2. 
3. StringToSign = HTTP-VERB + "\n" +
4.     Content-MD5 + "\n" +
5.     Content-Type + "\n" +
6.     Expires + "\n" +
7.     CanonicalizedAmzHeaders +
8.     CanonicalizedResource;
```

`YourSecretAccessKeyID` is the AWS secret access key ID that Amazon assigns to you when you sign up to be an Amazon Web Service developer\. Notice how the `Signature` is URL\-Encoded to make it suitable for placement in the query string\. Note also that in `StringToSign`, the HTTP `Date` positional element has been replaced with `Expires`\. The `CanonicalizedAmzHeaders` and `CanonicalizedResource` are the same\. 

**Note**  
In the query string authentication method, you do not use the `Date` or the `x-amz-date request` header when calculating the string to sign\.

#### Query String Request Authentication<a name="query-str-auth-ex"></a>


| Request | StringToSign | 
| --- | --- | 
|  <pre>GET /photos/puppy.jpg?AWSAccessKeyId=AKIAIOSFODNN7EXAMPLE&<br />    Signature=NpgCjnDzrM%2BWFzoENXmpNDUsSn8%3D&<br />    Expires=1175139620 HTTP/1.1<br /><br />Host: johnsmith.s3.amazonaws.com</pre>  |  <pre>GET\n<br />\n<br />\n<br />1175139620\n<br /><br />/johnsmith/photos/puppy.jpg</pre>  | 

We assume that when a browser makes the GET request, it won't provide a Content\-MD5 or a Content\-Type header, nor will it set any x\-amz\- headers, so those parts of the `StringToSign` are left blank\. 

#### Using Base64 Encoding<a name="S3_Authentication_Base64"></a>

HMAC request signatures must be Base64 encoded\. Base64 encoding converts the signature into a simple ASCII string that can be attached to the request\. Characters that could appear in the signature string like plus \(\+\), forward slash \(/\), and equals \(=\) must be encoded if used in a URI\. For example, if the authentication code includes a plus \(\+\) sign, encode it as %2B in the request\. Encode a forward slash as %2F and equals as %3D\.

For examples of Base64 encoding, refer to the Amazon S3 [Authentication Examples](#RESTAuthenticationExamples)\.