package DLDBM;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Duong89 this class helps you to check valid of data in database
 */
public class DLChecking {
    /*Class hw*/

    DLEnvironmentSetting HW;

    public DLChecking(DLEnvironmentSetting HW) {
        this.HW = HW;
    }

    public boolean CheckExist(Object ThisO, String WhereField, Object value) {

        try {
            Class aClass = ThisO.getClass();
            String sql = "Select count(*) as 'Count' from " + aClass.getSimpleName() + " where " + WhereField + " = " + DLFormating.FormatSQLCommand(value);
            System.out.println(sql);
            Statement Stm = HW.getMyConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet Rs = Stm.executeQuery(sql);
            Rs.absolute(1);
            if (Rs.getInt(1) > 0) {
                return true;
            }

            Rs.close();
        } catch (SQLException e) {
            new DLLogging(HW).WriteError(e.getMessage(), DLChecking.class.getName(), "CheckExist()");
        }
        return false;
    }

    public boolean CheckExist(String tableName, String WhereField, Object value) {

        try {

            String sql = "Select count(*) as 'Count' from " + tableName + " where " + WhereField + " = " + DLFormating.FormatSQLCommand(value);
            System.out.println(sql);
            Statement Stm = HW.getMyConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet Rs = Stm.executeQuery(sql);
            Rs.absolute(1);
            if (Rs.getInt(1) > 0) {
                return true;
            }

            Rs.close();
        } catch (SQLException e) {
            new DLLogging(HW).WriteError(e.getMessage(), DLChecking.class.getName(), "CheckExist()");
        }
        return false;
    }

    public boolean CheckExist(String tableName, String WhereField[], Object value[]) {

        try {

            String sql = "Select count(*) as 'Count' from " + tableName + " where ";//

            //+ WhereField + " = " + DLFormating.FormatSQLCommand(value);
            System.out.println(sql);
            Statement Stm = HW.getMyConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet Rs = Stm.executeQuery(sql);
            Rs.absolute(1);
            if (Rs.getInt(1) > 0) {
                return true;
            }

            Rs.close();
        } catch (SQLException e) {
            new DLLogging(HW).WriteError(e.getMessage(), DLChecking.class.getName(), "CheckExist()");
        }
        return false;
    }
}
