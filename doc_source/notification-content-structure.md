# Event Message Structure<a name="notification-content-structure"></a>

The notification message Amazon S3 sends to publish an event is a JSON message with the following structure\. Note the following:
+ The `responseElements` key value is useful if you want to trace the request by following up with Amazon S3 support\. Both `x-amz-request-id` and `x-amz-id-2` help Amazon S3 to trace the individual request\. These values are the same as those that Amazon S3 returned in the response to your original PUT request, which initiated the event\.
+ The `s3` key provides information about the bucket and object involved in the event\. The object keyname value is URL encoded\. For example "red flower\.jpg" becomes "red\+flower\.jpg" \(S3 returns the "application/x\-www\-form\-urlencoded" as the content type in the response\)\.
+ The `sequencer` key provides a way to determine the sequence of events\. Event notifications are not guaranteed to arrive in the order that the events occurred\. However, notifications from events that create objects \(`PUT`s\) and delete objects contain a `sequencer`, which can be used to determine the order of events for a given object key\. 

  If you compare the `sequencer` strings from two event notifications on the same object key, the event notification with the greater `sequencer` hexadecimal value is the event that occurred later\. If you are using event notifications to maintain a separate database or index of your Amazon S3 objects, you will probably want to compare and store the `sequencer` values as you process each event notification\. 

  Note that:
  + `sequencer` cannot be used to determine order for events on different object keys\.
  + The sequencers can be of different lengths\. So to compare these values, you first left pad the shorter value with zeros and then do lexicographical comparison\.

```
{  
   "Records":[  
      {  
         "eventVersion":"2.0",
         "eventSource":"aws:s3",
         "awsRegion":"us-east-1",
         "eventTime":The time, in ISO-8601 format, for example, 1970-01-01T00:00:00.000Z, when S3 finished processing the request,
         "eventName":"event-type",
         "userIdentity":{  
            "principalId":"Amazon-customer-ID-of-the-user-who-caused-the-event"
         },
         "requestParameters":{  
            "sourceIPAddress":"ip-address-where-request-came-from"
         },
         "responseElements":{  
            "x-amz-request-id":"Amazon S3 generated request ID",
            "x-amz-id-2":"Amazon S3 host that processed the request"
         },
         "s3":{  
            "s3SchemaVersion":"1.0",
            "configurationId":"ID found in the bucket notification configuration",
            "bucket":{  
               "name":"bucket-name",
               "ownerIdentity":{  
                  "principalId":"Amazon-customer-ID-of-the-bucket-owner"
               },
               "arn":"bucket-ARN"
            },
            "object":{  
               "key":"object-key",
               "size":object-size,
               "eTag":"object eTag",
               "versionId":"object version if bucket is versioning-enabled, otherwise null",
               "sequencer": "a string representation of a hexadecimal value used to determine event sequence, 
                   only used with PUTs and DELETEs"            
            }
         }
      },
      {
          // Additional events
      }
   ]
}
```

The following are example messages:
+ Test message—When you configure an event notification on a bucket, Amazon S3 sends the following test message:

  ```
  1. {  
  2.    "Service":"Amazon S3",
  3.    "Event":"s3:TestEvent",
  4.    "Time":"2014-10-13T15:57:02.089Z",
  5.    "Bucket":"bucketname",
  6.    "RequestId":"5582815E1AEA5ADF",
  7.    "HostId":"8cLeGAmw098X5cv4Zkwcmo8vvZa3eH3eKxsPzbB9wrR+YstdA6Knx4Ip8EXAMPLE"
  8. }
  ```
+ Example message when an object is created using the PUT request—The following message is an example of a message Amazon S3 sends to publish an `s3:ObjectCreated:Put` event:

  ```
   1. {  
   2.    "Records":[  
   3.       {  
   4.          "eventVersion":"2.0",
   5.          "eventSource":"aws:s3",
   6.          "awsRegion":"us-east-1",
   7.          "eventTime":"1970-01-01T00:00:00.000Z",
   8.          "eventName":"ObjectCreated:Put",
   9.          "userIdentity":{  
  10.             "principalId":"AIDAJDPLRKLG7UEXAMPLE"
  11.          },
  12.          "requestParameters":{  
  13.             "sourceIPAddress":"127.0.0.1"
  14.          },
  15.          "responseElements":{  
  16.             "x-amz-request-id":"C3D13FE58DE4C810",
  17.             "x-amz-id-2":"FMyUVURIY8/IgAtTv8xRjskZQpcIZ9KG4V5Wp6S7S/JRWeUWerMUE5JgHvANOjpD"
  18.          },
  19.          "s3":{  
  20.             "s3SchemaVersion":"1.0",
  21.             "configurationId":"testConfigRule",
  22.             "bucket":{  
  23.                "name":"mybucket",
  24.                "ownerIdentity":{  
  25.                   "principalId":"A3NL1KOZZKExample"
  26.                },
  27.                "arn":"arn:aws:s3:::mybucket"
  28.             },
  29.             "object":{  
  30.                "key":"HappyFace.jpg",
  31.                "size":1024,
  32.                "eTag":"d41d8cd98f00b204e9800998ecf8427e",
  33.                "versionId":"096fKKXTRTtl3on89fVO.nfljtsv6qko",
  34.                "sequencer":"0055AED6DCD90281E5"
  35.             }
  36.          }
  37.       }
  38.    ]
  39. }
  ```