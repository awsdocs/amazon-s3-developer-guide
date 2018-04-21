# Upload Examples \(AWS Signature Version 2\)****<a name="HTTPPOSTExamples"></a>

**Topics**
+ [File Upload](#HTTPPOSTExamplesFileUpload)
+ [Text Area Upload](#HTTPPOSTExamplesTextArea)

**Note**  
The request authentication discussed in this section is based on AWS Signature Version 2, a protocol for authenticating inbound API requests to AWS services\.   
Amazon S3 now supports Signature Version 4, a protocol for authenticating inbound API requests to AWS services, in all AWS regions\. At this time, AWS regions created before January 30, 2014 will continue to support the previous protocol, Signature Version 2\. Any new regions after January 30, 2014 will support only Signature Version 4 and therefore all requests to those regions must be made with Signature Version 4\. For more information, see [Examples: Browser\-Based Upload using HTTP POST \(Using AWS Signature Version 4\)](http://docs.aws.amazon.com/AmazonS3/latest/API/sigv4-post-example.html) in the *Amazon Simple Storage Service API Reference*\. 

## File Upload<a name="HTTPPOSTExamplesFileUpload"></a>

This example shows the complete process for constructing a policy and form that can be used to upload a file attachment\.

### Policy and Form Construction<a name="HTTPPOSTExamplesFileUploadPolicy"></a>

The following policy supports uploads to Amazon S3 for the johnsmith bucket\.

```
 1. { "expiration": "2007-12-01T12:00:00.000Z",
 2.   "conditions": [
 3.     {"bucket": "johnsmith"},
 4.     ["starts-with", "$key", "user/eric/"],
 5.     {"acl": "public-read"},
 6.     {"success_action_redirect": "http://johnsmith.s3.amazonaws.com/successful_upload.html"},
 7.     ["starts-with", "$Content-Type", "image/"],
 8.     {"x-amz-meta-uuid": "14365123651274"},
 9.     ["starts-with", "$x-amz-meta-tag", ""]
10.   ]
11. }
```

This policy requires the following:
+ The upload must occur before 12:00 UTC on December 1, 2007\.
+ The content must be uploaded to the johnsmith bucket\.
+ The key must start with "user/eric/"\.
+ The ACL is set to public\-read\.
+ The success\_action\_redirect is set to http://johnsmith\.s3\.amazonaws\.com/successful\_upload\.html\.
+ The object is an image file\.
+ The x\-amz\-meta\-uuid tag must be set to 14365123651274\. 
+ The x\-amz\-meta\-tag can contain any value\.

The following is a Base64\-encoded version of this policy\.

```
1. eyAiZXhwaXJhdGlvbiI6ICIyMDA3LTEyLTAxVDEyOjAwOjAwLjAwMFoiLAogICJjb25kaXRpb25zIjogWwogICAgeyJidWNrZXQiOiAiam9obnNtaXRoIn0sCiAgICBbInN0YXJ0cy13aXRoIiwgIiRrZXkiLCAidXNlci9lcmljLyJdLAogICAgeyJhY2wiOiAicHVibGljLXJlYWQifSwKICAgIHsic3VjY2Vzc19hY3Rpb25fcmVkaXJlY3QiOiAiaHR0cDovL2pvaG5zbWl0aC5zMy5hbWF6b25hd3MuY29tL3N1Y2Nlc3NmdWxfdXBsb2FkLmh0bWwifSwKICAgIFsic3RhcnRzLXdpdGgiLCAiJENvbnRlbnQtVHlwZSIsICJpbWFnZS8iXSwKICAgIHsieC1hbXotbWV0YS11dWlkIjogIjE0MzY1MTIzNjUxMjc0In0sCiAgICBbInN0YXJ0cy13aXRoIiwgIiR4LWFtei1tZXRhLXRhZyIsICIiXQogIF0KfQo=
```

Using your credentials create a signature, for example `0RavWzkygo6QX9caELEqKi9kDbU=` is the signature for the preceding policy document\.

The following form supports a POST request to the johnsmith\.net bucket that uses this policy\.

```
 1. <html>
 2.   <head>
 3.     ...
 4.     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 5.     ...
 6.   </head>
 7.   <body>
 8.   ...
 9.   <form action="http://johnsmith.s3.amazonaws.com/" method="post" enctype="multipart/form-data">
10.     Key to upload: <input type="input" name="key" value="user/eric/" /><br />
11.     <input type="hidden" name="acl" value="public-read" />
12.     <input type="hidden" name="success_action_redirect" value="http://johnsmith.s3.amazonaws.com/successful_upload.html" />
13.     Content-Type: <input type="input" name="Content-Type" value="image/jpeg" /><br />
14.     <input type="hidden" name="x-amz-meta-uuid" value="14365123651274" />
15.     Tags for File: <input type="input" name="x-amz-meta-tag" value="" /><br />
16.     <input type="hidden" name="AWSAccessKeyId" value="AKIAIOSFODNN7EXAMPLE" />
17.     <input type="hidden" name="Policy" value="POLICY" />
18.     <input type="hidden" name="Signature" value="SIGNATURE" />
19.     File: <input type="file" name="file" /> <br />
20.     <!-- The elements after this will be ignored -->
21.     <input type="submit" name="submit" value="Upload to Amazon S3" />
22.   </form>
23.   ...
24. </html>
```

### Sample Request<a name="HTTPPOSTExamplesFileUploadRequest"></a>

This request assumes that the image uploaded is 117,108 bytes; the image data is not included\.

```
 1. POST / HTTP/1.1
 2. Host: johnsmith.s3.amazonaws.com
 3. User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.10) Gecko/20071115 Firefox/2.0.0.10
 4. Accept: text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5
 5. Accept-Language: en-us,en;q=0.5
 6. Accept-Encoding: gzip,deflate
 7. Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7
 8. Keep-Alive: 300
 9. Connection: keep-alive
10. Content-Type: multipart/form-data; boundary=9431149156168
11. Content-Length: 118698 
12. 
13. --9431149156168
14. Content-Disposition: form-data; name="key"
15. 
16. user/eric/MyPicture.jpg
17. --9431149156168
18. Content-Disposition: form-data; name="acl"
19. 
20. public-read
21. --9431149156168
22. Content-Disposition: form-data; name="success_action_redirect"
23. 
24. http://johnsmith.s3.amazonaws.com/successful_upload.html
25. --9431149156168
26. Content-Disposition: form-data; name="Content-Type"
27. 
28. image/jpeg
29. --9431149156168
30. Content-Disposition: form-data; name="x-amz-meta-uuid"
31. 
32. 14365123651274
33. --9431149156168
34. Content-Disposition: form-data; name="x-amz-meta-tag"
35. 
36. Some,Tag,For,Picture
37. --9431149156168
38. Content-Disposition: form-data; name="AWSAccessKeyId"
39. 
40. AKIAIOSFODNN7EXAMPLE
41. --9431149156168
42. Content-Disposition: form-data; name="Policy"
43. 
44. eyAiZXhwaXJhdGlvbiI6ICIyMDA3LTEyLTAxVDEyOjAwOjAwLjAwMFoiLAogICJjb25kaXRpb25zIjogWwogICAgeyJidWNrZXQiOiAiam9obnNtaXRoIn0sCiAgICBbInN0YXJ0cy13aXRoIiwgIiRrZXkiLCAidXNlci9lcmljLyJdLAogICAgeyJhY2wiOiAicHVibGljLXJlYWQifSwKICAgIHsic3VjY2Vzc19hY3Rpb25fcmVkaXJlY3QiOiAiaHR0cDovL2pvaG5zbWl0aC5zMy5hbWF6b25hd3MuY29tL3N1Y2Nlc3NmdWxfdXBsb2FkLmh0bWwifSwKICAgIFsic3RhcnRzLXdpdGgiLCAiJENvbnRlbnQtVHlwZSIsICJpbWFnZS8iXSwKICAgIHsieC1hbXotbWV0YS11dWlkIjogIjE0MzY1MTIzNjUxMjc0In0sCiAgICBbInN0YXJ0cy13aXRoIiwgIiR4LWFtei1tZXRhLXRhZyIsICIiXQogIF0KfQo=
45. --9431149156168
46. Content-Disposition: form-data; name="Signature"
47. 
48. 0RavWzkygo6QX9caELEqKi9kDbU=
49. --9431149156168
50. Content-Disposition: form-data; name="file"; filename="MyFilename.jpg"
51. Content-Type: image/jpeg
52. 
53. ...file content...
54. --9431149156168
55. Content-Disposition: form-data; name="submit"
56. 
57. Upload to Amazon S3
58. --9431149156168--
```

### Sample Response<a name="HTTPPOSTExamplesFileUploadResponse"></a>

```
1. HTTP/1.1 303 Redirect
2. x-amz-request-id: 1AEE782442F35865
3. x-amz-id-2: cxzFLJRatFHy+NGtaDFRR8YvI9BHmgLxjvJzNiGGICARZ/mVXHj7T+qQKhdpzHFh
4. Content-Type: application/xml
5. Date: Wed, 14 Nov 2007 21:21:33 GMT
6. Connection: close
7. Location: http://johnsmith.s3.amazonaws.com/successful_upload.html?bucket=johnsmith&key=user/eric/MyPicture.jpg&etag=&quot;39d459dfbc0faabbb5e179358dfb94c3&quot;
8. Server: AmazonS3
```

## Text Area Upload<a name="HTTPPOSTExamplesTextArea"></a>

**Topics**
+ [Policy and Form Construction](#HTTPPOSTExamplesTextAreaPolicy)
+ [Sample Request](#HTTPPOSTExamplesTextAreaRequest)
+ [Sample Response](#HTTPPOSTExamplesTextAreaResponse)

The following example shows the complete process for constructing a policy and form to upload a text area\. Uploading a text area is useful for submitting user\-created content, such as blog postings\.

### Policy and Form Construction<a name="HTTPPOSTExamplesTextAreaPolicy"></a>

The following policy supports text area uploads to Amazon S3 for the johnsmith bucket\.

```
 1. { "expiration": "2007-12-01T12:00:00.000Z",
 2.   "conditions": [
 3.     {"bucket": "johnsmith"},
 4.     ["starts-with", "$key", "user/eric/"],
 5.     {"acl": "public-read"},
 6.     {"success_action_redirect": "http://johnsmith.s3.amazonaws.com/new_post.html"},
 7.     ["eq", "$Content-Type", "text/html"],
 8.     {"x-amz-meta-uuid": "14365123651274"},
 9.     ["starts-with", "$x-amz-meta-tag", ""]
10.   ]
11. }
```

This policy requires the following:
+ The upload must occur before 12:00 GMT on 2007\-12\-01\.
+ The content must be uploaded to the johnsmith bucket\.
+ The key must start with "user/eric/"\.
+ The ACL is set to public\-read\.
+ The success\_action\_redirect is set to http://johnsmith\.s3\.amazonaws\.com/new\_post\.html\.
+ The object is HTML text\.
+ The x\-amz\-meta\-uuid tag must be set to 14365123651274\. 
+ The x\-amz\-meta\-tag can contain any value\.

Following is a Base64\-encoded version of this policy\.

```
1. eyAiZXhwaXJhdGlvbiI6ICIyMDA3LTEyLTAxVDEyOjAwOjAwLjAwMFoiLAogICJjb25kaXR
2. pb25zIjogWwogICAgeyJidWNrZXQiOiAiam9obnNtaXRoIn0sCiAgICBbInN0YXJ0cy13aXRoIiwgIiRrZXkiLCAidXNlci9lcmljLyJd
3. LAogICAgeyJhY2wiOiAicHVibGljLXJlYWQifSwKICAgIHsic3VjY2Vzc19hY3Rpb25fcmVkaXJlY3QiOiAiaHR0cDovL2pvaG5zbWl0a
4. C5zMy5hbWF6b25hd3MuY29tL25ld19wb3N0Lmh0bWwifSwKICAgIFsiZXEiLCAiJENvbnRlbnQtVHlwZSIsICJ0ZXh0L2h0bWwiXSwKI
5. CAgIHsieC1hbXotbWV0YS11dWlkIjogIjE0MzY1MTIzNjUxMjc0In0sCiAgICBbInN0YXJ0cy13aXRoIiwgIiR4LWFtei1tZXRhLXRhZy
6. IsICIiXQogIF0KfQo=
```

Using your credentials, create a signature\. For example, `qA7FWXKq6VvU68lI9KdveT1cWgF=` is the signature for the preceding policy document\.

The following form supports a POST request to the johnsmith\.net bucket that uses this policy\.

```
 1. <html>
 2.   <head>
 3.     ...
 4.     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 5.     ...
 6.   </head>
 7.   <body>
 8.   ...
 9.   <form action="http://johnsmith.s3.amazonaws.com/" method="post" enctype="multipart/form-data">
10.     Key to upload: <input type="input" name="key" value="user/eric/" /><br />
11.     <input type="hidden" name="acl" value="public-read" />
12.     <input type="hidden" name="success_action_redirect" value="http://johnsmith.s3.amazonaws.com/new_post.html" />
13.     <input type="hidden" name="Content-Type" value="text/html" />
14.     <input type="hidden" name="x-amz-meta-uuid" value="14365123651274" />
15.     Tags for File: <input type="input" name="x-amz-meta-tag" value="" /><br />
16.     <input type="hidden" name="AWSAccessKeyId" value="AKIAIOSFODNN7EXAMPLE" />
17.     <input type="hidden" name="Policy" value="POLICY" />
18.     <input type="hidden" name="Signature" value="SIGNATURE" />
19.     Entry: <textarea name="file" cols="60" rows="10">
20. 
21. Your blog post goes here.
22. 
23.     </textarea><br />
24.     <!-- The elements after this will be ignored -->
25.     <input type="submit" name="submit" value="Upload to Amazon S3" />
26.   </form>
27.   ...
28. </html>
```

### Sample Request<a name="HTTPPOSTExamplesTextAreaRequest"></a>

This request assumes that the image uploaded is 117,108 bytes; the image data is not included\.

```
 1. POST / HTTP/1.1
 2. Host: johnsmith.s3.amazonaws.com
 3. User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.10) Gecko/20071115 Firefox/2.0.0.10
 4. Accept: text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5
 5. Accept-Language: en-us,en;q=0.5
 6. Accept-Encoding: gzip,deflate
 7. Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7
 8. Keep-Alive: 300
 9. Connection: keep-alive
10. Content-Type: multipart/form-data; boundary=178521717625888
11. Content-Length: 118635
12. 
13. -178521717625888
14. Content-Disposition: form-data; name="key"
15. 
16. ser/eric/NewEntry.html
17. --178521717625888
18. Content-Disposition: form-data; name="acl"
19. 
20. public-read
21. --178521717625888
22. Content-Disposition: form-data; name="success_action_redirect"
23. 
24. http://johnsmith.s3.amazonaws.com/new_post.html
25. --178521717625888
26. Content-Disposition: form-data; name="Content-Type"
27. 
28. text/html
29. --178521717625888
30. Content-Disposition: form-data; name="x-amz-meta-uuid"
31. 
32. 14365123651274
33. --178521717625888
34. Content-Disposition: form-data; name="x-amz-meta-tag"
35. 
36. Interesting Post
37. --178521717625888
38. Content-Disposition: form-data; name="AWSAccessKeyId"
39. 
40. AKIAIOSFODNN7EXAMPLE
41. --178521717625888
42. Content-Disposition: form-data; name="Policy"
43. eyAiZXhwaXJhdGlvbiI6ICIyMDA3LTEyLTAxVDEyOjAwOjAwLjAwMFoiLAogICJjb25kaXRpb25zIjogWwogICAgeyJidWNrZXQiOiAiam9obnNtaXRoIn0sCiAgICBbInN0YXJ0cy13aXRoIiwgIiRrZXkiLCAidXNlci9lcmljLyJdLAogICAgeyJhY2wiOiAicHVibGljLXJlYWQifSwKICAgIHsic3VjY2Vzc19hY3Rpb25fcmVkaXJlY3QiOiAiaHR0cDovL2pvaG5zbWl0aC5zMy5hbWF6b25hd3MuY29tL25ld19wb3N0Lmh0bWwifSwKICAgIFsiZXEiLCAiJENvbnRlbnQtVHlwZSIsICJ0ZXh0L2h0bWwiXSwKICAgIHsieC1hbXotbWV0YS11dWlkIjogIjE0MzY1MTIzNjUxMjc0In0sCiAgICBbInN0YXJ0cy13aXRoIiwgIiR4LWFtei1tZXRhLXRhZyIsICIiXQogIF0KfQo=
44. 
45. --178521717625888
46. Content-Disposition: form-data; name="Signature"
47. 
48. qA7FWXKq6VvU68lI9KdveT1cWgF=
49. --178521717625888
50. Content-Disposition: form-data; name="file"
51. 
52. ...content goes here...
53. --178521717625888
54. Content-Disposition: form-data; name="submit"
55. 
56. Upload to Amazon S3
57. --178521717625888--
```

### Sample Response<a name="HTTPPOSTExamplesTextAreaResponse"></a>

```
1. HTTP/1.1 303 Redirect
2. x-amz-request-id: 1AEE782442F35865
3. x-amz-id-2: cxzFLJRatFHy+NGtaDFRR8YvI9BHmgLxjvJzNiGGICARZ/mVXHj7T+qQKhdpzHFh
4. Content-Type: application/xml
5. Date: Wed, 14 Nov 2007 21:21:33 GMT
6. Connection: close
7. Location: http://johnsmith.s3.amazonaws.com/new_post.html?bucket=johnsmith&key=user/eric/NewEntry.html&etag=40c3271af26b7f1672e41b8a274d28d4
8. Server: AmazonS3
```