# Amazon S3 Analytics – Storage Class Analysis<a name="analytics-storage-class"></a>

By using Amazon S3 analytics *storage class analysis* you can analyze storage access patterns to help you decide when to transition the right data to the right storage class\. This new Amazon S3 analytics feature observes data access patterns to help you determine when to transition less frequently accessed STANDARD storage to the STANDARD\_IA \(IA, for infrequent access\) storage class\. For more information about storage classes, see [Storage Classes](storage-class-intro.md)\. 

After storage class analysis observes the infrequent access patterns of a filtered set of data over a period of time, you can use the analysis results to help you improve your lifecycle policies\. You can configure storage class analysis to analyze all the objects in a bucket\. Or, you can configure filters to group objects together for analysis by common prefix \(that is, objects that have names that begin with a common string\), by object tags, or by both prefix and tags\. You'll most likely find that filtering by object groups is the best way to benefit from storage class analysis\. 

**Important**  
Storage class analysis does not give recommendations for transitions to the ONEZONE\_IA or GLACIER storage classes\.

You can have multiple storage class analysis filters per bucket, up to 1,000, and will receive a separate analysis for each filter\. Multiple filter configurations allow you analyze specific groups of objects to improve your lifecycle policies that transition objects to STANDARD\_IA\. 

Storage class analysis shows storage usage visualizations in the Amazon S3 console that are updated daily\. The storage usage data can also be exported daily to a file in an S3 bucket\. You can open the exported usage report file in a spreadsheet application or use it with the business intelligence tools of your choice such as Amazon QuickSight\.

**Topics**
+ [How Do I Set Up Storage Class Analysis?](#analytics-storage-class-how-to-set-up)
+ [How Do I Use Storage Class Analysis?](#analytics-storage-class-contents)
+ [How Can I Export Storage Class Analysis Data?](#analytics-storage-class-export-to-file)
+ [Amazon S3 Analytics REST APIs](#analytics-storage-class-related-resources)

## How Do I Set Up Storage Class Analysis?<a name="analytics-storage-class-how-to-set-up"></a>

You set up storage class analysis by configuring what object data you want to analyze\. You can configure storage class analysis to do the following:
+ **Analyze the entire contents of a bucket\.**

  You'll receive an analysis for all the objects in the bucket\.
+ **Analyze objects grouped together by prefix and tags\.**

  You can configure filters that group objects together for analysis by prefix, or by object tags, or by a combination of prefix and tags\. You receive a separate analysis for each filter you configure\. You can have multiple filter configurations per bucket, up to 1,000\. 
+ **Export analysis data\.** 

  When you configure storage class analysis for a bucket or filter, you can choose to have the analysis data exported to a file each day\. The analysis for the day is added to the file to form a historic analysis log for the configured filter\. The file is updated daily at the destination of your choice\. When selecting data to export, you specify a destination bucket and optional destination prefix where the file is written\.

You can use the Amazon S3 console, the REST API, or the AWS CLI or AWS SDKs to configure storage class analysis\.
+ For information about how to configure storage class analysis in the Amazon S3 console, see [ How Do I Configure Storage Class Analysis?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/configure-analytics-storage-class.html)\.
+ To use the Amazon S3 API, use the [PutBucketAnalyticsConfiguration](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTAnalyticsConfig.html) REST API, or the equivalent, from the AWS CLI or AWS SDKs\. 

## How Do I Use Storage Class Analysis?<a name="analytics-storage-class-contents"></a>

You use storage class analysis to observe your data access patterns over time to gather information to help you improve the lifecycle management of your STANDARD\_IA storage\. After you configure a filter, you'll start seeing data analysis based on the filter in the Amazon S3 console in 24 to 48 hours\. However, storage class analysis observes the access patterns of a filtered data set for 30 days or longer to gather information for analysis before giving a result\. The analysis continues to run after the initial result and updates the result as the access patterns change

When you first configure a filter the Amazon S3 console shows a message similar to the following\.

![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/storage-class-analysis-observe-bar-start-observe.png)![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)

Storage class analysis observes the access patterns of a filtered object data set for 30 days or longer to gather enough information for the analysis\. After storage class analysis has gathered sufficient information, you'll see a message in the Amazon S3 console similar to the following\.

![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/storage-class-analysis-observe-bar.png)![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)

When performing the analysis for infrequently accessed objects storage class analysis looks at the filtered set of objects grouped together based on age since they were uploaded to Amazon S3\. Storage class analysis determines if the age group is infrequently accessed by looking at the following factors for the filtered data set:
+ Objects in the STANDARD storage class that are larger than 128K\.
+ How much average total storage you have per age group\.
+ Average number of bytes transferred out \(not frequency\) per age group\.
+ Analytics export data only includes requests with data relevant to storage class analysis\. This might cause differences in the number of requests, and the total upload and request bytes compared to what are shown in storage metrics or tracked by your own internal systems\.
+ Failed GET and PUT requests are not counted for the analysis\. However, you will see failed requests in storage metrics\. 

**How Much of My Storage did I Retrieve?**

The Amazon S3 console graphs how much of the storage in the filtered data set has been retrieved for the observation period as shown in the following example\.

![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/storage-class-analysis-how-much-retrieved.png)![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)

**What Percentage of My Storage did I Retrieve?**

The Amazon S3 console also graphs what percentage of the storage in the filtered data set has been retrieved for the observation period as shown in the following example\.

![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/storage-class-analysis-percentage-retrieved.png)![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)

As stated earlier in this topic, when you are performing the analysis for infrequently accessed objects, storage class analysis looks at the filtered set of objects grouped together based on the age since they were uploaded to Amazon S3\. The storage class analysis uses the following predefined object age groups: 
+ Amazon S3 Objects less than 15 days old
+ Amazon S3 Objects 15\-29 days old
+ Amazon S3 Objects 30\-44 days old
+ Amazon S3 Objects 45\-59 days old
+ Amazon S3 Objects 60\-74 days old
+ Amazon S3 Objects 75\-89 days old
+ Amazon S3 Objects 90\-119 days old
+ Amazon S3 Objects 120\-149 days old
+ Amazon S3 Objects 150\-179 days old
+ Amazon S3 Objects 180\-364 days old
+ Amazon S3 Objects 365\-729 days old
+ Amazon S3 Objects 730 days and older

Usually it takes about 30 days of observing access patterns to gather enough information for an analysis result\. It might take longer than 30 days, depending on the unique access pattern of your data\. However, after you configure a filter you'll start seeing data analysis based on the filter in the Amazon S3 console in 24 to 48 hours\. You can see analysis on a daily basis of object access broken down by object age group in the Amazon S3 console\. 

**How Much of My Storage is Infrequently Accessed?**

The Amazon S3 console shows the access patterns grouped by the predefined object age groups as shown in the following example\.

![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/storage-class-analysis-infrequently-accesses.png)![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)

The **Frequently accessed** or **Infrequently accessed** text shown at the bottom of each age group is based on the same logic as the lifecycle policy recommendation being prepared\. After a recommended age for a lifecycle policy is ready \(RecommendedObjectAge\), all of the age tiers younger than that recommended age are marked as infrequently accessed, regardless of the current cumulative access ratio\. This text is meant as a visual aid to help you in the lifecycle creation process\. 

## How Can I Export Storage Class Analysis Data?<a name="analytics-storage-class-export-to-file"></a>

You can choose to have storage class analysis export analysis reports to a comma\-separated values \(CSV\) flat file\. Reports are updated daily and are based on the object age group filters you configure\. When using the Amazon S3 console you can choose the export report option when you create a filter\. When selecting data export you specify a destination bucket and optional destination prefix where the file is written\. You can export the data to a destination bucket in a different account\. The destination bucket must be in the same region as the bucket that you configure to be analyzed\.

You must create a bucket policy on the destination bucket to grant permissions to Amazon S3 to verify what AWS account owns the bucket and to write objects to the bucket in the defined location\. For an example policy, see [Granting Permissions for Amazon S3 Inventory and Amazon S3 Analytics](example-bucket-policies.md#example-bucket-policies-use-case-9)\.

After you configure storage class analysis reports, you start getting the exported report daily after 24 hours\. After that, Amazon S3 continues monitoring and providing daily exports\. 

You can open the CSV file in a spreadsheet application or import the file into other applications like [Amazon QuickSight](http://docs.aws.amazon.com/quicksight/latest/user/welcome.html)\. For information on using Amazon S3 files with Amazon QuickSight, see [ Create a Data Set Using Amazon S3 Files](http://docs.aws.amazon.com/quicksight/latest/user/create-a-data-set-s3.html) in the *Amazon QuickSight User Guide*\. 

Data in the exported file is sorted by date within object age group as shown in following examples\. If the storage class is STANDARD the row also contains data for the columns `ObjectAgeForSIATransition` and `RecommendedObjectAgeForSIATransition`\.

![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/storage-class-analysis-export-file1.png)![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)

![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/storage-class-analysis-export-file2.png)![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)

At the end of the report the object age group is ALL\. The ALL rows contain cumulative totals for all the age groups for that day as shown in the following example\. 

![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/storage-class-analysis-export-file3.png)![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)![\[Screen shot.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/)

The next section describes the columns used in the report\.

### Exported File Layout<a name="analytics-storage-class-export-file-layout"></a>

The following table describe the layout of the exported file\.

If you see an expand arrow \(**↗**\) in the upper\-right corner of the table, you can open the table in a new window\. To close the window, choose the close button \(**X**\) in the lower\-right corner\.


**Amazon S3 Storage Class Analysis Export File Layout**  

| Column Name | Dimension/Metric | DataType | Description | 
| --- | --- | --- | --- | 
| Date  | Dimension | String  | Date when the record was processed\. Format is MM\-DD\-YYYY\. | 
| ConfigId  | Dimension | String  | Value entered as the filter name when adding the filter configuration\.  | 
| Filter | Dimension | String  | Full filter values as configured when adding the filter configuration\. | 
| StorageClass | Dimension | String  | Storage class of the data\. | 
| ObjectAge | Dimension | String  | Age group for the objects in the filter\. In addition to the 12 different age groups \(0\-14 days, 15\-29 days, 30\-44 days, 45\-59 days, 60\-74 days, 75\-89 days, 90\-119 days, 120\-149 days, 150\-179 days, 180\-364 days, 365\-729 days, 730 days\+\) for 128KB\+ objects, there is one extra value='ALL', which represents all age groups\. | 
| ObjectCount  | Metric  |  Integer  | Total number of objects counted per storage class for the day in the age group\. For the `AgeGroup='ALL'`, the value is the total object count for all the age groups for the day\. | 
| DataUploaded\_MB  | Metric | Number | Total data in MB uploaded per storage class for the day in the age group\. For the `AgeGroup='ALL'`, the value is the total upload count in MB for all the age groups for the day\. \(Note that you will not see multipart object upload activity in your export data because multipart upload requests do not currently have storage class information\.\) | 
| Storage\_MB  | Metric | Number  | Total storage in MB per storage class for the day in the age group\. For the `AgeGroup='ALL'`, the value is the overall storage count in MB for all the age groups for the day\. | 
| DataRetrieved\_MB | Metric | Number | Data transferred out in MBs per storage class with GET requests for the day in the age group\. For `AgeGroup='ALL'`, the value is the overall data transferred out in MB with GET requests for all the age groups for the day\. | 
| GetRequestCount | Metric | Integer | Number of GET requests made per storage class for the day in the age group\. For AgeGroup='ALL', the value represents the overall GET request count for all the age groups for the day\.  | 
| CumulativeAccessRatio | Metric | Number | Cumulative access ratio\. This ratio is used to represent the usage/byte heat on any given age group to help determine if an age group is eligible for transition to STANDARD\_IA\.  | 
| ObjectAgeForSIATransition | Metric | Integer In Days  | This value exists only where the `AgeGroup=’ALL’` and storage class = STANDARD\. It represents the observed age for transition to STANDARD\_IA\. | 
| RecommendedObjectAgeForSIATransition  | Metric | Integer In Days  | This value exists only where the `AgeGroup=’ALL’` and storage class = STANDARD\. It represents the object age in days to consider for transition to STANDARD\_IA after the `ObjectAgeForSIATransition` stabilizes\. | 

## Amazon S3 Analytics REST APIs<a name="analytics-storage-class-related-resources"></a>

The following are the REST operations used for storage inventory\.
+  [ DELETE Bucket analytics configuration](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEAnalyticsConfiguration.html) 
+  [ GET Bucket analytics configuration](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETAnalyticsConfig.html) 
+  [ List Bucket Analytics Configuration](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketListAnalyticsConfigs.html) 
+  [ PUT Bucket analytics configuration](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTAnalyticsConfig.html) 