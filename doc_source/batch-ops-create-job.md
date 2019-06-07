# Creating a Batch Operations Job<a name="batch-ops-create-job"></a>

This section describes the information that you need to create an Amazon S3 batch operations job\. It also describes the results of a `Create Job` request\.

## Create Job Request<a name="batch-ops-create-job-request-elements"></a>

To create a job, you must provide the following information:

**Operation**  
Specify the operation that you want Amazon S3 batch operations to execute against the objects in the manifest\. Each operation type accepts parameters that are specific to that operation, which enables you to perform the same tasks as if you performed the operation one\-by\-one on each object\.

**Manifest**  
The manifest is a list of all of the objects that you want Amazon S3 batch operations to execute the specified action on\. You can use an [ Amazon S3 Inventory](storage-inventory.md) report as a manifest or use your own customized CSV list of objects\. For more information about manifests, see [Specifying a Manifest](batch-ops-basics.md#specify-batchjob-manifest)\.

**Priority**  
Use job priorities to indicate the relative priority of this job to others running in your account\. A higher number indicates higher priority\.  
 Job priorities have no intrinsic meaning to Amazon S3 batch operations, so you can use them to prioritize jobs however you want\. For example, you might want to assign all `Initiate Restore Object` jobs a priority of 1, all `PUT Object Copy` jobs a priority of 2, and all `Put Object ACL` jobs a priority of 3\. Batch operations prioritize jobs according to priority numbers, but strict ordering isn't guaranteed\. Thus, you shouldn't use job priorities to ensure that any one job will start or finish before any other job\. If you need to ensure strict ordering, wait until one job has finished before starting the next\. 

**RoleArn**  
You must specify an IAM role to run the job\. The IAM role that you use must have sufficient permissions to perform the operation that is specified in the job\. For example, to run an `PUT Object Copy` job, the IAM role must have `s3:GetObject` permissions for the source bucket and `s3:PutObject` permissions for the destination bucket\. The role also needs permissions to read the manifest and write the job\-completion report\. For more information about IAM roles, see [IAM Roles](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html)\. For more information about Amazon S3 permissions, see [Specifying Permissions in a Policy](using-with-s3-actions.md)\.

**Report**  
Specify whether you want Amazon S3 batch operations to generate a completion report\. If you request a job\-completion report, then you must also provide the parameters for the report in this element\. The necessary information includes the bucket where you want to store the report, the format of the report, whether you want the report to include the details of all tasks or only failed tasks, and an optional prefix string\.

**Description \(Optional\)**  
You can also provide a description of up to 256 characters to help you track and monitor your job\. Amazon S3 includes this description whenever it returns information about a job or displays job details on the Amazon S3 console\. You can then easily sort and filter jobs according to the descriptions that you assigned\. Descriptions don't need to be unique, so you can use descriptions as categories \(for example, "Weekly Log Copy Jobs"\) to help you track groups of similar jobs\.

## Create Job Response<a name="batch-ops-create-job-response-elements"></a>

If the `Create Job` request succeeds, Amazon S3 returns a job ID\. The job ID is a unique identifier that Amazon S3 generates automatically so that you can identify your batch operations job and monitor its status\.

When you create a job through the AWS CLI, AWS SDKs, or REST API, you can set Amazon S3 batch operations to begin processing the job automatically\. The job runs as soon as it's ready and not waiting behind higher\-priority jobs\. When you create a job through the AWS Management Console, you must review the job details and confirm that you want to run it before batch operations can begin to process it\. After you confirm that you want to run the job, it progresses as though you had created it through one of the other methods\. If a job remains in the suspended state for over 30 days, it will fail\.

## Related Resources<a name="batch-ops-create-job-related-resources"></a>
+ [The Basics: Amazon S3 Batch Operations Jobs](batch-ops-basics.md)
+ [Operations](batch-ops-operations.md)
+ [Managing Batch Operations Jobs](batch-ops-managing-jobs.md)