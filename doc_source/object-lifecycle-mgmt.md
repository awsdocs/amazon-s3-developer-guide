# Object Lifecycle Management<a name="object-lifecycle-mgmt"></a>

To manage your objects so that they are stored cost effectively throughout their lifecycle, configure their lifecycle\.  A *lifecycle configuration* is a set of rules that define actions that Amazon S3 applies to a group of objects\. There are two types of actions:
+ **Transition actions**—Define when objects transition to another [storage class](http://docs.aws.amazon.com/AmazonS3/latest/dev/storage-class-intro.html)\. For example, you might choose to transition objects to the STANDARD\_IA storage class 30 days after you created them, or archive objects to the GLACIER storage class one year after creating them\. 

   

  There are costs associated with the lifecycle transition requests\. For pricing information, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

   
+ **Expiration actions**—Define when objects expire\. Amazon S3 deletes expired objects on your behalf\. 

   

  The lifecycle expiration costs depend on when you choose to expire objects\. For more information, see [Configuring Object Expiration](lifecycle-expire-general-considerations.md)\.

For more information about lifecycle rules, see [Lifecycle Configuration Elements](intro-lifecycle-rules.md)\. 

## When Should I Use Lifecycle Configuration?<a name="lifecycle-config-overview-what"></a>

Define lifecycle configuration rules for objects that have a well\-defined lifecycle\. For example: 
+ If you upload periodic logs to a bucket, your application might need them for a week or a month\. After that, you might want to delete them\.
+ Some documents are frequently accessed for a limited period of time\. After that, they are infrequently accessed\. At some point, you might not need real\-time access to them, but your organization or regulations might require you to archive them for a specific period\. After that, you can delete them\. 
+ You might upload some types of data to Amazon S3 primarily for archival purposes\. For example, you might archive digital media, financial and healthcare records, raw genomics sequence data, long\-term database backups, and data that must be retained for regulatory compliance\.

With lifecycle configuration rules, you can tell Amazon S3 to transition objects to less expensive storage classes, or archive or delete them\.

## How Do I Configure a Lifecycle?<a name="lifecycle-config-overview-how"></a>

A lifecycle configuration, an XML file, comprises a set of rules with predefined actions that you want Amazon S3 to perform on objects during their lifetime\. 

Amazon S3 provides a set of API operations for managing lifecycle configuration on a bucket\. Amazon S3 stores the configuration as a *lifecycle subresource* that is attached to your bucket\. For details, see the following:

[PUT Bucket lifecycle](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTlifecycle.html)

[GET Bucket lifecycle](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlifecycle.html)

[DELETE Bucket lifecycle](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETElifecycle.html)

You can also configure the lifecycle by using the Amazon S3 console or programmatically by using the AWS SDK wrapper libraries\. If you need to, you can also make the REST API calls directly\. For more information, see [Setting Lifecycle Configuration on a Bucket](how-to-set-lifecycle-configuration-intro.md)\.

For more information, see the following topics:
+ [Additional Considerations for Lifecycle Configuration](lifecycle-additional-considerations.md)
+ [Lifecycle Configuration Elements](intro-lifecycle-rules.md)
+ [Examples of Lifecycle Configuration](lifecycle-configuration-examples.md)
+ [Setting Lifecycle Configuration on a Bucket](how-to-set-lifecycle-configuration-intro.md)