# Protecting data using client\-side encryption<a name="UsingClientSideEncryption"></a>

*Client\-side encryption* is the act of encrypting data before sending it to Amazon S3\. To enable client\-side encryption, you have the following options:
+ Use a customer master key \(CMK\) stored in AWS Key Management Service \(AWS KMS\)\.
+ Use a master key that you store within your application\.

The following AWS SDKs support client\-side encryption:
+ [AWS SDK for \.NET](https://aws.amazon.com/sdk-for-net/)
+ [AWS SDK for Go](https://aws.amazon.com/sdk-for-go/)
+ [AWS SDK for Java](https://aws.amazon.com/sdk-for-java/)
+ [AWS SDK for PHP](https://aws.amazon.com/sdk-for-php/)
+ [AWS SDK for Ruby](https://aws.amazon.com/sdk-for-ruby/)
+ [AWS SDK for C\+\+](https://aws.amazon.com/sdk-for-cpp/)

## Option 1: Using a CMK stored in AWS KMS<a name="client-side-encryption-kms-managed-master-key-intro"></a>

With this option, you use an AWS KMS CMK for client\-side encryption when uploading or downloading data in Amazon S3\.
+ **When uploading an object** — Using the CMK ID, the client first sends a request to AWS KMS for a CMK that it can use to encrypt your object data\. AWS KMS returns two versions of a randomly generated data key:
  + A plaintext version of the data key that the client uses to encrypt the object data\.
  + A cipher blob of the same data key that the client uploads to Amazon S3 as object metadata\.
**Note**  
The client obtains a unique data key for each object that it uploads\.
+  **When downloading an object** — The client downloads the encrypted object from Amazon S3 along with the cipher blob version of the data key stored as object metadata\. The client then sends the cipher blob to AWS KMS to get the plaintext version of the data key so that it can decrypt the object data\.

For more information about AWS KMS, see [What is AWS Key Management Service?](https://docs.aws.amazon.com/kms/latest/developerguide/overview.html) in the *AWS Key Management Service Developer Guide*\.

**Example**  
The following code example demonstrates how to upload an object to Amazon S3 using AWS KMS with the AWS SDK for Java\. The example uses an AWS managed CMK to encrypt data on the client side before uploading it to Amazon S3\. If you already have a CMK, you can use that by specifying the value of the `keyId` variable in the example code\. If you don't have a CMK, or you need another one, you can generate one through the Java API\. The example code automatically generates a CMK to use\.  
For instructions on creating and testing a working example, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\.  

```
        AWSKMS kmsClient = AWSKMSClientBuilder.standard()
                .withRegion(Regions.DEFAULT_REGION)
                .build();

        // create CMK for for testing this example
        CreateKeyRequest createKeyRequest = new CreateKeyRequest();
        CreateKeyResult createKeyResult = kmsClient.createKey(createKeyRequest);

// --
        // specify an Amazon KMS customer master key (CMK) ID
        String keyId = createKeyResult.getKeyMetadata().getKeyId();

        String s3ObjectKey = "EncryptedContent1.txt";
        String s3ObjectContent = "This is the 1st content to encrypt";
// --

        AmazonS3EncryptionV2 s3Encryption = AmazonS3EncryptionClientV2Builder.standard()
                .withRegion(Regions.US_WEST_2)
                .withCryptoConfiguration(new CryptoConfigurationV2().withCryptoMode(CryptoMode.StrictAuthenticatedEncryption))
                .withEncryptionMaterialsProvider(new KMSEncryptionMaterialsProvider(keyId))
                .build();

        s3Encryption.putObject(bucket_name, s3ObjectKey, s3ObjectContent);
        System.out.println(s3Encryption.getObjectAsString(bucket_name, s3ObjectKey));

        // schedule deletion of CMK generated for testing
        ScheduleKeyDeletionRequest scheduleKeyDeletionRequest =
                new ScheduleKeyDeletionRequest().withKeyId(keyId).withPendingWindowInDays(7);
        kmsClient.scheduleKeyDeletion(scheduleKeyDeletionRequest);

        s3Encryption.shutdown();
        kmsClient.shutdown();
```

## Option 2: Using a master key stored within your application<a name="client-side-encryption-client-side-master-key-intro"></a>

With this option, you use a master key that is stored within your application for client\-side data encryption\. 

**Important**  
Your client\-side master keys and your unencrypted data are never sent to AWS\. It's important that you safely manage your encryption keys\. If you lose them, you can't decrypt your data\.

This is how it works:
+ **When uploading an object** — You provide a client\-side master key to the Amazon S3 encryption client\. The client uses the master key only to encrypt the data encryption key that it generates randomly\. 

  The following steps describe the process:

  1. The Amazon S3 encryption client generates a one\-time\-use symmetric key \(also known as a *data encryption key* or *data key*\) locally\. It uses the data key to encrypt the data of a single Amazon S3 object\. The client generates a separate data key for each object\.

  1. The client encrypts the data encryption key using the master key that you provide\. The client uploads the encrypted data key and its material description as part of the object metadata\. The client uses the material description to determine which client\-side master key to use for decryption\.

  1. The client uploads the encrypted data to Amazon S3 and saves the encrypted data key as object metadata \(`x-amz-meta-x-amz-key`\) in Amazon S3\.
+ **When downloading an object** — The client downloads the encrypted object from Amazon S3\. Using the material description from the object's metadata, the client determines which master key to use to decrypt the data key\. The client uses that master key to decrypt the data key and then uses the data key to decrypt the object\. 

The client\-side master key that you provide can be either a symmetric key or a public/private key pair\. The following code examples show how to use each type of key\.

For more information, see [Client\-Side Data Encryption with the AWS SDK for Java and Amazon S3](https://aws.amazon.com/articles/2850096021478074)\.

**Note**  
If you get a cipher\-encryption error message when you use the encryption API for the first time, your version of the JDK might have a Java Cryptography Extension \(JCE\) jurisdiction policy file that limits the maximum key length for encryption and decryption transformations to 128 bits\. The AWS SDK requires a maximum key length of 256 bits\.   
To check your maximum key length, use the `getMaxAllowedKeyLength()` method of the `javax.crypto.Cipher` class\. To remove the key\-length restriction, install the [Java Cryptography Extension \(JCE\) Unlimited Strength Jurisdiction Policy Files](https://www.oracle.com/java/technologies/javase-jce8-downloads.html      )\.

**Example**  
The following code example shows how to do these tasks:  
+ Generate a 256\-bit AES key\.
+ Use the AES key to encrypt data on the client side before sending it to Amazon S3\.
+ Use the AES key to decrypt data received from Amazon S3\.
+ Print out a string representation of the decrypted object\.
For instructions on creating and testing a working example, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\.  

```
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);

// --
        // generate a symmetric encryption key for testing
        SecretKey secretKey = keyGenerator.generateKey();

        String s3ObjectKey = "EncryptedContent2.txt";
        String s3ObjectContent = "This is the 2nd content to encrypt";
// --

        AmazonS3EncryptionV2 s3Encryption = AmazonS3EncryptionClientV2Builder.standard()
                .withRegion(Regions.DEFAULT_REGION)
                .withClientConfiguration(new ClientConfiguration())
                .withCryptoConfiguration(new CryptoConfigurationV2().withCryptoMode(CryptoMode.AuthenticatedEncryption))
                .withEncryptionMaterialsProvider(new StaticEncryptionMaterialsProvider(new EncryptionMaterials(secretKey)))
                .build();

        s3Encryption.putObject(bucket_name, s3ObjectKey, s3ObjectContent);
        System.out.println(s3Encryption.getObjectAsString(bucket_name, s3ObjectKey));
        s3Encryption.shutdown();
```

**Example**  
The following code example shows how to do these tasks:  
+ Generate a 2048\-bit RSA key pair for testing purposes\.
+ Use the RSA keys to encrypt data on the client side before sending it to Amazon S3\.
+ Use the RSA keys to decrypt data received from Amazon S3\.
+ Print out a string representation of the decrypted object\.
For instructions on creating and testing a working example, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\.  

```
	        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
	        keyPairGenerator.initialize(2048);
	
	// --
	        // generate an asymmetric key pair for testing
	        KeyPair keyPair = keyPairGenerator.generateKeyPair();
	
	        String s3ObjectKey = "EncryptedContent3.txt";
	        String s3ObjectContent = "This is the 3rd content to encrypt";
	// --
	
	        AmazonS3EncryptionV2 s3Encryption = AmazonS3EncryptionClientV2Builder.standard()
	                .withRegion(Regions.US_WEST_2)
	                .withCryptoConfiguration(new CryptoConfigurationV2().withCryptoMode(CryptoMode.StrictAuthenticatedEncryption))
	                .withEncryptionMaterialsProvider(new StaticEncryptionMaterialsProvider(new EncryptionMaterials(keyPair)))
	                .build();
	
	        s3Encryption.putObject(bucket_name, s3ObjectKey, s3ObjectContent);
	        System.out.println(s3Encryption.getObjectAsString(bucket_name, s3ObjectKey));
	        s3Encryption.shutdown();
```