# Invoking a Lambda function from Amazon S3 batch operations<a name="batch-ops-invoke-lambda"></a>

S3 Batch Operations can invoke AWS Lambda functions to perform custom actions on objects that are listed in a manifest\. This section describes how to create a Lambda function to use with S3 Batch Operations and how to create a job to invoke the function\. The S3 Batch Operations job uses the `LambdaInvoke` operation to run a Lambda function on each object listed in a manifest\. 

You can work with S3 Batch Operations for Lambda using the AWS Management Console, AWS Command Line Interface \(AWS CLI\), AWS SDKs, or REST APIs\. For more information about using Lambda, see [ Getting Started with AWS Lambda](https://docs.aws.amazon.com/lambda/latest/dg/getting-started.html) in the *AWS Lambda Developer Guide*\. 

The following sections explain how you can get started using S3 Batch Operations with Lambda\. 

**Topics**
+ [Using Lambda with Amazon S3 batch operations](#batch-ops-invoke-lambda-using)
+ [Creating a Lambda function to use with S3 Batch Operations](#batch-ops-invoke-lambda-custom-functions)
+ [Creating an S3 Batch Operations job that invokes a Lambda function](#batch-ops-invoke-lambda-create-job)
+ [Providing task\-level information in Lambda manifests](#storing-task-level-information-in-lambda)

## Using Lambda with Amazon S3 batch operations<a name="batch-ops-invoke-lambda-using"></a>

When using S3 Batch Operations with AWS Lambda, you must create new Lambda functions specifically for use with S3 Batch Operations\. You can't reuse existing Amazon S3 event\-based functions with S3 Batch Operations\. Event functions can only receive messages; they don't return messages\. The Lambda functions that are used with S3 Batch Operations must accept and return messages\. For more information about using Lambda with Amazon S3 events, see [Using AWS Lambda with Amazon S3](https://docs.aws.amazon.com/lambda/latest/dg/with-s3.html) in the *AWS Lambda Developer Guide*\.

You create an S3 Batch Operations job that invokes your Lambda function\. The job runs the same Lambda function on all of the objects listed in your manifest\. You can control what versions of your Lambda function to use while processing the objects in your manifest\. S3 Batch Operations support unqualified Amazon Resource Names \(ARNs\), aliases, and specific versions\. For more information, see [ Introduction to AWS Lambda Versioning](https://docs.aws.amazon.com/lambda/latest/dg/versioning-intro.html) in the *AWS Lambda Developer Guide*\.

If you provide the S3 Batch Operations job with a function ARN that uses an alias or the `$LATEST` qualifier, and you update the version that either of those points to, S3 Batch Operations starts calling the new version of your Lambda function\. This can be useful when you want to update functionality part of the way through a large job\. If you don't want S3 Batch Operations to change the version that is used, provide the specific version in the `FunctionARN` parameter when you create your job\.

### Response and result codes<a name="batch-ops-invoke-lambda-response-codes"></a>

There are two levels of codes that S3 Batch Operations expect from Lambda functions\. The first is the response code for the entire request, and the second is a per\-task result code\. The following table contains the response codes\.


****  

| Response code | Description | 
| --- | --- | 
| Succeeded | The task completed normally\. If you requested a job completion report, the task's result string is included in the report\. | 
| TemporaryFailure | The task suffered a temporary failure and will be redriven before the job completes\. The result string is ignored\. If this is the final redrive, the error message is included in the final report\. | 
| PermanentFailure | The task suffered a permanent failure\. If you requested a job\-completion report, the task is marked as Failed and includes the error message string\. Result strings from failed tasks are ignored\. | 

## Creating a Lambda function to use with S3 Batch Operations<a name="batch-ops-invoke-lambda-custom-functions"></a>

This section provides example AWS Identity and Access Management \(IAM\) permissions that you must use with your Lambda function\. It also contains an example Lambda function to use with S3 Batch Operations\. If you have never created a Lambda function before, see [Tutorial: Using AWS Lambda with Amazon S3](https://docs.aws.amazon.com/lambda/latest/dg/with-s3-example.html) in the *AWS Lambda Developer Guide*\.

You must create Lambda functions specifically for use with S3 Batch Operations\. You can't reuse existing Amazon S3 event\-based Lambda functions\. This is because Lambda functions that are used for S3 Batch Operations must accept and return special data fields\. 

### Example IAM permissions<a name="batch-ops-invoke-lambda-custom-functions-iam"></a>

The following are examples of the IAM permissions that are necessary to use a Lambda function with S3 Batch Operations\. 

**Example — S3 Batch Operations trust policy**  
The following is an example of the trust policy that you can use for the Batch Operations IAM role\. This IAM role is specified when you create the job and gives Batch Operations permission to assume the IAM role\.  

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

**Example — Lambda IAM policy**  
The following is an example of an IAM policy that gives S3 Batch Operations permission to invoke the Lambda function and read the input manifest\.  

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

### Example request and response<a name="batch-ops-invoke-lambda-custom-functions-request"></a>

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
            "s3BucketArn": "arn:aws:s3:us-east-1:0123456788:awsexamplebucket1"
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

### Example Lambda function for S3 Batch Operations<a name="batch-ops-invoke-lambda-custom-functions-example"></a>

The following example Python Lambda function iterates through the manifest, copying and renaming each object\.

As the example shows, keys from S3 Batch Operations are URL encoded\. To use Amazon S3 with other AWS services, it's important that you URL decode the key that is passed from S3 Batch Operations\.

```
import boto3
import urllib
from botocore.exceptions import ClientError

def lambda_handler(event, context):
    # Instantiate boto client
    s3Client = boto3.client('s3')
    
    # Parse job parameters from S3 Batch Operations
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
        # and S3 Batch Operations will make the task for retry. If
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

## Creating an S3 Batch Operations job that invokes a Lambda function<a name="batch-ops-invoke-lambda-create-job"></a>

When creating an S3 Batch Operations job to invoke a Lambda function, you must provide the following:
+ The ARN of your Lambda function \(which might include the function alias or a specific version number\)
+ An IAM role with permission to invoke the function
+ The action parameter `LambdaInvokeFunction`

For more information about creating an S3 Batch Operations job, see [Creating an S3 Batch Operations job](batch-ops-create-job.md) and [Operations](batch-ops-operations.md)\.

The following example creates an S3 Batch Operations job that invokes a Lambda function using the AWS CLI\.

```
aws s3control create-job
    --account-id <AccountID>
    --operation  '{"LambdaInvoke": { "FunctionArn": "arn:aws:lambda:Region:AccountID:function:LambdaFunctionName" } }'
    --manifest '{"Spec":{"Format":"S3BatchOperations_CSV_20180820","Fields":["Bucket","Key"]},"Location":{"ObjectArn":"arn:aws:s3:::ManifestLocation","ETag":"ManifestETag"}}'
    --report '{"Bucket":"arn:aws:s3:::awsexamplebucket1","Format":"Report_CSV_20180820","Enabled":true,"Prefix":"ReportPrefix","ReportScope":"AllTasks"}'
    --priority 2
    --role-arn arn:aws:iam::AccountID:role/BatchOperationsRole
    --region Region
    --description “Lambda Function"
```

## Providing task\-level information in Lambda manifests<a name="storing-task-level-information-in-lambda"></a>

When you use AWS Lambda functions with S3 Batch Operations, you might want additional data to accompany each task/key that is operated on\. For example, you might want to have both a source object key and new object key provided\. Your Lambda function could then copy the source key to a new S3 bucket under a new name\. By default, Amazon S3 batch operations let you specify only the destination bucket and a list of source keys in the input manifest to your job\. The following describes how you can include additional data in your manifest so that you can run more complex Lambda functions\.

To specify per\-key parameters in your S3 Batch Operations manifest to use in your Lambda function's code, use the following URL\-encoded JSON format\. The `key` field is passed to your Lambda function as if it were an Amazon S3 object key\. But it can be interpreted by the Lambda function to contain other values or multiple keys, as shown following\. 

**Note**  
The maximum number of characters for the `key` field in the manifest is 1,024\.

**Example — manifest substituting the "Amazon S3 keys" with JSON strings**  
The URL\-encoded version must be provided to S3 Batch Operations\.  

```
my-bucket,{"origKey": "object1key", "newKey": "newObject1Key"}
my-bucket,{"origKey": "object2key", "newKey": "newObject2Key"}
my-bucket,{"origKey": "object3key", "newKey": "newObject3Key"}
```

**Example — manifest URL\-encoded**  
This URL\-encoded version must be provided to S3 Batch Operations\. The non\-URL\-encoded version does not work\.  

```
my-bucket,%7B%22origKey%22%3A%20%22object1key%22%2C%20%22newKey%22%3A%20%22newObject1Key%22%7D
my-bucket,%7B%22origKey%22%3A%20%22object2key%22%2C%20%22newKey%22%3A%20%22newObject2Key%22%7D
my-bucket,%7B%22origKey%22%3A%20%22object3key%22%2C%20%22newKey%22%3A%20%22newObject3Key%22%7D
```

**Example — Lambda function with manifest format writing results to the job report**  
 This Lambda function shows how to parse JSON that is encoded into the S3 Batch Operations manifest\.  

```
import json
from urllib.parse import unquote_plus


# This example Lambda function shows how to parse JSON that is encoded into the Amazon S3 batch
# operations manifest containing lines like this:
#
# bucket,encoded-json
# bucket,encoded-json
# bucket,encoded-json
#
# For example, if we wanted to send the following JSON to this Lambda function:
#
# bucket,{"origKey": "object1key", "newKey": "newObject1Key"}
# bucket,{"origKey": "object2key", "newKey": "newObject2Key"}
# bucket,{"origKey": "object3key", "newKey": "newObject3Key"}
#
# We would simply URL-encode the JSON like this to create the real manifest to create a batch
# operations job with:
#
# my-bucket,%7B%22origKey%22%3A%20%22object1key%22%2C%20%22newKey%22%3A%20%22newObject1Key%22%7D
# my-bucket,%7B%22origKey%22%3A%20%22object2key%22%2C%20%22newKey%22%3A%20%22newObject2Key%22%7D
# my-bucket,%7B%22origKey%22%3A%20%22object3key%22%2C%20%22newKey%22%3A%20%22newObject3Key%22%7D
#
def lambda_handler(event, context):
    # Parse job parameters from S3 batch operations
    jobId = event['job']['id']
    invocationId = event['invocationId']
    invocationSchemaVersion = event['invocationSchemaVersion']

    # Prepare results
    results = []

    # S3 batch operations currently only passes a single task at a time in the array of tasks.
    task = event['tasks'][0]

    # Extract the task values we might want to use
    taskId = task['taskId']
    s3Key = task['s3Key']
    s3VersionId = task['s3VersionId']
    s3BucketArn = task['s3BucketArn']
    s3BucketName = s3BucketArn.split(':::')[-1]

    try:
        # Assume it will succeed for now
        resultCode = 'Succeeded'
        resultString = ''

        # Decode the JSON string that was encoded into the S3 Key value and convert the
        # resulting string into a JSON structure.
        s3Key_decoded = unquote_plus(s3Key)
        keyJson = json.loads(s3Key_decoded)

        # Extract some values from the JSON that we might want to operate on.  In this example
        # we won't do anything except return the concatenated string as a fake result.
        newKey = keyJson['newKey']
        origKey = keyJson['origKey']
        resultString = origKey + " --> " + newKey

    except Exception as e:
        # If we run into any exceptions, fail this task so batch operations does not retry it and
        # return the exception string so we can see the failure message in the final report
        # created by batch operations.
        resultCode = 'PermanentFailure'
        resultString = 'Exception: {}'.format(e)
    finally:
        # Send back the results for this task.
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
```