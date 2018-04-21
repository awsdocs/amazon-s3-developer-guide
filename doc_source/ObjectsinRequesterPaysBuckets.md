# Downloading Objects in Requester Pays Buckets<a name="ObjectsinRequesterPaysBuckets"></a>

Because requesters are charged for downloading data from Requester Pays buckets, the requests must contain a special parameter, `x-amz-request-payer`, which confirms that the requester knows he or she will be charged for the download\. To access objects in Requester Pays buckets, requests must include one of the following\.
+ For GET, HEAD, and POST requests, include `x-amz-request-payer : requester` in the header
+ For signed URLs, include `x-amz-request-payer=requester` in the request

If the request succeeds and the requester is charged, the response includes the header `x-amz-request-charged:requester`\. If `x-amz-request-payer` is not in the request, Amazon S3 returns a 403 error and charges the bucket owner for the request\.

**Note**  
Bucket owners do not need to add `x-amz-request-payer` to their requests\.  
Ensure that you have included `x-amz-request-payer` and its value in your signature calculation\. For more information, see [Constructing the CanonicalizedAmzHeaders Element](RESTAuthentication.md#RESTAuthenticationConstructingCanonicalizedAmzHeaders)\.

**To download objects from a Requester Pays bucket**
+  Use a `GET` request to download an object from a Requester Pays bucket, as shown in the following request\.

  ```
  1. GET / [destinationObject] HTTP/1.1
  2. Host: [BucketName].s3.amazonaws.com
  3. x-amz-request-payer : requester
  4. Date: Wed, 01 Mar 2009 12:00:00 GMT
  5. Authorization: AWS [Signature]
  ```

If the GET request succeeds and the requester is charged, the response includes `x-amz-request-charged:requester`\.

Amazon S3 can return an `Access Denied` error for requests that try to get objects from a Requester Pays bucket\. For more information, see [Error Responses](http://docs.aws.amazon.com/AmazonS3/latest/API/ErrorResponses.html)\.