# Deleting Amazon S3 log files<a name="deleting-log-files-lifecycle"></a>

An S3 bucket with server access logging enabled can accumulate many server log objects over time\. Your application might need these access logs for a specific period after creation, and after that, you might want to delete them\. You can use Amazon S3 lifecycle configuration to set rules so that Amazon S3 automatically queues these objects for deletion at the end of their life\. 

You can define a lifecycle configuration for a subset of objects in your S3 bucket by using a shared prefix \(that is, objects that have names that begin with a common string\)\. If you specified a prefix in your server access logging configuration, you can set a lifecycle configuration rule to delete log objects that have that prefix\. For example, if your log objects have the prefix `logs/`, you can set a lifecycle configuration rule to delete all objects in the bucket that have the prefix `/logs` after a specified period of time\. For more information about lifecycle configuration, see [Object lifecycle management](object-lifecycle-mgmt.md)\.

## Related resources<a name="deleting-log-files-lifecycle-more-info"></a>

[Amazon S3 server access logging](ServerLogs.md)