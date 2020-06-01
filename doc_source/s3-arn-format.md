# Amazon S3 Resources<a name="s3-arn-format"></a>

The following common Amazon Resource Name \(ARN\) format identifies resources in AWS:

```
arn:partition:service:region:namespace:relative-id
```

For information about ARNs, see [Amazon Resource Names \(ARNs\)](https://docs.aws.amazon.com/general/latest/gr/aws-arns-and-namespaces.html) in the *AWS General Reference*\. 

For information about resources, see [IAM JSON Policy Elements: Resource](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements_resource.html) in the *IAM User Guide*\.

An Amazon S3 ARN excludes the AWS Region and namespace, but includes the following:
+ **Partition** ‐ `aws` is a common partition name\. If your resources are in the China \(Beijing\) Region, `aws-cn` is the partition name\.
+ **Service** ‐ `s3`\.
+ **Relative ID** ‐ `bucket-name` or a `bucket-name/object-key`\. You can use wild cards\. 

The ARN format for Amazon S3 resources reduces to the following:

```
1. arn:aws:s3:::bucket_name/key_name
```

For a complete list of Amazon S3 resources, see [Actions, resources, and condition keys for Amazon S3](list_amazons3.md)\. 

To find the ARN for an S3 bucket, you can look at the Amazon S3 console **Bucket Policy** or **CORS configuration** permissions pages\. For more information, see the following topics in the *Amazon Simple Storage Service Console User Guide*:
+ [How Do I Add an S3 Bucket Policy?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/add-bucket-policy.html)
+ [How Do I Allow Cross\-Domain Resource Sharing with CORS?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/add-cors-configuration.html)

## Amazon S3 ARN Examples<a name="s3-arn-examples"></a>

The following are examples of Amazon S3 resource ARNs\. 

**Bucket Name and Object Key Specified**  
The following ARN identifies the `/developers/design_info.doc` object in the `examplebucket` bucket\.

```
1. arn:aws:s3:::examplebucket/developers/design_info.doc
```

**Wildcards**  
You can use wildcards as part of the resource ARN\. You can use wildcard characters \(`*` and `?`\) within any ARN segment \(the parts separated by colons\)\. An asterisk \(`*`\) represents any combination of zero or more characters, and a question mark \(`?`\) represents any single character\. You can use multiple `*` or `?` characters in each segment, but a wildcard cannot span segments\. 
+ The following ARN uses the wildcard `*` in the relative\-ID part of the ARN to identify all objects in the `examplebucket` bucket\. 

  ```
  1. arn:aws:s3:::examplebucket/*
  ```
+ The following ARN uses `*` to indicate all Amazon S3 resources \(all S3 buckets and objects in your account\)\.

  ```
  arn:aws:s3:::*
  ```
+ The following ARN uses both wildcards, `*` and `?`, in the `relative-ID` part\. It identifies all objects in buckets such as `example1bucket`, `example2bucket`, `example3bucket`, and so on\.

  ```
  1. arn:aws:s3:::example?bucket/*
  ```

**Policy Variables**  
You can use policy variables in Amazon S3 ARNs\. At policy evaluation time, these predefined variables are replaced by their corresponding values\. Suppose that you organize your bucket as a collection of folders, one folder for each of your users\. The folder name is the same as the user name\. To grant users permission to their folders, you can specify a policy variable in the resource ARN:

```
arn:aws:s3:::bucket_name/developers/${aws:username}/
```

At runtime, when the policy is evaluated, the variable `${aws:username}` in the resource ARN is substituted with the user name making the request\. 