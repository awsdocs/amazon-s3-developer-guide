# Cleaning up your example resources<a name="getting-started-cleanup"></a>

If you created your static website as a learning exercise, you should delete the AWS resources that you allocated so that you no longer accrue charges\. After you delete your AWS resources, your website is no longer available\.

**Topics**
+ [Step 1: Delete the Amazon CloudFront distribution](#getting-started-cleanup-cloudfront)
+ [Step 2: Delete the Route 53 hosted zone](#getting-started-cleanup-route53)
+ [Step 3: Disable logging and delete your S3 bucket](#getting-started-cleanup-s3)

## Step 1: Delete the Amazon CloudFront distribution<a name="getting-started-cleanup-cloudfront"></a>

Before you delete an Amazon CloudFront distribution, you must disable it\. A disabled distribution is no longer functional and does not accrue charges\. You can enable a disabled distribution at any time\. After you delete a disabled distribution, it is no longer available\.

**To disable and delete a CloudFront distribution**

1. Open the CloudFront console at [ https://console\.aws\.amazon\.com/cloudfront/](https://console.aws.amazon.com/cloudfront/)\.

1. Select the distribution that you want to disable, and then choose **Disable**\.

1. When prompted for confirmation, choose **Yes, Disable**\.

1. Select the disabled distribution, and then choose **Delete**\.

1. When prompted for confirmation, choose **Yes, Delete**\.

## Step 2: Delete the Route 53 hosted zone<a name="getting-started-cleanup-route53"></a>

Before you delete the hosted zone, you must delete the record sets that you created\. You don't need to delete the NS and SOA records; these are automatically deleted when you delete the hosted zone\.

**To delete the record sets**

1. Open the Route 53 console at [https://console\.aws\.amazon\.com/route53/](https://console.aws.amazon.com/route53/)\.

1.  In the list of domain names, select your domain name, and then choose **Go to Record Sets**\. 

1. In the list of record sets, select the A records that you created\. 

   The type of each record set is listed in the **Type** column\. 

1. Choose **Delete Record Set**\. 

1. When prompted for confirmation, choose **Confirm**\. 

**To delete a Route 53 hosted zone**

1.  Continuing from the previous procedure, choose **Back to Hosted Zones**\. 

1.  Select your domain name, and then choose **Delete Hosted Zone**\. 

1.  When prompted for confirmation, choose **Confirm**\. 

## Step 3: Disable logging and delete your S3 bucket<a name="getting-started-cleanup-s3"></a>

Before you delete your S3 bucket, make sure that logging is disabled for the bucket\. Otherwise, AWS continues to write logs to your bucket as you delete it\.

**To disable logging for a bucket**

1. Open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\.

1. Under **Buckets**, choose your bucket name, and then choose **Properties**\.

1. From **Properties**, choose **Logging**\.

1. Clear the **Enabled** check box\.

1. Choose **Save**\.

Now, you can delete your bucket\. For more information, see [How Do I Delete an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/delete-bucket.html) in the *Amazon Simple Storage Service Console User Guide*\.