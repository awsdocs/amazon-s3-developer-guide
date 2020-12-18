# What is an S3 Storage Lens export manifest?<a name="storage_lens_whatis_metrics_export_manifest"></a>

Given the large amount of data aggregated, an S3 Storage Lens daily metrics export can be split into multiple files\. The manifest file `manifest.json` describes where the metrics export files for that day are located\. Whenever a new export is delivered, it is accompanied by a new manifest\. Each manifest contained in the `manifest.json` file provides metadata and other basic information about the export\. 

The manifest information includes the following properties:
+ `sourceAccountId` – The account ID of the configuration owner\.
+ `configId` – A unique identifier for the dashboard\.
+ `destinationBucket` – The destination bucket Amazon Resource Name \(ARN\) that the metrics export is placed in\.
+ `reportVersion` – The version of the export\.
+ `reportDate` – The date of the report\.
+ `reportFormat` – The format of the report\.
+ `reportSchema` – The schema of the report\.
+ `reportFiles` – The actual list of the export report files that are in the destination bucket\.



The following is an example of a manifest in a `manifest.json` file for a CSV\-formatted export\.

```
{
   "sourceAccountId":"123456789012",
   "configId":"my-dashboard-configuration-id",
   "destinationBucket":"arn:aws:s3:::destination-bucket",
   "reportVersion":"V_1",
   "reportDate":"2020-11-03",
   "reportFormat":"CSV",
   "reportSchema":"version_number,configuration_id,report_date,aws_account_number,aws_region,storage_class,record_type,record_value,bucket_name,metric_name,metric_value",
   "reportFiles":[
      {
         "key":"DestinationPrefix/StorageLens/123456789012/my-dashboard-configuration-id/V_1/reports/dt=2020-11-03/a38f6bc4-2e3d-4355-ac8a-e2fdcf3de158.csv",
         "size":1603959,
         "md5Checksum":"2177e775870def72b8d84febe1ad3574"
      }
}
```



The following is an example of a manifest in a `manifest.json` file for a Parquet\-formatted export\.

```
{
   "sourceAccountId":"123456789012",
   "configId":"my-dashboard-configuration-id",
   "destinationBucket":"arn:aws:s3:::destination-bucket",
   "reportVersion":"V_1",
   "reportDate":"2020-11-03",
   "reportFormat":"Parquet",
   "reportSchema":"message s3.storage.lens { required string version_number; required string configuration_id; required string report_date; required string aws_account_number; required string aws_region; required string storage_class; required string record_type; required string record_value; required string bucket_name; required string metric_name; required long metric_value; }",
   "reportFiles":[
      {
         "key":"DestinationPrefix/StorageLens/123456789012/my-dashboard-configuration-id/V_1/reports/dt=2020-11-03/bd23de7c-b46a-4cf4-bcc5-b21aac5be0f5.par",
         "size":14714,
         "md5Checksum":"b5c741ee0251cd99b90b3e8eff50b944"
      }
}
```

You can configure your metrics export to be generated as part of your dashboard configuration in the Amazon S3 console or by using the Amazon S3 REST API, AWS CLI, and SDKs\.