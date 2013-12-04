/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DLDBM;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author SONY
 */
public class DLDBManager {

    DLEnvironmentSetting HW;
    DLSQL HS;
    DLDatabase HWDB;

    public DLDBManager(DLEnvironmentSetting HW) {
        this.HW = HW;
        HS = new DLSQL(HW);
        HWDB = new DLDatabase(HW);
    }

    public boolean Insert(Object InsertObject) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception {
        Class classO = InsertObject.getClass();
        Method referToME = classO.getMethod("ReferTo",classO);
       String[] PrimaryKey = (String[]) classO.getMethod("PrimaryKey",classO).invoke(InsertObject, new Object[]{});

        String[] TableReferences = (String[]) referToME.invoke(InsertObject, new Object[]{});

        if (TableReferences.length == 0) {
            {
                HWDB.ExecuteSQL(HS.getInsertSQL(InsertObject, true), HW.getMyConnection());
                return true;
            }
        } else {
            boolean InsertOk = true;
            DLChecking check = new DLChecking(HW);
            for (String table : TableReferences) {

                String ThisTableCol = table.split("->")[0].split("\\.")[1];
                Object Value = classO.getMethod("get" + ThisTableCol, InsertObject.getClass()).invoke(InsertObject, new Object[]{});
                String[] ThisReferTable = table.split("->")[1].split("\\.");
                System.out.print("Check table:" + ThisReferTable[0] + " column:" + ThisReferTable[1] + " value=" + Value.toString());
                InsertOk = check.CheckExist(ThisReferTable[0], ThisReferTable[1], Value);
                System.out.print(InsertOk);
                if (InsertOk == false) {

                    throw new Exception("Invalid reference key in table " + ThisReferTable[0] + "." + ThisReferTable[1]);
                }
            }
            if (InsertOk) {
                HWDB.ExecuteSQL(HS.getInsertSQL(InsertObject, true), HW.getMyConnection());
                return true;

            }
        }
        return false;
    }
}
