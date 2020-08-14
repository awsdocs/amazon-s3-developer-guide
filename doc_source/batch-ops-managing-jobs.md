# Managing S3 Batch Operations jobs<a name="batch-ops-managing-jobs"></a>

Amazon S3 provides a robust set of tools to help you manage your Batch Operations jobs after you create them\. This section describes the operations you can use to manage your jobs\. You can perform all of the operations listed in this section using the AWS Management Console, AWS CLI, AWS SDKs, or REST APIs\.

**Topics**
+ [Listing jobs](#batch-ops-list-jobs)
+ [Viewing job details](#batch-ops-job-details)
+ [Controlling access and labeling jobs using tags](#batch-ops-job-tags)
+ [Assigning job priority](#batch-ops-job-priority)
+ [Job status](#batch-ops-job-status)
+ [Tracking job failure](#batch-ops-job-status-failure)
+ [Notifications and logging](#batch-ops-notifications)
+ [Completion reports](#batch-ops-completion-report)

The following video briefly describes how you can use the Amazon S3 console to manage your S3 Batch Operations jobs\.

[![AWS Videos](http://img.youtube.com/vi/https://www.youtube.com/embed/CuMDH6c0zm4//0.jpg)](http://www.youtube.com/watch?v=https://www.youtube.com/embed/CuMDH6c0zm4/)

## Listing jobs<a name="batch-ops-list-jobs"></a>

You can retrieve a list of your S3 Batch Operations jobs\. The list includes jobs that haven't yet finished and jobs that finished within the last 90 days\. The job list includes information for each job, such as its ID, description, priority, current status, and the number of tasks that have succeeded and failed\. You can filter your job list by status\. When you retrieve a job list through the console, you can also search your jobs by description or ID and filter them by AWS Region\.

## Viewing job details<a name="batch-ops-job-details"></a>

If you want more information about a job than you can retrieve by listing jobs, you can view all of the details for a single job\. In addition to the information returned in a job list, a single job's details include other items\. This information includes the operation parameters, details about the manifest, information about the completion report \(if you configured one when you created the job\), and the Amazon Resource Name \(ARN\) of the user role that you assigned to run the job\. By viewing an individual job's details, you can access a job's entire configuration\. 

## Controlling access and labeling jobs using tags<a name="batch-ops-job-tags"></a>

You can label and control access to your S3 Batch Operations jobs by adding *tags*\. Tags can be used to identify who is responsible for a Batch Operations job\. The presence of job tags can grant or limit a user's ability to cancel a job, activate a job in the confirmation state, or change a job's priority level\. You can create jobs with tags attached to them, and you can add tags to jobs after they are created\. Each tag is a key\-value pair that can be included when you create the job or updated later\.

**Warning**  
Job tags should not contain any confidential information or personal data\.

Consider the following tagging example: Suppose that you want your Finance department to create a Batch Operations job\. You could write an AWS Identity and Access Management \(IAM\) policy that allows a user to invoke `CreateJob`, provided that the job is created with the `Department` tag assigned the value `Finance`\. Furthermore, you could attach that policy to all users who are members of the Finance department\.

Continuing with this example, you could write a policy that allows a user to update the priority of any job that has the desired tags, or cancel any job that has those tags\. For more information, see [Example: Using job tags to control permissions for S3 Batch Operations](batch-ops-job-tags-examples.md)\.

You can add tags to new S3 Batch Operations jobs when you create them, or you can add them to existing jobs\. 

Note the following tag restrictions:
+ You can associate up to 50 tags with a job as long as they have unique tag keys\.
+ A tag key can be up to 128 Unicode characters in length, and tag values can be up to 256 Unicode characters in length\.
+ The key and values are case sensitive\.

For more information about tag restrictions, see [User\-Defined Tag Restrictions](https://docs.aws.amazon.com/awsaccountbilling/latest/aboutv2/allocation-tag-restrictions.html) in the *AWS Billing and Cost Management User Guide*\.

### API operations related to S3 Batch Operations job tagging<a name="batch-ops-job-tags-api"></a>

Amazon S3 supports the following API operations that are specific to S3 Batch Operations job tagging:
+ [GetJobTagging](https://docs.aws.amazon.com/AmazonS3/latest/API/API_control_GetJobTagging.html) — Returns the tag set associated with a Batch Operations job\. 
+ [PutJobTagging](https://docs.aws.amazon.com/AmazonS3/latest/API/API_control_PutJobTagging.html) — Replaces the tag set associated with a job\. There are two distinct scenarios for S3 Batch Operations job tag management using this API action:
  + Job has no tags — You can add a set of tags to a job \(the job has no prior tags\)\.
  + Job has a set of existing tags — To modify the existing tag set, you can either replace the existing tag set entirely, or make changes within the existing tag set by retrieving the existing tag set using [GetJobTagging](https://docs.aws.amazon.com/AmazonS3/latest/API/API_control_GetJobTagging.html), modify that tag set, and use this API action to replace the tag set with the one you have modified\.
**Note**  
If you send this request with an empty tag set, S3 Batch Operations deletes the existing tag set on the object\. If you use this method, you are charged for a Tier 1 Request \(PUT\)\. For more information, see [Amazon S3 pricing](https://aws.amazon.com/s3/pricing)\.  
To delete existing tags for your Batch Operations job, the `DeleteJobTagging` action is preferred because it achieves the same result without incurring charges\.
+ [DeleteJobTagging](https://docs.aws.amazon.com/AmazonS3/latest/API/API_control_DeleteJobTagging.html) — Deletes the tag set associated with a Batch Operations job\. 

## Assigning job priority<a name="batch-ops-job-priority"></a>

You can assign each job a numeric priority, which can be any positive integer\. S3 Batch Operations prioritize jobs according to the assigned priority\. Jobs with a higher priority \(or a higher numeric value for the priority parameter\) are evaluated first\. Priority is determined in descending order\. For example, a job queue with a priority value of 10 is given scheduling preference over a job queue with a priority value of 1\. 

You can change a job's priority while it is running\. If you submit a new job with a higher priority while a job is running, the lower\-priority job can pause to allow the higher\-priority job to run\.

**Note**  
S3 Batch Operations honor job priorities on a best\-effort basis\. Although jobs with higher priorities generally take precedence over jobs with lower priorities, Amazon S3 does not guarantee strict ordering of jobs\.

## Job status<a name="batch-ops-job-status"></a>

After you create a job, it progresses through a series of statuses\. The following table describes the statuses that jobs can have and the possible transitions between job statuses\.


****  

| Status | Description | Transitions | 
| --- | --- | --- | 
| New | A job begins in the New state when you create it\. | A job automatically moves to the Preparing state when Amazon S3 begins processing the manifest object\. | 
| Preparing | Amazon S3 is processing the manifest object and other job parameters to set up and run the job\. | A job automatically moves to the Ready state after Amazon S3 finishes processing the manifest and other parameters\. It is then ready to begin running the specified operation on the objects listed in the manifest\.If the job requires confirmation before running, such as when you create a job using the Amazon S3 console, then the job transitions from `Preparing` to `Suspended`\. It remains in the `Suspended` state until you confirm that you want to run it\. | 
| Suspended | The job requires confirmation, but you have not yet confirmed that you want to run it\. Only jobs that you create using the Amazon S3 console require confirmation\. A job that is created using the console enters the Suspended state immediately after Preparing\. After you confirm that you want to run the job and the job becomes Ready, it never returns to the Suspended state\. | After you confirm that you want to run the job, its status changes to Ready\. | 
| Ready | Amazon S3 is ready to begin running the requested object operations\. | A job automatically moves to Active when Amazon S3 begins to run it\. The amount of time that a job remains in the Ready state depends on whether you have higher\-priority jobs running already and how long those jobs take to complete\. | 
| Active | Amazon S3 is executing the requested operation on the objects listed in the manifest\. While a job is Active, you can monitor its progress using the Amazon S3 console or the DescribeJob operation through the REST API, AWS CLI, or AWS SDKs\. | A job moves out of the Active state when it is no longer running operations on objects\. This can happen automatically, such as when a job completes successfully or fails\. Or it can occur as a result of user actions, such as canceling a job\. The state that the job moves to depends on the reason for the transition\. | 
| Pausing | The job is transitioning to Paused from another state\. | A job automatically moves to Paused when the Pausing stage is finished\. | 
| Paused | A job can become Paused if you submit another job with a higher priority while the current job is running\. | A Paused job automatically returns to Active after any higher\-priority jobs that are blocking the job's' execution complete, fail, or are suspended\. | 
| Complete | The job has finished executing the requested operation on all objects in the manifest\. The operation might have succeeded or failed for each object\. If you configured the job to generate a completion report, the report is available as soon as the job is Complete\. | Complete is a terminal state\. Once a job reaches Complete, it does not transition to any other state\. | 
| Cancelling | The job is transitioning to the Cancelled state\. | A job automatically moves to Cancelled when the Cancelling stage is finished\. | 
| Cancelled | You requested that the job be cancelled, and S3 Batch Operations has successfully cancelled the job\. The job will not submit any new requests to Amazon S3\. | Cancelled is a terminal state\. After a job reaches Cancelled, it will not transition to any other state\. | 
| Failing | The job is transitioning to the Failed state\. | A job automatically moves to Failed once the Failing stage is finished\. | 
| Failed | The job has failed and is no longer running\. For more information about job failures, see [Tracking job failure](#batch-ops-job-status-failure)\. | Failed is a terminal state\. After a job reaches Failed, it will not transition to any other state\. | 

## Tracking job failure<a name="batch-ops-job-status-failure"></a>

If an S3 Batch Operations job encounters a problem that prevents it from running successfully, such as not being able to read the specified manifest, the job fails\. When a job fails, it generates one or more failure codes or failure reasons\. S3 Batch Operations store the failure codes and reasons with the job so that you can view them by requesting the job's details\. If you requested a completion report for the job, the failure codes and reasons also appear there\.

To prevent jobs from running a large number of unsuccessful operations, Amazon S3 imposes a task\-failure threshold on every Batch Operations job\. When a job has run at least 1,000 tasks, Amazon S3 monitors the task failure rate\. At any point, if the failure rate \(the number of tasks that have failed as a proportion of the total number of tasks that have run\) exceeds 50 percent, the job fails\. If your job fails because it exceeded the task\-failure threshold, you can identify the cause of the failures\. For example, you might have accidentally included some objects in the manifest that don't exist in the specified bucket\. After fixing the errors, you can resubmit the job\.

**Note**  
S3 Batch Operations operate asynchronously and the tasks don't necessarily run in the order that the objects are listed in the manifest\. Therefore, you can't use the manifest ordering to determine which objects' tasks succeeded and which ones failed\. Instead, you can examine the job's completion report \(if you requested one\) or view your AWS CloudTrail event logs to help determine the source of the failures\.

## Notifications and logging<a name="batch-ops-notifications"></a>

 In addition to requesting completion reports, you can also capture, review, and audit Batch Operations activity using AWS CloudTrail\. Because Batch Operations use existing Amazon S3 APIs to perform tasks, those tasks also emit the same events that they would if you called them directly\. Thus, you can track and record the progress of your job and all of its tasks using the same notification, logging, and auditing tools and processes that you already use with Amazon S3\. 

For more information about Amazon S3 events, see [ Configuring Amazon S3 event notifications](NotificationHowTo.md)\. 

## Completion reports<a name="batch-ops-completion-report"></a>

When you create a job, you can request a completion report\. As long as S3 Batch Operations successfully invoke at least one task, Amazon S3 generates a completion report after it finishes running tasks, fails, or is canceled\. You can configure the completion report to include all tasks or only failed tasks\. 

The completion report includes the job configuration and status and information for each task, including the object key and version, status, error codes, and descriptions of any errors\. Completion reports provide an easy way to view the results of your tasks in a consolidated format with no additional setup required\. For an example of a completion report, see [Example: Requesting S3 Batch Operations completion reports](batch-ops-examples-reports.md)\. 

If you don't configure a completion report, you can still monitor and audit your job and its tasks using CloudTrail and Amazon CloudWatch For more information, see [Example: Tracking an S3 Batch Operations job in Amazon EventBridge through AWS CloudTrail](batch-ops-examples-event-bridge-cloud-trail.md)\.