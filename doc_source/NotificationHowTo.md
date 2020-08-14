# Configuring Amazon S3 event notifications<a name="NotificationHowTo"></a>

The Amazon S3 notification feature enables you to receive notifications when certain events happen in your bucket\. To enable notifications, you must first add a notification configuration that identifies the events you want Amazon S3 to publish and the destinations where you want Amazon S3 to send the notifications\. You store this configuration in the *notification* subresource that is associated with a bucket\. For more information, see [Bucket configuration options](UsingBucket.md#bucket-config-options-intro)\. Amazon S3 provides an API for you to manage this subresource\. 

**Important**  
Amazon S3 event notifications are designed to be delivered at least once\. Typically, event notifications are delivered in seconds but can sometimes take a minute or longer\.  
If two writes are made to a single non\-versioned object at the same time, it is possible that only a single event notification will be sent\. If you want to ensure that an event notification is sent for every successful write, you can enable versioning on your bucket\. With versioning, every successful write will create a new version of your object and will also send an event notification\.

**Topics**
+ [Overview of notifications](#notification-how-to-overview)
+ [How to enable event notifications](#how-to-enable-disable-notification-intro)
+ [Event notification types and destinations](#notification-how-to-event-types-and-destinations)
+ [Configuring notifications with object key name filtering](#notification-how-to-filtering)
+ [Granting permissions to publish event notification messages to a destination](#grant-destinations-permissions-to-s3)
+ [Walkthrough: Configure a bucket for notifications \(SNS topic or SQS queue\)](ways-to-add-notification-config-to-bucket.md)
+ [Event message structure](notification-content-structure.md)

## Overview of notifications<a name="notification-how-to-overview"></a>

 Currently, Amazon S3 can publish notifications for the following events:
+ **New object created events** — Amazon S3 supports multiple APIs to create objects\. You can request notification when only a specific API is used \(for example, `s3:ObjectCreated:Put`\), or you can use a wildcard \(for example, `s3:ObjectCreated:*`\) to request notification when an object is created regardless of the API used\. 
+ **Object removal events** — Amazon S3 supports deletes of versioned and unversioned objects\. For information about object versioning, see [Object Versioning](ObjectVersioning.md) and [Using versioning](Versioning.md)\. 

  You can request notification when an object is deleted or a versioned object is permanently deleted by using the `s3:ObjectRemoved:Delete` event type\. Or you can request notification when a delete marker is created for a versioned object by using `s3:ObjectRemoved:DeleteMarkerCreated`\. You can also use a wildcard `s3:ObjectRemoved:*` to request notification anytime an object is deleted\. For information about deleting versioned objects, see [Deleting object versions](DeletingObjectVersions.md)\. 
+ **Restore object events **— Amazon S3 supports the restoration of objects archived to the S3 Glacier storage classes\. You request to be notified of object restoration completion by using `s3:ObjectRestore:Completed`\. You use `s3:ObjectRestore:Post` to request notification of the initiation of a restore\.
+ **Reduced Redundancy Storage \(RRS\) object lost events** — Amazon S3 sends a notification message when it detects that an object of the RRS storage class has been lost\. 
+ **Replication events** — Amazon S3 sends event notifications for replication configurations that have S3 Replication Time Control \(S3 RTC\) enabled\. It sends these notifications when an object fails replication, when an object exceeds the 15\-minute threshold, when an object is replicated after the 15\-minute threshold, and when an object is no longer tracked by replication metrics\. It publishes a second event when that object replicates to the destination Region\.

For a list of supported event types, see [Supported event types](#supported-notification-event-types)\. 

Amazon S3 supports the following destinations where it can publish events:
+ **Amazon Simple Notification Service \(Amazon SNS\) topic**

  Amazon SNS is a flexible, fully managed push messaging service\. Using this service, you can push messages to mobile devices or distributed services\. With SNS you can publish a message once, and deliver it one or more times\. For more information about SNS, see the [Amazon SNS](https://aws.amazon.com/sns/) product detail page\.
+ **Amazon Simple Queue Service \(Amazon SQS\) queue**

  Amazon SQS is a scalable and fully managed message queuing service\. You can use SQS to transmit any volume of data without requiring other services to be always available\. In your notification configuration, you can request that Amazon S3 publish events to an SQS queue\. 

  Currently, Standard SQS queue is only allowed as an Amazon S3 event notification destination, whereas FIFO SQS queue is not allowed\. For more information about Amazon SQS, see the [Amazon SQS](https://aws.amazon.com/sqs/) product detail page\.
+ **AWS Lambda**

  AWS Lambda is a compute service that makes it easy for you to build applications that respond quickly to new information\. AWS Lambda runs your code in response to events such as image uploads, in\-app activity, website clicks, or outputs from connected devices\. 

  You can use AWS Lambda to extend other AWS services with custom logic, or create your own backend that operates at AWS scale, performance, and security\. With AWS Lambda, you can easily create discrete, event\-driven applications that run only when needed and scale automatically from a few requests per day to thousands per second\. 

  AWS Lambda can run custom code in response to Amazon S3 bucket events\. You upload your custom code to AWS Lambda and create what is called a Lambda function\. When Amazon S3 detects an event of a specific type \(for example, an object created event\), it can publish the event to AWS Lambda and invoke your function in Lambda\. In response, AWS Lambda runs your function\. 

**Warning**  
If your notification ends up writing to the bucket that triggers the notification, this could cause an execution loop\. For example, if the bucket triggers a Lambda function each time an object is uploaded, and the function uploads an object to the bucket, then the function indirectly triggers itself\. To avoid this, use two buckets, or configure the trigger to only apply to a prefix used for incoming objects\.  
For more information and an example of using Amazon S3 notifications with AWS Lambda, see [Using AWS Lambda with Amazon S3](https://docs.aws.amazon.com/lambda/latest/dg/with-s3.html) in the *AWS Lambda Developer Guide*\. 

## How to enable event notifications<a name="how-to-enable-disable-notification-intro"></a>

Enabling notifications is a bucket\-level operation; that is, you store notification configuration information in the *notification* subresource associated with a bucket\. You can use any of the following methods to manage notification configuration:
+ **Using the Amazon S3 console**

  The console UI enables you to set a notification configuration on a bucket without having to write any code\. For more information, see [How Do I Enable and Configure Event Notifications for an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-event-notifications.html) in the *Amazon Simple Storage Service Console User Guide*\.
+ **Programmatically using the AWS SDKs**
**Note**  
If you need to, you can also make the Amazon S3 REST API calls directly from your code\. However, this can be cumbersome because it requires you to write code to authenticate your requests\. 

  Internally, both the console and the SDKs call the Amazon S3 REST API to manage *notification* subresources associated with the bucket\. For notification configuration using AWS SDK examples, see [Walkthrough: Configure a bucket for notifications \(SNS topic or SQS queue\)](ways-to-add-notification-config-to-bucket.md)\.

  Regardless of the method that you use, Amazon S3 stores the notification configuration as XML in the *notification* subresource associated with a bucket\. For information about bucket subresources, see [Bucket configuration options](UsingBucket.md#bucket-config-options-intro)\. 

  By default, notifications are not enabled for any type of event\. Therefore, initially the *notification* subresource stores an empty configuration\.

  ```
  <NotificationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/"> 
  </NotificationConfiguration>
  ```

  To enable notifications for events of specific types, you replace the XML with the appropriate configuration that identifies the event types you want Amazon S3 to publish and the destination where you want the events published\. For each destination, you add a corresponding XML configuration\. For example: 
  + **Publish event messages to an SQS queue** — To set an SQS queue as the notification destination for one or more event types, you add the `QueueConfiguration`\.

    ```
    <NotificationConfiguration>
      <QueueConfiguration>
        <Id>optional-id-string</Id>
        <Queue>sqs-queue-arn</Queue>
        <Event>event-type</Event>
        <Event>event-type</Event>
         ...
      </QueueConfiguration>
       ...
    </NotificationConfiguration>
    ```
  + **Publish event messages to an SNS topic** — To set an SNS topic as the notification destination for specific event types, you add the `TopicConfiguration`\.

    ```
    <NotificationConfiguration>
      <TopicConfiguration>
         <Id>optional-id-string</Id>
         <Topic>sns-topic-arn</Topic>
         <Event>event-type</Event>
         <Event>event-type</Event>
          ...
      </TopicConfiguration>
       ...
    </NotificationConfiguration>
    ```
  + **Invoke the AWS Lambda function and provide an event message as an argument **— To set a Lambda function as the notification destination for specific event types, you add the `CloudFunctionConfiguration`\.

    ```
    <NotificationConfiguration>
      <CloudFunctionConfiguration>   
         <Id>optional-id-string</Id>   
         <CloudFunction>cloud-function-arn</CloudFunction>        
         <Event>event-type</Event>      
         <Event>event-type</Event>      
          ...  
      </CloudFunctionConfiguration>
       ...
    </NotificationConfiguration>
    ```

  To remove all notifications configured on a bucket, you save an empty `<NotificationConfiguration/>` element in the *notification* subresource\. 

  When Amazon S3 detects an event of the specific type, it publishes a message with the event information\. For more information, see [Event message structure](notification-content-structure.md)\. 

## Event notification types and destinations<a name="notification-how-to-event-types-and-destinations"></a>

This section describes the event notification types that are supported by Amazon S3 and the type of destinations where the notifications can be published\.

### Supported event types<a name="supported-notification-event-types"></a>

Amazon S3 can publish events of the following types\. You specify these event types in the notification configuration\.


|  Event types |  Description  | 
| --- | --- | 
|  *s3:ObjectCreated:\** *s3:ObjectCreated:Put* *s3:ObjectCreated:Post* *s3:ObjectCreated:Copy* *s3:ObjectCreated:CompleteMultipartUpload*  | Amazon S3 APIs such as PUT, POST, and COPY can create an object\. Using these event types, you can enable notification when an object is created using a specific API, or you can use the *s3:ObjectCreated:\** event type to request notification regardless of the API that was used to create an object\.  You do not receive event notifications from failed operations\.  | 
|  *s3:ObjectRemoved:\** *s3:ObjectRemoved:Delete* *s3:ObjectRemoved:DeleteMarkerCreated*  | By using the *ObjectRemoved* event types, you can enable notification when an object or a batch of objects is removed from a bucket\. You can request notification when an object is deleted or a versioned object is permanently deleted by using the *s3:ObjectRemoved:Delete* event type\. Or you can request notification when a delete marker is created for a versioned object by using *s3:ObjectRemoved:DeleteMarkerCreated*\. For information about deleting versioned objects, see [Deleting object versions](DeletingObjectVersions.md)\. You can also use a wildcard `s3:ObjectRemoved:*` to request notification anytime an object is deleted\.  You do not receive event notifications from automatic deletes from lifecycle policies or from failed operations\.  | 
|  *s3:ObjectRestore:Post* *s3:ObjectRestore:Completed*  |  Using restore object event types you can receive notifications for initiation and completion when restoring objects from the S3 Glacier storage class\. You use `s3:ObjectRestore:Post` to request notification of object restoration initiation\. You use `s3:ObjectRestore:Completed` to request notification of restoration completion\.   | 
| s3:ReducedRedundancyLostObject | You can use this event type to request Amazon S3 to send a notification message when Amazon S3 detects that an object of the RRS storage class is lost\. | 
| s3:Replication:OperationFailedReplication | You receive this notification event when an object that was eligible for replication using Amazon S3 Replication Time Control failed to replicate\. | 
| s3:Replication:OperationMissedThreshold | You receive this notification event when an object that was eligible for replication using Amazon S3 Replication Time Control exceeded the 15\-minute threshold for replication\. | 
| s3:Replication:OperationReplicatedAfterThreshold | You receive this notification event for an object that was eligible for replication using the Amazon S3 Replication Time Control feature replicated after the 15\-minute threshold\. | 
| s3:Replication:OperationNotTracked | You receive this notification event for an object that was eligible for replication using Amazon S3 Replication Time Control but is no longer tracked by replication metrics\. | 

### Supported destinations<a name="supported-notification-destinations"></a>

Amazon S3 can send event notification messages to the following destinations\. You specify the ARN value of these destinations in the notification configuration\.
+ Publish event messages to an Amazon Simple Notification Service \(Amazon SNS\) topic
+ Publish event messages to an Amazon Simple Queue Service \(Amazon SQS\) queue
**Note**  
If the destination queue or topic is SSE enabled, Amazon S3 will need access to the associated AWS Key Management Service \(AWS KMS\) customer master key \(CMK\) to enable message encryption\.
+ Publish event messages to AWS Lambda by invoking a Lambda function and providing the event message as an argument

You must grant Amazon S3 permissions to post messages to an Amazon SNS topic or an Amazon SQS queue\. You must also grant Amazon S3 permission to invoke an AWS Lambda function on your behalf\. For information about granting these permissions, see [Granting permissions to publish event notification messages to a destination](#grant-destinations-permissions-to-s3)\. 

## Configuring notifications with object key name filtering<a name="notification-how-to-filtering"></a>

You can configure notifications to be filtered by the prefix and suffix of the key name of objects\. For example, you can set up a configuration so that you are sent a notification only when image files with a "`.jpg`" file name extension are added to a bucket\. Or, you can have a configuration that delivers a notification to an Amazon SNS topic when an object with the prefix "`images/`" is added to the bucket, while having notifications for objects with a "`logs/`" prefix in the same bucket delivered to an AWS Lambda function\. 

You can set up notification configurations that use object key name filtering in the Amazon S3 console and by using Amazon S3 APIs through the AWS SDKs or the REST APIs directly\. For information about using the console UI to set a notification configuration on a bucket, see [ How Do I Enable and Configure Event Notifications for an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-event-notifications.html) in the *Amazon Simple Storage Service Console User Guide*\. 

Amazon S3 stores the notification configuration as XML in the *notification* subresource associated with a bucket as described in [How to enable event notifications ](#how-to-enable-disable-notification-intro)\. You use the `Filter` XML structure to define the rules for notifications to be filtered by the prefix and/or suffix of an object key name\. For information about the details of the `Filter` XML structure, see [PUT Bucket notification](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTnotification.html) in the *Amazon Simple Storage Service API Reference*\. 

Notification configurations that use `Filter` cannot define filtering rules with overlapping prefixes, overlapping suffixes, or prefix and suffix overlapping\. The following sections have examples of valid notification configurations with object key name filtering\. They also contain examples of notification configurations that are invalid because of prefix/suffix overlapping\. 

### Examples of valid notification configurations with object key name filtering<a name="notification-how-to-filtering-example-valid"></a>

The following notification configuration contains a queue configuration identifying an Amazon SQS queue for Amazon S3 to publish events to of the `s3:ObjectCreated:Put` type\. The events will be published whenever an object that has a prefix of `images/` and a `jpg` suffix is PUT to a bucket\. 

```
<NotificationConfiguration>
  <QueueConfiguration>
      <Id>1</Id>
      <Filter>
          <S3Key>
              <FilterRule>
                  <Name>prefix</Name>
                  <Value>images/</Value>
              </FilterRule>
              <FilterRule>
                  <Name>suffix</Name>
                  <Value>jpg</Value>
              </FilterRule>
          </S3Key>
     </Filter>
     <Queue>arn:aws:sqs:us-west-2:444455556666:s3notificationqueue</Queue>
     <Event>s3:ObjectCreated:Put</Event>
  </QueueConfiguration>
</NotificationConfiguration>
```

The following notification configuration has multiple non\-overlapping prefixes\. The configuration defines that notifications for PUT requests in the `images/` folder go to queue\-A, while notifications for PUT requests in the `logs/` folder go to queue\-B\.

```
<NotificationConfiguration>
  <QueueConfiguration>
     <Id>1</Id>
     <Filter>
            <S3Key>
                <FilterRule>
                    <Name>prefix</Name>
                    <Value>images/</Value>
                </FilterRule>
            </S3Key>
     </Filter>
     <Queue>arn:aws:sqs:us-west-2:444455556666:sqs-queue-A</Queue>
     <Event>s3:ObjectCreated:Put</Event>
  </QueueConfiguration>
  <QueueConfiguration>
     <Id>2</Id>
     <Filter>
            <S3Key>
                <FilterRule>
                    <Name>prefix</Name>
                    <Value>logs/</Value>
                </FilterRule>
            </S3Key>
     </Filter>
     <Queue>arn:aws:sqs:us-west-2:444455556666:sqs-queue-B</Queue>
     <Event>s3:ObjectCreated:Put</Event>
  </QueueConfiguration>
</NotificationConfiguration>
```

The following notification configuration has multiple non\-overlapping suffixes\. The configuration defines that all `.jpg` images newly added to the bucket are processed by Lambda cloud\-function\-A, and all newly added `.png` images are processed by cloud\-function\-B\. The `.png` and `.jpg` suffixes are not overlapping even though they have the same last letter\. Two suffixes are considered overlapping if a given string can end with both suffixes\. A string cannot end with both `.png` and `.jpg`, so the suffixes in the example configuration are not overlapping suffixes\. 

```
<NotificationConfiguration>
  <CloudFunctionConfiguration>
     <Id>1</Id>
     <Filter>
            <S3Key>
                <FilterRule>
                    <Name>suffix</Name>
                    <Value>.jpg</Value>
                </FilterRule>
            </S3Key>
     </Filter>
     <CloudFunction>arn:aws:lambda:us-west-2:444455556666:cloud-function-A</CloudFunction>
     <Event>s3:ObjectCreated:Put</Event>
  </CloudFunctionConfiguration>
  <CloudFunctionConfiguration>
     <Id>2</Id>
     <Filter>
            <S3Key>
                <FilterRule>
                    <Name>suffix</Name>
                    <Value>.png</Value>
                </FilterRule>
            </S3Key>
     </Filter>
     <CloudFunction>arn:aws:lambda:us-west-2:444455556666:cloud-function-B</CloudFunction>
     <Event>s3:ObjectCreated:Put</Event>
  </CloudFunctionConfiguration>
</NotificationConfiguration>
```

Your notification configurations that use `Filter` cannot define filtering rules with overlapping prefixes for the same event types, unless the overlapping prefixes are used with suffixes that do not overlap\. The following example configuration shows how objects created with a common prefix but non\-overlapping suffixes can be delivered to different destinations\.

```
<NotificationConfiguration>
  <CloudFunctionConfiguration>
     <Id>1</Id>
     <Filter>
            <S3Key>
                <FilterRule>
                    <Name>prefix</Name>
                    <Value>images</Value>
                </FilterRule>
                <FilterRule>
                    <Name>suffix</Name>
                    <Value>.jpg</Value>
                </FilterRule>
            </S3Key>
     </Filter>
     <CloudFunction>arn:aws:lambda:us-west-2:444455556666:cloud-function-A</CloudFunction>
     <Event>s3:ObjectCreated:Put</Event>
  </CloudFunctionConfiguration>
  <CloudFunctionConfiguration>
     <Id>2</Id>
     <Filter>
            <S3Key>
                <FilterRule>
                    <Name>prefix</Name>
                    <Value>images</Value>
                </FilterRule>
                <FilterRule>
                    <Name>suffix</Name>
                    <Value>.png</Value>
                </FilterRule>
            </S3Key>
     </Filter>
     <CloudFunction>arn:aws:lambda:us-west-2:444455556666:cloud-function-B</CloudFunction>
     <Event>s3:ObjectCreated:Put</Event>
  </CloudFunctionConfiguration>
</NotificationConfiguration>
```

### Examples of notification configurations with invalid Prefix/Suffix overlapping<a name="notification-how-to-filtering-examples-invalid"></a>

For the most part, your notification configurations that use `Filter` cannot define filtering rules with overlapping prefixes, overlapping suffixes, or overlapping combinations of prefixes and suffixes for the same event types\. \(You can have overlapping prefixes as long as the suffixes do not overlap\. For an example, see [Configuring notifications with object key name filtering](#notification-how-to-filtering)\.\)

You can use overlapping object key name filters with different event types\. For example, you could create a notification configuration that uses the prefix `image/` for the `ObjectCreated:Put` event type and the prefix `image/` for the `ObjectRemoved:*` event type\. 

You get an error if you try to save a notification configuration that has invalid overlapping name filters for the same event types when using the Amazon S3 console or API\. This section shows examples of notification configurations that are not valid because of overlapping name filters\. 

Any existing notification configuration rule is assumed to have a default prefix and suffix that match any other prefix and suffix respectively\. The following notification configuration is not valid because it has overlapping prefixes, where the root prefix overlaps with any other prefix\. \(The same thing would be true if you were using a suffix instead of a prefix in this example\. The root suffix overlaps with any other suffix\.\)

```
<NotificationConfiguration>
     <TopicConfiguration>
         <Topic>arn:aws:sns:us-west-2:444455556666:sns-notification-one</Topic>
         <Event>s3:ObjectCreated:*</Event>
    </TopicConfiguration>
    <TopicConfiguration>
         <Topic>arn:aws:sns:us-west-2:444455556666:sns-notification-two</Topic>
         <Event>s3:ObjectCreated:*</Event>
         <Filter>
             <S3Key>
                 <FilterRule>
                     <Name>prefix</Name>
                     <Value>images</Value>
                 </FilterRule>
            </S3Key>
        </Filter>
    </TopicConfiguration>             
</NotificationConfiguration>
```

The following notification configuration is not valid because it has overlapping suffixes\. Two suffixes are considered overlapping if a given string can end with both suffixes\. A string can end with `jpg` and `pg`, so the suffixes are overlapping\. \(The same is true for prefixes; two prefixes are considered overlapping if a given string can begin with both prefixes\.\)

```
 <NotificationConfiguration>
     <TopicConfiguration>
         <Topic>arn:aws:sns:us-west-2:444455556666:sns-topic-one</Topic>
         <Event>s3:ObjectCreated:*</Event>
         <Filter>
             <S3Key>
                 <FilterRule>
                     <Name>suffix</Name>
                     <Value>jpg</Value>
                 </FilterRule>
            </S3Key>
        </Filter>
    </TopicConfiguration>
    <TopicConfiguration>
         <Topic>arn:aws:sns:us-west-2:444455556666:sns-topic-two</Topic>
         <Event>s3:ObjectCreated:Put</Event>
         <Filter>
             <S3Key>
                 <FilterRule>
                     <Name>suffix</Name>
                     <Value>pg</Value>
                 </FilterRule>
            </S3Key>
        </Filter>
    </TopicConfiguration>
</NotificationConfiguration
```

The following notification configuration is not valid because it has overlapping prefixes and suffixes\. 

```
<NotificationConfiguration>
     <TopicConfiguration>
         <Topic>arn:aws:sns:us-west-2:444455556666:sns-topic-one</Topic>
         <Event>s3:ObjectCreated:*</Event>
         <Filter>
             <S3Key>
                 <FilterRule>
                     <Name>prefix</Name>
                     <Value>images</Value>
                 </FilterRule>
                 <FilterRule>
                     <Name>suffix</Name>
                     <Value>jpg</Value>
                 </FilterRule>
            </S3Key>
        </Filter>
    </TopicConfiguration>
    <TopicConfiguration>
         <Topic>arn:aws:sns:us-west-2:444455556666:sns-topic-two</Topic>
         <Event>s3:ObjectCreated:Put</Event>
         <Filter>
             <S3Key>
                 <FilterRule>
                     <Name>suffix</Name>
                     <Value>jpg</Value>
                 </FilterRule>
            </S3Key>
        </Filter>
    </TopicConfiguration>
</NotificationConfiguration>
```

## Granting permissions to publish event notification messages to a destination<a name="grant-destinations-permissions-to-s3"></a>

Before Amazon S3 can publish messages to a destination, you must grant the Amazon S3 principal the necessary permissions to call the relevant API to publish messages to an SNS topic, an SQS queue, or a Lambda function\. 

### Granting permissions to invoke an AWS Lambda function<a name="grant-lambda-invoke-permission-to-s3"></a>

Amazon S3 publishes event messages to AWS Lambda by invoking a Lambda function and providing the event message as an argument\.

When you use the Amazon S3 console to configure event notifications on an Amazon S3 bucket for a Lambda function, the console sets up the necessary permissions on the Lambda function so that Amazon S3 has permissions to invoke the function from the bucket\. For more information, see [How Do I Enable and Configure Event Notifications for an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-event-notifications.html) in the *Amazon Simple Storage Service Console User Guide*\. 

You can also grant Amazon S3 permissions from AWS Lambda to invoke your Lambda function\. For more information, see [Tutorial: Using AWS Lambda with Amazon S3](https://docs.aws.amazon.com/lambda/latest/dg/with-s3-example.html) in the *AWS Lambda Developer Guide*\.

### Granting permissions to publish messages to an SNS topic or an SQS queue<a name="grant-sns-sqs-permission-for-s3"></a>

To grant Amazon S3 permissions to publish messages to the SNS topic or SQS queue, you attach an AWS Identity and Access Management \(IAM\) policy to the destination SNS topic or SQS queue\. 

For an example of how to attach a policy to an SNS topic or an SQS queue, see [Walkthrough: Configure a bucket for notifications \(SNS topic or SQS queue\)](ways-to-add-notification-config-to-bucket.md)\. For more information about permissions, see the following topics:
+ [Example Cases for Amazon SNS Access Control](https://docs.aws.amazon.com/sns/latest/dg/AccessPolicyLanguage_UseCases_Sns.html) in the *Amazon Simple Notification Service Developer Guide*
+ [Access Control Using AWS Identity and Access Management \(IAM\)](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/UsingIAM.html) in the *Amazon Simple Queue Service Developer Guide*

#### IAM policy for a destination SNS topic<a name="sns-topic-policy"></a>

The following is an example of an IAM policy that you attach to the destination SNS topic\.

```
{
 "Version": "2012-10-17",
 "Id": "example-ID",
 "Statement": [
  {
   "Sid": "example-statement-ID",
   "Effect": "Allow",
   "Principal": {
     "Service": "s3.amazonaws.com"  
   },
   "Action": [
    "SNS:Publish"
   ],
   "Resource": "arn:aws:sns:Region:account-id:topic-name",
   "Condition": {
      "ArnLike": { "aws:SourceArn": "arn:aws:s3:::awsexamplebucket1" },
      "StringEquals": { "aws:SourceAccount": "bucket-owner-account-id" }
   }
  }
 ]
}
```

#### IAM policy for a destination SQS queue<a name="sqs-queue-policy"></a>

The following is an example of an IAM policy that you attach to the destination SQS queue\.

```
{
 "Version": "2012-10-17",
 "Id": "example-ID",
 "Statement": [
  {
   "Sid": "example-statement-ID",
   "Effect": "Allow",
   "Principal": {
     "Service": "s3.amazonaws.com"  
   },
   "Action": [
    "SQS:SendMessage"
   ],
   "Resource": "arn:aws:sqs:Region:account-id:queue-name",
   "Condition": {
      "ArnLike": { "aws:SourceArn": "arn:aws:s3:*:*:awsexamplebucket1" },
      "StringEquals": { "aws:SourceAccount": "bucket-owner-account-id" }
   }
  }
 ]
}
```

Note that for both the Amazon SNS and Amazon SQS IAM policies, you can specify the `StringLike` condition in the policy, instead of the `ArnLike` condition\.

```
"Condition": {         
  "StringLike": { "aws:SourceArn": "arn:aws:s3:*:*:bucket-name" }
  }
```

#### AWS KMS key policy<a name="key-policy-sns-sqs"></a>

If the SQS queue or SNS topics are encrypted with an AWS Key Management Service \(AWS KMS\) customer managed customer master key \(CMK\), you must grant the Amazon S3 service principal permission to work with the encrypted topics and or queue\. To grant the Amazon S3 service principal permission, add the following statement to the key policy for the customer managed CMK:

```
{
    "Version": "2012-10-17",
    "Id": "example-ID",
    "Statement": [
        {
            "Sid": "example-statement-ID",
            "Effect": "Allow",
            "Principal": {
                "Service": "s3.amazonaws.com"
            },
            "Action": [
                "kms:GenerateDataKey",
                "kms:Decrypt"
            ],
            "Resource": "*"
        }
    ]
}
```

For more information about AWS KMS key policies, see [Using Key Policies in AWS KMS](https://docs.aws.amazon.com/kms/latest/developerguide/key-policies.html) in the *AWS Key Management Service Developer Guide*\. For more information about using server\-side encryption with AWS KMS for Amazon SQS and Amazon SNS, see the following:
+ [Configuring AWS KMS Permissions for Amazon SNS](https://docs.aws.amazon.com/sns/latest/dg/sns-key-management.html) in the *Amazon Simple Notification Service Developer Guide*\.
+ [Configuring AWS KMS Permissions for Amazon SQS](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-key-management.html) in the *Amazon Simple Queue Service Developer Guide*\.
+ [Encrypting messages published to Amazon SNS with AWS KMS](http://aws.amazon.com/blogs/compute/encrypting-messages-published-to-amazon-sns-with-aws-kms/) in the *AWS Compute Blog*\.