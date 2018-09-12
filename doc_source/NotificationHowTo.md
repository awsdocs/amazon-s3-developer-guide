# Configuring Amazon S3 Event Notifications<a name="NotificationHowTo"></a>

The Amazon S3 notification feature enables you to receive notifications when certain events happen in your bucket\. To enable notifications, you must first add a notification configuration identifying the events you want Amazon S3 to publish, and the destinations where you want Amazon S3 to send the event notifications\. You store this configuration in the *notification* subresource \(see [Bucket Configuration Options](UsingBucket.md#bucket-config-options-intro)\) associated with a bucket\. Amazon S3 provides an API for you to manage this subresource\. 

**Topics**
+ [Overview](#notification-how-to-overview)
+ [How to Enable Event Notifications](#how-to-enable-disable-notification-intro)
+ [Event Notification Types and Destinations](#notification-how-to-event-types-and-destinations)
+ [Configuring Notifications with Object Key Name Filtering](#notification-how-to-filtering)
+ [Granting Permissions to Publish Event Notification Messages to a Destination](#grant-destinations-permissions-to-s3)
+ [Example Walkthrough 1: Configure a Bucket for Notifications \(Message Destination: SNS Topic and SQS Queue\)](ways-to-add-notification-config-to-bucket.md)
+ [Example Walkthrough 2: Configure a Bucket for Notifications \(Message Destination: AWS Lambda\)](notification-walkthrough-2.md)
+ [Event Message Structure](notification-content-structure.md)

## Overview<a name="notification-how-to-overview"></a>

 Currently, Amazon S3 can publish the following events:
+ A new object created event—Amazon S3 supports multiple APIs to create objects\. You can request notification when only a specific API is used \(e\.g\., `s3:ObjectCreated:Put`\) or you can use a wildcard \(e\.g\., `s3:ObjectCreated:*`\) to request notification when an object is created regardless of the API used\. 
+ An object removal event—Amazon S3 supports deletes of versioned and unversioned objects\. For information about object versioning, see [Object Versioning](ObjectVersioning.md) and [Using Versioning](Versioning.md)\. 

  You can request notification when an object is deleted or a versioned object is permanently deleted by using the `s3:ObjectRemoved:Delete` event type\. Or you can request notification when a delete marker is created for a versioned object by using `s3:ObjectRemoved:DeleteMarkerCreated`\. You can also use a wildcard `s3:ObjectRemoved:*` to request notification anytime an object is deleted\. For information about deleting versioned objects, see [Deleting Object Versions](DeletingObjectVersions.md)\. 
+ A Reduced Redundancy Storage \(RRS\) object lost event—Amazon S3 sends a notification message when it detects that an object of the RRS storage class has been lost\. 

For a list of supported event types, see [Supported Event Types](#supported-notification-event-types)\. 

Amazon S3 supports the following destinations where it can publish events:
+ Amazon Simple Notification Service \(Amazon SNS\) topic

  Amazon SNS is a flexible, fully managed push messaging service\. Using this service, you can push messages to mobile devices or distributed services\. With SNS you can publish a message once, and deliver it one or more times\. An SNS topic is an access point that recipients can dynamically subscribe to in order to receive event notifications\. For more information about SNS, see the [Amazon SNS](https://aws.amazon.com/sns/) product detail page\.
+ Amazon Simple Queue Service \(Amazon SQS\) queue

  Amazon SQS is a scalable and fully managed message queuing service\. You can use SQS to transmit any volume of data without requiring other services to be always available\. In your notification configuration you can request that Amazon S3 publish events to an SQS queue\. For more information about SQS, see [Amazon SQS](https://aws.amazon.com/sqs/) product detail page\.
+ AWS Lambda

  AWS Lambda is a compute service that makes it easy for you to build applications that respond quickly to new information\. AWS Lambda runs your code in response to events such as image uploads, in\-app activity, website clicks, or outputs from connected devices\. You can use AWS Lambda to extend other AWS services with custom logic, or create your own back\-end that operates at AWS scale, performance, and security\. With AWS Lambda, you can easily create discrete, event\-driven applications that execute only when needed and scale automatically from a few requests per day to thousands per second\. 

  AWS Lambda can run custom code in response to Amazon S3 bucket events\. You upload your custom code to AWS Lambda and create what is called a Lambda function\. When Amazon S3 detects an event of a specific type \(for example, an object created event\), it can publish the event to AWS Lambda and invoke your function in Lambda\. In response, AWS Lambda executes your function\. For more information, see [AWS Lambda](https://aws.amazon.com/lambda/) product detail page\. 

The following sections offer more detail about how to enable event notifications on a bucket\. The subtopics also provide example walkthroughs to help you explore the notification feature\.
+  [Example Walkthrough 1: Configure a Bucket for Notifications \(Message Destination: SNS Topic and SQS Queue\)](ways-to-add-notification-config-to-bucket.md)
+  [Example Walkthrough 2: Configure a Bucket for Notifications \(Message Destination: AWS Lambda\)](notification-walkthrough-2.md) 

## How to Enable Event Notifications<a name="how-to-enable-disable-notification-intro"></a>

Enabling notifications is a bucket\-level operation; that is, you store notification configuration information in the *notification* subresource associated with a bucket\. You can use any of the following methods to manage notification configuration:
+ Using the Amazon S3 console

  The console UI enables you to set a notification configuration on a bucket without having to write any code\. For instruction, see [How Do I Enable and Configure Event Notifications for an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-event-notifications.html) in the *Amazon Simple Storage Service Console User Guide*\.
+ Programmatically using the AWS SDKs
**Note**  
If you need to, you can also make the Amazon S3 REST API calls directly from your code\. However, this can be cumbersome because it requires you to write code to authenticate your requests\. 

  Internally, both the console and the SDKs call the Amazon S3 REST API to manage *notification* subresources associated with the bucket\. For notification configuration using AWS SDK examples, see the walkthrough link provided in the preceding section\.

  Regardless of the method you use, Amazon S3 stores the notification configuration as XML in the *notification* subresource associated with a bucket\. For information about bucket subresources, see [Bucket Configuration Options](UsingBucket.md#bucket-config-options-intro)\)\. By default, notifications are not enabled for any type of event\. Therefore, initially the *notification* subresource stores an empty configuration\.

  ```
  <NotificationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/"> 
  </NotificationConfiguration>
  ```

  To enable notifications for events of specific types, you replace the XML with the appropriate configuration that identifies the event types you want Amazon S3 to publish and the destination where you want the events published\. For each destination, you add a corresponding XML configuration\. For example: 
  + Publish event messages to an SQS queue—To set an SQS queue as the notification destination for one or more event types, you add the `QueueConfiguration`\.

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
  + Publish event messages to an SNS topic—To set an SNS topic as the notification destination for specific event types, you add the `TopicConfiguration`\.

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
  + Invoke the AWS Lambda function and provide an event message as an argument—To set a Lambda function as the notification destination for specific event types, you add the `CloudFunctionConfiguration`\.

    ```
    <NotificationConfiguration>
      <CloudFunctionConfiguration>   
         <Id>optional-id-string</Id>   
         <Cloudcode>cloud-function-arn</Cloudcode>        
         <Event>event-type</Event>      
         <Event>event-type</Event>      
          ...  
      </CloudFunctionConfiguration>  
       ...
    </NotificationConfiguration>
    ```

  To remove all notifications configured on a bucket, you save an empty `<NotificationConfiguration/>` element in the *notification* subresource\. 

  When Amazon S3 detects an event of the specific type, it publishes a message with the event information\. For more information, see [Event Message Structure](notification-content-structure.md)\. 

## Event Notification Types and Destinations<a name="notification-how-to-event-types-and-destinations"></a>

This section describes the event notification types that are supported by Amazon S3 and the type of destinations where the notifications can be published\.

### Supported Event Types<a name="supported-notification-event-types"></a>

Amazon S3 can publish events of the following types\. You specify these event types in the notification configuration\.


|  Event types |  Description  | 
| --- | --- | 
|  *s3:ObjectCreated:\** *s3:ObjectCreated:Put* *s3:ObjectCreated:Post* *s3:ObjectCreated:Copy* *s3:ObjectCreated:CompleteMultipartUpload*  | Amazon S3 APIs such as PUT, POST, and COPY can create an object\. Using these event types, you can enable notification when an object is created using a specific API, or you can use the *s3:ObjectCreated:\** event type to request notification regardless of the API that was used to create an object\.  You will not receive event notifications from failed operations\.  | 
|  *s3:ObjectRemoved:\** *s3:ObjectRemoved:Delete* *s3:ObjectRemoved:DeleteMarkerCreated*  | By using the *ObjectRemoved* event types, you can enable notification when an object or a batch of objects is removed from a bucket\.  You can request notification when an object is deleted or a versioned object is permanently deleted by using the *s3:ObjectRemoved:Delete* event type\. Or you can request notification when a delete marker is created for a versioned object by using *s3:ObjectRemoved:DeleteMarkerCreated*\. For information about deleting versioned objects, see [Deleting Object Versions](DeletingObjectVersions.md)\. You can also use a wildcard `s3:ObjectRemoved:*` to request notification anytime an object is deleted\.  You will not receive event notifications from automatic deletes from lifecycle policies or from failed operations\.  | 
| s3:ReducedRedundancyLostObject | You can use this event type to request Amazon S3 to send a notification message when Amazon S3 detects that an object of the RRS storage class is lost\. | 

### Supported Destinations<a name="supported-notification-destinations"></a>

Amazon S3 can send event notification messages to the following destinations\. You specify the ARN value of these destinations in the notification configuration\.
+ Publish event messages to an Amazon Simple Notification Service \(Amazon SNS\) topic
+ Publish event messages to an Amazon Simple Queue Service \(Amazon SQS\) queue
**Note**  
If the destination queue is SSE enabled, Amazon S3 will need access to the associated KMS key to enable message encryption\.
+ Publish event messages to AWS Lambda by invoking a Lambda function and providing the event message as an argument

You must grant Amazon S3 permissions to post messages to an Amazon SNS topic or an Amazon SQS queue\. You must also grant Amazon S3 permission to invoke an AWS Lambda function on your behalf\. For information about granting these permissions, see [Granting Permissions to Publish Event Notification Messages to a Destination](#grant-destinations-permissions-to-s3)\. 

## Configuring Notifications with Object Key Name Filtering<a name="notification-how-to-filtering"></a>

You can configure notifications to be filtered by the prefix and suffix of the key name of objects\. For example, you can set up a configuration so that you are sent a notification only when image files with a "\.jpg" extension are added to a bucket\. Or you can have a configuration that delivers a notification to an Amazon SNS topic when an object with the prefix "images/" is added to the bucket, while having notifications for objects with a "logs/" prefix in the same bucket delivered to an AWS Lambda function\. 

You can set up notification configurations that use object key name filtering in the Amazon S3 console and by using Amazon S3 APIs through the AWS SDKs or the REST APIs directly\. For information about using the console UI to set a notification configuration on a bucket, see [ How Do I Enable and Configure Event Notifications for an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-event-notifications.html) in the *Amazon Simple Storage Service Console User Guide*\. 

Amazon S3 stores the notification configuration as XML in the *notification* subresource associated with a bucket as described in [How to Enable Event Notifications ](#how-to-enable-disable-notification-intro)\. You use the `Filter` XML structure to define the rules for notifications to be filtered by the prefix and/or suffix of an object key name\. For information about the details of the `Filter` XML structure, see [PUT Bucket notification](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTnotification.html) in the *Amazon Simple Storage Service API Reference*\. 

Notification configurations that use `Filter` cannot define filtering rules with overlapping prefixes, overlapping suffixes, or prefix and suffix overlapping\. The following sections have examples of valid notification configurations with object key name filtering and examples of notification configurations that are invalid because of prefix/suffix overlapping\. 

### Examples of Valid Notification Configurations with Object Key Name Filtering<a name="notification-how-to-filtering-example-valid"></a>

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

The following notification configuration has multiple non\-overlapping prefixes\. The configuration defines that notifications for PUT requests in the `images/` folder will go to queue\-A while notifications for PUT requests in the `logs/` folder will go to queue\-B\.

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

The following notification configuration has multiple non\-overlapping suffixes\. The configuration defines that all \.jpg images newly added to the bucket will be processed by Lambda cloud\-function\-A and all newly added \.png images will be processed by cloud\-function\-B\. The suffixes \.png and \.jpg are not overlapping even though they have the same last letter\. Two suffixes are considered overlapping if a given string can end with both suffixes\. A string cannot end with both \.png and \.jpg so the suffixes in the example configuration are not overlapping suffixes\. 

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
     <Cloudcode>arn:aws:lambda:us-west-2:444455556666:cloud-function-A</Cloudcode>
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
     <Cloudcode>arn:aws:lambda:us-west-2:444455556666:cloud-function-B</Cloudcode>
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
     <Cloudcode>arn:aws:lambda:us-west-2:444455556666:cloud-function-A</Cloudcode>
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
     <Cloudcode>arn:aws:lambda:us-west-2:444455556666:cloud-function-B</Cloudcode>
     <Event>s3:ObjectCreated:Put</Event>
  </CloudFunctionConfiguration>
</NotificationConfiguration>
```

### Examples of Notification Configurations with Invalid Prefix/Suffix Overlapping<a name="notification-how-to-filtering-examples-invalid"></a>

Your notification configurations that use `Filter`, for the most part, cannot define filtering rules with overlapping prefixes, overlapping suffixes, or overlapping combinations of prefixes and suffixes for the same event types\. \(You can have overlapping prefixes as long as the suffixes do not overlap\. For an example, see [Configuring Notifications with Object Key Name Filtering](#notification-how-to-filtering)\.\)

You can use overlapping object key name filters with different event types\. For example, you could create a notification configuration that uses the prefix `image/` for the `ObjectCreated:Put` event type and the prefix `image/` for the `ObjectDeleted:*` event type\. 

You will get an error if you try to save a notification configuration that has invalid overlapping name filters for the same event types, when using the AWS Amazon S3 console or when using the Amazon S3 API\. This section shows examples of notification configurations that are invalid because of overlapping name filters\. 

Any existing notification configuration rule is assumed to have a default prefix and suffix that match any other prefix and suffix respectively\. The following notification configuration is invalid because it has overlapping prefixes, where the root prefix overlaps with any other prefix\. \(The same thing would be true if we were using suffix instead of prefix in this example\. The root suffix overlaps with any other suffix\.\)

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

The following notification configuration is invalid because it has overlapping suffixes\. Two suffixes are considered overlapping if a given string can end with both suffixes\. A string can end with `jpg` and `pg` so the suffixes are overlapping\. \(The same is true for prefixes, two prefixes are considered overlapping if a given string can begin with both prefixes\.\)

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

The following notification configuration is invalid because it has overlapping prefixes and suffixes\. 

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
         <Topic>arn:aws:snsus-west-2:444455556666:sns-topic-two</Topic>
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

## Granting Permissions to Publish Event Notification Messages to a Destination<a name="grant-destinations-permissions-to-s3"></a>

Before Amazon S3 can publish messages to a destination, you must grant the Amazon S3 principal the necessary permissions to call the relevant API to publish messages to an SNS topic, an SQS queue, or a Lambda function\. 

### Granting Permissions to Invoke an AWS Lambda Function<a name="grant-lambda-invoke-permission-to-s3"></a>

Amazon S3 publishes event messages to AWS Lambda by invoking a Lambda function and providing the event message as an argument\.

When you use the Amazon S3 console to configure event notifications on an Amazon S3 bucket for a Lambda function, the Amazon S3 console will set up the necessary permissions on the Lambda function so that Amazon S3 has permissions to invoke the function from the bucket\. For more information, see [How Do I Enable and Configure Event Notifications for an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-event-notifications.html) in the *Amazon Simple Storage Service Console User Guide*\. 

You can also grant Amazon S3 permissions from AWS Lambda to invoke your Lambda function\. For more information, see [Tutorial: Using AWS Lambda with Amazon S3](http://docs.aws.amazon.com/lambda/latest/dg/with-s3-example.html) in the *AWS Lambda Developer Guide*\.

### Granting Permissions to Publish Messages to an SNS Topic or an SQS Queue<a name="grant-sns-sqs-permission-for-s3"></a>

You attach an IAM policy to the destination SNS topic or SQS queue to grant Amazon S3 permissions to publish messages to the SNS topic or SQS queue\. 

Example of an IAM policy that you attach to the destination SNS topic\.

```
{
 "Version": "2008-10-17",
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
   "Resource": "SNS-ARN",
   "Condition": {
      "ArnLike": { "aws:SourceArn": "arn:aws:s3:*:*:bucket-name" }
   }
  }
 ]
}
```

Example of an IAM policy that you attach to the destination SQS queue\.

```
{
 "Version": "2008-10-17",
 "Id": "example-ID",
 "Statement": [
  {
   "Sid": "example-statement-ID",
   "Effect": "Allow",
   "Principal": {
     "AWS": "*"  
   },
   "Action": [
    "SQS:SendMessage"
   ],
   "Resource": "SQS-ARN",
   "Condition": {
      "ArnLike": { "aws:SourceArn": "arn:aws:s3:*:*:bucket-name" }
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

Example of a key policy that you attach to the associated KMS key if the SQS queue is SSE enabled\. 

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

The policy grants Amazon S3 service principal permission for specific KMS actions that are necessary to encrypt messages added to the queue\.

For an example of how to attach a policy to a SNS topic or an SQS queue, see [Example Walkthrough 1: Configure a Bucket for Notifications \(Message Destination: SNS Topic and SQS Queue\)](ways-to-add-notification-config-to-bucket.md)\.

For more information about permissions, see the following topics:
+ [Example Cases for Amazon SNS Access Control](http://docs.aws.amazon.com/sns/latest/dg/AccessPolicyLanguage_UseCases_Sns.html) in the *Amazon Simple Notification Service Developer Guide*
+ [Access Control Using AWS Identity and Access Management \(IAM\)](http://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/UsingIAM.html) in the *Amazon Simple Queue Service Developer Guide*