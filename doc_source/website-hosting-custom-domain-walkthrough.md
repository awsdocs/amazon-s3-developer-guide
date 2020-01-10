# Example: Setting Up a Static Website Using a Custom Domain<a name="website-hosting-custom-domain-walkthrough"></a>

Suppose that you want to host your static website on Amazon S3\. You registered a domain \(for example, `example.com`\), and you want requests for `http://www.example.com` and `http://example.com` to be served from your Amazon S3 content\. Whether you have an existing static website that you want to host on Amazon S3 or you are starting from scratch, you can use this example to learn how to host websites and create redirects on Amazon S3\.

**Topics**
+ [Before You Begin](#root-domain-walkthrough-before-you-begin)
+ [Step 1: Register a Domain](#website-hosting-custom-domain-walkthrough-domain-registry)
+ [Step 2: Create Two Buckets](#root-domain-walkthrough-create-buckets)
+ [Step 3: Configure Your Root Domain Bucket for Website Hosting](#root-domain-walkthrough-configure-bucket-aswebsite)
+ [Step 3: Configure Your Subdomain Bucket for Website Redirect](#root-domain-walkthrough-configure-redirect)
+ [Step 4: Configure Logging for Website Traffic](#root-domain-walkthrough-configure-logging)
+ [Step 5: Upload Index and Website Content](#upload-website-content)
+ [Step 6: Edit Block Public Access Settings](#root-domain-walkthrough-configure-bucket-permissions)
+ [Step 7: Attach a Bucket Policy](#add-bucket-policy-root-domain)
+ [Step 8: Test Your Domain Endpoint](#root-domain-walkthrough-test-website)
+ [Step 9: Add Alias Records for Your Domain and Subdomain](#root-domain-walkthrough-add-arecord-to-hostedzone)
+ [Step 10: Test the Website](#root-domain-testing)

## Before You Begin<a name="root-domain-walkthrough-before-you-begin"></a>

As you follow the steps in this example, you work with the following services:

**Amazon Route 53 –** You use Route 53 to register domains and to define where you want to route internet traffic for your domain\. The example shows how to create Route 53 alias records that route traffic for your domain \(`example.com`\) and subdomain \(`www.example.com`\) to an Amazon S3 bucket that contains an HTML file\.

**Amazon S3 –** You use Amazon S3 to create buckets, upload a sample website page, configure permissions so that everyone can see the content, and then configure the buckets for website hosting\.

## Step 1: Register a Domain<a name="website-hosting-custom-domain-walkthrough-domain-registry"></a>

If you don't already have a registered domain name, such as `example.com`, register one with Route 53\. For more information, see [Registering a New Domain](https://docs.aws.amazon.com/Route53/latest/DeveloperGuide/domain-register.html) in the *Amazon Route 53 Developer Guide*\. When you have a registered domain name, your next tasks are to create and configure Amazon S3 buckets for website hosting and to upload your website content\. 

## Step 2: Create Two Buckets<a name="root-domain-walkthrough-create-buckets"></a>

To support requests from both the root domain and subdomain, you create two buckets:
+ **Domain Bucket** ‐ `example.com`
+ **Subdomain Bucket** ‐ `www.example.com` 

You host your content out of the root domain bucket \(`example.com`\)\. You create a redirect request for the subdomain bucket \(`www.example.com`\)\. The redirect request redirects users who try to access `www.example.com` to the root domain\. In other words, if someone enters `www.example.com` in their browser, they are redirected to `example.com` and see the content that is hosted in the Amazon S3 bucket with that name\. First, you create two buckets, one for your domain and the other for your subdomain\. The bucket names must match the name of the website that you are hosting\. For example, to host your `example.com` website on Amazon S3, you would create the following buckets:

**To create your buckets for website hosting**

The instructions below provide an overview of how to create your buckets for website hosting\. For detailed, step\-by\-step instructions on creating a bucket, see [How Do I Create an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-bucket.html) in the *Amazon Simple Storage Service Console User Guide*\.

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. Create two buckets: 

   1. Choose **Create bucket**\.

   1. Enter a **Bucket name**\.
      + One bucket name must match your domain \(for example, `example.com` and `www.example.com`\)\.
      + One bucket name must match your subdomain \(for example, `www.example.com`\)\.

   1. Choose the Region where you want to create the bucket\. 

      Choose a Region close to you to minimize latency and costs, or to address regulatory requirements\. The Region that you choose determines your Amazon S3 website endpoint\. For more information, see [Website Endpoints](WebsiteEndpoints.md)

   1. To accept the default settings and create the bucket, choose **Create**\.

In the next step, you configure `example.com` for website hosting\. 

## Step 3: Configure Your Root Domain Bucket for Website Hosting<a name="root-domain-walkthrough-configure-bucket-aswebsite"></a>

In this step, you configure your root domain bucket \(`example.com`\) as a website\. This bucket will contain your website content\. When you configure a bucket for website hosting, you can access the website using the Amazon S3 assigned bucket website endpoint\. 

**To configure your bucket for website hosting**

1. In the Amazon S3 console, in the **S3 buckets** list, choose the bucket with the same name as your domain\.

   The bucket named after your domain contains the website content\.

1. Choose **Properties**\.

1. Choose **Static website hosting**\.

1. Choose **Use this bucket to host a website**\.

1. In the **Index Document** box, enter the name of your index page\. 

   The file name of the home page of a website is typically `index.html`, but you can give it any name\. In [Step 5: Upload Index and Website Content](#upload-website-content), you will upload the `index.html` document for your static website\. If you have not created a website, the instructions for creating the `index.html` document are in [Step 5: Upload Index and Website Content](#upload-website-content)\.
**Note**  
If you do not enter the name of your index page, the **Save** button will not become active, and you will not be able to configure your bucket for static website hosting\.

1. Choose **Save**\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/staticwebsitehosting20.png)

In the next step, you configure your subdomain \(`www.example.com`\) to redirect requests to your domain \(`example.com`\)\. 

## Step 3: Configure Your Subdomain Bucket for Website Redirect<a name="root-domain-walkthrough-configure-redirect"></a>

Now that you have configured your root domain bucket for website hosting, you can configure your subdomain bucket to redirect all requests to the domain\. In this example, all requests for `www.example.com` are redirected to `example.com`\.

**To redirect requests from `www.example.com` to `example.com`**

1. In the Amazon S3 console, in the **S3 buckets** list, choose your subdomain bucket \( `www.example.com`, in this example\)\.

1. Choose **Properties**\.

1. Choose **Static website hosting**\.

1. Choose **Redirect requests**\. 

1. In the **Target bucket or domain** box, enter your domain \(for example, example\.com\)\.

1. In the **Protocol** box, enter `http`\.

1. Choose **Save**\.

## Step 4: Configure Logging for Website Traffic<a name="root-domain-walkthrough-configure-logging"></a>

Optionally, you can configure logging to track the number of visitors accessing your website\. To do that, you enable logging for the root domain bucket\. For more information, see [\(Optional\) Configuring Web Traffic Logging](LoggingWebsiteTraffic.md)\.

## Step 5: Upload Index and Website Content<a name="upload-website-content"></a>

Now that you've configured your root domain bucket for website hosting and your subdomain bucket for redirect, you can upload your index document and optional website content to your root domain bucket\. The content can be text files, family photos, videos—whatever you want\. If you have not yet created a website, then you only need the index file for this example\. In [Step 3: Configure Your Root Domain Bucket for Website Hosting](#root-domain-walkthrough-configure-bucket-aswebsite), you entered the name of the index document\. The HTML file that you upload to your bucket to serve as your website index must have the same file name\. 

You can create your index\.html file with the following HTML and then upload it to the bucket that will host the website\. In this example, you upload the `index.html` document to the domain bucket \(`example.com`\) that will serve the website content\.

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

1. Upload your index document to the root domain bucket \(for example, `example.com`\)\.

1. \(Optional\) Upload your website content to the root domain bucket \(for example, `example.com`\)\.

For step\-by\-step instructions, see [How Do I Upload an Object to an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/upload-objects.html) in the *Amazon Simple Storage Service Console User Guide*\. 

## Step 6: Edit Block Public Access Settings<a name="root-domain-walkthrough-configure-bucket-permissions"></a>

The bucket that you use to host a website must have public read access\. It is intentional that everyone in the world will have read access to this bucket\. By default, Amazon S3 blocks public access to your account and buckets\. To grant public read access, you must disable block public access for the bucket and write a bucket policy that allows public read access\. In this example, `example.com` contains the website content\. Therefore, you need to make this bucket publicly readable\. 

**Warning**  
When you turn off all block public access settings and add a bucket policy that enables public read access to a bucket, the bucket can be public ally accessed by anyone connected to the internet\. Confirm your intent to make your bucket public\.

1. Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. Select the bucket that you have configured as a static website, and choose **Edit public access settings**\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/edit-public-access.png)

1. Clear **Block *all* public access**, and choose **Save**\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/edit-public-access-clear.png)

1. In the confirmation box, enter **confirm**, and then choose **Confirm**\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/edit-public-access-confirm.png)

   Under **S3 buckets**, the **Access** for your bucket updates to **Objects can be public**\. You can now add a bucket policy to make the objects in the bucket publicly readable\. If the **Access** still displays as **Bucket and objects not public**, you might have to [edit the block public access settings](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/block-public-access-account.html) for your account before adding a bucket policy\.

## Step 7: Attach a Bucket Policy<a name="add-bucket-policy-root-domain"></a>

After you edit block public access settings for your root domain bucket, you can attach a bucket policy that grants public read access\. You should add the bucket policy to the root domain bucket that contains your website content, the same bucket for which you turned off block all public access\. In this example, you attach a bucket policy to the `example.com` bucket\.
+ To grant public read access, attach the following bucket policy to the bucket you use to host your website, substituting the name of your bucket for `example.com`\. 

  For step\-by\-step instructions to attach a bucket policy, see [How Do I Add an S3 Bucket Policy?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/add-bucket-policy.html) in the *Amazon Simple Storage Service Console User Guide*\. 

  ```
  {
     "Version":"2012-10-17",
     "Statement":[
        {
           "Sid":"PublicReadGetObject",
           "Effect":"Allow",
           "Principal":"*",
           "Action":[
              "s3:GetObject"
           ],
           "Resource":[
              "arn:aws:s3:::example.com/*"
           ]
        }
     ]
  }
  ```

  After you add a bucket policy, under **S3 buckets**, the **Access** for your bucket updates to **Public**\. If the **Access** appears as **Only authorized users of this account**, you might have to [edit the block public access settings](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/block-public-access-account.html) for your account\.
**Important**  
The preceding policy is an example only and allows full access to the contents of your bucket\. For more information about security best practices, see [How can I secure the files in my Amazon S3 bucket?](https://aws.amazon.com/premiumsupport/knowledge-center/secure-s3-resources/)

  In the next step, you can figure out your website endpoints and test your domain endpoint\.

## Step 8: Test Your Domain Endpoint<a name="root-domain-walkthrough-test-website"></a>

After you configure your domain bucket to host a public website and your subdomain bucket to redirect, you can figure out your website endpoints and test your domain endpoint\. For more information, see [Website Endpoints](WebsiteEndpoints.md)\.

Amazon S3 Region\-specific website endpoints follow this format:

```
http://bucket-name.s3-website.Region.amazonaws.com
```

If the `example.com` domain bucket is in the US West \(Oregon\) Region, the Amazon S3 website endpoint is as follows:

```
http://example.com.s3-website.us-west-2.amazonaws.com/
```

## To test the domain endpoint
+ To test your domain endpoint, enter the endpoint URL in your browser\. 

  The browser should display the index document that you uploaded to the bucket\. 

In the next step, you use Amazon Route 53 to enable customers to use both of your custom URLs to navigate to your site\. 

## Step 9: Add Alias Records for Your Domain and Subdomain<a name="root-domain-walkthrough-add-arecord-to-hostedzone"></a>

In this step, you create the alias records that you add to the hosted zone for your domain maps `example.com` and `www.example.com`\. Instead of using IP addresses, the alias records use the Amazon S3 website endpoints\. Amazon Route 53 maintains a mapping between the alias records and the IP addresses where the Amazon S3 buckets reside\. You create two alias records, one for your root domain and one for your subdomain\.

**To add an Alias record for your root domain \(`example.com`\)**

1. Open the Route 53 console at [https://console\.aws\.amazon\.com/route53/](https://console.aws.amazon.com/route53/)\.
**Note**  
If you don't already use Route 53, see [Step 1: Register a Domain](https://docs.aws.amazon.com/Route53/latest/DeveloperGuide//getting-started.html#getting-started-find-domain-name) in the *Amazon Route 53 Developer Guide*\. After completing your setup, you can resume the instructions\.

1. Choose **Hosted Zones**\.

1. In the list of hosted zones, choose the name of the hosted zone that matches your domain name\.

1. Choose **Create Record Set**\.

1. Specify the following values:  
**Name**  
Accept the default value, which is the name of your hosted zone and your domain\.   
For the root domain, you do not need to enter any additional information in the **Name** field\.  
**Type**  
Choose **A – IPv4 address**\.  
**Alias**  
Choose **Yes**\.  
**Alias Target**  
In the **S3 website endpoints**section of the list, choose the same bucket name that appears in the **Name** field, for example `example.com(s3-website.us-west-2)`\.  
**Routing Policy**  
Accept the default value of **Simple**\.  
**Evaluate Target Health**  
Accept the default value of **No**\.

   The screenshot below shows the alias record for the root domain, `example.com`:

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
In the **S3 website endpoints** section of the list, choose the same bucket name that appears in the **Name** field, for example, `www.example.com (s3-website.us-west-2)`\)\.  
**Routing Policy**  
Accept the default value of **Simple**\.  
**Evaluate Target Health**  
Accept the default value of **No**\.

   The screenshot below shows the alias record for `www.example.com`, the subdomain:  
![\[Screenshot showing record set for subdomain.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/route-53-subdomain.png)

1. Choose **Create**\.

**Note**  
Changes generally propagate to all Route 53 servers within 60 seconds\. When propagation is done, you'll be able to route traffic to your Amazon S3 bucket by using the names of the alias records that you created in this procedure\.

## Step 10: Test the Website<a name="root-domain-testing"></a>

Verify that the website and the redirect work correctly\. In your browser, enter your URLs\. In this example, you try the following URLs:
+ **Domain** \(`http://example.com`\) – Displays the index document in the `example.com` bucket\.
+ **Subdomain **\(`http://www.example.com`\) – Redirects your request to `http://example.com`\. You see the index document in the `example.com` bucket\.

In some cases, you might need to clear the cache of your web browser to see the expected behavior\.