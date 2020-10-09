# Bucket owner condition<a name="bucket-owner-condition"></a>

Amazon S3 bucket owner condition ensures that the buckets you use in your S3 operations belong to the AWS accounts that you expect\.

Most S3 operations read from or write to specific S3 buckets\. These operations include uploading, copying, and downloading objects, retrieving or modifying bucket configurations, and retrieving or modifying object configurations\. When you perform these operations, you specify the bucket that you want to use by including its name with the request\. For example, to retrieve an object from S3, you make a request that specifies the name of a bucket and the object key to retrieve from that bucket\.

Because Amazon S3 identifies buckets based on their names, an application that uses an incorrect bucket name in a request could inadvertently perform operations against a different bucket than expected\. To help avoid unintentional bucket interactions in situations like this, you can use *bucket owner condition*\. Bucket owner condition enables you to verify that the target bucket is owned by the expected AWS account, providing an additional layer of assurance that your S3 operations are having the effects you intend\.

**Topics**
+ [When to use bucket owner condition](#bucket-owner-condition-when-to-use)
+ [Verifying a bucket owner](#bucket-owner-condition-use)
+ [Examples](#bucket-owner-condition-examples)
+ [Restrictions and limitations](#bucket-owner-condition-restrictions-limitations)

## When to use bucket owner condition<a name="bucket-owner-condition-when-to-use"></a>

We recommend using bucket owner condition whenever you perform a supported S3 operation and know the account ID of the expected bucket owner\. Bucket owner condition is available for all S3 object operations and most S3 bucket operations\. For a list of S3 operations that don't support bucket owner condition, see [Restrictions and limitations](#bucket-owner-condition-restrictions-limitations)\.

To see the benefit of using bucket owner condition, consider the following scenario involving AWS customer Bea:

1. Bea develops an application that uses Amazon S3\. During development, Bea uses her testing\-only AWS account to create a bucket named `bea-data-test`, and configures her application to make requests to `bea-data-test`\.

1. Bea deploys her application, but forgets to reconfigure the application to use a bucket in her production AWS account\.

1. In production, Bea's application makes requests to `bea-data-test`, which succeed\. This results in production data being written to the bucket in Bea's test account\.

Bea can help protect against situations like this by using bucket owner condition\. With bucket owner condition, Bea can include the AWS account ID of the expected bucket owner in her requests\. Amazon S3 then checks the account ID of the bucket owner before processing each request\. If the actual bucket owner doesn't match the expected bucket owner, the request fails\.

If Bea uses bucket owner condition, the scenario described earlier won't result in Bea's application inadvertently writing to a test bucket\. Instead, the requests that her application makes at step 3 will fail with a descriptive error message\. By using bucket owner condition, Bea helps eliminate the risk of accidentally interacting with buckets in the wrong AWS account\.

## Verifying a bucket owner<a name="bucket-owner-condition-use"></a>

To use bucket owner condition, you include a parameter with your request that specifies the expected bucket owner\. Most S3 operations involve only a single bucket, and require only this single parameter to use bucket owner condition\. For `CopyObject` operations, this first parameter specifies the expected owner of the destination bucket, and you include a second parameter to specify the expected owner of the source bucket\.

When you make a request that includes a bucket owner condition parameter, S3 checks the account ID of the bucket owner against the specified parameter before processing the request\. If the parameter matches the bucket owner's account ID, S3 processes the request\. If the parameter doesn't match the bucket owner's account ID, the request fails with a descriptive error message\.

You can use bucket owner condition with the AWS Command Line Interface \(AWS CLI\), AWS SDKs, and Amazon S3 REST APIs\. When using bucket owner condition with the AWS CLI and Amazon S3 REST APIs, use the following parameter names\.


****  

| Access method | Parameter for non\-copy operations | Copy operation source parameter | Copy operation destination parameter | 
| --- | --- | --- | --- | 
| AWS CLI | \-\-expected\-bucket\-owner | \-\-expected\-source\-bucket\-owner | \-\-expected\-bucket\-owner | 
| Amazon S3 REST APIs | x\-amz\-expected\-bucket\-owner header | x\-amz\-source\-expected\-bucket\-owner header | x\-amz\-expected\-bucket\-owner header | 

The parameter names that are required to use bucket owner condition with the AWS SDKs vary depending on the language\. To determine the required parameters, see the SDK documentation for your desired language\. You can find the SDK documentation at [Tools to Build on AWS](https://aws.amazon.com/tools/)\.

## Examples<a name="bucket-owner-condition-examples"></a>

The following examples show how you can implement bucket owner condition in Amazon S3 using the AWS CLI or the AWS SDK for Java 2\.x\.

**Example**  
***Example: Upload an object***  
The following example uploads an object to S3 bucket `DOC-EXAMPLE-BUCKET1`, using bucket owner condition to ensure that `DOC-EXAMPLE-BUCKET1` is owned by AWS account `111122223333`\.  

```
aws s3api put-object \
                 --bucket DOC-EXAMPLE-BUCKET1 --key exampleobject --body example_file.txt \
                 --expected-bucket-owner 111122223333
```

```
public void putObjectExample() {
    S3Client s3Client = S3Client.create();;
    PutObjectRequest request = PutObjectRequest.builder()
            .bucket("DOC-EXAMPLE-BUCKET1")
            .key("exampleobject")
            .expectedBucketOwner("111122223333")
            .build();
    Path path = Paths.get("example_file.txt");
    s3Client.putObject(request, path);
}
```

**Example**  
***Example: Copy an object***  
The following example copies the object `object1` from S3 bucket `DOC-EXAMPLE-BUCKET1` to S3 bucket `DOC-EXAMPLE-BUCKET2`\. It uses bucket owner condition to ensure that the buckets are owned by the expected accounts according to the following table\.   


****  

| Bucket | Expected owner | 
| --- | --- | 
| DOC\-EXAMPLE\-BUCKET1 | 111122223333 | 
| DOC\-EXAMPLE\-BUCKET2 | 444455556666 | 

```
aws s3api copy-object --copy-source DOC-EXAMPLE-BUCKET1/object1 \
                            --bucket DOC-EXAMPLE-BUCKET2 --key object1copy \
                            --expected-source-bucket-owner 111122223333 --expected-bucket-owner 444455556666
```

```
public void copyObjectExample() {
        S3Client s3Client = S3Client.create();
        CopyObjectRequest request = CopyObjectRequest.builder()
                .copySource("DOC-EXAMPLE-BUCKET1/object1")
                .destinationBucket("DOC-EXAMPLE-BUCKET2")
                .destinationKey("object1copy")
                .expectedSourceBucketOwner("111122223333")
                .expectedBucketOwner("444455556666")
                .build();
        s3Client.copyObject(request);
    }
```

**Example**  
***Example: Retrieve a bucket policy***  
The following example retrieves the access policy for S3 bucket `DOC-EXAMPLE-BUCKET1`, using bucket owner condition to ensure that `DOC-EXAMPLE-BUCKET1` is owned by AWS account `111122223333`\.  

```
aws s3api get-bucket-policy --bucket DOC-EXAMPLE-BUCKET1 --expected-bucket-owner 111122223333
```

```
public void getBucketPolicyExample() {
    S3Client s3Client = S3Client.create();
    GetBucketPolicyRequest request = GetBucketPolicyRequest.builder()
            .bucket("DOC-EXAMPLE-BUCKET1")
            .expectedBucketOwner("111122223333")
            .build();
    try {
        GetBucketPolicyResponse response = s3Client.getBucketPolicy(request);
    }
    catch (S3Exception e) {
        // The call was transmitted successfully, but Amazon S3 couldn't process 
        // it, so it returned an error response.
        e.printStackTrace();
    }
}
```

## Restrictions and limitations<a name="bucket-owner-condition-restrictions-limitations"></a>

Amazon S3 bucket owner condition has the following restrictions and limitations:
+ The value of the bucket owner condition parameter must be an AWS account ID \(12\-digit alphanumeric string\)\. Service principals aren't supported\. 
+ Bucket owner condition isn't available for [CreateBucket](https://docs.aws.amazon.com/AmazonS3/latest/API/API_CreateBucket.html), [ListBuckets](https://docs.aws.amazon.com/AmazonS3/latest/API/API_ListBuckets.html), or any of the operations included in [AWS S3 Control](https://docs.aws.amazon.com/AmazonS3/latest/API/API_Operations_AWS_S3_Control.html)\. Amazon S3 ignores any bucket owner condition parameters included with requests to these operations\. 
+ Bucket owner condition only verifies that the account specified in the verification parameter owns the bucket\. Bucket owner condition doesn't check the configuration of the bucket\. It also doesn't guarantee that the bucket's configuration meets any specific conditions or matches any past state\. 