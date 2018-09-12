# Examples of Creating a Bucket<a name="create-bucket-get-location-example"></a>

**Topics**
+ [Using the Amazon S3 Console](#create-bucket-get-location-console)
+ [Using the AWS SDK for Java](#create-bucket-get-location-java)
+ [Using the AWS SDK for \.NET](#create-bucket-get-location-dotnet)
+ [Using the AWS SDK for Ruby Version 3](#create-bucket-get-location-ruby)
+ [Using Other AWS SDKs](#create-bucket-using-other-sdks)

The following code examples create a bucket programmatically using the AWS SDKs for Java, \.NET, and Ruby\. The code examples perform the following tasks:
+ Create a bucket, if the bucket doesn't already exist—The examples create a bucket by performing the following tasks:
  + Create a client by explicitly specifying an AWS Region \(the example uses the `s3-eu-west-1` Region\)\. Accordingly, the client communicates with Amazon S3 using the `s3-eu-west-1.amazonaws.com` endpoint\. You can specify any other AWS Region\. For a list of AWS Regions, see [Regions and Endpoints](http://docs.aws.amazon.com/general/latest/gr/rande.html#s3_region) in the *AWS General Reference*\. 
  + Send a create bucket request by specifying only a bucket name\. The create bucket request doesn't specify another AWS Region\. The client sends a request to Amazon S3 to create the bucket in the Region you specified when creating the client\. Once you have created a bucket, you can't change its Region\.
**Note**  
If you explicitly specify an AWS Region in your create bucket request that is different from the Region you specifed when you created the client, you might get an error\. For more information, see [Creating a Bucket](UsingBucket.md#create-bucket-intro)\.

    The SDK libraries send the PUT bucket request to Amazon S3 to create the bucket\. For more information, see [PUT Bucket](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUT.html)\.
+ Retrieve information about the location of the bucket—Amazon S3 stores bucket location information in the *location* subresource that is associated with the bucket\. The SDK libraries send the GET Bucket location request \(see [GET Bucket location](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlocation.html)\) to retrieve this information\.

## Using the Amazon S3 Console<a name="create-bucket-get-location-console"></a>

To create a bucket using the Amazon S3 console, see [How Do I Create an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-bucket.html) in the *Amazon Simple Storage Service Console User Guide*\.

## Using the AWS SDK for Java<a name="create-bucket-get-location-java"></a>

**Example**  
This example shows how to create an Amazon S3 bucket using the AWS SDK for Java\. For instructions on creating and testing a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\.   

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

import java.io.IOException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;

public class CreateBucket {

    public static void main(String[] args) throws IOException {
        String clientRegion = "*** Client region ***";
        String bucketName = "*** Bucket name ***";

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(clientRegion)
                    .build();

            if (!s3Client.doesBucketExistV2(bucketName)) {
                // Because the CreateBucketRequest object doesn't specify a region, the
                // bucket is created in the region specified in the client.
                s3Client.createBucket(new CreateBucketRequest(bucketName));
                
                // Verify that the bucket was created by retrieving it and checking its location.
                String bucketLocation = s3Client.getBucketLocation(new GetBucketLocationRequest(bucketName));
                System.out.println("Bucket location: " + bucketLocation);
            }
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it and returned an error response.
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

## Using the AWS SDK for \.NET<a name="create-bucket-get-location-dotnet"></a>

For information about how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\. 

**Example**  

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

﻿using Amazon.S3;
using Amazon.S3.Model;
using Amazon.S3.Util;
using System;
using System.Threading.Tasks;

namespace Amazon.DocSamples.S3
{
    class CreateBucketTest
    {
        private const string bucketName = "*** bucket name ***";
        // Specify your bucket region (an example region is shown).
        private static readonly RegionEndpoint bucketRegion = RegionEndpoint.USWest2;
        private static IAmazonS3 s3Client;
        public static void Main()
        {
            s3Client = new AmazonS3Client(bucketRegion);
            CreateBucketAsync().Wait();
        }

        static async Task CreateBucketAsync()
        {
            try
            {
                if (!(await AmazonS3Util.DoesS3BucketExistAsync(s3Client, bucketName)))
                {
                    var putBucketRequest = new PutBucketRequest
                    {
                        BucketName = bucketName,
                        UseClientRegion = true
                    };

                    PutBucketResponse putBucketResponse = await s3Client.PutBucketAsync(putBucketRequest);
                }
                // Retrieve the bucket location.
                string bucketLocation = await FindBucketLocationAsync(s3Client);
            }
            catch (AmazonS3Exception e)
            {
                Console.WriteLine("Error encountered on server. Message:'{0}' when writing an object", e.Message);
            }
            catch (Exception e)
            {
                Console.WriteLine("Unknown encountered on server. Message:'{0}' when writing an object", e.Message);
            }
        }
        static async Task<string> FindBucketLocationAsync(IAmazonS3 client)
        {
            string bucketLocation;
            var request = new GetBucketLocationRequest()
            {
                BucketName = bucketName
            };
            GetBucketLocationResponse response = await client.GetBucketLocationAsync(request);
            bucketLocation = response.Location.ToString();
            return bucketLocation;
        }
    }
}
```

## Using the AWS SDK for Ruby Version 3<a name="create-bucket-get-location-ruby"></a>

For information about how to create and test a working sample, see [Using the AWS SDK for Ruby \- Version 3](UsingTheMPRubyAPI.md)\. 

**Example**  

```
require 'aws-sdk-s3'
	        
s3 = Aws::S3::Client.new(region: 'us-west-2')
s3.create_bucket(bucket: 'bucket-name')
```

## Using Other AWS SDKs<a name="create-bucket-using-other-sdks"></a>

For information about using other AWS SDKs, see [Sample Code and Libraries](https://aws.amazon.com/code/)\. 