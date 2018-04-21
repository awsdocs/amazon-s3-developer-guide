# Using BitTorrent with Amazon S3<a name="S3Torrent"></a>

**Topics**
+ [How You are Charged for BitTorrent Delivery](S3TorrentCharge.md)
+ [Using BitTorrent to Retrieve Objects Stored in Amazon S3](S3TorrentRetrieve.md)
+ [Publishing Content Using Amazon S3 and BitTorrent](S3TorrentPublish.md)

BitTorrent is an open, peer\-to\-peer protocol for distributing files\. You can use the BitTorrent protocol to retrieve any publicly\-accessible object in Amazon S3\. This section describes why you might want to use BitTorrent to distribute your data out of Amazon S3 and how to do so\.

Amazon S3 supports the BitTorrent protocol so that developers can save costs when distributing content at high scale\. Amazon S3 is useful for simple, reliable storage of any data\. The default distribution mechanism for Amazon S3 data is via client/server download\. In client/server distribution, the entire object is transferred point\-to\-point from Amazon S3 to every authorized user who requests that object\. While client/server delivery is appropriate for a wide variety of use cases, it is not optimal for everybody\. Specifically, the costs of client/server distribution increase linearly as the number of users downloading objects increases\. This can make it expensive to distribute popular objects\. 

BitTorrent addresses this problem by recruiting the very clients that are downloading the object as distributors themselves: Each client downloads some pieces of the object from Amazon S3 and some from other clients, while simultaneously uploading pieces of the same object to other interested "peers\." The benefit for publishers is that for large, popular files the amount of data actually supplied by Amazon S3 can be substantially lower than what it would have been serving the same clients via client/server download\. Less data transferred means lower costs for the publisher of the object\.

**Note**  
You can get torrent only for objects that are less than 5 GB in size\.