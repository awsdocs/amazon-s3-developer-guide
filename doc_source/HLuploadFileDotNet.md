# Upload a File<a name="HLuploadFileDotNet"></a>

The following tasks guide you through using the high\-level \.NET classes to upload a file\. The API provides several variations, *overloads*, of the `Upload` method to easily upload your data\.


**High\-Level API File Uploading Process**  

|  |  | 
| --- |--- |
| 1 | Create an instance of the `TransferUtility` class by providing your AWS credentials\.  | 
| 2 | Execute one of the `TransferUtility.Upload` overloads depending on whether you are uploading data from a file, a stream, or a directory\. | 

The following C\# code sample demonstrates the preceding tasks\.

**Example**  

```
1. TransferUtility utility = new TransferUtility();
2. utility.Upload(filePath, existingBucketName);
```

 When uploading large files using the \.NET API, timeout might occur even while data is being written to the request stream\. You can set explicit timeout using the `TransferUtilityConfig.DefaultTimeout` as demonstrated in the following C\# code sample\. 

**Example**  

```
1. TransferUtilityConfig config = new TransferUtilityConfig();
2. config.DefaultTimeout = 11111;
3. TransferUtility utility = new TransferUtility(config);
```

**Example**  
The following C\# code example uploads a file to an Amazon S3 bucket\. The example illustrates the use of various `TransferUtility.Upload` overloads to upload a file; each successive call to upload replaces the previous upload\. For instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)   

```
using System;
using System.IO;
using Amazon.S3;
using Amazon.S3.Transfer;

namespace s3.amazon.com.docsamples
{
    class UploadFileMPUHighLevelAPI
    {
        static string existingBucketName = "*** Provide bucket name ***";
        static string keyName            = "*** Provide your object key ***";
        static string filePath           = "*** Provide file name ***";

        static void Main(string[] args)
        {
            try
            { 
                TransferUtility fileTransferUtility = new
                    TransferUtility(new AmazonS3Client(Amazon.RegionEndpoint.USEast1));

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
            catch (AmazonS3Exception s3Exception)
            {
                Console.WriteLine(s3Exception.Message,
                                  s3Exception.InnerException);
            }
        }
    }
}
```