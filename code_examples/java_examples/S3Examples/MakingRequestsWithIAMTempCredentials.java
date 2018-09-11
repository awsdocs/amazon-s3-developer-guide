// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetSessionTokenRequest;
import com.amazonaws.services.securitytoken.model.GetSessionTokenResult;

public class MakingRequestsWithIAMTempCredentials {
    public static void main(String[] args) {
        String clientRegion = "*** Client region ***";
        String roleARN = "*** ARN for role to be assumed ***";
        String roleSessionName = "*** Role session name ***";
        String bucketName = "*** Bucket name ***";

        try {
            // Creating the STS client is part of your trusted code. It has
            // the security credentials you use to obtain temporary security credentials.
            AWSSecurityTokenService stsClient = AWSSecurityTokenServiceClientBuilder.standard()
                                                    .withCredentials(new ProfileCredentialsProvider())
                                                    .withRegion(clientRegion)
                                                    .build();

            // Assume the IAM role. Note that you cannot assume the role of an AWS root account;
            // Amazon S3 will deny access. You must use credentials for an IAM user or an IAM role.
            AssumeRoleRequest roleRequest = new AssumeRoleRequest()
                                                    .withRoleArn(roleARN)
                                                    .withRoleSessionName(roleSessionName);
            stsClient.assumeRole(roleRequest);

            // Start a session.
            GetSessionTokenRequest getSessionTokenRequest = new GetSessionTokenRequest();
            // The duration can be set to more than 3600 seconds only if temporary
            // credentials are requested by an IAM user rather than an account owner.
            getSessionTokenRequest.setDurationSeconds(7200);
            GetSessionTokenResult sessionTokenResult = stsClient.getSessionToken(getSessionTokenRequest);
            Credentials sessionCredentials = sessionTokenResult.getCredentials();

            // Package the temporary security credentials as a BasicSessionCredentials object 
            // for an Amazon S3 client object to use.
            BasicSessionCredentials basicSessionCredentials = new BasicSessionCredentials(
                    sessionCredentials.getAccessKeyId(), sessionCredentials.getSecretAccessKey(),
                    sessionCredentials.getSessionToken());

            // Provide temporary security credentials so that the Amazon S3 client 
			// can send authenticated requests to Amazon S3. You create the client 
			// using the basicSessionCredentials object.
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                                    .withCredentials(new AWSStaticCredentialsProvider(basicSessionCredentials))
                                    .withRegion(clientRegion)
                                    .build();

            // Verify that assuming the role worked and the permissions are set correctly
            // by getting a set of object keys from the bucket.
            ObjectListing objects = s3Client.listObjects(bucketName);
            System.out.println("No. of Objects: " + objects.getObjectSummaries().size());
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
