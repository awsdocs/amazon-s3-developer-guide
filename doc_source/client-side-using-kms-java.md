# Example: Client\-Side Encryption \(Option 1: Using an AWS KMS–Managed Customer Master Key \(AWS SDK for Java\)\)<a name="client-side-using-kms-java"></a>

The following Java code example uploads an object to Amazon S3\. The example uses a KMS\-managed customer master key \(CMK\) to encrypt data on the client\-side before uploading to Amazon S3\. You will need the CMK ID in the code\.

For more information about how client\-side encryption using a KMS\-managed CMK works, see [Option 1: Using an AWS KMS–Managed Customer Master Key \(CMK\)](UsingClientSideEncryption.md#client-side-encryption-kms-managed-master-key-intro)\. 

For instructions on how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\. You will need to update the code by providing your bucket name and a CMK ID\. 

```
import java.io.ByteArrayInputStream;
import java.util.Arrays;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3EncryptionClient;
import com.amazonaws.services.s3.model.CryptoConfiguration;
import com.amazonaws.services.s3.model.KMSEncryptionMaterialsProvider;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class testKMSkeyUploadObject {

    private static AmazonS3EncryptionClient encryptionClient;

    public static void main(String[] args) throws Exception { 
       String bucketName = "***bucket name***"; 
        String objectKey  = "ExampleKMSEncryptedObject";
        String kms_cmk_id = "***AWS KMS customer master key ID***";
        
        KMSEncryptionMaterialsProvider materialProvider = new KMSEncryptionMaterialsProvider(kms_cmk_id);
       
        encryptionClient = new AmazonS3EncryptionClient(new ProfileCredentialsProvider(), materialProvider,
                new CryptoConfiguration().withKmsRegion(Regions.US_EAST_1))
            .withRegion(Region.getRegion(Regions.US_EAST_1));
        
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
    }
}
```