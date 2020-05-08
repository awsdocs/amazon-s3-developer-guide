# Setting permissions for website access<a name="WebsiteAccessPermissionsReqd"></a>

When you configure a bucket as a static website, if you want your website to be public, you can grant public read access\. To make your bucket publicly readable, you must disable block public access settings for the bucket and write a bucket policy that grants public read access\. If your bucket contains objects that are not owned by the bucket owner, you might also need to add an object access control list \(ACL\) that grants everyone read access\.

**Note**  
On the website endpoint, if a user requests an object that doesn't exist, Amazon S3 returns HTTP response code `404 (Not Found)`\. If the object exists but you haven't granted read permission on it, the website endpoint returns HTTP response code `403 (Access Denied)`\. The user can use the response code to infer whether a specific object exists\. If you don't want this behavior, you should not enable website support for your bucket\. 

## Step 1: Edit block public access settings<a name="block-public-access-static-site"></a>

If you want to configure an existing bucket as a static website that has public access, you must edit block public access settings for that bucket\. You may also have to edit your account\-level block public access settings\. Amazon S3 applies the most restrictive combination of the bucket\-level and account\-level block public access settings\.

For example, if you allow public access for a bucket but block all public access at the account level, Amazon S3 will continue to block public access to the bucket\. In this scenario, you would have to edit your bucket\-level and account\-level block public access settings\. For more information, see [Using Amazon S3 block public access](access-control-block-public-access.md)\.

By default, Amazon S3 blocks public access to your account and buckets\. If you want to use a bucket to host a static website, you can use these steps to edit your block public access settings\. 

**Warning**  
Before you complete this step, review [Using Amazon S3 Block Public Access](https://docs.aws.amazon.com/AmazonS3/latest/dev/access-control-block-public-access.html) to ensure that you understand and accept the risks involved with allowing public access\. When you turn off block public access settings to make your bucket public, anyone on the internet can access your bucket\. We recommend that you block all public access to your buckets\.

1. Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. Choose the name of the bucket that you have configured as a static website\.

1. Choose **Permissions**\.

1. Choose **Edit**\.

1. Clear **Block *all* public access**, and choose **Save**\.
**Warning**  
Before you complete this step, review [Using Amazon S3 Block Public Access](https://docs.aws.amazon.com/AmazonS3/latest/dev/access-control-block-public-access.html) to ensure you understand and accept the risks involved with allowing public access\. When you turn off block public access settings to make your bucket public, anyone on the internet can access your bucket\. We recommend that you block all public access to your buckets\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/edit-public-access-clear.png)

1. In the confirmation box, enter **confirm**, and then choose **Confirm**\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/edit-public-access-confirm.png)

   Under **S3 buckets**, the **Access** for your bucket updates to **Objects can be public**\. You can now add a bucket policy to make the objects in the bucket publicly readable\. If the **Access** still displays as **Bucket and objects not public**, you might have to [edit the block public access settings](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/block-public-access-account.html) for your account before adding a bucket policy\.

## Step 2: Add a bucket policy<a name="bucket-policy-static-site"></a>

To make the objects in your bucket publicly readable, you must write a bucket policy that grants everyone `s3:GetObject` permission\. 

After you edit S3 Block Public Access settings, you can add a bucket policy to grant public read access to your bucket\. When you grant public read access, anyone on the internet can access your bucket\.

**Important**  
The following policy is an example only and allows full access to the contents of your bucket\. Before you proceed with this step, review [How can I secure the files in my Amazon S3 bucket?](https://aws.amazon.com/premiumsupport/knowledge-center/secure-s3-resources/) to ensure that you understand the best practices for securing the files in your S3 bucket and risks involved in granting public access\.

1. Under **Buckets**, choose the name of your bucket\.

1. Choose **Permissions**\.

1. Choose **Bucket Policy**\.

1. To grant public read access for your website, copy the following bucket policy, and paste it in the **Bucket policy editor**\.

   ```
   {
       "Version": "2012-10-17",
       "Statement": [
           {
               "Sid": "PublicReadGetObject",
               "Effect": "Allow",
               "Principal": "*",
               "Action": [
                   "s3:GetObject"
               ],
               "Resource": [
                   "arn:aws:s3:::example.com/*"
               ]
           }
       ]
   }
   ```

1. Update the `Resource` to include your bucket name\.

   In the preceding example bucket policy, *example\.com* is the bucket name\. To use this bucket policy with your own bucket, you must update this name to match your bucket name\.

1. Choose **Save**\.

   A warning appears indicating that the bucket has public access\. In **Bucket Policy**, a **Public** label appears\.

   If you see an error that says `Policy has invalid resource`, confirm that the bucket name in the bucket policy matches your bucket name\. For information about adding a bucket policy, see [How Do I Add an S3 Bucket Policy?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/add-bucket-policy.html)

   If you get an **Error \- Access denied** warning and the **Bucket policy editor** does not allow you to save the bucket policy, check your account\-level and bucket\-level block public access settings to confirm that you allow public access to the bucket\.

### Object access control lists<a name="object-acl"></a>

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