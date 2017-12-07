# Using BitTorrent to Retrieve Objects Stored in Amazon S3<a name="S3TorrentRetrieve"></a>

Any object in Amazon S3 that can be read anonymously can also be downloaded via BitTorrent\. Doing so requires use of a BitTorrent client application\. Amazon does not distribute a BitTorrent client application, but there are many free clients available\. The Amazon S3BitTorrent implementation has been tested to work with the official BitTorrent client \(go to [http://www\.bittorrent\.com/](http://www.bittorrent.com/)\)\.

The starting point for a BitTorrent download is a \.torrent file\. This small file describes for BitTorrent clients both the data to be downloaded and where to get started finding that data\. A \.torrent file is a small fraction of the size of the actual object to be downloaded\. Once you feed your BitTorrent client application an Amazon S3 generated \.torrent file, it should start downloading immediately from Amazon S3 `and` from any "peer" BitTorrent clients\.

Retrieving a \.torrent file for any publicly available object is easy\. Simply add a "?torrent" query string parameter at the end of the REST GET request for the object\. No authentication is required\. Once you have a BitTorrent client installed, downloading an object using BitTorrent download might be as easy as opening this URL in your web browser\.

There is no mechanism to fetch the \.torrent for an Amazon S3 object using the SOAP API\.

**Note**  
 SOAP support over HTTP is deprecated, but it is still available over HTTPS\. New Amazon S3 features will not be supported for SOAP\. We recommend that you use either the REST API or the AWS SDKs\. 

**Example**  
This example retrieves the Torrent file for the "Nelson" object in the "quotes" bucket\.  
`Sample Request`  

```
1. GET /quotes/Nelson?torrent HTTP/1.0
2. Date: Wed, 25 Nov 2009 12:00:00 GMT
```
`Sample Response`  

```
1. HTTP/1.1 200 OK
2. x-amz-request-id: 7CD745EBB7AB5ED9
3. Date: Wed, 25 Nov 2009 12:00:00 GMT
4. Content-Disposition: attachment; filename=Nelson.torrent;
5. Content-Type: application/x-bittorrent
6. Content-Length: 537
7. Server: AmazonS3
8. 
9. <body: a Bencoded dictionary as defined by the BitTorrent specification>
```