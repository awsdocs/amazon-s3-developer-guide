# Conditional Functions<a name="s3-glacier-select-sql-reference-conditional"></a>

Amazon S3 Select and Amazon Glacier Select support the following conditional functions\.

**Topics**
+ [COALESCE](#s3-glacier-select-sql-reference-coalesce)
+ [NULLIF](#s3-glacier-select-sql-reference-nullif)

## COALESCE<a name="s3-glacier-select-sql-reference-coalesce"></a>

Evaluates the arguments in order and returns the first non\-unknown, that is, the first non\-null or non\-missing\. This function does not propagate null and missing\.

### Syntax<a name="s3-glacier-select-sql-reference-coalesce-syntax"></a>

```
COALESCE ( expression, expression, ... )
```

### Parameters<a name="s3-glacier-select-sql-reference-coalesce-parameters"></a>

 *expression*   
The target expression that the function operates on\.

### Examples<a name="s3-glacier-select-sql-reference-coalesce-examples"></a>

```
COALESCE(1)                -- 1
COALESCE(null)             -- null
COALESCE(null, null)       -- null
COALESCE(missing)          -- null
COALESCE(missing, missing) -- null
COALESCE(1, null)          -- 1
COALESCE(null, null, 1)    -- 1
COALESCE(null, 'string')   -- 'string'
COALESCE(missing, 1)       -- 1
```

## NULLIF<a name="s3-glacier-select-sql-reference-nullif"></a>

Given two expressions, returns NULL if the two expressions evaluate to the same value; otherwise, returns the result of evaluating the first expression\.

### Syntax<a name="s3-glacier-select-sql-reference-nullif-syntax"></a>

```
NULLIF ( expression1, expression2 )
```

### Parameters<a name="s3-glacier-select-sql-reference-nullif-parameters"></a>

 *expression1, expression2*   
The target expressions that the function operates on\.

### Examples<a name="s3-glacier-select-sql-reference-nullif-examples"></a>

```
NULLIF(1, 1)             -- null
NULLIF(1, 2)             -- 1
NULLIF(1.0, 1)           -- null
NULLIF(1, '1')           -- 1
NULLIF([1], [1])         -- null
NULLIF(1, NULL)          -- 1
NULLIF(NULL, 1)          -- null
NULLIF(null, null)       -- null
NULLIF(missing, null)    -- null
NULLIF(missing, missing) -- null
```