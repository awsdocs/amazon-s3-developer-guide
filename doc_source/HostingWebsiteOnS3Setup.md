# Example: Setting up a Static Website<a name="HostingWebsiteOnS3Setup"></a>

You can configure an Amazon S3 bucket to function like a website\. This example walks you through the steps of hosting a website on Amazon S3\.

**Topics**
+ [Step 1: Creating a Bucket and Configuring It as a Website](#step1-create-bucket-config-as-website)
+ [Editing Block Public Access Settings](#step2-edit-block-public-access)
+ [Step 3: Adding a Bucket Policy That Makes Your Bucket Content Publicly Available](#step3-add-bucket-policy-make-content-public)
+ [Step 4: Uploading an Index Document](#step3-upload-index-doc)
+ [Step 5: Testing Your Website](#step4-test-web-site)

## Step 1: Creating a Bucket and Configuring It as a Website<a name="step1-create-bucket-config-as-website"></a>

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3)\.

1. Create a bucket\.

   For step\-by\-step instructions, see [How Do I Create an Amazon S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-bucket.html) in *Amazon Simple Storage Service Console User Guide*\.

   For bucket naming guidelines, see [Bucket Restrictions and Limitations](BucketRestrictions.md)\. If you have a registered domain name, for additional information about bucket naming, see [Customizing Amazon S3 URLs with CNAMEs](VirtualHosting.md#VirtualHostingCustomURLs)\.

1. Open the bucket **Properties** pane, choose **Static Website Hosting**, and do the following:

   1. Choose **Use this bucket to host a website**\.

   1. In the **Index Document** box, type the name of your index document\. The name is typically `index.html`\.

   1.  Choose **Save** to save the website configuration\.

   1. Write down the **Endpoint**\.

      This is the Amazon S3\-provided website endpoint for your bucket\. You use this endpoint in the following steps to test your website\.

## Editing Block Public Access Settings<a name="step2-edit-block-public-access"></a>

By default, Amazon S3 blocks public access to your account and buckets\. If you want to use a bucket to host a static website, you can use these steps to edit block public access settings: 

1. Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. Select the bucket that you have configured as a static website, and choose **Edit public access settings**\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/edit-public-access.png)

1. Clear **Block *all* public access**, and choose **Save**\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/edit-public-access-clear.png)

1. In the confirmation box, enter **confirm**, and then choose **Confirm**\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/edit-public-access-confirm.png)

   Under **S3 buckets**, the **Access** for your bucket updates to **Objects can be public**\. You can now add a bucket policy to make the objects in the bucket publicly readable\. If the **Access** still displays as **Bucket and objects not public**, you might have to [edit the block public access settings](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/block-public-access-account.html) for your account before adding a bucket policy\.

## Step 3: Adding a Bucket Policy That Makes Your Bucket Content Publicly Available<a name="step3-add-bucket-policy-make-content-public"></a>

1. In the **Properties** pane for the bucket, choose **Permissions**\.

1. Choose **Bucket Policy**\.

1. To grant public read access for your website, copy the following bucket policy, and paste it in the **Bucket policy editor**\.

   ```
    1. {
    2.    "Version":"2012-10-17",
    3.    "Statement":[{
    4.  	"Sid":"PublicReadForGetBucketObjects",
    5.          "Effect":"Allow",
    6.  	  "Principal": "*",
    7.        "Action":["s3:GetObject"],
    8.        "Resource":["arn:aws:s3:::example-bucket/*"
    9.        ]
   10.      }
   11.    ]
   12.  }
   ```

1. In the policy, replace *example\-bucket* with the name of your bucket\.

1. Choose **Save**\.

   In your Amazon S3 bucket listing, the **Access** for your bucket updates to **Public**\. 

## Step 4: Uploading an Index Document<a name="step3-upload-index-doc"></a>

1. Create a document\. Give it the same name that you gave the index document earlier\.

1. Using the console, upload the index document to your bucket\.

   For instructions, see [How Do I Upload Files and Folders to an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/upload-objects.html) in the *Amazon Simple Storage Service Console User Guide*\.

## Step 5: Testing Your Website<a name="step4-test-web-site"></a>

Enter the following URL in the browser, replacing *example\-bucket* with the name of your bucket and *website\-region* with the name of the AWS Region where you deployed your bucket\. For information about AWS Region names, see [Website Endpoints](WebsiteEndpoints.md) \)\. 

Amazon S3 Region\-specific website endpoints follow this format:

```
http://bucket-name.s3-website.Region.amazonaws.com
```

For example, if you create a bucket named `example-bucket` in the US West \(Oregon\) Region, your website is available at the following URL:

```
http://example-bucket.s3-website.us-west-2.amazonaws.com
```

If your browser displays your `index.html` page, the website was successfully deployed\.

**Note**  
HTTPS access to the website is not supported\.

You now have a website hosted on Amazon S3\. This website is available at the Amazon S3 website endpoint\. However, you might have a domain, such as `example.com`, that you want to use to serve the content from the website you created\. You might also want to use Amazon S3 root domain support to serve requests for both `http://www.example.com` and `http://example.com`\. This requires additional steps\. For an example, see [Example: Setting Up a Static Website Using a Custom Domain](website-hosting-custom-domain-walkthrough.md)\. 