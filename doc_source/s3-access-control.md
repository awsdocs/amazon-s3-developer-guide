# Managing Access Permissions to Your Amazon S3 Resources<a name="s3-access-control"></a>

By default, all Amazon S3 resources—buckets, objects, and related subresources \(for example, `lifecycle` configuration and `website` configuration\)—are private: only the resource owner, an AWS account that created it, can access the resource\. The resource owner can optionally grant access permissions to others by writing an access policy\. 

Amazon S3 offers access policy options broadly categorized as resource\-based policies and user policies\. Access policies you attach to your resources \(buckets and objects\) are referred to as resource\-based policies\. For example, bucket policies and access control lists \(ACLs\) are resource\-based policies\. You can also attach access policies to users in your account\. These are called user policies\. You may choose to use resource\-based policies, user policies, or some combination of these to manage permissions to your Amazon S3 resources\. The introductory topics provide general guidelines for managing permissions\.

We recommend you first review the access control overview topics\. For more information, see [Introduction to Managing Access Permissions to Your Amazon S3 Resources](intro-managing-access-s3-resources.md)\. Then for more information about specific access policy options, see the following topics:
+  [Using Bucket Policies and User Policies](using-iam-policies.md) 
+ [Managing Access with ACLs](S3_ACLs_UsingACLs.md)