# Protecting Data in Amazon S3<a name="DataDurability"></a>

**Topics**
+ [Protecting Data Using Encryption](UsingEncryption.md)
+ [Using Versioning](Versioning.md)

Amazon S3 provides a highly durable storage infrastructure designed for mission\-critical and primary data storage\. Objects are redundantly stored on multiple devices across multiple facilities in an Amazon S3 region\. To help better ensure data durability, Amazon S3 `PUT` and `PUT Object copy` operations synchronously store your data across multiple facilities before returning `SUCCESS`\. Once the objects are stored, Amazon S3 maintains their durability by quickly detecting and repairing any lost redundancy\. 

Amazon S3 also regularly verifies the integrity of data stored using checksums\. If Amazon S3 detects data corruption, it is repaired using redundant data\. In addition, Amazon S3 calculates checksums on all network traffic to detect corruption of data packets when storing or retrieving data\. 

Amazon S3's standard storage is: 
+ Backed with the [Amazon S3 Service Level Agreement](https://aws.amazon.com/s3/sla/)
+ Designed to provide 99\.999999999% durability and 99\.99% availability of objects over a given year
+ Designed to sustain the concurrent loss of data in two facilities 

Amazon S3 further protects your data using versioning\. You can use versioning to preserve, retrieve, and restore every version of every object stored in your Amazon S3 bucket\. With versioning, you can easily recover from both unintended user actions and application failures\. By default, requests retrieve the most recently written version\. You can retrieve older versions of an object by specifying a version of the object in a request\. 