package DLDBM;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Statement;
import java.sql.ResultSet;

/**
 *
 * @author Duong89
 */
public class DLKeyMaker  {
 DLEnvironmentSetting HW;
    public DLKeyMaker(DLEnvironmentSetting HW) {
        this.HW = HW;
    }

    public  int GetIntKey(String table, String KeyField) {
        try {
            String sql = "Select " + KeyField + " from " + table + " order by " + KeyField + " asc";
            Statement Stm =new  DLDatabase(new DLEnvironmentSetting()).getConnectionType1().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet Rs = Stm.executeQuery(sql);
            if (!Rs.next()) {
                return 0;
            }
            Rs.last();
            if (Rs.getInt(1) >= 0) {
                return Rs.getInt(1) + 1;
            }
            Rs.close();
        } catch (Exception e) {
         new DLLogging(HW).WriteError(e.getMessage(),DLKeyMaker.class.getName(),"GetIntKey");
         e.printStackTrace();
        }
        return 0;
    }
   
}

