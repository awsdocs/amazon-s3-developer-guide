# Assessing your storage activity and usage with Amazon S3 Storage Lens<a name="storage_lens"></a>

Amazon S3 Storage Lens aggregates your usage and activity metrics and displays the information in an interactive dashboard on the Amazon S3 console or through a metrics data export that can be downloaded in CSV or Parquet format\. You can use the dashboard to visualize insights and trends, flag outliers, and provides recommendations for optimizing storage costs and applying data protection best practices\. You can use S3 Storage Lens through the AWS Management Console, AWS CLI, AWS SDKs, or REST API\.

Amazon S3 Storage Lens provides a single view of usage and activity across your Amazon S3 storage\. With drill\-down options to generate insights at the organization, account, bucket, object, or even prefix level\. S3 Storage Lens analyzes storage metrics to deliver contextual recommendations to help optimize storage costs and apply best practices on data protection\. 

On the [S3 console](https://console.aws.amazon.com/s3), S3 Storage Lens provides an interactive *default dashboard* that is updated daily\. Other dashboards can be scoped by account \(for AWS Organizations users\), AWS Regions, and S3 buckets to provide [usage metrics](https://docs.aws.amazon.com/AmazonS3/latest/dev/storage_lens_basics_metrics_recommendations.html#storage_lens_basics_metrics_types) for free\. For an additional charge, you can upgrade to receive *advanced metrics and recommendations*\. These include usage metrics with prefix\-level aggregation, activity metrics aggregated by bucket, and contextual recommendations \(available only in the dashboard\)\. S3 Storage Lens can be used to get a summary of storage insights, detect outliers, enhance data protection, and optimize storage costs\. For more information, see [Using S3 Storage Lens in the console](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/storage_lens_console.html)\. For more information about S3 Storage Lens pricing, see [Amazon S3 pricing](http://aws.amazon.com/s3/pricing)\.

In addition to the dashboard in the S3 console, you can export metrics in CSV or Apache Parquet format to an S3 bucket of your choice for further analysis\. For information, see [Viewing Amazon S3 Storage Lens metrics using a data export](storage_lens_view_metrics_export.md)\.

Use S3 Storage Lens to generate summary insights, such as finding out how much storage you have across your entire organization, or what are the fastest growing buckets and prefixes\. Identify outliers in your storage metrics, and then drill down to further investigate the source of the spike in usage or activity\. 

You can assess your storage based on data protection best practices in Amazon S3, such as analyzing the percentage of your buckets that have encryption or object lock enabled\. And you can identify potential cost savings opportunities, such as by analyzing your request activity per bucket to find buckets where objects could be transitioned to a lower\-cost storage class\. 

**Topics**
+ [Understanding Amazon S3 Storage Lens](storage_lens_basics_metrics_recommendations.md)
+ [Using Amazon S3 Storage Lens with AWS Organizations](storage_lens_with_organizations.md)
+ [Setting permissions to use Amazon S3 Storage Lens](storage_lens_iam_permissions.md)
+ [Viewing storage usage and activity metrics with Amazon S3 Storage Lens](storage_lens_view_metrics.md)
+ [Amazon S3 Storage Lens metrics glossary](storage_lens_metrics_glossary.md)
+ [Amazon S3 Storage Lens examples and console walk\-through](S3LensExamples.md)