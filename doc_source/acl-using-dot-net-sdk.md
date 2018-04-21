# Managing ACLs Using the AWS SDK for \.NET<a name="acl-using-dot-net-sdk"></a>

## Setting an ACL When Creating a Resource<a name="set-acl-dot-net-create-resource"></a>

When creating a resource \(buckets and objects\), you can grant permissions by specifying a collection of Grants \(see [Access Control List \(ACL\) Overview](acl-overview.md)\) in your request\. For each Grant, you create an `S3Grant` object explicitly specifying the grantee and the permission\.

For example, the following C\# code example sends a [PUT Bucket](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUT.html) request to create a bucket and then a `PutObject` request to put a new object in the new bucket\. In the request, the code specifies permissions for full control for the owner and WRITE permission for the Amazon S3 **Log Delivery** group\. The `PutObject` call includes the object data in the request body and the ACL grants in the request headers \(see [PUT Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html)\)\. 

**Example**  

```
static string bucketName = "*** Provide existing bucket name ***";
static string newBucketName = "*** Provide a name for a new bucket ***";
static string newKeyName = "*** Provide a name for a new key ***";

IAmazonS3 client;
client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1);
				
// Retrieve ACL from one of the owner's buckets
S3AccessControlList acl = client.GetACL(new GetACLRequest
{
    BucketName = bucketName,
}).AccessControlList;
        
// Describe grant for full control for owner.
S3Grant grant1 = new S3Grant
{
    Grantee = new S3Grantee { CanonicalUser = acl.Owner.Id },
    Permission = S3Permission.FULL_CONTROL
};

// Describe grant for write permission for the LogDelivery group.
S3Grant grant2 = new S3Grant
{
    Grantee = new S3Grantee { URI = "http://acs.amazonaws.com/groups/s3/LogDelivery" },
    Permission = S3Permission.WRITE
};

PutBucketRequest request = new PutBucketRequest()
{
    BucketName = newBucketName,
    BucketRegion = S3Region.US,
    Grants = new List<S3Grant> { grant1, grant2 }
};
PutBucketResponse response = client.PutBucket(request);

PutObjectRequest objectRequest = new PutObjectRequest()
{  
    ContentBody = "Object data for simple put.",
    BucketName = newBucketName,
    Key = newKeyName,
    Grants = new List<S3Grant> { grant1 }
};
PutObjectResponse objectResponse = client.PutObject(objectRequest);
```

For more information about uploading objects, see [Working with Amazon S3 Objects](UsingObjects.md)\. 

In the preceding code example, for each `S3Grant` you explicitly identify a grantee and permission\. Alternatively, you can specify a canned \(predefined\) ACL \(see [Canned ACL ](acl-overview.md#canned-acl)\) in your request when creating a resource\. The following C\# code example creates an object and specifies a `LogDeliveryWrite` canned ACL in the request to grant the **Log Delivery** group WRITE and READ\_ACP permissions on the bucket\.

**Example**  

```
 1. static string newBucketName = "*** Provide existing bucket name ***";
 2. static string keyName = "*** Provide key name ***";
 3. 
 4. IAmazonS3 client;
 5. client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1);
 6. 
 7. PutBucketRequest request = new PutBucketRequest()
 8. {
 9.    BucketName = newBucketName,
10.    BucketRegion = S3Region.US,
11.    // Add canned ACL.
12.    CannedACL = S3CannedACL.LogDeliveryWrite
13. };
14. PutBucketResponse response = client.PutBucket(request);
```

For information about the underlying REST API, go to [PUT Bucket](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUT.html)\.

## Updating ACL on an Existing Resource<a name="set-acl-dot-net-existing-resource"></a>

You can set an ACL on an existing object or a bucket by calling the `AmazonS3Client.PutACL` method\. You create an instance of the `S3AccessControlList` class with a list of ACL grants and include the list in the `PutACL` request\. 

The following C\# code example reads an existing ACL first, using the `AmazonS3Client.GetACL` method, add new grants to it, and then sets the revised ACL on the object\.

**Example**  

```
static string bucketName = "*** Provide existing bucket name ***";
static string keyName = "*** Provide key name ***";

IAmazonS3 client;
client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1);

// Retrieve ACL for object
S3AccessControlList acl = client.GetACL(new GetACLRequest
{
    BucketName = bucketName,
    Key = keyName
}).AccessControlList;

// Retrieve owner
Owner owner = acl.Owner;

// Clear existing grants.
acl.Grants.Clear();

// First, add grant to reset owner's full permission 
// (previous clear statement removed all permissions).
S3Grant grant0 = new S3Grant
{
    Grantee = new S3Grantee { CanonicalUser = acl.Owner.Id }
};
acl.AddGrant(grant0.Grantee, S3Permission.FULL_CONTROL);

// Describe grant for permission using email address.
S3Grant grant1 = new S3Grant
{
    Grantee = new S3Grantee { EmailAddress = emailAddress },
    Permission = S3Permission.WRITE_ACP
};

// Describe grant for permission to the LogDelivery group.
S3Grant grant2 = new S3Grant
{
    Grantee = new S3Grantee { URI = "http://acs.amazonaws.com/groups/s3/LogDelivery" },
    Permission = S3Permission.WRITE
};

// Create new ACL.
S3AccessControlList newAcl = new S3AccessControlList
{
    Grants = new List<S3Grant> { grant1, grant2 },
    Owner = owner
};

// Set new ACL.
PutACLResponse response = client.PutACL(new PutACLRequest
{
    BucketName = bucketName,
    Key = keyName,
    AccessControlList = newAcl
});
```

Instead of creating `S3Grant` objects and specifying grantee and permission explicitly, you can also specify a canned ACL in your request\. The following C\# code example sets a canned ACL on a new bucket\. The sample request specifies an `AuthenticatedRead` canned ACL to grant read access to the Amazon S3 `Authenticated Users` group\.

**Example**  

```
static string newBucketName = "*** Provide new bucket name ***";

IAmazonS3 client;
client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1);

PutBucketRequest request = new PutBucketRequest()
{
   BucketName = newBucketName,
   BucketRegion = S3Region.US,
   // Add canned ACL.
   CannedACL = S3CannedACL.AuthenticatedRead
};
PutBucketResponse response = client.PutBucket(request);
```

## An Example<a name="set-acl-dot-net-create-resource-example"></a>

The following C\# code example performs the following tasks:
+ Create a bucket\. In the request, it specifies a `log-delivery-write` canned ACL, granting write permission to the `LogDelivery` Amazon S3 group\.
+ Read the ACL on the bucket\.
+ Clear existing permissions and add new the permission to the ACL\.
+ Call `PutACL` request to add the new ACL to the bucket\.

 For instructions on how to create and test a working example, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\. 

```
using System;
using System.Collections.Specialized;
using System.Configuration;
using Amazon.S3;
using Amazon.S3.Model;
using Amazon.S3.Util;
using System.Collections.Generic;

namespace s3.amazon.com.docsamples
{
  class ManageACLs
  {
    static string bucketName    = "*** Provide existing bucket name ***";
    static string newBucketName = "*** Provide a name for a new bucket ***";
    static string keyName       = "*** Provide key name ***";
    static string newKeyName    = "*** Provide a name for a new key ***";
    static string emailAddress  = "*** Provide email address ***";

    static IAmazonS3 client;

    public static void Main(string[] args)
    {
      try
      {
        using (client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1))
        {
          // Add bucket (specify canned ACL).
          AddBucketWithCannedACL(newBucketName);

          // Get ACL on a bucket.
          GetBucketACL(bucketName);

          // Add (replace) ACL on an object in a bucket.
          AddACLToExistingObject(bucketName, keyName);

          Console.WriteLine("Example complete.");
        }
      }
      catch (AmazonS3Exception amazonS3Exception)
      {
        if (amazonS3Exception.ErrorCode != null &&
            (amazonS3Exception.ErrorCode.Equals("InvalidAccessKeyId")
            ||
            amazonS3Exception.ErrorCode.Equals("InvalidSecurity")))
        {
          Console.WriteLine("Check the provided AWS Credentials.");
          Console.WriteLine("For service sign up go to http://aws.amazon.com/s3");
        }
        else
        {
          Console.WriteLine(
              "Error occurred. Message:'{0}' when writing an object"
              , amazonS3Exception.Message);
        }
      }
      catch (Exception e)
      {
        Console.WriteLine(e.Message);
      }

      Console.WriteLine("Press any key to continue...");
      Console.ReadKey();
    }

    static void AddBucketWithCannedACL(string bucketName)
    {
       PutBucketRequest request = new PutBucketRequest()
       {
           BucketName = newBucketName,
           BucketRegion = S3Region.US,
           // Add canned ACL.
           CannedACL = S3CannedACL.LogDeliveryWrite
       };
       PutBucketResponse response = client.PutBucket(request);
     }

    static void GetBucketACL(string bucketName)
    {
      GetACLResponse response = client.GetACL(new GetACLRequest
      {
         BucketName = bucketName
      });
      
      //   GetACLResponse response = client.GetACL(request);
      S3AccessControlList accessControlList = response.AccessControlList;
      //response.Dispose();
    }

    static void AddACLToExistingObject(string bucketName, string keyName)
    {
        // Retrieve ACL for object
        S3AccessControlList acl = client.GetACL(new GetACLRequest
        {
            BucketName = bucketName,
            Key = keyName
        }).AccessControlList;

        // Retrieve owner
        Owner owner = acl.Owner;

        // Clear existing grants.
        acl.Grants.Clear();

        // First, add grant to reset owner's full permission 
        // (previous clear statement removed all permissions).
        S3Grant grant0 = new S3Grant
        {
            Grantee = new S3Grantee { CanonicalUser = acl.Owner.Id }
        };
        acl.AddGrant(grant0.Grantee, S3Permission.FULL_CONTROL);

        // Describe grant for permission using email address.
        S3Grant grant1 = new S3Grant
        {
            Grantee = new S3Grantee { EmailAddress = emailAddress },
            Permission = S3Permission.WRITE_ACP
        };

        // Describe grant for permission to the LogDelivery group.
        S3Grant grant2 = new S3Grant
        {
            Grantee = new S3Grantee { URI = "http://acs.amazonaws.com/groups/s3/LogDelivery" },
            Permission = S3Permission.WRITE
        };

        // Create new ACL.
        S3AccessControlList newAcl = new S3AccessControlList
        {
            Grants = new List<S3Grant> { grant1, grant2 },
            Owner = owner
        };

        // Set new ACL.
        PutACLResponse response = client.PutACL(new PutACLRequest
        {
            BucketName = bucketName,
            Key = keyName,
            AccessControlList = newAcl
        });

        // Get and print response.
        Console.WriteLine(client.GetACL(new GetACLRequest()
        {
            BucketName = bucketName,
            Key = keyName
        }
        ));
    }
  }
}
```