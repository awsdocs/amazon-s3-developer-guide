# Examples of Creating a Bucket<a name="create-bucket-get-location-example"></a>

**Topics**
+ [Using the Amazon S3 Console](#create-bucket-get-location-console)
+ [Using the AWS SDK for Java](#create-bucket-get-location-java)
+ [Using the AWS SDK for \.NET](#create-bucket-get-location-dotnet)
+ [Using the AWS SDK for Ruby Version 3](#create-bucket-get-location-ruby)
+ [Using Other AWS SDKs](#create-bucket-using-other-sdks)

This section provides code examples of creating a bucket programmatically using the AWS SDKs for Java, \.NET, and Ruby\. The code examples perform the following tasks:
+ Create a bucket if it does not exist — The examples create a bucket as follows:
  + Create a client by explicitly specifying an AWS Region \(example uses the `s3-eu-west-1` Region\)\. Accordingly, the client communicates with Amazon S3 using the `s3-eu-west-1.amazonaws.com` endpoint\. You can specify any other AWS Region\. For a list of available AWS Regions, see [Regions and Endpoints](http://docs.aws.amazon.com/general/latest/gr/rande.html#s3_region) in the *AWS General Reference*\. 
  + Send a create bucket request by specifying only a bucket name\. The create bucket request does not specify another AWS Region; therefore, the client sends a request to Amazon S3 to create the bucket in the Region you specified when creating the client\. 
**Note**  
If you specify a Region in your create bucket request that conflicts with the Region you specify when you create the client, you might get an error\. For more information, see [Creating a Bucket](UsingBucket.md#create-bucket-intro)\.

    The SDK libraries send the PUT bucket request to Amazon S3 \(see [PUT Bucket](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUT.html)\) to create the bucket\. 
+ Retrieve bucket location information — Amazon S3 stores bucket location information in the *location* subresource associated with the bucket\. The SDK libraries send the GET Bucket location request \(see [GET Bucket location](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlocation.html)\) to retrieve this information\.

## Using the Amazon S3 Console<a name="create-bucket-get-location-console"></a>

For creating a bucket using Amazon S3 console, see [How Do I Create an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-bucket.html) in the *Amazon Simple Storage Service Console User Guide*\.

## Using the AWS SDK for Java<a name="create-bucket-get-location-java"></a>

**Example**  
For instructions on how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\.   

```
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;

public class CreateBucket {
	private static String bucketName     = "*** bucket name ***";
	
	public static void main(String[] args) throws IOException {
        AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
        s3client.setRegion(Region.getRegion(Regions.US_WEST_1));
       
        try {
            if(!(s3client.doesBucketExist(bucketName)))
            {
            	// Note that CreateBucketRequest does not specify region. So bucket is 
            	// created in the region specified in the client.
            	s3client.createBucket(new CreateBucketRequest(
						bucketName));
            }
            // Get location.
            String bucketLocation = s3client.getBucketLocation(new GetBucketLocationRequest(bucketName));
            System.out.println("bucket location = " + bucketLocation);

         } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which " +
            		"means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which " +
            		"means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
}
```

## Using the AWS SDK for \.NET<a name="create-bucket-get-location-dotnet"></a>

For information about how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\. 

**Example**  

```
using System;
using Amazon.S3;
using Amazon.S3.Model;
using Amazon.S3.Util;

namespace s3.amazon.com.docsamples
{
    class CreateBucket
    {
        static string bucketName = "*** bucket name ***";

        public static void Main(string[] args)
        {
            using (var client = new AmazonS3Client(Amazon.RegionEndpoint.EUWest1))
            {

                if (!(AmazonS3Util.DoesS3BucketExist(client, bucketName)))
                {
                    CreateABucket(client);
                }
                // Retrieve bucket location.
                string bucketLocation = FindBucketLocation(client);
            }

            Console.WriteLine("Press any key to continue...");
            Console.ReadKey();
        }

        static string FindBucketLocation(IAmazonS3 client)
        {
            string bucketLocation;
            GetBucketLocationRequest request = new GetBucketLocationRequest()
            {
                BucketName = bucketName
            };
            GetBucketLocationResponse response = client.GetBucketLocation(request);
            bucketLocation = response.Location.ToString();
            return bucketLocation;
        }

        static void CreateABucket(IAmazonS3 client)
        {
            try
            {
                PutBucketRequest putRequest1 = new PutBucketRequest
                {
                    BucketName = bucketName,
                    UseClientRegion = true
                };

                PutBucketResponse response1 = client.PutBucket(putRequest1);
            }
            catch (AmazonS3Exception amazonS3Exception)
            {
                if (amazonS3Exception.ErrorCode != null &&
                    (amazonS3Exception.ErrorCode.Equals("InvalidAccessKeyId")
                    ||
                    amazonS3Exception.ErrorCode.Equals("InvalidSecurity")))
                {
                    Console.WriteLine("Check the provided AWS Credentials.");
                    Console.WriteLine(
                        "For service sign up go to http://aws.amazon.com/s3");
                }
                else
                {
                    Console.WriteLine(
                        "Error occurred. Message:'{0}' when writing an object"
                        , amazonS3Exception.Message);
                }
            }
        }
    }
}
```

## Using the AWS SDK for Ruby Version 3<a name="create-bucket-get-location-ruby"></a>

For information about how to create and test a working sample, see [Using the AWS SDK for Ruby \- Version 3](UsingTheMPRubyAPI.md)\. 

**Example**  

```
require 'aws-sdk'
	        
s3 = Aws::S3::Client.new(region: 'us-west-1')
s3.create_bucket(bucket: 'bucket-name')
```

## Using Other AWS SDKs<a name="create-bucket-using-other-sdks"></a>

For information about using other AWS SDKs, see [Sample Code and Libraries](https://aws.amazon.com/code/)\. 