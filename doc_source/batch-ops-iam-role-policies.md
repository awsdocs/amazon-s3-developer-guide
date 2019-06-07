# Granting Permissions for Batch Operations<a name="batch-ops-iam-role-policies"></a>

Amazon S3 must have your permissions to perform batch operations on your behalf\. You grant these permissions through an AWS Identity and Access Management \(IAM\) role\. This section provides examples of the trust and permissions policies you use when creating an IAM role\. For more information, see [IAM Roles](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html)\. 

[![AWS Videos](http://img.youtube.com/vi/https://www.youtube.com/embed/GrxlP39ye20//0.jpg)](http://www.youtube.com/watch?v=https://www.youtube.com/embed/GrxlP39ye20/)

## Trust Policy<a name="batch-ops-iam-role-policies-trust"></a>

You attach the following trust policy to the IAM role to allow the Amazon S3 batch operations service principal to assume the role\.

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

## Permissions Policies<a name="batch-ops-iam-role-policies-perm"></a>

Depending on the type of operations, you can attach one of the following policies\.

**Note**  
Regardless of the operation, Amazon S3 needs permissions to read your manifest object from your S3 bucket and optionally write a report to your bucket\. Therefore, all of the following policies include these permissions\.
For Amazon S3 inventory report manifests, Amazon S3 batch operations require permission to read the manifest\.json object and all associated CSV data files\.
+ PUT copy object

  ```
  {
    "Version":"2012-10-17",
    "Statement":[
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
          "s3:GetObject"
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
+ PUT object tagging

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
+ PUT object ACL

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
+ Initiate Glacier restore

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