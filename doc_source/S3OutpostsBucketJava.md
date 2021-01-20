# Creating and managing Amazon S3 on Outposts bucket<a name="S3OutpostsBucketJava"></a>

You can use the SDK for Java to create and manage your S3 on Outposts buckets\. From these examples, you can create and get an Outposts bucket, list buckets for an Outpost, create and manage access points, lifecycleconfiguration, and policy for the Outpost bucket\. 

**Topics**
+ [Configuring S3 control client for an Amazon S3 on Outposts](#S3OutpostsCongfigureS3ControlClientJava)
+ [Create an Amazon S3 on Outposts bucket](#S3OutpostsCreateBucketJava)
+ [Get the Amazon S3 on Outposts bucket](#S3OutpostsGetBucketJava)
+ [Get a list of buckets in an Outpost](#S3OutpostsListRegionalBucketJava)
+ [Create an access point for an Amazon S3 on Outposts bucket](#S3OutpostsCreateAccessPointJava)
+ [Gets an access point for an Amazon S3 on Outposts bucket](#S3OutpostsGetAccessPointJava)
+ [:List access points for an AWS Outpost](#S3OutpostsListAccessPointJava)
+ [Add a lifecycle configuration for your Outposts bucket](#S3OutpostsPutBucketLifecycleConfigurationJava)
+ [Gets a lifecycle configuration for an Amazon S3 on Outposts bucket](#S3OutpostsGetBucketLifecycleConfigurationJava)
+ [Put a policy on your Outposts bucket](#S3OutpostsPutBucketPolicyJava)
+ [Gets a policy for an Amazon S3 on Outposts bucket](#S3OutpostsGetBucketPolicyJava)
+ [Put a policy on your Outposts access point](#S3OutpostsPutAccessPointPolicyJava)
+ [Gets a policy for an Amazon S3 on Outposts access point](#S3OutpostsGetAccessPointPolicyJava)
+ [Create an endpoint on an Outpost](#S3OutpostsCreateEndpointJava)
+ [Delete an endpoint on an Outpost](#S3OutpostsDeleteEndpointJava)
+ [List endpoints for Amazon S3 on Outposts Outpost](#S3OutpostsListEndpointsJava)

## Configuring S3 control client for an Amazon S3 on Outposts<a name="S3OutpostsCongfigureS3ControlClientJava"></a>

The following example configures the S3 control client for S3 on Outposts using the SDK for Java\. 

```
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3control.AWSS3Control;
import com.amazonaws.services.s3control.AWSS3ControlClient;

public AWSS3Control createS3ControlClient() {

    String accessKey = AWSAccessKey;
    String secretKey = SecretAccessKey;
    BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);

    return AWSS3ControlClient.builder().enableUseArnRegion()
            .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
            .build();

}
```

## Create an Amazon S3 on Outposts bucket<a name="S3OutpostsCreateBucketJava"></a>

The following example creates an S3 on Outposts `s3-outposts:CreateBucket` using the SDK for Java\. 

```
import com.amazonaws.services.s3control.model.*;

public String createBucket(String bucketName) {

    CreateBucketRequest reqCreateBucket = new CreateBucketRequest()
            .withBucket(bucketName)
            .withOutpostId(OutpostId)
            .withCreateBucketConfiguration(new CreateBucketConfiguration());

    CreateBucketResult respCreateBucket = s3ControlClient.createBucket(reqCreateBucket);
    System.out.printf("CreateBucket Response: %s%n", respCreateBucket.toString());

    return respCreateBucket.getBucketArn();

}
```

## Get the Amazon S3 on Outposts bucket<a name="S3OutpostsGetBucketJava"></a>

The following S3 on Outposts example gets a bucket using the SDK for Java\. 

```
import com.amazonaws.services.s3control.model.*;

public void getBucket(String bucketArn) {

    GetBucketRequest reqGetBucket = new GetBucketRequest()
            .withBucket(bucketArn)
            .withAccountId(AccountId);

    GetBucketResult respGetBucket = s3ControlClient.getBucket(reqGetBucket);
    System.out.printf("GetBucket Response: %s%n", respGetBucket.toString());

}
```



## Get a list of buckets in an Outpost<a name="S3OutpostsListRegionalBucketJava"></a>

The following SDK for Java example gets a list of buckets in an Outpost\. 

```
import com.amazonaws.services.s3control.model.*;

public void listRegionalBuckets() {

    ListRegionalBucketsRequest reqListBuckets = new ListRegionalBucketsRequest()
            .withAccountId(AccountId)
            .withOutpostId(OutpostId);

    ListRegionalBucketsResult respListBuckets = s3ControlClient.listRegionalBuckets(reqListBuckets);
    System.out.printf("ListRegionalBuckets Response: %s%n", respListBuckets.toString());

}
```



## Create an access point for an Amazon S3 on Outposts bucket<a name="S3OutpostsCreateAccessPointJava"></a>

The following SDK for Java example creates an access point for an Outposts bucket\.

```
import com.amazonaws.services.s3control.model.*;

public String createAccessPoint(String bucketArn, String accessPointName) {

    CreateAccessPointRequest reqCreateAP = new CreateAccessPointRequest()
            .withAccountId(AccountId)
            .withBucket(bucketArn)
            .withName(accessPointName)
            .withVpcConfiguration(new VpcConfiguration().withVpcId("vpc-12345"));

    CreateAccessPointResult respCreateAP = s3ControlClient.createAccessPoint(reqCreateAP);
    System.out.printf("CreateAccessPoint Response: %s%n", respCreateAP.toString());

    return respCreateAP.getAccessPointArn();

}
```



## Gets an access point for an Amazon S3 on Outposts bucket<a name="S3OutpostsGetAccessPointJava"></a>

The following SDK for Java example gets an access point for an Outposts bucket\.

```
import com.amazonaws.services.s3control.model.*;

public void getAccessPoint(String accessPointArn) {

    GetAccessPointRequest reqGetAP = new GetAccessPointRequest()
            .withAccountId(AccountId)
            .withName(accessPointArn);

    GetAccessPointResult respGetAP = s3ControlClient.getAccessPoint(reqGetAP);
    System.out.printf("GetAccessPoint Response: %s%n", respGetAP.toString());

}
```



## :List access points for an AWS Outpost<a name="S3OutpostsListAccessPointJava"></a>

The following SDK for Java example List access points for an Outposts bucket\.

```
import com.amazonaws.services.s3control.model.*;

public void listAccessPoints(String bucketArn) {

    ListAccessPointsRequest reqListAPs = new ListAccessPointsRequest()
            .withAccountId(AccountId)
            .withBucket(bucketArn);

    ListAccessPointsResult respListAPs = s3ControlClient.listAccessPoints(reqListAPs);
    System.out.printf("ListAccessPoints Response: %s%n", respListAPs.toString());

}
```



## Add a lifecycle configuration for your Outposts bucket<a name="S3OutpostsPutBucketLifecycleConfigurationJava"></a>

The following SDK for Java example puts an lifecycle configruations for an Outposts bucket where all objects with the flagged prefix and tags expire after 10 days\.

```
import com.amazonaws.services.s3control.model.*;

public void putBucketLifecycleConfiguration(String bucketArn) {

    S3Tag tag1 = new S3Tag().withKey("mytagkey1").withValue("mytagvalue1");
    S3Tag tag2 = new S3Tag().withKey("mytagkey2").withValue("mytagvalue2");

    LifecycleRuleFilter lifecycleRuleFilter = new LifecycleRuleFilter()
            .withAnd(new LifecycleRuleAndOperator()
                    .withPrefix("myprefix")
                    .withTags(tag1, tag2));

    LifecycleExpiration lifecycleExpiration = new LifecycleExpiration()
            .withExpiredObjectDeleteMarker(false)
            .withDays(1);

    LifecycleRule lifecycleRule = new LifecycleRule()
            .withStatus("Enabled")
            .withFilter(lifecycleRuleFilter)
            .withExpiration(lifecycleExpiration)
            .withID("id-1");


    LifecycleConfiguration lifecycleConfiguration = new LifecycleConfiguration()
            .withRules(lifecycleRule);

    PutBucketLifecycleConfigurationRequest reqPutBucketLifecycle = new PutBucketLifecycleConfigurationRequest()
            .withAccountId(AccountId)
            .withBucket(bucketArn)
            .withLifecycleConfiguration(lifecycleConfiguration);

    PutBucketLifecycleConfigurationResult respPutBucketLifecycle = s3ControlClient.putBucketLifecycleConfiguration(reqPutBucketLifecycle);
    System.out.printf("PutBucketLifecycleConfiguration Response: %s%n", respPutBucketLifecycle.toString());


}
```



## Gets a lifecycle configuration for an Amazon S3 on Outposts bucket<a name="S3OutpostsGetBucketLifecycleConfigurationJava"></a>

The following SDK for Java example gets an access point for an Outposts bucket\.

```
import com.amazonaws.services.s3control.model.*;

public void getBucketLifecycleConfiguration(String bucketArn) {

    GetBucketLifecycleConfigurationRequest reqGetBucketLifecycle = new GetBucketLifecycleConfigurationRequest()
            .withAccountId(AccountId)
            .withBucket(bucketArn);

    GetBucketLifecycleConfigurationResult respGetBucketLifecycle = s3ControlClient.getBucketLifecycleConfiguration(reqGetBucketLifecycle);
    System.out.printf("GetBucketLifecycleConfiguration Response: %s%n", respGetBucketLifecycle.toString());

}
```



## Put a policy on your Outposts bucket<a name="S3OutpostsPutBucketPolicyJava"></a>

The following SDK for Java example puts policy for an Outposts bucket\.

```
import com.amazonaws.services.s3control.model.*;

public void putBucketPolicy(String bucketArn) {

    String policy = "{\"Version\":\"2012-10-17\",\"Id\":\"testBucketPolicy\",\"Statement\":[{\"Sid\":\"st1\",\"Effect\":\"Allow\",\"Principal\":{\"AWS\":\"" + AccountId+ "\"},\"Action\":\"s3-outposts:*\",\"Resource\":\"" + bucketArn + "\"}]}";

    PutBucketPolicyRequest reqPutBucketPolicy = new PutBucketPolicyRequest()
            .withAccountId(AccountId)
            .withBucket(bucketArn)
            .withPolicy(policy);

    PutBucketPolicyResult respPutBucketPolicy = s3ControlClient.putBucketPolicy(reqPutBucketPolicy);
    System.out.printf("PutBucketPolicy Response: %s%n", respPutBucketPolicy.toString());

}
```



## Gets a policy for an Amazon S3 on Outposts bucket<a name="S3OutpostsGetBucketPolicyJava"></a>

The following SDK for Java example gets a policy for an Outposts bucket\.



```
import com.amazonaws.services.s3control.model.*;

public void getBucketPolicy(String bucketArn) {

    GetBucketPolicyRequest reqGetBucketPolicy = new GetBucketPolicyRequest()
            .withAccountId(AccountId)
            .withBucket(bucketArn);

    GetBucketPolicyResult respGetBucketPolicy = s3ControlClient.getBucketPolicy(reqGetBucketPolicy);
    System.out.printf("GetBucketPolicy Response: %s%n", respGetBucketPolicy.toString());

}
```

## Put a policy on your Outposts access point<a name="S3OutpostsPutAccessPointPolicyJava"></a>

The following SDK for Java example puts policy for an Outposts bucket\.

```
import com.amazonaws.services.s3control.model.*;

public void putAccessPointPolicy(String accessPointArn) {

    String policy = "{\"Version\":\"2012-10-17\",\"Id\":\"testAccessPointPolicy\",\"Statement\":[{\"Sid\":\"st1\",\"Effect\":\"Allow\",\"Principal\":{\"AWS\":\"" + AccountId + "\"},\"Action\":\"s3-outposts:*\",\"Resource\":\"" + accessPointArn + "\"}]}";

    PutAccessPointPolicyRequest reqPutAccessPointPolicy = new PutAccessPointPolicyRequest()
            .withAccountId(AccountId)
            .withName(accessPointArn)
            .withPolicy(policy);

    PutAccessPointPolicyResult respPutAccessPointPolicy = s3ControlClient.putAccessPointPolicy(reqPutAccessPointPolicy);
    System.out.printf("PutAccessPointPolicy Response: %s%n", respPutAccessPointPolicy.toString());
    printWriter.printf("PutAccessPointPolicy Response: %s%n", respPutAccessPointPolicy.toString());

}
```



## Gets a policy for an Amazon S3 on Outposts access point<a name="S3OutpostsGetAccessPointPolicyJava"></a>

The following SDK for Java example gets a policy for an Outposts bucket\.



```
import com.amazonaws.services.s3control.model.*;

public void getAccessPointPolicy(String accessPointArn) {

    GetAccessPointPolicyRequest reqGetAccessPointPolicy = new GetAccessPointPolicyRequest()
            .withAccountId(AccountId)
            .withName(accessPointArn);

    GetAccessPointPolicyResult respGetAccessPointPolicy = s3ControlClient.getAccessPointPolicy(reqGetAccessPointPolicy);
    System.out.printf("GetAccessPointPolicy Response: %s%n", respGetAccessPointPolicy.toString());
    printWriter.printf("GetAccessPointPolicy Response: %s%n", respGetAccessPointPolicy.toString());

}
```

## Create an endpoint on an Outpost<a name="S3OutpostsCreateEndpointJava"></a>

The following SDK for Java example creates an endpoint for an Outpost\.

```
import com.amazonaws.services.s3outposts.AmazonS3Outposts;
import com.amazonaws.services.s3outposts.AmazonS3OutpostsClientBuilder;
import com.amazonaws.services.s3outposts.model.CreateEndpointRequest;
import com.amazonaws.services.s3outposts.model.CreateEndpointResult;

public void createEndpoint() {
    AmazonS3Outposts s3OutpostsClient = AmazonS3OutpostsClientBuilder
                .standard().build();
                
    CreateEndpointRequest createEndpointRequest = new CreateEndpointRequest()
                .withOutpostId("op-0d79779cef3c30a40")
                .withSubnetId("subnet-8c7a57c5")
                .withSecurityGroupId("sg-ab19e0d1");
    CreateEndpointResult createEndpointResult = s3OutpostsClient.createEndpoint(createEndpointRequest);
    System.out.println("Endpoint is created and its arn is " + createEndpointResult.getEndpointArn());
}
```

## Delete an endpoint on an Outpost<a name="S3OutpostsDeleteEndpointJava"></a>

The following SDK for Java example delete an endpoint for an Outpost\.

```
import com.amazonaws.arn.Arn;
import com.amazonaws.services.s3outposts.AmazonS3Outposts;
import com.amazonaws.services.s3outposts.AmazonS3OutpostsClientBuilder;
import com.amazonaws.services.s3outposts.model.DeleteEndpointRequest;

public void deleteEndpoint(String endpointArnInput) {
    String outpostId = "op-0d79779cef3c30a40";
    AmazonS3Outposts s3OutpostsClient = AmazonS3OutpostsClientBuilder
                .standard().build();
                
    Arn endpointArn = Arn.fromString(endpointArnInput);
    String[] resourceParts = endpointArn.getResource().getResource().split("/");
    String endpointId = resourceParts[resourceParts.length - 1];
    DeleteEndpointRequest deleteEndpointRequest = new DeleteEndpointRequest()
                .withEndpointId(endpointId)
                .withOutpostId(outpostId);
    s3OutpostsClient.deleteEndpoint(deleteEndpointRequest);
    System.out.println("Endpoint with id " + endpointId + " is deleted.");
}
```

## List endpoints for Amazon S3 on Outposts Outpost<a name="S3OutpostsListEndpointsJava"></a>

The following SDK for Java example lists endpoints for an Outpost\.

```
import com.amazonaws.services.s3outposts.AmazonS3Outposts;
import com.amazonaws.services.s3outposts.AmazonS3OutpostsClientBuilder;
import com.amazonaws.services.s3outposts.model.ListEndpointsRequest;
import com.amazonaws.services.s3outposts.model.ListEndpointsResult;

public void listEndpoints() {
    AmazonS3Outposts s3OutpostsClient = AmazonS3OutpostsClientBuilder
                .standard().build();
                
    ListEndpointsRequest listEndpointsRequest = new ListEndpointsRequest();
    ListEndpointsResult listEndpointsResult = s3OutpostsClient.listEndpoints(listEndpointsRequest);
    System.out.println("List endpoints result is " + listEndpointsResult);
}
```

