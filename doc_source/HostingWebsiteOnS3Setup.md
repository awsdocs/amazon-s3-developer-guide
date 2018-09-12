# Example: Setting up a Static Website<a name="HostingWebsiteOnS3Setup"></a>

You can configure an Amazon S3 bucket to function like a website\. This example walks you through the steps of hosting a website on Amazon S3\.

**Topics**
+ [Step 1: Creating a Bucket and Configuring It as a Website](#step1-create-bucket-config-as-website)
+ [Step 2: Adding a Bucket Policy That Makes Your Bucket Content Publicly Available](#step2-add-bucket-policy-make-content-public)
+ [Step 3: Uploading an Index Document](#step3-upload-index-doc)
+ [Step 4: Testing Your Website](#step4-test-web-site)

## Step 1: Creating a Bucket and Configuring It as a Website<a name="step1-create-bucket-config-as-website"></a>

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3)\.

1. Create a bucket\.

   For step\-by\-step instructions, see [How Do I Create an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-bucket.html) in *Amazon Simple Storage Service Console User Guide*\.

   For bucket naming guidelines, see [Bucket Restrictions and Limitations](BucketRestrictions.md)\. If you have a registered domain name, for additional information about bucket naming, see [Customizing Amazon S3 URLs with CNAMEs](VirtualHosting.md#VirtualHostingCustomURLs)\.

1. Open the bucket **Properties** pane, choose **Static Website Hosting**, and do the following:

   1. Choose **Enable website hosting**\.

   1. In the **Index Document** box, type the name of your index document\. The name is typically `index.html`\.

   1. Choose **Save** to save the website configuration\.

   1. Write down the **Endpoint**\.

      This is the Amazon S3\-provided website endpoint for your bucket\. You use this endpoint in the following steps to test your website\.

## Step 2: Adding a Bucket Policy That Makes Your Bucket Content Publicly Available<a name="step2-add-bucket-policy-make-content-public"></a>

1. In the **Properties** pane for the bucket, choose **Permissions**\.

1. Choose **Add Bucket Policy**\.

1. To host a website, your bucket must have public read access\. It is intentional that everyone in the world will have read access to this bucket\. Copy the following bucket policy, and then paste it in the Bucket Policy Editor\. 

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

## Step 3: Uploading an Index Document<a name="step3-upload-index-doc"></a>

1. Create a document\. Give it the same name that you gave the index document earlier\.

1. Using the console, upload the index document to your bucket\.

   For instructions, see [Uploading S3 Objects](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/upload-objects.html) in the *Amazon Simple Storage Service Console User Guide*\.

## Step 4: Testing Your Website<a name="step4-test-web-site"></a>

Type the following URL in the browser, replacing *example\-bucket* with the name of your bucket and *website\-region* with the name of the AWS Region where you deployed your bucket\. For information about AWS Region names, see [Website Endpoints](WebsiteEndpoints.md) \)\. 

```
1. http://example-bucket.s3-website-region.amazonaws.com
```

If your browser displays your `index.html` page, the website was successfully deployed\.

**Note**  
HTTPS access to the website is not supported\.

You now have a website hosted on Amazon S3\. This website is available at the Amazon S3 website endpoint\. However, you might have a domain, such as `example.com`, that you want to use to serve the content from the website you created\. You might also want to use Amazon S3 root domain support to serve requests for both `http://www.example.com` and `http://example.com`\. This requires additional steps\. For an example, see [Example: Setting up a Static Website Using a Custom Domain](website-hosting-custom-domain-walkthrough.md)\. 