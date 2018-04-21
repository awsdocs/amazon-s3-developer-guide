# Example 2: Encrypt and Upload a File to Amazon S3 Using a Client\-Side Asymmetric Master Key<a name="encrypt-client-side-asymmetric-master-key"></a>

This section provides example code using the AWS SDK for Java to first create a 1024\-bit RSA key pair\. The example then uses that key pair as the client\-side master key for the purpose of encrypting and upload a file\.

This is how it works:
+ First create a 1024\-bit RSA key pair \(asymmetric master key\) and save it to a file\.
+ Upload an object to Amazon S3using an S3 encryption client that encrypts sample data on the client\-side\. The example also downloads the object and verifies that the data is the same\.

## Example 2a: Creating a 1024\-bit RSA Key Pair<a name="rsa-key-pair"></a>

Run this code to first generate a 1024\-bit key pair \(asymmetric master key\)\. The example saves the master key to a file \(secret\.key\) in a temp directory \(on Windows, it is the `c:\Users\<username>\AppData\Local\Tmp` folder\.

For instructions on how to create and test a working sample, see [Using the AWS SDK for Java](UsingTheMPDotJavaAPI.md)\.

```
import static org.junit.Assert.assertTrue;

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
import java.util.Arrays;

public class GenerateAsymmetricMasterKey {
    private static final String keyDir  = System.getProperty("java.io.tmpdir");
    private static final SecureRandom srand = new SecureRandom();

    public static void main(String[] args) throws Exception {
        // Generate RSA key pair of 1024 bits
        KeyPair keypair = genKeyPair("RSA", 1024);
        // Save to file system
        saveKeyPair(keyDir, keypair);
        // Loads from file system
        KeyPair loaded = loadKeyPair(keyDir, "RSA");
        // Sanity check
        assertTrue(Arrays.equals(keypair.getPublic().getEncoded(), loaded
                .getPublic().getEncoded()));
        assertTrue(Arrays.equals(keypair.getPrivate().getEncoded(), loaded
                .getPrivate().getEncoded()));
    }

    public static KeyPair genKeyPair(String algorithm, int bitLength)
            throws NoSuchAlgorithmException {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(algorithm);
        keyGenerator.initialize(1024, srand);
        return keyGenerator.generateKeyPair();
    }

    public static void saveKeyPair(String dir, KeyPair keyPair)
            throws IOException {
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
                publicKey.getEncoded());
        FileOutputStream fos = new FileOutputStream(dir + "/public.key");
        fos.write(x509EncodedKeySpec.getEncoded());
        fos.close();

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                privateKey.getEncoded());
        fos = new FileOutputStream(dir + "/private.key");
        fos.write(pkcs8EncodedKeySpec.getEncoded());
        fos.close();
    }

    public static KeyPair loadKeyPair(String path, String algorithm)
            throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        // read public key from file
        File filePublicKey = new File(path + "/public.key");
        FileInputStream fis = new FileInputStream(filePublicKey);
        byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
        fis.read(encodedPublicKey);
        fis.close();

        // read private key from file
        File filePrivateKey = new File(path + "/private.key");
        fis = new FileInputStream(filePrivateKey);
        byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
        fis.read(encodedPrivateKey);
        fis.close();

        // Convert them into KeyPair
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
                encodedPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
                encodedPrivateKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        return new KeyPair(publicKey, privateKey);
    }
}
```

This code example is for demonstration purposes only\. For production use, you should consult your security engineer on how to obtain or generate the client\-side master key\.

## Example 2b: Uploading a File to Amazon S3 Using a Key Pair<a name="ClientSideEncryptionExample-KeyPair"></a>

Run this code to encrypt sample data using a symmetric master key created by the preceding code example\. The example uses an S3 encryption client to encrypt the data on the client\-side and then upload it to Amazon S3\. 

For instructions on how to create and test a working sample, see [Using the AWS SDK for Java](UsingTheMPDotJavaAPI.md)\.

```
import java.io.ByteArrayInputStream;
import java.io.File;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
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

public class S3ClientSideEncryptionAsymmetricMasterKey {
    private static final String keyDir  = System.getProperty("java.io.tmpdir");
    private static final String bucketName = UUID.randomUUID() + "-"
            + DateTimeFormat.forPattern("yyMMdd-hhmmss").print(new DateTime());
    private static final String objectKey = UUID.randomUUID().toString();
    
    public static void main(String[] args) throws Exception {
   
        // 1. Load keys from files
        byte[] bytes = FileUtils.readFileToByteArray(new File(
                keyDir + "private.key"));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
        PrivateKey pk = kf.generatePrivate(ks);

        bytes = FileUtils.readFileToByteArray(new File(keyDir + "public.key"));
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(
                new X509EncodedKeySpec(bytes));

        KeyPair loadedKeyPair = new KeyPair(publicKey, pk);

        // 2. Construct an instance of AmazonS3EncryptionClient.
        EncryptionMaterials encryptionMaterials = new EncryptionMaterials(
                loadedKeyPair);
        AmazonS3EncryptionClient encryptionClient = new AmazonS3EncryptionClient(
                new ProfileCredentialsProvider(),
                new StaticEncryptionMaterialsProvider(encryptionMaterials));
        // Create the bucket
        encryptionClient.createBucket(bucketName);
        // 3. Upload the object.
        byte[] plaintext = "Hello World, S3 Client-side Encryption Using Asymmetric Master Key!"
                .getBytes();
        System.out.println("plaintext's length: " + plaintext.length);
        encryptionClient.putObject(new PutObjectRequest(bucketName, objectKey,
                new ByteArrayInputStream(plaintext), new ObjectMetadata()));

        // 4. Download the object.
        S3Object downloadedObject = encryptionClient.getObject(bucketName, objectKey);
        byte[] decrypted = IOUtils.toByteArray(downloadedObject
                .getObjectContent());
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