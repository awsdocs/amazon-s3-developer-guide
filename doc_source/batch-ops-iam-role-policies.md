# Granting Permissions for Amazon S3 Batch Operations<a name="batch-ops-iam-role-policies"></a>

This section describes how to grant the necessary permissions required for creating and performing Amazon S3 Batch Operations jobs\.

**Topics**
+ [Required Permissions for Creating an Amazon S3 Batch Operations Job](#batch-ops-job-required-permissions)
+ [Creating an Amazon S3 Batch Operations IAM Role](#batch-ops-iam-role-policies-create)

## Required Permissions for Creating an Amazon S3 Batch Operations Job<a name="batch-ops-job-required-permissions"></a>

To create an Amazon S3 Batch Operations job, the `s3:CreateJob` permission is required\. The same entity creating the job must also have the `iam:PassRole` permission to pass the AWS Identity and Access Management \(IAM\) role specified for the job to Amazon S3 Batch Operations\. For information about creating this IAM role, see the next topic [Creating an Amazon S3 Batch Operations IAM Role](#batch-ops-iam-role-policies-create)\.

## Creating an Amazon S3 Batch Operations IAM Role<a name="batch-ops-iam-role-policies-create"></a>

Amazon S3 must have your permissions to perform batch operations on your behalf\. You grant these permissions through an AWS Identity and Access Management \(IAM\) role\. This section provides examples of the trust and permissions policies you use when creating an IAM role\. For more information, see [IAM Roles](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html)\. For examples, see [Example: Using Job Tags to Control Permissions for Amazon S3 Batch Operations](batch-ops-job-tags-examples.md)\.

In your IAM policies, you can also use condition keys to filter access permissions for Amazon S3 batch operations jobs\. For more information and a complete list of Amazon S3‐specific condition keys, see [Actions, Resources, and Condition Keys for Amazon S3](list_amazons3.md)\.

The following video shows how to set up IAM permissions for Batch Operations jobs using the AWS Management Console\.

[![AWS Videos](http://img.youtube.com/vi/https://www.youtube.com/embed/GrxlP39ye20//0.jpg)](http://www.youtube.com/watch?v=https://www.youtube.com/embed/GrxlP39ye20/)

### Trust Policy<a name="batch-ops-iam-role-policies-trust"></a>

To allow the Amazon S3 Batch Operations service principal to assume the IAM role, attach the following trust policy to the role\.

```
{
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
}
```

### Permissions Policies<a name="batch-ops-iam-role-policies-perm"></a>

Depending on the type of operations, you can attach one of the following policies\.

**Note**  
Regardless of the operation, Amazon S3 needs permissions to read your manifest object from your S3 bucket and optionally write a report to your bucket\. Therefore, all of the following policies include these permissions\.
For Amazon S3 inventory report manifests, Amazon S3 Batch Operations require permission to read the manifest\.json object and all associated CSV data files\.
Version\-specific permissions such as `s3:GetObjectVersion` are only required when you are specifying the version ID of the objects\.
If you are running Amazon S3 Batch Operations on encrypted objects, the IAM role must also have access to the AWS KMS keys used to encrypt them\.
+ **PUT copy object**

  ```
  {
      "Version": "2012-10-17",
      "Statement": [
          {
              "Action": [
                  "s3:PutObject",
                  "s3:PutObjectAcl",
                  "s3:PutObjectTagging"
              ],
              "Effect": "Allow",
              "Resource": "arn:aws:s3:::{{DestinationBucket}}/*"
          },
          {
              "Action": [
                  "s3:GetObject",
                  "s3:GetObjectAcl",
                  "s3:GetObjectTagging"
              ],
              "Effect": "Allow",
              "Resource": "arn:aws:s3:::{{SourceBucket}}/*"
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
  ```
+ **PUT object tagging**

  ```
  {
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
  }
  ```
+ **PUT object ACL**

  ```
  {
    "Version":"2012-10-17",
    "Statement":[
      {
        "Effect":"Allow",
        "Action":[
          "s3:PutObjectAcl",
          "s3:PutObjectVersionAcl"
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
  }
  ```
+ Initiate S3 Glacier restore

  ```
  {
    "Version":"2012-10-17",
    "Statement":[
      {
        "Effect":"Allow",
        "Action":[
            "s3:RestoreObject"
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
  }
  ```

### Related Resources<a name="batch-ops-create-job-related-resources"></a>
+ [The Basics: Amazon S3 Batch Operations Jobs](batch-ops-basics.md)
+ [Operations](batch-ops-operations.md)
+ [Managing Batch Operations Jobs](batch-ops-managing-jobs.md)