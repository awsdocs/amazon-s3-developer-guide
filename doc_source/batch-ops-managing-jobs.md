# Managing Jobs<a name="batch-ops-managing-jobs"></a>

[Sign up for the Preview](https://pages.awscloud.com/S3BatchOperations-Preview.html)

Amazon S3 Batch Operations provides a robust set of tools to help you manage your jobs after you have created them\. This section describes the operations you can use to manage your jobs\. You can perform all of the operations listed in this section using the AWS Management Console, AWS CLI, AWS SDKs, or REST APIs\.

## Listing Jobs<a name="batch-ops-list-jobs"></a>

You can retrieve a list of the jobs that you've created that haven't yet finished, as well as jobs that have finished within the last 30 days\. The job list includes information for each job such as its ID, description, priority, current status, and the number of tasks that have succeeded and failed\. You can filter your job list by status, and when you retrieve a job list through the AWS Management Console, you can also search your jobs by description or ID and filter them by region\.

## Viewing Job Details<a name="batch-ops-job-details"></a>

If you want more information about a job than you can retrieve by listing jobs, you can view all of the details for a single job\. Besides the information returned in a job list, a single job's details include items such as the operation parameters, details about the manifest, information about the completion report \(if you configured one when you created the job\), and the ARN of the user role that you assigned to run the job\. By viewing an individual job's details, you can access a job's entire configuration\. You can retrieve a job's details from the time you create it until 30 days after it finishes\. If you need to store a job's details for a longer period of time, you can easily do so by requesting a completion report when you create the job\.

## Job Priority<a name="batch-ops-job-priority"></a>

You can assign each job a numeric priority\. Amazon S3 Batch Operations prioritizes jobs according to the assigned priority\. The maximum priority is over 2 billion, enabling you to exercise fine\-grained control over your jobs' prioritization\. You can change a job's priority while it is running, and if you submit a new job with a higher priority while a job is running, the lower\-priority job can pause to allow the higher\-priority job to run\.

**Note**  
Amazon S3 Batch Operations honors job priorities on a best\-effort basis\. Although jobs with higher priorities generally will take precedence over jobs with lower priorities, Batch Operations doesn't guarantee strict ordering of jobs\.

## Job Failure<a name="batch-ops-job-status-failure"></a>

If a batch operations job encounters a problem that prevents it from running successfully, such as not being able to read the specified manifest, it fails\. When a job fails, it generates one or more failure codes or failure reasons\. Amazon S3 Batch Operations stores the failure codes and reasons with the job so that you can view them by requesting the job's details\. If you requested a completion report for the job, the failure codes and reasons also appear there\.

In order to prevent jobs from running a large number of unsuccessful operations, Amazon S3 Batch Operations imposes a task\-failure threshold on every job\. Once a job has executed at least 1000 tasks, Amazon S3 Batch Operations monitors the task failure rate\. If, at any point, the failure rate \(the number of tasks that have failed as a proportion of the total number of tasks that have executed\) exceeds 50%, then the job fails\. If your job fails because it exceeded the task\-failure threshold, you can identify the cause of the failures—for example, you might have accidentally included some objects in the manifest that don't exist in the specified bucket—and resubmit the job once you have fixed the errors\.

**Note**  
Because Amazon S3 Batch Operations operates asynchronously and doesn't necessarily execute tasks in the order that the objects are listed in the manifest, you can't use the manifest ordering to determine which objects' tasks succeeded and which ones failed\. Instead, you can examine the job's completion report \(if you requested one\) or view your AWS CloudTrail event logs to help determine the source of the failures\.

## Notifications and Logging<a name="batch-ops-notifications"></a>

In addition to providing completion reports, Amazon S3 Batch Operations also enables you to capture, review, and audit its activity using S3 events\. As a job progresses, it emits events that you can capture using AWS CloudTrail, Amazon SNS, and Amazon SQS\. Because S3 Batch Operations uses existing S3 APIs to perform tasks, those tasks also emit the same events that they would if you called them directly\. Thus, you can track and record the progress of your job and all of its tasks using the same notification, logging, and auditing tools and processes that you already use with Amazon S3\. For more information about S3 events, see [ Configuring Amazon S3 Event Notifications](NotificationHowTo.md)\. 

## Completion Reports<a name="batch-ops-completion-report"></a>

When you create a job, you can request a completion report\. If you request a completion report, then as long as Amazon S3 Batch Operations successfully invokes at least one task, it generates a completion report after it finishes running tasks, fails, or is cancelled\. You can configure the completion report to include all tasks or only failed tasks\. The completion report includes the job configuration and status and information for each task including the object key and version, status, error codes, and descriptions of any errors\. If you don't configure a completion report, you can still monitor and audit your job and its tasks using CloudTrail, CloudWatch, SNS, and SQS, but completion reports provide an easy way to view the results of your tasks in a consolidated format with no additional setup required\.