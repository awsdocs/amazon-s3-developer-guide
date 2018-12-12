# Creating a Job<a name="batch-ops-create-job"></a>

[Sign up for the Preview](https://pages.awscloud.com/S3BatchOperations-Preview.html)

This section describes how to create a job and the results of a Create Job request\.

## Create Job Request<a name="batch-ops-create-job-request-elements"></a>

To create a job, you must provide the following information:

**Operation**  
You must specify the operation that you want Amazon S3 Batch Operations to execute against the objects in the manifest\. Each operation type accepts parameters specific to that operation, which enables you to perform the same tasks as if you performed the operation one\-by\-one on each object\.

**Manifest**  
The manifest is a list of all of the objects that you want Amazon S3 Batch Operations to execute the specified action on\. You can use an [ Amazon S3 Inventory](storage-inventory.md) report as a manifest or use your own customized CSV list of objects\.

**Priority**  
You can use job priorities to indicate the relative priority of this job to others running in your account\. Job priorities have no intrinsic meaning to Amazon S3 Batch Operations, so you can use them to prioritize jobs in any way that you want\. For example, you might want to assign all Glacier Restore jobs a priority of 1, all Copy Object jobs a priority of 2, and all Set Object ACL jobs a priority of 3\. Amazon S3 Batch Operations prioritizes jobs according to priority numbers, but it doesn't guarantee strict ordering\. Thus, you shouldn't use job priorities to ensure that any one job will start or finish before any other job\. If you need to ensure strict ordering, you should wait until one job has finished before starting the next\.

**RoleArn**  
You must specify an IAM Role that will run the job\. The IAM Role that you use to run the job must have sufficient permissions to perform the operation specified in the job\. For example, to run an S3CopyObject job, the IAM Role must have `s3:GetObject` permissions for the source bucket and `s3:PutObject` permissions for the destination bucket\. The Role also needs permissions to read the manifest and write the job\-completion report\. For more information about IAM Roles, see [IAM Roles](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html)\. For more information about Amazon S3 permissions, see [Specifying Permissions in a Policy](using-with-s3-actions.md)\.

**Report**  
Report specifies whether you want Amazon S3 Batch Operations to generate a job\-completion report\. If you request a job\-completion report, then you must also provide the parameters for the report in this element\. The necessary information includes the bucket where you want to store the report, the format of the report, whether you want the report to include the details of all tasks or only failed tasks, and an optional prefix string\.

**Description \(Optional\)**  
You can also provide a description of up to 256 characters that can help you track and monitor your job\. Amazon S3 includes this description whenever it returns information about a job or displays job details in the Amazon S3 Console, enabling you to easily sort and filter jobs according to the descriptions you've assigned\. Descriptions don't need to be unique, so you can use descriptions as categories \(for example, "Weekly Log Copy Jobs"\) to help you track groups of similar jobs\.

## Create Job Response<a name="batch-ops-create-job-response-elements"></a>

If the Create Job request succeeds, Amazon S3 Batch Operations returns a job ID\. The job ID is a unique identifier that Amazon S3 generates automatically so that you can identify your job and monitor its status\.

When you create a job through the AWS CLI, AWS SDKs, or REST API, S3 Batch Operations begins to process the job automatically, and the job runs as soon as it's ready and not waiting behind higher\-priority jobs\. When you create a job through the AWS Management Console, you must review the job details and confirm that you want to run it before S3 Batch Operations will begin to process it\. After you confirm that you want to run the job, it progresses as though you had created it through one of the other methods\.