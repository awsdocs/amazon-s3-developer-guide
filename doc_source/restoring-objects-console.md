# Restore an Archived Object Using the Amazon S3 Console<a name="restoring-objects-console"></a>

You can use the Amazon S3 console to restore a copy of an object that has been archived to Amazon Glacier\. For instructions on how to restore an archive using the AWS Management Console, see [ How Do I Restore an S3 Object that has been Archived to Amazon Glacier?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/restore-archived-objects.html) in the *Amazon Simple Storage Service Console User Guide*\.

Note that when you restore an archive you are paying for both the archive and a copy you restored temporarily\. For information about pricing, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\. 

Amazon S3 restores a temporary copy of the object only for the specified duration\. After that Amazon S3 deletes the restored object copy\. You can modify the expiration period of a restored copy, by reissuing a restore, in which case Amazon S3 updates the expiration period, relative to the current time\. 

Amazon S3 calculates expiration time of the restored object copy by adding the number of days specified in the restoration request to the current time and rounding the resulting time to the next day midnight UTC\. For example, if an object was created on 10/15/2012 10:30 am UTC and the restoration period was specified as 3 days, then the restored copy expires on 10/19/2012 00:00 UTC, at which time Amazon S3 deletes the object copy\. 

You can restore an object copy for any number of days\. However you should restore objects only for the duration you need because of the storage costs associated with the object copy\. For pricing information, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.