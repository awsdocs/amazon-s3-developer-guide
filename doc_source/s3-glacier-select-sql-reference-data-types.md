# Data Types<a name="s3-glacier-select-sql-reference-data-types"></a>

Amazon S3 Select and Amazon Glacier Select support several primitive data types\.

## Data Type Conversions<a name="s3-glacier-select-sql-reference-data-conversion"></a>

The general rule is to follow the `CAST` function if defined\. If `CAST` is not defined, then all input data is treated as a string\. It must be cast into the relevant data types when necessary\.

For more information about the `CAST` function, see [CAST](s3-glacier-select-sql-reference-conversion.md#s3-glacier-select-sql-reference-cast)\.

## Supported Data Types<a name="s3-glacier-select-sql-reference-supported-data-types"></a>

Amazon S3 Select and Amazon Glacier Select support the following set of primitive data types\.


|  Name  |  Description  |  Examples  | 
| --- | --- | --- | 
| bool | TRUE or FALSE | FALSE | 
| int, integer | 8\-byte signed integer in the range \-9,223,372,036,854,775,808 to 9,223,372,036,854,775,807\.  | 100000 | 
| string | UTF8\-encoded variable\-length string\. The default limit is one character\. The maximum character limit is 2,147,483,647\.  | 'xyz' | 
| float | 8\-byte floating point number\.  | CAST\(0\.456 AS FLOAT\) | 
| decimal, numeric |  Base\-10 number, with maximum precision of 38 \(that is, the maximum number of significant digits\), and with scale within the range of \-231 to 231\-1 \(that is, the base\-10 exponent\)\.  | 123\.456  | 
| timestamp |  Time stamps represent a specific moment in time, always include a local offset, and are capable of arbitrary precision\. In the text format, time stamps follow the [W3C note on date and time formats](https://www.w3.org/TR/NOTE-datetime), but they must end with the literal "T" if not at least whole\-day precision\. Fractional seconds are allowed, with at least one digit of precision, and an unlimited maximum\. Local\-time offsets can be represented as either hour:minute offsets from UTC, or as the literal "Z" to denote a local time of UTC\. They are required on time stamps with time and are not allowed on date values\.  | CAST\('2007\-04\-05T14:30Z' AS TIMESTAMP\) | 