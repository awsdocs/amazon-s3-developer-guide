# Amazon S3 and Amazon DevPay Process<a name="DevPayProcess"></a>

 Following is a high\-level overview of the Amazon DevPay process\. 


**Launch Process**  

|  |  | 
| --- |--- |
| 1 | A customer signs up for your product through Amazon\. | 
| 2 | The customer receives an activation key\. | 
| 3 | The customer enters the activation key into your application\. | 
| 4 | Your application communicates with Amazon and obtains the user's token\. If your application is installed on the user's computer, it also obtains an Access Key ID and Secret Access Key on behalf of the customer\.  | 
| 5 | Your application provides the customer's token and the application product token when making Amazon S3 requests on behalf of the customer\. If your application is installed on the customer's computer, it authenticates with the customer's credentials\. | 
| 6 | Amazon uses the customer's token and your product token to determine who to bill for the Amazon S3 usage\.  | 
| 7 | Once a month, Amazon processes usage data and bills your customers according to the terms you defined\. | 
| 8 | AWS deducts the fixed Amazon DevPay transaction fee and pays you the difference\. AWS then separately charges you for the Amazon S3 usage costs incurred by your customers and the percentage\-based Amazon DevPay fee\. | 