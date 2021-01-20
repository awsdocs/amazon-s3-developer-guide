# Troubleshooting Amazon S3 by Symptom<a name="troubleshooting-by-symptom"></a>

The following topics list symptoms to help you troubleshoot some of the issues that you might encounter when working with Amazon S3\.

**Topics**
+ [Significant Increases in HTTP 503 Responses to Requests to Buckets with Versioning Enabled](#troubleshooting-by-symptom-increase-503-reponses)
+ [Unexpected Behavior When Accessing Buckets Set with CORS](#troubleshooting-by-symptom-increase)

## Significant Increases in HTTP 503 Responses to Amazon S3 Requests to Buckets with Versioning Enabled<a name="troubleshooting-by-symptom-increase-503-reponses"></a>

If you notice a significant increase in the number of HTTP 503\-slow down responses received for Amazon S3 PUT or DELETE object requests to a bucket that has versioning enabled, you might have one or more objects in the bucket for which there are millions of versions\. When you have objects with millions of versions, Amazon S3 automatically throttles requests to the bucket to protect the customer from an excessive amount of request traffic, which could potentially impede other requests made to the same bucket\. 

To determine which S3 objects have millions of versions, use the Amazon S3 inventory tool\. The inventory tool generates a report that provides a flat file list of the objects in a bucket\. For more information, see [ Amazon S3 inventory](storage-inventory.md)\.

The Amazon S3 team encourages customers to investigate applications that repeatedly overwrite the same S3 object, potentially creating millions of versions for that object, to determine whether the application is working as intended\. If you have a use case that requires millions of versions for one or more S3 objects, contact the AWS Support team at [AWS Support](https://console.aws.amazon.com/support/home) to discuss your use case and to help us assist you in determining the optimal solution for your use case scenario\.

To help prevent this issue, consider the following best practices:
+ Enable a lifecycle management "NonCurrentVersion" expiration policy and an "ExpiredObjectDeleteMarker" policy to expire the previous versions of objects and delete markers without associated data objects in the bucket\. 
+ Keep your directory structure as flat as possible and make each directory name unique\.

## Unexpected Behavior When Accessing Buckets Set with CORS<a name="troubleshooting-by-symptom-increase"></a>

 If you encounter unexpected behavior when accessing buckets set with the cross\-origin resource sharing \(CORS\) configuration, see [Troubleshooting CORS issues](cors-troubleshooting.md)\.