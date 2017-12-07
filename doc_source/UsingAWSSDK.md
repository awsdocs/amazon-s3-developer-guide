# Using the AWS SDKs, CLI, and Explorers<a name="UsingAWSSDK"></a>


+ [Specifying Signature Version in Request Authentication](#specify-signature-version)
+ [Set Up the AWS CLI](setup-aws-cli.md)
+ [Using the AWS SDK for Java](UsingTheMPDotJavaAPI.md)
+ [Using the AWS SDK for \.NET](UsingTheMPDotNetAPI.md)
+ [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md)
+ [Using the AWS SDK for Ruby \- Version 3](UsingTheMPRubyAPI.md)
+ [Using the AWS SDK for Python \(Boto\)](UsingTheBotoAPI.md)

You can use the AWS SDKs when developing applications with Amazon S3\. The AWS SDKs simplify your programming tasks by wrapping the underlying REST API\. Mobile SDKs are also available for building connected mobile applications using AWS\. This section provides an overview of using AWS SDKs for developing Amazon S3 applications\. This section also describes how you can test the AWS SDK code examples provided in this guide\. 

In addition to the AWS SDKs, AWS Explorers are available for Visual Studio and Eclipse for Java IDE\. In this case, the SDKs and the explorers are available bundled together as AWS Toolkits\. 

You can also use the AWS Command Line Interface \(AWS CLI\) to manage Amazon S3 buckets and objects\.

**AWS Toolkit for Eclipse**  
The AWS Toolkit for Eclipse includes both the AWS SDK for Java and AWS Explorer for Eclipse\. The AWS Explorer for Eclipse is an open source plugin for Eclipse for Java IDE that makes it easier for developers to develop, debug, and deploy Java applications using AWS\. The easy to use GUI interface enables you to access and administer your AWS infrastructure including Amazon S3\. You can perform common operations such as manage your buckets and objects, set IAM policies, while developing applications, all from within the context of Eclipse for Java IDE\. For set up instructions, see [Set up the Toolkit](http://docs.aws.amazon.com/eclipse-toolkit/latest/user-guide/setup-install.html)\. For examples of using the explorer, see [How to Access AWS Explorer](http://docs.aws.amazon.com/eclipse-toolkit/latest/user-guide/open-aws-explorer.html)\. 

**AWS Toolkit for Visual Studio**  
AWS Explorer for Visual Studio is an extension for Microsoft Visual Studio that makes it easier for developers to develop, debug, and deploy \.NET applications using Amazon Web Services\. The easy\-to\-use GUI enables you to access and administer your AWS infrastructure including Amazon S3\. You can perform common operations such as managing your buckets and objects or setting IAM policies, while developing applications, all from within the context of Visual Studio\. For setup instructions, go to [Setting Up the AWS Toolkit for Visual Studio](http://docs.aws.amazon.com/AWSToolkitVS/latest/UserGuide/tkv_setup.html)\. For examples of using Amazon S3 using the explorer, go to [Using Amazon S3 from AWS Explorer](http://docs.aws.amazon.com/AWSToolkitVS/latest/UserGuide/using-s3.html)\. 

**AWS SDKs**  
You can download only the SDKs\. For information about downloading the SDK libraries, go to [Sample Code Libraries](https://aws.amazon.com/code/)\. 

**AWS CLI**  
The AWS CLI is a unified tool to manage your AWS services, including Amazon S3\. For information about downloading the AWS CLI, go to [AWS Command Line Interface](https://aws.amazon.com/cli/)\. 

## Specifying Signature Version in Request Authentication<a name="specify-signature-version"></a>

In the Asia Pacific \(Mumbai\), Asia Pacific \(Seoul\), EU \(Frankfurt\), EU \(London\), and China \(Beijing\) Regions, Amazon S3 supports only Signature Version 4\. In all other Regions, Amazon S3 supports both Signature Version 4 and Signature Version 2\. 

For all AWS Regions, AWS SDKs use Signature Version 4 by default to authenticate requests\. When using AWS SDKs that were released before May 2016, you may be required to request Signature Version 4 as shown in the following table:


| SDK | Requesting Signature Version 4 for Request Authentication | 
| --- | --- | 
| AWS CLI |  For the default profile, run the following command\. 

```
$ aws configure set default.s3.signature_version s3v4
``` For a custom profile, run the following command\. 

```
$ aws configure set profile.your_profile_name.s3.signature_version s3v4
```  | 
| Java SDK |  Add the following in your code\. 

```
System.setProperty(SDKGlobalConfiguration.ENABLE_S3_SIGV4_SYSTEM_PROPERTY, "true");
``` Or, on the command line, specify the following\. 

```
-Dcom.amazonaws.services.s3.enableV4
```  | 
|  JavaScript SDK |  Set the `signatureVersion` parameter to `v4` when constructing the client\. 

```
var s3 = new AWS.S3({signatureVersion: 'v4'});
```  | 
| PHP SDK |  Set the `signature` parameter to `v4` when constructing the Amazon S3 service client\. 

```
<?php 
									
$s3 = \Aws\S3\S3Client::factory(array('signature' => 'v4'));
```  | 
| Python\-Boto SDK |  Specify the following in the boto default config file\. 

```
[s3] use-sigv4 = True
```  | 
| Ruby SDK |  Ruby SDK \- Version 1: Set the `:s3_signature_version` parameter to `:v4` when constructing the client\. 

```
s3 = AWS::S3::Client.new(:s3_signature_version => :v4)
``` Ruby SDK \- Version 3: Set the `signature_version` parameter to `v4` when constructing the client\. 

```
s3 = Aws::S3::Client.new(signature_version: 'v4')
```  | 
| \.NET SDK |  Add the following to the code before creating the Amazon S3 client\. 

```
AWSConfigs.S3UseSignatureVersion4 = true;
``` Or, add the following to the config file\. 

```
<appSettings>
   <add key="AWS.S3.UseSignatureVersion4" value='true"/>
</appSettings>
```  | 