# Lifecycle and Other Bucket Configurations<a name="lifecycle-and-other-bucket-config"></a>

In addition to lifecycle configurations, you can associate other configurations with your bucket\. This section explains how lifecycle configuration relates to other bucket configurations\.

## Lifecycle and Versioning<a name="lifecycle-versioning-support-intro"></a>

You can add lifecycle configurations to unversioned buckets and versioning\-enabled buckets\. For more information, see [Object Versioning](ObjectVersioning.md)\. 

A versioning\-enabled bucket maintains one current object version, and zero or more noncurrent object versions\. You can define separate lifecycle rules for current and noncurrent object versions\.

For more information, see [Lifecycle Configuration Elements](intro-lifecycle-rules.md)\. For information about versioning, see [Object Versioning](ObjectVersioning.md)\.

## Lifecycle Configuration on MFA\-enabled Buckets<a name="lifecycle-general-considerations-mfa-enabled-bucket"></a>

Lifecycle configuration on MFA\-enabled buckets is not supported\.

## Lifecycle and Logging<a name="lifecycle-general-considerations-logging"></a>

Amazon S3 lifecycle actions are not captured by CloudTrail object level logging since CloudTrail captures API requests made to external Amazon S3 endpoints whereas Amazon S3 lifecycle actions are performed using internal Amazon S3 endpoints\. Amazon S3 server access logs can be enabled in an S3 bucket to capture Amazon S3 lifecycle related actions such as object transition to another storage class and object expiration resulting in permanent deletion or logical deletion\. For more information, see [Amazon S3 Server Access Logging](ServerLogs.md)

If you have logging enabled on your bucket, Amazon S3 server access logs report the results of the following operations:


| Operation log | Decription | 
| --- | --- | 
|  `S3.EXPIRE.OBJECT`  |  Amazon S3 permanently deletes the object due to the lifecycle expiration action\.  | 
|  `S3.CREATE.DELETEMARKER`  |  Amazon S3 logically deletes the current version and adds a delete marker in a versioning enabled bucket\.  | 
|  `S3.TRANSITION_SIA.OBJECT`  |  Amazon S3 transitions the object to the STANDARD\_IA storage class\.  | 
|  `S3.TRANSITION_ZIA.OBJECT`  |  Amazon S3 transitions the object to the ONEZONE\_IA storage class\.  | 
|  `S3.TRANSITION_INT.OBJECT`  |  Amazon S3 transitions the object to the Intelligent\-Tiering storage class\.  | 
|  `S3.TRANSITION.OBJECT`  |  Amazon S3 initiates the transition of object to the GLACIER storage class\.  | 
|  `S3.TRANSITION_GDA.OBJECT`  |  Amazon S3 initiates the transition of object to the GLACIER DEEP ARCHIVE storage class\.  | 
|  `S3.DELETE.UPLOAD`  |  Amazon S3 aborts incomplete multipart upload\.  | 

**Note**  
Amazon S3 server access log records are generally delivered on a best effort basis and cannot be used for complete accounting of all Amazon S3 requests\. 

### More Info<a name="lifecycle-general-considerations-logging-more-info"></a>
+ [Lifecycle Configuration Elements](intro-lifecycle-rules.md) 
+ [Transitioning to the GLACIER and DEEP ARCHIVE Storage Classes \(Object Archival\)](lifecycle-transition-general-considerations.md#before-deciding-to-archive-objects)
+ [Setting Lifecycle Configuration on a Bucket](how-to-set-lifecycle-configuration-intro.md) 