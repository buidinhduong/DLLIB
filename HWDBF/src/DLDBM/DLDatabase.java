package DLDBM;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Duong89
 */
public class DLDatabase {

    DLEnvironmentSetting HW;
    private Object[] ObjectClasses;

    public DLDatabase(DLEnvironmentSetting HW) {
        this.HW = HW;
    }

    public Connection getConnectionType1() throws Exception {
        String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
        String url = "jdbc:odbc:" + HW.getDatabaseName();
        Class.forName(driver);
        return DriverManager.getConnection(url);
    }

    public Connection GetMySQLConnection(String host, String database, String user, String password)
            throws Exception {

        String url = "";
        try {
            Class.forName("org.gjt.mm.mysql.Driver");
            url = "jdbc:mysql://" + host + ":3306/" + database;
            Connection con = DriverManager.getConnection(url, user, password);
            //  System.out.println("Connection established to " + url + "...");
            return con;
        } catch (Exception e) {
            //  System.out.println("Connection couldn't be established to " + url);
            throw (e);
        }
    }

    public Connection getConnectionType1(String DBName, String username, String password) throws Exception {
        String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
        String url = "jdbc:odbc:" + DBName;
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public Object getObject(Object ClassObject, String sql, Connection MyConnection) {
        try {
            Class aClass = ClassObject.getClass();
            ArrayList<Method> SetMethods = new ArrayList<Method>();
            for (Method SM : aClass.getDeclaredMethods()) {
                if (SM.getName().substring(0, 3).equalsIgnoreCase("set")) {
                    SetMethods.add(SM);
                }
            }
            Method methodOder = aClass.getMethod("OrderColumn");
            String[] Oder = (String[]) methodOder.invoke(ClassObject, new Object[0]);
            ArrayList<Method> OderGetMethods = new ArrayList<Method>();
            for (String a : Oder) {
                for (Object ThisME : SetMethods.toArray()) {
                    if (((Method) ThisME).getName().toLowerCase().equals("set" + a.toLowerCase())) {
                        OderGetMethods.add((Method) ThisME);
                    }
                }
            }
            SetMethods = OderGetMethods;
            Object[] ListMethod = SetMethods.toArray();
            ResultSet rs = GetResultSet(sql, MyConnection);
            rs.beforeFirst();
            while (rs.next()) {
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {

                    Object O = rs.getObject(i);
                    if (O == null) {
                        continue;
                    }
                    Object result = ((Method) ListMethod[i - 1]).invoke(ClassObject, O);

                }
                break;
            }

            rs.close();
            return ClassObject;
        } catch (Exception er) {
            er.printStackTrace();
        }
        return ClassObject;
    }

    public Connection GetJDBCConnectionType4() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        conn = DriverManager.getConnection("jdbc:microsoft:sqlserver://" + HW.getServerName() + ";Database =" + HW.getDatabaseName() + ";", HW.getUsername(), HW.getPassword());
        return conn;
    }

    public boolean ExecuteSQL(String sql, Connection MyConnection) throws Exception {
        boolean Ok = true;
        Connection connection = MyConnection;
        Statement stmt = connection.createStatement();
        Ok = stmt.execute(sql);
        connection.close();
        return Ok;
    }

    public ResultSet GetResultSet(String SQL, Connection ThisConnection) throws Exception {
        try {

            Statement Stm = HW.getMyConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet Rs = Stm.executeQuery(SQL);
           
            return Rs;
        } catch (Exception ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLDatabase.class.getName(), "GetResultSet");
            throw ex;
        }
    }

    public boolean ExportTable(String TableName) throws IllegalAccessException, Exception {
        DLSQL SQL = new DLSQL(HW);
        ArrayList Lis = this.GetObjects(CloneObject(TableName), this.HW.getMyConnection());
        ArrayList<String> SQLs = new ArrayList<String>();
        for (Object A : Lis) {
            String Ex = SQL.getInsertSQL(A);
            SQLs.add(Ex);
        }
        return true;
    }

    public boolean ExportDB() throws IllegalAccessException, Exception {
        DLSQL SQL = new DLSQL(HW);
        DLFile Fsql = new DLFile(HW);
        int TableCount = 0;
        int totalrecord = 0;
        for (int i = 0; i < ObjectClasses.length; i++) {
            String TableName = ObjectClasses[i].getClass().getSimpleName();
            ArrayList Lis = GetObjects(CloneObject(TableName), this.HW.getMyConnection());

            ArrayList<String> SQLs = new ArrayList<String>();
            TableCount++;
            int record = 0;
            for (Object A : Lis) {
                String Ex = SQL.getInsertSQL(A);
                SQLs.add(Ex);
                Fsql.AppendStringToFile(HW.getBackupFile(), Ex);
                record++;
                totalrecord++;
            }
            Fsql.AppendStringToFile(HW.getBackupFile(), "------number of backup record in table: " + TableName + " :" + record + "-----");
        }
        Fsql.AppendStringToFile(HW.getBackupFile(), "------total of backup table in database   " + HW.getDatabaseName() + " :" + TableCount);
        Fsql.AppendStringToFile(HW.getBackupFile(), "------total of backup record in database " + HW.getDatabaseName() + ":" + totalrecord);
        return true;
    }

    public ArrayList<Object> GetObjects(Object ClassObject, Connection MyConnection) throws Exception {
        ArrayList ListAll = new ArrayList();
        try {

            Class aClass = ClassObject.getClass();
            String SQL = "Select * from " + aClass.getSimpleName();
            ArrayList<Method> SetMethods = new ArrayList<Method>();
            for (Method SM : aClass.getDeclaredMethods()) {
                if (SM.getName().substring(0, 3).equalsIgnoreCase("set")) {
                    SetMethods.add(SM);
                }
            }
            Method methodOder = aClass.getMethod("OrderColumn");
            String[] Oder = (String[]) methodOder.invoke(ClassObject, new Object[0]);
            ArrayList<Method> OderGetMethods = new ArrayList<Method>();
            for (String a : Oder) {
                for (Object ThisME : SetMethods.toArray()) {
                    if (((Method) ThisME).getName().toLowerCase().equals("set" + a.toLowerCase())) {
                        OderGetMethods.add((Method) ThisME);
                    }
                }
            }
            SetMethods = OderGetMethods;
            Object[] ListMethod = SetMethods.toArray();
            ResultSet rs = GetResultSet(SQL, MyConnection);
            rs.beforeFirst();
            while (rs.next()) {
                Object NewObject = CloneObject(ClassObject.getClass().getSimpleName());

                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    Object O = DLFormating.ConvertToJavaObjectType(rs.getString(i), aClass.getMethod("get" + rs.getMetaData().getColumnName(i)).getReturnType().getSimpleName());
                    if (O == null) {
                        continue;
                    }
                    Object result = ((Method) ListMethod[i - 1]).invoke(NewObject, O);
                }
                ListAll.add(NewObject);
            }
            rs.close();
            return ListAll;
        } catch (Exception er) {
            //System.out.println("Erorr Message:" + ClassObject.getClass().getSimpleName());
            er.printStackTrace();
            new DLLogging(HW).WriteError(er.getMessage(), DLDatabase.class.getName(), "GetObjects()");
            throw er;
        }

    }

    public int ExecuteUpdateSQL(String sql) throws Exception {
        int Ok = -1;

        Connection connection = getConnectionType1();
        Statement stmt = connection.createStatement();
        Ok = stmt.executeUpdate(sql);
        connection.close();

        return Ok;
    }
    //phuong thuc nay fai duoc viet lai neu muon su dung GetObjects, liet ke tat ca class duoc su dung 

    protected Object CloneObject(String ClassName) {
        for (int i = 0; i < ObjectClasses.length; i++) {
            // System.out.print("Type:"+ClassObject.getClass().getSimpleName());
            if (ObjectClasses[i].getClass().getSimpleName().equalsIgnoreCase(ClassName)) {
                try {
                    //  System.out.print("Found:"+ObjectClasses[i].getClass().getSimpleName());
                    Class aClass = getObjectClasses()[i].getClass();
                    Method methodClone = aClass.getMethod("cloneObject");
                    return methodClone.invoke(ObjectClasses[i],new Object(){});
                } catch (IllegalAccessException ex) {
                    new DLLogging(HW).WriteError(ex.getMessage(), DLDatabase.class.getName(), "CloneObjects()");
                } catch (IllegalArgumentException ex) {
                    new DLLogging(HW).WriteError(ex.getMessage(), DLDatabase.class.getName(), "CloneObjects()");
                } catch (InvocationTargetException ex) {
                    new DLLogging(HW).WriteError(ex.getMessage(), DLDatabase.class.getName(), "CloneObjects()");
                } catch (NoSuchMethodException ex) {
                    new DLLogging(HW).WriteError(ex.getMessage(), DLDatabase.class.getName(), "CloneObjects()");
                } catch (SecurityException ex) {
                    new DLLogging(HW).WriteError(ex.getMessage(), DLDatabase.class.getName(), "CloneObjects()");
                }

            }
        }
        return null;
    }

    /**
     * @return the ObjectClasses
     */
    public Object[] getObjectClasses() {
        return ObjectClasses;
    }

    /**
     * @param ObjectClasses the ObjectClasses to set
     */
    public void setObjectClasses(Object[] ObjectClasses) {
        this.ObjectClasses = ObjectClasses;
    }
}
