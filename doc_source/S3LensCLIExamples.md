# Amazon S3 Storage Lens examples using the AWS CLI<a name="S3LensCLIExamples"></a>

Amazon S3 Storage Lens aggregates your usage and activity metrics and displays the information in an interactive dashboard on the Amazon S3 console or through a metrics data export that can be downloaded in CSV or Parquet format\. You can use the dashboard to visualize insights and trends, flag outliers, and provides recommendations for optimizing storage costs and applying data protection best practices\. You can use S3 Storage Lens through the AWS Management Console, AWS CLI, AWS SDKs, or REST API\.\. For more information, see [Assessing storage activity and usage with Amazon S3 Storage Lens](https://docs.aws.amazon.com/AmazonS3/latest/dev/storage_lens.html)\. 

The following examples show how you can use S3 Storage Lens with the AWS Command Line Interface\.

**Topics**
+ [Helper files for using Amazon S3 Storage Lens](#S3LensHelperFilesCLI)
+ [Using Amazon S3 Storage Lens configurations using the AWS CLI](#S3LensConfigurationsCLI)
+ [Using Amazon S3 Storage Lens with your AWS Organizations using the AWS CLI](#S3LensOrganizationsCLI)

## Helper files for using Amazon S3 Storage Lens<a name="S3LensHelperFilesCLI"></a>

Use the following json files for key inputs for your examples\.



### S3 Storage Lens sample configuration json<a name="S3LensHelperFilesSampleConfigurationCLI"></a>

**Example config\.json**  
Contains details of a S3 Storage Lens Organizations\-level *Advanced Metrics and Recommendations* configuration\.  
Additional charges apply for Advanced Metrics and Recommendations\. For more information, see [Advanced Metrics and Recommendations](https://docs.aws.amazon.com/AmazonS3/latest/dev/storage_lens_basics_metrics_recommendations.html#storage_lens_basics_metrics_selection)\.

```
        {
     "Id": "SampleS3StorageLensConfiguration",//Use this property to identify S3 Storage Lens configuration.
     "AwsOrg": {//Use this property when enabling S3 Storage Lens for AWS Organizations
        "Arn": "arn:aws:organizations::222222222222:organization/o-abcdefgh"
     },
     "AccountLevel": {
        "ActivityMetrics": {
           "IsEnabled":true
        },
        "BucketLevel": {
           "ActivityMetrics": {
              "IsEnabled":true//Mark this as false if you only want Free Metrics metrics.
           },
           "PrefixLevel":{
              "StorageMetrics":{
                 "IsEnabled":true,//Mark this as false if you only want Free Metrics metrics.
                 "SelectionCriteria":{
                    "MaxDepth":5,
                    "MinStorageBytesPercentage":1.25,
                    "Delimiter":"/"
                 }
              }
           }
        }
     },
     "Exclude": {//Replace with include if you prefer to include regions.
        "Regions": [
           "eu-west-1"
        ],
        "Buckets": [/This attribute is not supported for  Organizations-level configurations.
           "arn:aws:s3:::source_bucket1" 
        ]
     },
     "IsEnabled": true,//Whether the configuration is enabled
     "DataExport": {//Details about the metrics export
        "S3BucketDestination": {
           "OutputSchemaVersion": "V_1",
           "Format": "CSV",//You can add "Parquet" if you prefer.
           "AccountId": "ExampleAWSAccountNo8",
           "Arn": "arn:aws:s3:::destination-bucket-name",
           "Prefix": "prefix-for-your-export-destination",
           "Encryption": {
              "SSES3": {}
           }
        }
     }
  }
```

### S3 Storage Lens sample configuration tags json<a name="S3LensHelperFilesSampleConfigurationTagsCLI"></a>

**Example tags\.json**  

```
[
    {
        "Key": "key1",
        "Value": "value1"
    },
    {
        "Key": "key2",
        "Value": "value2"
    }
]
```

### S3 Storage Lens sample configuration IAM permissions<a name="S3LensHelperFilesSampleConfigurationIAMPermissionsCLI"></a>

**Example permissions\.json**  
S3 Storage Lens IAM permissions\.  

```
{
  "Version": "2012-10-17",  "Statement": [
    {
      "Effect": "Allow",      
      "Action": [       
        "iam:*",        
        "sts:AssumeRole"      
      ],      
      "Resource": "*"    
    },    
    {
      "Effect": "Allow",      
      "Action":
      [
        "s3:GetStorageLensConfiguration*",
        "s3:DeleteStorageLensConfiguration*",        
        "s3:PutStorageLensConfiguration*"      
      ],      
      "Condition": {
        "StringEquals": {
          "aws:ResourceTag/key1": "value1"        
        }
      },      
      "Resource": "*"  
    }
  ]
}
```

## Using Amazon S3 Storage Lens configurations using the AWS CLI<a name="S3LensConfigurationsCLI"></a>

You can use the AWS CLI to list, create, get and update your S3 Storage Lens configurations\. The following examples use the helper json files for key inputs\.

**Topics**
+ [Put an S3 Storage Lens configuration](#S3PutStorageLensConfigurationTagsCLI)
+ [Put an S3 Storage Lens configuration without tags](#S3PutStorageLensConfigurationWOTagsCLI)
+ [Gets an S3 Storage Lens configuration](#S3GetStorageLensConfigurationCLI)
+ [Lists S3 Storage Lens configurations without next token](#S3ListStorageLensConfigurationsWOTokenCLI)
+ [Lists S3 Storage Lens configurations](#S3ListStorageLensConfigurationsCLI)
+ [Delete an S3 Storage Lens configuration](#S3DeleteStorageLensConfigurationCLI)
+ [Put tags to an S3 Storage Lens configuration](#S3PutStorageLensConfigurationTaggingCLI)
+ [Get tags for an S3 Storage Lens configuration](#S3GetStorageLensConfigurationTaggingCLI)
+ [Delete tags for an S3 Storage Lens configuration](#S3DeleteStorageLensConfigurationTaggingCLI)

### Put an S3 Storage Lens configuration<a name="S3PutStorageLensConfigurationTagsCLI"></a>

**Example Puts an S3 Storage Lens configuration**  

```
aws s3control put-storage-lens-configuration --account-id=222222222222 --config-id=your-configuration-id --region=us-east-1 --storage-lens-configuration=file://./config.json --tags=file://./tags.json
```

### Put an S3 Storage Lens configuration without tags<a name="S3PutStorageLensConfigurationWOTagsCLI"></a>

**Example Puts an S3 Storage Lens configuration\.**  

```
aws s3control put-storage-lens-configuration --account-id=222222222222 --config-id=your-configuration-id --region=us-east-1 --storage-lens-configuration=file://./config.json
```

### Gets an S3 Storage Lens configuration<a name="S3GetStorageLensConfigurationCLI"></a>

**Example Get an S3 Storage Lens configuration**  

```
aws s3control get-storage-lens-configuration --account-id=222222222222 --config-id=your-configuration-id --region=us-east-1
```

### Lists S3 Storage Lens configurations without next token<a name="S3ListStorageLensConfigurationsWOTokenCLI"></a>

**Example Lists S3 Storage Lens configurations without next token**  

```
aws s3control list-storage-lens-configurations --account-id=222222222222 --region=us-east-1
```

### Lists S3 Storage Lens configurations<a name="S3ListStorageLensConfigurationsCLI"></a>

**Example Lists S3 Storage Lens configurations**  

```
aws s3control list-storage-lens-configurations --account-id=222222222222 --region=us-east-1 --next-token=abcdefghij1234
```

### Delete an S3 Storage Lens configuration<a name="S3DeleteStorageLensConfigurationCLI"></a>

**Example Delete an S3 Storage Lens configuration**  

```
aws s3control delete-storage-lens-configuration --account-id=222222222222 --region=us-east-1 --config-id=your-configuration-id
```

### Put tags to an S3 Storage Lens configuration<a name="S3PutStorageLensConfigurationTaggingCLI"></a>

**Example Put tags to an S3 Storage Lens configuration**  

```
aws s3control put-storage-lens-configuration-tagging --account-id=222222222222 --region=us-east-1 --config-id=your-configuration-id --tags=file://./tags.json
```

### Get tags for an S3 Storage Lens configuration<a name="S3GetStorageLensConfigurationTaggingCLI"></a>

**Example Get tags for an S3 Storage Lens configuration**  

```
aws s3control get-storage-lens-configuration-tagging --account-id=222222222222 --region=us-east-1 --config-id=your-configuration-id
```

### Delete tags for an S3 Storage Lens configuration<a name="S3DeleteStorageLensConfigurationTaggingCLI"></a>

**Example Delete tags for an S3 Storage Lens configuration**  

```
aws s3control delete-storage-lens-configuration-tagging --account-id=222222222222 --region=us-east-1 --config-id=your-configuration-id
```

## Using Amazon S3 Storage Lens with your AWS Organizations using the AWS CLI<a name="S3LensOrganizationsCLI"></a>

Use Amazon S3 Storage Lens to collect storage metrics and usage data for all accounts that are part of your AWS Organizations hierarchy\. For more information, see [Using Amazon S3 Storage Lens with AWS Organizations](https://docs.aws.amazon.com/AmazonS3/latest/dev/storage_lens_with_organizations.html)\. 

**Topics**
+ [Enable Organizations trusted access for S3 Storage Lens](#OrganizationsEnableTrustedAccessS3LensCLI)
+ [Disable Organizations trusted access for S3 Storage Lens](#OrganizationsDisableTrustedAccessS3LensCLI)
+ [Register Organizations delegated administrators for S3 Storage Lens](#OrganizationsRegisterDelegatedAdministratorS3LensCLI)
+ [De\-register Organizations delegated administrators for S3 Storage Lens](#OrganizationsDeregisterDelegatedAdministratorS3LensCLI)



### Enable Organizations trusted access for S3 Storage Lens<a name="OrganizationsEnableTrustedAccessS3LensCLI"></a>

**Example Enable Organizations trusted access for S3 Storage Lens**  

```
aws organizations enable-aws-service-access --service-principal storage-lens.s3.amazonaws.com
```

### Disable Organizations trusted access for S3 Storage Lens<a name="OrganizationsDisableTrustedAccessS3LensCLI"></a>

**Example Disable Organizations trusted access for S3 Storage Lens**  

```
aws organizations disable-aws-service-access --service-principal storage-lens.s3.amazonaws.com
```

### Register Organizations delegated administrators for S3 Storage Lens<a name="OrganizationsRegisterDelegatedAdministratorS3LensCLI"></a>

**Example Register Organizations delegated administrators for S3 Storage Lens**  

```
aws organizations register-delegated-administrator --service-principal storage-lens.s3.amazonaws.com —account-id 123456789012
```

### De\-register Organizations delegated administrators for S3 Storage Lens<a name="OrganizationsDeregisterDelegatedAdministratorS3LensCLI"></a>

**Example De\-register Organizations delegated administrators for S3 Storage Lens**  

```
aws organizations deregister-delegated-administrator --service-principal storage-lens.s3.amazonaws.com —account-id 123456789012
```