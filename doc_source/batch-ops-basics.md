# The basics: S3 Batch Operations<a name="batch-ops-basics"></a>

You can use S3 Batch Operations to perform large\-scale batch operations on Amazon S3 objects\. S3 Batch Operations can run a single operation or action on lists of Amazon S3 objects that you specify\. 

**Topics**
+ [How an S3 Batch Operations job works](#batch-ops-basics-how-it-works)
+ [Specifying a manifest](#specify-batchjob-manifest)

## How an S3 Batch Operations job works<a name="batch-ops-basics-how-it-works"></a>

A job is the basic unit of work for S3 Batch Operations\. A job contains all of the information necessary to run the specified operation on a list of objects\.

To create a job, you give S3 Batch Operations a list of objects and specify the action to perform on those objects\. S3 Batch Operations support the following operations:
+ [PUT copy object](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html)
+ [PUT object tagging](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTtagging.html)
+ [PUT object ACL](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTacl.html)
+ [Initiate S3 Glacier restore](https://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOSTrestore.html)
+ [Invoke an AWS Lambda function](https://docs.aws.amazon.com/lambda/latest/dg/API_Invoke.html)

The objects that you want a job to act on are listed in a manifest object\. A job performs the specified operation on each object that is included in its manifest\. You can use a CSV\-formatted [ Amazon S3 inventory](storage-inventory.md) report as a manifest, which makes it easy to create large lists of objects located in a bucket\. You can also specify a manifest in a simple CSV format that enables you to perform batch operations on a customized list of objects contained within a single bucket\. 

After you create a job, Amazon S3 processes the list of objects in the manifest and runs the specified operation against each object\. While a job is executing, you can monitor its progress programmatically or through the Amazon S3 console\. You can also configure a job to generate a completion report when it finishes\. The completion report describes the results of each task that was performed by the job\. For more information about monitoring jobs, see [Managing S3 Batch Operations jobs](batch-ops-managing-jobs.md)\.

## Specifying a manifest<a name="specify-batchjob-manifest"></a>

 A manifest is an Amazon S3 object that lists object keys that you want Amazon S3 to act upon\. To create a manifest for a job, you specify the manifest object key, ETag, and optional version ID\. The contents of the manifest must be URL encoded\. Manifests that use server\-side encryption with customer\-provided keys \(SSE\-C\) and server\-side encryption with AWS Key Management Service \(SSE\-KMS\) customer master keys \(CMKs\) are not supported\. Your manifest must contain the bucket name, object key, and optionally, the object version for each object\. Any other fields in the manifest are not used by S3 Batch Operations\. 

You can specify a manifest in a create job request using one of the following two formats\.
+ Amazon S3 inventory report — Must be a CSV\-formatted Amazon S3 inventory report\. You must specify the `manifest.json` file that is associated with the inventory report\. For more information about inventory reports, see [ Amazon S3 inventory](storage-inventory.md)\. If the inventory report includes version IDs, S3 Batch Operations operate on the specific object versions\.
**Note**  
S3 Batch Operations supports CSV inventory reports that are AWS KMS\-encrypted\.
+ CSV file — Each row in the file must include the bucket name, object key, and optionally, the object version\. Object keys must be URL\-encoded, as shown in the following examples\. The manifest must either include version IDs for all objects or omit version IDs for all objects\. For more information about the CSV manifest format, see [JobManifestSpec](https://docs.aws.amazon.com/AmazonS3/latest/API/API_control_JobManifestSpec.html) in the *Amazon Simple Storage Service API Reference*\.
**Note**  
S3 Batch Operations does not support CSV manifest files that are AWS KMS\-encrypted\.

  The following is an example manifest in CSV format without version IDs\.

  ```
  Examplebucket,objectkey1
  Examplebucket,objectkey2
  Examplebucket,objectkey3
  Examplebucket,photos/jpgs/objectkey4
  Examplebucket,photos/jpgs/newjersey/objectkey5
  Examplebucket,object%20key%20with%20spaces
  ```

  The following is an example manifest in CSV format including version IDs\.

  ```
  Examplebucket,objectkey1,PZ9ibn9D5lP6p298B7S9_ceqx1n5EJ0p
  Examplebucket,objectkey2,YY_ouuAJByNW1LRBfFMfxMge7XQWxMBF
  Examplebucket,objectkey3,jbo9_jhdPEyB4RrmOxWS0kU0EoNrU_oI
  Examplebucket,photos/jpgs/objectkey4,6EqlikJJxLTsHsnbZbSRffn24_eh5Ny4
  Examplebucket,photos/jpgs/newjersey/objectkey5,imHf3FAiRsvBW_EHB8GOu.NHunHO1gVs
  Examplebucket,object%20key%20with%20spaces,9HkPvDaZY5MVbMhn6TMn1YTb5ArQAo3w
  ```

**Important**  
If the objects in your manifest are in a versioned bucket, you should specify the version IDs for the objects\. When you create a job, S3 Batch Operations parse the entire manifest before running the job\. However, it doesn't take a "snapshot" of the state of the bucket\.   
Because manifests can contain billions of objects, jobs might take a long time to run\. If you overwrite an object with a new version while a job is running, and you didn't specify a version ID for that object, Amazon S3 performs the operation on the latest version of the object, and not the version that existed when you created the job\. The only way to avoid this behavior is to specify version IDs for the objects that are listed in the manifest\. 