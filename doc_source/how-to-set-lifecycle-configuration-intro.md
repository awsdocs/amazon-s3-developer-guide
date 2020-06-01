# Setting lifecycle configuration on a bucket<a name="how-to-set-lifecycle-configuration-intro"></a>

**Topics**
+ [Manage an object's lifecycle using the Amazon S3 console](manage-lifecycle-using-console.md)
+ [Set lifecycle configurations using the AWS CLI](set-lifecycle-cli.md)
+ [Managing object lifecycles using the AWS SDK for Java](manage-lifecycle-using-java.md)
+ [Manage an object's lifecycle using the AWS SDK for \.NET](manage-lifecycle-using-dot-net.md)
+ [Manage an object's lifecycle using the AWS SDK for Ruby](manage-lifecycle-using-ruby.md)
+ [Manage an object's lifecycle using the REST API](manage-lifecycle-using-rest.md)

This section explains how you can set S3 Lifecycle configuration on a bucket programmatically using AWS SDKs, or by using the Amazon S3 console or the AWS CLI\. Note the following:
+ When you add an S3 Lifecycle configuration to a bucket, there is usually some lag before a new or updated Lifecycle configuration is fully propagated to all the Amazon S3 systems\. Expect a delay of a few minutes before the configuration fully takes effect\. This delay can also occur when you delete an S3 Lifecycle configuration\.
+ When you disable or delete a Lifecycle rule, after a small delay, Amazon S3 stops scheduling new objects for deletion or transition\. Any objects that were already scheduled are unscheduled and are not deleted or transitioned\.
+ When you add a Lifecycle configuration to a bucket, the configuration rules apply to both existing objects and objects that you add later\. For example, if you add a Lifecycle configuration rule today with an expiration action that causes objects with a specific prefix to expire 30 days after creation, Amazon S3 will queue for removal any existing objects that are more than 30 days old\.
+ There may be a lag between when the Lifecycle configuration rules are satisfied and when the action triggered by satisfying the rule is taken\. However, changes in billing happen as soon as the Lifecycle configuration rule is satisfied even if the action is not yet taken\. One example is you are not charged for storage after the object expiration time even if the object is not deleted immediately\. Another example is you are charged Amazon S3 Glacier storage rates as soon as the object transition time elapses, even if the object is not immediately transitioned to the S3 Glacier storage class\. Lifecycle transitions to the S3 Intelligent\-Tiering storage class are the exception and changes in billing do not happen until the object has transitioned into the S3 Intelligent\-Tiering storage class\. 

For information about S3 Lifecycle configuration, see [Object lifecycle management](object-lifecycle-mgmt.md)\.