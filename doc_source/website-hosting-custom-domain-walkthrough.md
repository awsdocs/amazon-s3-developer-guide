# Example: Setting Up a Static Website Using a Custom Domain Name Registered with Route 53<a name="website-hosting-custom-domain-walkthrough"></a>

Suppose that you want to host your static website on Amazon S3\. You've registered a domain with Amazon Route 53 \(for example, `example.com`\), and you want requests for `http://www.example.com` and `http://example.com` to be served from your Amazon S3 content\. You can use this walkthrough to learn how to host a static website and create redirects on Amazon S3 for a website with a custom domain name that is registered with Route 53\. You can work with an existing website that you want to host on Amazon S3, or you use this walkthrough to start from scratch\. 

**Topics**
+ [Before You Begin](#root-domain-walkthrough-before-you-begin)
+ [Step 1: Register a Custom Domain with Route 53](#website-hosting-custom-domain-walkthrough-domain-registry)
+ [Step 2: Create Two Buckets](#root-domain-walkthrough-create-buckets)
+ [Step 3: Configure Your Root Domain Bucket for Website Hosting](#root-domain-walkthrough-configure-bucket-aswebsite)
+ [Step 4: Configure Your Subdomain Bucket for Website Redirect](#root-domain-walkthrough-configure-redirect)
+ [Step 5: Configure Logging for Website Traffic](#root-domain-walkthrough-configure-logging)
+ [Step 6: Upload Index and Website Content](#upload-website-content)
+ [Step 7: Edit Block Public Access Settings](#root-domain-walkthrough-configure-bucket-permissions)
+ [Step 8: Attach a Bucket Policy](#add-bucket-policy-root-domain)
+ [Step 9: Test Your Domain Endpoint](#root-domain-walkthrough-test-website)
+ [Step 10: Add Alias Records for Your Domain and Subdomain](#root-domain-walkthrough-add-record-to-hostedzone)
+ [Step 11: Test the Website](#root-domain-testing)

## Before You Begin<a name="root-domain-walkthrough-before-you-begin"></a>

As you follow the steps in this example, you work with the following services:

**Amazon Route 53 –** You use Route 53 to register domains and to define where you want to route internet traffic for your domain\. The example shows how to create Route 53 alias records that route traffic for your domain \(`example.com`\) and subdomain \(`www.example.com`\) to an Amazon S3 bucket that contains an HTML file\.

**Amazon S3 –** You use Amazon S3 to create buckets, upload a sample website page, configure permissions so that everyone can see the content, and then configure the buckets for website hosting\.

## Step 1: Register a Custom Domain with Route 53<a name="website-hosting-custom-domain-walkthrough-domain-registry"></a>

If you don't already have a registered domain name, such as `example.com`, register one with Route 53\. For more information, see [Registering a New Domain](https://docs.aws.amazon.com/Route53/latest/DeveloperGuide/domain-register.html) in the *Amazon Route 53 Developer Guide*\. After you register your domain name, you can create and configure your Amazon S3 buckets for website hosting\. 

## Step 2: Create Two Buckets<a name="root-domain-walkthrough-create-buckets"></a>

To support requests from both the root domain and subdomain, you create two buckets:
+ **Domain bucket** ‐ `example.com`
+ **Subdomain bucket** ‐ `www.example.com` 

You host your content out of the root domain bucket \(`example.com`\)\. You create a redirect request for the subdomain bucket \(`www.example.com`\)\. If someone enters `www.example.com` in their browser, they are redirected to `example.com` and see the content that is hosted in the Amazon S3 bucket with that name\. 

**To create your buckets for website hosting**

The following instructions provide an overview of how to create your buckets for website hosting\. For detailed, step\-by\-step instructions on creating a bucket, see [How Do I Create an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-bucket.html) in the *Amazon Simple Storage Service Console User Guide*\.

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. Create your root domain bucket: 

   1. Choose **Create bucket**\.

   1. Enter the **Bucket name** \(for example, `example.com`\)\.

   1. Choose the Region where you want to create the bucket\. 

      Choose a Region close to you to minimize latency and costs, or to address regulatory requirements\. The Region that you choose determines your Amazon S3 website endpoint\. For more information, see [Website Endpoints](WebsiteEndpoints.md)\.

   1. To accept the default settings and create the bucket, choose **Create**\.

1. Create your subdomain bucket: 

   1. Choose **Create bucket**\.

   1. Enter the **Bucket name** \(for example, `www.example.com`\)\.

   1. Choose the Region where you want to create the bucket\. 

      Choose a Region close to you to minimize latency and costs, or to address regulatory requirements\. The Region that you choose determines your Amazon S3 website endpoint\. For more information, see [Website Endpoints](WebsiteEndpoints.md)\.

   1. To accept the default settings and create the bucket, choose **Create**\.

In the next step, you configure `example.com` for website hosting\. 

## Step 3: Configure Your Root Domain Bucket for Website Hosting<a name="root-domain-walkthrough-configure-bucket-aswebsite"></a>

In this step, you configure your root domain bucket \(`example.com`\) as a website\. This bucket will contain your website content\. When you configure a bucket for website hosting, you can access the website using the [Website Endpoints](WebsiteEndpoints.md)\. 

**To configure your bucket for website hosting**

1. On the Amazon S3 console, in the **S3 buckets** list, choose the bucket that has the same name as your domain\.

   The bucket that is named after your domain contains the website content\.

1. Choose **Properties**\.

1. Choose **Static website hosting**\.

1. Choose **Use this bucket to host a website**\.

1. In the **Index Document** box, enter the name of your index page\. 

   The file name of the home page of a website is typically `index.html`, but you can give it any name\. In [Step 6: Upload Index and Website Content](#upload-website-content), you will upload the `index.html` document for your static website\. If you have not created a website, the instructions for creating the `index.html` document are in [Step 6: Upload Index and Website Content](#upload-website-content)\.
**Note**  
If you don't enter the name of your index page, the **Save** button won't become active, and you won't be able to configure your bucket for static website hosting\.

1. Choose **Save**\.  
![\[Console screenshot showing the Static website hosting dialog box with appropriate settings.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/staticwebsitehosting20.png)

In the next step, you configure your subdomain \(`www.example.com`\) to redirect requests to your domain \(`example.com`\)\. 

## Step 4: Configure Your Subdomain Bucket for Website Redirect<a name="root-domain-walkthrough-configure-redirect"></a>

After you configure your root domain bucket for website hosting, you can configure your subdomain bucket to redirect all requests to the domain\. In this example, all requests for `www.example.com` are redirected to `example.com`\.

**To redirect requests from `www.example.com` to `example.com`**

1. On the Amazon S3 console, in the **S3 buckets** list, choose your subdomain bucket \( `www.example.com` in this example\)\.

1. Choose **Properties**\.

1. Choose **Static website hosting**\.

1. Choose **Redirect requests**\. 

1. In the **Target bucket or domain** box, enter your domain \(for example, **example\.com**\)\.

1. In the **Protocol** box, enter **http**\.

1. Choose **Save**\.

## Step 5: Configure Logging for Website Traffic<a name="root-domain-walkthrough-configure-logging"></a>

Optionally, you can configure logging for your root domain bucket to track the number of visitors who access your website\. For more information, see [\(Optional\) Configuring Web Traffic Logging](LoggingWebsiteTraffic.md)\.

## Step 6: Upload Index and Website Content<a name="upload-website-content"></a>

You have now configured your root domain bucket for website hosting and your subdomain bucket for the redirect\. Next, you upload your index document and optional website content to your root domain bucket\. In [Step 3: Configure Your Root Domain Bucket for Website Hosting](#root-domain-walkthrough-configure-bucket-aswebsite), you entered the name of the index document\. The HTML file that you upload to your bucket to serve as your website index must have the same file name\. 

**To create an `index.html` file**  
You can use the following HTML to create your `index.html`\.

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

**To upload your index and website content**

1. In the **Bucket name** list, choose your domain bucket \(for example, `example.com`\.

1. Upload your index document to your domain bucket \(for example, `example.com`\):
   + Drag and drop the index file into the console bucket listing\.
   + Choose **Upload**, and follow the prompts to choose and upload the index file\.

1. \(Optional\) Upload your website content to your domain bucket\.

For step\-by\-step instructions, see [How Do I Upload an Object to an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/upload-objects.html) in the *Amazon Simple Storage Service Console User Guide*\. 

## Step 7: Edit Block Public Access Settings<a name="root-domain-walkthrough-configure-bucket-permissions"></a>

The bucket that you use to host a website must have public access\. By default, Amazon S3 blocks public access to your account and buckets\. To grant public access, you must edit the Block Public Access settings for the bucket\. In this example, you edit these settings for the domain bucket \(`example.com`\)\.

**Warning**  
Before you edit the Amazon S3 Block Public Access settings, confirm that you want anyone on the internet to be able to access your bucket\. We recommend that you block all public access to your buckets unless you require a public bucket for a specific use case, such as a public static website\.

1. Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. Select the bucket that you have configured as a static website, and choose **Edit public access settings**\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/edit-public-access.png)

1. Clear **Block *all* public access**, and choose **Save**\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/edit-public-access-clear.png)

1. In the confirmation box, enter **confirm**, and then choose **Confirm**\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/edit-public-access-confirm.png)

   Under **S3 buckets**, the **Access** for your bucket updates to **Objects can be public**\. You can now add a bucket policy to make the objects in the bucket publicly readable\. If the **Access** still displays as **Bucket and objects not public**, you might have to [edit the block public access settings](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/block-public-access-account.html) for your account before adding a bucket policy\.

## Step 8: Attach a Bucket Policy<a name="add-bucket-policy-root-domain"></a>

After you edit the Block Public Access settings for your root domain bucket, you can attach a bucket policy that grants public read access\. In this example, you attach a bucket policy to the `example.com` bucket\.
+ To grant public read access, attach the following bucket policy to the bucket that you use to host your website\. In the policy, replace *example\.com* with the name of your bucket\. 

  For step\-by\-step instructions on attaching a bucket policy, see [How Do I Add an S3 Bucket Policy?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/add-bucket-policy.html) in the *Amazon Simple Storage Service Console User Guide*\. 

  ```
   1. {
   2.   "Version":"2012-10-17",
   3.   "Statement":[{
   4. 	"Sid":"PublicReadGetObject",
   5.         "Effect":"Allow",
   6. 	  "Principal": "*",
   7.       "Action":["s3:GetObject"],
   8.       "Resource":["arn:aws:s3:::example.com/*"
   9.       ]
  10.     }
  11.   ]
  12. 
  13. }
  ```

  After you add a bucket policy, under **S3 buckets**, the **Access** for your bucket updates to **Public**\. If the **Access** appears as **Only authorized users of this account**, you might have to [edit the Block Public Access settings](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/block-public-access-account.html) for your account\.
**Important**  
The preceding policy is an example only and allows full access to the contents of your bucket\. For more information about security best practices, see [How can I secure the files in my Amazon S3 bucket?](https://aws.amazon.com/premiumsupport/knowledge-center/secure-s3-resources/)

  In the next step, you can figure out your website endpoints and test your domain endpoint\.

## Step 9: Test Your Domain Endpoint<a name="root-domain-walkthrough-test-website"></a>

After you configure your domain bucket to host a public website and your subdomain bucket to redirect, you can test your domain endpoint\. For more information, see [Website Endpoints](WebsiteEndpoints.md)\.

Depending on your Region, Amazon S3 website endpoints follow one of these two formats:

```
http://bucket-name.s3-website.Region.amazonaws.com
```

```
http://bucket-name.s3-website-Region.amazonaws.com
```

For a complete list of Amazon S3 website endpoints, see [Amazon S3 Website Endpoints](https://docs.aws.amazon.com/general/latest/gr/s3.html#s3_website_region_endpoints)\.

## To test the domain endpoint
+ In your web browser, enter the domain endpoint URL\. 

  The browser should display the index document that you uploaded to the bucket\. 

In the next step, you use Amazon Route 53 to enable customers to use both of your custom URLs to navigate to your site\. 

## Step 10: Add Alias Records for Your Domain and Subdomain<a name="root-domain-walkthrough-add-record-to-hostedzone"></a>

In this step, you create the alias records that you add to the hosted zone for your domain maps `example.com` and `www.example.com`\. Instead of using IP addresses, the alias records use the Amazon S3 website endpoints\. Amazon Route 53 maintains a mapping between the alias records and the IP addresses where the Amazon S3 buckets reside\. You create two alias records, one for your root domain and one for your subdomain\.

**To add an alias record for your root domain \(`example.com`\)**

1. Open the Route 53 console at [https://console\.aws\.amazon\.com/route53/](https://console.aws.amazon.com/route53/)\.
**Note**  
If you don't already use Route 53, see [Step 1: Register a Domain](https://docs.aws.amazon.com/Route53/latest/DeveloperGuide/getting-started.html#getting-started-find-domain-name) in the *Amazon Route 53 Developer Guide*\. After completing your setup, you can resume the instructions\.

1. Choose **Hosted Zones**\.

1. In the list of hosted zones, choose the name of the hosted zone that matches your domain name\.

1. Choose **Create Record Set**\.

1. Specify the following values:  
**Name**  
Accept the default value, which is the name of your hosted zone and your domain\.   
For the root domain, you don't need to enter any additional information in the **Name** field\.  
**Type**  
Choose **A – IPv4 address**\.  
**Alias**  
Choose **Yes**\.  
**Alias Target**  
In the **S3 website endpoints** section of the list, choose your bucket name\.   
The bucket name should match the name that appears in the **Name** box\. In the **Alias Target** listing, the bucket name is followed by the Amazon S3 website endpoint for the Region where the bucket was created, for example `example.com (s3-website-us-west-2)`\. **Alias Target** lists a bucket if:  
   + You configured the bucket as a static website\.
   + The bucket name is the same as the name of the record that you're creating\.
   + The current AWS account created the bucket\.
If your bucket does not appear in the **Alias Target** listing, enter the Amazon S3 website endpoint for the Region where the bucket was created, for example, `s3-website-us-west-2`\. For a complete list of Amazon S3 website endpoints, see [Amazon S3 Website Endpoints](https://docs.aws.amazon.com/general/latest/gr/s3.html#s3_website_region_endpoints)\. For more information about the alias target, see [Alias Target](https://docs.aws.amazon.com/Route53/latest/DeveloperGuide/resource-record-sets-values-alias.html#rrsets-values-alias-alias-target) in the *Amazon Route 53 Developer Guide*\.  
**Routing Policy**  
Accept the default value of **Simple**\.  
**Evaluate Target Health**  
Accept the default value of **No**\.

1. Choose **Create**\.

**To add an alias record for your subdomain \(`www.example.com`\)**

1. In the hosted zone for your root domain \(`example.com`\), choose **Create Record Set**\.

1. Specify the following values:  
**Name**  
For the subdomain, enter `www` in the box\.   
**Type**  
Choose **A – IPv4 address**\.  
**Alias**  
Choose **Yes**\.  
**Alias Target**  
In the **S3 website endpoints** section of the list, choose the same bucket name that appears in the **Name** field—for example, `www.example.com (s3-website-us-west-2)`\.  
**Routing Policy**  
Accept the default value of **Simple**\.  
**Evaluate Target Health**  
Accept the default value of **No**\.

1. Choose **Create**\.

**Note**  
Changes generally propagate to all Route 53 servers within 60 seconds\. When propagation is done, you can route traffic to your Amazon S3 bucket by using the names of the alias records that you created in this procedure\.

## Step 11: Test the Website<a name="root-domain-testing"></a>

Verify that the website and the redirect work correctly\. In your browser, enter your URLs\. In this example, you can try the following URLs:
+ **Domain** \(`http://example.com`\) – Displays the index document in the `example.com` bucket\.
+ **Subdomain **\(`http://www.example.com`\) – Redirects your request to `http://example.com`\. You see the index document in the `example.com` bucket\.

In some cases, you might need to clear the cache of your web browser to see the expected behavior\.