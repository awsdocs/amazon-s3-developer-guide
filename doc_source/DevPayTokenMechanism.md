# Amazon DevPay Token Mechanism<a name="DevPayTokenMechanism"></a>

To enable you to make requests on behalf of your customers and ensure that your customers are billed for use of your application, your application must send two tokens with each request: the product token and the user token\. 

 The product token identifies your product; you must have one product token for each Amazon DevPay product that you provide\. The user token identifies a user in relationship to your product; you must have a user token for each user/product combination\. For example, if you provide two products and a user subscribes to each, you must obtain a separate user token for each product\. 

For information on obtaining product and user tokens, refer to the *Amazon DevPay Amazon DevPay Getting Started Guide\. *