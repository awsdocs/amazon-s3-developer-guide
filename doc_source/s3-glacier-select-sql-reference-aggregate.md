# Aggregate Functions \(Amazon S3 Select only\)<a name="s3-glacier-select-sql-reference-aggregate"></a>

Amazon S3 Select supports the following aggregate functions\.

**Note**  
Amazon Glacier Select does not support aggregate functions\.


| Function | Argument Type | Return Type | 
| --- | --- | --- | 
| AVG\(expression\) | INT, FLOAT, DECIMAL | DECIMAL for an INT argument, FLOAT for a floating\-point argument; otherwise the same as the argument data type\. | 
| COUNT |  \-  | INT | 
| MAX\(expression\) | INT, DECIMAL | Same as the argument type\. | 
| MIN\(expression\) | INT, DECIMAL | Same as the argument type\. | 
| SUM\(expression\) | INT, FLOAT, DOUBLE, DECIMAL | INT for INT argument, FLOAT for a floating\-point argument; otherwise, the same as the argument data type\. | 