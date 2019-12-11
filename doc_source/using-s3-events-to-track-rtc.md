# Using Amazon S3 Events to Track S3 Replication Time Control Objects<a name="using-s3-events-to-track-rtc"></a>

You can track replication time for objects that did not replicate within 15 minutes by monitoring specific event notifications that S3 Replication Time Control \(S3 RTC\) publishes\. These events are published when an object that was eligible for replication using S3 RTC didn't replicate within 15 minutes, and when that object replicates to the destination Region\. 

Replication events are available within 15 minutes of enabling S3 RTC\. Amazon S3 events are available through Amazon SQS, Amazon SNS, or AWS Lambda\. For more information, see [Event Notification Types and Destinations](NotificationHowTo.md#notification-how-to-event-types-and-destinations)\.