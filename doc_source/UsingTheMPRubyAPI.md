# Using the AWS SDK for Ruby \- Version 3<a name="UsingTheMPRubyAPI"></a>

The AWS SDK for Ruby provides an API for Amazon S3 bucket and object operations\. For object operations, you can use the API to upload objects in a single operation or upload large objects in parts \(see [Using the AWS SDK for Ruby for Multipart Upload](uploadobjusingmpu-ruby-sdk.md)\)\. However, the API for a single operation upload can also accept large objects and behind the scenes manage the upload in parts for you, thereby reducing the amount of script you need to write\.

## The Ruby API Organization<a name="RubyAPIOrganization"></a>

When creating Amazon S3 applications using the AWS SDK for Ruby, you must install the SDK for Ruby gem\. For more information, see the [AWS SDK for Ruby \- Version 3](http://docs.aws.amazon.com/sdkforruby/api/index.html)\. Once installed, you can access the API, including the following key classes: 
+ **Aws::S3::Resource—**Represents the interface to Amazon S3 for the Ruby SDK and provides methods for creating and enumerating buckets\. 

  The `S3` class provides the `#buckets` instance method for accessing existing buckets or creating new ones\.
+ **Aws::S3::Bucket—**Represents an Amazon S3 bucket\.  

  The `Bucket` class provides the `#object(key)` and `#objects` methods for accessing the objects in a bucket, as well as methods to delete a bucket and return information about a bucket, like the bucket policy\.
+ **Aws::S3::Object—**Represents an Amazon S3 object identified by its key\.

  The `Object` class provides methods for getting and setting properties of an object, specifying the storage class for storing objects, and setting object permissions using access control lists\. The `Object` class also has methods for deleting, uploading and copying objects\. When uploading objects in parts, this class provides options for you to specify the order of parts uploaded and the part size\.

For more information about the AWS SDK for Ruby API, go to [AWS SDK for Ruby \- Version 2](http://docs.aws.amazon.com/sdkforruby/api/index.html)\.

## Testing the Ruby Script Examples<a name="TestingRubySamples"></a>

The easiest way to get started with the Ruby script examples is to install the latest AWS SDK for Ruby gem\. For information about installing or updating to the latest gem, go to [AWS SDK for Ruby \- Version 3](http://docs.aws.amazon.com/sdkforruby/api/index.html)\. The following tasks guide you through the creation and testing of the Ruby script examples assuming that you have installed the AWS SDK for Ruby\.


**General Process of Creating and Testing Ruby Script Examples**  

|  |  | 
| --- |--- |
|  1  |  To access AWS, you must provide a set of credentials for your SDK for Ruby application\. For more information, see [ Configuring the AWS SDK for Ruby](http://docs.aws.amazon.com//sdk-for-ruby/v3/developer-guide/setup-config.html)\.   | 
|  2  |  Create a new SDK for Ruby script and add the following lines to the top of the script\.  <pre>#!/usr/bin/env ruby<br /><br />require 'rubygems'<br />require 'aws-sdk-s3'<br />								</pre> The first line is the interpreter directive and the two `require` statements import two required gems into your script\.  | 
|  3  |  Copy the code from the section you are reading to your script\.   | 
|  4  | Update the code by providing any required data\. For example, if uploading a file, provide the file path and the bucket name\. | 
|  5  |  Run the script\. Verify changes to buckets and objects by using the AWS Management Console\. For more information about the AWS Management Console, go to [https://aws\.amazon\.com/console/](https://aws.amazon.com/console/)\.  | 

**Ruby Samples**

The following links contain samples to help get you started with the SDK for Ruby \- Version 3:
+ [Using the AWS SDK for Ruby Version 3](create-bucket-get-location-example.md#create-bucket-get-location-ruby)
+ [Upload an Object Using the AWS SDK for Ruby](UploadObjSingleOpRuby.md)