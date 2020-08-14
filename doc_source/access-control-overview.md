# Overview of managing access<a name="access-control-overview"></a>

When granting permissions, you decide who is getting them, which Amazon S3 resources they are getting permissions for, and specific actions you want to allow on those resources\. 

**Topics**
+ [Amazon S3 resources: Buckets and objects](#access-control-resources-basics)
+ [Amazon S3 bucket and object ownership](#about-resource-owner)
+ [Resource operations](#access-control-resource-operations-basics)
+ [Managing access to resources](#access-control-resources-manage-permissions-basics)
+ [Which access control method should I use?](#so-which-one-should-i-use)
+ [More info](#access-control-overview-related-topics)

## Amazon S3 resources: Buckets and objects<a name="access-control-resources-basics"></a>

In Amazon Web Services \(AWS\), a resource is an entity that you can work with\. In Amazon S3, buckets and objects are the resources, and both have associated subresources\. For example, bucket subresources include the following:
+ `lifecycle` – Stores lifecycle configuration information \(see [Object lifecycle management](object-lifecycle-mgmt.md)\)\.
+ `website` – Stores website configuration information if you configure your bucket for website hosting \(see [Hosting a static website on Amazon S3](WebsiteHosting.md)\)\. 
+ `versioning` – Stores versioning configuration \(see [PUT Bucket versioning](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTVersioningStatus.html)\)\. 
+ `policy` and `acl` \(access control list\) – Store access permission information for the bucket\. 
+ `cors` \(cross\-origin resource sharing\) – Supports configuring your bucket to allow cross\-origin requests \(see [Cross\-origin resource sharing \(CORS\)](cors.md)\)\. 
+ `logging` – Enables you to request Amazon S3 to save bucket access logs\.

Object subresources include the following:
+ `acl` – Stores a list of access permissions on the object\. This topic discusses how to use this subresource to manage object permissions \(see [Managing Access with ACLs](S3_ACLs_UsingACLs.md)\)\.
+ `restore` – Supports temporarily restoring an archived object \(see [POST Object restore](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOSTrestore.html)\)\. An object in the S3 Glacier storage class is an archived object\. To access the object, you must first initiate a restore request, which restores a copy of the archived object\. In the request, you specify the number of days that you want the restored copy to exist\. For more information about archiving objects, see [Object lifecycle management](object-lifecycle-mgmt.md)\.

## Amazon S3 bucket and object ownership<a name="about-resource-owner"></a>

Buckets and objects are Amazon S3 resources\. By default, only the resource owner can access these resources\. The resource owner refers to the AWS account that creates the resource\. For example: 
+ The AWS account that you use to create buckets and upload objects owns those resources\. 

   
+  If you upload an object using AWS Identity and Access Management \(IAM\) user or role credentials, the AWS account that the user or role belongs to owns the object\. 

   
+ A bucket owner can grant cross\-account permissions to another AWS account \(or users in another account\) to upload objects\. In this case, the AWS account that uploads objects owns those objects\. The bucket owner does not have permissions on the objects that other accounts own, with the following exceptions:
  + The bucket owner pays the bills\. The bucket owner can deny access to any objects, or delete any objects in the bucket, regardless of who owns them\. 
  + The bucket owner can archive any objects or restore archived objects regardless of who owns them\. Archival refers to the storage class used to store the objects\. For more information, see [Object lifecycle management](object-lifecycle-mgmt.md)\.

### Ownership and request authentication<a name="about-resource-owner-requests"></a>

All requests to a bucket are either authenticated or unauthenticated\. Authenticated requests must include a signature value that authenticates the request sender, unauthenticated requests do not\. For more information on request authentication, see [Making requests](MakingRequests.md)\.

A bucket owner can allow unauthenticated requests\. For example, unauthenticated [PUT Object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html) requests are allowed when a bucket has a public bucket policy, or when a bucket ACL grants `WRITE` or `FULL_CONTROL` access to the All Users group or the anonymous user specifically\. For more information about public bucket policies and public ACLs, see [The meaning of "public"](access-control-block-public-access.md#access-control-block-public-access-policy-status)\.

All unauthenticated requests are made by the anonymous user\. This user is represented in access control lists \(ACLs\) by the specific canonical user ID `65a011a29cdf8ec533ec3d1ccaae921c`\. If an object is uploaded to a bucket through an unauthenticated request, the anonymous user owns the object\. The default object ACL grants `FULL_CONTROL` to the anonymous user as the object's owner\. Therefore, Amazon S3 allows unauthenticated requests to retrieve the object or modify its ACL\. 

To prevent objects from being modified by the anonymous user, we recommend that you do not implement bucket policies that allow anonymous public writes to your bucket or use ACLs that allow the anonymous user write access to your bucket\. You can enforce this recommended behavior by using Amazon S3 Block Public Access\. 

For more information about blocking public access, see [Using Amazon S3 block public access](access-control-block-public-access.md)\. For more information about ACLs, see [Access Control List \(ACL\) Overview](acl-overview.md)\.

**Important**  
AWS recommends that you don't use the AWS account root user credentials to make authenticated requests\. Instead, create an IAM user and grant that user full access\. We refer to these users as administrator users\. You can use the administrator user credentials, instead of AWS account root user credentials, to interact with AWS and perform tasks, such as create a bucket, create users, and grant them permissions\. For more information, see [AWS Account Root User Credentials vs\. IAM User Credentials](https://docs.aws.amazon.com/general/latest/gr/root-vs-iam.html) in the *AWS General Reference* and [IAM Best Practices](https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html) in the *IAM User Guide*\.

## Resource operations<a name="access-control-resource-operations-basics"></a>

Amazon S3 provides a set of operations to work with the Amazon S3 resources\. For a list of available operations, see [Actions Defined by Amazon S3](list_amazons3.md#amazons3-actions-as-permissions)\.

## Managing access to resources<a name="access-control-resources-manage-permissions-basics"></a>

Managing access refers to granting others \(AWS accounts and users\) permission to perform the resource operations by writing an access policy\. For example, you can grant `PUT Object` permission to a user in an AWS account so the user can upload objects to your bucket\. In addition to granting permissions to individual users and accounts, you can grant permissions to everyone \(also referred as anonymous access\) or to all authenticated users \(users with AWS credentials\)\. For example, if you configure your bucket as a website, you may want to make objects public by granting the `GET Object` permission to everyone\. 

### Access policy options<a name="access-policies-alternatives-intro"></a>

Access policy describes who has access to what\. You can associate an access policy with a resource \(bucket and object\) or a user\. Accordingly, you can categorize the available Amazon S3 access policies as follows:
+ **Resource\-based policies ** – Bucket policies and access control lists \(ACLs\) are resource\-based because you attach them to your Amazon S3 resources\.   
![\[Diagram depicting AWS account resources, including an S3 bucket with a bucket ACL and bucket policy, and S3 objects with object ACLs.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/resource-based-policy.png)
  + ACL – Each bucket and object has an ACL associated with it\. An ACL is a list of grants identifying grantee and permission granted\. You use ACLs to grant basic read/write permissions to other AWS accounts\. ACLs use an Amazon S3–specific XML schema\. 

    The following is an example bucket ACL\. The grant in the ACL shows a bucket owner as having full control permission\. 

    ```
    <?xml version="1.0" encoding="UTF-8"?>
    <AccessControlPolicy xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
      <Owner>
        <ID>*** Owner-Canonical-User-ID ***</ID>
        <DisplayName>owner-display-name</DisplayName>
      </Owner>
      <AccessControlList>
        <Grant>
          <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
                   xsi:type="Canonical User">
            <ID>*** Owner-Canonical-User-ID ***</ID>
            <DisplayName>display-name</DisplayName>
          </Grantee>
          <Permission>FULL_CONTROL</Permission>
        </Grant>
      </AccessControlList>
    </AccessControlPolicy>
    ```

    Both bucket and object ACLs use the same XML schema\.
  + Bucket policy – For your bucket, you can add a bucket policy to grant other AWS accounts or IAM users permissions for the bucket and the objects in it\. Any object permissions apply only to the objects that the bucket owner creates\. Bucket policies supplement, and in many cases, replace ACL\-based access policies\.

    The following is an example bucket policy\. You express bucket policy \(and user policy\) using a JSON file\. The policy grants anonymous read permission on all objects in a bucket\. The bucket policy has one statement, which allows the `s3:GetObject` action \(read permission\) on objects in a bucket named `examplebucket`\.  By specifying the `principal` with a wild card \(\*\), the policy grants anonymous access, and should be used carefully\. For example, the following bucket policy would make objects publicly accessible\. 

    ```
    {
        "Version":"2012-10-17",
        "Statement": [
            {
                "Sid":"GrantAnonymousReadPermissions",
                "Effect":"Allow",
                "Principal": "*",
                "Action":["s3:GetObject"],
                "Resource":["arn:aws:s3:::awsexamplebucket1/*"]
            }
        ]
    }
    ```
+ **User policies** – You can use IAM to manage access to your Amazon S3 resources\. You can create IAM users, groups, and roles in your account and attach access policies to them granting them access to AWS resources, including Amazon S3\.   
![\[Diagram depicting the AWS account admin and other users with attached user policies.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/user-policy.png)

  For more information about IAM, see the [AWS Identity and Access Management \(IAM\)](https://aws.amazon.com/iam/) product detail page\. 

  The following is an example of a user policy\. You cannot grant anonymous permissions in an IAM user policy, because the policy is attached to a user\. The example policy allows the associated user that it's attached to perform six different Amazon S3 actions on a bucket and the objects in it\. You can attach this policy to a specific IAM user, group, or role\.

  ```
  {
      "Version": "2012-10-17",
      "Statement": [
          {
              "Sid": "AssignUserActions",
              "Effect": "Allow",
              "Action": [
                  "s3:PutObject",
                  "s3:GetObject",
                  "s3:ListBucket",
                  "s3:DeleteObject",
                  "s3:GetBucketLocation"
              ],
              "Resource": [
                   "arn:aws:s3:::awsexamplebucket1/*",
                   "arn:aws:s3:::awsexamplebucket1"
              ]
          },
          {
              "Sid": "ExampleStatement2",
              "Effect": "Allow",
              "Action": "s3:ListAllMyBuckets",
              "Resource": "*"
          }
      ]
  }
  ```

When Amazon S3 receives a request, it must evaluate all the access policies to determine whether to authorize or deny the request\. For more information about how Amazon S3 evaluates these policies, see [How Amazon S3 Authorizes a Request](how-s3-evaluates-access-control.md)\.

### Access Analyzer for S3<a name="access-analyzer-s3-info"></a>

On the Amazon S3 console, you can use Access Analyzer for S3 to review all buckets that have bucket access control lists \(ACLs\), bucket policies, or access point policies that grant public or shared access\. Access Analyzer for S3 alerts you to buckets that are configured to allow access to anyone on the internet or other AWS accounts, including AWS accounts outside of your organization\. For each public or shared bucket, you receive findings that report the source and level of public or shared access\. 

In Access Analyzer for S3, you can block all public access to a bucket with a single click\. We recommend that you block all access to your buckets unless you require public access to support a specific use case\. Before you block all public access, ensure that your applications will continue to work correctly without public access\. For more information, see [Using Amazon S3 block public access](access-control-block-public-access.md)\.

You can also drill down into bucket\-level permission settings to configure granular levels of access\. For specific and verified use cases that require public or shared access, you can acknowledge and record your intent for the bucket to remain public or shared by archiving the findings for the bucket\. You can revisit and modify these bucket configurations at any time\. You can also download your findings as a CSV report for auditing purposes\.

Access Analyzer for S3 is available at no extra cost on the Amazon S3 console\. Access Analyzer for S3 is powered by AWS Identity and Access Management \(IAM\) Access Analyzer\. To use Access Analyzer for S3 on the Amazon S3 console, you must visit the IAM console and create an account\-level analyzer in IAM Access Analyzer on a per\-Region basis\. 

For more information about Access Analyzer for S3, see [Using Access Analyzer for S3](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/access-analyzer.html) in the *Amazon Simple Storage Service Console User Guide*\.

## Which access control method should I use?<a name="so-which-one-should-i-use"></a>

 With the options available to write an access policy, the following questions arise:
+ When should I use which access control method? For example, to grant bucket permissions, should I use a bucket policy or bucket ACL? I own a bucket and the objects in the bucket\. Should I use a resource\-based access policy or an IAM user policy? If I use a resource\-based access policy, should I use a bucket policy or an object ACL to manage object permissions?
+ I own a bucket, but I don't own all of the objects in it\. How are access permissions managed for the objects that somebody else owns? 
+ If I grant access by using a combination of these access policy options, how does Amazon S3 determine if a user has permission to perform a requested operation? 

 The following sections explain these access control alternatives, how Amazon S3 evaluates access control mechanisms, and when to use which access control method\. They also provide example walkthroughs\.
+  [How Amazon S3 Authorizes a Request](how-s3-evaluates-access-control.md) 
+  [Guidelines for using the available access policy options](access-policy-alternatives-guidelines.md) 
+  [Example walkthroughs: Managing access to your Amazon S3 resources ](example-walkthroughs-managing-access.md) 

## More info<a name="access-control-overview-related-topics"></a>

We recommend that you first review the introductory topics that explain the options available for you to manage access to your Amazon S3 resources\. For more information, see [Identity and access management in Amazon S3](s3-access-control.md)\. You can then use the following topics for more information about specific access policy options\. 
+  [Using Bucket Policies and User Policies](using-iam-policies.md) 
+  [Managing Access with ACLs](S3_ACLs_UsingACLs.md) 