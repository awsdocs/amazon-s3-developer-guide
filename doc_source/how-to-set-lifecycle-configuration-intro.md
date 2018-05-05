# Setting Lifecycle Configuration on a Bucket<a name="how-to-set-lifecycle-configuration-intro"></a>

**Topics**
+ [Manage an Object's Lifecycle Using the Amazon S3 Console](manage-lifecycle-using-console.md)
+ [Set Lifecycle Configurations Using the AWS CLI](set-lifecycle-cli.md)
+ [Managing Object Lifecycles Using the AWS SDK for Java](manage-lifecycle-using-java.md)
+ [Manage an Object's Lifecycle Using the AWS SDK for \.NET](manage-lifecycle-using-dot-net.md)
+ [Manage an Object's Lifecycle Using the AWS SDK for Ruby](manage-lifecycle-using-ruby.md)
+ [Manage an Object's Lifecycle Using the REST API](manage-lifecycle-using-rest.md)

This section explains how you can set lifecycle configuration on a bucket programmatically using AWS SDKs, or by using the Amazon S3 console, or the AWS CLI\. Note the following:
+ When you add a lifecycle configuration to a bucket, there is usually some lag before a new or updated lifecycle configuration is fully propagated to all the Amazon S3 systems\. Expect a delay of a few minutes before the lifecycle configuration fully takes effect\. This delay can also occur when you delete a lifecycle configuration\.
+ When you disable or delete a lifecycle rule, after a small delay Amazon S3 stops scheduling new objects for deletion or transition\. Any objects that were already scheduled will be unscheduled and they won't be deleted or transitioned\.
+ When you add a lifecycle configuration to a bucket, the configuration rules apply to both existing objects and objects that you add later\. For example, if you add a lifecycle configuration rule today with an expiration action that causes objects with a specific prefix to expire 30 days after creation, Amazon S3 will queue for removal any existing objects that are more than 30 days old\.
+ There may be a lag between when the lifecycle configuration rules are satisfied and when the action triggered by satisfying the rule is taken\. However, changes in billing happen as soon as the lifecycle configuration rule is satisfied even if the action is not yet taken\. One example is you are not charged for storage after the object expiration time even if the object is not deleted immediately\. Another example is you are charged Amazon Glacier storage rates as soon as the object transition time elapses even if the object is not transitioned to Amazon Glacier immediately\. 

For information about lifecycle configuration, see [Object Lifecycle Management](object-lifecycle-mgmt.md)\.