# Listing Object Keys<a name="ListingKeysUsingAPIs"></a>

 Keys can be listed by prefix\. By choosing a common prefix for the names of related keys and marking these keys with a special character that delimits hierarchy, you can use the list operation to select and browse keys hierarchically\. This is similar to how files are stored in directories within a file system\. 

 Amazon S3 exposes a list operation that lets you enumerate the keys contained in a bucket\. Keys are selected for listing by bucket and prefix\. For example, consider a bucket named "dictionary" that contains a key for every English word\. You might make a call to list all the keys in that bucket that start with the letter "q"\. List results are always returned in UTF\-8 binary order\. 

 Both the SOAP and REST list operations return an XML document that contains the names of matching keys and information about the object identified by each key\. 

**Note**  
 SOAP support over HTTP is deprecated, but it is still available over HTTPS\. New Amazon S3 features will not be supported for SOAP\. We recommend that you use either the REST API or the AWS SDKs\. 

 Groups of keys that share a prefix terminated by a special delimiter can be rolled up by that common prefix for the purposes of listing\. This enables applications to organize and browse their keys hierarchically, much like how you would organize your files into directories in a file system\. For example, to extend the dictionary bucket to contain more than just English words, you might form keys by prefixing each word with its language and a delimiter, such as "French/logical"\. Using this naming scheme and the hierarchical listing feature, you could retrieve a list of only French words\. You could also browse the top\-level list of available languages without having to iterate through all the lexicographically intervening keys\. 

 For more information on this aspect of listing, see [Listing Keys Hierarchically Using a Prefix and Delimiter](ListingKeysHierarchy.md)\. 

**List Implementation Efficiency**  
List performance is not substantially affected by the total number of keys in your bucket, nor by the presence or absence of the prefix, marker, maxkeys, or delimiter arguments\. For information on improving overall bucket performance, including the list operation, see [Request Rate and Performance Guidelines](request-rate-perf-considerations.md)\.

## Iterating Through Multi\-Page Results<a name="ListingKeysPaginated"></a>

As buckets can contain a virtually unlimited number of keys, the complete results of a list query can be extremely large\. To manage large result sets, the Amazon S3 API supports pagination to split them into multiple responses\. Each list keys response returns a page of up to 1,000 keys with an indicator indicating if the response is truncated\. You send a series of list keys requests until you have received all the keys\. AWS SDK wrapper libraries provide the same pagination\. 

The following Java and \.NET SDK examples show how to use pagination when listing keys in a bucket:
+ [Listing Keys Using the AWS SDK for Java](ListingObjectKeysUsingJava.md)
+ [Listing Keys Using the AWS SDK for \.NET](ListingObjectKeysUsingNetSDK.md)

### Related Resources<a name="RelatedResources016"></a>
+ [Using the AWS SDKs, CLI, and Explorers](UsingAWSSDK.md)