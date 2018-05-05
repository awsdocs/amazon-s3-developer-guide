# Troubleshooting CORS Issues<a name="cors-troubleshooting"></a>

If you encounter unexpected behavior while accessing buckets set with the CORS configuration, try the following steps to troubleshoot:

1. Verify that the CORS configuration is set on the bucket\. 

   For instructions, see [Editing Bucket Permissions](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/EditingBucketPermissions.html) in the *Amazon Simple Storage Service Console User Guide*\. If the CORS configuration is set, the console displays an **Edit CORS Configuration** link in the **Permissions** section of the **Properties** bucket\.

1. Capture the complete request and response using a tool of your choice\. For each request Amazon S3 receives, there must be a CORS rule that matches the data in your request, as follows:

   1. Verify that the request has the Origin header\. 

      If the header is missing, Amazon S3 doesn't treat the request as a cross\-origin request, and doesn't send CORS response headers in the response\.

   1. Verify that the Origin header in your request matches at least one of the `AllowedOrigin` elements in the specified `CORSRule`\. 

      The scheme, the host, and the port values in the Origin request header must match the `AllowedOrigin` elements in the `CORSRule`\. For example, if you set the `CORSRule` to allow the origin `http://www.example.com`, then both `https://www.example.com` and `http://www.example.com:80` origins in your request don't match the allowed origin in your configuration\.

   1.  Verify that the method in your request \(or in a preflight request, the method specified in the `Access-Control-Request-Method`\) is one of the `AllowedMethod` elements in the same `CORSRule`\. 

   1. For a preflight request, if the request includes an `Access-Control-Request-Headers` header, verify that the `CORSRule` includes the `AllowedHeader` entries for each value in the `Access-Control-Request-Headers` header\. 