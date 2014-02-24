package DLDBM;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Duong89
 */
public class DLSQL {

    DLEnvironmentSetting HW;

    public DLSQL(DLEnvironmentSetting HW) {
        this.HW = HW;
    }

    public String getInsertSQL(Object ThisO) throws IllegalAccessException {
        try {
            boolean OderColumn = true;
            Class aClass = ThisO.getClass();
            String sql = "Insert into  " + aClass.getSimpleName() + " values(";
            ArrayList GetMethods = new ArrayList();
            for (Method me : aClass.getDeclaredMethods()) {
                if (me.getName().toLowerCase().contains("get")) {
                    GetMethods.add(me);
                }
            }
            if (OderColumn) {
                Method methodOder = aClass.getMethod("OrderColumn");

                String[] Oder = (String[]) methodOder.invoke(ThisO, new Object[0]);
                ArrayList<Method> OderGetMethods = new ArrayList<Method>();
                for (String a : Oder) {
                    for (Object ThisME : GetMethods.toArray()) {
                        if (((Method) ThisME).getName().toLowerCase().equals("get" + a.toLowerCase())) {
                            OderGetMethods.add((Method) ThisME);
                        }
                    }
                }
                GetMethods = OderGetMethods;
            }
            //after sort by OrderColumn
            Object[] Mes = GetMethods.toArray();

            for (int i = 0; i < Mes.length; i++) {
                if (((Method) Mes[i]).getName().toLowerCase().contains("get")) {
                    if (i <= Mes.length - 2) {
                        Object result = ((Method) Mes[i]).invoke(ThisO, new Object[0]);
                        sql += DLFormating.FormatSQLCommand(result) + ",";

                    }
                    if (i == Mes.length - 1) {
                        Object result = ((Method) Mes[i]).invoke(ThisO, new Object[0]);
                        sql += DLFormating.FormatSQLCommand(result) + ")";
                    }
                }
            }
            return sql;

        } catch (IllegalAccessException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLSQL.class.getName(), "GetInsertCommand()");
            throw ex;
        } catch (InvocationTargetException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLSQL.class.getName(), "GetInsertCommand()");
        } catch (NoSuchMethodException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLSQL.class.getName(), "GetInsertCommand()");
        } catch (SecurityException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLSQL.class.getName(), "GetInsertCommand()");
        }
        return null;
    }

    public String getInsertSQL(Object ThisO, boolean IdentityColumn) throws IllegalAccessException {
        try {
            boolean OderColumn = true;
            String Iden[] =new String[]{};
            Class aClass = ThisO.getClass();
            String sql = "Insert into  " + aClass.getSimpleName() + " values(";
            ArrayList GetMethods = new ArrayList();
            for (Method me : aClass.getDeclaredMethods()) {
                if (me.getName().toLowerCase().contains("get")) {
                    GetMethods.add(me);
                }
            }
            if (OderColumn) {
                Method methodOder = aClass.getMethod("OrderColumn");
                String[] Oder = (String[]) methodOder.invoke(ThisO, new Object[0]);
                if (IdentityColumn) {
                    Method IdentityCol = aClass.getMethod("IdentityColumn");
                    Iden =  (String[]) IdentityCol.invoke(ThisO,new Object[0]);
                }
                ArrayList<Method> OderGetMethods = new ArrayList<Method>();
                for (String a : Oder) {
                    if (Iden.toString().toLowerCase().contains(a.toLowerCase())) {
                        continue;
                    }
                    for (Object ThisME : GetMethods.toArray()) {
                        if (((Method) ThisME).getName().toLowerCase().equals("get" + a.toLowerCase())) {
                            OderGetMethods.add((Method) ThisME);
                        }
                    }
                }
                GetMethods = OderGetMethods;
            }
            //after sort by OrderColumn
            Object[] Mes = GetMethods.toArray();

            for (int i = 0; i < Mes.length; i++) {
                if (((Method) Mes[i]).getName().toLowerCase().contains("get")) {
                    if (i <= Mes.length - 2) {
                        Object result = ((Method) Mes[i]).invoke(ThisO, new Object[0]);
                        sql += DLFormating.FormatSQLCommand(result) + ",";

                    }
                    if (i == Mes.length - 1) {
                        Object result = ((Method) Mes[i]).invoke(ThisO, new Object[0]);
                        sql += DLFormating.FormatSQLCommand(result) + ")";
                    }
                }
            }
            return sql;

        } catch (IllegalAccessException ex) {

            new DLLogging(HW).WriteError(ex.getMessage(), DLSQL.class.getName(), "GetInsertCommand()");
            throw ex;
        } catch (InvocationTargetException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLSQL.class.getName(), "GetInsertCommand()");
        } catch (NoSuchMethodException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLSQL.class.getName(), "GetInsertCommand()");
        } catch (SecurityException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLSQL.class.getName(), "GetInsertCommand()");
        }
        return "";
    }

    public String getUpdateSQL(Object ThisO, boolean OderColumn, String WhereField, Object WhereValue) {
        String sql = "Update ";
        String EndSQL = " Where " + WhereField + "=" + DLFormating.FormatSQLCommand(WhereValue);
        try {
            Class aClass = ThisO.getClass();

            sql += aClass.getSimpleName() + " set ";

            ArrayList GetMethods = new ArrayList();
            for (Method me : aClass.getDeclaredMethods()) {
                if (me.getName().toLowerCase().contains("get")) {
                    GetMethods.add(me);
                }
            }
            if (OderColumn) {
                Method methodOder = aClass.getMethod("OrderColumn");
                String[] Oder = (String[]) methodOder.invoke(ThisO, new Object[0]);
                ArrayList<Method> OderGetMethods = new ArrayList<Method>();
                for (String a : Oder) {
                    for (Object ThisME : GetMethods.toArray()) {
                        if (((Method) ThisME).getName().toLowerCase().equals("get" + a.toLowerCase())) {
                            OderGetMethods.add((Method) ThisME);
                        }
                    }
                }
                GetMethods = OderGetMethods;
            }
            Object[] Mes = GetMethods.toArray();

            for (int i = 0; i < Mes.length; i++) {
                if (((Method) Mes[i]).getName().toLowerCase().contains("get")) {
                    String Column = ((Method) Mes[i]).getName().substring(3);
                    String EndColoumn = ((Method) Mes[Mes.length - 1]).getName().substring(3);
                    if (i <= Mes.length - 2) {
                        if (WhereField.equalsIgnoreCase(Column)) {
                            continue;
                        }
                        String End2 = (i == Mes.length - 2 && WhereField.equalsIgnoreCase(EndColoumn)) ? "" : ",";
                        Object result = ((Method) Mes[i]).invoke(ThisO, new Object[0]);
                        sql += Column + "=" + DLFormating.FormatSQLCommand(result) + End2;

                    }
                    if (i == Mes.length - 1) {
                        if (WhereField.equalsIgnoreCase(Column)) {

                            sql += EndSQL;
                            break;
                        }
                        Object result = ((Method) Mes[i]).invoke(ThisO, new Object[0]);
                        sql += Column + "=" + DLFormating.FormatSQLCommand(result) + EndSQL;
                    }
                }
            }

        } catch (IllegalAccessException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLSQL.class.getName(), "GetUpdateCommand()");
        } catch (IllegalArgumentException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLSQL.class.getName(), "GetUpdateCommand()");
        } catch (InvocationTargetException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLSQL.class.getName(), "GetUpdateCommand()");
        } catch (NoSuchMethodException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLSQL.class.getName(), "GetUpdateCommand()");
        } catch (SecurityException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLSQL.class.getName(), "GetUpdateCommand()");
        }

        return sql;
    }

    public String getUpdateSQL(Object ThisO, boolean OderColumn, String WhereField[], Object WhereValue[]) {
        String sql = "Update ";
        String EndSQL = " Where ";
        for (int i = 0; i < WhereField.length; i++) {
            if (i < WhereField.length - 1) {
                EndSQL += WhereField[i] + "=" + DLFormating.FormatSQLCommand(WhereValue[i]) + " and ";
            } else {
                EndSQL += WhereField[i] + "=" + DLFormating.FormatSQLCommand(WhereValue[i]);
            }
        }
        try {
            Class aClass = ThisO.getClass();

            sql += aClass.getSimpleName() + " set ";

            ArrayList GetMethods = new ArrayList();
            for (Method me : aClass.getDeclaredMethods()) {
                if (me.getName().toLowerCase().contains("get")) {
                    GetMethods.add(me);
                }
            }
            //thi code to sort by oder column
            if (OderColumn) {
                Method methodOder = aClass.getMethod("OrderColumn");
                String[] Oder = (String[]) methodOder.invoke(ThisO, new Object[0]);
                ArrayList<Method> OderGetMethods = new ArrayList<Method>();
                for (String a : Oder) {
                    for (Object ThisME : GetMethods.toArray()) {
                        if (((Method) ThisME).getName().toLowerCase().equals("get" + a.toLowerCase())) {
                            OderGetMethods.add((Method) ThisME);
                        }
                    }
                }
                GetMethods = OderGetMethods;
            }
            // Sorted
            Object[] Mes = GetMethods.toArray();
            String WhCol = "";
            for (String W : WhereField) {
                WhCol += W;
            }
            String EndCol = WhereField[WhereField.length - 1];
            //  System.out.println("\n\t"+EndCol);
            for (int i = 0; i < Mes.length; i++) {
                if (((Method) Mes[i]).getName().toLowerCase().contains("get")) {
                    String Column = ((Method) Mes[i]).getName().substring(3);

                    if (i <= Mes.length - 2) {


                        //if wherecolumn contain this col, continue
                        if (WhCol.toLowerCase().contains(Column.toLowerCase())) {
                            continue;
                        }
                        //if end col == mes[end];//+"" not +",";
                        String End2 = (i == Mes.length - 2) && (WhCol.toLowerCase().contains(EndCol.toLowerCase())) ? "" : ",";
                        Object result = ((Method) Mes[i]).invoke(ThisO, new Object[0]);
                        sql += Column + "=" + DLFormating.FormatSQLCommand(result) + ",";

                    }
                    if (i == Mes.length - 1) {
                        if (EndCol.equalsIgnoreCase(Column)) {

                            sql += EndSQL;
                            break;
                        }
                        Object result = ((Method) Mes[i]).invoke(ThisO, new Object[0]);
                        sql += Column + "=" + DLFormating.FormatSQLCommand(result) + EndSQL;
                    }
                }
            }

        } catch (IllegalAccessException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLSQL.class.getName(), "GetUpdateCommand()");
        } catch (IllegalArgumentException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLSQL.class.getName(), "GetUpdateCommand()");
        } catch (InvocationTargetException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLSQL.class.getName(), "GetUpdateCommand()");
        } catch (NoSuchMethodException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLSQL.class.getName(), "GetUpdateCommand()");
        } catch (SecurityException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLSQL.class.getName(), "GetUpdateCommand()");
        }

        return sql;
    }
}
