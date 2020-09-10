# S3 Batch Operations examples using the AWS CLI<a name="batch-ops-examples-cli"></a>

S3 Batch Operations track progress, send notifications, and store a detailed completion report of all actions, providing a fully managed, auditable, serverless experience\. You can use S3 Batch Operations through the AWS Management Console, AWS CLI, AWS SDKs, or REST API\. For more information, see [The basics: S3 Batch Operations](batch-ops-basics.md)\.

The following examples show how you can use S3 Batch Operations with the AWS Command Line Interface \(AWS CLI\)\.

**Topics**
+ [Creating and managing S3 Batch Operations jobs](#batch-ops-example-cli-create)
+ [Managing tags on S3 Batch Operations jobs](#batch-ops-example-cli-job-tags)
+ [Using S3 Batch Operations with S3 Object Lock](#batchops-example-cli-object-lock)

## Creating and managing S3 Batch Operations jobs<a name="batch-ops-example-cli-create"></a>

You can use the AWS CLI to create and manage your S3 Batch Operations jobs\. You can get a description of a Batch Operations job, update its status or priority, and find out which jobs are *Active* and *Complete*\. 

**Topics**
+ [Create an S3 Batch Operations job](#batch-ops-example-cli-job-create)
+ [Get the description of an S3 Batch Operations job](#batch-ops-example-cli-job-description)
+ [Get a list of Active and Complete jobs](#batch-ops-example-cli-active-jobs)
+ [Update the job priority](#batch-ops-example-cli-update-job-priority)
+ [Update the job status](#batch-ops-example-cli-update-job-status)

### Create an S3 Batch Operations job<a name="batch-ops-example-cli-job-create"></a>

The following example creates an S3 Batch Operations `S3PutObjectTagging` job using the AWS CLI\. 

**To create a Batch Operations `S3PutObjectTagging` job**

1. Create an AWS Identity and Access Management \(IAM\) role, and assign permissions\. This role grants Amazon S3 permission to add object tags, for which you create a job in the next step\.

   1. Create an IAM role as follows\.

      ```
      aws iam create-role \
       --role-name S3BatchJobRole \
       --assume-role-policy-document '{
         "Version":"2012-10-17",
         "Statement":[
            {
               "Effect":"Allow",
               "Principal":{
                  "Service":"batchoperations.s3.amazonaws.com"
               },
               "Action":"sts:AssumeRole"
            }
         ]
      }'
      ```

      Record the role's Amazon Resource Name \(ARN\)\. You need the ARN when you create a job\.

   1. Create an IAM policy with permissions, and attach it to the IAM role that you created in the previous step\. For more information about permissions, see [Granting permissions for Amazon S3 Batch Operations](batch-ops-iam-role-policies.md)\.

      ```
      aws iam put-role-policy \
        --role-name S3BatchJobRole \
        --policy-name PutObjectTaggingBatchJobPolicy \
        --policy-document '{
        "Version":"2012-10-17",
        "Statement":[
          {
            "Effect":"Allow",
            "Action":[
              "s3:PutObjectTagging",
              "s3:PutObjectVersionTagging"
            ],
            "Resource": "arn:aws:s3:::{{TargetResource}}/*"
          },
          {
            "Effect": "Allow",
            "Action": [
              "s3:GetObject",
              "s3:GetObjectVersion",
              "s3:GetBucketLocation"
            ],
            "Resource": [
              "arn:aws:s3:::{{ManifestBucket}}/*"
            ]
          },
          {
            "Effect":"Allow",
            "Action":[
              "s3:PutObject",
              "s3:GetBucketLocation"
            ],
            "Resource":[
              "arn:aws:s3:::{{ReportBucket}}/*"
            ]
          }
        ]
      }'
      ```

1. Create an `S3PutObjectTagging` job\. 

   The `manifest.csv` file provides a list of bucket and object key values\. The job applies the specified tags to objects identified in the manifest\. The `ETag` is the ETag of the `manifest.csv` object, which you can get from the Amazon S3 console\. The request specifies the `no-confirmation-required` parameter\. Therefore, Amazon S3 makes the job eligible for execution without you having to confirm it using the `udpate-job-status` command\.

   ```
   aws s3control create-job \
       --region us-west-2 \
       --account-id acct-id \
       --operation '{"S3PutObjectTagging": { "TagSet": [{"Key":"keyOne", "Value":"ValueOne"}] }}' \
       --manifest '{"Spec":{"Format":"S3BatchOperations_CSV_20180820","Fields":["Bucket","Key"]},"Location":{"ObjectArn":"arn:aws:s3:::my_manifests/manifest.csv","ETag":"60e460c9d1046e73f7dde5043ac3ae85"}}' \
       --report '{"Bucket":"arn:aws:s3:::bucket-where-completion-report-goes","Prefix":"final-reports", "Format":"Report_CSV_20180820","Enabled":true,"ReportScope":"AllTasks"}' \
       --priority 42 \
       --role-arn IAM-role \
       --client-request-token $(uuidgen) \
       --description "job Description" \
       --no-confirmation-required
   ```

   In response, Amazon S3 returns a job ID \(for example, `00e123a4-c0d8-41f4-a0eb-b46f9ba5b07c`\)\. You need the ID in the next commands\.

### Get the description of an S3 Batch Operations job<a name="batch-ops-example-cli-job-description"></a>

The following example gets the description of an S3 Batch Operations job using the AWS CLI\. 

```
aws s3control describe-job \
    --region us-west-2 \
    --account-id acct-id \
    --job-id 00e123a4-c0d8-41f4-a0eb-b46f9ba5b07c
```

### Get a list of Active and Complete jobs<a name="batch-ops-example-cli-active-jobs"></a>

The following AWS CLI example gets a list of `Active` and `Complete` jobs\. 

```
aws s3control list-jobs \
    --region us-west-2 \
    --account-id acct-id \
    --job-statuses '["Active","Complete"]' \
    --max-results 20
```

### Update the job priority<a name="batch-ops-example-cli-update-job-priority"></a>

The following example updates the job priority using the AWS CLI\. A higher number indicates a higher execution priority\.

```
aws s3control update-job-priority \
    --region us-west-2 \
    --account-id acct-id \
    --priority 98 \
    --job-id 00e123a4-c0d8-41f4-a0eb-b46f9ba5b07c
```

### Update the job status<a name="batch-ops-example-cli-update-job-status"></a>
+ If you didn't specify the `--no-confirmation-required` parameter in the previous `create-job` example, the job remains in a suspended state until you confirm the job by setting its status to `Ready`\. Amazon S3 then makes the job eligible for execution\.

  ```
  aws s3control update-job-status \
      --region us-west-2 \
      --account-id 181572960644 \
      --job-id 00e123a4-c0d8-41f4-a0eb-b46f9ba5b07c \
      --requested-job-status 'Ready'
  ```
+ Cancel the job by setting the job status to `Cancelled`\.

  ```
  aws s3control update-job-status \
       --region us-west-2 \
       --account-id 181572960644 \
       --job-id 00e123a4-c0d8-41f4-a0eb-b46f9ba5b07c \
       --status-update-reason "No longer needed" \
       --requested-job-status Cancelled
  ```

## Managing tags on S3 Batch Operations jobs<a name="batch-ops-example-cli-job-tags"></a>

You can label and control access to your S3 Batch Operations jobs by adding *tags*\. Tags can be used to identify who is responsible for a Batch Operations job\. You can create jobs with tags attached to them, and you can add tags to jobs after they are created\. For more information, see [Controlling access and labeling jobs using tags](batch-ops-managing-jobs.md#batch-ops-job-tags)\.

**Topics**
+ [Create a Batch Operations job with tags](#batch-ops-example-cli-job-tags-create-job)
+ [Delete the tags from a Batch Operations job](#batch-ops-example-cli-job-tags-delete-job-tagging)
+ [Get the tags of a Batch Operations job](#batch-ops-example-cli-job-tags-get-job-tagging)
+ [Put tags in a Batch Operations job](#batch-ops-example-cli-job-tags-put-job-tagging)

### Create an S3 Batch Operations job with tags<a name="batch-ops-example-cli-job-tags-create-job"></a>

The following AWS CLI example creates an S3 Batch Operations `S3PutObjectCopy` job using job tags as labels for the job\. 

1. Select the action or `OPERATION` that you want the Batch Operations job to perform, and choose your `TargetResource`\.

   ```
   read -d '' OPERATION <<EOF
   {
     "S3PutObjectCopy": {
       "TargetResource": "arn:aws:s3:::destination-bucket"
     }
   }
   EOF
   ```

1. Identify the job `TAGS` that you want for the job\. In this case, you apply two tags, `department` and `FiscalYear`, with the values `Marketing` and `2020` respectively\.

   ```
   read -d '' TAGS <<EOF
   [
     {
       "Key": "department",
       "Value": "Marketing"
     },
     {
       "Key": "FiscalYear",
       "Value": "2020"
     }
   ]
   EOF
   ```

1. Specify the `MANIFEST` for the Batch Operations job\.

   ```
   read -d '' MANIFEST <<EOF
   {
     "Spec": {
       "Format": "EXAMPLE_S3BatchOperations_CSV_20180820",
       "Fields": [
         "Bucket",
         "Key"
       ]
     },
     "Location": {
       "ObjectArn": "arn:aws:s3:::example-bucket/example_manifest.csv",
       "ETag": "example-5dc7a8bfb90808fc5d546218"
     }
   }
   EOF
   ```

1. Configure the `REPORT` for the Batch Operations job\.

   ```
   read -d '' REPORT <<EOF
   {
     "Bucket": "arn:aws:s3:::example-report-bucket",
     "Format": "Example_Report_CSV_20180820",
     "Enabled": true,
     "Prefix": "reports/copy-with-replace-metadata",
     "ReportScope": "AllTasks"
   }
   EOF
   ```

1. Run the`create-job` action to create your Batch Operations job with inputs set in the preceding steps\.

   ```
   aws \
       s3control create-job \
       --account-id 123456789012 \
       --manifest "${MANIFEST//$'\n'}" \
       --operation "${OPERATION//$'\n'/}" \
       --report "${REPORT//$'\n'}" \
       --priority 10 \
       --role-arn arn:aws:iam::123456789012:role/batch-operations-role \
       --tags "${TAGS//$'\n'/}" \
       --client-request-token "$(uuidgen)" \
       --region us-west-2 \
       --description "Copy with Replace Metadata";
   ```

### Delete the tags from an S3 Batch Operations job<a name="batch-ops-example-cli-job-tags-delete-job-tagging"></a>

The following example deletes the tags from a Batch Operations job using the AWS CLI\.

```
aws \
    s3control delete-job-tagging \
    --account-id 123456789012 \
    --job-id Example-e25a-4ed2-8bee-7f8ed7fc2f1c \
    --region us-east-1;
```

### Get the job tags of an S3 Batch Operations job<a name="batch-ops-example-cli-job-tags-get-job-tagging"></a>

The following example gets the tags of a Batch Operations job using the AWS CLI\.

```
aws \
    s3control get-job-tagging \
    --account-id 123456789012 \
    --job-id Example-e25a-4ed2-8bee-7f8ed7fc2f1c \
    --region us-east-1;
```

### Put job tags in an existing S3 Batch Operations job<a name="batch-ops-example-cli-job-tags-put-job-tagging"></a>

The following is an example of using `s3control put-job-tagging` to add job tags to your S3 Batch Operations job using the AWS CLI\.

**Note**  
If you send this request with an empty tag set, S3 Batch Operations deletes the existing tag set on the object\. Also, if you use this method, you are charged for a Tier 1 Request \(PUT\)\. For more information, see [Amazon S3 pricing](https://aws.amazon.com/s3/pricing)\.  
To delete existing tags for your Batch Operations job, the `DeleteJobTagging` action is preferred because it achieves the same result without incurring charges\.

1. Identify the job `TAGS` that you want for the job\. In this case, you apply two tags, `department` and `FiscalYear`, with the values `Marketing` and `2020` respectively\.

   ```
   read -d '' TAGS <<EOF
   [
     {
       "Key": "department",
       "Value": "Marketing"
     },
     {
       "Key": "FiscalYear",
       "Value": "2020"
     }
   ]
   EOF
   ```

1. Run the `put-job-tagging` action with the required parameters\.

   ```
   aws \
       s3control put-job-tagging \
       --account-id 123456789012 \
       --tags "${TAGS//$'\n'/}" \
       --job-id Example-e25a-4ed2-8bee-7f8ed7fc2f1c \
       --region us-east-1;
   ```

## Using S3 Batch Operations with S3 Object Lock<a name="batchops-example-cli-object-lock"></a>

You can use S3 Batch Operations with S3 Object Lock to manage retention or enable a legal hold for many Amazon S3 objects at once\. You specify the list of target objects in your manifest and submit it to Batch Operations for completion\. For more information, see [Managing S3 Object Lock retention dates](batch-ops-retention-date.md) and [Managing S3 Object Lock legal hold](batch-ops-legal-hold.md)\. 

The following examples show how to create an IAM role with S3 Batch Operations permissions and update the role permissions to create jobs that enable Object Lock using the AWS CLI\. In the examples, replace any variable values with those that suit your needs\. You must also have a `CSV` manifest identifying the objects for your S3 Batch Operations job\. For more information, see [Specifying a manifest](batch-ops-basics.md#specify-batchjob-manifest)\.

In this section, you perform two steps:

1. Create an IAM role and assign S3 Batch Operations permissions to run\.

   This step is required for all S3 Batch Operations jobs\.

   ```
   export AWS_PROFILE='aws-user'
   
   read -d '' bops_trust_policy <<EOF
   {
     "Version": "2012-10-17", 
     "Statement": [ 
       { 
         "Effect": "Allow", 
         "Principal": { 
           "Service": [
             "batchoperations.s3.amazonaws.com"
           ]
         }, 
         "Action": "sts:AssumeRole" 
       } 
     ]
   }
   EOF
   aws iam create-role --role-name bops-objectlock --assume-role-policy-document "${bops_trust_policy}"
   ```

1. Set up S3 Batch Operations with S3 Object Lock to run\.

   In this step, you allow the role to do the following:

   1. Run Object Lock on the S3 bucket that contains the target objects that you want Batch Operations to run on\.

   1. Read the S3 bucket where the manifest CSV file and the objects are located\.

   1. Write the results of the S3 Batch Operations job to the reporting bucket\.

   ```
   read -d '' bops_permissions <<EOF
   {
       "Version": "2012-10-17",
       "Statement": [
           {
               "Effect": "Allow",
               "Action": "s3:GetBucketObjectLockConfiguration",
               "Resource": [
                   "arn:aws:s3:::{{ManifestBucket}}"
               ]
           },
           {
               "Effect": "Allow",
               "Action": [
                   "s3:GetObject",
                   "s3:GetObjectVersion",
                   "s3:GetBucketLocation"
               ],
               "Resource": [
                   "arn:aws:s3:::{{ManifestBucket}}/*"
               ]
           },
           {
               "Effect": "Allow",
               "Action": [
                   "s3:PutObject",
                   "s3:GetBucketLocation"
               ],
               "Resource": [
                   "arn:aws:s3:::{{ReportBucket}}/*"
               ]
           }
       ]
   }
   EOF
   
   aws iam put-role-policy --role-name bops-objectlock --policy-name object-lock-permissions --policy-document "${bops_permissions}"
   ```

**Topics**
+ [Use S3 Batch Operations to set S3 Object Lock retention](#batch-ops-cli-object-lock-retention-example)
+ [Use S3 Batch Operations to turn off S3 Object Lock legal hold](#batch-ops-cli-object-lock-legalhold-example)

### Use S3 Batch Operations to set S3 Object Lock retention<a name="batch-ops-cli-object-lock-retention-example"></a>

The following example allows the rule to set S3 Object Lock retention for your objects in the manifest bucket\.

 You update the role to include `s3:PutObjectRetention` permissions so that you can run Object Lock retention on the objects in your bucket\.

```
export AWS_PROFILE='aws-user'

read -d '' retention_permissions <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "s3:PutObjectRetention"
            ],
            "Resource": [
                "arn:aws:s3:::{{ManifestBucket}}/*"
            ]
        }
    ]
}
EOF

aws iam put-role-policy --role-name bops-objectlock --policy-name retention-permissions --policy-document "${retention_permissions}"
```

#### Use S3 Batch Operations with S3 Object Lock retention compliance mode<a name="batch-ops-cli-object-lock-compliance-example"></a>

The following example builds on the previous examples of creating a trust policy, and setting S3 Batch Operations and S3 Object Lock configuration permissions on your objects\. This example sets the retention mode to `COMPLIANCE` and the `retain until date` to January 1, 2020\. It creates a job that targets objects in the manifest bucket and reports the results in the reports bucket that you identified\.

```
export AWS_PROFILE='aws-user'
export AWS_DEFAULT_REGION='us-west-2'
export ACCOUNT_ID=123456789012
export ROLE_ARN='arn:aws:iam::123456789012:role/bops-objectlock'

read -d '' OPERATION <<EOF
{
  "S3PutObjectRetention": {
    "Retention": {
      "RetainUntilDate":"Jan 1 00:00:00 PDT 2020",
      "Mode":"COMPLIANCE"
    }
  }
}
EOF

read -d '' MANIFEST <<EOF
{
  "Spec": {
    "Format": "S3BatchOperations_CSV_20180820",
    "Fields": [
      "Bucket",
      "Key"
    ]
  },
  "Location": {
    "ObjectArn": "arn:aws:s3:::ManifestBucket/complaince-objects-manifest.csv",
    "ETag": "Your-manifest-ETag"
  }
}
EOF

read -d '' REPORT <<EOF
{
  "Bucket": "arn:aws:s3:::ReportBucket",
  "Format": "Report_CSV_20180820",
  "Enabled": true,
  "Prefix": "reports/compliance-objects-bops",
  "ReportScope": "AllTasks"
}
EOF

aws \
    s3control create-job \
    --account-id "${ACCOUNT_ID}" \
    --manifest "${MANIFEST//$'\n'}" \
    --operation "${OPERATION//$'\n'/}" \
    --report "${REPORT//$'\n'}" \
    --priority 10 \
    --role-arn "${ROLE_ARN}" \
    --client-request-token "$(uuidgen)" \
    --region "${AWS_DEFAULT_REGION}" \
    --description "Set compliance retain-until to 1 Jul 2030";
```

The following example extends the `COMPLIANCE` mode's `retain until date` to January 15, 2020\.

```
export AWS_PROFILE='aws-user'
export AWS_DEFAULT_REGION='us-west-2'
export ACCOUNT_ID=123456789012
export ROLE_ARN='arn:aws:iam::123456789012:role/bops-objectlock'

read -d '' OPERATION <<EOF
{
  "S3PutObjectRetention": {
    "Retention": {
      "RetainUntilDate":"Jan 15 00:00:00 PDT 2020",
      "Mode":"COMPLIANCE"
    }
  }
}
EOF

read -d '' MANIFEST <<EOF
{
  "Spec": {
    "Format": "S3BatchOperations_CSV_20180820",
    "Fields": [
      "Bucket",
      "Key"
    ]
  },
  "Location": {
    "ObjectArn": "arn:aws:s3:::ManifestBucket/complaince-objects-manifest.csv",
    "ETag": "Your-manifest-ETag"
  }
}
EOF

read -d '' REPORT <<EOF
{
  "Bucket": "arn:aws:s3:::ReportBucket",
  "Format": "Report_CSV_20180820",
  "Enabled": true,
  "Prefix": "reports/compliance-objects-bops",
  "ReportScope": "AllTasks"
}
EOF

aws \
    s3control create-job \
    --account-id "${ACCOUNT_ID}" \
    --manifest "${MANIFEST//$'\n'}" \
    --operation "${OPERATION//$'\n'/}" \
    --report "${REPORT//$'\n'}" \
    --priority 10 \
    --role-arn "${ROLE_ARN}" \
    --client-request-token "$(uuidgen)" \
    --region "${AWS_DEFAULT_REGION}" \
    --description "Extend compliance retention to 15 Jan 2020";
```

#### Use S3 Batch Operations with S3 Object Lock retention governance mode<a name="batch-ops-cli-object-lock-governance-example"></a>

The following example builds on the previous example of creating a trust policy, and setting S3 Batch Operations and S3 Object Lock configuration permissions\. It shows how to apply S3 Object Lock retention governance with the `retain until date` of January 30, 2020, across multiple objects\. It creates a Batch Operations job that uses the manifest bucket and reports the results in the reports bucket\.

```
export AWS_PROFILE='aws-user'
export AWS_DEFAULT_REGION='us-west-2'
export ACCOUNT_ID=123456789012
export ROLE_ARN='arn:aws:iam::123456789012:role/bops-objectlock'

read -d '' OPERATION <<EOF
{
  "S3PutObjectRetention": {
    "Retention": {
      "RetainUntilDate":"Jan 30 00:00:00 PDT 2020",
      "Mode":"GOVERNANCE"
    }
  }
}
EOF

read -d '' MANIFEST <<EOF
{
  "Spec": {
    "Format": "S3BatchOperations_CSV_20180820",
    "Fields": [
      "Bucket",
      "Key"
    ]
  },
  "Location": {
    "ObjectArn": "arn:aws:s3:::ManifestBucket/governance-objects-manifest.csv",
    "ETag": "Your-manifest-ETag"
  }
}
EOF

read -d '' REPORT <<EOF
{
  "Bucket": "arn:aws:s3:::ReportBucketT",
  "Format": "Report_CSV_20180820",
  "Enabled": true,
  "Prefix": "reports/governance-objects",
  "ReportScope": "AllTasks"
}
EOF

aws \
    s3control create-job \
    --account-id "${ACCOUNT_ID}" \
    --manifest "${MANIFEST//$'\n'}" \
    --operation "${OPERATION//$'\n'/}" \
    --report "${REPORT//$'\n'}" \
    --priority 10 \
    --role-arn "${ROLE_ARN}" \
    --client-request-token "$(uuidgen)" \
    --region "${AWS_DEFAULT_REGION}" \
    --description "Put governance retention";
```

The following example builds on the previous example of creating a trust policy, and setting S3 Batch Operations and S3 Object Lock configuration permissions\. It shows how to bypass retention governance across multiple objects and creates a Batch Operations job that uses the manifest bucket and reports the results in the reports bucket\.

```
export AWS_PROFILE='aws-user'

read -d '' bypass_governance_permissions <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "s3:BypassGovernanceRetention"
            ],
            "Resource": [
                "arn:aws:s3:::ManifestBucket/*"
            ]
        }
    ]
}
EOF

aws iam put-role-policy --role-name bops-objectlock --policy-name bypass-governance-permissions --policy-document "${bypass_governance_permissions}"

export AWS_PROFILE='aws-user'
export AWS_DEFAULT_REGION='us-west-2'
export ACCOUNT_ID=123456789012
export ROLE_ARN='arn:aws:iam::123456789012:role/bops-objectlock'

read -d '' OPERATION <<EOF
{
  "S3PutObjectRetention": {
    "BypassGovernanceRetention": true,
    "Retention": {
    }
  }
}
EOF

read -d '' MANIFEST <<EOF
{
  "Spec": {
    "Format": "S3BatchOperations_CSV_20180820",
    "Fields": [
      "Bucket",
      "Key"
    ]
  },
  "Location": {
    "ObjectArn": "arn:aws:s3:::ManifestBucket/governance-objects-manifest.csv",
    "ETag": "Your-manifest-ETag"
  }
}
EOF

read -d '' REPORT <<EOF
{
  "Bucket": "arn:aws:s3:::REPORT_BUCKET",
  "Format": "Report_CSV_20180820",
  "Enabled": true,
  "Prefix": "reports/bops-governance",
  "ReportScope": "AllTasks"
}
EOF

aws \
    s3control create-job \
    --account-id "${ACCOUNT_ID}" \
    --manifest "${MANIFEST//$'\n'}" \
    --operation "${OPERATION//$'\n'/}" \
    --report "${REPORT//$'\n'}" \
    --priority 10 \
    --role-arn "${ROLE_ARN}" \
    --client-request-token "$(uuidgen)" \
    --region "${AWS_DEFAULT_REGION}" \
    --description "Remove governance retention";
```

### Use S3 Batch Operations to turn off S3 Object Lock legal hold<a name="batch-ops-cli-object-lock-legalhold-example"></a>

The following example builds on the previous examples of creating a trust policy, and setting S3 Batch Operations and S3 Object Lock configuration permissions\. It shows how to disable Object Lock legal hold on objects using Batch Operations\. 

The example first updates the role to grant `s3:PutObjectLegalHold` permissions, creates a Batch Operations job that turns off \(removes\) legal hold from the objects identified in the manifest, and then reports on it\.

```
export AWS_PROFILE='aws-user'

read -d '' legal_hold_permissions <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "s3:PutObjectLegalHold"
            ],
            "Resource": [
                "arn:aws:s3:::ManifestBucket/*"
            ]
        }
    ]

EOF

aws iam put-role-policy --role-name bops-objectlock --policy-name legal-hold-permissions --policy-document "${legal_hold_permissions}"
```

The following example turns off legal hold\.

```
export AWS_PROFILE='aws-user'
export AWS_DEFAULT_REGION='us-west-2'
export ACCOUNT_ID=123456789012
export ROLE_ARN='arn:aws:iam::123456789012:role/bops-objectlock'

read -d '' OPERATION <<EOF
{
  "S3PutObjectLegalHold": {
    "LegalHold": {
      "Status":"OFF"
    }
  }
}
EOF

read -d '' MANIFEST <<EOF
{
  "Spec": {
    "Format": "S3BatchOperations_CSV_20180820",
    "Fields": [
      "Bucket",
      "Key"
    ]
  },
  "Location": {
    "ObjectArn": "arn:aws:s3:::ManifestBucket/legalhold-object-manifest.csv",
    "ETag": "Your-manifest-ETag"
  }
}
EOF

read -d '' REPORT <<EOF
{
  "Bucket": "arn:aws:s3:::ReportBucket",
  "Format": "Report_CSV_20180820",
  "Enabled": true,
  "Prefix": "reports/legalhold-objects-bops",
  "ReportScope": "AllTasks"
}
EOF

aws \
    s3control create-job \
    --account-id "${ACCOUNT_ID}" \
    --manifest "${MANIFEST//$'\n'}" \
    --operation "${OPERATION//$'\n'/}" \
    --report "${REPORT//$'\n'}" \
    --priority 10 \
    --role-arn "${ROLE_ARN}" \
    --client-request-token "$(uuidgen)" \
    --region "${AWS_DEFAULT_REGION}" \
    --description "Turn off legal hold";
```