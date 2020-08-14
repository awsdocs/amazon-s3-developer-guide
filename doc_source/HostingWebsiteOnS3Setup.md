# Configuring a static website<a name="HostingWebsiteOnS3Setup"></a>

You can configure an Amazon S3 bucket to function like a website\. This example walks you through the steps of hosting a website on Amazon S3\.

**Note**  
Amazon S3 does not support HTTPS access to the website\. If you want to use HTTPS, you can use Amazon CloudFront to serve a static website hosted on Amazon S3\.  
For more information, see [How do I use CloudFront to serve a static website hosted on Amazon S3?](http://aws.amazon.com/premiumsupport/knowledge-center/cloudfront-serve-static-website/) and [ Requiring HTTPS for communication between viewers and CloudFront](https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/using-https-viewers-to-cloudfront.html)\.

**Topics**
+ [Step 1: Create a bucket](#step1-create-bucket-config-as-website)
+ [Step 2: Enable static website hosting](#step2-create-bucket-config-as-website)
+ [Step 3: Edit block public access settings](#step2-edit-block-public-access)
+ [Step 4: Add a bucket policy that makes your bucket content publicly available](#step3-add-bucket-policy-make-content-public)
+ [Step 5: Configure an index document](#step3-upload-index-doc)
+ [Step 6: Test your website endpoint](#step4-test-web-site)
+ [Step 7: Clean up](#getting-started-cleanup-s3-website-overview)

## Step 1: Create a bucket<a name="step1-create-bucket-config-as-website"></a>

The following instructions provide an overview of how to create your buckets for website hosting\. For detailed, step\-by\-step instructions on creating a bucket, see [How Do I Create an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-bucket.html) in the *Amazon Simple Storage Service Console User Guide*\.

**To create a bucket**

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. Choose **Create bucket**\.

1. Enter the **Bucket name** \(for example, `example.com`\)\.

1. Choose the Region where you want to create the bucket\. 

   Choose a Region close to you to minimize latency and costs, or to address regulatory requirements\. The Region that you choose determines your Amazon S3 website endpoint\. For more information, see [Website endpoints](WebsiteEndpoints.md)\.

1. To accept the default settings and create the bucket, choose **Create**\.

## Step 2: Enable static website hosting<a name="step2-create-bucket-config-as-website"></a>

After you create a bucket, you can enable static website hosting for your bucket\. You can create a new bucket or use an existing bucket\.

**To enable static website hosting**

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. In the **Bucket name** list, choose the bucket that you want to use for your static website\.

1. Choose **Properties**\.

1. Choose **Static website hosting**\.

1. Choose **Use this bucket to host a website**\.

1. Enter the name of your index document\. 

   The index document name is typically `index.html`\. The index document name is case sensitive and must exactly match the file name of the HTML index document that you plan to upload to your S3 bucket\. For more information, see [Configuring an index document](IndexDocumentSupport.md)\.

1. \(Optional\) If you want to add a custom error document, in the **Error document** box, enter the key name for the error document \(for example, **error\.html**\)\. 

   The error document name is case sensitive and must exactly match the file name of the HTML error document that you plan to upload to your S3 bucket\. For more information, see [\(Optional\) configuring a custom error document](CustomErrorDocSupport.md)\.

1. \(Optional\) If you want to specify advanced redirection rules, in **Edit redirection rules**, use XML to describe the rules\.

   For more information, see [Configuring advanced conditional redirects](how-to-page-redirect.md#advanced-conditional-redirects)\.

1. Under **Static website hosting**, note the **Endpoint**\.

   The **Endpoint** is the Amazon S3 website endpoint for your bucket\. After you finish configuring your bucket as a static website, you can use this endpoint to test your website\.

1. Choose **Save**\.

## Step 3: Edit block public access settings<a name="step2-edit-block-public-access"></a>

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

## Step 4: Add a bucket policy that makes your bucket content publicly available<a name="step3-add-bucket-policy-make-content-public"></a>

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

## Step 5: Configure an index document<a name="step3-upload-index-doc"></a>

When you enable static website hosting for your bucket, you enter the name of the index document \(for example, **index\.html**\)\. After you enable static website hosting for the bucket, you upload an HTML file with this index document name to your bucket\.

**To configure the index document**

1. Create an `index.html` file\.

   If you don't have an `index.html` file, you can use the following HTML to create one:

   ```
   <html xmlns="http://www.w3.org/1999/xhtml" >
   <head>
       <title>My Website Home Page</title>
   </head>
   <body>
     <h1>Welcome to my website</h1>
     <p>Now hosted on Amazon S3!</p>
   </body>
   </html>
   ```

1. Save the index file locally with the *exact* index document name that you entered when you enabled static website hosting for your bucket \(for example, `index.html`\)\.

   The index document file name must exactly match the index document name that you enter in the **Static website hosting** dialog box\. The index document name is case sensitive\. For example, if you enter `index.html` for the **Index document** name in the **Static website hosting** dialog box, your index document file name must also be `index.html` and not `Index.html`\.

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. In the **Buckets** list, choose the name of the bucket that you want to use to host a static website\.

1. Enable static website hosting for your bucket, and enter the exact name of your index document \(for example, `index.html`\)\. For more information, see [Enabling website hosting](EnableWebsiteHosting.md)\.

   After enabling static website hosting, proceed to step 6\. 

1. To upload the index document to your bucket, do one of the following:
   + Drag and drop the index file into the console bucket listing\.
   + Choose **Upload**, and follow the prompts to choose and upload the index file\.

   For step\-by\-step instructions, see [How Do I Upload Files and Folders to an Amazon S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/upload-objects.html) in the *Amazon Simple Storage Service Console User Guide*\.

1. \(Optional\) Upload other website content to your bucket\.

## Step 6: Test your website endpoint<a name="step4-test-web-site"></a>

After you configure static website hosting for your bucket, you can test your website endpoint\.

**Note**  
Amazon S3 does not support HTTPS access to the website\. If you want to use HTTPS, you can use Amazon CloudFront to serve a static website hosted on Amazon S3\.  
For more information, see [How do I use CloudFront to serve a static website hosted on Amazon S3?](http://aws.amazon.com/premiumsupport/knowledge-center/cloudfront-serve-static-website/) and [Requiring HTTPS for communication between viewers and CloudFront](https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/using-https-viewers-to-cloudfront.html)\.

**To test your website endpoint**

If you noted your website endpoint when you enabled static website hosting, to test your website, enter the website endpoint in your browser\. If your browser displays your `index.html` page, the website was successfully deployed\. For more information, see [Amazon S3 Website Endpoints](https://docs.aws.amazon.com/AmazonS3/latest/dev/WebsiteEndpoints.html)\.

If you need to get your website endpoint before testing, follow these steps:

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. In **Buckets** list, choose the name of the bucket that you want to use to host a static website\.

1. Choose **Properties**\.

1. Choose **Static website hosting**\.

1. To test your website endpoint, next to **Endpoint**, choose your website endpoint\.

   If your browser displays your `index.html` page, the website was successfully deployed\.

You now have a website hosted on Amazon S3\. This website is available at the Amazon S3 website endpoint\. However, you might have a domain, such as `example.com`, that you want to use to serve the content from the website you created\. You might also want to use Amazon S3 root domain support to serve requests for both `http://www.example.com` and `http://example.com`\. This requires additional steps\. For an example, see [Configuring a static website using a custom domain registered with Route 53](website-hosting-custom-domain-walkthrough.md)\. 

## Step 7: Clean up<a name="getting-started-cleanup-s3-website-overview"></a>

If you created your static website only as a learning exercise, delete the AWS resources that you allocated so that you no longer accrue charges\. After you delete your AWS resources, your website is no longer available\. For more information, see [How Do I Delete an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/delete-bucket.html) in the *Amazon Simple Storage Service Console User Guide*\.