# Amazon S3 Storage Lens metrics glossary<a name="storage_lens_metrics_glossary"></a>

By default, all dashboards are configured with *free metrics*, which include *usage metrics* aggregated down to the bucket level with a 14\-day data retention\. This means that you can see all the usage metrics that S3 Storage Lens aggregates, and your metrics are available 14 days from the day the data was aggregated\. 

*Advanced metrics and recommendations* include *usage* and *activity* metrics that can be aggregated by prefix\. Activity metrics can be aggregated by bucket with a 15\-month data retention policy\. There are additional charges when you use S3 Storage Lens with advanced metrics and recommendations\. For more information, see [ Amazon S3 pricing](http://aws.amazon.com/s3/pricing/)\.


****  

| Metric name | Description | Free | Type | Category | Derived? | Derived metric formula | 
| --- | --- | --- | --- | --- | --- | --- | 
|  Total Storage  | The total storage |  Y  |  Usage  |  Summary |  N  |   | 
|  Object Count  | The total object count |  Y  |  Usage  |  Summary |  N  |   | 
|  \# Avg Object Size  | The average object size |  Y  |  Usage  |  Summary |  Y  | Sum\(StorageBytes\)/ sum\(ObjectCount\)  | 
| \# Buckets  | The total number of active buckets |  Y  |  Usage  |  Summary |  Y  | DistinctCount\[Bucketname\]  | 
| \# Accounts  | The number of accounts whose storage is in scope |  Y  |  Usage  |  Summary |  Y  | DistinctCount\[AccountID\]  | 
| Current Version Storage Bytes  | The number of bytes that are a current version  |  Y  |  Usage  | Data Protection, Cost Efficiency  |  N  |   | 
| % Current Version Bytes  | The percentage of bytes in scope that are current version |  Y  |  Usage  | Data Protection, Cost Efficiency  |  Y  | Sum\(CurrentVersion Bytes\) /sum\(Storage Bytes\)  | 
| Current Version Object Count  | The number of bytes that are noncurrent version  |  Y  |  Usage  | Data Protection, Cost Efficiency  |  N  |   | 
| % Current Version Objects  | The percentage of objects in scope that are a noncurrent version  |  Y  |  Usage  | Data Protection, Cost Efficiency  |  Y  | Sum\(CurrentVersion Objects\)/sum\(ObjectCount\)  | 
| Non\-Current Version Storage Bytes  | The number of noncurrent versioned bytes  |  Y  |  Usage  | Data Protection, Cost Efficiency  |  N  |   | 
| % Non\-Current Version Bytes  | The percentage of bytes in scope that are noncurrent version |  Y  |  Usage  | Data Protection, Cost Efficiency  |  Y  | Sum\(NonCurrentVersionSto rageBytes\)/ Sum\(StorageBytes\)  | 
| Non\-Current Version Object Count  | The count of the noncurrent version objects |  Y  |  Usage  | Data Protection, Cost Efficiency  |  N  |   | 
| % Non\-Current Version Objects  | The percentage of objects in scope that are a noncurrent version |  Y  |  Usage  | Data Protection, Cost Efficiency  |  Y  | Sum\(NonCurrentVersionObjectCount\)/Sum\(ObjectCount\)  | 
| Delete Marker Object Count  | The total number of objects with a delete marker |  Y  |  Usage  |  Cost Efficiency  |  N  |   | 
| % Delete Marker Objects  | The percentage of objects in scope with a delete marker  |  Y  |  Usage  |  Cost Efficiency  |  Y  |   | 
| Encrypted Storage Bytes  | The total number of encrypted bytes using [Amazon S3 server\-side encryption](https://docs.aws.amazon.com/AmazonS3/latest/dev/serv-side-encryption.html ) |  Y  |  Usage  | Data Protection  |  N  |   | 
| % Encrypted Bytes | The percentage of total bytes in scope that are encrypted using [Amazon S3 server\-side encryption](https://docs.aws.amazon.com/AmazonS3/latest/dev/serv-side-encryption.html ) |  Y  |  Usage  | Data Protection  |  Y  | Sum\(EncryptedStorageBytes\)/ Sum\(StorageBytes\)  | 
| Encrypted Object Count  | The total object counts that are encrypted using [Amazon S3 server\-side encryption](https://docs.aws.amazon.com/AmazonS3/latest/dev/serv-side-encryption.html ) |  Y  |  Usage  | Data Protection  |  N  |   | 
| % Encrypted Objects  | The percentage of objects in scope that are encrypted using [Amazon S3 server\-side encryption](https://docs.aws.amazon.com/AmazonS3/latest/dev/serv-side-encryption.html ) |  Y  |  Usage  | Data Protection  |  Y  | Sum\(EncryptedStorageBytes\)/Sum\(ObjectCount\)  | 
| Unencrypted Storage Bytes  | The number of bytes in scope that are unencrypted  |  Y  |  Usage  | Data Protection  |  Y  | Sum\(StorageBytes\) \- sum\(EncryptedStorageBytes\)  | 
| % Unencrypted Bytes  | The percentage of bytes in scope that are unencrypted  |  Y  |  Usage  | Data Protection  |  Y  | Sum\(UnencryptedStorageBytes\)/ Sum\(StorageBytes\)  | 
| Unencrypted Object Count  | The count of the objects that are unencrypted  |  Y  |  Usage  | Data Protection  |  Y  | Sum\(ObjectCounts\) \- sum\(EncryptedObjectCounts\)  | 
| % Unencrypted Objects  | The percentage of unencrypted objects  |  Y  |  Usage  | Data Protection  |  Y  | Sum\(UnencryptedStorageBytes\)/ Sum\(ObjectCount\)  | 
| Replicated Storage Bytes  | The total number of bytes in scope that are replicated  |  Y  |  Usage  | Data Protection  |  N  |   | 
| % Replicated Bytes  | The percentage of total bytes in scope that are replicated  |  Y  |  Usage  | Data Protection  |  Y  | Sum\(ReplicatedStorageBytes\)/ Sum\(StorageBytes\)  | 
| Replicated Object Count  | The count of replicated objects  |  Y  |  Usage  | Data Protection  |  N  |   | 
| % Replicated Objects  | The percentage of total objects that are replicated  |  Y  |  Usage  | Data Protection  |  Y  | Sum\(ReplicatedObjects\)/ Sum\(ObjectCount\)  | 
| Object Lock Enabled Storage Bytes  | The total number of bytes in scope that have Object Lock enabled |  Y  |  Usage  | Data Protection  |  N  |   | 
| % Object Lock Bytes  | The percentage of total bytes in scope that have Object Lock enabled  |  Y  |  Usage  | Data Protection  |  Y  | Sum\(ObjectLockBytes\)/ Sum\(StorageBytes\)  | 
| Object Lock Enabled Object Count  | The total number of objects in scope that have Object Lock enabled |  Y  |  Usage  | Data Protection  |  N  |   | 
| % Object Lock Objects  | The percentage of objects in scope that have Object Lock enabled |  Y  |  Usage  | Data Protection  |  Y  | Sum\(ObjectLockObjects\)/ Sum\(ObjectCount\)  | 
| Incomplete Multipart Upload Storage Bytes  | The total bytes in scope with incomplete multipart uploads  |  Y  |  Usage  |  Cost Efficiency  |  N  |   | 
| % Incomplete MPU Bytes  | The percentage of bytes in scope that are results of incomplete multipart uploads  |  Y  |  Usage  |  Cost Efficiency  |  Y  | Sum\(IncompleteMPUbytes\)/ Sum\(StorageBytes\)  | 
| Incomplete Multipart Upload Object Count  | The number of objects in scope that are incomplete multipart uploads  |  Y  |  Usage  |  Cost Efficiency  |  N  |   | 
| % Incomplete MPU Objects  | The percentage of objects in scope that are incomplete multipart uploads  |  Y  |  Usage  |  Cost Efficiency  |  Y  | Sum\(IncompleteMPUobjects \)/Sum\( ObjectCount\)  | 
| All Requests  | The total number of requests made  |  N  |  Activity |  Summary, Activity |  N  |   | 
| Get Requests  | The total number of GET requests made  |  N  |  Activity |  Activity |  N  |   | 
| Put Requests  | The total number of PUT requests made  |  N  |  Activity |  Activity |  N  |   | 
| Head Requests | The total number of head requests made  |  N  |  Activity |  Activity |  N  |   | 
| Delete Requests  | The total number of delete requests made  |  N  |  Activity |  Activity |  N  |   | 
| List Requests | The total number of list requests made  |  N  |  Activity |  Activity |  N  |   | 
| Post Requests  | The total number of post requests made  |  N  |  Activity |  Activity |  N  |   | 
| Select Requests | The total number of select requests |  N  |  Activity |  Activity |  N  |   | 
| Select Scanned Bytes | The number of select bytes scanned |  N  |  Activity |  Activity |  N  |   | 
| Select Returned Bytes  | The number of select bytes returned  |  N  |  Activity |  Activity |  N  |   | 
| Bytes Downloaded  | The number of bytes in scope that were downloaded  |  N  |  Activity |  Activity |  N  |   | 
| % Retrieval Rate  | The percentage of retrieval rate  |  N  |  Activity |  Activity, Cost Efficiency  |  Y  | Sum\(BytesDownloaded\)/Sum\(StorageBytes\)  | 
| Bytes Uploaded  | The number of bytes uploaded  |  N  |  Activity |  Activity |  N  |   | 
| % Ingest Ratio  | The number of bytes loaded as a percentage of total storage bytes in scope  |  N  |  Activity |  Activity, Cost Efficiency  |  Y  | Sum\(BytesUploaded\) /Sum\(Storage Bytes\)  | 
| 4xx Errors  | The total 4xx errors in scope  |  N  |  Activity |  Activity |  N  |   | 
| 5xx Errors  | The total 5xx errors in scope  |  N  |  Activity |  Activity |  N  |   | 
| Total Errors | The sum of all the \(4xx\) and \(5xx\) errors  |  N  |  Activity |  Activity |  Y  |  Sum\(4xxErrors\) \+ Sum\(5xxErrors\) | 
| % Error Rate | The total errors as a percent of total requests |  N  |  Activity |  Activity |  Y  |  Sum\(TotalErrors\)/ Sum\(TotalRequests\)  | 