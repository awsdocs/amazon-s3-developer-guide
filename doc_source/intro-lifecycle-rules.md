# Lifecycle Configuration Elements<a name="intro-lifecycle-rules"></a>

**Topics**
+ [ID Element](#intro-lifecycle-rule-id)
+ [Status Element](#intro-lifecycle-rule-status)
+ [Filter Element](#intro-lifecycle-rules-filter)
+ [Elements to Describe Lifecycle Actions](#intro-lifecycle-rules-actions)

You specify a lifecycle configuration as XML, consisting of one or more lifecycle rules\. 

```
<LifecycleConfiguration>
    <Rule>
         ...
    </Rule>
    <Rule>
         ...
    </Rule>
</LifecycleConfiguration>
```

Each rule consists of the following:
+ Rule metadata that include a rule ID, and status indicating whether the rule is enabled or disabled\. If a rule is disabled, Amazon S3 doesn't perform any actions specified in the rule\.
+ Filter identifying objects to which the rule applies\. You can specify a filter by using an object key prefix, one or more object tags, or both\. 
+ One or more transition or expiration actions with a date or a time period in the object's lifetime when you want Amazon S3 to perform the specified action\. 

The following sections describe the XML elements in a lifecycle configuration\. For example lifecycle configurations, see [Examples of Lifecycle Configuration](lifecycle-configuration-examples.md)\.

## ID Element<a name="intro-lifecycle-rule-id"></a>

A lifecycle configuration can have up to 1,000 rules\. The <ID> element uniquely identifies a rule\. ID length is limited to 255 characters\.

## Status Element<a name="intro-lifecycle-rule-status"></a>

The <Status> element value can be either Enabled or Disabled\. If a rule is disabled, Amazon S3 doesn't perform any of the actions defined in the rule\.

## Filter Element<a name="intro-lifecycle-rules-filter"></a>

A lifecycle rule can apply to all or a subset of objects in a bucket based on the <Filter> element that you specify in the lifecycle rule\. 

You can filter objects by key prefix, object tags, or a combination of both \(in which case Amazon S3 uses a logical AND to combine the filters\)\. Consider the following examples:
+ **Specifying a filter using key prefixes** – This example shows a lifecycle rule that applies to a subset of objects based on the key name prefix \(`logs/`\)\. For example, the lifecycle rule applies to objects `logs/mylog.txt`, `logs/temp1.txt`, and `logs/test.txt`\. The rule does not apply to the object `example.jpg`\.

  ```
  <LifecycleConfiguration>
      <Rule>
          <Filter>
             <Prefix>logs/</Prefix>
          </Filter>
          transition/expiration actions.
           ...
      </Rule>
      ...
  </LifecycleConfiguration>
  ```

  If you want to apply a lifecycle action to a subset of objects based on different key name prefixes, specify separate rules\. In each rule, specify a prefix\-based filter\. For example, to describe a lifecycle action for objects with key prefixes `projectA/` and `projectB/`, you specify two rules as shown following: 

  ```
  <LifecycleConfiguration>
      <Rule>
          <Filter>
             <Prefix>projectA/</Prefix>
          </Filter>
          transition/expiration actions.
           ...
      </Rule>
  
      <Rule>
          <Filter>
             <Prefix>projectB/</Prefix>
          </Filter>
          transition/expiration actions.
           ...
      </Rule>
  </LifecycleConfiguration>
  ```

  For more information about object keys, see [Object Keys](UsingMetadata.md#object-keys)\. 
+ **Specifying a filter based on object tags** – In the following example, the lifecycle rule specifies a filter based on a tag \(*key*\) and value \(*value*\)\. The rule then applies only to a subset of objects with the specific tag\.

  ```
  <LifecycleConfiguration>
      <Rule>
          <Filter>
             <Tag>
                <Key>key</Key>
                <Value>value</Value>
             </Tag>
          </Filter>
          transition/expiration actions.
          ...
      </Rule>
  </LifecycleConfiguration>
  ```

  You can specify a filter based on multiple tags\. You must wrap the tags in the <AND> element shown in the following example\. The rule directs Amazon S3 to perform lifecycle actions on objects with two tags \(with the specific tag key and value\)\.

  ```
  <LifecycleConfiguration>
      <Rule>
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
        transition/expiration actions.
      </Rule>
  </Lifecycle>
  ```

  The lifecycle rule applies to objects that have both of the tags specified\. Amazon S3 performs a logical AND\. Note the following:
  + Each tag must match both key and value exactly\.
  + The rule applies to a subset of objects that have one or more tags specified in the rule\. If an object has additional tags specified, it doesn't matter\.
**Note**  
When you specify multiple tags in a filter, each tag key must be unique\.
+ **Specifying a filter based on both prefix and one or more tags** – In a lifecycle rule, you can specify a filter based on both the key prefix and one or more tags\. Again, you must wrap all of these in the <And> element as shown following: 

  ```
  <LifecycleConfiguration>
      <Rule>
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
            </And>
          </Filter>
          <Status>Enabled</Status>
          transition/expiration actions.
      </Rule>
  </LifecycleConfiguration>
  ```

  Amazon S3 combines these filters using a logical AND\. That is, the rule applies to subset of objects with a specific key prefix and specific tags\. A filter can have only one prefix, and zero or more tags\.
+ You can specify an empty filter, in which case the rule applies to all objects in the bucket\.

  ```
  <LifecycleConfiguration>
      <Rule>
          <Filter>
          </Filter>
          <Status>Enabled</Status>
          transition/expiration actions.
      </Rule>
  </LifecycleConfiguration>
  ```

## Elements to Describe Lifecycle Actions<a name="intro-lifecycle-rules-actions"></a>

You can direct Amazon S3 to perform specific actions in an object's lifetime by specifying one or more of the following predefined actions in a lifecycle rule\. The effect of these actions depends on the versioning state of your bucket\. 
+ **Transition** action element – You specify the `Transition` action to transition objects from one storage class to another\. For more information about transitioning objects, see [Supported Transitions and Related Constraints](lifecycle-transition-general-considerations.md#lifecycle-general-considerations-transition-sc)\. When a specified date or time period in the object's lifetime is reached, Amazon S3 performs the transition\. 

  For a versioned bucket \(versioning\-enabled or versioning\-suspended bucket\), the `Transition` action applies to the current object version\. To manage noncurrent versions, Amazon S3 defines the `NoncurrentVersionTransition` action \(described below\)\.
+ **Expiration action element** – The `Expiration` action expires objects identified in the rule and applies to eligible objects in any of the Amazon S3 storage classes\. For more information about storage classes, see [Storage Classes](storage-class-intro.md)\. Amazon S3 makes all expired objects unavailable\. Whether the objects are permanently removed depends on the versioning state of the bucket\. 
**Important**  
Object expiration lifecycle polices do not remove incomplete multipart uploads\. To remove incomplete multipart uploads you must use the **AbortIncompleteMultipartUpload** lifecycle configuration action that is described later in this section\. 
  + **Non\-versioned bucket** – The `Expiration` action results in Amazon S3 permanently removing the object\. 
  + **Versioned bucket** – For a versioned bucket \(that is, versioning\-enabled or versioning\-suspended\), there are several considerations that guide how Amazon S3 handles the `expiration` action\. For more information, see [Using Versioning](Versioning.md)\. Regardless of the versioning state, the following applies:
    + The `Expiration` action applies only to the current version \(it has no impact on noncurrent object versions\)\.
    + Amazon S3 doesn't take any action if there are one or more object versions and the delete marker is the current version\.
    + If the current object version is the only object version and it is also a delete marker \(also referred as an *expired object delete marker*, where all object versions are deleted and you only have a delete marker remaining\), Amazon S3 removes the expired object delete marker\. You can also use the expiration action to direct Amazon S3 to remove any expired object delete markers\. For an example, see [Example 7: Removing Expired Object Delete Markers](lifecycle-configuration-examples.md#lifecycle-config-conceptual-ex7)\. 

    Also consider the following when setting up Amazon S3 to manage expiration:
    + **Versioning\-enabled bucket** 

      If the current object version is not a delete marker, Amazon S3 adds a delete marker with a unique version ID\. This makes the current version noncurrent, and the delete marker the current version\. 
    + **Versioning\-suspended bucket** 

      In a versioning\-suspended bucket, the expiration action causes Amazon S3 to create a delete marker with null as the version ID\. This delete marker replaces any object version with a null version ID in the version hierarchy, which effectively deletes the object\. 

In addition, Amazon S3 provides the following actions that you can use to manage noncurrent object versions in a versioned bucket \(that is, versioning\-enabled and versioning\-suspended buckets\)\.
+ **NoncurrentVersionTransition** action element – Use this action to specify how long \(from the time the objects became noncurrent\) you want the objects to remain in the current storage class before Amazon S3 transitions them to the specified storage class\. For more information about transitioning objects, see [Supported Transitions and Related Constraints](lifecycle-transition-general-considerations.md#lifecycle-general-considerations-transition-sc)\. 
+ **NoncurrentVersionExpiration** action element – Use this action to specify how long \(from the time the objects became noncurrent\) you want to retain noncurrent object versions before Amazon S3 permanently removes them\. The deleted object can't be recovered\. 

  This delayed removal of noncurrent objects can be helpful when you need to correct any accidental deletes or overwrites\. For example, you can configure an expiration rule to delete noncurrent versions five days after they become noncurrent\. For example, suppose that on 1/1/2014 10:30 AM UTC, you create an object called `photo.gif` \(version ID 111111\)\. On 1/2/2014 11:30 AM UTC, you accidentally delete `photo.gif` \(version ID 111111\), which creates a delete marker with a new version ID \(such as version ID 4857693\)\. You now have five days to recover the original version of `photo.gif` \(version ID 111111\) before the deletion is permanent\. On 1/8/2014 00:00 UTC, the lifecycle rule for expiration executes and permanently deletes `photo.gif` \(version ID 111111\), five days after it became a noncurrent version\. 
**Important**  
Object expiration lifecycle policies do not remove incomplete multipart uploads\. To remove incomplete multipart uploads, you must use the **AbortIncompleteMultipartUpload** lifecycle configuration action that is described later in this section\. 

In addition to the transition and expiration actions, you can use the following lifecycle configuration action to direct Amazon S3 to abort incomplete multipart uploads\. 
+ **AbortIncompleteMultipartUpload** action element – Use this element to set a maximum time \(in days\) that you want to allow multipart uploads to remain in progress\. If the applicable multipart uploads \(determined by the key name `prefix` specified in the lifecycle rule\) are not successfully completed within the predefined time period, Amazon S3 aborts the incomplete multipart uploads\. For more information, see [Aborting Incomplete Multipart Uploads Using a Bucket Lifecycle Policy](mpuoverview.md#mpu-abort-incomplete-mpu-lifecycle-config)\. 
**Note**  
You cannot specify this lifecycle action in a rule that specifies a filter based on object tags\. 
+ **ExpiredObjectDeleteMarker** action element – In a versioning\-enabled bucket, a delete marker with zero noncurrent versions is referred to as the expired object delete marker\. You can use this lifecycle action to direct S3 to remove the expired object delete markers\. For an example, see [Example 7: Removing Expired Object Delete Markers](lifecycle-configuration-examples.md#lifecycle-config-conceptual-ex7)\.
**Note**  
You cannot specify this lifecycle action in a rule that specifies a filter based on object tags\. 

### How Amazon S3 Calculates How Long an Object Has Been Noncurrent<a name="non-current-days-calculations"></a>

 In a versioning\-enabled bucket, you can have multiple versions of an object, there is always one current version, and zero or more noncurrent versions\. Each time you upload an object, the current version is retained as the noncurrent version and the newly added version, the successor, becomes the current version\. To determine the number of days an object is noncurrent, Amazon S3 looks at when its successor was created\. Amazon S3 uses the number of days since its successor was created as the number of days an object is noncurrent\. 

**Restoring Previous Versions of an Object When Using Lifecycle Configurations**  
 As explained in detail in the topic [Restoring Previous Versions](RestoringPreviousVersions.md), you can use either of the following two methods to retrieve previous versions of an object:  
By copying a noncurrent version of the object into the same bucket\. The copied object becomes the current version of that object, and all object versions are preserved\.
By permanently deleting the current version of the object\. When you delete the current object version, you, in effect, turn the noncurrent version into the current version of that object\.
When using lifecycle configuration rules with versioning\-enabled buckets, we recommend as a best practice that you use the first method\.   
 Because of Amazon S3's eventual consistency semantics, a current version that you permanently deleted may not disappear until the changes propagate \(Amazon S3 may be unaware of this deletion\)\. In the meantime, the lifecycle rule that you configured to expire noncurrent objects may permanently remove noncurrent objects, including the one you want to restore\. So, copying the old version, as recommended in the first method, is the safer alternative\.

The following table summarizes the behavior of the lifecycle configuration rule actions on objects in relation to the versioning state of the bucket containing the object\.


**Lifecycle Actions and Bucket Versioning State**  

| Action | Nonversioned Bucket \(Versioning Not Enabled\) | Versioning\-Enabled Bucket | Versioning\-Suspended Bucket | 
| --- | --- | --- | --- | 
|  `Transition` When a specified date or time period in the object's lifetime is reached\.  | Amazon S3 transitions the object to the specified storage class\. | Amazon S3 transitions the current version of the object to the specified storage class\. | Same behavior as a versioning\-enabled bucket\. | 
|  `Expiration` When a specified date or time period in the object's lifetime is reached\.  | Expiration deletes the object, and the deleted object cannot be recovered\. | If the current version is not a delete marker, Amazon S3 creates a delete marker, which becomes the current version, and the existing current version is retained as a noncurrent version\. | The lifecycle creates a delete marker with null version ID, which becomes the current version\. If the version ID of the current version of the object is null, the Expiration action permanently deletes this version\. Otherwise, the current version is retained as a noncurrent version\. | 
|  `NoncurrentVersionTransition` When the specified number of days from when the object becomes noncurrent is reached\.  | NoncurrentVersionTransition has no effect\. |  Amazon S3 transitions the noncurrent object versions to the specified storage class\.  | Same behavior as a versioning\-enabled bucket\. | 
|  `NoncurrentVersionExpiration` When the specified number of days from when the object becomes noncurrent is reached\.  | NoncurrentVersionExpiration has no effect\. | NoncurrentVersionExpiration action deletes the noncurrent version of the object, and the deleted object cannot be recovered\. | Same behavior as a versioning\-enabled bucket\. | 

### Lifecycle Rules: Based on an Object's Age<a name="intro-lifecycle-rules-number-of-days"></a>

You can specify a time period, in number of days from the creation \(or modification\) of the objects, when Amazon S3 can take the action\. 

When you specify the number of days in the `Transition` and `Expiration` actions in a lifecycle configuration, note the following:
+ It is the number of days since object creation when the action will occur\.
+ Amazon S3 calculates the time by adding the number of days specified in the rule to the object creation time and rounding the resulting time to the next day midnight UTC\. For example, if an object was created at 1/15/2014 10:30 AM UTC and you specify 3 days in a transition rule, then the transition date of the object would be calculated as 1/19/2014 00:00 UTC\. 

**Note**  
Amazon S3 maintains only the last modified date for each object\. For example, the Amazon S3 console shows the **Last Modified** date in the object **Properties** pane\. When you initially create a new object, this date reflects the date the object is created\. If you replace the object, the date changes accordingly\. So when we use the term *creation date*, it is synonymous with the term *last modified date*\. 

When specifying the number of days in the `NoncurrentVersionTransition` and `NoncurrentVersionExpiration` actions in a lifecycle configuration, note the following:
+ It is the number of days from when the version of the object becomes noncurrent \(that is, since the object was overwritten or deleted\), as the time when Amazon S3 will perform the action on the specified object or objects\.
+ Amazon S3 calculates the time by adding the number of days specified in the rule to the time when the new successor version of the object is created and rounding the resulting time to the next day midnight UTC\. For example, in your bucket, you have a current version of an object that was created at 1/1/2014 10:30 AM UTC, if the new successor version of the object that replaces the current version is created at 1/15/2014 10:30 AM UTC and you specify 3 days in a transition rule, then the transition date of the object would be calculated as 1/19/2014 00:00 UTC\. 

### Lifecycle Rules: Based on a Specific Date<a name="intro-lifecycle-rules-date"></a>

When specifying an action in a lifecycle rule, you can specify a date when you want Amazon S3to take the action\. When the specific date arrives, S3 applies the action to all qualified objects \(based on the filter criteria\)\. 

If you specify a lifecycle action with a date that is in the past, all qualified objects become immediately eligible for that lifecycle action\.

**Important**  
The date\-based action is not a one\-time action\. S3 continues to apply the date\-based action even after the date has passed, as long as the rule status is Enabled\.  
For example, suppose that you specify a date\-based Expiration action to delete all objects \(assume no filter specified in the rule\)\. On the specified date, S3 expires all the objects in the bucket\. S3 also continues to expire any new objects you create in the bucket\. To stop the lifecycle action, you must remove the action from the lifecycle configuration, disable the rule, or delete the rule from the lifecycle configuration\.

The date value must conform to the ISO 8601 format\. The time is always midnight UTC\. 

**Note**  
You can't create the date\-based lifecycle rules using the Amazon S3 console, but you can view, disable, or delete such rules\. 