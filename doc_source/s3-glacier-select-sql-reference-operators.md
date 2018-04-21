# Operators<a name="s3-glacier-select-sql-reference-operators"></a>

Amazon S3 Select and Amazon Glacier Select support the following operators\.

## Logical Operators<a name="s3-glacier-select-sql-reference-loical-ops"></a>
+ `AND`
+ `NOT`
+ `OR`

## Comparison Operators<a name="s3-glacier-select-sql-reference-compare-ops"></a>
+ `<` 
+ `>` 
+ `<=`
+ `>=`
+ `=`
+ `<>`
+ `!=`
+ `BETWEEN`
+ `IN` – For example: `IN ('a', 'b', 'c')`

## Pattern Matching Operators<a name="s3-glacier-select-sql-reference-pattern"></a>
+ `LIKE`

## Math Operators<a name="s3-glacier-select-sql-referencemath-ops"></a>

Addition, subtraction, multiplication, division, and modulo are supported\.
+ \+
+ \-
+ \*
+ %

## Operator Precedence<a name="s3-glacier-select-sql-reference-op-Precedence"></a>

The following table shows the operators' precedence in decreasing order\.


|  Operator/Element  |  Associativity |  Required  | 
| --- | --- | --- | 
| \-  | right  | unary minus  | 
| \*, /, %  | left  | multiplication, division, modulo  | 
| \+, \-  | left  | addition, subtraction  | 
| IN |  | set membership  | 
| BETWEEN |  | range containment  | 
| LIKE |  | string pattern matching  | 
| <> |  | less than, greater than  | 
| = | right  | equality, assignment | 
| NOT | right | logical negation  | 
| AND | left | logical conjunction  | 
| OR | left | logical disjunction  | 