# Controlling ownership of uploaded objects using S3 Object Ownership<a name="about-object-ownership"></a>

****  
***S3 Object Ownership can be configured through the AWS Management Console, AWS Command Line Interface, AWS SDKs, or Amazon S3 REST APIs\. AWS CloudFormation support is planned but not yet available\.***

 S3 Object Ownership is an Amazon S3 bucket setting that you can use to control ownership of new objects that are uploaded to your buckets\. By default, when other AWS accounts upload objects to your bucket, the objects remain owned by the uploading account\. With S3 Object Ownership, any new objects that are written by other accounts with the `bucket-owner-full-control` canned access control list \(ACL\) automatically become owned by the bucket owner, who will have full control of the objects\. You can create shared data stores that multiple users and teams in different accounts can write to and read from, and standardize ownership of new objects in your bucket, allowing you as the bucket owner to share and manage access to these objects via resource\-based policies such as a bucket policy\. S3 Object Ownership does not affect existing objects\. 

S3 Object Ownership has two settings:
+ **Object writer** – The uploading account will own the object\.
+ **Bucket owner preferred** – The bucket owner will own the object if the object is uploaded with the `bucket-owner-full-control` canned ACL\. Without this setting and canned ACL, the object is uploaded and remains owned by the uploading account\. For information about enforcing object ownership, see [How do I ensure that I take ownership of new objects?](#ensure-object-ownership)\. 

**Topics**
+ [How do I ensure that I take ownership of new objects?](#ensure-object-ownership)
+ [Using S3 Object Ownership with Amazon S3 replication](#object-ownership-replication)
+ [Setting S3 Object Ownership](enable-object-ownership.md)

## How do I ensure that I take ownership of new objects?<a name="ensure-object-ownership"></a>

After setting S3 Object Ownership to bucket owner preferred, you can add a bucket policy to require all Amazon S3 PUT operations to include the `bucket-owner-full-control` canned ACL\. This ACL grants the bucket owner full control of new objects, and with the S3 Object Ownership setting it transfers object ownership to the bucket owner\. If the uploader fails to meet the ACL requirement in their upload, the request will fail\. This enables bucket owners to enforce uniform object ownership across all newly uploaded objects in their buckets\.

The following bucket policy specifies that account *111122223333* can upload objects to *awsdoc\-example\-bucket* only when the object's ACL is set to `bucket-owner-full-control`\. 

```
{
   "Version": "2012-10-17",
   "Statement": [
      {
         "Sid": "Only allow writes to my bucket with bucket owner full control",
         "Effect": "Allow",
         "Principal": {
            "AWS": [
               "arn:aws:iam::111122223333:user/ExampleUser"
            ]
         },
         "Action": [
            "s3:PutObject"
         ],
         "Resource": "arn:aws:s3:::awsdoc-example-bucket/*",
         "Condition": {
            "StringEquals": {
               "s3:x-amz-acl": "bucket-owner-full-control"
            }
         }
      }
   ]
}
```

The following is an example copy operation that includes the `bucket-owner-full-control` canned ACL using the AWS Command Line Interface \(AWS CLI\)\.

```
aws s3 cp file.txt s3://awsdoc-example-bucket --acl bucket-owner-full-control
```

If the client does not include the `bucket-owner-full-control` canned ACL, the operation fails, and the uploader will receive the following error: 

An error occurred \(AccessDenied\) when calling the PutObject operation: Access Denied\.

**Note**  
If clients need access to objects after uploading you will need to grant additional permissions to the uploading account\. For information about granting accounts access to your resources, see [Example walkthroughs: Managing access to your Amazon S3 resources ](example-walkthroughs-managing-access.md)\.

## Using S3 Object Ownership with Amazon S3 replication<a name="object-ownership-replication"></a>

S3 Object Ownership does not change the behavior of Amazon S3 replication\. In replication, the owner of the source object also owns the replica by default\. When the source and destination buckets are owned by different AWS accounts, you can add optional configuration settings to change replica ownership\. To transfer ownership of replicated objects to the destination bucket owner, you can use the Amazon S3 replication owner override option\. For more information about transferring ownership of replicas, see [Changing the replica owner](replication-change-owner.md)\.