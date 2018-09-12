# Example Walkthrough 1: Configure a Bucket for Notifications \(Message Destination: SNS Topic and SQS Queue\)<a name="ways-to-add-notification-config-to-bucket"></a>

**Topics**
+ [Walkthrough Summary](#notification-walkthrough-summary)
+ [Step 1: Create an Amazon SNS Topic](#step1-create-sns-topic-for-notification)
+ [Step 2: Create an Amazon SQS Queue](#step1-create-sqs-queue-for-notification)
+ [Step 3: Add a Notification Configuration to Your Bucket](#step2-enable-notification)
+ [Step 4: Test the Setup](#notification-walkthrough-1-test)

## Walkthrough Summary<a name="notification-walkthrough-summary"></a>

In this walkthrough you add notification configuration on a bucket requesting Amazon S3 to:
+ Publish events of the `s3:ObjectCreated:*` type to an Amazon SQS queue\.
+ Publish events of the `s3:ReducedRedundancyLostObject` type to an Amazon SNS topic\.

For information about notification configuration, see [ Configuring Amazon S3 Event Notifications](NotificationHowTo.md)\.

You can do all these steps using the console, without writing any code\. In addition, code examples, using AWS SDKs for Java and \.NET are also provided so you can add notification configuration programmatically\. 

You will do the following in this walkthrough:

1. Create an Amazon SNS topic\.

   Using the Amazon SNS console, you create an SNS topic and subscribe to the topic so that any events posted to it are delivered to you\. You will specify email as the communications protocol\. After you create a topic, Amazon SNS will send an email\. You must click a link in the email to confirm the topic subscription\. 

   You will attach an access policy to the topic to grant Amazon S3 permission to post messages\. 

1. Create an Amazon SQS queue\.

   Using the Amazon SQS console, you create an SQS queue\. You can access any messages Amazon S3 sends to the queue programmatically\. But for this walkthrough, you will verify notification messages in the console\. 

   You will attach an access policy to the topic to grant Amazon S3 permission to post messages\.

1. Add notification configuration to a bucket\. 

## Step 1: Create an Amazon SNS Topic<a name="step1-create-sns-topic-for-notification"></a>

Follow the steps to create and subscribe to an Amazon Simple Notification Service \(Amazon SNS\) topic\.

1. Using Amazon SNS console create a topic\. For instructions, see [Create a Topic](http://docs.aws.amazon.com/sns/latest/dg/CreateTopic.html) in the *Amazon Simple Notification Service Developer Guide*\. 

1. Subscribe to the topic\. For this exercise, use email as the communications protocol\. For instructions, see [Subscribe to a Topic](http://docs.aws.amazon.com/sns/latest/dg/SubscribeTopic.html) in the *Amazon Simple Notification Service Developer Guide*\. 

   You will get email requesting you to confirm your subscription to the topic\. Confirm the subscription\. 

1. Replace the access policy attached to the topic by the following policy\. You must update the policy by providing your SNS topic ARN and bucket name:

   ```
   {
    "Version": "2008-10-17",
    "Id": "example-ID",
    "Statement": [
     {
      "Sid": "example-statement-ID",
      "Effect": "Allow",
      "Principal": {
       "AWS":"*"  
      },
      "Action": [
       "SNS:Publish"
      ],
      "Resource": "SNS-topic-ARN",
      "Condition": {
         "ArnLike": { "aws:SourceArn": "arn:aws:s3:*:*:bucket-name" }
      }
     }
    ]
   }
   ```

1. Note the topic ARN\.

   The SNS topic you created is another resource in your AWS account, and it has a unique Amazon Resource Name \(ARN\)\. You will need this ARN in the next step\. The ARN will be of the following format:

   ```
   arn:aws:sns:aws-region:account-id:topic-name
   ```

## Step 2: Create an Amazon SQS Queue<a name="step1-create-sqs-queue-for-notification"></a>

Follow the steps to create and subscribe to an Amazon Simple Queue Service \(Amazon SQS\) queue\.

1. Using the Amazon SQS console, create a queue\. For instructions, see [Getting Started with Amazon SQS](http://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-getting-started.html) in the *Amazon Simple Queue Service Developer Guide*\. 

1. Replace the access policy attached to the queue with the following policy \(in the SQS console, you select the queue, and in the **Permissions** tab, click **Edit Policy Document \(Advanced\)**\.

   ```
   {
    "Version": "2012-10-17",
    "Id": "example-ID",
    "Statement": [
     {
      "Sid": "example-statement-ID",
      "Effect": "Allow",
      "Principal": {
       "AWS":"*"  
      },
      "Action": [
       "SQS:SendMessage"
      ],
      "Resource": "SQS-queue-ARN",
      "Condition": {
         "ArnLike": { "aws:SourceArn": "arn:aws:s3:*:*:bucket-name" }
      }
     }
    ]
   }
   ```

1. \(Optional\) If the Amazon SQS queue is server\-side encryption \(SSE\) enabled, add the following policy to the associated custom AWS Key Management Service \(AWS KMS\) customer master key \(CMK\)\. You must add the policy to a custom CMK because the default AWS managed CMK for Amazon SQS cannot be modified\. For more information about using SSE for Amazon SQS with AWS KMS, see [Protecting Data Using Server\-Side Encryption \(SSE\) and AWS KMS](http://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-server-side-encryption.html)\. 

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

1. Note the queue ARN 

   The SQS queue you created is another resource in your AWS account, and it has a unique Amazon Resource Name \(ARN\)\. You will need this ARN in the next step\. The ARN will be of the following format:

   ```
   arn:aws:sqs:aws-region:account-id:queue-name
   ```

## Step 3: Add a Notification Configuration to Your Bucket<a name="step2-enable-notification"></a>

You can enable bucket notifications either by using the Amazon S3 console or programmatically by using AWS SDKs\. Choose any one of the options to configure notifications on your bucket\. This section provides code examples using the AWS SDKs for Java and \.NET\.

### Step 3 \(option a\): Enable Notifications on a Bucket Using the Console<a name="step2-enable-notification-using-console"></a>

Using the Amazon S3 console, add a notification configuration requesting Amazon S3 to:
+ Publish events of the `s3:ObjectCreated:*` type to your Amazon SQS queue\.
+ Publish events of the `s3:ReducedRedundancyLostObject` type to your Amazon SNS topic\.

After you save the notification configuration, Amazon S3 will post a test message, which you will get via email\. 

For instructions, see [How Do I Enable and Configure Event Notifications for an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-event-notifications.html) in the *Amazon Simple Storage Service Console User Guide*\. 

### Step 3 \(option b\): Enable Notifications on a Bucket Using the AWS SDK for \.NET<a name="step2-enable-notification-using-awssdk-dotnet"></a>

The following C\# code example provides a complete code listing that adds a notification configuration to a bucket\. You will need to update the code and provide your bucket name and SNS topic ARN\. For information about how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\. 

**Example**  

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

﻿using Amazon.S3;
using Amazon.S3.Model;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Amazon.DocSamples.S3
{
    class EnableNotificationsTest
    {
        private const string bucketName = "*** bucket name ***";
        private const string snsTopic = "*** SNS topic ARN ***";
        private const string sqsQueue = "*** SQS topic ARN ***";
        // Specify your bucket region (an example region is shown).
        private static readonly RegionEndpoint bucketRegion = RegionEndpoint.USWest2;
        private static IAmazonS3 client;

        public static void Main()
        {
            client = new AmazonS3Client(bucketRegion);
            EnableNotificationAsync().Wait();
        }

        static async Task EnableNotificationAsync()
        {
            try
            {
                PutBucketNotificationRequest request = new PutBucketNotificationRequest
                {
                    BucketName = bucketName
                };

                TopicConfiguration c = new TopicConfiguration
                {
                    Events = new List<EventType> { EventType.ObjectCreatedCopy },
                    Topic = snsTopic
                };
                request.TopicConfigurations = new List<TopicConfiguration>();
                request.TopicConfigurations.Add(c);
                request.QueueConfigurations = new List<QueueConfiguration>();
                request.QueueConfigurations.Add(new QueueConfiguration()
                {
                    Events = new List<EventType> { EventType.ObjectCreatedPut },
                    Queue = sqsQueue
                });
                
                PutBucketNotificationResponse response = await client.PutBucketNotificationAsync(request);
            }
            catch (AmazonS3Exception e)
            {
                Console.WriteLine("Error encountered on server. Message:'{0}' ", e.Message);
            }
            catch (Exception e)
            {
                Console.WriteLine("Unknown error encountered on server. Message:'{0}' ", e.Message);
            }
        }
    }
}
```

### Step 3 \(option c\): Enable Notifications on a Bucket Using the AWS SDK for Java<a name="step2-enable-notification-using-java"></a>

The following example shows how to add a notification configuration to a bucket\. For instructions on creating and testing a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\.

**Example**  

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

import java.io.IOException;
import java.util.EnumSet;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketNotificationConfiguration;
import com.amazonaws.services.s3.model.TopicConfiguration;
import com.amazonaws.services.s3.model.QueueConfiguration;
import com.amazonaws.services.s3.model.S3Event;
import com.amazonaws.services.s3.model.SetBucketNotificationConfigurationRequest;

public class EnableNotificationOnABucket {

    public static void main(String[] args) throws IOException {
        String bucketName = "*** Bucket name ***";
        String clientRegion = "*** Client region ***";
        String snsTopicARN = "*** SNS Topic ARN ***";
        String sqsQueueARN = "*** SQS Queue ARN ***";

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(clientRegion)
                    .build();
            BucketNotificationConfiguration notificationConfiguration = new BucketNotificationConfiguration();

            // Add an SNS topic notification.
            notificationConfiguration.addConfiguration("snsTopicConfig",
                    new TopicConfiguration(snsTopicARN, EnumSet.of(S3Event.ObjectCreated)));

            // Add an SQS queue notification.
            notificationConfiguration.addConfiguration("sqsQueueConfig",
                    new QueueConfiguration(sqsQueueARN, EnumSet.of(S3Event.ObjectCreated)));
            
            // Create the notification configuration request and set the bucket notification configuration.
            SetBucketNotificationConfigurationRequest request = new SetBucketNotificationConfigurationRequest(
                    bucketName, notificationConfiguration);
            s3Client.setBucketNotificationConfiguration(request);
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }
}
```

## Step 4: Test the Setup<a name="notification-walkthrough-1-test"></a>

Now you can test the setup by uploading an object to your bucket and verify the event notification in the Amazon SQS console\. For instructions, see [Receiving a Message](http://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-getting-started.htmlReceiveMessage.html) in the *Amazon Simple Queue Service Developer Guide "Getting Started" section*\. 