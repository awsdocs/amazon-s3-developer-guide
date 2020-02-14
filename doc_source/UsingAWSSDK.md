# Using the AWS SDKs, CLI, and Explorers<a name="UsingAWSSDK"></a>

You can use the AWS SDKs when developing applications with Amazon S3\. The AWS SDKs simplify your programming tasks by wrapping the underlying REST API\. The AWS Mobile SDKs and the AWS Amplify JavaScript library are also available for building connected mobile and web applications using AWS\. 

This section provides an overview of using AWS SDKs for developing Amazon S3 applications\. This section also describes how you can test the AWS SDK code examples provided in this guide\. 

**Topics**
+ [Specifying the Signature Version in Request Authentication](#specify-signature-version)
+ [Setting Up the AWS CLI](setup-aws-cli.md)
+ [Using the AWS SDK for Java](UsingTheMPJavaAPI.md)
+ [Using the AWS SDK for \.NET](UsingTheMPDotNetAPI.md)
+ [Using the AWS SDK for PHP and Running PHP Examples](UsingTheMPphpAPI.md)
+ [Using the AWS SDK for Ruby \- Version 3](UsingTheMPRubyAPI.md)
+ [Using the AWS SDK for Python \(Boto\)](UsingTheBotoAPI.md)
+ [Using the AWS Mobile SDKs for iOS and Android](using-mobile-sdks.md)
+ [Using the AWS Amplify JavaScript Library](using-aws-amplify.md)

In addition to the AWS SDKs, AWS Explorers are available for Visual Studio and Eclipse for Java IDE\. In this case, the SDKs and the explorers are available bundled together as AWS Toolkits\. 

You can also use the AWS Command Line Interface \(AWS CLI\) to manage Amazon S3 buckets and objects\.

**AWS Toolkit for Eclipse**  
The AWS Toolkit for Eclipse includes both the AWS SDK for Java and AWS Explorer for Eclipse\. The AWS Explorer for Eclipse is an open source plugin for Eclipse for Java IDE that makes it easier for developers to develop, debug, and deploy Java applications using AWS\. The easy\-to\-use GUI enables you to access and administer your AWS infrastructure including Amazon S3\. You can perform common operations such as managing your buckets and objects and setting IAM policies, while developing applications, all from within the context of Eclipse for Java IDE\. For set up instructions, see [Set up the Toolkit](https://docs.aws.amazon.com/eclipse-toolkit/latest/user-guide/setup-install.html)\. For examples of using the explorer, see [How to Access AWS Explorer](https://docs.aws.amazon.com/eclipse-toolkit/latest/user-guide/open-aws-explorer.html)\. 

**AWS Toolkit for Visual Studio**  
AWS Explorer for Visual Studio is an extension for Microsoft Visual Studio that makes it easier for developers to develop, debug, and deploy \.NET applications using Amazon Web Services\. The easy\-to\-use GUI enables you to access and administer your AWS infrastructure including Amazon S3\. You can perform common operations such as managing your buckets and objects or setting IAM policies, while developing applications, all from within the context of Visual Studio\. For setup instructions, go to [Setting Up the AWS Toolkit for Visual Studio](https://docs.aws.amazon.com/AWSToolkitVS/latest/UserGuide/tkv_setup.html)\. For examples of using Amazon S3 using the explorer, see [Using Amazon S3 from AWS Explorer](https://docs.aws.amazon.com/AWSToolkitVS/latest/UserGuide/using-s3.html)\. 

**AWS SDKs**  
You can download only the SDKs\. For information about downloading the SDK libraries, see [Sample Code Libraries](https://aws.amazon.com/tools/)\. 

**AWS CLI**  
The AWS CLI is a unified tool to manage your AWS services, including Amazon S3\. For information about downloading the AWS CLI, see [AWS Command Line Interface](https://aws.amazon.com/cli/)\. 

## Specifying the Signature Version in Request Authentication<a name="specify-signature-version"></a>

 Amazon S3 supports only AWS Signature Version 4 in most AWS Regions\. In some of the older AWS Regions, Amazon S3 supports both Signature Version 4 and Signature Version 2\. However, Signature Version 2 is being turned off \(deprecated\)\. For more information about the end of support for Signature Version 2, see [AWS Signature Version 2 Turned Off \(Deprecated\) for Amazon S3](#UsingAWSSDK-sig2-deprecation)\.

For a list of all the Amazon S3 Regions and the signature versions they support, see [Regions and Endpoints](https://docs.aws.amazon.com/general/latest/gr/rande.html#s3_region) in the *AWS General Reference*\.

For all AWS Regions, AWS SDKs use Signature Version 4 by default to authenticate requests\. When using AWS SDKs that were released before May 2016, you might be required to request Signature Version 4, as shown in the following table\.


| SDK | Requesting Signature Version 4 for Request Authentication | 
| --- | --- | 
| AWS CLI |  For the default profile, run the following command: <pre>$ aws configure set default.s3.signature_version s3v4</pre> For a custom profile, run the following command: <pre>$ aws configure set profile.your_profile_name.s3.signature_version s3v4</pre>  | 
| Java SDK |  Add the following in your code: <pre>System.setProperty(SDKGlobalConfiguration.ENABLE_S3_SIGV4_SYSTEM_PROPERTY, "true");</pre> Or, on the command line, specify the following: <pre>-Dcom.amazonaws.services.s3.enableV4</pre>  | 
|  JavaScript SDK |  Set the `signatureVersion` parameter to `v4` when constructing the client: <pre>var s3 = new AWS.S3({signatureVersion: 'v4'});</pre>  | 
| PHP SDK |  Set the `signature` parameter to `v4` when constructing the Amazon S3 service client for PHP SDK v2: <pre><?php <br />$client = S3Client::factory([<br />    'region' => 'YOUR-REGION',<br />    'version' => 'latest',<br />    'signature' => 'v4'<br />]);</pre> When using the PHP SDK v3, set the `signature_version` parameter to `v4` during construction of the Amazon S3 service client: <pre><?php <br />$s3 = new Aws\S3\S3Client([<br />    'version' => '2006-03-01',<br />    'region' => 'YOUR-REGION',<br />    'signature_version' => 'v4'<br />]);</pre>  | 
| Python\-Boto SDK |  Specify the following in the boto default config file: <pre>[s3] use-sigv4 = True</pre>  | 
| Ruby SDK |  Ruby SDK \- Version 1: Set the `:s3_signature_version` parameter to `:v4` when constructing the client: <pre>s3 = AWS::S3::Client.new(:s3_signature_version => :v4)</pre> Ruby SDK \- Version 3: Set the `signature_version` parameter to `v4` when constructing the client: <pre>s3 = Aws::S3::Client.new(signature_version: 'v4')</pre>  | 
| \.NET SDK |  Add the following to the code before creating the Amazon S3 client: <pre>AWSConfigsS3.UseSignatureVersion4 = true;</pre> Or, add the following to the config file: <pre><appSettings><br />   <add key="AWS.S3.UseSignatureVersion4" value="true" /><br /></appSettings></pre>  | 

 

### AWS Signature Version 2 Turned Off \(Deprecated\) for Amazon S3<a name="UsingAWSSDK-sig2-deprecation"></a>

Signature Version 2 is being turned off \(deprecated\) in Amazon S3\.  Amazon S3 will then only accept API requests that are signed using Signature Version 4\. 

This section provides answers to common questions regarding the end of support for Signature Version 2\. 

**What is Signature Version 2/4, and What Does It Mean to Sign Requests?**  
The Signature Version 2 or Signature Version 4 signing process is used to authenticate your Amazon S3 API requests\. Signing requests enables Amazon S3 to identify who is sending the request and protects your requests from bad actors\.

For more information about signing AWS requests, see [Signing AWS API Requests](https://docs.aws.amazon.com/general/latest/gr/signing_aws_api_requests.html) in the *AWS General Reference*\. 

**What Update Are You Making?**  
We currently support Amazon S3 API requests that are signed using Signature Version 2 and Signature Version 4 processes\. After that, Amazon S3 will only accept requests that are signed using Signature Version 4\. 

For more information about signing AWS requests, see [Changes in Signature Version 4](https://docs.aws.amazon.com/general/latest/gr/sigv4_changes.html) in the *AWS General Reference*\. 

**Why Are You Making the Update?**  
Signature Version 4 provides improved security by using a signing key instead of your secret access key\. Signature Version 4 is currently supported in all AWS Regions, whereas Signature Version 2 is only supported in Regions that were launched before January 2014\. This update allows us to provide a more consistent experience across all Regions\. 

**How Do I Ensure That I'm Using Signature Version 4, and What Updates Do I Need?**  
The signature version that is used to sign your requests is usually set by the tool or the SDK on the client side\. By default, the latest versions of our AWS SDKs use Signature Version 4\. For third\-party software, contact the appropriate support team for your software to confirm what version you need\. If you are sending direct REST calls to Amazon S3, you must modify your application to use the Signature Version 4 signing process\. 

For information about which version of the AWS SDKs to use when moving to Signature Version 4, see [Moving from Signature Version 2 to Signature Version 4](#UsingAWSSDK-move-to-Sig4)\. 

For information about using Signature Version 4 with the Amazon S3 REST API, see [Authenticating Requests \(AWS Signature Version 4\)](https://docs.aws.amazon.com/AmazonS3/latest/API/sig-v4-authenticating-requests.html) in the *Amazon Simple Storage Service API Reference*\.

**What Happens if I Don't Make Updates?**  
Requests signed with Signature Version 2 that are made after that will fail to authenticate with Amazon S3\. Requesters will see errors stating that the request must be signed with Signature Version 4\. 

**Should I Make Changes Even if I’m Using a Presigned URL That Requires Me to Sign for More than 7 Days?**  
If you are using a presigned URL that requires you to sign for more than 7 days, no action is currently needed\. You can continue to use AWS Signature Version 2 to sign and authenticate the presigned URL\. We will follow up and provide more details on how to migrate to Signature Version 4 for a presigned URL scenario\. 

#### More Info<a name="UsingAWSSDK-sev2-deprecation-more-info"></a>
+ For more information about using Signature Version 4, see [Signing AWS API Requests](https://docs.aws.amazon.com/general/latest/gr/signing_aws_api_requests.html)\.
+ View the list of changes between Signature Version 2 and Signature Version 4 in [Changes in Signature Version 4](https://docs.aws.amazon.com/general/latest/gr/sigv4_changes.html)\. 
+ View the post [AWS Signature Version 4 to replace AWS Signature Version 2 for signing Amazon S3 API requests](https://forums.aws.amazon.com/ann.jspa?annID=5816) in the AWS forums\.
+ If you have any questions or concerns, contact [AWS Support](https://docs.aws.amazon.com/awssupport/latest/user/getting-started.html)\.

   

### Moving from Signature Version 2 to Signature Version 4<a name="UsingAWSSDK-move-to-Sig4"></a>

If you currently use Signature Version 2 for Amazon S3 API request authentication, you should move to using Signature Version 4\. Support is ending for Signature Version 2, as described in [AWS Signature Version 2 Turned Off \(Deprecated\) for Amazon S3](#UsingAWSSDK-sig2-deprecation)\.

For information about using Signature Version 4 with the Amazon S3 REST API, see [Authenticating Requests \(AWS Signature Version 4\)](https://docs.aws.amazon.com/AmazonS3/latest/API/sig-v4-authenticating-requests.html) in the *Amazon Simple Storage Service API Reference*\.

The following table lists the SDKs with the necessary minimum version to use Signature Version 4 \(SigV4\)\.  If you are using presigned URLs with the AWS Java, JavaScript \(Node\.js\), or Python \(Boto/CLI\) SDKs, you must set the correct AWS Region and set Signature Version 4 in the client configuration\. For information about setting `SigV4` in the client configuration, see [Specifying the Signature Version in Request Authentication](#specify-signature-version)\.


| If you use this SDK/Product | Upgrade to this SDK version | Code change needed to the client to use Sigv4? | Link to SDK documentation | 
| --- | --- | --- | --- | 
|  AWS SDK for Java v1  | Upgrade to Java 1\.11\.201\+ or v2 in Q4 2018\. | Yes | [Specifying the Signature Version in Request Authentication](#specify-signature-version) | 
|  AWS SDK for Java v2 \(preview\)  | No SDK upgrade is needed\. | No | [AWS SDK for Java](https://aws.amazon.com/sdk-for-java/) | 
|  AWS SDK for \.NET v1   | Upgrade to 3\.1\.10 or later\. | Yes | [AWS SDK for \.NET](https://github.com/aws/aws-sdk-net/tree/aws-sdk-net-v1/) | 
|  AWS SDK for \.NET v2   | Upgrade to 3\.1\.10 or later\. | No | [AWS SDK for \.NET v2](https://github.com/aws/aws-sdk-net/tree/aws-sdk-net-v2/) | 
|  AWS SDK for \.NET v3   | Upgrade to 3\.3\.0\.0 or later\. | Yes | [AWS SDK for \.NET v3](https://github.com/aws/aws-sdk-net) | 
|  AWS SDK for JavaScript v1   | Upgrade to 2\.68\.0 or later\. | Yes | [AWS SDK for JavaScript](https://github.com/aws/aws-sdk-js) | 
|  AWS SDK for JavaScript v2   | Upgrade to 2\.68\.0 or later\. | Yes | [AWS SDK for JavaScript](https://github.com/aws/aws-sdk-js) | 
|  AWS SDK for JavaScript v3   | No action is currently needed\. Upgrade to major version V3 in Q3 2019\. | No | [AWS SDK for JavaScript](https://github.com/aws/aws-sdk-js) | 
|  AWS SDK for PHP v1   | Recommend to upgrade to the most recent version of PHP or, at least to v2\.7\.4 with the signature parameter set to v4 in the S3 client's configuration\. | Yes | [AWS SDK for PHP](https://aws.amazon.com/sdk-for-php/) | 
|  AWS SDK for PHP v2   | Recommend to upgrade to the most recent version of PHP or, at least to v2\.7\.4 with the signature parameter set to v4 in the S3 client's configuration\. | No | [AWS SDK for PHP](https://aws.amazon.com/sdk-for-php/) | 
|  AWS SDK for PHP v3   | No SDK upgrade is needed\. | No | [AWS SDK for PHP](https://aws.amazon.com/sdk-for-php/) | 
|  Boto2   | Upgrade to Boto2 v2\.49\.0\. | Yes | [Boto 2 Upgrade](https://github.com/boto/boto/commit/16729da27b95d6dbbd81bcebb43bcf099ce23fd3) | 
|  Boto3   | Upgrade to 1\.5\.71 \(Botocore\), 1\.4\.6 \(Boto3\)\. | Yes | [Boto 3 \- AWS SDK for Python](https://github.com/boto/boto3) | 
|  AWS CLI   | Upgrade to 1\.11\.108\. | Yes | [AWS Command Line Interface](https://aws.amazon.com/cli/) | 
|  AWS CLI v2 \(preview\)   | No SDK upgrade is needed\. | No | [AWS Command Line Interface version 2](https://github.com/aws/aws-cli/tree/v2) | 
|  AWS SDK for Ruby v1   | Upgrade to Ruby V3\. | Yes | [Ruby V3 for AWS](https://rubygems.org/gems/aws-sdk/versions) | 
|  AWS SDK for Ruby v2   | Upgrade to Ruby V3\. | Yes | [Ruby V3 for AWS](https://rubygems.org/gems/aws-sdk/versions) | 
|  AWS SDK for Ruby v3   | No SDK upgrade is needed\. | No | [Ruby V3 for AWS](https://rubygems.org/gems/aws-sdk/versions) | 
|  Go   | No SDK upgrade is needed\. | No | [AWS SDK for Go](https://aws.amazon.com/sdk-for-go/) | 
|  C\+\+   | No SDK upgrade is needed\. | No | [AWS SDK for C\+\+](https://aws.amazon.com/sdk-for-cpp/) | 

**AWS Tools for Windows PowerShell or AWS Tools for PowerShell Core**  
If you are using module versions *earlier* than 3\.3\.0\.0, you must upgrade to 3\.3\.0\.0\. 

To get the version information, use the `Get-Module` cmdlet: 

```
          Get-Module –Name AWSPowershell
          Get-Module –Name AWSPowershell.NetCore
```

To update the 3\.3\.0\.0 version, use the `Update-Module` cmdlet: 

```
          Update-Module –Name AWSPowershell
          Update-Module –Name AWSPowershell.NetCore
```

You can use presigned URLs that are valid for more than 7 days that you will send Signature Version 2 traffic on\.