# Specifying Server\-Side Encryption with Customer\-Provided Encryption Keys Using the \.NET SDK<a name="sse-c-using-dot-net-sdk"></a>

The following C\# code example illustrates server\-side encryption with customer\-provided keys \(SSE\-C\) \(see [Protecting Data Using Server\-Side Encryption with Customer\-Provided Encryption Keys \(SSE\-C\)](ServerSideEncryptionCustomerKeys.md)\)\. The example performs the following operations, each operation shows how you specify SSE\-C–related headers in the request:
+ **Put object** – upload an object requesting server\-side encryption using customer\-provided encryption keys\.
+ **Get object** – download the object uploaded in the previous step\. It shows that the request must provide the same encryption information for Amazon S3 to decrypt the object so that it can return it to you\.
+ **Get object metadata** – The request shows that the same encryption information you specified when creating the object is required to retrieve the object metadata\.
+ **Copy object** – This example makes a copy of the previously uploaded object\. Because the source object is stored using SSE\-C, you must provide encryption information in your copy request\. By default, the object copy will not be encrypted\. But in this example, you request that Amazon S3 store the object copy encrypted using SSE\-C, and therefore you provide encryption\-related information for the target as well\. 

**Note**  
When using multipart upload API to upload large objects, you provide the same encryption information that you provide in your request as shown in the following example\. For multipart upload \.NET SDK examples, see [Using the AWS \.NET SDK for Multipart Upload \(High\-Level API\)](usingHLmpuDotNet.md) and [Using the AWS \.NET SDK for Multipart Upload \(Low\-Level API\)](usingLLmpuDotNet.md)\.

For information about how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\. 

**Example**  

```
using System;
using System.IO;
using System.Security.Cryptography;
using Amazon.S3;
using Amazon.S3.Model;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace s3.amazon.com.docsamples
{
    class SSEClientEncryptionKeyObjectOperations
    {
        static string bucketName        = "*** bucket name ***"; 
        static string keyName           = "*** object key name for new object ***"; 
        static string copyTargetKeyName = "*** copy operation target object key name ***"; 

        static IAmazonS3 client;

        public static void Main(string[] args)
        {
            using (client = new AmazonS3Client(Amazon.RegionEndpoint.USWest2))
            {
                try
                {
                    // Create encryption key.
                    Aes aesEncryption = Aes.Create();
                    aesEncryption.KeySize = 256;
                    aesEncryption.GenerateKey();
                    string base64Key = Convert.ToBase64String(aesEncryption.Key);

                    // 1. Upload object.
                    PutObjectRequest putObjectRequest = UploadObject(base64Key);
                    // 2. Download object (and also verify content is same as what you uploaded).
                    DownloadObject(base64Key, putObjectRequest);
                    // 3. Get object metadata (and also verify AES256 encryption).
                    GetObjectMetadata(base64Key);
                    // 4. Copy object (both source and target objects use server-side encryption with 
                    //    customer-provided encryption key.
                    CopyObject(aesEncryption, base64Key);
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

            Console.WriteLine("Press any key to continue...");
            Console.ReadKey();
        }

        private static void CopyObject(Aes aesEncryption, string base64Key)
        {
            aesEncryption.GenerateKey();
            string copyBase64Key = Convert.ToBase64String(aesEncryption.Key);

            CopyObjectRequest copyRequest = new CopyObjectRequest
            {
                SourceBucket = bucketName,
                SourceKey = keyName,
                DestinationBucket = bucketName,
                DestinationKey = copyTargetKeyName,
                // Source object encryption information.
                CopySourceServerSideEncryptionCustomerMethod = ServerSideEncryptionCustomerMethod.AES256,
                CopySourceServerSideEncryptionCustomerProvidedKey = base64Key,
                // Target object encryption information.
                ServerSideEncryptionCustomerMethod = ServerSideEncryptionCustomerMethod.AES256,
                ServerSideEncryptionCustomerProvidedKey = copyBase64Key
            };
            client.CopyObject(copyRequest);
        }

        private static void DownloadObject(string base64Key, PutObjectRequest putObjectRequest)
        {
            GetObjectRequest getObjectRequest = new GetObjectRequest
            {
                BucketName = bucketName,
                Key = keyName,
                // Provide encryption information of the object stored in S3.
                ServerSideEncryptionCustomerMethod = ServerSideEncryptionCustomerMethod.AES256,
                ServerSideEncryptionCustomerProvidedKey = base64Key
            };

            using (GetObjectResponse getResponse = client.GetObject(getObjectRequest))
            using (StreamReader reader = new StreamReader(getResponse.ResponseStream))
            {
                string content = reader.ReadToEnd();
                Assert.AreEqual(putObjectRequest.ContentBody, content);
                Assert.AreEqual(ServerSideEncryptionCustomerMethod.AES256, getResponse.ServerSideEncryptionCustomerMethod);
            }
        }

        private static void GetObjectMetadata(string base64Key)
        {
            GetObjectMetadataRequest getObjectMetadataRequest = new GetObjectMetadataRequest
            {
                BucketName = bucketName,
                Key = keyName,

                // Object stored in S3 is encrypted. So provide necessary encryption information.
                ServerSideEncryptionCustomerMethod = ServerSideEncryptionCustomerMethod.AES256,
                ServerSideEncryptionCustomerProvidedKey = base64Key
            };

            GetObjectMetadataResponse getObjectMetadataResponse = client.GetObjectMetadata(getObjectMetadataRequest);
            Assert.AreEqual(ServerSideEncryptionCustomerMethod.AES256, getObjectMetadataResponse.ServerSideEncryptionCustomerMethod);
        }

        private static PutObjectRequest UploadObject(string base64Key)
        {
            PutObjectRequest putObjectRequest = new PutObjectRequest
            {
                BucketName = bucketName,
                Key = keyName,
                ContentBody = "sample text",
                ServerSideEncryptionCustomerMethod = ServerSideEncryptionCustomerMethod.AES256,
                ServerSideEncryptionCustomerProvidedKey = base64Key
            };
            PutObjectResponse putObjectResponse = client.PutObject(putObjectRequest);
            return putObjectRequest;
        }
    }
}
```

## Other Amazon S3 Operations and SSE\-C<a name="sse-c-dotnet-other-api"></a>

The example in the preceding section shows how to request server\-side encryption with customer\-provided key \(SSE\-C\) in the PUT, GET, Head, and Copy operations\. This section describes other APIs that support SSE\-C\.

To upload large objects, you can use multipart upload API \(see [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)\)\. You can use either high\-level or low\-level APIs to upload large objects\. These APIs support encryption\-related headers in the request\.
+ When using high\-level Transfer\-Utility API, you provide the encryption\-specific headers in the `TransferUtilityUploadRequest` as shown\. For code examples, see [Using the AWS \.NET SDK for Multipart Upload \(High\-Level API\)](usingHLmpuDotNet.md)\.

  ```
  TransferUtilityUploadRequest request = new TransferUtilityUploadRequest()
  {
      FilePath = filePath,
      BucketName = existingBucketName,
      Key = keyName,
      // Provide encryption information.
      ServerSideEncryptionCustomerMethod = ServerSideEncryptionCustomerMethod.AES256,
      ServerSideEncryptionCustomerProvidedKey = base64Key,
  };
  ```
+ When using the low\-level API, you provide encryption\-related information in the initiate multipart upload request, followed by identical encryption information in the subsequent upload part requests\. You do not need to provide any encryption\-specific headers in your complete multipart upload request\. For examples, see [Using the AWS \.NET SDK for Multipart Upload \(Low\-Level API\)](usingLLmpuDotNet.md)\.

  The following is a low\-level multipart upload example that makes a copy of an existing large object\. In the example, the object to be copied is stored in Amazon S3 using SSE\-C, and you want to save the target object also using SSE\-C\. In the example, you do the following:
  + Initiate a multipart upload request by providing an encryption key and related information\.
  + Provide source and target object encryption keys and related information in the `CopyPartRequest`\.
  + Obtain the size of the source object to be copied by retrieving the object metadata\.
  + Upload the objects in 5 MB parts\.  
**Example**  

  ```
  using System;
  using System.Collections.Generic;
  using System.Security.Cryptography;
  using Amazon.S3;
  using Amazon.S3.Model;
  
  namespace s3.amazon.com.docsamples
  {
      class SSECLowLevelMPUcopyObject
      {
          static string existingBucketName     = "*** bucket name ***";
          static string sourceKeyName          = "*** key name ***";
          static string targetKeyName          = "*** key name ***";
  
          static void Main(string[] args)
          {
              IAmazonS3 s3Client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1);
              List<CopyPartResponse> uploadResponses = new List<CopyPartResponse>();
  
              Aes aesEncryption = Aes.Create();
              aesEncryption.KeySize = 256;
              aesEncryption.GenerateKey();
              string base64Key = Convert.ToBase64String(aesEncryption.Key);
  
              // 1. Initialize.
              InitiateMultipartUploadRequest initiateRequest = new InitiateMultipartUploadRequest
              {
                  BucketName = existingBucketName,
                  Key = targetKeyName,
                  ServerSideEncryptionCustomerMethod = ServerSideEncryptionCustomerMethod.AES256,
                  ServerSideEncryptionCustomerProvidedKey = base64Key,
  
              };
  
              InitiateMultipartUploadResponse initResponse =
                  s3Client.InitiateMultipartUpload(initiateRequest);
  
              // 2. Upload Parts.
              long partSize = 5 * (long)Math.Pow(2, 20); // 5 MB
              long firstByte = 0;
              long lastByte = partSize;
  
              try
              {
                  // First find source object size. Because object is stored encrypted with
                  // customer provided key you need to provide encryption information in your request.
                  GetObjectMetadataRequest getObjectMetadataRequest = new GetObjectMetadataRequest()
                  {
                      BucketName = existingBucketName,
                      Key = sourceKeyName,
                      ServerSideEncryptionCustomerMethod = ServerSideEncryptionCustomerMethod.AES256,
                      ServerSideEncryptionCustomerProvidedKey = "***source object encryption key ***"
                  };
  
                  GetObjectMetadataResponse getObjectMetadataResponse = s3Client.GetObjectMetadata(getObjectMetadataRequest);
  
                  long filePosition = 0; 
                  for (int i = 1; filePosition < getObjectMetadataResponse.ContentLength; i++)
                  {
                      CopyPartRequest copyPartRequest = new CopyPartRequest
                      {
                          UploadId = initResponse.UploadId,
                          // Source.
                          SourceBucket = existingBucketName,
                          SourceKey = sourceKeyName,
                          // Source object is stored using SSE-C. Provide encryption information.
                          CopySourceServerSideEncryptionCustomerMethod = ServerSideEncryptionCustomerMethod.AES256,
                          CopySourceServerSideEncryptionCustomerProvidedKey = "***source object encryption key ***",
                          FirstByte = firstByte,
                          // If the last part is smaller then our normal part size then use the remaining size.
                          LastByte = lastByte > getObjectMetadataResponse.ContentLength ?
                              getObjectMetadataResponse.ContentLength - 1 : lastByte,
  
                          // Target.
                          DestinationBucket = existingBucketName,
                          DestinationKey = targetKeyName,
                          PartNumber = i,
                          // Ecnryption information for the target object.
                          ServerSideEncryptionCustomerMethod = ServerSideEncryptionCustomerMethod.AES256,
                          ServerSideEncryptionCustomerProvidedKey = base64Key
                      };
                      uploadResponses.Add(s3Client.CopyPart(copyPartRequest));
                      filePosition += partSize;
                      firstByte += partSize;
                      lastByte += partSize;
                  }
  
                  // Step 3: complete.
                  CompleteMultipartUploadRequest completeRequest = new CompleteMultipartUploadRequest
                  {
                      BucketName = existingBucketName,
                      Key = targetKeyName,
                      UploadId = initResponse.UploadId,
                  };
                  completeRequest.AddPartETags(uploadResponses);
  
                  CompleteMultipartUploadResponse completeUploadResponse =
                      s3Client.CompleteMultipartUpload(completeRequest);
              }
              catch (Exception exception)
              {
                  Console.WriteLine("Exception occurred: {0}", exception.Message);
                  AbortMultipartUploadRequest abortMPURequest = new AbortMultipartUploadRequest
                  {
                      BucketName = existingBucketName,
                      Key = targetKeyName,
                      UploadId = initResponse.UploadId
                  };
                  s3Client.AbortMultipartUpload(abortMPURequest);
              }
          }
      }
  }
  ```