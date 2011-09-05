DROP TABLE BIGINT_TYPE_TABLE;
DROP TABLE BINARY_TYPE_TABLE;
DROP TABLE BIT_TYPE_TABLE;
DROP TABLE BLOB_TYPE_TABLE;
DROP TABLE BOOLEAN_TYPE_TABLE;
DROP TABLE CHAR_TYPE_TABLE;
DROP TABLE CLOB_TYPE_TABLE;
DROP TABLE DATE_TYPE_TABLE;
DROP TABLE DECIMAL_TYPE_TABLE;
DROP TABLE DOUBLE_TYPE_TABLE;
DROP TABLE FLOAT_TYPE_TABLE;
DROP TABLE INTEGER_TYPE_TABLE;
DROP TABLE LONGVARBINARY_TYPE_TABLE;
DROP TABLE LONGVARCHAR_TYPE_TABLE;
DROP TABLE NUMERIC_TYPE_TABLE;
DROP TABLE REAL_TYPE_TABLE;
DROP TABLE SMALLINT_TYPE_TABLE;
DROP TABLE TIME_TYPE_TABLE;
DROP TABLE TIMESTAMP_TYPE_TABLE;
DROP TABLE TINYINT_TYPE_TABLE;
DROP TABLE VARBINARY_TYPE_TABLE;
DROP TABLE VARCHAR_TYPE_TABLE;

CREATE TABLE BIGINT_TYPE_TABLE
(
   BIGINT_COLUMN BIGINT
);

CREATE TABLE BINARY_TYPE_TABLE
(
   BINARY_COLUMN BLOB
);

CREATE TABLE BIT_TYPE_TABLE
(
   BIT_COLUMN SMALLINT
);

CREATE TABLE BLOB_TYPE_TABLE
(
   BLOB_COLUMN BLOB
);

CREATE TABLE BOOLEAN_TYPE_TABLE
(
   BOOLEAN_COLUMN SMALLINT
);

CREATE TABLE CHAR_TYPE_TABLE
(
   CHAR_COLUMN VARCHAR(4000)
);

CREATE TABLE CLOB_TYPE_TABLE
(
   CLOB_COLUMN clob(1073741823)
);

CREATE TABLE DATE_TYPE_TABLE
(
   DATE_COLUMN DATE
);

CREATE TABLE DECIMAL_TYPE_TABLE
(
   DECIMAL_COLUMN DECIMAL(31,10)
);

CREATE TABLE DOUBLE_TYPE_TABLE
(
   DOUBLE_COLUMN FLOAT(52)
);

CREATE TABLE FLOAT_TYPE_TABLE
(
   FLOAT_COLUMN FLOAT(52)
);

CREATE TABLE INTEGER_TYPE_TABLE
(
   INTEGER_COLUMN INT
);

CREATE TABLE LONGVARBINARY_TYPE_TABLE
(
   LONGVARBINARY_COLUMN BLOB(1073741823)
);

CREATE TABLE LONGVARCHAR_TYPE_TABLE
(
   LONGVARCHAR_COLUMN LONG VARCHAR
);

CREATE TABLE NUMERIC_TYPE_TABLE
(
   NUMERIC_COLUMN BIGINT
);

CREATE TABLE REAL_TYPE_TABLE
(
   REAL_COLUMN REAL
);

CREATE TABLE SMALLINT_TYPE_TABLE
(
   SMALLINT_COLUMN SMALLINT
);

CREATE TABLE TIME_TYPE_TABLE
(
   TIME_COLUMN TIME
);

CREATE TABLE TIMESTAMP_TYPE_TABLE
(
   TIMESTAMP_COLUMN TIMESTAMP
);

CREATE TABLE TINYINT_TYPE_TABLE
(
   TINYINT_COLUMN SMALLINT
);

CREATE TABLE VARBINARY_TYPE_TABLE
(
   VARBINARY_COLUMN BLOB
);

CREATE TABLE VARCHAR_TYPE_TABLE
(
   VARCHAR_COLUMN clob(1073741823)
);
