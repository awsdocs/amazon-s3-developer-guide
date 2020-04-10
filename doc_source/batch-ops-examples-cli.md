# Amazon S3 Batch Operations Examples Using the AWS CLI<a name="batch-ops-examples-cli"></a>

Amazon S3 batch operations track progress, send notifications, and store a detailed completion report of all actions, providing a fully managed, auditable, serverless experience\. You can use Amazon S3 batch operations through the AWS Management Console, AWS CLI, AWS SDKs, or REST API\. For more information, see [The Basics: Amazon S3 Batch Operations Jobs](batch-ops-basics.md)\.

The following are examples show how you can use Amazon S3 Batch Operations with the AWS Command Line Interface \(AWS CLI\)\.

**Topics**
+ [Creating an Amazon S3 Batch Operations Job](#batch-ops-example-cli-create)
+ [Using Amazon S3 Batch Operations Job Tagging](#batch-ops-example-cli-job-tags)

## Creating an Amazon S3 Batch Operations Job Using the AWS CLI<a name="batch-ops-example-cli-create"></a>

The following example creates an Amazon S3 Batch Operations `S3PutObjectTagging` job using the AWS CLI\. 

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

   1. Create an IAM policy with permissions, and attach it to the IAM role that you created in the previous step\. For more information about permissions, see [Granting Permissions for Amazon S3 Batch Operations](batch-ops-iam-role-policies.md)\.

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

1. Get the job description\.

   ```
   aws s3control describe-job \
       --region us-west-2 \
       --account-id acct-id \
       --job-id 00e123a4-c0d8-41f4-a0eb-b46f9ba5b07c
   ```

1. Get a list of `Active` and `Complete` jobs\.

   ```
   aws s3control list-jobs \
       --region us-west-2 \
       --account-id acct-id \
       --job-statuses '["Active","Complete"]' \
       --max-results 20
   ```

1. Update the job priority \(a higher number indicates a higher execution priority\)\.

   ```
   aws s3control update-job-priority \
       --region us-west-2 \
       --account-id acct-id \
       --priority 98 \
       --job-id 00e123a4-c0d8-41f4-a0eb-b46f9ba5b07c
   ```

1. If you didn't specify the `--no-confirmation-required` parameter in the `create-job`, the job remains in a suspended state until you confirm the job by setting its status to `Ready`\. Amazon S3 then makes the job eligible for execution\.

   ```
   aws s3control update-job-status \
       --region us-west-2 \
       --account-id 181572960644 \
       --job-id 00e123a4-c0d8-41f4-a0eb-b46f9ba5b07c \
       --requested-job-status 'Ready'
   ```

1. Cancel the job by setting the job status to `Cancelled`\.

   ```
   aws s3control update-job-status \
        --region us-west-2 \
        --account-id 181572960644 \
        --job-id 00e123a4-c0d8-41f4-a0eb-b46f9ba5b07c \
        --status-update-reason "No longer needed" \
        --requested-job-status Cancelled
   ```

## Using Amazon S3 Batch Operations Job Tagging with the AWS CLI<a name="batch-ops-example-cli-job-tags"></a>

You can label and control access to your Amazon S3 Batch Operations jobs by adding *tags*\. Tags can be used to identify who is responsible for a Batch Operations job\. You can create jobs with tags attached to them, and you can add tags to jobs after they are created\. For more information, see [Controlling Access and Labeling Jobs Using Tags](batch-ops-managing-jobs.md#batch-ops-job-tags)\.

**Topics**
+ [Create a Batch Operations Job with Tags](#batch-ops-example-cli-job-tags-create-job)
+ [Delete the Tags of a Batch Operations Job](#batch-ops-example-cli-job-tags-delete-job-tagging)
+ [Get the Tags of a Batch Operations Job](#batch-ops-example-cli-job-tags-get-job-tagging)
+ [Put Tags in a Batch Operations Job](#batch-ops-example-cli-job-tags-put-job-tagging)

### Create an Amazon S3 Batch Operations Job with Tags<a name="batch-ops-example-cli-job-tags-create-job"></a>

The following example creates an Amazon S3 Batch Operations `S3PutObjectCopy` job using job tags as labels for the job using the AWS CLI\. 

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

1. Execute the`create-job` action to create your Batch Operations job with inputs set in the preceding steps\.

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

### Delete the Tags of an Amazon S3 Batch Operations Job<a name="batch-ops-example-cli-job-tags-delete-job-tagging"></a>

The following example deletes the tags from a Batch Operations job using the AWS CLI\.

```
aws \
    s3control delete-job-tagging \
    --account-id 123456789012 \
    --job-id Example-e25a-4ed2-8bee-7f8ed7fc2f1c \
    --region us-east-1;
```

### Get the Job Tags of an Amazon S3 Batch Operations Job<a name="batch-ops-example-cli-job-tags-get-job-tagging"></a>

The following example gets the tags of a Batch Operations job using the AWS CLI\.

```
aws \
    s3control get-job-tagging \
    --account-id 123456789012 \
    --job-id Example-e25a-4ed2-8bee-7f8ed7fc2f1c \
    --region us-east-1;
```

### Put Job Tags in an Existing Amazon S3 Batch Operations Job<a name="batch-ops-example-cli-job-tags-put-job-tagging"></a>

The following is an example of using `s3control put-job-tagging` to add job tags to your Amazon S3 Batch Operations job using the AWS CLI\.

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