# Upload a Directory<a name="HLuploadDirDotNet"></a>

Using the `TransferUtility` class you can also upload an entire directory\. By default, Amazon S3 only uploads the files at the root of the specified directory\. You can, however, specify to recursively upload files in all the subdirectories\. 

You can also specify filtering expressions to select files, in the specified directory, based on some filtering criteria\. For example, to upload only the \.pdf files from a directory you specify a "\*\.pdf" filter expression\. 

When uploading files from a directory you cannot specify the object's key name\. It is constructed from the file's location in the directory as well as its name\. For example, assume you have a directory, c:\\myfolder, with the following structure:

**Example**  

```
1. C:\myfolder
2.       \a.txt
3.       \b.pdf
4.       \media\               
5.              An.mp3
```

When you upload this directory, Amazon S3 uses the following key names:

**Example**  

```
1. a.txt
2. b.pdf
3. media/An.mp3
```

The following tasks guide you through using the high\-level \.NET classes to upload a directory\. 


**High\-Level API Directory Uploading Process**  

|  |  | 
| --- |--- |
| 1 | Create an instance of the `TransferUtility` class by providing your AWS credentials\.  | 
| 2 | Execute one of the `TransferUtility.UploadDirectory` overloads\. | 

The following C\# code sample demonstrates the preceding tasks\.

**Example**  

```
1. TransferUtility utility = new TransferUtility();
2. utility.UploadDirectory(directoryPath, existingBucketName);
```

**Example**  
The following C\# code example uploads a directory to an Amazon S3 bucket\. The example illustrates the use of various `TransferUtility.UploadDirectory` overloads to upload a directory, each successive call to upload replaces the previous upload\. For instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.  

```
using System;
using System.IO;
using Amazon.S3;
using Amazon.S3.Transfer;

namespace s3.amazon.com.docsamples
{
    class UploadDirectoryMPUHighLevelAPI
    {
        static string existingBucketName = "*** Provide bucket name ***";
        static string directoryPath      = "*** Provide directory name ***";

        static void Main(string[] args)
        {
            try
            {
                TransferUtility directoryTransferUtility =
                    new TransferUtility(new AmazonS3Client(Amazon.RegionEndpoint.USEast1));

                // 1. Upload a directory.
                directoryTransferUtility.UploadDirectory(directoryPath,
                                                         existingBucketName);
                Console.WriteLine("Upload statement 1 completed");

                // 2. Upload only the .txt files from a directory. 
                //    Also, search recursively. 
                directoryTransferUtility.UploadDirectory(
                                               directoryPath,
                                               existingBucketName,
                                               "*.txt",
                                               SearchOption.AllDirectories);
                Console.WriteLine("Upload statement 2 completed");

                // 3. Same as 2 and some optional configuration 
                //    Search recursively for .txt files to upload).
                TransferUtilityUploadDirectoryRequest request =
                    new TransferUtilityUploadDirectoryRequest
                    {
                        BucketName = existingBucketName,
                        Directory = directoryPath,
                        SearchOption = SearchOption.AllDirectories,
                        SearchPattern = "*.txt"
                    };

                directoryTransferUtility.UploadDirectory(request);
                Console.WriteLine("Upload statement 3 completed");
            }

            catch (AmazonS3Exception e)
            {
                Console.WriteLine(e.Message, e.InnerException);
            }
        }
    }
}
```