# Setting Up Cross\-Region Replication Using the AWS SDK for Java<a name="crr-using-java"></a>

When the source and destination buckets are owned by two different AWS accounts, you can use either the AWS CLI or one of the AWS SDKs to add a replication configuration to the source bucket\. You can't use the console to add the replication configuration, because the console doesn't provide a way to specify a destination bucket owned by another AWS account when you add a replication configuration to a source bucket\. For more information, see [Setting Up Cross\-Region Replication](crr-how-setup.md)\. 

The following example adds a replication configuration to a bucket and then retrieves and verifies the configuration\. For instructions on creating and testing a working sample, see [Testing the Amazon S3 Java Code Examples](UsingTheMPJavaAPI.md#TestingJavaSamples)\. 

```
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketReplicationConfiguration;
import com.amazonaws.services.s3.model.ReplicationDestinationConfig;
import com.amazonaws.services.s3.model.ReplicationRule;
import com.amazonaws.services.s3.model.ReplicationRuleStatus;
import com.amazonaws.services.s3.model.StorageClass;

public class CrossRegionReplication {

    public static void main(String[] args) throws IOException {
        String clientRegion = "*** Client region ***";
        String accountId = "*** Account ID ***";
        String roleName = "*** Role name ***";
        String sourceBucketName = "*** Source bucket name ***";
        String destBucketName = "*** Destination bucket name ***";
        String prefix = "Tax/";
        
        String roleARN = String.format("arn:aws:iam::%s:role/%s", accountId, roleName);
        String destinationBucketARN = "arn:aws:s3:::" + destBucketName;
   
        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(clientRegion)
                    .build();

            // Create the replication rule.
            Map<String, ReplicationRule> replicationRules = new HashMap<String, ReplicationRule>();
            replicationRules.put("ReplicationRule1",
                                 new ReplicationRule()
                                     .withStatus(ReplicationRuleStatus.Enabled)
                                     .withPrefix(prefix)
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
            System.out.println("Retrieved source-bucket replication rule prefix: " + rule.getPrefix());
            System.out.println("Retrieved source-bucket replication rule status: " + rule.getStatus());
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

## Related Topics<a name="crr-using-java-related-topics"></a>

[Cross\-Region Replication \(CRR\)](crr.md)

[Setting Up Cross\-Region Replication](crr-how-setup.md)