# Event message structure<a name="notification-content-structure"></a>

The notification message that Amazon S3 sends to publish an event is in the JSON format\. The following example shows the structure of the JSON message\. 

Note the following about the example:
+ The `eventVersion` key value contains a major and minor version in the form `<major>`\.`<minor>`\.

  The major version is incremented if Amazon S3 makes a change to the event structure that is not backward compatible\. This includes removing a JSON field that is already present or changing how the contents of a field are represented \(for example, a date format\)\.

  The minor version is incremented if Amazon S3 adds new fields to the event structure\. This might occur if new information is provided for some or all existing events, or if new information is provided on only newly introduced event types\. Applications should ignore new fields to stay forward compatible with new minor versions of the event structure\.

  If new event types are introduced but the structure of the event is otherwise unmodified, the event version does not change\.

  To ensure that your applications can parse the event structure correctly, we recommend that you do an equal\-to comparison on the major version number\. To ensure that the fields expected by your application are present, we also recommend doing a greater\-than\-or\-equal\-to comparison on the minor version\.
+ The `responseElements` key value is useful if you want to trace a request by following up with AWS Support\. Both `x-amz-request-id` and `x-amz-id-2` help Amazon S3 trace an individual request\. These values are the same as those that Amazon S3 returns in the response to the request that initiates the events, so they can be used to match the event to the request\.
+ The `s3` key provides information about the bucket and object involved in the event\. The object key name value is URL encoded\. For example, "red flower\.jpg" becomes "red\+flower\.jpg" \(Amazon S3 returns "`application/x-www-form-urlencoded`" as the content type in the response\)\.
+ The `sequencer` key provides a way to determine the sequence of events\. Event notifications are not guaranteed to arrive in the order that the events occurred\. However, notifications from events that create objects \(`PUT`s\) and delete objects contain a `sequencer`, which can be used to determine the order of events for a given object key\. 

  If you compare the `sequencer` strings from two event notifications on the same object key, the event notification with the greater `sequencer` hexadecimal value is the event that occurred later\. If you are using event notifications to maintain a separate database or index of your Amazon S3 objects, you will probably want to compare and store the `sequencer` values as you process each event notification\. 

  Note the following:
  + You cannot use `sequencer` to determine order for events on different object keys\.
  + The sequencers can be of different lengths\. So to compare these values, you first left pad the shorter value with zeros, and then do a lexicographical comparison\.
+ The `glacierEventData` key is only visible for `s3:ObjectRestore:Completed` events\. 
+ The `restoreEventData` key contains attributes related to your restore request\.
+ The `replicationEventData` key is only visible for replication events\.

The following example shows version 2\.2 of the event message JSON structure, which is the version currently being used by Amazon S3\.

```
{  
   "Records":[  
      {  
         "eventVersion":"2.2",
         "eventSource":"aws:s3",
         "awsRegion":"us-west-2",
         "eventTime":The time, in ISO-8601 format, for example, 1970-01-01T00:00:00.000Z, when Amazon S3 finished processing the request,
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
         },
         "glacierEventData": {
            "restoreEventData": {
               "lifecycleRestorationExpiryTime": "The time, in ISO-8601 format, for example, 1970-01-01T00:00:00.000Z, of Restore Expiry",
               "lifecycleRestoreStorageClass": "Source storage class for restore"
            }
         }
      }
   ]
}
```

The following example shows version 2\.0 of the event message structure, which is no longer used by Amazon S3\.

```
{  
   "Records":[  
      {  
         "eventVersion":"2.0",
         "eventSource":"aws:s3",
         "awsRegion":"us-west-2",
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
      }
   ]
}
```

The following are example messages:
+ Test message—When you configure an event notification on a bucket, Amazon S3 sends the following test message\.

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
+ Example message when an object is created using the PUT request—The following message is an example of a message Amazon S3 sends to publish an `s3:ObjectCreated:Put` event\.

  ```
   1. {  
   2.    "Records":[  
   3.       {  
   4.          "eventVersion":"2.1",
   5.          "eventSource":"aws:s3",
   6.          "awsRegion":"us-west-2",
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

For a definition of each IAM identification prefix \(AIDA, AROA, AGPA, etc\.\), see [Understanding Unique ID Prefixes](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_identifiers.html#identifiers-prefixesl)\.