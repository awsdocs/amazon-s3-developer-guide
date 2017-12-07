# POST with Adobe Flash<a name="HTTPPOSTFlash"></a>

This section describes how to use `POST` with Adobe Flash\.

## Adobe Flash Player Security<a name="AdobeFlashPlayerSecurity"></a>

By default, the Adobe Flash Player security model prohibits Adobe Flash Players from making network connections to servers outside the domain that serves the SWF file\.

To override the default, you must upload a publicly readable crossdomain\.xml file to the bucket that will accept POST uploads\. The following is a sample crossdomain\.xml file\.

```
1. <?xml version="1.0"?>
2. <!DOCTYPE cross-domain-policy SYSTEM
3. "http://www.macromedia.com/xml/dtds/cross-domain-policy.dtd">
4. <cross-domain-policy>
5. <allow-access-from domain="*" secure="false" />
6. </cross-domain-policy>
```

**Note**  
For more information about the Adobe Flash security model, go to the Adobe website\.  
Adding the crossdomain\.xml file to your bucket allows any Adobe Flash Player to connect to the crossdomain\.xml file within your bucket; however, it does not grant access to the actual Amazon S3 bucket\.

## Adobe Flash Considerations<a name="HTTPPOSTAdobeFlashConsiderations"></a>

 The FileReference API in Adobe Flash adds the `Filename` form field to the POST request\. When you build Adobe Flash applications that upload to Amazon S3 by using the FileReference API action, include the following condition in your policy: 

```
1. ['starts-with', '$Filename', '']
```

Some versions of the Adobe Flash Player do not properly handle HTTP responses that have an empty body\. To configure POST to return a response that does not have an empty body, set `success_action_status` to 201\. Amazon S3 will then return an XML document with a 201 status code\. For information about the content of the XML document, see [POST Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html)\. For information about form fields, see [HTML Form Fields](HTTPPOSTForms.md#HTTPPOSTFormFields)\. 