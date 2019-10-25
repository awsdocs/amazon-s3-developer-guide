# Permissions Required for Website Access<a name="WebsiteAccessPermissionsReqd"></a>

When you configure a bucket as a website, you must grant public read access to the bucket so that people can access the website\. To make your bucket publicly readable, you must disable block public access settings for the bucket and write a bucket policy\. If your bucket contains objects that not owned by the bucket owner, you might also need to add an object access control list \(ACL\) that grants everyone read access\.

**Note**  
On the website endpoint, if a user requests an object that doesn't exist, Amazon S3 returns HTTP response code `404 (Not Found)`\. If the object exists but you haven't granted read permission on it, the website endpoint returns HTTP response code `403 (Access Denied)`\. The user can use the response code to infer whether a specific object exists\. If you don't want this behavior, you should not enable website support for your bucket\. 

## Edit Block Public Access Settings<a name="block-public-access-static-site"></a>

By default Amazon S3 does not allow public access to your account or buckets\. If you want to configure an existing bucket as a static website that has public access, you must edit block public access settings for that bucket\. You may also have to edit your account\-level block public access settings\. Amazon S3 applies the most restrictive combination of the bucket\-level and account\-level block public access settings\. For example, if you allow public access for a bucket but block all public access at the account level, Amazon S3 will continue to block public access to the bucket\. In this scenario, you would have to edit your bucket\-level and account\-level block public access settings\. For more information, see [Using Amazon S3 Block Public Access](access-control-block-public-access.md)\.

**Important**  
Before you turn off block public access, confirm that you want anyone on the internet to be able to access your bucket\. We recommend that you block all public access to your buckets unless you require a public bucket for a specific use case, such as a public static website\. If you only want certain users on your account to access your static website, you can edit your block public access settings rather than disabling block public access\. For more information, see [Using Amazon S3 Block Public Access](access-control-block-public-access.md)\.

**To disable block public access for a bucket configured as a static website**

1. Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. Select the bucket that you have configured as a static website, and choose **Edit public access settings**\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/edit-public-access.png)

1. Clear **Block *all* public access**, and choose **Save**\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/edit-public-access-clear.png)

1. In the confirmation box, enter **confirm**, and then choose **Confirm**\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/edit-public-access-confirm.png)

   Under **S3 buckets**, the **Access** for your bucket updates to **Objects can be public**\. You can now add a bucket policy to make the objects in the bucket publicly readable\. If the **Access** still displays as **Bucket and objects not public**, you might have to [edit the block public access settings](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/block-public-access-account.html) for your account before adding a bucket policy\.

## Add a Bucket Policy<a name="bucket-policy-static-site"></a>

To make the objects in your bucket publicly readable, you must write a bucket policy that grants everyone `s3:GetObject` permission\. The following sample bucket policy grants everyone access to the objects in the specified folder\. Before you add a bucket policy that grants public read access to a bucket, confirm that you have disabled block public access for the bucket\. To use the following bucket policy, update the `Resource` to match your bucket\. For more information about bucket policies, see [Using Bucket Policies and User Policies](using-iam-policies.md)\.

```
 1. {
 2.   "Version":"2012-10-17",
 3.   "Statement":[{
 4. 	"Sid":"PublicReadGetObject",
 5.         "Effect":"Allow",
 6. 	  "Principal": "*",
 7.       "Action":["s3:GetObject"],
 8.       "Resource":["arn:aws:s3:::example-bucket/*"
 9.       ]
10.     }
11.   ]
12. }
```

**To add a bucket policy**

1. Choose the bucket that you have configured as a static website\.

1. Choose **Permissions**\.

1. Choose **Bucket Policy**\.

1. In the **Bucket policy editor**, add a bucket policy, and choose **Save**\.

   Under **S3 buckets**, the **Access** for your bucket updates to **Public**\. If the **Access** displays as **Other authorized users of this account**, you might have to [edit the block public access settings](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/block-public-access-account.html) for your account\. 

### Object Access Control Lists<a name="object-acl"></a>

You can use a bucket policy to grant public read permission to your objects\. However, the bucket policy applies only to objects that are owned by the bucket owner\. If your bucket contains objects that aren't owned by the bucket owner, the bucket owner should use the object access control list \(ACL\) to grant public READ permission on those objects\.

To make an object publicly readable using an ACL, grant READ permission to the `AllUsers` group, as shown in the following grant element\. Add this grant element to the object ACL\. For information about managing ACLs, see [Managing Access with ACLs](S3_ACLs_UsingACLs.md)\.

```
1. <Grant>
2.   <Grantee xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
3.           xsi:type="Group">
4.     <URI>http://acs.amazonaws.com/groups/global/AllUsers</URI>
5.   </Grantee>
6.   <Permission>READ</Permission>
7. </Grant>
```