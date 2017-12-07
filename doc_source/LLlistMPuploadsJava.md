# List Multipart Uploads<a name="LLlistMPuploadsJava"></a>

The following tasks guide you through using the low\-level Java classes to list all in\-progress multipart uploads on a bucket\.


**Low\-Level API Multipart Uploads Listing Process**  

|  |  | 
| --- |--- |
| 1 | Create an instance of the `ListMultipartUploadsRequest` class and provide the bucket name\. | 
| 2 | Execute the `AmazonS3Client.listMultipartUploads` method\. The method returns an instance of the `MultipartUploadListing` class that gives you information about the multipart uploads in progress\. | 

The following Java code sample demonstrates the preceding tasks\.

**Example**  

```
1. ListMultipartUploadsRequest allMultpartUploadsRequest = 
2.      new ListMultipartUploadsRequest(existingBucketName);
3. MultipartUploadListing multipartUploadListing = 
4.      s3Client.listMultipartUploads(allMultpartUploadsRequest);
```