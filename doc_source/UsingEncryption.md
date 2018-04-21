# Protecting Data Using Encryption<a name="UsingEncryption"></a>

**Topics**
+ [Protecting Data Using Server\-Side Encryption](serv-side-encryption.md)
+ [Protecting Data Using Client\-Side Encryption](UsingClientSideEncryption.md)

Data protection refers to protecting data while in\-transit \(as it travels to and from Amazon S3\) and at rest \(while it is stored on disks in Amazon S3 data centers\)\. You can protect data in transit by using SSL or by using client\-side encryption\. You have the following options of protecting data at rest in Amazon S3\.
+ **Use Server\-Side Encryption** – You request Amazon S3 to encrypt your object before saving it on disks in its data centers and decrypt it when you download the objects\. 
+ **Use Client\-Side Encryption** – You can encrypt data client\-side and upload the encrypted data to Amazon S3\. In this case, you manage the encryption process, the encryption keys, and related tools\.