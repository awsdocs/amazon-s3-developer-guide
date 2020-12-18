# Understanding the Amazon S3 Storage Lens export schema<a name="storage_lens_understanding_metrics_export_schema"></a>

The following table contains the schema of your S3 Storage Lens metrics export\.


| Attribute Name  | Data Type | Column Name | Description | 
| --- | --- | --- | --- | 
| VersionNumber | String | version\_number | The version of the S3 Storage Lens metrics being used\. | 
| ConfigurationId | String | configuration\_id | The name of the configuration\_id of your S3 Storage Lens configuration\. | 
| ReportDate  | String  | report\_date  | The date the metrics were tracked\.  | 
|  AwsAccountNumber  |  String  |  aws\_account\_number  |  Your AWS account number\.  | 
|  AwsRegion  |  String  |  aws\_region  |  The AWS Region for which the metrics are being tracked\.  | 
|  StorageClass  |  String  |  storage\_class  |  The storage class of the bucket in question\.  | 
|  RecordType  |  ENUM  |  record\_type  |  The type of artifact that is being reported \(ACCOUNT, BUCKET, or PREFIX\)\.  | 
|  RecordValue  |  String  |  record\_value  |  The record value\. This field is populated when the record\_type is PREFIX\. The record value is only URL\-encoded in the CSV format  | 
|  BucketName  |  String  |  bucket\_name  |  The name of the bucket that is being reported\.  | 
|  MetricName  |  String  |  metric\_name  |  The name of the metric that is being reported\.  | 
|  MetricValue  |  Long  |  metric\_value  |  The value of the metric that is being reported\.  | 

## Example of an S3 Storage Lens metrics export<a name="storage_lens_sample_metrics_export"></a>

The following is an example of an S3 Storage Lens metrics export based on this schema\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/sample_storage_lens_export.png)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)