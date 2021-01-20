# Setting permissions to use Amazon S3 Storage Lens<a name="storage_lens_iam_permissions"></a>

Amazon S3 Storage Lens requires new permissions in AWS Identity and Access Management \(IAM\) to authorize access to S3 Storage Lens actions\. You can attach the policy to IAM users, groups, or roles to grant them permissions to enable or disable S3 Storage Lens, or to access any S3 Storage Lens dashboard or configuration\. 

The IAM user or role must belong to the account that created or owns the dashboard or configuration, unless your account is a member of AWS Organizations, and you were given access to create organization\-level dashboards by your management account as a delegated administrator\. 

**Note**  
You can’t use your account's root user credentials to view Amazon S3 Storage Lens dashboards\. To access S3 Storage Lens dashboards, you must grant the requisite IAM permissions to a new or existing IAM user\. Then, sign in with those user credentials to access S3 Storage Lens dashboards\. For more information, see [AWS Identity and Access Management best practices](https://docs.aws.amazon.com/IAM/latest/UserGuide/best-practices.html)\. 
Using S3 Storage Lens on the Amazon S3 console can require multiple permissions\. For example; to edit a dashboard on the console, you need the following permissions:  
`s3:ListStorageLensConfigurations`
`s3:GetStorageLensConfiguration`
`s3:PutStorageLensConfiguration`

**Topics**
+ [Setting account permissions to use S3 Storage Lens](#storage_lens_iam_permissions_account)
+ [Setting permissions to use S3 Storage Lens with AWS Organizations](#storage_lens_iam_permissions_organizations)

## Setting account permissions to use S3 Storage Lens<a name="storage_lens_iam_permissions_account"></a>


**Amazon S3 Storage Lens related IAM permissions**  

| Action | IAM permissions | 
| --- | --- | 
| Create or update an S3 Storage Lens dashboard in the Amazon S3 console\. |  `s3:ListStorageLensConfigurations` `s3:GetStorageLensConfiguration` `s3:GetStorageLensConfiguration` `s3:PutStorageLensConfiguration` `s3:PutStorageLensConfigurationTagging`  | 
| Get tags of an S3 Storage Lens dashboard on the Amazon S3 console\. |  `s3:ListStorageLensConfigurations` `s3:GetStorageLensConfigurationTagging`  | 
| View an S3 Storage Lens dashboard on the Amazon S3 console\. |  `s3:ListStorageLensConfigurations` `s3:GetStorageLensConfiguration` `s3:GetStorageLensDashboard`  | 
| Delete an S3 Storage Lens dashboard on Amazon S3 console\. |  `s3:ListStorageLensConfigurations` `s3:GetStorageLensConfiguration` `s3:DeleteStorageLensConfiguration`  | 
| Create or update an S3 Storage Lens configuration in the AWS CLI or SDK\. |  `s3:PutStorageLensConfiguration` `s3:PutStorageLensConfigurationTagging`  | 
| Get tags of an S3 Storage Lens configuration in the AWS CLI or SDK\. |  `s3:GetStorageLensConfigurationTagging`  | 
| View an S3 Storage Lens configuration in the AWS CLI or SDK\. |  `s3:GetStorageLensConfiguration`  | 
| Delete an S3 Storage Lens configuration in AWS CLI or SDK\. |  `s3:DeleteStorageLensConfiguration`  | 

**Note**  
You can use resource tags in an IAM policy to manage permissions\.
An IAM user/role with these permissions can see metrics from buckets and prefixes that they might not have direct permission to read or list objects from\.
For S3 Storage Lens configurations with *advanced metrics and recommendations* aggregated at the prefix\-level, if the selected prefix matches object keys, it may show the object key as your prefix up to the delimiter and maximum depth selected\.
For metrics exports, which are stored in a bucket in your account, permissions are granted using the existing `s3:GetObject` permission in the IAM policy\. Similarly, for an AWS Organizations entity, the organization management or delegated administrator account can use IAM policies to manage access permissions for organization\-level dashboard and configurations\.

## Setting permissions to use S3 Storage Lens with AWS Organizations<a name="storage_lens_iam_permissions_organizations"></a>

You can use Amazon S3 Storage Lens to collect storage metrics and usage data for all accounts that are part of your AWS Organizations hierarchy\. The following are the actions and permissions related to using S3 Storage Lens with Organizations\.


**AWS Organizations\-related IAM permissions for using Amazon S3 Storage Lens**  

| Action | IAM Permissions | 
| --- | --- | 
| Enable trusted access for S3 Storage Lens for your organization\. |  `organizations:EnableAWSServiceAccess`  | 
| Disable trusted access S3 Storage Lens for your organization\. |  `organizations:DisableAWSServiceAccess`  | 
| Register a delegated administrator to create S3 Storage Lens dashboards or configurations for your organization\. |  `organizations:RegisterDelegatedAdministrator`  | 
| De\-register a delegated administrator to create S3 Storage Lens dashboards or configurations for your organization\. |  `organizations:DeregisterDelegatedAdministrator`  | 
| Additional permissions to create S3 Storage Lens organization\-wide configurations |  `organizations:DescribeOrganization` `organizations:ListAccounts` `organizations:ListAWSServiceAccessForOrganization` `organizations:ListDelegatedAdministrators` `iam:CreateServiceLinkedRole`  | 