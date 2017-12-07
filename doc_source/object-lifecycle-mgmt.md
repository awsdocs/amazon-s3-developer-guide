# Object Lifecycle Management<a name="object-lifecycle-mgmt"></a>

Lifecycle configuration enables you to specify the lifecycle management of objects in a bucket\.  The configuration is a set of one or more rules, where each rule defines an action for Amazon S3 to apply to a group of objects\. These actions can be classified as follows:

+ **Transition actions** – In which you define when objects transition to another [storage class](http://docs.aws.amazon.com/AmazonS3/latest/dev/storage-class-intro.html)\. For example, you may choose to transition objects to the STANDARD\_IA \(IA, for infrequent access\) storage class 30 days after creation, or archive objects to the GLACIER storage class one year after creation\. 

   

+ **Expiration actions** – In which you specify when the objects expire\. Then Amazon S3 deletes the expired objects on your behalf\.

For more information about lifecycle rules, see [Lifecycle Configuration Elements](intro-lifecycle-rules.md)\. 

## When Should I Use Lifecycle Configuration for Objects?<a name="lifecycle-config-overview-what"></a>

You can define lifecycle configuration rules for objects that have a well\-defined lifecycle\. For example: 

+ If you are uploading periodic logs to your bucket, your application might need these logs for a week or a month after creation, and after that you might want to delete them\.

+ Some documents are frequently accessed for a limited period of time\. After that, these documents are less frequently accessed\. Over time, you might not need real\-time access to these objects, but your organization or regulations might require you to archive them for a longer period and then optionally delete them later\. 

+ You might also upload some types of data to Amazon S3 primarily for archival purposes, for example digital media archives, financial and healthcare records, raw genomics sequence data, long\-term database backups, and data that must be retained for regulatory compliance\.

Using lifecycle configuration rules, you can direct S3 to tier down the storage classes, archive, or delete the objects during their lifecycle\.

## How Do I Configure a Lifecycle?<a name="lifecycle-config-overview-how"></a>

A lifecycle configuration, an XML file, comprises a set of rules with predefined actions that you want Amazon S3 to perform on objects during their lifetime\. 

Amazon S3 provides a set of API operations that you use to manage lifecycle configuration on a bucket\. Amazon S3 stores the configuration as a *lifecycle subresource* that is attached to your bucket\. 

[PUT Bucket lifecycle](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTlifecycle.html)

[GET Bucket lifecycle](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlifecycle.html)

[DELETE Bucket lifecycle](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETElifecycle.html)

You can also configure the lifecycle by using the Amazon S3 console or programmatically by using the AWS SDK wrapper libraries, and if you need to you can also make the REST API calls directly\. For more information, see [Setting Lifecycle Configuration On a Bucket](how-to-set-lifecycle-configuration-intro.md)\.

For more information, see the following topics:

+ [Lifecycle Configuration: Additional Considerations](lifecycle-additional-considerations.md)

+ [Lifecycle Configuration Elements](intro-lifecycle-rules.md)

+ [Examples of Lifecycle Configuration](lifecycle-configuration-examples.md)

+ [Setting Lifecycle Configuration On a Bucket](how-to-set-lifecycle-configuration-intro.md)