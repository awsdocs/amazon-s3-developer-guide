# TCP Window Scaling<a name="TCPWindowScaling"></a>

 TCP window scaling allows you to improve network throughput performance between your operating system and application layer and Amazon S3 by supporting window sizes larger than 64 KB\. At the start of the TCP session, a client advertises its supported receive window WSCALE factor, and Amazon S3 responds with its supported receive window WSCALE factor for the upstream direction\. 

 Although TCP window scaling can improve performance, it can be challenging to set correctly\. Make sure to adjust settings at both the application and kernel level\. For more information about TCP window scaling, refer to your operating system's documentation and go to [RFC 1323](http://www.ietf.org/rfc/rfc1323.txt)\. 