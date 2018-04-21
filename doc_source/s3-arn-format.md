# Specifying Resources in a Policy<a name="s3-arn-format"></a>

The following is the common Amazon Resource Name \(ARN\) format to identify any resources in AWS\.

```
arn:partition:service:region:namespace:relative-id
```

For your Amazon S3 resources: 
+ `aws` is a common partition name\. If your resources are in the China \(Beijing\) Region, `aws-cn` is the partition name\.
+ `s3` is the service\. 
+ You don't specify Region and namespace\.
+ For Amazon S3, it can be a `bucket-name` or a `bucket-name/object-key`\. You can use wild card\. 

Then the ARN format for Amazon S3 resources reduces to the following:

```
1. arn:aws:s3:::bucket_name
2. arn:aws:s3:::bucket_name/key_name
```

The following are examples of Amazon S3 resource ARNs\. 
+ This ARN identifies the `/developers/design_info.doc` object in the `examplebucket` bucket\.

  ```
  1. arn:aws:s3:::examplebucket/developers/design_info.doc
  ```
+ You can use wildcards as part of the resource ARN\. You can use wildcard characters \(`*` and `?`\) within any ARN segment \(the parts separated by colons\)\. An asterisk \(`*`\) represents any combination of zero or more characters, and a question mark \(`?`\) represents any single character\. You can use multiple `*` or `?` characters in each segment, but a wildcard cannot span segments\. 
  + This ARN uses the wildcard `*` in the relative\-ID part of the ARN to identify all objects in the `examplebucket` bucket\. 

    ```
    1. arn:aws:s3:::examplebucket/*
    ```

    This ARN uses `*` to indicate all Amazon S3 resources \(all S3 buckets and objects in your account\)\.

    ```
    arn:aws:s3:::*
    ```
  + This ARN uses both wildcards, `*` and `?`, in the relative\-ID part\. It identifies all objects in buckets such as `example1bucket`, `example2bucket`, `example3bucket`, and so on\.

    ```
    1. arn:aws:s3:::example?bucket/*
    ```
+ You can use policy variables in Amazon S3 ARNs\. At policy evaluation time, these predefined variables are replaced by their corresponding values\. Suppose that you organize your bucket as a collection of folders, one folder for each of your users\. The folder name is the same as the user name\. To grant users permission to their folders, you can specify a policy variable in the resource ARN:

  ```
  arn:aws:s3:::bucket_name/developers/${aws:username}/
  ```

  At run time, when the policy is evaluated, the variable `${aws:username}` in the resource ARN is substituted with the user name making the request\. 

To find the ARN for an S3 bucket, you can look at the Amazon S3 console **Bucket Policy** or **CORS configuration** permissions pages\. For more information, see [How Do I Add an S3 Bucket Policy?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/add-bucket-policy.html) or [How Do I Allow Cross\-Domain Resource Sharing with CORS?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/add-cors-configuration.html) in the *Amazon Simple Storage Service Console User Guide*\.

For more information about ARNs, see the following:
+ [Resource](http://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements.html#Resource) in the *IAM User Guide*
+ [IAM Policy Variables Overview](http://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_variables.html) in the *IAM User Guide*
+ [ARNs](http://docs.aws.amazon.com/general/latest/gr/aws-arns-and-namespaces.html) in the *AWS General Reference*

For more information about other access policy language elements, see [Access Policy Language Overview](access-policy-language-overview.md)\.