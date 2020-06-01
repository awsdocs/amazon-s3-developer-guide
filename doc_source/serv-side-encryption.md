# Protecting data using server\-side encryption<a name="serv-side-encryption"></a>

Server\-side encryption is the encryption of data at its destination by the application or service that receives it\. Amazon S3 encrypts your data at the object level as it writes it to disks in its data centers and decrypts it for you when you access it\. As long as you authenticate your request and you have access permissions, there is no difference in the way you access encrypted or unencrypted objects\. For example, if you share your objects using a presigned URL, that URL works the same way for both encrypted and unencrypted objects\. Additionally, when you list objects in your bucket, the list API returns a list of all objects, regardless of whether they are encrypted\.

**Note**  
You can't apply different types of server\-side encryption to the same object simultaneously\.

You have three mutually exclusive options, depending on how you choose to manage the encryption keys\.

**Server\-Side Encryption with Amazon S3\-Managed Keys \(SSE\-S3\)**  
When you use Server\-Side Encryption with Amazon S3\-Managed Keys \(SSE\-S3\), each object is encrypted with a unique key\. As an additional safeguard, it encrypts the key itself with a master key that it regularly rotates\. Amazon S3 server\-side encryption uses one of the strongest block ciphers available, 256\-bit Advanced Encryption Standard \(AES\-256\), to encrypt your data\. For more information, see [Protecting Data Using Server\-Side Encryption with Amazon S3\-Managed Encryption Keys \(SSE\-S3\)](UsingServerSideEncryption.md)\.

**Server\-Side Encryption with Customer Master Keys \(CMKs\) Stored in AWS Key Management Service \(SSE\-KMS\)**  
Server\-Side Encryption with Customer Master Keys \(CMKs\) Stored in AWS Key Management Service \(SSE\-KMS\) is similar to SSE\-S3, but with some additional benefits and charges for using this service\. There are separate permissions for the use of a CMK that provides added protection against unauthorized access of your objects in Amazon S3\. SSE\-KMS also provides you with an audit trail that shows when your CMK was used and by whom\. Additionally, you can create and manage customer managed CMKs or use AWS managed CMKs that are unique to you, your service, and your Region\. For more information, see [Protecting Data Using Server\-Side Encryption with CMKs Stored in AWS Key Management Service \(SSE\-KMS\)](UsingKMSEncryption.md)\.

**Server\-Side Encryption with Customer\-Provided Keys \(SSE\-C\)**  
With Server\-Side Encryption with Customer\-Provided Keys \(SSE\-C\), you manage the encryption keys and Amazon S3 manages the encryption, as it writes to disks, and decryption, when you access your objects\. For more information, see [Protecting data using server\-side encryption with customer\-provided encryption keys \(SSE\-C\)](ServerSideEncryptionCustomerKeys.md)\.