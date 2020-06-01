# Set lifecycle configurations using the AWS CLI<a name="set-lifecycle-cli"></a>

You can use the following AWS CLI commands to manage S3 Lifecycle configurations:
+ `put-bucket-lifecycle-configuration`
+ `get-bucket-lifecycle-configuration`
+ `delete-bucket-lifecycle`

For instructions on setting up the AWS CLI, see [Setting Up the AWS CLI](setup-aws-cli.md)\.

The Amazon S3 Lifecycle configuration is an XML file\. But when using the AWS CLI, you cannot specify the XML\. You must specify the JSON instead\. The following are example XML Lifecycle configurations and equivalent JSON that you can specify in an AWS CLIcommand\.
+ Consider the following example S3 Lifecycle configuration\.

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

  The equivalent JSON is shown\.

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
+ Consider the following example S3 Lifecycle configuration\.

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

  The equivalent JSON is shown\.

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

You can test the `put-bucket-lifecycle-configuration` as follows\.

**To test the configuration**

1. Save the JSON Lifecycle configuration in a file \(`lifecycle.json`\)\. 

1. Run the following AWS CLI command to set the Lifecycle configuration on your bucket\.

   ```
   $ aws s3api put-bucket-lifecycle-configuration  \
   --bucket bucketname  \
   --lifecycle-configuration file://lifecycle.json
   ```

1. To verify, retrieve the S3 Lifecycle configuration using the `get-bucket-lifecycle-configuration` AWS CLI command as follows\.

   ```
   $ aws s3api get-bucket-lifecycle-configuration  \
   --bucket bucketname
   ```

1. To delete the S3 Lifecycle configuration use the `delete-bucket-lifecycle` AWS CLI command as follows\.

   ```
   aws s3api delete-bucket-lifecycle \
   --bucket bucketname
   ```