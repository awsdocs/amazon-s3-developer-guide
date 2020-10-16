# Amazon S3 on Outposts examples using the AWS CLI<a name="S3OutpostsCLIExamples"></a>

With Amazon S3 on Outposts, you can create S3 buckets on your AWS Outposts and easily store and retrieve objects on\-premises for applications that require local data access, local data processing, and data residency\. You can use S3 on Outposts through the AWS Management Console, AWS CLI, AWS SDKs, or REST API\. For more information, see [Using Amazon S3 on Outposts](https://docs.aws.amazon.com/AmazonS3/latest/dev/S3onOutposts.html)\. 

The following examples show how you can use S3 on Outposts with the AWS Command Line Interface\.

**Topics**
+ [Creating and managing Amazon S3 on Outposts bucket](#S3OutpostsBucketCLI)
+ [Working with objects using Amazon S3 on Outposts](#S3OutpostsObjectCLI)

## Creating and managing Amazon S3 on Outposts bucket<a name="S3OutpostsBucketCLI"></a>

You can use the AWS CLI to create and manage your S3 on Outposts buckets\. From these examples, you can create and get an Outposts bucket, list buckets for an Outpost, create and manage access points, lifecycleconfiguration, and policy for the Outpost bucket\. 

**Topics**
+ [Create an S3 on Outposts bucket](#S3OutpostsCreateBucketCLI)
+ [Get the S3 on Outposts bucket](#S3OutpostsGetBucketCLI)
+ [Get a list of S3 on Outposts buckets](#S3OutpostsListRegionalBucketCLI)
+ [Create an access point for an S3 on Outposts bucket](#S3OutpostsCreateAccessPointCLI)
+ [Get an access point for an S3 on Outposts bucket](#S3OutpostsGetAccessPointCLI)
+ [List access points for an Outpost](#S3OutpostsListAccessPointCLI)
+ [Put a lifecycle configuration for an S3 on Outposts bucket](#S3OutpostsPutBucketLifecycleConfigurationCLI)
+ [Get a lifecycle configuration for an S3 on Outposts bucket](#S3OutpostsGetBucketLifecycleConfigurationCLI)
+ [Put a policy on an S3 on Outposts bucket](#S3OutpostsPutBucketPolicyCLI)
+ [Get a policy for an S3 on Outposts bucket](#S3OutpostsGetBucketPolicyCLI)
+ [Put a policy on an S3 on Outposts access point](#S3OutpostsPutAccessPointPolicyCLI)
+ [Get a policy for an S3 on Outposts access point](#S3OutpostsGetAccessPointPolicyCLI)
+ [Create an endpoint on an Outpost](#S3OutpostsCreateEndpointCLI)
+ [List endpoints for an Outpost](#S3OutpostsListEndpointsCLI)
+ [Delete an endpoint on an Outpost](#S3OutpostsDeleteEndpointCLI)

### Create an S3 on Outposts bucket<a name="S3OutpostsCreateBucketCLI"></a>

The following example creates an S3 on Outposts `s3-outposts:CreateBucket` using the AWS CLI\. 

```
aws s3control create-bucket --bucket example-outpost-bucket --outpost-id op-01ac5d28a6a232904
```

### Get the S3 on Outposts bucket<a name="S3OutpostsGetBucketCLI"></a>

The following S3 on Outposts example gets a bucket using the AWS CLI\. 

```
aws s3control get-bucket --account-id 123456789012 --bucket "arn:aws:s3-outposts:<your-region>:123456789012:outpost/op-01ac5d28a6a232904/bucket/example-outpost-bucket"
```

### Get a list of S3 on Outposts buckets<a name="S3OutpostsListRegionalBucketCLI"></a>

The following AWS CLI example gets a list of buckets in an Outpost\. 

```
aws s3control list-regional-buckets --account-id 123456789012 --outpost-id op-01ac5d28a6a232904
```

### Create an access point for an S3 on Outposts bucket<a name="S3OutpostsCreateAccessPointCLI"></a>

The following AWS CLI example creates an access point for an Outposts bucket\.

```
aws s3control create-access-point --account-id 123456789012 --name example-access-point --bucket "arn:aws:s3-outposts:<your-region>:123456789012:outpost/op-01ac5d28a6a232904/bucket/example-outpost-bucket" --vpc-configuration VpcId=example-vpc-12345
```

### Get an access point for an S3 on Outposts bucket<a name="S3OutpostsGetAccessPointCLI"></a>

The following AWS CLI example gets an access point for an Outposts bucket\.

```
aws s3control get-access-point --account-id 123456789012 --name arn:aws:s3-outposts:<your-region>:123456789012:outpost/op-01ac5d28a6a232904/accesspoint/example-access-point
```

### List access points for an Outpost<a name="S3OutpostsListAccessPointCLI"></a>

The following AWS CLI example List access points for an Outposts bucket\.

```
aws s3control list-access-points --account-id 123456789012 --bucket arn:aws:s3-outposts:<your-region>:123456789012:outpost/op-01ac5d28a6a232904/bucket/example-outpost-bucket
```

### Put a lifecycle configuration for an S3 on Outposts bucket<a name="S3OutpostsPutBucketLifecycleConfigurationCLI"></a>

The following AWS CLI example puts an lifecycle configruations for an Outposts bucket where all objects with the flagged prefix and tags expire after 10 days\.

1. Save the lifecycle configuration policy to a JSON file\.

   ```
   {
       "Rules": [
           {
               "ID": "id-1",
               "Filter": {
                   "And": {
                       "Prefix": "myprefix", 
                       "Tags": [
                           {
                               "Value": "mytagvalue1", 
                               "Key": "mytagkey1"
                           }, 
                           {
                               "Value": "mytagvalue2", 
                               "Key": "mytagkey2"
                           }
                       ]
                   }
               }, 
               "Status": "Enabled", 
               "Expiration": {
                   "Days": 10
               }
           }
       ]
   }
   ```

1. Submit the JSON file as part of the put lifecycle configuration CLI command\.

   ```
   aws s3control put-bucket-lifecycle-configuration --account-id 123456789012 --bucket arn:aws:s3-outposts:<your-region>:123456789012:outpost/op-01ac5d28a6a232904/bucket/example-outpost-bucket --lifecycle-configuration file://lifecycle1.json
   ```

### Get a lifecycle configuration for an S3 on Outposts bucket<a name="S3OutpostsGetBucketLifecycleConfigurationCLI"></a>

The following AWS CLI example gets an access point for an Outposts bucket\.

```
aws s3control get-bucket-lifecycle-configuration --account-id 123456789012 --bucket arn:aws:s3-outposts:<your-region>:123456789012:outpost/op-01ac5d28a6a232904/bucket/example-outpost-bucket
```

### Put a policy on an S3 on Outposts bucket<a name="S3OutpostsPutBucketPolicyCLI"></a>

The following AWS CLI example puts policy for an Outposts bucket\.

1. Save the bucket policy to a JSON file\.

   ```
   {
      "Version":"2012-10-17",
      "Id":"testBucketPolicy",
      "Statement":[
         {
            "Sid":"st1",
            "Effect":"Allow",
            "Principal":{
               "AWS":"123456789012"
            },
            "Action":"s3-outposts:*",
            "Resource":"arn:aws:s3-outposts:<your-region>:123456789012:outpost/op-01ac5d28a6a232904/bucket/example-outpost-bucket"
         }
      ]
   }
   ```

1. Submit the JSON file as part of the put bucket policy CLI command\.

   ```
   aws s3control put-bucket-policy --account-id 123456789012 --bucket arn:aws:s3-outposts:<your-region>:123456789012:outpost/op-01ac5d28a6a232904/bucket/example-outpost-bucket --policy file://policy1.json
   ```

### Get a policy for an S3 on Outposts bucket<a name="S3OutpostsGetBucketPolicyCLI"></a>

The following AWS CLI example gets a policy for an Outposts bucket\.

```
aws s3control get-bucket-policy --account-id 123456789012 --bucket arn:aws:s3-outposts:<your-region>:123456789012:outpost/op-01ac5d28a6a232904/bucket/example-outpost-bucket
```

### Put a policy on an S3 on Outposts access point<a name="S3OutpostsPutAccessPointPolicyCLI"></a>

The following AWS CLI example puts policy for an Outposts bucket\.

1. Save the access point policy to a JSON file\.

   ```
   {
      "Version":"2012-10-17",
      "Id":"testBucketPolicy",
      "Statement":[
         {
            "Sid":"st1",
            "Effect":"Allow",
            "Principal":{
               "AWS":"123456789012"
            },
            "Action":"s3-outposts:*",
            "Resource":"arn:aws:s3-outposts:<your-region>:123456789012:outpost/ec2/bucket/example-outpost-bucket"
         }
      ]
   }
   ```

1. Submit the JSON file as part of the put bucket policy CLI command\.

   ```
   aws s3control put-access-point-policy --account-id 123456789012 --name arn:aws:s3-outposts:<your-region>:123456789012:outpost/op-01ac5d28a6a232904/accesspoint/example-access-point --policy file://appolicy1.json
   ```

### Get a policy for an S3 on Outposts access point<a name="S3OutpostsGetAccessPointPolicyCLI"></a>

The following AWS CLI example gets a policy for an Outposts bucket\.

```
aws s3control get-access-point-policy --account-id 123456789012 --name arn:aws:s3-outposts:<your-region>:123456789012:outpost/op-01ac5d28a6a232904/accesspoint/example-access-point
```

### Create an endpoint on an Outpost<a name="S3OutpostsCreateEndpointCLI"></a>

The following AWS CLI example creates an endpoint for an Outpost\.

```
aws s3outposts create-endpoint --outpost-id op-01ac5d28a6a232904 --subnet-id subnet-8c7a57c5 —-security-group-id sg-ab19e0d1
```

### List endpoints for an Outpost<a name="S3OutpostsListEndpointsCLI"></a>

The following AWS CLI example lists endpoints for an Outpost\.

```
aws s3outposts list-endpoints 
```

### Delete an endpoint on an Outpost<a name="S3OutpostsDeleteEndpointCLI"></a>

The following AWS CLI example creates an endpoint for an Outpost\.

```
aws s3outposts delete-endpoint --endpoint-id m3kvngonpdx2kadiusm --outpost-id op-01ac5d28a6a232904 
```

## Working with objects using Amazon S3 on Outposts<a name="S3OutpostsObjectCLI"></a>

You can use the AWS CLI to put and manage your S3 on Outposts objects\. From these examples, you can put objects and get objects from an Outpost bucket\. 

**Topics**
+ [Put an object in to an S3 on Outposts bucket](#S3OutpostsPutObjectCLI)
+ [Get the S3 on Outposts bucket](#S3OutpostsGetObjectCLI)
+ [List objects in an S3 on Outposts bucket](#S3OutpostsListObjectsCLI)

### Put an object in to an S3 on Outposts bucket<a name="S3OutpostsPutObjectCLI"></a>

The following example put an S3 on Outposts `s3-outposts:PutObject` using the AWS CLI\. 

```
aws s3api put-object --bucket arn:aws:s3-outposts:<your-region>:123456789012:outpost/op-01ac5d28a6a232904/accesspoint/example-access-point --key testkey --body sample-object.xml
```

### Get the S3 on Outposts bucket<a name="S3OutpostsGetObjectCLI"></a>

The following S3 on Outposts example gets a bucket using the AWS CLI\. 

```
aws s3api get-object --bucket arn:aws:s3-outposts:<your-region>:123456789012:outpost/op-01ac5d28a6a232904/accesspoint/example-access-point --key testkey sample-object.xml
```

### List objects in an S3 on Outposts bucket<a name="S3OutpostsListObjectsCLI"></a>

The following example list S3 on Outposts `s3-outposts:ListObejectsV2` using the AWS CLI\. 

```
aws s3api list-objects-v2 --bucket arn:aws:s3-outposts:us-west-2:123456789012:outpost/op-01ac5d28a6a232904/accesspoint/example-access-point
```
