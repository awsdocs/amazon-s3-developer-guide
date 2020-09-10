# Example: Using job tags to control permissions for S3 Batch Operations<a name="batch-ops-job-tags-examples"></a>

To help you manage your S3 Batch Operations jobs, you can add *job tags*\. With job tags, you can control access to your Batch Operations jobs and enforce that tags be applied when any job is created\. 

You can apply up to 50 job tags to each Batch Operations job\. This allows you to set very granular policies restricting the set of users that can edit the job\. Job tags can grant or limit a user’s ability to cancel a job, activate a job in the confirmation state, or change a job’s priority level\. In addition, you can enforce that tags be applied to all new jobs, and specify the allowed key\-value pairs for the tags\. You can express all of these conditions using the same [IAM policy language](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_iam-tags.html)\. For more information, see [Actions, resources, and condition keys for Amazon S3](list_amazons3.md)\.

The following example shows how you can use S3 Batch Operations job tags to grant users permission to create and edit only the jobs that are run within a specific *department* \(for example, the *Finance* or *Compliance* department\)\. You can also assign jobs based on the stage of *development* that they are related to, such as *QA* or *Production*\.

In this example, you use S3 Batch Operations job tags in AWS Identity and Access Management \(IAM\) policies to grant users permission to create and edit only the jobs being run within their department\. You assign jobs based on the stage of development that they are related to, such as *QA* or *Production*\. 

This example uses the following departments, with each using Batch Operations in different ways:
+ Finance
+ Compliance
+ Business Intelligence
+ Engineering

## Controlling access by assigning tags to users and resources<a name="job-tags-examples-attaching-tags"></a>

In this scenario, the administrators are using [attribute\-based access control \(ABAC\)](https://docs.aws.amazon.com/IAM/latest/UserGuide/introduction_attribute-based-access-control.html)\. ABAC is an IAM authorization strategy that defines permissions by attaching tags to both IAM users and AWS resources\.

Users and jobs are assigned one of the following department tags:

**Key : Value**
+ `department : Finance`
+ `department : Compliance`
+ `department : BusinessIntelligence`
+ `department : Engineering`
**Note**  
Job tag keys and values are case sensitive\.

Using the ABAC access control strategy, you grant a user in the Finance department permission to create and manage S3 Batch Operations jobs within their department by associating the tag `department=Finance` with their IAM user\.

Furthermore, you can attach a managed policy to the IAM user that allows any user in their company to create or modify S3 Batch Operations jobs within their respective departments\. 

The policy in this example includes three policy statements:
+ The first statement in the policy allows the user to create a Batch Operations job provided that the job creation request includes a job tag that matches their respective department\. This is expressed using the `"${aws:PrincipalTag/department}"` syntax, which is replaced by the IAM user’s department tag at policy evaluation time\. The condition is satisfied when the value provided for the department tag in the request `("aws:RequestTag/department")` matches the user’s department\. 
+ The second statement in the policy allows users to change the priority of jobs or update a job’s status provided that the job the user is updating matches the user’s department\. 
+ The third statement allows a user to update a Batch Operations job’s tags at any time via a `PutJobTagging` request as long as \(1\) their department tag is preserved and \(2\) the job they’re updating is within their department\. 

```
{
      "Version": "2012-10-17",
      "Statement": [
            {
                  "Effect": "Allow",
                  "Action": "s3:CreateJob",
                  "Resource": "*",
                  "Condition": {
                        "StringEquals": {
                              "aws:RequestTag/department": "${aws:PrincipalTag/department}"        
                }      
            }    
        },
            {
                  "Effect": "Allow",
                  "Action": [
                        "s3:UpdateJobPriority",
                        "s3:UpdateJobStatus"      
            ],
                  "Resource": "*",
                  "Condition": {
                        "StringEquals": {
                              "aws:ResourceTag/department": "${aws:PrincipalTag/department}"        
                }      
            }    
        },
            {
                  "Effect": "Allow",
                  "Action": "s3:PutJobTagging",
                  "Resource": "*",
                  "Condition": {
                        "StringEquals": {
                              "aws:RequestTag/department": "${aws:PrincipalTag/department}",
                              "aws:ResourceTag/department": "${aws:PrincipalTag/department}"        
                }      
            }    
        }  
    ]
}
```

## Tagging Batch Operations jobs by stage and enforcing limits on job priority<a name="tagging-jobs-by-stage-and-enforcing-limits-on-job-priority"></a>

All S3 Batch Operations jobs have a numeric priority, which Amazon S3 uses to decide in what order to run the jobs\. For this example, you restrict the maximum priority that most users can assign to jobs, with higher priority ranges reserved for a limited set of privileged users, as follows:
+ QA stage priority range \(low\): 1\-100
+ Production stage priority range \(high\): 1\-300

To do this, introduce a new tag set representing the *stage* of the job:

**Key : Value**
+ `stage : QA`
+ `stage : Production`

### Creating and updating low\-priority jobs within a department<a name="creating-and-updating-low-priority-jobs"></a>

This policy introduces two new restrictions on S3 Batch Operations job creation and update, in addition to the department\-based restriction:
+ It allows users to create or update jobs in their department with a new condition that requires the job to include the tag `stage=QA`\.
+ It allows users to create or update a job’s priority up to a new maximum priority of 100\.

```
{
        "Version": "2012-10-17",
        "Statement": [
        {
        "Effect": "Allow",
        "Action": "s3:CreateJob",
        "Resource": "*",
        "Condition": {
            "StringEquals": {
                "aws:RequestTag/department": "${aws:PrincipalTag/department}",
                "aws:RequestTag/stage": "QA"
            },
            "NumericLessThanEquals": {
                "s3:RequestJobPriority": 100
            }
        }
    },
    {
        "Effect": "Allow",
        "Action": [
            "s3:UpdateJobStatus"
        ],
        "Resource": "*",
        "Condition": {
            "StringEquals": {
                "aws:ResourceTag/department": "${aws:PrincipalTag/department}"
            }
        }
    },
    {
        "Effect": "Allow",
        "Action": "s3:UpdateJobPriority",
        "Resource": "*",
        "Condition": {
            "StringEquals": {
                "aws:ResourceTag/department": "${aws:PrincipalTag/department}",
                "aws:ResourceTag/stage": "QA"
            },
            "NumericLessThanEquals": {
                "s3:RequestJobPriority": 100
            }
        }
    },
    {
        "Effect": "Allow",
        "Action": "s3:PutJobTagging",
        "Resource": "*",
        "Condition": {
            "StringEquals": {
                "aws:RequestTag/department" : "${aws:PrincipalTag/department}",
                "aws:ResourceTag/department": "${aws:PrincipalTag/department}",
                "aws:RequestTag/stage": "QA",
                "aws:ResourceTag/stage": "QA"
            }
        }
    },
    {
        "Effect": "Allow",
        "Action": "s3:GetJobTagging",
        "Resource": "*"
    }
    ]
}
```

### Creating and updating high\-priority jobs within a department<a name="creating-and-updating-high-priority-jobs"></a>

A small number of users might require the ability to create high priority jobs in either *QA* or *Production*\. To support this need, you create a managed policy that’s adapted from the low\-priority policy in the previous section\. 

This policy does the following: 
+ Allows users to create or update jobs in their department with either the tag `stage=QA` or `stage=Production`\.
+ Allows users to create or update a job’s priority up to a maximum of 300\.

```
{
      "Version": "2012-10-17",
      "Statement": [
          {
                "Effect": "Allow",
                "Action": "s3:CreateJob",
                "Resource": "*",
                "Condition": {
                      "ForAnyValue:StringEquals": {
                            "aws:RequestTag/stage": [
                                  "QA",
                                  "Production"        
                    ]      
                },
                      "StringEquals": {
                            "aws:RequestTag/department": "${aws:PrincipalTag/department}"      
                },
                      "NumericLessThanEquals": {
                            "s3:RequestJobPriority": 300      
                }    
            }  
        },
          {
                "Effect": "Allow",
                "Action": [
                      "s3:UpdateJobStatus"    
            ],
                "Resource": "*",
                "Condition": {
                      "StringEquals": {
                            "aws:ResourceTag/department": "${aws:PrincipalTag/department}"      
                }    
            }  
        },
          {
                "Effect": "Allow",
                "Action": "s3:UpdateJobPriority",
                "Resource": "*",
                "Condition": {
                      "ForAnyValue:StringEquals": {
                            "aws:ResourceTag/stage": [
                                  "QA",
                                  "Production"        
                    ]      
                },
                      "StringEquals": {
                            "aws:ResourceTag/department": "${aws:PrincipalTag/department}"      
                },
                      "NumericLessThanEquals": {
                            "s3:RequestJobPriority": 300      
                }    
            }  
        },
          {
                "Effect": "Allow",
                "Action": "s3:PutJobTagging",
                "Resource": "*",
                "Condition": {
                      "StringEquals": {
                            "aws:RequestTag/department": "${aws:PrincipalTag/department}",
                            "aws:ResourceTag/department": "${aws:PrincipalTag/department}"      
                },
                      "ForAnyValue:StringEquals": {
                            "aws:RequestTag/stage": [
                                  "QA",
                                  "Production"        
                    ],
                            "aws:ResourceTag/stage": [
                                  "QA",
                                  "Production"        
                    ]      
                }    
            }  
        }  
    ]
}
```