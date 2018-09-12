# Using Versioning<a name="Versioning"></a>

Versioning is a means of keeping multiple variants of an object in the same bucket\. You can use versioning to preserve, retrieve, and restore every version of every object stored in your Amazon S3 bucket\. With versioning, you can easily recover from both unintended user actions and application failures\. 

In one bucket, for example, you can have two objects with the same key, but different version IDs, such as `photo.gif` \(version 111111\) and `photo.gif `\(version 121212\)\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/versioning_Enabled.png)

Versioning\-enabled buckets enable you to recover objects from accidental deletion or overwrite\. For example:
+ If you delete an object, instead of removing it permanently, Amazon S3 inserts a delete marker, which becomes the current object version\. You can always restore the previous version\. For more information, see [Deleting Object Versions](DeletingObjectVersions.md)\.
+ If you overwrite an object, it results in a new object version in the bucket\. You can always restore the previous version\.

**Important**  
If you have an object expiration lifecycle policy in your non\-versioned bucket and you want to maintain the same permanent delete behavior when you enable versioning, you must add a noncurrent expiration policy\. The noncurrent expiration lifecycle policy will manage the deletes of the noncurrent object versions in the version\-enabled bucket\. \(A version\-enabled bucket maintains one current and zero or more noncurrent object versions\.\) For more information, see [ How Do I Create a Lifecycle Policy for an S3 Bucket?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-lifecycle.html) in the *Amazon Simple Storage Service Console User Guide*\. 

Buckets can be in one of three states: unversioned \(the default\), versioning\-enabled, or versioning\-suspended\.

**Important**  
Once you version\-enable a bucket, it can never return to an unversioned state\. You can, however, suspend versioning on that bucket\.

The versioning state applies to all \(never some\) of the objects in that bucket\. The first time you enable a bucket for versioning, objects in it are thereafter always versioned and given a unique version ID\. Note the following:
+ Objects stored in your bucket before you set the versioning state have a version ID of `null`\. When you enable versioning, existing objects in your bucket do not change\. What changes is how Amazon S3 handles the objects in future requests\. For more information, see [Managing Objects in a Versioning\-Enabled Bucket](manage-objects-versioned-bucket.md)\.
+ The bucket owner \(or any user with appropriate permissions\) can suspend versioning to stop accruing object versions\. When you suspend versioning, existing objects in your bucket do not change\. What changes is how Amazon S3 handles objects in future requests\. For more information, see [Managing Objects in a Versioning\-Suspended Bucket](VersionSuspendedBehavior.md)\.

## How to Configure Versioning on a Bucket<a name="how-to-enable-disable-versioning-intro"></a>

You can configure bucket versioning using any of the following methods:
+ Configure versioning using the Amazon S3 console\.
+ Configure versioning programmatically using the AWS SDKs\.

  Both the console and the SDKs call the REST API that Amazon S3 provides to manage versioning\. 
**Note**  
If you need to, you can also make the Amazon S3 REST API calls directly from your code\. However, this can be cumbersome because it requires you to write code to authenticate your requests\. 

  Each bucket you create has a *versioning* subresource \(see [Bucket Configuration Options](UsingBucket.md#bucket-config-options-intro)\) associated with it\. By default, your bucket is unversioned, and accordingly the versioning subresource stores empty versioning configuration\.

  ```
  <VersioningConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/"> 
  </VersioningConfiguration>
  ```

  To enable versioning, you send a request to Amazon S3 with a versioning configuration that includes a status\. 

  ```
  <VersioningConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/"> 
    <Status>Enabled</Status> 
  </VersioningConfiguration>
  ```

  To suspend versioning, you set the status value to `Suspended`\. 

The bucket owner, an AWS account that created the bucket \(root account\), and authorized users can configure the versioning state of a bucket\. For more information about permissions, see [Managing Access Permissions to Your Amazon S3 Resources](s3-access-control.md)\. 

For an example of configuring versioning, see [Examples of Enabling Bucket Versioning](manage-versioning-examples.md)\.

## MFA Delete<a name="MultiFactorAuthenticationDelete"></a>

You can optionally add another layer of security by configuring a bucket to enable MFA \(multi\-factor authentication\) Delete, which requires additional authentication for either of the following operations:
+ Change the versioning state of your bucket
+ Permanently delete an object version

 MFA Delete requires two forms of authentication together:
+ Your security credentials
+ The concatenation of a valid serial number, a space, and the six\-digit code displayed on an approved authentication device

MFA Delete thus provides added security in the event, for example, your security credentials are compromised\. 

To enable or disable MFA Delete, you use the same API that you use to configure versioning on a bucket\. Amazon S3 stores the MFA Delete configuration in the same *versioning* subresource that stores the bucket's versioning status\.

```
<VersioningConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/"> 
  <Status>VersioningState</Status>
  <MfaDelete>MfaDeleteState</MfaDelete>  
</VersioningConfiguration>
```

To use MFA Delete, you can use either a hardware or virtual MFA device to generate an authentication code\. The following example shows a generated authentication code displayed on a hardware device\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/MFADevice.png)

**Note**  
MFA Delete and MFA\-protected API access are features intended to provide protection for different scenarios\. You configure MFA Delete on a bucket to ensure that data in your bucket cannot be accidentally deleted\. MFA\-protected API access is used to enforce another authentication factor \(MFA code\) when accessing sensitive Amazon S3 resources\. You can require any operations against these Amazon S3 resources be done with temporary credentials created using MFA\. For an example, see [Adding a Bucket Policy to Require MFA](example-bucket-policies.md#example-bucket-policies-use-case-7)\.

For more information on how to purchase and activate an authentication device, see [https://aws\.amazon\.com/iam/details/mfa/](https://aws.amazon.com/iam/details/mfa/)\. 

**Note**  
The bucket owner, the AWS account that created the bucket \(root account\), and all authorized IAM users can enable versioning, but only the bucket owner \(root account\) can enable MFA Delete\.

## Related Topics<a name="versioning-related-topics"></a>

For more information, see the following topics:
+ [Examples of Enabling Bucket Versioning](manage-versioning-examples.md)
+ [Managing Objects in a Versioning\-Enabled Bucket](manage-objects-versioned-bucket.md)
+ [Managing Objects in a Versioning\-Suspended Bucket](VersionSuspendedBehavior.md)
+ [Significant Increases in HTTP 503 Responses to Amazon S3 Requests to Buckets with Versioning Enabled](troubleshooting.md#troubleshooting-by-symptom-increase-503-reponses) 