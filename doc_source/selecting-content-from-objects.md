# Selecting content from objects<a name="selecting-content-from-objects"></a>

With Amazon S3 Select, you can use simple structured query language \(SQL\) statements to filter the contents of Amazon S3 objects and retrieve just the subset of data that you need\. By using Amazon S3 Select to filter this data, you can reduce the amount of data that Amazon S3 transfers, which reduces the cost and latency to retrieve this data\.

Amazon S3 Select works on objects stored in CSV, JSON, or Apache Parquet format\. It also works with objects that are compressed with GZIP or BZIP2 \(for CSV and JSON objects only\), and server\-side encrypted objects\. You can specify the format of the results as either CSV or JSON, and you can determine how the records in the result are delimited\.

You pass SQL expressions to Amazon S3 in the request\. Amazon S3 Select supports a subset of SQL\. For more information about the SQL elements that are supported by Amazon S3 Select, see [SQL Reference for Amazon S3 Select and S3 Glacier Select](s3-glacier-select-sql-reference.md)\.

You can perform SQL queries using AWS SDKs, the SELECT Object Content REST API, the AWS Command Line Interface \(AWS CLI\), or the Amazon S3 console\. The Amazon S3 console limits the amount of data returned to 40 MB\. To retrieve more data, use the AWS CLI or the API\.

## Requirements and limits<a name="selecting-content-from-objects-requirements-and-limits"></a>

The following are requirements for using Amazon S3 Select:
+ You must have `s3:GetObject` permission for the object you are querying\.
+ If the object you are querying is encrypted with a customer\-provided encryption key \(SSE\-C\), you must use `https`, and you must provide the encryption key in the request\.

The following limits apply when using Amazon S3 Select:
+ The maximum length of a SQL expression is 256 KB\.
+ The maximum length of a record in the input or result is 1 MB\.
+ Amazon S3 Select can only emit nested data using the JSON output format\.

Additional limitations apply when using Amazon S3 Select with Parquet objects:
+ Amazon S3 Select supports only columnar compression using GZIP or Snappy\. Amazon S3 Select doesn't support whole\-object compression for Parquet objects\.
+ Amazon S3 Select doesn't support Parquet output\. You must specify the output format as CSV or JSON\.
+ The maximum uncompressed row group size is 256 MB\.
+ You must use the data types specified in the object's schema\.
+ Selecting on a repeated field returns only the last value\.

## Constructing a request<a name="selecting-content-from-objects-contructing-request"></a>

When you construct a request, you provide details of the object that is being queried using an `InputSerialization` object\. You provide details of how the results are to be returned using an `OutputSerialization` object\. You also include the SQL expression that Amazon S3 uses to filter the request\.

For more information about constructing an Amazon S3 Select request, see [ SELECTObjectContent](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectSELECTContent.html) in the *Amazon Simple Storage Service API Reference*\. You can also see one of the SDK code examples in the following sections\.

### Requests using scan ranges<a name="selecting-content-from-objects-using-byte-range"></a>

With Amazon S3 Select, you can scan a subset of an object by specifying a range of bytes to query\. This capability lets you parallelize scanning the whole object by splitting the work into separate Amazon S3 Select requests for a series of non\-overlapping scan ranges\. Scan ranges don't need to be aligned with record boundaries\. An Amazon S3 Select scan range request runs across the byte range that you specify\. A record that starts within the scan range specified but extends beyond the scan range will be processed by the query\. For example; the following shows an Amazon S3 object containing a series of records in a line\-delimited CSV format:

```
A,B
C,D
D,E
E,F
G,H
I,J
```

 Use the Amazon S3 Select `ScanRange` parameter and *Start* at \(Byte\) 1 and *End* at \(Byte\) 4\. So the scan range would start at **","** and scan till the end of record starting at **"C"** and return the result **C, D** because that is the end of the record\. 

 Amazon S3 Select scan range requests support Parquet, CSV \(without quoted delimiters\), and JSON objects \(in LINES mode only\)\. CSV and JSON objects must be uncompressed\. For line\-based CSV and JSON objects, when a scan range is specified as part of the Amazon S3 Select request, all records that start within the scan range are processed\. For Parquet objects, all of the row groups that start within the scan range requested are processed\. 

Amazon S3 Select scan range requests are available to use on the Amazon S3 CLI, API and SDK\. You can use the `ScanRange` parameter in the Amazon S3 Select request for this feature\. For more information, see the [ Amazon S3 SELECT Object Content](https://docs.aws.amazon.com/AmazonS3/latest/API/API_SelectObjectContent.html) in the *Amazon Simple Storage Service API Reference*\.

## Errors<a name="selecting-content-from-objects-errors"></a>

Amazon S3 Select returns an error code and associated error message when an issue is encountered while attempting to run a query\. For a list of error codes and descriptions, see the [List of SELECT Object Content Error Codes](https://docs.aws.amazon.com/AmazonS3/latest/API/ErrorResponses.html#SelectObjectContentErrorCodeList) section of the *Error Responses* page in the *Amazon Simple Storage Service API Reference*\.

**Topics**
+ [Requirements and limits](#selecting-content-from-objects-requirements-and-limits)
+ [Constructing a request](#selecting-content-from-objects-contructing-request)
+ [Errors](#selecting-content-from-objects-errors)
+ [Related resources](#RelatedResources014)
+ [Selecting content from objects using the SDK for Java](SelectObjectContentUsingJava.md)
+ [Selecting content from objects using the REST API](SelectObjectContentUsingRestApi.md)
+ [Selecting content from objects using other SDKs](SelectObjectContentUsingOtherSDKs.md)

## Related resources<a name="RelatedResources014"></a>
+ [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)