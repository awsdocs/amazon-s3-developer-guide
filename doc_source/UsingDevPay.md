# Using Amazon DevPay with Amazon S3<a name="UsingDevPay"></a>


+ [Amazon S3 Customer Data Isolation](DevPayDataIsolation.md)
+ [Amazon DevPay Token Mechanism](DevPayTokenMechanism.md)
+ [Amazon S3 and Amazon DevPay Authentication](DevPayAuthentication.md)
+ [Amazon S3 Bucket Limitation](DevPayBucketLimitation.md)
+ [Amazon S3 and Amazon DevPay Process](DevPayProcess.md)
+ [Additional Information](DevPayAddlInfo.md)

Amazon DevPay enables you to charge customers for using your Amazon S3 product through Amazon's authentication and billing infrastructure\. You can charge any amount for your product including usage charges \(storage, transactions, and bandwidth\), monthly fixed charges, and a one\-time charge\. 

 Once a month, Amazon bills your customers for you\. AWS then deducts the fixed Amazon DevPay transaction fee and pays you the difference\. AWS then separately charges you for the Amazon S3 usage costs incurred by your customers and the percentage\-based Amazon DevPay fee\. 

 If your customers do not pay their bills, AWS turns off access to Amazon S3 \(and your product\)\. AWS handles all payment processing\. 