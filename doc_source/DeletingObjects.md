# Deleting Objects<a name="DeletingObjects"></a>

**Topics**
+ [Deleting Objects from a Version\-Enabled Bucket](#DeletingObjectsfromaVersion-EnabledBucket)
+ [Deleting Objects from an MFA\-Enabled Bucket](#DeletingObjectsfromanMFA-EnabledBucket)
+ [Related Resources](#RelatedResources001)
+ [Deleting One Object Per Request](DeletingOneObject.md)
+ [Deleting Multiple Objects Per Request](DeletingMultipleObjects.md)

 You can delete one or more objects directly from Amazon S3\. You have the following options when deleting an object: 
+ **Delete a single object—**Amazon S3 provides the DELETE API that you can use to delete one object in a single HTTP request\. 
+ **Delete multiple objects—**Amazon S3 also provides the Multi\-Object Delete API that you can use to delete up to 1000 objects in a single HTTP request\. 

When deleting objects from a bucket that is not version\-enabled, you provide only the object key name, however, when deleting objects from a version\-enabled bucket, you can optionally provide version ID of the object to delete a specific version of the object\. 

## Deleting Objects from a Version\-Enabled Bucket<a name="DeletingObjectsfromaVersion-EnabledBucket"></a>

If your bucket is version\-enabled, then multiple versions of the same object can exist in the bucket\. When working with version\-enabled buckets, the delete API enables the following options:
+ **Specify a non\-versioned delete request—**That is, you specify only the object's key, and not the version ID\. In this case, Amazon S3 creates a delete marker and returns its version ID in the response\. This makes your object disappear from the bucket\. For information about object versioning and the delete marker concept, see [Object Versioning](ObjectVersioning.md)\.
+ **Specify a versioned delete request—**That is, you specify both the key and also a version ID\. In this case the following two outcomes are possible:
  + If the version ID maps to a specific object version, then Amazon S3 deletes the specific version of the object\.
  + If the version ID maps to the delete marker of that object, Amazon S3 deletes the delete marker\. This makes the object reappear in your bucket\. 

## Deleting Objects from an MFA\-Enabled Bucket<a name="DeletingObjectsfromanMFA-EnabledBucket"></a>

When deleting objects from a Multi Factor Authentication \(MFA\) enabled bucket, note the following:
+ If you provide an invalid MFA token, the request always fails\.
+ If you have an MFA\-enabled bucket, and you make a versioned delete request \(you provide an object key and version ID\), the request will fail if you don't provide a valid MFA token\. In addition, when using the Multi\-Object Delete API on an MFA\-enabled bucket, if any of the deletes is a versioned delete request \(that is, you specify object key and version ID\), the entire request will fail if you don't provide an MFA token\. 

On the other hand, in the following cases the request succeeds:
+ If you have an MFA\-enabled bucket, and you make a non\-versioned delete request \(you are not deleting a versioned object\),  and you don't provide an MFA token, the delete succeeds\. 
+ If you have a Multi\-Object Delete request specifying only non\-versioned objects to delete from an MFA\-enabled bucket,  and you don't provide an MFA token, the deletions succeed\.

For information on MFA delete, see [MFA Delete](Versioning.md#MultiFactorAuthenticationDelete)\.

## Related Resources<a name="RelatedResources001"></a>
+ [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)