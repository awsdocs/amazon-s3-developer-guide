# Understanding Amazon S3 Storage Lens<a name="storage_lens_basics_metrics_recommendations"></a>

Amazon S3 Storage Lens provides a single view of object storage usage and activity across your entire Amazon S3 storage\. It includes drill\-down options to generate insights at the organization, account, Region, bucket, or even prefix level\. 

Amazon S3 Storage Lens aggregates your usage and activity metrics and displays the information in an interactive dashboard on the Amazon S3 console or through a metrics data export that can be downloaded in CSV or Parquet format\. You can use the dashboard to visualize insights and trends, flag outliers, and provides recommendations for optimizing storage costs and applying data protection best practices\. You can use S3 Storage Lens through the AWS Management Console, AWS CLI, AWS SDKs, or REST API\.

## Amazon S3 Storage Lens concepts and terminology<a name="storage_lens_basics"></a>

This section contains the terminology and concepts that are essential for understanding and using Amazon S3 Storage Lens successfully\.

**Topics**
+ [Configuration](#storage_lens_basics_configuration)
+ [Default dashboard](#storage_lens_basics_default_dashboard)
+ [Dashboards](#storage_lens_basics_dashboards)
+ [Metrics export](#storage_lens_basics_metrics_export)
+ [Home Region](#storage_lens_basics_home_region)
+ [Retention period](#storage_lens_basics_retention_period)
+ [Metrics types](#storage_lens_basics_metrics_types)
+ [Recommendations](#storage_lens_basics_recommendations)
+ [Metrics selection](#storage_lens_basics_metrics_selection)
+ [S3 Storage Lens and AWS Organizations](#storage_lens_basics_organizations)

### Configuration<a name="storage_lens_basics_configuration"></a>

Amazon S3 Storage Lens requires a *configuration* that contains the properties that are used to aggregate metrics on your behalf for a single dashboard or export\. This includes all or partial sections of your organization account’s storage, including filtering by Region, bucket, and prefix\-level \(available only with advanced metrics\) scope\. It includes information about whether you chose *free metrics* or *advanced metrics and recommendations*\. It also includes whether a metrics export is required, and information about where to place the metrics export if applicable\.

### Default dashboard<a name="storage_lens_basics_default_dashboard"></a>

The S3 Storage Lens default dashboard on the console is named **default\-account\-dashboard**\. S3 preconfigures the dashboard to visualize the summarized insights and trends of your entire account’s aggregated storage usage and activity metrics, and updates them daily in the Amazon S3 console\. You can't modify the configuration scope of the default dashboard, but you can upgrade the metrics selection from **free metrics** to the paid **advanced metrics and recommendations**\. You can also configure the optional metrics export, or even disable the dashboard\. However, you can't delete the default dashboard\.

**Note**  
If you disable your default dashboard, it is no longer updated, and you will no longer receive any new daily metrics\. You can still see historic data until the 14\-day expiration period, or 15 months if you are subscribed to *advanced metrics and recommendations* for that dashboard\. You can re\-enable the dashboard within the expiration period to access this data\.

### Dashboards<a name="storage_lens_basics_dashboards"></a>

You can also use Amazon S3 Storage Lens to configure a dashboard that visualizes summarized insights and trends of aggregated storage usage and activity metrics, updated daily on the Amazon S3 console\. You can create and modify S3 Storage Lens dashboards to express all or partial sections of your organization account’s storage\. You can filter by AWS Region, bucket, and prefix \(available only with advanced metrics and recommendations\)\. You can also disable or delete the dashboard\.

**Note**  
You can use S3 Storage Lens to create up to 50 dashboards per home Region\.
If you disable a dashboard, it is no longer updated, and you will no longer receive any new daily metrics\. You can still see historic data until the 14\-day expiration period \(or 15 months, if you subscribed to advanced metrics and recommendations for that dashboard\)\. You can re\-enable the dashboard within the expiration period to access this data\.
If you delete your dashboard, you lose all your dashboard configuration settings\. You will no longer receive any new daily metrics, and you also lose access to the historical data associated with that dashboard\. If you want to access the historic data for a deleted dashboard, you must create another dashboard with the same name in the same home Region\.
Organization\-level dashboards can only be limited to a regional scope\.

### Metrics export<a name="storage_lens_basics_metrics_export"></a>

An S3 Storage Lens *metrics export* is a file that contains all the metrics identified in your S3 Storage Lens configuration\. This information is generated daily in CSV or Parquet format in an S3 bucket of your choice for further analysis\. You can generate an S3 Storage Lens metrics export from the S3 console by editing your dashboard configuration, or by using the AWS CLI and SDKs\.

### Home Region<a name="storage_lens_basics_home_region"></a>

The home Region is the Region where all Amazon S3 Storage Lens metrics for a given dashboard or configuration's are stored\. You must choose a home Region when you create your S3 Storage Lens dashboard or configuration\. After a home Region is assigned, it can't be changed\.

**Note**  
Creating a home Region is not supported the following Regions:  
Africa \(Cape Town\) \(af\-south\-1\)
Asia Pacific \(Hong Kong\) \(ap\-east\-1\)
Europe \(Milan\) \(eu\-south\-1\)
Middle East \(Bahrain\) \(me\-south\-1\)

### Retention period<a name="storage_lens_basics_retention_period"></a>

Amazon S3 Storage Lens metrics are retained so you can see historical trends and compare differences in your storage usage and activity over time\. The retention periods depend on your [metrics selection](https://docs.aws.amazon.com/AmazonS3/latest/dev/storage_lens_basics_metrics_selection.html) and cannot be modified\. Free metrics are retained for a 14\-day period, and advanced metrics are retained for a 15\-month period\.

### Metrics types<a name="storage_lens_basics_metrics_types"></a>

S3 Storage Lens offers two types of storage metrics: *usage* and *activity*\.
+ **Usage metrics**

  S3 Storage Lens collects *usage metrics* for all dashboards and configurations\. Usage metrics describe the size, quantity, and characteristics of your storage\. This includes the total bytes stored, object count, and average object size in addition to metrics that describe feature utilization such as encrypted bytes, or delete market object counts\. For more information about the usage metrics aggregated by S3 Storage Lens, see [Metrics glossary](https://docs.aws.amazon.com/AmazonS3/latest/dev/storage_lens_metrics_glossary.html)\.
+ **Activity metrics**

  S3 Storage Lens aggregates *activity metrics* for all dashboards and configurations that have the *advanced metrics and recommendations metrics* type enabled\. Activity metrics describe the details of how often your storage is requested\. This includes the number of requests by type, upload and download bytes, and errors\. For more information about the activity metrics that are aggregated by S3 Storage Lens, see [Metrics glossary](https://docs.aws.amazon.com/AmazonS3/latest/dev/storage_lens_metrics_glossary.html)\.

### Recommendations<a name="storage_lens_basics_recommendations"></a>

S3 Storage Lens provides automated *recommendations* to help you optimize your storage\. Recommendations are placed contextually alongside relevant metrics in the S3 Storage Lens dashboard\. Historical data is not eligible for recommendations because recommendations are relevant to what is happening in the most recent period\. Recommendations only appear when they are relevant\.

S3 Storage Lens recommendations come in the following forms:
+ **Suggestions**

  *Suggestions* alert you to trends within your storage usage and activity that might indicate a storage cost optimization opportunity or data protection best practice\. You can use the suggested topics in the *Amazon S3 Developer Guide* and the S3 Storage Lens dashboard to drill down for more details about the specific Regions, buckets, or prefixes to further assist you\.
+ **Call\-outs**

  Call\-outs are recommendations that alert you to interesting anomalies within your storage usage and activity over a period that might need further attention or monitoring\.
  + **Outlier call\-outs**

    S3 Storage Lens provides call\-outs for metrics that are *outliers*, based on your recent 30\-day trend\. The outlier is calculated using a standard score, also known as a *z\-score*\. In this score, the current day’s metric is subtracted from the average of the last 30 days for that metric, and then divided by the standard deviation for that metric over the last 30 days\. The resulting score is usually between \-3 and \+3\. This number represents the number of standard deviations that the current day’s metric is from the mean\. 

    S3 Storage Lens considers metrics with a score >2 or <\-2 to be outliers because they are higher or lower than 95 percent of normally distributed data\. 
  + **Significant change call\-outs**

    The *significant change call\-out* applies to metrics that are expected to change less frequently\. Therefore it is set to a higher sensitivity than the outlier calculation, which is typically in the range of \+/\- 20 percent versus the prior day, week, or month\.

    **Addressing call\-outs in your storage usage and activity** – If you receive a significant change call\-out, it’s not necessarily a problem, and could be the result of an anticipated change in your storage\. For example, you might have recently added a large number of new objects, deleted a large number of objects, or made similar planned changes\. 

    If you see a significant change call\-out on your dashboard, take note of it and determine whether it can be explained by recent circumstances\. If not, use the S3 Storage Lens dashboard to drill down for more details to understand the specific Regions, buckets, or prefixes that are driving the fluctuation\.
+ **Reminders**

  *Reminders* provide insights into how Amazon S3 works\. They can help you learn more about ways to use S3 features to reduce storage costs or apply data protection best practices\.

### Metrics selection<a name="storage_lens_basics_metrics_selection"></a>

S3 Storage Lens offers two metrics selections that you can choose for your dashboard and export: *free metrics* and *advanced metrics and recommendations*\.
+ **Free metrics**

  S3 Storage Lens offers free metrics for all dashboards and configurations\. Free metrics contain metrics that are relevant to your storage usage\. This includes the number of buckets, the objects in your account, and what state they are in\. All free metrics are collected daily and retained for a 14\-day retention period\. For more information about what usage metrics are aggregated by S3 Storage Lens, see [Metrics glossary](https://docs.aws.amazon.com/AmazonS3/latest/dev/storage_lens_metrics_glossary.html)\.
+ **Advanced metrics and recommendations**

  S3 Storage Lens offers free metrics for all dashboards and configurations and the option to upgrade to the* advanced metrics and recommendations* option\. Advanced metrics contain all the usage metrics that are included in free metrics\. This includes the number of buckets, the objects in your account, and what state they are in\. 

  With advanced metrics, you can also collect usage metrics at the prefix level\. In addition, advanced metrics include activity metrics\. Activity metrics data is relevant to your storage activity\. This includes the number of requests, scans, and errors with respect to the configuration scope and what state they are in\. All advanced metrics are collected daily and retained for a 15\-month retention period\. For more information about the storage metrics aggregated by S3 Storage Lens, see [Metrics glossary](https://docs.aws.amazon.com/AmazonS3/latest/dev/storage_lens_metrics_glossary.html)\.

  This metrics selection also provides recommendations to help you optimize your storage\. Recommendations are placed contextually alongside relevant metrics in the dashboard\. Additional charges apply\. For more information, see [Amazon S3 pricing](http://aws.amazon.com/s3/pricing/)\.
**Note**  
Recommendations are available only when you use the S3 Storage Lens dashboard on the Amazon S3 console, and not via the AWS CLI and SDKs\.

### S3 Storage Lens and AWS Organizations<a name="storage_lens_basics_organizations"></a>

AWS Organizations is an AWS service that helps you aggregate all your AWS accounts under one organization hierarchy\. Amazon S3 Storage Lens works with AWS Organizations to provide a single view of object storage usage and activity across your Amazon S3 storage\.

For more information, see [Using Amazon S3 Storage Lens with AWS OrganizationsEnabling trusted access for S3 Storage Lens](storage_lens_with_organizations.md)\.
+ **Trusted access**

  Using your organization’s management account, you must enable *trusted access* for S3 Storage Lens to aggregate storage metrics and usage data for all member accounts in your organization\. You can then create dashboards or exports for your organization using your management account or by giving delegated administrator access to other accounts in your organization\. 

  You can disable trusted access for S3 Storage Lens at any time, which stops S3 Storage Lens from aggregating metrics for your organization\.
+ **Delegated administrator**

  You can create dashboards and metrics for S3 Storage Lens for your organization using your AWS Organizations management account, or by giving *delegated administrator* access to other accounts in your organization\. You can deregister delegated administrators at any time, which prevents S3 Storage Lens from collecting data on an organization level\.

For more information, see [Amazon S3 Storage Lens and AWS Organizations](https://docs.aws.amazon.com/organizations/latest/userguide/services-that-can-integrate-s3lens.html) in the *AWS Organizations User Guide*\.

#### Amazon S3 Storage Lens service\-linked roles<a name="storage_lens_basics_service_linked_role"></a>

Along with AWS Organizations trusted access, Amazon S3 Storage Lens uses AWS Identity and Access Management \(IAM\) service\-linked roles\. A service\-linked role is a unique type of IAM role that is linked directly to S3 Storage Lens\. Service\-linked roles are predefined by S3 Storage Lens and include all the permissions that it requires to collect daily storage usage and activity metrics from member accounts in your organization\. 

For more information, see [Using service\-linked roles for Amazon S3 Storage Lens](https://docs.aws.amazon.com/AmazonS3/latest/dev/using-service-linked-roles.html)\.