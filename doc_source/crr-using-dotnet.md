# Setting Up Cross\-Region Replication Using the AWS SDK for \.NET<a name="crr-using-dotnet"></a>

When the source and destination buckets are owned by two different AWS accounts, you can use either the AWS CLI or one of the AWS SDKs to add replication configuration on the source bucket\. You cannot use the console to add the replication configuration because the console does not provide a way for you to specify a destination bucket owned by another AWS account at the time you add replication configuration on a source bucket\. For more information, see [Setting Up Cross\-Region Replication](crr-how-setup.md)\.

The following AWS SDK for \.NET code example first adds replication configuration to a bucket and then retrieves it\. You need to update the code by providing your bucket names and IAM role ARN\. For instructions on how to create and test a working sample, see [Running the Amazon S3 \.NET Code Examples](UsingTheMPDotNetAPI.md#TestingDotNetApiSamples)\.

```
using System;
using System.Collections.Generic;
using Amazon.S3;
using Amazon.S3.Model;

namespace s3.amazon.com.docsamples
{
    class CrossRegionReplication
    {
        static string sourceBucket         = "source-bucket";
        static string destinationBucketArn = "arn:aws:s3:::destination-bucket";
        static string roleArn              = "arn:aws:iam::account-id:role/role-name";


        public static void Main(string[] args)
        {
            try
            {
                using (var client = new AmazonS3Client(Amazon.RegionEndpoint.USEast1))
                {
                    EnableReplication(client);
                    RetrieveReplicationConfiguration(client);
                }

                Console.WriteLine("Press any key to continue...");
                Console.ReadKey();
            }
            catch (AmazonS3Exception amazonS3Exception)
            {
                if (amazonS3Exception.ErrorCode != null &&
                    (amazonS3Exception.ErrorCode.Equals("InvalidAccessKeyId")
                    ||
                    amazonS3Exception.ErrorCode.Equals("InvalidSecurity")))
                {
                    Console.WriteLine("Check the provided AWS Credentials.");
                    Console.WriteLine(
                    "To sign up for service, go to http://aws.amazon.com/s3");
                }
                else
                {
                    Console.WriteLine(
                     "Error occurred. Message:'{0}' when enabling notifications.",
                     amazonS3Exception.Message);
                }
            }

        }

        static void EnableReplication(IAmazonS3 client)
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

            PutBucketReplicationResponse putResponse = client.PutBucketReplication(putRequest);
        }

        private static void RetrieveReplicationConfiguration(IAmazonS3 client)
        {
            // Retrieve the configuration.
            GetBucketReplicationRequest getRequest = new GetBucketReplicationRequest
            {
                BucketName = sourceBucket
            };
            GetBucketReplicationResponse getResponse = client.GetBucketReplication(getRequest);
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

## Related Topics<a name="crr-using-dotnet-related-topics"></a>

[Cross\-Region Replication \(CRR\)](crr.md)

[Setting Up Cross\-Region Replication](crr-how-setup.md)