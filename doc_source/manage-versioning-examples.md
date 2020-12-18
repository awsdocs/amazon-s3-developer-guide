# Examples of enabling bucket versioning<a name="manage-versioning-examples"></a>

**Topics**
+ [Using the Amazon S3 console](#manage-versioning-examples-console)
+ [Using the AWS CLI](#manage-versioning-examples-cli)
+ [Using the AWS SDK for Java](#manage-versioning-examples-java)
+ [Using the AWS SDK for \.NET](#manage-versioning-examples-dotnet)
+ [Using other AWS SDKs](#manage-versioning-examples-sdks)

 This section provides examples of enabling versioning on a bucket\. The examples first enable versioning on a bucket and then retrieve versioning status\. For an introduction, see [Using versioning](Versioning.md)\.

## Using the Amazon S3 console<a name="manage-versioning-examples-console"></a>

For more information about enabling versioning on a bucket using the Amazon S3 console, see [ How Do I Enable or Suspend Versioning for an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-versioning.html) in the *Amazon Simple Storage Service Console User Guide*\.

## Using the AWS CLI<a name="manage-versioning-examples-cli"></a>

**Example: Enable versioning on a bucket**

```
aws s3api put-bucket-versioning --bucket DOC-EXAMPLE-BUCKET1 --versioning-configuration Status=Enabled
```

**Example: Enable versioning and multi\-factor authentication \(MFA\) delete on a bucket**

```
aws s3api put-bucket-versioning --bucket DOC-EXAMPLE-BUCKET1 --versioning-configuration Status=Enabled,MFADelete=Enabled --mfa "SERIAL 123456"
```

**Note**  
Using MFA delete requires an approved physical or virtual authentication device\. For more information about using MFA Delete on Amazon S3, see [MFA delete](Versioning.md#MultiFactorAuthenticationDelete)\.

For more information about enabling versioning using the AWS CLI, see [put\-bucket\-versioining](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/s3api/put-bucket-versioning.html) in the *AWS CLI Command Reference*\.

## Using the AWS SDK for Java<a name="manage-versioning-examples-java"></a>

**Example**  
For instructions on how to create and test a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\.   

```
import java.io.IOException;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.BucketVersioningConfiguration;
import com.amazonaws.services.s3.model.SetBucketVersioningConfigurationRequest;

public class BucketVersioningConfigurationExample {
    public static String bucketName = "*** bucket name ***"; 
    public static AmazonS3Client s3Client;

    public static void main(String[] args) throws IOException {
        s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
        s3Client.setRegion(Region.getRegion(Regions.US_EAST_1));
        try {

            // 1. Enable versioning on the bucket.
        	BucketVersioningConfiguration configuration = 
        			new BucketVersioningConfiguration().withStatus("Enabled");
            
			SetBucketVersioningConfigurationRequest setBucketVersioningConfigurationRequest = 
					new SetBucketVersioningConfigurationRequest(bucketName,configuration);
			
			s3Client.setBucketVersioningConfiguration(setBucketVersioningConfigurationRequest);
			
			// 2. Get bucket versioning configuration information.
			BucketVersioningConfiguration conf = s3Client.getBucketVersioningConfiguration(bucketName);
			 System.out.println("bucket versioning configuration status:    " + conf.getStatus());

        } catch (AmazonS3Exception amazonS3Exception) {
            System.out.format("An Amazon S3 error occurred. Exception: %s", amazonS3Exception.toString());
        } catch (Exception ex) {
            System.out.format("Exception: %s", ex.toString());
        }        
    }
}
```

## Using the AWS SDK for \.NET<a name="manage-versioning-examples-dotnet"></a>

For information about how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\. 

**Example**  

```
using System;
using Amazon.S3;
using Amazon.S3.Model;

namespace s3.amazon.com.docsamples
{
    class BucketVersioningConfiguration
    {
        static string bucketName = "*** bucket name ***";

        public static void Main(string[] args)
        {
            using (var client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1))
            {
                try
                {
                    EnableVersioningOnBucket(client);
                    string bucketVersioningStatus = RetrieveBucketVersioningConfiguration(client);
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
                        "To sign up for service, go to http://aws.amazon.com/s3");
                    }
                    else
                    {
                        Console.WriteLine(
                         "Error occurred. Message:'{0}' when listing objects",
                         amazonS3Exception.Message);
                    }
                }
            }

            Console.WriteLine("Press any key to continue...");
            Console.ReadKey();
        }

        static void EnableVersioningOnBucket(IAmazonS3 client)
        {

                PutBucketVersioningRequest request = new PutBucketVersioningRequest
                {
                    BucketName = bucketName,
                    VersioningConfig = new S3BucketVersioningConfig 
                    {
                        Status = VersionStatus.Enabled
                    }
                };

                PutBucketVersioningResponse response = client.PutBucketVersioning(request);
        }


        static string RetrieveBucketVersioningConfiguration(IAmazonS3 client)
        {
                GetBucketVersioningRequest request = new GetBucketVersioningRequest
                {
                    BucketName = bucketName
                };
 
                GetBucketVersioningResponse response = client.GetBucketVersioning(request);
                return response.VersioningConfig.Status;
            }
    }
}
```

## Using other AWS SDKs<a name="manage-versioning-examples-sdks"></a>

For information about using other AWS SDKs, see [Sample Code and Libraries](https://aws.amazon.com/code/)\. 