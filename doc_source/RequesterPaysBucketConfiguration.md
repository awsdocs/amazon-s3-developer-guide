# Setting the requestPayment Bucket Configuration<a name="RequesterPaysBucketConfiguration"></a>

Only the bucket owner can set the `RequestPaymentConfiguration.payer` configuration value of a bucket to `BucketOwner`, the default, or `Requester`\. Setting the `requestPayment` resource is optional\. By default, the bucket is not a Requester Pays bucket\.

To revert a Requester Pays bucket to a regular bucket, you use the value `BucketOwner`\. Typically, you would use `BucketOwner` when uploading data to the Amazon S3 bucket, and then you would set the value to `Requester` before publishing the objects in the bucket\.

**To set requestPayment**
+ Use a `PUT` request to set the `Payer` value to `Requester` on a specified bucket\.

  ```
  1. PUT ?requestPayment HTTP/1.1
  2. Host: [BucketName].s3.amazonaws.com
  3. Content-Length: 173
  4. Date: Wed, 01 Mar 2009 12:00:00 GMT
  5. Authorization: AWS [Signature]
  6. 
  7. <RequestPaymentConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  8. <Payer>Requester</Payer>
  9. </RequestPaymentConfiguration>
  ```

If the request succeeds, Amazon S3 returns a response similar to the following\.

```
1. HTTP/1.1 200 OK
2. x-amz-id-2: [id]
3. x-amz-request-id: [request_id]
4. Date: Wed, 01 Mar 2009 12:00:00 GMT
5. Content-Length: 0
6. Connection: close
7. Server: AmazonS3
8. x-amz-request-charged:requester
```

You can set Requester Pays only at the bucket level; you cannot set Requester Pays for specific objects within the bucket\.

You can configure a bucket to be `BucketOwner` or `Requester` at any time\. Realize, however, that there might be a small delay, on the order of minutes, before the new configuration value takes effect\.

**Note**  
Bucket owners who give out pre\-signed URLs should think twice before configuring a bucket to be Requester Pays, especially if the URL has a very long lifetime\. The bucket owner is charged each time the requester uses a pre\-signed URL that uses the bucket owner's credentials\. 