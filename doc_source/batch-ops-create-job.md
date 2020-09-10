# Creating an S3 Batch Operations job<a name="batch-ops-create-job"></a>

With S3 Batch Operations, you can perform large\-scale Batch Operations on a list of specific Amazon S3 objects\. You can create S3 Batch Operations jobs using the AWS Management Console, AWS Command Line Interface \(AWS CLI\), AWS SDKs, or REST API\. 

This section describes the information that you need to create an S3 Batch Operations job\. It also describes the results of a `Create Job` request\.

**Note**  
For step\-by\-step instructions for creating a job using the Amazon S3 console, see [Creating an S3 Batch Operations Job](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/batch-ops-create-job.html) in the *Amazon Simple Storage Service Console User Guide\.*

## Creating a job request<a name="batch-ops-create-job-request-elements"></a>

To create an S3 Batch Operations job, you must provide the following information:

**Operation**  
Specify the operation that you want S3 Batch Operations to run against the objects in the manifest\. Each operation type accepts parameters that are specific to that operation\. This enables you to perform the same tasks as if you performed the operation one\-by\-one on each object\.

**Manifest**  
The manifest is a list of all of the objects that you want S3 Batch Operations to run the specified action on\. You can use a CSV\-formatted [ Amazon S3 inventory](storage-inventory.md) report as a manifest or use your own customized CSV list of objects\.   
For more information about manifests, see [Specifying a manifest](batch-ops-basics.md#specify-batchjob-manifest)\.

**Priority**  
Use job priorities to indicate the relative priority of this job to others running in your account\. A higher number indicates higher priority\.  
 Job priorities only have meaning relative to the priorities that are set for other jobs in the same account and Region\. So you can choose whatever numbering system works for you\. For example, you might want to assign all `Initiate Restore Object` jobs a priority of 1, all `PUT Object Copy` jobs a priority of 2, and all `Put Object ACL` jobs a priority of 3\.   
S3 Batch Operations prioritize jobs according to priority numbers, but strict ordering isn't guaranteed\. Thus, you shouldn't use job priorities to ensure that any one job will start or finish before any other job\. If you need to ensure strict ordering, wait until one job has finished before starting the next\. 

**RoleArn**  
Specify an AWS Identity and Access Management \(IAM\) role to run the job\. The IAM role that you use must have sufficient permissions to perform the operation that is specified in the job\. For example, to run a `PUT Object Copy` job, the IAM role must have `s3:GetObject` permissions for the source bucket and `s3:PutObject` permissions for the destination bucket\. The role also needs permissions to read the manifest and write the job\-completion report\.   
For more information about IAM roles, see [IAM Roles](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html) in the *IAM User Guide*\.   
For more information about Amazon S3 permissions, see [Amazon S3 Actions](using-with-s3-actions.md)\.

**Report**  
Specify whether you want S3 Batch Operations to generate a completion report\. If you request a job\-completion report, you must also provide the parameters for the report in this element\. The necessary information includes the bucket where you want to store the report, the format of the report, whether you want the report to include the details of all tasks or only failed tasks, and an optional prefix string\.

**Tags \(Optional\)**  
You can label and control access to your S3 Batch Operations jobs by adding *tags*\. Tags can be used to identify who is responsible for a Batch Operations job\. You can create jobs with tags attached to them, and you can add tags to jobs after you create them\. For example, you could grant an IAM user permission to invoke `CreateJob` provided that the job is created with the tag `"Department=Finance"`\.   
For more information, see [Controlling access and labeling jobs using tags](batch-ops-managing-jobs.md#batch-ops-job-tags)\.

**Description \(Optional\)**  
To track and monitor your job, you can also provide a description of up to 256 characters\. Amazon S3 includes this description whenever it returns information about a job or displays job details on the Amazon S3 console\. You can then easily sort and filter jobs according to the descriptions that you assigned\. Descriptions don't need to be unique, so you can use descriptions as categories \(for example, "Weekly Log Copy Jobs"\) to help you track groups of similar jobs\.

## Creating a job response<a name="batch-ops-create-job-response-elements"></a>

If the `Create Job` request succeeds, Amazon S3 returns a job ID\. The job ID is a unique identifier that Amazon S3 generates automatically so that you can identify your Batch Operations job and monitor its status\.

When you create a job through the AWS CLI, AWS SDKs, or REST API, you can set S3 Batch Operations to begin processing the job automatically\. The job runs as soon as it's ready and not waiting behind higher\-priority jobs\. 

When you create a job through the AWS Management Console, you must review the job details and confirm that you want to run it before Batch Operations can begin to process it\. After you confirm that you want to run the job, it progresses as though you had created it through one of the other methods\. If a job remains in the suspended state for over 30 days, it will fail\.

For examples, see [S3 Batch Operations examples using the AWS CLI](batch-ops-examples-cli.md)\.