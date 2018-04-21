# Specifying Conditions in a Policy<a name="amazon-s3-policy-keys"></a>

The access policy language allows you to specify conditions when granting permissions\. The `Condition`  element \(or `Condition` block\) lets you specify conditions for when a policy is in effect\. In the `Condition` element, which is optional, you build expressions in which you use Boolean operators \(equal, less than, etc\.\) to match your condition against values in the request\. For example, when granting a user permission to upload an object, the bucket owner can require the object be publicly readable by adding the `StringEquals` condition as shown here:

```
{ 
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "statement1",
      "Effect": "Allow",
      "Action": [
        "s3:PutObject"
      ],
      "Resource": [
        "arn:aws:s3:::examplebucket/*"
      ],
      "Condition": {
        "StringEquals": {
          "s3:x-amz-acl": [
            "public-read"
          ]
        }
      }
    }
  ]
}
```

The `Condition` block specifies the `StringEquals` condition that is applied to the specified key\-value pair, `"s3:x-amz-acl":["public-read"]`\. There is a set of predefined keys you can use in expressing a condition\. The example uses the `s3:x-amz-acl` condition key\. This condition requires user to include the `x-amz-acl` header with value `public-read` in every PUT object request\. 

For more information about specifying conditions in an access policy language, see [Condition](http://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements.html#Condition) in the *IAM User Guide*\. 

The following topics describe AWS\-wide and Amazon S3–specific condition keys and provide example policies\. 

**Topics**
+ [Available Condition Keys](#AvailableKeys-iamV2)
+ [Amazon S3 Condition Keys for Object Operations](#object-keys-in-amazon-s3-policies)
+ [Amazon S3 Condition Keys for Bucket Operations](#bucket-keys-in-amazon-s3-policies)

## Available Condition Keys<a name="AvailableKeys-iamV2"></a>

The predefined keys available for specifying conditions in an Amazon S3 access policy can be classified as follows:
+ **AWS\-wide keys** – AWS provides a set of common keys that are supported by all AWS services that support policies\. These keys that are common to all services are called AWS\-wide keys and use the prefix `aws:`\. For a list of AWS\-wide keys, see [Available Keys for Conditions](http://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements.html#AvailableKeys) in the *IAM User Guide*\. There are also keys that are specific to Amazon S3, which use the prefix `s3:`\. Amazon S3–specific keys are discussed in the next bulleted item\.

   

  The new condition keys `aws:sourceVpce` and `aws:sourceVpc` are used in bucket policies for VPC endpoints\. For examples of using these condition keys, see [Example Bucket Policies for VPC Endpoints for Amazon S3](example-bucket-policies-vpc-endpoint.md)\.

  The following example bucket policy allows authenticated users permission to use the `s3:GetObject` action if the request originates from a specific range of IP addresses \(192\.168\.143\.\*\), unless the IP address is 192\.168\.143\.188\. In the condition block, the `IpAddress` and the `NotIpAddress` are conditions, and each condition is provided a key\-value pair for evaluation\. Both the key\-value pairs in this example use the `aws:SourceIp` AWS\-wide key\.
**Note**  
The `IPAddress` and `NotIpAddress` key values specified in the condition uses CIDR notation as described in RFC 4632\. For more information, go to [http://www\.rfc\-editor\.org/rfc/rfc4632\.txt](http://www.rfc-editor.org/rfc/rfc4632.txt)\.

  ```
  {
      "Version": "2012-10-17",
      "Id": "S3PolicyId1",
      "Statement": [
          {
              "Sid": "statement1",
              "Effect": "Allow",
              "Principal": "*",
              "Action":["s3:GetObject"]  ,
              "Resource": "arn:aws:s3:::examplebucket/*",
              "Condition" : {
                  "IpAddress" : {
                      "aws:SourceIp": "192.168.143.0/24" 
                  },
                  "NotIpAddress" : {
                      "aws:SourceIp": "192.168.143.188/32" 
                  } 
              } 
          } 
      ]
  }
  ```
+ **Amazon S3–specific keys** – In addition to the AWS\-wide keys, the following are the condition keys that are applicable only in the context of granting Amazon S3 specific permissions\. These Amazon S3–specific keys use the prefix `s3:`\. 
  + `s3:x-amz-acl`
  + `s3:x-amz-copy-source`
  + `s3:x-amz-metadata-directive`
  + `s3:x-amz-server-side-encryption`
  + `s3:VersionId`
  + `s3:LocationConstraint`
  + `s3:delimiter`
  + `s3:max-keys`
  + `s3:prefix`
  + `s3:x-amz-server-side-encryption-aws-kms-key-id`
  + `s3:ExistingObjectTag/<tag-key>` 

    For example policies using object tags related condition keys, see [Object Tagging and Access Control Policies](object-tagging.md#tagging-and-policies)\.
  + `s3:RequestObjectTagKeys`
  + `s3:RequestObjectTag/<tag-key>`

   

  For example, the following bucket policy allows the `s3:PutObject` permission for two AWS accounts if the request includes the `x-amz-acl` header making the object publicly readable\. 

  ```
  {
      "Version":"2012-10-17",
      "Statement": [ 
          {
  	        "Sid":"AddCannedAcl",
              "Effect":"Allow",
  	        "Principal": {
                  "AWS": ["arn:aws:iam::account1-ID:root","arn:aws:iam::account2-ID:root"]
              },
  	        "Action":["s3:PutObject"],
              "Resource": ["arn:aws:s3:::examplebucket/*"],
              "Condition": {
                  "StringEquals": {
                      "s3:x-amz-acl":["public-read"]
                  }
              }
          }
      ]
  }
  ```

  The `Condition` block uses the `StringEquals` condition, and it is provided a key\-value pair, `"s3:x-amz-acl":["public-read"`, for evaluation\. In the key\-value pair, the `s3:x-amz-acl` is an Amazon S3–specific key, as indicated by the prefix `s3:`\. 

**Important**  
 Not all conditions make sense for all actions\. For example, it makes sense to include an `s3:LocationConstraint` condition on a policy that grants the `s3:CreateBucket` Amazon S3 permission, but not for the `s3:GetObject` permission\. Amazon S3 can test for semantic errors of this type that involve Amazon S3–specific conditions\. However, if you are creating a policy for an IAM user and you include a semantically invalid Amazon S3 condition, no error is reported, because IAM cannot validate Amazon S3 conditions\. 

The following section describes the condition keys that can be used to grant conditional permission for bucket and object operations\. In addition, there are condition keys related to Amazon S3 Signature Version 4 authentication\. For more information, go to [Amazon S3 Signature Version 4 Authentication Specific Policy Keys](http://docs.aws.amazon.com/AmazonS3/latest/API/bucket-policy-s3-sigv4-conditions.html) in the *Amazon Simple Storage Service API Reference*\.

## Amazon S3 Condition Keys for Object Operations<a name="object-keys-in-amazon-s3-policies"></a>

 The following table shows which Amazon S3 conditions you can use with which Amazon S3 actions\. Example policies are provided following the table\. Note the following about the Amazon S3–specific condition keys described in the following table:
+ The condition key names are preceded by the prefix `s3:`\. For example, `s3:x-amz-acl`\.
+ Each condition key maps to the same name request header allowed by the API on which the condition can be set\. That is, these condition keys dictate behavior of the same name request headers\. For example:
  + The condition key `s3:x-amz-acl` that you can use to grant condition permission for the `s3:PutObject` permission defines behavior of the `x-amz-acl` request header that the PUT Object API supports\. 
  + The condition key `s3:VersionId` that you can use to grant conditional permission for the `s3:GetObjectVersion` permission defines behavior of the `versionId` query parameter that you set in a GET Object request\.

[\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/amazon-s3-policy-keys.html)

### Example 1: Granting s3:PutObject permission with a condition requiring the bucket owner to get full control<a name="grant-putobject-conditionally-1"></a>

Suppose that Account A owns a bucket and the account administrator wants to grant Dave, a user in Account B, permissions to upload objects\. By default, objects that Dave uploads are owned by Account B, and Account A has no permissions on these objects\. Because the bucket owner is paying the bills, it wants full permissions on the objects that Dave uploads\. The Account A administrator can do this by granting the `s3:PutObject` permission to Dave, with a condition that the request include ACL\-specific headers, that either grants full permission explicitly or uses a canned ACL \(see [PUT Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html)\)\.
+ Require the `x-amz-full-control` header in the request with full control permission to the bucket owner\.

  The following bucket policy grants the `s3:PutObject` permission to user Dave with a condition using the `s3:x-amz-grant-full-control` condition key, which requires the request to include the `x-amz-full-control` header\.

  ```
  {
    "Version": "2012-10-17",
    "Statement": [
      {
        "Sid": "statement1",
        "Effect": "Allow",
        "Principal": {
          "AWS": "arn:aws:iam::AccountB-ID:user/Dave"
        },
        "Action": "s3:PutObject",
        "Resource": "arn:aws:s3:::examplebucket/*",
        "Condition": {
          "StringEquals": {
            "s3:x-amz-grant-full-control": "id=AccountA-CanonicalUserID"
          }
        }
      }
    ]
  }
  ```
**Note**  
This example is about cross\-account permission\. However, if Dave, who is getting the permission, belongs to the AWS account that owns the bucket, then this conditional permission is not necessary, because the parent account to which Dave belongs owns objects that the user uploads\.

  The preceding bucket policy grants conditional permission to user Dave in Account B\. While this policy is in effect, it is possible for Dave to get the same permission without any condition via some other policy\. For example, Dave can belong to a group and you grant the group `s3:PutObject` permission without any condition\. To avoid such permission loopholes, you can write a stricter access policy by adding explicit deny\. In this example, you explicitly deny the user Dave upload permission if he does not include the necessary headers in the request granting full permissions to the bucket owner\. Explicit deny always supersedes any other permission granted\. The following is the revised access policy example\.

  ```
  {
      "Version": "2012-10-17",
      "Statement": [
          {
              "Sid": "statement1",
              "Effect": "Allow",
              "Principal": {
                  "AWS": "arn:aws:iam::AccountB-ID:user/AccountBadmin"
              },
              "Action": "s3:PutObject",
              "Resource": "arn:aws:s3:::examplebucket/*",
              "Condition": {
                  "StringEquals": {
                      "s3:x-amz-grant-full-control": "id=AccountA-CanonicalUserID"
                  }
              }
          },
          {
              "Sid": "statement2",
              "Effect": "Deny",
              "Principal": {
                  "AWS": "arn:aws:iam::AccountB-ID:user/AccountBadmin"
              },
              "Action": "s3:PutObject",
              "Resource": "arn:aws:s3:::examplebucket/*",
              "Condition": {
                  "StringNotEquals": {
                      "s3:x-amz-grant-full-control": "id=AccountA-CanonicalUserID"
                  }
              }
          }
      ]
  }
  ```

  If you have two AWS accounts, you can test the policy using the AWS Command Line Interface \(AWS CLI\)\. You attach the policy and, using Dave's credentials, test the permission using the following AWS CLI `put-object` command\. You provide Dave's credentials by adding the `--profile` parameter\. You grant full control permission to the bucket owner by adding the `--grant-full-control` parameter\. For more information about setting up and using the AWS CLI, see [Setting Up the Tools for the Example Walkthroughs](policy-eval-walkthrough-download-awscli.md)\. 

  ```
  aws s3api put-object --bucket examplebucket --key HappyFace.jpg --body c:\HappyFace.jpg --grant-full-control id="AccountA-CanonicalUserID" --profile AccountBUserProfile
  ```
+ Require the `x-amz-acl` header with a canned ACL granting full control permission to the bucket owner\.

  To require the `x-amz-acl` header in the request, you can replace the key\-value pair in the `Condition` block and specify the `s3:x-amz-acl` condition key as shown below\.

  ```
  "Condition": {
  	"StringNotEquals": {
  		"s3:x-amz-acl": "bucket-owner-full-control"
  	}
  ```

  To test the permission using the AWS CLI, you specify the `--acl` parameter\. The AWS CLI then adds the `x-amz-acl` header when it sends the request\.

  ```
  aws s3api put-object --bucket examplebucket --key HappyFace.jpg --body c:\HappyFace.jpg --acl "bucket-owner-full-control" --profile AccountBadmin
  ```

### Example 2: Granting s3:PutObject permission requiring objects stored using server\-side encryption<a name="putobject-require-sse"></a>

Suppose that Account A owns a bucket\. The account administrator wants to grant Jane, a user in Account A, permission to upload objects with a condition that Jane always request server\-side encryption so that Amazon S3 saves objects encrypted\. The Account A administrator can accomplish using the `s3:x-amz-server-side-encryption` condition key as shown\. The key\-value pair in the `Condition` block specifies the `s3:x-amz-server-side-encryption` key\.

```
"Condition": {
     "StringNotEquals": {
         "s3:x-amz-server-side-encryption": "AES256"
     }
```

When testing the permission using the AWS CLI, you must add the required parameter using the `--server-side-encryption` parameter\.

```
aws s3api put-object --bucket example1bucket --key HappyFace.jpg --body c:\HappyFace.jpg --server-side-encryption "AES256" --profile AccountBadmin
```

### Example 3: Granting s3:PutObject permission to copy objects with a restriction on the copy source<a name="putobject-limit-copy-source-2"></a>

In the PUT Object request, when you specify a source object, it is a copy operation \(see [PUT Object \- Copy](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html)\)\. Accordingly, the bucket owner can grant a user permission to copy objects with restrictions on the source\. For example:
+ Allow copying objects only from the `sourcebucket` bucket\.
+ Allow copying objects from the `sourcebucket` bucket, and only the objects whose key name prefix starts with `public/` f\. For example, `sourcebucket/public/*`
+ Allow copying only a specific object from the `sourcebucket`; for example, `sourcebucket/example.jpg`\.

The following bucket policy grants user Dave `s3:PutObject` permission that allows him to copy only objects with a condition that the request include the `s3:x-amz-copy-source` header and the header value specify the `/examplebucket/public/*` key name prefix\. 

```
{
    "Version": "2012-10-17",
    "Statement": [
       {
            "Sid": "cross-account permission to user in your own account",
            "Effect": "Allow",
            "Principal": {
                "AWS": "arn:aws:iam::AccountA-ID:user/Dave"
            },
            "Action": ["s3:PutObject"],
            "Resource": "arn:aws:s3:::examplebucket/*"
        },
        {
            "Sid": "Deny your user permission to upload object if copy source is not /bucket/folder",
            "Effect": "Deny",
            "Principal": {
                "AWS": "arn:aws:iam::AccountA-ID:user/Dave"
            },
            "Action": "s3:PutObject",
            "Resource": "arn:aws:s3:::examplebucket/*",
            "Condition": {
                "StringNotLike": {
                    "s3:x-amz-copy-source": "examplebucket/public/*"
                }
            }
        }
    ]
}
```

You can test the permission using the AWS CLI `copy-object` command\. You specify the source by adding the `--copy-source` parameter, and the key name prefix must match the prefix allowed in the policy\. You need to provide the user Dave credentials using the `--profile` parameter\. For more information about setting up the AWS CLI, see [Setting Up the Tools for the Example Walkthroughs](policy-eval-walkthrough-download-awscli.md)\.

```
aws s3api copy-object --bucket examplebucket --key HappyFace.jpg 
--copy-source examplebucket/public/PublicHappyFace1.jpg --profile AccountADave
```

Note that the preceding policy uses the `StringNotLike` condition\. To grant permission to copy only a specific object, you must change the condition from `StringNotLike` to `StringNotEquals` and then specify the exact object key as shown\. 

```
"Condition": {
       "StringNotEquals": {
           "s3:x-amz-copy-source": "examplebucket/public/PublicHappyFace1.jpg"
       }
}
```

### Example 4: Granting access to a specific version of an object<a name="getobjectversion-limit-access-to-specific-version-3"></a>

Suppose that Account A owns a version\-enabled bucket\. The bucket has several versions of the `HappyFace.jpg` object\. The account administrator now wants to grant its user \(Dave\) permission to get only a specific version of the object\. The account administrator can accomplish this by granting Dave `s3:GetObjectVersion` permission conditionally as shown\. The key\-value pair in the `Condition` block specifies the `s3:VersionId` condition key\. 

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "statement1",
            "Effect": "Allow",
            "Principal": {
                "AWS": "arn:aws:iam::AccountA-ID:user/Dave"
            },
            "Action": ["s3:GetObjectVersion"],
            "Resource": "arn:aws:s3:::examplebucketversionenabled/HappyFace.jpg"
        },
        {
            "Sid": "statement2",
            "Effect": "Deny",
            "Principal": {
                "AWS": "arn:aws:iam::AccountA-ID:user/Dave"
            },
            "Action": ["s3:GetObjectVersion"],
            "Resource": "arn:aws:s3:::examplebucketversionenabled/HappyFace.jpg",
            "Condition": {
                "StringNotEquals": {
                    "s3:VersionId": "AaaHbAQitwiL_h47_44lRO2DDfLlBO5e"
                }
            }
        }
    ]
}
```

In this case, Dave needs to know the exact object version ID to retrieve the object\. 

You can test the permissions using the AWS CLI `get-object` command with the `--version-id` parameter identifying the specific object version\. The command retrieves the object and saves it to the `OutputFile.jpg` file\.

```
aws s3api get-object --bucket examplebucketversionenabled --key HappyFace.jpg OutputFile.jpg --version-id AaaHbAQitwiL_h47_44lRO2DDfLlBO5e --profile AccountADave
```

### Example 5: Restrict object uploads to objects with a specific storage class<a name="example-storage-class-condition-key"></a>

Suppose that Account A owns a bucket\. The account administrator wants to restrict Dave, a user in Account A, to be able to only upload objects to the bucket that are stored with the `STANDARD_IA` storage class\. The Account A administrator can do this by using the `s3:x-amz-storage-class` condition key as shown in the following example bucket policy\. 

```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "statement1",
      "Effect": "Allow",
      "Principal": {
        "AWS": "arn:aws:iam::AccountA-ID:user/Dave"
      },
      "Action": "s3:PutObject",
      "Resource": [
        "arn:aws:s3:::examplebucket/*"
      ],
      "Condition": {
        "StringEquals": {
          "s3:x-amz-storage-class": [
            "STANDARD_IA"
          ]
        }
      }
    }
  ]
}
```

## Amazon S3 Condition Keys for Bucket Operations<a name="bucket-keys-in-amazon-s3-policies"></a>

The following table shows list of bucket operation–specific permissions you can grant in policies, and for each of the permissions, the available keys you can use in specifying a condition\. 

[\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/amazon-s3-policy-keys.html)

### Example 1: Allow a user to create a bucket but only in a specific region<a name="condition-key-bucket-ops-1"></a>

Suppose that an AWS account administrator wants to grant its user \(Dave\) permission to create a bucket in the South America \(São Paulo\) Region only\. The account administrator can attach the following user policy granting the `s3:CreateBucket` permission with a condition as shown\. The key\-value pair in the `Condition` block specifies the `s3:LocationConstraint` key and the `sa-east-1` region as its value\.

**Note**  
In this example, the bucket owner is granting permission to one of its users, so either a bucket policy or a user policy can be used\. This example shows a user policy\.

For a list of Amazon S3 Regions, go to [Regions and Endpoints](http://docs.aws.amazon.com/general/latest/gr/rande.html#s3_region) in the *Amazon Web Services General Reference*\. 

```
{
   "Version":"2012-10-17",
   "Statement":[
      {
         "Sid":"statement1",
         "Effect":"Allow",
         "Action":[
            "s3:CreateBucket"
         ],
         "Resource":[
            "arn:aws:s3:::*"
         ],
         "Condition": {
             "StringLike": {
                 "s3:LocationConstraint": "sa-east-1"
             }
         }
       }
    ]
}
```

This policy restricts the user from creating a bucket in any other Region except `sa-east-1`\. However, it is possible some other policy will grant this user permission to create buckets in another Region\. For example, if the user belongs to a group, the group may have a policy attached to it allowing all users in the group permission to create buckets in some other Region\. To ensure that the user does not get permission to create buckets in any other Region, you can add an explicit deny statement in this policy\. 

```
{
   "Version":"2012-10-17",
   "Statement":[
      {
         "Sid":"statement1",
         "Effect":"Allow",
         "Action":[
            "s3:CreateBucket"
         ],
         "Resource":[
            "arn:aws:s3:::*"
         ],
         "Condition": {
             "StringLike": {
                 "s3:LocationConstraint": "sa-east-1"
             }
         }
       },
      {
         "Sid":"statement2",
         "Effect":"Deny",
         "Action":[
            "s3:CreateBucket"
         ],
         "Resource":[
            "arn:aws:s3:::*"
         ],
         "Condition": {
             "StringNotLike": {
                 "s3:LocationConstraint": "sa-east-1"
             }
         }
       }
    ]
}
```

The `Deny` statement uses the `StringNotLike` condition\. That is, a create bucket request is denied if the location constraint is not "sa\-east\-1"\. The explicit deny does not allow the user to create a bucket in any other Region, no matter what other permission the user gets\. 

You can test the policy using the following `create-bucket` AWS CLI command\. This example uses the `bucketconfig.txt` file to specify the location constraint\. Note the Windows file path\. You need to update the bucket name and path as appropriate\. You must provide user credentials using the `--profile` parameter\. For more information about setting up and using the AWS CLI, see [Setting Up the Tools for the Example Walkthroughs](policy-eval-walkthrough-download-awscli.md)\.

```
aws s3api create-bucket --bucket examplebucket --profile AccountADave --create-bucket-configuration file://c:/Users/someUser/bucketconfig.txt
```

The `bucketconfig.txt` file specifies the configuration as follows:

```
{"LocationConstraint": "sa-east-1"}
```

### Example 2: Allow a user to get a list of objects in a bucket according to a specific prefix<a name="condition-key-bucket-ops-2"></a>

A bucket owner can restrict a user to list the contents of a specific folder in the bucket\. This is useful if objects in the bucket are organized by key name prefixes\. The Amazon S3 console then uses the prefixes to show a folder hierarchy \(only the console supports the concept of folders; the Amazon S3 API supports only buckets and objects\)\. 

In this example, the bucket owner and the parent account to which the user belongs are the same\. So the bucket owner can use either a bucket policy or a user policy\. First, we show a user policy\.

The following user policy grants the `s3:ListBucket` permission \(see [GET Bucket \(List Objects\)](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html)\) with a condition that requires the user to specify the `prefix` in the request with the value `projects`\. 

```
{
   "Version":"2012-10-17",
   "Statement":[
      {
         "Sid":"statement1",
         "Effect":"Allow",
         "Action":[
            "s3:ListBucket"
         ],
         "Resource":[
            "arn:aws:s3:::examplebucket"
         ],
         "Condition" : {
             "StringEquals" : {
                 "s3:prefix": "projects" 
             }
          } 
       },
      {
         "Sid":"statement2",
         "Effect":"Deny",
         "Action":[
            "s3:ListBucket"
         ],
         "Resource":[
            "arn:aws:s3:::examplebucket"
         ],
         "Condition" : {
             "StringNotEquals" : {
                 "s3:prefix": "projects" 
             }
          } 
       }         
    ]
}
```

The condition restricts the user to listing object keys with the `projects` prefix\. The added explicit deny denies the user request for listing keys with any other prefix no matter what other permissions the user might have\. For example, it is possible that the user gets permission to list object keys without any restriction; for example, either by updates to the preceding user policy or via a bucket policy\. But because explicit deny always supersedes, the user request to list keys other than the `project` prefix is denied\. 

The preceding policy is a user policy\. If you add the `Principal` element to the policy, identifying the user, you now have a bucket policy as shown\.

```
{
   "Version":"2012-10-17",
   "Statement":[
      {
         "Sid":"statement1",
         "Effect":"Allow",
         "Principal": {
            "AWS": "arn:aws:iam::BucketOwner-accountID:user/user-name"
         },  
         "Action":[
            "s3:ListBucket"
         ],
         "Resource":[
            "arn:aws:s3:::examplebucket"
         ],
         "Condition" : {
             "StringEquals" : {
                 "s3:prefix": "examplefolder" 
             }
          } 
       },
      {
         "Sid":"statement2",
         "Effect":"Deny",
         "Principal": {
            "AWS": "arn:aws:iam::BucketOwner-AccountID:user/user-name"
         },  
         "Action":[
            "s3:ListBucket"
         ],
         "Resource":[
            "arn:aws:s3:::examplebucket"
         ],
         "Condition" : {
             "StringNotEquals" : {
                 "s3:prefix": "examplefolder" 
             }
          } 
       }         
    ]
}
```

You can test the policy using the following `list-object` AWS CLI command\. In the command, you provide user credentials using the `--profile` parameter\. For more information about setting up and using the AWS CLI, see [Setting Up the Tools for the Example Walkthroughs](policy-eval-walkthrough-download-awscli.md)\.

```
aws s3api list-objects --bucket examplebucket --prefix examplefolder --profile AccountADave
```

Now if the bucket is version\-enabled, to list the objects in the bucket, instead of `s3:ListBucket` permission, you must grant the `s3:ListBucketVersions` permission in the preceding policy\. This permission also supports the `s3:prefix` condition key\. 