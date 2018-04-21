# Managing ACLs Using the AWS SDK for Java<a name="acl-using-java-sdk"></a>

## Setting an ACL When Creating a Resource<a name="set-acl-java-create-resource"></a>

When creating a resource \(buckets and objects\), you can grant permissions \(see [Access Control List \(ACL\) Overview](acl-overview.md)\) by adding an `AccessControlList` in your request\. For each permission, you explicitly specify the grantee and the permission\.

For example, the following Java code snippet sends a `PutObject` request to upload an object\. In the request, the code snippet specifies permissions to two AWS accounts and the Amazon S3 `AllUsers` group\. The `PutObject` call includes the object data in the request body and the ACL grants in the request headers \(see [PUT Object](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html)\)\. 

```
String bucketName     = "bucket-name";
String keyName        = "object-key";
String uploadFileName = "file-name";

AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());

AccessControlList acl = new AccessControlList();
acl.grantPermission(new CanonicalGrantee("d25639fbe9c19cd30a4c0f43fbf00e2d3f96400a9aa8dabfbbebe1906Example"), Permission.ReadAcp);
acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
acl.grantPermission(new EmailAddressGrantee("user@email.com"), Permission.WriteAcp);

File file = new File(uploadFileName);
s3client.putObject(new PutObjectRequest(bucketName, keyName, file).withAccessControlList(acl));
```

For more information about uploading objects, see [Working with Amazon S3 Objects](UsingObjects.md)\. 

In the preceding code snippet, in granting each permission you explicitly identified a grantee and a permission\. Alternatively, you can specify a canned \(predefined\) ACL \(see [Canned ACL ](acl-overview.md#canned-acl)\) in your request when creating a resource\. The following Java code snippet creates a bucket and specifies a `LogDeliveryWrite` canned ACL in the request to grant write permission to the Amazon S3 `LogDelivery` group\. 

**Example**  

```
1. String bucketName     = "bucket-name";
2. AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
3. 
4. s3client.createBucket(new CreateBucketRequest (bucketName).withCannedAcl(CannedAccessControlList.LogDeliveryWrite));
```

For information about the underlying REST API, go to [PUT Bucket](http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUT.html)\.

## Updating ACL on an Existing Resource<a name="set-acl-java-existing-resource"></a>

You can set ACL on an existing object or a bucket\. You create an instance of the `AccessControlList` class and grant permissions and call the appropriate set ACL method\. The following Java code snippet calls the `setObjectAcl` method to set ACL on an existing object\. 

```
String bucketName     = "bucket-name";
String keyName        = "object-key";

AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());

AccessControlList acl = new AccessControlList();
acl.grantPermission(new CanonicalGrantee("d25639fbe9c19cd30a4c0f43fbf00e2d3f96400a9aa8dabfbbebe1906Example"), Permission.ReadAcp);
acl.grantPermission(GroupGrantee.AuthenticatedUsers, Permission.Read);
acl.grantPermission(new EmailAddressGrantee("user@email.com"), Permission.WriteAcp);
Owner owner = new Owner();
owner.setId("852b113e7a2f25102679df27bb0ae12b3f85be6f290b936c4393484beExample");
owner.setDisplayName("display-name");
acl.setOwner(owner);

s3client.setObjectAcl(bucketName, keyName, acl);
```

**Note**  
In the preceding code snippet, you can optionally read an existing ACL first, by calling the `getObjectAcl` method, add new grants to it, and then set the revised ACL on the resource\.

Instead of granting permissions by explicitly specifying grantees and permissions explicitly, you can also specify a canned ACL in your request\. The following Java code snippet sets the ACL on an existing object\. In the request, the snippet specifies the canned ACL `AuthenticatedRead` to grant read access to the Amazon S3 `Authenticated Users` group\.

```
String bucketName     = "bucket-name";
String keyName        = "object-key";

AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());

s3client.setObjectAcl(bucketName, keyName, CannedAccessControlList.AuthenticatedRead);
```

## An Example<a name="set-acl-java-create-resource-example"></a>

The following Java code example first creates a bucket\. In the create request, it specifies a `public-read` canned ACL\. It then retrieves the ACL in an `AccessControlList` instance, clears grants, and adds new grants to the `AccessControlList`\. Finally, it saves the updated `AccessControlList`, that is, it replaces the bucket ACL subresource\.

The following Java code example performs the following tasks:
+ Create a bucket\. In the request, it specifies a `log-delivery-write` canned ACL, granting write permission to the `LogDelivery` Amazon S3 group\.
+ Read the ACL on the bucket\.
+ Clear existing permissions and add the new permission to the ACL\.
+ Call `setBucketAcl` to add the new ACL to the bucket\. 

**Note**  
To test the following code example, you must update the code and provide your credentials, and also provide the canonical user ID and email address of the accounts that you want to grant permissions to\.

```
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CanonicalGrantee;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.Grant;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.Region;


public class ACLExample {
	private static String bucketName = "*** Provide bucket name ***";
	
	public static void main(String[] args) throws IOException {
        AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
        
        Collection<Grant> grantCollection = new ArrayList<Grant>();
        try {
            // 1. Create bucket with Canned ACL.
            CreateBucketRequest createBucketRequest = 
            	new CreateBucketRequest(bucketName, Region.US_Standard).withCannedAcl(CannedAccessControlList.LogDeliveryWrite);  
            
            Bucket resp = s3Client.createBucket(createBucketRequest);

            // 2. Update ACL on the existing bucket.
            AccessControlList bucketAcl = s3Client.getBucketAcl(bucketName);
           
            
            // (Optional) delete all grants.
            bucketAcl.getGrants().clear();
            
            // Add grant - owner.
            Grant grant0 = new Grant(
            		new CanonicalGrantee("852b113e7a2f25102679df27bb0ae12b3f85be6f290b936c4393484beExample"), 
            		Permission.FullControl);
            grantCollection.add(grant0);       
            
            // Add grant using canonical user id.
            Grant grant1 = new Grant(
            		new CanonicalGrantee("d25639fbe9c19cd30a4c0f43fbf00e2d3f96400a9aa8dabfbbebe1906Example"),
            		Permission.Write);        
            grantCollection.add(grant1);
                       
            // Grant LogDelivery group permission to write to the bucket.
            Grant grant3 = new Grant(GroupGrantee.LogDelivery, 
            		                 Permission.Write);
            grantCollection.add(grant3);
            
           bucketAcl.getGrants().addAll(grantCollection);

            // Save (replace) ACL.
            s3Client.setBucketAcl(bucketName, bucketAcl);
            
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which" +
            		" means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means"+
            		" the client encountered " +
                    "a serious internal problem while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
}
```