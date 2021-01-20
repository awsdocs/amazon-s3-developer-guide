# Example 1: Configuring replication when the source and destination buckets are owned by the same account<a name="replication-walkthrough1"></a>

In this example, you set up replication for source and destination buckets that are owned by the same AWS account\. Examples are provided for using the Amazon S3 console, the AWS Command Line Interface \(AWS CLI\), and the AWS SDK for Java and AWS SDK for \.NET\.

**Topics**

## Configure replication when buckets are owned by the same account \(console\)<a name="replication-ex1-console"></a>

For step\-by\-step instructions, see [How Do I Add a Replication Rule to an S3 Bucket?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-replication.html) in the *Amazon Simple Storage Service Console User Guide*\. This topic provides instructions for setting replication configuration when buckets are owned by same and different AWS accounts\.

## Configure replication when buckets are owned by the same account \(AWS CLI\)<a name="replication-ex1-cli"></a>

To use the AWS CLI to set up replication when the source and destination buckets are owned by the same AWS account, you create source and destination buckets, enable versioning on the buckets, create an IAM role that gives Amazon S3 permission to replicate objects, and add the replication configuration to the source bucket\. To verify your setup, you test it\.

**To set up replication when source and destination buckets are owned by the same AWS account**

1. Set a credentials profile for the AWS CLI\. In this example, we use the profile name `acctA`\. For information about setting credential profiles, see [Named Profiles](https://docs.aws.amazon.com/cli/latest/userguide/cli-multiple-profiles.html) in the *AWS Command Line Interface User Guide*\. 
**Important**  
The profile you use for this exercise must have the necessary permissions\. For example, in the replication configuration, you specify the IAM role that Amazon S3 can assume\. You can do this only if the profile you use has the `iam:PassRole` permission\. For more information, see [Granting a User Permissions to Pass a Role to an AWS Service](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_use_passrole.html) in the *IAM User Guide*\. If you use administrator user credentials to create a named profile, you can perform all the tasks\. 

1. Create a *source* bucket and enable versioning on it\. The following code creates a *source* bucket in the US East \(N\. Virginia\) \(us\-east\-1\) Region\.

   

   ```
   aws s3api create-bucket \
   --bucket source \
   --region us-east-1 \
   --profile acctA
   ```

   ```
   aws s3api put-bucket-versioning \
   --bucket source \
   --versioning-configuration Status=Enabled \
   --profile acctA
   ```

1. Create a *destination* bucket and enable versioning on it\. The following code creates a *destination* bucket in the US West \(Oregon\) \(us\-west\-2\) Region\. 
**Note**  
To set up replication configuration when both source and destination buckets are in the same AWS account, you use the same profile\. This example uses `acctA`\. To test replication configuration when the buckets are owned by different AWS accounts, you specify different profiles for each\. This example uses the `acctB` profile for the destination bucket\.

   

   ```
   aws s3api create-bucket \
   --bucket destination \
   --region us-west-2 \
   --create-bucket-configuration LocationConstraint=us-west-2 \
   --profile acctA
   ```

   ```
   aws s3api put-bucket-versioning \
   --bucket destination \
   --versioning-configuration Status=Enabled \
   --profile acctA
   ```

1. Create an IAM role\. You specify this role in the replication configuration that you add to the *source* bucket later\. Amazon S3 assumes this role to replicate objects on your behalf\. You create an IAM role in two steps:
   + Create a role\.
   + Attach a permissions policy to the role\.

   1. Create the IAM role\.

      1. Copy the following trust policy and save it to a file named `S3-role-trust-policy.json` in the current directory on your local computer\. This policy grants Amazon S3 service principal permissions to assume the role\.

         ```
         {
            "Version":"2012-10-17",
            "Statement":[
               {
                  "Effect":"Allow",
                  "Principal":{
                     "Service":"s3.amazonaws.com"
                  },
                  "Action":"sts:AssumeRole"
               }
            ]
         }
         ```

      1. Run the following command to create a role\.

         ```
         $ aws iam create-role \
         --role-name replicationRole \
         --assume-role-policy-document file://s3-role-trust-policy.json  \
         --profile acctA
         ```

   1. Attach a permissions policy to the role\.

      1. Copy the following permissions policy and save it to a file named `S3-role-permissions-policy.json` in the current directory on your local computer\. This policy grants permissions for various Amazon S3 bucket and object actions\. 

         ```
         {
            "Version":"2012-10-17",
            "Statement":[
               {
                  "Effect":"Allow",
                  "Action":[
                     "s3:GetObjectVersionForReplication",
                     "s3:GetObjectVersionAcl"
                  ],
                  "Resource":[
                     "arn:aws:s3:::source-bucket/*"
                  ]
               },
               {
                  "Effect":"Allow",
                  "Action":[
                     "s3:ListBucket",
                     "s3:GetReplicationConfiguration"
                  ],
                  "Resource":[
                     "arn:aws:s3:::source-bucket"
                  ]
               },
               {
                  "Effect":"Allow",
                  "Action":[
                     "s3:ReplicateObject",
                     "s3:ReplicateDelete",
                     "s3:ReplicateTags",
                     "s3:GetObjectVersionTagging"
         
                  ],
                  "Resource":"arn:aws:s3:::destination-bucket/*"
               }
            ]
         }
         ```

      1. Run the following command to create a policy and attach it to the role\.

         ```
         $ aws iam put-role-policy \
         --role-name replicationRole \
         --policy-document file://s3-role-permissions-policy.json \
         --policy-name replicationRolePolicy \
         --profile acctA
         ```

1. Add replication configuration to the *source* bucket\. 

   1. Although the Amazon S3 API requires replication configuration as XML, the AWS CLI requires that you specify the replication configuration as JSON\. Save the following JSON in a file called `replication.json` to the local directory on your computer\.

      ```
      {
        "Role": "IAM-role-ARN",
        "Rules": [
          {
            "Status": "Enabled",
            "Priority": 1,
            "DeleteMarkerReplication": { "Status": "Disabled" },
            "Filter" : { "Prefix": "Tax"},
            "Destination": {
              "Bucket": "arn:aws:s3:::destination-bucket"
            }
          }
        ]
      }
      ```

   1. Update the JSON by providing values for the *destination\-bucket* and *IAM\-role\-ARN*\. Save the changes\.

   1. Run the following command to add the replication configuration to your source bucket\. Be sure to provide the *source* bucket name\.

      ```
      $ aws s3api put-bucket-replication \
      --replication-configuration file://replication.json \
      --bucket source \
      --profile acctA
      ```

   To retrieve the replication configuration, use the `get-bucket-replication` command\.

   ```
   $ aws s3api get-bucket-replication \
   --bucket source \
   --profile acctA
   ```

1. Test the setup in the Amazon S3 console: 

   1.  Sign in to the AWS Management Console and open the Amazon S3 console at [https://console\.aws\.amazon\.com/s3/](https://console.aws.amazon.com/s3/)\. 

   1. In the *source* bucket, create a folder named `Tax`\. 

   1. Add sample objects to the `Tax` folder in the *source* bucket\. 
**Note**  
The amount of time it takes for Amazon S3 to replicate an object depends on the size of the object\. For information about how to see the status of replication, see [Replication status information](replication-status.md)\.

      In the *destination* bucket, verify the following:
      + That Amazon S3 replicated the objects\.
      + In object **properties**, that the **Replication Status** is set to `Replica` \(identifying this as a replica object\)\.
      + In object **properties**, that the permission section shows no permissions\. This means that the replica is still owned by the *source* bucket owner, and the *destination* bucket owner has no permission on the object replica\. You can add optional configuration to tell Amazon S3 to change the replica ownership\. For an example, see [Example 3: Changing the replica owner when the source and destination buckets are owned by different accounts](replication-walkthrough-3.md)\.   
![\[Screen shot of object properties showing the replication status and permissions.\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/images/crr-wt2-10.png)

       

   1. Update an object's ACL in the *source* bucket and verify that changes appear in the *destination* bucket\.

      For instructions, see [How Do I Set Permissions on an Object?](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/set-object-permissions.html) in the *Amazon Simple Storage Service Console User Guide*\.

## Configure replication when buckets are owned by the same account \(AWS SDK\)<a name="replication-ex1-sdk"></a>

Use the following code examples to add a replication configuration to a bucket with the AWS SDK for Java and AWS SDK for \.NET, respectively\.

------
#### [ Java ]

The following example adds a replication configuration to a bucket and then retrieves and verifies the configuration\. For instructions on creating and testing a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\. 

```
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder;
import com.amazonaws.services.identitymanagement.model.CreateRoleRequest;
import com.amazonaws.services.identitymanagement.model.PutRolePolicyRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.BucketReplicationConfiguration;
import com.amazonaws.services.s3.model.BucketVersioningConfiguration;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteMarkerReplication;
import com.amazonaws.services.s3.model.DeleteMarkerReplicationStatus;
import com.amazonaws.services.s3.model.ReplicationDestinationConfig;
import com.amazonaws.services.s3.model.ReplicationRule;
import com.amazonaws.services.s3.model.ReplicationRuleStatus;
import com.amazonaws.services.s3.model.SetBucketVersioningConfigurationRequest;
import com.amazonaws.services.s3.model.StorageClass;
import com.amazonaws.services.s3.model.replication.ReplicationFilter;
import com.amazonaws.services.s3.model.replication.ReplicationFilterPredicate;
import com.amazonaws.services.s3.model.replication.ReplicationPrefixPredicate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrossRegionReplication {

    public static void main(String[] args) throws IOException {
        Regions clientRegion = Regions.DEFAULT_REGION;
        String accountId = "*** Account ID ***";
        String roleName = "*** Role name ***";
        String sourceBucketName = "*** Source bucket name ***";
        String destBucketName = "*** Destination bucket name ***";
        String prefix = "Tax/";

        String roleARN = String.format("arn:aws:iam::%s:role/%s", accountId, roleName);
        String destinationBucketARN = "arn:aws:s3:::" + destBucketName;

        AmazonS3 s3Client = AmazonS3Client.builder()
            .withCredentials(new ProfileCredentialsProvider())
            .withRegion(clientRegion)
            .build();

        createBucket(s3Client, clientRegion, sourceBucketName);
        createBucket(s3Client, clientRegion, destBucketName);
        assignRole(roleName, clientRegion, sourceBucketName, destBucketName);


        try {


            // Create the replication rule.
            List<ReplicationFilterPredicate> andOperands = new ArrayList<ReplicationFilterPredicate>();
            andOperands.add(new ReplicationPrefixPredicate(prefix));


            Map<String, ReplicationRule> replicationRules = new HashMap<String, ReplicationRule>();
            replicationRules.put("ReplicationRule1",
                new ReplicationRule()
                    .withPriority(0)
                    .withStatus(ReplicationRuleStatus.Enabled)
                    .withDeleteMarkerReplication(new DeleteMarkerReplication().withStatus(DeleteMarkerReplicationStatus.DISABLED))
                    .withFilter(new ReplicationFilter().withPredicate(new ReplicationPrefixPredicate(prefix)))
                    .withDestinationConfig(new ReplicationDestinationConfig()
                        .withBucketARN(destinationBucketARN)
                        .withStorageClass(StorageClass.Standard)));

            // Save the replication rule to the source bucket.
            s3Client.setBucketReplicationConfiguration(sourceBucketName,
                new BucketReplicationConfiguration()
                    .withRoleARN(roleARN)
                    .withRules(replicationRules));

            // Retrieve the replication configuration and verify that the configuration
            // matches the rule we just set.
            BucketReplicationConfiguration replicationConfig = s3Client.getBucketReplicationConfiguration(sourceBucketName);
            ReplicationRule rule = replicationConfig.getRule("ReplicationRule1");
            System.out.println("Retrieved destination bucket ARN: " + rule.getDestinationConfig().getBucketARN());
            System.out.println("Retrieved priority: " + rule.getPriority());
            System.out.println("Retrieved source-bucket replication rule status: " + rule.getStatus());
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }

    private static void createBucket(AmazonS3 s3Client, Regions region, String bucketName) {
        CreateBucketRequest request = new CreateBucketRequest(bucketName, region.getName());
        s3Client.createBucket(request);
        BucketVersioningConfiguration configuration = new BucketVersioningConfiguration().withStatus(BucketVersioningConfiguration.ENABLED);

        SetBucketVersioningConfigurationRequest enableVersioningRequest = new SetBucketVersioningConfigurationRequest(bucketName, configuration);
        s3Client.setBucketVersioningConfiguration(enableVersioningRequest);


    }

    private static void assignRole(String roleName, Regions region, String sourceBucket, String destinationBucket) {
        AmazonIdentityManagement iamClient = AmazonIdentityManagementClientBuilder.standard()
            .withRegion(region)
            .withCredentials(new ProfileCredentialsProvider())
            .build();
        StringBuilder trustPolicy = new StringBuilder();
        trustPolicy.append("{\\r\\n   ");
        trustPolicy.append("\\\"Version\\\":\\\"2012-10-17\\\",\\r\\n   ");
        trustPolicy.append("\\\"Statement\\\":[\\r\\n      {\\r\\n         ");
        trustPolicy.append("\\\"Effect\\\":\\\"Allow\\\",\\r\\n         \\\"Principal\\\":{\\r\\n            ");
        trustPolicy.append("\\\"Service\\\":\\\"s3.amazonaws.com\\\"\\r\\n         },\\r\\n         ");
        trustPolicy.append("\\\"Action\\\":\\\"sts:AssumeRole\\\"\\r\\n      }\\r\\n   ]\\r\\n}");

        CreateRoleRequest createRoleRequest = new CreateRoleRequest()
            .withRoleName(roleName)
            .withAssumeRolePolicyDocument(trustPolicy.toString());

        iamClient.createRole(createRoleRequest);

        StringBuilder permissionPolicy = new StringBuilder();
        permissionPolicy.append("{\\r\\n   \\\"Version\\\":\\\"2012-10-17\\\",\\r\\n   \\\"Statement\\\":[\\r\\n      {\\r\\n         ");
        permissionPolicy.append("\\\"Effect\\\":\\\"Allow\\\",\\r\\n         \\\"Action\\\":[\\r\\n             ");
        permissionPolicy.append("\\\"s3:GetObjectVersionForReplication\\\",\\r\\n            ");
        permissionPolicy.append("\\\"s3:GetObjectVersionAcl\\\"\\r\\n         ],\\r\\n         \\\"Resource\\\":[\\r\\n            ");
        permissionPolicy.append("\\\"arn:aws:s3:::");
        permissionPolicy.append(sourceBucket);
        permissionPolicy.append("/*\\\"\\r\\n         ]\\r\\n      },\\r\\n      {\\r\\n         ");
        permissionPolicy.append("\\\"Effect\\\":\\\"Allow\\\",\\r\\n         \\\"Action\\\":[\\r\\n            ");
        permissionPolicy.append("\\\"s3:ListBucket\\\",\\r\\n            \\\"s3:GetReplicationConfiguration\\\"\\r\\n         ");
        permissionPolicy.append("],\\r\\n         \\\"Resource\\\":[\\r\\n            \\\"arn:aws:s3:::");
        permissionPolicy.append(sourceBucket);
        permissionPolicy.append("\\r\\n         ");
        permissionPolicy.append("]\\r\\n      },\\r\\n      {\\r\\n         \\\"Effect\\\":\\\"Allow\\\",\\r\\n         ");
        permissionPolicy.append("\\\"Action\\\":[\\r\\n            \\\"s3:ReplicateObject\\\",\\r\\n            ");
        permissionPolicy.append("\\\"s3:ReplicateDelete\\\",\\r\\n            \\\"s3:ReplicateTags\\\",\\r\\n            ");
        permissionPolicy.append("\\\"s3:GetObjectVersionTagging\\\"\\r\\n\\r\\n         ],\\r\\n         ");
        permissionPolicy.append("\\\"Resource\\\":\\\"arn:aws:s3:::");
        permissionPolicy.append(destinationBucket);
        permissionPolicy.append("/*\\\"\\r\\n      }\\r\\n   ]\\r\\n}");

        PutRolePolicyRequest putRolePolicyRequest = new PutRolePolicyRequest()
            .withRoleName(roleName)
            .withPolicyDocument(permissionPolicy.toString())
            .withPolicyName("crrRolePolicy");

        iamClient.putRolePolicy(putRolePolicyRequest);


    }
}
```

------
#### [ C\# ]

The following AWS SDK for \.NET code example adds a replication configuration to a bucket and then retrieves it\. To use this code, provide the names for your buckets and the Amazon Resource Name \(ARN\) for your IAM role\. For instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.

```
using Amazon;
using Amazon.S3;
using Amazon.S3.Model;
using System;
using System.Threading.Tasks;

namespace Amazon.DocSamples.S3
{
    class CrossRegionReplicationTest
    {
        private const string sourceBucket = "*** source bucket ***";
        // Bucket ARN example - arn:aws:s3:::destinationbucket
        private const string destinationBucketArn = "*** destination bucket ARN ***";
        private const string roleArn = "*** IAM Role ARN ***";
        // Specify your bucket region (an example region is shown).
        private static readonly RegionEndpoint sourceBucketRegion = RegionEndpoint.USWest2;
        private static IAmazonS3 s3Client;
        public static void Main()
        {
            s3Client = new AmazonS3Client(sourceBucketRegion);
            EnableReplicationAsync().Wait();
        }
        static async Task EnableReplicationAsync()
        {
            try
            {
                ReplicationConfiguration replConfig = new ReplicationConfiguration
                {
                    Role = roleArn,
                    Rules =
                        {
                            new ReplicationRule
                            {
                                Prefix = "Tax",
                                Status = ReplicationRuleStatus.Enabled,
                                Destination = new ReplicationDestination
                                {
                                    BucketArn = destinationBucketArn
                                }
                            }
                        }
                };

                PutBucketReplicationRequest putRequest = new PutBucketReplicationRequest
                {
                    BucketName = sourceBucket,
                    Configuration = replConfig
                };

                PutBucketReplicationResponse putResponse = await s3Client.PutBucketReplicationAsync(putRequest);

                // Verify configuration by retrieving it.
                await RetrieveReplicationConfigurationAsync(s3Client);
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
        private static async Task RetrieveReplicationConfigurationAsync(IAmazonS3 client)
        {
            // Retrieve the configuration.
            GetBucketReplicationRequest getRequest = new GetBucketReplicationRequest
            {
                BucketName = sourceBucket
            };
            GetBucketReplicationResponse getResponse = await client.GetBucketReplicationAsync(getRequest);
            // Print.
            Console.WriteLine("Printing replication configuration information...");
            Console.WriteLine("Role ARN: {0}", getResponse.Configuration.Role);
            foreach (var rule in getResponse.Configuration.Rules)
            {
                Console.WriteLine("ID: {0}", rule.Id);
                Console.WriteLine("Prefix: {0}", rule.Prefix);
                Console.WriteLine("Status: {0}", rule.Status);
            }
        }
    }
}
```

------