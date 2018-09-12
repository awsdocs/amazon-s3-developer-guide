# Specifying Server\-Side Encryption with Customer\-Provided Encryption Keys Using the AWS SDK for \.NET<a name="sse-c-using-dot-net-sdk"></a>

The following C\# example shows how server\-side encryption with customer\-provided keys \(SSE\-C\) works\. The example performs the following operations\. Each operation shows how to specify SSE\-C–related headers in the request\.
+ **Put object**—Uploads an object and requests server\-side encryption using customer\-provided encryption keys\. 
+ **Get object**—Downloads the object that was uploaded in the previous step\. The request provides the same encryption information that was provided when the object was uploaded\. Amazon S3 needs this information to decrypt the object and return it to you\.
+ **Get object metadata**—Provides the same encryption information used when the object was created to retrieve the object's metadata\.
+ **Copy object**—Makes a copy of the uploaded object\. Because the source object is stored using SSE\-C, the copy request must provide encryption information\. By default, Amazon S3 does not encrypt a copy of an object\. The code directs Amazon S3 to encrypte the copied object using SSE\-C by providing encryption\-related information for the target\. It also stores the target\.

**Note**  
For examples of uploading large objects using the multipart upload API, see [Using the AWS SDK for \.NET for Multipart Upload \(High\-Level API\)](usingHLmpuDotNet.md) and [Using the AWS SDK for \.NET for Multipart Upload \(Low\-Level API\)](usingLLmpuDotNet.md)\.

For information about SSE\-C, see [Protecting Data Using Server\-Side Encryption with Customer\-Provided Encryption Keys \(SSE\-C\)](ServerSideEncryptionCustomerKeys.md)\)\. For information about creating and testing a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\. 

**Example**  

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

﻿using Amazon.S3;
using Amazon.S3.Model;
using System;
using System.IO;
using System.Security.Cryptography;
using System.Threading.Tasks;

namespace Amazon.DocSamples.S3
{
    class SSEClientEncryptionKeyObjectOperationsTest
    {
        private const string bucketName = "*** bucket name ***"; 
        private const string keyName = "*** key name for new object created ***"; 
        private const string copyTargetKeyName = "*** key name for object copy ***";
        // Specify your bucket region (an example region is shown).
        private static readonly RegionEndpoint bucketRegion = RegionEndpoint.USWest2;
        private static IAmazonS3 client;

        public static void Main()
        {
            client = new AmazonS3Client(bucketRegion);
            ObjectOpsUsingClientEncryptionKeyAsync().Wait();
        }

        private static async Task ObjectOpsUsingClientEncryptionKeyAsync()
        {
            try
            {
                // Create an encryption key.
                Aes aesEncryption = Aes.Create();
                aesEncryption.KeySize = 256;
                aesEncryption.GenerateKey();
                string base64Key = Convert.ToBase64String(aesEncryption.Key);

                // 1. Upload the object.
                PutObjectRequest putObjectRequest = await UploadObjectAsync(base64Key);
                // 2. Download the object and verify that its contents matches what you uploaded.
                await DownloadObjectAsync(base64Key, putObjectRequest);
                // 3. Get object metadata and verify that the object uses AES-256 encryption.
                await GetObjectMetadataAsync(base64Key);
                // 4. Copy both the source and target objects using server-side encryption with 
                //    a customer-provided encryption key.
                await CopyObjectAsync(aesEncryption, base64Key);
            }
            catch (AmazonS3Exception e)
            {
                Console.WriteLine("Error encountered ***. Message:'{0}' when writing an object", e.Message);
            }
            catch (Exception e)
            {
                Console.WriteLine("Unknown encountered on server. Message:'{0}' when writing an object", e.Message);
            }
        }

        private static async Task<PutObjectRequest> UploadObjectAsync(string base64Key)
        {
            PutObjectRequest putObjectRequest = new PutObjectRequest
            {
                BucketName = bucketName,
                Key = keyName,
                ContentBody = "sample text",
                ServerSideEncryptionCustomerMethod = ServerSideEncryptionCustomerMethod.AES256,
                ServerSideEncryptionCustomerProvidedKey = base64Key
            };
            PutObjectResponse putObjectResponse = await client.PutObjectAsync(putObjectRequest);
            return putObjectRequest;
        }
        private static async Task DownloadObjectAsync(string base64Key, PutObjectRequest putObjectRequest)
        {
            GetObjectRequest getObjectRequest = new GetObjectRequest
            {
                BucketName = bucketName,
                Key = keyName,
                // Provide encryption information for the object stored in Amazon S3.
                ServerSideEncryptionCustomerMethod = ServerSideEncryptionCustomerMethod.AES256,
                ServerSideEncryptionCustomerProvidedKey = base64Key
            };

            using (GetObjectResponse getResponse = await client.GetObjectAsync(getObjectRequest))
            using (StreamReader reader = new StreamReader(getResponse.ResponseStream))
            {
                string content = reader.ReadToEnd();
                if (String.Compare(putObjectRequest.ContentBody, content) == 0)
                    Console.WriteLine("Object content is same as we uploaded");
                else
                    Console.WriteLine("Error...Object content is not same.");

                if (getResponse.ServerSideEncryptionCustomerMethod == ServerSideEncryptionCustomerMethod.AES256)
                    Console.WriteLine("Object encryption method is AES256, same as we set");
                else
                    Console.WriteLine("Error...Object encryption method is not the same as AES256 we set");

                // Assert.AreEqual(putObjectRequest.ContentBody, content);
                // Assert.AreEqual(ServerSideEncryptionCustomerMethod.AES256, getResponse.ServerSideEncryptionCustomerMethod);
            }
        }
        private static async Task GetObjectMetadataAsync(string base64Key)
        {
            GetObjectMetadataRequest getObjectMetadataRequest = new GetObjectMetadataRequest
            {
                BucketName = bucketName,
                Key = keyName,

                // The object stored in Amazon S3 is encrypted, so provide the necessary encryption information.
                ServerSideEncryptionCustomerMethod = ServerSideEncryptionCustomerMethod.AES256,
                ServerSideEncryptionCustomerProvidedKey = base64Key
            };

            GetObjectMetadataResponse getObjectMetadataResponse = await client.GetObjectMetadataAsync(getObjectMetadataRequest);
            Console.WriteLine("The object metadata show encryption method used is: {0}", getObjectMetadataResponse.ServerSideEncryptionCustomerMethod);
            // Assert.AreEqual(ServerSideEncryptionCustomerMethod.AES256, getObjectMetadataResponse.ServerSideEncryptionCustomerMethod);
        }
        private static async Task CopyObjectAsync(Aes aesEncryption, string base64Key)
        {
            aesEncryption.GenerateKey();
            string copyBase64Key = Convert.ToBase64String(aesEncryption.Key);

            CopyObjectRequest copyRequest = new CopyObjectRequest
            {
                SourceBucket = bucketName,
                SourceKey = keyName,
                DestinationBucket = bucketName,
                DestinationKey = copyTargetKeyName,
                // Information about the source object's encryption.
                CopySourceServerSideEncryptionCustomerMethod = ServerSideEncryptionCustomerMethod.AES256,
                CopySourceServerSideEncryptionCustomerProvidedKey = base64Key,
                // Information about the target object's encryption.
                ServerSideEncryptionCustomerMethod = ServerSideEncryptionCustomerMethod.AES256,
                ServerSideEncryptionCustomerProvidedKey = copyBase64Key
            };
            await client.CopyObjectAsync(copyRequest);
        }
    }
}
```

## Other Amazon S3 Operations and SSE\-C<a name="sse-c-dotnet-other-api"></a>

The example in the preceding section shows how to request server\-side encryption with customer\-provided key \(SSE\-C\) in the PUT, GET, Head, and Copy operations\. This section describes other Amazon S3 APIs that support SSE\-C\.

To upload large objects, you can use multipart upload API \(see [Uploading Objects Using Multipart Upload API](uploadobjusingmpu.md)\)\. AWS SDK for \.NET provides both high\-level or low\-level APIs to upload large objects\. These APIs support encryption\-related headers in the request\.
+ When using high\-level `Transfer-Utility `API, you provide the encryption\-specific headers in the `TransferUtilityUploadRequest` as shown\. For code examples, see [Using the AWS SDK for \.NET for Multipart Upload \(High\-Level API\)](usingHLmpuDotNet.md)\.

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
+ When using the low\-level API, you provide encryption\-related information in the initiate multipart upload request, followed by identical encryption information in the subsequent upload part requests\. You do not need to provide any encryption\-specific headers in your complete multipart upload request\. For examples, see [Using the AWS SDK for \.NET for Multipart Upload \(Low\-Level API\)](usingLLmpuDotNet.md)\.

  The following is a low\-level multipart upload example that makes a copy of an existing large object\. In the example, the object to be copied is stored in Amazon S3 using SSE\-C, and you want to save the target object also using SSE\-C\. In the example, you do the following:
  + Initiate a multipart upload request by providing an encryption key and related information\.
  + Provide source and target object encryption keys and related information in the `CopyPartRequest`\.
  + Obtain the size of the source object to be copied by retrieving the object metadata\.
  + Upload the objects in 5 MB parts\.  
**Example**  

  ```
  // Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
  // SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)
  
  ﻿using Amazon.S3;
  using Amazon.S3.Model;
  using System;
  using System.Collections.Generic;
  using System.IO;
  using System.Security.Cryptography;
  using System.Threading.Tasks;
  
  namespace Amazon.DocSamples.S3
  {
      class SSECLowLevelMPUcopyObjectTest
      {
          private const string existingBucketName = "*** bucket name ***";
          private const string sourceKeyName      = "*** source object key name ***"; 
          private const string targetKeyName      = "*** key name for the target object ***";
          private const string filePath           = @"*** file path ***";
          // Specify your bucket region (an example region is shown).
          private static readonly RegionEndpoint bucketRegion = RegionEndpoint.USWest2;
          private static IAmazonS3 s3Client;
          static void Main()
          {
              s3Client = new AmazonS3Client(bucketRegion);
              CopyObjClientEncryptionKeyAsync().Wait();
          }
  
          private static async Task CopyObjClientEncryptionKeyAsync()
          {
              Aes aesEncryption = Aes.Create();
              aesEncryption.KeySize = 256;
              aesEncryption.GenerateKey();
              string base64Key = Convert.ToBase64String(aesEncryption.Key);
  
              await CreateSampleObjUsingClientEncryptionKeyAsync(base64Key, s3Client);
  
              await CopyObjectAsync(s3Client, base64Key);
          }
          private static async Task CopyObjectAsync(IAmazonS3 s3Client, string base64Key)
          {
              List<CopyPartResponse> uploadResponses = new List<CopyPartResponse>();
  
              // 1. Initialize.
              InitiateMultipartUploadRequest initiateRequest = new InitiateMultipartUploadRequest
              {
                  BucketName = existingBucketName,
                  Key = targetKeyName,
                  ServerSideEncryptionCustomerMethod = ServerSideEncryptionCustomerMethod.AES256,
                  ServerSideEncryptionCustomerProvidedKey = base64Key,
              };
  
              InitiateMultipartUploadResponse initResponse =
                  await s3Client.InitiateMultipartUploadAsync(initiateRequest);
  
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
                      ServerSideEncryptionCustomerProvidedKey = base64Key // " * **source object encryption key ***"
                  };
  
                  GetObjectMetadataResponse getObjectMetadataResponse = await s3Client.GetObjectMetadataAsync(getObjectMetadataRequest);
  
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
                          CopySourceServerSideEncryptionCustomerProvidedKey = base64Key, //"***source object encryption key ***",
                          FirstByte = firstByte,
                          // If the last part is smaller then our normal part size then use the remaining size.
                          LastByte = lastByte > getObjectMetadataResponse.ContentLength ?
                              getObjectMetadataResponse.ContentLength - 1 : lastByte,
  
                          // Target.
                          DestinationBucket = existingBucketName,
                          DestinationKey = targetKeyName,
                          PartNumber = i,
                          // Encryption information for the target object.
                          ServerSideEncryptionCustomerMethod = ServerSideEncryptionCustomerMethod.AES256,
                          ServerSideEncryptionCustomerProvidedKey = base64Key
                      };
                      uploadResponses.Add(await s3Client.CopyPartAsync(copyPartRequest));
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
                      await s3Client.CompleteMultipartUploadAsync(completeRequest);
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
          private static async Task CreateSampleObjUsingClientEncryptionKeyAsync(string base64Key, IAmazonS3 s3Client)
          {
              // List to store upload part responses.
              List<UploadPartResponse> uploadResponses = new List<UploadPartResponse>();
  
              // 1. Initialize.
              InitiateMultipartUploadRequest initiateRequest = new InitiateMultipartUploadRequest
              {
                  BucketName = existingBucketName,
                  Key = sourceKeyName,
                  ServerSideEncryptionCustomerMethod = ServerSideEncryptionCustomerMethod.AES256,
                  ServerSideEncryptionCustomerProvidedKey = base64Key
              };
  
              InitiateMultipartUploadResponse initResponse =
                 await s3Client.InitiateMultipartUploadAsync(initiateRequest);
  
              // 2. Upload Parts.
              long contentLength = new FileInfo(filePath).Length;
              long partSize = 5 * (long)Math.Pow(2, 20); // 5 MB
  
              try
              {
                  long filePosition = 0;
                  for (int i = 1; filePosition < contentLength; i++)
                  {
                      UploadPartRequest uploadRequest = new UploadPartRequest
                      {
                          BucketName = existingBucketName,
                          Key = sourceKeyName,
                          UploadId = initResponse.UploadId,
                          PartNumber = i,
                          PartSize = partSize,
                          FilePosition = filePosition,
                          FilePath = filePath,
                          ServerSideEncryptionCustomerMethod = ServerSideEncryptionCustomerMethod.AES256,
                          ServerSideEncryptionCustomerProvidedKey = base64Key
                      };
  
                      // Upload part and add response to our list.
                      uploadResponses.Add(await s3Client.UploadPartAsync(uploadRequest));
  
                      filePosition += partSize;
                  }
  
                  // Step 3: complete.
                  CompleteMultipartUploadRequest completeRequest = new CompleteMultipartUploadRequest
                  {
                      BucketName = existingBucketName,
                      Key = sourceKeyName,
                      UploadId = initResponse.UploadId,
                      //PartETags = new List<PartETag>(uploadResponses)
  
                  };
                  completeRequest.AddPartETags(uploadResponses);
  
                  CompleteMultipartUploadResponse completeUploadResponse =
                      await s3Client.CompleteMultipartUploadAsync(completeRequest);
  
              }
              catch (Exception exception)
              {
                  Console.WriteLine("Exception occurred: {0}", exception.Message);
                  AbortMultipartUploadRequest abortMPURequest = new AbortMultipartUploadRequest
                  {
                      BucketName = existingBucketName,
                      Key = sourceKeyName,
                      UploadId = initResponse.UploadId
                  };
                  await s3Client.AbortMultipartUploadAsync(abortMPURequest);
              }
          }
      }
  }
  ```