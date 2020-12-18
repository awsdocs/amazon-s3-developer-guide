# Object key and metadata<a name="UsingMetadata"></a>

Each Amazon S3 object has data, a key, and metadata\. The *object key* \(or key name\) uniquely identifies the object in a bucket\. *Object metadata* is a set of name\-value pairs\. You can set object metadata at the time you upload it\. After you upload the object, you cannot modify object metadata\. The only way to modify object metadata is to make a copy of the object and set the metadata\. 

**Topics**
+ [Object keys](#object-keys)
+ [Object metadata](#object-metadata)

## Object keys<a name="object-keys"></a>

When you create an object, you specify the key name, which uniquely identifies the object in the bucket\. For example, on the [Amazon S3 console](https://console.aws.amazon.com/s3/home), when you highlight a bucket, a list of objects in your bucket appears\. These names are the *object keys*\. The name for a key is a sequence of Unicode characters whose UTF\-8 encoding is at most 1,024 bytes long\. 

The Amazon S3 data model is a flat structure: You create a bucket, and the bucket stores objects\. There is no hierarchy of subbuckets or subfolders\. However, you can infer logical hierarchy using key name prefixes and delimiters as the Amazon S3 console does\. The Amazon S3 console supports a concept of folders\. For more information about how to edit metadata from the Amazon S3 console, see [Editing object metadata](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/add-object-metadata.html) in the *Amazon Simple Storage Service Console User Guide*\.

**Note**  
Amazon S3 supports buckets and objects, and there is no hierarchy\. However, by using prefixes and delimiters in an object key name, the Amazon S3 console and the AWS SDKs can infer hierarchy and introduce the concept of folders\.
The Amazon S3 console implements folder object creation by creating a zero\-byte object with the folder *prefix and delimiter* value as the key\. These folder objects don't appear in the console\. Otherwise they behave like any other objects and can be viewed and manipulated through the REST API, AWS CLI, and AWS SDKs\.

### Object key naming guidelines<a name="object-key-guidelines"></a>

You can use any UTF\-8 character in an object key name\. However, using certain characters in key names can cause problems with some applications and protocols\. The following guidelines help you maximize compliance with DNS, web\-safe characters, XML parsers, and other APIs\. 

#### Safe characters<a name="object-key-guidelines-safe-characters"></a>

The following character sets are generally safe for use in key names\.


|  |  | 
| --- |--- |
| Alphanumeric characters |    0\-9   a\-z   A\-Z    | 
| Special characters |    Forward slash \(`/`\)   Exclamation point \(`!`\)   Hyphen \(`-`\)   Underscore \(`_`\)   Period \(`.`\)   Asterisk \(`*`\)   Single quote \(`'`\)   Open parenthesis \(`(`\)   Close parenthesis \(`)`\)    | 

The following are examples of valid object key names:
+ `4my-organization`
+ `my.great_photos-2014/jan/myvacation.jpg`
+ `videos/2014/birthday/video1.wmv`

**Important**  
If an object key name ends with a single period \(\.\), or two periods \(\.\.\), you can’t download the object using the Amazon S3 console\. To download an object with a key name ending with “\.” or “\.\.”, you must use the AWS Command Line Interface \(AWS CLI\), AWS SDKs, or REST API\.

#### Characters that might require special handling<a name="object-key-guidelines-special-handling"></a>

The following characters in a key name might require additional code handling and likely need to be URL encoded or referenced as HEX\. Some of these are non\-printable characters that your browser might not handle, which also requires special handling:
+ Ampersand \("&"\) 
+ Dollar \("$"\) 
+ ASCII character ranges 00–1F hex \(0–31 decimal\) and 7F \(127 decimal\) 
+ 'At' symbol \("@"\) 
+ Equals \("="\) 
+ Semicolon \(";"\) 
+ Colon \(":"\) 
+ Plus \("\+"\) 
+ Space – Significant sequences of spaces might be lost in some uses \(especially multiple spaces\) 
+ Comma \(","\) 
+ Question mark \("?"\) 

#### Characters to avoid<a name="object-key-guidelines-avoid-characters"></a>

Avoid the following characters in a key name because of significant special handling for consistency across all applications\. 
+ Backslash \("\\"\) 
+ Left curly brace \("\{"\) 
+ Non\-printable ASCII characters \(128–255 decimal characters\)
+ Caret \("^"\) 
+ Right curly brace \("\}"\) 
+ Percent character \("%"\) 
+ Grave accent / back tick \("`"\) 
+ Right square bracket \("\]"\) 
+ Quotation marks 
+ 'Greater Than' symbol \(">"\) 
+ Left square bracket \("\["\) 
+ Tilde \("\~"\) 
+ 'Less Than' symbol \("<"\) 
+ 'Pound' character \("\#"\) 
+ Vertical bar / pipe \("\|"\) 

## Object metadata<a name="object-metadata"></a>

There are two kinds of metadata: *system metadata* and *user\-defined metadata*\. 

### System\-defined object metadata<a name="SysMetadata"></a>

For each object stored in a bucket, Amazon S3 maintains a set of system metadata\. Amazon S3 processes this system metadata as needed\. For example, Amazon S3 maintains object creation date and size metadata and uses this information as part of object management\. 

There are two categories of system metadata: 

1. Metadata such as object creation date is system controlled, where only Amazon S3 can modify the value\. 

1. Other system metadata, such as the storage class configured for the object and whether the object has server\-side encryption enabled, are examples of system metadata whose values you control\. If your bucket is configured as a website, sometimes you might want to redirect a page request to another page or an external URL\. In this case, a webpage is an object in your bucket\. Amazon S3 stores the page redirect value as system metadata whose value you control\. 

   When you create objects, you can configure values of these system metadata items or update the values when you need to\. For more information about storage classes, see [Amazon S3 storage classes](storage-class-intro.md)\. 

   For more information about server\-side encryption, see [Protecting data using encryption](UsingEncryption.md)\. 

**Note**  
The PUT request header is limited to 8 KB in size\. Within the PUT request header, the system\-defined metadata is limited to 2 KB in size\. The size of system\-defined metadata is measured by taking the sum of the number of bytes in the US\-ASCII encoding of each key and value\. 

The following table provides a list of system\-defined metadata and whether you can update it\.


| Name | Description | Can user modify the value? | 
| --- | --- | --- | 
| Date | Current date and time\. | No | 
| Content\-Length | Object size in bytes\. | No | 
| Content\-Type | Object type\. | Yes | 
| Last\-Modified |  Object creation date or the last modified date, whichever is the latest\.  | No | 
| Content\-MD5 | The base64\-encoded 128\-bit MD5 digest of the object\. | Yes | 
| x\-amz\-server\-side\-encryption | Indicates whether server\-side encryption is enabled for the object, and whether that encryption is from the AWS Key Management Service \(AWS KMS\) or from Amazon S3 managed encryption \(SSE\-S3\)\. For more information, see [Protecting data using server\-side encryption](serv-side-encryption.md)\.  | Yes | 
| x\-amz\-version\-id | Object version\. When you enable versioning on a bucket, Amazon S3 assigns a version number to objects added to the bucket\. For more information, see [Using versioning](Versioning.md)\. | No | 
| x\-amz\-delete\-marker | In a bucket that has versioning enabled, this Boolean marker indicates whether the object is a delete marker\.  | No | 
| x\-amz\-storage\-class | Storage class used for storing the object\. For more information, see [Amazon S3 storage classes](storage-class-intro.md)\. | Yes | 
| x\-amz\-website\-redirect\-location |  Redirects requests for the associated object to another object in the same bucket or an external URL\. For more information, see [\(Optional\) Configuring a webpage redirect](how-to-page-redirect.md)\. | Yes | 
| x\-amz\-server\-side\-encryption\-aws\-kms\-key\-id | If x\-amz\-server\-side\-encryption is present and has the value of aws:kms, this indicates the ID of the AWS KMS symmetric customer master key \(CMK\) that was used for the object\. | Yes | 
| x\-amz\-server\-side\-encryption\-customer\-algorithm | Indicates whether server\-side encryption with customer\-provided encryption keys \(SSE\-C\) is enabled\. For more information, see [Protecting data using server\-side encryption with customer\-provided encryption keys \(SSE\-C\)](ServerSideEncryptionCustomerKeys.md)\.  | Yes | 

### User\-defined object metadata<a name="UserMetadata"></a>

When uploading an object, you can also assign metadata to the object\. You provide this optional information as a name\-value \(key\-value\) pair when you send a PUT or POST request to create the object\. When you upload objects using the REST API, the optional user\-defined metadata names must begin with "x\-amz\-meta\-" to distinguish them from other HTTP headers\. When you retrieve the object using the REST API, this prefix is returned\. When you upload objects using the SOAP API, the prefix is not required\. When you retrieve the object using the SOAP API, the prefix is removed, regardless of which API you used to upload the object\. 

**Note**  
 SOAP support over HTTP is deprecated, but it is still available over HTTPS\. New Amazon S3 features will not be supported for SOAP\. We recommend that you use either the REST API or the AWS SDKs\. 

When metadata is retrieved through the REST API, Amazon S3 combines headers that have the same name \(ignoring case\) into a comma\-delimited list\. If some metadata contains unprintable characters, it is not returned\. Instead, the `x-amz-missing-meta` header is returned with a value of the number of unprintable metadata entries\.

User\-defined metadata is a set of key\-value pairs\. Amazon S3 stores user\-defined metadata keys in lowercase\.

Amazon S3 allows arbitrary Unicode characters in your metadata values\.

To avoid issues around the presentation of these metadata values, you should conform to using US\-ASCII characters when using REST and UTF\-8 when using SOAP or browser\-based uploads via POST\.

When using non US\-ASCII characters in your metadata values, the provided Unicode string is examined for non US\-ASCII characters\. If the string contains only US\-ASCII characters, it is presented as is\. If the string contains non US\-ASCII characters, it is first character\-encoded using UTF\-8 and then encoded into US\-ASCII\.

Example:

```
PUT /Key HTTP/1.1
Host: awsexamplebucket1.s3.amazonaws.com
x-amz-meta-nonascii: ÄMÄZÕÑ S3

HEAD /Key HTTP/1.1
Host: awsexamplebucket1.s3.amazonaws.com
x-amz-meta-nonascii: =?UTF-8?B?w4PChE3Dg8KEWsODwpXDg8KRIFMz?=

PUT /Key HTTP/1.1
Host: awsexamplebucket1.s3.amazonaws.com
x-amz-meta-ascii: AMAZONS3

HEAD /Key HTTP/1.1
Host: awsexamplebucket1.s3.amazonaws.com
x-amz-meta-ascii: AMAZONS3
```

**Note**  
The PUT request header is limited to 8 KB in size\. Within the PUT request header, the user\-defined metadata is limited to 2 KB in size\. The size of user\-defined metadata is measured by taking the sum of the number of bytes in the UTF\-8 encoding of each key and value\. 

For information about adding metadata to your object after it’s been uploaded, see [How Do I Add Metadata to an S3 Object?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/add-object-metadata.html) in the *Amazon Simple Storage Service Console User Guide*\.