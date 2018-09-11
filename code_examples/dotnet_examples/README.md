  Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

   This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0
   International License (the "License"). You may not use this file except in compliance with the
   License. A copy of the License is located at http://creativecommons.org/licenses/by-nc-sa/4.0/.

   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
   either express or implied. See the License for the specific language governing permissions and
   limitations under the License.


Amazon S3 Documentation .NET Examples
==============================================

These are the .NET examples that are used in the [Amazon S3 developer documentation](https://docs.aws.amazon.com/AmazonS3/latest/dev/Welcome.html).

Prerequisites
=============

To build and run these examples, you need the following:

* The [AWS SDK for .NET](https://aws.amazon.com/sdk-for-net/) (downloaded and extracted somewhere on
  your machine).
* AWS credentials, either configured in a local AWS credentials file or by setting the
  ``AWS_ACCESS_KEY_ID`` and ``AWS_SECRET_ACCESS_KEY`` environment variables. 
* Microsoft Visual Studio installed. The example code is tested using Visual Studio Professional 2015.

For information about how to set AWS credentials for use with the AWS SDK for .NET,
see [Using an AWS Credentials File](https://docs.aws.amazon.com/sdk-for-net/v3/developer-guide/net-dg-config-creds.html#creds-file) in the *AWS
SDK for .NET Developer Guide*.

Running the Code Examples
=========================

To run the .NET examples, you must do the following:

* Download the amazon-s3-developer-guide files from GitHub as a ZIP.
* Unzip the contents to a directory on your machine.
* Open the Visual Studio solution S3Examples.sln from amazon-s3-developer-guide/code_examples/dotnet_examples/.
* Open the S3Examples project properties and set the "start up object:" to the example that you want to run.
* Open the example .cs file and update the code by providing values for class variables such as bucketName, objectKey, and bucketRegion.
* Build and run the example code.

For more information about the AWS SDK for .NET, see [aws-net-developer-guide](https://github.com/awsdocs/aws-net-developer-guide/blob/master/doc_source/welcome.rst).

**IMPORTANT**

   The examples perform AWS operations for the account and Region for which you have specified
   credentials. By running the examples, you might incur AWS service charges. See the [AWS Pricing](https://aws.amazon.com/pricing/) page for details about the charges you can
   expect for a given service and operation.

   Some of these examples perform *destructive* operations on AWS resources, such as deleting an
   Amazon Glacier archive. **Be very careful** when running an operation that
   might delete or modify AWS resources in your account. It's best to create separate test-only
   resources when experimenting with these examples.

All of the examples require replacing certain configuration values in the source code. These values
are specified as string variables at the beginning of each example, and they begin and end with three stars
(for example, "\*\*\* bucket name \*\*\*"). The source code comments and developer guide provide
further information.
