# Setting the Storage Class of an Object You Upload<a name="SetStoClsOfObjUploaded"></a>

To set the storage class of an object you upload to RRS, you set `x-amz-storage-class` to `REDUCED_REDUNDANCY` in a `PUT` request\.

**How to Set the Storage Class of an Object You're Uploading to RRS**

+ Create a `PUT Object` request setting the `x-amz-storage-class` request header to `REDUCED_REDUNDANCY`\.

  You must have the correct permissions on the bucket to perform the `PUT` operation\. The default value for the storage class is `STANDARD` \(for regular Amazon S3 storage\)\.

  The following example sets the storage class of `my-image.jpg` to RRS\.

  ```
  1. PUT /my-image.jpg HTTP/1.1
  2. Host: myBucket.s3.amazonaws.com
  3. Date: Wed, 12 Oct 2009 17:50:00 GMT
  4. Authorization: AWS AKIAIOSFODNN7EXAMPLE:xQE0diMbLRepdf3YB+FIEXAMPLE=
  5. Content-Type: image/jpeg
  6. Content-Length: 11434
  7. Expect: 100-continue
  8. x-amz-storage-class: REDUCED_REDUNDANCY
  ```