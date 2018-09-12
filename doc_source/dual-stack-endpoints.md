# Using Amazon S3 Dual\-Stack Endpoints<a name="dual-stack-endpoints"></a>

Amazon S3 dual\-stack endpoints support requests to S3 buckets over IPv6 and IPv4\. This section describes how to use dual\-stack endpoints\.

**Topics**
+ [Amazon S3 Dual\-Stack Endpoints](#dual-stack-endpoints-description)
+ [Using Dual\-Stack Endpoints from the AWS CLI](#dual-stack-endpoints-cli)
+ [Using Dual\-Stack Endpoints from the AWS SDKs](#dual-stack-endpoints-sdks)
+ [Using Dual\-Stack Endpoints from the REST API](#dual-stack-endpoints-examples-rest-api)

## Amazon S3 Dual\-Stack Endpoints<a name="dual-stack-endpoints-description"></a>

When you make a request to a dual\-stack endpoint, the bucket URL resolves to an IPv6 or an IPv4 address\. For more information about accessing a bucket over IPv6, see [Making Requests to Amazon S3 over IPv6](ipv6-access.md)\.

When using the REST API, you directly access an Amazon S3 endpoint by using the endpoint name \(URI\)\. You can access an S3 bucket through a dual\-stack endpoint by using a virtual hosted\-style or a path\-style endpoint name\. Amazon S3 supports only regional dual\-stack endpoint names, which means that you must specify the region as part of the name\. 

Use the following naming conventions for the dual\-stack virtual hosted\-style and path\-style endpoint names:
+ Virtual hosted\-style dual\-stack endpoint: 

   *bucketname*\.s3\.dualstack\.*aws\-region*\.amazonaws\.com

   
+ Path\-style dual\-stack endpoint: 

  s3\.dualstack\.*aws\-region*\.amazonaws\.com/*bucketname*

For more information about endpoint name style, see [Accessing a Bucket](UsingBucket.md#access-bucket-intro)\. For a list of Amazon S3 endpoints, see [Regions and Endpoints](http://docs.aws.amazon.com/general/latest/gr/rande.html#s3_region) in the *AWS General Reference*\. 

**Important**  
You can use transfer acceleration with dual\-stack endpoints\. For more information, see [Getting Started with Amazon S3 Transfer Acceleration](transfer-acceleration.md#transfer-acceleration-getting-started)\.

When using the AWS Command Line Interface \(AWS CLI\) and AWS SDKs, you can use a parameter or flag to change to a dual\-stack endpoint\. You can also specify the dual\-stack endpoint directly as an override of the Amazon S3 endpoint in the config file\. The following sections describe how to use dual\-stack endpoints from the AWS CLI and the AWS SDKs\.

## Using Dual\-Stack Endpoints from the AWS CLI<a name="dual-stack-endpoints-cli"></a>

This section provides examples of AWS CLI commands used to make requests to a dual\-stack endpoint\. For instructions on setting up the AWS CLI, see [Setting Up the AWS CLI](setup-aws-cli.md)\.

You set the configuration value `use_dualstack_endpoint` to `true` in a profile in your AWS Config file to direct all Amazon S3 requests made by the `s3` and `s3api` AWS CLI commands to the dual\-stack endpoint for the specified region\. You specify the region in the config file or in a command using the `--region` option\. 

When using dual\-stack endpoints with the AWS CLI, both `path` and `virtual` addressing styles are supported\. The addressing style, set in the config file, controls if the bucket name is in the hostname or part of the URL\. By default, the CLI will attempt to use virtual style where possible, but will fall back to path style if necessary\. For more information, see [AWS CLI Amazon S3 Configuration](http://docs.aws.amazon.com/cli/latest/topic/s3-config.html)\.

You can also make configuration changes by using a command, as shown in the following example, which sets `use_dualstack_endpoint` to `true` and `addressing_style` to `virtual` in the default profile\.

```
$ aws configure set default.s3.use_dualstack_endpoint true
$ aws configure set default.s3.addressing_style virtual
```

If you want to use a dual\-stack endpoint for specified AWS CLI commands only \(not all commands\), you can use either of the following methods: 
+ You can use the dual\-stack endpoint per command by setting the `--endpoint-url` parameter to `https://s3.dualstack.aws-region.amazonaws.com` or `http://s3.dualstack.aws-region.amazonaws.com` for any `s3` or `s3api` command\.

  ```
  $ aws s3api list-objects --bucket bucketname --endpoint-url https://s3.dualstack.aws-region.amazonaws.com
  ```
+ You can set up separate profiles in your AWS Config file\. For example, create one profile that sets `use_dualstack_endpoint` to `true` and a profile that does not set `use_dualstack_endpoint`\. When you run a command, specify which profile you want to use, depending upon whether or not you want to use the dual\-stack endpoint\. 

**Note**  
When using the AWS CLI you currently cannot use transfer acceleration with dual\-stack endpoints\. However, support for the AWS CLI is coming soon\. For more information, see [Using Transfer Acceleration from the AWS Command Line Interface \(AWS CLI\) ](transfer-acceleration-examples.md#transfer-acceleration-examples-aws-cli)\. 

## Using Dual\-Stack Endpoints from the AWS SDKs<a name="dual-stack-endpoints-sdks"></a>

This section provides examples of how to access a dual\-stack endpoint by using the AWS SDKs\. 

### AWS SDK for Java Dual\-Stack Endpoint Example<a name="dual-stack-endpoints-examples-java"></a>

The following example shows how to enable dual\-stack endpoints when creating an Amazon S3 client using the AWS SDK for Java\.

For instructions on creating and testing a working Java sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\. 

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class DualStackEndpoints {

    public static void main(String[] args) {
        String clientRegion = "*** Client region ***";
        String bucketName = "*** Bucket name ***";

        try {
            // Create an Amazon S3 client with dual-stack endpoints enabled.
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(clientRegion)
                    .withDualstackEnabled(true)
                    .build();

            s3Client.listObjects(bucketName);
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

If you are using the AWS SDK for Java on Windows, you might have to set the following Java virtual machine \(JVM\) property: 

```
java.net.preferIPv6Addresses=true
```

### AWS \.NET SDK Dual\-Stack Endpoint Example<a name="dual-stack-endpoints-examples-dotnet"></a>

When using the AWS SDK for \.NET you use the `AmazonS3Config` class to enable the use of a dual\-stack endpoint as shown in the following example\. 

```
var config = new AmazonS3Config
{
    UseDualstackEndpoint = true,
    RegionEndpoint = RegionEndpoint.USWest2
};

using (var s3Client = new AmazonS3Client(config))
{
    var request = new ListObjectsRequest
    {
        BucketName = “myBucket”
    };

    var response = await s3Client.ListObjectsAsync(request);
}
```

For a full \.NET sample for listing objects, see [Listing Keys Using the AWS SDK for \.NET](ListingObjectKeysUsingNetSDK.md)\. 

For information about how to create and test a working \.NET sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\. 

## Using Dual\-Stack Endpoints from the REST API<a name="dual-stack-endpoints-examples-rest-api"></a>

For information about making requests to dual\-stack endpoints by using the REST API, see [Making Requests to Dual\-Stack Endpoints by Using the REST API](RESTAPI.md#rest-api-dual-stack)\.