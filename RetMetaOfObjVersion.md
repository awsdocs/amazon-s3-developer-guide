# Retrieving the Metadata of an Object Version<a name="RetMetaOfObjVersion"></a>

If you only want to retrieve the metadata of an object \(and not its content\), you use the `HEAD` operation\. By default, you get the metadata of the most recent version\. To retrieve the metadata of a specific object version, you specify its version ID\.

**To retrieve the metadata of an object version**

1. Set `versionId` to the ID of the version of the object whose metadata you want to retrieve\.

1. Send a `HEAD Object versionId` request\.

**Example Retrieving the Metadata of a Versioned Object**  
The following request retrieves the metadata of version 3HL4kqCxf3vjVBH40Nrjfkd of `my-image.jpg`\.  

```
1. HEAD /my-image.jpg?versionId=3HL4kqCxf3vjVBH40Nrjfkd HTTP/1.1
2. Host: bucket.s3.amazonaws.com
3. Date: Wed, 28 Oct 2009 22:32:00 GMT
4. Authorization: AWS AKIAIOSFODNN7EXAMPLE:0RQf4/cRonhpaBX5sCYVf1bNRuU=
```

The following shows a sample response\.

```
 1. HTTP/1.1 200 OK
 2. x-amz-id-2: ef8yU9AS1ed4OpIszj7UDNEHGran
 3. x-amz-request-id: 318BC8BC143432E5
 4. x-amz-version-id: 3HL4kqtJlcpXroDTDmjVBH40Nrjfkd
 5. Date: Wed, 28 Oct 2009 22:32:00 GMT
 6. Last-Modified: Sun, 1 Jan 2006 12:00:00 GMT
 7. ETag: "fba9dede5f27731c9771645a39863328"
 8. Content-Length: 434234
 9. Content-Type: text/plain
10. Connection: close
11. Server: AmazonS3
```