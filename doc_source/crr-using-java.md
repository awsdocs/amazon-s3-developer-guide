# Setting Up Cross\-Region Replication Using the AWS SDK for Java<a name="crr-using-java"></a>

When the source and destination buckets are owned by two different AWS accounts, you can use either the AWS CLI or one of the AWS SDKs to add replication configuration on the source bucket\. You cannot use the console to add the replication configuration because the console does not provide a way for you to specify a destination bucket owned by another AWS account at the time you add replication configuration on a source bucket\. For more information, see [Setting Up Cross\-Region Replication](crr-how-setup.md)\.

The following AWS SDK for Java code example first adds replication configuration to a bucket and then retrieves it\. You need to update the code by providing your bucket names and IAM role ARN\. For instructions on how to create and test a working sample, see [Testing the Java Code Examples](UsingTheMPDotJavaAPI.md#TestingJavaSamples)\.

```
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.BucketReplicationConfiguration;
import com.amazonaws.services.s3.model.ReplicationDestinationConfig;
import com.amazonaws.services.s3.model.ReplicationRule;
import com.amazonaws.services.s3.model.ReplicationRuleStatus;

public class CrossRegionReplicationComplete {
    private static String sourceBucketName = "source-bucket"; 
    private static String roleARN = "arn:aws:iam::account-id:role/role-name"; 
    private static String destinationBucketArn = "arn:aws:s3:::destination-bucket"; 
    
    public static void main(String[] args) throws IOException {
        AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
        try {
            Map<String, ReplicationRule> replicationRules = new HashMap<String, ReplicationRule>();
            replicationRules.put(
                    "a-sample-rule-id",
                    new ReplicationRule()
                        .withPrefix("Tax/")
                        .withStatus(ReplicationRuleStatus.Enabled)
                        .withDestinationConfig(
                                new ReplicationDestinationConfig()
                                  .withBucketARN(destinationBucketArn)
                                  .withStorageClass(StorageClass.Standard_Infrequently_Accessed)
                        )
            );
            s3Client.setBucketReplicationConfiguration(
                    sourceBucketName,
                    new BucketReplicationConfiguration()
                        .withRoleARN(roleARN)
                        .withRules(replicationRules)
            ); 
            BucketReplicationConfiguration replicationConfig = s3Client.getBucketReplicationConfiguration(sourceBucketName);
            
            ReplicationRule rule = replicationConfig.getRule("a-sample-rule-id");
            
            System.out.println("Destination Bucket ARN : " + rule.getDestinationConfig().getBucketARN());
            System.out.println("Prefix : " + rule.getPrefix());
            System.out.println("Status : " + rule.getStatus());
            
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
                    "communicate with Amazon S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
}
```

## Related Topics<a name="crr-using-java-related-topics"></a>

[Cross\-Region Replication \(CRR\)](crr.md)

[Setting Up Cross\-Region Replication](crr-how-setup.md)