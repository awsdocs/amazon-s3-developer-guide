# Example: Setting up a Static Website Using a Custom Domain<a name="website-hosting-custom-domain-walkthrough"></a>

Suppose that you want to host your static website on Amazon S3\. You registered a domain \(for example, `example.com`\), and you want requests for `http://www.example.com` and `http://example.com` to be served from your Amazon S3 content\. Whether you have an existing static website that you want to host on Amazon S3, or you are starting from scratch, use this example to learn how to host websites on Amazon S3\.

**Topics**
+ [Before You Begin](#root-domain-walkthrough-before-you-begin)
+ [Step 1: Register a Domain](#website-hosting-custom-domain-walkthrough-domain-registry)
+ [Step 2: Create and Configure Buckets and Upload Data](#root-domain-walkthrough-s3-tasks)
+ [Step 3: Add Alias Records for example\.com and www\.example\.com](#root-domain-walkthrough-add-arecord-to-hostedzone)
+ [Step 4: Testing](#root-domain-testing)

## Before You Begin<a name="root-domain-walkthrough-before-you-begin"></a>

As you follow the steps in this example, you work with the following services:

**Amazon Route 53 –** You use Route 53 to register domains and to define where you want to route internet traffic for your domain\. We explain how to create Route 53 alias records that route traffic for your domain \(example\.com\) and subdomain \(www\.example\.com\) to an Amazon S3 bucket that contains an HTML file\.

**Amazon S3 –** You use Amazon S3 to create buckets, upload a sample website page, configure permissions so that everyone can see the content, and then configure the buckets for website hosting\.

## Step 1: Register a Domain<a name="website-hosting-custom-domain-walkthrough-domain-registry"></a>

If you don't already have a registered domain name, such as `example.com`, register one with Route 53\. For more information, see [Registering a New Domain](http://docs.aws.amazon.com/Route53/latest/DeveloperGuide/domain-register.html) in the *Amazon Route 53 Developer Guide*\. When you have a registered domain name, your next tasks are to create and configure Amazon S3 buckets for website hosting and to upload your website content\. 

## Step 2: Create and Configure Buckets and Upload Data<a name="root-domain-walkthrough-s3-tasks"></a>

To support requests from both the root domain such as `example.com` and subdomain such as `www.example.com`, you create two buckets\. One bucket contains the content\. You configure the other bucket to redirect requests\.

### Step 2\.1: Create Two Buckets<a name="root-domain-walkthrough-create-buckets"></a>

The bucket names must match the names of the website that you are hosting\. For example, to host your `example.com` website on Amazon S3, you would create a bucket named `example.com`\. To host a website under `www.example.com`, you would name the bucket `www.example.com`\. In this example, your website supports requests from both `example.com` and `www.example.com`\. 

In this step, you sign in to the Amazon S3 console with your AWS account credentials and create the following two buckets\. 
+ `example.com` 
+ `www.example.com` 

**Note**  
Like domains, subdomains must have their own S3 buckets, and the buckets must share the exact names as the subdomains\. In this example, we are creating the `www.example.com` subdomain, so we also need an S3 bucket named www\.example\.com\.

**To create your buckets and upload your website content for hosting**

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. Create two buckets that match your domain name and subdomain\. For instance, `example.com` and `www.example.com`\.

   For step\-by\-step instructions, see [How Do I Create an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-bucket.html) in the *Amazon Simple Storage Service Console User Guide*\.

1. Upload your website data to the `example.com` bucket\. 

   You will host your content out of the root domain bucket \(`example.com`\), and you will redirect requests for `www.example.com` to the root domain bucket\. You can store content in either bucket\. For this example, you host content in the `example.com` bucket\. The content can be text files, family photos, videos—whatever you want\. If you have not yet created a website, then you only need one file for this example\. You can upload any file\. For example, you can create a file using the following HTML and upload it to the bucket\. The file name of the home page of a website is typically index\.html, but you can give it any name\. In a later step, you provide this file name as the index document name for your website\.

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

   For step\-by\-step instructions, see [How Do I Upload an Object to an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/upload-objects.html) in the *Amazon Simple Storage Service Console User Guide*\. 

1. To host a website, your bucket must have public read access\. It is intentional that everyone in the world will have read access to this bucket\. To grant public read access, attach the following bucket policy to the `example.com` bucket, substituting the name of your bucket for *example\.com*\. For step\-by\-step instructions to attach a bucket policy, see [How Do I Add an S3 Bucket Policy?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/add-bucket-policy.html) in the *Amazon Simple Storage Service Console User Guide*\. 

   ```
   {
     "Version":"2012-10-17",
     "Statement":[{
   	"Sid":"PublicReadGetObject",
           "Effect":"Allow",
   	  "Principal": "*",
         "Action":["s3:GetObject"],
         "Resource":["arn:aws:s3:::example.com/*"
         ]
       }
     ]
   }
   ```

    You now have two buckets, *example\.com* and *www\.example\.com*, and you have uploaded your website content to the *example\.com* bucket\. In the next step, you configure *www\.example\.com* to redirect requests to your *example\.com* bucket\. By redirecting requests, you can maintain only one copy of your website content\. Visitors who type `www` in their browsers and those who specify only the root domain are routed to the same website content in your *example\.com* bucket\. 

### Step 2\.2: Configure Buckets for Website Hosting<a name="root-domain-walkthrough-configure-bucket-aswebsite"></a>

 When you configure a bucket for website hosting, you can access the website using the Amazon S3 assigned bucket website endpoint\. 

In this step, you configure both buckets for website hosting\. First, you configure `example.com` as a website and then you configure `www.example.com` to redirect all requests to the `example.com` bucket\.

**To configure your buckets for website hosting**

1. Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. In the **Bucket name** list, choose the name of the bucket that you want to enable static website hosting for\.

1. Choose **Properties**\.

1. Choose **Static website hosting**\.

1. Configure the `example.com` bucket for website hosting\. In the **Index Document** box, type the name that you gave your index page\.   
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/staticwebsitehosting20.png)

1. Choose **Save**\.

### Step 2\.3: Configure Your Website Redirect<a name="root-domain-walkthrough-configure-redirect"></a>

Now that you have configured your bucket for website hosting, configure the `www.example.com` bucket to redirect all requests for `www.example.com` to `example.com`\.

**To redirect requests from `www.example.com` to `example.com`**

1. In the Amazon S3 console, in the **Buckets** list, choose your bucket \( `www.example.com`, in this example\)\.

1. Choose **Properties**\.

1. Choose **Static website hosting**\.

1. Choose **Redirect requests**\. In the **Target bucket or domain** box, type *example\.com*\.

1. Choose **Save**\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/swsRootDomainWebsiteEndpoint12.png)

### Step 2\.4: Configure Logging for Website Traffic<a name="root-domain-walkthrough-configure-logging"></a>

Optionally, you can configure logging to track the number of visitors accessing your website\. To do that, you enable logging for the root domain bucket\. For more information, see [\(Optional\) Configuring Web Traffic Logging](LoggingWebsiteTraffic.md)\.

### Step 2\.5: Test Your Endpoint and Redirect<a name="root-domain-walkthrough-test-website"></a>

To test the website, type the URL of the endpoint in your browser\. Your request is redirected, and the browser displays the index document for *example\.com*\. 

 In the next step, you use Amazon Route 53 to enable customers to use all of the URLs to navigate to your site\.

## Step 3: Add Alias Records for example\.com and www\.example\.com<a name="root-domain-walkthrough-add-arecord-to-hostedzone"></a>

In this step, you create the alias records that you add to the hosted zone for your domain maps `example.com` and `www.example.com` to the corresponding S3 buckets\. Instead of using IP addresses, the alias records use the Amazon S3 website endpoints\. Amazon Route 53 maintains a mapping between the alias records and the IP addresses where the Amazon S3 buckets reside\.

**To route traffic to your website**

1. Open the Route 53 console at [https://console\.aws\.amazon\.com/route53/](https://console.aws.amazon.com/route53/)\.

1. In the navigation pane, choose **Hosted zones**\.
**Note**  
When you registered your domain, Amazon Route 53 automatically created a hosted zone with the same name\. A hosted zone contains information about how you want Route 53 to route traffic for the domain\.

1. In the list of hosted zones, choose the name of your domain\.

1. Choose **Create Record Set**\.
**Note**  
Each record contains information about how you want to route traffic for one domain \(example\.com\) or subdomain \(www\.example\.com\)\. Records are stored in the hosted zone for your domain\.

1. Specify the following values:  
**Name**  
For the first record that you'll create, accept the default value, which is the name of your hosted zone and your domain\. This will route internet traffic to the bucket that has the same name as your domain\.  
Repeat this step to create a second record for your subdomain\. For the second record, type **www**\. This will route internet traffic to the www\.*example\.com* bucket\.  
**Type**  
Choose **A – IPv4 address**\.  
**Alias**  
Choose **Yes**\.  
**Alias Target**  
Type the name of the region that you created your Amazon S3 bucket in\. Use the applicable value from the **Website Endpoint** column in the [Amazon Simple Storage Service Website Endpoints](http://docs.aws.amazon.com/general/latest/gr/rande.html#s3_website_region_endpoints) table in the [AWS Regions and Endpoints](http://docs.aws.amazon.com/general/latest/gr/rande.html) chapter of the *Amazon Web Services General Reference*\.  
You specify the same value for **Alias Target** for both records\. Route 53 figures out which bucket to route traffic to based on the name of the record\.  
**Routing Policy**  
Accept the default value of **Simple**\.  
**Evaluate Target Health**  
Accept the default value of **No**\.

1. Choose **Create**\.

1. For www\.*example\.com*, repeat steps 4 through 6 to create a record\.

The following screenshot shows the alias record for `example.com` as an illustration\. You also need to create an alias record for `www.example.com`\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/RootDomainAliasRecord.png)

**Note**  
Creating, changing, and deleting resource record sets take time to propagate to the Route 53 DNS servers\. Changes generally propagate to all Route 53 name servers in a couple of minutes\. In rare circumstances, propagation can take up to 30 minutes\.

## Step 4: Testing<a name="root-domain-testing"></a>

To verify that the website is working correctly, in your browser, try the following URLs:
+ `http://example.com` – Displays the index document in the `example.com` bucket\.
+ `http://www.example.com` – Redirects your request to `http://example.com`\. 

In some cases, you might need to clear the cache of your web browser to see the expected behavior\.