# Examples of lifecycle configuration<a name="lifecycle-configuration-examples"></a>

This section provides examples of S3 Lifecycle configuration\. Each example shows how you can specify the XML in each of the example scenarios\.

**Topics**
+ [Example 1: Specifying a filter](#lifecycle-config-ex1)
+ [Example 2: Disabling a lifecycle rule](#lifecycle-config-conceptual-ex2)
+ [Example 3: Tiering down storage class over an object's lifetime](#lifecycle-config-conceptual-ex3)
+ [Example 4: Specifying multiple rules](#lifecycle-config-conceptual-ex4)
+ [Example 5: Overlapping filters, conflicting lifecycle actions, and what Amazon S3 does](#lifecycle-config-conceptual-ex5)
+ [Example 6: Specifying a lifecycle rule for a versioning\-enabled bucket](#lifecycle-config-conceptual-ex6)
+ [Example 7: Removing expired object delete markers](#lifecycle-config-conceptual-ex7)
+ [Example 8: Lifecycle configuration to abort multipart uploads](#lc-expire-mpu)

## Example 1: Specifying a filter<a name="lifecycle-config-ex1"></a>

Each S3 Lifecycle rule includes a filter that you can use to identify a subset of objects in your bucket to which the Lifecycle rule applies\. The following S3 Lifecycle configurations show examples of how you can specify a filter\.
+ In this Lifecycle configuration rule, the filter specifies a key prefix \(`tax/`\)\. Therefore, the rule applies to objects with key name prefix `tax/`, such as `tax/doc1.txt` and `tax/doc2.txt`\.

  The rule specifies two actions that direct Amazon S3 to do the following:
  + Transition objects to the S3 Glacier storage class 365 days \(one year\) after creation\.
  + Delete objects \(the `Expiration` action\) 3,650 days \(10 years\) after creation\.

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
        <StorageClass>S3 Glacier</StorageClass>
      </Transition>
      <Expiration>
        <Days>3650</Days>
      </Expiration>
    </Rule>
  </LifecycleConfiguration>
  ```

  Instead of specifying object age in terms of days after creation, you can specify a date for each action\. However, you can't use both `Date` and `Days` in the same rule\. 
+ If you want the Lifecycle rule to apply to all objects in the bucket, specify an empty prefix\. In the following configuration, the rule specifies a `Transition` action directing Amazon S3 to transition objects to the S3 Glacier storage class 0 days after creation in which case objects are eligible for archival to Amazon S3 Glacier at midnight UTC following creation\. 

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
        <StorageClass>S3 Glacier</StorageClass>
      </Transition>
    </Rule>
  </LifecycleConfiguration>
  ```
+ You can specify zero or one key name prefix and zero or more object tags in a filter\. The following example code applies the Lifecycle rule to a subset of objects with the `tax/` key prefix and to objects that have two tags with specific key and value\. Note that when you specify more than one filter, you must include the AND as shown \(Amazon S3 applies a logical AND to combine the specified filter conditions\)\.

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
+ You can filter objects based only on tags\. For example, the following Lifecycle rule applies to objects that have the two specified tags \(it does not specify any prefix\)\.

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
When you have multiple rules in an S3 Lifecycle configuration, an object can become eligible for multiple Lifecycle actions\. In such cases, Amazon S3 follows these general rules:  
Permanent deletion takes precedence over transition\.
Transition takes precedence over creation of delete markers\.
When an object is eligible for both a S3 Glacier and S3 Standard\-IA \(or S3 One Zone\-IA\) transition, Amazon S3 chooses the S3 Glacier transition\.
 For examples, see [Example 5: Overlapping filters, conflicting lifecycle actions, and what Amazon S3 does ](#lifecycle-config-conceptual-ex5)\. 

## Example 2: Disabling a lifecycle rule<a name="lifecycle-config-conceptual-ex2"></a>

You can temporarily disable a Lifecycle rule\. The following Lifecycle configuration specifies two rules:
+ Rule 1 directs Amazon S3 to transition objects with the `logs/` prefix to the S3 Glacier storage class soon after creation\. 
+ Rule 2 directs Amazon S3 to transition objects with the `documents/` prefix to the S3 Glacier storage class soon after creation\. 

In the policy, Rule 1 is enabled and Rule 2 is disabled\. Amazon S3 does not take any action on disabled rules\.

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
      <StorageClass>S3 Glacier</StorageClass>
    </Transition>
  </Rule>
  <Rule>
    <ID>Rule2</ID>
    <Prefix>documents/</Prefix>
    <Status>Disabled</Status>
    <Transition>
      <Days>0</Days>
      <StorageClass>S3 Glacier</StorageClass>
    </Transition>
  </Rule>
</LifecycleConfiguration>
```

## Example 3: Tiering down storage class over an object's lifetime<a name="lifecycle-config-conceptual-ex3"></a>

In this example, you use lifecycle configuration to tier down the storage class of objects over their lifetime\. Tiering down can help reduce storage costs\. For more information about pricing, see [Amazon S3 pricing](https://aws.amazon.com/s3/pricing/)\.

The following S3 Lifecycle configuration specifies a rule that applies to objects with key name prefix `logs/`\. The rule specifies the following actions:
+ Two transition actions:
  + Transition objects to the S3 Standard\-IA storage class 30 days after creation\.
  + Transition objects to the S3 Glacier storage class 90 days after creation\.
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
You can use one rule to describe all Lifecycle actions if all actions apply to the same set of objects \(identified by the filter\)\. Otherwise, you can add multiple rules with each specifying a different filter\.

## Example 4: Specifying multiple rules<a name="lifecycle-config-conceptual-ex4"></a>

You can specify multiple rules if you want different Lifecycle actions of different objects\. The following Lifecycle configuration has two rules:
+ Rule 1 applies to objects with the key name prefix `classA/`\. It directs Amazon S3 to transition objects to the S3 Glacier storage class one year after creation and expire these objects 10 years after creation\.
+ Rule 2 applies to objects with key name prefix `classB/`\. It directs Amazon S3 to transition objects to the S3 Standard\-IA storage class 90 days after creation and delete them one year after creation\.

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

## Example 5: Overlapping filters, conflicting lifecycle actions, and what Amazon S3 does<a name="lifecycle-config-conceptual-ex5"></a>

You might specify an S3 Lifecycle configuration in which you specify overlapping prefixes, or actions\.

Generally, Amazon S3 Lifecycle optimizes for cost\. For example, if two expiration policies overlap, the shorter expiration policy is honored so that data is not stored for longer than expected\. 

Likewise, if two transition policies overlap, S3 Lifecycle transitions your objects to the lower\-cost storage class\. In both cases, S3 Lifecycle tries to choose the path that is least expensive for you\. An exception to this general rule is with the S3 Intelligent\-Tiering storage class\. S3 Intelligent\-Tiering is favored by S3 Lifecycle over any storage class, aside from S3 Glacier and S3 Glacier Deep Archive storage classes\.

The following examples show how Amazon S3 chooses to resolve potential conflicts\.

**Example 1: Overlapping prefixes \(no conflict\)**  
The following example configuration has two rules that specify overlapping prefixes as follows:  
+ First rule specifies an empty filter, indicating all objects in the bucket\. 
+ Second rule specifies a key name prefix `logs/`, indicating only a subset of objects\.
Rule 1 requests Amazon S3 to delete all objects one year after creation\. Rule 2 requests Amazon S3 to transition a subset of objects to the S3 Standard\-IA storage class 30 days after creation\.  

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

**Example 2: Conflicting lifecycle actions**  
In this example configuration, there are two rules that direct Amazon S3 to perform two different actions on the same set of objects at the same time in object's lifetime:  
+ Both rules specify the same key name prefix, so both rules apply to the same set of objects\.
+ Both rules specify the same 365 days after object creation when the rules apply\.
+ One rule directs Amazon S3 to transition objects to the S3 Standard\-IA storage class and another rule wants Amazon S3 to expire the objects at the same time\.

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

**Example 3: Overlapping prefixes resulting in conflicting lifecycle actions**  
In this example, the configuration has two rules, which specify overlapping prefixes as follows:  
+ Rule 1 specifies an empty prefix \(indicating all objects\)\.
+ Rule 2 specifies a key name prefix \(`logs/`\) that identifies a subset of all objects\.
For the subset of objects with the `logs/` key name prefix, Lifecycle actions in both rules apply\. One rule directing Amazon S3 to transition objects 10 days after creation and another rule directing Amazon S3 to transition objects 365 days after creation\.   

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

**Example 4: Tag\-based filtering and resulting conflicting lifecycle actions**  
Suppose that you have the following S3 Lifecycle policy that has two rules, each specifying a tag filter:  
+ Rule 1 specifies a tag\-based filter \(`tag1/value1`\)\. This rule directs Amazon S3 to transition objects to the S3 Glacier storage class 365 days after creation\.
+ Rule 2 specifies a tag\-based filter \(`tag2/value2`\)\. This rule directs Amazon S3 to expire objects 14 days after creation\.
The Lifecycle configuration is shown following\.  

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
The policy is fine, but if there is an object with both tags, then S3 has to decide what to do\. That is, both rules apply to an object, and in effect you are directing Amazon S3 to perform conflicting actions\. In this case, Amazon S3 expires the object 14 days after creation\. The object is removed, and therefore the transition action does not come into play\.

## Example 6: Specifying a lifecycle rule for a versioning\-enabled bucket<a name="lifecycle-config-conceptual-ex6"></a>

Suppose that you have a versioning\-enabled bucket, which means that for each object you have a current version and zero or more noncurrent versions\. You want to maintain one year's worth of history and then delete the noncurrent versions\. For more information about S3 Versioning, see [Object Versioning](ObjectVersioning.md)\. 

Also, you want to save storage costs by moving noncurrent versions to S3 Glacier 30 days after they become noncurrent \(assuming cold data for which you don't need real\-time access\)\. In addition, you also expect frequency of access of the current versions to diminish 90 days after creation so you might choose to move these objects to the S3 Standard\-IA storage class\.

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
14.             <StorageClass>S3 Glacier</StorageClass>   
15.         </NoncurrentVersionTransition>    
16.        <NoncurrentVersionExpiration>     
17.             <NoncurrentDays>365</NoncurrentDays>    
18.        </NoncurrentVersionExpiration> 
19.     </Rule>
20. </LifecycleConfiguration>
```

## Example 7: Removing expired object delete markers<a name="lifecycle-config-conceptual-ex7"></a>

A versioning\-enabled bucket has one current version and zero or more noncurrent versions for each object\. When you delete an object, note the following:
+ If you don't specify a version ID in your delete request, Amazon S3 adds a delete marker instead of deleting the object\. The current object version becomes noncurrent, and then the delete marker becomes the current version\. 
+ If you specify a version ID in your delete request, Amazon S3 deletes the object version permanently \(a delete marker is not created\)\.
+ A delete marker with zero noncurrent versions is referred to as the *expired object delete marker*\. 

This example shows a scenario that can create expired object delete markers in your bucket, and how you can use S3 Lifecycle configuration to direct Amazon S3 to remove the expired object delete markers\.

Suppose that you write a Lifecycle policy that specifies the `NoncurrentVersionExpiration` action to remove the noncurrent versions 30 days after they become noncurrent, as shown following\.

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

The `NoncurrentVersionExpiration` action does not apply to the current object versions\. It only removes noncurrent versions\.

For current object versions, you have the following options to manage their lifetime depending on whether the current object versions follow a well\-defined lifecycle: 
+ **Current object versions follow a well\-defined lifecycle\.**

  In this case you can use Lifecycle policy with the `Expiration` action to direct Amazon S3 to remove current versions as shown in the following example\.

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

  Amazon S3 removes current versions 60 days after they are created by adding a delete marker for each of the current object versions\. This makes the current version noncurrent and the delete marker becomes the current version\. For more information, see [Using versioning](Versioning.md)\. 
**Note**  
This rule will automatically perform `ExpiredObjectDeleteMarker` cleanup in a versioned bucket making the need to include an `ExpiredObjectDeleteMarker` tag unnecessary\.

  The `NoncurrentVersionExpiration` action in the same Lifecycle configuration removes noncurrent objects 30 days after they become noncurrent\. Thus, in this example, all object versions are permanently removed 90 days after object creation\. You will have expired object delete markers, but Amazon S3 detects and removes the expired object delete markers for you\. 
+ **Current object versions don't have a well\-defined lifecycle\.** 

  In this case you might remove the objects manually when you don't need them, creating a delete marker with one or more noncurrent versions\. If Lifecycle configuration with `NoncurrentVersionExpiration` action removes all the noncurrent versions, you now have expired object delete markers\.

  Specifically for this scenario, Amazon S3 Lifecycle configuration provides an `Expiration` action where you can request Amazon S3 to remove the expired object delete markers\.

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
When specifying the `ExpiredObjectDeleteMarker` Lifecycle action, the rule cannot specify a tag\-based filter\.

## Example 8: Lifecycle configuration to abort multipart uploads<a name="lc-expire-mpu"></a>

You can use the multipart upload API to upload large objects in parts\. For more information about multipart uploads, see [Multipart upload overview](mpuoverview.md)\. 

Using S3 Lifecycle configuration, you can direct Amazon S3 to stop incomplete multipart uploads \(identified by the key name prefix specified in the rule\) if they don't complete within a specified number of days after initiation\. When Amazon S3 aborts a multipart upload, it deletes all parts associated with the multipart upload\. This ensures that you don't have incomplete multipart uploads with parts that are stored in Amazon S3 and, therefore, you don't have to pay any storage costs for these parts\. 

**Note**  
When specifying the `AbortIncompleteMultipartUpload` Lifecycle action, the rule cannot specify a tag\-based filter\.

The following is an example S3 Lifecycle configuration that specifies a rule with the `AbortIncompleteMultipartUpload` action\. This action requests Amazon S3 to stop incomplete multipart uploads seven days after initiation\.

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