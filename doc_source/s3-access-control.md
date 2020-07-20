# Identity and access management in Amazon S3<a name="s3-access-control"></a>

By default, all Amazon S3 resources—buckets, objects, and related subresources \(for example, `lifecycle` configuration and `website` configuration\)—are private: only the resource owner, an AWS account that created it, can access the resource\. The resource owner can optionally grant access permissions to others by writing an access policy\. 

Amazon S3 offers access policy options broadly categorized as resource\-based policies and user policies\. Access policies you attach to your resources \(buckets and objects\) are referred to as resource\-based policies\. For example, bucket policies and access control lists \(ACLs\) are resource\-based policies\. You can also attach access policies to users in your account\. These are called user policies\. You may choose to use resource\-based policies, user policies, or some combination of these to manage permissions to your Amazon S3 resources\. The introductory topics provide general guidelines for managing permissions\.

## Introduction to managing access to Amazon S3 resources<a name="intro-managing-access-s3-resources"></a>

We recommend you first review the introductory topics that explain the options for managing access to your Amazon S3 resources:
+ [Overview of managing access](access-control-overview.md)
+ [How Amazon S3 Authorizes a Request](how-s3-evaluates-access-control.md)
+ [Guidelines for using the available access policy options](access-policy-alternatives-guidelines.md)
+ [Example walkthroughs: Managing access to your Amazon S3 resources ](example-walkthroughs-managing-access.md)

Several security best practices also address access control, including:
+ [Ensure Amazon S3 buckets are not publicly accessible](security-best-practices.md#public)
+ [Implement least privilege access](security-best-practices.md#least)
+ [Use IAM roles](security-best-practices.md#roles)
+ [Enable MFA (Multi-Factor Authentication) Delete](security-best-practices.md#mfa)
+ [Identify and audit all your Amazon S3 buckets](security-best-practices.md#audit)
+ [Monitor AWS security advisories](security-best-practices.md#advisories)

## Amazon S3 resource access options<a name="s3-resource-access-options"></a>

After you've reviewed introductory topics about managing access to Amazon S3 resources, you can then use the following topics to get more information about specific access policy options:
+ [Using Bucket Policies and User Policies](using-iam-policies.md)
+ [Managing Access with ACLs](S3_ACLs_UsingACLs.md)
+ [Using Amazon S3 block public access](access-control-block-public-access.md)