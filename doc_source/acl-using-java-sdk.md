# Managing ACLs Using the AWS SDK for Java<a name="acl-using-java-sdk"></a>

This section provides examples of how to configure access control list \(ACL\) grants on buckets and objects\. The first example creates a bucket with a canned ACL \(see [Canned ACL](acl-overview.md#canned-acl)\), creates a list of custom permission grants, and then replaces the canned ACL with an ACL containing the custom grants\. The second example shows how to modify an ACL using the `AccessControlList.grantPermission()` method\.

## Setting ACL Grants<a name="set-acl-java-create-resource"></a>

**Example**  
This example creates a bucket\. In the request, the example specifies a canned ACL that grants the Log Delivery group permission to write logs to the bucket\.   

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CanonicalGrantee;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.Grant;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;

public class CreateBucketWithACL {

    public static void main(String[] args) throws IOException {
        String clientRegion = "*** Client region ***";
        String bucketName = "*** Bucket name ***";

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(clientRegion)
                    .build();

            // Create a bucket with a canned ACL. This ACL will be deleted by the
            // getGrantsAsList().clear() call below. It is here for demonstration
            // purposes.
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName, clientRegion)
                    .withCannedAcl(CannedAccessControlList.LogDeliveryWrite);
            s3Client.createBucket(createBucketRequest);
    
            // Create a collection of grants to add to the bucket.
            Collection<Grant> grantCollection = new ArrayList<Grant>();
            
            // Grant the account owner full control.
            Grant grant1 = new Grant(new CanonicalGrantee(s3Client.getS3AccountOwner().getId()), Permission.FullControl);
            grantCollection.add(grant1);
    
            // Grant the LogDelivery group permission to write to the bucket.
            Grant grant2 = new Grant(GroupGrantee.LogDelivery, Permission.Write);
            grantCollection.add(grant2);
    
            // Save (replace) grants by deleting all current ACL grants and replacing
            // them with the two we just created.
            AccessControlList bucketAcl = s3Client.getBucketAcl(bucketName);
            bucketAcl.getGrantsAsList().clear();
            bucketAcl.getGrantsAsList().addAll(grantCollection);
            s3Client.setBucketAcl(bucketName, bucketAcl);
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it and returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }
}
```

## Configuring ACL Grants on an Existing Object<a name="set-acl-java-existing-resource-example"></a>

**Example**  
This example updates the ACL on an object\. The example performs the following tasks:   
+ Retrieves an object's ACL
+ Clears the ACL by removing all existing permissions
+ Adds two permissions: full access to the owner, and WRITE\_ACP \(see [What Permissions Can I Grant?](acl-overview.md#permissions)\) to a user identified by an email address
+ Saves the ACL to the object

```
// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

import java.io.IOException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.CanonicalGrantee;
import com.amazonaws.services.s3.model.EmailAddressGrantee;
import com.amazonaws.services.s3.model.Permission;

public class ModifyACLExistingObject {

    public static void main(String[] args) throws IOException {
        String clientRegion = "*** Client region ***";
        String bucketName = "*** Bucket name ***";
        String keyName = "*** Key name ***";
        String emailGrantee = "*** user@example.com ***";

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(clientRegion)
                    .build();

            // Get the existing object ACL that we want to modify.
            AccessControlList acl = s3Client.getObjectAcl(bucketName, keyName);
            
            // Clear the existing list of grants.
            acl.getGrantsAsList().clear();
            
            // Grant a sample set of permissions, using the existing ACL owner for Full Control permissions.
            acl.grantPermission(new CanonicalGrantee(acl.getOwner().getId()), Permission.FullControl);
            acl.grantPermission(new EmailAddressGrantee(emailGrantee), Permission.WriteAcp);
    
            // Save the modified ACL back to the object.
            s3Client.setObjectAcl(bucketName, keyName, acl);
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }
}
```