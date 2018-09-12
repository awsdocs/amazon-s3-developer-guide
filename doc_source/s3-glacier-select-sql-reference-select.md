# SELECT Command<a name="s3-glacier-select-sql-reference-select"></a>

Amazon S3 Select and Amazon Glacier Select support only the `SELECT` SQL command\. The following ANSI standard clauses are supported for `SELECT`: 
+ `SELECT` list
+ `FROM` clause 
+ `WHERE` clause
+ `LIMIT` clause \(Amazon S3 Select only\)

**Note**  
Amazon S3 Select and Amazon Glacier Select queries currently do not support subqueries or joins\.

## SELECT List<a name="s3-glacier-select-sql-reference-select-list"></a>

The `SELECT` list names the columns, functions, and expressions that you want the query to return\. The list represents the output of the query\. 

```
SELECT *
SELECT projection [ AS column_alias | column_alias ] [, ...]
```

The first form with `*` \(asterisk\) returns every row that passed the `WHERE` clause, as\-is\. The second form creates a row with user\-defined output scalar expressions ***projection*** for each column\.

## FROM Clause<a name="s3-glacier-select-sql-reference-from"></a>

Amazon S3 Select and Amazon Glacier Select support the following forms of the `FROM` clause:

```
FROM table_name
FROM table_name alias
FROM table_name AS alias
```

Where `table_name` is one of `S3Object` \(for Amazon S3 Select\) or `ARCHIVE` or `OBJECT` \(for Amazon Glacier Select\) referring to the archive being queried over\. Users coming from traditional relational databases can think of this as a database schema that contains multiple views over a table\.

Following standard SQL, the `FROM` clause creates rows that are filtered in the `WHERE` clause and projected in the `SELECT` list\. 

For JSON objects that are stored in Amazon S3 Select, you can also use the following forms of the `FROM` clause:

```
FROM S3Object[*].path
FROM S3Object[*].path alias
FROM S3Object[*].path AS alias
```

Using this form of the `FROM` clause, you can select from arrays or objects within a JSON object\. You can specify `path` using one of the following forms:
+ By name \(in an object\): `.name` or `['name']`
+ By index \(in an array\): `[index]`
+ By wildcard \(in an object\): `.*`
+ By wildcard \(in an array\): `[*]`

**Note**  
This form of the `FROM` clause works only with JSON objects\.
Wildcards always emit at least one record\. If no record matches, then Amazon S3 Select emits the value `MISSING`\. During output serialization \(after the query is complete\), Amazon S3 Select replaces `MISSING` values with empty records\.
Aggregate functions \(`AVG`, `COUNT`, `MAX`, `MIN`, and `SUM`\) skip `MISSING` values\.
If you don't provide an alias when using a wildcard, you can refer to the row using the last element in the path\. For example, you could select all prices from a list of books using the query `SELECT price FROM S3Object[*].books[*].price`\. If the path ends in a wildcard rather than a name, then you can use the value `_1` to refer to the row\. For example, instead of `SELECT price FROM S3Object[*].books[*].price`, you could use the query `SELECT _1.price FROM S3Object[*].books[*]`\.
Amazon S3 Select always treats a JSON document as an array of root\-level values\. Thus, even if the JSON object that you are querying has only one root element, the `FROM` clause must begin with `S3Object[*]`\. However, for compatibility reasons, Amazon S3 Select allows you to omit the wildcard if you don't include a path\. Thus, the complete clause `FROM S3Object` is equivalent to `FROM S3Object[*] as S3Object`\. If you include a path, you must also use the wildcard\. So `FROM S3Object` and `FROM S3Object[*].path` are both valid clauses, but `FROM S3Object.path` is not\.

**Example**  
**Examples:**  
*Example \#1*  
This example shows results using the following dataset and query:  

```
{
    "Rules": [
        {"id": "id-1", "condition": "x < 20"},
        {"condition": "y > x"},
        {"id": "id-2", "condition": "z = DEBUG"}
    ]
},
{
    "created": "June 27",
    "modified": "July 6"
}
```

```
SELECT id FROM S3Object[*].Rules[*].id
```

```
{"id":"id-1"},
{},
{"id":"id-2"},
{}
```
Amazon S3 Select produces each result for the following reasons:  
+ *\{"id":"id\-1"\}* — S3Object\[0\]\.Rules\[0\]\.id produced a match\.
+ *\{\}* — S3Object\[0\]\.Rules\[1\]\.id did not match a record, so Amazon S3 Select emitted `MISSING`, which was then changed to an empty record during output serialization and returned\.
+ *\{"id":"id\-2"\}* — S3Object\[0\]\.Rules\[2\]\.id produced a match\.
+ *\{\}* — S3Object\[1\] did not match on `Rules`, so Amazon S3 Select emitted `MISSING`, which was then changed to an empty record during output serialization and returned\.
If you don't want Amazon S3 Select to return empty records when it doesn't find a match, you can test for the value `MISSING`\. The following query returns the same results as the previous query, but with the empty values omitted:  

```
SELECT id FROM S3Object[*].Rules[*].id WHERE id IS NOT MISSING
```

```
{"id":"id-1"},
{"id":"id-2"}
```
*Example \#2*  
This example shows results using the following dataset and queries:  

```
{
    "created": "936864000",
    "dir_name": "important_docs",
    "files": [
        {
            "name": "."
        },
        {
            "name": ".."
        },
        {
            "name": ".aws"
        },
        {
            "name": "downloads"
        }
    ],
    "owner": "AWS S3"
},
{
    "created": "936864000",
    "dir_name": "other_docs",
    "files": [
        {
            "name": "."
        },
        {
            "name": ".."
        },
        {
            "name": "my stuff"
        },
        {
            "name": "backup"
        }
    ],
    "owner": "User"
}
```

```
SELECT d.dir_name, d.files FROM S3Object[*] d
```

```
{
    "dir_name": "important_docs",
    "files": [
        {
            "name": "."
        },
        {
            "name": ".."
        },
        {
            "name": ".aws"
        },
        {
            "name": "downloads"
        }
    ]
},
{
    "dir_name": "other_docs",
    "files": [
        {
            "name": "."
        },
        {
            "name": ".."
        },
        {
            "name": "my stuff"
        },
        {
            "name": "backup"
        }
    ]
}
```

```
SELECT _1.dir_name, _1.owner FROM S3Object[*]
```

```
{
    "dir_name": "important_docs",
    "owner": "AWS S3"
},
{
    "dir_name": "other_docs",
    "owner": "User"
}
```

## WHERE Clause<a name="s3-glacier-select-sql-reference-where"></a>

The `WHERE` clause follows this syntax: 

```
WHERE condition
```

The `WHERE` clause filters rows based on the *condition*\. A condition is an expression that has a Boolean result\. Only rows for which the condition evaluates to `TRUE` are returned in the result\.

## LIMIT Clause \(Amazon S3 Select only\)<a name="s3-glacier-select-sql-reference-limit"></a>

The `LIMIT` clause follows this syntax: 

```
LIMIT number
```

The `LIMIT` clause limits the number of records that you want the query to return based on *number*\.

**Note**  
Amazon Glacier Select does not support the `LIMIT` clause\.

## Attribute Access<a name="s3-glacier-select-sql-reference-attribute-access"></a>

The `SELECT` and `WHERE` clauses can refer to record data using one of the methods in the following sections, depending on whether the file that is being queried is in CSV or JSON format\.

### CSV<a name="s3-glacier-select-sql-reference-attribute-access-csv"></a>
+ **Column Numbers** – You can refer to the *Nth* column of a row with the column name `_N`, where N is the column position\. The position count starts at 1\. For example, the first column is named `_1` and the second column is named `_2`\.

  You can refer to a column as `_N` or `alias._N`\. For example, `_2` and `myAlias._2` are both valid ways to refer to a column in the `SELECT` list and `WHERE` clause\.
+ **Column Headers** – For objects in CSV format that have a header row, the headers are available to the `SELECT` list and `WHERE` clause\. In particular, as in traditional SQL, within `SELECT` and `WHERE` clause expressions, you can refer to the columns by `alias.column_name` or `column_name`\.

### JSON \(Amazon S3 Select only\)<a name="s3-glacier-select-sql-reference-attribute-access-json"></a>
+ **Document** – You can access JSON document fields as `alias.name`\. Nested fields can also be accessed; for example, `alias.name1.name2.name3`\.
+ **List** – You can access elements in a JSON list using zero\-based indexes with the `[]` operator\. For example, you can access the second element of a list as `alias[1]`\. Accessing list elements can be combined with fields as `alias.name1.name2[1].name3`\.
+ **Examples:** Consider this JSON object as a sample dataset:

  ```
  {"name": "Susan Smith",
  "org": "engineering",
  "projects":
      [
       {"project_name":"project1", "completed":false},
       {"project_name":"project2", "completed":true}
      ]
  }
  ```

  *Example \#1*

  The following query returns these results:

  ```
  Select s.name from S3Object s
  ```

  ```
  {"name":"Susan Smith"}
  ```

  *Example \#2*

  The following query returns these results:

  ```
  Select s.projects[0].project_name from S3Object s
  ```

  ```
  {"project_name":"project1"}
  ```

## Case Sensitivity of Header/Attribute Names<a name="s3-glacier-select-sql-reference-case-sensitivity"></a>

With Amazon S3 Select and Amazon Glacier Select, you can use double quotation marks to indicate that column headers \(for CSV objects\) and attributes \(for JSON objects\) are case sensitive\. Without double quotation marks, object headers/attributes are case insensitive\. An error is thrown in cases of ambiguity\.

The following examples are either 1\) Amazon S3 or Amazon Glacier objects in CSV format with the specified column header\(s\), and with `FileHeaderInfo` set to "Use" for the query request; or 2\) Amazon S3 objects in JSON format with the specified attributes\.

*Example \#1:* The object being queried has header/attribute "NAME"\.
+ The following expression successfully returns values from the object \(no quotation marks: case insensitive\):

  ```
  SELECT s.name from S3Object s
  ```
+ The following expression results in a 400 error `MissingHeaderName` \(quotation marks: case sensitive\):

  ```
  SELECT s."name" from S3Object s
  ```

*Example \#2:* The Amazon S3 object being queried has one header/attribute with "NAME" and another header/attribute with "name"\.
+ The following expression results in a 400 error `AmbiguousFieldName` \(no quotation marks: case insensitive, but there are two matches\):

  ```
  SELECT s.name from S3Object s
  ```
+ The following expression successfully returns values from the object \(quotation marks: case sensitive, so it resolves the ambiguity\)\.

  ```
  SELECT s."NAME" from S3Object s
  ```

## Using Reserved Keywords as User\-Defined Terms<a name="s3-glacier-select-sql-reference-using-keywords"></a>

Amazon S3 Select and Amazon Glacier Select have a set of reserved keywords that are needed to execute the SQL expressions used to query object content\. Reserved keywords include function names, data types, operators, and so on\. In some cases, user\-defined terms like the column headers \(for CSV files\) or attributes \(for JSON object\) may clash with a reserved keyword\. When this happens, you must use double quotation marks to indicate that you are intentionally using a user\-defined term that clashes with a reserved keyword\. Otherwise a 400 parse error will result\.

For the full list of reserved keywords see [Reserved Keywords](s3-glacier-select-sql-reference-keyword-list.md)\.

The following example is either 1\) an Amazon S3 or Amazon Glacier object in CSV format with the specified column headers, with `FileHeaderInfo` set to "Use" for the query request, or 2\) an Amazon S3 object in JSON format with the specified attributes\.

*Example:* The object being queried has header/attribute named "CAST", which is a reserved keyword\.
+ The following expression successfully returns values from the object \(quotation marks: use user\-defined header/attribute\):

  ```
  SELECT s."CAST" from S3Object s
  ```
+ The following expression results in a 400 parse error \(no quotation marks: clash with reserved keyword\):

  ```
  SELECT s.CAST from S3Object s
  ```

## Scalar Expressions<a name="s3-glacier-select-sql-reference-scalar"></a>

Within the `WHERE` clause and the `SELECT` list, you can have SQL *scalar expressions*, which are expressions that return scalar values\. They have the following form:
+ ***literal*** 

  An SQL literal\. 
+ ***column\_reference*** 

  A reference to a column in the form *column\_name* or *alias\.column\_name*\. 
+ ***unary\_op*** ***expression*** 

  Where ***unary\_op*** unary is an SQL unary operator\.
+ ***expression*** ***binary\_op*** ***expression*** 

   Where ****binary\_op**** is an SQL binary operator\. 
+ **func\_name** 

   Where **func\_name** is the name of a scalar function to invoke\. 
+ ***expression*** `[ NOT ] BETWEEN` ***expression*** `AND` ***expression***
+ ***expression*** `LIKE` ***expression*** \[ `ESCAPE` ***expression*** \]