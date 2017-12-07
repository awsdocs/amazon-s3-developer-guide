# Amazon S3 Customer Data Isolation<a name="DevPayDataIsolation"></a>

 Amazon DevPay requests store and access data on behalf of the users of your product\. The resources created by your application are owned by your users; unless you modify the ACL, you cannot read or modify the user's data\. 

 Data stored by your product is isolated from other Amazon DevPay products and general Amazon S3 access\. Customers that *store* data in Amazon S3 through your product can only * access* that data through your product\. The data cannot be accessed through other Amazon DevPay products or through a personal AWS account\. 

 Two users of a product can only access each others data if your application explicitly grants access through the ACL\. 

## Example<a name="UsingDevPayExample"></a>

The following figure illustrates allowed, disallowed, and conditional \(discretionary\) data access\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/devpay_isolation.png)

Betty's access is limited as follows:

+ She can access Lolcatz data through the Lolcatz product\. If she attempts to access her Lolcatz data through another product or a personal AWS account, her requests will be denied\.

+ She can access Alvin's eScrapBook data through the eScrapBook product if access is explicitly granted\. 