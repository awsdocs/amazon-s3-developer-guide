# Managing Batch Operations Jobs<a name="batch-ops-managing-jobs"></a>

[Sign up for the Preview](https://pages.awscloud.com/S3BatchOperations-Preview.html)

Amazon S3 provides a robust set of tools to help you manage your batch operations jobs after you have created them\. This section describes the operations you can use to manage your jobs\. You can perform all of the operations listed in this section using the AWS Management Console, AWS CLI, AWS SDKs, or REST APIs\.

**Topics**
+ [Listing Jobs](#batch-ops-list-jobs)
+ [Viewing Job Details](#batch-ops-job-details)
+ [Assigning Job Priority](#batch-ops-job-priority)
+ [Job Status](#batch-ops-job-status)
+ [Tracking Job Failure](#batch-ops-job-status-failure)
+ [Notifications and Logging](#batch-ops-notifications)
+ [Completion Reports](#batch-ops-completion-report)

## Listing Jobs<a name="batch-ops-list-jobs"></a>

You can retrieve a list of your batch operations jobs\. The list includes jobs that haven't yet finished and jobs that finished within the last 90 days\. The job list includes information for each job, such as its ID, description, priority, current status, and the number of tasks that have succeeded and failed\. You can filter your job list by status\. When you retrieve a job list through the console, you can also search your jobs by description or ID and filter them by AWS Region\.

## Viewing Job Details<a name="batch-ops-job-details"></a>

If you want more information about a job than you can retrieve by listing jobs, you can view all of the details for a single job\. In addition to the information returned in a job list, a single job's details include other items\. This information includes the operation parameters, details about the manifest, information about the completion report \(if you configured one when you created the job\), and the Amazon Resource Name \(ARN\) of the user role that you assigned to run the job\. By viewing an individual job's details, you can access a job's entire configuration\. 

## Assigning Job Priority<a name="batch-ops-job-priority"></a>

You can assign each job a numeric priority, which can be any positive integer\. Amazon S3 batch operations prioritize jobs according to the assigned priority\. Jobs with a higher priority \(or a higher numeric value for the priority parameter\) are evaluated first\. Priority is determined in descending order\. For example, a job queue with a priority value of 10 is given scheduling preference over a job queue with a priority value of 1\. 

You can change a job's priority while it is running\. If you submit a new job with a higher priority while a job is running, the lower\-priority job can pause to allow the higher\-priority job to run\.

**Note**  
Amazon S3 batch operations honor job priorities on a best\-effort basis\. Although jobs with higher priorities generally take precedence over jobs with lower priorities, Amazon S3 does not guarantee strict ordering of jobs\.

## Job Status<a name="batch-ops-job-status"></a>

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
| Cancelled | You requested that the job be cancelled, and Amazon S3 batch operations has successfully cancelled the job\. The job will not submit any new requests to Amazon S3\. | Cancelled is a terminal state\. After a job reaches Cancelled, it will not transition to any other state\. | 
| Failing | The job is transitioning to the Failed state\. | A job automatically moves to Failed once the Failing stage is finished\. | 
| Failed | The job has failed and is no longer running\. For more information about job failures, see [Tracking Job Failure](#batch-ops-job-status-failure)\. | Failed is a terminal state\. After a job reaches Failed, it will not transition to any other state\. | 

## Tracking Job Failure<a name="batch-ops-job-status-failure"></a>

If a batch operations job encounters a problem that prevents it from running successfully, such as not being able to read the specified manifest, the job fails\. When a job fails, it generates one or more failure codes or failure reasons\. Amazon S3 batch operations store the failure codes and reasons with the job so that you can view them by requesting the job's details\. If you requested a completion report for the job, the failure codes and reasons also appear there\.

To prevent jobs from running a large number of unsuccessful operations, Amazon S3 imposes a task\-failure threshold on every batch operations job\. When a job has executed at least 1,000 tasks, Amazon S3 monitors the task failure rate\. If, at any point, the failure rate \(the number of tasks that have failed as a proportion of the total number of tasks that have executed\) exceeds 50 percent, then the job fails\. If your job fails because it exceeded the task\-failure threshold, you can identify the cause of the failures\. For example, you might have accidentally included some objects in the manifest that don't exist in the specified bucket\. After fixing the errors, you can resubmit the job\.

**Note**  
Amazon S3 batch operations operate asynchronously and the tasks don't necessarily execute in the order that the objects are listed in the manifest\. Therefore, you can't use the manifest ordering to determine which objects' tasks succeeded and which ones failed\. Instead, you can examine the job's completion report \(if you requested one\) or view your AWS CloudTrail event logs to help determine the source of the failures\.

## Notifications and Logging<a name="batch-ops-notifications"></a>

In addition to requesting completion reports, you can also capture, review, and audit batch operations activity using Amazon S3 events\. As a job progresses, it emits events that you can capture using AWS CloudTrail, Amazon Simple Notification Service \(Amazon SNS\), and Amazon Simple Queue Service \(Amazon SQS\)\. Because batch operations use existing Amazon S3 APIs to perform tasks, those tasks also emit the same events that they would if you called them directly\. Thus, you can track and record the progress of your job and all of its tasks using the same notification, logging, and auditing tools and processes that you already use with Amazon S3\. For more information about Amazon S3 events, see [ Configuring Amazon S3 Event Notifications](NotificationHowTo.md)\. 

## Completion Reports<a name="batch-ops-completion-report"></a>

When you create a job, you can request a completion report\. Then as long as Amazon S3 batch operations successfully invoke at least one task, Amazon S3 generates a completion report after it finishes running tasks, fails, or is canceled\. You can configure the completion report to include all tasks or only failed tasks\. 

The completion report includes the job configuration and status and information for each task, including the object key and version, status, error codes, and descriptions of any errors\. If you don't configure a completion report, you can still monitor and audit your job and its tasks using CloudTrail, Amazon CloudWatch, Amazon SNS, and Amazon SQS\. However, completion reports provide an easy way to view the results of your tasks in a consolidated format with no additional setup required\.