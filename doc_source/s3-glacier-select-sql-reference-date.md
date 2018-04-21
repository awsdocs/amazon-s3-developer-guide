# Date Functions<a name="s3-glacier-select-sql-reference-date"></a>

Amazon S3 Select and Amazon Glacier Select support the following date functions\.

**Topics**
+ [DATE\_ADD](#s3-glacier-select-sql-reference-date-add)
+ [DATE\_DIFF](#s3-glacier-select-sql-reference-date-diff)
+ [EXTRACT](#s3-glacier-select-sql-reference-extract)
+ [TO\_STRING](#s3-glacier-select-sql-reference-to-string)
+ [TO\_TIMESTAMP](#s3-glacier-select-sql-reference-to-timestamp)
+ [UTCNOW](#s3-glacier-select-sql-reference-utcnow)

## DATE\_ADD<a name="s3-glacier-select-sql-reference-date-add"></a>

Given a date part, a quantity, and a time stamp, returns an updated time stamp by altering the date part by the quantity\.

### Syntax<a name="s3-glacier-select-sql-reference-date-add-syntax"></a>

```
DATE_ADD( date_part, quantity, timestamp )
```

### Parameters<a name="s3-glacier-select-sql-reference-date-add-parameters"></a>

*date\_part*   
Specifies which part of the date to modify\. This can be one of the following:  
+ year
+ month
+ day
+ hour
+ minute
+ second

 *quantity*   
The value to apply to the updated time stamp\. Positive values for quantity add to the time stamp's date\_part, and negative values subtract\.

 *timestamp*   
The target time stamp that the function operates on\.

### Examples<a name="s3-glacier-select-sql-reference-date-add-examples"></a>

```
DATE_ADD(year, 5, `2010-01-01T`)                -- 2015-01-01 (equivalent to 2015-01-01T)
DATE_ADD(month, 1, `2010T`)                     -- 2010-02T (result will add precision as necessary)
DATE_ADD(month, 13, `2010T`)                    -- 2011-02T
DATE_ADD(day, -1, `2017-01-10T`)                -- 2017-01-09 (equivalent to 2017-01-09T)
DATE_ADD(hour, 1, `2017T`)                      -- 2017-01-01T01:00-00:00
DATE_ADD(hour, 1, `2017-01-02T03:04Z`)          -- 2017-01-02T04:04Z
DATE_ADD(minute, 1, `2017-01-02T03:04:05.006Z`) -- 2017-01-02T03:05:05.006Z
DATE_ADD(second, 1, `2017-01-02T03:04:05.006Z`) -- 2017-01-02T03:04:06.006Z
```

## DATE\_DIFF<a name="s3-glacier-select-sql-reference-date-diff"></a>

Given a date part and two valid time stamps, returns the difference in date parts\. The return value is a negative integer when the `date_part` value of `timestamp1` is greater than the `date_part` value of `timestamp2`\. The return value is a positive integer when the `date_part` value of `timestamp1` is less than the `date_part` value of `timestamp2`\.

### Syntax<a name="s3-glacier-select-sql-reference-date-diff-syntax"></a>

```
DATE_DIFF( date_part, timestamp1, timestamp2 )
```

### Parameters<a name="s3-glacier-select-sql-reference-date-diff-parameters"></a>

 *date\_part*   
Specifies which part of the time stamps to compare\. For the definition of `date_part`, see [DATE\_ADD](#s3-glacier-select-sql-reference-date-add)\.

 *timestamp1*   
The first time stamp to compare\.

 *timestamp2*   
The second time stamp to compare\.

### Examples<a name="s3-glacier-select-sql-reference-date-diff-examples"></a>

```
DATE_DIFF(year, `2010-01-01T`, `2011-01-01T`)            -- 1
DATE_DIFF(year, `2010T`, `2010-05T`)                     -- 4 (2010T is equivalent to 2010-01-01T00:00:00.000Z)
DATE_DIFF(month, `2010T`, `2011T`)                       -- 12
DATE_DIFF(month, `2011T`, `2010T`)                       -- -12
DATE_DIFF(day, `2010-01-01T23:00T`, `2010-01-02T01:00T`) -- 0 (need to be at least 24h apart to be 1 day apart)
```

## EXTRACT<a name="s3-glacier-select-sql-reference-extract"></a>

Given a date part and a time stamp, returns the time stamp's date part value\.

### Syntax<a name="s3-glacier-select-sql-reference-extract-syntax"></a>

```
EXTRACT( date_part FROM timestamp )
```

### Parameters<a name="s3-glacier-select-sql-reference-extract-parameters"></a>

 *date\_part*   
Specifies which part of the time stamps to extract\. This can be one of the following:  
+ year
+ month
+ day
+ hour
+ minute
+ second
+ timezone\_hour
+ timezone\_minute

 *timestamp*   
The target time stamp that the function operates on\.

### Examples<a name="s3-glacier-select-sql-reference-extract-examples"></a>

```
EXTRACT(YEAR FROM `2010-01-01T`)                           -- 2010
EXTRACT(MONTH FROM `2010T`)                                -- 1 (equivalent to 2010-01-01T00:00:00.000Z)
EXTRACT(MONTH FROM `2010-10T`)                             -- 10
EXTRACT(HOUR FROM `2017-01-02T03:04:05+07:08`)             -- 3
EXTRACT(MINUTE FROM `2017-01-02T03:04:05+07:08`)           -- 4
EXTRACT(TIMEZONE_HOUR FROM `2017-01-02T03:04:05+07:08`)    -- 7
EXTRACT(TIMEZONE_MINUTE FROM `2017-01-02T03:04:05+07:08`)  -- 8
```

## TO\_STRING<a name="s3-glacier-select-sql-reference-to-string"></a>

Given a time stamp and a format pattern, returns a string representation of the time stamp in the given format\.

### Syntax<a name="s3-glacier-select-sql-reference-size-syntax"></a>

```
TO_STRING ( timestamp time_format_pattern )
```

### Parameters<a name="s3-glacier-select-sql-reference-size-parameters"></a>

 *timestamp*   
The target time stamp that the function operates on\.

 *time\_format\_pattern*   
A string that has the following special character interpretations\.      
[\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/AmazonS3/latest/dev/s3-glacier-select-sql-reference-date.html)

### Examples<a name="s3-glacier-select-sql-reference-size-examples"></a>

```
TO_STRING(`1969-07-20T20:18Z`,  'MMMM d, y')                    -- "July 20, 1969"
TO_STRING(`1969-07-20T20:18Z`, 'MMM d, yyyy')                   -- "Jul 20, 1969"
TO_STRING(`1969-07-20T20:18Z`, 'M-d-yy')                        -- "7-20-69"
TO_STRING(`1969-07-20T20:18Z`, 'MM-d-y')                        -- "07-20-1969"
TO_STRING(`1969-07-20T20:18Z`, 'MMMM d, y h:m a')               -- "July 20, 1969 8:18 PM"
TO_STRING(`1969-07-20T20:18Z`, 'y-MM-dd''T''H:m:ssX')           -- "1969-07-20T20:18:00Z"
TO_STRING(`1969-07-20T20:18+08:00Z`, 'y-MM-dd''T''H:m:ssX')     -- "1969-07-20T20:18:00Z"
TO_STRING(`1969-07-20T20:18+08:00`, 'y-MM-dd''T''H:m:ssXXXX')   -- "1969-07-20T20:18:00+0800"
TO_STRING(`1969-07-20T20:18+08:00`, 'y-MM-dd''T''H:m:ssXXXXX')  -- "1969-07-20T20:18:00+08:00"
```

## TO\_TIMESTAMP<a name="s3-glacier-select-sql-reference-to-timestamp"></a>

Given a string, converts it to a time stamp\. This is the inverse operation of TO\_STRING\.

### Syntax<a name="s3-glacier-select-sql-reference-to-timestamp-syntax"></a>

```
TO_TIMESTAMP ( string )
```

### Parameters<a name="s3-glacier-select-sql-reference-to-timestamp-parameters"></a>

 *string*   
The target string that the function operates on\.

### Examples<a name="s3-glacier-select-sql-reference-to-timestamp-examples"></a>

```
TO_TIMESTAMP('2007T')                         -- `2007T`
TO_TIMESTAMP('2007-02-23T12:14:33.079-08:00') -- `2007-02-23T12:14:33.079-08:00`
```

## UTCNOW<a name="s3-glacier-select-sql-reference-utcnow"></a>

Returns the current time in UTC as a time stamp\.

### Syntax<a name="s3-glacier-select-sql-reference-utcnow-syntax"></a>

```
UTCNOW()
```

### Parameters<a name="s3-glacier-select-sql-reference-utcnow-parameters"></a>

*none*

### Examples<a name="s3-glacier-select-sql-reference-utcnow-examples"></a>

```
UTCNOW() -- 2017-10-13T16:02:11.123Z
```