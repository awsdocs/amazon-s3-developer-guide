# Selecting Content from Objects<a name="selecting-content-from-objects"></a>

With Amazon S3 Select, you can use simple structured query language \(SQL\) statements to filter the contents of Amazon S3 objects and retrieve just the subset of data that you need\. By using Amazon S3 Select to filter this data, you can reduce the amount of data that Amazon S3 transfers, which reduces the cost and latency to retrieve this data\.

Amazon S3 Select works on objects stored in CSV, JSON, or Apache Parquet format\. It also works with objects that are compressed with GZIP or BZIP2 \(for CSV and JSON objects only\), and server\-side encrypted objects\. You can specify the format of the results as either CSV or JSON, and you can determine how the records in the result are delimited\.

You pass SQL expressions to Amazon S3 in the request\. Amazon S3 Select supports a subset of SQL\. For more information about the SQL elements that are supported by Amazon S3 Select, see [SQL Reference for Amazon S3 Select and Amazon Glacier Select](s3-glacier-select-sql-reference.md)\.

You can perform SQL queries using AWS SDKs, the SELECT Object Content REST API, the AWS Command Line Interface \(AWS CLI\), or the Amazon S3 console\. The Amazon S3 console limits the amount of data returned to 40 MB\. To retrieve more data, use the AWS CLI or the API\.

## Requirements and Limits<a name="selecting-content-from-objects-requirements-and-limits"></a>

The following are requirements for using Amazon S3 Select:
+ You must have `s3:GetObject` permission for the object you are querying\.
+ If the object you are querying is encrypted with a customer\-provided encryption key \(SSE\-C\), you must use `https`, and you must provide the encryption key in the request\.

The following limits apply when using Amazon S3 Select:
+ The maximum length of a SQL expression is 256 KB\.
+ The maximum length of a record in the result is 1 MB\.

Additional limitations apply when using Amazon S3 Select with Parquet objects:
+ Amazon S3 Select supports only columnar compression using GZIP or Snappy\. Amazon S3 Select doesn't support whole\-object compression for Parquet objects\.
+ Amazon S3 Select doesn't support Parquet output\. You must specify the output format as CSV or JSON\.
+ The maximum uncompressed block size is 256 MB\.
+ The maximum number of columns is 100\.
+ You must use the data types specified in the object's schema\.
+ Selecting on a repeated field returns only the last value\.

## Constructing a Request<a name="selecting-content-from-objects-contructing-request"></a>

When you construct a request, you provide details of the object that is being queried using an `InputSerialization` object\. You provide details of how the results are to be returned using an `OutputSerialization` object\. You also include the SQL expression that Amazon S3 uses to filter the request\.

For more information about constructing an Amazon S3 Select request, see [ SELECT Object Content](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectSELECTContent.html) in the *Amazon Simple Storage Service API Reference*\. You can also see one of the SDK code examples in the following sections\.

## Errors<a name="selecting-content-from-objects-errors"></a>

Amazon S3 Select returns an error code and associated error message when an issue is encountered while attempting to execute a query\. For a list of error codes and descriptions, see the [Special Errors](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectSELECTContent.html#RESTObjectSELECTContent-responses-special-errors) section of the *SELECT Object Content* page in the *Amazon Simple Storage Service API Reference*\.

**Topics**
+ [Requirements and Limits](#selecting-content-from-objects-requirements-and-limits)
+ [Constructing a Request](#selecting-content-from-objects-contructing-request)
+ [Errors](#selecting-content-from-objects-errors)
+ [Related Resources](#RelatedResources014)
+ [Selecting Content from Objects Using the SDK for Java](SelectObjectContentUsingJava.md)
+ [Selecting Content from Objects Using the REST API](SelectObjectContentUsingRestApi.md)
+ [Selecting Content from Objects Using Other SDKs](SelectObjectContentUsingOtherSDKs.md)

## Related Resources<a name="RelatedResources014"></a>
+ [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)