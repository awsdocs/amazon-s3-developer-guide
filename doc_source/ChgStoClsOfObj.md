# Changing the Storage Class of an Object in Amazon S3<a name="ChgStoClsOfObj"></a>


+ [Return Code for Lost Data](RtnCodeLostData.md)

You can also change the storage class of an object that is already stored in Amazon S3 by copying it to the same key name in the same bucket\. To do that, you use the following request headers in a `PUT Object copy` request:

+ `x-amz-metadata-directive` set to `COPY`

+ `x-amz-storage-class` set to `STANDARD`, `STANDARD_IA`, or `REDUCED_REDUNDANCY`

**Important**  
To optimize the execution of the copy request, do not change any of the other metadata in the `PUT Object copy` request\. If you need to change metadata other than the storage class, set `x-amz-metadata-directive` to `REPLACE` for better performance\.

**How to Rewrite the Storage Class of an Object in Amazon S3**

+ Create a `PUT Object copy` request and set the `x-amz-storage-class` request header to `REDUCED_REDUNDANCY` \(for RRS\) or `STANDARD` \(for regular Amazon S3 storage\) or `STANDARD_IA` \(for Standard\-Infrequent Access\), and make the target name the same as the source name\.

  You must have the correct permissions on the bucket to perform the copy operation\.

  The following example sets the storage class of `my-image.jpg` to RRS\.

  ```
  1. PUT /my-image.jpg HTTP/1.1
  2. Host: bucket.s3.amazonaws.com
  3. Date: Wed, 28 Oct 2009 22:32:00 GMT
  4. x-amz-copy-source: /bucket/my-image.jpg
  5. Authorization: AWS AKIAIOSFODNN7EXAMPLE:0RQf4/cRonhpaBX5sCYVf1bNRuU=
  6. x-amz-storage-class: REDUCED_REDUNDANCY
  7. x-amz-metadata-directive: COPY
  ```

  The following example sets the storage class of `my-image.jpg` to Standard\.

  ```
  1. PUT /my-image.jpg HTTP/1.1
  2. Host: bucket.s3.amazonaws.com
  3. Date: Wed, 28 Oct 2009 22:32:00 GMT
  4. x-amz-copy-source: /bucket/my-image.jpg
  5. Authorization: AWS AKIAIOSFODNN7EXAMPLE:0RQf4/cRonhpaBX5sCYVf1bNRuU=
  6. x-amz-storage-class: STANDARD
  7. x-amz-metadata-directive: COPY
  ```

  The following example sets the storage class of `my-image.jpg` to Standard\-Infrequent Access\.

  ```
  1. PUT /my-image.jpg HTTP/1.1
  2. Host: bucket.s3.amazonaws.com
  3. Date: Sat, 30 Apr 2016 23:29:37 GMT
  4. x-amz-copy-source: /bucket/my-image.jpg
  5. Authorization: AWS AKIAIOSFODNN7EXAMPLE:0RQf4/cRonhpaBX5sCYVf1bNRuU=
  6. x-amz-storage-class: STANDARD_IA
  7. x-amz-metadata-directive: COPY
  ```

**Note**  
If you copy an object and fail to include the `x-amz-storage-class` request header, the storage class of the target object defaults to `STANDARD`\.

It is not possible to change the storage class of a specific version of an object\. When you copy it, Amazon S3 gives it a new version ID\.

**Note**  
When an object is written in a copy request, the entire object is rewritten in order to apply the new storage class\. 

For more information about versioning, see [Using Versioning](Versioning.md)\.