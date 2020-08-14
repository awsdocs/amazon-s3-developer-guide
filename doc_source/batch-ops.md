# Performing S3 Batch Operations<a name="batch-ops"></a>

You can use S3 Batch Operations to perform large\-scale batch operations on Amazon S3 objects\. S3 Batch Operations can perform a single operation on lists of Amazon S3 objects that you specify\. A single job can perform the specified operation on billions of objects containing exabytes of data\. Amazon S3 tracks progress, sends notifications, and stores a detailed completion report of all actions, providing a fully managed, auditable, serverless experience\. You can use S3 Batch Operations through the AWS Management Console, AWS CLI, AWS SDKs, or REST API\.

Use S3 Batch Operations to copy objects and set object tags or access control lists \(ACLs\)\. You can also initiate object restores from Amazon S3 Glacier or invoke an AWS Lambda function to perform custom actions using your objects\. You can perform these operations on a custom list of objects, or you can use an Amazon S3 inventory report to make generating even the largest lists of objects easy\. Amazon S3 Batch Operations use the same Amazon S3 APIs that you already use with Amazon S3, so you'll find the interface familiar\. 

**Topics**
+ [Terminology](#batch-ops-terminology)
+ [The basics: S3 Batch Operations](batch-ops-basics.md)
+ [Creating an S3 Batch Operations job](batch-ops-create-job.md)
+ [Operations](batch-ops-operations.md)
+ [Managing S3 Batch Operations jobs](batch-ops-managing-jobs.md)
+ [S3 Batch Operations examples](batch-ops-examples.md)

[![AWS Videos](http://img.youtube.com/vi/https://www.youtube.com/embed/hUv34voEftc//0.jpg)](http://www.youtube.com/watch?v=https://www.youtube.com/embed/hUv34voEftc/)

## Terminology<a name="batch-ops-terminology"></a>

This section uses the terms *jobs*, *operations*, and *tasks*, which are defined as follows:

**Job**  
A job is the basic unit of work for S3 Batch Operations\. A job contains all of the information necessary to run the specified operation on the objects listed in the manifest\. After you provide this information and request that the job begin, the job executes the operation for each object in the manifest\. 

**Operation**  
The operation is the type of API [action](https://docs.aws.amazon.com/AmazonS3/latest/API/API_Operations.html), such as copying objects, that you want the Batch Operations job to run\. Each job performs a single type of operation across all objects that are specified in the manifest\.

**Task**  
A task is the unit of execution for a job\. A task represents a single call to an Amazon S3 or AWS Lambda API operation to perform the job's operation on a single object\. Over the course of a job's lifetime, S3 Batch Operations create one task for each object specified in the manifest\.