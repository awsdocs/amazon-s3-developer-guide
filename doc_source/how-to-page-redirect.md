# \(Optional\) configuring a webpage redirect<a name="how-to-page-redirect"></a>

If your Amazon S3 bucket is configured for website hosting, you can configure a webpage redirect\. You have the following options for configuring a redirect\.

**Topics**
+ [Setting a page redirect in the Amazon S3 console](#page-redirect-using-console)
+ [Setting a page redirect from the REST API](#page-redirect-using-rest-api)
+ [Redirecting requests for a bucket's website endpoint to another host](#redirect-endpoint-host)
+ [Configuring advanced conditional redirects](#advanced-conditional-redirects)

## Setting a page redirect in the Amazon S3 console<a name="page-redirect-using-console"></a>

You can redirect requests for an object to another object or URL by setting the website redirect location in the metadata of the object\. You set the redirect by adding the `x-amz-website-redirect-location` property to the object metadata\. On the Amazon S3 console, you set the **Website Redirect Location** in the metadata of the object\. If you use the [Amazon S3 API](#page-redirect-using-rest-api), you set `x-amz-website-redirect-location`\. The website then interprets the object as a 301 redirect\. 

To redirect a request to another object, you set the redirect location to the key of the target object\. To redirect a request to an external URL, you set the redirect location to the URL that you want\. For more information about object metadata, see [System\-defined object metadata](UsingMetadata.md#SysMetadata)\.

When you set a page redirect, you can either keep or delete the source object content\. For example, if you have a `page1.html` object in your bucket, you can redirect any requests for this page to another object, `page2.html`\. You have two options:
+ Keep the content of the `page1.html` object and redirect page requests\.
+ Delete the content of `page1.html` and upload a zero\-byte object named `page1.html` to replace the existing object and redirect page requests\. 

**To redirect requests for an object**

1. Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. Choose the name of the bucket that you have configured as a static website \(for example, `example.com`\)\.

1. On the **Overview** tab for your bucket, choose the object that you want to create a redirect for\.

1. Choose **Properties**\.

1. Choose **Metadata**\.

1. Choose **\+ Add Metadata**\.

1. In **Key**, choose **Website\-Redirect\-Location**\.

1. In **Value**, enter the key name of the object that you want to redirect to, for example, `/page2.html`\.

   For another object in the same bucket, the `/` prefix in the value is required\. You can also set the value to an external URL, for example, `http://www.example.com`\.

1. Choose **Save**\.

## Setting a page redirect from the REST API<a name="page-redirect-using-rest-api"></a>

The following Amazon S3 API actions support the `x-amz-website-redirect-location` header in the request\. Amazon S3 stores the header value in the object metadata as `x-amz-website-redirect-location`\. 
+ [PUT Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html)
+ [Initiate Multipart Upload](https://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html)
+ [POST Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html)
+ [PUT Object \- Copy](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html)

A bucket configured for website hosting has both the website endpoint and the REST endpoint\. A request for a page that is configured as a 301 redirect has the following possible outcomes, depending on the endpoint of the request:
+ **Region\-specific website endpoint – **Amazon S3 redirects the page request according to the value of the `x-amz-website-redirect-location` property\. 
+ **REST endpoint – **Amazon S3 doesn't redirect the page request\. It returns the requested object\.

For more information about the endpoints, see [Key differences between a website endpoint and a REST API endpoint](WebsiteEndpoints.md#WebsiteRestEndpointDiff)\.

When setting a page redirect, you can either keep or delete the object content\. For example, suppose that you have a `page1.html` object in your bucket\.
+ To keep the content of `page1.html` and only redirect page requests, you can submit a [PUT Object \- Copy](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html) request to create a new `page1.html` object that uses the existing `page1.html` object as the source\. In your request, you set the `x-amz-website-redirect-location` header\. When the request is complete, you have the original page with its content unchanged, but Amazon S3 redirects any requests for the page to the redirect location that you specify\.
+ To delete the content of the `page1.html` object and redirect requests for the page, you can send a PUT Object request to upload a zero\-byte object that has the same object key: `page1.html`\. In the PUT request, you set `x-amz-website-redirect-location` for `page1.html` to the new object\. When the request is complete, `page1.html` has no content, and requests are redirected to the location that is specified by `x-amz-website-redirect-location`\.

When you retrieve the object using the [GET Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html) action, along with other object metadata, Amazon S3 returns the `x-amz-website-redirect-location` header in the response\.

## Redirecting requests for a bucket's website endpoint to another host<a name="redirect-endpoint-host"></a>

You can redirect all requests for a website endpoint for a bucket to another host\. If you redirect all requests, any request made to the website endpoint is redirected to the specified host name\. 

For example, if your root domain is `example.com`, and you want to serve requests for both `http://example.com` and `http://www.example.com`, you can create two buckets named `example.com` and `www.example.com`\. Then, maintain the content in the `example.com` bucket, and configure the other `www.example.com` bucket to redirect all requests to the `example.com` bucket\. For more information, see [Configuring a Static Website Using a Custom Domain Name](https://docs.aws.amazon.com/AmazonS3/latest/dev/website-hosting-custom-domain-walkthrough.html)\.

**To redirect requests for a bucket website endpoint**

1. Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. Choose the name of the bucket that you have configured as a static website \(for example, `example.com`\)\.

1. Choose **Properties**\.

1. Choose **Static website hosting**\.

1. Choose **Redirect requests**\. 

1. In the **Target bucket or domain** box, enter the bucket or domain that you want to redirect to\.

   For example, if you are redirecting to a root domain address, you would enter **example\.com**\.

1. In the **Protocol** box, enter the protocol for the redirected requests \(**http** or **https**\)\.

   If you do not specify a protocol, the protocol of the original request is used\.

1. Choose **Save**\.

## Configuring advanced conditional redirects<a name="advanced-conditional-redirects"></a>

Using advanced redirection rules, you can route requests conditionally according to specific object key names, prefixes in the request, or response codes\. For example, suppose that you delete or rename an object in your bucket\. You can add a routing rule that redirects the request to another object\. If you want to make a folder unavailable, you can add a routing rule to redirect the request to another webpage\. You can also add a routing rule to handle error conditions by routing requests that return the error to another domain when the error is processed\.

When configuring a bucket for website hosting, you have the option of specifying advanced redirection rules\. Amazon S3 has a limitation of 50 routing rules per website configuration\. If you require more than 50 routing rules, you can use object redirect\. For more information, see [\(Optional\) configuring a webpage redirect](#how-to-page-redirect)\.

**To configure redirection rules for a static website**

To add redirection rules for a bucket that already has static website hosting enabled, follow these steps\.

1. Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. In the **Buckets** list, choose the name of a bucket that you have configured as a static website\.

1. Choose **Properties**\.

1. Choose **Static website hosting**\.

1. In **Redirection rules**, enter your redirection rules\. 

   You describe the rules using XML\. For general syntax and examples for specifying redirection rules, see [Syntax for specifying routing rules](#configure-bucket-as-website-routing-rule-syntax)\. Amazon S3 has a limitation of 50 routing rules per website configuration\. If you require more than 50 routing rules, you can use object redirect\. For more information, see [Setting a page redirect in the Amazon S3 console](#page-redirect-using-console)\.

1. Choose **Save**\.

### Syntax for specifying routing rules<a name="configure-bucket-as-website-routing-rule-syntax"></a>

The following is general syntax for defining the routing rules in a website configuration\.

```
<RoutingRules> =
    <RoutingRules>
         <RoutingRule>...</RoutingRule>
         [<RoutingRule>...</RoutingRule>   
         ...]
    </RoutingRules>

<RoutingRule> =
   <RoutingRule>
      [ <Condition>...</Condition> ]
      <Redirect>...</Redirect>
   </RoutingRule>

<Condition> =
   <Condition> 
      [ <KeyPrefixEquals>...</KeyPrefixEquals> ]
      [ <HttpErrorCodeReturnedEquals>...</HttpErrorCodeReturnedEquals> ]
   </Condition>
    Note: <Condition> must have at least one child element.

<Redirect> =
   <Redirect> 
      [ <HostName>...</HostName> ]
      [ <Protocol>...</Protocol> ]
      [ <ReplaceKeyPrefixWith>...</ReplaceKeyPrefixWith>  ]
      [ <ReplaceKeyWith>...</ReplaceKeyWith> ]
      [ <HttpRedirectCode>...</HttpRedirectCode> ]
   </Redirect>
    Note: <Redirect> must have at least one child element. 
           Also, you can have either ReplaceKeyPrefix with or ReplaceKeyWith, 
           but not both.
```

The following table describes the elements in the routing rule\.


|  Name  |  Description  | 
| --- | --- | 
| RoutingRules |  Container for a collection of RoutingRule elements\.  | 
| RoutingRule |  A rule that identifies a condition and the redirect that is applied when the condition is met\.  Condition: [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/how-to-page-redirect.html)  | 
| Condition |  Container for describing a condition that must be met for the specified redirect to be applied\. If the routing rule does not include a condition, the rule is applied to all requests\.  | 
| KeyPrefixEquals |  The prefix of the object key name from which requests are redirected\.  `KeyPrefixEquals` is required if `HttpErrorCodeReturnedEquals` is not specified\. If both `KeyPrefixEquals` and `HttpErrorCodeReturnedEquals` are specified, both must be true for the condition to be met\.  | 
| HttpErrorCodeReturnedEquals |  The HTTP error code that must match for the redirect to apply\. If an error occurs, and if the error code meets this value, then the specified redirect applies\. `HttpErrorCodeReturnedEquals` is required if `KeyPrefixEquals` is not specified\. If both `KeyPrefixEquals` and `HttpErrorCodeReturnedEquals` are specified, both must be true for the condition to be met\.  | 
| Redirect |  Container element that provides instructions for redirecting the request\. You can redirect requests to another host or another page, or you can specify another protocol to use\. A `RoutingRule` must have a `Redirect` element\. A `Redirect` element must contain at least one of the following sibling elements: `Protocol`, `HostName`, `ReplaceKeyPrefixWith`, `ReplaceKeyWith`, or `HttpRedirectCode`\.  | 
| Protocol |  The protocol, `http` or `https`, to be used in the `Location` header that is returned in the response\.  If one of its siblings is supplied, `Protocol` is not required\.  | 
| HostName |  The hostname to be used in the `Location` header that is returned in the response\. If one of its siblings is supplied, `HostName` is not required\.  | 
| ReplaceKeyPrefixWith |  The prefix of the object key name that replaces the value of `KeyPrefixEquals` in the redirect request\.  If one of its siblings is supplied, `ReplaceKeyPrefixWith` is not required\. It can be supplied only if `ReplaceKeyWith` is not supplied\.  | 
| ReplaceKeyWith |  The object key to be used in the `Location` header that is returned in the response\.  If one of its siblings is supplied, `ReplaceKeyWith` is not required\. It can be supplied only if `ReplaceKeyPrefixWith` is not supplied\.  | 
| HttpRedirectCode |  The HTTP redirect code to be used in the `Location` header that is returned in the response\. If one of its siblings is supplied, `HttpRedirectCode` is not required\.  | 

#### Examples<a name="redirect-rule-examples"></a>

The following examples explain common redirection tasks:

**Example 1: Redirect after renaming a key prefix**  
Suppose that your bucket contains the following objects:  
+ index\.html
+ docs/article1\.html
+ docs/article2\.html
You decide to rename the folder from `docs/` to `documents/`\. After you make this change, you need to redirect requests for prefix `docs/` to `documents/`\. For example, request for `docs/article1.html` will be redirected to `documents/article1.html`\.  
In this case, you add the following routing rule to the website configuration\.  

```
  <RoutingRules>
    <RoutingRule>
    <Condition>
      <KeyPrefixEquals>docs/</KeyPrefixEquals>
    </Condition>
    <Redirect>
      <ReplaceKeyPrefixWith>documents/</ReplaceKeyPrefixWith>
    </Redirect>
    </RoutingRule>
  </RoutingRules>
```

**Example 2: Redirect requests for a deleted folder to a page**  
Suppose that you delete the `images/` folder \(that is, you delete all objects with the key prefix `images/`\)\. You can add a routing rule that redirects requests for any object with the key prefix `images/` to a page named `folderdeleted.html`\.  

```
  <RoutingRules>
    <RoutingRule>
    <Condition>
       <KeyPrefixEquals>images/</KeyPrefixEquals>
    </Condition>
    <Redirect>
      <ReplaceKeyWith>folderdeleted.html</ReplaceKeyWith>
    </Redirect>
    </RoutingRule>
  </RoutingRules>
```

**Example 3: Redirect for an HTTP error**  
Suppose that when a requested object is not found, you want to redirect requests to an Amazon Elastic Compute Cloud \(Amazon EC2\) instance\. Add a redirection rule so that when an HTTP status code 404 \(Not Found\) is returned, the site visitor is redirected to an Amazon EC2 instance that handles the request\.   
The following example also inserts the object key prefix `report-404/` in the redirect\. For example, if you request a page `ExamplePage.html` and it results in an HTTP 404 error, the request is redirected to a page `report-404/ExamplePage.html` on the specified Amazon EC2 instance\. If there is no routing rule and the HTTP error 404 occurs, the error document that is specified in the configuration is returned\.  

```
  <RoutingRules>
    <RoutingRule>
    <Condition>
      <HttpErrorCodeReturnedEquals>404</HttpErrorCodeReturnedEquals >
    </Condition>
    <Redirect>
      <HostName>ec2-11-22-333-44.compute-1.amazonaws.com</HostName>
      <ReplaceKeyPrefixWith>report-404/</ReplaceKeyPrefixWith>
    </Redirect>
    </RoutingRule>
  </RoutingRules>
```