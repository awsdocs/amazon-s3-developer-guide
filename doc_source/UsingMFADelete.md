# Using MFA Delete<a name="UsingMFADelete"></a>

If a bucket's versioning configuration is MFA Delete–enabled, the bucket owner must include the `x-amz-mfa` request header in requests to permanently delete an object version or change the versioning state of the bucket\. Requests that include `x-amz-mfa` must use HTTPS\. The header's value is the concatenation of your authentication device's serial number, a space, and the authentication code displayed on it\. If you do not include this request header, the request fails\.

For more information about authentication devices, see [https://aws\.amazon\.com/iam/details/mfa/](https://aws.amazon.com/iam/details/mfa/)\.

**Example Deleting an Object from an MFA Delete Enabled Bucket**  
The following example shows how to delete `my-image.jpg` \(with the specified version\), which is in a bucket configured with MFA Delete enabled\. Note the space between *\[SerialNumber\]* and *\[AuthenticationCode\]*\. For more information, see [DELETE Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectDELETE.html)\.  

```
1. DELETE /my-image.jpg?versionId=3HL4kqCxf3vjVBH40Nrjfkd HTTPS/1.1
2. Host: bucketName.s3.amazonaws.com
3. x-amz-mfa: 20899872 301749
4. Date: Wed, 28 Oct 2009 22:32:00 GMT
5. Authorization: AWS AKIAIOSFODNN7EXAMPLE:0RQf4/cRonhpaBX5sCYVf1bNRuU=
```

For more information about enabling MFA delete, see [MFA Delete](Versioning.md#MultiFactorAuthenticationDelete)\.