# Amazon S3 Transfer Acceleration Examples<a name="transfer-acceleration-examples"></a>

This section provides examples of how to enable Amazon S3 Transfer Acceleration on a bucket and use the acceleration endpoint for the enabled bucket\. Some of the AWS SDK supported languages \(for example, Java and \.NET\) use an accelerate endpoint client configuration flag so you don't need to explicitly set the endpoint for Transfer Acceleration to *bucketname*\.s3\-accelerate\.amazonaws\.com\. For more information about Transfer Acceleration, see [Amazon S3 Transfer Acceleration](transfer-acceleration.md)\.


+ [Using the Amazon S3 Console](#transfer-acceleration-examples-console)
+ [Using Transfer Acceleration from the AWS Command Line Interface \(AWS CLI\)](#transfer-acceleration-examples-aws-cli)
+ [Using Transfer Acceleration from the AWS SDK for Java](#transfer-acceleration-examples-java)
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

## Using Transfer Acceleration from the AWS SDK for Java<a name="transfer-acceleration-examples-java"></a>

This section provides examples of using the AWS SDK for Java for Transfer Acceleration\. For information about how to create and test a working Java sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\. 

### Enabling Amazon S3 Transfer Acceleration on a Bucket from the AWS SDK for Java<a name="transfer-acceleration-examples-java-1"></a>

The following Java example shows how to enable Transfer Acceleration on a bucket\. 

**Example**  

```
import java.io.IOException;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.BucketAccelerateConfiguration;
import com.amazonaws.services.s3.model.BucketAccelerateStatus;
import com.amazonaws.services.s3.model.GetBucketAccelerateConfigurationRequest;
import com.amazonaws.services.s3.model.SetBucketAccelerateConfigurationRequest;

public class BucketAccelertionConfiguration {

    public static String bucketName = "*** Provide bucket name ***"; 
    public static AmazonS3Client s3Client;
    
    public static void main(String[] args) throws IOException {
       
        s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
        s3Client.setRegion(Region.getRegion(Regions.US_WEST_2));
        
		// 1. Enable bucket for Amazon S3 Transfer Acceleration.
        s3Client.setBucketAccelerateConfiguration(new SetBucketAccelerateConfigurationRequest(bucketName,
				new BucketAccelerateConfiguration(BucketAccelerateStatus.Enabled)));
      		
        // 2. Get the acceleration status of the bucket.
        String accelerateStatus = s3Client.getBucketAccelerateConfiguration(new GetBucketAccelerateConfigurationRequest(bucketName)).getStatus();
    
        System.out.println("Acceleration status = " + accelerateStatus);
            
    }
}
```

### Creating an Amazon S3 Client to Use a Amazon S3 Transfer Acceleration Endpoint from the AWS SDK for Java<a name="transfer-acceleration-examples-java-client"></a>

You use the `setS3ClientOptions` method from the AWS Java SDK to use a transfer acceleration endpoint when creating an instance of `AmazonS3Client`\. 

#### Creating an Amazon S3 Java Client to Use the Transfer Acceleration Endpoint<a name="transfer-acceleration-examples-java-client-accelerate"></a>

The following example shows how to use the `setS3ClientOptions` method from the AWS Java SDK to use a transfer acceleration endpoint when creating an instance of `AmazonS3Client`\. 

```
AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
s3Client.setRegion(Region.getRegion(Regions.US_WEST_2));
s3Client.setS3ClientOptions(S3ClientOptions.builder().setAccelerateModeEnabled(true).build());
```

#### Creating an Amazon S3 Java Client to Use the Transfer Acceleration Dual\-Stack Endpoint<a name="transfer-acceleration-examples-java-client-dual-stack"></a>

The following example shows how to use the `setS3ClientOptions` method from the AWS Java SDK to use a Transfer Acceleration dual\-stack endpoint when creating an instance of `AmazonS3Client`\. 

```
AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
s3Client.setRegion(Region.getRegion(Regions.US_WEST_2));
s3Client.setS3ClientOptions(S3ClientOptions.builder().enableDualstack().setAccelerateModeEnabled(true).build());
```

 If you are using the AWS Java SDK on Microsoft Windows to use a Transfer Acceleration dual\-stack endpoint, you might have to set the following Java virtual machine \(JVM\) property\. 

```
java.net.preferIPv6Addresses=true
```

### Uploading Objects to a Bucket Enabled for Transfer Acceleration Using the AWS SDK for Java<a name="transfer-acceleration-examples-java-uploads"></a>

The Java examples in this section show how to use the accelerate endpoint to upload objects\. You can use the examples with the Transfer Acceleration dual\-stack endpoint by changing the code that creates an instance of `AmazonS3Client` as described in [Creating an Amazon S3 Java Client to Use the Transfer Acceleration Dual\-Stack Endpoint](#transfer-acceleration-examples-java-client-dual-stack)\. 

For information about how to create and test a working Java sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\. 

#### Java Example: Uploading a Single Object to a Bucket Enabled for Transfer Acceleration<a name="transfer-acceleration-examples-java-2"></a>

The following Java example shows how to use the accelerate endpoint to upload a single object\.

**Example**  

```
import java.io.File;
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class AcceleratedUploadSingleObject {

    	private static String bucketName     = "*** Provide bucket name ***";
    	private static String keyName        = "*** Provide key name ***";
    	private static String uploadFileName = "*** Provide file name with full path ***";
    	 	
    	public static void main(String[] args) throws IOException {
            AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider()); 
            s3Client.setRegion(Region.getRegion(Regions.US_WEST_2));
            
            // Use Amazon S3 Transfer Acceleration endpoint.           
            s3Client.setS3ClientOptions(S3ClientOptions.builder().setAccelerateModeEnabled(true).build());
            
            try {
            	System.out.println("Uploading a new object to S3 from a file\n");
                File file = new File(uploadFileName);
                s3Client.putObject(new PutObjectRequest(
                		                 bucketName, keyName, file));

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

#### Java Example: Multipart Upload to a Bucket Enabled for Transfer Acceleration<a name="transfer-acceleration-examples-java-3"></a>

The following Java example shows how to use the accelerate endpoint for a multipart upload\.

**Example**  

```
import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;

import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

public class AccelerateMultipartUploadUsingHighLevelAPI {
 
    private static String EXISTING_BUCKET_NAME = "*** Provide bucket name ***";
    private static String KEY_NAME  = "*** Provide key name ***";
    private static String FILE_PATH = "*** Provide file name with full path ***";
	
    public static void main(String[] args) throws Exception {
        
        AmazonS3Client s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
        s3Client.configureRegion(Regions.US_WEST_2);
           
        // Use Amazon S3 Transfer Acceleration endpoint.           
        s3Client.setS3ClientOptions(S3ClientOptions.builder().setAccelerateModeEnabled(true).build());
       
    	TransferManager tm = new TransferManager(s3Client);        
        System.out.println("TransferManager");
        // TransferManager processes all transfers asynchronously, 
        // so this call will return immediately.
        Upload upload = tm.upload(
        		EXISTING_BUCKET_NAME, KEY_NAME, new File(FILE_PATH));
        System.out.println("Upload");

        try {
        	// Or you can block and wait for the upload to finish
        	upload.waitForCompletion();
        	System.out.println("Upload complete");
        } catch (AmazonClientException amazonClientException) {
        	System.out.println("Unable to upload file, upload was aborted.");
        	amazonClientException.printStackTrace();
        }
    }
}
```

## Using Transfer Acceleration from the AWS SDK for \.NET<a name="transfer-acceleration-examples-dotnet"></a>

This section provides examples of using the AWS SDK for \.NET for Transfer Acceleration\. For information about how to create and test a working \.NET sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\. 

### \.NET Example 1: Enable Transfer Acceleration on a Bucket<a name="transfer-acceleration-examples-examples-dotnet-1"></a>

The following \.NET example shows how to enable Transfer Acceleration on a bucket\. 

**Example**  

```
using System;
using System.Collections.Generic;
using Amazon.S3;
using Amazon.S3.Model;
using Amazon.S3.Util;

namespace s3.amazon.com.docsamples
{

    class SetTransferAccelerateState
    {
        private static string bucketName = "Provide bucket name";
            
        public static void Main(string[] args)
        {
            using (var s3Client = new AmazonS3Client(Amazon.RegionEndpoint.USWest2))
      
            try
            {
                EnableTransferAcclerationOnBucket(s3Client);
                BucketAccelerateStatus bucketAcclerationStatus = GetBucketAccelerateState(s3Client);

                Console.WriteLine("Acceleration state = '{0}' ", bucketAcclerationStatus);
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
                    "To sign up for the service, go to http://aws.amazon.com/s3");
                }
                else
                {
                    Console.WriteLine(
                     "Error occurred. Message:'{0}' when setting transfer acceleration",
                     amazonS3Exception.Message);
                }
            }
            Console.WriteLine("Press any key to continue...");
            Console.ReadKey();
        }

        static void EnableTransferAcclerationOnBucket(IAmazonS3 s3Client)
        {
            PutBucketAccelerateConfigurationRequest request = new PutBucketAccelerateConfigurationRequest
            {
                BucketName = bucketName,
                AccelerateConfiguration = new AccelerateConfiguration
                {
                    Status = BucketAccelerateStatus.Enabled
                }
            };

            PutBucketAccelerateConfigurationResponse response = s3Client.PutBucketAccelerateConfiguration(request);
        }

        static BucketAccelerateStatus GetBucketAccelerateState(IAmazonS3 s3Client)
        {
            GetBucketAccelerateConfigurationRequest request = new GetBucketAccelerateConfigurationRequest
            {
                BucketName = bucketName
            };

            GetBucketAccelerateConfigurationResponse response = s3Client.GetBucketAccelerateConfiguration(request);
            return response.Status;
        }
    }
}
```

### \.NET Example 2: Uploading a Single Object to a Bucket Enabled for Transfer Acceleration<a name="transfer-acceleration-examples-examples-dotnet-2"></a>

The following \.NET example shows how to use the accelerate endpoint to upload a single object\.

**Example**  

```
using System;
using System.Collections.Generic;
using Amazon;
using Amazon.S3;
using Amazon.S3.Model;
using Amazon.S3.Util;

namespace s3.amazon.com.docsamples
{

     public class UploadtoAcceleratedBucket
    {
        private static RegionEndpoint TestRegionEndpoint = RegionEndpoint.USWest2; 
        private static string bucketName = "Provide bucket name";
        static string keyName  = "*** Provide key name ***";
        static string filePath = "*** Provide filename of file to upload with the full path ***";
   
        public static void  Main(string[] args)
        {
            using (var client = new AmazonS3Client(new AmazonS3Config
            {
                RegionEndpoint = TestRegionEndpoint,
                UseAccelerateEndpoint = true
            }))
         
            {
                WriteObject(client);
                Console.WriteLine("Press any key to continue...");
                Console.ReadKey();
            }
        }

        static void WriteObject(IAmazonS3 client)
        {   
            try
            {
                PutObjectRequest putRequest = new PutObjectRequest
                {
                    BucketName = bucketName,
                    Key = keyName,
                    FilePath = filePath,
                };
            client.PutObject(putRequest);
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

### \.NET Example 3: Multipart Upload to a Bucket Enabled for Transfer Acceleration<a name="transfer-acceleration-examples-examples-java-3"></a>

The following \.NET example shows how to use the accelerate endpoint for a multipart upload\.

**Example**  

```
using System;
using System.IO;
using Amazon;
using Amazon.S3;
using Amazon.S3.Model;
using Amazon.S3.Transfer;

namespace s3.amazon.com.docsamples
{
    class AcceleratedUploadFileMPUHAPI
    {
        private static RegionEndpoint TestRegionEndpoint = RegionEndpoint.USWest2; 
        private static string existingBucketName = "Provide bucket name";
        private static string keyName    = "*** Provide your object key ***";
        private static string filePath   = "*** Provide file name with full path ***";
      
        static void Main(string[] args)
        {
            try
            {
                var client = new AmazonS3Client(new AmazonS3Config
                {
                    RegionEndpoint = TestRegionEndpoint,
                    UseAccelerateEndpoint = true
                });
                using (TransferUtility fileTransferUtility = new
                 TransferUtility(client))
                {

                    // 1. Upload a file, file name is used as the object key name.
                    fileTransferUtility.Upload(filePath, existingBucketName);
                    Console.WriteLine("Upload 1 completed");

                    // 2. Specify object key name explicitly.
                    fileTransferUtility.Upload(filePath,
                                              existingBucketName, keyName);
                    Console.WriteLine("Upload 2 completed");

                    // 3. Upload data from a type of System.IO.Stream.
                    using (FileStream fileToUpload =
                        new FileStream(filePath, FileMode.Open, FileAccess.Read))
                    {
                        fileTransferUtility.Upload(fileToUpload,
                                                   existingBucketName, keyName);
                    }
                    Console.WriteLine("Upload 3 completed");

                    // 4.Specify advanced settings/options.
                    TransferUtilityUploadRequest fileTransferUtilityRequest = new TransferUtilityUploadRequest
                    {
                        BucketName = existingBucketName,
                        FilePath = filePath,
                        StorageClass = S3StorageClass.ReducedRedundancy,
                        PartSize = 6291456, // 6 MB.
                        Key = keyName,
                        CannedACL = S3CannedACL.PublicRead
                    };
                    fileTransferUtilityRequest.Metadata.Add("param1", "Value1");
                    fileTransferUtilityRequest.Metadata.Add("param2", "Value2");
                    fileTransferUtility.Upload(fileTransferUtilityRequest);
                    Console.WriteLine("Upload 4 completed");
                }
            }
            catch (AmazonS3Exception s3Exception)
            {
                Console.WriteLine("{0} {1}", s3Exception.Message,
                                  s3Exception.InnerException);
            }
        }
    }
}
```

## Using Transfer Acceleration from the AWS SDK for JavaScript<a name="transfer-acceleration-examples-javascript"></a>

For an example of enabling Transfer Acceleration by using the AWS SDK for JavaScript, see [Calling the putBucketAccelerateConfiguration operation](http://docs.aws.amazon.com/AWSJavaScriptSDK/latest/AWS/S3.html#putBucketAccelerateConfiguration-property) in the *AWS SDK for JavaScript API Reference*\.

## Using Transfer Acceleration from the AWS SDK for Python \(Boto\)<a name="transfer-acceleration-examples-python"></a>

For an example of enabling Transfer Acceleration by using the SDK for Python, see [ put\_bucket\_accelerate\_configuration](http://boto3.readthedocs.org/en/latest/reference/services/s3.html#S3.Client.put_bucket_accelerate_configuration) in the *AWS SDK for Python \(Boto 3\) API Reference*\.

## Using Other AWS SDKs<a name="transfer-acceleration-examples-sdks"></a>

For information about using other AWS SDKs, see [Sample Code and Libraries](https://aws.amazon.com/code/)\. 