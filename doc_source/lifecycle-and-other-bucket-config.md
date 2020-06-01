# Lifecycle and other bucket configurations<a name="lifecycle-and-other-bucket-config"></a>

In addition to S3 Lifecycle configurations, you can associate other configurations with your bucket\. This section explains how S3 Lifecycle configuration relates to other bucket configurations\.

## Lifecycle and versioning<a name="lifecycle-versioning-support-intro"></a>

You can add S3 Lifecycle configurations to unversioned buckets and versioning\-enabled buckets\. For more information, see [Object Versioning](ObjectVersioning.md)\. 

A versioning\-enabled bucket maintains one current object version, and zero or more noncurrent object versions\. You can define separate Lifecycle rules for current and noncurrent object versions\.

For more information, see [Lifecycle configuration elements](intro-lifecycle-rules.md)\. For information about S3 Versioning, see [Object Versioning](ObjectVersioning.md)\.

## Lifecycle configuration on MFA\-enabled buckets<a name="lifecycle-general-considerations-mfa-enabled-bucket"></a>

Lifecycle configuration on multi\-factor authentication \(MFA\)\-enabled buckets is not supported\.

## Lifecycle and logging<a name="lifecycle-general-considerations-logging"></a>

Amazon S3 Lifecycle actions are not captured by AWS CloudTrail object level logging\. CloudTrail captures API requests made to external Amazon S3 endpoints, whereas S3 Lifecycle actions are performed using internal Amazon S3 endpoints\. Amazon S3 server access logs can be enabled in an S3 bucket to capture S3 Lifecycle\-related actions such as object transition to another storage class and object expiration resulting in permanent deletion or logical deletion\. For more information, see [Amazon S3 server access logging](ServerLogs.md)\.

If you have logging enabled on your bucket, Amazon S3 server access logs report the results of the following operations\.


| Operation log | Description | 
| --- | --- | 
|  `S3.EXPIRE.OBJECT`  |  Amazon S3 permanently deletes the object due to the Lifecycle expiration action\.  | 
|  `S3.CREATE.DELETEMARKER`  |  Amazon S3 logically deletes the current version and adds a delete marker in a Versioning enabled bucket\.  | 
|  `S3.TRANSITION_SIA.OBJECT`  |  Amazon S3 transitions the object to the S3 Standard\-IA storage class\.  | 
|  `S3.TRANSITION_ZIA.OBJECT`  |  Amazon S3 transitions the object to the S3 One Zone\-IA storage class\.  | 
|  `S3.TRANSITION_INT.OBJECT`  |  Amazon S3 transitions the object to the Intelligent\-Tiering storage class\.  | 
|  `S3.TRANSITION.OBJECT`  |  Amazon S3 initiates the transition of object to the S3 Glacier storage class\.  | 
|  `S3.TRANSITION_GDA.OBJECT`  |  Amazon S3 initiates the transition of object to the S3 Glacier S3 Glacier Deep Archive storage class\.  | 
|  `S3.DELETE.UPLOAD`  |  Amazon S3 aborts incomplete multipart upload\.  | 

**Note**  
Amazon S3 server access log records are generally delivered on a best effort basis and cannot be used for complete accounting of all Amazon S3 requests\. 

### More info<a name="lifecycle-general-considerations-logging-more-info"></a>
+ [Lifecycle configuration elements](intro-lifecycle-rules.md) 
+ [Transitioning to the S3 Glacier and S3 Glacier Deep Archive storage classes \(object archival\)](lifecycle-transition-general-considerations.md#before-deciding-to-archive-objects)
+ [Setting lifecycle configuration on a bucket](how-to-set-lifecycle-configuration-intro.md) 