# Set Lifecycle Configurations Using the AWS CLI<a name="set-lifecycle-cli"></a>

You can use the following AWS CLI commands to manage lifecycle configurations:
+ put\-bucket\-lifecycle\-configuration
+ get\-bucket\-lifecycle\-configuration
+ delete\-bucket\-lifecycle

For instructions to set up the AWS CLI, see [Setting Up the AWS CLI](setup-aws-cli.md)\.

Note that the Amazon S3 lifecycle configuration is an XML file\. But when using CLI, you cannot specify the XML, you must specify JSON instead\. The following are examples XML lifecycle configurations and equivalent JSON that you can specify in AWS CLI command:
+ Consider the following example lifecycle configuration:

  ```
  <LifecycleConfiguration>
      <Rule>
          <ID>ExampleRule</ID>
          <Filter>
             <Prefix>documents/</Prefix>
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

  The equivalent JSON is shown:

  ```
  {
      "Rules": [
          {
              "Filter": {
                  "Prefix": "documents/"
              },
              "Status": "Enabled",
              "Transitions": [
                  {
                      "Days": 365,
                      "StorageClass": "GLACIER"
                  }
              ],
              "Expiration": {
                  "Days": 3650
              },
              "ID": "ExampleRule"
          }
      ]
  }
  ```
+ Consider the following example lifecycle configuration:

  ```
  <LifecycleConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
      <Rule>
          <ID>id-1</ID>
          <Expiration>
              <Days>1</Days>
          </Expiration>
          <Filter>
              <And>
                  <Prefix>myprefix</Prefix>
                  <Tag>
                      <Key>mytagkey1</Key>
                      <Value>mytagvalue1</Value>
                  </Tag>
                  <Tag>
                      <Key>mytagkey2</Key>
                      <Value>mytagvalue2</Value>
                  </Tag>
              </And>
          </Filter>
          <Status>Enabled</Status>    
      </Rule>
  </LifecycleConfiguration>
  ```

  The equivalent JSON is shown:

  ```
  {
      "Rules": [
          {
              "ID": "id-1",
              "Filter": {
                  "And": {
                      "Prefix": "myprefix", 
                      "Tags": [
                          {
                              "Value": "mytagvalue1", 
                              "Key": "mytagkey1"
                          }, 
                          {
                              "Value": "mytagvalue2", 
                              "Key": "mytagkey2"
                          }
                      ]
                  }
              }, 
              "Status": "Enabled", 
              "Expiration": {
                  "Days": 1
              }
          }
      ]
  }
  ```

You can test the `put-bucket-lifecycle-configuration` as follows:

1. Save the JSON lifecycle configuration in a file \(lifecycle\.json\)\. 

1. Run the following AWS CLI command to set the lifecycle configuration on your bucket:

   ```
   $ aws s3api put-bucket-lifecycle-configuration  \
   --bucket bucketname  \
   --lifecycle-configuration file://lifecycle.json
   ```

1. To verify, retrieve the lifecycle configuration using the `get-bucket-lifecycle-configuration` AWS CLI command as follows:

   ```
   $ aws s3api get-bucket-lifecycle-configuration  \
   --bucket bucketname
   ```

1. To delete the lifecycle configuration use the `delete-bucket-lifecycle` AWS CLI command as follows:

   ```
   aws s3api delete-bucket-lifecycle \
   --bucket bucketname
   ```