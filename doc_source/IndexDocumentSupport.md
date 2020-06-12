# Configuring an index document<a name="IndexDocumentSupport"></a>

When you enable website hosting, you must also configure and upload an index document\. An *index document* is a webpage that Amazon S3 returns when a request is made to the root of a website or any subfolder\. For example, if a user enters `http://www.example.com` in the browser, the user is not requesting any specific page\. In that case, Amazon S3 serves up the index document, which is sometimes referred to as the *default page*\.

When you enable static website hosting for your bucket, you enter the name of the index document \(for example, `index.html`\)\. After you enable static website hosting for your bucket, you upload an HTML file with the index document name to your bucket\. 

The trailing slash at the root\-level URL is optional\. For example, if you configure your website with `index.html` as the index document, either of the following URLs returns `index.html`\.

```
1. http://example-bucket.s3-website.Region.amazonaws.com/
2. http://example-bucket.s3-website.Region.amazonaws.com
```

For more information about Amazon S3 website endpoints, see [Website endpoints](WebsiteEndpoints.md)\.

## Index document and folders<a name="IndexDocumentsandFolders"></a>

In Amazon S3, a bucket is a flat container of objects\. It does not provide any hierarchical organization as the file system on your computer does\. However, you can create a logical hierarchy by using object key names that imply a folder structure\. 

For example, consider a bucket with three objects that have the following key names\. Although these are stored with no physical hierarchical organization, you can infer the following logical folder structure from the key names:
+ `sample1.jpg` — Object is at the root of the bucket\.
+ `photos/2006/Jan/sample2.jpg` — Object is in the `photos/2006/Jan` subfolder\.
+ `photos/2006/Feb/sample3.jpg` — Object is in the `photos/2006/Feb` subfolder\. 

In the Amazon S3 console, you can also create a folder in a bucket\. For example, you can create a folder named `photos`\. You can upload objects to the bucket or to the `photos` folder within the bucket\. If you add the object `sample.jpg` to the bucket, the key name is `sample.jpg`\. If you upload the object to the `photos` folder, the object key name is `photos/sample.jpg`\.

If you create a folder structure in your bucket, you must have an index document at each level\. In each folder, the index document must have the same name, for example, `index.html`\. When a user specifies a URL that resembles a folder lookup, the presence or absence of a trailing slash determines the behavior of the website\. For example, the following URL, with a trailing slash, returns the `photos/index.html` index document\. 

```
1. http://bucket-name.s3-website.Region.amazonaws.com/photos/
```

However, if you exclude the trailing slash from the preceding URL, Amazon S3 first looks for an object `photos` in the bucket\. If the `photos` object is not found, it searches for an index document, `photos/index.html`\. If that document is found, Amazon S3 returns a `302 Found` message and points to the `photos/` key\. For subsequent requests to `photos/`, Amazon S3 returns `photos/index.html`\. If the index document is not found, Amazon S3 returns an error\.

## Configuring an index document<a name="configuring-index-document"></a>

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

Next, you must set permissions for website access\. For more information, see [Setting permissions for website access](WebsiteAccessPermissionsReqd.md)\. You can also optionally configure an [error document](CustomErrorDocSupport.md), [web traffic logging](LoggingWebsiteTraffic.md), or a [redirect](how-to-page-redirect.md)\.