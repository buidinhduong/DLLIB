package DLDBM;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Duong89
 */
public class DLFormating {

    DLEnvironmentSetting HW;

    public DLFormating(DLEnvironmentSetting HW) {
        this.HW = HW;
    }

    public static String FormatSQLCommand(Object Value) {
        if (Value == null) {
            return "''";
        }
        String NumberType = "FloatIntegerIntDoubleLongShort";
        if (Value.getClass().getSimpleName().equalsIgnoreCase("Boolean")) {
            return Value.toString().equalsIgnoreCase("true") == true ? "1" : "0";
        }
        if (!NumberType.toLowerCase().contains(Value.getClass().getSimpleName().toLowerCase())) {
            if (Value.toString().contains("'")) {
                return "N'" + Value.toString().replace("'", "\"") + "'";
            }
            return "N'" + Value.toString() + "'";
        } else {
            return Value.toString();
        }

    }

    public static String FormatSQLCommand(Object Value, String objectType) {

        String NumberType = "FloatIntegerIntDoubleLongShort";
        if (Value == null && !NumberType.equalsIgnoreCase(objectType)) {
            return "''";
        }
        if (Value.getClass().getSimpleName().equalsIgnoreCase("Boolean")) {
            return Value.toString().equalsIgnoreCase("true") == true ? "1" : "0";
        }
        if (!NumberType.toLowerCase().contains(Value.getClass().getSimpleName().toLowerCase())) {
            if (Value.toString().contains("'")) {
                return "N'" + Value.toString().replace("'", "\"") + "'";
            }
            return "N'" + Value.toString() + "'";
        } else {
            return Value.toString();
        }

    }

    public static Object ConvertToJavaObjectType(Object StartO, String toType) throws Exception {
        if (StartO == null) {
            return StartO;
        }
        Object Ret = null;
        if ("boolean".contains(toType.toLowerCase())) {
            Ret = Boolean.valueOf(StartO.toString().equalsIgnoreCase("1") == true ? "true" : "false");
        }
        if ("date".contains(toType.toLowerCase())) {
            if (StartO.toString().toString().contains("-") && StartO.toString().contains(":")) {
                Ret = java.sql.Date.valueOf(StartO.toString().split(" ")[0]);
            }
        }
        if ("long".contains(toType.toLowerCase())) {
            Ret = Long.valueOf(StartO.toString());
        }

        if ("float".contains(toType.toLowerCase())) {
            Ret = Float.valueOf(StartO.toString());
        }
        if ("short".contains(toType.toLowerCase())) {
            Ret = Short.valueOf(StartO.toString());
        }
        if ("image".contains(toType.toLowerCase())) {
            //  throw new Exception("Do not support type: java.awt.Image");
        }
        if ("integerint".contains(toType.toLowerCase())) {
            Ret = Integer.valueOf(StartO.toString());
        }

        if (Ret == null) {
            Ret = StartO;
        }

        return Ret;

    }

    public static String MySQLtoJavaType(String Type) {
        if (Type.toLowerCase().contains("char")) {
            return "String";
        }
        if (Type.toLowerCase().contains("int")) {
            return "int";
        }
        if (Type.toLowerCase().contains("text")) {
            return "String";
        }
        if (Type.toLowerCase().contains("datetime")) {
            return "java.sql.Date";
        }
        if (Type.toLowerCase().contains("float")) {
            return "float";
        }
        if (Type.toLowerCase().contains("decimal")) {
            return "float";
        }
        return Type;
    }

    public String ConvertToJavaType(String tstrSqlType) {
        if (HW.getDatabaseType().equalsIgnoreCase("MySQL")) {
            return MySQLtoJavaType(tstrSqlType);
        }
        String _Type = "";
        // DBTYPE & JAvTPYE mus be same:lenght and Return tpye
        String[] DBTYPEs = {"bigint", "smallint", "tinyint", "int",
            "bit", "decimal", "numeric", "money", "smallmoney", "float",
            "real", "datetime", "char", "sql_variant", "varchar", "text",
            "nchar", "nvarchar", "ntext",
            "binary", "varbinary", "image", "timestamp", "smalldatetime", "uniqueidentifier"};
        String[] JavaTypes = {"long", "short", "byte", "int", "boolean",
            "java.math.BigDecimal", "float", "float", "float", "float", "java.sql.Date",
            "java.sql.Date", "String", "Object", "String", "String",
            "String", "String", "String", " byte[]",
            "byte[]", "java.awt.Image", "String", "java.sql.Date", "Object"};

        for (int i = 0; i < DBTYPEs.length; i++) {
            if (DBTYPEs[i].equalsIgnoreCase(tstrSqlType)) {
                _Type = JavaTypes[i];
                break;
            }
        }
        return _Type;
    }

    public static String ConvertToSQLType(String Type) {
        String _Type = "";
        // DBTYPE & JAvTPYE mus be same:lenght and Return tpye
        String[] DBTYPEs = {"bigint", "smallint", "tinyint", "int",
            "bit", "decimal", "numeric", "money", "smallmoney", "float",
            "real", "datetime", "char", "sql_variant", "varchar", "text",
            "nchar", "nvarchar", "ntext",
            "binary", "varbinary", "image", "timestamp", "smalldatetime", "uniqueidentifier"};
        String[] JavaTypes = {"long", "short", "byte", "int", "boolean",
            "java.math.BigDecimal", "float", "float", "float", "float", "java.sql.Date",
            "java.sql.Date", "String", "Object", "String", "String",
            "String", "String", "String", " byte[]",
            "byte[]", "java.awt.Image", "String", "java.sql.Date", "Object"};
        for (int i = 0; i < DBTYPEs.length; i++) {
            if (JavaTypes[i].equalsIgnoreCase(Type)) {
                _Type = DBTYPEs[i];
                break;
            }
        }
        return _Type;
    }
}

