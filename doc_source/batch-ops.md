# Performing Batch Operations<a name="batch-ops"></a>

You can use Amazon S3 batch operations to perform large\-scale batch operations on Amazon S3 objects\. Amazon S3 batch operations can execute a single operation on lists of Amazon S3 objects that you specify\. A single job can perform the specified operation on billions of objects containing exabytes of data\. Amazon S3 tracks progress, sends notifications, and stores a detailed completion report of all actions, providing a fully managed, auditable, serverless experience\. You can use Amazon S3 batch operations through the AWS Management Console, AWS CLI, AWS SDKs, or REST API\.

Use Amazon S3 batch operations to copy objects and set object tags or access control lists \(ACLs\)\. You can also initiate object restores from Amazon S3 Glacier or invoke an AWS Lambda function to perform custom actions using your objects\. You can perform these operations on a custom list of objects, or you can use an Amazon S3 inventory report to make generating even the largest lists of objects easy\. Amazon S3 batch operations use the same Amazon S3 APIs that you already use with Amazon S3, so you'll find the interface familiar\. 

**Topics**
+ [Terminology](#batch-ops-terminology)
+ [The Basics: Amazon S3 Batch Operations Jobs](batch-ops-basics.md)
+ [Creating an Amazon S3 Batch Operations Job](batch-ops-create-job.md)
+ [Operations](batch-ops-operations.md)
+ [Managing Batch Operations Jobs](batch-ops-managing-jobs.md)
+ [Amazon S3 Batch Operations Examples](batch-ops-examples.md)

[![AWS Videos](http://img.youtube.com/vi/https://www.youtube.com/embed/hUv34voEftc//0.jpg)](http://www.youtube.com/watch?v=https://www.youtube.com/embed/hUv34voEftc/)

## Terminology<a name="batch-ops-terminology"></a>

This section uses the terms *jobs*, *operations*, and *tasks*, which are defined as follows:

**Job**  
A job is the basic unit of work for Amazon S3 batch operations\. A job contains all of the information necessary to execute the specified operation on the objects listed in the manifest\. After you provide this information and request that the job begin, the job executes the operation for each object in the manifest\. 

**Operation**  
An operation is a single command that you want a job to execute\. Each job contains only one type of operation with one set of parameters, which Amazon S3 batch operations execute for each object\.

**Task**  
A task is the unit of execution for a job\. A task represents a single call to an Amazon S3 or AWS Lambda API operation to perform the job's operation on a single object\. Over the course of a job's lifetime, Amazon S3 batch operations create one task for each object specified in the manifest\.