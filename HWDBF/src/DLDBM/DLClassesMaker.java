package DLDBM;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Duong89
 */
public class DLClassesMaker {

    DLEnvironmentSetting HW;
    protected ArrayList<DLClassTableInfo> ListObjectClass = new ArrayList<DLClassTableInfo>();

    protected int ResultGetRowCount(ResultSet rs) throws SQLException {
        try {
            rs.last();
            return rs.getRow();
        } catch (SQLException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLClassesMaker.class.getName(), "ResultGetRowCount()");
            throw ex;
        }
    }

    protected String GetNameOfColumn(ResultSet rs, int row, int column) {
        String Name = "";
        try {
            ResultSet TampRS = rs;
            TampRS.absolute(row);
            Name = TampRS.getString(column);

        } catch (SQLException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLClassesMaker.class.getName(), "GetNameOfColumn()");
            Logger.getLogger(DLClassesMaker.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
        return Name;

    }

    public void GetListObjectClass(ResultSet rs, int StartRow) throws SQLException, Exception {
        try {
            DLClassTableInfo Cls = new DLClassTableInfo();
            if (StartRow < ResultGetRowCount(rs) && StartRow != 0) {
                rs.absolute(StartRow);
                String CurrentTable = rs.getString("table_name");
                ArrayList<String> Fields = new ArrayList<String>();
                ArrayList<String> Types = new ArrayList<String>();
                String[] Field = new String[Fields.size()];
                String[] Type = new String[Types.size()];
                rs.absolute(StartRow);
                if (StartRow == 1) {
                    rs.beforeFirst();
                } else {
                    rs.absolute(StartRow - 1);
                }
                while (rs.next()) {
                    String ThisTable = "";
                    if (rs.getRow() != 0) {
                        ThisTable = GetNameOfColumn(rs, rs.getRow(), 1);
                    } else {
                        return;
                    }
                    if (ThisTable.equalsIgnoreCase(CurrentTable)) {
                        String CN = rs.getString("column_name");
                        Fields.add(CN);
                        String DT = new DLFormating(HW).ConvertToJavaType(rs.getString("data_type"));
                        Types.add(DT);
                    } else {
                        Cls.setClassName(CurrentTable);
                        Cls.setFields(Fields.toArray(Field));
                        Cls.setTypes(Types.toArray(Type));
                        ListObjectClass.add(Cls);
                        GetListObjectClass(rs, rs.getRow());
                    }
                }
            }
            setPrimaryKey();
            setConstraintReferFrom();
            setConstraintReferTo();
            setIdentityColoumn();

        } catch (SQLException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLClassesMaker.class.getName(), "GetNameOfColumn()");
            throw ex;
        }

    }

    protected void setIdentityColoumn() throws SQLException, Exception {
        DLDatabaseDiscover db = new DLDatabaseDiscover(HW);
        for (DLClassTableInfo clas : ListObjectClass) {

            clas.setIdentityColumn(db.getIdentityColumns(clas.ClassName));
        }
    }

    protected void setConstraintReferFrom() throws SQLException, Exception {
        DLDatabaseDiscover Ds = new DLDatabaseDiscover(HW);

        for (DLClassTableInfo C : ListObjectClass) {
            ArrayList<String> refBy = new ArrayList<String>();
            try {
                ResultSet Rs = Ds.getConstraintReferFrom(C.ClassName);
                while (Rs.next()) {
                    refBy.add(Rs.getString(3) + "." + Rs.getString(4) + "->this." + Rs.getString(7));
                }
                C.setReferFrom(refBy.toArray(new String[refBy.size()]));

            } catch (SQLException ex) {
                Logger.getLogger(DLClassesMaker.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
        }
    }

    protected void setPrimaryKey() throws SQLException, Exception {
        DLDatabaseDiscover Ds = new DLDatabaseDiscover(HW);
        for (DLClassTableInfo C : ListObjectClass) {
            ArrayList<String> Pk = new ArrayList<String>();
            try {
                ResultSet Rs = Ds.getPrimaryKey(C.ClassName);
                while (Rs.next()) {
                    Pk.add(Rs.getString(3));
                }
                C.setPrimaryKey(Pk.toArray(new String[Pk.size()]));

            } catch (SQLException ex) {
                Logger.getLogger(DLClassesMaker.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
        }
    }

    protected void setConstraintReferTo() throws SQLException, Exception {
        DLDatabaseDiscover Ds = new DLDatabaseDiscover(HW);
        for (DLClassTableInfo C : ListObjectClass) {
            ArrayList<String> refTo = new ArrayList<String>();
            try {
                ResultSet Rs = Ds.getConstraintReferTo(C.ClassName);
                while (Rs.next()) {
                    refTo.add("this." + Rs.getString(2) + "->" + Rs.getString(3) + "." + Rs.getString(4));
                }
                C.setReferTo(refTo.toArray(new String[refTo.size()]));

            } catch (SQLException ex) {
                Logger.getLogger(DLClassesMaker.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
        }
    }

    protected void AddMySQlTableToListClass(String TableName, Connection MySQLConnection) throws SQLException {
        try {
            Statement St = MySQLConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            DLClassTableInfo ThisTableClass = new DLClassTableInfo();
            ThisTableClass.setClassName(TableName);
            ResultSet ThistableInfo = St.executeQuery("show columns from " + TableName + ";");
            ArrayList<String> Fields = new ArrayList<String>();
            ArrayList<String> TypeS = new ArrayList<String>();
            DLFormating Format = new DLFormating(HW);
            ThistableInfo.beforeFirst();
            while (ThistableInfo.next()) {
                Fields.add(ThistableInfo.getString(1));
                TypeS.add(Format.ConvertToJavaType(ThistableInfo.getString(2)));
            }
            ThisTableClass.setFields(Fields.toArray(new String[Fields.size()]));
            ThisTableClass.setTypes(TypeS.toArray(new String[TypeS.size()]));
            ListObjectClass.add(ThisTableClass);
        } catch (SQLException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), this.getClass().getName(), "AddMySQlTableToListClass()");
            Logger.getLogger(DLClassesMaker.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    protected void getListObjectClassMySQL(Connection MySQLConnection) {

        try {
            Statement St = MySQLConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet tablesRS = St.executeQuery("show tables;");
            tablesRS.beforeFirst();
            while (tablesRS.next()) {
                AddMySQlTableToListClass(tablesRS.getString(1), MySQLConnection);
            }
        } catch (SQLException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), this.getClass().getName(), "getListObjectClassMySQL()");
            Logger.getLogger(DLClassesMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected static String DefaultContructor(String ClassName) {
        String DefaultContructor = "\npublic " + ClassName + " (){";
        DefaultContructor += "\n}";
        return DefaultContructor;
    }

    protected static String Contructor(String Name, String[] Type_ParamName) {
        String con = "\npublic " + Name + " (";
        String SetValue = "";
        for (int i = 0; i < Type_ParamName.length; i++) {
            if (i < Type_ParamName.length - 1) {
                con += Type_ParamName[i] + ",";
            } else {
                con += Type_ParamName[i] + "){";
            }
            SetValue += "\nthis." + Type_ParamName[i].split(" ")[1] + "=" + Type_ParamName[i].split(" ")[1] + ";";
        }
        con += SetValue + "\n}";
        return con;

    }

    protected String IdentityColumn(String identityColumn) {

        String idencode = "\npublic String IdentityColumn(){";
        idencode += "\nreturn  \""+identityColumn+"\";";
        
        idencode += "\n}";
        return idencode;

    }

    protected static String GetProperty(String DataType, String FieldName, boolean IsStatic) {
        String Start = "";
        if (IsStatic) {
            Start = "\npublic static ";
        } else {
            Start = "\npublic ";
        }
        Start += DataType + " get" + FieldName + "(){ \n return " + FieldName + ";\n}";
        return Start;
    }

    protected static String SetProperty(String DataType, String FieldName, boolean IsStatic) {
        String Start = "";
        if (IsStatic) {
            Start = "\npublic static void ";
        } else {
            Start = "\npublic void ";
        }
        Start += " set" + FieldName + "(" + DataType + " " + FieldName + "){ \n this." + FieldName + "=" + FieldName + ";\n}";
        return Start;
    }

    protected String[] Type_Fields(String[] Type, String[] Fields) {
        String[] RT = new String[Type.length];
        for (int i = 0; i < Type.length; i++) {
            RT[i] = Type[i] + " " + Fields[i];
        }
        return RT;
    }

    protected String Import() {
        String Import = "\nimport " + this.getClass().getPackage().getName() + ".HWFormating;";
        Import += "\nimport " + this.getClass().getPackage().getName() + ".HWClassInterface;";

        return Import;
    }

    protected String OrderColumn(String[] Fields) {
        String OrderMethod = "\npublic String[] OrderColumn(){";
        OrderMethod += "\nreturn new String[]{";
        for (int i = 0; i < Fields.length; i++) {
            if (i < Fields.length - 1) {
                OrderMethod += "\"" + Fields[i] + "\",";
            } else {
                OrderMethod += "\"" + Fields[i] + "\"};";
            }
        }
        OrderMethod += "\n}";
        return OrderMethod;
    }

    protected String CloneMethod(String ClassName) {
        String Clone = "\npublic " + ClassName + " cloneObject(){";
        Clone += "\n\t  return new " + ClassName + "();\n}";
        return Clone;
    }

    protected String InsertMethod(DLClassTableInfo TI) {
        String BodyInsert = "\npublic String Insert(){";
        BodyInsert += "\nreturn \"Insert into " + TI.getClassName() + " values(\"";
        // "CategoryID="+DLFormating.FormatSQLCommand(this.getCategoryID());
        for (int i = 0; i < TI.getFields().length; i++) {
            if (i < TI.getFields().length - 1) {
                BodyInsert += "\n+HWFormating.FormatSQLCommand(this.get" + TI.getFields()[i] + "())+\",\"";
            } else {
                BodyInsert += "\n+HWFormating.FormatSQLCommand(this.get" + TI.getFields()[i] + "())+\")\";";
            }
        }
        BodyInsert += "\n}";
        return BodyInsert;
    }

    protected String UpdateMethod(DLClassTableInfo TI) {
        String BodyUpdate = "\npublic String Update(){";
        BodyUpdate += "\nreturn \"Update " + TI.getClassName() + " set \"";
        // "CategoryID="+DLFormating.FormatSQLCommand(this.getCategoryID());
        for (int i = 0; i < TI.getFields().length; i++) {
            if (i == 0) {
                continue;
            }
            if (i < TI.getFields().length - 1) {
                BodyUpdate += "\n+\"" + TI.getFields()[i] + "=\"+HWFormating.FormatSQLCommand(this.get" + TI.getFields()[i] + "())+\",\"";
            } else {
                BodyUpdate += "\n+\"" + TI.getFields()[i] + "=\"+HWFormating.FormatSQLCommand(this.get" + TI.getFields()[i] + "())";
            }

        }
        BodyUpdate += "\n+\" where " + TI.getFields()[0] + "=\"+HWFormating.FormatSQLCommand(this.get" + TI.getFields()[0] + "());";
        BodyUpdate += "\n}";
        return BodyUpdate;
    }

    protected String GetDefaultContentFileClass(DLClassTableInfo InfoTable) {
        StringBuilder ContentBuilder = new StringBuilder();
        ContentBuilder.append("/*This Class was created automatic by [HWDBM]." + "\nEmail:hack.wan@yahoo.com.vn\nMobile:0948657899\nWebsite:my.opera.com/HWDBM" + "\nIf you dont understand or you want to know more about it,\nyou " + "can send an email for me, or access to my website." + "\nyou can also download my HWDBM on web\nThank you !*/");
        ContentBuilder.append(Import());
        ContentBuilder.append("\npublic class " + InfoTable.getClassName() + " implements HWClassInterface {");
        for (String F : Type_Fields(InfoTable.Types, InfoTable.Fields)) {
            ContentBuilder.append("\n" + F + ";");
        }
        ContentBuilder.append(DefaultContructor(InfoTable.getClassName()));
        ContentBuilder.append("\n" + Contructor(InfoTable.ClassName, Type_Fields(InfoTable.Types, InfoTable.Fields)));
        for (int i = 0; i < InfoTable.Types.length; i++) {
            ContentBuilder.append("\n" + GetProperty(InfoTable.Types[i], InfoTable.Fields[i], false));
            ContentBuilder.append("\n" + SetProperty(InfoTable.Types[i], InfoTable.Fields[i], false));
        }
        ContentBuilder.append("/*\nThis method used to make an insert,update sql with specified order in array*/");
        ContentBuilder.append(OrderColumn(InfoTable.Fields));
        ContentBuilder.append(CloneMethod(InfoTable.ClassName));
        ContentBuilder.append(primaryKeyMethod(InfoTable));
        ContentBuilder.append(ReferFromMethod(InfoTable));
        ContentBuilder.append(ReferToMethod(InfoTable));
        ContentBuilder.append(IdentityColumn(InfoTable.getIdentityColumn()));
        ContentBuilder.append("\n}");//end of file
        return ContentBuilder.toString();
    }

    protected String GetFullContentFileClass(DLClassTableInfo InfoTable) {
        StringBuilder ContentBuilder = new StringBuilder();
        ContentBuilder.append("/*This Class was created  by [HWDBM]." + "\nEmail:hack.wan@yahoo.com.vn\nMobile:0948657899\nWebsite:my.opera.com/HWDBM" + "\nIf you dont understand or you want to know more about it,\nyou " + "can send an email for me, or access to my website." + "\nyou can also download my HWDBM on web\nThank you !*/");
        ContentBuilder.append(Import());
        ContentBuilder.append("\npublic class " + InfoTable.getClassName() + " implements HWClassInterface {");
        for (String F : Type_Fields(InfoTable.Types, InfoTable.Fields)) {
            ContentBuilder.append("\n" + F + ";");
        }
        ContentBuilder.append(DefaultContructor(InfoTable.getClassName()));
        ContentBuilder.append("\n" + Contructor(InfoTable.ClassName, Type_Fields(InfoTable.Types, InfoTable.Fields)));
        for (int i = 0; i < InfoTable.Types.length; i++) {
            ContentBuilder.append("\n" + GetProperty(InfoTable.Types[i], InfoTable.Fields[i], false));
            ContentBuilder.append("\n" + SetProperty(InfoTable.Types[i], InfoTable.Fields[i], false));
        }
        ContentBuilder.append("/*\nThis method's used to make an insert,update sql with specified order in array*/");
        ContentBuilder.append(OrderColumn(InfoTable.Fields));
        ContentBuilder.append(UpdateMethod(InfoTable));
        ContentBuilder.append(InsertMethod(InfoTable));
        ContentBuilder.append(CloneMethod(InfoTable.ClassName));
        ContentBuilder.append(primaryKeyMethod(InfoTable));
        ContentBuilder.append(ReferFromMethod(InfoTable));
        ContentBuilder.append(ReferToMethod(InfoTable));
        ContentBuilder.append(IdentityColumn(InfoTable.getIdentityColumn()));
        ContentBuilder.append("\n}");//end of file
        return ContentBuilder.toString();
    }

    protected String primaryKeyMethod(DLClassTableInfo ClassT) {
        String primaryKey = "\npublic String[] PrimaryKey(){";
        primaryKey += "\n return new String[]{";
        if (ClassT.getPrimaryKey() != null) {
            for (int i = 0; i < ClassT.getPrimaryKey().length; i++) {
                if (i < ClassT.getPrimaryKey().length - 1) {
                    primaryKey += "\"" + ClassT.getPrimaryKey()[i] + "\",";
                } else {
                    primaryKey += "\"" + ClassT.getPrimaryKey()[i] + "\"";
                }
            }
        }
        primaryKey += "};\n}";
        return primaryKey;
    }

    protected String ReferFromMethod(DLClassTableInfo ClassT) {
        String primaryKey = "\npublic String[] ReferBy(){";
        primaryKey += "\n return new String[]{";
        if (ClassT.getPrimaryKey() != null) {
            for (int i = 0; i < ClassT.getReferFrom().length; i++) {
                if (i < ClassT.getReferFrom().length - 1) {
                    primaryKey += "\"" + ClassT.getReferFrom()[i] + "\",";
                } else {
                    primaryKey += "\"" + ClassT.getReferFrom()[i] + "\"";
                }
            }
        }
        primaryKey += "};\n}";
        return primaryKey;
    }

    protected String ReferToMethod(DLClassTableInfo ClassT) {
        String primaryKey = "\npublic String[] ReferTo(){";
        primaryKey += "\n return new String[]{";
        if (ClassT.getPrimaryKey() != null) {
            for (int i = 0; i < ClassT.getReferTo().length; i++) {
                if (i < ClassT.getReferTo().length - 1) {
                    primaryKey += "\"" + ClassT.getReferTo()[i] + "\",";
                } else {
                    primaryKey += "\"" + ClassT.getReferTo()[i] + "\"";
                }
            }
        }
        primaryKey += "};\n}";
        return primaryKey;
    }

    public void CreateFullClasses() throws Exception {
        try {

            GetListObjectClass(new DLDatabaseDiscover(HW).GetTableInfomation(), 1);
            DLFile P = new DLFile(HW);
            for (DLClassTableInfo C : ListObjectClass) {
                String Body = GetFullContentFileClass(C);
                String ThisFile = this.HW.getClassesFolder() + C.getClassName() + ".java";
                File MyF = new File(ThisFile);
                String FullBody = "package " + MyF.getParentFile().getName() + ";" + "\n" + Body;

                P.WriteFile(ThisFile, FullBody);
            }
            CreateDBDescription();
        } catch (Exception er) {
            er.printStackTrace();
            new DLLogging(HW).WriteError(er.getMessage(), this.getClass().getName(), "CreateClasses()");
            throw er;
        }

    }

    public void CreateDefaultClasses() throws Exception {
        try {
            if (HW.getDatabaseType().equalsIgnoreCase("SQLServer")) {
                GetListObjectClass(new DLDatabaseDiscover(HW).GetTableInfomation(), 1);
            }
            if (HW.getDatabaseType().equalsIgnoreCase("MySQL")) {
                getListObjectClassMySQL(HW.getMyConnection());
            }
            DLFile P = new DLFile(HW);
            for (DLClassTableInfo C : ListObjectClass) {
                String Body = GetDefaultContentFileClass(C);
                String ThisFile = this.HW.getClassesFolder() + C.getClassName() + ".java";
                File MyF = new File(ThisFile);
                String FullBody = "package " + MyF.getParentFile().getName() + ";" + "\n" + Body;
                P.WriteFile(ThisFile, FullBody);
            }
            CreateDBDescription();
        } catch (Exception er) {
            er.printStackTrace();
            new DLLogging(HW).WriteError(er.getMessage(), this.getClass().getName(), "CreateClasses()");
            throw er;
        }

    }

    protected String CreateDBDescription() {
        String ThisFile = this.HW.getClassesFolder() + ListObjectClass.get(0).getClassName() + ".java";
        File MyF = new File(ThisFile);
        String HWDBDecription = "package " + MyF.getParentFile().getName() + ";\npublic class HWDBDescription{";
        HWDBDecription += "\npublic static Object[] getAllObjectClass(){";
        String ListO = "\nreturn new Object[]{";
        for (int i = 0; i < ListObjectClass.size(); i++) {
            if (i == ListObjectClass.size() - 1) {
                ListO += "new " + ListObjectClass.get(i).getClassName() + "()};";
            } else {
                ListO += "new " + ListObjectClass.get(i).getClassName() + "(),";
            }
        }
        HWDBDecription += ListO;
        HWDBDecription += "\n}\n}";
        DLFile F = new DLFile(HW);
        F.WriteFile(HW.getClassesFolder() + "\\HWDBDescription.java", HWDBDecription);
        return HW.getClassesFolder() + "\\HWDBDescription.java";
    }

    public DLClassesMaker(DLEnvironmentSetting HW) {
        this.HW = HW;
    }
}
