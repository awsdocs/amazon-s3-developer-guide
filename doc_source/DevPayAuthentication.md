# Amazon S3 and Amazon DevPay Authentication<a name="DevPayAuthentication"></a>

Although the token mechanism uniquely identifies a customer and product, it does not provide authentication\. 

Normally, your applications communicate directly with Amazon S3 using your Access Key ID and Secret Access Key\. For Amazon DevPay, Amazon S3 authentication works a little differently\.

If your Amazon DevPay product is a web application, you securely store the Secret Access Key on your servers and use the user token to specify the customer for which requests are being made\. 

However, if your Amazon S3 application is installed on your customers' computers, your application must obtain an Access Key ID and a Secret Access Key for each installation and must use those credentials when communicating with Amazon S3\.

The following figure shows the differences between authentication for web applications and user applications\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/devpay_installations.png)