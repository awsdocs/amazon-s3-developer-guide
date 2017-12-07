# Specifying Resources in a Policy<a name="s3-arn-format"></a>

The following is the common Amazon Resource Name \(ARN\) format to identify any resources in AWS\.

```
arn:partition:service:region:namespace:relative-id
```

For your Amazon S3 resources, 

+ `aws` is a common partition name\. If your resources are in China \(Beijing\) region, `aws-cn` is the partition name\.

+ `s3` is the service\. 

+ you don't specify region and namespace\.

+ For Amazon S3, it can be a `bucket-name` or a `bucket-name/object-key`\. You can use wild card\. 

Then the ARN format for Amazon S3 resources reduces to:

```
1. arn:aws:s3:::bucket_name
2. arn:aws:s3:::bucket_name/key_name
```

The following are examples of Amazon S3 resource ARNs\. 

+ This ARN identifies the `/developers/design_info.doc` object in the `examplebucket` bucket\.

  ```
  1. arn:aws:s3:::examplebucket/developers/design_info.doc
  ```

+ You can use wildcards as part of the resource ARN\. You can use wildcard characters \(\* and ?\) within any ARN segment \(the parts separated by colons\)\. An asterisk \(\*\) represents any combination of zero or more characters and a question mark \(?\) represents any single character\. You can have use multiple \* or ? characters in each segment, but a wildcard cannot span segments\. 

  + This ARN uses wildcard '\*' in relative\-ID part of the ARN to identify all objects in the `examplebucket` bucket\. 

    ```
    1. arn:aws:s3:::examplebucket/*
    ```

    This ARN uses '\*' to indicate all Amazon S3 resources \(all S3 buckets and objects in your account\)\.

    ```
    arn:aws:s3:::*
    ```

  + This ARN uses both wildcards, '\*', and '?', in the relative\-ID part\. It identifies all objects in buckets such as `example1bucket`, `example2bucket`, `example3bucket` and so on\.

    ```
    1. arn:aws:s3:::example?bucket/*
    ```

+ You can use policy variables in Amazon S3 ARNs\. At policy evaluation time, these predefined variables are replaced by their corresponding values\. Suppose you organize your bucket as a collection of folders, one folder for each of your users\. The folder name is the same as the user name\. To grant users permission to their folders, you can specify a policy variable in the resource ARN:

  ```
  arn:aws:s3:::bucket_name/developers/${aws:username}/
  ```

  At run time, when the policy is evaluated, the variable `${aws:username}` in the resource ARN is substituted with the user name making the request\. 

For more information, see the following resources:

+ [Resource](http://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements.html#Resource) in the *IAM User Guide*

+ [IAM Policy Variables Overview](http://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_variables.html) in the *IAM User Guide*\.

+ [ARNs](http://docs.aws.amazon.com/general/latest/gr/aws-arns-and-namespaces.html) in the *AWS General Reference*

For more information about other access policy language elements, see [Access Policy Language Overview](access-policy-language-overview.md)\.