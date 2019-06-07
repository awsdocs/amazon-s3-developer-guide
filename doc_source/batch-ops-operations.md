# Operations<a name="batch-ops-operations"></a>

Amazon S3 batch operations support five different operations\. The topics in this section describe each operation\. 

You can try the following AWS Command Line Interface \(AWS CLI\) commands to test Amazon S3 batch operations\.

1. Create an IAM role and assign permissions\. This role grants Amazon S3 permission to add object tags for which you are creating a job in the next step\.

   1. Create an IAM role as follows:

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

   1. Create an IAM policy with permissions and attach it to the IAM role that you created in the previous step\. For more information about permissions, see [Granting Permissions for Batch Operations](batch-ops-iam-role-policies.md)\.

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

1. Create an `S3PutObjectTagging` job\. The `manifest.csv` file provides a list of bucket and object key values\. The job applies the specified tags to objects identified in the manifest\. The `ETag` is the ETag of the manifest\.csv object, which you can get from the Amazon S3 console\. The request specifies the `no-confirmation-required` parameter\. Therefore, Amazon S3 makes the job eligible for execution without you having to confirm it using the `udpate-job-status` command\.

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
   aws s3ccontrol list-jobs \
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

1. If, in the `create-job`, you did not specify the `--no-confirmation-required` parameter, the job remains in a suspended state until you confirm the job by setting its status to `Ready`\. Amazon S3 then makes the job eligible for execution\.

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

**Topics**
+ [PUT Object Copy](batch-ops-copy-object.md)
+ [Initiate Restore Object](batch-ops-initiate-restore-object.md)
+ [Invoke a Lambda Function](batch-ops-invoke-lambda.md)
+ [Put Object ACL](batch-ops-put-object-acl.md)
+ [Put Object Tagging](batch-ops-put-object-tagging.md)