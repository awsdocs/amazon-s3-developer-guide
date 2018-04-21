# Querying Archived Objects<a name="querying-glacier-archives"></a>

With the select type of [POST Object restore](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOSTrestore.html), you can perform filtering operations using simple Structured Query Language \(SQL\) statements directly on your data that is archived by Amazon S3 to Amazon Glacier\. When you provide an SQL query for an archived object, select runs the query in place and writes the output results to an S3 bucket\. You can run queries and custom analytics on your data that is stored in Amazon Glacier, without having to restore your entire object to Amazon S3\.

When you perform select queries, Amazon Glacier provides three data access tiers—*expedited*, *standard*, and *bulk*\. All of these tiers provide different data access times and costs, and you can choose any one of them depending on how quickly you want your data to be available\. For more information, see [Data Access Tiers](#querying-glacier-archives-access-tiers)\.

You can use the select type of restore with the AWS SDKs, the Amazon Glacier REST API, and the AWS Command Line Interface \(AWS CLI\)\.

**Topics**
+ [Select Requirements and Limits](#glacier-select-requirements-and-limits)
+ [How Do I Query Data Using Select?](#glacier-select-restrictions)
+ [Error Handling](#glacier-select-access-control)
+ [Data Access Tiers](#querying-glacier-archives-access-tiers)
+ [More Info](#querying-glacier-archives-more-info)

## Select Requirements and Limits<a name="glacier-select-requirements-and-limits"></a>

The following are requirements for using select:
+ Archive objects that are queried by select must be formatted as uncompressed comma\-separated values \(CSV\)\. 
+ An S3 bucket for output\. The AWS account that you use to initiate an Amazon Glacier select job must have write permissions for the S3 bucket\. The Amazon S3 bucket must be in the same AWS Region as the bucket that contains the archived object that is being queried\.
+ The requesting AWS account must have permissions to perform the `s3:RestoreObject` and `s3:GetObject` actions\. For more information about these permissions, see [Permissions Related to Bucket Subresource Operations](using-with-s3-actions.md#using-with-s3-actions-related-to-bucket-subresources)\. 
+ The archive must not be encrypted with SSE\-C or client\-side encryption\. 

The following limits apply when using select:
+ There are no limits on the number of records that select can process\. An input or output record must not exceed 1 MB; otherwise, the query fails\. There is a limit of 1,048,576 columns per record\.
+ There is no limit on the size of your final result\. However, your results are broken into multiple parts\. 
+ An SQL expression is limited to 128 KB\.

## How Do I Query Data Using Select?<a name="glacier-select-restrictions"></a>

Using select, you can use SQL commands to query Amazon Glacier archive objects that are in encrypted uncompressed CSV format\. With this restriction, you can perform simple query operations on your text\-based data in Amazon Glacier\. For example, you might look for a specific name or ID among a set of archived text files\. 

To query your Amazon Glacier data, create a select request using the [POST Object restore](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOSTrestore.html) operation\. When performing a select request, you provide the SQL expression, the archive to query, and the location to store the results\. 

The following example expression returns all records from the archived object specified in [POST Object restore](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOSTrestore.html)\.

```
SELECT * FROM object
```

Amazon Glacier Select supports a subset of the ANSI SQL language\. It supports common filtering SQL clauses like `SELECT`, `FROM`, and `WHERE`\. It does not support `SUM`, `COUNT`, `GROUP BY`, `JOINS`, `DISTINCT`, `UNION`, `ORDER BY`, and `LIMIT`\. For more information about support for SQL, see [SQL Reference for Amazon S3 Select and Amazon Glacier Select](http://docs.aws.amazon.com/AmazonS3/latest/dev/s3-glacier-select-sql-reference.html) in the *Amazon Simple Storage Service Developer Guide*\.

### Select Output<a name="glacier-select-output"></a>

When you initiate a select request, you define an output location for the results of your select query\. This location must be an Amazon S3 bucket in the same AWS Region as the bucket that contains the archived object that is being queried\. The AWS account that initiates the job must have permissions to write to the S3 bucket\. 

You can specify the Amazon S3 storage class and encryption for the output objects stored in Amazon S3\. Select supports SSE\-KMS and SSE\-S3 encryption\. Select doesn't support SSE\-C and client\-side encryption\. For more information about Amazon S3 storage classes and encryption, see [Storage Classes](storage-class-intro.md) and [Protecting Data Using Server\-Side Encryption](serv-side-encryption.md)\.

Amazon Glacier Select results are stored in the S3 bucket using the prefix provided in the output location specified in [POST Object restore](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOSTrestore.html)\. From this information, select creates a unique prefix referring to the job ID\. \(Prefixes are used to group Amazon S3 objects together by beginning object names with a common string\.\) Under this unique prefix, there are two new prefixes created, `results` for results and `errors` for logs and errors\. Upon completion of the job, a result manifest is written which contains the location of all results\.

There is also a placeholder file named `job.txt` that is written to the output location\. After it is written it is never updated\. The placeholder file is used for the following:
+ Validation of the write permission and majority of SQL syntax errors synchronously\. 
+ Providing a static output about your select request that you can easily reference whenever you want\. 

For example, suppose that you make a select request with the output location for the results specified as `s3://example-bucket/my-prefix`, and the job response returns the job ID as `examplekne1209ualkdjh812elkassdu9012e`\. After the select job finishes, you can see the following Amazon S3 objects in your bucket:

```
s3://example-bucket/my-prefix/examplekne1209ualkdjh812elkassdu9012e/job.txt
s3://example-bucket/my-prefix/examplekne1209ualkdjh812elkassdu9012e/results/abc
s3://example-bucket/my-prefix/examplekne1209ualkdjh812elkassdu9012e/results/def
s3://example-bucket/my-prefix/examplekne1209ualkdjh812elkassdu9012e/results/ghi
s3://example-bucket/my-prefix/examplekne1209ualkdjh812elkassdu9012e/result_manifest.txt
```

The select query results are broken into multiple parts\. In the example, select uses the prefix that you specified when setting the output location and appends the job ID and the `results` prefix\. It then writes the results in three parts, with the object names ending in `abc`, `def`, and `ghi`\. The result manifest contains all three files to allow programmatic retrieval\. If the job fails with any error, then a file is visible under the error prefix and an `error_manifest.txt` is produced\.

Presence of a `result_manifest.txt` file along with the absence of `error_manifest.txt` guarantees that the job finished successfully\. There is no guarantee provided on how results are ordered\.

**Note**  
The length of an Amazon S3 object name, also referred to as the *key*, can be no more than 1,024 bytes\. Amazon Glacier select reserves 128 bytes for prefixes\. And, the length of your Amazon S3 location path cannot be more than 512 bytes\. A request with a length greater than 512 bytes returns an exception, and the request is not accepted\.

## Error Handling<a name="glacier-select-access-control"></a>

Select notifies you of two kinds of errors\. The first set of errors is sent to you synchronously when you submit the query in [POST Object restore](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOSTrestore.html)\. These errors are sent to you as part of the HTTP response\. Another set of errors can occur after the query has been accepted successfully, but they happen during query execution\. In this case, the errors are written to the specified output location under the `errors` prefix\.

Select stops executing the query after encountering an error\. To execute the query successfully, you must resolve all errors\. You can check the logs to identify which records caused a failure\. 

Because queries run in parallel across multiple compute nodes, the errors that you get are not in sequential order\. For example, if your query fails with an error in row 6,234, it does not mean that all rows before row 6,234 were successfully processed\. The next run of the query might show an error in a different row\. 

## Data Access Tiers<a name="querying-glacier-archives-access-tiers"></a>

You can specify one of the following data access tiers when querying an archived object: 
+ **`Expedited`** – Allows you to quickly access your data when occasional urgent requests for a subset of archives are required\. For all but the largest archived object \(250 MB\+\), data accessed using `Expedited` retrievals are typically made available within 1–5 minutes\. There are two types of `Expedited` data access: On\-Demand and Provisioned\. On\-Demand requests are similar to EC2 On\-Demand instances and are available most of the time\. Provisioned requests are guaranteed to be available when you need them\. For more information, see [Provisioned Capacity](#querying-glacier-archives-expedited-capacity)\. 
+ **`Standard`** – Allows you to access any of your archived objects within several hours\. Standard retrievals typically finish within 3–5 hours\. This is the default tier\.
+ **`Bulk`** – The lowest\-cost data access option in Amazon Glacier, enabling you to retrieve large amounts, even petabytes, of data inexpensively in a day\. `Bulk` access typically finishes within 5–12 hours\. 

To make an `Expedited`, `Standard`, or `Bulk` request, set the `Tier` request element in the [POST Object restore](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOSTrestore.html) REST API request to the option you want, or the equivalent in the AWS CLI or AWS SDKs\. For `Expedited` access, there is no need to designate whether an expedited retrieval is On\-Demand or Provisioned\. If you purchased provisioned capacity, all `Expedited` retrievals are automatically served through your provisioned capacity\. For information about tier pricing, see [Amazon Glacier Pricing](http://aws.amazon.com/glacier/pricing/)\.

### Provisioned Capacity<a name="querying-glacier-archives-expedited-capacity"></a>

Provisioned capacity guarantees that your retrieval capacity for expedited retrievals is available when you need it\. Each unit of capacity ensures that at least three expedited retrievals can be performed every five minutes and provides up to 150 MB/s of retrieval throughput\.

You should purchase provisioned retrieval capacity if your workload requires highly reliable and predictable access to a subset of your data in minutes\. Without provisioned capacity, `Expedited` retrievals are accepted, except for rare situations of unusually high demand\. However, if you require access to `Expedited` retrievals under all circumstances, you must purchase provisioned retrieval capacity\. You can purchase provisioned capacity using the Amazon S3 console, the Amazon Glacier console, the [Purchase Provisioned Capacity](http://docs.aws.amazon.com/amazonglacier/latest/dev/api-PurchaseProvisionedCapacity.html) REST API, the AWS SDKs, or the AWS CLI\. For provisioned capacity pricing information, see the [Amazon Glacier Pricing](https://aws.amazon.com/glacier/pricing/)\. 

## More Info<a name="querying-glacier-archives-more-info"></a>
+ [POST Object restore](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOSTrestore.html) in the *Amazon Simple Storage Service API Reference*
+ [SQL Reference for Amazon S3 Select and Amazon Glacier Select](http://docs.aws.amazon.com/AmazonS3/latest/dev/s3-glacier-select-sql-reference.html) in the *Amazon Simple Storage Service Developer Guide*