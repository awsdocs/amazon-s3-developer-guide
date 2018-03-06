# Example: Setting up a Static Website Using a Custom Domain<a name="website-hosting-custom-domain-walkthrough"></a>

Suppose that you want to host your static website on Amazon S3\. You registered a domain \(for example, `example.com`\), and you want requests for `http://www.example.com` and `http://example.com` to be served from your Amazon S3 content\. Whether you have an existing static website that you want to host on Amazon S3, or you are starting from scratch, use this example to learn how to host websites on Amazon S3\.


+ [Before You Begin](#root-domain-walkthrough-before-you-begin)
+ [Step 1: Register a Domain](#website-hosting-custom-domain-walkthrough-domain-registry)
+ [Step 2: Create and Configure Buckets and Upload Data](#root-domain-walkthrough-s3-tasks)
+ [Step 3: Create and Configure Amazon Route 53 Hosted Zone](#root-domain-walkthrough-switch-to-route53-as-dnsprovider)
+ [Step 4: Switch to Amazon Route 53 as Your DNS Provider](#root-domain-walkthrough-update-ns-record)
+ [Step 5: Testing](#root-domain-testing)

## Before You Begin<a name="root-domain-walkthrough-before-you-begin"></a>

As you walk through the steps in this example, you work with the following services:

**Domain registrar of your choice –** If you don't already have a registered domain name, such as `example.com`, create and register one with a registrar of your choice\. You can typically register a domain for a small yearly fee\. For procedural information about registering a domain name, see the registrar's website\.

**Amazon S3 –** You use Amazon S3 to create buckets, upload a sample website page, configure permissions so that everyone can see the content, and then configure the buckets for website hosting\. In this example, because you want to allow requests for both `http://www.example.com` and `http://example.com`, you create two buckets; however, you host content in only one bucket\. You configure the other Amazon S3 bucket to redirect requests to the bucket that hosts the content\.

**Amazon Route 53 –** You configure Amazon Route 53 as your Domain Name System \(DNS\) provider\. You create a hosted zone in Amazon Route 53 for your domain and configure applicable DNS records\. If you are switching from an existing DNS provider, you need to ensure that you have transferred all of the DNS records for your domain\. 

We recommend that you have basic familiarity with domains, DNS, CNAME records, and A records\. A detailed explanation of these concepts is beyond the scope of this guide\. Your domain registrar should provide any basic information that you need\.

In this example, we use Route 53\. However, you can use most registrars to define a CNAME record that points to an Amazon S3 bucket\.

**Note**  
All the steps in this example use `example.com` as a domain name\. Replace this domain name with the one that you registered\.

## Step 1: Register a Domain<a name="website-hosting-custom-domain-walkthrough-domain-registry"></a>

If you already have a registered domain, you can skip this step\. If you are new to hosting a website, your first step is to register a domain, such as `example.com`, with a registrar of your choice\.   

After you choose a registrar, register your domain name according to the instructions at the registrar’s website\. For a list of registrar websites that you can use to register your domain name, see [Information for Registrars and Registrants](https://www.icann.org/resources/pages/registrars-0d-2012-02-25-en) at the ICANN\.org website\.

 When you have a registered domain name, your next task is to create and configure Amazon S3 buckets for website hosting and to upload your website content\. 

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

1. Configure permissions for your objects to make them publicly accessible\. 

   Attach the following bucket policy to the `example.com` bucket, substituting the name of your bucket for *example\.com*\. For step\-by\-step instructions to attach a bucket policy, see [How Do I Add an S3 Bucket Policy?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/add-bucket-policy.html) in the *Amazon Simple Storage Service Console User Guide*\. 

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

## Step 3: Create and Configure Amazon Route 53 Hosted Zone<a name="root-domain-walkthrough-switch-to-route53-as-dnsprovider"></a>

 Configure Amazon Route 53 as your Domain Name System \(DNS\) provider\. If you want to serve content from your root domain, such as `example.com`, you must use Amazon Route 53\. You create a hosted zone, which holds the DNS records associated with your domain:

+ An alias record that maps the domain *example\.com* to the example\.com bucket\. This is the bucket that you configured as a website endpoint in step 2\.2\.

+ Another alias record that maps the subdomain www\.*example\.com* to the www\.*example\.com* bucket\. You configured this bucket to redirect requests to the example\.com bucket in step 2\.2\.

### Step 3\.1: Create a Hosted Zone for Your Domain<a name="root-domain-walkthrough-create-route53-hostedzone"></a>

Go to the Amazon Route 53 console at [https://console\.aws\.amazon\.com/route53](https://console.aws.amazon.com/route53/home) and then create a hosted zone for your domain\. For instructions, go to [Creating a Hosted Zone](http://docs.aws.amazon.com/Route53/latest/DeveloperGuide/MigratingDNS.html#Step_CreateHostedZone) in the *http://docs\.aws\.amazon\.com/Route53/latest/DeveloperGuide/*\. 

The following example shows the hosted zone created for the `example.com` domain\. Write down the Route 53 name servers \(NS\) for this domain\. You will need them later\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/RootDomainR53NameServers.png)

### Step 3\.2: Add Alias Records for example\.com and www\.example\.com<a name="root-domain-walkthrough-add-arecord-to-hostedzone"></a>

The alias records that you add to the hosted zone for your domain maps `example.com` and `www.example.com` to the corresponding S3 buckets\. Instead of using IP addresses, the alias records use the Amazon S3 website endpoints\. Amazon Route 53 maintains a mapping between the alias records and the IP addresses where the S3 buckets reside\. 

For step\-by\-step instructions, see [Creating Resource Record Sets by Using the Amazon Route 53 Console](http://docs.aws.amazon.com/Route53/latest/DeveloperGuide/resource-record-sets-creating.html) in the *Amazon Route 53 Developer Guide*\. 

The following screenshot shows the alias record for `example.com` as an illustration\. You also need to create an alias record for `www.example.com`\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/RootDomainAliasRecord.png)

To enable this hosted zone, you must use Amazon Route 53 as the DNS server for your domain **example\.com**\. If you are moving an existing website to Amazon S3, first you must transfer DNS records associated with your domain **example\.com** to the hosted zone that you created in Amazon Route 53 for your domain\. If you are creating a new website, you can go directly to step 4\.

**Note**  
Creating, changing, and deleting resource record sets take time to propagate to the Route 53 DNS servers\. Changes generally propagate to all Route 53 name servers in a couple of minutes\. In rare circumstances, propagation can take up to 30 minutes\.

### Step 3\.3: Transfer Other DNS Records from Your Current DNS Provider to Route 53<a name="root-domain-walkthrough-migrate-dns-records"></a>

Before you switch to Amazon Route 53 as your DNS provider, you must transfer the remaining DNS records—including MX records, CNAME records, and A records—from your DNS provider to Amazon Route 53\. You don't need to transfer the following records:

+ NS records– Instead of transferring these, replace their values with the name server values that are provided by Amazon Route 53\.

+ SOA record– Amazon Route 53 provides this record in the hosted zone with a default value\. 

Migrating required DNS records is a critical step to ensure the continued availability of all the existing services hosted under the domain name\.

### Step 3\.4: Create A Type DNS Records<a name="root-domain-walkthroguh-add-a-type-record"></a>

If you're not transferring your website from another existing website, you need to create new A type DNS records\.

**Note**  
If you've already transferred A type records for this website from a different DNS provider, you can skip the rest of this step\.

**To create A type DNS records in the Route 53 console**

1. Open the Route 53 console in your web browser\.

1. On the **Dashboard**, choose **Hosted zones**\.

1. Choose your domain name in the table of hosted zones\.

1. Choose **Create Record Set**\.

1. In the **Create Record Set** form that appears on the right, choose **Yes** for **Alias**\.

1. In **Alias Target**, provide the Amazon S3 website endpoint—for example, `s3-website-us-west-2.amazonaws.com`\.

1. Choose **Save Record Set**\.

Now that you've added an A type DNS record to your record set, it appears in the table as in the following example\.

![\[Screenshot showing table of record sets in Route 53 console\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/RootDomainARecord.png)

## Step 4: Switch to Amazon Route 53 as Your DNS Provider<a name="root-domain-walkthrough-update-ns-record"></a>

To switch to Amazon Route 53 as your DNS provider, contact your DNS provider and update the name server \(NS\) record to use the name servers in the delegation that you set in Amazon Route 53\. 

On your DNS provider's site, update the NS record with the delegation set values of the hosted zone as shown in the following Amazon Route 53 console screenshot\. For more information, see [Updating Your DNS Service's Name Server Records](http://docs.aws.amazon.com/Route53/latest/DeveloperGuide/MigratingDNS.html#Step_UpdateRegistrar) in *Amazon Route 53 Developer Guide*\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/RootDomainR53NameServers.png)

When the transfer to Route 53 is complete, verify that the name server for your domain has indeed changed\. On a Linux computer, use the `dig` DNS lookup utility\. For example, use this `dig` command:

```
dig +recurse +trace www.example.com any
```

It returns the following output \(only partial output is shown\)\. The output shows the same name servers on the Amazon Route 53 hosted zone that you created for the `example.com` domain\.

```
...
example.com.      172800  IN      NS      ns-9999.awsdns-99.com.
example.com.      172800  IN      NS      ns-9999.awsdns-99.org.
example.com.      172800  IN      NS      ns-9999.awsdns-99.co.uk.
example.com.      172800  IN      NS      ns-9999.awsdns-99.net.

www.example.com.  300     IN      CNAME   www.example.com.s3-website-us-east-1.amazonaws.com.
...
```

## Step 5: Testing<a name="root-domain-testing"></a>

 To verify that the website is working correctly, in your browser, try the following URLs:

+ `http://example.com` \- Displays the index document in the `example.com` bucket\.

+ `http://www.example.com`\- Redirects your request to `http://example.com`\. 

 In some cases, you might need to clear the cache of your web browser to see the expected behavior\.