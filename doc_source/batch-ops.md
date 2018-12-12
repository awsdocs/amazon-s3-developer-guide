# Performing Batch Operations<a name="batch-ops"></a>

[Sign up for the Preview](https://pages.awscloud.com/S3BatchOperations-Preview.html)

Amazon S3 Batch Operations enables you to easily perform large\-scale batch operations on Amazon S3 objects\. S3 Batch Operations executes a single operation on lists of S3 objects that you specify\. A single job can perform the specified operation on billions of objects containing exabytes of data\. S3 Batch Operations tracks progress, sends notifications, and stores a detailed completion report of all actions, providing a fully managed, auditable, serverless experience\. You can use Amazon S3 Batch Operations through the AWS Management Console, AWS CLI, AWS SDKs, or REST API\.

Using Amazon S3 Batch Operations, you can copy objects, set object tags or Access Control Lists \(ACLs\), initiate object restores from Glacier, or invoke a Lambda function to perform custom actions using your objects\. You can perform these operations on a custom list of objects or you can use an S3 inventory report to make generating even the largest lists of objects easy\. S3 Batch Operations invokes these operations using the same APIs that you already use with Amazon S3, so Amazon S3 users will find its interface familiar\. For more information about the operations that Amazon S3 Batch Operations supports, see the following topics:
+ [PUT Copy Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html)
+ [Set Object Tagging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTtagging.html)
+ [Set Object ACL](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTacl.html)
+ [Initiate Glacier Restore](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOSTrestore.html)
+ [Invoke an AWS Lambda Function](https://docs.aws.amazon.com/lambda/latest/dg/API_Invoke.html)

## Related Resources<a name="batch-ops-intro-more-info"></a>

For more information about Amazon S3 Batch Operations, see the following pages:
+ [The Basics: Amazon S3 Batch Operations Jobs](batch-ops-basics.md)
+ [Creating a Job](batch-ops-create-job.md)
+ [Managing Jobs](batch-ops-managing-jobs.md)

## Terminology in this Guide<a name="batch-ops-terminology"></a>

This guide refers to jobs, operations, and tasks\. As used in this guide, these terms mean the following:

**Job**  
A job is the basic unit of work for Amazon S3 Batch Operations\. A job contains all of the information necessary to execute the specified operation against the objects listed in the manifest\. Once you have provided this information and requested that the job begin, the job executes the operation against each object in the manifest\. 

**Operation**  
An operation is a single command that you want a job to execute\. Each job contains only one operation with one set of parameters, which Amazon S3 Batch Operations executes against each object\.

**Task**  
A task is the unit of execution for a job\. A task represents a single call to an Amazon S3 or AWS Lambda API to perform the job's operation against a single object\. Over the course of a job's lifetime, Amazon S3 Batch Operations will create one task for each object specified in the manifest\.