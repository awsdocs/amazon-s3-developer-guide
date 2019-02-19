# Invoke a Lambda Function<a name="batch-ops-invoke-lambda"></a>

The `LambdaInvoke` operation runs an AWS Lambda function on each object in the manifest\. You can create custom Lambda functions to run or use existing functions provided by the AWS Serverless Application Model\. You can pass custom arguments to the function and record the output of the function in a job completion report\. For more information about using Lambda, see [What is AWS Lambda?](https://docs.aws.amazon.com/lambda/latest/dg/) in the *AWS Lambda Developer Guide*\.

## Restrictions and Limitations<a name="batch-ops-invoke-lambda-restrictions"></a>
+ You cannot use existing Lambda functions \(other than compatible functions from AWS Serverless Application Model\)\.
+ Use the same function for all objects\.