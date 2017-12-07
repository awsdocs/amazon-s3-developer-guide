# List Multipart Uploads<a name="LLlistMPuploadsDotNet"></a>

The following tasks guide you through using the low\-level \.NET classes to list all in\-progress multipart uploads on a bucket\.


**Low\-Level API Multipart Uploads Listing Process**  

|  |  | 
| --- |--- |
| 1 | Create an instance of the `ListMultipartUploadsRequest` class and provide the bucket name\. | 
| 2 | Execute the `AmazonS3Client.ListMultipartUploads` method\. The method returns an instance of the `ListMultipartUploadsResponse` class, providing you the information about the in\-progress multipart uploads\. | 

The following C\# code sample demonstrates the preceding tasks\.

**Example**  

```
1.  ListMultipartUploadsRequest request = new ListMultipartUploadsRequest
2. {
3.      BucketName = existingBucketName
4. };
```