# Using Service\-Linked Roles for Amazon S3 Storage Lens<a name="using-service-linked-roles"></a>

To use Amazon S3 Storage Lens to collect and aggregate metrics across all your accounts in AWS Organizations, you must first ensure that S3 Storage Lens has trusted access enabled by the Management account in your organization\. S3 Storage Lens creates a service\-linked role to allow it to enable it to get the list of AWS accounts belonging to your organization\. This list of accounts is used by S3 Storage Lens to collect metrics for S3 resources in all those member accounts when S3 Storage Lens dashboard or configurations are created or updated\.

Amazon S3 Storage Lens uses AWS Identity and Access Management \(IAM\)[ service\-linked roles](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_terms-and-concepts.html#iam-term-service-linked-role)\. A service\-linked role is a unique type of IAM role that is linked directly to S3 Storage Lens\. Service\-linked roles are predefined by S3 Storage Lens and include all the permissions that the service requires to call other AWS services on your behalf\.

A service\-linked role makes setting up S3 Storage Lens easier because you don't have to manually add the necessary permissions\. S3 Storage Lens defines the permissions of its service\-linked roles, and unless defined otherwise, only S3 Storage Lens can assume its roles\. The defined permissions include the trust policy and the permissions policy, and that permissions policy cannot be attached to any other IAM entity\.

You can delete this service\-linked role only after first deleting their related resources\. This protects your S3 Storage Lens resources because you can't inadvertently remove permission to access the resources\.

For information about other services that support service\-linked roles, see [AWS Services That Work with IAM](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_aws-services-that-work-with-iam.html) and look for the services that have **Yes **in the **Service\-Linked Role** column\. Choose a **Yes** with a link to view the service\-linked role documentation for that service\.

## Service\-Linked Role Permissions for Amazon S3 Storage Lens<a name="slr-permissions"></a>

S3 Storage Lens uses the service\-linked role named **AWSServiceRoleForS3StorageLens** – This enables access to AWS services and resources used or managed by S3 Storage Lens\. This allows S3 Storage Lens to access AWS Organizations resources on your behalf\.

The S3 Storage Lens service\-linked role trusts the following service on your organization's storage:
+ `storage-lens.s3.amazonaws.com`

The role permissions policy allows S3 Storage Lens to complete the following actions:
+ `organizations:DescribeOrganization`

  `organizations:ListAccounts`

  `organizations:ListAWSServiceAccessForOrganization`

  `organizations:ListDelegatedAdministrators`

You must configure permissions to allow an IAM entity \(such as a user, group, or role\) to create, edit, or delete a service\-linked role\. For more information, see [Service\-Linked Role Permissions](https://docs.aws.amazon.com/IAM/latest/UserGuide/using-service-linked-roles.html#service-linked-role-permissions) in the *IAM User Guide*\.

## Creating a Service\-Linked Role for S3 Storage Lens<a name="create-slr"></a>

You don't need to manually create a service\-linked role\. When you complete one of the following tasks while signed into the AWS Organizations Management or the delegate administrator accounts, S3 Storage Lens creates the service\-linked role for you:
+ Create an S3 Storage Lens dashboard configuration for your organization in the Amazon S3 console\.
+ `PUT` an S3 Storage Lens configuration for your organization using the REST API, AWS CLI and SDKs\.

**Note**  
S3 Storage Lens will support a maximum of five delegated administrators per Organization\.

If you delete this service\-linked role, the preceding actions will recreate it as needed\.

### Example Policy for S3 Storage Lens service\-linked role<a name="slr-sample-policy"></a>

**Example Permissions policy for the S3 Storage Lens service\-linked role**  

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "AwsOrgsAccess",
            "Effect": "Allow",
            "Action": [
                "organizations:DescribeOrganization",
                "organizations:ListAccounts",
                "organizations:ListAWSServiceAccessForOrganization",
                "organizations:ListDelegatedAdministrators"
            ],
            "Resource": [
                "*"
            ]
        }
    ]
}
```

## Editing a Service\-Linked Role for Amazon S3 Storage Lens<a name="edit-slr"></a>

S3 Storage Lens does not allow you to edit the AWSServiceRoleForS3StorageLens service\-linked role\. After you create a service\-linked role, you cannot change the name of the role because various entities might reference the role\. However, you can edit the description of the role using IAM\. For more information, see [Editing a Service\-Linked Role](https://docs.aws.amazon.com/IAM/latest/UserGuide/using-service-linked-roles.html#edit-service-linked-role) in the *IAM User Guide*\.

## Deleting a Service\-Linked Role for Amazon S3 Storage Lens<a name="delete-slr"></a>

If you no longer need to use the service\-linked role, we recommend that you delete that role\. That way you don't have an unused entity that is not actively monitored or maintained\. However, you must clean up the resources for your service\-linked role before you can manually delete it\.

**Note**  
If the Amazon S3 Storage Lens service is using the role when you try to delete the resources, then the deletion might fail\. If that happens, wait for a few minutes and try the operation again\.

To delete the AWSServiceRoleForS3StorageLens you must delete all the organization level S3 Storage Lens configurations present in all Regions using the AWS Organizations Management or the delegate administrator accounts\.

The resources are organization\-level S3 Storage Lens configurations\. Use S3 Storage Lens to clean up the resources and then use the IAM console, CLI, REST API or AWS SDK to delete the role\. 

In the REST API, AWS CLI and SDKs, S3 Storage Lens configurations can be discovered using `ListStorageLensConfigurations` in all the Regions where your Organization has created S3 Storage Lens configurations\. Use the action `DeleteStorageLensConfiguration` to delete these configurations so you can then delete the role\.

**Note**  
To delete the service\-linked role you must delete all the organization\-level S3 Storage Lens configurations in all the Regions where they exist\.

**To delete Amazon S3 Storage Lens resources used by the AWSServiceRoleForS3StorageLens**

1. You must use the `ListStorageLensConfigurations` in every Region that you have S3 Storage Lens configurations to get a list of your organization level configurations\. This list may also be obtained from the Amazon S3 console\.

1. These configurations then must be deleted from the appropriate regional endpoints by invoking the `DeleteStorageLensConfiguration` API call or via the Amazon S3 console\. 

**To manually delete the service\-linked role using IAM**

After the configurations are deleted the AWSServiceRoleForS3StorageLens can be deleted from the IAM console or by invoking the IAM API `DeleteServiceLinkedRole`, the AWS CLI, or the AWS SDK\. For more information, see [Deleting a Service\-Linked Role](https://docs.aws.amazon.com/IAM/latest/UserGuide/using-service-linked-roles.html#delete-service-linked-role) in the *IAM User Guide*\.

## Supported Regions for S3 Storage Lens Service\-Linked Roles<a name="slr-regions"></a>

S3 Storage Lens supports using service\-linked roles in all of the regions where the service is available\. For more information, see [Amazon S3 Regions and Endpoints](https://docs.aws.amazon.com/general/latest/gr/s3.html)\.