# Using Amazon S3 Storage Lens with AWS Organizations<a name="storage_lens_with_organizations"></a>

You can use Amazon S3 Storage Lens to collect storage metrics and usage data for all AWS accounts that are part of your AWS Organizations hierarchy\. To do this, you must be using AWS Organizations, and you must enable S3 Storage Lens trusted access using your AWS Organizations management\. 

After enabling trusted access, you can add delegated administrator access to accounts in your organization\. These accounts can then create organization\-wide dashboards and configurations for S3 Storage Lens\. 

For more information about enabling trusted access, see [Amazon S3 Storage Lens and AWS Organizations](https://docs.aws.amazon.com/organizations/latest/userguide/services-that-can-integrate-s3lens.html) in the *AWS Organizations User Guide*\.

**Topics**
+ [Enabling trusted access for S3 Storage Lens](#storage_lens_with_organizations_enabling_trusted_access)
+ [Disabling trusted access for S3 Storage Lens](#storage_lens_with_organizations_disabling_trusted_access)
+ [Registering a delegated administrator for S3 Storage Lens](#storage_lens_with_organizations_registering_delegated_admins)
+ [Deregistering a delegated administrator for S3 Storage Lens](#storage_lens_with_organizations_deregistering_delegated_admins)

## Enabling trusted access for S3 Storage Lens<a name="storage_lens_with_organizations_enabling_trusted_access"></a>

By enabling trusted access, you allow Amazon S3 Storage Lens to have access to your AWS Organizations hierarchy, membership, and structure through the AWS Organizations APIs\. S3 Storage Lens will be a trusted service for your entire organization’s structure\. 

Whenever a dashboard configuration is created, S3 Storage Lens creates service\-linked roles in your organization’s management or delegated administrator accounts\. The service\-linked role grants S3 Storage Lens permissions to describe organizations, list accounts, verify a list of service access for the organizations, and get delegated administrators for the organization\. S3 Storage Lens can then collect cross\-account storage usage and activity metrics for dashboards within accounts in your organizations\. For more information, see [ Using service\-linked roles for Amazon S3 Storage Lens](https://docs.aws.amazon.com/AmazonS3/latest/dev/using-service-linked-roles.html)\. 

After enabling trusted access, you can assign delegate administrator access to accounts in your organization\. When an account is marked as a delegate administrator for a service, the account receives authorization to access all read\-only organization APIs\. This provides visibility to the members and structures of your organization so that they can create S3 Storage Lens dashboards on your behalf\.

**Note**  
Only the management account can enable trusted access for Amazon S3 Storage Lens\.

## Disabling trusted access for S3 Storage Lens<a name="storage_lens_with_organizations_disabling_trusted_access"></a>

By disabling trusted access, you limit S3 Storage Lens to working only on an account level\. In addition, each account holder can only see the S3 Storage Lens benefits limited to the scope of their account, and not their entire organization\. Any dashboards requiring trusted access are no longer updated, but will retain their historic data per their respective [ retention periods](https://docs.aws.amazon.com/AmazonS3/latest/dev/storage_lens_basics_metrics_recommendations.html#storage_lens_basics_retention_period)\. 

Removing an account as a delegated administrator will limit their S3 Storage Lens dashboard metrics access to only work on an account level\. Any organizational dashboards that they created are no longer updated, but they will retain their historic data per their respective retention periods\. 

**Note**  
This action also automatically stops all organization\-level dashboards from collecting and aggregating storage metrics\. 
Your management and delegated administrator accounts will still be able to see the historic data for your exiting organization\-level dashboards according to their respective retention periods\.

## Registering a delegated administrator for S3 Storage Lens<a name="storage_lens_with_organizations_registering_delegated_admins"></a>

You can create organization\-level dashboards using your organization’s management account or a delegated administrator account\. Delegated administrator accounts allow other accounts besides your management account to create organization\-level dashboards\. Only the management account of an organization can register and deregister other accounts as delegated administrators for the organization\.

To register a delegated administrator using the Amazon S3 console, see [Register accounts for delegated administrator access](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/storage_lens_console_organizations_registering_delegated_admins.html) in the *Amazon Simple Storage Service Console User Guide*\.

You can also register a delegated administrator using the AWS Organizations REST API, AWS CLI, or SDKs from the management account\. For more information, see [RegisterDelegatedAdministrator](https://docs.aws.amazon.com/organizations/latest/APIReference/API_RegisterDelegatedAdministrator.html) in the *AWS Organizations API Reference*\.

**Note**  
Before you can designate a delegated administrator using the AWS Organizations REST API, AWS CLI, or SDKs, you must call the [EnableAWSOrganizationsAccess](https://docs.aws.amazon.com/servicecatalog/latest/dg/API_EnableAWSOrganizationsAccess.html) operation\.

## Deregistering a delegated administrator for S3 Storage Lens<a name="storage_lens_with_organizations_deregistering_delegated_admins"></a>

You can also de\-register a delegated administrator account\. Delegated administrator accounts allow other accounts besides your management account to create organization\-level dashboards\. Only the management account of an organization can de\-register accounts as delegated administrators for the organization\.

To de\-register a delegated admin using the S3 console, see [ Deregister accounts for delegated administrator access](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/storage_lens_console_organizations_deregistering_delegated_admins.html) in the *Amazon Simple Storage Service Console User Guide*\.

You can also de\-register a delegated administrator using the AWS Organizations REST API, AWS CLI, or SDKs from the management account\. For more information, see [ DeregisterDelegatedAdministrator](https://docs.aws.amazon.com/organizations/latest/APIReference/API_DeregisterDelegatedAdministrator.html) in the *AWS Organizations API Reference*\.

**Note**  
This action also automatically stop all organization\-level dashboards created by that delegated administrator from aggregating new storage metrics\.
The delegate administrator accounts will still be able to see the historic data for those dashboards according to their respective retention periods\.