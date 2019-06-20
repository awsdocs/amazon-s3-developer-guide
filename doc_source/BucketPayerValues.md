# Retrieving the requestPayment Configuration<a name="BucketPayerValues"></a>

You can determine the `Payer` value that is set on a bucket by requesting the resource `requestPayment`\.

**To return the requestPayment resource**
+ Use a GET request to obtain the `requestPayment` resource, as shown in the following request\.

  ```
  1. GET ?requestPayment HTTP/1.1
  2. Host: [BucketName].s3.amazonaws.com
  3. Date: Wed, 01 Mar 2009 12:00:00 GMT
  4. Authorization: AWS [Signature]
  ```

If the request succeeds, Amazon S3 returns a response similar to the following\.

```
 1. HTTP/1.1 200 OK
 2. x-amz-id-2: [id]
 3. x-amz-request-id: [request_id]
 4. Date: Wed, 01 Mar 2009 12:00:00 GMT
 5. Content-Type: [type]
 6. Content-Length: [length]
 7. Connection: close
 8. Server: AmazonS3
 9. 
10. <?xml version="1.0" encoding="UTF-8"?>
11. <RequestPaymentConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
12. <Payer>Requester</Payer>
13. </RequestPaymentConfiguration>
```

This response shows that the `payer` value is set to `Requester`\. 