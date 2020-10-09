# Data protection in Amazon S3<a name="DataDurability"></a>

Amazon S3 provides a highly durable storage infrastructure designed for mission\-critical and primary data storage\. Objects are redundantly stored on multiple devices across multiple facilities in an Amazon S3 Region\. To help better ensure data durability, Amazon S3 `PUT` and `PUT Object copy` operations synchronously store your data across multiple facilities\. After the objects are stored, Amazon S3 maintains their durability by quickly detecting and repairing any lost redundancy\. 

Amazon S3 standard storage offers the following features: 
+ Backed with the [Amazon S3 Service Level Agreement](https://aws.amazon.com/s3/sla/)
+ Designed to provide 99\.999999999% durability and 99\.99% availability of objects over a given year
+ Designed to sustain the concurrent loss of data in two facilities 

Amazon S3 further protects your data using versioning\. You can use versioning to preserve, retrieve, and restore every version of every object that is stored in your Amazon S3 bucket\. With versioning, you can easily recover from both unintended user actions and application failures\. By default, requests retrieve the most recently written version\. You can retrieve older versions of an object by specifying a version of the object in a request\. 

For data protection purposes, we recommend that you protect AWS account credentials and set up individual user accounts with AWS Identity and Access Management \(IAM\), so that each user is given only the permissions necessary to fulfill their job duties\.

If you require FIPS 140\-2 validated cryptographic modules when accessing AWS through a command line interface or an API, use a FIPS endpoint\. For more information about the available FIPS endpoints, see [Federal Information Processing Standard \(FIPS\) 140\-2](http://aws.amazon.com/compliance/fips/)\.

The following security best practices also address data protection in Amazon S3:
+ [Implement server-side encryption](security-best-practices.md#server-side)
+ [Enforce encryption of data in transit](security-best-practices.md#transit)
+ [Consider using Amazon Macie with Amazon S3](security-best-practices.md#macie)
+ [Identify and audit all your Amazon S3 buckets](security-best-practices.md#audit)
+ [Monitor AWS security advisories](security-best-practices.md#advisories)