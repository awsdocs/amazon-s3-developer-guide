# Example Walkthroughs: Managing Access to Your Amazon S3 Resources<a name="example-walkthroughs-managing-access"></a>

This topic provides the following introductory walkthrough examples for granting access to Amazon S3 resources\. These examples use the AWS Management Console to create resources \(buckets, objects, users\) and grant them permissions\. The examples then show you how to verify permissions using the command line tools, so you don't have to write any code\. We provide commands using both the AWS Command Line Interface \(CLI\) and the AWS Tools for Windows PowerShell\.
+ [Example 1: Bucket Owner Granting Its Users Bucket Permissions ](example-walkthroughs-managing-access-example1.md)

  The IAM users you create in your account have no permissions by default\. In this exercise, you grant a user permission to perform bucket and object operations\.
+ [Example 2: Bucket Owner Granting Cross\-Account Bucket Permissions ](example-walkthroughs-managing-access-example2.md)

  In this exercise, a bucket owner, Account A, grants cross\-account permissions to another AWS account, Account B\. Account B then delegates those permissions to users in its account\. 
+ **Managing object permissions when the object and bucket owners are not the same**

  The example scenarios in this case are about a bucket owner granting object permissions to others, but not all objects in the bucket are owned by the bucket owner\. What permissions does the bucket owner need, and how can it delegate those permissions?

  The AWS account that creates a bucket is called the bucket owner\. The owner can grant other AWS accounts permission to upload objects, and the AWS accounts that create objects own them\. The bucket owner has no permissions on those objects created by other AWS accounts\. If the bucket owner writes a bucket policy granting access to objects, the policy does not apply to objects that are owned by other accounts\. 

  In this case, the object owner must first grant permissions to the bucket owner using an object ACL\. The bucket owner can then delegate those object permissions to others, to users in its own account, or to another AWS account, as illustrated by the following examples\.
  + [Example 3: Bucket Owner Granting Its Users Permissions to Objects It Does Not Own ](example-walkthroughs-managing-access-example3.md)

    In this exercise, the bucket owner first gets permissions from the object owner\. The bucket owner then delegates those permissions to users in its own account\.
  + [Example 4: Bucket Owner Granting Cross\-account Permission to Objects It Does Not Own](example-walkthroughs-managing-access-example4.md)

    After receiving permissions from the object owner, the bucket owner cannot delegate permission to other AWS accounts because cross\-account delegation is not supported \(see [Permission Delegation](access-policy-alternatives-guidelines.md#permission-delegation)\)\. Instead, the bucket owner can create an IAM role with permissions to perform specific operations \(such as get object\) and allow another AWS account to assume that role\. Anyone who assumes the role can then access objects\. This example shows how a bucket owner can use an IAM role to enable this cross\-account delegation\. 

## Before You Try the Example Walkthroughs<a name="before-you-try-example-walkthroughs-manage-access"></a>

These examples use the AWS Management Console to create resources and grant permissions\. And to test permissions, the examples use the command line tools, AWS Command Line Interface \(CLI\) and AWS Tools for Windows PowerShell, so you don't need to write any code\. To test permissions you will need to set up one of these tools\. For more information, see [Setting Up the Tools for the Example Walkthroughs](policy-eval-walkthrough-download-awscli.md)\. 

In addition, when creating resources these examples don't use root credentials of an AWS account\. Instead, you create an administrator user in these accounts to perform these tasks\. 

### About Using an Administrator User to Create Resources and Grant Permissions<a name="about-using-root-credentials"></a>

AWS Identity and Access Management \(IAM\) recommends not using the root credentials of your AWS account to make requests\. Instead, create an IAM user, grant that user full access, and then use that user's credentials to interact with AWS\. We refer to this user as an administrator user\. For more information, go to [Root Account Credentials vs\. IAM User Credentials](http://docs.aws.amazon.com/general/latest/gr/root-vs-iam.html) in the *AWS General Reference* and [IAM Best Practices](http://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html) in the *IAM User Guide*\.

All example walkthroughs in this section use the administrator user credentials\. If you have not created an administrator user for your AWS account, the topics show you how\. 

Note that to sign in to the AWS Management Console using the user credentials, you will need to use the IAM User Sign\-In URL\. The IAM console provides this URL for your AWS account\. The topics show you how to get the URL\.