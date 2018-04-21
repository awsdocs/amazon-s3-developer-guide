# Lifecycle and Other Bucket Configurations<a name="lifecycle-and-other-bucket-config"></a>

In addition to lifecycle configurations, you can associate other configurations with your bucket\. This section explains how lifecycle configuration relates to other bucket configurations\.

## Lifecycle and Versioning<a name="lifecycle-versioning-support-intro"></a>

You can add lifecycle configurations to unversioned buckets and versioning\-enabled buckets\. For more information, see [Object Versioning](ObjectVersioning.md)\. 

A versioning\-enabled bucket maintains one current object version, and zero or more noncurrent object versions\. You can define separate lifecycle rules for current and noncurrent object versions\.

For more information, see [Lifecycle Configuration Elements](intro-lifecycle-rules.md)\. For information about versioning, see [Object Versioning](ObjectVersioning.md)\.

## Lifecycle Configuration on MFA\-enabled Buckets<a name="lifecycle-general-considerations-mfa-enabled-bucket"></a>

Lifecycle configuration on MFA\-enabled buckets is not supported\.

## Lifecycle and Logging<a name="lifecycle-general-considerations-logging"></a>

If you have logging enabled on your bucket, Amazon S3 reports the results of an expiration action as follows: 
+ If the lifecycle expiration action results in Amazon S3 permanently removing the object, Amazon S3 reports it as an `S3.EXPIRE.OBJECT` operation in the log record\.
+ For a versioning\-enabled bucket, if the lifecycle expiration action results in a logical deletion of the current version, in which Amazon S3 adds a delete marker, Amazon S3 reports the logical deletion as an `S3.CREATE.DELETEMARKER` operation in the log record\. For more information, see [Object Versioning](ObjectVersioning.md)\.
+ When Amazon S3 transitions an object to the GLACIER storage class, it reports it as an operation `S3.TRANSITION.OBJECT` in the log record to indicate it has initiated the operation\. When the object is transitioned to the STANDARD\_IA \(or ONEZONE\_IA\) storage class, it is reported as an `S3.TRANSITION_SIA.OBJECT` \(or `S3.TRANSITION_ZIA.OBJECT`\) operation\. 

### More Info<a name="lifecycle-general-considerations-logging-more-info"></a>
+ [Lifecycle Configuration Elements](intro-lifecycle-rules.md) 
+ [Transitioning to the GLACIER Storage Class \(Object Archival\)](lifecycle-transition-general-considerations.md#before-deciding-to-archive-objects)
+ [Setting Lifecycle Configuration on a Bucket](how-to-set-lifecycle-configuration-intro.md) 