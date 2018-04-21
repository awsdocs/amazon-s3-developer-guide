# Object Subresources<a name="ObjectAndSoubResource"></a>

Amazon S3 defines a set of subresources associated with buckets and objects\. Subresources are subordinates to objects; that is, subresources do not exist on their own, they are always associated with some other entity, such as an object or a bucket\. 

 The following table lists the subresources associated with Amazon S3 objects\.


| Subresource | Description | 
| --- | --- | 
| acl | Contains a list of grants identifying the grantees and the permissions granted\. When you create an object, the acl identifies the object owner as having full control over the object\. You can retrieve an object ACL or replace it with an updated list of grants\. Any update to an ACL requires you to replace the existing ACL\. For more information about ACLs, see [Managing Access with ACLs](S3_ACLs_UsingACLs.md)\. | 
| torrent | Amazon S3 supports the BitTorrent protocol\. Amazon S3 uses the torrent subresource to return the torrent file associated with the specific object\. To retrieve a torrent file, you specify the torrent subresource in your GET request\. Amazon S3 creates a torrent file and returns it\. You can only retrieve the torrent subresource, you cannot create, update, or delete the torrent subresource\. For more information, see [Using BitTorrent with Amazon S3](S3Torrent.md)\. | 