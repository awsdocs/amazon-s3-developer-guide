# Examples: Client\-Side Encryption \(Option 2: Using a Client\-Side Master Key \(AWS SDK for Java\)\)<a name="UsingClientSideEncryptionUpload"></a>

This section provides code examples of client\-side encryption\. As described in the overview \(see [Protecting Data Using Client\-Side Encryption](UsingClientSideEncryption.md)\) the client\-side master key you provide can be either a symmetric key or a public/private key pair\. This section provides examples of both types of master keys, symmetric master key \(256\-bit Advanced Encryption Standard \(AES\) secret key\) and asymmetric master key \(1024\-bit RSA key pair\)\. 


+ [Example 1: Encrypt and Upload a File Using a Client\-Side Symmetric Master Key](encrypt-client-side-symmetric-master-key.md)
+ [Example 2: Encrypt and Upload a File to Amazon S3 Using a Client\-Side Asymmetric Master Key](encrypt-client-side-asymmetric-master-key.md)

**Note**  
If you get a cipher encryption error message when you use the encryption API for the first time, your version of the JDK may have a Java Cryptography Extension \(JCE\) jurisdiction policy file that limits the maximum key length for encryption and decryption transformations to 128 bits\. The AWS SDK requires a maximum key length of 256 bits\. To check your maximum key length, use the `getMaxAllowedKeyLength` method of the `javax.crypto.Cipher` class\. To remove the key length restriction, install the Java Cryptography Extension \(JCE\) Unlimited Strength Jurisdiction Policy Files at the [Java SE download page](http://docs.oracle.com/javase/8/)\.