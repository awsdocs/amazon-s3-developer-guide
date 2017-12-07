# Protecting Data Using Client\-Side Encryption<a name="UsingClientSideEncryption"></a>

Client\-side encryption refers to encrypting data before sending it to Amazon S3\. You have the following two options for using data encryption keys:

+ Use an AWS KMS\-managed customer master key

+ Use a client\-side master key

## Option 1: Using an AWS KMS–Managed Customer Master Key \(CMK\)<a name="client-side-encryption-kms-managed-master-key-intro"></a>

When using an AWS KMS\-managed customer master key for client\-side data encryption, you don't have to worry about providing any encryption keys to the Amazon S3 encryption client \(for example, the `AmazonS3EncryptionClient` in the AWS SDK for Java\)\. Instead, you provide only an AWS KMS customer master key ID \(CMK ID\), and the client does the rest\. This is how it works:

+ **When uploading an object** – Using the CMK ID, the client first sends a request to AWS KMS for a key that it can use to encrypt your object data\. In response, AWS KMS returns a randomly generated data encryption key\. In fact, AWS KMS returns two versions of the data encryption key:

  + A plain text version that the client uses to encrypt the object data\.

  + A cipher blob of the same data encryption key that the client uploads to Amazon S3 as object metadata\.
**Note**  
The client obtains a unique data encryption key for each object it uploads\.

  For a working example, see [Example: Client\-Side Encryption \(Option 1: Using an AWS KMS–Managed Customer Master Key \(AWS SDK for Java\)\)](client-side-using-kms-java.md)\.

+  **When downloading an object** – The client first downloads the encrypted object from Amazon S3 along with the cipher blob version of the data encryption key stored as object metadata\. The client then sends the cipher blob to AWS KMS to get the plain text version of the same, so that it can decrypt the object data\.

For more information about AWS KMS, go to [What is the AWS Key Management Service?](http://docs.aws.amazon.com/kms/latest/developerguide/overview.html) in the *AWS Key Management Service Developer Guide*\.

## Option 2: Using a Client\-Side Master Key<a name="client-side-encryption-client-side-master-key-intro"></a>

This section shows how to provide your client\-side master key in the client\-side data encryption process\. 

**Important**  
Your client\-side master keys and your unencrypted data are never sent to AWS; therefore, it is important that you safely manage your encryption keys\. If you lose them, you won't be able to decrypt your data\.

This is how it works:

+ **When uploading an object** – You provide a client\-side master key to the Amazon S3 encryption client \(for example, `AmazonS3EncryptionClient` when using the AWS SDK for Java\)\. The client uses this master key only to encrypt the data encryption key that it generates randomly\. The process works like this:

  1. The Amazon S3 encryption client locally generates a one\-time\-use symmetric key \(also known as a data encryption key or data key\)\. It uses this data key to encrypt the data of a single S3 object \(for each object, the client generates a separate data key\)\.

  1. The client encrypts the data encryption key using the master key you provide\. 

     The client uploads the encrypted data key and its material description as part of the object metadata\. The material description helps the client later determine which client\-side master key to use for decryption \(when you download the object, the client decrypts it\)\.

  1. The client then uploads the encrypted data to Amazon S3 and also saves the encrypted data key as object metadata \(`x-amz-meta-x-amz-key`\) in Amazon S3 by default\. 

+ **When downloading an object** – The client first downloads the encrypted object from Amazon S3 along with the metadata\. Using the material description in the metadata, the client first determines which master key to use to decrypt the encrypted data key\. Using that master key, the client decrypts the data key and uses it to decrypt the object\. 

The client\-side master key you provide can be either a symmetric key or a public/private key pair\. For examples, see [Examples: Client\-Side Encryption \(Option 2: Using a Client\-Side Master Key \(AWS SDK for Java\)\)](UsingClientSideEncryptionUpload.md)\.

For more information, see the [ Client\-Side Data Encryption with the AWS SDK for Java and Amazon S3 ](https://aws.amazon.com/articles/2850096021478074) article\.

The following AWS SDKs support client\-side encryption:

+ [AWS SDK for \.NET](https://aws.amazon.com/sdk-for-net/)

+ [AWS SDK for Go](https://aws.amazon.com/sdk-for-go/)

+ [AWS SDK for Java](https://aws.amazon.com/sdk-for-java/)

+ [AWS SDK for PHP](https://aws.amazon.com/sdk-for-php/)

+ [AWS SDK for Ruby](https://aws.amazon.com/sdk-for-ruby/)