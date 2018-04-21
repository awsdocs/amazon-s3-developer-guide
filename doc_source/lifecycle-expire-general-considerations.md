# Configuring Object Expiration<a name="lifecycle-expire-general-considerations"></a>

 When an object reaches the end of its lifetime, Amazon S3 queues it for removal and removes it asynchronously\. There may be a delay between the expiration date and the date at which Amazon S3 removes an object\. You are not charged for storage time associated with an object that has expired\. 

 To find when an object is scheduled to expire, use the [HEAD Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectHEAD.html) or the [GET Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html) API operations\. These API operations return response headers that provide this information\. 

If you create a lifecycle expiration rule that causes objects that have been in STANDARD\_IA \(or ONEZONE\_IA\) storage for less than 30 days to expire, you are charged for 30 days\. If you create a lifecycle expiration rule that causes objects that have been in GLACIER storage for less than 90 days to expire, you are charged for 90 days\. For more information, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.