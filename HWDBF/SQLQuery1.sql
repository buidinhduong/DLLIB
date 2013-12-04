 SELECT f.name AS ForeignKey, OBJECT_NAME(f.parent_object_id) AS TableName, COL_NAME(fc.parent_object_id, fc.parent_column_id) AS ColumnName, OBJECT_NAME (f.referenced_object_id) AS ReferenceTableName, COL_NAME(fc.referenced_object_id, fc.referenced_column_id) AS ReferenceColumnName FROM sys.foreign_keys AS f INNER JOIN sys.foreign_key_columns AS fc ON f.OBJECT_ID = fc.constraint_object_id   where OBJECT_NAME(f.parent_object_id)='Attempts'
 select cast(f.name  as varchar(255)) as foreign_key_name , r.keycnt  , cast(c.name as  varchar(255)) as foreign_table   , cast(fc.name as varchar(255)) as  foreign_column_1  , cast(fc2.name as varchar(255)) as foreign_column_2  ,  cast(p.name as varchar(255)) as primary_table   , cast(rc.name as varchar(255))  as primary_column_1  , cast(rc2.name as varchar(255)) as  primary_column_2   from sysobjects f    inner join sysobjects c on  f.parent_obj = c.id    inner join sysreferences r on f.id =  r.constid     inner join sysobjects p on r.rkeyid = p.id     inner  join syscolumns rc on r.rkeyid = rc.id and r.rkey1 = rc.colid    inner  join syscolumns fc on r.fkeyid = fc.id and r.fkey1 = fc.colid    left join  syscolumns rc2 on r.rkeyid = rc2.id and r.rkey2 = rc.colid   left join  syscolumns fc2 on r.fkeyid = fc2.id and r.fkey2 = fc.colid   where f.type =  'F' and p.name='Nguoi'  ORDER BY cast(p.name as varchar(255))
 SELECT 
OBJECT_NAME(f.parent_object_id) AS TableName,
COL_NAME(fc.parent_object_id,
fc.parent_column_id) AS ColumnName,
OBJECT_NAME (f.referenced_object_id) AS ReferenceTableName,
COL_NAME(fc.referenced_object_id,
fc.referenced_column_id) AS ReferenceColumnName
FROM sys.foreign_keys AS f
INNER JOIN sys.foreign_key_columns AS fc
ON f.OBJECT_ID = fc.constraint_object_id


SELECT 
    FK_Table  = FK.TABLE_NAME, 
    FK_Column = CU.COLUMN_NAME, 
    PK_Table  = PK.TABLE_NAME, 
    PK_Column = PT.COLUMN_NAME, 
    Constraint_Name = C.CONSTRAINT_NAME 
FROM 
    INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS C 
    INNER JOIN 
    INFORMATION_SCHEMA.TABLE_CONSTRAINTS FK 
        ON C.CONSTRAINT_NAME = FK.CONSTRAINT_NAME 
    INNER JOIN 
    INFORMATION_SCHEMA.TABLE_CONSTRAINTS PK 
        ON C.UNIQUE_CONSTRAINT_NAME = PK.CONSTRAINT_NAME 
    INNER JOIN 
    INFORMATION_SCHEMA.KEY_COLUMN_USAGE CU 
        ON C.CONSTRAINT_NAME = CU.CONSTRAINT_NAME 
    INNER JOIN 
    ( 
        SELECT 
            i1.TABLE_NAME, i2.COLUMN_NAME 
        FROM 
            INFORMATION_SCHEMA.TABLE_CONSTRAINTS i1 
            INNER JOIN 
            INFORMATION_SCHEMA.KEY_COLUMN_USAGE i2 
            ON i1.CONSTRAINT_NAME = i2.CONSTRAINT_NAME 
            WHERE i1.CONSTRAINT_TYPE = 'PRIMARY KEY' 
    ) PT  
  ON PT.TABLE_NAME = PK.TABLE_NAME where FK.TABLE_NAME='hang'
-- optional: 
ORDER BY 
    1,2,3,4
SELECT
K_Table = FK.TABLE_NAME,
FK_Column = CU.COLUMN_NAME,
PK_Table = PK.TABLE_NAME,
PK_Column = PT.COLUMN_NAME,
Constraint_Name = C.CONSTRAINT_NAME
FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS C
INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS FK ON C.CONSTRAINT_NAME = FK.CONSTRAINT_NAME
INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS PK ON C.UNIQUE_CONSTRAINT_NAME = PK.CONSTRAINT_NAME
INNER JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE CU ON C.CONSTRAINT_NAME = CU.CONSTRAINT_NAME
INNER JOIN (
SELECT i1.TABLE_NAME, i2.COLUMN_NAME
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS i1
INNER JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE i2 ON i1.CONSTRAINT_NAME = i2.CONSTRAINT_NAME
WHERE i1.CONSTRAINT_TYPE = 'PRIMARY KEY'
) PT ON PT.TABLE_NAME = PK.TABLE_NAME

SELECT  
    T.TABLE_NAME,  
    T.CONSTRAINT_NAME,  
    K.COLUMN_NAME,  
    K.ORDINAL_POSITION  
FROM  
    INFORMATION_SCHEMA.TABLE_CONSTRAINTS T 
    INNER JOIN 
    INFORMATION_SCHEMA.KEY_COLUMN_USAGE K 
    ON T.CONSTRAINT_NAME = K.CONSTRAINT_NAME  
WHERE 
    T.CONSTRAINT_TYPE = 'PRIMARY KEY'  and T.TABLE_NAME='HoaDon'
    -- AND T.TABLE_NAME = 'table_name' 
ORDER BY 
    T.TABLE_NAME, 
    K.ORDINAL_POSITION
 
 ----------
 select cast(f.name  as varchar(255)) as foreign_key_name , r.keycnt  , cast(c.name as  varchar(255)) as foreign_table   , cast(fc.name as varchar(255)) as  foreign_column_1  , cast(fc2.name as varchar(255)) as foreign_column_2  ,  cast(p.name as varchar(255)) as primary_table   , cast(rc.name as varchar(255))  as primary_column_1  , cast(rc2.name as varchar(255)) as  primary_column_2   from sysobjects f    inner join sysobjects c on  f.parent_obj = c.id    inner join sysreferences r on f.id =  r.constid     inner join sysobjects p on r.rkeyid = p.id     inner  join syscolumns rc on r.rkeyid = rc.id and r.rkey1 = rc.colid 
    inner  join syscolumns fc on r.fkeyid = fc.id and r.fkey1 = fc.colid 
       left join  syscolumns rc2 on r.rkeyid = rc2.id and r.rkey2 = rc.colid
          left join  syscolumns fc2 on r.fkeyid = fc2.id and r.fkey2 = fc.colid
             where f.type =  'F' and p.name='Nguoi' 
              ORDER BY cast(p.name as varchar(255))