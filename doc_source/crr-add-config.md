# Replication Configuration Overview<a name="crr-add-config"></a>

Amazon S3 stores a replication configuration as XML\. In the replication configuration XML file, you specify an AWS Identity and Access Management \(IAM\) role and one or more rules\. 

```
<ReplicationConfiguration>
    <Role>IAM-role-ARN</Role>
    <Rule>
        ...
    </Rule>
    <Rule>
         ...
    </Rule>
     ...
</ReplicationConfiguration>
```

Amazon S3 can't replicate objects without your permission\. You grant permissions with the IAM role that you specify in the replication configuration\. Amazon S3 assumes the IAM role to replicate objects on your behalf\. You must grant the required permissions to the IAM role first\. For more information about managing permissions, see [Setting Up Permissions for CRR ](setting-repl-config-perm-overview.md)\.

You add one rule in replication configuration in the following scenarios:
+ You want to replicate all objects\.
+ You want to replicate a subset of objects\. You identify the object subset by adding a filter in the rule\. In the filter, you specify an object key prefix, tags, or a combination of both, to identify the subset of objects that the rule applies to\. 

You add multiple rules in a replication configuration if you want to select a different subset of objects\. In each rule, you specify a filter that selects a different subset of objects\. For example, you might choose to replicates objects that have either `tax/` or` document/` key prefixes\. You would add two rules and specify the `tax/` key prefix filter in one rule and the `document/` key prefix in the other\.

The following sections provide additional information\.

**Topics**
+ [The Basic Rule Configuration](#crr-config-min-rule-config)
+ [Optional: Specifying a Filter](#crr-config-optional-filter)
+ [Additional Destination Configurations](#crr-config-optional-dest-config)
+ [Example Replication Configurations](#crr-config-example-configs)
+ [Backward Compatibility](#crr-backward-compat-considerations)

## The Basic Rule Configuration<a name="crr-config-min-rule-config"></a>

### <a name="crr-config-min-rule-config"></a>

Each rule must include the rule's status and priority, and indicate whether to replicate delete makers\. 
+ `Status` indicates whether the rule is enabled or disabled\. If a rule is disabled, Amazon S3 doesn't perform the actions specified in the rule\. 
+ `Priority` indicates which rule has priority when multiple rules apply to an object\. 
+ Currently, delete markers aren't replicated, so you must set `DeleteMarkerReplication` to `Disabled`\.

In the destination configuration, you must provide the name of the bucket where you want Amazon S3 to replicate objects\. 

The following code shows the minimum requirements for a rule:

```
...
    <Rule>
        <ID>Rule-1</ID>
        <Status>rule-Enabled-or-Diasbled</Priority>
        <Priority>integer</Status>
        <DeleteMarkerReplication>
           <Status>Disabled</Status>
        </DeleteMarkerReplication>
        <Destination>        
           <Bucket>arn:aws:s3:::bucket-name</Bucket> 
        </Destination>    
    </Rule>
    <Rule>
         ...
    </Rule>
     ...
...
```

You can also specify other configuration options\. For example, you might choose to use a storage class for object replicas that differs from the class for the source object\. 

## Optional: Specifying a Filter<a name="crr-config-optional-filter"></a>

To choose a subset of objects that the rule applies to, add an optional filter\. You can filter by object key prefix, object tags, or combination of both\. If you filter on both a key prefix and object tags, Amazon S3 combines the filters using a logical AND operator\. In other words, the rule applies to a subset of objects with a specific key prefix and specific tags\. 

To specify a rule with a filter based on an object key prefix, use the following code\. You can specify only one prefix\.

```
<Rule>
    ...
    <Filter>
        <Prefix>key-prefix</Prefix>   
    </Filter>
    ...
</Rule>
...
```

To specify a rule with a filter based on object tags, use the following code\. You can specify one or more object tags\.

```
<Rule>
    ...
    <Filter>
        <And>
            <Tag>
                <Key>key1</Key>
                <Value>value1</Value>
            </Tag>
            <Tag>
                <Key>key2</Key>
                <Value>value2</Value>
            </Tag>
             ...
        </And>
    </Filter>
    ...
</Rule>
...
```

To specify a rule filter with a combination of a key prefix and object tags, use this code\. You warp these filters in a AND parent element\. Amazon S3 performs logical AND operation to combine these filters\. In other words, the rule applies to a subset of objects with a specific key prefix and specific tags\. 

```
<Rule>
    ...
    <Filter>
        <And>
            <Prefix>key-prefix</Prefix>
            <Tag>
                <Key>key1</Key>
                <Value>value1</Value>
            </Tag>
            <Tag>
                <Key>key2</Key>
                <Value>value2</Value>
            </Tag>
             ...
    </Filter>
    ...
</Rule>
...
```

## Additional Destination Configurations<a name="crr-config-optional-dest-config"></a>

In the destination configuration, you specify the bucket where you want Amazon S3 to replicate objects\. You can configure CRR to replicate objects from one source bucket to one destination bucket\. If you add multiple rules in a replication configuration, all of the rules must identify the same destination bucket\. 

```
...
<Destination>        
    <Bucket>arn:aws:s3:::destination-bucket</Bucket>
</Destination>
...
```

You have the following options you can add in the <Destination> element:
+ You can specify the storage class for the object replicas\. By default, Amazon S3 uses the storage class of the source object to create object replicas\. For example, 

  ```
  ...
  <Destination>
         <Bucket>arn:aws:s3:::destinationbucket</Bucket>
         <StorageClass>storage-class</StorageClass>
  </Destination>
  ...
  ```
+ When source and destination buckets aren't owned by the same accounts, you can change the ownership of the replica to the AWS account that owns the destination bucket by adding the `AccessControlTranslation` element:

  ```
  ...
  <Destination>
     <Bucket>arn:aws:s3:::destinationbucket</Bucket>
     <Account>destination-bucket-owner-account-id</Account>
     <AccessControlTranslation>
         <Owner>Destination</Owner>
     </AccessControlTranslation>
  </Destination>
  ...
  ```

  If you don't add this element to the replication configuration, the replicas are owned by same AWS account that owns the source object\. For more information, see [CRR Additional Configuration: Changing the Replica Owner](crr-change-owner.md)\.
+ Your source bucket might contain objects that were created with server\-side encryption using AWS KMS\-managed keys\. By default, Amazon S3 doesn't replicate these objects\. You can optionally direct Amazon S3 to replicate these objects by first explicitly opting into this feature by adding the SourceSelectionCriteria element and then providing the AWS KMS key \(for the AWS Region of the destination bucket\) to use for encrypting object replicas\. 

  ```
  ...
  <SourceSelectionCriteria>
    <SseKmsEncryptedObjects>
      <Status>Enabled</Status>
    </SseKmsEncryptedObjects>
  </SourceSelectionCriteria>
  <Destination>
    <Bucket>arn:aws:s3:::dest-bucket-name</Bucket>
    <EncryptionConfiguration>
      <ReplicaKmsKeyID>AWS KMS key IDs to use for encrypting object replicas</ReplicaKmsKeyID>
    </EncryptionConfiguration>
  </Destination>
  ...
  ```

  For more information, see [CRR Additional Configuration: Replicating Objects Created with Server\-Side Encryption \(SSE\) Using AWS KMS\-Managed Encryption Keys](crr-replication-config-for-kms-objects.md)\.

## Example Replication Configurations<a name="crr-config-example-configs"></a>

To get started, you can add the following example replication configurations to your bucket, as appropriate\.

**Important**  
To add a replication configuration to a bucket, you must have the `iam:PassRole` permission\. This permission allows you to pass the IAM role that grants Amazon S3 replication permissions\. You specify the IAM role by providing the Amazon Resource Name \(ARN\) that is used in the `Role` element in the replication configuration XML\. For more information, see [Granting a User Permissions to Pass a Role to an AWS Service](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_use_passrole.html) in the *IAM User Guide*\.

**Example 1: Replication Configuration with One Rule**  
The following basic replication configuration specifies one rule\. The rule specifies an IAM role that Amazon S3 can assume and a destination bucket for object replicas\. The rule `Status` indicates that the rule is in effect\.  

```
<?xml version="1.0" encoding="UTF-8"?>
<ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Role>arn:aws:iam::AcctID:role/role-name</Role>
  <Rule>
    <Status>Enabled</Status>

    <Destination><Bucket>arn:aws:s3:::destinationbucket</Bucket></Destination>

  </Rule>
</ReplicationConfiguration>
```
To choose a subset of objects to replicate, you can add a filter\. In the following configuration, the filter specifies an object key prefix\. This rule applies to objects that have the prefix `Tax/` in their key names\.   

```
<?xml version="1.0" encoding="UTF-8"?>
<ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Role>arn:aws:iam::AcctID:role/role-name</Role>
  <Rule>
    <Status>Enabled</Status>
    <Priority>1</Priority>
    <DeleteMarkerReplication>
       <Status>string</Status>
    </DeleteMarkerReplication>

    <Filter>
       <Prefix>Tax/</Prefix>
    </Filter>

    <Destination><Bucket>arn:aws:s3:::destinationbucket</Bucket></Destination>

  </Rule>
</ReplicationConfiguration>
```
If you specify the `Filter` element, you must also include the `Priority` and `DeleteMarkerReplication` elements\. In this example, priority is irrelevant because there is only one rule\.  
In the following configuration, the filter specifies one prefix and two tags\. The rule applies to the subset of objects that have the specified key prefix and tags\. Specifically, it applies to object that have the `Tax/` prefix in their key names and the two specified object tags\. Priority doesn't apply because there is only one rule\.  

```
<?xml version="1.0" encoding="UTF-8"?>
<ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Role>arn:aws:iam::AcctID:role/role-name</Role>
  <Rule>
    <Status>Enabled</Status>
    <Priority>1</Priority>
    <DeleteMarkerReplication>
       <Status>string</Status>
    </DeleteMarkerReplication>

    <Filter>
        <And>
          <Prefix>Tax/</Prefix>
          <Tag>
             <Tag>
                <Key>tagA</Key>
                <Value>valueA</Value>
             </Tag>
          </Tag>
          <Tag>
             <Tag>
                <Key>tagB</Key>
                <Value>valueB</Value>
             </Tag>
          </Tag>
       </And>

    </Filter>

    <Destination><Bucket>arn:aws:s3:::destinationbucket</Bucket></Destination>

  </Rule>
</ReplicationConfiguration>
```
You can specify a storage class for the object replicas as follows:  

```
<?xml version="1.0" encoding="UTF-8"?>

<ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Role>arn:aws:iam::account-id:role/role-name</Role>
  <Rule>
    <Status>Enabled</Status>
    <Destination>
       <Bucket>arn:aws:s3:::destinationbucket</Bucket>
       <StorageClass>storage-class</StorageClass>
    </Destination>
  </Rule>
</ReplicationConfiguration>
```
You can specify any storage class that Amazon S3 supports, except for `GLACIER`\. To transition objects to the `GLACIER` storage class, you use lifecycle configuration\. For more information about lifecycle management, see [Object Lifecycle Management](object-lifecycle-mgmt.md)\. For more information about storage classes, see [Storage Classes](storage-class-intro.md)\.

**Example 2: Replication Configuration with Two Rules**  

**Example**  
In the following replication configuration:  
+ Each rule filters on a different key prefix so that each rule applies to a distinct subset of objects\. Amazon S3 replicates objects with key names `Tax/doc1.pdf` and `Project/project1.txt`, but it doesn't replicate objects with the key name `PersonalDoc/documentA`\. 
+  Rule priority is irrelevant because the rules apply to two distinct sets of objects\. The next example shows what happens when rule priority is applied\. 
+ The second rule specifies a storage class for object replicas\. Amazon S3 uses the specified storage class for those object replicas\.
+ Both rules specify the same destination bucket\. You can specify only one destination bucket, regardless of how many rules you specify\.

```
<?xml version="1.0" encoding="UTF-8"?>

<ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Role>arn:aws:iam::account-id:role/role-name</Role>
  <Rule>
    <Status>Enabled</Status>
    <Priority>1</Priority>
    <DeleteMarkerReplication>
       <Status>string</Status>
    </DeleteMarkerReplication>
    <Filter>
        <Prefix>Tax</Prefix>
    </Filter>
    <Status>Enabled</Status>
    <Destination>
      <Bucket>arn:aws:s3:::destinationbucket</Bucket>
    </Destination>
     ...
  </Rule>
 <Rule>
    <Status>Enabled</Status>
    <Priority>2</Priority>
    <DeleteMarkerReplication>
       <Status>string</Status>
    </DeleteMarkerReplication>
    <Filter>
        <Prefix>Project</Prefix>
    </Filter>
    <Status>Enabled</Status>
    <Destination>
      <Bucket>arn:aws:s3:::destinationbucket</Bucket>
     <StorageClass>STANDARD_IA</StorageClass>
    </Destination>
     ...
  </Rule>


</ReplicationConfiguration>
```

**Example 3: Replication Configuration with Two Rules with Overlapping Prefixes**  <a name="overlap-rule-example"></a>
In this configuration, the two rules specify filters with overlapping key prefixes, `star/` and `starship`\. Both rules apply to objects with the keyname `starship-x`\. In this case, Amazon S3 uses the rule priority to determine which rule to apply\.   

```
<ReplicationConfiguration>

  <Role>arn:aws:iam::AcctID:role/role-name</Role>

  <Rule>
    <Status>Enabled</Status>
    <Priority>1</Priority>
    <DeleteMarkerReplication>
       <Status>string</Status>
    </DeleteMarkerReplication>
    <Filter>
        <Prefix>star</Prefix>
    </Filter>
    <Destination>
      <Bucket>arn:aws:s3:::destinationbucket</Bucket>
    </Destination>
  </Rule>
  <Rule>
    <Status>Enabled</Status>
    <Priority>1</Priority>
    <DeleteMarkerReplication>
       <Status>string</Status>
    </DeleteMarkerReplication>
    <Filter>
        <Prefix>starship</Prefix>
    </Filter>    
    <Destination>
      <Bucket>arn:aws:s3:::destinationbucket</Bucket>
    </Destination>
  </Rule>
</ReplicationConfiguration>
```

**Example 4: Example Walkthroughs**  
For example walkthroughs, see [Cross\-Region Replication \(CRR\) Walkthroughs](crr-example-walkthroughs.md)\.

For more information about the XML structure of replication configuration, see [PutBucketReplication](http://docs.aws.amazon.com/AmazonS3/latest/API/API_PutBucketReplication.html) in the *Amazon Simple Storage Service API Reference*\. 

## Backward Compatibility<a name="crr-backward-compat-considerations"></a>

The latest version of the replication configuration XML is V2\. For backward compatibility, `Amazon S3 ` continues to support the V1 configuration\. If you have used replication configuration XML V1, consider the following issues that affect backward compatibility:
+ Replication configuration XML V2 includes the `Filter` element for rules\. With the `Filter` element, you can specify object filters based on the object key prefix, tags, or both to scope the objects that the rule applies to\. Replication configuration XML V1 supported filtering based on only the key prefix, in which case you add the `Prefix` directly as a child element of the `Rule` element\. For example,

  ```
  <?xml version="1.0" encoding="UTF-8"?>
  <ReplicationConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Role>arn:aws:iam::AcctID:role/role-name</Role>
    <Rule>
      <Status>Enabled</Status>
      <Prefix>key-prefix</Prefix>
      <Destination><Bucket>arn:aws:s3:::destinationbucket</Bucket></Destination>
  
    </Rule>
  </ReplicationConfiguration>
  ```

  For backward compatibility, `Amazon S3 ` continues to support the V1 configuration\. 
+ When you delete an object from your source bucket without specifying an object version ID, Amazon S3 adds a delete marker\. If you use V1 of the replication configuration XML, Amazon S3 replicates delete markers that resulted from user actions\. In other words, if the user deleted the object, and not if Amazon S3 deleted it because the object expired as part of lifecycle action\. In V2, Amazon S3 doesn't replicate delete markers and therefore you must set the `DeleteMarkerReplication` element to `Disabled`\. 

  ```
  ...
      <Rule>
          <ID>Rule-1</ID>
          <Status>rule-Enabled-or-Diasbled</Priority>
          <Priority>integer</Status>
          <DeleteMarkerReplication>
             <Status>Disabled</Status>
          </DeleteMarkerReplication>        
          <Destination>        
             <Bucket>arn:aws:s3:::bucket-name</Bucket> 
          </Destination>    
      </Rule>
  ...
  ```