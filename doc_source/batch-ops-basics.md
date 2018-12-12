# The Basics: Amazon S3 Batch Operations Jobs<a name="batch-ops-basics"></a>

[Sign up for the Preview](https://pages.awscloud.com/S3BatchOperations-Preview.html)

 To create a job, you give Amazon S3 Batch Operations a list of objects and select the action to perform on those objects\. Amazon S3 Batch Operations supports five different operations:
+ [PUT Copy Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html)
+ [Set Object Tagging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTtagging.html)
+ [Set Object ACL](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTacl.html)
+ [Initiate Glacier Restore](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOSTrestore.html)
+ [Invoke an AWS Lambda Function](https://docs.aws.amazon.com/lambda/latest/dg/API_Invoke.html)

The objects that you want a job to act on are listed in a manifest object\. A job performs the specified operation on each object included in its manifest\. You can use an [ Amazon S3 Inventory](storage-inventory.md) report as a manifest, which makes it easy to create large lists of objects located in a bucket\. You can also specify a manifest in a simple CSV format that enables you to perform batch operations on a customized list of objects contained within a single bucket\. 

**Important**  
If the objects in your manifest are in a versioned bucket, you should specify version IDs for the objects\. When you create a job, Amazon S3 Batch Operations parses the entire manifest before running the job, but it doesn't take a "snapshot" of the state of the bucket\. Because manifests can contain billions of objects, jobs might take a long time to run\. If you overwrite an object with a new version while a job is running and you didn't specify a version ID for that object, Amazon S3 will perform the operation on the latest version of the object, not the version that existed when you created the job\. The only way to avoid this behavior is to specify a version ID for the object in the manifest\. 

Once you've created a job, Amazon S3 processes the list of objects in the manifest and executes the specified operation against each object\. While a job is executing, you can monitor its progress programmatically or through the Amazon S3 console\. You can also configure a job to generate a completion report when it finishes\. The completion report describes the results of each task executed by the job\. For more information about monitoring jobs, see [Managing Jobs](batch-ops-managing-jobs.md)\.