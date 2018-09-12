# Amazon S3 Transfer Acceleration Examples<a name="transfer-acceleration-examples"></a>

This section provides examples of how to enable Amazon S3 Transfer Acceleration on a bucket and use the acceleration endpoint for the enabled bucket\. Some of the AWS SDK supported languages \(for example, Java and \.NET\) use an accelerate endpoint client configuration flag so you don't need to explicitly set the endpoint for Transfer Acceleration to *bucketname*\.s3\-accelerate\.amazonaws\.com\. For more information about Transfer Acceleration, see [Amazon S3 Transfer Acceleration](transfer-acceleration.md)\.

**Topics**
+ [Using the Amazon S3 Console](#transfer-acceleration-examples-console)
+ [Using Transfer Acceleration from the AWS Command Line Interface \(AWS CLI\)](#transfer-acceleration-examples-aws-cli)
+ [Using Transfer Acceleration with the AWS SDK for Java](#transfer-acceleration-examples-java)
+ [Using Transfer Acceleration from the AWS SDK for \.NET](#transfer-acceleration-examples-dotnet)
+ [Using Transfer Acceleration from the AWS SDK for JavaScript](#transfer-acceleration-examples-javascript)
+ [Using Transfer Acceleration from the AWS SDK for Python \(Boto\)](#transfer-acceleration-examples-python)
+ [Using Other AWS SDKs](#transfer-acceleration-examples-sdks)

## Using the Amazon S3 Console<a name="transfer-acceleration-examples-console"></a>

For information about enabling Transfer Acceleration on a bucket using the Amazon S3 console, see [Enabling Transfer Acceleration](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-transfer-acceleration.html) in the *Amazon Simple Storage Service Console User Guide*\.

## Using Transfer Acceleration from the AWS Command Line Interface \(AWS CLI\)<a name="transfer-acceleration-examples-aws-cli"></a>

This section provides examples of AWS CLI commands used for Transfer Acceleration\. For instructions on setting up the AWS CLI, see [Setting Up the AWS CLI](setup-aws-cli.md)\.

### Enabling Transfer Acceleration on a Bucket Using the AWS CLI<a name="transfer-acceleration-examples-aws-cli-1"></a>

Use the AWS CLI [ put\-bucket\-accelerate\-configuration](http://docs.aws.amazon.com/cli/latest/reference/s3api/put-bucket-accelerate-configuration.html) command to enable or suspend Transfer Acceleration on a bucket\. The following example sets `Status=Enabled` to enable Transfer Acceleration on a bucket\. You use `Status=Suspended` to suspend Transfer Acceleration\.

**Example**  

```
$ aws s3api put-bucket-accelerate-configuration --bucket bucketname --accelerate-configuration Status=Enabled
```

### Using the Transfer Acceleration from the AWS CLI<a name="transfer-acceleration-examples-aws-cli-2"></a>

Setting the configuration value `use_accelerate_endpoint` to `true` in a profile in your AWS Config File will direct all Amazon S3 requests made by s3 and s3api AWS CLI commands to the accelerate endpoint: `s3-accelerate.amazonaws.com`\. Transfer Acceleration must be enabled on your bucket to use the accelerate endpoint\. 

All request are sent using the virtual style of bucket addressing:  `my-bucket.s3-accelerate.amazonaws.com`\. Any `ListBuckets`, `CreateBucket`, and `DeleteBucket` requests will not be sent to the accelerate endpoint as the endpoint does not support those operations\. For more information about `use_accelerate_endpoint`, see [AWS CLI S3 Configuration](http://docs.aws.amazon.com/cli/latest/topic/s3-config.html)\. 

The following example sets `use_accelerate_endpoint` to `true` in the default profile\.

**Example**  

```
$ aws configure set default.s3.use_accelerate_endpoint true
```

If you want to use the accelerate endpoint for some AWS CLI commands but not others, you can use either one of the following two methods: 
+ You can use the accelerate endpoint per command by setting the `--endpoint-url` parameter to `https://s3-accelerate.amazonaws.com` or `http://s3-accelerate.amazonaws.com` for any s3 or s3api command\.
+ You can setup separate profiles in your AWS Config File\. For example, create one profile that sets `use_accelerate_endpoint` to `true` and a profile that does not set `use_accelerate_endpoint`\. When you execute a command specify which profile you want to use, depending upon whether or not you want to use the accelerate endpoint\. 

### AWS CLI Examples of Uploading an Object to a Bucket Enabled for Transfer Acceleration<a name="transfer-acceleration-examples-aws-cli-3"></a>

The following example uploads a file to a bucket enabled for Transfer Acceleration by using the default profile that has been configured to use the accelerate endpoint\.

**Example**  

```
$ aws s3 cp file.txt s3://bucketname/keyname --region region
```

The following example uploads a file to a bucket enabled for Transfer Acceleration by using the `--endpoint-url` parameter to specify the accelerate endpoint\.

**Example**  

```
$ aws configure set s3.addressing_style virtual
$ aws s3 cp file.txt s3://bucketname/keyname --region region --endpoint-url http://s3-accelerate.amazonaws.com
```

## Using Transfer Acceleration with the AWS SDK for Java<a name="transfer-acceleration-examples-java"></a>

### <a name="transfer-acceleration-examples-java-uploads"></a>

**Example**  
The following example shows how to use an accelerate endpoint to upload an object to Amazon S3\. The example does the following:  
+ Creates an `AmazonS3Client` that is configured to use accelerate endpoints\. All buckets that the client accesses must have transfer acceleration enabled\.
+ Enables transfer acceleration on a specified bucket\. This step is necessary only if the bucket you specify doesn't already have transfer acceleration enabled\.
+ Verifies that transfer acceleration is enabled for the specified bucket\.
+ Uploads a new object to the specified bucket using the bucket's accelerate endpoint\.
For more information about using Transfer Acceleration, see [Getting Started with Amazon S3 Transfer Acceleration](transfer-acceleration.md#transfer-acceleration-getting-started)\. For instructions on creating and testing a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\.  

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketAccelerateConfiguration;
import com.amazonaws.services.s3.model.BucketAccelerateStatus;
import com.amazonaws.services.s3.model.GetBucketAccelerateConfigurationRequest;
import com.amazonaws.services.s3.model.SetBucketAccelerateConfigurationRequest;

public class TransferAcceleration {
    public static void main(String[] args) {
        String clientRegion = "*** Client region ***";
        String bucketName = "*** Bucket name ***";
        String keyName = "*** Key name ***";
        
        try {
            // Create an Amazon S3 client that is configured to use the accelerate endpoint.
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new ProfileCredentialsProvider())
                    .enableAccelerateMode()
                    .build();

            // Enable Transfer Acceleration for the specified bucket.
            s3Client.setBucketAccelerateConfiguration(
                    new SetBucketAccelerateConfigurationRequest(bucketName,
                                                                new BucketAccelerateConfiguration(
                                                                        BucketAccelerateStatus.Enabled)));
    
            // Verify that transfer acceleration is enabled for the bucket.
            String accelerateStatus = s3Client.getBucketAccelerateConfiguration(
                                                    new GetBucketAccelerateConfigurationRequest(bucketName))
                                              .getStatus();
            System.out.println("Bucket accelerate status: " + accelerateStatus);
            
            // Upload a new object using the accelerate endpoint.
            s3Client.putObject(bucketName, keyName, "Test object for transfer acceleration");
            System.out.println("Object \"" + keyName + "\" uploaded with transfer acceleration.");
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }
}
```

## Using Transfer Acceleration from the AWS SDK for \.NET<a name="transfer-acceleration-examples-dotnet"></a>

The following example shows how to use the AWS SDK for \.NET to enable Transfer Acceleration on a bucket\. For instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.

**Example**  

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

﻿using Amazon.S3;
using Amazon.S3.Model;
using System;
using System.Threading.Tasks;

namespace Amazon.DocSamples.S3
{
    class TransferAccelerationTest
    {
        private const string bucketName = "*** bucket name ***";
        // Specify your bucket region (an example region is shown).
        private static readonly RegionEndpoint bucketRegion = RegionEndpoint.USWest2;
        private static IAmazonS3 s3Client;
        public static void Main()
        {
            s3Client = new AmazonS3Client(bucketRegion);
            EnableAccelerationAsync().Wait();
        }

        static async Task EnableAccelerationAsync()
        {
                try
                {
                    var putRequest = new PutBucketAccelerateConfigurationRequest
                    {
                        BucketName = bucketName,
                        AccelerateConfiguration = new AccelerateConfiguration
                        {
                            Status = BucketAccelerateStatus.Enabled
                        }
                    };
                    await s3Client.PutBucketAccelerateConfigurationAsync(putRequest);

                    var getRequest = new GetBucketAccelerateConfigurationRequest
                    {
                        BucketName = bucketName
                    };
                    var response = await s3Client.GetBucketAccelerateConfigurationAsync(getRequest);

                    Console.WriteLine("Acceleration state = '{0}' ", response.Status);
                }
                catch (AmazonS3Exception amazonS3Exception)
                {
                    Console.WriteLine(
                        "Error occurred. Message:'{0}' when setting transfer acceleration",
                        amazonS3Exception.Message);
                }
        }
    }
}
```

When uploading an object to a bucket that has Transfer Acceleration enabled, you specify using acceleration endpoint at the time of creating a client as shown:

```
var client = new AmazonS3Client(new AmazonS3Config
            {
                RegionEndpoint = TestRegionEndpoint,
                UseAccelerateEndpoint = true
            }
```

## Using Transfer Acceleration from the AWS SDK for JavaScript<a name="transfer-acceleration-examples-javascript"></a>

For an example of enabling Transfer Acceleration by using the AWS SDK for JavaScript, see [Calling the putBucketAccelerateConfiguration operation](http://docs.aws.amazon.com/AWSJavaScriptSDK/latest/AWS/S3.html#putBucketAccelerateConfiguration-property) in the *AWS SDK for JavaScript API Reference*\.

## Using Transfer Acceleration from the AWS SDK for Python \(Boto\)<a name="transfer-acceleration-examples-python"></a>

For an example of enabling Transfer Acceleration by using the SDK for Python, see [ put\_bucket\_accelerate\_configuration](http://boto3.readthedocs.org/en/latest/reference/services/s3.html#S3.Client.put_bucket_accelerate_configuration) in the *AWS SDK for Python \(Boto 3\) API Reference*\.

## Using Other AWS SDKs<a name="transfer-acceleration-examples-sdks"></a>

For information about using other AWS SDKs, see [Sample Code and Libraries](https://aws.amazon.com/code/)\. 