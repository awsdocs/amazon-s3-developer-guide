# Protecting Data Using Encryption<a name="UsingEncryption"></a>

Data protection refers to protecting data while in\-transit \(as it travels to and from Amazon S3\) and at rest \(while it is stored on disks in Amazon S3 data centers\)\. You can protect data in transit using Secure Sockets Layer \(SSL\) or client\-side encryption\. You have the following options for protecting data at rest in Amazon S3:
+ **Server\-Side Encryption** – Request Amazon S3 to encrypt your object before saving it on disks in its data centers and then decrypt it when you download the objects\. 
+ **Client\-Side Encryption** – Encrypt data client\-side and upload the encrypted data to Amazon S3\. In this case, you manage the encryption process, the encryption keys, and related tools\.

For more information about server\-side encryption and client\-side encryption, review the topics listed below\.

**Topics**
+ [Protecting Data Using Server\-Side Encryption](serv-side-encryption.md)
+ [Protecting Data Using Client\-Side Encryption](UsingClientSideEncryption.md)