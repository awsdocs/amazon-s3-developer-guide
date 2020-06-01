# Put object tagging<a name="batch-ops-put-object-tagging"></a>

The Put Object Tagging operation replaces the Amazon S3 object tags of each object listed in the manifest\. An Amazon S3 object tag is a key\-value pair of strings that you can use to store metadata about an object\.

To create a Put Object Tagging job, you provide a set of tags that you want to apply\. S3 Batch Operations apply the same set of tags to each object\. The tag set that you provide replaces whatever tag sets are already associated with the objects in the manifest\. S3 Batch Operations do not support adding tags to objects while leaving the existing tags in place\.

If the objects in your manifest are in a versioned bucket, you can apply the tag set to specific versions of each object\. You do this by specifying a version ID for each object in the manifest\. If you don't include a version ID for any object, then S3 Batch Operations will apply the tag set to the latest version of each object\. 

## Restrictions and limitations<a name="batch-ops-set-tagging-restrictions"></a>
+ The role that you specify to run the Put Object Tagging job must have permissions to perform the underlying Amazon S3 PUT Object tagging operation\. For more information about the permissions required, see [PUT Object tagging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTtagging.html) in the *Amazon Simple Storage Service API Reference*\.
+ S3 Batch Operations use the Amazon S3 PUT Object tagging operation to apply tags to each object in the manifest\. Therefore, all restrictions and limitations that apply to the underlying PUT Object tagging operation also apply to S3 Batch Operations Put Object Tagging jobs\. For more information, see the [Related resources](#batch-ops-put-object-tagging-related-resources) section of this page\.

## Related resources<a name="batch-ops-put-object-tagging-related-resources"></a>
+ [Object tagging](object-tagging.md)
+ [GET Object tagging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETtagging.html) in the *Amazon Simple Storage Service API Reference*
+ [PUT Object tagging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTtagging.html) in the *Amazon Simple Storage Service API Reference*