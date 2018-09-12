# Protecting Data Using Client\-Side Encryption<a name="UsingClientSideEncryption"></a>

*Client\-side encryption* is the act of encrypting data before sending it to Amazon S3\. To enable client\-side encryption, you have the following options:
+ Use an AWS KMS\-managed customer master key
+ Use a client\-side master key

The following AWS SDKs support client\-side encryption:
+ [AWS SDK for \.NET](https://aws.amazon.com/sdk-for-net/)
+ [AWS SDK for Go](https://aws.amazon.com/sdk-for-go/)
+ [AWS SDK for Java](https://aws.amazon.com/sdk-for-java/)
+ [AWS SDK for PHP](https://aws.amazon.com/sdk-for-php/)
+ [AWS SDK for Ruby](https://aws.amazon.com/sdk-for-ruby/)

## Option 1: Using an AWS KMS–Managed Customer Master Key \(CMK\)<a name="client-side-encryption-kms-managed-master-key-intro"></a>

When using an AWS KMS\-managed customer master key to enable client\-side data encryption, you provide an AWS KMS customer master key ID \(CMK ID\)\. 
+ **When uploading an object**—Using the CMK ID, the client first sends a request to the AWS Key Management Service \(AWS KMS\) for a key that it can use to encrypt your object data\. AWS KMS returns two versions of a randomly generated data encryption key:
  + A plain\-text version that the client uses to encrypt the object data
  + A cipher blob of the same data encryption key that the client uploads to Amazon S3 as object metadata
**Note**  
The client obtains a unique data encryption key for each object that it uploads\.
+  **When downloading an object**—The client downloads the encrypted object from Amazon S3 along with the cipher blob version of the data encryption key stored as object metadata\. The client then sends the cipher blob to AWS KMS to get the plain\-text version of the key so that it can decrypt the object data\.

For more information about AWS KMS, see [What is the AWS Key Management Service?](http://docs.aws.amazon.com/kms/latest/developerguide/overview.html) in the *AWS Key Management Service Developer Guide*\.

**Example**  
The following example uploads an object to Amazon S3 using AWS KMS with the AWS SDK for Java\. The example uses a KMS\-managed customer master key \(CMK\) to encrypt data on the client side before uploading it to Amazon S3\. If you already have a CMK, you can use that by specifying the value of the `kms_cmk_id` variable in the sample code\. If you don't have a CMK, or you need another one, you can generate one through the Java API\. The example shows how to generate a CMK\.  
For more information on key material, see [Importing Key Material in AWS Key Management Service \(AWS KMS\)](http://docs.aws.amazon.com/kms/latest/developerguide/importing-keys.html)\. For instructions on creating and testing a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\.  

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.CreateKeyResult;
import com.amazonaws.services.s3.AmazonS3Encryption;
import com.amazonaws.services.s3.AmazonS3EncryptionClientBuilder;
import com.amazonaws.services.s3.model.CryptoConfiguration;
import com.amazonaws.services.s3.model.KMSEncryptionMaterialsProvider;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class UploadObjectKMSKey {

    public static void main(String[] args) throws IOException {
        String bucketName = "*** Bucket name ***";
        String keyName = "*** Object key name ***";
        String clientRegion = "*** Client region ***";
        String kms_cmk_id = "***AWS KMS customer master key ID***";
        int readChunkSize = 4096;

        try {
            // Optional: If you don't have a KMS key (or need another one),
            // create one. This example creates a key with AWS-created
            // key material.
            AWSKMS kmsClient = AWSKMSClientBuilder.standard()
                                    .withCredentials(new ProfileCredentialsProvider())
                                    .withRegion(clientRegion)
                                    .build();
            CreateKeyResult keyResult = kmsClient.createKey();
            kms_cmk_id = keyResult.getKeyMetadata().getKeyId();
    
             // Create the encryption client.
            KMSEncryptionMaterialsProvider materialProvider = new KMSEncryptionMaterialsProvider(kms_cmk_id);
            CryptoConfiguration cryptoConfig = new CryptoConfiguration()
                    .withAwsKmsRegion(RegionUtils.getRegion(clientRegion));
            AmazonS3Encryption encryptionClient = AmazonS3EncryptionClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withEncryptionMaterials(materialProvider)
                    .withCryptoConfiguration(cryptoConfig)
                    .withRegion(clientRegion).build();
    
            // Upload an object using the encryption client.
            String origContent = "S3 Encrypted Object Using KMS-Managed Customer Master Key.";
            int origContentLength = origContent.length();
            encryptionClient.putObject(bucketName, keyName, origContent);
    
            // Download the object. The downloaded object is still encrypted.
            S3Object downloadedObject = encryptionClient.getObject(bucketName, keyName);
            S3ObjectInputStream input = downloadedObject.getObjectContent();
    
            // Decrypt and read the object and close the input stream.
            byte[] readBuffer = new byte[readChunkSize];
            ByteArrayOutputStream baos = new ByteArrayOutputStream(readChunkSize);
            int bytesRead = 0;
            int decryptedContentLength = 0;
    
            while ((bytesRead = input.read(readBuffer)) != -1) {
                baos.write(readBuffer, 0, bytesRead);
                decryptedContentLength += bytesRead;
            }
            input.close();
    
            // Verify that the original and decrypted contents are the same size.
            System.out.println("Original content length: " + origContentLength);
            System.out.println("Decrypted content length: " + decryptedContentLength);
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
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

## Option 2: Using a Client\-Side Master Key<a name="client-side-encryption-client-side-master-key-intro"></a>

This section shows how to use a client\-side master key for client\-side data encryption\. 

**Important**  
Your client\-side master keys and your unencrypted data are never sent to AWS\. It's important that you safely manage your encryption keys\. If you lose them, you won't be able to decrypt your data\.

This is how it works:
+ **When uploading an object**—You provide a client\-side master key to the Amazon S3 encryption client\. The client uses the master key only to encrypt the data encryption key that it generates randomly\. The process works like this:

  1. The Amazon S3 encryption client generates a one\-time\-use symmetric key \(also known as a data encryption key or data key\) locally\. It uses the data key to encrypt the data of a single Amazon S3 object\. The client generates a separate data key for each object\.

  1. The client encrypts the data encryption key using the master key that you provide\. The client uploads the encrypted data key and its material description as part of the object metadata\. The client uses the material description to determine which client\-side master key to use for decryption\.

  1. The client uploads the encrypted data to Amazon S3 and saves the encrypted data key as object metadata \(`x-amz-meta-x-amz-key`\) in Amazon S3\.
+ **When downloading an object**—The client downloads the encrypted object from Amazon S3\. Using the material description from the object's metadata, the client determines which master key to use to decrypt the data key\. The client uses that master key to decrypt the data key and then uses the data key to decrypt the object\. 

The client\-side master key that you provide can be either a symmetric key or a public/private key pair\. The following examples show how to use both types of keys\.

For more information, see [ Client\-Side Data Encryption with the AWS SDK for Java and Amazon S3 ](https://aws.amazon.com/articles/2850096021478074)\.

**Note**  
If you get a cipher\-encryption error message when you use the encryption API for the first time, your version of the JDK may have a Java Cryptography Extension \(JCE\) jurisdiction policy file that limits the maximum key length for encryption and decryption transformations to 128 bits\. The AWS SDK requires a maximum key length of 256 bits\. To check your maximum key length, use the `getMaxAllowedKeyLength()` method of the `javax.crypto.Cipher` class\. To remove the key\-length restriction, install the Java Cryptography Extension \(JCE\) Unlimited Strength Jurisdiction Policy Files at the [Java SE download page](http://docs.oracle.com/javase/8/)\.

**Example**  
The following example shows how to do these tasks:  
+ Generate a 256\-bit AES key
+ Save and load the AES key to and from the file system
+ Use the AES key to encrypt data on the client side before sending it to Amazon S3
+ Use the AES key to decrypt data received from Amazon S3
+ Verify that the decrypted data is the same as the original data
For instructions on creating and testing a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\.  

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3EncryptionClientBuilder;
import com.amazonaws.services.s3.model.EncryptionMaterials;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.StaticEncryptionMaterialsProvider;

public class S3ClientSideEncryptionSymMasterKey {

    public static void main(String[] args) throws Exception {
        String clientRegion = "*** Client region ***";
        String bucketName = "*** Bucket name ***";
        String objectKeyName = "*** Object key name ***";
        String masterKeyDir = System.getProperty("java.io.tmpdir");
        String masterKeyName = "secret.key";

        // Generate a symmetric 256-bit AES key.
        KeyGenerator symKeyGenerator = KeyGenerator.getInstance("AES");
        symKeyGenerator.init(256);
        SecretKey symKey = symKeyGenerator.generateKey();

        // To see how it works, save and load the key to and from the file system.
        saveSymmetricKey(masterKeyDir, masterKeyName, symKey);
        symKey = loadSymmetricAESKey(masterKeyDir, masterKeyName, "AES");

        try {
            // Create the Amazon S3 encryption client.
            EncryptionMaterials encryptionMaterials = new EncryptionMaterials(symKey);
            AmazonS3 s3EncryptionClient = AmazonS3EncryptionClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withEncryptionMaterials(new StaticEncryptionMaterialsProvider(encryptionMaterials))
                    .withRegion(clientRegion)
                    .build();

            // Upload a new object. The encryption client automatically encrypts it.
            byte[] plaintext = "S3 Object Encrypted Using Client-Side Symmetric Master Key.".getBytes();
            s3EncryptionClient.putObject(new PutObjectRequest(bucketName, 
                                                            objectKeyName, 
                                                            new ByteArrayInputStream(plaintext), 
                                                            new ObjectMetadata()));
    
            // Download and decrypt the object.
            S3Object downloadedObject = s3EncryptionClient.getObject(bucketName, objectKeyName);
            byte[] decrypted = com.amazonaws.util.IOUtils.toByteArray(downloadedObject.getObjectContent());
    
            // Verify that the data that you downloaded is the same as the original data.
            System.out.println("Plaintext: " + new String(plaintext));
            System.out.println("Decrypted text: " + new String(decrypted));
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }

    private static void saveSymmetricKey(String masterKeyDir, String masterKeyName, SecretKey secretKey) throws IOException {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(secretKey.getEncoded());
        FileOutputStream keyOutputStream = new FileOutputStream(masterKeyDir + File.separator + masterKeyName);
        keyOutputStream.write(x509EncodedKeySpec.getEncoded());
        keyOutputStream.close();
    }

    private static SecretKey loadSymmetricAESKey(String masterKeyDir, String masterKeyName, String algorithm)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        // Read the key from the specified file.
        File keyFile = new File(masterKeyDir + File.separator + masterKeyName);
        FileInputStream keyInputStream = new FileInputStream(keyFile);
        byte[] encodedPrivateKey = new byte[(int) keyFile.length()];
        keyInputStream.read(encodedPrivateKey);
        keyInputStream.close();

        // Reconstruct and return the master key.
        return new SecretKeySpec(encodedPrivateKey, "AES");
    }
}
```

The following example shows how to do these tasks:
+ Generate a 1024\-bit RSA key pair
+ Save and load the RSA keys to and from the file system
+ Use the RSA keys to encrypt data on the client side before sending it to Amazon S3
+ Use the RSA keys to decrypt data received from Amazon S3
+ Verify that the decrypted data is the same as the original data

For instructions on creating and testing a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\.

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3EncryptionClientBuilder;
import com.amazonaws.services.s3.model.EncryptionMaterials;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.StaticEncryptionMaterialsProvider;
import com.amazonaws.util.IOUtils;

public class S3ClientSideEncryptionAsymmetricMasterKey {

    public static void main(String[] args) throws Exception {
        String clientRegion = "*** Client region ***";
        String bucketName = "*** Bucket name ***";
        String objectKeyName = "*** Key name ***";
        String rsaKeyDir = System.getProperty("java.io.tmpdir");
        String publicKeyName = "public.key";
        String privateKeyName = "private.key";

        // Generate a 1024-bit RSA key pair.
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
        keyGenerator.initialize(1024, new SecureRandom());
        KeyPair origKeyPair = keyGenerator.generateKeyPair();

        // To see how it works, save and load the key pair to and from the file system.
        saveKeyPair(rsaKeyDir, publicKeyName, privateKeyName, origKeyPair);
        KeyPair keyPair = loadKeyPair(rsaKeyDir, publicKeyName, privateKeyName, "RSA");

        try {
            // Create the encryption client.
            EncryptionMaterials encryptionMaterials = new EncryptionMaterials(keyPair);
            AmazonS3 s3EncryptionClient = AmazonS3EncryptionClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withEncryptionMaterials(new StaticEncryptionMaterialsProvider(encryptionMaterials))
                    .withRegion(clientRegion)
                    .build();
    
            // Create a new object.
            byte[] plaintext = "S3 Object Encrypted Using Client-Side Asymmetric Master Key.".getBytes();
            S3Object object = new S3Object();
            object.setKey(objectKeyName);
            object.setObjectContent(new ByteArrayInputStream(plaintext));
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(plaintext.length);

            // Upload the object. The encryption client automatically encrypts it.
            PutObjectRequest putRequest = new PutObjectRequest(bucketName, 
                                                               object.getKey(), 
                                                               object.getObjectContent(),
                                                               metadata);
            s3EncryptionClient.putObject(putRequest);
    
            // Download and decrypt the object.
            S3Object downloadedObject = s3EncryptionClient.getObject(bucketName, object.getKey());
            byte[] decrypted = IOUtils.toByteArray(downloadedObject.getObjectContent());
    
            // Verify that the data that you downloaded is the same as the original data.
            System.out.println("Plaintext: " + new String(plaintext));
            System.out.println("Decrypted text: " + new String(decrypted));
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }

    private static void saveKeyPair(String dir, 
                                   String publicKeyName, 
                                   String privateKeyName, 
                                   KeyPair keyPair) throws IOException {
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // Write the public key to the specified file.
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        FileOutputStream publicKeyOutputStream = new FileOutputStream(dir + File.separator + publicKeyName);
        publicKeyOutputStream.write(x509EncodedKeySpec.getEncoded());
        publicKeyOutputStream.close();

        // Write the private key to the specified file.
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        FileOutputStream privateKeyOutputStream = new FileOutputStream(dir + File.separator + privateKeyName);
        privateKeyOutputStream.write(pkcs8EncodedKeySpec.getEncoded());
        privateKeyOutputStream.close();
    }

    private static KeyPair loadKeyPair(String dir,
                                      String publicKeyName,
                                      String privateKeyName,
                                      String algorithm)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Read the public key from the specified file.
        File publicKeyFile = new File(dir + File.separator + publicKeyName);
        FileInputStream publicKeyInputStream = new FileInputStream(publicKeyFile);
        byte[] encodedPublicKey = new byte[(int) publicKeyFile.length()];
        publicKeyInputStream.read(encodedPublicKey);
        publicKeyInputStream.close();

        // Read the private key from the specified file.
        File privateKeyFile = new File(dir + File.separator + privateKeyName);
        FileInputStream privateKeyInputStream = new FileInputStream(privateKeyFile);
        byte[] encodedPrivateKey = new byte[(int) privateKeyFile.length()];
        privateKeyInputStream.read(encodedPrivateKey);
        privateKeyInputStream.close();

        // Convert the keys into a key pair.
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        return new KeyPair(publicKey, privateKey);
    }
}
```