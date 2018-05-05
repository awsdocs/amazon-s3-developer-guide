# Listing Objects in a Versioning\-Enabled Bucket<a name="list-obj-version-enabled-bucket"></a>

**Topics**
+ [Using the Console](#list-obj-version-enabled-bucket-console)
+ [Using the AWS SDKs](#list-obj-version-enabled-bucket-sdk-examples)
+ [Using the REST API](#ListingtheObjectsinaVersioningEnabledBucket)

This section provides an example of listing object versions from a versioning\-enabled bucket\. Amazon S3 stores object version information in the *versions* subresource \(see [Bucket Configuration Options](UsingBucket.md#bucket-config-options-intro)\) associated with the bucket\. 

## Using the Console<a name="list-obj-version-enabled-bucket-console"></a>

For information about listing object versions in the console, see [ How Do I See the Versions of an S3 Object?](http://docs.aws.amazon.com/AmazonS3/latest/user-guide/view-object-versions.html) in the Amazon Simple Storage Service Console User Guide\. 

## Using the AWS SDKs<a name="list-obj-version-enabled-bucket-sdk-examples"></a>

The code examples in this section retrieve an object listing from a version\-enabled bucket\. Each request returns up to 1000 versions\. If you have more, you will need to send a series of requests to retrieve a list of all versions\. To illustrate how pagination works, the code examples limit the response to two object versions\. If there are more than two object versions in the bucket, the response returns the `IsTruncated` element with the value "true" and also includes the `NextKeyMarker` and `NextVersionIdMarker` elements whose values you can use to retrieve the next set of object keys\. The code example includes these values in the subsequent request to retrieve the next set of objects\.

 For information about using other AWS SDKs, see [Sample Code and Libraries](https://aws.amazon.com/code/)\. 

### Using the AWS SDK for Java<a name="list-obj-version-enabled-bucket-java"></a>

For information about how to create and test a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\. 

**Example**  

```
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListVersionsRequest;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.VersionListing;

public class ListKeysVersionEnabledBucket {
	private static String bucketName = "*** bucket name ***";
	
	public static void main(String[] args) throws IOException {
        AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
        try {
            System.out.println("Listing objects");
            
            ListVersionsRequest request = new ListVersionsRequest()
                .withBucketName(bucketName)
                .withMaxResults(2);
                // you can specify .withPrefix to obtain version list for a specific object or objects with 
                // the specified key prefix.
   
            VersionListing versionListing;            
            do {
                versionListing = s3client.listVersions(request);
                for (S3VersionSummary objectSummary : 
                	versionListing.getVersionSummaries()) {
                    System.out.println(" - " + objectSummary.getKey() + "  " +
                            "(size = " + objectSummary.getSize() + ")" +
                    		"(versionID= " + objectSummary.getVersionId() + ")");

                }
                request.setKeyMarker(versionListing.getNextKeyMarker());
                request.setVersionIdMarker(versionListing.getNextVersionIdMarker());
            } while (versionListing.isTruncated());
         } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, " +
            		"which means your request made it " +
                    "to Amazon S3, but was rejected with an error response " +
                    "for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, " +
            		"which means the client encountered " +
                    "an internal error while trying to communicate" +
                    " with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
}
```

### Using the AWS SDK for \.NET<a name="list-obj-version-enabled-bucket-dotnet"></a>

For information about how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\. 

**Example**  

```
using Amazon.S3;
using Amazon.S3.Model;
using System;
using System.Threading.Tasks;

namespace Amazon.DocSamples.S3
{
    class ListObjectsVersioningEnabledBucketTest
    {
        static string bucketName = "*** bucket name ***";
        // Specify your bucket region (an example region is shown).
        private static readonly RegionEndpoint bucketRegion = RegionEndpoint.USWest2;
        private static IAmazonS3 s3Client;

        public static void Main(string[] args)
        {
            s3Client = new AmazonS3Client(bucketRegion);
            GetObjectListWithAllVersionsAsync().Wait();
        }

        static async Task GetObjectListWithAllVersionsAsync()
        {
            try
            {
                ListVersionsRequest request = new ListVersionsRequest()
                {
                    BucketName = bucketName,
                    // You can optionally specify key name prefix in the request
                    // if you want list of object versions of a specific object.

                    // For this example we limit response to return list of 2 versions.
                    MaxKeys = 2
                };
                do
                {
                    ListVersionsResponse response = await s3Client.ListVersionsAsync(request); 
                    // Process response.
                    foreach (S3ObjectVersion entry in response.Versions)
                    {
                        Console.WriteLine("key = {0} size = {1}",
                            entry.Key, entry.Size);
                    }

                    // If response is truncated, set the marker to get the next 
                    // set of keys.
                    if (response.IsTruncated)
                    {
                        request.KeyMarker = response.NextKeyMarker;
                        request.VersionIdMarker = response.NextVersionIdMarker;
                    }
                    else
                    {
                        request = null;
                    }
                } while (request != null);
            }
            catch (AmazonS3Exception e)
            {
                Console.WriteLine("Error encountered on server. Message:'{0}' when writing an object", e.Message);
            }
            catch (Exception e)
            {
                Console.WriteLine("Unknown encountered on server. Message:'{0}' when writing an object", e.Message);
            }
        }
    }
}
```

## Using the REST API<a name="ListingtheObjectsinaVersioningEnabledBucket"></a>

To list all of the versions of all of the objects in a bucket, you use the `versions` subresource in a `GET Bucket` request\. Amazon S3 can retrieve only a maximum of 1000 objects, and each object version counts fully as an object\. Therefore, if a bucket contains two keys \(e\.g\., `photo.gif` and `picture.jpg`\), and the first key has 990 versions and the second key has 400 versions; a single request would retrieve all 990 versions of `photo.gif` and only the most recent 10 versions of `picture.jpg`\.

Amazon S3 returns object versions in the order in which they were stored, with the most recently stored returned first\.

**To list all object versions in a bucket**
+ In a `GET Bucket` request, include the `versions` sub\-resource\.

  ```
  1. GET /?versions HTTP/1.1
  2. Host: bucketName.s3.amazonaws.com
  3. Date: Wed, 28 Oct 2009 22:32:00 +0000
  4. Authorization: AWS AKIAIOSFODNN7EXAMPLE:0RQf4/cRonhpaBX5sCYVf1bNRuU=
  ```

### Retrieving a Subset of Objects in a Bucket<a name="RetBucObjSubset"></a>

This section discusses the following two example scenarios:
+ You want to retrieve a subset of all object versions in a bucket, for example, retrieve all versions of a specific object\.
+ The number of object versions in the response exceeds the value for `max-key` \(1000 by default\), so that you have to submit a second request to retrieve the remaining object versions\.

 To retrieve a subset of object versions, you use the request parameters for GET Bucket\. For more information, see [GET Bucket](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html)\. 

#### Example 1: Retrieving All Versions of Only a Specific Object<a name="ReturningAllVersionsofanObject"></a>

You can retrieve all versions of an object using the `versions` subresource and the `prefix` request parameter using the following process\. For more information about `prefix`, see [GET Bucket](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html)\.


**Retrieving All Versions of a Key**  

|  |  | 
| --- |--- |
| 1 | Set the prefix parameter to the key of the object you want to retrieve\. | 
| 2 |  Send a `GET Bucket` request using the `versions` subresource and `prefix`\. `GET /?versions&prefix=objectName HTTP/1.1`  | 

**Example Retrieving Objects Using a Prefix**  
The following example retrieves objects whose key is or begins with `myObject`\.  

```
1. GET /?versions&prefix=myObject HTTP/1.1
2. Host: bucket.s3.amazonaws.com
3. Date: Wed, 28 Oct 2009 22:32:00 GMT
4. Authorization: AWS AKIAIOSFODNN7EXAMPLE:0RQf4/cRonhpaBX5sCYVf1bNRuU=
```

You can use the other request parameters to retrieve a subset of all versions of the object\. For more information, see [GET Bucket](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html)\.

#### Example 2: Retrieving a Listing of Additional Objects if the Response Is Truncated<a name="ReturningAdditionalObjectVersionsAfterExceedingMaxKeys"></a>

If the number of objects that could be returned in a `GET` request exceeds the value of `max-keys`, the response contains `<isTruncated>true</isTruncated>`, and includes the first key \(in `NextKeyMarker`\) and the first version ID \(in `NextVersionIdMarker`\) that satisfy the request, but were not returned\. You use those returned values as the starting position in a subsequent request to retrieve the additional objects that satisfy the `GET` request\. 

Use the following process to retrieve additional objects that satisfy the original `GET Bucket versions` request from a bucket\. For more information about `key-marker`, `version-id-marker`, `NextKeyMarker`, and `NextVersionIdMarker`, see [GET Bucket](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html)\.


**Retrieving Additional Responses that Satisfy the Original GET Request**  

|  |  | 
| --- |--- |
| 1 | Set the value of key\-marker to the key returned in NextKeyMarker in the previous response\. | 
| 2 | Set the value of version\-id\-marker to the version ID returned in NextVersionIdMarker in the previous response\. | 
| 3 | Send a GET Bucket versions request using key\-marker and version\-id\-marker\. | 

**Example Retrieving Objects Starting with a Specified Key and Version ID**  

```
1. GET /?versions&key-marker=myObject&version-id-marker=298459348571 HTTP/1.1
2. Host: bucket.s3.amazonaws.com
3. Date: Wed, 28 Oct 2009 22:32:00 GMT
4. Authorization: AWS AKIAIOSFODNN7EXAMPLE:0RQf4/cRonhpaBX5sCYVf1bNRuU=
```