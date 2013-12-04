package DLDBM;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Duong89
 */
public class DLDatabaseDiscover {

    DLEnvironmentSetting HWSetting;

    public DLDatabaseDiscover(DLEnvironmentSetting HWSetting) {
        this.HWSetting = HWSetting;
    }

    public ResultSet GetTableInfomation() throws Exception {

        String sql = "select table_name, column_name, data_type" + " from information_schema.columns" + " where table_name in" + " (" + "  select table_name" + " " + " from Information_Schema.Tables" + "  where Table_Type='Base Table'" + ") order by table_name";
        return new DLDatabase(HWSetting).GetResultSet(sql, HWSetting.getMyConnection());

    }

    public ResultSet getConstraintReferFrom(String tablename) throws Exception {
        String SQl = "select cast(f.name  as varchar(255)) as foreign_key_name" + " , r.keycnt " + " , cast(c.name as  varchar(255)) as foreign_table  " + " , cast(fc.name as varchar(255)) as  foreign_column_1 " + " , cast(fc2.name as varchar(255)) as foreign_column_2 " + " ,  cast(p.name as varchar(255)) as primary_table" + "   , cast(rc.name as varchar(255))  as primary_column_1 " + " , cast(rc2.name as varchar(255)) as  primary_column_2 " + "  from sysobjects f " + "   inner join sysobjects c on  f.parent_obj = c.id " + "   inner join sysreferences r on f.id =  r.constid " + "    inner join sysobjects p on r.rkeyid = p.id " + "    inner  join syscolumns rc on r.rkeyid = rc.id and r.rkey1 = rc.colid " + "   inner  join syscolumns fc on r.fkeyid = fc.id and r.fkey1 = fc.colid " + "   left join  syscolumns rc2 on r.rkeyid = rc2.id and r.rkey2 = rc.colid " + "  left join  syscolumns fc2 on r.fkeyid = fc2.id and r.fkey2 = fc.colid " + "  where f.type =  'F' and p.name='" + tablename + "' " + " ORDER BY cast(p.name as varchar(255)) ";
        return new DLDatabase(HWSetting).GetResultSet(SQl, HWSetting.getMyConnection());
    }

    public ResultSet getPrimaryKey(String tableName) throws Exception {
        String SQL = "SELECT " + "   T.TABLE_NAME,  " + "  T.CONSTRAINT_NAME,  " + "   K.COLUMN_NAME,  " + "   K.ORDINAL_POSITION  " + "FROM  " + "    INFORMATION_SCHEMA.TABLE_CONSTRAINTS T " + "    INNER JOIN " + "    INFORMATION_SCHEMA.KEY_COLUMN_USAGE K " + "    ON T.CONSTRAINT_NAME = K.CONSTRAINT_NAME  " + "WHERE " + "    T.CONSTRAINT_TYPE = 'PRIMARY KEY'  and T.TABLE_NAME='" + tableName + "'" + " ORDER BY " + "   T.TABLE_NAME, " + "    K.ORDINAL_POSITION";
        return new DLDatabase(HWSetting).GetResultSet(SQL, HWSetting.getMyConnection());
    }

    public ResultSet getConstraintReferTo(String tablename) throws Exception {
        String SQl = " SELECT " + " FK_Table  = FK.TABLE_NAME, " + " FK_Column = CU.COLUMN_NAME, " + " PK_Table  = PK.TABLE_NAME, " + " PK_Column = PT.COLUMN_NAME, " + "  Constraint_Name = C.CONSTRAINT_NAME " + "FROM " + "   INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS C " + "   INNER JOIN " + "  INFORMATION_SCHEMA.TABLE_CONSTRAINTS FK " + "     ON C.CONSTRAINT_NAME = FK.CONSTRAINT_NAME " + "  INNER JOIN " + "   INFORMATION_SCHEMA.TABLE_CONSTRAINTS PK " + "      ON C.UNIQUE_CONSTRAINT_NAME = PK.CONSTRAINT_NAME " + "  INNER JOIN " + "  INFORMATION_SCHEMA.KEY_COLUMN_USAGE CU " + "       ON C.CONSTRAINT_NAME = CU.CONSTRAINT_NAME " + "   INNER JOIN " + " ( " + "     SELECT " + "         i1.TABLE_NAME, i2.COLUMN_NAME " + "     FROM " + "         INFORMATION_SCHEMA.TABLE_CONSTRAINTS i1 " + "         INNER JOIN " + "         INFORMATION_SCHEMA.KEY_COLUMN_USAGE i2 " + "         ON i1.CONSTRAINT_NAME = i2.CONSTRAINT_NAME " + "         WHERE i1.CONSTRAINT_TYPE = 'PRIMARY KEY' " + " ) PT  " + " ON PT.TABLE_NAME = PK.TABLE_NAME where FK.TABLE_NAME='" + tablename + "'";
        DLDatabase HWD = new DLDatabase(HWSetting);
        return HWD.GetResultSet(SQl, HWSetting.getMyConnection());
    }

    public String[] GetAllTables(Connection Con) throws SQLException {
        String[] Tables = null;
        ArrayList<String> ListTable = new ArrayList();

        DatabaseMetaData dbm = Con.getMetaData();
        String[] types = {"TABLE"};
        ResultSet rs = dbm.getTables(null, null, null, types);

        while (rs.next()) {
            String table = rs.getString("TABLE_NAME");
            ListTable.add(table);

        }
        Con.close();
        Tables = new String[ListTable.toArray().length];

        return ListTable.toArray(Tables);
    }

    public String getIdentityColumns(String tableName) throws SQLException, Exception {
//        String sql = "SELECT   OBJECT_NAME(OBJECT_ID) AS TABLENAME, " +
//                " \nNAME AS COLUMNNAME " +
//                " \n--SEED_VALUE, " +
//                " \n--INCREMENT_VALUE, " +
//                " \n--LAST_VALUE, " +
//                " \n--IS_NOT_FOR_REPLICATION " +
//                " \nFROM     SYS.IDENTITY_COLUMNS where OBJECT_NAME(OBJECT_ID) ='"+tableName+"'  " +
//                " \nORDER BY 1 ";
        String sql = "SELECT   OBJECT_NAME(OBJECT_ID) AS TABLENAME,  NAME AS COLUMNNAME  FROM     SYS.IDENTITY_COLUMNS where OBJECT_NAME(OBJECT_ID) ='" + tableName + "' ";
        ResultSet rs = new DLDatabase(HWSetting).GetResultSet(sql, HWSetting.getMyConnection());
        String idenCol ="";
        rs.beforeFirst();
        while (rs.next()) {
            idenCol = rs.getString(2);
           

        }
       
        return idenCol;
    }
}
