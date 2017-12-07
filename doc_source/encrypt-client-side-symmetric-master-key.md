# Example 1: Encrypt and Upload a File Using a Client\-Side Symmetric Master Key<a name="encrypt-client-side-symmetric-master-key"></a>

This section provides example code using the AWS SDK for Java to do the following:

+ First create a 256\-bit AES symmetric master key and save it to a file\.

+ Upload an object to Amazon S3 using an S3 encryption client that first encrypts sample data on the client\-side\. The example also downloads the object and verifies that the data is the same\.

## Example 1a: Creating a Symmetric Master Key<a name="ClientSideEncryptionExample-AESKey"></a>

Run this code to first generate a 256\-bit AES symmetric master key for encrypted uploads to Amazon S3\. The example saves the master key to a file \(secret\.key\) in a temp directory \(on Windows, it is the `c:\Users\<username>\AppData\Local\Tmp` folder\.

 For instructions on how to create and test a working sample, see [Using the AWS SDK for Java](UsingTheMPDotJavaAPI.md)\. 

```
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Assert;

public class GenerateSymmetricMasterKey {

    private static final String keyDir  = System.getProperty("java.io.tmpdir"); 
    private static final String keyName = "secret.key";
    
    public static void main(String[] args) throws Exception {
        
        //Generate symmetric 256 bit AES key.
        KeyGenerator symKeyGenerator = KeyGenerator.getInstance("AES");
        symKeyGenerator.init(256); 
        SecretKey symKey = symKeyGenerator.generateKey();
 
        //Save key.
        saveSymmetricKey(keyDir, symKey);
        
        //Load key.
        SecretKey symKeyLoaded = loadSymmetricAESKey(keyDir, "AES");           
        Assert.assertTrue(Arrays.equals(symKey.getEncoded(), symKeyLoaded.getEncoded()));
    }

    public static void saveSymmetricKey(String path, SecretKey secretKey) 
        throws IOException {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
                secretKey.getEncoded());
        FileOutputStream keyfos = new FileOutputStream(path + "/" + keyName);
        keyfos.write(x509EncodedKeySpec.getEncoded());
        keyfos.close();
    }
    
    public static SecretKey loadSymmetricAESKey(String path, String algorithm) 
        throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException{
        //Read private key from file.
        File keyFile = new File(path + "/" + keyName);
        FileInputStream keyfis = new FileInputStream(keyFile);
        byte[] encodedPrivateKey = new byte[(int)keyFile.length()];
        keyfis.read(encodedPrivateKey);
        keyfis.close(); 

        //Generate secret key.
        return new SecretKeySpec(encodedPrivateKey, "AES");
    }
}
```

This code example is for demonstration purposes only\. For production use, you should consult your security engineer on how to obtain or generate the client\-side master key\.

## Example 1b: Uploading a File to Amazon S3 Using a Symmetric Key<a name="ClientSideEncryptionExample-DirectoryUpload"></a>

Run this code to encrypt sample data using a symmetric master key created by the preceding code example\. The example uses an S3 encryption client to encrypt the data on the client\-side and then upload it to Amazon S3\. 

For instructions on how to create and test a working sample, see [Using the AWS SDK for Java](UsingTheMPDotJavaAPI.md)\.

```
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Assert;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3EncryptionClient;
import com.amazonaws.services.s3.model.EncryptionMaterials;
import com.amazonaws.services.s3.model.ListVersionsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.StaticEncryptionMaterialsProvider;
import com.amazonaws.services.s3.model.VersionListing;

public class S3ClientSideEncryptionWithSymmetricMasterKey {
    private static final String masterKeyDir = System.getProperty("java.io.tmpdir");
    private static final String bucketName = UUID.randomUUID() + "-"
            + DateTimeFormat.forPattern("yyMMdd-hhmmss").print(new DateTime());
    private static final String objectKey = UUID.randomUUID().toString();

    public static void main(String[] args) throws Exception {
        SecretKey mySymmetricKey = GenerateSymmetricMasterKey
                .loadSymmetricAESKey(masterKeyDir, "AES");

        EncryptionMaterials encryptionMaterials = new EncryptionMaterials(
                mySymmetricKey);

        AmazonS3EncryptionClient encryptionClient = new AmazonS3EncryptionClient(
                new ProfileCredentialsProvider(),
                new StaticEncryptionMaterialsProvider(encryptionMaterials));
        // Create the bucket
        encryptionClient.createBucket(bucketName);

        // Upload object using the encryption client.
        byte[] plaintext = "Hello World, S3 Client-side Encryption Using Asymmetric Master Key!"
                .getBytes();
        System.out.println("plaintext's length: " + plaintext.length);
        encryptionClient.putObject(new PutObjectRequest(bucketName, objectKey,
                new ByteArrayInputStream(plaintext), new ObjectMetadata()));

        // Download the object.
        S3Object downloadedObject = encryptionClient.getObject(bucketName,
                objectKey);
        byte[] decrypted = IOUtils.toByteArray(downloadedObject
                .getObjectContent());
        
        // Verify same data.
        Assert.assertTrue(Arrays.equals(plaintext, decrypted));
        deleteBucketAndAllContents(encryptionClient);
    }

    private static void deleteBucketAndAllContents(AmazonS3 client) {
        System.out.println("Deleting S3 bucket: " + bucketName);
        ObjectListing objectListing = client.listObjects(bucketName);

        while (true) {
            for ( Iterator<?> iterator = objectListing.getObjectSummaries().iterator(); iterator.hasNext(); ) {
                S3ObjectSummary objectSummary = (S3ObjectSummary) iterator.next();
                client.deleteObject(bucketName, objectSummary.getKey());
            }

            if (objectListing.isTruncated()) {
                objectListing = client.listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
        };
        VersionListing list = client.listVersions(new ListVersionsRequest().withBucketName(bucketName));
        for ( Iterator<?> iterator = list.getVersionSummaries().iterator(); iterator.hasNext(); ) {
            S3VersionSummary s = (S3VersionSummary)iterator.next();
            client.deleteVersion(bucketName, s.getKey(), s.getVersionId());
        }
        client.deleteBucket(bucketName);
    }
}
```