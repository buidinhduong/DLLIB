SELECT * FROM INFORMATION_SCHEMA.CONSTRAINT_TABLE_USAGE where TABLE_NAME = N'card'
SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS where TABLE_NAME = N'card'
SELECT * FROM INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE where TABLE_NAME = N'hoa'

select cast(f.name  as varchar(255)) as foreign_key_name
    , r.keycnt
    , cast(c.name as  varchar(255)) as foreign_table
    , cast(fc.name as varchar(255)) as  foreign_column_1
    , cast(fc2.name as varchar(255)) as foreign_column_2
    ,  cast(p.name as varchar(255)) as primary_table
    , cast(rc.name as varchar(255))  as primary_column_1
    , cast(rc2.name as varchar(255)) as  primary_column_2
    from sysobjects f
    inner join sysobjects c on  f.parent_obj = c.id
    inner join sysreferences r on f.id =  r.constid
    inner join sysobjects p on r.rkeyid = p.id
    inner  join syscolumns rc on r.rkeyid = rc.id and r.rkey1 = rc.colid
    inner  join syscolumns fc on r.fkeyid = fc.id and r.fkey1 = fc.colid
    left join  syscolumns rc2 on r.rkeyid = rc2.id and r.rkey2 = rc.colid
    left join  syscolumns fc2 on r.fkeyid = fc2.id and r.fkey2 = fc.colid
    where f.type =  'F' and p.name='Articles'
 ORDER BY cast(p.name as varchar(255)) 
 
 sELECT f.name AS ForeignKey,
OBJECT_NAME(f.parent_object_id) AS TableName,
COL_NAME(fc.parent_object_id,
fc.parent_column_id) AS ColumnName,
OBJECT_NAME (f.referenced_object_id) AS ReferenceTableName,
COL_NAME(fc.referenced_object_id,
fc.referenced_column_id) AS ReferenceColumnName
FROM sys.foreign_keys AS f
INNER JOIN sys.foreign_key_columns AS fc
ON f.OBJECT_ID = fc.constraint_object_id
 where f.parent_object_id='1'
 
 SELECT
ALTER TABLE ‘+FK.TABLE_NAME+
‘ ADD CONSTRAINT ‘+C.CONSTRAINT_NAME+’ FOREIGN KEY’+
‘(‘+CU.COLUMN_NAME+’) ‘+
‘REFERENCES ‘+PK.TABLE_NAME+
‘(‘+PT.COLUMN_NAME+’)’ ForeignKeyScripts
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
–WHERE PK.TABLE_NAME IN ('Table1', 'Table2')
–WHERE FK.TABLE_NAME IN ('Table1', 'Table2')

SELECT sfk.fkeyid, sof.name AS fTableName, sor.name AS rTableName, scf.name AS fColName, scr.name AS rColName
FROM dbo.sysforeignkeys sfk INNER JOIN
dbo.sysobjects sof ON sfk.fkeyid = sof.id INNER JOIN
dbo.sysobjects sor ON sfk.rkeyid = sor.id INNER JOIN
dbo.syscolumns scf ON sfk.fkey = scf.colid AND sof.id = scf.id INNER JOIN
dbo.syscolumns scr ON sfk.rkey = scr.colid AND sor.id = scr.id

SELECT f.name AS ForeignKey,
OBJECT_NAME(f.parent_object_id) AS TableName,
COL_NAME(fc.parent_object_id,
fc.parent_column_id) AS ColumnName,
OBJECT_NAME (f.referenced_object_id) AS ReferenceTableName,
COL_NAME(fc.referenced_object_id,
fc.referenced_column_id) AS ReferenceColumnName,
f.update_referential_action_desc AS UpdateAction,
f.delete_referential_action_desc AS DeleteAction
FROM sys.foreign_keys AS f
INNER JOIN sys.foreign_key_columns AS fc
ON f.OBJECT_ID = fc.constraint_object_id
ORDER BY TableName ASC, ColumnName ASC

SELECT TABLE_NAME, TABLE_SCHEMA, COLUMN_NAME, CONSTRAINT_NAME FROM INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE CCU
INNER JOIN sys.foreign_keys f
ON CCU.TABLE_NAME = object_name(parent_object_id) and CONSTRAINT_NAME = f.name
WHERE f.type = 'F'
AND NOT EXISTS (
SELECT OBJECT_NAME(i.object_id) AS tablename,
i.name AS indexname,
a.name AS columnname
FROM sys.indexes i
INNER JOIN sys.index_columns c
ON i.object_id = c.object_id AND i.index_id = c.index_id
INNER JOIN sys.all_columns a
ON a.object_id = c.object_id AND a.column_id = c.column_id
WHERE CCU.TABLE_NAME = OBJECT_NAME(i.object_id)
AND CCU.COLUMN_NAME = a.name
)

ORDER BY CCU.TABLE_SCHEMA, CCU.TABLE_NAME, CCU.COLUMN_NAME