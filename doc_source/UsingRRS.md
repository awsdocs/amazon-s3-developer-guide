# Using Reduced Redundancy Storage<a name="UsingRRS"></a>


+ [Setting the Storage Class of an Object You Upload](SetStoClsOfObjUploaded.md)
+ [Changing the Storage Class of an Object in Amazon S3](ChgStoClsOfObj.md)

Amazon S3 stores objects according to their storage class\. It assigns the storage class to an object when it is written to Amazon S3\. You can assign objects a specific storage class \(`standard` or `reduced redundancy`\) only when you write the objects to an Amazon S3 bucket or when you copy objects that are already stored in Amazon S3\. Standard is the default storage class\. For information about storage classes, see [Object Key and Metadata](UsingMetadata.md)\.

 In order to reduce storage costs, you can use reduced redundancy storage for noncritical, reproducible data at lower levels of redundancy than Amazon S3 provides with standard storage\. The lower level of redundancy results in less durability and availability, but in many cases, the lower costs can make reduced redundancy storage an acceptable storage solution\. For example, it can be a cost\-effective solution for sharing media content that is durably stored elsewhere\. It can also make sense if you are storing thumbnails and other resized images that can be easily reproduced from an original image\.

 Reduced redundancy storage is designed to provide 99\.99% durability of objects over a given year\. This durability level corresponds to an average annual expected loss of 0\.01% of objects\. For example, if you store 10,000 objects using the RRS option, you can, on average, expect to incur an annual loss of a single object per year \(0\.01% of 10,000 objects\)\. 

**Note**  
This annual loss represents an expected average and does not guarantee the loss of less than 0\.01% of objects in a given year\.

Reduced redundancy storage stores objects on multiple devices across multiple facilities, providing 400 times the durability of a typical disk drive, but it does not replicate objects as many times as Amazon S3 standard storage\. In addition, reduced redundancy storage is designed to sustain the loss of data in a single facility\.

If an object in reduced redundancy storage has been lost, Amazon S3 will return a 405 error on requests made to that object\. Amazon S3 also offers notifications for reduced redundancy storage object loss: you can configure your bucket so that when Amazon S3 detects the loss of an RRS object, a notification will be sent through Amazon Simple Notification Service \(Amazon SNS\)\. You can then replace the lost object\. To enable notifications from the Amazon S3 console, choose the **Properties** tab for your bucket, and then choose **Events**\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/SettingRRSNotif.png)

Latency and throughput for reduced redundancy storage are the same as for standard storage\. For more information about cost considerations, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.