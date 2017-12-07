# Expiring Objects: General Considerations<a name="lifecycle-expire-general-considerations"></a>

 When an object reaches the end of its lifetime, Amazon S3 queues it for removal and removes it asynchronously\. There may be a delay between the expiration date and the date at which Amazon S3 removes an object\. You are not charged for storage time associated with an object that has expired\. 

 To find when an object is scheduled to expire, you can use the [HEAD Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectHEAD.html) or the [GET Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html) API operations\. These API operations return response headers that provide object expiration information\. 

There are additional cost considerations if you put a lifecycle policy to expire objects that have been in STANDARD\_IA for less than 30 days, or GLACIER for less than 90 days\. For more information about cost considerations, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.