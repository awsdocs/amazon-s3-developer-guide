# Invoking a Lambda Function from Amazon S3 Batch Operations<a name="batch-ops-invoke-lambda"></a>

Amazon S3 batch operations can invoke AWS Lambda functions to perform custom actions on objects that are listed in a manifest\. This section describes how to create a Lambda function to use with Amazon S3 batch operations and how to create a job to invoke the function\. The Amazon S3 batch operations job uses the `LambdaInvoke` operation to run a Lambda function on each object listed in a manifest\. 

You can work with Amazon S3 batch operations for Lambda using the AWS Management Console, AWS Command Line Interface \(AWS CLI\), AWS SDKs, or REST APIs\. For more information about using Lambda, see [ Getting Started with AWS Lambda](https://docs.aws.amazon.com/lambda/latest/dg/getting-started.html) in the *AWS Lambda Developer Guide*\. 

The following topics explain how you can get started using Amazon S3 batch operations with Lambda\. 

**Topics**
+ [Using Lambda with Amazon S3 Batch Operations](#batch-ops-invoke-lambda-using)
+ [Creating a Lambda Function to Use with Amazon S3 Batch Operations](#batch-ops-invoke-lambda-custom-functions)
+ [Creating an Amazon S3 Batch Operations Job That Invokes a Lambda Function](#batch-ops-invoke-lambda-create-job)

## Using Lambda with Amazon S3 Batch Operations<a name="batch-ops-invoke-lambda-using"></a>

When using Amazon S3 batch operations with AWS Lambda, you must create new Lambda functions specifically for use with Amazon S3 batch operations\. You can't reuse existing Amazon S3 event\-based functions with Amazon S3 batch operations\. Event functions can only receive messages; they don't return messages\. The Lambda functions that are used with Amazon S3 batch operations must accept and return messages\. For more information about using Lambda with Amazon S3 events, see [Using AWS Lambda with Amazon S3](https://docs.aws.amazon.com/lambda/latest/dg/with-s3.html) in the *AWS Lambda Developer Guide*\.

You create an Amazon S3 batch operations job that invokes your Lambda function\. The job runs the same Lambda function on all of the objects listed in your manifest\. You can control what versions of your Lambda function to use while processing the objects in your manifest\. Amazon S3 batch operations support unqualified Amazon Resource Names \(ARNs\), aliases, and specific versions\. For more information, see [ Introduction to AWS Lambda Versioning](https://docs.aws.amazon.com/lambda/latest/dg/versioning-intro.html) in the *AWS Lambda Developer Guide*\.

If you provide the Amazon S3 batch operations job with a function ARN that uses an alias or the `$LATEST` qualifier, and you update what version either of those points to, Amazon S3 batch operations will start calling the new version of your Lambda function\. This can be useful when you want to update functionality part of the way through a large job\. If you want Amazon S3 batch operations not to change the version that is used, provide the specific version in the `FunctionARN` parameter when you create your job\.

### Response and Result Codes<a name="batch-ops-invoke-lambda-response-codes"></a>

There are two levels of codes that Amazon S3 batch operations expect from Lambda functions\. The first is the response code for the entire request, and the second is a per\-task result code\. The following table contains the response codes\.


****  

| Response Code | Description | 
| --- | --- | 
| Succeeded | The task completed normally\. If you requested a job completion report, the task's result string is included in the report\. | 
| TemporaryFailure | The task suffered a temporary failure and will be redriven before the job completes\. The result string is ignored\. If this is the final redrive, the error message is included in the final report\. | 
| PermanentFailure | The task suffered a permanent failure\. If you requested a job\-completion report, the task is marked as Failed and includes the error message string\. Result strings from failed tasks are ignored\. | 

## Creating a Lambda Function to Use with Amazon S3 Batch Operations<a name="batch-ops-invoke-lambda-custom-functions"></a>

This section provides example AWS Identity and Access Management \(IAM\) permissions that you must use with your Lambda function and an example Lambda function to use with Amazon S3 batch operations\. If you have never created a Lambda function before, we recommend the following tutorial[ Using AWS Lambda with Amazon S3](https://docs.aws.amazon.com/lambda/latest/dg/with-s3-example.html) in the *AWS Lambda Developer Guide*\.

You must create Lambda functions specifically for use with Amazon S3 batch operations\. You can't reuse existing Amazon S3 event\-based Lambda functions because Lambda functions that are used for Amazon S3 batch operations must accept and return special data fields\. 

### IAM Permissions<a name="batch-ops-invoke-lambda-custom-functions-iam"></a>

The following are examples of the IAM permissions that are necessary to use a Lambda function with Amazon S3 batch operations\. 

**Example Amazon S3 Batch Operations Trust Policy**  
The following is an example of the trust policy you can use for the execution role to give Lambda permission to execute the function invoked by an Amazon S3 batch operations job\.  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Principal": {
                "Service": "batchoperations.s3.amazonaws.com"
            },
            "Action": "sts:AssumeRole"
        }
    ]
}
```

**Example Lambda IAM Policy**  
The following is an example of an IAM policy to give Amazon S3 batch operations permission to invoke the Lambda function and read the input manifest\.  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "BatchOperationsLambdaPolicy",
            "Effect": "Allow",
            "Action": [
                "s3:GetObject",
                "s3:GetObjectVersion",
                "s3:PutObject",
                "lambda:InvokeFunction"
            ],
            "Resource": "*"
        }
    ]
}
```

### Example Request and Response<a name="batch-ops-invoke-lambda-custom-functions-request"></a>

This section provides request and response examples for the Lambda function\.

**Example Request**  
The following is a JSON example of a request for the Lambda function\.  

```
{
    "invocationSchemaVersion": "1.0",
    "invocationId": "YXNkbGZqYWRmaiBhc2RmdW9hZHNmZGpmaGFzbGtkaGZza2RmaAo",
    "job": {
        "id": "f3cc4f60-61f6-4a2b-8a21-d07600c373ce"
    },
    "tasks": [
        {
            "taskId": "dGFza2lkZ29lc2hlcmUK",
            "s3Key": "customerImage1.jpg",
            "s3VersionId": "1",
            "s3BucketArn": "arn:aws:s3:us-east-1:0123456788:awsexamplebucket"
        }
    ]
}
```

**Example Response**  
The following is a JSON example of a response for the Lambda function\.  

```
{
  "invocationSchemaVersion": "1.0",
  "treatMissingKeysAs" : "PermanentFailure",
  "invocationId" : "YXNkbGZqYWRmaiBhc2RmdW9hZHNmZGpmaGFzbGtkaGZza2RmaAo",
  "results": [
    {
      "taskId": "dGFza2lkZ29lc2hlcmUK",
      "resultCode": "Succeeded",
      "resultString": "[\"Mary Major", \"John Stiles\"]"
    }
  ]
}
```

### Example Lambda Function for Amazon S3 Batch Operations<a name="batch-ops-invoke-lambda-custom-functions-example"></a>

The following example Python Lambda function iterates through the manifest, copying and renaming each object\.

As the example shows, keys from Amazon S3 batch operations are URL encoded\. To use Amazon S3 with other AWS services, it's important that you URL decode the key that is passed from Amazon S3 batch operations\.

```
import boto3
import urllib
from botocore.exceptions import ClientError

def lambda_handler(event, context):
    # Instantiate boto client
    s3Client = boto3.client('s3')
    
    # Parse job parameters from Amazon S3 batch operations
    jobId = event['job']['id']
    invocationId = event['invocationId']
    invocationSchemaVersion = event['invocationSchemaVersion']
    
    # Prepare results
    results = []
    
    # Parse Amazon S3 Key, Key Version, and Bucket ARN
    taskId = event['tasks'][0]['taskId']
    s3Key = urllib.unquote(event['tasks'][0]['s3Key']).decode('utf8')
    s3VersionId = event['tasks'][0]['s3VersionId']
    s3BucketArn = event['tasks'][0]['s3BucketArn']
    s3Bucket = s3BucketArn.split(':::')[-1]
    
    # Construct CopySource with VersionId
    copySrc = {'Bucket': s3Bucket, 'Key': s3Key}
    if s3VersionId is not None:
        copySrc['VersionId'] = s3VersionId
        
    # Copy object to new bucket with new key name
    try:
        # Prepare result code and string
        resultCode = None
        resultString = None
        
        # Construct New Key
        newKey = rename_key(s3Key)
        newBucket = 'destination-bucket-name'
        
        # Copy Object to New Bucket
        response = s3Client.copy_object(
            CopySource = copySrc,
            Bucket = newBucket,
            Key = newKey
        )
        
        # Mark as succeeded
        resultCode = 'Succeeded'
        resultString = str(response)
    except ClientError as e:
        # If request timed out, mark as a temp failure
        # and Amason S3 batch operations will make the task for retry. If
        # any other exceptions are received, mark as permanent failure.
        errorCode = e.response['Error']['Code']
        errorMessage = e.response['Error']['Message']
        if errorCode == 'RequestTimeout':
            resultCode = 'TemporaryFailure'
            resultString = 'Retry request to Amazon S3 due to timeout.'
        else:
            resultCode = 'PermanentFailure'
            resultString = '{}: {}'.format(errorCode, errorMessage)
    except Exception as e:
        # Catch all exceptions to permanently fail the task
        resultCode = 'PermanentFailure'
        resultString = 'Exception: {}'.format(e.message)
    finally:
        results.append({
            'taskId': taskId,
            'resultCode': resultCode,
            'resultString': resultString
        })
    
    return {
        'invocationSchemaVersion': invocationSchemaVersion,
        'treatMissingKeysAs': 'PermanentFailure',
        'invocationId': invocationId,
        'results': results
    }

def rename_key(s3Key):
    # Rename the key by adding additional suffix
    return s3Key + '_new_suffix'
```

## Creating an Amazon S3 Batch Operations Job That Invokes a Lambda Function<a name="batch-ops-invoke-lambda-create-job"></a>

When creating an Amazon S3 batch operations job to invoke a Lambda function, you must provide the following:
+ The ARN of your Lambda function \(which might include the function alias or a specific version number\)
+ An IAM role with permission to invoke the function
+ The action parameter `LambdaInvokeFunction`

For more information about creating an Amazon S3 batch operations job, see [Creating a Batch Operations Job](batch-ops-create-job.md) and [Operations](batch-ops-operations.md)\.

The following example creates an Amazon S3 batch operations job that invokes a Lambda function using the AWS CLI\.

```
aws s3control create-job
    --account-id <AccountID>
    --operation  '{"LambdaInvoke": { "FunctionArn": "arn:aws:lambda:Region:AccountID:function:LambdaFunctionName" } }'
    --manifest '{"Spec":{"Format":"S3BatchOperations_CSV_20180820","Fields":["Bucket","Key"]},"Location":{"ObjectArn":"arn:aws:s3:::ManifestLocation","ETag":"ManifestETag"}}'
    --report '{"Bucket":"arn:aws:s3:::awsexamplebucket","Format":"Report_CSV_20180820","Enabled":true,"Prefix":"ReportPrefix","ReportScope":"AllTasks"}'
    --priority 2
    --role-arn arn:aws:iam::AccountID:role/BatchOperationsRole
    --region Region
    --description “Lambda Function"
```