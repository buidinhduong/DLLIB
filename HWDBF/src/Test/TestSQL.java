/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Test;

import DLDBM.DLEnvironmentSetting;
import DLDBM.DLSQL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author buiduong
 */
public class TestSQL {
    public static void main(String[] arg)
    {
        DLEnvironmentSetting e=new DLEnvironmentSetting();
        
        DLSQL hSQl=new DLSQL(e);
        Student a=new Student();
        try {
            System.out.print( hSQl.getInsertSQL(a));
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TestSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
