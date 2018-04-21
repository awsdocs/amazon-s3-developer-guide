# Listing Keys Hierarchically Using a Prefix and Delimiter<a name="ListingKeysHierarchy"></a>

 The prefix and delimiter parameters limit the kind of results returned by a list operation\. Prefix limits results to only those keys that begin with the specified prefix, and delimiter causes list to roll up all keys that share a common prefix into a single summary list result\. 

 The purpose of the prefix and delimiter parameters is to help you organize and then browse your keys hierarchically\. To do this, first pick a delimiter for your bucket, such as slash \(/\), that doesn't occur in any of your anticipated key names\. Next, construct your key names by concatenating all containing levels of the hierarchy, separating each level with the delimiter\. 

 For example, if you were storing information about cities, you might naturally organize them by continent, then by country, then by province or state\. Because these names don't usually contain punctuation, you might select slash \(/\) as the delimiter\. The following examples use a slash \(/\) delimiter\.
+ Europe/France/Aquitaine/Bordeaux
+ North America/Canada/Quebec/Montreal
+ North America/USA/Washington/Bellevue
+ North America/USA/Washington/Seattle

 If you stored data for every city in the world in this manner, it would become awkward to manage a flat key namespace\. By using `Prefix` and `Delimiter` with the list operation, you can use the hierarchy you've created to list your data\. For example, to list all the states in USA, set `Delimiter`='/' and `Prefix`='North America/USA/'\. To list all the provinces in Canada for which you have data, set `Delimiter`='/' and `Prefix`='North America/Canada/'\.

 A list request with a delimiter lets you browse your hierarchy at just one level, skipping over and summarizing the \(possibly millions of\) keys nested at deeper levels\. For example, assume you have a bucket \(`ExampleBucket`\) the following keys\.

`sample.jpg` 

`photos/2006/January/sample.jpg` 

`photos/2006/February/sample2.jpg` 

`photos/2006/February/sample3.jpg` 

`photos/2006/February/sample4.jpg` 

The sample bucket has only the `sample.jpg` object at the root level\. To list only the root level objects in the bucket you send a GET request on the bucket with "/" delimiter character\. In response, Amazon S3 returns the `sample.jpg` object key because it does not contain the "/" delimiter character\. All other keys contain the delimiter character\. Amazon S3 groups these keys and return a single `CommonPrefixes` element with prefix value `photos/` that is a substring from the beginning of these keys to the first occurrence of the specified delimiter\.

**Example**  

```
 1. <ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
 2.   <Name>ExampleBucket</Name>
 3.   <Prefix></Prefix>
 4.   <Marker></Marker>
 5.   <MaxKeys>1000</MaxKeys>
 6.   <Delimiter>/</Delimiter>
 7.   <IsTruncated>false</IsTruncated>
 8.   <Contents>
 9.     <Key>sample.jpg</Key>
10.     <LastModified>2011-07-24T19:39:30.000Z</LastModified>
11.     <ETag>&quot;d1a7fb5eab1c16cb4f7cf341cf188c3d&quot;</ETag>
12.     <Size>6</Size>
13.     <Owner>
14.       <ID>75cc57f09aa0c8caeab4f8c24e99d10f8e7faeebf76c078efc7c6caea54ba06a</ID>
15.       <DisplayName>displayname</DisplayName>
16.     </Owner>
17.     <StorageClass>STANDARD</StorageClass>
18.   </Contents>
19.   <CommonPrefixes>
20.     <Prefix>photos/</Prefix>
21.   </CommonPrefixes>
22. </ListBucketResult>
```