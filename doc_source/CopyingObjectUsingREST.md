# Copy an Object Using the REST API<a name="CopyingObjectUsingREST"></a>

This example describes how to copy an object using REST\. For more information about the REST API, go to [PUT Object \(Copy\)](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html)\.

This example copies the `flotsam` object from the `pacific` bucket to the `jetsam` object of the `atlantic` bucket, preserving its metadata\.

```
1. PUT /jetsam HTTP/1.1
2. Host: atlantic.s3.amazonaws.com
3. x-amz-copy-source: /pacific/flotsam
4. Authorization: AWS AKIAIOSFODNN7EXAMPLE:ENoSbxYByFA0UGLZUqJN5EUnLDg=
5. Date: Wed, 20 Feb 2008 22:12:21 +0000
```

The signature was generated from the following information\.

```
1. PUT\r\n
2. \r\n
3. \r\n
4. Wed, 20 Feb 2008 22:12:21 +0000\r\n
5. 
6. x-amz-copy-source:/pacific/flotsam\r\n
7. /atlantic/jetsam
```

Amazon S3 returns the following response that specifies the ETag of the object and when it was last modified\.

```
 1. HTTP/1.1 200 OK
 2. x-amz-id-2: Vyaxt7qEbzv34BnSu5hctyyNSlHTYZFMWK4FtzO+iX8JQNyaLdTshL0KxatbaOZt
 3. x-amz-request-id: 6B13C3C5B34AF333
 4. Date: Wed, 20 Feb 2008 22:13:01 +0000
 5. 
 6. Content-Type: application/xml
 7. Transfer-Encoding: chunked
 8. Connection: close
 9. Server: AmazonS3
10. <?xml version="1.0" encoding="UTF-8"?>
11. 
12. <CopyObjectResult>
13.    <LastModified>2008-02-20T22:13:01</LastModified>
14.    <ETag>"7e9c608af58950deeb370c98608ed097"</ETag>
15. </CopyObjectResult>
```