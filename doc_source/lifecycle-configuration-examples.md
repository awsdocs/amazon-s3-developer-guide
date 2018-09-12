# Examples of Lifecycle Configuration<a name="lifecycle-configuration-examples"></a>

This section provides examples of lifecycle configuration\. Each example shows how you can specify the XML in each of the example scenarios\.

**Topics**
+ [Example 1: Specifying a Filter](#lifecycle-config-ex1)
+ [Example 2: Disabling a Lifecycle Rule](#lifecycle-config-conceptual-ex2)
+ [Example 3: Tiering Down Storage Class over an Object's Lifetime](#lifecycle-config-conceptual-ex3)
+ [Example 4: Specifying Multiple Rules](#lifecycle-config-conceptual-ex4)
+ [Example 5: Overlapping Filters, Conflicting Lifecycle Actions, and What Amazon S3 Does](#lifecycle-config-conceptual-ex5)
+ [Example 6: Specifying a Lifecycle Rule for a Versioning\-Enabled Bucket](#lifecycle-config-conceptual-ex6)
+ [Example 7: Removing Expired Object Delete Markers](#lifecycle-config-conceptual-ex7)
+ [Example 8: Lifecycle Configuration to Abort Multipart Uploads](#lc-expire-mpu)

## Example 1: Specifying a Filter<a name="lifecycle-config-ex1"></a>

Each lifecycle rule includes a filter that you can use to identify a subset of objects in your bucket to which the lifecycle rule applies\. The following lifecycle configurations show examples of how you can specify a filter\.
+ In this lifecycle configuration rule, the filter specifies a key prefix \(`tax/`\)\. Therefore, the rule applies to objects with key name prefix `tax/`, such as `tax/doc1.txt` and `tax/doc2.txt`

  The rule specifies two actions that direct Amazon S3 to do the following:
  + Transition objects to the GLACIER storage class 365 days \(one year\) after creation\.
  + Delete objects \(the `Expiration` action\) 3650 days \(10 years\) after creation\.

  ```
  <LifecycleConfiguration>
    <Rule>
      <ID>Transition and Expiration Rule</ID>
      <Filter>
         <Prefix>tax/</Prefix>
      </Filter>
      <Status>Enabled</Status>
      <Transition>
        <Days>365</Days>
        <StorageClass>GLACIER</StorageClass>
      </Transition>
      <Expiration>
        <Days>3650</Days>
      </Expiration>
    </Rule>
  </LifecycleConfiguration>
  ```

  Instead of specifying object age in terms of days after creation, you can specify a date for each action\. However, you can't use both `Date` and `Days` in the same rule\. 
+ If you want the lifecycle rule to apply to all objects in the bucket, specify an empty prefix\. In the following configuration, the rule specifies a `Transition` action directing Amazon S3 to transition objects to the GLACIER storage class 0 days after creation in which case objects are eligible for archival to Amazon Glacier at midnight UTC following creation\. 

  ```
  <LifecycleConfiguration>
    <Rule>
      <ID>Archive all object same-day upon creation</ID>
      <Filter>
        <Prefix></Prefix>
      </Filter>
      <Status>Enabled</Status>
      <Transition>
        <Days>0</Days>
        <StorageClass>GLACIER</StorageClass>
      </Transition>
    </Rule>
  </LifecycleConfiguration>
  ```
+ You can specify zero or one key name prefix and zero or more object tags in a filter\. The following example code applies the lifecycle rule to a subset of objects with the `tax/` key prefix and to objects that have two tags with specific key and value\. Note that when you specify more than one filter, you must include the AND as shown \(Amazon S3 applies a logical AND to combine the specified filter conditions\)\.

  ```
  ...
  <Filter>
     <And>
        <Prefix>tax/</Prefix>
        <Tag>
           <Key>key1</Key>
           <Value>value1</Value>
        </Tag>
        <Tag>
           <Key>key2</Key>
           <Value>value2</Value>
        </Tag>
      </And>
  </Filter>
  ...
  ```
+ You can filter objects based only on tags\. For example, the following lifecycle rule applies to objects that have the two specified tags \(it does not specify any prefix\):

  ```
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
      </And>
  </Filter>
  ...
  ```

**Important**  
When you have multiple rules in a lifecycle configuration, an object can become eligible for multiple lifecycle actions\. The general rules that Amazon S3 follows in such cases are:  
Permanent deletion takes precedence over transition\.
Transition takes precedence over creation of delete markers\.
When an object is eligible for both a GLACIER and STANDARD\_IA \(or ONEZONE\_IA\) transition, Amazon S3 chooses the GLACIER transition\.
 For examples, see [Example 5: Overlapping Filters, Conflicting Lifecycle Actions, and What Amazon S3 Does ](#lifecycle-config-conceptual-ex5) 

## Example 2: Disabling a Lifecycle Rule<a name="lifecycle-config-conceptual-ex2"></a>

You can temporarily disable a lifecycle rule\. The following lifecycle configuration specifies two rules:
+ Rule 1 directs Amazon S3 to transition objects with the `logs/` prefix to the GLACIER storage class soon after creation\. 
+ Rule 2 directs Amazon S3 to transition objects with the `documents/` prefix to the GLACIER storage class soon after creation\. 

In the policy Rule 1 is enabled and Rule 2 is disable\. Amazon S3 will not take any action on disabled rules\.

```
<LifecycleConfiguration>
  <Rule>
    <ID>Rule1</ID>
    <Filter>
      <Prefix>logs/</Prefix>
    </Filter>
    <Status>Enabled</Status>
    <Transition>
      <Days>0</Days>
      <StorageClass>GLACIER</StorageClass>
    </Transition>
  </Rule>
  <Rule>
    <ID>Rule2</ID>
    <Prefix>documents/</Prefix>
    <Status>Disabled</Status>
    <Transition>
      <Days>0</Days>
      <StorageClass>GLACIER</StorageClass>
    </Transition>
  </Rule>
</LifecycleConfiguration>
```

## Example 3: Tiering Down Storage Class over an Object's Lifetime<a name="lifecycle-config-conceptual-ex3"></a>

In this example, you leverage lifecycle configuration to tier down the storage class of objects over their lifetime\. Tiering down can help reduce storage costs\. For more information about pricing, see [Amazon S3 Pricing](https://aws.amazon.com/s3/pricing/)\.

The following lifecycle configuration specifies a rule that applies to objects with key name prefix `logs/`\. The rule specifies the following actions:
+ Two transition actions:
  + Transition objects to the STANDARD\_IA storage class 30 days after creation\.
  + Transition objects to the GLACIER storage class 90 days after creation\.
+ One expiration action that directs Amazon S3 to delete objects a year after creation\.

```
<LifecycleConfiguration>
  <Rule>
    <ID>example-id</ID>
    <Filter>
       <Prefix>logs/</Prefix>
    </Filter>
    <Status>Enabled</Status>
    <Transition>
      <Days>30</Days>
      <StorageClass>STANDARD_IA</StorageClass>
    </Transition>
    <Transition>
      <Days>90</Days>
      <StorageClass>GLACIER</StorageClass>
    </Transition>
    <Expiration>
      <Days>365</Days>
    </Expiration>
  </Rule>
</LifecycleConfiguration>
```

**Note**  
You can use one rule to describe all lifecycle actions if all actions apply to the same set of objects \(identified by the filter\)\. Otherwise, you can add multiple rules with each specifying a different filter\.

## Example 4: Specifying Multiple Rules<a name="lifecycle-config-conceptual-ex4"></a>

You can specify multiple rules if you want different lifecycle actions of different objects\. The following lifecycle configuration has two rules:
+ Rule 1 applies to objects with the key name prefix `classA/`\. It directs Amazon S3 to transition objects to the GLACIER storage class one year after creation and expire these objects 10 years after creation\.
+ Rule 2 applies to objects with key name prefix `classB/`\. It directs Amazon S3 to transition objects to the STANDARD\_IA storage class 90 days after creation and delete them one year after creation\.

```
<LifecycleConfiguration>
    <Rule>
        <ID>ClassADocRule</ID>
        <Filter>
           <Prefix>classA/</Prefix>        
        </Filter>
        <Status>Enabled</Status>
        <Transition>        
           <Days>365</Days>        
           <StorageClass>GLACIER</StorageClass>       
        </Transition>    
        <Expiration>
             <Days>3650</Days>
        </Expiration>
    </Rule>
    <Rule>
        <ID>ClassBDocRule</ID>
        <Filter>
            <Prefix>classB/</Prefix>
        </Filter>
        <Status>Enabled</Status>
        <Transition>        
           <Days>90</Days>        
           <StorageClass>STANDARD_IA</StorageClass>       
        </Transition>    
        <Expiration>
             <Days>365</Days>
        </Expiration>
    </Rule>
</LifecycleConfiguration>
```

## Example 5: Overlapping Filters, Conflicting Lifecycle Actions, and What Amazon S3 Does<a name="lifecycle-config-conceptual-ex5"></a>

You might specify a lifecycle configuration in which you specify overlapping prefixes, or actions\. The following examples show how Amazon S3 chooses to resolve potential conflicts\. 

**Example 1: Overlapping Prefixes \(No Conflict\)**  
The following example configuration has two rules that specify overlapping prefixes as follows:  
+ First rule specifies an empty filter, indicating all objects in the bucket\. 
+ Second rule specifies a key name prefix `logs/`, indicating only a subset of objects\.
Rule 1 requests Amazon S3 to delete all objects one year after creation, and Rule 2 requests Amazon S3 to transition a subset of objects to the STANDARD\_IA storage class 30 days after creation\.  

```
 1. <LifecycleConfiguration>
 2.   <Rule>
 3.     <ID>Rule 1</ID>
 4.     <Filter>
 5.     </Filter>
 6.     <Status>Enabled</Status>
 7.     <Expiration>
 8.       <Days>365</Days>
 9.     </Expiration>
10.   </Rule>
11.   <Rule>
12.     <ID>Rule 2</ID>
13.     <Filter>
14.       <Prefix>logs/</Prefix>
15.     </Filter>
16.     <Status>Enabled</Status>
17.     <Transition>
18.       <StorageClass>STANDARD_IA<StorageClass>
19.       <Days>30</Days>
20.     </Transition>
21.    </Rule>
22. </LifecycleConfiguration>
```

**Example 2: Conflicting Lifecycle Actions**  
In this example configuration, there are two rules that direct Amazon S3 to perform two different actions on the same set of objects at the same time in object's lifetime:  
+ Both rules specify the same key name prefix, so both rules apply to the same set of objects\.
+ Both rules specify the same 365 days after object creation when the rules apply\.
+ One rule directs Amazon S3 to transition objects to the STANDARD\_IA storage class and another rule wants Amazon S3 to expire the objects at the same time\.

```
<LifecycleConfiguration>
  <Rule>
    <ID>Rule 1</ID>
    <Filter>
      <Prefix>logs/</Prefix>
    </Filter>
    <Status>Enabled</Status>
    <Expiration>
      <Days>365</Days>
    </Expiration>        
  </Rule>
  <Rule>
    <ID>Rule 2</ID>
    <Filter>
      <Prefix>logs/</Prefix>
    </Filter>
    <Status>Enabled</Status>
    <Transition>
      <StorageClass>STANDARD_IA<StorageClass>
      <Days>365</Days>
    </Transition>
   </Rule>
</LifecycleConfiguration>
```
In this case, because you want objects to expire \(removed\), there is no point in changing the storage class, and Amazon S3 simply chooses the expiration action on these objects\.

**Example 3: Overlapping Prefixes Resulting in Conflicting Lifecycle Actions**  
In this example, the configuration has two rules which specify overlapping prefixes as follows:  
+ Rule 1 specifies an empty prefix \(indicating all objects\)\.
+ Rule 2 specifies a key name prefix \(`logs/`\) that identifies a subset of all objects\.
For the subset of objects with the `logs/` key name prefix, lifecycle actions in both rules apply\. One rule directing Amazon S3 to transition objects 10 days after creation and another rule directing Amazon S3 to transition objects 365 days after creation\.   

```
<LifecycleConfiguration>
  <Rule>
    <ID>Rule 1</ID>
    <Filter>
      <Prefix></Prefix>
    </Filter>
    <Status>Enabled</Status>
    <Transition>
      <StorageClass>STANDARD_IA<StorageClass>
      <Days>10</Days> 
    </Transition>
  </Rule>
  <Rule>
    <ID>Rule 2</ID>
    <Filter>
      <Prefix>logs/</Prefix>
    </Filter>
    <Status>Enabled</Status>
    <Transition>
      <StorageClass>STANDARD_IA<StorageClass>
      <Days>365</Days> 
    </Transition>
   </Rule>
</LifecycleConfiguration>
```
In this case, Amazon S3 chooses to transition them 10 days after creation\. 

**Example 4: Tag\-based Filtering and Resulting Conflicting Lifecycle Actions**  
Suppose you have the following lifecycle policy that has two rules, each specifying a tag filter:  
+ Rule 1 specifies a tag\-based filter \(`tag1/value1`\)\. This rule directs Amazon S3 to transition objects to the GLACIER storage class 365 days after creation\.
+ Rule 2 specifies a tag\-based filter \(`tag2/value2`\)\. This rule directs Amazon S3 to expire objects 14 days after creation\.
The lifecycle configuration is shown following:  

```
<LifecycleConfiguration>
  <Rule>
    <ID>Rule 1</ID>
    <Filter>
      <Tag>
         <Key>tag1</Key>
         <Value>value1</Value>
      </Tag>
    </Filter>
    <Status>Enabled</Status>
    <Transition>
      <StorageClass>GLACIER<StorageClass>
      <Days>365</Days> 
    </Transition>
  </Rule>
  <Rule>
    <ID>Rule 2</ID>
    <Filter>
      <Tag>
         <Key>tag2</Key>
         <Value>value1</Value>
      </Tag>
    </Filter>
    <Status>Enabled</Status>
    <Expiration>
      <Days>14</Days> 
    </Expiration>
   </Rule>
</LifecycleConfiguration>
```
The policy is fine, but if there is an object with both tags, then S3 has to decide what to do\. That is, both rules apply to an object and in effect you are directing Amazon S3 to perform conflicting actions\. In this case, Amazon S3 expires the object 14 days after creation\. The object is removed, and therefore the transition action does not come into play\.

## Example 6: Specifying a Lifecycle Rule for a Versioning\-Enabled Bucket<a name="lifecycle-config-conceptual-ex6"></a>

Suppose you have a versioning\-enabled bucket, which means that for each object you have a current version and zero or more noncurrent versions\. You want to maintain one year's worth of history and then delete the noncurrent versions\. For more information about versioning, see [Object Versioning](ObjectVersioning.md)\. 

Also, you want to save storage costs by moving noncurrent versions to GLACIER 30 days after they become noncurrent \(assuming cold data for which you don't need real\-time access\)\. In addition, you also expect frequency of access of the current versions to diminish 90 days after creation so you might choose to move these objects to the STANDARD\_IA storage class\.

```
 1. <LifecycleConfiguration>
 2.     <Rule>
 3.         <ID>sample-rule</ID>
 4.         <Filter>
 5.            <Prefix></Prefix>
 6.         </Filter>
 7.         <Status>Enabled</Status>
 8.         <Transition>
 9.            <Days>90</Days>
10.            <StorageClass>STANDARD_IA</StorageClass>
11.         </Transition>
12.         <NoncurrentVersionTransition>      
13.             <NoncurrentDays>30</NoncurrentDays>      
14.             <StorageClass>GLACIER</StorageClass>   
15.         </NoncurrentVersionTransition>    
16.        <NoncurrentVersionExpiration>     
17.             <NoncurrentDays>365</NoncurrentDays>    
18.        </NoncurrentVersionExpiration> 
19.     </Rule>
20. </LifecycleConfiguration>
```

## Example 7: Removing Expired Object Delete Markers<a name="lifecycle-config-conceptual-ex7"></a>

A versioning\-enabled bucket has one current version and one or more noncurrent versions for each object\. When you delete an object, note the following:
+ If you don't specify a version ID in your delete request, Amazon S3 adds a delete marker instead of deleting the object\. The current object version becomes noncurrent, and then the delete marker becomes the current version\. 
+ If you specify a version ID in your delete request, Amazon S3 deletes the object version permanently \(a delete marker is not created\)\.
+ A delete marker with zero noncurrent versions is referred to as the *expired object delete marker*\. 

This example shows a scenario that can create expired object delete markers in your bucket, and how you can use lifecycle configuration to direct Amazon S3 to remove the expired object delete markers\.

Suppose you write a lifecycle policy that specifies the `NoncurrentVersionExpiration` action to remove the noncurrent versions 30 days after they become noncurrent as shown following: 

```
<LifecycleConfiguration>
    <Rule>
        ...
        <NoncurrentVersionExpiration>     
            <NoncurrentDays>30</NoncurrentDays>    
        </NoncurrentVersionExpiration>
    </Rule>
</LifecycleConfiguration>
```

The `NoncurrentVersionExpiration` action does not apply to the current object versions, it only removes noncurrent versions\.

For current object versions, you have the following options to manage their lifetime depending on whether or not the current object versions follow a well\-defined lifecycle: 
+ **Current object versions follow a well\-defined lifecycle\.**

  In this case you can use lifecycle policy with the `Expiration` action to direct Amazon S3 to remove current versions as shown in the following example: 

  ```
  <LifecycleConfiguration>
      <Rule>
          ...
          <Expiration>
             <Days>60</Days>
          </Expiration>
          <NoncurrentVersionExpiration>     
              <NoncurrentDays>30</NoncurrentDays>    
          </NoncurrentVersionExpiration>
      </Rule>
  </LifecycleConfiguration>
  ```

  Amazon S3 removes current versions 60 days after they are created by adding a delete marker for each of the current object versions\. This makes the current version noncurrent and the delete marker becomes the current version\. For more information, see [Using Versioning](Versioning.md)\. 

  The `NoncurrentVersionExpiration` action in the same lifecycle configuration removes noncurrent objects 30 days after they become noncurrent\. Thus, all object versions are removed and you have expired object delete markers, but Amazon S3 detects and removes the expired object delete markers for you\. 
+ **Current object versions don't have a well\-defined lifecycle\.** 

  In this case you might remove the objects manually when you don't need them, creating a delete marker with one or more noncurrent versions\. If lifecycle configuration with `NoncurrentVersionExpiration` action removes all the noncurrent versions, you now have expired object delete markers\.

  Specifically for this scenario, Amazon S3 lifecycle configuration provides an `Expiration` action where you can request Amazon S3 to remove the expired object delete markers: 

  ```
  <LifecycleConfiguration>
      <Rule>
         <ID>Rule 1</ID>
          <Filter>
            <Prefix>logs/</Prefix>
          </Filter>
          <Status>Enabled</Status>
          <Expiration>
             <ExpiredObjectDeleteMarker>true</ExpiredObjectDeleteMarker>
          </Expiration>
          <NoncurrentVersionExpiration>     
              <NoncurrentDays>30</NoncurrentDays>    
          </NoncurrentVersionExpiration>
      </Rule>
  </LifecycleConfiguration>
  ```

By setting the `ExpiredObjectDeleteMarker` element to true in the `Expiration` action, you direct Amazon S3 to remove expired object delete markers\.

**Note**  
When specifying the `ExpiredObjectDeleteMarker` lifecycle action, the rule cannot specify a tag\-based filter\.

## Example 8: Lifecycle Configuration to Abort Multipart Uploads<a name="lc-expire-mpu"></a>

You can use the multipart upload API to upload large objects in parts\. For more information about multipart uploads, see [Multipart Upload Overview](mpuoverview.md)\. 

Using lifecycle configuration, you can direct Amazon S3 to abort incomplete multipart uploads \(identified by the key name prefix specified in the rule\) if they don't complete within a specified number of days after initiation\. When Amazon S3 aborts a multipart upload, it deletes all parts associated with the multipart upload\. This ensures that you don't have incomplete multipart uploads with parts that are stored in Amazon S3 and, therefore, you don't have to pay any storage costs for these parts\. 

**Note**  
When specifying the `AbortIncompleteMultipartUpload` lifecycle action, the rule cannot specify a tag\-based filter\.

The following is an example lifecycle configuration that specifies a rule with the `AbortIncompleteMultipartUpload` action\. This action requests Amazon S3 to abort incomplete multipart uploads seven days after initiation\.

```
<LifecycleConfiguration>
    <Rule>
        <ID>sample-rule</ID>
        <Filter>
           <Prefix>SomeKeyPrefix/</Prefix>
        </Filter>
        <Status>rule-status</Status>
        <AbortIncompleteMultipartUpload>
          <DaysAfterInitiation>7</DaysAfterInitiation>
        </AbortIncompleteMultipartUpload>
    </Rule>
</LifecycleConfiguration>
```