# Configuring Index Document Support<a name="IndexDocumentSupport"></a>

An *index document* is a webpage that Amazon S3 returns when a request is made to the root of a website or any subfolder\. For example, if a user enters `http://www.example.com` in the browser, the user is not requesting any specific page\. In that case, Amazon S3 serves up the index document, which is sometimes referred to as the default page\.

When you configure your bucket as a website, provide the name of the index document\. You then upload an object with this name and configure it to be publicly readable\. 

The trailing slash at the root\-level URL is optional\. For example, if you configure your website with `index.html` as the index document, either of the following two URLs return `index.html`\.

```
1. http://example-bucket.s3-website-region.amazonaws.com/
2. http://example-bucket.s3-website-region.amazonaws.com
```

For more information about Amazon S3 website endpoints, see [Website Endpoints](WebsiteEndpoints.md)\.

## Index Documents and Folders<a name="IndexDocumentsandFolders"></a>

 In Amazon S3, a bucket is a flat container of objects; it does not provide any hierarchical organization as the file system on your computer does\. You can create a logical hierarchy by using object key names that imply a folder structure\. For example, consider a bucket with three objects and the following key names\. 
+ `sample1.jpg`
+ `photos/2006/Jan/sample2.jpg`
+ `photos/2006/Feb/sample3.jpg`

Although these are stored with no physical hierarchical organization, you can infer the following logical folder structure from the key names\.
+ `sample1.jpg` object is at the root of the bucket\.
+ `sample2.jpg` object is in the `photos/2006/Jan` subfolder\.
+ `sample3.jpg` object is in the `photos/2006/Feb` subfolder\. 

 The folder concept that Amazon S3 console supports is based on object key names\. To continue the previous example, the console displays the `examplebucket` with a `photos` folder\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/swsRootDomainBucketWithFolder.png)

You can upload objects to the bucket or to the `photos` folder within the bucket\. If you add the object `sample.jpg` to the bucket, the key name is `sample.jpg`\. If you upload the object to the `photos` folder, the object key name is `photos/sample.jpg`\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/swsRootDomainBucketWithFolderObject.png)

If you create such a folder structure in your bucket, you must have an index document at each level\. When a user specifies a URL that resembles a folder lookup, the presence or absence of a trailing slash determines the behavior of the website\. For example, the following URL, with a trailing slash, returns the `photos/index.html` index document\. 

```
1. http://example-bucket.s3-website-region.amazonaws.com/photos/
```

However, if you exclude the trailing slash from the preceding URL, Amazon S3 first looks for an object `photos` in the bucket\. If the `photos` object is not found, then it searches for an index document, ` photos/index.html`\. If that document is found, Amazon S3 returns a `302 Found` message and points to the `photos/` key\. For subsequent requests to `photos/`, Amazon S3 returns `photos/index.html`\. If the index document is not found, Amazon S3 returns an error\.