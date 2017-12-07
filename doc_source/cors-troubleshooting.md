# Troubleshooting CORS Issues<a name="cors-troubleshooting"></a>

When you are accessing buckets set with the CORS configuration, if you encounter unexpected behavior the following are some troubleshooting actions you can take:

1. Verify that the CORS configuration is set on the bucket\. 

   For instructions, go to [Editing Bucket Permissions](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/EditingBucketPermissions.html) in the *Amazon Simple Storage Service Console User Guide*\. If you have the CORS configuration set, the console displays an **Edit CORS Configuration** link in the **Permissions** section of the **Properties** bucket\.

1. Capture the complete request and response using a tool of your choice\. For each request Amazon S3 receives, there must exist one CORS rule matching the data in your request, as follows:

   1. Verify the request has the Origin header\. 

      If the header is missing, Amazon S3 does not treat the request as a cross\-origin request and does not send CORS response headers back in the response\.

   1. Verify that the Origin header in your request matches at least one of the `AllowedOrigin` elements in the specific `CORSRule`\. 

      The scheme, the host, and the port values in the Origin request header must match the `AllowedOrigin` in the `CORSRule`\. For example, if you set the `CORSRule` to allow the origin `http://www.example.com`, then both `https://www.example.com` and `http://www.example.com:80` origins in your request do not match the allowed origin in your configuration\.

   1.  Verify that the Method in your request \(or the method specified in the `Access-Control-Request-Method` in case of a preflight request\) is one of the `AllowedMethod` elements in the same `CORSRule`\. 

   1. For a preflight request, if the request includes an `Access-Control-Request-Headers` header, verify that the `CORSRule` includes the `AllowedHeader` entries for each value in the `Access-Control-Request-Headers` header\. 